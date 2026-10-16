package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Revenue, purchase cost of the units sold, margin and margin percentage of a category in non-cancelled orders in a period. */
public record CategoryMargin(String category, BigDecimal revenue, BigDecimal cost, BigDecimal margin, BigDecimal marginPercent) {
}
