package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.WeekdayInvoices;
import com.acme.backoffice.service.WeekdayInvoicesService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeekdayInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final WeekdayInvoicesService service = new WeekdayInvoicesService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    private Customer customer(Region region) {
        counter++;
        return store.customers().create(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", region, CustomerStatus.ACTIVE));
    }

    private void invoice(Customer customer, LocalDate date, InvoiceStatus status) {
        counter++;
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.invoices().create(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal("10.00"), status));
    }

    @Test
    void countsInvoicesPerWeekdayOverallAndPerRegion() {
        // 2024-01-01 is a Monday
        Customer north = customer(Region.NORTH);
        Customer south = customer(Region.SOUTH);
        Customer east = customer(Region.EAST);
        Customer west = customer(Region.WEST);
        invoice(north, LocalDate.of(2024, 1, 1), InvoiceStatus.OPEN);
        invoice(north, LocalDate.of(2024, 1, 8), InvoiceStatus.PAID);
        invoice(south, LocalDate.of(2024, 1, 15), InvoiceStatus.OPEN);
        invoice(east, LocalDate.of(2024, 1, 7), InvoiceStatus.OPEN);
        invoice(west, LocalDate.of(2024, 1, 3), InvoiceStatus.OPEN);
        invoice(north, LocalDate.of(2024, 1, 2), InvoiceStatus.CANCELLED);
        invoice(north, LocalDate.of(2023, 12, 31), InvoiceStatus.OPEN);
        invoice(north, LocalDate.of(2024, 1, 16), InvoiceStatus.OPEN);

        List<WeekdayInvoices> days = service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15));

        assertEquals(7, days.size());
        assertEquals(DayOfWeek.MONDAY, days.get(0).weekday());
        assertEquals(3, days.get(0).total());
        assertEquals(2, days.get(0).perRegion().get(Region.NORTH));
        assertEquals(1, days.get(0).perRegion().get(Region.SOUTH));
        assertEquals(0, days.get(0).perRegion().get(Region.EAST));
        assertEquals(0, days.get(1).total());
        assertEquals(DayOfWeek.WEDNESDAY, days.get(2).weekday());
        assertEquals(1, days.get(2).perRegion().get(Region.WEST));
        assertEquals(DayOfWeek.SUNDAY, days.get(6).weekday());
        assertEquals(1, days.get(6).total());
        assertEquals(1, days.get(6).perRegion().get(Region.EAST));
    }

    @Test
    void emptyRangeHasSevenEmptyDays() {
        List<WeekdayInvoices> days = service.report(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31));

        assertEquals(7, days.size());
        for (WeekdayInvoices day : days) {
            assertEquals(0, day.total());
            assertEquals(4, day.perRegion().size());
        }
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.report(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.report(null, LocalDate.of(2024, 1, 1)));
    }
}
