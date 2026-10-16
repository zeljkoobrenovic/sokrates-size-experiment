package com.acme.backoffice.model;

import java.util.List;

/** The stock movements of one warehouse, oldest first. */
public record WarehouseMovements(Warehouse warehouse, List<StockMovement> movements) {
}
