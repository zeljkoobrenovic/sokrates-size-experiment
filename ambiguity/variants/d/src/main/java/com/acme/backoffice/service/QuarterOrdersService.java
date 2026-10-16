package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.QuarterOrders;

import java.util.List;

/** The number of orders placed per quarter of a year, overall and per region. */
public class QuarterOrdersService {
    private final DataStore store;

    public QuarterOrdersService(DataStore store) {
        this.store = store;
    }

    public List<QuarterOrders> report(int year) {
        if (year < 1) {
            throw new IllegalArgumentException("Year must be positive");
        }
        return store.findSalesOrderCountPerQuarter(year);
    }
}
