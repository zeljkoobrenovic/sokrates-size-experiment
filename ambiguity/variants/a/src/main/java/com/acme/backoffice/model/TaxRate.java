package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** The VAT rate in percent of a country from the given date on. */
public record TaxRate(long id, String country, LocalDate validFrom, BigDecimal ratePercent) {
}
