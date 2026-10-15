package com.acme.backoffice;

import java.sql.Connection;

/** All database access of BackOffice. */
public class DataStore {
    private final Connection connection;

    public DataStore(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
