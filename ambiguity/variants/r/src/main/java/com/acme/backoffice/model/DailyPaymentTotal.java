package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** The number and total of the payments received on one day. */
public record DailyPaymentTotal(LocalDate paymentDate, int paymentCount, BigDecimal total) {
}
