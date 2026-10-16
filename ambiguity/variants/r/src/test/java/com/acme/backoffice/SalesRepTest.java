package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesRep;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalesRepTest {
    private final DataStore store = new DataStore(Database.openInMemory());

    @Test
    void listRepsAndRepsOfRegion() {
        SalesRep carl = store.salesReps().create(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
        store.salesReps().create(new SalesRep(0, "Bea", "bea@example.com", Region.NORTH));
        store.salesReps().create(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
        assertTrue(carl.id() > 0);
        assertEquals(List.of("Anna", "Bea", "Carl"), store.salesReps().listByName().stream().map(SalesRep::name).toList());
        assertEquals(List.of("Anna", "Bea"), store.salesReps().listByRegion(Region.NORTH).stream().map(SalesRep::name).toList());
        assertEquals(List.of(carl), store.salesReps().listByRegion(Region.WEST));
        assertTrue(store.salesReps().listByRegion(Region.EAST).isEmpty());
    }

    @Test
    void customersOfRep() {
        SalesRep anna = store.salesReps().create(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
        SalesRep carl = store.salesReps().create(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
        Customer zeta = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.customers().create(new Customer(0, "Unassigned", "u@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.customers().assignToSalesRep(zeta.id(), anna.id());
        store.customers().assignToSalesRep(alpha.id(), anna.id());
        assertEquals(List.of(alpha, zeta), store.customers().listOfSalesRep(anna.id()));
        assertTrue(store.customers().listOfSalesRep(carl.id()).isEmpty());

        store.customers().assignToSalesRep(zeta.id(), carl.id());
        assertEquals(List.of(alpha), store.customers().listOfSalesRep(anna.id()));
        assertEquals(List.of(zeta), store.customers().listOfSalesRep(carl.id()));
        assertTrue(store.customers().listOfSalesRep(999).isEmpty());
    }
}
