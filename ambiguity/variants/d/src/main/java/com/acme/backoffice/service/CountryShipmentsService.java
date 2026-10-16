package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CountryShipmentCount;
import com.acme.backoffice.model.Shipment;

import java.time.LocalDate;
import java.util.List;

/** The shipments for the customers of a country shipped between two dates, and the number of shipments per country. */
public class CountryShipmentsService {
    private final DataStore store;

    public CountryShipmentsService(DataStore store) {
        this.store = store;
    }

    public List<Shipment> shipments(String country, LocalDate from, LocalDate to) {
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country is required");
        }
        checkRange(from, to);
        return store.findShipmentsInCountryShippedBetween(country, from, to);
    }

    public List<CountryShipmentCount> countPerCountry(LocalDate from, LocalDate to) {
        checkRange(from, to);
        return store.findShipmentCountPerCountry(from, to);
    }

    private void checkRange(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
    }
}
