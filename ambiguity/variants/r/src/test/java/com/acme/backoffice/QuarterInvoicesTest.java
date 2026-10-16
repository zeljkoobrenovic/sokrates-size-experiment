package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.QuarterInvoices;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.QuarterInvoicesService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuarterInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final QuarterInvoicesService service = new QuarterInvoicesService(store);
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
    void countsInvoicesPerQuarterOverallAndPerRegion() {
        Customer north = customer(Region.NORTH);
        Customer south = customer(Region.SOUTH);
        Customer east = customer(Region.EAST);
        Customer west = customer(Region.WEST);
        invoice(north, LocalDate.of(2024, 1, 1), InvoiceStatus.OPEN);
        invoice(north, LocalDate.of(2024, 3, 31), InvoiceStatus.PAID);
        invoice(south, LocalDate.of(2024, 3, 15), InvoiceStatus.OPEN);
        invoice(east, LocalDate.of(2024, 4, 1), InvoiceStatus.OPEN);
        invoice(west, LocalDate.of(2024, 12, 31), InvoiceStatus.PAID);
        invoice(north, LocalDate.of(2024, 2, 1), InvoiceStatus.CANCELLED);
        invoice(north, LocalDate.of(2023, 12, 31), InvoiceStatus.OPEN);
        invoice(north, LocalDate.of(2025, 1, 1), InvoiceStatus.OPEN);

        List<QuarterInvoices> quarters = service.report(2024);

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
    void yearWithoutInvoicesHasFourEmptyQuarters() {
        List<QuarterInvoices> quarters = service.report(2030);

        assertEquals(4, quarters.size());
        for (QuarterInvoices quarter : quarters) {
            assertEquals(0, quarter.total());
            assertEquals(4, quarter.perRegion().size());
        }
    }

    @Test
    void rejectsInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> service.report(0));
    }
}
