package com.acme.backoffice.model;

/** A warehouse with a unique code. The id is 0 until the warehouse has been created in the database. */
public record Warehouse(long id, String code, String name, String city, Region region) {
}
