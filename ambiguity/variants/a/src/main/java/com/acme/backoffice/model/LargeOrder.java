package com.acme.backoffice.model;

import java.math.BigDecimal;

/** A large order: the order, its value (quantity times unit price of the lines) and the customer who placed it. */
public record LargeOrder(SalesOrder order, BigDecimal value, Customer customer) {
}
