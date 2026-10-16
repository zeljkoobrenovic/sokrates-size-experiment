package com.acme.backoffice.model;

import java.time.LocalDate;

/** A cancelled order: the order, the date and reason of its cancellation and the customer who placed it. */
public record CancelledOrder(SalesOrder order, LocalDate cancelDate, CancellationReason reason, Customer customer) {
}
