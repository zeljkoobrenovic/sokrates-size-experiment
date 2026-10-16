package com.acme.backoffice.model;

/** A customer with the number of non-cancelled orders placed in a period, and the customer's region. */
public record RepeatCustomer(Region region, long customerId, String name, int orderCount) {
}
