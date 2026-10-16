package com.acme.backoffice.model;

import java.time.LocalDate;

/** A paid invoice whose last payment came after its due date, with the customer and the days late. */
public record LatePaidInvoice(long customerId, String customerName, String invoiceNumber, LocalDate dueDate,
                              LocalDate paidDate, int daysLate) {
}
