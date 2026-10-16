package com.acme.backoffice.model;

/** The number of support tickets about an order. */
public record OrderTickets(long orderId, String orderNumber, long customerId, int ticketCount) {
}
