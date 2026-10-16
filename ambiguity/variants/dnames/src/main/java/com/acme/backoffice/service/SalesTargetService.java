package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.SalesTargetProgress;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

/** Monthly sales targets per rep and how far the reps have come towards them. */
public class SalesTargetService {
    static final BigDecimal LOW_PERCENT = new BigDecimal("80");

    private final DataStore store;

    public SalesTargetService(DataStore store) {
        this.store = store;
    }

    /** Sets (or replaces) the target of the rep for the month. */
    public void setTarget(long salesRepId, YearMonth month, BigDecimal target) {
        if (target == null || target.signum() <= 0) {
            throw new IllegalArgumentException("Target must be positive");
        }
        store.saveSalesTarget(salesRepId, month.atDay(1), target);
    }

    /** Per rep with a target for the month: target, revenue and percentage reached. */
    public List<SalesTargetProgress> progress(YearMonth month) {
        return store.listSalesTargetProgressPerSalesRep(month.atDay(1), month.atEndOfMonth());
    }

    /** The reps whose percentage reached for the month is below 80. */
    public List<SalesTargetProgress> repsBelowTarget(YearMonth month) {
        return progress(month).stream().filter(p -> p.percentReached().compareTo(LOW_PERCENT) < 0).toList();
    }
}
