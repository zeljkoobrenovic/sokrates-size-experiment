package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesRepCustomerCount;
import com.acme.backoffice.service.SalesRepCustomerCountService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalesRepCustomerCountTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRepCustomerCountService service = new SalesRepCustomerCountService(store);
    private int counter;

    private SalesRep rep(String name) {
        return store.createSalesRep(new SalesRep(0, name, name + "@example.com", Region.NORTH));
    }

    private void customer(SalesRep rep, LocalDate created) {
        counter++;
        Customer customer = store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        if (created != null) {
            store.updateCustomerCreatedDate(customer.id(), created);
        }
        if (rep != null) {
            store.assignCustomerToSalesRep(customer.id(), rep.id());
        }
    }

    private String describe(SalesRepCustomerCount count) {
        return count.name() + ":" + count.customerCount();
    }

    @Test
    void countsCustomersPerRepHighestFirst() {
        SalesRep anna = rep("Anna");
        SalesRep bob = rep("Bob");
        SalesRep carl = rep("Carl");
        SalesRep dora = rep("Dora");
        customer(anna, LocalDate.of(2024, 1, 1));
        customer(bob, LocalDate.of(2024, 1, 15));
        customer(bob, LocalDate.of(2024, 1, 31));
        customer(carl, LocalDate.of(2024, 1, 10));
        customer(bob, LocalDate.of(2023, 12, 31));
        customer(bob, LocalDate.of(2024, 2, 1));
        customer(dora, null);
        customer(null, LocalDate.of(2024, 1, 5));

        assertEquals(List.of("Bob:2", "Anna:1", "Carl:1", "Dora:0"),
                service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)).stream().map(this::describe).toList());
    }

    @Test
    void noRepsGivesEmptyReport() {
        assertEquals(List.of(), service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.report(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.report(null, LocalDate.of(2024, 1, 1)));
    }
}
