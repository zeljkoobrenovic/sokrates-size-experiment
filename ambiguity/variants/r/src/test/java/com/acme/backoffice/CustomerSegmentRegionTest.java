package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerRegionSales;
import com.acme.backoffice.model.CustomerSegment;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SegmentRegionCustomers;
import com.acme.backoffice.service.CustomerClosureService;
import com.acme.backoffice.service.CustomerSegmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerSegmentRegionTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CustomerSegmentService segments = new CustomerSegmentService(store);
    private final LocalDate asOf = LocalDate.of(2025, 1, 1);
    private Product item;
    private Customer bigNorth;
    private int counter;

    private Customer customer(String name, Region region) {
        return store.customers().create(new Customer(0, name, name + "@example.com", null, "X", region, CustomerStatus.ACTIVE));
    }

    private void order(Customer customer, LocalDate date, OrderStatus status, int amount) {
        store.orders().create(new SalesOrder(0, "SO-" + (++counter), customer.id(), date, status,
                List.of(new OrderLine(item.id(), amount, new BigDecimal("1.00")))));
    }

    private String describe(SegmentRegionCustomers group) {
        return group.segment() + ":" + group.region() + ":" + group.customerCount() + ":"
                + group.customers().stream().map(CustomerRegionSales::name).toList();
    }

    @BeforeEach
    void setUp() {
        item = store.products().create(new Product(0, "I-1", "Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        bigNorth = customer("BigNorth", Region.NORTH);
        order(bigNorth, asOf.minusDays(10), OrderStatus.CONFIRMED, 60000);
        order(customer("BigSouth", Region.SOUTH), asOf.minusDays(10), OrderStatus.CONFIRMED, 70000);
        order(customer("MidSouth", Region.SOUTH), asOf.minusDays(10), OrderStatus.SHIPPED, 20000);
        order(customer("MidSouth2", Region.SOUTH), asOf.minusDays(10), OrderStatus.SHIPPED, 30000);
        order(customer("Late", Region.EAST), asOf, OrderStatus.CONFIRMED, 90000);
        order(customer("Cancelled", Region.EAST), asOf.minusDays(5), OrderStatus.CANCELLED, 90000);
        customer("IdleNorth", Region.NORTH);
    }

    @Test
    void groupsCustomersBySegmentAndRegion() {
        assertEquals(List.of(
                "A:NORTH:1:[BigNorth]",
                "A:SOUTH:1:[BigSouth]",
                "B:SOUTH:2:[MidSouth2, MidSouth]",
                "C:NORTH:1:[IdleNorth]",
                "C:EAST:2:[Cancelled, Late]"),
                segments.listPerSegmentAndRegion(asOf).stream().map(this::describe).toList());
    }

    @Test
    void closedCustomersAreLeftOut() {
        new CustomerClosureService(store).close(bigNorth.id(), asOf.minusDays(1));

        List<SegmentRegionCustomers> groups = segments.listPerSegmentAndRegion(asOf);
        assertTrue(groups.stream().noneMatch(g -> g.segment() == CustomerSegment.A && g.region() == Region.NORTH));
    }

    @Test
    void requiresADate() {
        assertThrows(IllegalArgumentException.class, () -> segments.listPerSegmentAndRegion(null));
    }
}
