package com.acme.backoffice;

import com.acme.backoffice.model.CancellationCount;
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

class OrderCancellationTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer beta = store.customers().create(new Customer(0, "Beta", "b@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 6, 10);

    private void create(String number, Customer customer) {
        store.orders().create(new SalesOrder(0, number, customer.id(), day, OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
    }

    @Test
    void reasonIsRequiredAndOrderStaysUntouchedWithoutIt() {
        create("SO-1", alpha);
        assertThrows(IllegalArgumentException.class, () -> orders.cancel("SO-1", null, day));
        assertEquals(OrderStatus.DRAFT, store.orders().findByNumber("SO-1").orElseThrow().status());
        assertEquals(List.of(), orders.getCancellations(day, day));
    }

    @Test
    void cancellingRecordsStatusHistoryAndReason() {
        create("SO-1", alpha);
        assertEquals(OrderStatus.CANCELLED, orders.cancel("SO-1", CancellationReason.CREDIT, day).status());
        assertEquals(1, orders.getHistory("SO-1").size());
        assertEquals(List.of(new CancellationCount(CancellationReason.CREDIT, alpha.id(), "Alpha", 1)),
                orders.getCancellations(day, day));
    }

    @Test
    void reportsPerReasonAndCustomerWithinInclusivePeriod() {
        for (String number : List.of("SO-1", "SO-2", "SO-3", "SO-4", "SO-5", "SO-6")) {
            create(number, number.equals("SO-3") || number.equals("SO-6") ? beta : alpha);
        }
        orders.cancel("SO-1", CancellationReason.OUT_OF_STOCK, day);
        orders.cancel("SO-2", CancellationReason.OUT_OF_STOCK, day.plusDays(2));
        orders.cancel("SO-3", CancellationReason.OUT_OF_STOCK, day.plusDays(2));
        orders.cancel("SO-4", CancellationReason.CUSTOMER_REQUEST, day.plusDays(5));
        orders.cancel("SO-5", CancellationReason.OTHER, day.minusDays(1));
        orders.cancel("SO-6", CancellationReason.OTHER, day.plusDays(6));
        assertEquals(List.of(
                new CancellationCount(CancellationReason.CUSTOMER_REQUEST, alpha.id(), "Alpha", 1),
                new CancellationCount(CancellationReason.OUT_OF_STOCK, alpha.id(), "Alpha", 2),
                new CancellationCount(CancellationReason.OUT_OF_STOCK, beta.id(), "Beta", 1)),
                orders.getCancellations(day, day.plusDays(5)));
        assertEquals(List.of(), orders.getCancellations(day.plusDays(10), day.plusDays(20)));
    }
}
