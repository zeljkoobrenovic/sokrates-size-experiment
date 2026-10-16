package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerSales;
import com.acme.backoffice.model.CustomerSegment;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.CustomerSegmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerSegmentTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CustomerSegmentService segments = new CustomerSegmentService(store);
    private final LocalDate asOf = LocalDate.of(2025, 1, 1);
    private Product item;
    private int counter;

    private Customer customer(String name) {
        return store.createCustomer(new Customer(0, name, name + "@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    }

    private void order(Customer customer, LocalDate date, OrderStatus status, int amount) {
        store.createSalesOrder(new SalesOrder(0, "SO-" + (++counter), customer.id(), date, status,
                List.of(new OrderLine(item.id(), amount, new BigDecimal("1.00")))));
    }

    @BeforeEach
    void setUp() {
        item = store.createProduct(new Product(0, "I-1", "Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Customer big = customer("Big");
        order(big, asOf.minusDays(365), OrderStatus.CONFIRMED, 50001);
        order(big, asOf.minusDays(366), OrderStatus.CONFIRMED, 99999);
        order(big, asOf, OrderStatus.CONFIRMED, 99999);
        Customer edgeHigh = customer("EdgeHigh");
        order(edgeHigh, asOf.minusDays(10), OrderStatus.SHIPPED, 50000);
        Customer edgeLow = customer("EdgeLow");
        order(edgeLow, asOf.minusDays(10), OrderStatus.CONFIRMED, 6000);
        order(edgeLow, asOf.minusDays(20), OrderStatus.CONFIRMED, 4000);
        Customer small = customer("Small");
        order(small, asOf.minusDays(10), OrderStatus.CONFIRMED, 9999);
        order(small, asOf.minusDays(5), OrderStatus.CANCELLED, 90000);
        customer("Idle");
    }

    @Test
    void classifiesByBoundaries() {
        assertEquals(CustomerSegment.A, CustomerSegmentService.segmentOf(new BigDecimal("50000.01")));
        assertEquals(CustomerSegment.B, CustomerSegmentService.segmentOf(new BigDecimal("50000")));
        assertEquals(CustomerSegment.B, CustomerSegmentService.segmentOf(new BigDecimal("10000")));
        assertEquals(CustomerSegment.C, CustomerSegmentService.segmentOf(new BigDecimal("9999.99")));
    }

    @Test
    void listsCustomersOfSegment() {
        assertEquals(List.of("Big"), segments.listCustomers(CustomerSegment.A, asOf).stream().map(CustomerSales::name).toList());
        assertEquals(List.of("EdgeHigh", "EdgeLow"), segments.listCustomers(CustomerSegment.B, asOf).stream().map(CustomerSales::name).toList());
        assertEquals(List.of("Small", "Idle"), segments.listCustomers(CustomerSegment.C, asOf).stream().map(CustomerSales::name).toList());
    }

    @Test
    void countsCustomersPerSegment() {
        Map<CustomerSegment, Integer> counts = segments.countPerSegment(asOf);
        assertEquals(1, counts.get(CustomerSegment.A));
        assertEquals(2, counts.get(CustomerSegment.B));
        assertEquals(2, counts.get(CustomerSegment.C));
    }
}
