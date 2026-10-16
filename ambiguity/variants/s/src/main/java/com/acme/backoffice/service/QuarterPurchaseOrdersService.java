package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.QuarterPurchaseOrders;

import java.util.List;

/** The number of purchase orders placed per quarter of a year, overall and per region of the receiving warehouse. */
public class QuarterPurchaseOrdersService {
    private final DataStore store;

    public QuarterPurchaseOrdersService(DataStore store) {
        this.store = store;
    }

    public List<QuarterPurchaseOrders> report(int year) {
        if (year < 1) {
            throw new IllegalArgumentException("Year must be positive");
        }
        return store.findPurchaseOrdersPerQuarter(year);
    }
}
