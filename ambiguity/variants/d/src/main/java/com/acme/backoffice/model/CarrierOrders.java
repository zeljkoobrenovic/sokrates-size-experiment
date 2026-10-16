package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The orders shipped with a carrier in a period: their number and total value (quantity times unit price of the lines). */
public record CarrierOrders(Carrier carrier, int orderCount, BigDecimal value) {
}
