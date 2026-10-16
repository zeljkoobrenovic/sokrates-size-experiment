package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.QuarterReturns;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.QuarterReturnsService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuarterReturnsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final QuarterReturnsService service = new QuarterReturnsService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse warehouse = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private int counter;

    private Customer customer(Region region) {
        counter++;
        return store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", region, CustomerStatus.ACTIVE));
    }

    private void salesReturn(Customer customer, LocalDate date) {
        counter++;
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.DELIVERED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.createSalesReturn(new SalesReturn(0, "RET-" + counter, order.id(), date, "Damaged",
                List.of(new ReturnLine(widget.id(), 1))), warehouse.id());
    }

    @Test
    void countsReturnsPerQuarterOverallAndPerRegion() {
        Customer north = customer(Region.NORTH);
        Customer south = customer(Region.SOUTH);
        Customer east = customer(Region.EAST);
        Customer west = customer(Region.WEST);
        salesReturn(north, LocalDate.of(2024, 1, 1));
        salesReturn(north, LocalDate.of(2024, 3, 31));
        salesReturn(south, LocalDate.of(2024, 3, 15));
        salesReturn(east, LocalDate.of(2024, 4, 1));
        salesReturn(west, LocalDate.of(2024, 12, 31));
        salesReturn(north, LocalDate.of(2023, 12, 31));
        salesReturn(north, LocalDate.of(2025, 1, 1));

        List<QuarterReturns> quarters = service.report(2024);

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
    void yearWithoutReturnsHasFourEmptyQuarters() {
        List<QuarterReturns> quarters = service.report(2030);

        assertEquals(4, quarters.size());
        for (QuarterReturns quarter : quarters) {
            assertEquals(0, quarter.total());
            assertEquals(4, quarter.perRegion().size());
        }
    }

    @Test
    void rejectsInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> service.report(0));
    }
}
