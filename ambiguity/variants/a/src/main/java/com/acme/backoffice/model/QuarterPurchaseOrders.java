package com.acme.backoffice.model;

import java.util.Map;

/** Purchase orders placed in one calendar quarter (1 to 4): the total and the number per region (every region present). */
public record QuarterPurchaseOrders(int quarter, int total, Map<Region, Integer> perRegion) {
}
