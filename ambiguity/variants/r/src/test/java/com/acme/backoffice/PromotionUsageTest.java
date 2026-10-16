package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.model.PromotionUsage;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.OrderEntryService;
import com.acme.backoffice.service.PromotionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromotionUsageTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderEntryService entry = new OrderEntryService(store);
    private final PromotionService promotions = new PromotionService(store);
    private final Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
    private final Product apples = store.products().create(new Product(0, "A-1", "Apples", "Food", new BigDecimal("12.00"), "kg", true));
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);
    private final LocalDate june30 = LocalDate.of(2024, 6, 30);

    private void place(String number, LocalDate date, OrderStatus status, OrderLine... lines) {
        entry.placeOrder(number, customer.id(), date, status, List.of(lines));
    }

    private OrderLine widgets(int quantity) {
        return new OrderLine(widget.id(), quantity, new BigDecimal("10.00"));
    }

    private OrderLine apples(int quantity) {
        return new OrderLine(apples.id(), quantity, new BigDecimal("12.00"));
    }

    @Test
    void summarisesLinesDiscountAndRevenuePerPromotion() {
        promotions.createPromotion("HW20", new BigDecimal("20"), june1, june30, "Hardware");
        promotions.createPromotion("FOOD50", new BigDecimal("50"), june1, june30, "Food");
        place("SO-1", LocalDate.of(2024, 6, 5), OrderStatus.CONFIRMED, widgets(10), apples(2));
        place("SO-2", june30, OrderStatus.CONFIRMED, widgets(5));

        List<PromotionUsage> usage = promotions.usage(june1, june30);
        assertEquals(2, usage.size());
        assertEquals("FOOD50", usage.get(0).code());
        assertEquals(1, usage.get(0).lineCount());
        assertEquals(0, new BigDecimal("12.00").compareTo(usage.get(0).discount()));
        assertEquals(0, new BigDecimal("12.00").compareTo(usage.get(0).revenue()));
        assertEquals("HW20", usage.get(1).code());
        assertEquals(2, usage.get(1).lineCount());
        assertEquals(0, new BigDecimal("30.00").compareTo(usage.get(1).discount()));
        assertEquals(0, new BigDecimal("120.00").compareTo(usage.get(1).revenue()));
    }

    @Test
    void ignoresCancelledOrdersAndOrdersOutsideThePeriod() {
        promotions.createPromotion("HW20", new BigDecimal("20"), june1, june30, "Hardware");
        place("SO-1", LocalDate.of(2024, 6, 5), OrderStatus.CANCELLED, widgets(10));
        place("SO-2", LocalDate.of(2024, 6, 20), OrderStatus.CONFIRMED, widgets(1));
        place("SO-3", LocalDate.of(2024, 6, 21), OrderStatus.CONFIRMED, widgets(1));

        assertEquals(1, promotions.usage(june1, LocalDate.of(2024, 6, 20)).get(0).lineCount());
        assertEquals(2, promotions.usage(june1, june30).get(0).lineCount());
        assertTrue(promotions.usage(LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31)).isEmpty());
    }

    @Test
    void listsUnusedPromotionsRunningInThePeriod() {
        promotions.createPromotion("HW20", new BigDecimal("20"), june1, june30, "Hardware");
        promotions.createPromotion("ALL5", new BigDecimal("5"), june1, june30, null);
        promotions.createPromotion("FOOD10", new BigDecimal("10"), june1, june30, "Food");
        promotions.createPromotion("JULY", new BigDecimal("5"), LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31), null);
        place("SO-1", LocalDate.of(2024, 6, 5), OrderStatus.CANCELLED, widgets(10));
        place("SO-2", LocalDate.of(2024, 6, 6), OrderStatus.CONFIRMED, apples(1));

        List<String> unused = promotions.listUnused(june1, june30).stream().map(Promotion::code).toList();
        assertEquals(List.of("ALL5", "HW20"), unused);
    }

    @Test
    void hundredPercentPromotionUsesCataloguePrice() {
        promotions.createPromotion("FREE", new BigDecimal("100"), june1, june30, "Hardware");
        place("SO-1", june1, OrderStatus.CONFIRMED, widgets(3));
        PromotionUsage usage = promotions.usage(june1, june30).get(0);
        assertEquals(0, new BigDecimal("30.00").compareTo(usage.discount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(usage.revenue()));
    }
}
