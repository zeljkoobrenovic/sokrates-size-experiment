package com.acme.backoffice.model;

/** Units of a product that came back in sales returns in a period. */
public record ProductReturns(long productId, String sku, String name, long unitsReturned) {
}
