package com.acme.backoffice.model;

/** A contact person of a customer. The id is 0 until the contact has been created in the database. The phone may be null. */
public record CustomerContact(long id, long customerId, String name, String role, String email, String phone) {
}
