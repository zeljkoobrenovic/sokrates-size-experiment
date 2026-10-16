package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.PaymentReceived;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CancelledInvoicePaymentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final LocalDate day = LocalDate.of(2024, 6, 10);

    private Invoice invoice(String number) {
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + number, alpha.id(), LocalDate.of(2024, 1, 1),
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        return store.createInvoice(new Invoice(0, number, order.id(), alpha.id(), LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 1), new BigDecimal("500.00"), InvoiceStatus.OPEN));
    }

    private List<String> invoiceNumbers(List<PaymentReceived> payments) {
        return payments.stream().map(PaymentReceived::invoiceNumber).toList();
    }

    @Test
    void paymentsOfCancelledInvoicesAreLeftOutOfThePaymentLists() {
        store.assignCustomerToSalesRep(alpha.id(), anna.id());
        Invoice kept = invoice("I-1");
        Invoice cancelled = invoice("I-2");
        store.createPayment(new Payment(0, kept.id(), day, new BigDecimal("10.00")));
        store.createPayment(new Payment(0, cancelled.id(), day.plusDays(1), new BigDecimal("20.00")));
        store.updateInvoiceStatus(cancelled.id(), InvoiceStatus.CANCELLED);
        LocalDate from = day.minusDays(5);
        LocalDate to = day.plusDays(5);

        assertEquals(List.of("I-1"), invoiceNumbers(store.listPaymentsReceivedBetween(from, to)));
        assertEquals(List.of("I-1"), invoiceNumbers(store.listPaymentsOfCustomerReceivedBetween(alpha.id(), from, to)));
        assertEquals(List.of("I-1"), invoiceNumbers(store.listPaymentsInRegionReceivedBetween(Region.NORTH, from, to)));
        assertEquals(List.of("I-1"), invoiceNumbers(store.listPaymentsOfSalesRepCustomersReceivedBetween(anna.id(), from, to)));
    }
}
