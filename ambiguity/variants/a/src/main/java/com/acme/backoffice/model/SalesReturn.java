package com.acme.backoffice.model;

import java.time.LocalDate;
import java.util.List;

/** Goods returned by a customer for a sales order. The id is 0 until the return has been created in the database. */
public record SalesReturn(long id, String number, long orderId, LocalDate returnDate, String reason, List<ReturnLine> lines) {
}
