package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionReport;

import java.time.LocalDate;

/** The report of a region for a period; an invoice is overdue when due before the end of the period. */
public class RegionReportService {
    private final DataStore store;

    public RegionReportService(DataStore store) {
        this.store = store;
    }

    public RegionReport report(Region region, LocalDate from, LocalDate to) {
        if (region == null || from == null || to == null) {
            throw new IllegalArgumentException("Region and period are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Period end is before its start");
        }
        return store.findRegionReportBetween(region, from, to);
    }
}
