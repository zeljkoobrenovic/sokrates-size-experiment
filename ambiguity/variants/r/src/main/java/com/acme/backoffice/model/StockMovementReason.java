package com.acme.backoffice.model;

/** Why the stock of a product in a warehouse changed. */
public enum StockMovementReason {
    SHIPMENT, RECEIPT, RETURN, ADJUSTMENT, TRANSFER
}
