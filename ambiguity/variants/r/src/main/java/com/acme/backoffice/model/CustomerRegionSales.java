package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and total value of the non-cancelled orders of a customer in a period, with the region of the customer. */
public record CustomerRegionSales(long customerId, String name, Region region, int orderCount, BigDecimal revenue) {
}
