package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.StockMovement;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseMovements;
import com.acme.backoffice.service.StockService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockMovementsByCategoryTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final Warehouse a = store.createWarehouse(new Warehouse(0, "A", "Alpha", "Oslo", Region.NORTH));
    private final Warehouse b = store.createWarehouse(new Warehouse(0, "B", "Beta", "Warsaw", Region.EAST));
    private final Product nail = store.createProduct(new Product(0, "N", "Nail", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product screw = store.createProduct(new Product(0, "S", "Screw", "Fasteners", new BigDecimal("2.00"), "piece", true));
    private final Product paint = store.createProduct(new Product(0, "P", "Paint", "Paints", new BigDecimal("10.00"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 5, 10);

    @Test
    void listsMovementsOfCategoryWithinPeriodPerWarehouse() {
        store.createStockMovement(nail.id(), b.id(), day, -5, StockMovementReason.SHIPMENT);
        store.createStockMovement(screw.id(), a.id(), day.plusDays(1), -10, StockMovementReason.SHIPMENT);
        store.createStockMovement(nail.id(), a.id(), day, 100, StockMovementReason.RECEIPT);
        store.createStockMovement(paint.id(), a.id(), day, 20, StockMovementReason.RECEIPT);
        store.createStockMovement(nail.id(), a.id(), day.minusDays(1), 99, StockMovementReason.RECEIPT);
        store.createStockMovement(nail.id(), a.id(), day.plusDays(3), 99, StockMovementReason.RECEIPT);

        List<WarehouseMovements> result = stock.getMovementsOfCategory("Fasteners", day, day.plusDays(2));

        assertEquals(2, result.size());
        assertEquals(a, result.get(0).warehouse());
        List<StockMovement> inA = result.get(0).movements();
        assertEquals(2, inA.size());
        assertEquals(nail.id(), inA.get(0).productId());
        assertEquals(day, inA.get(0).movementDate());
        assertEquals(100, inA.get(0).quantityChange());
        assertEquals(screw.id(), inA.get(1).productId());
        assertEquals(-10, inA.get(1).quantityChange());
        assertEquals(b, result.get(1).warehouse());
        assertEquals(1, result.get(1).movements().size());
        assertEquals(-5, result.get(1).movements().get(0).quantityChange());
    }

    @Test
    void emptyResultAndInvalidArgumentsFail() {
        assertEquals(List.of(), stock.getMovementsOfCategory("Fasteners", day, day));
        assertThrows(IllegalArgumentException.class, () -> stock.getMovementsOfCategory("Fasteners", day, day.minusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> stock.getMovementsOfCategory("Fasteners", null, day));
        assertThrows(IllegalArgumentException.class, () -> stock.getMovementsOfCategory(" ", day, day));
    }
}
