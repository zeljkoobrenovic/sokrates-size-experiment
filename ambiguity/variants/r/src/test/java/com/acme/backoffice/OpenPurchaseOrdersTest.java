package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Supplier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenPurchaseOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Supplier nordic = store.suppliers().create(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
    private final Supplier baltic = store.suppliers().create(new Supplier(0, "Baltic Trade", "sales@baltic.example", "Latvia", 5));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));

    private void order(String number, Supplier supplier, LocalDate expected, PurchaseOrderStatus status) {
        store.purchaseOrders().create(new PurchaseOrder(0, number, supplier.id(), LocalDate.of(2024, 7, 1), expected, status,
                List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.10")))));
    }

    private List<String> numbers(List<PurchaseOrder> orders) {
        return orders.stream().map(PurchaseOrder::number).toList();
    }

    private void createOrders() {
        order("PO-1", nordic, LocalDate.of(2024, 7, 20), PurchaseOrderStatus.SENT);
        order("PO-2", baltic, LocalDate.of(2024, 7, 10), PurchaseOrderStatus.SENT);
        order("PO-3", nordic, LocalDate.of(2024, 7, 5), PurchaseOrderStatus.DRAFT);
        order("PO-4", nordic, LocalDate.of(2024, 7, 6), PurchaseOrderStatus.RECEIVED);
        order("PO-5", baltic, LocalDate.of(2024, 7, 7), PurchaseOrderStatus.CANCELLED);
    }

    @Test
    void listsOnlySentOrdersByExpectedDate() {
        createOrders();
        List<PurchaseOrder> open = store.purchaseOrders().listOpen();
        assertEquals(List.of("PO-2", "PO-1"), numbers(open));
        assertEquals(1, open.get(0).lines().size());
    }

    @Test
    void listsOpenOrdersOfOneSupplier() {
        createOrders();
        assertEquals(List.of("PO-1"), numbers(store.purchaseOrders().listOpenOfSupplier(nordic.id())));
        assertEquals(List.of("PO-2"), numbers(store.purchaseOrders().listOpenOfSupplier(baltic.id())));
        assertTrue(store.purchaseOrders().listOpenOfSupplier(999).isEmpty());
    }

    @Test
    void overdueMeansExpectedDateBeforeTheGivenDate() {
        createOrders();
        assertTrue(store.purchaseOrders().listOverdue(LocalDate.of(2024, 7, 10)).isEmpty());
        assertEquals(List.of("PO-2"), numbers(store.purchaseOrders().listOverdue(LocalDate.of(2024, 7, 11))));
        assertEquals(List.of("PO-2", "PO-1"), numbers(store.purchaseOrders().listOverdue(LocalDate.of(2024, 8, 1))));
    }

    @Test
    void receivedOrdersLeaveTheOpenList() {
        createOrders();
        long id = store.purchaseOrders().findByNumber("PO-1").orElseThrow().id();
        store.purchaseOrders().receive(id, store.warehouses().create(new com.acme.backoffice.model.Warehouse(0, "WH-N", "North", "Oslo",
                com.acme.backoffice.model.Region.NORTH)).id(), LocalDate.of(2024, 7, 12));
        assertEquals(List.of("PO-2"), numbers(store.purchaseOrders().listOpen()));
    }
}
