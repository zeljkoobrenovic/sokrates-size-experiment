package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.ShipmentStatus;
import com.acme.backoffice.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipmentSearchTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
    private final Carrier fast = store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50")));
    private final Carrier road = store.createCarrier(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80")));

    private void ship(String number, Customer customer, Warehouse warehouse, Carrier carrier, LocalDate shipDate, LocalDate delivered) {
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, number, customer.id(), LocalDate.of(2024, 1, 1), OrderStatus.CONFIRMED, List.of()));
        store.createShipment(new Shipment(0, order.id(), warehouse.id(), carrier.id(), "T-" + number, shipDate));
        if (delivered != null) {
            store.updateShipmentDeliveryDate(order.id(), delivered);
        }
    }

    private void setUp() {
        ship("SO-1", alpha, north, fast, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 3));
        ship("SO-2", beta, east, road, LocalDate.of(2024, 2, 10), null);
        ship("SO-3", alpha, north, road, LocalDate.of(2024, 3, 1), null);
        ship("SO-4", alpha, east, fast, LocalDate.of(2024, 3, 5), LocalDate.of(2024, 3, 9));
    }

    private List<String> tracking(List<Shipment> found) {
        return found.stream().map(Shipment::trackingNumber).toList();
    }

    @Test
    void withoutCriteriaReturnsAllOldestFirst() {
        setUp();

        assertEquals(List.of("T-SO-1", "T-SO-2", "T-SO-3", "T-SO-4"), tracking(store.searchShipments(null, null, null, null, null, null)));
    }

    @Test
    void filtersBySingleCriterion() {
        setUp();

        assertEquals(List.of("T-SO-1", "T-SO-4"), tracking(store.searchShipments(fast.id(), null, null, null, null, null)));
        assertEquals(List.of("T-SO-2", "T-SO-4"), tracking(store.searchShipments(null, east.id(), null, null, null, null)));
        assertEquals(List.of("T-SO-2"), tracking(store.searchShipments(null, null, beta.id(), null, null, null)));
        assertEquals(List.of("T-SO-2", "T-SO-3"), tracking(store.searchShipments(null, null, null, ShipmentStatus.IN_TRANSIT, null, null)));
        assertEquals(List.of("T-SO-1", "T-SO-4"), tracking(store.searchShipments(null, null, null, ShipmentStatus.DELIVERED, null, null)));
    }

    @Test
    void dateRangeIsInclusive() {
        setUp();

        assertEquals(List.of("T-SO-2", "T-SO-3"), tracking(store.searchShipments(null, null, null, null, LocalDate.of(2024, 2, 10), LocalDate.of(2024, 3, 1))));
        assertEquals(List.of("T-SO-3", "T-SO-4"), tracking(store.searchShipments(null, null, null, null, LocalDate.of(2024, 3, 1), null)));
        assertEquals(List.of("T-SO-1"), tracking(store.searchShipments(null, null, null, null, null, LocalDate.of(2024, 2, 9))));
    }

    @Test
    void combinesCriteria() {
        setUp();

        assertEquals(List.of("T-SO-3"), tracking(store.searchShipments(road.id(), north.id(), alpha.id(), ShipmentStatus.IN_TRANSIT,
                LocalDate.of(2024, 2, 15), LocalDate.of(2024, 3, 31))));
        assertEquals(List.of("T-SO-4"), tracking(store.searchShipments(fast.id(), east.id(), alpha.id(), ShipmentStatus.DELIVERED, null, null)));
        assertTrue(store.searchShipments(road.id(), null, null, ShipmentStatus.DELIVERED, null, null).isEmpty());
    }
}
