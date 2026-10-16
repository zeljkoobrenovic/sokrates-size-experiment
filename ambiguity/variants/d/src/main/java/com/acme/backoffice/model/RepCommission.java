package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Commission of a sales rep for a month: 3% of the paid invoices up to the target, 5% of the part above it. */
public record RepCommission(long salesRepId, String salesRepName, BigDecimal paidAmount, BigDecimal target,
                            BigDecimal commission) {
}
