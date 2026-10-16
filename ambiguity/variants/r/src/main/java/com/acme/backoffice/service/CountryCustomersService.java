package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CountryCustomerCount;
import com.acme.backoffice.model.Customer;

import java.time.LocalDate;
import java.util.List;

/** The customers of a country created between two dates, and the number of customers created per country. */
public class CountryCustomersService {
    private final DataStore store;

    public CountryCustomersService(DataStore store) {
        this.store = store;
    }

    public List<Customer> customers(String country, LocalDate from, LocalDate to) {
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country is required");
        }
        checkRange(from, to);
        return store.customers().listCreatedInCountry(country, from, to);
    }

    public List<CountryCustomerCount> countPerCountry(LocalDate from, LocalDate to) {
        checkRange(from, to);
        return store.customers().countPerCountry(from, to);
    }

    private void checkRange(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
    }
}
