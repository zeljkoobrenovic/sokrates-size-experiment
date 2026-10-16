package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The stock of a product over all warehouses that has not been shipped for a while, with its value (quantity times the last purchase unit cost). */
public record SlowMovingStock(Product product, long quantity, BigDecimal value) {
}
