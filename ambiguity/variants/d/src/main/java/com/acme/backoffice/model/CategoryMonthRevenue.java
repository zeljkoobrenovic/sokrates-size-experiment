package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.YearMonth;

/** The revenue of a product category in one calendar month, from non-cancelled orders. */
public record CategoryMonthRevenue(String category, YearMonth month, BigDecimal revenue) {
}
