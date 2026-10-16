package com.acme.backoffice;

import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.CategorySupplierProducts;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.service.SupplierProductService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SupplierProductsPerCategoryTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SupplierProductService service = new SupplierProductService(store);
    private final Supplier nordic = store.suppliers().create(new Supplier(0, "Nordic Supply", "n@example.com", "Sweden", 3));
    private final Supplier baltic = store.suppliers().create(new Supplier(0, "Baltic Trade", "b@example.com", "Latvia", 4));

    private Product product(String sku, String category) {
        return store.products().create(new Product(0, sku, sku, category, new BigDecimal("1.00"), "piece", true));
    }

    @Test
    void listsCheapestFirstAndFindsSingleSupplierCategories() {
        store.categories().create(new Category("ROOT", "Root", null));
        store.categories().create(new Category("SUB", "Sub", "ROOT"));
        store.categories().create(new Category("EMPTY", "Empty", "ROOT"));
        store.categories().create(new Category("OTHER", "Other", null));
        Product a = product("A", "ROOT");
        Product b = product("B", "SUB");
        Product c = product("C", "OTHER");
        service.record(nordic.id(), a.id(), 5, new BigDecimal("2.50"));
        service.record(baltic.id(), a.id(), 9, new BigDecimal("1.75"));
        service.record(nordic.id(), b.id(), 4, new BigDecimal("3.00"));
        service.record(nordic.id(), c.id(), 1, new BigDecimal("0.10"));

        CategorySupplierProducts result = service.listSupplierProductsOfCategory("ROOT");

        assertEquals("ROOT", result.category().code());
        assertEquals(List.of("1.75", "2.50", "3.00"),
                result.supplierProducts().stream().map(sp -> sp.lastUnitCost().toPlainString()).toList());
        assertEquals(List.of("SUB:Nordic Supply"),
                result.singleSupplier().stream().map(s -> s.category() + ":" + s.supplier().name()).toList());
    }

    @Test
    void emptyCategoryHasNothingAndUnknownCategoryFails() {
        store.categories().create(new Category("EMPTY", "Empty", null));
        CategorySupplierProducts result = service.listSupplierProductsOfCategory("EMPTY");
        assertEquals(List.of(), result.supplierProducts());
        assertEquals(List.of(), result.singleSupplier());
        assertThrows(IllegalArgumentException.class, () -> service.listSupplierProductsOfCategory("NOPE"));
    }
}
