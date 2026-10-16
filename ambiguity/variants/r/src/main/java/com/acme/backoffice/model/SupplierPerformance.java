package com.acme.backoffice.model;

import java.math.BigDecimal;

/**
 * Delivery performance of a supplier for the purchase orders received in a period: the number received, the average delay in
 * days against the expected date (negative if early) and the percentage of them received later than expected.
 */
public record SupplierPerformance(Supplier supplier, int receivedCount, BigDecimal averageDelayDays, BigDecimal lateShare) {
}
