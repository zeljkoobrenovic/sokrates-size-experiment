package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** A sales return of a customer with its value: the returned quantities at the unit prices of the order. */
public record CustomerReturn(long id, String number, long customerId, String customerName, LocalDate returnDate,
                             String reason, BigDecimal returnedValue) {
}
