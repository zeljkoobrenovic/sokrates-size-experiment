package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** An order line of a customer that got a promotion, with the discount given on it. */
public record CustomerPromotionLine(String orderNumber, LocalDate orderDate, int lineNo, String sku, int quantity,
                                    BigDecimal unitPrice, String promotionCode, BigDecimal discount) {
}
