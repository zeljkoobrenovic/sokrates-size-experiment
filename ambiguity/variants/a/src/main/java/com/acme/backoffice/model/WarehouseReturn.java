package com.acme.backoffice.model;

import java.time.LocalDate;

/** A sales return received into a warehouse, with the number of the returned order. */
public record WarehouseReturn(long id, String number, String orderNumber, LocalDate returnDate, String reason) {
}
