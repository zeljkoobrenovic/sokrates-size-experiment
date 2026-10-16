package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.WeekdayInvoices;

import java.time.LocalDate;
import java.util.List;

/** The number of invoices issued per day of the week between two dates, overall and per region. */
public class WeekdayInvoicesService {
    private final DataStore store;

    public WeekdayInvoicesService(DataStore store) {
        this.store = store;
    }

    public List<WeekdayInvoices> report(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        return store.findInvoicesPerWeekday(from, to);
    }
}
