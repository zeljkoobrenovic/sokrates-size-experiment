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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalesOrderTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));

    private SalesOrder order(String number, LocalDate date) {
        return new SalesOrder(0, number, customer.id(), date, OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")), new OrderLine(gadget.id(), 1, new BigDecimal("30.00"))));
    }

    @Test
    void createAndFindWithLines() {
        SalesOrder created = store.orders().create(order("SO-1", LocalDate.of(2024, 1, 5)));
        assertTrue(created.id() > 0);
        assertEquals(created, store.orders().findByNumber("SO-1").orElseThrow());
        assertEquals(2, created.lines().size());
        assertTrue(store.orders().findByNumber("SO-2").isEmpty());
    }

    @Test
    void listForCustomerNewestFirst() {
        store.orders().create(order("SO-1", LocalDate.of(2024, 1, 5)));
        store.orders().create(order("SO-2", LocalDate.of(2024, 3, 1)));
        store.orders().create(order("SO-3", LocalDate.of(2024, 2, 1)));
        assertEquals(List.of("SO-2", "SO-3", "SO-1"),
                store.orders().listOfCustomer(customer.id()).stream().map(SalesOrder::number).toList());
        assertTrue(store.orders().listOfCustomer(customer.id() + 1).isEmpty());
    }

    @Test
    void failedCreateLeavesNoOrder() {
        SalesOrder bad = new SalesOrder(0, "SO-1", customer.id(), LocalDate.of(2024, 1, 5), OrderStatus.DRAFT,
                List.of(new OrderLine(999, 1, BigDecimal.ONE)));
        assertThrows(IllegalStateException.class, () -> store.orders().create(bad));
        assertTrue(store.orders().findByNumber("SO-1").isEmpty());
    }

    @Test
    void numberMustBeUnique() {
        store.orders().create(order("SO-1", LocalDate.of(2024, 1, 5)));
        assertThrows(IllegalStateException.class, () -> store.orders().create(order("SO-1", LocalDate.of(2024, 1, 6))));
    }
}
