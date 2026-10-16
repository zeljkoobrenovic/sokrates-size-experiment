package com.acme.backoffice.model;

/** The number of customers of a sales rep created in a period. */
public record SalesRepCustomerCount(long salesRepId, String name, int customerCount) {
}
