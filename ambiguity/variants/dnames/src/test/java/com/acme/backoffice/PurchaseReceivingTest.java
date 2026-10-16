package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.PurchaseReceivingService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseReceivingTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PurchaseReceivingService receiving = new PurchaseReceivingService(store);
    private final LocalDate day = LocalDate.of(2024, 7, 10);
    private final Supplier nordic = store.createSupplier(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));

    private void order(String number, PurchaseOrderStatus status) {
        store.createPurchaseOrder(new PurchaseOrder(0, number, nordic.id(), day, day.plusDays(14), status,
                List.of(new PurchaseOrderLine(widget.id(), 200, new BigDecimal("2.10")),
                        new PurchaseOrderLine(gadget.id(), 50, new BigDecimal("18.00")))));
    }

    @Test
    void receivingAddsStockAndMarksOrderReceived() {
        order("PO-1", PurchaseOrderStatus.SENT);
        store.setStockQuantity(north.id(), widget.id(), 5);
        receiving.receive("PO-1", "WH-N", LocalDate.of(2024, 7, 12));
        assertEquals(205, store.findStockQuantityInWarehouse(north.id(), widget.id()));
        assertEquals(50, store.findStockQuantityInWarehouse(north.id(), gadget.id()));
        assertEquals(0, store.findStockQuantityInWarehouse(east.id(), widget.id()));
        assertEquals(PurchaseOrderStatus.RECEIVED, store.findPurchaseOrderByNumber("PO-1").orElseThrow().status());
        assertEquals(LocalDate.of(2024, 7, 12), receiving.findReceivedDate("PO-1").orElseThrow());
    }

    @Test
    void ordersThatAreNotSentCannotBeReceived() {
        order("PO-D", PurchaseOrderStatus.DRAFT);
        order("PO-R", PurchaseOrderStatus.RECEIVED);
        order("PO-C", PurchaseOrderStatus.CANCELLED);
        for (String number : List.of("PO-D", "PO-R", "PO-C")) {
            assertThrows(IllegalStateException.class, () -> receiving.receive(number, "WH-N", day));
            assertTrue(receiving.findReceivedDate(number).isEmpty());
        }
        assertEquals(0, store.findStockQuantityInAllWarehouses(widget.id()));
    }

    @Test
    void receivingTwiceFailsAndDoesNotAddStockAgain() {
        order("PO-1", PurchaseOrderStatus.SENT);
        receiving.receive("PO-1", "WH-N", day);
        assertThrows(IllegalStateException.class, () -> receiving.receive("PO-1", "WH-E", day.plusDays(1)));
        assertEquals(200, store.findStockQuantityInAllWarehouses(widget.id()));
        assertEquals(day, receiving.findReceivedDate("PO-1").orElseThrow());
    }

    @Test
    void unknownOrderWarehouseOrMissingDateIsRejected() {
        order("PO-1", PurchaseOrderStatus.SENT);
        assertThrows(IllegalArgumentException.class, () -> receiving.receive("PO-404", "WH-N", day));
        assertThrows(IllegalArgumentException.class, () -> receiving.receive("PO-1", "WH-404", day));
        assertThrows(IllegalArgumentException.class, () -> receiving.receive("PO-1", "WH-N", null));
        assertEquals(PurchaseOrderStatus.SENT, store.findPurchaseOrderByNumber("PO-1").orElseThrow().status());
    }
}
