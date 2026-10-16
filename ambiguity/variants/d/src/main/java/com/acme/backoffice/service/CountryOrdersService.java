package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CountryOrderCount;
import com.acme.backoffice.model.SalesOrder;

import java.time.LocalDate;
import java.util.List;

/** The orders of the customers of a country placed between two dates, and the number of orders per country. */
public class CountryOrdersService {
    private final DataStore store;

    public CountryOrdersService(DataStore store) {
        this.store = store;
    }

    public List<SalesOrder> orders(String country, LocalDate from, LocalDate to) {
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country is required");
        }
        checkRange(from, to);
        return store.findNotCancelledSalesOrdersInCountry(country, from, to);
    }

    public List<CountryOrderCount> countPerCountry(LocalDate from, LocalDate to) {
        checkRange(from, to);
        return store.findSalesOrderCountPerCountry(from, to);
    }

    private void checkRange(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
    }
}
