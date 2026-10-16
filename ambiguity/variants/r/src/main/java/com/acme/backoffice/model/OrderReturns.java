package com.acme.backoffice.model;

import java.math.BigDecimal;

/**
 * An order that has returns: its value (quantity times unit price of the lines), the value of everything returned from
 * it, and the returned share of the order value in percent (two decimals, 0.00 for an order without value).
 */
public record OrderReturns(SalesOrder order, BigDecimal orderValue, BigDecimal returnedValue, BigDecimal returnedPercent) {
}
