package com.acme.backoffice;

import com.acme.backoffice.model.AddressType;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerAddress;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.AddressService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerAddressTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final AddressService service = new AddressService(store);
    private final Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));

    @Test
    void addUpdateAndList() {
        CustomerAddress billing = service.addAddress(customer.id(), AddressType.BILLING, "1 Main St", "Austin", "73301", "USA");
        CustomerAddress shipping = service.addAddress(customer.id(), AddressType.SHIPPING, "9 Dock Rd", "Houston", "77001", "USA");
        assertTrue(billing.id() > 0);
        assertEquals(List.of(billing, shipping), service.listAddresses(customer.id()));

        service.updateAddress(billing.id(), AddressType.BILLING, "2 Main St", "Dallas", "75001", "USA");
        CustomerAddress updated = store.customerContacts().findAddressById(billing.id()).orElseThrow();
        assertEquals("2 Main St", updated.street());
        assertEquals("Dallas", updated.city());
        assertEquals(List.of(updated, shipping), service.listAddresses(customer.id()));
        assertTrue(service.listAddresses(999).isEmpty());
    }

    @Test
    void defaultShippingAddressIsUnique() {
        CustomerAddress billing = service.addAddress(customer.id(), AddressType.BILLING, "1 Main St", "Austin", "73301", "USA");
        CustomerAddress dock = service.addAddress(customer.id(), AddressType.SHIPPING, "9 Dock Rd", "Houston", "77001", "USA");
        CustomerAddress depot = service.addAddress(customer.id(), AddressType.SHIPPING, "4 Depot Ln", "Dallas", "75001", "USA");
        Customer other = store.customers().create(new Customer(0, "Other", "o@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        CustomerAddress otherDock = service.addAddress(other.id(), AddressType.SHIPPING, "1 Quay", "Leeds", "LS1", "UK");
        service.setDefaultShippingAddress(otherDock.id());

        service.setDefaultShippingAddress(dock.id());
        service.setDefaultShippingAddress(depot.id());
        assertEquals(List.of(false, false, true),
                service.listAddresses(customer.id()).stream().map(CustomerAddress::defaultShipping).toList());
        assertTrue(store.customerContacts().findAddressById(otherDock.id()).orElseThrow().defaultShipping());

        assertThrows(IllegalStateException.class, () -> service.setDefaultShippingAddress(billing.id()));
        assertThrows(IllegalStateException.class,
                () -> service.updateAddress(depot.id(), AddressType.BILLING, "4 Depot Ln", "Dallas", "75001", "USA"));
        service.updateAddress(dock.id(), AddressType.BILLING, "9 Dock Rd", "Houston", "77001", "USA");
        assertEquals(AddressType.BILLING, store.customerContacts().findAddressById(dock.id()).orElseThrow().type());
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.addAddress(999, AddressType.BILLING, "1 Main St", "Austin", "73301", "USA"));
        assertThrows(IllegalArgumentException.class, () -> service.addAddress(customer.id(), AddressType.BILLING, " ", "Austin", "73301", "USA"));
        assertThrows(IllegalArgumentException.class, () -> service.addAddress(customer.id(), null, "1 Main St", "Austin", "73301", "USA"));
        assertThrows(IllegalArgumentException.class, () -> service.setDefaultShippingAddress(999));
        assertThrows(IllegalArgumentException.class, () -> service.updateAddress(999, AddressType.BILLING, "1 Main St", "Austin", "73301", "USA"));
    }
}
