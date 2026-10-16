package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.WeekdayShipments;

import java.time.LocalDate;
import java.util.List;

/** The number of shipments sent per day of the week between two dates, overall and per region. */
public class WeekdayShipmentsService {
    private final DataStore store;

    public WeekdayShipmentsService(DataStore store) {
        this.store = store;
    }

    public List<WeekdayShipments> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.findShipmentCountPerWeekday(from, to);
    }
}
