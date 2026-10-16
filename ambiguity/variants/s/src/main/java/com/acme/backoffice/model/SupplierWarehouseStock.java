package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The stock quantity and value (quantity times the last purchase unit cost) of a supplier's products in one warehouse. */
public record SupplierWarehouseStock(Warehouse warehouse, long quantity, BigDecimal value) {
}
