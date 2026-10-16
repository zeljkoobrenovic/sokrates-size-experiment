package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRep anna = store.salesReps().create(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final SalesRep carl = store.salesReps().create(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.customers().create(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer zeta = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    RepInvoicesTest() {
        store.customers().assignToSalesRep(alpha.id(), anna.id());
        store.customers().assignToSalesRep(beta.id(), anna.id());
        store.customers().assignToSalesRep(zeta.id(), carl.id());
    }

    private void invoice(Customer customer, LocalDate dueDate, InvoiceStatus status, LocalDate paymentDate) {
        counter++;
        LocalDate date = dueDate.minusDays(30);
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), date,
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        Invoice invoice = store.invoices().create(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date, dueDate,
                new BigDecimal("100.00"), status));
        if (paymentDate != null) {
            store.payments().create(new Payment(0, invoice.id(), paymentDate, new BigDecimal("100.00")));
        }
    }

    private List<String> numbers(List<Invoice> invoices) {
        return invoices.stream().map(Invoice::number).toList();
    }

    @Test
    void listsOpenInvoicesOfTheRepsCustomersOldestDueFirst() {
        invoice(alpha, LocalDate.of(2024, 3, 1), InvoiceStatus.OPEN, null);
        invoice(beta, LocalDate.of(2024, 2, 1), InvoiceStatus.OPEN, null);
        invoice(alpha, LocalDate.of(2024, 1, 1), InvoiceStatus.PAID, LocalDate.of(2024, 1, 5));
        invoice(alpha, LocalDate.of(2024, 1, 1), InvoiceStatus.CANCELLED, null);
        invoice(zeta, LocalDate.of(2024, 1, 1), InvoiceStatus.OPEN, null);
        assertEquals(List.of("INV-2", "INV-1"), numbers(store.invoices().listOpenOfSalesRep(anna.id())));
        assertEquals(List.of("INV-5"), numbers(store.invoices().listOpenOfSalesRep(carl.id())));
    }

    @Test
    void listsOnlyOpenInvoicesDueBeforeTheDateAsOverdue() {
        invoice(alpha, LocalDate.of(2024, 3, 1), InvoiceStatus.OPEN, null);
        invoice(beta, LocalDate.of(2024, 2, 1), InvoiceStatus.OPEN, null);
        invoice(alpha, LocalDate.of(2024, 1, 1), InvoiceStatus.PAID, LocalDate.of(2024, 1, 5));
        invoice(zeta, LocalDate.of(2024, 1, 1), InvoiceStatus.OPEN, null);
        assertEquals(List.of("INV-2"), numbers(store.invoices().listOverdueOfSalesRep(LocalDate.of(2024, 3, 1), anna.id())));
        assertEquals(List.of("INV-2", "INV-1"), numbers(store.invoices().listOverdueOfSalesRep(LocalDate.of(2024, 3, 2), anna.id())));
    }

    @Test
    void listsPaidInvoicesByLastPaymentDateInclusive() {
        invoice(alpha, LocalDate.of(2024, 3, 1), InvoiceStatus.PAID, LocalDate.of(2024, 6, 1));
        invoice(beta, LocalDate.of(2024, 3, 1), InvoiceStatus.PAID, LocalDate.of(2024, 6, 30));
        invoice(alpha, LocalDate.of(2024, 3, 1), InvoiceStatus.PAID, LocalDate.of(2024, 7, 1));
        invoice(alpha, LocalDate.of(2024, 3, 1), InvoiceStatus.PAID, LocalDate.of(2024, 5, 31));
        invoice(alpha, LocalDate.of(2024, 3, 1), InvoiceStatus.OPEN, null);
        invoice(zeta, LocalDate.of(2024, 3, 1), InvoiceStatus.PAID, LocalDate.of(2024, 6, 15));
        assertEquals(List.of("INV-1", "INV-2"),
                numbers(store.invoices().listPaidOfSalesRep(anna.id(), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30))));
        assertEquals(List.of("INV-6"),
                numbers(store.invoices().listPaidOfSalesRep(carl.id(), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30))));
    }

    @Test
    void repWithoutCustomersHasNoInvoices() {
        SalesRep dora = store.salesReps().create(new SalesRep(0, "Dora", "dora@example.com", Region.WEST));
        invoice(alpha, LocalDate.of(2024, 3, 1), InvoiceStatus.OPEN, null);
        assertTrue(store.invoices().listOpenOfSalesRep(dora.id()).isEmpty());
        assertTrue(store.invoices().listPaidOfSalesRep(dora.id(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)).isEmpty());
    }
}
