package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** The fixed exchange rate of a currency from the given date on: units of the currency per 1 EUR. */
public record ExchangeRate(long id, Currency currency, LocalDate rateDate, BigDecimal perEur) {
}
