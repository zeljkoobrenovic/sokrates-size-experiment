package com.acme.backoffice.model;

import java.util.List;

/** The open and paid invoices of a region in a period, with the total per status (open first, then paid). */
public record RegionInvoices(Region region, List<Invoice> invoices, List<InvoiceStatusTotal> totals) {
}
