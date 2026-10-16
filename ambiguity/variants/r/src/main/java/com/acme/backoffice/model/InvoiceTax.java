package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The net, VAT and gross amount of an invoice, with the VAT rate in percent that was applied. */
public record InvoiceTax(long invoiceId, BigDecimal ratePercent, BigDecimal net, BigDecimal vat, BigDecimal gross) {
}
