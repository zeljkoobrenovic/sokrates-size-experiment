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
import com.acme.backoffice.model.RepPayments;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentsPerRepTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRep anna = store.salesReps().create(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final SalesRep carl = store.salesReps().create(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
    private final SalesRep dora = store.salesReps().create(new SalesRep(0, "Dora", "dora@example.com", Region.WEST));
    private final Customer alpha = customer("Alpha", anna);
    private final Customer zeta = customer("Zeta", carl);
    private final Customer loner = customer("Loner", null);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    private Customer customer(String name, SalesRep rep) {
        Customer customer = store.customers().create(new Customer(0, name, name + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        if (rep != null) {
            store.customers().assignToSalesRep(customer.id(), rep.id());
        }
        return customer;
    }

    private void pay(Customer customer, LocalDate paymentDate, String amount) {
        counter++;
        LocalDate date = LocalDate.of(2024, 1, 1);
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), date,
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        Invoice invoice = store.invoices().create(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date,
                LocalDate.of(2024, 1, 31), new BigDecimal("1000.00"), InvoiceStatus.OPEN));
        store.payments().create(new Payment(0, invoice.id(), paymentDate, new BigDecimal(amount)));
    }

    @Test
    void listsPaymentsOfOneRepBetweenTwoDatesInclusive() {
        pay(alpha, LocalDate.of(2024, 3, 31), "20.00");
        pay(alpha, LocalDate.of(2024, 3, 1), "10.00");
        pay(alpha, LocalDate.of(2024, 2, 29), "99.00");
        pay(alpha, LocalDate.of(2024, 4, 1), "98.00");
        pay(zeta, LocalDate.of(2024, 3, 15), "50.00");
        pay(loner, LocalDate.of(2024, 3, 15), "60.00");
        List<PaymentReceived> payments = store.payments().listReceivedOfSalesRep(anna.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of(new BigDecimal("10.00"), new BigDecimal("20.00")),
                payments.stream().map(PaymentReceived::amount).toList());
        assertEquals(List.of("INV-2", "INV-1"), payments.stream().map(PaymentReceived::invoiceNumber).toList());
        assertTrue(store.payments().listReceivedOfSalesRep(dora.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)).isEmpty());
    }

    @Test
    void totalsPaymentsPerRepBiggestFirst() {
        pay(alpha, LocalDate.of(2024, 3, 5), "10.00");
        pay(alpha, LocalDate.of(2024, 3, 6), "20.00");
        pay(zeta, LocalDate.of(2024, 3, 7), "50.00");
        pay(zeta, LocalDate.of(2024, 5, 7), "500.00");
        pay(loner, LocalDate.of(2024, 3, 7), "70.00");
        assertEquals(List.of(
                new RepPayments(carl.id(), "Carl", 1, new BigDecimal("50.00")),
                new RepPayments(anna.id(), "Anna", 2, new BigDecimal("30.00"))),
                store.payments().receivedPerSalesRep(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
    }

    @Test
    void repsWithoutPaymentsAreLeftOut() {
        pay(zeta, LocalDate.of(2024, 3, 7), "5.00");
        List<RepPayments> result = store.payments().receivedPerSalesRep(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(1, result.size());
        assertTrue(result.stream().noneMatch(r -> r.salesRepId() == dora.id()));
    }
}
