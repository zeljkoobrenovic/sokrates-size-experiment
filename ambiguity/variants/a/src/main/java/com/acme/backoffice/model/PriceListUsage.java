package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Per customer the order lines sold at a special price in a period, their revenue and the revenue at catalogue prices. */
public record PriceListUsage(long customerId, String customerName, int lineCount, BigDecimal specialRevenue,
                             BigDecimal catalogueRevenue) {
}
