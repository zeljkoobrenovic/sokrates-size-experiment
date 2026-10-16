package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.LatestPurchaseOrdersService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LatestPurchaseOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final LatestPurchaseOrdersService service = new LatestPurchaseOrdersService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Supplier nordic = store.createSupplier(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
    private final Supplier acme = store.createSupplier(new Supplier(0, "Acme Supply", "orders@acme.example", "UK", 5));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse south = store.createWarehouse(new Warehouse(0, "WH-S", "South", "Rome", Region.SOUTH));

    private void order(String number, Supplier supplier, Warehouse warehouse, LocalDate date, PurchaseOrderStatus status) {
        store.createPurchaseOrder(new PurchaseOrder(0, number, supplier.id(), date, date.plusDays(14), status,
                List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.10"))), warehouse == null ? 0 : warehouse.id()));
    }

    private List<String> numbers(List<PurchaseOrder> orders) {
        return orders.stream().map(PurchaseOrder::number).toList();
    }

    private void fill() {
        order("A", nordic, north, LocalDate.of(2024, 1, 1), PurchaseOrderStatus.SENT);
        order("B", acme, south, LocalDate.of(2024, 1, 5), PurchaseOrderStatus.DRAFT);
        order("C", nordic, north, LocalDate.of(2024, 1, 3), PurchaseOrderStatus.RECEIVED);
        order("D", acme, south, LocalDate.of(2024, 1, 9), PurchaseOrderStatus.SENT);
        order("E", acme, null, LocalDate.of(2024, 1, 5), PurchaseOrderStatus.SENT);
        order("F", nordic, north, LocalDate.of(2024, 1, 20), PurchaseOrderStatus.CANCELLED);
    }

    @Test
    void listsLatestPurchaseOrdersOverall() {
        fill();
        assertEquals(List.of("D", "E", "B"), numbers(service.overall(3)));
        assertEquals(5, service.overall(10).size());
        assertEquals(1, service.overall(1).get(0).lines().size());
    }

    @Test
    void listsLatestPurchaseOrdersPerRegion() {
        fill();
        assertEquals(List.of("C", "A"), numbers(service.forRegion(Region.NORTH, 5)));
        assertEquals(List.of("D"), numbers(service.forRegion(Region.SOUTH, 1)));
        assertEquals(List.of(), numbers(service.forRegion(Region.EAST, 5)));
    }

    @Test
    void listsLatestPurchaseOrdersPerSupplier() {
        fill();
        assertEquals(List.of("D", "E"), numbers(service.forSupplier(acme.id(), 2)));
        assertEquals(List.of("C"), numbers(service.forSupplier(nordic.id(), 1)));
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.overall(0));
        assertThrows(IllegalArgumentException.class, () -> service.forRegion(null, 1));
        assertThrows(IllegalArgumentException.class, () -> service.forSupplier(9999, 1));
    }
}
