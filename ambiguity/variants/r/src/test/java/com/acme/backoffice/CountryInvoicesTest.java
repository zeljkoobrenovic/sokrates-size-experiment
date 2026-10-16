package com.acme.backoffice;

import com.acme.backoffice.model.CountryInvoiceCount;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.CountryInvoicesService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CountryInvoicesService service = new CountryInvoicesService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    private Customer customer(String country) {
        counter++;
        return store.customers().create(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, country, Region.NORTH, CustomerStatus.ACTIVE));
    }

    private void invoice(String number, Customer customer, LocalDate date, InvoiceStatus status) {
        SalesOrder order = store.orders().create(new SalesOrder(0, "SO-" + number, customer.id(), date, OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.invoices().create(new Invoice(0, number, order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal("10.00"), status));
    }

    private void fill() {
        Customer uk1 = customer("UK");
        Customer uk2 = customer("UK");
        Customer de = customer("DE");
        Customer fr = customer("FR");
        invoice("A", uk1, LocalDate.of(2024, 1, 20), InvoiceStatus.OPEN);
        invoice("B", uk2, LocalDate.of(2024, 1, 1), InvoiceStatus.PAID);
        invoice("C", uk1, LocalDate.of(2024, 1, 31), InvoiceStatus.OPEN);
        invoice("D", uk1, LocalDate.of(2023, 12, 31), InvoiceStatus.OPEN);
        invoice("E", uk1, LocalDate.of(2024, 2, 1), InvoiceStatus.OPEN);
        invoice("F", uk1, LocalDate.of(2024, 1, 15), InvoiceStatus.CANCELLED);
        invoice("G", de, LocalDate.of(2024, 1, 10), InvoiceStatus.OPEN);
        invoice("H", fr, LocalDate.of(2024, 1, 11), InvoiceStatus.OPEN);
        invoice("I", fr, LocalDate.of(2024, 1, 12), InvoiceStatus.OPEN);
    }

    @Test
    void listsInvoicesOfCountryInPeriodOldestFirst() {
        fill();

        List<Invoice> invoices = service.invoices("UK", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        assertEquals(List.of("B", "A", "C"), invoices.stream().map(Invoice::number).toList());
        assertEquals(List.of(), service.invoices("IT", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void countsInvoicesPerCountryHighestFirst() {
        fill();

        assertEquals(List.of(new CountryInvoiceCount("UK", 3), new CountryInvoiceCount("FR", 2), new CountryInvoiceCount("DE", 1)),
                service.countPerCountry(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void emptyPeriodGivesEmptyResults() {
        assertEquals(List.of(), service.countPerCountry(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31)));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.countPerCountry(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.invoices("UK", null, LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.invoices(" ", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2)));
    }
}
