package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Number and value of the purchase orders received from the suppliers of a country in a period. */
public record CountrySpending(String country, int orderCount, BigDecimal totalValue) {
}
