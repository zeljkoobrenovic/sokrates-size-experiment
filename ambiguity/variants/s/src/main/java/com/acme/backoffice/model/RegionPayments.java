package com.acme.backoffice.model;

import java.util.List;

/** The payments received from customers of a region in a period, oldest first, and their totals per month. */
public record RegionPayments(Region region, List<PaymentReceived> payments, List<MonthlyPaymentTotal> monthlyTotals) {
}
