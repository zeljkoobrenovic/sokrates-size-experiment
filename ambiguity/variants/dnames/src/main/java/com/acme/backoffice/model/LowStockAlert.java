package com.acme.backoffice.model;

/** A product in a warehouse whose stock quantity is at or below its reorder level there. */
public record LowStockAlert(Warehouse warehouse, Product product, int quantity, int reorderLevel) {
}
