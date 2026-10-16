package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.util.List;

/** The order lines of a customer that got a promotion in a period, and the total discount given to the customer. */
public record CustomerPromotions(List<CustomerPromotionLine> lines, BigDecimal discount) {
}
