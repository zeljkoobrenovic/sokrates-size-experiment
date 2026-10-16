package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RoleContacts;
import com.acme.backoffice.service.ContactService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactsPerRegionTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ContactService service = new ContactService(store);
    private final Customer zeta = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer other = store.createCustomer(new Customer(0, "Other", "o@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));

    @Test
    void groupsByRoleIgnoringCase() {
        service.addContact(zeta.id(), "Ann Lee", "Buyer", "ann@zeta.example", null);
        service.addContact(zeta.id(), "Bob Ray", "Accountant", "bob@zeta.example", null);
        service.addContact(alpha.id(), "Cy Dunn", "buyer", "cy@alpha.example", null);
        service.addContact(other.id(), "Di Fox", "Director", "di@other.example", null);

        List<RoleContacts> roles = service.listContactsPerRole(Region.WEST);
        assertEquals(2, roles.size());
        assertEquals("Accountant", roles.get(0).role());
        assertEquals(List.of("Bob Ray"), roles.get(0).contacts().stream().map(c -> c.contact().name()).toList());
        assertEquals(List.of("Cy Dunn", "Ann Lee"), roles.get(1).contacts().stream().map(c -> c.contact().name()).toList());
        assertEquals(List.of("Alpha", "Zeta"), roles.get(1).contacts().stream().map(c -> c.customerName()).toList());
    }

    @Test
    void emptyRegionAndInvalidInput() {
        assertTrue(service.listContactsPerRole(Region.SOUTH).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> service.listContactsPerRole(null));
    }
}
