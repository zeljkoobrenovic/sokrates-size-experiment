package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** An open invoice with its customer, the currency it was issued in, and its amount in that currency and in EUR. */
public record CurrencyOpenInvoice(long customerId, String customerName, Currency currency, String invoiceNumber,
                                  LocalDate dueDate, BigDecimal currencyAmount, BigDecimal eurAmount) {
}
