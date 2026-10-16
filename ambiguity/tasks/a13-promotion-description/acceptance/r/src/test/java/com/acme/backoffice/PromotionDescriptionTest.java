package com.acme.backoffice;

import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.model.Region;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A promotion has a description shown to customers (none when not given), kept when created and shown in every
 * promotion listing. The new attribute is reached by reflection so that this test compiles against the code without it.
 */
class PromotionDescriptionTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final LocalDate june1 = LocalDate.of(2024, 6, 1);
    private final LocalDate june30 = LocalDate.of(2024, 6, 30);

    private static final List<String> PROMOTION_BEFORE = List.of("id", "code", "discountPercent", "startDate", "endDate", "category");

    private static Promotion promotion(String code, String percent, LocalDate start, LocalDate end, String category,
            String description) {
        java.util.Map<String, Object> values = new java.util.HashMap<>();
        values.put("id", 0L); values.put("code", code); values.put("discountPercent", new BigDecimal(percent));
        values.put("startDate", start); values.put("endDate", end); values.put("category", category);
        return AddedRecordComponent.create(Promotion.class, PROMOTION_BEFORE, values, description, "A promotion's description");
    }

    private static String description(Promotion promotion) {
        return (String) AddedRecordComponent.read(promotion, PROMOTION_BEFORE, "A promotion's description");
    }

    private static List<String> descriptions(List<Promotion> promotions) {
        return promotions.stream().map(PromotionDescriptionTest::description).toList();
    }

    @Test
    void descriptionIsShownInEveryPromotionListing() {
        Promotion summer = store.promotions().create(promotion("SUMMER", "15", june1, june30, "Hardware", "Summer sale on all hardware"));
        store.promotions().create(promotion("TOOLS", "5", june1, june30, "Hardware", null));
        store.promotions().create(promotion("WINTER", "20", LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 31), "Hardware",
                "Winter clearance"));
        store.promotions().saveRegion(summer.id(), Region.NORTH);

        LocalDate june15 = LocalDate.of(2024, 6, 15);
        assertEquals("Summer sale on all hardware", description(summer));
        assertEquals("Summer sale on all hardware", description(store.promotions().findByCode("SUMMER").orElseThrow()));
        assertEquals(Arrays.asList("Summer sale on all hardware", null), descriptions(store.promotions().listActiveOn(june15)));
        assertEquals(Arrays.asList("Summer sale on all hardware", null),
                descriptions(store.promotions().listActiveOnInRegion(june15, Region.NORTH)));
        assertEquals(Arrays.asList("Summer sale on all hardware", null, "Winter clearance"),
                descriptions(store.promotions().listByCategory("Hardware")));
        assertEquals("Summer sale on all hardware", description(store.promotions().findBest("Hardware", june15).orElseThrow()));
        assertEquals(Arrays.asList("Summer sale on all hardware", null, "Winter clearance"),
                descriptions(store.promotions().listUnused(june1, LocalDate.of(2024, 12, 31))));
    }
}
