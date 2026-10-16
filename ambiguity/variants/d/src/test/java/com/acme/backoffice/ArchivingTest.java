package com.acme.backoffice;

import com.acme.backoffice.model.ArchivedYearRevenue;
import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArchivingTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final LocalDate today = LocalDate.of(2026, 7, 1);

    private void create(String number) {
        store.createSalesOrder(new SalesOrder(0, number, alpha.id(), LocalDate.of(2023, 1, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
    }

    private void deliver(String number, LocalDate date) {
        orders.confirm(number, date);
        orders.ship(number, date);
        orders.deliver(number, date);
    }

    private List<String> numbers(List<SalesOrder> list) {
        return list.stream().map(SalesOrder::number).toList();
    }

    @Test
    void archivesOnlyDeliveredOrCancelledOrdersOlderThanTwoYears() {
        for (String n : List.of("SO-1", "SO-2", "SO-3", "SO-4", "SO-5")) {
            create(n);
        }
        deliver("SO-1", LocalDate.of(2024, 6, 30));
        deliver("SO-2", LocalDate.of(2024, 7, 1));
        orders.cancel("SO-3", CancellationReason.OTHER, LocalDate.of(2023, 2, 1));
        orders.confirm("SO-4", LocalDate.of(2023, 2, 1));
        assertEquals(2, orders.archive(today));
        assertEquals(List.of("SO-1", "SO-3"), numbers(orders.listArchived()));
        assertEquals(0, orders.archive(today));
    }

    @Test
    void archivedOrdersLeaveTheListsButStayInTheSalesReports() {
        create("SO-1");
        create("SO-2");
        deliver("SO-1", LocalDate.of(2023, 2, 1));
        orders.confirm("SO-2", LocalDate.of(2023, 2, 1));
        BigDecimal before = store.sumNotCancelledSalesOrderValueOfCustomer(alpha.id());
        assertEquals(2, store.findSalesOrdersOfCustomer(alpha.id()).size());
        assertEquals(1, orders.archive(today));
        assertEquals(List.of("SO-2"), numbers(store.findSalesOrdersOfCustomer(alpha.id())));
        assertEquals(List.of("SO-2"), numbers(store.findOpenSalesOrders()));
        assertEquals(List.of("SO-2"), numbers(store.findOpenSalesOrdersOfCustomer(alpha.id())));
        assertEquals(List.of("SO-2"), numbers(store.findOpenSalesOrdersInRegion(Region.WEST)));
        assertEquals(List.of("SO-2"), numbers(store.findSalesOrdersMatching(null, null, null, null, null)));
        assertEquals(before, store.sumNotCancelledSalesOrderValueOfCustomer(alpha.id()));
    }

    @Test
    void listsArchivedOrdersPerCustomerAndRegionAndRevenuePerYear() {
        Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        create("SO-1");
        create("SO-2");
        create("SO-3");
        store.createSalesOrder(new SalesOrder(0, "SO-4", beta.id(), LocalDate.of(2022, 3, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        deliver("SO-1", LocalDate.of(2023, 2, 1));
        orders.cancel("SO-2", CancellationReason.OTHER, LocalDate.of(2023, 2, 1));
        orders.confirm("SO-3", LocalDate.of(2023, 2, 1));
        deliver("SO-4", LocalDate.of(2023, 2, 1));
        assertEquals(3, orders.archive(today));
        assertEquals(List.of("SO-1", "SO-2"), numbers(orders.listArchivedByCustomer(alpha.id())));
        assertEquals(List.of("SO-4"), numbers(orders.listArchivedByCustomer(beta.id())));
        assertEquals(List.of("SO-1", "SO-2"), numbers(orders.listArchivedByRegion(Region.WEST)));
        assertEquals(List.of("SO-4"), numbers(orders.listArchivedByRegion(Region.NORTH)));
        assertEquals(List.of(new ArchivedYearRevenue(2022, new BigDecimal("10.00")),
                new ArchivedYearRevenue(2023, new BigDecimal("9.00"))), orders.archivedRevenuePerYear());
        assertThrows(IllegalArgumentException.class, () -> orders.listArchivedByRegion(null));
    }

    @Test
    void dateIsRequired() {
        assertThrows(IllegalArgumentException.class, () -> orders.archive(null));
    }
}
