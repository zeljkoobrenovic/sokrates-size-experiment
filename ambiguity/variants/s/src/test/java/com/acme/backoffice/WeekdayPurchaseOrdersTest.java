package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WeekdayPurchaseOrders;
import com.acme.backoffice.service.WeekdayPurchaseOrdersService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeekdayPurchaseOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final WeekdayPurchaseOrdersService service = new WeekdayPurchaseOrdersService(store);
    private final Supplier nordic = store.createSupplier(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse south = store.createWarehouse(new Warehouse(0, "WH-S", "South", "Rome", Region.SOUTH));
    private final Warehouse east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));
    private int counter;

    private void order(Warehouse warehouse, LocalDate date, PurchaseOrderStatus status) {
        counter++;
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-" + counter, nordic.id(), date, date.plusDays(14), status,
                List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.10"))), warehouse == null ? 0 : warehouse.id()));
    }

    @Test
    void countsPurchaseOrdersPerWeekdayOverallAndPerRegion() {
        order(north, LocalDate.of(2024, 3, 4), PurchaseOrderStatus.SENT);
        order(south, LocalDate.of(2024, 3, 11), PurchaseOrderStatus.DRAFT);
        order(east, LocalDate.of(2024, 3, 11), PurchaseOrderStatus.RECEIVED);
        order(null, LocalDate.of(2024, 3, 4), PurchaseOrderStatus.SENT);
        order(north, LocalDate.of(2024, 3, 10), PurchaseOrderStatus.SENT);
        order(north, LocalDate.of(2024, 3, 5), PurchaseOrderStatus.CANCELLED);
        order(north, LocalDate.of(2024, 2, 26), PurchaseOrderStatus.SENT);
        order(north, LocalDate.of(2024, 3, 18), PurchaseOrderStatus.SENT);

        List<WeekdayPurchaseOrders> days = service.report(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 17));

        assertEquals(7, days.size());
        assertEquals(DayOfWeek.MONDAY, days.get(0).weekday());
        assertEquals(4, days.get(0).total());
        assertEquals(1, days.get(0).perRegion().get(Region.NORTH));
        assertEquals(1, days.get(0).perRegion().get(Region.SOUTH));
        assertEquals(1, days.get(0).perRegion().get(Region.EAST));
        assertEquals(0, days.get(0).perRegion().get(Region.WEST));
        assertEquals(0, days.get(1).total());
        assertEquals(DayOfWeek.SUNDAY, days.get(6).weekday());
        assertEquals(1, days.get(6).total());
        assertEquals(1, days.get(6).perRegion().get(Region.NORTH));
    }

    @Test
    void rangeWithoutPurchaseOrdersHasSevenEmptyDays() {
        List<WeekdayPurchaseOrders> days = service.report(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31));

        assertEquals(7, days.size());
        for (WeekdayPurchaseOrders day : days) {
            assertEquals(0, day.total());
            assertEquals(4, day.perRegion().size());
        }
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.report(LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.report(null, LocalDate.of(2024, 3, 1)));
    }
}
