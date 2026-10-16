package com.acme.backoffice.model;

/** The number of returns of the customers of a sales rep in a period. */
public record SalesRepReturnCount(long salesRepId, String name, int returnCount) {
}
