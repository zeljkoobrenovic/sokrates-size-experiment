package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The weight in kg shipped from a warehouse in a period. */
public record WarehouseShippedWeight(Warehouse warehouse, BigDecimal weightKg) {
}
