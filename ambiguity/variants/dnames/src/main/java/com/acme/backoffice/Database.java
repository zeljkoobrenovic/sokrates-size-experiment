package com.acme.backoffice;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

/** Opens a fresh in-memory H2 database with the schema from schema.sql. */
public class Database {
    private static final AtomicInteger COUNTER = new AtomicInteger();

    public static Connection openInMemory() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:mem:backoffice" + COUNTER.incrementAndGet() + ";DB_CLOSE_DELAY=-1");
            try (InputStream in = Database.class.getResourceAsStream("/schema.sql"); Statement statement = connection.createStatement()) {
                String schema = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                for (String sql : schema.split(";")) {
                    String trimmed = sql.replaceAll("(?m)^\\s*--.*$", "").trim();
                    if (!trimmed.isEmpty()) {
                        statement.execute(trimmed);
                    }
                }
            }
            return connection;
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Cannot open the database", e);
        }
    }
}
