package com.acme.backoffice;

import com.acme.backoffice.model.LowStockAlert;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.ReorderService;
import com.acme.backoffice.service.StockService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LowStockTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final ReorderService reorder = new ReorderService(store);
    private final Product bolt = store.products().create(new Product(0, "P-1", "Bolt", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Product nut = store.products().create(new Product(0, "P-2", "Nut", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Product screw = store.products().create(new Product(0, "P-3", "Screw", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Warehouse first = store.warehouses().create(new Warehouse(0, "WH-1", "First", "Oslo", Region.NORTH));
    private final Warehouse second = store.warehouses().create(new Warehouse(0, "WH-2", "Second", "Rome", Region.SOUTH));

    private void fill() {
        stock.setQuantity(first.id(), bolt.id(), 10);
        stock.setQuantity(first.id(), nut.id(), 11);
        stock.setQuantity(second.id(), bolt.id(), 4);
        reorder.setReorderLevel(first.id(), bolt.id(), 10);
        reorder.setReorderLevel(first.id(), nut.id(), 10);
        reorder.setReorderLevel(first.id(), screw.id(), 5);
        reorder.setReorderLevel(second.id(), bolt.id(), 5);
    }

    @Test
    void warehouseListsProductsAtOrBelowLevelIncludingUnstocked() {
        fill();
        assertEquals(List.of(new LowStockAlert(first, bolt, 10, 10), new LowStockAlert(first, screw, 0, 5)),
                reorder.getLowStock(first.id()));
        assertEquals(List.of(new LowStockAlert(second, bolt, 4, 5)), reorder.getLowStock(second.id()));
    }

    @Test
    void allWarehousesAreOrderedByWarehouseCode() {
        fill();
        assertEquals(List.of(new LowStockAlert(first, bolt, 10, 10), new LowStockAlert(first, screw, 0, 5),
                new LowStockAlert(second, bolt, 4, 5)), reorder.getLowStock());
    }

    @Test
    void settingLevelAgainReplacesIt() {
        fill();
        reorder.setReorderLevel(first.id(), nut.id(), 11);
        reorder.setReorderLevel(first.id(), bolt.id(), 9);
        assertEquals(List.of(new LowStockAlert(first, nut, 11, 11), new LowStockAlert(first, screw, 0, 5)),
                reorder.getLowStock(first.id()));
    }

    @Test
    void productsWithoutReorderLevelNeverAlert() {
        stock.setQuantity(first.id(), bolt.id(), 0);
        assertEquals(List.of(), reorder.getLowStock());
    }

    @Test
    void negativeLevelIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> reorder.setReorderLevel(first.id(), bolt.id(), -1));
    }
}
