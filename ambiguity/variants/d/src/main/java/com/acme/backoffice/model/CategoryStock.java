package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The stock quantity and value (quantity times the last purchase unit cost) of a product category over all warehouses. */
public record CategoryStock(String category, long quantity, BigDecimal value) {
}
