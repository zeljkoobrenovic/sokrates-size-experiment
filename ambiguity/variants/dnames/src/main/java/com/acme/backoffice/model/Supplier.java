package com.acme.backoffice.model;

/** A supplier with a rating from 1 to 5. The id is 0 until the supplier has been created in the database. */
public record Supplier(long id, String name, String contactEmail, String country, int rating) {
}
