package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.QuarterShipments;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.QuarterShipmentsService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuarterShipmentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final QuarterShipmentsService service = new QuarterShipmentsService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse warehouse = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Carrier carrier = store.carriers().create(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 2));
    private int counter;

    private Customer customer(Region region) {
        counter++;
        return store.customers().create(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", region, CustomerStatus.ACTIVE));
    }

    private void shipment(Customer customer, LocalDate date) {
        counter++;
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.shipments().create(new Shipment(0, order.id(), warehouse.id(), carrier.id(), "T-" + counter, date));
    }

    @Test
    void countsShipmentsPerQuarterOverallAndPerRegion() {
        Customer north = customer(Region.NORTH);
        Customer south = customer(Region.SOUTH);
        Customer east = customer(Region.EAST);
        Customer west = customer(Region.WEST);
        shipment(north, LocalDate.of(2024, 1, 1));
        shipment(north, LocalDate.of(2024, 3, 31));
        shipment(south, LocalDate.of(2024, 3, 15));
        shipment(east, LocalDate.of(2024, 4, 1));
        shipment(west, LocalDate.of(2024, 12, 31));
        shipment(north, LocalDate.of(2023, 12, 31));
        shipment(north, LocalDate.of(2025, 1, 1));

        List<QuarterShipments> quarters = service.report(2024);

        assertEquals(4, quarters.size());
        assertEquals(1, quarters.get(0).quarter());
        assertEquals(3, quarters.get(0).total());
        assertEquals(2, quarters.get(0).perRegion().get(Region.NORTH));
        assertEquals(1, quarters.get(0).perRegion().get(Region.SOUTH));
        assertEquals(0, quarters.get(0).perRegion().get(Region.EAST));
        assertEquals(0, quarters.get(0).perRegion().get(Region.WEST));
        assertEquals(1, quarters.get(1).total());
        assertEquals(1, quarters.get(1).perRegion().get(Region.EAST));
        assertEquals(0, quarters.get(2).total());
        assertEquals(4, quarters.get(3).quarter());
        assertEquals(1, quarters.get(3).total());
        assertEquals(1, quarters.get(3).perRegion().get(Region.WEST));
    }

    @Test
    void yearWithoutShipmentsHasFourEmptyQuarters() {
        List<QuarterShipments> quarters = service.report(2030);

        assertEquals(4, quarters.size());
        for (QuarterShipments quarter : quarters) {
            assertEquals(0, quarter.total());
            assertEquals(4, quarter.perRegion().size());
        }
    }

    @Test
    void rejectsInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> service.report(0));
    }
}
