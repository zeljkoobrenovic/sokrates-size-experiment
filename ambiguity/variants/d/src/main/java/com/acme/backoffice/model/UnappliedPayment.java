package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** A payment that exceeds the amount due of its invoice, with the part of it that is not needed to pay the invoice. */
public record UnappliedPayment(long id, String invoiceNumber, long customerId, String customerName,
                               LocalDate paymentDate, BigDecimal amount, BigDecimal unappliedAmount) {
}
