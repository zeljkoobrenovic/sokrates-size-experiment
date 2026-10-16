package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesRepCustomerReportRow;
import com.acme.backoffice.service.CustomerClosureService;
import com.acme.backoffice.service.CustomerSegmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalesRepCustomerReportTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CustomerSegmentService segments = new CustomerSegmentService(store);
    private final LocalDate asOf = LocalDate.of(2025, 1, 1);
    private Product item;
    private SalesRep anna;
    private int counter;

    private Customer customer(String name, SalesRep rep) {
        Customer created = store.customers().create(new Customer(0, name, name + "@example.com", null, "X", Region.NORTH, CustomerStatus.ACTIVE));
        if (rep != null) {
            store.customers().assignToSalesRep(created.id(), rep.id());
        }
        return created;
    }

    private SalesOrder order(Customer customer, LocalDate date, OrderStatus status, int amount) {
        return store.orders().create(new SalesOrder(0, "SO-" + (++counter), customer.id(), date, status,
                List.of(new OrderLine(item.id(), amount, new BigDecimal("1.00")))));
    }

    private Invoice invoice(SalesOrder order, Customer customer, String amount, InvoiceStatus status) {
        return store.invoices().create(new Invoice(0, "INV-" + order.number(), order.id(), customer.id(),
                asOf.minusDays(40), asOf.minusDays(10), new BigDecimal(amount), status));
    }

    private String describe(SalesRepCustomerReportRow row) {
        return row.name() + ":" + row.segment() + ":" + row.lastOrderDate() + ":" + row.openInvoiceAmount();
    }

    @BeforeEach
    void setUp() {
        item = store.products().create(new Product(0, "I-1", "Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        anna = store.salesReps().create(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
        SalesRep carl = store.salesReps().create(new SalesRep(0, "Carl", "carl@example.com", Region.NORTH));

        Customer big = customer("Big", anna);
        SalesOrder bigOrder = order(big, asOf.minusDays(30), OrderStatus.CONFIRMED, 60000);
        order(big, asOf.minusDays(5), OrderStatus.CANCELLED, 100);
        Invoice partial = invoice(bigOrder, big, "100.00", InvoiceStatus.OPEN);
        store.payments().create(new Payment(0, partial.id(), asOf.minusDays(20), new BigDecimal("30.00")));
        invoice(order(big, asOf.minusDays(3), OrderStatus.CONFIRMED, 1), big, "20.00", InvoiceStatus.OPEN);
        invoice(order(big, asOf.minusDays(2), OrderStatus.CONFIRMED, 1), big, "500.00", InvoiceStatus.PAID);

        Customer mid = customer("Mid", anna);
        order(mid, asOf.minusDays(100), OrderStatus.SHIPPED, 20000);

        customer("Idle", anna);
        order(customer("Other", carl), asOf.minusDays(10), OrderStatus.CONFIRMED, 70000);
        order(customer("NoRep", null), asOf.minusDays(10), OrderStatus.CONFIRMED, 70000);
        Customer closed = customer("Closed", anna);
        new CustomerClosureService(store).close(closed.id(), asOf.minusDays(1));
    }

    @Test
    void reportsSegmentLastOrderAndOpenAmountOfTheCustomersOfTheRep() {
        assertEquals(List.of(
                "Big:A:" + asOf.minusDays(2) + ":90.00",
                "Idle:C:null:0",
                "Mid:B:" + asOf.minusDays(100) + ":0"),
                segments.salesRepCustomerReport(anna.id(), asOf).stream().map(this::describe).toList());
    }

    @Test
    void repWithoutCustomersHasEmptyReport() {
        assertEquals(List.of(), segments.salesRepCustomerReport(9999, asOf));
    }

    @Test
    void requiresADate() {
        assertThrows(IllegalArgumentException.class, () -> segments.salesRepCustomerReport(anna.id(), null));
    }
}
