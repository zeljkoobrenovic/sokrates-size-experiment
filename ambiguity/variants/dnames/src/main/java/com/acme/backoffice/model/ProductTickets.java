package com.acme.backoffice.model;

/** The number of support tickets about a product. */
public record ProductTickets(long productId, String sku, String name, int ticketCount) {
}
