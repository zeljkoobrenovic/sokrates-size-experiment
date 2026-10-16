package com.acme.backoffice.model;

/** The stock movements of one reason in one warehouse for one product category: how many, and the net quantity change. */
public record ReasonMovements(StockMovementReason reason, Warehouse warehouse, String category, long movementCount,
                              long quantityChange) {
}
