package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {
    private final DataStore store = new DataStore(Database.openInMemory());

    @Test
    void createAndFindBySku() {
        Product created = store.products().create(new Product(0, "A-1", "Anvil", "Tools", new BigDecimal("99.90"), "piece", true));
        assertTrue(created.id() > 0);
        assertEquals(created, store.products().findBySku("A-1").orElseThrow());
        assertTrue(store.products().findBySku("nope").isEmpty());
    }

    @Test
    void skuMustBeUnique() {
        store.products().create(new Product(0, "A-1", "Anvil", "Tools", new BigDecimal("99.90"), "piece", true));
        assertThrows(IllegalStateException.class,
                () -> store.products().create(new Product(0, "A-1", "Other", "Tools", new BigDecimal("1.00"), "piece", true)));
    }

    @Test
    void update() {
        Product created = store.products().create(new Product(0, "A-1", "Anvil", "Tools", new BigDecimal("99.90"), "piece", true));
        Product changed = new Product(created.id(), "A-2", "Big Anvil", "Heavy", new BigDecimal("150.00"), "kg", false);
        store.products().update(changed);
        assertEquals(changed, store.products().findBySku("A-2").orElseThrow());
        assertTrue(store.products().findBySku("A-1").isEmpty());
    }

    @Test
    void listActiveSortedByName() {
        store.products().create(new Product(0, "C", "Charlie", "X", new BigDecimal("1.00"), "piece", true));
        store.products().create(new Product(0, "A", "Alpha", "X", new BigDecimal("2.00"), "piece", true));
        store.products().create(new Product(0, "B", "Bravo", "X", new BigDecimal("3.00"), "piece", false));
        List<String> names = store.products().listActive().stream().map(Product::name).toList();
        assertEquals(List.of("Alpha", "Charlie"), names);
    }

    private void createSearchData() {
        store.products().create(new Product(0, "A", "Steel Anvil", "Tools", new BigDecimal("10.00"), "piece", true));
        store.products().create(new Product(0, "B", "Anvil 100%", "Tools", new BigDecimal("20.00"), "piece", true));
        store.products().create(new Product(0, "C", "Apples", "Food", new BigDecimal("15.00"), "kg", true));
        store.products().create(new Product(0, "D", "Old Anvil", "Tools", new BigDecimal("15.00"), "piece", false));
    }

    private static List<String> names(List<Product> products) {
        return products.stream().map(Product::name).toList();
    }

    @Test
    void searchByNamePartIgnoresCaseAndInactive() {
        createSearchData();
        assertEquals(List.of("Anvil 100%", "Steel Anvil"), names(store.products().searchActiveByName("ANVIL")));
        assertEquals(List.of("Anvil 100%"), names(store.products().searchActiveByName("100%")));
        assertTrue(store.products().searchActiveByName("a_v").isEmpty());
    }

    @Test
    void searchByCategoryOnlyActive() {
        createSearchData();
        assertEquals(List.of("Anvil 100%", "Steel Anvil"), names(store.products().searchActiveByCategory("Tools")));
        assertTrue(store.products().searchActiveByCategory("Nothing").isEmpty());
    }

    @Test
    void searchByPriceRangeIsInclusiveAndOnlyActive() {
        createSearchData();
        assertEquals(List.of("Anvil 100%", "Apples", "Steel Anvil"),
                names(store.products().searchActiveByPriceRange(new BigDecimal("10.00"), new BigDecimal("20.00"))));
        assertEquals(List.of("Apples"),
                names(store.products().searchActiveByPriceRange(new BigDecimal("15.00"), new BigDecimal("15.00"))));
    }
}
