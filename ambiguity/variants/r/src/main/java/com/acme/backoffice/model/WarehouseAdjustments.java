package com.acme.backoffice.model;

import java.util.List;

/** The stock adjustments of one warehouse. */
public record WarehouseAdjustments(Warehouse warehouse, List<StockAdjustment> adjustments) {
}
