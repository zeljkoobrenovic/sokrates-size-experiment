package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.LatestOrdersService;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArchivedLatestOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final LatestOrdersService latest = new LatestOrdersService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));

    private void create(String number, Customer customer, LocalDate date) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, OrderStatus.DRAFT,
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
    void archivedOrdersLeaveTheLatestOrderLists() {
        create("SO-1", alpha, LocalDate.of(2023, 1, 1));
        create("SO-2", alpha, LocalDate.of(2023, 1, 2));
        create("SO-3", beta, LocalDate.of(2023, 1, 3));
        create("SO-4", alpha, LocalDate.of(2023, 1, 4));
        deliver("SO-1", LocalDate.of(2023, 2, 1));
        deliver("SO-3", LocalDate.of(2023, 2, 1));
        orders.confirm("SO-2", LocalDate.of(2023, 2, 1));
        deliver("SO-4", LocalDate.of(2026, 6, 1));
        assertEquals(2, orders.archive(LocalDate.of(2026, 7, 1)));

        assertEquals(List.of("SO-4", "SO-2"), numbers(latest.overall(10)));
        assertEquals(List.of("SO-4", "SO-2"), numbers(latest.forRegion(Region.WEST, 10)));
        assertEquals(List.of("SO-4", "SO-2"), numbers(latest.forCustomer(alpha.id(), 10)));
        assertEquals(List.of(), numbers(latest.forCustomer(beta.id(), 10)));
    }
}
