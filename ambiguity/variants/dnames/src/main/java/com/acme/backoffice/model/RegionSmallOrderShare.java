package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The small orders of a region, all its orders, and the small share in percent (two decimals). */
public record RegionSmallOrderShare(Region region, int smallOrders, int totalOrders, BigDecimal sharePercent) {
}
