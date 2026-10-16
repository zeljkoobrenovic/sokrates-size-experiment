package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The unpaid part of a customer's open invoices in the customer's currency. */
public record CustomerOpenAmount(Currency currency, BigDecimal amount) {
}
