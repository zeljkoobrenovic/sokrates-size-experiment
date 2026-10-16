package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Activity summary of a customer. The order figures cover the non-cancelled orders; the dates are null if there are
 * no such orders.
 */
public record CustomerActivity(LocalDate firstOrderDate, LocalDate lastOrderDate, int orderCount, int returnCount,
                               BigDecimal totalOrdered, BigDecimal totalPaid, BigDecimal totalCredited) {
}
