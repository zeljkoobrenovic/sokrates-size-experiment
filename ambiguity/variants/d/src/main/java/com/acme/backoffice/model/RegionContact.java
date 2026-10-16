package com.acme.backoffice.model;

/** A contact person of a customer, with the name of the customer. */
public record RegionContact(String customerName, CustomerContact contact) {
}
