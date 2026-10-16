package com.acme.backoffice.model;

/** A customer of the company. The id is 0 until the customer has been created in the database. */
public record Customer(long id, String name, String email, String phone, String country, Region region, CustomerStatus status) {
}
