package com.acme.backoffice;

import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RemainingLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.OrderService;
import com.acme.backoffice.service.ShipmentService;
import com.acme.backoffice.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartialShipmentTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ShipmentService shipments = new ShipmentService(store);
    private final StockService stock = new StockService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);
    private Warehouse north;
    private Product widget;
    private Product gadget;

    @BeforeEach
    void setUp() {
        Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));
        north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50")));
        stock.setQuantity(north.id(), widget.id(), 10);
        stock.setQuantity(north.id(), gadget.id(), 1);
        store.createSalesOrder(new SalesOrder(0, "SO-1", customer.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 6, new BigDecimal("4.50")), new OrderLine(gadget.id(), 2, new BigDecimal("30.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-2", customer.id(), day, OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.50")))));
    }

    private OrderStatus status(String number) {
        return store.findSalesOrderByNumber(number).orElseThrow().status();
    }

    @Test
    void partsMakeOrderPartiallyShippedThenShipped() {
        assertEquals(List.of(new RemainingLine(1, widget.id(), 6, 0, 6), new RemainingLine(2, gadget.id(), 2, 0, 2)),
                shipments.getRemaining("SO-1"));
        shipments.shipPart("SO-1", 1, 4, "WH-N", "FastFreight", "FF-1", day);
        assertEquals(OrderStatus.PARTIALLY_SHIPPED, status("SO-1"));
        assertEquals(6, stock.getQuantity(north.id(), widget.id()));
        assertEquals(List.of(new RemainingLine(1, widget.id(), 6, 4, 2), new RemainingLine(2, gadget.id(), 2, 0, 2)),
                shipments.getRemaining("SO-1"));
        assertTrue(shipments.findByOrder("SO-1").isEmpty());

        shipments.shipPart("SO-1", 1, 2, "WH-N", "FastFreight", "FF-2", day.plusDays(1));
        assertEquals(List.of(new RemainingLine(2, gadget.id(), 2, 0, 2)), shipments.getRemaining("SO-1"));
        assertEquals(OrderStatus.PARTIALLY_SHIPPED, status("SO-1"));

        stock.setQuantity(north.id(), gadget.id(), 5);
        shipments.shipPart("SO-1", 2, 2, "WH-N", "FastFreight", "FF-3", day.plusDays(2));
        assertEquals(OrderStatus.SHIPPED, status("SO-1"));
        assertTrue(shipments.getRemaining("SO-1").isEmpty());
        assertEquals("FF-3", shipments.findByOrder("SO-1").orElseThrow().trackingNumber());
        assertEquals(3, stock.getQuantity(north.id(), gadget.id()));
        assertEquals(List.of(OrderStatus.PARTIALLY_SHIPPED, OrderStatus.PARTIALLY_SHIPPED, OrderStatus.SHIPPED),
                new OrderService(store).getHistory("SO-1").stream().map(c -> c.status()).toList());
    }

    @Test
    void invalidPartsChangeNothing() {
        assertThrows(IllegalStateException.class, () -> shipments.shipPart("SO-1", 1, 7, "WH-N", "FastFreight", "FF-1", day));
        assertThrows(IllegalStateException.class, () -> shipments.shipPart("SO-1", 2, 2, "WH-N", "FastFreight", "FF-1", day));
        assertThrows(IllegalStateException.class, () -> shipments.shipPart("SO-2", 1, 1, "WH-N", "FastFreight", "FF-1", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.shipPart("SO-1", 3, 1, "WH-N", "FastFreight", "FF-1", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.shipPart("SO-1", 1, 0, "WH-N", "FastFreight", "FF-1", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.shipPart("SO-9", 1, 1, "WH-N", "FastFreight", "FF-1", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.shipPart("SO-1", 1, 1, "WH-X", "FastFreight", "FF-1", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.shipPart("SO-1", 1, 1, "WH-N", "Nobody", "FF-1", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.shipPart("SO-1", 1, 1, "WH-N", "FastFreight", " ", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.shipPart("SO-1", 1, 1, "WH-N", "FastFreight", "FF-1", null));
        assertEquals(OrderStatus.CONFIRMED, status("SO-1"));
        assertEquals(10, stock.getQuantity(north.id(), widget.id()));
        assertEquals(1, stock.getQuantity(north.id(), gadget.id()));
        assertEquals(2, shipments.getRemaining("SO-1").size());
    }

    @Test
    void wholeLineShippedAndFurtherShippingRefused() {
        shipments.shipPart("SO-1", 1, 6, "WH-N", "FastFreight", "FF-1", day);
        assertThrows(IllegalStateException.class, () -> shipments.shipPart("SO-1", 1, 1, "WH-N", "FastFreight", "FF-2", day));
        assertThrows(IllegalStateException.class, () -> shipments.ship("SO-1", "WH-N", "FastFreight", "FF-3", day));
        assertThrows(IllegalStateException.class, () -> new OrderService(store).cancel("SO-1", CancellationReason.OTHER, day));
        shipments.shipPart("SO-1", 2, 1, "WH-N", "FastFreight", "FF-4", day);
        assertEquals(OrderStatus.PARTIALLY_SHIPPED, status("SO-1"));
        assertThrows(IllegalStateException.class, () -> shipments.shipPart("SO-1", 2, 1, "WH-N", "FastFreight", "FF-5", day));
        assertEquals(List.of(new RemainingLine(2, gadget.id(), 2, 1, 1)), shipments.getRemaining("SO-1"));
    }
}
