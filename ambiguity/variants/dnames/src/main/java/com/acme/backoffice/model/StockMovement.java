package com.acme.backoffice.model;

import java.time.LocalDate;

/** One logged change of the stock of a product in a warehouse; the quantity change is negative when stock leaves. */
public record StockMovement(long id, long productId, long warehouseId, LocalDate movementDate, int quantityChange,
                            StockMovementReason reason) {
}
