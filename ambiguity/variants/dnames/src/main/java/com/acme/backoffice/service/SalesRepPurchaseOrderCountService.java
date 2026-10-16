package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.SalesRepPurchaseOrderCount;

import java.time.LocalDate;
import java.util.List;

/** The number of purchase orders between two dates for the customers of each sales rep, highest first. */
public class SalesRepPurchaseOrderCountService {
    private final DataStore store;

    public SalesRepPurchaseOrderCountService(DataStore store) {
        this.store = store;
    }

    public List<SalesRepPurchaseOrderCount> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.countPurchaseOrdersIntoSalesRepRegionsPerSalesRep(from, to);
    }
}
