package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionLargeInvoices;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.InvoiceService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LargeInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final InvoiceService invoices = new InvoiceService(store);
    private final Customer north = store.customers().create(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.customers().create(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    private void invoice(Customer customer, LocalDate date, InvoiceStatus status, String amount) {
        counter++;
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), date,
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        store.invoices().create(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal(amount), status));
    }

    private List<String> lines(List<RegionLargeInvoices> result) {
        return result.stream().flatMap(r -> r.invoices().stream().map(i -> r.region() + ":" + i.number())).toList();
    }

    @Test
    void listsInvoicesAboveTheAmountInThePeriodPerRegionBiggestFirst() {
        LocalDate day = LocalDate.of(2024, 2, 10);
        invoice(south, day, InvoiceStatus.OPEN, "300.00");
        invoice(north, day, InvoiceStatus.OPEN, "200.00");
        invoice(north, day.plusDays(1), InvoiceStatus.PAID, "400.00");
        invoice(north, day, InvoiceStatus.OPEN, "100.00");
        invoice(north, day, InvoiceStatus.CANCELLED, "900.00");
        invoice(north, day.minusDays(1), InvoiceStatus.OPEN, "900.00");
        invoice(north, day.plusDays(2), InvoiceStatus.OPEN, "900.00");
        invoice(south, day, InvoiceStatus.OPEN, "300.00");

        List<RegionLargeInvoices> result = invoices.listLargeByRegion(new BigDecimal("100.00"), day, day.plusDays(1));

        assertEquals(List.of("NORTH:INV-3", "NORTH:INV-2", "SOUTH:INV-1", "SOUTH:INV-8"), lines(result));
        assertEquals(2, result.size());
    }

    @Test
    void returnsNothingWhenNoInvoiceIsLarge() {
        invoice(north, LocalDate.of(2024, 2, 10), InvoiceStatus.OPEN, "10.00");
        assertEquals(List.of(), invoices.listLargeByRegion(new BigDecimal("10.00"), LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28)));
    }

    @Test
    void validatesArguments() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        BigDecimal amount = BigDecimal.TEN;
        assertThrows(IllegalArgumentException.class, () -> invoices.listLargeByRegion(null, day, day));
        assertThrows(IllegalArgumentException.class, () -> invoices.listLargeByRegion(amount, null, day));
        assertThrows(IllegalArgumentException.class, () -> invoices.listLargeByRegion(amount, day, null));
        assertThrows(IllegalArgumentException.class, () -> invoices.listLargeByRegion(amount, day, day.minusDays(1)));
    }
}
