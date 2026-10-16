package com.acme.backoffice.model;

import java.util.List;

/** The large invoices of a region in a period, biggest first. */
public record RegionLargeInvoices(Region region, List<Invoice> invoices) {
}
