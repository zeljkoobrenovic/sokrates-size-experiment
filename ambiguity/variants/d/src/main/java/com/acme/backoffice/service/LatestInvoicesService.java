package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.Region;

import java.util.List;

/** The most recent invoices: overall, per region and per customer. */
public class LatestInvoicesService {
    private final DataStore store;

    public LatestInvoicesService(DataStore store) {
        this.store = store;
    }

    public List<Invoice> overall(int count) {
        checkCount(count);
        return store.findLatestInvoices(count);
    }

    public List<Invoice> forRegion(Region region, int count) {
        checkCount(count);
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        return store.findLatestInvoicesInRegion(region, count);
    }

    public List<Invoice> forCustomer(long customerId, int count) {
        checkCount(count);
        if (store.findCustomerById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer " + customerId);
        }
        return store.findLatestInvoicesOfCustomer(customerId, count);
    }

    private void checkCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be positive");
        }
    }
}
