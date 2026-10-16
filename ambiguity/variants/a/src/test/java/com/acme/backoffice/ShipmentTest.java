package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.Warehouse;
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

class ShipmentTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ShipmentService shipments = new ShipmentService(store);
    private final StockService stock = new StockService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);
    private Warehouse north;
    private Warehouse east;
    private Product widget;
    private Product gadget;
    private Carrier carrier;

    @BeforeEach
    void setUp() {
        Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));
        north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
        carrier = store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50")));
        store.createCarrier(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        stock.setQuantity(north.id(), widget.id(), 10);
        stock.setQuantity(north.id(), gadget.id(), 1);
        stock.setQuantity(east.id(), widget.id(), 3);
        store.createSalesOrder(new SalesOrder(0, "SO-1", customer.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 4, new BigDecimal("4.50")), new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
        store.createSalesOrder(new SalesOrder(0, "SO-2", customer.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.50")), new OrderLine(gadget.id(), 2, new BigDecimal("30.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-3", customer.id(), day, OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.50")))));
    }

    @Test
    void shipTakesStockAndMarksOrderShipped() {
        Shipment shipment = shipments.ship("SO-1", "WH-N", "FastFreight", "FF-1", day);
        assertEquals(north.id(), shipment.warehouseId());
        assertEquals(4, stock.getQuantity(north.id(), widget.id()));
        assertEquals(3, stock.getQuantity(east.id(), widget.id()));
        assertEquals(OrderStatus.SHIPPED, store.findSalesOrderByNumber("SO-1").orElseThrow().status());
        Shipment found = shipments.findByOrder("SO-1").orElseThrow();
        assertEquals(shipment, found);
        assertEquals(carrier.id(), found.carrierId());
        assertEquals("FF-1", found.trackingNumber());
        assertEquals(day, found.shipDate());
    }

    @Test
    void shortStockChangesNothing() {
        assertThrows(IllegalStateException.class, () -> shipments.ship("SO-2", "WH-N", "FastFreight", "FF-2", day));
        assertEquals(10, stock.getQuantity(north.id(), widget.id()));
        assertEquals(1, stock.getQuantity(north.id(), gadget.id()));
        assertEquals(OrderStatus.CONFIRMED, store.findSalesOrderByNumber("SO-2").orElseThrow().status());
        assertTrue(shipments.findByOrder("SO-2").isEmpty());
        assertThrows(IllegalStateException.class, () -> shipments.ship("SO-1", "WH-E", "FastFreight", "FF-3", day));
        assertEquals(3, stock.getQuantity(east.id(), widget.id()));
    }

    @Test
    void invalidShipments() {
        assertThrows(IllegalStateException.class, () -> shipments.ship("SO-3", "WH-N", "FastFreight", "FF-4", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.ship("SO-9", "WH-N", "FastFreight", "FF-4", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.ship("SO-1", "WH-X", "FastFreight", "FF-4", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.ship("SO-1", "WH-N", " ", "FF-4", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.ship("SO-1", "WH-N", "Nobody", "FF-4", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.ship("SO-1", "WH-N", "FastFreight", null, day));
        assertThrows(IllegalArgumentException.class, () -> shipments.ship("SO-1", "WH-N", "FastFreight", "FF-4", null));
        shipments.ship("SO-1", "WH-N", "FastFreight", "FF-4", day);
        assertThrows(IllegalStateException.class, () -> shipments.ship("SO-1", "WH-N", "FastFreight", "FF-5", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.findByOrder("SO-9"));
        assertThrows(IllegalArgumentException.class, () -> shipments.listByWarehouse("WH-X", day, day));
    }

    @Test
    void listByWarehouseAndPeriod() {
        stock.setQuantity(north.id(), gadget.id(), 5);
        shipments.ship("SO-2", "WH-N", "RoadRunner", "RR-1", day.plusDays(10));
        shipments.ship("SO-1", "WH-N", "FastFreight", "FF-1", day);
        assertEquals(List.of("FF-1", "RR-1"), shipments.listByWarehouse("WH-N", day, day.plusDays(10)).stream().map(Shipment::trackingNumber).toList());
        assertEquals(List.of("FF-1"), shipments.listByWarehouse("WH-N", day, day.plusDays(9)).stream().map(Shipment::trackingNumber).toList());
        assertTrue(shipments.listByWarehouse("WH-E", day, day.plusDays(30)).isEmpty());
    }
}
