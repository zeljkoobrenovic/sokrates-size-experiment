package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The delivered orders of a carrier in a period: their number and the average days from order to delivery (two decimals). */
public record CarrierLeadTime(Carrier carrier, int orderCount, BigDecimal averageDays) {
}
