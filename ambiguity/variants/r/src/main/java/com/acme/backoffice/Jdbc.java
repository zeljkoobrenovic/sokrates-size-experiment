package com.acme.backoffice;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The JDBC plumbing shared by all stores: binds parameters, runs statements, maps rows and turns an
 * {@link SQLException} into an {@link IllegalStateException} with the given message.
 *
 * <p>Parameters are bound in order with {@code setObject}; an enum is bound by its name and null binds SQL NULL.
 *
 * <p>The {@code ...Raw} variants throw the {@link SQLException} instead; they are for use inside
 * {@link #inTransaction}, where a failure must roll the whole unit of work back.
 */
final class Jdbc {
    /** Maps the current row of a result set. */
    @FunctionalInterface
    interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    /** A unit of work run inside a transaction. */
    @FunctionalInterface
    interface Work<T> {
        T run() throws SQLException;
    }

    private final Connection connection;

    Jdbc(Connection connection) {
        this.connection = connection;
    }

    Connection connection() {
        return connection;
    }

    /** All rows of the query, mapped. */
    <T> List<T> list(String error, String sql, RowMapper<T> mapper, Object... params) {
        try {
            return listRaw(sql, mapper, params);
        } catch (SQLException e) {
            throw new IllegalStateException(error, e);
        }
    }

    /** The first row of the query, mapped, or empty if there is none. */
    <T> Optional<T> first(String error, String sql, RowMapper<T> mapper, Object... params) {
        try {
            return firstRaw(sql, mapper, params);
        } catch (SQLException e) {
            throw new IllegalStateException(error, e);
        }
    }

    /** The first row of a query that always returns a row (an aggregate), mapped. */
    <T> T single(String error, String sql, RowMapper<T> mapper, Object... params) {
        try {
            return singleRaw(sql, mapper, params);
        } catch (SQLException e) {
            throw new IllegalStateException(error, e);
        }
    }

    /** The int in the first column of an aggregate query. */
    int queryInt(String error, String sql, Object... params) {
        return single(error, sql, rs -> rs.getInt(1), params);
    }

    /** The decimal in the first column of an aggregate query; zero when it is SQL NULL. */
    BigDecimal queryAmount(String error, String sql, Object... params) {
        return single(error, sql, rs -> zeroIfNull(rs.getBigDecimal(1)), params);
    }

    /** Runs an INSERT, UPDATE or DELETE; the number of rows changed. */
    int update(String error, String sql, Object... params) {
        try {
            return updateRaw(sql, params);
        } catch (SQLException e) {
            throw new IllegalStateException(error, e);
        }
    }

    /** Runs an INSERT; the generated id of the new row. */
    long insert(String error, String sql, Object... params) {
        try {
            return insertRaw(sql, params);
        } catch (SQLException e) {
            throw new IllegalStateException(error, e);
        }
    }

    /**
     * Runs the work in one transaction: commits when it completes, rolls back when it throws an {@link SQLException}
     * (reported as an {@link IllegalStateException} with the given message), and restores auto-commit afterwards.
     */
    <T> T inTransaction(String error, Work<T> work) {
        try {
            connection.setAutoCommit(false);
            T result = work.run();
            connection.commit();
            return result;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException(error, e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    <T> List<T> listRaw(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
        try (PreparedStatement statement = prepare(sql, params); ResultSet rs = statement.executeQuery()) {
            List<T> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(mapper.map(rs));
            }
            return rows;
        }
    }

    <T> Optional<T> firstRaw(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
        try (PreparedStatement statement = prepare(sql, params); ResultSet rs = statement.executeQuery()) {
            return rs.next() ? Optional.of(mapper.map(rs)) : Optional.empty();
        }
    }

    <T> T singleRaw(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
        try (PreparedStatement statement = prepare(sql, params); ResultSet rs = statement.executeQuery()) {
            rs.next();
            return mapper.map(rs);
        }
    }

    int updateRaw(String sql, Object... params) throws SQLException {
        try (PreparedStatement statement = prepare(sql, params)) {
            return statement.executeUpdate();
        }
    }

    long insertRaw(String sql, Object... params) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(statement, params);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return keys.getLong(1);
            }
        }
    }

    static BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private PreparedStatement prepare(String sql, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            bind(statement, params);
            return statement;
        } catch (SQLException e) {
            statement.close();
            throw e;
        }
    }

    private static void bind(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            statement.setObject(i + 1, param instanceof Enum<?> e ? e.name() : param);
        }
    }
}
