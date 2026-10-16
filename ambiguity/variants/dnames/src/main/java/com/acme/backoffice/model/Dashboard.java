package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** The summary of the business on a date: what is open, overdue, in stock and waiting. */
public record Dashboard(LocalDate date, int openOrders, BigDecimal openOrderValue, BigDecimal overdueInvoiceAmount,
        BigDecimal stockValue, int openTickets, int backOrders, int ordersAwaitingApproval) {
}
