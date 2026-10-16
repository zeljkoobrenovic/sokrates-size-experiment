package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Invoice(long id, String number, long orderId, long customerId, LocalDate invoiceDate, LocalDate dueDate,
                      BigDecimal amount, InvoiceStatus status) {
}
