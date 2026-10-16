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
        Customer created = store.customers().create(new Customer(0, "Acme", "a@acme.example", "123", "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        assertTrue(created.id() > 0);
        assertEquals(created, store.customers().findById(created.id()).orElseThrow());
        assertTrue(store.customers().findById(9999).isEmpty());
    }

    @Test
    void update() {
        Customer created = store.customers().create(new Customer(0, "Acme", "a@acme.example", "123", "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer changed = new Customer(created.id(), "Acme Ltd", "b@acme.example", "456", "Italy", Region.WEST, CustomerStatus.CLOSED);
        store.customers().update(changed);
        assertEquals(changed, store.customers().findById(created.id()).orElseThrow());
    }

    @Test
    void listSortedByName() {
        store.customers().create(new Customer(0, "Charlie", "c@x.example", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.customers().create(new Customer(0, "Alice", "a@x.example", null, "UK", Region.EAST, CustomerStatus.ON_HOLD));
        store.customers().create(new Customer(0, "Bob", "b@x.example", null, "UK", Region.WEST, CustomerStatus.ACTIVE));
        List<String> names = store.customers().listOpen().stream().map(Customer::name).toList();
        assertEquals(List.of("Alice", "Bob", "Charlie"), names);
    }

    @Test
    void search() {
        store.customers().create(new Customer(0, "Charlie Trading", "c@x.example", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.customers().create(new Customer(0, "Alice TRADING", "a@x.example", null, "Spain", Region.EAST, CustomerStatus.ON_HOLD));
        store.customers().create(new Customer(0, "Bob 100%", "b@x.example", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        assertEquals(List.of("Alice TRADING", "Charlie Trading"), store.customers().searchByName("TRADING").stream().map(Customer::name).toList());
        assertEquals(List.of("Bob 100%"), store.customers().searchByName("0%").stream().map(Customer::name).toList());
        assertTrue(store.customers().searchByName("%x").isEmpty());
        assertEquals(List.of("Bob 100%", "Charlie Trading"), store.customers().listInRegion(Region.NORTH).stream().map(Customer::name).toList());
        assertEquals(List.of("Bob 100%", "Charlie Trading"), store.customers().listInCountry("UK").stream().map(Customer::name).toList());
        assertEquals(List.of("Alice TRADING"), store.customers().listWithStatus(CustomerStatus.ON_HOLD).stream().map(Customer::name).toList());
        assertTrue(store.customers().listWithStatus(CustomerStatus.CLOSED).isEmpty());
    }
}
