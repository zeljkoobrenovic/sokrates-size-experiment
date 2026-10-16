package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The delivered orders of a region in a period: their number and the average days from order to delivery (two decimals). */
public record RegionLeadTime(Region region, int orderCount, BigDecimal averageDays) {
}
