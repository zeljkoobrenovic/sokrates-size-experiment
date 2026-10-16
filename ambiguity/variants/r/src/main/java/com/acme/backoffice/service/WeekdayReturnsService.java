package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.WeekdayReturns;

import java.time.LocalDate;
import java.util.List;

/** The number of sales returns per day of the week between two dates, overall and per region. */
public class WeekdayReturnsService {
    private final DataStore store;

    public WeekdayReturnsService(DataStore store) {
        this.store = store;
    }

    public List<WeekdayReturns> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.returns().countPerWeekday(from, to);
    }
}
