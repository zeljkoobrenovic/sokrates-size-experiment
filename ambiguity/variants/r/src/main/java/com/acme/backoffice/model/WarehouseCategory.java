package com.acme.backoffice.model;

/** A product category and a warehouse that has no stock of it. */
public record WarehouseCategory(Warehouse warehouse, String category) {
}
