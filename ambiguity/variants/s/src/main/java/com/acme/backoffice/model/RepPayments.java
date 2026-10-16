package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and total amount of the payments received from a sales rep's customers. */
public record RepPayments(long salesRepId, String name, int paymentCount, BigDecimal totalAmount) {
}
