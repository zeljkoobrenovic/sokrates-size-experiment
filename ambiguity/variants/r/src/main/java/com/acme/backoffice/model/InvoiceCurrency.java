package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The currency an invoice was issued in and its amount in that currency. The amount of the invoice itself stays in EUR. */
public record InvoiceCurrency(long invoiceId, Currency currency, BigDecimal amount) {
}
