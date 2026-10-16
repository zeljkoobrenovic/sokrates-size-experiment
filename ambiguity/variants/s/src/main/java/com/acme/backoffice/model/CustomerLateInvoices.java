package com.acme.backoffice.model;

import java.util.List;

/** The invoices a customer paid late in a period. */
public record CustomerLateInvoices(long customerId, String customerName, List<LatePaidInvoice> invoices) {
}
