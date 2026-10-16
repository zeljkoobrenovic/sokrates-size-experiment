package com.acme.backoffice.model;

import java.time.LocalDate;
import java.util.List;

/** A move of stock from one warehouse to another. The id is 0 until the transfer has been created in the database. */
public record WarehouseTransfer(long id, String number, long fromWarehouseId, long toWarehouseId, LocalDate transferDate,
                                List<WarehouseTransferLine> lines) {
}
