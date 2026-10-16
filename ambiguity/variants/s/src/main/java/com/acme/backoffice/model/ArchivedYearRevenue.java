package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The revenue of the archived, non-cancelled orders placed in one calendar year. */
public record ArchivedYearRevenue(int year, BigDecimal revenue) {
}
