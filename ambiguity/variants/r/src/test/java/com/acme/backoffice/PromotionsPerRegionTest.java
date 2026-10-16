package com.acme.backoffice;

import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.service.PromotionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromotionsPerRegionTest {
    private final PromotionService promotions = new PromotionService(new DataStore(Database.openInMemory()));
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);
    private final LocalDate june30 = LocalDate.of(2024, 6, 30);

    private List<String> codes(List<Promotion> list) {
        return list.stream().map(Promotion::code).toList();
    }

    @Test
    void listsRegionPromotionsAndUnlimitedOnes() {
        promotions.createPromotion("ALL", BigDecimal.TEN, june1, june30, null);
        promotions.createPromotion("N", BigDecimal.TEN, june1, june30, "Hardware");
        promotions.createPromotion("S", BigDecimal.TEN, june1, june30, null);
        promotions.limitToRegion("N", Region.NORTH);
        promotions.limitToRegion("S", Region.SOUTH);
        assertEquals(List.of("ALL", "N"), codes(promotions.listActive(june1, Region.NORTH)));
        assertEquals(List.of("ALL", "S"), codes(promotions.listActive(june1, Region.SOUTH)));
        assertEquals(List.of("ALL"), codes(promotions.listActive(june1, Region.EAST)));
    }

    @Test
    void respectsPeriodAndChangedRegion() {
        promotions.createPromotion("N", BigDecimal.TEN, june1, june30, null);
        promotions.limitToRegion("N", Region.NORTH);
        assertEquals(List.of("N"), codes(promotions.listActive(june30, Region.NORTH)));
        assertTrue(promotions.listActive(LocalDate.of(2024, 7, 1), Region.NORTH).isEmpty());
        promotions.limitToRegion("N", Region.WEST);
        assertTrue(promotions.listActive(june1, Region.NORTH).isEmpty());
        assertEquals(List.of("N"), codes(promotions.listActive(june1, Region.WEST)));
    }

    @Test
    void rejectsMissingRegionOrUnknownCode() {
        assertThrows(IllegalArgumentException.class, () -> promotions.limitToRegion("NOPE", Region.NORTH));
        promotions.createPromotion("X", BigDecimal.TEN, june1, june30, null);
        assertThrows(IllegalArgumentException.class, () -> promotions.limitToRegion("X", null));
        assertThrows(IllegalArgumentException.class, () -> promotions.listActive(june1, null));
    }
}
