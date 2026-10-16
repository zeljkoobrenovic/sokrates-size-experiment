package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number of customers over their credit limit in a region, and the total amount by which they exceed it. */
public record RegionOverLimit(Region region, int customerCount, BigDecimal excessAmount) {
}
