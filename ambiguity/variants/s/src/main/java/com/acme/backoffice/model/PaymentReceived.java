package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** A payment together with the number of its invoice and the customer who paid. */
public record PaymentReceived(long id, String invoiceNumber, long customerId, String customerName,
                              LocalDate paymentDate, BigDecimal amount) {
}
