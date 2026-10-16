package com.acme.backoffice.model;

/** An order that contains a product, with the customer who placed it. */
public record ProductOrder(SalesOrder order, Customer customer) {
}
