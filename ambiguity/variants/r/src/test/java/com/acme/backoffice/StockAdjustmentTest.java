package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.StockAdjustment;
import com.acme.backoffice.model.StockMovement;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.StockAdjustmentService;
import com.acme.backoffice.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockAdjustmentTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final StockAdjustmentService adjustments = new StockAdjustmentService(store);
    private final LocalDate day = LocalDate.of(2024, 6, 10);
    private Warehouse north;
    private Warehouse east;
    private Product widget;

    @BeforeEach
    void setUp() {
        widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        north = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        east = store.warehouses().create(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
    }

    @Test
    void countedQuantityReplacesStockAndDifferenceIsLogged() {
        stock.setQuantity(north.id(), widget.id(), 10);

        StockAdjustment down = adjustments.adjust(widget.id(), north.id(), day, 7, "Damaged");
        assertEquals(10, down.quantityBefore());
        assertEquals(-3, down.difference());
        assertEquals(7, stock.getQuantity(north.id(), widget.id()));

        adjustments.adjust(widget.id(), north.id(), day.plusDays(1), 12, "Found");
        assertEquals(12, stock.getQuantity(north.id(), widget.id()));

        List<StockMovement> movements = stock.getMovements(widget.id(), north.id(), day, day.plusDays(5));
        assertEquals(List.of(-3, 5), movements.stream().map(StockMovement::quantityChange).toList());
        assertTrue(movements.stream().allMatch(m -> m.reason() == StockMovementReason.ADJUSTMENT));
    }

    @Test
    void countWithoutExistingStockAndWithoutDifference() {
        StockAdjustment first = adjustments.adjust(widget.id(), east.id(), day, 4, "First count");
        assertEquals(0, first.quantityBefore());
        assertEquals(4, stock.getQuantity(east.id(), widget.id()));

        adjustments.adjust(widget.id(), east.id(), day.plusDays(1), 4, "Recount");
        assertEquals(1, stock.getMovements(widget.id(), east.id(), day, day.plusDays(5)).size());
        assertEquals(2, adjustments.getWarehouseAdjustments(east.id(), day, day.plusDays(5)).size());
    }

    @Test
    void adjustmentsAreListedPerWarehouseAndPeriod() {
        adjustments.adjust(widget.id(), north.id(), day, 5, "A");
        adjustments.adjust(widget.id(), east.id(), day, 6, "B");
        adjustments.adjust(widget.id(), north.id(), day.plusDays(10), 8, "C");

        List<StockAdjustment> found = adjustments.getWarehouseAdjustments(north.id(), day, day.plusDays(5));
        assertEquals(1, found.size());
        assertEquals("A", found.get(0).reason());
        assertEquals(2, adjustments.getWarehouseAdjustments(north.id(), day, day.plusDays(10)).size());
    }

    @Test
    void invalidInputIsRejectedAndChangesNothing() {
        stock.setQuantity(north.id(), widget.id(), 10);
        assertThrows(IllegalArgumentException.class, () -> adjustments.adjust(widget.id(), north.id(), day, -1, "x"));
        assertThrows(IllegalArgumentException.class, () -> adjustments.adjust(widget.id(), north.id(), null, 1, "x"));
        assertThrows(IllegalArgumentException.class, () -> adjustments.adjust(widget.id(), north.id(), day, 1, " "));
        assertThrows(IllegalStateException.class, () -> adjustments.adjust(widget.id(), 9999, day, 1, "x"));

        assertEquals(10, stock.getQuantity(north.id(), widget.id()));
        assertTrue(stock.getMovements(widget.id(), north.id(), day, day).isEmpty());
        assertTrue(adjustments.getWarehouseAdjustments(north.id(), day, day).isEmpty());
    }
}
