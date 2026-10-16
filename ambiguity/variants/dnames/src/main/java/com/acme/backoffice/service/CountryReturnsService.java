package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CountryReturnCount;
import com.acme.backoffice.model.SalesReturn;

import java.time.LocalDate;
import java.util.List;

/** The returns of the customers of a country dated between two dates, and the number of returns per country. */
public class CountryReturnsService {
    private final DataStore store;

    public CountryReturnsService(DataStore store) {
        this.store = store;
    }

    public List<SalesReturn> returns(String country, LocalDate from, LocalDate to) {
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country is required");
        }
        checkRange(from, to);
        return store.listSalesReturnsInCountryDatedBetween(country, from, to);
    }

    public List<CountryReturnCount> countPerCountry(LocalDate from, LocalDate to) {
        checkRange(from, to);
        return store.countSalesReturnsPerCountry(from, to);
    }

    private void checkRange(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
    }
}
