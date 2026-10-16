package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerLateInvoices;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.LatePaidInvoice;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LatePaidInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final InvoiceService invoices = new InvoiceService(store);
    private final PaymentService payments = new PaymentService(store);
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    /** Creates an invoice due on the given date, with one payment of 10.00 per given date (no dates: left open). */
    private void invoice(Customer customer, LocalDate due, LocalDate... paymentDates) {
        counter++;
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), due.minusDays(30),
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        store.createInvoice(new Invoice(0, "INV-" + counter, order.id(), customer.id(), due.minusDays(30), due,
                new BigDecimal("10.00").multiply(new BigDecimal(Math.max(1, paymentDates.length))), InvoiceStatus.OPEN));
        for (LocalDate date : paymentDates) {
            payments.pay("INV-" + counter, date, new BigDecimal("10.00"));
        }
    }

    private List<String> lines(List<CustomerLateInvoices> result) {
        return result.stream().flatMap(c -> c.invoices().stream().map(i -> c.customerName() + ":" + i.invoiceNumber() + ":" + i.daysLate())).toList();
    }

    @Test
    void listsInvoicesPaidAfterTheDueDateInThePeriodPerCustomer() {
        invoice(beta, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 11));
        invoice(alpha, LocalDate.of(2024, 2, 10), LocalDate.of(2024, 2, 12));
        invoice(alpha, LocalDate.of(2024, 1, 20), LocalDate.of(2024, 2, 20));
        invoice(alpha, LocalDate.of(2024, 2, 5), LocalDate.of(2024, 2, 5));
        invoice(alpha, LocalDate.of(2024, 2, 5), LocalDate.of(2024, 2, 1));
        invoice(alpha, LocalDate.of(2024, 2, 5));
        invoice(alpha, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15));
        invoice(alpha, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 2));
        invoice(beta, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 31), LocalDate.of(2024, 2, 3));

        List<CustomerLateInvoices> result = invoices.listPaidLateByPeriod(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29));

        assertEquals(List.of("Alpha:INV-3:31", "Alpha:INV-2:2", "Beta:INV-1:10", "Beta:INV-9:2"), lines(result));
        assertEquals(2, result.size());
        LatePaidInvoice first = result.get(0).invoices().get(0);
        assertEquals(LocalDate.of(2024, 2, 20), first.paidDate());
        assertEquals(LocalDate.of(2024, 1, 20), first.dueDate());
    }

    @Test
    void returnsNothingWhenNoInvoiceWasPaidLate() {
        assertEquals(List.of(), invoices.listPaidLateByPeriod(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29)));
    }

    @Test
    void validatesArguments() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> invoices.listPaidLateByPeriod(null, day));
        assertThrows(IllegalArgumentException.class, () -> invoices.listPaidLateByPeriod(day, null));
        assertThrows(IllegalArgumentException.class, () -> invoices.listPaidLateByPeriod(day, day.minusDays(1)));
    }
}
