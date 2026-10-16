package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The weight in kg shipped by a carrier in a period. */
public record CarrierShippedWeight(Carrier carrier, BigDecimal weightKg) {
}
