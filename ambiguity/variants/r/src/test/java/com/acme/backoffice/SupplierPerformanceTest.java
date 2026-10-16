package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupplierPerformance;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.PurchaseReceivingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierPerformanceTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PurchaseReceivingService receiving = new PurchaseReceivingService(store);
    private final LocalDate day = LocalDate.of(2024, 7, 1);
    private Product widget;

    private void order(String number, Supplier supplier, int expectedInDays) {
        store.purchaseOrders().create(new PurchaseOrder(0, number, supplier.id(), day, day.plusDays(expectedInDays),
                PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.10")))));
    }

    @BeforeEach
    void setUp() {
        widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
        Supplier nordic = store.suppliers().create(new Supplier(0, "Nordic Supply", "n@example.com", "Sweden", 3));
        Supplier baltic = store.suppliers().create(new Supplier(0, "Baltic Goods", "b@example.com", "Sweden", 4));
        store.suppliers().create(new Supplier(0, "Idle", "i@example.com", "Norway", 2));
        order("PO-1", nordic, 10);
        order("PO-2", nordic, 10);
        order("PO-3", nordic, 10);
        order("PO-4", nordic, 10);
        order("PO-5", baltic, 10);
        order("PO-6", baltic, 10);
        receiving.receive("PO-1", "WH-N", day.plusDays(8));
        receiving.receive("PO-2", "WH-N", day.plusDays(10));
        receiving.receive("PO-3", "WH-N", day.plusDays(15));
        receiving.receive("PO-5", "WH-N", day.plusDays(11));
    }

    @Test
    void countsDelayAndLateShareForReceivedOrders() {
        List<SupplierPerformance> result = receiving.getSupplierPerformance(day, day.plusDays(30));
        assertEquals(2, result.size());
        SupplierPerformance baltic = result.get(0);
        assertEquals("Baltic Goods", baltic.supplier().name());
        assertEquals(1, baltic.receivedCount());
        assertEquals(new BigDecimal("1.00"), baltic.averageDelayDays());
        assertEquals(new BigDecimal("100.00"), baltic.lateShare());
        SupplierPerformance nordic = result.get(1);
        assertEquals("Nordic Supply", nordic.supplier().name());
        assertEquals(3, nordic.receivedCount());
        assertEquals(new BigDecimal("1.00"), nordic.averageDelayDays());
        assertEquals(new BigDecimal("33.33"), nordic.lateShare());
    }

    @Test
    void onlyOrdersReceivedInThePeriodCount() {
        List<SupplierPerformance> result = receiving.getSupplierPerformance(day.plusDays(10), day.plusDays(10));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).receivedCount());
        assertEquals(new BigDecimal("0.00"), result.get(0).averageDelayDays());
        assertEquals(new BigDecimal("0.00"), result.get(0).lateShare());
        assertTrue(receiving.getSupplierPerformance(day.minusDays(10), day.minusDays(1)).isEmpty());
    }

    @Test
    void earlyReceiptsGiveNegativeDelay() {
        List<SupplierPerformance> result = receiving.getSupplierPerformance(day.plusDays(8), day.plusDays(8));
        assertEquals(new BigDecimal("-2.00"), result.get(0).averageDelayDays());
    }

    @Test
    void invalidPeriod() {
        assertThrows(IllegalArgumentException.class, () -> receiving.getSupplierPerformance(null, day));
        assertThrows(IllegalArgumentException.class, () -> receiving.getSupplierPerformance(day, null));
        assertThrows(IllegalArgumentException.class, () -> receiving.getSupplierPerformance(day.plusDays(1), day));
    }
}
