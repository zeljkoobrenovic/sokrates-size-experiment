package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.StockLevel;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.StockService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockOverviewTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final Product bolt = store.createProduct(new Product(0, "P-1", "Bolt", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Product nut = store.createProduct(new Product(0, "P-2", "Nut", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Product screw = store.createProduct(new Product(0, "P-3", "Screw", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Warehouse first = store.createWarehouse(new Warehouse(0, "WH-1", "First", "Oslo", Region.NORTH));
    private final Warehouse second = store.createWarehouse(new Warehouse(0, "WH-2", "Second", "Rome", Region.SOUTH));

    private void fill() {
        stock.setQuantity(first.id(), bolt.id(), 10);
        stock.setQuantity(second.id(), bolt.id(), 15);
        stock.setQuantity(first.id(), nut.id(), 3);
    }

    @Test
    void totalSumsAllWarehouses() {
        fill();
        assertEquals(25, stock.getTotalQuantity(bolt.id()));
        assertEquals(3, stock.getTotalQuantity(nut.id()));
        assertEquals(0, stock.getTotalQuantity(screw.id()));
    }

    @Test
    void warehouseStockListsItsProductsByName() {
        fill();
        assertEquals(List.of(new StockLevel(bolt, 10), new StockLevel(nut, 3)), stock.getWarehouseStock(first.id()));
        assertEquals(List.of(new StockLevel(bolt, 15)), stock.getWarehouseStock(second.id()));
    }

    @Test
    void productsBelowThresholdIncludeUnstockedOnes() {
        fill();
        assertEquals(List.of(new StockLevel(nut, 3), new StockLevel(screw, 0)), stock.getProductsBelow(25));
        assertEquals(List.of(new StockLevel(bolt, 25), new StockLevel(nut, 3), new StockLevel(screw, 0)), stock.getProductsBelow(26));
        assertEquals(List.of(new StockLevel(screw, 0)), stock.getProductsBelow(1));
    }
}
