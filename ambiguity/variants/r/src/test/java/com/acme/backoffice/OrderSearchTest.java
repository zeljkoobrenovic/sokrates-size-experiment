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

class OrderSearchTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer west = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer south = store.customers().create(new Customer(0, "Beta", "b@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, Customer customer, LocalDate date, OrderStatus status) {
        store.orders().create(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
    }

    private void fill() {
        order("SO-1", west, LocalDate.of(2024, 3, 5), OrderStatus.CONFIRMED);
        order("SO-2", south, LocalDate.of(2024, 3, 1), OrderStatus.DELIVERED);
        order("SO-3", west, LocalDate.of(2024, 3, 10), OrderStatus.DELIVERED);
        order("SO-4", south, LocalDate.of(2024, 2, 29), OrderStatus.CONFIRMED);
        order("SO-5", south, LocalDate.of(2024, 3, 5), OrderStatus.CONFIRMED);
    }

    private List<String> numbers(List<SalesOrder> orders) {
        return orders.stream().map(SalesOrder::number).toList();
    }

    @Test
    void noCriteriaReturnsAllNewestFirst() {
        fill();
        List<SalesOrder> orders = store.orders().search(null, null, null, null, null);
        assertEquals(List.of("SO-3", "SO-5", "SO-1", "SO-2", "SO-4"), numbers(orders));
        assertEquals(1, orders.get(0).lines().size());
    }

    @Test
    void singleCriteria() {
        fill();
        assertEquals(List.of("SO-3", "SO-1"), numbers(store.orders().search(west.id(), null, null, null, null)));
        assertEquals(List.of("SO-3", "SO-2"), numbers(store.orders().search(null, OrderStatus.DELIVERED, null, null, null)));
        assertEquals(List.of("SO-5", "SO-2", "SO-4"), numbers(store.orders().search(null, null, Region.SOUTH, null, null)));
        assertEquals(List.of("SO-3", "SO-5", "SO-1"), numbers(store.orders().search(null, null, null, LocalDate.of(2024, 3, 5), null)));
        assertEquals(List.of("SO-2", "SO-4"), numbers(store.orders().search(null, null, null, null, LocalDate.of(2024, 3, 1))));
    }

    @Test
    void combinedCriteria() {
        fill();
        LocalDate from = LocalDate.of(2024, 3, 1);
        LocalDate to = LocalDate.of(2024, 3, 5);
        assertEquals(List.of("SO-5"), numbers(store.orders().search(south.id(), OrderStatus.CONFIRMED, Region.SOUTH, from, to)));
        assertEquals(List.of("SO-5", "SO-1", "SO-2"), numbers(store.orders().search(null, null, null, from, to)));
        assertEquals(List.of(), store.orders().search(west.id(), null, Region.SOUTH, null, null));
    }
}
