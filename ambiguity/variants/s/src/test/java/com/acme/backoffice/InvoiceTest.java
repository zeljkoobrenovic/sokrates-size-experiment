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
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvoiceTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final InvoiceService invoices = new InvoiceService(store);
    private final Customer customer = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer other = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    private void create(String number, Customer owner) {
        store.createSalesOrder(new SalesOrder(0, number, owner.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
    }

    @Test
    void invoiceShippedOrder() {
        create("SO-1", customer);
        orders.ship("SO-1", day);
        Invoice invoice = invoices.invoice("SO-1", LocalDate.of(2024, 1, 15));
        assertEquals("INV-0001", invoice.number());
        assertEquals(customer.id(), invoice.customerId());
        assertEquals(store.findSalesOrderByNumber("SO-1").orElseThrow().id(), invoice.orderId());
        assertEquals(LocalDate.of(2024, 2, 14), invoice.dueDate());
        assertEquals(0, new BigDecimal("9.00").compareTo(invoice.amount()));
        assertEquals(InvoiceStatus.OPEN, invoice.status());
        assertEquals(invoice, invoices.find("INV-0001").orElseThrow());
        assertTrue(invoices.find("INV-9999").isEmpty());
    }

    @Test
    void onlyShippedOrdersAndOnlyOnce() {
        create("SO-1", customer);
        assertThrows(IllegalStateException.class, () -> invoices.invoice("SO-1", day));
        orders.ship("SO-1", day);
        invoices.invoice("SO-1", day);
        assertThrows(IllegalStateException.class, () -> invoices.invoice("SO-1", day));
        assertThrows(IllegalArgumentException.class, () -> invoices.invoice("SO-9", day));
        assertThrows(IllegalArgumentException.class, () -> invoices.invoice("SO-1", null));
    }

    @Test
    void listInvoicesOfCustomer() {
        create("SO-1", customer);
        create("SO-2", customer);
        create("SO-3", other);
        for (String number : List.of("SO-1", "SO-2", "SO-3")) {
            orders.ship(number, day);
        }
        invoices.invoice("SO-1", day);
        invoices.invoice("SO-2", day.plusDays(3));
        invoices.invoice("SO-3", day);
        assertEquals(List.of("INV-0002", "INV-0001"), invoices.listByCustomer(customer.id()).stream().map(Invoice::number).toList());
        assertEquals(List.of("INV-0003"), invoices.listByCustomer(other.id()).stream().map(Invoice::number).toList());
        assertTrue(invoices.listByCustomer(999).isEmpty());
    }
}
