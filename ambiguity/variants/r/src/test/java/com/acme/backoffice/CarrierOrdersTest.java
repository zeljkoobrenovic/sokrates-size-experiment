package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CarrierOrders;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarrierOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final LocalDate day = LocalDate.of(2024, 6, 1);
    private Carrier fast;
    private Carrier slow;

    @BeforeEach
    void setUp() {
        Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        Warehouse north = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        fast = store.carriers().create(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
        slow = store.carriers().create(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80"), 5));
        store.carriers().create(new Carrier(0, "Idle", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        SalesOrder[] orders = new SalesOrder[4];
        for (int i = 1; i <= 4; i++) {
            orders[i - 1] = store.orders().create(new SalesOrder(0, "SO-" + i, customer.id(), day.plusDays(i), OrderStatus.CONFIRMED,
                    List.of(new OrderLine(widget.id(), i, new BigDecimal("4.50")), new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        }
        // SO-1: 5.50, SO-2: 10.00, SO-3: 14.50, SO-4: 19.00
        store.shipments().create(new Shipment(0, orders[0].id(), north.id(), fast.id(), "FF-1", day.plusDays(10)));
        store.shipments().create(new Shipment(0, orders[1].id(), north.id(), slow.id(), "RR-2", day.plusDays(11)));
        store.shipments().create(new Shipment(0, orders[2].id(), north.id(), fast.id(), "FF-3", day.plusDays(40)));
        store.shipments().createPart(orders[3].id(), 1, north.id(), fast.id(), "FF-4A", day.plusDays(12), 2);
        store.shipments().createPart(orders[3].id(), 1, north.id(), fast.id(), "FF-4B", day.plusDays(13), 2);
        store.shipments().createPart(orders[3].id(), 2, north.id(), slow.id(), "RR-4C", day.plusDays(14), 1);
    }

    @Test
    void listsOrdersOfCarrierShippedInPeriodOnce() {
        List<SalesOrder> result = store.orders().listByCarrierAndPeriod(fast.id(), day, day.plusDays(30));
        assertEquals(List.of("SO-1", "SO-4"), result.stream().map(SalesOrder::number).toList());
        assertEquals(2, result.get(0).lines().size());
    }

    @Test
    void periodBoundsAreInclusive() {
        List<SalesOrder> result = store.orders().listByCarrierAndPeriod(fast.id(), day.plusDays(10), day.plusDays(10));
        assertEquals(List.of("SO-1"), result.stream().map(SalesOrder::number).toList());
        assertTrue(store.orders().listByCarrierAndPeriod(fast.id(), day.plusDays(15), day.plusDays(20)).isEmpty());
    }

    @Test
    void countsOrdersAndValuePerCarrier() {
        List<CarrierOrders> result = store.carriers().ordersPerCarrier(day, day.plusDays(30));
        assertEquals(2, result.size());
        assertEquals("FastFreight", result.get(0).carrier().name());
        assertEquals(2, result.get(0).orderCount());
        assertEquals(0, new BigDecimal("24.50").compareTo(result.get(0).value()));
        assertEquals("RoadRunner", result.get(1).carrier().name());
        assertEquals(2, result.get(1).orderCount());
        assertEquals(0, new BigDecimal("29.00").compareTo(result.get(1).value()));
    }

    @Test
    void leavesOutOrdersShippedOutsideThePeriod() {
        List<CarrierOrders> result = store.carriers().ordersPerCarrier(day.plusDays(35), day.plusDays(45));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).orderCount());
        assertEquals(0, new BigDecimal("14.50").compareTo(result.get(0).value()));
    }
}
