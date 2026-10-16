package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** One line of the sales rep customers report. */
public record SalesRepCustomerReportRow(long customerId, String name, CustomerSegment segment, LocalDate lastOrderDate, BigDecimal openInvoiceAmount) {
}
