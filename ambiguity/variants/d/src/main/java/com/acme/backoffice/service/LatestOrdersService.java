package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;

import java.util.List;

/** The most recently placed orders: overall, per region and per customer. */
public class LatestOrdersService {
    private final DataStore store;

    public LatestOrdersService(DataStore store) {
        this.store = store;
    }

    public List<SalesOrder> overall(int count) {
        checkCount(count);
        return store.findLatestSalesOrders(count);
    }

    public List<SalesOrder> forRegion(Region region, int count) {
        checkCount(count);
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        return store.findLatestSalesOrdersInRegion(region, count);
    }

    public List<SalesOrder> forCustomer(long customerId, int count) {
        checkCount(count);
        if (store.findCustomerById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer " + customerId);
        }
        return store.findLatestSalesOrdersOfCustomer(customerId, count);
    }

    private void checkCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be positive");
        }
    }
}
