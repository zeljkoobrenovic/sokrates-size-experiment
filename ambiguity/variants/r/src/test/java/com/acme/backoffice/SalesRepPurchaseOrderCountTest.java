package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesRepPurchaseOrderCount;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.SalesRepPurchaseOrderCountService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalesRepPurchaseOrderCountTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRepPurchaseOrderCountService service = new SalesRepPurchaseOrderCountService(store);
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Supplier supplier = store.suppliers().create(new Supplier(0, "Acme Supply", "s@example.com", "Sweden", 3));
    private final Warehouse north = store.warehouses().create(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse south = store.warehouses().create(new Warehouse(0, "WH-S", "South", "Rome", Region.SOUTH));
    private int counter;

    private SalesRep rep(String name) {
        return store.salesReps().create(new SalesRep(0, name, name + "@example.com", Region.NORTH));
    }

    private void customer(SalesRep rep, Region region) {
        counter++;
        Customer customer = store.customers().create(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", region, CustomerStatus.ACTIVE));
        store.customers().assignToSalesRep(customer.id(), rep.id());
    }

    private void purchaseOrder(Warehouse warehouse, LocalDate date) {
        counter++;
        store.purchaseOrders().create(new PurchaseOrder(0, "PO-" + counter, supplier.id(), date, date.plusDays(14), PurchaseOrderStatus.SENT,
                List.of(new PurchaseOrderLine(widget.id(), 1, new BigDecimal("1.00"))), warehouse.id()));
    }

    private String describe(SalesRepPurchaseOrderCount count) {
        return count.name() + ":" + count.purchaseOrderCount();
    }

    @Test
    void countsPurchaseOrdersPerRepHighestFirst() {
        SalesRep anna = rep("Anna");
        SalesRep bob = rep("Bob");
        SalesRep carl = rep("Carl");
        SalesRep dora = rep("Dora");
        customer(anna, Region.NORTH);
        customer(bob, Region.SOUTH);
        customer(bob, Region.SOUTH);
        customer(carl, Region.NORTH);
        customer(carl, Region.SOUTH);
        purchaseOrder(north, LocalDate.of(2024, 1, 1));
        purchaseOrder(south, LocalDate.of(2024, 1, 15));
        purchaseOrder(south, LocalDate.of(2024, 1, 31));
        purchaseOrder(south, LocalDate.of(2023, 12, 31));
        purchaseOrder(north, LocalDate.of(2024, 2, 1));

        assertEquals(List.of("Carl:3", "Bob:2", "Anna:1", "Dora:0"),
                service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)).stream().map(this::describe).toList());
    }

    @Test
    void noRepsGivesEmptyReport() {
        assertEquals(List.of(), service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.report(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.report(null, LocalDate.of(2024, 1, 1)));
    }
}
