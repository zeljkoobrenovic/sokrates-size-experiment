package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.util.List;

/** The overpayments of a customer. */
public record CustomerUnappliedPayments(long customerId, String customerName, List<UnappliedPayment> payments) {
    /** The unapplied amount of all the payments. */
    public BigDecimal unappliedAmount() {
        return payments.stream().map(UnappliedPayment::unappliedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
