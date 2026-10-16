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

class OrderTotalsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer west = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer north = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));

    private void order(String number, Customer customer, OrderStatus status, OrderLine... lines) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), LocalDate.of(2024, 3, 1), status, List.of(lines)));
    }

    private void fill() {
        order("SO-1", west, OrderStatus.CONFIRMED, new OrderLine(widget.id(), 10, new BigDecimal("4.75")),
                new OrderLine(gadget.id(), 2, new BigDecimal("30.00")));
        order("SO-2", west, OrderStatus.DELIVERED, new OrderLine(widget.id(), 3, new BigDecimal("4.50")));
        order("SO-3", west, OrderStatus.CANCELLED, new OrderLine(gadget.id(), 5, new BigDecimal("30.00")));
        order("SO-4", north, OrderStatus.CANCELLED, new OrderLine(gadget.id(), 1, new BigDecimal("30.00")));
    }

    @Test
    void orderTotalSumsQuantityTimesPrice() {
        fill();
        assertEquals(0, new BigDecimal("107.50").compareTo(store.sumSalesOrderValueByNumber("SO-1")));
        assertEquals(0, new BigDecimal("13.50").compareTo(store.sumSalesOrderValueByNumber("SO-2")));
    }

    @Test
    void unknownOrderOrOrderWithoutLinesIsZero() {
        fill();
        order("SO-5", north, OrderStatus.DRAFT);
        assertEquals(0, BigDecimal.ZERO.compareTo(store.sumSalesOrderValueByNumber("SO-NONE")));
        assertEquals(0, BigDecimal.ZERO.compareTo(store.sumSalesOrderValueByNumber("SO-5")));
    }

    @Test
    void customerTotalIgnoresCancelledOrders() {
        fill();
        assertEquals(0, new BigDecimal("121.00").compareTo(store.sumNonCancelledSalesOrderValueOfCustomer(west.id())));
    }

    @Test
    void customerWithOnlyCancelledOrdersOrNoneIsZero() {
        fill();
        assertEquals(0, BigDecimal.ZERO.compareTo(store.sumNonCancelledSalesOrderValueOfCustomer(north.id())));
        assertEquals(0, BigDecimal.ZERO.compareTo(store.sumNonCancelledSalesOrderValueOfCustomer(north.id() + 100)));
    }
}
