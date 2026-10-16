package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** An open invoice with at least one payment, with its customer, region, amount paid so far and amount still due. */
public record PartiallyPaidInvoice(long customerId, String customerName, Region region, String invoiceNumber,
                                   LocalDate dueDate, BigDecimal amount, BigDecimal paidAmount, BigDecimal dueAmount) {
}
