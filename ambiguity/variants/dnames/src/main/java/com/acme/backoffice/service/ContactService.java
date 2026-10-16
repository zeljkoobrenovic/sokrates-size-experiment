package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CustomerContact;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionContact;
import com.acme.backoffice.model.RoleContacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Customer contacts: a customer has any number of contact persons; an email address belongs to one contact only. */
public class ContactService {
    private final DataStore store;

    public ContactService(DataStore store) {
        this.store = store;
    }

    /** Adds a contact to an existing customer. The phone is optional. */
    public CustomerContact addContact(long customerId, String name, String role, String email, String phone) {
        if (name == null || name.isBlank() || role == null || role.isBlank() || email == null || email.isBlank()) {
            throw new IllegalArgumentException("Name, role and email are required");
        }
        if (store.findCustomerById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer: " + customerId);
        }
        if (store.findCustomerContactByEmail(email.trim()).isPresent()) {
            throw new IllegalArgumentException("Email already used by a contact: " + email);
        }
        return store.createCustomerContact(new CustomerContact(0, customerId, name.trim(), role.trim(), email.trim(),
                phone == null || phone.isBlank() ? null : phone.trim()));
    }

    public List<CustomerContact> listContacts(long customerId) {
        return store.listContactsOfCustomer(customerId);
    }

    /** Contacts of all customers with the role, ignoring case. */
    public List<CustomerContact> findByRole(String role) {
        return store.listCustomerContactsWithRole(role == null ? "" : role.trim());
    }

    /** The contact with the email address, ignoring case. */
    public Optional<CustomerContact> findByEmail(String email) {
        return store.findCustomerContactByEmail(email == null ? "" : email.trim());
    }

    /** Contacts of the customers of the region per role, roles compared ignoring case and sorted alphabetically. */
    public List<RoleContacts> listContactsPerRole(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        List<RoleContacts> roles = new ArrayList<>();
        for (RegionContact contact : store.listCustomerContactsInRegion(region)) {
            RoleContacts last = roles.isEmpty() ? null : roles.get(roles.size() - 1);
            if (last == null || !last.role().equalsIgnoreCase(contact.contact().role())) {
                last = new RoleContacts(contact.contact().role(), new ArrayList<>());
                roles.add(last);
            }
            last.contacts().add(contact);
        }
        return roles;
    }
}
