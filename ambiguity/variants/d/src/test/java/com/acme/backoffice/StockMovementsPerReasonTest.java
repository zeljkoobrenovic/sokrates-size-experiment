package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ReasonMovements;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.StockService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockMovementsPerReasonTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final Warehouse a = store.createWarehouse(new Warehouse(0, "A", "Alpha", "Oslo", Region.NORTH));
    private final Warehouse b = store.createWarehouse(new Warehouse(0, "B", "Beta", "Warsaw", Region.EAST));
    private final Product nail = store.createProduct(new Product(0, "N", "Nail", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product screw = store.createProduct(new Product(0, "S", "Screw", "Fasteners", new BigDecimal("2.00"), "piece", true));
    private final Product paint = store.createProduct(new Product(0, "P", "Paint", "Paints", new BigDecimal("10.00"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 5, 10);

    @Test
    void groupsMovementsByReasonWarehouseAndCategoryWithinPeriod() {
        store.createStockMovement(nail.id(), a.id(), day, 100, StockMovementReason.RECEIPT);
        store.createStockMovement(paint.id(), a.id(), day, 20, StockMovementReason.RECEIPT);
        store.createStockMovement(nail.id(), a.id(), day, -30, StockMovementReason.SHIPMENT);
        store.createStockMovement(screw.id(), a.id(), day.plusDays(1), -10, StockMovementReason.SHIPMENT);
        store.createStockMovement(nail.id(), b.id(), day.plusDays(2), -5, StockMovementReason.SHIPMENT);
        store.createStockMovement(nail.id(), b.id(), day.minusDays(1), -99, StockMovementReason.SHIPMENT);
        store.createStockMovement(nail.id(), b.id(), day.plusDays(3), -99, StockMovementReason.SHIPMENT);

        List<ReasonMovements> result = stock.getMovementsPerReason(day, day.plusDays(2));

        assertEquals(List.of(
                new ReasonMovements(StockMovementReason.RECEIPT, a, "Fasteners", 1, 100),
                new ReasonMovements(StockMovementReason.RECEIPT, a, "Paints", 1, 20),
                new ReasonMovements(StockMovementReason.SHIPMENT, a, "Fasteners", 2, -40),
                new ReasonMovements(StockMovementReason.SHIPMENT, b, "Fasteners", 1, -5)), result);
    }

    @Test
    void emptyPeriodGivesNothingAndInvalidPeriodFails() {
        assertEquals(List.of(), stock.getMovementsPerReason(day, day));
        assertThrows(IllegalArgumentException.class, () -> stock.getMovementsPerReason(day, day.minusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> stock.getMovementsPerReason(null, day));
    }
}
