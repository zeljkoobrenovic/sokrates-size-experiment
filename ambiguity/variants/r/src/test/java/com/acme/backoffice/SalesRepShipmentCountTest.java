package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesRepShipmentCount;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.SalesRepShipmentCountService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalesRepShipmentCountTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRepShipmentCountService service = new SalesRepShipmentCountService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse warehouse = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Carrier carrier = store.carriers().create(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
    private int counter;

    private SalesRep rep(String name) {
        return store.salesReps().create(new SalesRep(0, name, name + "@example.com", Region.NORTH));
    }

    private Customer customer(SalesRep rep) {
        counter++;
        Customer customer = store.customers().create(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        if (rep != null) {
            store.customers().assignToSalesRep(customer.id(), rep.id());
        }
        return customer;
    }

    private void shipment(Customer customer, LocalDate date) {
        counter++;
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.shipments().create(new Shipment(0, order.id(), warehouse.id(), carrier.id(), "T-" + counter, date));
    }

    private String describe(SalesRepShipmentCount count) {
        return count.name() + ":" + count.shipmentCount();
    }

    @Test
    void countsShipmentsPerRepHighestFirst() {
        SalesRep anna = rep("Anna");
        SalesRep bob = rep("Bob");
        SalesRep carl = rep("Carl");
        SalesRep dora = rep("Dora");
        Customer annaCustomer = customer(anna);
        Customer bobOne = customer(bob);
        Customer bobTwo = customer(bob);
        Customer carlCustomer = customer(carl);
        customer(dora);
        Customer unassigned = customer(null);
        shipment(annaCustomer, LocalDate.of(2024, 1, 1));
        shipment(bobOne, LocalDate.of(2024, 1, 15));
        shipment(bobTwo, LocalDate.of(2024, 1, 31));
        shipment(carlCustomer, LocalDate.of(2024, 1, 10));
        shipment(bobOne, LocalDate.of(2023, 12, 31));
        shipment(bobOne, LocalDate.of(2024, 2, 1));
        shipment(unassigned, LocalDate.of(2024, 1, 5));

        assertEquals(List.of("Bob:2", "Anna:1", "Carl:1", "Dora:0"),
                service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)).stream().map(this::describe).toList());
    }

    @Test
    void noRepsGivesEmptyReport() {
        assertEquals(List.of(), service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.report(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.report(null, LocalDate.of(2024, 1, 1)));
    }
}
