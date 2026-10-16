package com.acme.backoffice;

import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatement;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.StatementEntry;
import com.acme.backoffice.model.StatementEntryType;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.StatementService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatementTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StatementService statements = new StatementService(store);
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.customers().create(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private final Warehouse north = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));

    private Invoice invoice(String number, Customer customer, LocalDate date, String amount, InvoiceStatus status) {
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + number, customer.id(), date,
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        return store.invoices().create(new Invoice(0, number, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal(amount), status));
    }

    private void pay(Invoice invoice, LocalDate date, String amount) {
        store.payments().create(new Payment(0, invoice.id(), date, new BigDecimal(amount)));
    }

    private void credit(String number, Customer customer, LocalDate date, String amount) {
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + number, customer.id(), date,
                OrderStatus.DELIVERED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        SalesReturn salesReturn = store.returns().create(new SalesReturn(0, "R-" + number, order.id(), date, "Damaged",
                List.of(new ReturnLine(widget.id(), 1))), north.id());
        store.creditNotes().create(new CreditNote(0, number, customer.id(), salesReturn.id(), date, new BigDecimal(amount)));
    }

    private static void assertAmount(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual), "expected " + expected + " but was " + actual);
    }

    @Test
    void statementListsEntriesInDateOrderWithRunningBalance() {
        Invoice old = invoice("I-0", alpha, LocalDate.of(2024, 5, 20), "100.00", InvoiceStatus.OPEN);
        pay(old, LocalDate.of(2024, 5, 25), "30.00");
        credit("CN-0", alpha, LocalDate.of(2024, 5, 28), "10.00");
        Invoice i1 = invoice("I-1", alpha, LocalDate.of(2024, 6, 1), "200.00", InvoiceStatus.OPEN);
        Invoice i2 = invoice("I-2", alpha, LocalDate.of(2024, 6, 10), "50.00", InvoiceStatus.OPEN);
        pay(old, LocalDate.of(2024, 6, 5), "20.00");
        credit("CN-1", alpha, LocalDate.of(2024, 6, 10), "5.00");
        pay(i1, LocalDate.of(2024, 6, 10), "40.00");
        pay(i2, LocalDate.of(2024, 7, 1), "999.00");
        invoice("I-9", alpha, LocalDate.of(2024, 6, 15), "7.00", InvoiceStatus.OPEN);
        invoice("I-B", beta, LocalDate.of(2024, 6, 2), "1000.00", InvoiceStatus.OPEN);

        CustomerStatement statement = statements.statement(alpha.id(), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals("Alpha", statement.customerName());
        assertAmount("60.00", statement.openingBalance());
        assertEquals(List.of("I-1", "I-0", "I-2", "I-1", "CN-1", "I-9"),
                statement.entries().stream().map(StatementEntry::reference).toList());
        assertEquals(List.of(StatementEntryType.INVOICE, StatementEntryType.PAYMENT, StatementEntryType.INVOICE,
                        StatementEntryType.PAYMENT, StatementEntryType.CREDIT_NOTE, StatementEntryType.INVOICE),
                statement.entries().stream().map(StatementEntry::type).toList());
        assertAmount("-20.00", statement.entries().get(1).amount());
        assertAmount("260.00", statement.entries().get(0).balance());
        assertAmount("240.00", statement.entries().get(1).balance());
        assertAmount("290.00", statement.entries().get(2).balance());
        assertAmount("250.00", statement.entries().get(3).balance());
        assertAmount("245.00", statement.entries().get(4).balance());
        assertAmount("252.00", statement.closingBalance());
    }

    @Test
    void emptyPeriodKeepsBalance() {
        invoice("I-1", alpha, LocalDate.of(2024, 1, 5), "80.00", InvoiceStatus.OPEN);
        CustomerStatement statement = statements.statement(alpha.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertTrue(statement.entries().isEmpty());
        assertAmount("80.00", statement.openingBalance());
        assertAmount("80.00", statement.closingBalance());
    }

    @Test
    void periodBoundariesAreInclusiveAndCancelledInvoicesAreLeftOut() {
        invoice("I-1", alpha, LocalDate.of(2024, 3, 1), "10.00", InvoiceStatus.OPEN);
        invoice("I-2", alpha, LocalDate.of(2024, 3, 31), "20.00", InvoiceStatus.OPEN);
        Invoice cancelled = invoice("I-3", alpha, LocalDate.of(2024, 3, 15), "500.00", InvoiceStatus.CANCELLED);
        pay(cancelled, LocalDate.of(2024, 3, 16), "100.00");
        invoice("I-4", alpha, LocalDate.of(2024, 4, 1), "40.00", InvoiceStatus.OPEN);
        CustomerStatement statement = statements.statement(alpha.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("I-1", "I-2"), statement.entries().stream().map(StatementEntry::reference).toList());
        assertAmount("0.00", statement.openingBalance());
        assertAmount("30.00", statement.closingBalance());
    }

    @Test
    void rejectsUnknownCustomerAndInvalidPeriod() {
        LocalDate day = LocalDate.of(2024, 3, 1);
        assertThrows(IllegalArgumentException.class, () -> statements.statement(999, day, day));
        assertThrows(IllegalArgumentException.class, () -> statements.statement(alpha.id(), day, day.minusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> statements.statement(alpha.id(), null, day));
    }
}
