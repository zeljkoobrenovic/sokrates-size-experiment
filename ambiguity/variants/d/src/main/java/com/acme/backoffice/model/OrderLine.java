package com.acme.backoffice.model;

import java.math.BigDecimal;

/**
 * One line of a sales order; the unit price is the price at the time of ordering, after any promotion. The promotion
 * code is null when no promotion was applied.
 */
public record OrderLine(long productId, int quantity, BigDecimal unitPrice, String promotionCode) {
    public OrderLine(long productId, int quantity, BigDecimal unitPrice) {
        this(productId, quantity, unitPrice, null);
    }
}
