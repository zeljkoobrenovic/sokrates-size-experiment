package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesRepInvoiceCount;
import com.acme.backoffice.service.SalesRepInvoiceCountService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalesRepInvoiceCountTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRepInvoiceCountService service = new SalesRepInvoiceCountService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    private SalesRep rep(String name) {
        return store.createSalesRep(new SalesRep(0, name, name + "@example.com", Region.NORTH));
    }

    private Customer customer(SalesRep rep) {
        counter++;
        Customer customer = store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        if (rep != null) {
            store.assignCustomerToSalesRep(customer.id(), rep.id());
        }
        return customer;
    }

    private void invoice(Customer customer, LocalDate date, InvoiceStatus status) {
        counter++;
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.createInvoice(new Invoice(0, "INV-" + counter, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal("10.00"), status));
    }

    private String describe(SalesRepInvoiceCount count) {
        return count.name() + ":" + count.invoiceCount();
    }

    @Test
    void countsInvoicesPerRepHighestFirst() {
        SalesRep anna = rep("Anna");
        SalesRep bob = rep("Bob");
        SalesRep carl = rep("Carl");
        SalesRep dora = rep("Dora");
        Customer annaCustomer = customer(anna);
        Customer bobOne = customer(bob);
        Customer bobTwo = customer(bob);
        Customer carlCustomer = customer(carl);
        customer(dora);
        Customer unassigned = customer(null);
        invoice(annaCustomer, LocalDate.of(2024, 1, 1), InvoiceStatus.OPEN);
        invoice(bobOne, LocalDate.of(2024, 1, 15), InvoiceStatus.OPEN);
        invoice(bobTwo, LocalDate.of(2024, 1, 31), InvoiceStatus.PAID);
        invoice(carlCustomer, LocalDate.of(2024, 1, 10), InvoiceStatus.OPEN);
        invoice(bobOne, LocalDate.of(2023, 12, 31), InvoiceStatus.OPEN);
        invoice(bobOne, LocalDate.of(2024, 2, 1), InvoiceStatus.OPEN);
        invoice(bobOne, LocalDate.of(2024, 1, 20), InvoiceStatus.CANCELLED);
        invoice(unassigned, LocalDate.of(2024, 1, 5), InvoiceStatus.OPEN);

        assertEquals(List.of("Bob:2", "Anna:1", "Carl:1", "Dora:0"),
                service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)).stream().map(this::describe).toList());
    }

    @Test
    void noRepsGivesEmptyReport() {
        assertEquals(List.of(), service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.report(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.report(null, LocalDate.of(2024, 1, 1)));
    }
}
