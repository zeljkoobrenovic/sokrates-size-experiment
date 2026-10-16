package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseStockValue;
import com.acme.backoffice.service.StockService;
import com.acme.backoffice.service.StockValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockValueTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final StockService stock = new StockService(store);
    private final StockValueService values = new StockValueService(store);
    private Warehouse north;
    private Warehouse east;
    private Product widget;
    private Product gadget;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));
        north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
        store.createWarehouse(new Warehouse(0, "WH-Z", "Empty", "Berlin", Region.EAST));
        supplier = store.createSupplier(new Supplier(0, "Nordic Supply", "n@example.com", "Norway", 4));
    }

    private void purchase(String number, LocalDate date, PurchaseOrderStatus status, Product product, String cost) {
        store.createPurchaseOrder(new PurchaseOrder(0, number, supplier.id(), date, date.plusDays(10), status,
                List.of(new PurchaseOrderLine(product.id(), 10, new BigDecimal(cost)))));
    }

    @Test
    void valueUsesLastPurchaseCostPerWarehouseAndInTotal() {
        purchase("PO-1", LocalDate.of(2024, 1, 1), PurchaseOrderStatus.RECEIVED, widget, "2.00");
        purchase("PO-2", LocalDate.of(2024, 3, 1), PurchaseOrderStatus.SENT, widget, "2.50");
        purchase("PO-3", LocalDate.of(2024, 5, 1), PurchaseOrderStatus.DRAFT, widget, "9.00");
        purchase("PO-4", LocalDate.of(2024, 6, 1), PurchaseOrderStatus.CANCELLED, widget, "8.00");
        purchase("PO-5", LocalDate.of(2024, 2, 1), PurchaseOrderStatus.RECEIVED, gadget, "18.00");
        stock.setQuantity(north.id(), widget.id(), 10);
        stock.setQuantity(north.id(), gadget.id(), 2);
        stock.setQuantity(east.id(), widget.id(), 4);

        List<WarehouseStockValue> perWarehouse = values.getValuePerWarehouse();
        assertEquals(List.of("WH-E", "WH-N", "WH-Z"), perWarehouse.stream().map(v -> v.warehouse().code()).toList());
        assertEquals(List.of(new BigDecimal("10.00"), new BigDecimal("61.00"), new BigDecimal("0.00")),
                perWarehouse.stream().map(WarehouseStockValue::value).toList());
        assertEquals(new BigDecimal("71.00"), values.getTotalValue());
    }

    @Test
    void productWithoutPurchaseCountsAsZero() {
        assertEquals(new BigDecimal("0.00"), values.getTotalValue());
        stock.setQuantity(north.id(), widget.id(), 10);
        assertEquals(new BigDecimal("0.00"), values.getTotalValue());
    }
}
