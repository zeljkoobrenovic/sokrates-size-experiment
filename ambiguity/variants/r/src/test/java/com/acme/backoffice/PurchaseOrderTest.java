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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseOrderTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Supplier nordic = store.suppliers().create(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
    private final Supplier acme = store.suppliers().create(new Supplier(0, "Acme Parts", "sales@acmeparts.example", "USA", 5));
    private final Product widget = store.products().create(new Product(0, "WID-001", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.products().create(new Product(0, "GAD-002", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));

    private PurchaseOrder order(String number, Supplier supplier, LocalDate date, PurchaseOrderStatus status) {
        return new PurchaseOrder(0, number, supplier.id(), date, date.plusDays(14), status,
                List.of(new PurchaseOrderLine(widget.id(), 200, new BigDecimal("2.10")),
                        new PurchaseOrderLine(gadget.id(), 50, new BigDecimal("18.00"))));
    }

    @Test
    void createAndFind() {
        PurchaseOrder created = store.purchaseOrders().create(order("PO-1", nordic, LocalDate.of(2024, 7, 1), PurchaseOrderStatus.SENT));
        assertTrue(created.id() > 0);
        PurchaseOrder found = store.purchaseOrders().findByNumber("PO-1").orElseThrow();
        assertEquals(created, found);
        assertEquals(LocalDate.of(2024, 7, 15), found.expectedDate());
        assertEquals(PurchaseOrderStatus.SENT, found.status());
        assertEquals(List.of(widget.id(), gadget.id()), found.lines().stream().map(PurchaseOrderLine::productId).toList());
        assertEquals(new BigDecimal("18.00"), found.lines().get(1).unitCost());
        assertTrue(store.purchaseOrders().findByNumber("PO-404").isEmpty());
    }

    @Test
    void listBySupplierNewestFirst() {
        store.purchaseOrders().create(order("PO-1", nordic, LocalDate.of(2024, 7, 1), PurchaseOrderStatus.RECEIVED));
        store.purchaseOrders().create(order("PO-2", acme, LocalDate.of(2024, 7, 2), PurchaseOrderStatus.DRAFT));
        store.purchaseOrders().create(order("PO-3", nordic, LocalDate.of(2024, 7, 8), PurchaseOrderStatus.DRAFT));
        assertEquals(List.of("PO-3", "PO-1"), store.purchaseOrders().listOfSupplier(nordic.id()).stream().map(PurchaseOrder::number).toList());
        assertEquals(2, store.purchaseOrders().listOfSupplier(nordic.id()).get(0).lines().size());
        assertTrue(store.purchaseOrders().listOfSupplier(999).isEmpty());
    }

    @Test
    void numberIsUniqueAndFailedCreateLeavesNothing() {
        store.purchaseOrders().create(order("PO-1", nordic, LocalDate.of(2024, 7, 1), PurchaseOrderStatus.DRAFT));
        assertThrows(IllegalStateException.class,
                () -> store.purchaseOrders().create(order("PO-1", acme, LocalDate.of(2024, 7, 2), PurchaseOrderStatus.DRAFT)));
        assertTrue(store.purchaseOrders().listOfSupplier(acme.id()).isEmpty());
    }

    @Test
    void invalidLineRollsBackOrder() {
        PurchaseOrder bad = new PurchaseOrder(0, "PO-BAD", nordic.id(), LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 8),
                PurchaseOrderStatus.DRAFT, List.of(new PurchaseOrderLine(widget.id(), 0, new BigDecimal("2.10"))));
        assertThrows(IllegalStateException.class, () -> store.purchaseOrders().create(bad));
        assertTrue(store.purchaseOrders().findByNumber("PO-BAD").isEmpty());
    }
}
