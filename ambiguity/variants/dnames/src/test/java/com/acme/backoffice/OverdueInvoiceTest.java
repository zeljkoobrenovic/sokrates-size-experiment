package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.OrderService;
import com.acme.backoffice.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OverdueInvoiceTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final InvoiceService invoices = new InvoiceService(store);
    private final Customer west = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer north = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    private void invoice(String number, Customer owner, LocalDate invoiceDate) {
        store.createSalesOrder(new SalesOrder(0, number, owner.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
        orders.ship(number, day);
        invoices.invoice(number, invoiceDate);
    }

    private List<String> numbers(List<Invoice> list) {
        return list.stream().map(Invoice::number).toList();
    }

    @Test
    void overdueInvoicesOldestDueDateFirst() {
        invoice("SO-1", west, day.plusDays(10));
        invoice("SO-2", north, day);
        invoice("SO-3", west, day.plusDays(5));
        // INV-0002 is due Jul 1, INV-0003 Jul 6, INV-0001 Jul 11
        assertTrue(invoices.listOverdue(LocalDate.of(2024, 7, 1)).isEmpty());
        assertEquals(List.of("INV-0002"), numbers(invoices.listOverdue(LocalDate.of(2024, 7, 2))));
        assertEquals(List.of("INV-0002", "INV-0003", "INV-0001"), numbers(invoices.listOverdue(LocalDate.of(2024, 8, 1))));
    }

    @Test
    void overdueByCustomerAndRegion() {
        invoice("SO-1", west, day.plusDays(10));
        invoice("SO-2", north, day);
        invoice("SO-3", west, day.plusDays(5));
        LocalDate asOf = LocalDate.of(2024, 8, 1);
        assertEquals(List.of("INV-0003", "INV-0001"), numbers(invoices.listOverdueByCustomer(asOf, west.id())));
        assertEquals(List.of("INV-0002"), numbers(invoices.listOverdueByCustomer(asOf, north.id())));
        assertTrue(invoices.listOverdueByCustomer(asOf, 999).isEmpty());
        assertEquals(List.of("INV-0003", "INV-0001"), numbers(invoices.listOverdueByRegion(asOf, Region.WEST)));
        assertEquals(List.of("INV-0002"), numbers(invoices.listOverdueByRegion(asOf, Region.NORTH)));
        assertTrue(invoices.listOverdueByRegion(asOf, Region.SOUTH).isEmpty());
    }

    @Test
    void paidInvoicesAreNotOverdue() {
        invoice("SO-1", west, day);
        invoice("SO-2", west, day);
        new PaymentService(store).pay("INV-0001", day, new BigDecimal("9.00"));
        assertEquals(List.of("INV-0002"), numbers(invoices.listOverdue(LocalDate.of(2024, 8, 1))));
    }

    @Test
    void requiresDate() {
        assertThrows(IllegalArgumentException.class, () -> invoices.listOverdue(null));
        assertThrows(IllegalArgumentException.class, () -> invoices.listOverdueByCustomer(null, 1));
        assertThrows(IllegalArgumentException.class, () -> invoices.listOverdueByRegion(day, null));
    }
}
