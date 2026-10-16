package com.acme.backoffice.model;

/** A product that is part of a bundle, with the quantity needed for one bundle. */
public record BundleComponent(Product component, int quantity) {
}
