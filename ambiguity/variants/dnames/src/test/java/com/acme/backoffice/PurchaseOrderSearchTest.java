package com.acme.backoffice;

import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseOrderSearchTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Supplier nordic = store.createSupplier(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
    private final Supplier baltic = store.createSupplier(new Supplier(0, "Baltic Goods", "info@baltic.example", "Sweden", 2));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse east = store.createWarehouse(new Warehouse(0, "WH-E", "East", "Warsaw", Region.EAST));

    private void order(String number, Supplier supplier, Warehouse warehouse, PurchaseOrderStatus status, LocalDate expected) {
        store.createPurchaseOrder(new PurchaseOrder(0, number, supplier.id(), LocalDate.of(2024, 1, 1), expected, status,
                List.of(), warehouse.id()));
    }

    private void setUp() {
        order("PO-1", nordic, north, PurchaseOrderStatus.SENT, LocalDate.of(2024, 2, 1));
        order("PO-2", baltic, east, PurchaseOrderStatus.DRAFT, LocalDate.of(2024, 2, 10));
        order("PO-3", nordic, east, PurchaseOrderStatus.SENT, LocalDate.of(2024, 3, 1));
        order("PO-4", nordic, north, PurchaseOrderStatus.RECEIVED, LocalDate.of(2024, 3, 5));
    }

    private List<String> numbers(List<PurchaseOrder> found) {
        return found.stream().map(PurchaseOrder::number).toList();
    }

    @Test
    void withoutCriteriaReturnsAllEarliestFirst() {
        setUp();

        assertEquals(List.of("PO-1", "PO-2", "PO-3", "PO-4"), numbers(store.searchPurchaseOrdersByOptionalCriteria(null, null, null, null, null)));
    }

    @Test
    void filtersBySingleCriterion() {
        setUp();

        assertEquals(List.of("PO-1", "PO-3", "PO-4"), numbers(store.searchPurchaseOrdersByOptionalCriteria(nordic.id(), null, null, null, null)));
        assertEquals(List.of("PO-2", "PO-3"), numbers(store.searchPurchaseOrdersByOptionalCriteria(null, east.id(), null, null, null)));
        assertEquals(List.of("PO-1", "PO-3"), numbers(store.searchPurchaseOrdersByOptionalCriteria(null, null, PurchaseOrderStatus.SENT, null, null)));
    }

    @Test
    void dateRangeIsInclusive() {
        setUp();

        assertEquals(List.of("PO-2", "PO-3"), numbers(store.searchPurchaseOrdersByOptionalCriteria(null, null, null, LocalDate.of(2024, 2, 10), LocalDate.of(2024, 3, 1))));
        assertEquals(List.of("PO-3", "PO-4"), numbers(store.searchPurchaseOrdersByOptionalCriteria(null, null, null, LocalDate.of(2024, 3, 1), null)));
        assertEquals(List.of("PO-1"), numbers(store.searchPurchaseOrdersByOptionalCriteria(null, null, null, null, LocalDate.of(2024, 2, 9))));
    }

    @Test
    void combinesCriteria() {
        setUp();

        assertEquals(List.of("PO-3"), numbers(store.searchPurchaseOrdersByOptionalCriteria(nordic.id(), east.id(), PurchaseOrderStatus.SENT,
                LocalDate.of(2024, 2, 15), LocalDate.of(2024, 3, 31))));
        assertEquals(List.of("PO-4"), numbers(store.searchPurchaseOrdersByOptionalCriteria(nordic.id(), north.id(), PurchaseOrderStatus.RECEIVED, null, null)));
        assertTrue(store.searchPurchaseOrdersByOptionalCriteria(baltic.id(), null, PurchaseOrderStatus.SENT, null, null).isEmpty());
    }
}
