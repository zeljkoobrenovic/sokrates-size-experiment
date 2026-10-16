package com.acme.backoffice.model;

/** The quantity of a product transferred from one warehouse to another. */
public record TransferRoute(long fromWarehouseId, String fromWarehouseCode, long toWarehouseId, String toWarehouseCode,
                            long quantity) {
}
