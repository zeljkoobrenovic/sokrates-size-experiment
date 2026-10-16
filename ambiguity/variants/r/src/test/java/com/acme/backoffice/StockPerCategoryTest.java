package com.acme.backoffice;

import com.acme.backoffice.model.CategoryStock;
import com.acme.backoffice.model.CategoryWarehouseStock;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockPerCategoryTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Warehouse a = store.warehouses().create(new Warehouse(0, "A", "Alpha", "Oslo", Region.NORTH));
    private final Warehouse b = store.warehouses().create(new Warehouse(0, "B", "Beta", "Warsaw", Region.EAST));
    private final Product nail = store.products().create(new Product(0, "N", "Nail", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product screw = store.products().create(new Product(0, "S", "Screw", "Fasteners", new BigDecimal("2.00"), "piece", true));
    private final Product paint = store.products().create(new Product(0, "P", "Paint", "Paints", new BigDecimal("10.00"), "piece", true));
    private final Product tool = store.products().create(new Product(0, "T", "Tool", "Tools", new BigDecimal("20.00"), "piece", true));

    private void setUp() {
        Supplier supplier = store.suppliers().create(new Supplier(0, "Sup", "s@example.com", "Sweden", 3));
        store.purchaseOrders().create(new PurchaseOrder(0, "PO-1", supplier.id(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5),
                PurchaseOrderStatus.RECEIVED, List.of(new PurchaseOrderLine(nail.id(), 10, new BigDecimal("0.50")),
                new PurchaseOrderLine(screw.id(), 10, new BigDecimal("1.50")), new PurchaseOrderLine(paint.id(), 10, new BigDecimal("6.00")))));
        store.stock().setQuantity(a.id(), nail.id(), 40);
        store.stock().setQuantity(a.id(), screw.id(), 10);
        store.stock().setQuantity(a.id(), paint.id(), 5);
        store.stock().setQuantity(b.id(), nail.id(), 20);
        store.stock().setQuantity(b.id(), tool.id(), 0);
    }

    @Test
    void sumsQuantityAndValuePerCategoryAndWarehouse() {
        setUp();

        assertEquals(List.of(
                new CategoryWarehouseStock("Fasteners", a, 50, new BigDecimal("35.00")),
                new CategoryWarehouseStock("Fasteners", b, 20, new BigDecimal("10.00")),
                new CategoryWarehouseStock("Paints", a, 5, new BigDecimal("30.00"))), store.stock().quantityAndValuePerCategoryAndWarehouse());
    }

    @Test
    void sumsQuantityAndValuePerCategoryOverWarehouses() {
        setUp();

        assertEquals(List.of(
                new CategoryStock("Fasteners", 70, new BigDecimal("45.00")),
                new CategoryStock("Paints", 5, new BigDecimal("30.00"))), store.stock().quantityAndValuePerCategory());
    }

    @Test
    void listsCategoriesWithoutStockPerWarehouse() {
        setUp();

        assertEquals(List.of(
                new WarehouseCategory(a, "Tools"),
                new WarehouseCategory(b, "Paints"),
                new WarehouseCategory(b, "Tools")), store.stock().categoriesWithoutStockPerWarehouse());
    }
}
