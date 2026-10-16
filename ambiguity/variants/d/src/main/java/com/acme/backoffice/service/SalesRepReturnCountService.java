package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.SalesRepReturnCount;

import java.time.LocalDate;
import java.util.List;

/** The number of returns between two dates for the customers of each sales rep, highest first. */
public class SalesRepReturnCountService {
    private final DataStore store;

    public SalesRepReturnCountService(DataStore store) {
        this.store = store;
    }

    public List<SalesRepReturnCount> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.findSalesReturnCountPerSalesRep(from, to);
    }
}
