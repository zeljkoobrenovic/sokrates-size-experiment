package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Figures of a region in a period: all its customers, the non-cancelled orders placed in the period with their
 * revenue, the open invoices dated in the period with the part of them overdue at the end of the period, and the
 * shipments shipped, returns made and not closed tickets opened in the period.
 */
public record RegionReport(Region region, LocalDate from, LocalDate to, int customerCount, int orderCount,
        BigDecimal revenue, int openInvoiceCount, BigDecimal overdueAmount, int shipmentCount, int returnCount,
        int openTicketCount) {
}
