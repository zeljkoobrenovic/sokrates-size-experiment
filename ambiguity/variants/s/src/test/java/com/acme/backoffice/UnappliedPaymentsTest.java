package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.CustomerUnappliedPayments;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnappliedPaymentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PaymentService payments = new PaymentService(store);
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    /** Creates an invoice of 100.00 with one payment per given amount, on consecutive days from 2024-02-01. */
    private void invoice(Customer customer, String... paymentAmounts) {
        counter++;
        LocalDate date = LocalDate.of(2024, 1, 1);
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date,
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        Invoice invoice = store.createInvoice(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date,
                date.plusDays(30), new BigDecimal("100.00"), InvoiceStatus.PAID));
        for (int i = 0; i < paymentAmounts.length; i++) {
            store.createPayment(new Payment(0, invoice.id(), LocalDate.of(2024, 2, 1).plusDays(i), new BigDecimal(paymentAmounts[i])));
        }
    }

    private List<String> lines(List<CustomerUnappliedPayments> result) {
        return result.stream().flatMap(c -> c.payments().stream()
                .map(p -> c.customerName() + ":" + p.invoiceNumber() + ":" + p.amount() + ":" + p.unappliedAmount())).toList();
    }

    @Test
    void listsPaymentsExceedingTheAmountDuePerCustomer() {
        invoice(beta, "130.00");
        invoice(alpha, "80.00", "40.00", "10.00");
        invoice(alpha, "100.00");
        invoice(alpha, "60.00");

        List<CustomerUnappliedPayments> result = payments.listUnapplied();

        assertEquals(List.of("Alpha:INV-2:40.00:20.00", "Alpha:INV-2:10.00:10.00", "Beta:INV-1:130.00:30.00"), lines(result));
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("30.00"), result.get(0).unappliedAmount());
        assertEquals(new BigDecimal("30.00"), result.get(1).unappliedAmount());
    }

    @Test
    void returnsNothingWhenNothingIsOverpaid() {
        invoice(alpha, "60.00", "40.00");
        assertEquals(List.of(), payments.listUnapplied());
    }
}
