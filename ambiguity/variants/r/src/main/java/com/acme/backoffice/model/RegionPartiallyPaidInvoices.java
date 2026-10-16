package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.util.List;

/** The partially paid invoices of the customers of a region, per customer. */
public record RegionPartiallyPaidInvoices(Region region, List<CustomerPartiallyPaidInvoices> customers) {
    /** The amount still due on all the invoices of the region. */
    public BigDecimal dueAmount() {
        return customers.stream().map(CustomerPartiallyPaidInvoices::dueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
