package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderHistoryEntry;
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

class OrderHistoryTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer anna = store.createCustomer(new Customer(0, "Anna", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer bob = store.createCustomer(new Customer(0, "Bob", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer carl = store.createCustomer(new Customer(0, "Carl", "c@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, Customer customer, LocalDate date, OrderStatus status, OrderLine... lines) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, status, List.of(lines)));
    }

    private OrderLine line(int quantity, String price) {
        return new OrderLine(widget.id(), quantity, new BigDecimal(price));
    }

    private List<String> numbers(List<OrderHistoryEntry> entries) {
        return entries.stream().map(OrderHistoryEntry::number).toList();
    }

    @Test
    void listsCustomerOrdersOldestFirstWithLinesAndTotal() {
        order("SO-2", anna, LocalDate.of(2024, 3, 1), OrderStatus.CONFIRMED, line(2, "5.00"), line(1, "1.50"));
        order("SO-1", anna, LocalDate.of(2024, 1, 1), OrderStatus.CANCELLED, line(3, "2.00"));
        order("SO-3", bob, LocalDate.of(2024, 2, 1), OrderStatus.DRAFT, line(1, "1.00"));

        List<OrderHistoryEntry> history = store.listOrderHistoryByCustomer(anna.id());
        assertEquals(List.of("SO-1", "SO-2"), numbers(history));
        assertEquals(OrderStatus.CANCELLED, history.get(0).status());
        assertEquals(LocalDate.of(2024, 1, 1), history.get(0).orderDate());
        assertEquals(1, history.get(0).lineCount());
        assertEquals(0, new BigDecimal("6.00").compareTo(history.get(0).total()));
        assertEquals(2, history.get(1).lineCount());
        assertEquals(0, new BigDecimal("11.50").compareTo(history.get(1).total()));
    }

    @Test
    void orderWithoutLinesHasZeroTotal() {
        order("SO-1", anna, LocalDate.of(2024, 1, 1), OrderStatus.DRAFT);
        OrderHistoryEntry entry = store.listOrderHistoryByCustomer(anna.id()).get(0);
        assertEquals(0, entry.lineCount());
        assertEquals(0, BigDecimal.ZERO.compareTo(entry.total()));
    }

    @Test
    void listsRegionOrdersOfAllItsCustomersOldestFirst() {
        order("SO-1", bob, LocalDate.of(2024, 2, 1), OrderStatus.CONFIRMED, line(1, "1.00"));
        order("SO-2", anna, LocalDate.of(2024, 1, 1), OrderStatus.SHIPPED, line(1, "1.00"));
        order("SO-3", carl, LocalDate.of(2024, 1, 15), OrderStatus.CONFIRMED, line(1, "1.00"));

        assertEquals(List.of("SO-2", "SO-1"), numbers(store.listOrderHistoryByRegion(Region.NORTH)));
        assertEquals(List.of("SO-3"), numbers(store.listOrderHistoryByRegion(Region.SOUTH)));
        assertTrue(store.listOrderHistoryByRegion(Region.EAST).isEmpty());
    }
}
