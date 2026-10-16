package com.acme.backoffice.model;

/** The number of invoices of the customers of a country issued in a period. */
public record CountryInvoiceCount(String country, int invoiceCount) {
}
