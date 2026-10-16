package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.DormantCustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DormantCustomerTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final DormantCustomerService dormant = new DormantCustomerService(store);
    private final LocalDate asOf = LocalDate.of(2025, 1, 1);
    private Product item;
    private int counter;

    private Customer customer(String name, Region region, CustomerStatus status) {
        return store.createCustomer(new Customer(0, name, name + "@example.com", null, "USA", region, status));
    }

    private void order(Customer customer, LocalDate date, OrderStatus status) {
        store.createSalesOrder(new SalesOrder(0, "SO-" + (++counter), customer.id(), date, status,
                List.of(new OrderLine(item.id(), 1, new BigDecimal("1.00")))));
    }

    private List<String> names(List<Customer> customers) {
        return customers.stream().map(Customer::name).toList();
    }

    @BeforeEach
    void setUp() {
        item = store.createProduct(new Product(0, "I-1", "Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        order(customer("RecentWest", Region.WEST, CustomerStatus.ACTIVE), asOf.minusDays(1), OrderStatus.CONFIRMED);
        order(customer("EdgeIn", Region.WEST, CustomerStatus.ACTIVE), asOf.minusDays(90), OrderStatus.CONFIRMED);
        order(customer("EdgeOut", Region.WEST, CustomerStatus.ACTIVE), asOf.minusDays(91), OrderStatus.CONFIRMED);
        order(customer("OnDate", Region.NORTH, CustomerStatus.ACTIVE), asOf, OrderStatus.CONFIRMED);
        order(customer("CancelledOnly", Region.NORTH, CustomerStatus.ACTIVE), asOf.minusDays(5), OrderStatus.CANCELLED);
        customer("Never", Region.EAST, CustomerStatus.ACTIVE);
        customer("Aaa", Region.EAST, CustomerStatus.ACTIVE);
        customer("Held", Region.SOUTH, CustomerStatus.ON_HOLD);
        customer("Closed", Region.SOUTH, CustomerStatus.CLOSED);
    }

    @Test
    void listsActiveCustomersWithoutRecentOrders() {
        assertEquals(List.of("Aaa", "Never", "CancelledOnly", "OnDate", "EdgeOut"),
                names(dormant.listDormant(asOf, 90)));
    }

    @Test
    void groupsPerRegion() {
        Map<Region, List<Customer>> perRegion = dormant.listDormantPerRegion(asOf, 90);
        assertEquals(List.of("CancelledOnly", "OnDate"), names(perRegion.get(Region.NORTH)));
        assertEquals(List.of("Aaa", "Never"), names(perRegion.get(Region.EAST)));
        assertEquals(List.of(), names(perRegion.get(Region.SOUTH)));
        assertEquals(List.of("EdgeOut"), names(perRegion.get(Region.WEST)));
    }
}
