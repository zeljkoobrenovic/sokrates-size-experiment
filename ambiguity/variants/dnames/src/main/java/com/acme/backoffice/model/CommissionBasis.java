package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The paid invoice total of a sales rep for a month and the rep's target for it, null if there is none. */
public record CommissionBasis(long salesRepId, String salesRepName, BigDecimal paidAmount, BigDecimal target) {
}
