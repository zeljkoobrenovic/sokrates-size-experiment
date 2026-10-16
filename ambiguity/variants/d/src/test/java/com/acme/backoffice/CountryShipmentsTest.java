package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CountryShipmentCount;
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
import com.acme.backoffice.service.CountryShipmentsService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryShipmentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CountryShipmentsService service = new CountryShipmentsService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse warehouse = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Carrier carrier = store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
    private int counter;

    private Customer customer(String country) {
        counter++;
        return store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, country, Region.NORTH, CustomerStatus.ACTIVE));
    }

    private void shipment(String tracking, Customer customer, LocalDate date) {
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + tracking, customer.id(), date, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.createShipment(new Shipment(0, order.id(), warehouse.id(), carrier.id(), tracking, date));
    }

    private void fill() {
        Customer uk1 = customer("UK");
        Customer uk2 = customer("UK");
        Customer de = customer("DE");
        Customer fr = customer("FR");
        shipment("A", uk1, LocalDate.of(2024, 1, 20));
        shipment("B", uk2, LocalDate.of(2024, 1, 1));
        shipment("C", uk1, LocalDate.of(2024, 1, 31));
        shipment("D", uk1, LocalDate.of(2023, 12, 31));
        shipment("E", uk1, LocalDate.of(2024, 2, 1));
        shipment("G", de, LocalDate.of(2024, 1, 10));
        shipment("H", fr, LocalDate.of(2024, 1, 11));
        shipment("I", fr, LocalDate.of(2024, 1, 12));
    }

    @Test
    void listsShipmentsOfCountryInPeriodOldestFirst() {
        fill();

        List<Shipment> shipments = service.shipments("UK", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        assertEquals(List.of("B", "A", "C"), shipments.stream().map(Shipment::trackingNumber).toList());
        assertEquals(List.of(), service.shipments("IT", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void countsShipmentsPerCountryHighestFirst() {
        fill();

        assertEquals(List.of(new CountryShipmentCount("UK", 3), new CountryShipmentCount("FR", 2), new CountryShipmentCount("DE", 1)),
                service.countPerCountry(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void emptyPeriodGivesEmptyResults() {
        assertEquals(List.of(), service.countPerCountry(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31)));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.countPerCountry(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.shipments("UK", null, LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.shipments(" ", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2)));
    }
}
