package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.WeekdayOrders;

import java.time.LocalDate;
import java.util.List;

/** The number of orders placed per day of the week between two dates, overall and per region. */
public class WeekdayOrdersService {
    private final DataStore store;

    public WeekdayOrdersService(DataStore store) {
        this.store = store;
    }

    public List<WeekdayOrders> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.countNonCancelledSalesOrdersPerWeekdayAndRegion(from, to);
    }
}
