package com.acme.backoffice.model;

import java.math.BigDecimal;

/** Units sold and revenue of a product in non-cancelled orders in a period. */
public record ProductSales(long productId, String sku, String name, long unitsSold, BigDecimal revenue) {
}
