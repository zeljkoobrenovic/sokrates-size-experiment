package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductShipments;
import com.acme.backoffice.model.Region;
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

class ProductShipmentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ShipmentService shipments = new ShipmentService(store);
    private final Customer customer = store.customers().create(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("9.00"), "piece", true));
    private final Warehouse whA = store.warehouses().create(new Warehouse(0, "WH-A", "A", "Oslo", Region.NORTH));
    private final Warehouse whB = store.warehouses().create(new Warehouse(0, "WH-B", "B", "Bergen", Region.NORTH));
    private final Carrier carrier = store.carriers().create(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
    private int counter;

    private Shipment ship(Warehouse warehouse, LocalDate shipDate, OrderLine... lines) {
        counter++;
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), shipDate.minusDays(1),
                OrderStatus.SHIPPED, List.of(lines)));
        return store.shipments().create(new Shipment(0, order.id(), warehouse.id(), carrier.id(), "T-" + counter, shipDate));
    }

    private OrderLine line(Product product, int quantity) {
        return new OrderLine(product.id(), quantity, product.unitPrice());
    }

    @Test
    void listsShipmentsAndQuantityPerWarehouse() {
        Shipment first = ship(whB, LocalDate.of(2024, 2, 5), line(widget, 3), line(gadget, 1));
        Shipment second = ship(whA, LocalDate.of(2024, 2, 1), line(widget, 4));
        Shipment third = ship(whB, LocalDate.of(2024, 2, 20), line(widget, 2));
        ship(whA, LocalDate.of(2024, 2, 10), line(gadget, 7));
        ship(whA, LocalDate.of(2024, 1, 31), line(widget, 10));
        ship(whA, LocalDate.of(2024, 2, 21), line(widget, 10));

        ProductShipments result = shipments.listByProduct("W-1", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 20));

        assertEquals(widget, result.product());
        assertEquals(List.of(second.id(), first.id(), third.id()), result.shipments().stream().map(Shipment::id).toList());
        assertEquals(List.of("WH-A", "WH-B"), result.quantities().stream().map(q -> q.warehouse().code()).toList());
        assertEquals(List.of(4, 5), result.quantities().stream().map(q -> q.quantity()).toList());
    }

    @Test
    void countsPartsOfPartiallyShippedOrders() {
        Shipment whole = ship(whA, LocalDate.of(2024, 2, 5), line(widget, 6));
        Shipment split = ship(whB, LocalDate.of(2024, 2, 9), line(widget, 5));
        store.shipments().createPart(split.orderId(), 1, whA.id(), carrier.id(), "P-1", LocalDate.of(2024, 2, 8), 2);
        store.shipments().createPart(split.orderId(), 1, whB.id(), carrier.id(), "P-2", LocalDate.of(2024, 2, 9), 3);

        ProductShipments result = shipments.listByProduct("W-1", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28));

        assertEquals(List.of(whole.id(), split.id()), result.shipments().stream().map(Shipment::id).toList());
        assertEquals(List.of(8, 3), result.quantities().stream().map(q -> q.quantity()).toList());
    }

    @Test
    void returnsNothingWhenProductWasNotShipped() {
        ship(whA, LocalDate.of(2024, 2, 5), line(widget, 6));
        ProductShipments result = shipments.listByProduct("G-1", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28));
        assertEquals(List.of(), result.shipments());
        assertEquals(List.of(), result.quantities());
    }

    @Test
    void validatesArguments() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> shipments.listByProduct("NOPE", day, day));
        assertThrows(IllegalArgumentException.class, () -> shipments.listByProduct("W-1", null, day));
        assertThrows(IllegalArgumentException.class, () -> shipments.listByProduct("W-1", day, null));
        assertThrows(IllegalArgumentException.class, () -> shipments.listByProduct("W-1", day, day.minusDays(1)));
    }
}
