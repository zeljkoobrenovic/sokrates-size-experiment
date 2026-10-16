package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Dashboard;

import java.time.LocalDate;

/**
 * One summary for a date. Orders, invoices, tickets and back orders count as they stood on the date (an order counts
 * from its order date, an invoice is overdue when due before the date). Stock has no history, so the stock value is
 * the current one, valued by last purchase cost as in StockValueService.
 */
public class DashboardService {
    private final DataStore store;

    public DashboardService(DataStore store) {
        this.store = store;
    }

    public Dashboard summarize(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        return new Dashboard(date, store.countOpenSalesOrdersOnDate(date), store.findOpenSalesOrderValueOnDate(date),
                store.findOverdueInvoiceAmount(date), store.findTotalStockValue(),
                store.countSupportTicketsOpenOnDate(date), store.countBackOrdersOnDate(date),
                store.countSalesOrdersAwaitingApprovalOnDate(DiscountApprovalService.APPROVAL_THRESHOLD, date));
    }
}
