package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WarehousePurchaseOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Supplier nordic = store.createSupplier(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));

    private void order(String number, long warehouseId, LocalDate expected, PurchaseOrderStatus status) {
        store.createPurchaseOrder(new PurchaseOrder(0, number, nordic.id(), LocalDate.of(2024, 7, 1), expected, status,
                List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.10"))), warehouseId));
    }

    private List<String> numbers(List<PurchaseOrder> orders) {
        return orders.stream().map(PurchaseOrder::number).toList();
    }

    private void createOrders() {
        order("PO-1", north.id(), LocalDate.of(2024, 7, 20), PurchaseOrderStatus.SENT);
        order("PO-2", east.id(), LocalDate.of(2024, 7, 10), PurchaseOrderStatus.SENT);
        order("PO-3", north.id(), LocalDate.of(2024, 7, 5), PurchaseOrderStatus.SENT);
        order("PO-4", north.id(), LocalDate.of(2024, 7, 6), PurchaseOrderStatus.DRAFT);
        order("PO-5", north.id(), LocalDate.of(2024, 7, 7), PurchaseOrderStatus.RECEIVED);
        order("PO-6", 0, LocalDate.of(2024, 7, 8), PurchaseOrderStatus.SENT);
    }

    @Test
    void warehouseIsStoredWithThePurchaseOrder() {
        createOrders();
        assertEquals(north.id(), store.findPurchaseOrderByNumber("PO-1").orElseThrow().warehouseId());
        assertEquals(0, store.findPurchaseOrderByNumber("PO-6").orElseThrow().warehouseId());
    }

    @Test
    void listsOpenOrdersOfOneWarehouseByExpectedDate() {
        createOrders();
        List<PurchaseOrder> open = store.findOpenPurchaseOrdersIntoWarehouse(north.id());
        assertEquals(List.of("PO-3", "PO-1"), numbers(open));
        assertEquals(1, open.get(0).lines().size());
        assertEquals(List.of("PO-2"), numbers(store.findOpenPurchaseOrdersIntoWarehouse(east.id())));
        assertTrue(store.findOpenPurchaseOrdersIntoWarehouse(999).isEmpty());
    }

    @Test
    void overdueOrdersOfOneWarehouseAreBeforeTheGivenDate() {
        createOrders();
        assertTrue(store.findOverduePurchaseOrdersIntoWarehouse(north.id(), LocalDate.of(2024, 7, 5)).isEmpty());
        assertEquals(List.of("PO-3"), numbers(store.findOverduePurchaseOrdersIntoWarehouse(north.id(), LocalDate.of(2024, 7, 6))));
        assertEquals(List.of("PO-3", "PO-1"), numbers(store.findOverduePurchaseOrdersIntoWarehouse(north.id(), LocalDate.of(2024, 8, 1))));
        assertEquals(List.of("PO-2"), numbers(store.findOverduePurchaseOrdersIntoWarehouse(east.id(), LocalDate.of(2024, 8, 1))));
    }
}
