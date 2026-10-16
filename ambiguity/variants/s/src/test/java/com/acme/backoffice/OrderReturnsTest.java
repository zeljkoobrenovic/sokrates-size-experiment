package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderReturns;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderReturnsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Product bolt = store.createProduct(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product nut = store.createProduct(new Product(0, "N-1", "Nut", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private int counter;

    private SalesOrder order(Customer customer, LocalDate date, OrderLine... lines) {
        counter++;
        return store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.DELIVERED, List.of(lines)));
    }

    private void giveBack(SalesOrder order, LocalDate date, ReturnLine... lines) {
        counter++;
        store.createSalesReturn(new SalesReturn(0, "R-" + counter, order.id(), date, "Damaged", List.of(lines)), north.id());
    }

    @Test
    void listsOrdersWithReturnsOfTheCustomerBetweenTwoDatesWithReturnedShare() {
        SalesOrder first = order(alpha, LocalDate.of(2024, 3, 1),
                new OrderLine(bolt.id(), 10, new BigDecimal("2.00")), new OrderLine(nut.id(), 10, new BigDecimal("3.00")));
        SalesOrder second = order(alpha, LocalDate.of(2024, 3, 31), new OrderLine(bolt.id(), 30, new BigDecimal("1.00")));
        SalesOrder noReturn = order(alpha, LocalDate.of(2024, 3, 10), new OrderLine(bolt.id(), 5, new BigDecimal("1.00")));
        SalesOrder tooEarly = order(alpha, LocalDate.of(2024, 2, 29), new OrderLine(bolt.id(), 5, new BigDecimal("1.00")));
        SalesOrder tooLate = order(alpha, LocalDate.of(2024, 4, 1), new OrderLine(bolt.id(), 5, new BigDecimal("1.00")));
        SalesOrder other = order(beta, LocalDate.of(2024, 3, 5), new OrderLine(bolt.id(), 5, new BigDecimal("1.00")));
        giveBack(first, LocalDate.of(2024, 3, 5), new ReturnLine(bolt.id(), 2), new ReturnLine(nut.id(), 1));
        giveBack(first, LocalDate.of(2024, 5, 1), new ReturnLine(nut.id(), 1));
        giveBack(second, LocalDate.of(2024, 4, 2), new ReturnLine(bolt.id(), 1));
        giveBack(tooEarly, LocalDate.of(2024, 3, 5), new ReturnLine(bolt.id(), 1));
        giveBack(tooLate, LocalDate.of(2024, 4, 5), new ReturnLine(bolt.id(), 1));
        giveBack(other, LocalDate.of(2024, 3, 6), new ReturnLine(bolt.id(), 1));

        List<OrderReturns> result = store.listOrdersWithReturnsByCustomerAndPeriod(alpha.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals(List.of(first.number(), second.number()), result.stream().map(r -> r.order().number()).toList());
        assertEquals(2, result.get(0).order().lines().size());
        assertEquals(new BigDecimal("50.00"), result.get(0).orderValue());
        assertEquals(new BigDecimal("10.00"), result.get(0).returnedValue());
        assertEquals(new BigDecimal("20.00"), result.get(0).returnedPercent());
        assertEquals(new BigDecimal("30.00"), result.get(1).orderValue());
        assertEquals(new BigDecimal("1.00"), result.get(1).returnedValue());
        assertEquals(new BigDecimal("3.33"), result.get(1).returnedPercent());
        assertTrue(noReturn.id() > 0);
    }

    @Test
    void returnsNothingWhenNoOrderInThePeriodHasReturns() {
        SalesOrder order = order(alpha, LocalDate.of(2024, 3, 1), new OrderLine(bolt.id(), 10, new BigDecimal("2.00")));
        giveBack(order, LocalDate.of(2024, 3, 2), new ReturnLine(bolt.id(), 1));
        assertTrue(store.listOrdersWithReturnsByCustomerAndPeriod(alpha.id(), LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30)).isEmpty());
        assertTrue(store.listOrdersWithReturnsByCustomerAndPeriod(beta.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)).isEmpty());
    }
}
