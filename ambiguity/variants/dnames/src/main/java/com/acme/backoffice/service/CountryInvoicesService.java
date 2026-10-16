package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CountryInvoiceCount;
import com.acme.backoffice.model.Invoice;

import java.time.LocalDate;
import java.util.List;

/** The invoices of the customers of a country issued between two dates, and the number of invoices per country. */
public class CountryInvoicesService {
    private final DataStore store;

    public CountryInvoicesService(DataStore store) {
        this.store = store;
    }

    public List<Invoice> invoices(String country, LocalDate from, LocalDate to) {
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country is required");
        }
        checkRange(from, to);
        return store.listNonCancelledInvoicesInCountryDatedBetween(country, from, to);
    }

    public List<CountryInvoiceCount> countPerCountry(LocalDate from, LocalDate to) {
        checkRange(from, to);
        return store.countNonCancelledInvoicesPerCountry(from, to);
    }

    private void checkRange(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
    }
}
