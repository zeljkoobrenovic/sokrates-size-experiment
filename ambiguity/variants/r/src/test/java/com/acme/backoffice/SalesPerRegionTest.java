package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionSales;
import com.acme.backoffice.model.SalesOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalesPerRegionTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer zeta = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer beta = store.customers().create(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer sud = store.customers().create(new Customer(0, "Sud", "s@example.com", null, "Italy", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, Customer customer, LocalDate date, OrderStatus status, int quantity) {
        store.orders().create(new SalesOrder(0, number, customer.id(), date, status,
                List.of(new OrderLine(widget.id(), quantity, new BigDecimal("10.00")))));
    }

    @Test
    void sumsNonCancelledOrdersPerRegionInPeriodSortedByValue() {
        order("SO-1", alpha, LocalDate.of(2024, 6, 1), OrderStatus.CONFIRMED, 2);
        order("SO-2", beta, LocalDate.of(2024, 6, 30), OrderStatus.DELIVERED, 1);
        order("SO-3", zeta, LocalDate.of(2024, 6, 15), OrderStatus.CONFIRMED, 5);
        order("SO-4", zeta, LocalDate.of(2024, 6, 16), OrderStatus.CANCELLED, 9);
        order("SO-5", alpha, LocalDate.of(2024, 7, 1), OrderStatus.CONFIRMED, 7);
        order("SO-6", sud, LocalDate.of(2024, 5, 31), OrderStatus.CONFIRMED, 7);

        List<RegionSales> result = store.salesReports().salesPerRegion(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals(2, result.size());
        assertEquals(Region.WEST, result.get(0).region());
        assertEquals(1, result.get(0).orderCount());
        assertEquals(0, new BigDecimal("50.00").compareTo(result.get(0).totalValue()));
        assertEquals(Region.NORTH, result.get(1).region());
        assertEquals(2, result.get(1).orderCount());
        assertEquals(0, new BigDecimal("30.00").compareTo(result.get(1).totalValue()));
    }

    @Test
    void emptyResultIsEmptyList() {
        assertTrue(store.salesReports().salesPerRegion(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)).isEmpty());
    }
}
