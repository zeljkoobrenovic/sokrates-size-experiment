package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** One sales order line found by the order line search, with its order, customer and product. */
public record OrderLineMatch(long orderId, String orderNumber, LocalDate orderDate, long customerId, String customerName,
                             int lineNo, Product product, int quantity, BigDecimal unitPrice, String promotionCode) {
}
