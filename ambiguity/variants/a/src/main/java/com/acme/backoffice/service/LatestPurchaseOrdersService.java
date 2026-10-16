package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.Region;

import java.util.List;

/** The most recent purchase orders: overall, per region (of the delivery warehouse) and per supplier. */
public class LatestPurchaseOrdersService {
    private final DataStore store;

    public LatestPurchaseOrdersService(DataStore store) {
        this.store = store;
    }

    public List<PurchaseOrder> overall(int count) {
        checkCount(count);
        return store.findLatestPurchaseOrders(count);
    }

    public List<PurchaseOrder> forRegion(Region region, int count) {
        checkCount(count);
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        return store.findLatestPurchaseOrdersByRegion(region, count);
    }

    public List<PurchaseOrder> forSupplier(long supplierId, int count) {
        checkCount(count);
        if (store.findSupplierById(supplierId).isEmpty()) {
            throw new IllegalArgumentException("Unknown supplier " + supplierId);
        }
        return store.findLatestPurchaseOrdersBySupplier(supplierId, count);
    }

    private void checkCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be positive");
        }
    }
}
