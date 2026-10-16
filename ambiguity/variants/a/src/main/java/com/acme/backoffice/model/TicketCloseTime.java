package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The number of tickets of a priority closed in a period and the average days from opening to closing (2 decimals). */
public record TicketCloseTime(TicketPriority priority, int closedCount, BigDecimal averageDays) {
}
