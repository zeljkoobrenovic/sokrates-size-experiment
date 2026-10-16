package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and total value of the non-cancelled orders of a customer in a period. */
public record CustomerSales(long customerId, String name, int orderCount, BigDecimal revenue) {
}
