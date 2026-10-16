package com.acme.backoffice.model;

import java.util.List;

/**
 * The shipments containing a product shipped in a period, oldest first, and the quantity of the product shipped per warehouse,
 * ordered by warehouse code.
 */
public record ProductShipments(Product product, List<Shipment> shipments, List<WarehouseShippedQuantity> quantities) {
}
