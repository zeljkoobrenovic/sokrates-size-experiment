package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.OrderEntryService;
import com.acme.backoffice.service.PromotionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderPromotionTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderEntryService entry = new OrderEntryService(store);
    private final PromotionService promotions = new PromotionService(store);
    private final Customer customer = store.customers().create(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product apples = store.products().create(new Product(0, "A-1", "Apples", "Food", new BigDecimal("12.00"), "kg", true));
    private final LocalDate june10 = LocalDate.of(2024, 6, 10);

    private SalesOrder place(String number, LocalDate date) {
        return entry.placeOrder(number, customer.id(), date, OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("10.00")), new OrderLine(apples.id(), 1, new BigDecimal("12.00"))));
    }

    @Test
    void appliesBestPromotionForCategoryOrAllProducts() {
        promotions.createPromotion("ALL10", new BigDecimal("10"), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), null);
        promotions.createPromotion("HW25", new BigDecimal("25"), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), "Hardware");
        promotions.createPromotion("FOOD5", new BigDecimal("5"), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), "Food");
        SalesOrder order = place("SO-1", june10);
        assertEquals(new BigDecimal("7.50"), order.lines().get(0).unitPrice());
        assertEquals("HW25", order.lines().get(0).promotionCode());
        assertEquals(new BigDecimal("10.80"), order.lines().get(1).unitPrice());
        assertEquals("ALL10", order.lines().get(1).promotionCode());
    }

    @Test
    void recordsPromotionCodeOnStoredLine() {
        promotions.createPromotion("ALL10", new BigDecimal("10"), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), null);
        place("SO-1", june10);
        SalesOrder found = store.orders().findByNumber("SO-1").orElseThrow();
        assertEquals("ALL10", found.lines().get(0).promotionCode());
        assertEquals(new BigDecimal("9.00"), found.lines().get(0).unitPrice());
    }

    @Test
    void leavesPriceAloneWhenNoPromotionIsActiveOnOrderDate() {
        promotions.createPromotion("ALL10", new BigDecimal("10"), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), null);
        SalesOrder order = place("SO-1", LocalDate.of(2024, 7, 1));
        assertEquals(new BigDecimal("10.00"), order.lines().get(0).unitPrice());
        assertNull(order.lines().get(0).promotionCode());
        assertNull(store.orders().findByNumber("SO-1").orElseThrow().lines().get(0).promotionCode());
    }

    @Test
    void promotionPeriodIsInclusive() {
        promotions.createPromotion("ALL10", new BigDecimal("10"), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), null);
        assertEquals("ALL10", place("SO-1", LocalDate.of(2024, 6, 1)).lines().get(0).promotionCode());
        assertEquals("ALL10", place("SO-2", LocalDate.of(2024, 6, 30)).lines().get(0).promotionCode());
    }

    @Test
    void rejectsUnknownProduct() {
        assertThrows(IllegalArgumentException.class, () -> entry.placeOrder("SO-1", customer.id(), june10, OrderStatus.DRAFT,
                List.of(new OrderLine(999, 1, BigDecimal.ONE))));
    }
}
