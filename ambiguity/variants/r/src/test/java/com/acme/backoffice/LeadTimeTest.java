package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CarrierLeadTime;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionLeadTime;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeadTimeTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ShipmentService shipments = new ShipmentService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    private void order(String number, Customer customer, LocalDate date) {
        Product widget = store.products().findBySku("W-1").orElseThrow();
        store.orders().create(new SalesOrder(0, number, customer.id(), date, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.50")))));
    }

    @BeforeEach
    void setUp() {
        Customer north = store.customers().create(new Customer(0, "Nord", "n@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer south = store.customers().create(new Customer(0, "Sud", "s@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        Warehouse wh = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        store.carriers().create(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
        store.carriers().create(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80"), 5));
        new StockService(store).setQuantity(wh.id(), widget.id(), 100);
        order("SO-1", north, day);
        order("SO-2", north, day.plusDays(1));
        order("SO-3", south, day.plusDays(2));
        order("SO-4", south, day.plusDays(3));
        order("SO-5", north, day.plusDays(40));
        shipments.ship("SO-1", "WH-N", "FastFreight", "FF-1", day.plusDays(1));
        shipments.ship("SO-2", "WH-N", "RoadRunner", "RR-2", day.plusDays(2));
        shipments.ship("SO-3", "WH-N", "FastFreight", "FF-3", day.plusDays(3));
        shipments.ship("SO-4", "WH-N", "FastFreight", "FF-4", day.plusDays(4));
        shipments.ship("SO-5", "WH-N", "FastFreight", "FF-5", day.plusDays(41));
        shipments.markDelivered("SO-1", day.plusDays(2));
        shipments.markDelivered("SO-2", day.plusDays(7));
        shipments.markDelivered("SO-3", day.plusDays(6));
        shipments.markDelivered("SO-5", day.plusDays(50));
    }

    @Test
    void averagesDaysFromOrderToDeliveryPerRegion() {
        List<RegionLeadTime> result = shipments.getLeadTimeByRegion(day, day.plusDays(30));
        assertEquals(2, result.size());
        assertEquals(Region.NORTH, result.get(0).region());
        assertEquals(2, result.get(0).orderCount());
        assertEquals(new BigDecimal("4.00"), result.get(0).averageDays());
        assertEquals(Region.SOUTH, result.get(1).region());
        assertEquals(1, result.get(1).orderCount());
        assertEquals(new BigDecimal("4.00"), result.get(1).averageDays());
    }

    @Test
    void averagesDaysFromOrderToDeliveryPerCarrier() {
        List<CarrierLeadTime> result = shipments.getLeadTimeByCarrier(day, day.plusDays(30));
        assertEquals(2, result.size());
        assertEquals("FastFreight", result.get(0).carrier().name());
        assertEquals(2, result.get(0).orderCount());
        assertEquals(new BigDecimal("3.00"), result.get(0).averageDays());
        assertEquals("RoadRunner", result.get(1).carrier().name());
        assertEquals(1, result.get(1).orderCount());
        assertEquals(new BigDecimal("6.00"), result.get(1).averageDays());
    }

    @Test
    void onlyOrdersOfThePeriodCount() {
        List<RegionLeadTime> result = shipments.getLeadTimeByRegion(day.plusDays(40), day.plusDays(40));
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("10.00"), result.get(0).averageDays());
        assertTrue(shipments.getLeadTimeByCarrier(day.minusDays(10), day.minusDays(1)).isEmpty());
        assertTrue(shipments.getLeadTimeByRegion(day.plusDays(3), day.plusDays(3)).isEmpty());
    }

    @Test
    void invalidPeriod() {
        assertThrows(IllegalArgumentException.class, () -> shipments.getLeadTimeByRegion(null, day));
        assertThrows(IllegalArgumentException.class, () -> shipments.getLeadTimeByCarrier(day, null));
        assertThrows(IllegalArgumentException.class, () -> shipments.getLeadTimeByCarrier(day.plusDays(1), day));
    }
}
