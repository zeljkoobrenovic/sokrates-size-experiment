package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseSuggestion;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupplierProduct;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.ReorderService;
import com.acme.backoffice.service.StockService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PurchaseSuggestionTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final ReorderService reorder = new ReorderService(store);
    private final Product bolt = store.createProduct(new Product(0, "P-1", "Bolt", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Product nut = store.createProduct(new Product(0, "P-2", "Nut", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Product screw = store.createProduct(new Product(0, "P-3", "Screw", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Product washer = store.createProduct(new Product(0, "P-4", "Washer", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Warehouse first = store.createWarehouse(new Warehouse(0, "WH-1", "First", "Oslo", Region.NORTH));
    private final Warehouse second = store.createWarehouse(new Warehouse(0, "WH-2", "Second", "Rome", Region.SOUTH));
    private final Supplier cheap = store.createSupplier(new Supplier(0, "Cheap", "c@example.com", "Sweden", 2));
    private final Supplier fast = store.createSupplier(new Supplier(0, "Fast", "f@example.com", "Norway", 4));

    private void fill() {
        stock.setQuantity(first.id(), bolt.id(), 4);
        stock.setQuantity(first.id(), nut.id(), 11);
        stock.setQuantity(first.id(), washer.id(), 0);
        stock.setQuantity(second.id(), bolt.id(), 1);
        reorder.setReorderLevel(first.id(), bolt.id(), 10);
        reorder.setReorderLevel(first.id(), nut.id(), 10);
        reorder.setReorderLevel(first.id(), screw.id(), 5);
        reorder.setReorderLevel(first.id(), washer.id(), 0);
        reorder.setReorderLevel(second.id(), bolt.id(), 2);
        store.createSupplierProduct(new SupplierProduct(0, cheap.id(), bolt.id(), 20, new BigDecimal("0.50")));
        store.createSupplierProduct(new SupplierProduct(0, fast.id(), bolt.id(), 3, new BigDecimal("0.80")));
        store.createSupplierProduct(new SupplierProduct(0, cheap.id(), screw.id(), 9, new BigDecimal("0.30")));
        store.createSupplierProduct(new SupplierProduct(0, fast.id(), screw.id(), 2, new BigDecimal("0.30")));
    }

    @Test
    void suggestsQuantityToTwiceTheLevelWithCheapestSupplier() {
        fill();
        assertEquals(List.of(
                new PurchaseSuggestion(first, bolt, 4, 10, 16, cheap, new BigDecimal("0.50")),
                new PurchaseSuggestion(first, screw, 0, 5, 10, fast, new BigDecimal("0.30"))),
                reorder.getPurchaseSuggestions(first.id()));
    }

    @Test
    void otherWarehouseUsesItsOwnStockAndLevel() {
        fill();
        assertEquals(List.of(new PurchaseSuggestion(second, bolt, 1, 2, 3, cheap, new BigDecimal("0.50"))),
                reorder.getPurchaseSuggestions(second.id()));
    }

    @Test
    void productWithoutSupplierHasNone() {
        stock.setQuantity(first.id(), nut.id(), 1);
        reorder.setReorderLevel(first.id(), nut.id(), 3);
        List<PurchaseSuggestion> result = reorder.getPurchaseSuggestions(first.id());
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).suggestedQuantity());
        assertNull(result.get(0).supplier());
        assertNull(result.get(0).unitCost());
    }

    @Test
    void nothingSuggestedWithoutLowStock() {
        fill();
        stock.setQuantity(first.id(), bolt.id(), 50);
        stock.setQuantity(first.id(), screw.id(), 50);
        assertEquals(List.of(), reorder.getPurchaseSuggestions(first.id()));
    }
}
