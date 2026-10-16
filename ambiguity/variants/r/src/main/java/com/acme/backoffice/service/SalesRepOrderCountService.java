package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.SalesRepOrderCount;

import java.time.LocalDate;
import java.util.List;

/** The number of orders placed between two dates by the customers of each sales rep, highest first. */
public class SalesRepOrderCountService {
    private final DataStore store;

    public SalesRepOrderCountService(DataStore store) {
        this.store = store;
    }

    public List<SalesRepOrderCount> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.orders().countPerSalesRep(from, to);
    }
}
