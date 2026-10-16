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

class EmptyStockLinesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final Product bolt = store.products().create(new Product(0, "P-1", "Bolt", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Product nut = store.products().create(new Product(0, "P-2", "Nut", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Product screw = store.products().create(new Product(0, "P-3", "Screw", "Cat", new BigDecimal("1.00"), "piece", true));
    private final Warehouse first = store.warehouses().create(new Warehouse(0, "WH-1", "First", "Oslo", Region.NORTH));

    @Test
    void warehouseStockLeavesOutProductsThatAreNoLongerHeld() {
        stock.setQuantity(first.id(), bolt.id(), 10);
        stock.setQuantity(first.id(), nut.id(), 4);
        stock.setQuantity(first.id(), screw.id(), 0);
        stock.takeQuantity(first.id(), nut.id(), 4);

        assertEquals(List.of(new StockLevel(bolt, 10)), stock.getWarehouseStock(first.id()));
        assertEquals(0, stock.getQuantity(first.id(), nut.id()));
    }
}
