package com.acme.backoffice;

import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.service.CategoryService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CategoryService categories = new CategoryService(store);

    private List<String> codes(List<Category> list) {
        return list.stream().map(Category::code).toList();
    }

    private List<String> names(List<Product> list) {
        return list.stream().map(Product::name).toList();
    }

    private void product(String sku, String name, String category, boolean active) {
        store.products().create(new Product(0, sku, name, category, BigDecimal.ONE, "piece", active));
    }

    @Test
    void createsTopLevelAndChildCategories() {
        Category top = categories.createCategory("HW", "Hardware", null);
        Category child = categories.createCategory("FAS", "Fasteners", "HW");
        assertNull(top.parentCode());
        assertEquals("HW", child.parentCode());
        assertNull(categories.createCategory("FD", "Food", " ").parentCode());
        assertEquals(child, store.categories().findByCode("FAS").orElseThrow());
        assertTrue(store.categories().findByCode("nope").isEmpty());
    }

    @Test
    void listsAllCategoriesAndChildren() {
        categories.createCategory("HW", "Hardware", null);
        categories.createCategory("FD", "Food", null);
        categories.createCategory("NUT", "Nuts", "HW");
        categories.createCategory("BLT", "Bolts", "HW");
        categories.createCategory("HEX", "Hex bolts", "BLT");
        assertEquals(List.of("BLT", "FD", "HEX", "HW", "NUT"), codes(categories.listCategories()));
        assertEquals(List.of("BLT", "NUT"), codes(categories.listChildren("HW")));
        assertTrue(categories.listChildren("NUT").isEmpty());
        assertTrue(categories.listChildren("nope").isEmpty());
    }

    @Test
    void listsActiveProductsIncludingChildCategories() {
        categories.createCategory("HW", "Hardware", null);
        categories.createCategory("BLT", "Bolts", "HW");
        categories.createCategory("HEX", "Hex bolts", "BLT");
        categories.createCategory("FD", "Food", null);
        product("1", "Hammer", "HW", true);
        product("2", "Bolt", "BLT", true);
        product("3", "Hex bolt", "HEX", true);
        product("4", "Old bolt", "BLT", false);
        product("5", "Apple", "FD", true);
        product("6", "Loose", "UNLISTED", true);
        assertEquals(List.of("Bolt", "Hammer", "Hex bolt"), names(categories.listProducts("HW")));
        assertEquals(List.of("Bolt", "Hex bolt"), names(categories.listProducts("BLT")));
        assertEquals(List.of("Hex bolt"), names(categories.listProducts("HEX")));
        assertEquals(List.of("Apple"), names(categories.listProducts("FD")));
        assertTrue(categories.listProducts("nope").isEmpty());
    }

    @Test
    void rejectsInvalidCategories() {
        categories.createCategory("HW", "Hardware", null);
        assertThrows(IllegalArgumentException.class, () -> categories.createCategory("HW", "Again", null));
        assertThrows(IllegalArgumentException.class, () -> categories.createCategory(" ", "Blank", null));
        assertThrows(IllegalArgumentException.class, () -> categories.createCategory("X", " ", null));
        assertThrows(IllegalArgumentException.class, () -> categories.createCategory("X", "Orphan", "nope"));
    }
}
