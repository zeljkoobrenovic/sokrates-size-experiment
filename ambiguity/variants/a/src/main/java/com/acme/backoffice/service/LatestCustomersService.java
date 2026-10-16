package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.Region;

import java.util.List;

/** The most recently created customers: overall, per region and ahead of a given customer. */
public class LatestCustomersService {
    private final DataStore store;

    public LatestCustomersService(DataStore store) {
        this.store = store;
    }

    public List<Customer> overall(int count) {
        checkCount(count);
        return store.findLatestCustomers(count);
    }

    public List<Customer> forRegion(Region region, int count) {
        checkCount(count);
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        return store.findLatestCustomersByRegion(region, count);
    }

    public List<Customer> beforeCustomer(long customerId, int count) {
        checkCount(count);
        if (store.findCustomerById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer " + customerId);
        }
        return store.findLatestCustomersBefore(customerId, count);
    }

    private void checkCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be positive");
        }
    }
}
