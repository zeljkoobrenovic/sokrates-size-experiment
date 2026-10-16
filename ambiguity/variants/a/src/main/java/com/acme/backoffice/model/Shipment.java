package com.acme.backoffice.model;

import java.time.LocalDate;

/** A shipment of an order; the delivery date is null while it is in transit. */
public record Shipment(long id, long orderId, long warehouseId, long carrierId, String trackingNumber, LocalDate shipDate,
                       LocalDate deliveryDate) {
    public Shipment(long id, long orderId, long warehouseId, long carrierId, String trackingNumber, LocalDate shipDate) {
        this(id, orderId, warehouseId, carrierId, trackingNumber, shipDate, null);
    }
}
