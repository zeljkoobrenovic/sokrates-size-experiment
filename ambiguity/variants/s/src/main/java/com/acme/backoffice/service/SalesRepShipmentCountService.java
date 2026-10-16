package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.SalesRepShipmentCount;

import java.time.LocalDate;
import java.util.List;

/** The number of shipments between two dates for the customers of each sales rep, highest first. */
public class SalesRepShipmentCountService {
    private final DataStore store;

    public SalesRepShipmentCountService(DataStore store) {
        this.store = store;
    }

    public List<SalesRepShipmentCount> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.findShipmentCountPerSalesRep(from, to);
    }
}
