package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and value of the purchase orders received from a supplier in a period. */
public record SupplierSpending(Supplier supplier, int orderCount, BigDecimal totalValue) {
}
