package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.QuarterReturns;

import java.util.List;

/** The number of sales returns per quarter of a year, overall and per region. */
public class QuarterReturnsService {
    private final DataStore store;

    public QuarterReturnsService(DataStore store) {
        this.store = store;
    }

    public List<QuarterReturns> report(int year) {
        if (year < 1) {
            throw new IllegalArgumentException("Year must be positive");
        }
        return store.countSalesReturnsPerQuarterAndRegion(year);
    }
}
