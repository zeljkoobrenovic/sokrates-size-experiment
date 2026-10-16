package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.LargeOrder;
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

class LargeOrderTest {
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
    void listsOrdersAboveTheValueBetweenTwoDatesPerRegionBiggestFirst() {
        SalesOrder smallNorth = order(north, LocalDate.of(2024, 3, 1), OrderStatus.CONFIRMED, 150);
        SalesOrder bigNorth = order(north, LocalDate.of(2024, 3, 31), OrderStatus.DELIVERED, 500);
        SalesOrder bigSouth = order(south, LocalDate.of(2024, 3, 10), OrderStatus.CONFIRMED, 300);
        SalesOrder midSouth = order(south, LocalDate.of(2024, 3, 11), OrderStatus.CONFIRMED, 200);
        order(north, LocalDate.of(2024, 3, 5), OrderStatus.CONFIRMED, 100);
        order(north, LocalDate.of(2024, 3, 6), OrderStatus.CANCELLED, 900);
        order(north, LocalDate.of(2024, 2, 29), OrderStatus.CONFIRMED, 900);
        order(south, LocalDate.of(2024, 4, 1), OrderStatus.CONFIRMED, 900);

        List<LargeOrder> result = store.findLargeSalesOrders(new BigDecimal("100.00"), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals(List.of(bigNorth.number(), smallNorth.number(), bigSouth.number(), midSouth.number()),
                result.stream().map(r -> r.order().number()).toList());
        assertEquals(new BigDecimal("500.00"), result.get(0).value());
        assertEquals(north.id(), result.get(0).customer().id());
        assertEquals(Region.NORTH, result.get(1).customer().region());
        assertEquals("Sud", result.get(2).customer().name());
        assertEquals(1, result.get(3).order().lines().size());
    }

    @Test
    void returnsNothingWhenNoOrderIsAboveTheValue() {
        order(north, LocalDate.of(2024, 3, 1), OrderStatus.CONFIRMED, 100);
        assertTrue(store.findLargeSalesOrders(new BigDecimal("100.00"), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)).isEmpty());
    }
}
