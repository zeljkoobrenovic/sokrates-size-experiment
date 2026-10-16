package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Revenue, purchase cost of the units sold and margin of a product in non-cancelled orders in a period. */
public record ProductMargin(long productId, String sku, String name, BigDecimal revenue, BigDecimal cost, BigDecimal margin) {
}
