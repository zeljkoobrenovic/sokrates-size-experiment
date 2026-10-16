package com.acme.backoffice;

import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.OrderStatusChange;
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

class OrderStatusTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    private void create(String number) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), day, OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
    }

    private OrderStatus statusOf(String number) {
        return store.findSalesOrderByNumber(number).orElseThrow().status();
    }

    @Test
    void fullLifecycleRecordsDates() {
        create("SO-1");
        assertEquals(OrderStatus.CONFIRMED, orders.confirm("SO-1", day.plusDays(1)).status());
        orders.ship("SO-1", day.plusDays(2));
        orders.deliver("SO-1", day.plusDays(5));
        assertEquals(OrderStatus.DELIVERED, statusOf("SO-1"));
        long id = store.findSalesOrderByNumber("SO-1").orElseThrow().id();
        assertEquals(List.of(
                new OrderStatusChange(id, OrderStatus.CONFIRMED, day.plusDays(1)),
                new OrderStatusChange(id, OrderStatus.SHIPPED, day.plusDays(2)),
                new OrderStatusChange(id, OrderStatus.DELIVERED, day.plusDays(5))), orders.getHistory("SO-1"));
    }

    @Test
    void cancelDraftAndConfirmed() {
        create("SO-1");
        create("SO-2");
        orders.cancel("SO-1", CancellationReason.OTHER, day);
        orders.confirm("SO-2", day);
        orders.cancel("SO-2", CancellationReason.OTHER, day.plusDays(1));
        assertEquals(OrderStatus.CANCELLED, statusOf("SO-1"));
        assertEquals(OrderStatus.CANCELLED, statusOf("SO-2"));
    }

    @Test
    void otherTransitionsAreRefused() {
        create("SO-1");
        assertThrows(IllegalStateException.class, () -> orders.ship("SO-1", day));
        assertThrows(IllegalStateException.class, () -> orders.deliver("SO-1", day));
        orders.confirm("SO-1", day);
        assertThrows(IllegalStateException.class, () -> orders.confirm("SO-1", day));
        orders.ship("SO-1", day);
        assertThrows(IllegalStateException.class, () -> orders.cancel("SO-1", CancellationReason.OTHER, day));
        orders.deliver("SO-1", day);
        assertThrows(IllegalStateException.class, () -> orders.cancel("SO-1", CancellationReason.OTHER, day));
        assertEquals(OrderStatus.DELIVERED, statusOf("SO-1"));
        assertEquals(3, orders.getHistory("SO-1").size());
    }

    @Test
    void cancelledOrderIsFinal() {
        create("SO-1");
        orders.cancel("SO-1", CancellationReason.OTHER, day);
        assertThrows(IllegalStateException.class, () -> orders.confirm("SO-1", day));
        assertThrows(IllegalStateException.class, () -> orders.cancel("SO-1", CancellationReason.OTHER, day));
    }

    @Test
    void unknownOrderAndMissingDate() {
        assertThrows(IllegalArgumentException.class, () -> orders.confirm("SO-9", day));
        create("SO-1");
        assertThrows(IllegalArgumentException.class, () -> orders.confirm("SO-1", null));
        assertEquals(OrderStatus.DRAFT, statusOf("SO-1"));
    }
}
