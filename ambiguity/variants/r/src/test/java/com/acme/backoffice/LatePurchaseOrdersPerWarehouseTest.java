package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseLatePurchaseOrders;
import com.acme.backoffice.service.PurchaseReceivingService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LatePurchaseOrdersPerWarehouseTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PurchaseReceivingService service = new PurchaseReceivingService(store);
    private final Supplier nordic = store.suppliers().create(new Supplier(0, "Nordic Supply", "n@example.com", "Sweden", 3));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("2.00"), "piece", true));

    private void receive(String number, String expected, String warehouse, String received) {
        store.purchaseOrders().create(new PurchaseOrder(0, number, nordic.id(), LocalDate.of(2024, 1, 1), LocalDate.parse(expected),
                PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(widget.id(), 5, new BigDecimal("2.00")))));
        service.receive(number, warehouse, LocalDate.parse(received));
    }

    @Test
    void listsLateOrdersReceivedIntoTheWarehouseWithDaysLate() {
        store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        store.warehouses().create(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
        receive("PO-1", "2024-03-10", "WH-N", "2024-03-15");
        receive("PO-2", "2024-03-10", "WH-N", "2024-03-10");
        receive("PO-3", "2024-03-10", "WH-N", "2024-03-05");
        receive("PO-4", "2024-03-01", "WH-N", "2024-03-02");
        receive("PO-5", "2024-02-10", "WH-N", "2024-02-28");
        receive("PO-6", "2024-03-10", "WH-N", "2024-04-01");
        receive("PO-7", "2024-03-10", "WH-E", "2024-03-20");
        store.purchaseOrders().create(new PurchaseOrder(0, "PO-8", nordic.id(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1),
                PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(widget.id(), 5, new BigDecimal("2.00")))));

        WarehouseLatePurchaseOrders result = service.listLateByWarehouse("WH-N", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals("WH-N", result.warehouse().code());
        assertEquals(List.of("PO-4:1", "PO-1:5"),
                result.late().stream().map(l -> l.order().number() + ":" + l.daysLate()).toList());
        assertEquals(LocalDate.of(2024, 3, 15), result.late().get(1).receivedDate());
        assertEquals(1, result.late().get(0).order().lines().size());
        assertEquals(List.of("PO-7:10"), service.listLateByWarehouse("WH-E", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31))
                .late().stream().map(l -> l.order().number() + ":" + l.daysLate()).toList());
    }

    @Test
    void rejectsUnknownWarehouseAndBadDates() {
        store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        assertThrows(IllegalArgumentException.class, () -> service.listLateByWarehouse("NOPE", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
        assertThrows(IllegalArgumentException.class, () -> service.listLateByWarehouse("WH-N", LocalDate.of(2024, 3, 31), LocalDate.of(2024, 3, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.listLateByWarehouse("WH-N", null, LocalDate.of(2024, 3, 1)));
    }
}
