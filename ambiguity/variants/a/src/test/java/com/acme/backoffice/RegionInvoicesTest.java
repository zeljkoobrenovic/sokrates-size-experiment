package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionInvoices;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.InvoiceService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegionInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final InvoiceService invoices = new InvoiceService(store);
    private final Customer north = store.createCustomer(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.createCustomer(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    private void invoice(Customer customer, LocalDate date, InvoiceStatus status, String amount) {
        counter++;
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date,
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        store.createInvoice(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal(amount), status));
    }

    private List<String> totals(RegionInvoices result) {
        return result.totals().stream().map(t -> t.status() + ":" + t.invoiceCount() + ":" + t.total()).toList();
    }

    @Test
    void listsOpenAndPaidInvoicesOfTheRegionInThePeriodWithTotalsPerStatus() {
        invoice(north, LocalDate.of(2024, 3, 1), InvoiceStatus.OPEN, "100.00");
        invoice(north, LocalDate.of(2024, 2, 1), InvoiceStatus.PAID, "50.00");
        invoice(north, LocalDate.of(2024, 2, 15), InvoiceStatus.OPEN, "25.50");
        invoice(north, LocalDate.of(2024, 2, 10), InvoiceStatus.CANCELLED, "999.00");
        invoice(north, LocalDate.of(2024, 1, 31), InvoiceStatus.OPEN, "7.00");
        invoice(north, LocalDate.of(2024, 3, 2), InvoiceStatus.PAID, "7.00");
        invoice(south, LocalDate.of(2024, 2, 5), InvoiceStatus.OPEN, "1000.00");

        RegionInvoices result = invoices.listByRegionAndPeriod(Region.NORTH, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 1));

        assertEquals(List.of("INV-2", "INV-3", "INV-1"), result.invoices().stream().map(Invoice::number).toList());
        assertEquals(List.of("OPEN:2:125.50", "PAID:1:50.00"), totals(result));
    }

    @Test
    void reportsZeroTotalsForStatusesWithoutInvoices() {
        invoice(north, LocalDate.of(2024, 2, 1), InvoiceStatus.PAID, "50.00");

        RegionInvoices result = invoices.listByRegionAndPeriod(Region.NORTH, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 1));
        assertEquals(List.of("OPEN:0:0", "PAID:1:50.00"), totals(result));

        RegionInvoices empty = invoices.listByRegionAndPeriod(Region.EAST, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 1));
        assertEquals(List.of(), empty.invoices());
        assertEquals(List.of("OPEN:0:0", "PAID:0:0"), totals(empty));
    }

    @Test
    void validatesArguments() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> invoices.listByRegionAndPeriod(null, day, day));
        assertThrows(IllegalArgumentException.class, () -> invoices.listByRegionAndPeriod(Region.NORTH, null, day));
        assertThrows(IllegalArgumentException.class, () -> invoices.listByRegionAndPeriod(Region.NORTH, day, null));
        assertThrows(IllegalArgumentException.class, () -> invoices.listByRegionAndPeriod(Region.NORTH, day, day.minusDays(1)));
    }
}
