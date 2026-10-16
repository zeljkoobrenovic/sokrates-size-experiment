package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerSales;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalesPerCustomerTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer gamma = store.createCustomer(new Customer(0, "Gamma", "g@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer zeta = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, Customer customer, LocalDate date, OrderStatus status, int quantity) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("10.00")))));
    }

    private void fill() {
        order("SO-1", alpha, LocalDate.of(2024, 6, 1), OrderStatus.CONFIRMED, 2);
        order("SO-2", alpha, LocalDate.of(2024, 6, 30), OrderStatus.DELIVERED, 1);
        order("SO-3", zeta, LocalDate.of(2024, 6, 15), OrderStatus.CONFIRMED, 9);
        order("SO-4", beta, LocalDate.of(2024, 6, 16), OrderStatus.CANCELLED, 20);
        order("SO-5", beta, LocalDate.of(2024, 7, 1), OrderStatus.CONFIRMED, 7);
        order("SO-6", gamma, LocalDate.of(2024, 6, 10), OrderStatus.CONFIRMED, 3);
        order("SO-7", beta, LocalDate.of(2024, 6, 11), OrderStatus.CONFIRMED, 3);
    }

    @Test
    void sumsNonCancelledOrdersPerCustomerBiggestFirst() {
        fill();
        List<CustomerSales> result = store.sumNonCancelledSalesPerCustomerBetween(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals(List.of("Zeta", "Alpha", "Beta", "Gamma"), result.stream().map(CustomerSales::name).toList());
        assertEquals(1, result.get(0).orderCount());
        assertEquals(0, new BigDecimal("90.00").compareTo(result.get(0).revenue()));
        assertEquals(2, result.get(1).orderCount());
        assertEquals(0, new BigDecimal("30.00").compareTo(result.get(1).revenue()));
        assertEquals(1, result.get(2).orderCount());
    }

    @Test
    void topCustomersOfRegionAreLimited() {
        fill();
        List<CustomerSales> top = store.listTopRevenueCustomersInRegionBetween(Region.NORTH, 2, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 31));
        assertEquals(List.of("Beta", "Alpha"), top.stream().map(CustomerSales::name).toList());
        assertEquals(2, top.get(0).orderCount());
        assertEquals(0, new BigDecimal("100.00").compareTo(top.get(0).revenue()));
        assertEquals(3, store.listTopRevenueCustomersInRegionBetween(Region.NORTH, 10, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 31)).size());
    }

    @Test
    void emptyResultIsEmptyList() {
        fill();
        assertTrue(store.sumNonCancelledSalesPerCustomerBetween(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)).isEmpty());
        assertTrue(store.listTopRevenueCustomersInRegionBetween(Region.SOUTH, 5, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)).isEmpty());
    }
}
