package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** A customer of a sales rep with the revenue of a period, last order date (null if none) and unpaid open invoice amount. */
public record SalesRepCustomer(long customerId, String name, BigDecimal revenue, LocalDate lastOrderDate, BigDecimal openInvoiceAmount) {
}
