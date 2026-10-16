package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.QuarterPurchaseOrders;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.QuarterPurchaseOrdersService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuarterPurchaseOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final QuarterPurchaseOrdersService service = new QuarterPurchaseOrdersService(store);
    private final Supplier nordic = store.suppliers().create(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse north = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse south = store.warehouses().create(new Warehouse(0, "WH-S", "South", "Rome", Region.SOUTH));
    private final Warehouse east = store.warehouses().create(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
    private final Warehouse west = store.warehouses().create(new Warehouse(0, "WH-W", "West", "Dublin", Region.WEST));
    private int counter;

    private void order(Warehouse warehouse, LocalDate date, PurchaseOrderStatus status) {
        counter++;
        store.purchaseOrders().create(new PurchaseOrder(0, "PO-" + counter, nordic.id(), date, date.plusDays(14), status,
                List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.10"))), warehouse == null ? 0 : warehouse.id()));
    }

    @Test
    void countsPurchaseOrdersPerQuarterOverallAndPerRegion() {
        order(north, LocalDate.of(2024, 1, 1), PurchaseOrderStatus.SENT);
        order(north, LocalDate.of(2024, 3, 31), PurchaseOrderStatus.RECEIVED);
        order(south, LocalDate.of(2024, 3, 15), PurchaseOrderStatus.DRAFT);
        order(east, LocalDate.of(2024, 4, 1), PurchaseOrderStatus.SENT);
        order(west, LocalDate.of(2024, 12, 31), PurchaseOrderStatus.SENT);
        order(null, LocalDate.of(2024, 12, 1), PurchaseOrderStatus.SENT);
        order(north, LocalDate.of(2024, 2, 1), PurchaseOrderStatus.CANCELLED);
        order(north, LocalDate.of(2023, 12, 31), PurchaseOrderStatus.SENT);
        order(north, LocalDate.of(2025, 1, 1), PurchaseOrderStatus.SENT);

        List<QuarterPurchaseOrders> quarters = service.report(2024);

        assertEquals(4, quarters.size());
        assertEquals(1, quarters.get(0).quarter());
        assertEquals(3, quarters.get(0).total());
        assertEquals(2, quarters.get(0).perRegion().get(Region.NORTH));
        assertEquals(1, quarters.get(0).perRegion().get(Region.SOUTH));
        assertEquals(0, quarters.get(0).perRegion().get(Region.EAST));
        assertEquals(0, quarters.get(0).perRegion().get(Region.WEST));
        assertEquals(1, quarters.get(1).total());
        assertEquals(1, quarters.get(1).perRegion().get(Region.EAST));
        assertEquals(0, quarters.get(2).total());
        assertEquals(4, quarters.get(3).quarter());
        assertEquals(2, quarters.get(3).total());
        assertEquals(1, quarters.get(3).perRegion().get(Region.WEST));
        assertEquals(0, quarters.get(3).perRegion().get(Region.NORTH));
    }

    @Test
    void yearWithoutPurchaseOrdersHasFourEmptyQuarters() {
        List<QuarterPurchaseOrders> quarters = service.report(2030);

        assertEquals(4, quarters.size());
        for (QuarterPurchaseOrders quarter : quarters) {
            assertEquals(0, quarter.total());
            assertEquals(4, quarter.perRegion().size());
        }
    }

    @Test
    void rejectsInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> service.report(0));
    }
}
