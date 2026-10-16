package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.QuarterCustomers;

import java.util.List;

/** The number of customers created per quarter of a year, overall and per region. */
public class QuarterCustomersService {
    private final DataStore store;

    public QuarterCustomersService(DataStore store) {
        this.store = store;
    }

    public List<QuarterCustomers> report(int year) {
        if (year < 1) {
            throw new IllegalArgumentException("Year must be positive");
        }
        return store.customers().countPerQuarter(year);
    }
}
