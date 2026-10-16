package com.acme.backoffice.model;

/** The number of support tickets with a priority. */
public record PriorityTickets(TicketPriority priority, int ticketCount) {
}
