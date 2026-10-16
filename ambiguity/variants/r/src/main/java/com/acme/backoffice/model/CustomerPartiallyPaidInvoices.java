package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.util.List;

/** The partially paid invoices of a customer. */
public record CustomerPartiallyPaidInvoices(long customerId, String customerName, List<PartiallyPaidInvoice> invoices) {
    /** The amount still due on all the invoices. */
    public BigDecimal dueAmount() {
        return invoices.stream().map(PartiallyPaidInvoice::dueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
