package com.acme.backoffice.model;

import java.util.List;

/** What a region manager has to look after in a region: open orders, overdue invoices and open tickets of its customers. */
public record RegionOverview(Region region, List<SalesOrder> openOrders, List<Invoice> overdueInvoices,
        List<SupportTicket> openTickets) {
}
