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
import com.acme.backoffice.model.WarehouseLateShipments;
import com.acme.backoffice.service.ShipmentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LateShipmentsByWarehouseTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ShipmentService shipments = new ShipmentService(store);
    private final Customer customer = store.createCustomer(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse whA = store.createWarehouse(new Warehouse(0, "WH-A", "A", "Oslo", Region.NORTH));
    private final Warehouse whB = store.createWarehouse(new Warehouse(0, "WH-B", "B", "Bergen", Region.NORTH));
    private final Warehouse whC = store.createWarehouse(new Warehouse(0, "WH-C", "C", "Tromso", Region.NORTH));
    private final Carrier carrier = store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
    private int counter;

    private Shipment ship(Warehouse warehouse, LocalDate shipDate, Integer daysToDeliver) {
        counter++;
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), shipDate.minusDays(1),
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, widget.unitPrice()))));
        Shipment shipment = store.createShipment(new Shipment(0, order.id(), warehouse.id(), carrier.id(), "T-" + counter, shipDate));
        if (daysToDeliver != null) {
            store.updateShipmentDeliveryDate(order.id(), shipDate.plusDays(daysToDeliver));
        }
        return shipment;
    }

    @Test
    void listsLateShipmentsAndShareOfDeliveredPerWarehouse() {
        Shipment lateB = ship(whB, LocalDate.of(2024, 2, 9), 5);
        Shipment lateA2 = ship(whA, LocalDate.of(2024, 2, 8), 3);
        Shipment lateA1 = ship(whA, LocalDate.of(2024, 2, 1), 4);
        ship(whA, LocalDate.of(2024, 2, 3), 2);
        ship(whA, LocalDate.of(2024, 2, 4), 1);
        ship(whA, LocalDate.of(2024, 2, 5), null);
        ship(whB, LocalDate.of(2024, 2, 10), 2);
        ship(whB, LocalDate.of(2024, 2, 11), 1);
        ship(whC, LocalDate.of(2024, 2, 12), 1);
        ship(whC, LocalDate.of(2024, 1, 31), 9);
        ship(whA, LocalDate.of(2024, 3, 1), 9);

        List<WarehouseLateShipments> result = shipments.listLateByWarehouse(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29));

        assertEquals(List.of("WH-A", "WH-B", "WH-C"), result.stream().map(r -> r.warehouse().code()).toList());
        assertEquals(List.of(lateA1.id(), lateA2.id()), result.get(0).late().stream().map(Shipment::id).toList());
        assertEquals(List.of(lateB.id()), result.get(1).late().stream().map(Shipment::id).toList());
        assertEquals(List.of(), result.get(2).late());
        assertEquals(List.of(4, 3, 1), result.stream().map(WarehouseLateShipments::delivered).toList());
        assertEquals(List.of(new BigDecimal("50.0"), new BigDecimal("33.3"), new BigDecimal("0.0")),
                result.stream().map(WarehouseLateShipments::latePercent).toList());
    }

    @Test
    void returnsNothingWithoutDeliveries() {
        ship(whA, LocalDate.of(2024, 2, 5), null);
        assertEquals(List.of(), shipments.listLateByWarehouse(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29)));
    }

    @Test
    void validatesArguments() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> shipments.listLateByWarehouse(null, day));
        assertThrows(IllegalArgumentException.class, () -> shipments.listLateByWarehouse(day, null));
        assertThrows(IllegalArgumentException.class, () -> shipments.listLateByWarehouse(day, day.minusDays(1)));
    }
}
