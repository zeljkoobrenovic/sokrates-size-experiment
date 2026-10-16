package com.acme.backoffice.model;

import java.math.BigDecimal;

/** A customer with credit notes not yet used against an invoice: their number and total amount. */
public record CustomerOpenCredit(long customerId, String name, Region region, int noteCount, BigDecimal openAmount) {
}
