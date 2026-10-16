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
        Product created = store.createProduct(new Product(0, "A-1", "Anvil", "Tools", new BigDecimal("99.90"), "piece", true));
        assertTrue(created.id() > 0);
        assertEquals(created, store.findProductBySku("A-1").orElseThrow());
        assertTrue(store.findProductBySku("nope").isEmpty());
    }

    @Test
    void skuMustBeUnique() {
        store.createProduct(new Product(0, "A-1", "Anvil", "Tools", new BigDecimal("99.90"), "piece", true));
        assertThrows(IllegalStateException.class,
                () -> store.createProduct(new Product(0, "A-1", "Other", "Tools", new BigDecimal("1.00"), "piece", true)));
    }

    @Test
    void update() {
        Product created = store.createProduct(new Product(0, "A-1", "Anvil", "Tools", new BigDecimal("99.90"), "piece", true));
        Product changed = new Product(created.id(), "A-2", "Big Anvil", "Heavy", new BigDecimal("150.00"), "kg", false);
        store.updateProduct(changed);
        assertEquals(changed, store.findProductBySku("A-2").orElseThrow());
        assertTrue(store.findProductBySku("A-1").isEmpty());
    }

    @Test
    void listActiveSortedByName() {
        store.createProduct(new Product(0, "C", "Charlie", "X", new BigDecimal("1.00"), "piece", true));
        store.createProduct(new Product(0, "A", "Alpha", "X", new BigDecimal("2.00"), "piece", true));
        store.createProduct(new Product(0, "B", "Bravo", "X", new BigDecimal("3.00"), "piece", false));
        List<String> names = store.listActiveProductsOrderedByName().stream().map(Product::name).toList();
        assertEquals(List.of("Alpha", "Charlie"), names);
    }

    private void createSearchData() {
        store.createProduct(new Product(0, "A", "Steel Anvil", "Tools", new BigDecimal("10.00"), "piece", true));
        store.createProduct(new Product(0, "B", "Anvil 100%", "Tools", new BigDecimal("20.00"), "piece", true));
        store.createProduct(new Product(0, "C", "Apples", "Food", new BigDecimal("15.00"), "kg", true));
        store.createProduct(new Product(0, "D", "Old Anvil", "Tools", new BigDecimal("15.00"), "piece", false));
    }

    private static List<String> names(List<Product> products) {
        return products.stream().map(Product::name).toList();
    }

    @Test
    void searchByNamePartIgnoresCaseAndInactive() {
        createSearchData();
        assertEquals(List.of("Anvil 100%", "Steel Anvil"), names(store.listActiveProductsWithNameContaining("ANVIL")));
        assertEquals(List.of("Anvil 100%"), names(store.listActiveProductsWithNameContaining("100%")));
        assertTrue(store.listActiveProductsWithNameContaining("a_v").isEmpty());
    }

    @Test
    void searchByCategoryOnlyActive() {
        createSearchData();
        assertEquals(List.of("Anvil 100%", "Steel Anvil"), names(store.listActiveProductsOfExactCategory("Tools")));
        assertTrue(store.listActiveProductsOfExactCategory("Nothing").isEmpty());
    }

    @Test
    void searchByPriceRangeIsInclusiveAndOnlyActive() {
        createSearchData();
        assertEquals(List.of("Anvil 100%", "Apples", "Steel Anvil"),
                names(store.listActiveProductsInPriceRange(new BigDecimal("10.00"), new BigDecimal("20.00"))));
        assertEquals(List.of("Apples"),
                names(store.listActiveProductsInPriceRange(new BigDecimal("15.00"), new BigDecimal("15.00"))));
    }
}
