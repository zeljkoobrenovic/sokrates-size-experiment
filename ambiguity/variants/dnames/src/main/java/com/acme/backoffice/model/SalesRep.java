package com.acme.backoffice.model;

/** A sales rep responsible for a region. The id is 0 until the sales rep has been created in the database. */
public record SalesRep(long id, String name, String email, Region region) {
}
