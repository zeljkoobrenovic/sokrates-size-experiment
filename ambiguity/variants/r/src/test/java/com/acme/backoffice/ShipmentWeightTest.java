package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CarrierShippedWeight;
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
import com.acme.backoffice.model.WarehouseShippedWeight;
import com.acme.backoffice.service.ShipmentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShipmentWeightTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ShipmentService shipments = new ShipmentService(store);
    private final Customer customer = store.customers().create(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product heavy = store.products().create(new Product(0, "H-1", "Heavy", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product light = store.products().create(new Product(0, "L-1", "Light", "Hardware", new BigDecimal("1.50"), "piece", true));
    private final Product unweighed = store.products().create(new Product(0, "U-1", "Unweighed", "Hardware", new BigDecimal("1.00"), "piece", true));
    private final Warehouse whA = store.warehouses().create(new Warehouse(0, "WH-A", "A", "Oslo", Region.NORTH));
    private final Warehouse whB = store.warehouses().create(new Warehouse(0, "WH-B", "B", "Bergen", Region.NORTH));
    private final Warehouse whC = store.warehouses().create(new Warehouse(0, "WH-C", "C", "Tromso", Region.NORTH));
    private final Carrier carrierX = store.carriers().create(new Carrier(0, "XCarrier", ServiceLevel.EXPRESS, new BigDecimal("2.50")));
    private final Carrier carrierA = store.carriers().create(new Carrier(0, "ACarrier", ServiceLevel.STANDARD, new BigDecimal("1.50")));
    private final Carrier carrierZ = store.carriers().create(new Carrier(0, "ZCarrier", ServiceLevel.STANDARD, new BigDecimal("1.50")));
    private int counter;

    private SalesOrder order(LocalDate orderDate, OrderLine... lines) {
        counter++;
        return store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), orderDate, OrderStatus.SHIPPED, List.of(lines)));
    }

    private void ship(Warehouse warehouse, Carrier carrier, LocalDate shipDate, int heavyQuantity, int lightQuantity) {
        SalesOrder order = order(shipDate.minusDays(1), new OrderLine(heavy.id(), heavyQuantity, heavy.unitPrice()),
                new OrderLine(light.id(), lightQuantity, light.unitPrice()),
                new OrderLine(unweighed.id(), 7, unweighed.unitPrice()));
        store.shipments().create(new Shipment(0, order.id(), warehouse.id(), carrier.id(), "T-" + counter, shipDate));
    }

    @Test
    void sumsShippedWeightPerCarrierAndWarehouse() {
        shipments.setProductWeight("H-1", new BigDecimal("2.500"));
        shipments.setProductWeight("L-1", new BigDecimal("0.400"));
        ship(whB, carrierX, LocalDate.of(2024, 2, 1), 2, 5);
        ship(whA, carrierX, LocalDate.of(2024, 2, 29), 1, 1);
        ship(whA, carrierA, LocalDate.of(2024, 2, 10), 3, 10);
        ship(whC, carrierZ, LocalDate.of(2024, 3, 1), 4, 4);
        ship(whC, carrierZ, LocalDate.of(2024, 1, 31), 4, 4);

        // a split order counts the parts at their own dates, carriers and warehouses
        SalesOrder split = order(LocalDate.of(2024, 2, 4), new OrderLine(heavy.id(), 4, heavy.unitPrice()));
        store.shipments().create(new Shipment(0, split.id(), whC.id(), carrierZ.id(), "T-SPLIT", LocalDate.of(2024, 2, 5)));
        store.shipments().createPart(split.id(), 1, whC.id(), carrierZ.id(), "T-P1", LocalDate.of(2024, 2, 5), 1);
        store.shipments().createPart(split.id(), 1, whA.id(), carrierA.id(), "T-P2", LocalDate.of(2024, 2, 6), 3);
        store.shipments().createPart(split.id(), 1, whA.id(), carrierA.id(), "T-P3", LocalDate.of(2024, 3, 2), 9);

        LocalDate from = LocalDate.of(2024, 2, 1);
        LocalDate to = LocalDate.of(2024, 2, 29);
        List<CarrierShippedWeight> byCarrier = shipments.shippedWeightByCarrier(from, to);
        assertEquals(List.of("ACarrier", "XCarrier", "ZCarrier"), byCarrier.stream().map(r -> r.carrier().name()).toList());
        assertEquals(List.of("19.000", "9.900", "2.500"),
                byCarrier.stream().map(r -> r.weightKg().toPlainString()).toList());

        List<WarehouseShippedWeight> byWarehouse = shipments.shippedWeightByWarehouse(from, to);
        assertEquals(List.of("WH-A", "WH-B", "WH-C"), byWarehouse.stream().map(r -> r.warehouse().code()).toList());
        assertEquals(List.of("21.900", "7.000", "2.500"),
                byWarehouse.stream().map(r -> r.weightKg().toPlainString()).toList());
    }

    @Test
    void leavesOutShipmentsWithoutWeight() {
        ship(whA, carrierX, LocalDate.of(2024, 2, 1), 2, 5);
        assertEquals(List.of(), shipments.shippedWeightByCarrier(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29)));
        assertEquals(List.of(), shipments.shippedWeightByWarehouse(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29)));
    }

    @Test
    void validatesArguments() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> shipments.shippedWeightByCarrier(null, day));
        assertThrows(IllegalArgumentException.class, () -> shipments.shippedWeightByWarehouse(day, null));
        assertThrows(IllegalArgumentException.class, () -> shipments.shippedWeightByCarrier(day, day.minusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> shipments.shippedWeightByWarehouse(day, day.minusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> shipments.setProductWeight("NOPE", BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> shipments.setProductWeight("H-1", new BigDecimal("-1")));
    }
}
