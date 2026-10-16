package com.acme.backoffice;

import com.acme.backoffice.model.Currency;
import com.acme.backoffice.model.ExchangeRate;
import com.acme.backoffice.model.InvoiceCurrency;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Foreign currencies: the exchange rates against EUR by date, the currency a customer is invoiced in, and the currency
 * and amount an invoice was issued in.
 */
public final class CurrencyStore {
    private final Jdbc jdbc;

    CurrencyStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    // ---- exchange rates ----

    /** Sets the exchange rate (units of the currency per 1 EUR) valid from the date on, replacing a rate set for the same date. */
    public ExchangeRate saveExchangeRate(ExchangeRate rate) {
        // Two statements without a transaction, as in the original.
        jdbc.update("Cannot save exchange rate",
                "MERGE INTO exchange_rate (currency, rate_date, per_eur) KEY (currency, rate_date) VALUES (?, ?, ?)",
                rate.currency().name(), rate.rateDate(), rate.perEur());
        long id = jdbc.single("Cannot save exchange rate", "SELECT id FROM exchange_rate WHERE currency = ? AND rate_date = ?",
                rs -> rs.getLong("id"), rate.currency().name(), rate.rateDate());
        return new ExchangeRate(id, rate.currency(), rate.rateDate(), rate.perEur());
    }

    /** The rate of the currency that applies on the date: the latest one set on or before it, if any. */
    public Optional<ExchangeRate> findExchangeRate(Currency currency, LocalDate asOf) {
        return jdbc.first("Cannot find exchange rate", "SELECT id, currency, rate_date, per_eur FROM exchange_rate "
                        + "WHERE currency = ? AND rate_date <= ? ORDER BY rate_date DESC LIMIT 1",
                rs -> new ExchangeRate(rs.getLong("id"), Currency.valueOf(rs.getString("currency")),
                        rs.getObject("rate_date", LocalDate.class), rs.getBigDecimal("per_eur")),
                currency.name(), asOf);
    }

    // ---- currency of a customer ----

    /** Sets the currency the customer is invoiced in, replacing the one set before. */
    public void saveCustomerCurrency(long customerId, Currency currency) {
        jdbc.update("Cannot save customer currency",
                "MERGE INTO customer_currency (customer_id, currency) KEY (customer_id) VALUES (?, ?)", customerId, currency.name());
    }

    /** The currency the customer is invoiced in, if one was set (callers use EUR otherwise). */
    public Optional<Currency> findCustomerCurrency(long customerId) {
        return jdbc.first("Cannot find customer currency", "SELECT currency FROM customer_currency WHERE customer_id = ?",
                rs -> Currency.valueOf(rs.getString("currency")), customerId);
    }

    // ---- currency of an invoice ----

    public InvoiceCurrency createInvoiceCurrency(InvoiceCurrency invoiceCurrency) {
        jdbc.update("Cannot create invoice currency", "INSERT INTO invoice_currency (invoice_id, currency, amount) VALUES (?, ?, ?)",
                invoiceCurrency.invoiceId(), invoiceCurrency.currency().name(), invoiceCurrency.amount());
        return invoiceCurrency;
    }

    /** The currency and amount an invoice was issued in, if recorded (an invoice without one is in EUR). */
    public Optional<InvoiceCurrency> findInvoiceCurrency(long invoiceId) {
        return jdbc.first("Cannot find invoice currency", "SELECT invoice_id, currency, amount FROM invoice_currency WHERE invoice_id = ?",
                rs -> new InvoiceCurrency(rs.getLong("invoice_id"), Currency.valueOf(rs.getString("currency")),
                        rs.getBigDecimal("amount")), invoiceId);
    }
}
