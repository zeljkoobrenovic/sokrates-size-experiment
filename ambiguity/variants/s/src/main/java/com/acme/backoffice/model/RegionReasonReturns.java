package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and value of the sales returns of the customers of a region given for a reason in a period. */
public record RegionReasonReturns(Region region, String reason, int returnCount, BigDecimal returnedValue) {
}
