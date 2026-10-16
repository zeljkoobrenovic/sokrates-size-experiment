package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and total value of the non-cancelled orders of the customers of a region in a period. */
public record RegionSales(Region region, int orderCount, BigDecimal totalValue) {
}
