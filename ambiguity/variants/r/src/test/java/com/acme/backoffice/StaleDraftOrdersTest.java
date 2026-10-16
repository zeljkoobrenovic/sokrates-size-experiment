package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.DraftOrder;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StaleDraftOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final LocalDate today = LocalDate.of(2024, 6, 30);

    private void order(String number, Customer customer, int ageDays, OrderStatus status) {
        Product widget = store.products().findBySku("W-1").orElseThrow();
        store.orders().create(new SalesOrder(0, number, customer.id(), today.minusDays(ageDays), status,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
    }

    private List<String> numbers(List<DraftOrder> list) {
        return list.stream().map(d -> d.order().number()).toList();
    }

    @BeforeEach
    void setUp() {
        SalesRep zed = store.salesReps().create(new SalesRep(0, "Zed", "z@example.com", Region.NORTH));
        SalesRep amy = store.salesReps().create(new SalesRep(0, "Amy", "a@example.com", Region.SOUTH));
        Customer nord = store.customers().create(new Customer(0, "Nord", "n@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer sud = store.customers().create(new Customer(0, "Sud", "s@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer free = store.customers().create(new Customer(0, "Aaa", "f@example.com", null, "Denmark", Region.NORTH, CustomerStatus.ACTIVE));
        store.customers().assignToSalesRep(nord.id(), zed.id());
        store.customers().assignToSalesRep(sud.id(), amy.id());
        store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        order("SO-1", nord, 50, OrderStatus.DRAFT);
        order("SO-2", nord, 40, OrderStatus.DRAFT);
        order("SO-3", sud, 45, OrderStatus.DRAFT);
        order("SO-4", free, 60, OrderStatus.DRAFT);
        order("SO-5", sud, 30, OrderStatus.DRAFT);
        order("SO-6", sud, 90, OrderStatus.CONFIRMED);
        order("SO-7", nord, 10, OrderStatus.DRAFT);
    }

    @Test
    void groupsPerCustomerOldestFirst() {
        List<DraftOrder> result = orders.getStaleDraftOrdersByCustomer(today, 30);
        assertEquals(List.of("SO-4", "SO-1", "SO-2", "SO-3"), numbers(result));
        assertEquals(50, result.get(1).ageDays());
        assertEquals("Nord", result.get(1).customer().name());
        assertEquals("Zed", result.get(1).salesRep().name());
        assertNull(result.get(0).salesRep());
        assertEquals(1, result.get(1).order().lines().size());
    }

    @Test
    void groupsPerSalesRepWithUnassignedLast() {
        assertEquals(List.of("SO-3", "SO-1", "SO-2", "SO-4"), numbers(orders.getStaleDraftOrdersBySalesRep(today, 30)));
    }

    @Test
    void orderExactlyNDaysOldIsNotOlder() {
        assertEquals(List.of("SO-4"), numbers(orders.getStaleDraftOrdersByCustomer(today, 50)));
        assertTrue(orders.getStaleDraftOrdersBySalesRep(today, 60).isEmpty());
    }

    @Test
    void rejectsBadArguments() {
        assertThrows(IllegalArgumentException.class, () -> orders.getStaleDraftOrdersByCustomer(null, 30));
        assertThrows(IllegalArgumentException.class, () -> orders.getStaleDraftOrdersBySalesRep(today, -1));
    }
}
