package com.acme.backoffice.model;

import java.math.BigDecimal;

/**
 * Sales figures of one calendar month: non-cancelled orders and their revenue, customers created, returns and the
 * amount credited for them.
 */
public record MonthlySales(int month, int orderCount, BigDecimal revenue, int newCustomers, int returnCount,
                           BigDecimal creditedAmount) {
}
