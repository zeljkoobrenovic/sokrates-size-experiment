package com.acme.backoffice;

import com.acme.backoffice.model.CustomerContact;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RoleContacts;
import com.acme.backoffice.service.ContactService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClosedCustomerContactsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ContactService service = new ContactService(store);
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer gamma = store.createCustomer(new Customer(0, "Gamma", "g@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));

    ClosedCustomerContactsTest() {
        service.addContact(alpha.id(), "Ann Lee", "Buyer", "ann@alpha.example", null);
        service.addContact(beta.id(), "Bob Ray", "buyer", "bob@beta.example", null);
        service.addContact(beta.id(), "Cy Dunn", "Accountant", "cy@beta.example", null);
        service.addContact(gamma.id(), "Di Fox", "Buyer", "di@gamma.example", null);
        store.closeCustomer(beta.id(), LocalDate.of(2024, 3, 1));
        store.closeCustomer(gamma.id(), LocalDate.of(2024, 3, 2));
    }

    private static List<String> names(List<CustomerContact> contacts) {
        return contacts.stream().map(CustomerContact::name).toList();
    }

    @Test
    void contactsByRoleLeaveOutClosedCustomers() {
        assertEquals(List.of("Ann Lee"), names(service.findByRole("Buyer")));
        assertEquals(List.of(), names(service.findByRole("Accountant")));
    }

    @Test
    void regionContactsLeaveOutClosedCustomers() {
        List<RoleContacts> roles = service.listContactsPerRole(Region.WEST);
        assertEquals(List.of("Buyer"), roles.stream().map(RoleContacts::role).toList());
        assertEquals(List.of("Ann Lee"), roles.get(0).contacts().stream().map(c -> c.contact().name()).toList());
        assertEquals(List.of(), service.listContactsPerRole(Region.NORTH));
    }

    @Test
    void reopenedCustomerContactsComeBack() {
        store.reopenCustomer(beta.id());
        assertEquals(List.of("Ann Lee", "Bob Ray"), names(service.findByRole("BUYER")));
        List<RoleContacts> roles = service.listContactsPerRole(Region.WEST);
        assertEquals(List.of("Accountant", "Buyer"), roles.stream().map(RoleContacts::role).toList());
        assertEquals(List.of("Ann Lee", "Bob Ray"), roles.get(1).contacts().stream().map(c -> c.contact().name()).toList());
    }

    @Test
    void ownContactListOfAClosedCustomerIsUnchanged() {
        assertEquals(List.of("Bob Ray", "Cy Dunn"), names(service.listContacts(beta.id())));
    }
}
