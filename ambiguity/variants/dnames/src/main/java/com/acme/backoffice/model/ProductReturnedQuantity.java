package com.acme.backoffice.model;

/** The total returned quantity of a product. */
public record ProductReturnedQuantity(long productId, String sku, String productName, long quantity) {
}
