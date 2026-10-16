package com.acme.backoffice.model;

import java.time.LocalDate;
import java.util.List;

/**
 * An order placed with a supplier. The id is 0 until the purchase order has been created in the database.
 * The warehouse id is the warehouse the order will be received into, or 0 if none is named.
 */
public record PurchaseOrder(long id, String number, long supplierId, LocalDate orderDate, LocalDate expectedDate,
                            PurchaseOrderStatus status, List<PurchaseOrderLine> lines, long warehouseId) {

    /** A purchase order that names no warehouse. */
    public PurchaseOrder(long id, String number, long supplierId, LocalDate orderDate, LocalDate expectedDate,
                         PurchaseOrderStatus status, List<PurchaseOrderLine> lines) {
        this(id, number, supplierId, orderDate, expectedDate, status, lines, 0);
    }
}
