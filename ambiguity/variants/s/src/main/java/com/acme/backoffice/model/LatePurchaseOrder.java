package com.acme.backoffice.model;

import java.time.LocalDate;

/** A purchase order received after its expected date, with the receiving date and the days late. */
public record LatePurchaseOrder(PurchaseOrder order, LocalDate receivedDate, int daysLate) {
}
