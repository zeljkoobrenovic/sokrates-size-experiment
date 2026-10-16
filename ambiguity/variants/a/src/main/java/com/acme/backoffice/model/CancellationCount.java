package com.acme.backoffice.model;

/** Number of orders of a customer cancelled for a reason in a period. */
public record CancellationCount(CancellationReason reason, long customerId, String name, int orderCount) {
}
