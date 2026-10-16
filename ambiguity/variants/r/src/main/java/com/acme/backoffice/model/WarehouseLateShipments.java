package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * The shipments of a warehouse delivered late in a period, oldest first, the number of delivered shipments, and the percentage
 * of delivered shipments that were late (one decimal).
 */
public record WarehouseLateShipments(Warehouse warehouse, List<Shipment> late, int delivered, BigDecimal latePercent) {
}
