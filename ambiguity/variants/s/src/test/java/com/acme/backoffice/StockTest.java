package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.StockService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final Product product = store.createProduct(new Product(0, "P-1", "Thing", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Warehouse first = store.createWarehouse(new Warehouse(0, "WH-1", "First", "Oslo", Region.NORTH));
    private final Warehouse second = store.createWarehouse(new Warehouse(0, "WH-2", "Second", "Rome", Region.SOUTH));

    @Test
    void unknownStockIsZero() {
        assertEquals(0, stock.getQuantity(first.id(), product.id()));
    }

    @Test
    void setReplacesQuantity() {
        stock.setQuantity(first.id(), product.id(), 10);
        stock.setQuantity(first.id(), product.id(), 4);
        assertEquals(4, stock.getQuantity(first.id(), product.id()));
    }

    @Test
    void addCreatesAndIncreases() {
        stock.addQuantity(first.id(), product.id(), 5);
        stock.addQuantity(first.id(), product.id(), 7);
        assertEquals(12, stock.getQuantity(first.id(), product.id()));
        assertEquals(0, stock.getQuantity(second.id(), product.id()));
    }

    @Test
    void takeDecreasesAndAllowsZero() {
        stock.setQuantity(first.id(), product.id(), 10);
        stock.takeQuantity(first.id(), product.id(), 6);
        assertEquals(4, stock.getQuantity(first.id(), product.id()));
        stock.takeQuantity(first.id(), product.id(), 4);
        assertEquals(0, stock.getQuantity(first.id(), product.id()));
    }

    @Test
    void takingMoreThanAvailableFailsAndChangesNothing() {
        stock.setQuantity(first.id(), product.id(), 3);
        assertThrows(IllegalStateException.class, () -> stock.takeQuantity(first.id(), product.id(), 4));
        assertEquals(3, stock.getQuantity(first.id(), product.id()));
        assertThrows(IllegalStateException.class, () -> stock.takeQuantity(second.id(), product.id(), 1));
    }

    @Test
    void negativeValuesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> stock.setQuantity(first.id(), product.id(), -1));
        assertThrows(IllegalArgumentException.class, () -> stock.addQuantity(first.id(), product.id(), -1));
        assertThrows(IllegalArgumentException.class, () -> stock.takeQuantity(first.id(), product.id(), -1));
    }
}
