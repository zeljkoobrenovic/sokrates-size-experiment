package com.acme.backoffice.model;

import java.math.BigDecimal;

/** How often a promotion was applied to order lines in a period, the discount given and the revenue of those lines. */
public record PromotionUsage(String code, int lineCount, BigDecimal discount, BigDecimal revenue) {
}
