package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WeekdayReturns;
import com.acme.backoffice.service.WeekdayReturnsService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeekdayReturnsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final WeekdayReturnsService service = new WeekdayReturnsService(store);
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
    void countsReturnsPerWeekdayOverallAndPerRegion() {
        Customer north = customer(Region.NORTH);
        Customer south = customer(Region.SOUTH);
        Customer east = customer(Region.EAST);
        salesReturn(north, LocalDate.of(2024, 3, 4));
        salesReturn(south, LocalDate.of(2024, 3, 11));
        salesReturn(east, LocalDate.of(2024, 3, 11));
        salesReturn(north, LocalDate.of(2024, 3, 10));
        salesReturn(north, LocalDate.of(2024, 2, 26));
        salesReturn(north, LocalDate.of(2024, 3, 18));

        List<WeekdayReturns> days = service.report(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 17));

        assertEquals(7, days.size());
        assertEquals(DayOfWeek.MONDAY, days.get(0).weekday());
        assertEquals(3, days.get(0).total());
        assertEquals(1, days.get(0).perRegion().get(Region.NORTH));
        assertEquals(1, days.get(0).perRegion().get(Region.SOUTH));
        assertEquals(1, days.get(0).perRegion().get(Region.EAST));
        assertEquals(0, days.get(0).perRegion().get(Region.WEST));
        assertEquals(0, days.get(1).total());
        assertEquals(DayOfWeek.SUNDAY, days.get(6).weekday());
        assertEquals(1, days.get(6).total());
        assertEquals(1, days.get(6).perRegion().get(Region.NORTH));
    }

    @Test
    void rangeWithoutReturnsHasSevenEmptyDays() {
        List<WeekdayReturns> days = service.report(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31));

        assertEquals(7, days.size());
        for (WeekdayReturns day : days) {
            assertEquals(0, day.total());
            assertEquals(4, day.perRegion().size());
        }
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.report(LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.report(null, LocalDate.of(2024, 3, 1)));
    }
}
