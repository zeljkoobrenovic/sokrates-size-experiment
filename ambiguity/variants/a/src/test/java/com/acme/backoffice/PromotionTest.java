package com.acme.backoffice;

import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.service.PromotionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromotionTest {
    private final PromotionService promotions = new PromotionService(new DataStore(Database.openInMemory()));
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);
    private final LocalDate june30 = LocalDate.of(2024, 6, 30);

    private List<String> codes(List<Promotion> list) {
        return list.stream().map(Promotion::code).toList();
    }

    @Test
    void createsPromotionForAllProductsOrCategory() {
        Promotion all = promotions.createPromotion("ALL", new BigDecimal("10"), june1, june30, null);
        Promotion hardware = promotions.createPromotion("HW", new BigDecimal("12.5"), june1, june30, "Hardware");
        assertTrue(all.id() > 0);
        assertNull(all.category());
        assertEquals("Hardware", hardware.category());
        assertNull(promotions.createPromotion("BLANK", BigDecimal.ONE, june1, june30, " ").category());
    }

    @Test
    void listsPromotionsActiveOnDateInclusive() {
        promotions.createPromotion("B", BigDecimal.TEN, june1, june30, null);
        promotions.createPromotion("A", BigDecimal.TEN, june30, LocalDate.of(2024, 7, 15), "Hardware");
        promotions.createPromotion("C", BigDecimal.TEN, LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 31), null);
        assertEquals(List.of("B"), codes(promotions.listActive(june1)));
        assertEquals(List.of("A", "B"), codes(promotions.listActive(june30)));
        assertEquals(List.of("A"), codes(promotions.listActive(LocalDate.of(2024, 7, 1))));
        assertTrue(promotions.listActive(LocalDate.of(2024, 5, 31)).isEmpty());
    }

    @Test
    void listsPromotionsOfCategoryOnly() {
        promotions.createPromotion("ALL", BigDecimal.TEN, june1, june30, null);
        promotions.createPromotion("HW2", BigDecimal.TEN, LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 30), "Hardware");
        promotions.createPromotion("HW1", BigDecimal.TEN, june1, june30, "Hardware");
        promotions.createPromotion("TOY", BigDecimal.TEN, june1, june30, "Toys");
        assertEquals(List.of("HW1", "HW2"), codes(promotions.listByCategory("Hardware")));
        assertTrue(promotions.listByCategory("Food").isEmpty());
    }

    @Test
    void rejectsInvalidPromotions() {
        promotions.createPromotion("P", BigDecimal.TEN, june1, june30, null);
        assertThrows(IllegalArgumentException.class, () -> promotions.createPromotion("P", BigDecimal.TEN, june1, june30, null));
        assertThrows(IllegalArgumentException.class, () -> promotions.createPromotion(" ", BigDecimal.TEN, june1, june30, null));
        assertThrows(IllegalArgumentException.class, () -> promotions.createPromotion("Z", BigDecimal.ZERO, june1, june30, null));
        assertThrows(IllegalArgumentException.class, () -> promotions.createPromotion("Z", new BigDecimal("100.01"), june1, june30, null));
        assertThrows(IllegalArgumentException.class, () -> promotions.createPromotion("Z", BigDecimal.TEN, june30, june1, null));
    }
}
