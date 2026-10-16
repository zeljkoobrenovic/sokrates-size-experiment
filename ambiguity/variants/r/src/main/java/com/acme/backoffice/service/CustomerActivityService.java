package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CustomerActivity;

/** Overview of what a customer has ordered, returned, paid and been credited. */
public class CustomerActivityService {
    private final DataStore store;

    public CustomerActivityService(DataStore store) {
        this.store = store;
    }

    public CustomerActivity activity(long customerId) {
        store.customers().findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown customer: " + customerId));
        return store.customers().findActivity(customerId);
    }
}
