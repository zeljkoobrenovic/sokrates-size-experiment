package com.acme.backoffice.model;

/** The number of shipments of the customers of a sales rep in a period. */
public record SalesRepShipmentCount(long salesRepId, String name, int shipmentCount) {
}
