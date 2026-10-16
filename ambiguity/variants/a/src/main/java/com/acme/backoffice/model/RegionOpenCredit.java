package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number of customers with open credit, number of open credit notes and their total amount in a region. */
public record RegionOpenCredit(Region region, int customerCount, int noteCount, BigDecimal openAmount) {
}
