package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionShipments;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.ShipmentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegionShipmentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ShipmentService shipments = new ShipmentService(store);
    private final Customer north = store.createCustomer(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.createCustomer(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse warehouse = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Carrier carrier = store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
    private int counter;

    private Shipment ship(Customer customer, LocalDate shipDate, LocalDate deliveryDate) {
        counter++;
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), shipDate.minusDays(1),
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.50")))));
        Shipment shipment = store.createShipment(new Shipment(0, order.id(), warehouse.id(), carrier.id(), "T-" + counter, shipDate));
        if (deliveryDate != null) {
            store.setShipmentDeliveryDateIfInTransit(order.id(), deliveryDate);
        }
        return shipment;
    }

    @Test
    void listsInTransitAndLateShipmentsOfTheRegionInThePeriod() {
        Shipment transit = ship(north, LocalDate.of(2024, 2, 10), null);
        Shipment late = ship(north, LocalDate.of(2024, 2, 5), LocalDate.of(2024, 2, 8));
        ship(north, LocalDate.of(2024, 2, 6), LocalDate.of(2024, 2, 8));
        ship(north, LocalDate.of(2024, 1, 31), null);
        ship(north, LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 20));
        ship(south, LocalDate.of(2024, 2, 7), null);
        ship(south, LocalDate.of(2024, 2, 7), LocalDate.of(2024, 2, 20));

        RegionShipments result = shipments.listByRegion(Region.NORTH, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 1));

        assertEquals(Region.NORTH, result.region());
        assertEquals(List.of(transit.id()), result.inTransit().stream().map(Shipment::id).toList());
        assertEquals(List.of(late.id()), result.late().stream().map(Shipment::id).toList());
    }

    @Test
    void returnsNothingWhenThereAreNoShipments() {
        ship(north, LocalDate.of(2024, 2, 10), null);
        RegionShipments result = shipments.listByRegion(Region.SOUTH, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28));
        assertEquals(List.of(), result.inTransit());
        assertEquals(List.of(), result.late());
    }

    @Test
    void validatesArguments() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> shipments.listByRegion(null, day, day));
        assertThrows(IllegalArgumentException.class, () -> shipments.listByRegion(Region.NORTH, null, day));
        assertThrows(IllegalArgumentException.class, () -> shipments.listByRegion(Region.NORTH, day, null));
        assertThrows(IllegalArgumentException.class, () -> shipments.listByRegion(Region.NORTH, day, day.minusDays(1)));
    }
}
