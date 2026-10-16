package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and total amount of the invoices with one status. */
public record InvoiceStatusTotal(InvoiceStatus status, int invoiceCount, BigDecimal total) {
}
