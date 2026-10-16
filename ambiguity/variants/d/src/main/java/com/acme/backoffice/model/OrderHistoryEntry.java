package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** One order in an order history: date, status, number of lines and total value (quantity times unit price). */
public record OrderHistoryEntry(String number, long customerId, LocalDate orderDate, OrderStatus status, int lineCount,
                                BigDecimal total) {
}
