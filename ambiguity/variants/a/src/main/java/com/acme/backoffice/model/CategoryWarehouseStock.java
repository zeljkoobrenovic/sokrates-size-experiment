package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The stock quantity and value (quantity times the last purchase unit cost) of a product category in one warehouse. */
public record CategoryWarehouseStock(String category, Warehouse warehouse, long quantity, BigDecimal value) {
}
