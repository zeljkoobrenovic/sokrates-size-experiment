package com.acme.backoffice.model;

import java.time.LocalDate;

/**
 * A customer whose first non-cancelled order was placed in a period, with the customer's region, the date of that
 * order and the customer's sales rep (null when the customer has none).
 */
public record FirstTimeCustomer(Region region, SalesRep salesRep, long customerId, String name, LocalDate firstOrderDate) {
}
