package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseAdjustments;
import com.acme.backoffice.service.StockAdjustmentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NegativeAdjustmentsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockAdjustmentService adjustments = new StockAdjustmentService(store);
    private final Warehouse a = store.createWarehouse(new Warehouse(0, "A", "Alpha", "Oslo", Region.NORTH));
    private final Warehouse b = store.createWarehouse(new Warehouse(0, "B", "Beta", "Warsaw", Region.EAST));
    private final Product nail = store.createProduct(new Product(0, "N", "Nail", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product paint = store.createProduct(new Product(0, "P", "Paint", "Paints", new BigDecimal("10.00"), "piece", true));
    private final LocalDate day = LocalDate.of(2024, 5, 10);

    @Test
    void listsLossesPerWarehouseBiggestFirst() {
        store.setStockQuantity(a.id(), nail.id(), 100);
        store.setStockQuantity(a.id(), paint.id(), 50);
        store.setStockQuantity(b.id(), nail.id(), 40);
        store.setStockQuantity(b.id(), paint.id(), 40);
        adjustments.adjust(nail.id(), b.id(), day.plusDays(1), 30, "Lost");
        adjustments.adjust(nail.id(), a.id(), day.plusDays(1), 95, "Small");
        adjustments.adjust(paint.id(), a.id(), day.plusDays(2), 30, "Big");
        adjustments.adjust(paint.id(), b.id(), day.plusDays(2), 45, "Gain");
        adjustments.adjust(nail.id(), a.id(), day.plusDays(20), 0, "Too late");

        List<WarehouseAdjustments> result = adjustments.getNegativeAdjustments(day, day.plusDays(5));

        assertEquals(2, result.size());
        assertEquals(a, result.get(0).warehouse());
        assertEquals(2, result.get(0).adjustments().size());
        assertEquals("Big", result.get(0).adjustments().get(0).reason());
        assertEquals(-20, result.get(0).adjustments().get(0).difference());
        assertEquals("Small", result.get(0).adjustments().get(1).reason());
        assertEquals(b, result.get(1).warehouse());
        assertEquals(1, result.get(1).adjustments().size());
        assertEquals("Lost", result.get(1).adjustments().get(0).reason());
    }

    @Test
    void emptyResultAndInvalidArgumentsFail() {
        assertEquals(List.of(), adjustments.getNegativeAdjustments(day, day));
        assertThrows(IllegalArgumentException.class, () -> adjustments.getNegativeAdjustments(day, day.minusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> adjustments.getNegativeAdjustments(null, day));
        assertThrows(IllegalArgumentException.class, () -> adjustments.getNegativeAdjustments(day, null));
    }
}
