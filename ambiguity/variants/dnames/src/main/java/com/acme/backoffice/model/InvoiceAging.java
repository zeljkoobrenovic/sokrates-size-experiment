package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Unpaid amount of the open invoices of a customer, split by how many days they are overdue on a given date. */
public record InvoiceAging(long customerId, String name, BigDecimal notDue, BigDecimal days1To30,
                           BigDecimal days31To60, BigDecimal days61To90, BigDecimal over90) {
}
