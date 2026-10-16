package com.acme.backoffice.model;

import java.math.BigDecimal;

/** A product of the catalogue. The id is 0 until the product has been created in the database. */
public record Product(long id, String sku, String name, String category, BigDecimal unitPrice, String unitOfMeasure, boolean active) {
}
