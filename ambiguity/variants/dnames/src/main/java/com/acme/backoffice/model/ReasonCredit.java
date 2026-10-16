package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and amount of the credit notes given for the returns with a reason in a period. */
public record ReasonCredit(String reason, int noteCount, BigDecimal creditedAmount) {
}
