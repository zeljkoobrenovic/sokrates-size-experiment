package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Customer;

import java.time.LocalDate;
import java.util.List;

/** Closes and reopens customers; closed customers are hidden but their orders and invoices remain. */
public class CustomerClosureService {
    private final DataStore store;

    public CustomerClosureService(DataStore store) {
        this.store = store;
    }

    /** Closes the customer; returns false if it was already closed. */
    public boolean close(long customerId, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (store.customers().findById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer " + customerId);
        }
        return store.customers().close(customerId, date);
    }

    /** Reopens the customer; returns false if it was not closed. */
    public boolean reopen(long customerId) {
        return store.customers().reopen(customerId);
    }

    public List<Customer> listClosed() {
        return store.customers().listClosed();
    }
}
