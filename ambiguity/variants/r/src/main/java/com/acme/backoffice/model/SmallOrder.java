package com.acme.backoffice.model;

import java.math.BigDecimal;

/** A small order: the order, its value (quantity times unit price of the lines) and the customer who placed it. */
public record SmallOrder(SalesOrder order, BigDecimal value, Customer customer) {
}
