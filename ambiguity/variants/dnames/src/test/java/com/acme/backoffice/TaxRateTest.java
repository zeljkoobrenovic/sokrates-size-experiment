package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceTax;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.TaxService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaxRateTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final TaxService taxes = new TaxService(store);
    private final Customer german = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "Germany", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer swiss = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "Switzerland", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private Invoice invoice(Customer customer, String number, LocalDate date) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), LocalDate.of(2025, 1, 1), OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("10.05")))));
        return new InvoiceService(store).invoice(number, date);
    }

    private void assertAmount(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual), "expected " + expected + " but was " + actual);
    }

    @Test
    void rateAppliesFromItsDateOnAndCanBeReplaced() {
        taxes.setRate("Germany", LocalDate.of(2025, 1, 1), new BigDecimal("19"));
        taxes.setRate("Germany", LocalDate.of(2025, 6, 1), new BigDecimal("21"));
        assertAmount("0", taxes.getRatePercent("Germany", LocalDate.of(2024, 12, 31)));
        assertAmount("19", taxes.getRatePercent("Germany", LocalDate.of(2025, 5, 31)));
        assertAmount("21", taxes.getRatePercent("Germany", LocalDate.of(2025, 6, 1)));
        taxes.setRate("Germany", LocalDate.of(2025, 6, 1), new BigDecimal("22"));
        assertAmount("22", taxes.getRatePercent("Germany", LocalDate.of(2025, 7, 1)));
    }

    @Test
    void invalidRatesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> taxes.setRate("Germany", LocalDate.of(2025, 1, 1), new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class, () -> taxes.setRate("Germany", LocalDate.of(2025, 1, 1), new BigDecimal("101")));
        assertThrows(IllegalArgumentException.class, () -> taxes.setRate(" ", LocalDate.of(2025, 1, 1), new BigDecimal("19")));
    }

    @Test
    void invoiceShowsNetVatAndGrossUsingRateOnInvoiceDate() {
        taxes.setRate("Germany", LocalDate.of(2025, 1, 1), new BigDecimal("19"));
        taxes.setRate("Germany", LocalDate.of(2025, 6, 1), new BigDecimal("21"));
        InvoiceTax early = taxes.getInvoiceTax(invoice(german, "SO-1", LocalDate.of(2025, 3, 1)));
        assertAmount("19", early.ratePercent());
        assertAmount("100.50", early.net());
        assertAmount("19.10", early.vat());
        assertAmount("119.60", early.gross());
        InvoiceTax late = taxes.getInvoiceTax(invoice(german, "SO-2", LocalDate.of(2025, 6, 1)));
        assertAmount("21.11", late.vat());
        assertAmount("121.61", late.gross());
    }

    @Test
    void ratesAreKeptPerCountryAndNoRateMeansNoVat() {
        taxes.setRate("Germany", LocalDate.of(2025, 1, 1), new BigDecimal("19"));
        InvoiceTax tax = taxes.getInvoiceTax(invoice(swiss, "SO-3", LocalDate.of(2025, 3, 1)));
        assertAmount("0", tax.vat());
        assertAmount("100.50", tax.gross());
    }
}
