package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The number and total of the payments received in one calendar month. */
public record MonthlyPaymentTotal(int year, int month, int paymentCount, BigDecimal total) {
}
