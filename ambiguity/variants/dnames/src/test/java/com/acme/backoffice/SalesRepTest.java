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
        SalesRep carl = store.createSalesRep(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
        store.createSalesRep(new SalesRep(0, "Bea", "bea@example.com", Region.NORTH));
        store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
        assertTrue(carl.id() > 0);
        assertEquals(List.of("Anna", "Bea", "Carl"), store.listSalesRepsOrderedByName().stream().map(SalesRep::name).toList());
        assertEquals(List.of("Anna", "Bea"), store.listSalesRepsInRegion(Region.NORTH).stream().map(SalesRep::name).toList());
        assertEquals(List.of(carl), store.listSalesRepsInRegion(Region.WEST));
        assertTrue(store.listSalesRepsInRegion(Region.EAST).isEmpty());
    }

    @Test
    void customersOfRep() {
        SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
        SalesRep carl = store.createSalesRep(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
        Customer zeta = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.createCustomer(new Customer(0, "Unassigned", "u@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(zeta.id(), anna.id());
        store.assignCustomerToSalesRep(alpha.id(), anna.id());
        assertEquals(List.of(alpha, zeta), store.listNotClosedCustomersOfSalesRep(anna.id()));
        assertTrue(store.listNotClosedCustomersOfSalesRep(carl.id()).isEmpty());

        store.assignCustomerToSalesRep(zeta.id(), carl.id());
        assertEquals(List.of(alpha), store.listNotClosedCustomersOfSalesRep(anna.id()));
        assertEquals(List.of(zeta), store.listNotClosedCustomersOfSalesRep(carl.id()));
        assertTrue(store.listNotClosedCustomersOfSalesRep(999).isEmpty());
    }
}
