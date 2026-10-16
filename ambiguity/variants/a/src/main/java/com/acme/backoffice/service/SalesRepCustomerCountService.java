package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.SalesRepCustomerCount;

import java.time.LocalDate;
import java.util.List;

/** The number of customers created between two dates per sales rep, highest first. */
public class SalesRepCustomerCountService {
    private final DataStore store;

    public SalesRepCustomerCountService(DataStore store) {
        this.store = store;
    }

    public List<SalesRepCustomerCount> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.findCustomerCountPerSalesRep(from, to);
    }
}
