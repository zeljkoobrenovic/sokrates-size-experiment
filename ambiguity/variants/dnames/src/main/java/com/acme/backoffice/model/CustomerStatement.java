package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** What a customer owes at the start of a period, what happened during it in date order, and what is owed at the end. */
public record CustomerStatement(long customerId, String customerName, LocalDate from, LocalDate to,
                                BigDecimal openingBalance, List<StatementEntry> entries, BigDecimal closingBalance) {
}
