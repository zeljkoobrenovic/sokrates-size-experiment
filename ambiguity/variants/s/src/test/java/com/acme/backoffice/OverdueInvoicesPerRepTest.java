package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RepOverdueInvoices;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OverdueInvoicesPerRepTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final SalesRep carl = store.createSalesRep(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
    private final SalesRep dora = store.createSalesRep(new SalesRep(0, "Dora", "dora@example.com", Region.WEST));
    private final Customer alpha = customer("Alpha", anna);
    private final Customer beta = customer("Beta", anna);
    private final Customer zeta = customer("Zeta", carl);
    private final Customer loner = customer("Loner", null);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    private Customer customer(String name, SalesRep rep) {
        Customer customer = store.createCustomer(new Customer(0, name, name + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        if (rep != null) {
            store.assignCustomerToSalesRep(customer.id(), rep.id());
        }
        return customer;
    }

    private void invoice(Customer customer, LocalDate dueDate, String amount, InvoiceStatus status) {
        counter++;
        LocalDate date = dueDate.minusDays(30);
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date,
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        store.createInvoice(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date, dueDate,
                new BigDecimal(amount), status));
    }

    @Test
    void countsAndSumsOverdueInvoicesBiggestAmountFirst() {
        invoice(alpha, LocalDate.of(2024, 1, 1), "100.00", InvoiceStatus.OPEN);
        invoice(beta, LocalDate.of(2024, 2, 1), "50.00", InvoiceStatus.OPEN);
        invoice(zeta, LocalDate.of(2024, 1, 1), "400.00", InvoiceStatus.OPEN);
        assertEquals(List.of(
                new RepOverdueInvoices(carl.id(), "Carl", 1, new BigDecimal("400.00")),
                new RepOverdueInvoices(anna.id(), "Anna", 2, new BigDecimal("150.00"))),
                store.findOverdueInvoicesPerSalesRep(LocalDate.of(2024, 3, 1)));
    }

    @Test
    void ignoresPaidCancelledNotYetDueAndUnassignedInvoices() {
        invoice(alpha, LocalDate.of(2024, 3, 1), "100.00", InvoiceStatus.OPEN);
        invoice(alpha, LocalDate.of(2024, 1, 1), "70.00", InvoiceStatus.PAID);
        invoice(alpha, LocalDate.of(2024, 1, 1), "80.00", InvoiceStatus.CANCELLED);
        invoice(loner, LocalDate.of(2024, 1, 1), "90.00", InvoiceStatus.OPEN);
        assertTrue(store.findOverdueInvoicesPerSalesRep(LocalDate.of(2024, 3, 1)).isEmpty());
        assertEquals(List.of(new RepOverdueInvoices(anna.id(), "Anna", 1, new BigDecimal("100.00"))),
                store.findOverdueInvoicesPerSalesRep(LocalDate.of(2024, 3, 2)));
    }

    @Test
    void repsWithoutOverdueInvoicesAreLeftOut() {
        invoice(zeta, LocalDate.of(2024, 1, 1), "10.00", InvoiceStatus.OPEN);
        List<RepOverdueInvoices> result = store.findOverdueInvoicesPerSalesRep(LocalDate.of(2024, 3, 1));
        assertEquals(1, result.size());
        assertTrue(result.stream().noneMatch(r -> r.salesRepId() == dora.id()));
    }
}
