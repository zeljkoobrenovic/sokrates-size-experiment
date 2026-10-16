package com.acme.backoffice;

import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.service.PromotionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Spaces typed around a promotion code when the promotion is set up are not part of the code. */
class PromotionCodeSpacesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PromotionService promotions = new PromotionService(store);
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);
    private final LocalDate june30 = LocalDate.of(2024, 6, 30);

    @Test
    void spacesAroundTheCodeAreDropped() {
        Promotion created = promotions.createPromotion("  SPRING10 ", new BigDecimal("10"), june1, june30, null);

        assertEquals("SPRING10", created.code());
        assertTrue(store.findPromotionByCode("SPRING10").isPresent(), "the promotion is found by the code without spaces");
        assertEquals(List.of("SPRING10"),
                promotions.listActive(LocalDate.of(2024, 6, 15)).stream().map(Promotion::code).toList());
    }

    @Test
    void theSameCodeWithOrWithoutSpacesIsADuplicate() {
        promotions.createPromotion("SPRING10", new BigDecimal("10"), june1, june30, null);
        assertThrows(IllegalArgumentException.class,
                () -> promotions.createPromotion(" SPRING10", new BigDecimal("15"), june1, june30, null));
        assertEquals(1, promotions.listActive(LocalDate.of(2024, 6, 15)).size());
    }
}
