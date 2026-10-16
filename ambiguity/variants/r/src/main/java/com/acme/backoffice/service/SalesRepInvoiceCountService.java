package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.SalesRepInvoiceCount;

import java.time.LocalDate;
import java.util.List;

/** The number of invoices issued between two dates to the customers of each sales rep, highest first. */
public class SalesRepInvoiceCountService {
    private final DataStore store;

    public SalesRepInvoiceCountService(DataStore store) {
        this.store = store;
    }

    public List<SalesRepInvoiceCount> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.invoices().countPerSalesRep(from, to);
    }
}
