package com.acme.backoffice.model;

/** One line of a warehouse transfer: the quantity of a product that is moved. */
public record WarehouseTransferLine(long productId, int quantity) {
}
