package com.acme.backoffice.model;

/** The total quantity of a product ordered from a supplier. */
public record SupplierPurchasedQuantity(Supplier supplier, long quantity) {
}
