package com.acme.backoffice.model;

/** An invoice together with the region of its customer. */
public record RegionLargeInvoice(Region region, Invoice invoice) {
}
