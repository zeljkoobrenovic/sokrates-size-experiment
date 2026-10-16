package com.acme.backoffice;

import com.acme.backoffice.model.BackOrder;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.BackOrderService;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BackOrderTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final BackOrderService service = new BackOrderService(store);
    private final Customer alpha;
    private final Customer beta;
    private final Product widget;
    private final Product gadget;

    BackOrderTest() {
        alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
        gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("20.00"), "piece", true));
        Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        store.setStockQuantity(north.id(), widget.id(), 5);
        order("SO-1", alpha, widget, 10, OrderStatus.CONFIRMED);
        order("SO-2", beta, gadget, 1, OrderStatus.CONFIRMED);
        order("SO-3", beta, widget, 6, OrderStatus.CONFIRMED);
        order("SO-4", alpha, widget, 50, OrderStatus.DRAFT);
        order("SO-5", alpha, widget, 2, OrderStatus.CONFIRMED);
    }

    private void order(String number, Customer customer, Product product, int quantity, OrderStatus status) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), LocalDate.of(2024, 1, 1), status,
                List.of(new OrderLine(product.id(), quantity, new BigDecimal("10.00")))));
    }

    private List<String> numbers(List<BackOrder> backOrders) {
        return backOrders.stream().map(BackOrder::orderNumber).toList();
    }

    @Test
    void backOrdersAreListedOldestFirstAndFiltered() {
        service.putOnBackOrder("SO-3", LocalDate.of(2024, 2, 5));
        service.putOnBackOrder("SO-2", LocalDate.of(2024, 2, 9));
        service.putOnBackOrder("SO-1", LocalDate.of(2024, 2, 1));
        assertEquals(List.of("SO-1", "SO-3", "SO-2"), numbers(service.listAll()));
        assertEquals(List.of("SO-3", "SO-2"), numbers(service.listByCustomer(beta.id())));
        assertEquals(List.of("SO-1"), numbers(service.listByCustomer(alpha.id())));
        assertEquals(List.of("SO-1", "SO-3"), numbers(service.listByProduct(widget.id())));
        assertEquals(List.of("SO-2"), numbers(service.listByProduct(gadget.id())));
    }

    @Test
    void shippedOrderLeavesTheBackOrderList() {
        service.putOnBackOrder("SO-1", LocalDate.of(2024, 2, 1));
        new OrderService(store).ship("SO-1", LocalDate.of(2024, 2, 3));
        assertTrue(service.listAll().isEmpty());
    }

    @Test
    void invalidBackOrdersAreRejected() {
        assertThrows(IllegalStateException.class, () -> service.putOnBackOrder("SO-5", LocalDate.of(2024, 2, 1)));
        assertThrows(IllegalStateException.class, () -> service.putOnBackOrder("SO-4", LocalDate.of(2024, 2, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.putOnBackOrder("SO-X", LocalDate.of(2024, 2, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.putOnBackOrder("SO-1", null));
        service.putOnBackOrder("SO-1", LocalDate.of(2024, 2, 1));
        assertThrows(IllegalStateException.class, () -> service.putOnBackOrder("SO-1", LocalDate.of(2024, 2, 2)));
        assertEquals(List.of("SO-1"), numbers(service.listAll()));
    }
}
