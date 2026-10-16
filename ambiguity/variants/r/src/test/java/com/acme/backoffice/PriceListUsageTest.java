package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.PriceListUsage;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.OrderEntryService;
import com.acme.backoffice.service.PriceListService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceListUsageTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderEntryService entry = new OrderEntryService(store);
    private final PriceListService prices = new PriceListService(store);
    private final Customer zeta = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
    private final Product gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("4.00"), "piece", true));
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);
    private final LocalDate june30 = LocalDate.of(2024, 6, 30);

    private void place(String number, Customer customer, LocalDate date, OrderStatus status, OrderLine... lines) {
        entry.placeOrder(number, customer.id(), date, status, List.of(lines));
    }

    private void assertAmount(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual));
    }

    @Test
    void comparesSpecialRevenueWithCatalogueRevenuePerCustomer() {
        prices.addPrice(zeta.id(), widget.id(), new BigDecimal("7.00"), june1, june30);
        prices.addPrice(alpha.id(), gadget.id(), new BigDecimal("3.00"), june1, june30);
        place("SO-1", zeta, LocalDate.of(2024, 6, 5), OrderStatus.CONFIRMED,
                new OrderLine(widget.id(), 10, new BigDecimal("10.00")), new OrderLine(gadget.id(), 5, new BigDecimal("4.00")));
        place("SO-2", zeta, june30, OrderStatus.CONFIRMED, new OrderLine(widget.id(), 2, new BigDecimal("10.00")));
        place("SO-3", alpha, june1, OrderStatus.CONFIRMED, new OrderLine(gadget.id(), 4, new BigDecimal("4.00")));

        List<PriceListUsage> usage = prices.usage(june1, june30);
        assertEquals(2, usage.size());
        assertEquals("Alpha", usage.get(0).customerName());
        assertEquals(1, usage.get(0).lineCount());
        assertAmount("12.00", usage.get(0).specialRevenue());
        assertAmount("16.00", usage.get(0).catalogueRevenue());
        assertEquals("Zeta", usage.get(1).customerName());
        assertEquals(2, usage.get(1).lineCount());
        assertAmount("84.00", usage.get(1).specialRevenue());
        assertAmount("120.00", usage.get(1).catalogueRevenue());
    }

    @Test
    void ignoresCancelledOrdersOtherPeriodsAndLinesWithoutSpecialPrice() {
        prices.addPrice(zeta.id(), widget.id(), new BigDecimal("7.00"), june1, LocalDate.of(2024, 6, 15));
        place("SO-1", zeta, LocalDate.of(2024, 6, 5), OrderStatus.CANCELLED, new OrderLine(widget.id(), 1, new BigDecimal("10.00")));
        place("SO-2", zeta, LocalDate.of(2024, 6, 20), OrderStatus.CONFIRMED, new OrderLine(widget.id(), 1, new BigDecimal("10.00")));
        place("SO-3", zeta, LocalDate.of(2024, 6, 10), OrderStatus.CONFIRMED, new OrderLine(widget.id(), 1, new BigDecimal("10.00")));

        assertEquals(1, prices.usage(june1, june30).get(0).lineCount());
        assertTrue(prices.usage(LocalDate.of(2024, 6, 11), june30).isEmpty());
        assertTrue(prices.usage(LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31)).isEmpty());
    }
}
