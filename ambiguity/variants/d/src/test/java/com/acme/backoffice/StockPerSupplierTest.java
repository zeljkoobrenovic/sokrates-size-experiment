package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupplierStock;
import com.acme.backoffice.model.SupplierWarehouseStock;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.SupplierProductService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockPerSupplierTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SupplierProductService service = new SupplierProductService(store);
    private final Warehouse a = store.createWarehouse(new Warehouse(0, "A", "Alpha", "Oslo", Region.NORTH));
    private final Warehouse b = store.createWarehouse(new Warehouse(0, "B", "Beta", "Warsaw", Region.EAST));
    private final Product nail = store.createProduct(new Product(0, "N", "Nail", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product screw = store.createProduct(new Product(0, "S", "Screw", "Fasteners", new BigDecimal("2.00"), "piece", true));
    private final Product paint = store.createProduct(new Product(0, "P", "Paint", "Paints", new BigDecimal("10.00"), "piece", true));
    private final Supplier sup = store.createSupplier(new Supplier(0, "Sup", "s@example.com", "Sweden", 3));
    private final Supplier other = store.createSupplier(new Supplier(0, "Other", "o@example.com", "Latvia", 4));

    @Test
    void sumsQuantityAndValueOfSupplierProductsPerWarehouse() {
        service.record(sup.id(), nail.id(), 5, new BigDecimal("0.50"));
        service.record(sup.id(), screw.id(), 5, new BigDecimal("1.50"));
        service.record(other.id(), paint.id(), 5, new BigDecimal("6.00"));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-1", sup.id(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5),
                PurchaseOrderStatus.RECEIVED, List.of(new PurchaseOrderLine(nail.id(), 10, new BigDecimal("0.50")),
                new PurchaseOrderLine(screw.id(), 10, new BigDecimal("1.50")), new PurchaseOrderLine(paint.id(), 10, new BigDecimal("6.00")))));
        store.setStockQuantity(a.id(), nail.id(), 40);
        store.setStockQuantity(a.id(), screw.id(), 10);
        store.setStockQuantity(a.id(), paint.id(), 5);
        store.setStockQuantity(b.id(), nail.id(), 20);
        store.setStockQuantity(b.id(), screw.id(), 0);

        SupplierStock result = service.getStockOfSupplier(sup.id());

        assertEquals("Sup", result.supplier().name());
        assertEquals(List.of(new SupplierWarehouseStock(a, 50, new BigDecimal("35.00")),
                new SupplierWarehouseStock(b, 20, new BigDecimal("10.00"))), result.warehouses());
        assertEquals(List.of(new SupplierWarehouseStock(a, 5, new BigDecimal("30.00"))),
                service.getStockOfSupplier(other.id()).warehouses());
    }

    @Test
    void supplierWithoutStockHasNoWarehousesAndUnknownSupplierFails() {
        assertEquals(List.of(), service.getStockOfSupplier(sup.id()).warehouses());
        assertThrows(IllegalArgumentException.class, () -> service.getStockOfSupplier(9999));
    }
}
