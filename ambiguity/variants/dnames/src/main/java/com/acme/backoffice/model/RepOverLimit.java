package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number of customers of a sales rep over their credit limit, and the total amount by which they exceed it. */
public record RepOverLimit(long salesRepId, String name, int customerCount, BigDecimal excessAmount) {
}
