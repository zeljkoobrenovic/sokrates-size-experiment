package com.acme.backoffice.model;

/** The number of delivered shipments of a warehouse in a period, and how many of them were delivered late. */
public record WarehouseDeliveryCount(Warehouse warehouse, int delivered, int late) {
}
