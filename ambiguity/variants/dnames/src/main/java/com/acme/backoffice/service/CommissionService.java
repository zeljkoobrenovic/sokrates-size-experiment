package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CommissionBasis;
import com.acme.backoffice.model.RepCommission;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;

/** Commission of the sales reps on the paid invoices of their customers. */
public class CommissionService {
    static final BigDecimal BASE_RATE = new BigDecimal("0.03");
    static final BigDecimal ABOVE_TARGET_RATE = new BigDecimal("0.05");

    private final DataStore store;

    public CommissionService(DataStore store) {
        this.store = store;
    }

    /** The commission of the rep for the month. */
    public RepCommission commissionOf(long salesRepId, YearMonth month) {
        CommissionBasis basis = store.findCommissionBasisOfSalesRep(salesRepId, month.atDay(1), month.atEndOfMonth())
                .orElseThrow(() -> new IllegalArgumentException("Unknown sales rep " + salesRepId));
        return commission(basis);
    }

    /** The commission of every rep for the month, sorted by rep name. */
    public List<RepCommission> commissionOfAll(YearMonth month) {
        return store.listCommissionBasesPerSalesRep(month.atDay(1), month.atEndOfMonth()).stream().map(this::commission).toList();
    }

    private RepCommission commission(CommissionBasis basis) {
        BigDecimal paid = basis.paidAmount();
        BigDecimal withinTarget = paid;
        BigDecimal aboveTarget = BigDecimal.ZERO;
        if (basis.target() != null && paid.compareTo(basis.target()) > 0) {
            withinTarget = basis.target();
            aboveTarget = paid.subtract(basis.target());
        }
        BigDecimal commission = withinTarget.multiply(BASE_RATE).add(aboveTarget.multiply(ABOVE_TARGET_RATE))
                .setScale(2, RoundingMode.HALF_UP);
        return new RepCommission(basis.salesRepId(), basis.salesRepName(), paid, basis.target(), commission);
    }
}
