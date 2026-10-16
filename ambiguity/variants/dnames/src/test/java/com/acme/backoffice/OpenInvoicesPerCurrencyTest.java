package com.acme.backoffice;

import com.acme.backoffice.model.Currency;
import com.acme.backoffice.model.CurrencyInvoices;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceCurrency;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.InvoiceService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenInvoicesPerCurrencyTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final InvoiceService invoices = new InvoiceService(store);
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private int counter;

    /** Creates an invoice of 100.00 EUR due on the given day; a null currency records none. */
    private void invoice(Customer customer, LocalDate due, InvoiceStatus status, Currency currency, String currencyAmount) {
        counter++;
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), due.minusDays(30),
                OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        Invoice invoice = store.createInvoice(new Invoice(0, "INV-" + counter, order.id(), customer.id(), due.minusDays(30), due,
                new BigDecimal("100.00"), status));
        if (currency != null) {
            store.createInvoiceCurrency(new InvoiceCurrency(invoice.id(), currency, new BigDecimal(currencyAmount)));
        }
    }

    private List<String> lines(List<CurrencyInvoices> result) {
        return result.stream().flatMap(r -> r.customers().stream().flatMap(c -> c.invoices().stream()
                .map(i -> r.currency() + ":" + c.customerName() + ":" + i.invoiceNumber() + ":" + i.currencyAmount() + ":" + i.eurAmount()))).toList();
    }

    @Test
    void listsOpenInvoicesPerCurrencyAndCustomerInCurrencyAndEur() {
        invoice(beta, LocalDate.of(2024, 3, 1), InvoiceStatus.OPEN, Currency.USD, "110.00");
        invoice(alpha, LocalDate.of(2024, 3, 10), InvoiceStatus.OPEN, Currency.USD, "55.00");
        invoice(alpha, LocalDate.of(2024, 2, 10), InvoiceStatus.OPEN, Currency.USD, "60.00");
        invoice(alpha, LocalDate.of(2024, 2, 1), InvoiceStatus.OPEN, null, null);
        invoice(beta, LocalDate.of(2024, 2, 1), InvoiceStatus.PAID, Currency.GBP, "85.00");
        invoice(beta, LocalDate.of(2024, 2, 1), InvoiceStatus.OPEN, Currency.GBP, "85.00");

        List<CurrencyInvoices> result = invoices.listOpenByCurrency();

        assertEquals(List.of("EUR:Alpha:INV-4:100.00:100.00", "GBP:Beta:INV-6:85.00:100.00",
                "USD:Alpha:INV-3:60.00:100.00", "USD:Alpha:INV-2:55.00:100.00", "USD:Beta:INV-1:110.00:100.00"), lines(result));
        assertEquals(3, result.size());
        assertEquals(new BigDecimal("225.00"), result.get(2).currencyAmount());
        assertEquals(new BigDecimal("300.00"), result.get(2).eurAmount());
        assertEquals(new BigDecimal("115.00"), result.get(2).customers().get(0).currencyAmount());
        assertEquals(new BigDecimal("200.00"), result.get(2).customers().get(0).eurAmount());
    }

    @Test
    void returnsNothingWhenNoInvoiceIsOpen() {
        invoice(alpha, LocalDate.of(2024, 3, 1), InvoiceStatus.PAID, Currency.USD, "110.00");
        assertEquals(List.of(), invoices.listOpenByCurrency());
    }
}
