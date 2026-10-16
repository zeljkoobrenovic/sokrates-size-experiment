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

class CancelledPurchaseOrdersPerRepTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRepPurchaseOrderCountService service = new SalesRepPurchaseOrderCountService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Supplier supplier = store.createSupplier(new Supplier(0, "Acme Supply", "s@example.com", "Sweden", 3));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse south = store.createWarehouse(new Warehouse(0, "WH-S", "South", "Rome", Region.SOUTH));
    private int counter;

    private SalesRep rep(String name) {
        return store.createSalesRep(new SalesRep(0, name, name + "@example.com", Region.NORTH));
    }

    private void customer(SalesRep rep, Region region) {
        counter++;
        Customer customer = store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, "UK", region, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(customer.id(), rep.id());
    }

    private void purchaseOrder(Warehouse warehouse, LocalDate date, PurchaseOrderStatus status) {
        counter++;
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-" + counter, supplier.id(), date, date.plusDays(14), status,
                List.of(new PurchaseOrderLine(widget.id(), 1, new BigDecimal("1.00"))), warehouse.id()));
    }

    @Test
    void cancelledPurchaseOrdersAreNotCounted() {
        SalesRep anna = rep("Anna");
        SalesRep bob = rep("Bob");
        customer(anna, Region.NORTH);
        customer(bob, Region.SOUTH);
        purchaseOrder(north, LocalDate.of(2024, 1, 5), PurchaseOrderStatus.SENT);
        purchaseOrder(north, LocalDate.of(2024, 1, 6), PurchaseOrderStatus.RECEIVED);
        purchaseOrder(south, LocalDate.of(2024, 1, 7), PurchaseOrderStatus.SENT);
        purchaseOrder(south, LocalDate.of(2024, 1, 8), PurchaseOrderStatus.CANCELLED);
        purchaseOrder(south, LocalDate.of(2024, 1, 9), PurchaseOrderStatus.CANCELLED);

        List<String> report = service.report(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)).stream()
                .map((SalesRepPurchaseOrderCount c) -> c.name() + ":" + c.purchaseOrderCount()).toList();
        assertEquals(List.of("Anna:2", "Bob:1"), report);
    }
}
