package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionPartiallyPaidInvoices;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PartiallyPaidInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final InvoiceService invoices = new InvoiceService(store);
    private final PaymentService payments = new PaymentService(store);
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.customers().create(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer gamma = store.customers().create(new Customer(0, "Gamma", "g@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    /** Creates an invoice of 100.00 due on the given day, with one payment per given amount. */
    private void invoice(Customer customer, LocalDate due, String... paymentAmounts) {
        counter++;
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), due.minusDays(30),
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        store.invoices().create(new Invoice(0, "INV-" + counter, order.id(), customer.id(), due.minusDays(30), due,
                new BigDecimal("100.00"), InvoiceStatus.OPEN));
        for (String amount : paymentAmounts) {
            payments.pay("INV-" + counter, LocalDate.of(2024, 1, 5), new BigDecimal(amount));
        }
    }

    private List<String> lines(List<RegionPartiallyPaidInvoices> result) {
        return result.stream().flatMap(r -> r.customers().stream().flatMap(c -> c.invoices().stream()
                .map(i -> r.region() + ":" + c.customerName() + ":" + i.invoiceNumber() + ":" + i.dueAmount()))).toList();
    }

    @Test
    void listsOpenInvoicesWithPaymentsPerRegionAndCustomerWithAmountDue() {
        invoice(gamma, LocalDate.of(2024, 3, 1), "10.00");
        invoice(beta, LocalDate.of(2024, 3, 1), "40.00");
        invoice(alpha, LocalDate.of(2024, 3, 10), "30.00", "20.00");
        invoice(alpha, LocalDate.of(2024, 2, 10), "25.50");
        invoice(alpha, LocalDate.of(2024, 2, 1));
        invoice(alpha, LocalDate.of(2024, 2, 1), "100.00");

        List<RegionPartiallyPaidInvoices> result = invoices.listPartiallyPaid();

        assertEquals(List.of("EAST:Gamma:INV-1:90.00", "NORTH:Alpha:INV-4:74.50", "NORTH:Alpha:INV-3:50.00",
                "NORTH:Beta:INV-2:60.00"), lines(result));
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("90.00"), result.get(0).dueAmount());
        assertEquals(new BigDecimal("184.50"), result.get(1).dueAmount());
        assertEquals(new BigDecimal("124.50"), result.get(1).customers().get(0).dueAmount());
        assertEquals(new BigDecimal("60.00"), result.get(1).customers().get(1).dueAmount());
        assertEquals(new BigDecimal("25.50"), result.get(1).customers().get(0).invoices().get(0).paidAmount());
    }

    @Test
    void returnsNothingWhenNoInvoiceIsPartiallyPaid() {
        invoice(alpha, LocalDate.of(2024, 3, 1));
        assertEquals(List.of(), invoices.listPartiallyPaid());
    }
}
