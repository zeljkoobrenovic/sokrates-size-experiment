package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.util.List;

/** The open invoices of a customer in one currency. */
public record CustomerCurrencyInvoices(long customerId, String customerName, List<CurrencyOpenInvoice> invoices) {
    /** The amount of all the invoices in their currency. */
    public BigDecimal currencyAmount() {
        return invoices.stream().map(CurrencyOpenInvoice::currencyAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** The amount of all the invoices in EUR. */
    public BigDecimal eurAmount() {
        return invoices.stream().map(CurrencyOpenInvoice::eurAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
