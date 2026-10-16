package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.LatestOrdersService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LatestOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final LatestOrdersService service = new LatestOrdersService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Customer north = store.customers().create(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.customers().create(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));

    private void order(String number, Customer customer, LocalDate date, OrderStatus status) {
        store.orders().create(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("10.00")))));
    }

    private List<String> numbers(List<SalesOrder> orders) {
        return orders.stream().map(SalesOrder::number).toList();
    }

    private void fill() {
        order("A", north, LocalDate.of(2024, 1, 1), OrderStatus.CONFIRMED);
        order("B", south, LocalDate.of(2024, 1, 5), OrderStatus.CONFIRMED);
        order("C", north, LocalDate.of(2024, 1, 3), OrderStatus.DELIVERED);
        order("D", south, LocalDate.of(2024, 1, 9), OrderStatus.CANCELLED);
        order("E", south, LocalDate.of(2024, 1, 5), OrderStatus.CONFIRMED);
    }

    @Test
    void listsLatestOrdersOverall() {
        fill();
        assertEquals(List.of("E", "B", "C"), numbers(service.overall(3)));
        assertEquals(4, service.overall(10).size());
        assertEquals(1, service.overall(1).get(0).lines().size());
    }

    @Test
    void listsLatestOrdersPerRegion() {
        fill();
        assertEquals(List.of("C", "A"), numbers(service.forRegion(Region.NORTH, 5)));
        assertEquals(List.of("E"), numbers(service.forRegion(Region.SOUTH, 1)));
        assertEquals(List.of(), numbers(service.forRegion(Region.EAST, 5)));
    }

    @Test
    void listsLatestOrdersPerCustomer() {
        fill();
        assertEquals(List.of("E", "B"), numbers(service.forCustomer(south.id(), 5)));
        assertEquals(List.of("C"), numbers(service.forCustomer(north.id(), 1)));
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.overall(0));
        assertThrows(IllegalArgumentException.class, () -> service.forRegion(null, 1));
        assertThrows(IllegalArgumentException.class, () -> service.forCustomer(9999, 1));
    }
}
