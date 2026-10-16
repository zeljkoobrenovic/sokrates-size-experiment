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
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerShipmentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer gamma = store.createCustomer(new Customer(0, "Gamma", "g@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Carrier fast = store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 3));
    private final Carrier slow = store.createCarrier(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80"), 7));
    private int counter;

    private void shipment(Customer customer, Carrier carrier, LocalDate shipDate, LocalDate deliveryDate) {
        counter++;
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), shipDate, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.50")))));
        store.createShipment(new Shipment(0, order.id(), north.id(), carrier.id(), "TR-" + counter, shipDate));
        if (deliveryDate != null) {
            store.updateShipmentDeliveryDate(order.id(), deliveryDate);
        }
    }

    private List<String> tracking(List<Shipment> shipments) {
        return shipments.stream().map(Shipment::trackingNumber).toList();
    }

    @Test
    void listsShipmentsOfTheCustomerBetweenTwoDatesInclusive() {
        shipment(alpha, fast, LocalDate.of(2024, 3, 10), null);
        shipment(alpha, fast, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 3));
        shipment(alpha, fast, LocalDate.of(2024, 2, 28), LocalDate.of(2024, 3, 1));
        shipment(alpha, fast, LocalDate.of(2024, 3, 31), null);
        shipment(beta, fast, LocalDate.of(2024, 3, 5), null);
        assertEquals(List.of("TR-2", "TR-1", "TR-4"),
                tracking(store.listShipmentsOfCustomerShippedBetween(alpha.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31))));
        assertEquals(List.of("TR-2"),
                tracking(store.listShipmentsOfCustomerShippedBetween(alpha.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 1))));
        assertEquals(List.of("TR-5"),
                tracking(store.listShipmentsOfCustomerShippedBetween(beta.id(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))));
        assertTrue(store.listShipmentsOfCustomerShippedBetween(gamma.id(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)).isEmpty());
    }

    @Test
    void listsShipmentsInTransitOfTheCustomerOldestFirst() {
        shipment(alpha, fast, LocalDate.of(2024, 3, 10), null);
        shipment(alpha, fast, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 3));
        shipment(alpha, slow, LocalDate.of(2024, 3, 2), null);
        shipment(beta, fast, LocalDate.of(2024, 3, 5), null);
        assertEquals(List.of("TR-3", "TR-1"), tracking(store.listShipmentsInTransitOfCustomer(alpha.id())));
        assertEquals(List.of("TR-4"), tracking(store.listShipmentsInTransitOfCustomer(beta.id())));
        assertTrue(store.listShipmentsInTransitOfCustomer(gamma.id()).isEmpty());
    }

    @Test
    void listsOnlyShipmentsDeliveredAfterThePromisedDays() {
        shipment(alpha, fast, LocalDate.of(2024, 3, 10), LocalDate.of(2024, 3, 14));
        shipment(alpha, fast, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 4));
        shipment(alpha, slow, LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 12));
        shipment(alpha, slow, LocalDate.of(2024, 3, 3), LocalDate.of(2024, 3, 10));
        shipment(alpha, fast, LocalDate.of(2024, 1, 1), null);
        shipment(beta, fast, LocalDate.of(2024, 3, 5), LocalDate.of(2024, 3, 20));
        assertEquals(List.of("TR-3", "TR-1"), tracking(store.listLateDeliveredShipmentsOfCustomer(alpha.id())));
        assertEquals(List.of("TR-6"), tracking(store.listLateDeliveredShipmentsOfCustomer(beta.id())));
        assertTrue(store.listLateDeliveredShipmentsOfCustomer(gamma.id()).isEmpty());
    }
}
