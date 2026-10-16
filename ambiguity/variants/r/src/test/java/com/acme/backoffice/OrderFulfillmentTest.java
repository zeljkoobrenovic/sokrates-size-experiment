package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseOrderFulfillment;
import com.acme.backoffice.service.BundleService;
import com.acme.backoffice.service.OrderFulfillmentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderFulfillmentTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderFulfillmentService service = new OrderFulfillmentService(store);
    private final Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Warehouse north = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse east = store.warehouses().create(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("9.00"), "piece", true));

    private void order(String number, OrderStatus status, OrderLine... lines) {
        store.orders().create(new SalesOrder(0, number, customer.id(), LocalDate.of(2024, 1, 1), status, List.of(lines)));
    }

    private OrderLine line(Product product, int quantity) {
        return new OrderLine(product.id(), quantity, new BigDecimal("1.00"));
    }

    private List<String> numbers(List<SalesOrder> orders) {
        return orders.stream().map(SalesOrder::number).toList();
    }

    @Test
    void splitsOrdersPerWarehouseByOwnStock() {
        store.stock().setQuantity(north.id(), widget.id(), 5);
        store.stock().setQuantity(north.id(), gadget.id(), 1);
        store.stock().setQuantity(east.id(), widget.id(), 2);
        store.stock().setQuantity(east.id(), gadget.id(), 4);
        order("SO-1", OrderStatus.CONFIRMED, line(widget, 3));
        order("SO-2", OrderStatus.CONFIRMED, line(widget, 2), line(gadget, 3));
        order("SO-3", OrderStatus.CONFIRMED, line(widget, 2), line(widget, 3));
        order("SO-4", OrderStatus.SHIPPED, line(widget, 1));

        List<WarehouseOrderFulfillment> result = service.listOpenOrdersPerWarehouse();
        assertEquals(2, result.size());
        assertEquals("WH-E", result.get(0).warehouse().code());
        assertEquals(List.of("SO-2"), numbers(result.get(0).shippable()));
        assertEquals(List.of("SO-1", "SO-3"), numbers(result.get(0).notShippable()));
        assertEquals("WH-N", result.get(1).warehouse().code());
        assertEquals(List.of("SO-1", "SO-3"), numbers(result.get(1).shippable()));
        assertEquals(List.of("SO-2"), numbers(result.get(1).notShippable()));
    }

    @Test
    void missingStockRowMeansNotShippable() {
        order("SO-1", OrderStatus.CONFIRMED, line(widget, 1));
        WarehouseOrderFulfillment first = service.listOpenOrdersPerWarehouse().get(0);
        assertTrue(first.shippable().isEmpty());
        assertEquals(List.of("SO-1"), numbers(first.notShippable()));
    }

    @Test
    void bundlesNeedTheirComponents() {
        Product kit = store.products().create(new Product(0, "K-1", "Kit", "Kits", new BigDecimal("20.00"), "piece", true));
        new BundleService(store).addComponent(kit.id(), widget.id(), 2);
        new BundleService(store).addComponent(kit.id(), gadget.id(), 1);
        store.stock().setQuantity(north.id(), widget.id(), 4);
        store.stock().setQuantity(north.id(), gadget.id(), 2);
        store.stock().setQuantity(east.id(), widget.id(), 4);
        store.stock().setQuantity(east.id(), gadget.id(), 1);
        order("SO-1", OrderStatus.CONFIRMED, line(kit, 2));

        List<WarehouseOrderFulfillment> result = service.listOpenOrdersPerWarehouse();
        assertEquals(List.of("SO-1"), numbers(result.get(1).shippable()));
        assertEquals(List.of("SO-1"), numbers(result.get(0).notShippable()));
    }

    @Test
    void noOpenOrders() {
        List<WarehouseOrderFulfillment> result = service.listOpenOrdersPerWarehouse();
        assertEquals(2, result.size());
        assertTrue(result.get(0).shippable().isEmpty() && result.get(0).notShippable().isEmpty());
    }
}
