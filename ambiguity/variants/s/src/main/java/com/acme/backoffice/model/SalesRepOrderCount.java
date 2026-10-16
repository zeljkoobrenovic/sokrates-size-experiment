package com.acme.backoffice.model;

/** The number of orders of the customers of a sales rep in a period. */
public record SalesRepOrderCount(long salesRepId, String name, int orderCount) {
}
