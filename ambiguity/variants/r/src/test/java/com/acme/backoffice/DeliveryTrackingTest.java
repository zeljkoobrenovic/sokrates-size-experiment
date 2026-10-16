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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeliveryTrackingTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ShipmentService shipments = new ShipmentService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    @BeforeEach
    void setUp() {
        Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        Warehouse north = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        store.carriers().create(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50")));
        store.carriers().create(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        store.carriers().create(new Carrier(0, "Idle", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        new StockService(store).setQuantity(north.id(), widget.id(), 100);
        for (int i = 1; i <= 4; i++) {
            store.orders().create(new SalesOrder(0, "SO-" + i, customer.id(), day, OrderStatus.CONFIRMED,
                    List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.50")))));
        }
        shipments.ship("SO-1", "WH-N", "FastFreight", "FF-1", day);
        shipments.ship("SO-2", "WH-N", "RoadRunner", "RR-2", day.plusDays(5));
        shipments.ship("SO-3", "WH-N", "FastFreight", "FF-3", day.plusDays(10));
    }

    private List<String> tracking(List<Shipment> list) {
        return list.stream().map(Shipment::trackingNumber).toList();
    }

    @Test
    void deliveryRemovesShipmentFromTransit() {
        assertEquals(List.of("FF-1", "RR-2", "FF-3"), tracking(shipments.listInTransit()));
        assertNull(shipments.findByOrder("SO-1").orElseThrow().deliveryDate());
        shipments.markDelivered("SO-1", day.plusDays(3));
        assertEquals(day.plusDays(3), shipments.findByOrder("SO-1").orElseThrow().deliveryDate());
        assertEquals(List.of("RR-2", "FF-3"), tracking(shipments.listInTransit()));
    }

    @Test
    void listByCarrier() {
        assertEquals(List.of("FF-1", "FF-3"), tracking(shipments.listInTransitByCarrier("FastFreight")));
        shipments.markDelivered("SO-1", day);
        assertEquals(List.of("FF-3"), tracking(shipments.listInTransitByCarrier("FastFreight")));
        assertEquals(List.of("RR-2"), tracking(shipments.listInTransitByCarrier("RoadRunner")));
        assertTrue(shipments.listInTransitByCarrier("Idle").isEmpty());
        assertThrows(IllegalArgumentException.class, () -> shipments.listInTransitByCarrier("Nobody"));
    }

    @Test
    void listInTransitLongerThanDays() {
        LocalDate asOf = day.plusDays(20);
        assertEquals(List.of("FF-1", "RR-2"), tracking(shipments.listInTransitLongerThan(10, asOf)));
        assertEquals(List.of("FF-1"), tracking(shipments.listInTransitLongerThan(15, asOf)));
        assertTrue(shipments.listInTransitLongerThan(20, asOf).isEmpty());
        assertEquals(3, shipments.listInTransitLongerThan(0, asOf).size());
        shipments.markDelivered("SO-1", day.plusDays(2));
        assertEquals(List.of("RR-2"), tracking(shipments.listInTransitLongerThan(10, asOf)));
        assertThrows(IllegalArgumentException.class, () -> shipments.listInTransitLongerThan(-1, asOf));
        assertThrows(IllegalArgumentException.class, () -> shipments.listInTransitLongerThan(1, null));
    }

    @Test
    void invalidDeliveries() {
        assertThrows(IllegalStateException.class, () -> shipments.markDelivered("SO-4", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.markDelivered("SO-9", day));
        assertThrows(IllegalArgumentException.class, () -> shipments.markDelivered("SO-1", null));
        assertThrows(IllegalStateException.class, () -> shipments.markDelivered("SO-2", day));
        shipments.markDelivered("SO-1", day.plusDays(1));
        assertThrows(IllegalStateException.class, () -> shipments.markDelivered("SO-1", day.plusDays(2)));
        assertEquals(day.plusDays(1), shipments.findByOrder("SO-1").orElseThrow().deliveryDate());
    }
}
