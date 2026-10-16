package com.acme.backoffice.model;

/** An address of a customer. The id is 0 until the address has been created in the database. Only a shipping address can be the default. */
public record CustomerAddress(long id, long customerId, AddressType type, String street, String city, String postalCode,
                              String country, boolean defaultShipping) {
}
