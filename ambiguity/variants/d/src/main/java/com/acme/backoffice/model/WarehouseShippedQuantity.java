package com.acme.backoffice.model;

/** The quantity of a product shipped from a warehouse in a period. */
public record WarehouseShippedQuantity(Warehouse warehouse, int quantity) {
}
