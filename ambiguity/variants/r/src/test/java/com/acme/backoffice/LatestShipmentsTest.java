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
import com.acme.backoffice.service.LatestShipmentsService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LatestShipmentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final LatestShipmentsService service = new LatestShipmentsService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse warehouse = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Carrier carrier = store.carriers().create(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
    private final Customer north = store.customers().create(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.customers().create(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));

    private void shipment(String tracking, Customer customer, LocalDate date) {
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + tracking, customer.id(), date, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.shipments().create(new Shipment(0, order.id(), warehouse.id(), carrier.id(), tracking, date));
    }

    private List<String> trackings(List<Shipment> shipments) {
        return shipments.stream().map(Shipment::trackingNumber).toList();
    }

    private void fill() {
        shipment("A", north, LocalDate.of(2024, 1, 1));
        shipment("B", south, LocalDate.of(2024, 1, 5));
        shipment("C", north, LocalDate.of(2024, 1, 3));
        shipment("D", south, LocalDate.of(2024, 1, 9));
        shipment("E", south, LocalDate.of(2024, 1, 5));
    }

    @Test
    void listsLatestShipmentsOverall() {
        fill();
        assertEquals(List.of("D", "E", "B"), trackings(service.overall(3)));
        assertEquals(5, service.overall(10).size());
    }

    @Test
    void listsLatestShipmentsPerRegion() {
        fill();
        assertEquals(List.of("C", "A"), trackings(service.forRegion(Region.NORTH, 5)));
        assertEquals(List.of("D"), trackings(service.forRegion(Region.SOUTH, 1)));
        assertEquals(List.of(), trackings(service.forRegion(Region.EAST, 5)));
    }

    @Test
    void listsLatestShipmentsPerCustomer() {
        fill();
        assertEquals(List.of("D", "E"), trackings(service.forCustomer(south.id(), 2)));
        assertEquals(List.of("C"), trackings(service.forCustomer(north.id(), 1)));
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.overall(0));
        assertThrows(IllegalArgumentException.class, () -> service.forRegion(null, 1));
        assertThrows(IllegalArgumentException.class, () -> service.forCustomer(9999, 1));
    }
}
