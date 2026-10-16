package com.acme.backoffice.model;

/** The number of invoices of the customers of a sales rep in a period. */
public record SalesRepInvoiceCount(long salesRepId, String name, int invoiceCount) {
}
