package com.acme.backoffice.model;

/** The number of waiting back orders with a line in a product category, and the units ordered on those lines. */
public record CategoryBackOrders(String category, int backOrderCount, int units) {
}
