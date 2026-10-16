package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.QuarterInvoices;

import java.util.List;

/** The number of invoices issued per quarter of a year, overall and per region. */
public class QuarterInvoicesService {
    private final DataStore store;

    public QuarterInvoicesService(DataStore store) {
        this.store = store;
    }

    public List<QuarterInvoices> report(int year) {
        if (year < 1) {
            throw new IllegalArgumentException("Year must be positive");
        }
        return store.findInvoicesPerQuarter(year);
    }
}
