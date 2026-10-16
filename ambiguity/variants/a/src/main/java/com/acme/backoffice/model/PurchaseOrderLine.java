package com.acme.backoffice.model;

import java.math.BigDecimal;

/** One line of a purchase order; the unit cost is the cost agreed with the supplier. */
public record PurchaseOrderLine(long productId, int quantity, BigDecimal unitCost) {
}
