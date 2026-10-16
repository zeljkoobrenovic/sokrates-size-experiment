package com.acme.backoffice.model;

/** A draft order that is a candidate for cleanup: the order, its age in days, the customer and the customer's sales rep (null if none). */
public record DraftOrder(SalesOrder order, long ageDays, Customer customer, SalesRep salesRep) {
}
