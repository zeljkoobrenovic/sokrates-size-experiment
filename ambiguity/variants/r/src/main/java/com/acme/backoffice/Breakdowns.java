package com.acme.backoffice;

import com.acme.backoffice.model.Region;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The count breakdowns that every entity has in the same shape: per quarter of a year and per weekday of a period (in
 * total and per region), per sales rep and per country. Each entity describes what it counts with a {@link Counted};
 * the store keeps the public method and its result type.
 */
final class Breakdowns {
    private Breakdowns() {
    }

    /**
     * What a breakdown counts.
     *
     * @param totalFrom   FROM and JOIN clauses for the total count, e.g. {@code "FROM invoice i JOIN customer c ON c.id = i.customer_id"}
     * @param regionFrom  FROM and JOIN clauses for the counts per region (may join more than {@code totalFrom})
     * @param dateColumn  the qualified date column the period applies to, e.g. {@code "i.invoice_date"}
     * @param condition   further conditions, starting with {@code " AND "}, or empty
     * @param regionColumn the qualified region column, e.g. {@code "c.region"}
     */
    record Counted(String totalFrom, String regionFrom, String dateColumn, String condition, String regionColumn) {
        Counted(String from, String dateColumn, String condition, String regionColumn) {
            this(from, from, dateColumn, condition, regionColumn);
        }
    }

    @FunctionalInterface
    interface QuarterRow<T> {
        T of(int quarter, int total, Map<Region, Integer> perRegion);
    }

    @FunctionalInterface
    interface WeekdayRow<T> {
        T of(DayOfWeek weekday, int total, Map<Region, Integer> perRegion);
    }

    @FunctionalInterface
    interface SalesRepRow<T> {
        T of(long salesRepId, String name, int count);
    }

    @FunctionalInterface
    interface CountryRow<T> {
        T of(String country, int count);
    }

    /** For each of the four quarters of the year, in order: the count in total and per region, zeros included. */
    static <T> List<T> perQuarter(Jdbc jdbc, String error, Counted counted, int year, QuarterRow<T> row) {
        String period = "YEAR(" + counted.dateColumn() + ") = ? AND QUARTER(" + counted.dateColumn() + ") = q.x";
        String sql = "SELECT q.x AS quarter_no, " + regionCounts(counted, period) + " FROM SYSTEM_RANGE(1, 4) q ORDER BY q.x";
        return jdbc.list(error, sql, rs -> row.of(rs.getInt("quarter_no"), rs.getInt("total_count"), perRegion(rs)),
                year, year, year, year, year);
    }

    /** For each day of the week, Monday to Sunday: the count between the two dates, inclusive, in total and per region. */
    static <T> List<T> perWeekday(Jdbc jdbc, String error, Counted counted, LocalDate from, LocalDate to, WeekdayRow<T> row) {
        String period = counted.dateColumn() + " >= ? AND " + counted.dateColumn() + " <= ? AND ISO_DAY_OF_WEEK("
                + counted.dateColumn() + ") = d.x";
        String sql = "SELECT d.x AS day_no, " + regionCounts(counted, period) + " FROM SYSTEM_RANGE(1, 7) d ORDER BY d.x";
        return jdbc.list(error, sql,
                rs -> row.of(DayOfWeek.of(rs.getInt("day_no")), rs.getInt("total_count"), perRegion(rs)),
                from, to, from, to, from, to, from, to, from, to);
    }

    /**
     * For each sales rep, the count between the two dates, inclusive, highest first, then by name and id; reps with
     * nothing are included with zero.
     *
     * @param countSubquery a {@code SELECT COUNT(*) ...} correlated on {@code r.id} with two date parameters (from, to)
     */
    static <T> List<T> perSalesRep(Jdbc jdbc, String error, String countSubquery, LocalDate from, LocalDate to,
                                   SalesRepRow<T> row) {
        String sql = "SELECT r.id AS id, r.name AS name, (" + countSubquery + ") AS item_count "
                + "FROM sales_rep r ORDER BY item_count DESC, r.name, r.id";
        return jdbc.list(error, sql, rs -> row.of(rs.getLong("id"), rs.getString("name"), rs.getInt("item_count")), from, to);
    }

    /**
     * The count per country of the customer between the two dates, inclusive, highest first, then by country;
     * countries with nothing are left out.
     *
     * @param fromWhere {@code FROM ... WHERE ...} with the customer joined as {@code c} and two date parameters (from, to)
     */
    static <T> List<T> perCountry(Jdbc jdbc, String error, String fromWhere, LocalDate from, LocalDate to, CountryRow<T> row) {
        String sql = "SELECT c.country AS country, COUNT(*) AS item_count " + fromWhere
                + " GROUP BY c.country ORDER BY item_count DESC, c.country";
        return jdbc.list(error, sql, rs -> row.of(rs.getString("country"), rs.getInt("item_count")), from, to);
    }

    private static String regionCounts(Counted counted, String period) {
        StringBuilder sql = new StringBuilder("(SELECT COUNT(*) " + counted.totalFrom() + " WHERE " + period
                + counted.condition() + ") AS total_count");
        for (Region region : List.of(Region.NORTH, Region.SOUTH, Region.EAST, Region.WEST)) {
            sql.append(", (SELECT COUNT(*) ").append(counted.regionFrom()).append(" WHERE ").append(period)
                    .append(counted.condition()).append(" AND ").append(counted.regionColumn()).append(" = '")
                    .append(region.name()).append("') AS ").append(region.name().toLowerCase(Locale.ROOT)).append("_count");
        }
        return sql.toString();
    }

    private static Map<Region, Integer> perRegion(java.sql.ResultSet rs) throws SQLException {
        Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
        perRegion.put(Region.NORTH, rs.getInt("north_count"));
        perRegion.put(Region.SOUTH, rs.getInt("south_count"));
        perRegion.put(Region.EAST, rs.getInt("east_count"));
        perRegion.put(Region.WEST, rs.getInt("west_count"));
        return perRegion;
    }
}
