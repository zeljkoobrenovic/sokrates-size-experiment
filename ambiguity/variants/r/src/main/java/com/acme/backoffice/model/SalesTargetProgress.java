package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Target of a sales rep for a month, the revenue of their customers' non-cancelled orders and the percentage reached. */
public record SalesTargetProgress(long salesRepId, String salesRepName, BigDecimal target, BigDecimal revenue,
                                  BigDecimal percentReached) {
}
