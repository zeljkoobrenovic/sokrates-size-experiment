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

    private static Promotion promotion(String code, String percent, LocalDate start, LocalDate end, String category,
            String description) {
        try {
            return Promotion.class.getConstructor(long.class, String.class, BigDecimal.class, LocalDate.class, LocalDate.class,
                    String.class, String.class).newInstance(0L, code, new BigDecimal(percent), start, end, category, description);
        } catch (NoSuchMethodException e) {
            return fail("A promotion cannot be given a description");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String description(Promotion promotion) {
        try {
            Method accessor = Promotion.class.getMethod("description");
            return (String) accessor.invoke(promotion);
        } catch (NoSuchMethodException e) {
            return fail("A promotion shows no description");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static List<String> descriptions(List<Promotion> promotions) {
        return promotions.stream().map(PromotionDescriptionTest::description).toList();
    }

    @Test
    void descriptionIsShownInEveryPromotionListing() {
        Promotion summer = store.createPromotion(promotion("SUMMER", "15", june1, june30, "Hardware", "Summer sale on all hardware"));
        store.createPromotion(promotion("TOOLS", "5", june1, june30, "Hardware", null));
        store.createPromotion(promotion("WINTER", "20", LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 31), "Hardware",
                "Winter clearance"));
        store.savePromotionRegion(summer.id(), Region.NORTH);

        LocalDate june15 = LocalDate.of(2024, 6, 15);
        assertEquals("Summer sale on all hardware", description(summer));
        assertEquals("Summer sale on all hardware", description(store.findPromotionByCode("SUMMER").orElseThrow()));
        assertEquals(Arrays.asList("Summer sale on all hardware", null), descriptions(store.listPromotionsActiveOnDate(june15)));
        assertEquals(Arrays.asList("Summer sale on all hardware", null),
                descriptions(store.listPromotionsActiveOnDateInRegionOrAllRegions(june15, Region.NORTH)));
        assertEquals(Arrays.asList("Summer sale on all hardware", null, "Winter clearance"),
                descriptions(store.listPromotionsOfExactCategory("Hardware")));
        assertEquals("Summer sale on all hardware", description(store.findHighestDiscountPromotionForCategoryOnDate("Hardware", june15).orElseThrow()));
        assertEquals(Arrays.asList("Summer sale on all hardware", null, "Winter clearance"),
                descriptions(store.listUnusedPromotionsBetween(june1, LocalDate.of(2024, 12, 31))));
    }
}
