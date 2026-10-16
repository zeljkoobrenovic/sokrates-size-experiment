package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** One line of a customer statement. The amount is positive for invoices and negative for payments and credit notes. */
public record StatementEntry(LocalDate date, StatementEntryType type, String reference, BigDecimal amount, BigDecimal balance) {
}
