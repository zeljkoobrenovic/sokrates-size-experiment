package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerTest {
    private final DataStore store = new DataStore(Database.openInMemory());

    @Test
    void createAndFindById() {
        Customer created = store.createCustomer(new Customer(0, "Acme", "a@acme.example", "123", "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        assertTrue(created.id() > 0);
        assertEquals(created, store.findCustomerById(created.id()).orElseThrow());
        assertTrue(store.findCustomerById(9999).isEmpty());
    }

    @Test
    void update() {
        Customer created = store.createCustomer(new Customer(0, "Acme", "a@acme.example", "123", "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer changed = new Customer(created.id(), "Acme Ltd", "b@acme.example", "456", "Italy", Region.WEST, CustomerStatus.CLOSED);
        store.updateCustomer(changed);
        assertEquals(changed, store.findCustomerById(created.id()).orElseThrow());
    }

    @Test
    void listSortedByName() {
        store.createCustomer(new Customer(0, "Charlie", "c@x.example", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.createCustomer(new Customer(0, "Alice", "a@x.example", null, "UK", Region.EAST, CustomerStatus.ON_HOLD));
        store.createCustomer(new Customer(0, "Bob", "b@x.example", null, "UK", Region.WEST, CustomerStatus.ACTIVE));
        List<String> names = store.findNotClosedCustomersByName().stream().map(Customer::name).toList();
        assertEquals(List.of("Alice", "Bob", "Charlie"), names);
    }

    @Test
    void search() {
        store.createCustomer(new Customer(0, "Charlie Trading", "c@x.example", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.createCustomer(new Customer(0, "Alice TRADING", "a@x.example", null, "Spain", Region.EAST, CustomerStatus.ON_HOLD));
        store.createCustomer(new Customer(0, "Bob 100%", "b@x.example", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        assertEquals(List.of("Alice TRADING", "Charlie Trading"), store.findNotClosedCustomersWithNameContaining("TRADING").stream().map(Customer::name).toList());
        assertEquals(List.of("Bob 100%"), store.findNotClosedCustomersWithNameContaining("0%").stream().map(Customer::name).toList());
        assertTrue(store.findNotClosedCustomersWithNameContaining("%x").isEmpty());
        assertEquals(List.of("Bob 100%", "Charlie Trading"), store.findNotClosedCustomersInRegion(Region.NORTH).stream().map(Customer::name).toList());
        assertEquals(List.of("Bob 100%", "Charlie Trading"), store.findNotClosedCustomersInCountry("UK").stream().map(Customer::name).toList());
        assertEquals(List.of("Alice TRADING"), store.findNotClosedCustomersWithStatus(CustomerStatus.ON_HOLD).stream().map(Customer::name).toList());
        assertTrue(store.findNotClosedCustomersWithStatus(CustomerStatus.CLOSED).isEmpty());
    }
}
