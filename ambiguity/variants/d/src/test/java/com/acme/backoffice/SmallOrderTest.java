package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionSmallOrderShare;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SmallOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmallOrderTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer north = store.createCustomer(new Customer(0, "Nord", "n@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.createCustomer(new Customer(0, "Sud", "s@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product bolt = store.createProduct(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private int counter;

    private SalesOrder order(Customer customer, LocalDate date, OrderStatus status, int quantity) {
        counter++;
        return store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date, status,
                List.of(new OrderLine(bolt.id(), quantity, new BigDecimal("1.00")))));
    }

    @Test
    void listsOrdersBelowTheValueBetweenTwoDatesPerCustomer() {
        SalesOrder sud = order(south, LocalDate.of(2024, 3, 1), OrderStatus.CONFIRMED, 20);
        SalesOrder nordLate = order(north, LocalDate.of(2024, 3, 31), OrderStatus.DELIVERED, 99);
        SalesOrder nordEarly = order(north, LocalDate.of(2024, 3, 2), OrderStatus.CONFIRMED, 10);
        order(north, LocalDate.of(2024, 3, 5), OrderStatus.CONFIRMED, 100);
        order(north, LocalDate.of(2024, 3, 6), OrderStatus.CANCELLED, 5);
        order(north, LocalDate.of(2024, 2, 29), OrderStatus.CONFIRMED, 5);
        order(south, LocalDate.of(2024, 4, 1), OrderStatus.CONFIRMED, 5);

        List<SmallOrder> result = store.findSmallSalesOrders(new BigDecimal("100.00"), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals(List.of(nordEarly.number(), nordLate.number(), sud.number()),
                result.stream().map(r -> r.order().number()).toList());
        assertEquals(new BigDecimal("99.00"), result.get(1).value());
        assertEquals(north.id(), result.get(0).customer().id());
        assertEquals("Sud", result.get(2).customer().name());
        assertEquals(1, result.get(2).order().lines().size());
    }

    @Test
    void computesTheShareOfSmallOrdersPerRegion() {
        order(north, LocalDate.of(2024, 3, 1), OrderStatus.CONFIRMED, 10);
        order(north, LocalDate.of(2024, 3, 2), OrderStatus.CONFIRMED, 500);
        order(north, LocalDate.of(2024, 3, 3), OrderStatus.CONFIRMED, 600);
        order(north, LocalDate.of(2024, 3, 4), OrderStatus.CANCELLED, 5);
        order(south, LocalDate.of(2024, 3, 5), OrderStatus.CONFIRMED, 100);
        order(south, LocalDate.of(2024, 4, 5), OrderStatus.CONFIRMED, 5);

        List<RegionSmallOrderShare> result = store.findSmallSalesOrderSharePerRegion(new BigDecimal("100.00"), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals(2, result.size());
        assertEquals(Region.NORTH, result.get(0).region());
        assertEquals(1, result.get(0).smallOrders());
        assertEquals(3, result.get(0).totalOrders());
        assertEquals(new BigDecimal("33.33"), result.get(0).sharePercent());
        assertEquals(Region.SOUTH, result.get(1).region());
        assertEquals(new BigDecimal("0.00"), result.get(1).sharePercent());
    }

    @Test
    void returnsNothingWhenThereAreNoOrders() {
        assertTrue(store.findSmallSalesOrders(new BigDecimal("100.00"), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)).isEmpty());
        assertTrue(store.findSmallSalesOrderSharePerRegion(new BigDecimal("100.00"), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)).isEmpty());
    }
}
