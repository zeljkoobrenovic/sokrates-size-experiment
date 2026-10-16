package com.acme.backoffice.model;

import java.time.LocalDate;

/** A support ticket of a customer. The id is 0 until created. The order id and the closed date may be null. */
public record SupportTicket(long id, String number, long customerId, Long orderId, String subject, TicketPriority priority,
        TicketStatus status, LocalDate openedDate, LocalDate closedDate) {
}
