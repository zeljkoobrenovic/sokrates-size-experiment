package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A percentage discount valid from start to end date, both inclusive. The category is null when the promotion applies to
 * all products. The id is 0 until the promotion has been created in the database.
 */
public record Promotion(long id, String code, BigDecimal discountPercent, LocalDate startDate, LocalDate endDate, String category) {
}
