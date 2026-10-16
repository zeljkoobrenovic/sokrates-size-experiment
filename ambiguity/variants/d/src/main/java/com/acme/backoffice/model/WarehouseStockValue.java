package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The value of the stock in one warehouse: quantity times the last purchase unit cost of each product. */
public record WarehouseStockValue(Warehouse warehouse, BigDecimal value) {
}
