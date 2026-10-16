package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer west = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer north = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, Customer customer, LocalDate date, OrderStatus status) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
    }

    private void fill() {
        order("SO-1", west, LocalDate.of(2024, 3, 1), OrderStatus.CONFIRMED);
        order("SO-2", north, LocalDate.of(2024, 1, 1), OrderStatus.CONFIRMED);
        order("SO-3", west, LocalDate.of(2024, 2, 1), OrderStatus.CONFIRMED);
        order("SO-4", west, LocalDate.of(2023, 12, 1), OrderStatus.DRAFT);
        order("SO-5", west, LocalDate.of(2023, 12, 2), OrderStatus.SHIPPED);
        order("SO-6", north, LocalDate.of(2023, 12, 3), OrderStatus.CANCELLED);
        order("SO-7", north, LocalDate.of(2023, 12, 4), OrderStatus.DELIVERED);
    }

    private List<String> numbers(List<SalesOrder> orders) {
        return orders.stream().map(SalesOrder::number).toList();
    }

    @Test
    void allOpenOrdersOldestFirst() {
        fill();
        assertEquals(List.of("SO-2", "SO-3", "SO-1"), numbers(store.listOpenSalesOrders()));
        assertEquals(1, store.listOpenSalesOrders().get(0).lines().size());
    }

    @Test
    void openOrdersOfOneCustomer() {
        fill();
        assertEquals(List.of("SO-3", "SO-1"), numbers(store.listOpenSalesOrdersOfCustomer(west.id())));
        assertEquals(List.of("SO-2"), numbers(store.listOpenSalesOrdersOfCustomer(north.id())));
        assertTrue(store.listOpenSalesOrdersOfCustomer(north.id() + 100).isEmpty());
    }

    @Test
    void openOrdersByRegion() {
        fill();
        assertEquals(List.of("SO-3", "SO-1"), numbers(store.listOpenSalesOrdersInRegion(Region.WEST)));
        assertEquals(List.of("SO-2"), numbers(store.listOpenSalesOrdersInRegion(Region.NORTH)));
        assertTrue(store.listOpenSalesOrdersInRegion(Region.EAST).isEmpty());
    }

    @Test
    void nothingOpenWhenEmpty() {
        assertTrue(store.listOpenSalesOrders().isEmpty());
    }
}
