package com.acme.backoffice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A special unit price of a customer for a product, valid from start to end date, both inclusive. The id is 0 until the
 * price has been created in the database.
 */
public record CustomerPrice(long id, long customerId, long productId, BigDecimal unitPrice, LocalDate startDate, LocalDate endDate) {
}
