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
import com.acme.backoffice.service.LatestInvoicesService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LatestInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final LatestInvoicesService service = new LatestInvoicesService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Customer north = store.customers().create(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.customers().create(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));

    private void invoice(String number, Customer customer, LocalDate date, InvoiceStatus status) {
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + number, customer.id(), date, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.invoices().create(new Invoice(0, number, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal("10.00"), status));
    }

    private List<String> numbers(List<Invoice> invoices) {
        return invoices.stream().map(Invoice::number).toList();
    }

    private void fill() {
        invoice("A", north, LocalDate.of(2024, 1, 1), InvoiceStatus.OPEN);
        invoice("B", south, LocalDate.of(2024, 1, 5), InvoiceStatus.OPEN);
        invoice("C", north, LocalDate.of(2024, 1, 3), InvoiceStatus.PAID);
        invoice("D", south, LocalDate.of(2024, 1, 9), InvoiceStatus.CANCELLED);
        invoice("E", south, LocalDate.of(2024, 1, 5), InvoiceStatus.PAID);
    }

    @Test
    void listsLatestInvoicesOverall() {
        fill();
        assertEquals(List.of("E", "B", "C"), numbers(service.overall(3)));
        assertEquals(4, service.overall(10).size());
    }

    @Test
    void listsLatestInvoicesPerRegion() {
        fill();
        assertEquals(List.of("C", "A"), numbers(service.forRegion(Region.NORTH, 5)));
        assertEquals(List.of("E"), numbers(service.forRegion(Region.SOUTH, 1)));
        assertEquals(List.of(), numbers(service.forRegion(Region.EAST, 5)));
    }

    @Test
    void listsLatestInvoicesPerCustomer() {
        fill();
        assertEquals(List.of("E", "B"), numbers(service.forCustomer(south.id(), 5)));
        assertEquals(List.of("C"), numbers(service.forCustomer(north.id(), 1)));
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.overall(0));
        assertThrows(IllegalArgumentException.class, () -> service.forRegion(null, 1));
        assertThrows(IllegalArgumentException.class, () -> service.forCustomer(9999, 1));
    }
}
