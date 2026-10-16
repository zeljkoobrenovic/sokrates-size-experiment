package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and total amount of the overdue invoices of a sales rep's customers. */
public record RepOverdueInvoices(long salesRepId, String name, int invoiceCount, BigDecimal totalAmount) {
}
