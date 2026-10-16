package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerContact;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.ContactService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerContactTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ContactService service = new ContactService(store);
    private final Customer zeta = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer other = store.customers().create(new Customer(0, "Other", "o@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));

    @Test
    void addAndList() {
        CustomerContact ann = service.addContact(zeta.id(), "Ann Lee", "Buyer", "ann@zeta.example", "+1 555 0111");
        CustomerContact bob = service.addContact(zeta.id(), "Bob Ray", "Accountant", "bob@zeta.example", " ");
        assertTrue(ann.id() > 0);
        assertNull(bob.phone());
        assertEquals(List.of(ann, bob), service.listContacts(zeta.id()));
        assertTrue(service.listContacts(other.id()).isEmpty());
    }

    @Test
    void findByRoleAcrossCustomers() {
        CustomerContact ann = service.addContact(zeta.id(), "Ann Lee", "Buyer", "ann@zeta.example", null);
        service.addContact(zeta.id(), "Bob Ray", "Accountant", "bob@zeta.example", null);
        CustomerContact cy = service.addContact(other.id(), "Cy Dunn", "buyer", "cy@other.example", null);
        assertEquals(List.of(ann, cy), service.findByRole("BUYER"));
        assertTrue(service.findByRole("Director").isEmpty());
    }

    @Test
    void findByEmail() {
        CustomerContact ann = service.addContact(zeta.id(), "Ann Lee", "Buyer", "ann@zeta.example", null);
        assertEquals(ann, service.findByEmail("ANN@zeta.example").orElseThrow());
        assertTrue(service.findByEmail("nobody@zeta.example").isEmpty());
    }

    @Test
    void rejectsInvalidInput() {
        service.addContact(zeta.id(), "Ann Lee", "Buyer", "ann@zeta.example", null);
        assertThrows(IllegalArgumentException.class, () -> service.addContact(999, "X", "Buyer", "x@x.example", null));
        assertThrows(IllegalArgumentException.class, () -> service.addContact(zeta.id(), " ", "Buyer", "x@x.example", null));
        assertThrows(IllegalArgumentException.class, () -> service.addContact(zeta.id(), "X", null, "x@x.example", null));
        assertThrows(IllegalArgumentException.class, () -> service.addContact(zeta.id(), "X", "Buyer", "", null));
        assertThrows(IllegalArgumentException.class, () -> service.addContact(other.id(), "Ann Two", "Buyer", "Ann@Zeta.example", null));
    }
}
