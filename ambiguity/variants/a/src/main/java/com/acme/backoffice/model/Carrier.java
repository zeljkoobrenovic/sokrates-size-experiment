package com.acme.backoffice.model;

import java.math.BigDecimal;

/**
 * A carrier with a unique name and the number of days within which it promises to deliver. The id is 0 until the carrier
 * has been created in the database.
 */
public record Carrier(long id, String name, ServiceLevel serviceLevel, BigDecimal pricePerKg, int promisedDays) {
    public static final int DEFAULT_PROMISED_DAYS = 7;

    public Carrier(long id, String name, ServiceLevel serviceLevel, BigDecimal pricePerKg) {
        this(id, name, serviceLevel, pricePerKg, DEFAULT_PROMISED_DAYS);
    }
}
