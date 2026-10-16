package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.QuarterShipments;

import java.util.List;

/** The number of shipments sent per quarter of a year, overall and per region. */
public class QuarterShipmentsService {
    private final DataStore store;

    public QuarterShipmentsService(DataStore store) {
        this.store = store;
    }

    public List<QuarterShipments> report(int year) {
        if (year < 1) {
            throw new IllegalArgumentException("Year must be positive");
        }
        return store.findShipmentsPerQuarter(year);
    }
}
