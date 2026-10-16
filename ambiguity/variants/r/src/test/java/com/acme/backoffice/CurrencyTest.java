package com.acme.backoffice;

import com.acme.backoffice.model.Currency;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerOpenAmount;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceCurrency;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.CurrencyService;
import com.acme.backoffice.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CurrencyService currencies = new CurrencyService(store);
    private final Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void shippedOrder(String number, String unitPrice) {
        store.orders().create(new SalesOrder(0, number, customer.id(), LocalDate.of(2025, 1, 1), OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal(unitPrice)))));
    }

    @Test
    void rateAppliesFromItsDateOnAndCanBeReplaced() {
        currencies.setRate(Currency.USD, LocalDate.of(2025, 1, 1), new BigDecimal("1.10"));
        currencies.setRate(Currency.USD, LocalDate.of(2025, 6, 1), new BigDecimal("1.20"));
        assertEquals(0, new BigDecimal("1.10").compareTo(store.currencies().findExchangeRate(Currency.USD, LocalDate.of(2025, 5, 31)).orElseThrow().perEur()));
        assertEquals(0, new BigDecimal("1.20").compareTo(store.currencies().findExchangeRate(Currency.USD, LocalDate.of(2025, 6, 1)).orElseThrow().perEur()));
        assertEquals(true, store.currencies().findExchangeRate(Currency.USD, LocalDate.of(2024, 12, 31)).isEmpty());
        currencies.setRate(Currency.USD, LocalDate.of(2025, 6, 1), new BigDecimal("1.30"));
        assertEquals(0, new BigDecimal("1.30").compareTo(store.currencies().findExchangeRate(Currency.USD, LocalDate.of(2025, 7, 1)).orElseThrow().perEur()));
    }

    @Test
    void eurRateAndNonPositiveRatesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> currencies.setRate(Currency.EUR, LocalDate.of(2025, 1, 1), BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> currencies.setRate(Currency.GBP, LocalDate.of(2025, 1, 1), BigDecimal.ZERO));
    }

    @Test
    void customerCurrencyDefaultsToEur() {
        assertEquals(Currency.EUR, currencies.getCustomerCurrency(customer.id()));
        currencies.setCustomerCurrency(customer.id(), Currency.GBP);
        assertEquals(Currency.GBP, currencies.getCustomerCurrency(customer.id()));
        currencies.setCustomerCurrency(customer.id(), Currency.USD);
        assertEquals(Currency.USD, currencies.getCustomerCurrency(customer.id()));
    }

    @Test
    void invoiceRecordsCurrencyAndAmountWhileKeepingEur() {
        currencies.setRate(Currency.USD, LocalDate.of(2025, 1, 1), new BigDecimal("1.10"));
        shippedOrder("SO-1", "10.00");
        Invoice invoice = currencies.invoice("SO-1", LocalDate.of(2025, 2, 1), Currency.USD);
        assertEquals(0, new BigDecimal("100.00").compareTo(invoice.amount()));
        InvoiceCurrency recorded = currencies.getInvoiceCurrency(store.invoices().findByNumber(invoice.number()).orElseThrow());
        assertEquals(Currency.USD, recorded.currency());
        assertEquals(0, new BigDecimal("110.00").compareTo(recorded.amount()));
    }

    @Test
    void invoiceWithoutCurrencyUsesTheCustomersCurrency() {
        currencies.setRate(Currency.GBP, LocalDate.of(2025, 1, 1), new BigDecimal("0.85"));
        currencies.setCustomerCurrency(customer.id(), Currency.GBP);
        shippedOrder("SO-1", "10.00");
        Invoice invoice = currencies.invoice("SO-1", LocalDate.of(2025, 2, 1), null);
        InvoiceCurrency recorded = currencies.getInvoiceCurrency(invoice);
        assertEquals(Currency.GBP, recorded.currency());
        assertEquals(0, new BigDecimal("85.00").compareTo(recorded.amount()));
    }

    @Test
    void eurInvoiceNeedsNoRate() {
        shippedOrder("SO-1", "10.00");
        Invoice invoice = currencies.invoice("SO-1", LocalDate.of(2025, 2, 1), Currency.EUR);
        InvoiceCurrency recorded = currencies.getInvoiceCurrency(invoice);
        assertEquals(Currency.EUR, recorded.currency());
        assertEquals(0, new BigDecimal("100.00").compareTo(recorded.amount()));
    }

    @Test
    void missingRateRejectsTheInvoiceAndCreatesNothing() {
        currencies.setRate(Currency.USD, LocalDate.of(2025, 3, 1), new BigDecimal("1.10"));
        shippedOrder("SO-1", "10.00");
        assertThrows(IllegalStateException.class, () -> currencies.invoice("SO-1", LocalDate.of(2025, 2, 1), Currency.USD));
        assertEquals(0, store.invoices().count());
    }

    @Test
    void invoiceWithoutRecordedCurrencyIsInEur() {
        shippedOrder("SO-1", "10.00");
        Invoice plain = new com.acme.backoffice.service.InvoiceService(store).invoice("SO-1", LocalDate.of(2025, 2, 1));
        InvoiceCurrency recorded = currencies.getInvoiceCurrency(plain);
        assertEquals(Currency.EUR, recorded.currency());
        assertEquals(0, plain.amount().compareTo(recorded.amount()));
    }

    @Test
    void openAmountIsShownInTheCustomersCurrency() {
        currencies.setRate(Currency.USD, LocalDate.of(2025, 1, 1), new BigDecimal("1.10"));
        currencies.setRate(Currency.USD, LocalDate.of(2025, 6, 1), new BigDecimal("1.20"));
        currencies.setCustomerCurrency(customer.id(), Currency.USD);
        shippedOrder("SO-1", "10.00");
        shippedOrder("SO-2", "5.00");
        Invoice usd = currencies.invoice("SO-1", LocalDate.of(2025, 2, 1), Currency.USD);
        currencies.invoice("SO-2", LocalDate.of(2025, 2, 1), Currency.EUR);

        CustomerOpenAmount open = currencies.openAmount(customer.id(), LocalDate.of(2025, 7, 1));
        assertEquals(Currency.USD, open.currency());
        // 100 EUR invoiced as 110.00 USD, plus 50 EUR invoice converted at 1.20 = 60.00
        assertEquals(0, new BigDecimal("170.00").compareTo(open.amount()));

        new PaymentService(store).pay(usd.number(), LocalDate.of(2025, 3, 1), new BigDecimal("40.00"));
        open = currencies.openAmount(customer.id(), LocalDate.of(2025, 7, 1));
        // 60 EUR of the USD invoice remain: 110 * 60 / 100 = 66.00, plus 60.00
        assertEquals(0, new BigDecimal("126.00").compareTo(open.amount()));
    }

    @Test
    void openAmountDefaultsToEurAndIgnoresPaidInvoices() {
        shippedOrder("SO-1", "10.00");
        Invoice invoice = currencies.invoice("SO-1", LocalDate.of(2025, 2, 1), null);
        CustomerOpenAmount open = currencies.openAmount(customer.id(), LocalDate.of(2025, 3, 1));
        assertEquals(Currency.EUR, open.currency());
        assertEquals(0, new BigDecimal("100.00").compareTo(open.amount()));
        new PaymentService(store).pay(invoice.number(), LocalDate.of(2025, 3, 1), new BigDecimal("100.00"));
        assertEquals(0, BigDecimal.ZERO.compareTo(currencies.openAmount(customer.id(), LocalDate.of(2025, 3, 2)).amount()));
    }
}
