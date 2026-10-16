package com.acme.backoffice;

import com.acme.backoffice.model.CountrySpending;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupplierSpending;
import com.acme.backoffice.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierSpendingTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Supplier nordic = store.suppliers().create(new Supplier(0, "Nordic Supply", "n@example.com", "Sweden", 3));
    private final Supplier baltic = store.suppliers().create(new Supplier(0, "Baltic Goods", "b@example.com", "Sweden", 4));
    private final Supplier acme = store.suppliers().create(new Supplier(0, "Acme Parts", "a@example.com", "USA", 5));
    private final Supplier idle = store.suppliers().create(new Supplier(0, "Idle Ltd", "i@example.com", "Norway", 2));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("9.00"), "piece", true));
    private final Warehouse warehouse = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));

    private void order(String number, Supplier supplier, int quantity, String unitCost, boolean receive, LocalDate receivedDate) {
        store.purchaseOrders().create(new PurchaseOrder(0, number, supplier.id(), LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 10),
                PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(widget.id(), quantity, new BigDecimal(unitCost)),
                new PurchaseOrderLine(gadget.id(), 1, new BigDecimal("1.00")))));
        if (receive) {
            store.purchaseOrders().receive(store.purchaseOrders().findByNumber(number).orElseThrow().id(), warehouse.id(), receivedDate);
        }
    }

    private void createOrders() {
        order("PO-1", nordic, 10, "2.00", true, LocalDate.of(2024, 7, 5));   // 21.00
        order("PO-2", nordic, 10, "3.00", true, LocalDate.of(2024, 7, 31));  // 31.00
        order("PO-3", baltic, 100, "1.00", true, LocalDate.of(2024, 7, 15)); // 101.00
        order("PO-4", acme, 10, "10.00", true, LocalDate.of(2024, 7, 20));   // 101.00
        order("PO-5", acme, 10, "10.00", true, LocalDate.of(2024, 8, 1));    // outside the period
        order("PO-6", baltic, 10, "10.00", false, null);                      // not received
    }

    @Test
    void spendingPerSupplierIsSortedByValueThenName() {
        createOrders();
        List<SupplierSpending> spending = store.suppliers().spendingPerSupplier(LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31));
        assertEquals(List.of("Acme Parts", "Baltic Goods", "Nordic Supply"), spending.stream().map(s -> s.supplier().name()).toList());
        assertEquals(List.of(1, 1, 2), spending.stream().map(SupplierSpending::orderCount).toList());
        assertEquals(List.of(new BigDecimal("101.00"), new BigDecimal("101.00"), new BigDecimal("52.00")),
                spending.stream().map(SupplierSpending::totalValue).toList());
    }

    @Test
    void spendingPerCountrySumsTheSuppliersOfACountry() {
        createOrders();
        List<CountrySpending> spending = store.suppliers().spendingPerCountry(LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31));
        assertEquals(List.of("Sweden", "USA"), spending.stream().map(CountrySpending::country).toList());
        assertEquals(3, spending.get(0).orderCount());
        assertEquals(new BigDecimal("153.00"), spending.get(0).totalValue());
        assertEquals(new BigDecimal("101.00"), spending.get(1).totalValue());
    }

    @Test
    void datesAreInclusiveAndEmptyPeriodsGiveNothing() {
        createOrders();
        assertEquals(1, store.suppliers().spendingPerSupplier(LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 1)).size());
        assertEquals(1, store.suppliers().spendingPerCountry(LocalDate.of(2024, 7, 20), LocalDate.of(2024, 7, 20)).size());
        assertTrue(store.suppliers().spendingPerSupplier(LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 30)).isEmpty());
        assertTrue(store.suppliers().spendingPerCountry(LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 30)).isEmpty());
    }
}
