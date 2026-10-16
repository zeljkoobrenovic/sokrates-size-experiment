package com.acme.backoffice.model;

import java.math.BigDecimal;

/** The amount spent on purchase orders for the products of a category (quantity times unit cost). */
public record CategorySpend(String category, BigDecimal spend) {
}
