package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionOverview;
import com.acme.backoffice.model.SalesRep;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Assigns a rep as manager of a region and shows the manager what is open in the managed regions. */
public class RegionManagerService {
    private final DataStore store;

    public RegionManagerService(DataStore store) {
        this.store = store;
    }

    /** Makes the rep the manager of the region, replacing the previous manager. */
    public void assign(Region region, long salesRepId) {
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        if (store.findSalesRepById(salesRepId).isEmpty()) {
            throw new IllegalArgumentException("Unknown sales rep " + salesRepId);
        }
        store.saveRegionManager(region, salesRepId);
    }

    public Optional<SalesRep> findManager(Region region) {
        return store.findRegionManager(region);
    }

    /** One overview per region managed by the rep, in region name order; overdue means due before the given date. */
    public List<RegionOverview> overviewFor(long salesRepId, LocalDate asOf) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (store.findSalesRepById(salesRepId).isEmpty()) {
            throw new IllegalArgumentException("Unknown sales rep " + salesRepId);
        }
        return store.listRegionsManagedBySalesRep(salesRepId).stream()
                .map(region -> new RegionOverview(region, store.listOpenSalesOrdersInRegion(region),
                        store.listOverdueInvoicesInRegion(asOf, region), store.listOpenSupportTicketsInRegion(region)))
                .toList();
    }
}
