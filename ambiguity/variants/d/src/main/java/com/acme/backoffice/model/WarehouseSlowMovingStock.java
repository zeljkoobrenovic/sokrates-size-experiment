package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The stock of a product in one warehouse that has not been shipped from that warehouse for a while, with its value (quantity times the last purchase unit cost). */
public record WarehouseSlowMovingStock(Warehouse warehouse, Product product, long quantity, BigDecimal value) {
}
