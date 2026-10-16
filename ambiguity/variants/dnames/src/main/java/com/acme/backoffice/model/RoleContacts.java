package com.acme.backoffice.model;

import java.util.List;

/** The contacts of the customers of a region that have one role. */
public record RoleContacts(String role, List<RegionContact> contacts) {
}
