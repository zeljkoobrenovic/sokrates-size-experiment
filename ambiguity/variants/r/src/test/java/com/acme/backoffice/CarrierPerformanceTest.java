package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CarrierPerformance;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ServiceLevel;
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

class CarrierPerformanceTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ShipmentService shipments = new ShipmentService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    @BeforeEach
    void setUp() {
        Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        Warehouse north = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        store.carriers().create(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
        store.carriers().create(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80"), 5));
        store.carriers().create(new Carrier(0, "Idle", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        new StockService(store).setQuantity(north.id(), widget.id(), 100);
        for (int i = 1; i <= 6; i++) {
            store.orders().create(new SalesOrder(0, "SO-" + i, customer.id(), day, OrderStatus.CONFIRMED,
                    List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.50")))));
        }
        shipments.ship("SO-1", "WH-N", "FastFreight", "FF-1", day);
        shipments.ship("SO-2", "WH-N", "FastFreight", "FF-2", day.plusDays(1));
        shipments.ship("SO-3", "WH-N", "FastFreight", "FF-3", day.plusDays(2));
        shipments.ship("SO-4", "WH-N", "RoadRunner", "RR-4", day.plusDays(1));
        shipments.ship("SO-5", "WH-N", "RoadRunner", "RR-5", day.plusDays(40));
        shipments.markDelivered("SO-1", day.plusDays(2));
        shipments.markDelivered("SO-2", day.plusDays(6));
        shipments.markDelivered("SO-4", day.plusDays(4));
    }

    @Test
    void countsAverageAndLateShipmentsPerCarrier() {
        List<CarrierPerformance> result = shipments.getCarrierPerformance(day, day.plusDays(30));
        assertEquals(2, result.size());
        CarrierPerformance fast = result.get(0);
        assertEquals("FastFreight", fast.carrier().name());
        assertEquals(2, fast.carrier().promisedDays());
        assertEquals(3, fast.shipmentCount());
        assertEquals(new BigDecimal("3.50"), fast.averageDaysToDeliver());
        assertEquals(1, fast.lateCount());
        CarrierPerformance road = result.get(1);
        assertEquals("RoadRunner", road.carrier().name());
        assertEquals(1, road.shipmentCount());
        assertEquals(new BigDecimal("3.00"), road.averageDaysToDeliver());
        assertEquals(0, road.lateCount());
    }

    @Test
    void onlyShipmentsOfThePeriodCount() {
        List<CarrierPerformance> result = shipments.getCarrierPerformance(day.plusDays(1), day.plusDays(1));
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).shipmentCount());
        assertEquals(1, result.get(0).lateCount());
        assertEquals(1, result.get(1).shipmentCount());
        assertTrue(shipments.getCarrierPerformance(day.minusDays(10), day.minusDays(1)).isEmpty());
    }

    @Test
    void undeliveredShipmentsAreCountedButNeverLate() {
        List<CarrierPerformance> result = shipments.getCarrierPerformance(day.plusDays(40), day.plusDays(40));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).shipmentCount());
        assertNull(result.get(0).averageDaysToDeliver());
        assertEquals(0, result.get(0).lateCount());
    }

    @Test
    void invalidPeriod() {
        assertThrows(IllegalArgumentException.class, () -> shipments.getCarrierPerformance(null, day));
        assertThrows(IllegalArgumentException.class, () -> shipments.getCarrierPerformance(day, null));
        assertThrows(IllegalArgumentException.class, () -> shipments.getCarrierPerformance(day.plusDays(1), day));
    }

    @Test
    void promisedDaysIsStored() {
        assertEquals(2, store.carriers().findByName("FastFreight").orElseThrow().promisedDays());
        assertEquals(Carrier.DEFAULT_PROMISED_DAYS, store.carriers().findByName("Idle").orElseThrow().promisedDays());
    }
}
