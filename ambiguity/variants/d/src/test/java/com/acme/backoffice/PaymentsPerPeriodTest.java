package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.DailyPaymentTotal;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.PaymentReceived;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentsPerPeriodTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 6, 10);

    private Invoice invoice(String number, Customer customer) {
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + number, customer.id(), LocalDate.of(2024, 1, 1),
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        return store.createInvoice(new Invoice(0, number, order.id(), customer.id(), LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 1), new BigDecimal("500.00"), InvoiceStatus.OPEN));
    }

    private void pay(Invoice invoice, LocalDate date, String amount) {
        store.createPayment(new Payment(0, invoice.id(), date, new BigDecimal(amount)));
    }

    @Test
    void listsPaymentsInclusiveOfBothDates() {
        Invoice i1 = invoice("I-1", alpha);
        Invoice i2 = invoice("I-2", beta);
        pay(i1, day.minusDays(1), "1.00");
        pay(i2, day.plusDays(2), "4.00");
        pay(i1, day, "2.00");
        pay(i1, day.plusDays(5), "8.00");
        pay(i2, day.plusDays(6), "16.00");

        List<PaymentReceived> payments = store.findPaymentsReceivedBetween(day, day.plusDays(5));
        assertEquals(List.of(day, day.plusDays(2), day.plusDays(5)), payments.stream().map(PaymentReceived::paymentDate).toList());
        assertEquals(List.of("Alpha", "Beta", "Alpha"), payments.stream().map(PaymentReceived::customerName).toList());
        assertEquals(List.of("I-1", "I-2", "I-1"), payments.stream().map(PaymentReceived::invoiceNumber).toList());
        assertEquals(0, new BigDecimal("2.00").compareTo(payments.get(0).amount()));
        assertEquals(alpha.id(), payments.get(0).customerId());
    }

    @Test
    void listsPaymentsOfOneCustomer() {
        Invoice i1 = invoice("I-1", alpha);
        Invoice i2 = invoice("I-2", beta);
        Invoice i3 = invoice("I-3", alpha);
        pay(i1, day, "1.00");
        pay(i2, day, "2.00");
        pay(i3, day.plusDays(1), "4.00");
        pay(i3, day.plusDays(30), "8.00");

        List<PaymentReceived> payments = store.findPaymentsReceivedFromCustomerBetween(alpha.id(), day, day.plusDays(10));
        assertEquals(List.of("I-1", "I-3"), payments.stream().map(PaymentReceived::invoiceNumber).toList());
        assertEquals(1, store.findPaymentsReceivedFromCustomerBetween(beta.id(), day, day.plusDays(10)).size());
    }

    @Test
    void totalsPerDay() {
        Invoice i1 = invoice("I-1", alpha);
        Invoice i2 = invoice("I-2", beta);
        pay(i1, day.minusDays(1), "100.00");
        pay(i1, day, "1.50");
        pay(i2, day, "2.50");
        pay(i2, day.plusDays(3), "4.00");
        pay(i2, day.plusDays(9), "100.00");

        List<DailyPaymentTotal> totals = store.findPaymentTotalsPerDay(day, day.plusDays(5));
        assertEquals(2, totals.size());
        assertEquals(day, totals.get(0).paymentDate());
        assertEquals(2, totals.get(0).paymentCount());
        assertEquals(0, new BigDecimal("4.00").compareTo(totals.get(0).total()));
        assertEquals(day.plusDays(3), totals.get(1).paymentDate());
        assertEquals(1, totals.get(1).paymentCount());
        assertEquals(0, new BigDecimal("4.00").compareTo(totals.get(1).total()));
    }

    @Test
    void nothingInPeriod() {
        pay(invoice("I-1", alpha), day, "1.00");
        assertTrue(store.findPaymentsReceivedBetween(day.plusDays(1), day.plusDays(2)).isEmpty());
        assertTrue(store.findPaymentsReceivedFromCustomerBetween(beta.id(), day, day).isEmpty());
        assertTrue(store.findPaymentTotalsPerDay(day.plusDays(1), day.plusDays(2)).isEmpty());
    }
}
