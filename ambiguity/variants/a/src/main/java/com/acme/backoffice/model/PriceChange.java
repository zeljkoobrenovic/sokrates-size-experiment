package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** A change of the catalogue price of a product. */
public record PriceChange(long id, long productId, LocalDate changeDate, BigDecimal oldPrice, BigDecimal newPrice) {
}
