package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** The value credited to a customer for an accepted return. The id is 0 until the note has been created in the database. */
public record CreditNote(long id, String number, long customerId, long returnId, LocalDate creditDate, BigDecimal amount) {
}
