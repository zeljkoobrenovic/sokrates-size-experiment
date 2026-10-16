package com.acme.backoffice;

import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.CancelledOrder;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CancelledOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    private void order(String number, Customer customer, LocalDate cancelDate) {
        Product widget = store.findProductBySku("W-1").orElseThrow();
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
        if (cancelDate != null) {
            orders.cancel(number, CancellationReason.OTHER, cancelDate);
        }
    }

    private List<String> numbers(List<CancelledOrder> list) {
        return list.stream().map(c -> c.order().number()).toList();
    }

    @BeforeEach
    void setUp() {
        Customer north = store.createCustomer(new Customer(0, "Nord", "n@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer south = store.createCustomer(new Customer(0, "Sud", "s@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer north2 = store.createCustomer(new Customer(0, "Aarhus", "a@example.com", null, "Denmark", Region.NORTH, CustomerStatus.ACTIVE));
        store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        order("SO-1", north, day.plusDays(1));
        order("SO-2", north, day.plusDays(5));
        order("SO-3", south, day.plusDays(3));
        order("SO-4", north2, day.plusDays(2));
        order("SO-5", south, day.plusDays(30));
        order("SO-6", south, null);
    }

    @Test
    void groupsPerCustomerNewestFirst() {
        List<CancelledOrder> result = orders.getCancelledOrdersByCustomer(day, day.plusDays(10));
        assertEquals(List.of("SO-4", "SO-2", "SO-1", "SO-3"), numbers(result));
        assertEquals(day.plusDays(5), result.get(1).cancelDate());
        assertEquals(CancellationReason.OTHER, result.get(1).reason());
        assertEquals("Nord", result.get(1).customer().name());
        assertEquals(1, result.get(1).order().lines().size());
    }

    @Test
    void groupsPerRegionNewestFirst() {
        List<CancelledOrder> result = orders.getCancelledOrdersByRegion(day, day.plusDays(10));
        assertEquals(List.of("SO-2", "SO-4", "SO-1", "SO-3"), numbers(result));
    }

    @Test
    void periodIsInclusiveAndExcludesOthers() {
        assertEquals(List.of("SO-4", "SO-1"), numbers(orders.getCancelledOrdersByRegion(day.plusDays(1), day.plusDays(2))));
        assertTrue(orders.getCancelledOrdersByCustomer(day.plusDays(6), day.plusDays(20)).isEmpty());
    }

    @Test
    void rejectsBadPeriod() {
        assertThrows(IllegalArgumentException.class, () -> orders.getCancelledOrdersByCustomer(null, day));
        assertThrows(IllegalArgumentException.class, () -> orders.getCancelledOrdersByRegion(day.plusDays(1), day));
    }
}
