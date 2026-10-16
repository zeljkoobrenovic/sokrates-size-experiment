package com.acme.backoffice.model;

import java.time.LocalDate;

/** A ticket closed in a period, with its customer and the days from opening to closing. */
public record ClosedTicket(String number, long customerId, String customerName, TicketPriority priority, LocalDate openedDate,
        LocalDate closedDate, int daysToClose) {
}
