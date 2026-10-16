package com.acme.backoffice.model;

import java.time.LocalDate;

/** A note that staff wrote on a sales order. The id is 0 until the note has been created in the database. */
public record OrderNote(long id, long orderId, String author, LocalDate date, String text) {
}
