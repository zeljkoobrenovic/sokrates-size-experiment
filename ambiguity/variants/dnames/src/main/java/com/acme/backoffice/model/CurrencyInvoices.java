package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.util.List;

/** The open invoices issued in a currency, per customer. */
public record CurrencyInvoices(Currency currency, List<CustomerCurrencyInvoices> customers) {
    /** The amount of all the invoices in the currency. */
    public BigDecimal currencyAmount() {
        return customers.stream().map(CustomerCurrencyInvoices::currencyAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** The amount of all the invoices in EUR. */
    public BigDecimal eurAmount() {
        return customers.stream().map(CustomerCurrencyInvoices::eurAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
