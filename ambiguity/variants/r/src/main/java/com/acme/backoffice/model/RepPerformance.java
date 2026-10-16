package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and total value of the non-cancelled orders of a sales rep's customers in a period. */
public record RepPerformance(int orderCount, BigDecimal totalValue) {
}
