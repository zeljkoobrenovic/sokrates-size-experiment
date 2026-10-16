package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.LatestCustomersService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LatestCustomersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final LatestCustomersService service = new LatestCustomersService(store);

    private Customer customer(String name, Region region, LocalDate created) {
        Customer customer = store.createCustomer(new Customer(0, name, name + "@example.com", null, "UK", region, CustomerStatus.ACTIVE));
        if (created != null) {
            store.updateCustomerCreatedDate(customer.id(), created);
        }
        return customer;
    }

    private List<String> names(List<Customer> customers) {
        return customers.stream().map(Customer::name).toList();
    }

    @Test
    void listsNewestCustomersOverallAndPerRegion() {
        customer("A", Region.NORTH, LocalDate.of(2050, 1, 1));
        customer("B", Region.SOUTH, LocalDate.of(2050, 3, 1));
        customer("C", Region.NORTH, LocalDate.of(2050, 2, 1));
        customer("D", Region.NORTH, LocalDate.of(2050, 3, 1));
        customer("NoDate", Region.NORTH, null);
        Customer closed = customer("Closed", Region.NORTH, LocalDate.of(2050, 4, 1));
        store.closeCustomer(closed.id(), LocalDate.of(2050, 4, 2));

        assertEquals(List.of("D", "B", "C"), names(service.overall(3)));
        assertEquals(List.of("D", "C"), names(service.forRegion(Region.NORTH, 2)));
        assertEquals(List.of("B"), names(service.forRegion(Region.SOUTH, 5)));
        assertEquals(List.of(), names(service.forRegion(Region.EAST, 5)));
    }

    @Test
    void listsCustomersJoinedJustBeforeACustomer() {
        customer("A", Region.NORTH, LocalDate.of(2050, 1, 1));
        customer("B", Region.SOUTH, LocalDate.of(2050, 2, 1));
        Customer c = customer("C", Region.EAST, LocalDate.of(2050, 3, 1));
        customer("D", Region.WEST, LocalDate.of(2050, 4, 1));
        Customer noDate = customer("NoDate", Region.WEST, null);

        assertEquals(List.of("B", "A"), names(service.beforeCustomer(c.id(), 5)));
        assertEquals(List.of("B"), names(service.beforeCustomer(c.id(), 1)));
        assertEquals(List.of(), names(service.beforeCustomer(noDate.id(), 5)));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.overall(0));
        assertThrows(IllegalArgumentException.class, () -> service.forRegion(null, 1));
        assertThrows(IllegalArgumentException.class, () -> service.beforeCustomer(999999, 1));
    }
}
