package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Currency;
import com.acme.backoffice.model.CustomerOpenAmount;
import com.acme.backoffice.model.ExchangeRate;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceCurrency;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.SalesOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Invoicing in EUR, USD or GBP with fixed exchange rates per date. Invoice amounts (and so all reports) stay in EUR;
 * the invoice currency and the amount in that currency are recorded next to them.
 */
public class CurrencyService {
    private final DataStore store;

    public CurrencyService(DataStore store) {
        this.store = store;
    }

    /** Sets the rate (units of the currency per 1 EUR) valid from the date on. EUR itself always has rate 1. */
    public ExchangeRate setRate(Currency currency, LocalDate rateDate, BigDecimal perEur) {
        if (currency == null || rateDate == null || perEur == null) {
            throw new IllegalArgumentException("Currency, date and rate are required");
        }
        if (currency == Currency.EUR) {
            throw new IllegalArgumentException("EUR has a fixed rate of 1");
        }
        if (perEur.signum() <= 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }
        return store.saveExchangeRate(new ExchangeRate(0, currency, rateDate, perEur));
    }

    public void setCustomerCurrency(long customerId, Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency is required");
        }
        store.saveCustomerCurrency(customerId, currency);
    }

    /** The currency the customer is invoiced in, EUR unless set otherwise. */
    public Currency getCustomerCurrency(long customerId) {
        return store.findCustomerCurrency(customerId).orElse(Currency.EUR);
    }

    /** Converts an EUR amount to the currency at the rate valid on the date. */
    public BigDecimal convertFromEur(BigDecimal eurAmount, Currency currency, LocalDate asOf) {
        if (currency == Currency.EUR) {
            return eurAmount;
        }
        ExchangeRate rate = store.findExchangeRateOn(currency, asOf)
                .orElseThrow(() -> new IllegalStateException("No " + currency + " rate on " + asOf));
        return eurAmount.multiply(rate.perEur()).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Invoices the order like {@link InvoiceService#invoice} and records the invoice in the currency, converted at the
     * rate valid on the invoice date. A null currency means the currency of the customer.
     */
    public Invoice invoice(String orderNumber, LocalDate invoiceDate, Currency currency) {
        if (invoiceDate == null) {
            throw new IllegalArgumentException("Invoice date is required");
        }
        SalesOrder order = store.findSalesOrderByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + orderNumber));
        Currency invoiceCurrency = currency != null ? currency : getCustomerCurrency(order.customerId());
        if (invoiceCurrency != Currency.EUR && store.findExchangeRateOn(invoiceCurrency, invoiceDate).isEmpty()) {
            throw new IllegalStateException("No " + invoiceCurrency + " rate on " + invoiceDate);
        }
        Invoice invoice = new InvoiceService(store).invoice(orderNumber, invoiceDate);
        store.createInvoiceCurrency(new InvoiceCurrency(invoice.id(), invoiceCurrency,
                convertFromEur(invoice.amount(), invoiceCurrency, invoiceDate)));
        return invoice;
    }

    /** The currency and amount of the invoice in that currency; EUR and the invoice amount if none was recorded. */
    public InvoiceCurrency getInvoiceCurrency(Invoice invoice) {
        return store.findInvoiceCurrency(invoice.id())
                .orElse(new InvoiceCurrency(invoice.id(), Currency.EUR, invoice.amount()));
    }

    /**
     * The unpaid part of the customer's open invoices in the customer's currency. Payments are kept in EUR, so the
     * unpaid share of an invoice issued in the customer's currency is taken from its recorded amount; invoices in
     * another currency are converted at the rate valid on the given date.
     */
    public CustomerOpenAmount openAmount(long customerId, LocalDate asOf) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        Currency currency = getCustomerCurrency(customerId);
        BigDecimal total = BigDecimal.ZERO;
        for (Invoice invoice : store.listAllInvoicesOfCustomerNewestFirst(customerId)) {
            if (invoice.status() != InvoiceStatus.OPEN) {
                continue;
            }
            BigDecimal unpaidEur = invoice.amount().subtract(store.sumPaymentsOfInvoice(invoice.id()));
            InvoiceCurrency recorded = getInvoiceCurrency(invoice);
            if (recorded.currency() == currency && invoice.amount().signum() != 0) {
                total = total.add(recorded.amount().multiply(unpaidEur).divide(invoice.amount(), 2, RoundingMode.HALF_UP));
            } else {
                total = total.add(convertFromEur(unpaidEur, currency, asOf));
            }
        }
        return new CustomerOpenAmount(currency, total.setScale(2, RoundingMode.HALF_UP));
    }
}
