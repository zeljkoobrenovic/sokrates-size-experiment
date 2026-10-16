package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.WeekdayPurchaseOrders;

import java.time.LocalDate;
import java.util.List;

/** The number of purchase orders per day of the week between two dates, overall and per region. */
public class WeekdayPurchaseOrdersService {
    private final DataStore store;

    public WeekdayPurchaseOrdersService(DataStore store) {
        this.store = store;
    }

    public List<WeekdayPurchaseOrders> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.findPurchaseOrdersPerWeekday(from, to);
    }
}
