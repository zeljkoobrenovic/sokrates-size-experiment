package com.acme.backoffice.model;

import java.math.BigDecimal;

/** A customer whose open invoice amount exceeds the credit limit; the sales rep name is null if there is no rep. */
public record CustomerOverLimit(long customerId, String name, Region region, String salesRepName, BigDecimal creditLimit,
                                BigDecimal openAmount) {
}
