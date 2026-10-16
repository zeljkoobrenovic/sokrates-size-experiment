package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Shipment;

import java.util.List;

/** The most recent shipments: overall, per region and per customer. */
public class LatestShipmentsService {
    private final DataStore store;

    public LatestShipmentsService(DataStore store) {
        this.store = store;
    }

    public List<Shipment> overall(int count) {
        checkCount(count);
        return store.shipments().listLatest(count);
    }

    public List<Shipment> forRegion(Region region, int count) {
        checkCount(count);
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        return store.shipments().listLatestInRegion(region, count);
    }

    public List<Shipment> forCustomer(long customerId, int count) {
        checkCount(count);
        if (store.customers().findById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer " + customerId);
        }
        return store.shipments().listLatestOfCustomer(customerId, count);
    }

    private void checkCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be positive");
        }
    }
}
