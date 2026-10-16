package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RepPerformance;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepPerformanceTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRep anna = store.salesReps().create(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final SalesRep carl = store.salesReps().create(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer zeta = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer loner = store.customers().create(new Customer(0, "Loner", "l@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, Customer customer, LocalDate date, OrderStatus status, int quantity) {
        store.orders().create(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("10.00")),
                        new OrderLine(widget.id(), 1, new BigDecimal("0.50")))));
    }

    @Test
    void countsAndSumsNonCancelledOrdersOfRepCustomersInPeriod() {
        store.customers().assignToSalesRep(alpha.id(), anna.id());
        store.customers().assignToSalesRep(zeta.id(), anna.id());
        store.customers().assignToSalesRep(loner.id(), carl.id());
        order("SO-1", alpha, LocalDate.of(2024, 6, 1), OrderStatus.CONFIRMED, 2);
        order("SO-2", zeta, LocalDate.of(2024, 6, 30), OrderStatus.DELIVERED, 1);
        order("SO-3", zeta, LocalDate.of(2024, 6, 15), OrderStatus.CANCELLED, 5);
        order("SO-4", alpha, LocalDate.of(2024, 7, 1), OrderStatus.CONFIRMED, 7);
        order("SO-5", alpha, LocalDate.of(2024, 5, 31), OrderStatus.CONFIRMED, 7);
        order("SO-6", loner, LocalDate.of(2024, 6, 10), OrderStatus.DRAFT, 3);

        RepPerformance annaResult = store.salesReps().findPerformance(anna.id(), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals(2, annaResult.orderCount());
        assertEquals(0, new BigDecimal("31.00").compareTo(annaResult.totalValue()));

        RepPerformance carlResult = store.salesReps().findPerformance(carl.id(), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals(1, carlResult.orderCount());
        assertEquals(0, new BigDecimal("30.50").compareTo(carlResult.totalValue()));
    }

    @Test
    void emptyResultIsZero() {
        RepPerformance none = store.salesReps().findPerformance(999, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        assertEquals(0, none.orderCount());
        assertEquals(0, BigDecimal.ZERO.compareTo(none.totalValue()));
    }
}
