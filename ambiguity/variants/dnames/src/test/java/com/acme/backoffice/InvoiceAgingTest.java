package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceAging;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvoiceAgingTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final LocalDate asOf = LocalDate.of(2024, 10, 1);

    private Invoice invoice(String number, Customer customer, LocalDate dueDate, String amount, InvoiceStatus status) {
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + number, customer.id(), LocalDate.of(2024, 1, 1),
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        return store.createInvoice(new Invoice(0, number, order.id(), customer.id(), LocalDate.of(2024, 1, 1), dueDate,
                new BigDecimal(amount), status));
    }

    private void pay(Invoice invoice, LocalDate date, String amount) {
        store.createPayment(new Payment(0, invoice.id(), date, new BigDecimal(amount)));
    }

    @Test
    void bucketsByDaysOverdue() {
        invoice("I-1", alpha, asOf, "1.00", InvoiceStatus.OPEN);
        invoice("I-2", alpha, asOf.plusDays(40), "2.00", InvoiceStatus.OPEN);
        invoice("I-3", alpha, asOf.minusDays(1), "4.00", InvoiceStatus.OPEN);
        invoice("I-4", alpha, asOf.minusDays(30), "8.00", InvoiceStatus.OPEN);
        invoice("I-5", alpha, asOf.minusDays(31), "16.00", InvoiceStatus.OPEN);
        invoice("I-6", alpha, asOf.minusDays(60), "32.00", InvoiceStatus.OPEN);
        invoice("I-7", alpha, asOf.minusDays(61), "64.00", InvoiceStatus.OPEN);
        invoice("I-8", alpha, asOf.minusDays(90), "128.00", InvoiceStatus.OPEN);
        invoice("I-9", alpha, asOf.minusDays(91), "256.00", InvoiceStatus.OPEN);

        List<InvoiceAging> aging = store.listOpenInvoiceAgingPerCustomer(asOf);
        assertEquals(1, aging.size());
        InvoiceAging a = aging.get(0);
        assertEquals(alpha.id(), a.customerId());
        assertEquals("Alpha", a.name());
        assertEquals(0, new BigDecimal("3.00").compareTo(a.notDue()));
        assertEquals(0, new BigDecimal("12.00").compareTo(a.days1To30()));
        assertEquals(0, new BigDecimal("48.00").compareTo(a.days31To60()));
        assertEquals(0, new BigDecimal("192.00").compareTo(a.days61To90()));
        assertEquals(0, new BigDecimal("256.00").compareTo(a.over90()));
    }

    @Test
    void usesUnpaidBalanceOfOpenInvoicesOnly() {
        Invoice partly = invoice("I-1", alpha, asOf.minusDays(10), "100.00", InvoiceStatus.OPEN);
        pay(partly, asOf.minusDays(5), "30.00");
        pay(partly, asOf.plusDays(1), "20.00");
        invoice("I-2", alpha, asOf.minusDays(10), "50.00", InvoiceStatus.PAID);
        invoice("I-3", alpha, asOf.minusDays(10), "25.00", InvoiceStatus.CANCELLED);
        invoice("I-4", beta, asOf.minusDays(100), "10.00", InvoiceStatus.OPEN);

        List<InvoiceAging> aging = store.listOpenInvoiceAgingPerCustomer(asOf);
        assertEquals(List.of("Alpha", "Beta"), aging.stream().map(InvoiceAging::name).toList());
        assertEquals(0, new BigDecimal("70.00").compareTo(aging.get(0).days1To30()));
        assertEquals(0, BigDecimal.ZERO.compareTo(aging.get(0).over90()));
        assertEquals(0, new BigDecimal("10.00").compareTo(aging.get(1).over90()));
        assertEquals(0, BigDecimal.ZERO.compareTo(aging.get(1).notDue()));
    }

    @Test
    void ignoresInvoicesDatedAfterTheDate() {
        invoice("I-1", alpha, asOf.plusDays(10), "5.00", InvoiceStatus.OPEN);
        assertTrue(store.listOpenInvoiceAgingPerCustomer(LocalDate.of(2023, 12, 31)).isEmpty());
        assertEquals(1, store.listOpenInvoiceAgingPerCustomer(asOf).size());
    }
}
