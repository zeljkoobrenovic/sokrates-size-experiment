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
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvoiceSearchTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer west = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer south = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void invoice(String number, Customer customer, LocalDate dueDate, String amount, InvoiceStatus status) {
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + number, customer.id(), LocalDate.of(2024, 1, 1),
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        store.createInvoice(new Invoice(0, number, order.id(), customer.id(), LocalDate.of(2024, 1, 1), dueDate,
                new BigDecimal(amount), status));
    }

    private void fill() {
        invoice("INV-1", west, LocalDate.of(2024, 3, 5), "100.00", InvoiceStatus.OPEN);
        invoice("INV-2", south, LocalDate.of(2024, 3, 1), "250.00", InvoiceStatus.PAID);
        invoice("INV-3", west, LocalDate.of(2024, 3, 10), "500.00", InvoiceStatus.PAID);
        invoice("INV-4", south, LocalDate.of(2024, 2, 29), "50.00", InvoiceStatus.OPEN);
        invoice("INV-5", south, LocalDate.of(2024, 3, 5), "100.00", InvoiceStatus.OPEN);
    }

    private List<String> numbers(List<Invoice> invoices) {
        return invoices.stream().map(Invoice::number).toList();
    }

    @Test
    void noCriteriaReturnsAllOldestFirst() {
        fill();
        assertEquals(List.of("INV-4", "INV-2", "INV-1", "INV-5", "INV-3"),
                numbers(store.searchInvoices(null, null, null, null, null)));
    }

    @Test
    void singleCriteria() {
        fill();
        assertEquals(List.of("INV-1", "INV-3"), numbers(store.searchInvoices(west.id(), null, null, null, null)));
        assertEquals(List.of("INV-2", "INV-3"), numbers(store.searchInvoices(null, InvoiceStatus.PAID, null, null, null)));
        assertEquals(List.of("INV-1", "INV-5", "INV-3"),
                numbers(store.searchInvoices(null, null, LocalDate.of(2024, 3, 5), null, null)));
        assertEquals(List.of("INV-4", "INV-2"), numbers(store.searchInvoices(null, null, null, LocalDate.of(2024, 3, 1), null)));
        assertEquals(List.of("INV-2", "INV-3"), numbers(store.searchInvoices(null, null, null, null, new BigDecimal("250.00"))));
    }

    @Test
    void combinedCriteria() {
        fill();
        LocalDate from = LocalDate.of(2024, 3, 1);
        LocalDate to = LocalDate.of(2024, 3, 5);
        assertEquals(List.of("INV-5"), numbers(store.searchInvoices(south.id(), InvoiceStatus.OPEN, from, to, new BigDecimal("100"))));
        assertEquals(List.of("INV-2", "INV-1", "INV-5"), numbers(store.searchInvoices(null, null, from, to, new BigDecimal("100"))));
        assertEquals(List.of(), store.searchInvoices(west.id(), InvoiceStatus.PAID, null, null, new BigDecimal("1000")));
    }
}
