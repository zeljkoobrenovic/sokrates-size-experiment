package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.OrderService;
import com.acme.backoffice.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PaymentService payments = new PaymentService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    @BeforeEach
    void invoiceOrder() {
        Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-1", customer.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("4.50")))));
        new OrderService(store).ship("SO-1", day);
        new InvoiceService(store).invoice("SO-1", day);
    }

    private BigDecimal money(String value) {
        return new BigDecimal(value);
    }

    @Test
    void partialPaymentsUntilPaid() {
        Payment first = payments.pay("INV-0001", day, money("20.00"));
        assertEquals(0, money("20.00").compareTo(first.amount()));
        assertEquals(0, money("25.00").compareTo(payments.amountDue("INV-0001")));
        assertEquals(InvoiceStatus.OPEN, store.findInvoiceByNumber("INV-0001").orElseThrow().status());
        payments.pay("INV-0001", day.plusDays(5), money("25.00"));
        assertEquals(0, BigDecimal.ZERO.compareTo(payments.amountDue("INV-0001")));
        assertEquals(InvoiceStatus.PAID, store.findInvoiceByNumber("INV-0001").orElseThrow().status());
        assertEquals(List.of(day, day.plusDays(5)), payments.listByInvoice("INV-0001").stream().map(Payment::paymentDate).toList());
    }

    @Test
    void fullPaymentAtOnce() {
        payments.pay("INV-0001", day, money("45.00"));
        assertEquals(InvoiceStatus.PAID, store.findInvoiceByNumber("INV-0001").orElseThrow().status());
    }

    @Test
    void invalidPayments() {
        assertThrows(IllegalArgumentException.class, () -> payments.pay("INV-9999", day, money("1.00")));
        assertThrows(IllegalArgumentException.class, () -> payments.pay("INV-0001", null, money("1.00")));
        assertThrows(IllegalArgumentException.class, () -> payments.pay("INV-0001", day, money("0.00")));
        assertThrows(IllegalArgumentException.class, () -> payments.pay("INV-0001", day, null));
        assertThrows(IllegalStateException.class, () -> payments.pay("INV-0001", day, money("45.01")));
        payments.pay("INV-0001", day, money("45.00"));
        assertThrows(IllegalStateException.class, () -> payments.pay("INV-0001", day, money("1.00")));
        assertThrows(IllegalArgumentException.class, () -> payments.listByInvoice("INV-9999"));
        assertThrows(IllegalArgumentException.class, () -> payments.amountDue("INV-9999"));
    }

    @Test
    void noPaymentsYet() {
        assertTrue(payments.listByInvoice("INV-0001").isEmpty());
        assertEquals(0, money("45.00").compareTo(payments.amountDue("INV-0001")));
    }
}
