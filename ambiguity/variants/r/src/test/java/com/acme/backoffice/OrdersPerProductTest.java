package com.acme.backoffice;

import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductOrder;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersPerProductTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);
    private Product widget;
    private Product gadget;

    private void order(String number, Customer customer, int offset, Product... products) {
        List<OrderLine> lines = new ArrayList<>();
        for (Product product : products) {
            lines.add(new OrderLine(product.id(), 2, product.unitPrice()));
        }
        store.orders().create(new SalesOrder(0, number, customer.id(), day.plusDays(offset), OrderStatus.CONFIRMED, lines));
    }

    private List<String> numbers(List<ProductOrder> list) {
        return list.stream().map(p -> p.order().number()).toList();
    }

    @BeforeEach
    void setUp() {
        Customer nord = store.customers().create(new Customer(0, "Nord", "n@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer aarhus = store.customers().create(new Customer(0, "Aarhus", "a@example.com", null, "Denmark", Region.NORTH, CustomerStatus.ACTIVE));
        widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("9.00"), "piece", true));
        order("SO-1", nord, 1, widget);
        order("SO-2", aarhus, 2, widget, gadget);
        order("SO-3", nord, 3, widget);
        order("SO-4", aarhus, 4, gadget);
        order("SO-5", nord, 5, widget);
        order("SO-6", aarhus, 40, widget);
        orders.cancel("SO-5", CancellationReason.OTHER, day.plusDays(6));
    }

    @Test
    void listsNewestFirstWithoutCancelledOrOtherProducts() {
        List<ProductOrder> result = orders.getOrdersByProduct(widget.id(), day, day.plusDays(10));
        assertEquals(List.of("SO-3", "SO-2", "SO-1"), numbers(result));
        assertEquals("Nord", result.get(0).customer().name());
        assertEquals(2, result.get(1).order().lines().size());
    }

    @Test
    void groupsPerCustomerNewestFirst() {
        List<ProductOrder> result = orders.getOrdersByProductPerCustomer(widget.id(), day, day.plusDays(10));
        assertEquals(List.of("SO-2", "SO-3", "SO-1"), numbers(result));
    }

    @Test
    void periodIsInclusive() {
        assertEquals(List.of("SO-2", "SO-1"), numbers(orders.getOrdersByProduct(widget.id(), day.plusDays(1), day.plusDays(2))));
        assertTrue(orders.getOrdersByProductPerCustomer(widget.id(), day.plusDays(10), day.plusDays(20)).isEmpty());
    }

    @Test
    void rejectsBadPeriod() {
        assertThrows(IllegalArgumentException.class, () -> orders.getOrdersByProduct(widget.id(), null, day));
        assertThrows(IllegalArgumentException.class, () -> orders.getOrdersByProductPerCustomer(widget.id(), day.plusDays(1), day));
    }
}
