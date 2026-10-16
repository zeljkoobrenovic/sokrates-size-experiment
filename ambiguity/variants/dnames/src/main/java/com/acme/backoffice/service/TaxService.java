package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceTax;
import com.acme.backoffice.model.TaxRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * VAT per country, with rates valid from a date on. The amount of an invoice is its net amount; the VAT is calculated
 * with the rate of the customer's country on the invoice date. A country without a rate has no VAT.
 */
public class TaxService {
    private final DataStore store;

    public TaxService(DataStore store) {
        this.store = store;
    }

    /** Sets the rate in percent of the country valid from the date on, replacing a rate set for the same date. */
    public TaxRate setRate(String country, LocalDate validFrom, BigDecimal ratePercent) {
        if (country == null || country.isBlank() || validFrom == null || ratePercent == null) {
            throw new IllegalArgumentException("Country, date and rate are required");
        }
        if (ratePercent.signum() < 0 || ratePercent.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Rate must be between 0 and 100");
        }
        return store.saveTaxRate(new TaxRate(0, country, validFrom, ratePercent));
    }

    /** The VAT rate in percent of the country on the date; zero if no rate is valid yet. */
    public BigDecimal getRatePercent(String country, LocalDate asOf) {
        return store.findTaxRateOn(country, asOf).map(TaxRate::ratePercent).orElse(BigDecimal.ZERO);
    }

    /** The net, VAT and gross amount of the invoice, using the rate of the customer's country on the invoice date. */
    public InvoiceTax getInvoiceTax(Invoice invoice) {
        Customer customer = store.findCustomerById(invoice.customerId())
                .orElseThrow(() -> new IllegalStateException("Unknown customer: " + invoice.customerId()));
        BigDecimal rate = getRatePercent(customer.country(), invoice.invoiceDate());
        BigDecimal net = invoice.amount();
        BigDecimal vat = net.multiply(rate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return new InvoiceTax(invoice.id(), rate, net, vat, net.add(vat));
    }
}
