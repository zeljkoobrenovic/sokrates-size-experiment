package com.acme.backoffice.model;

/** The number of support tickets that currently have a status. */
public record TicketStatusCount(TicketStatus status, int count) {
}
