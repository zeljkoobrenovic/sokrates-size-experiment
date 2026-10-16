package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesReturn;

import java.util.List;

/** The most recent sales returns: overall, per region and per customer. */
public class LatestReturnsService {
    private final DataStore store;

    public LatestReturnsService(DataStore store) {
        this.store = store;
    }

    public List<SalesReturn> overall(int count) {
        checkCount(count);
        return store.returns().listLatest(count);
    }

    public List<SalesReturn> forRegion(Region region, int count) {
        checkCount(count);
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        return store.returns().listLatestInRegion(region, count);
    }

    public List<SalesReturn> forCustomer(long customerId, int count) {
        checkCount(count);
        if (store.customers().findById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer " + customerId);
        }
        return store.returns().listLatestOfCustomer(customerId, count);
    }

    private void checkCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be positive");
        }
    }
}
