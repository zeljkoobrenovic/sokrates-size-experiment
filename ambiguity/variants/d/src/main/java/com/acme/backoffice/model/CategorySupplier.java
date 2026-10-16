package com.acme.backoffice.model;

/** A category whose products are delivered by this one supplier only. */
public record CategorySupplier(String category, Supplier supplier) {
}
