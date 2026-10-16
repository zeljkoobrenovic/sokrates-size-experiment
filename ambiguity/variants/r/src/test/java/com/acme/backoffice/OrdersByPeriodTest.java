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

class OrdersByPeriodTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, LocalDate date, OrderStatus status) {
        store.orders().create(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
    }

    private void fill() {
        order("SO-1", LocalDate.of(2024, 3, 5), OrderStatus.CONFIRMED);
        order("SO-2", LocalDate.of(2024, 3, 1), OrderStatus.DELIVERED);
        order("SO-3", LocalDate.of(2024, 3, 10), OrderStatus.CONFIRMED);
        order("SO-4", LocalDate.of(2024, 2, 29), OrderStatus.CONFIRMED);
        order("SO-5", LocalDate.of(2024, 3, 11), OrderStatus.CONFIRMED);
    }

    private List<String> numbers(List<SalesOrder> orders) {
        return orders.stream().map(SalesOrder::number).toList();
    }

    @Test
    void periodIsInclusiveAndSortedByDate() {
        fill();
        List<SalesOrder> orders = store.orders().listByPeriod(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 10));
        assertEquals(List.of("SO-2", "SO-1", "SO-3"), numbers(orders));
        assertEquals(1, orders.get(0).lines().size());
    }

    @Test
    void periodWithNoOrdersIsEmpty() {
        fill();
        assertEquals(List.of(), store.orders().listByPeriod(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31)));
    }

    @Test
    void periodAndStatusFiltersByStatus() {
        fill();
        LocalDate from = LocalDate.of(2024, 3, 1);
        LocalDate to = LocalDate.of(2024, 3, 10);
        assertEquals(List.of("SO-1", "SO-3"), numbers(store.orders().listByPeriodAndStatus(from, to, OrderStatus.CONFIRMED)));
        assertEquals(List.of("SO-2"), numbers(store.orders().listByPeriodAndStatus(from, to, OrderStatus.DELIVERED)));
        assertEquals(List.of(), store.orders().listByPeriodAndStatus(from, to, OrderStatus.CANCELLED));
    }
}
