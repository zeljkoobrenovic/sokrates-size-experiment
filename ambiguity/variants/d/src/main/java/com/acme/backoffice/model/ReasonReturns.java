package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and value of the sales returns given for a reason in a period. */
public record ReasonReturns(String reason, int returnCount, BigDecimal returnedValue) {
}
