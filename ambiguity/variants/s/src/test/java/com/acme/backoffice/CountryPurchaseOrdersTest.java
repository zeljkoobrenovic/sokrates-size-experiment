package com.acme.backoffice;

import com.acme.backoffice.model.CountryPurchaseOrderCount;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.CountryPurchaseOrdersService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryPurchaseOrdersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CountryPurchaseOrdersService service = new CountryPurchaseOrdersService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Supplier supplier = store.createSupplier(new Supplier(0, "Acme Supply", "s@example.com", "Sweden", 3));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Warehouse south = store.createWarehouse(new Warehouse(0, "WH-S", "South", "Rome", Region.SOUTH));
    private int counter;

    private void customer(String country, Region region) {
        counter++;
        store.createCustomer(new Customer(0, "C" + counter, "c" + counter + "@example.com", null, country, region, CustomerStatus.ACTIVE));
    }

    private void purchaseOrder(String number, Warehouse warehouse, LocalDate date) {
        store.createPurchaseOrder(new PurchaseOrder(0, number, supplier.id(), date, date.plusDays(14), PurchaseOrderStatus.SENT,
                List.of(new PurchaseOrderLine(widget.id(), 1, new BigDecimal("1.00"))), warehouse.id()));
    }

    private void fill() {
        customer("UK", Region.NORTH);
        customer("UK", Region.NORTH);
        customer("UK", Region.SOUTH);
        customer("DE", Region.SOUTH);
        customer("FR", Region.EAST);
        purchaseOrder("A", north, LocalDate.of(2024, 1, 20));
        purchaseOrder("B", south, LocalDate.of(2024, 1, 1));
        purchaseOrder("C", south, LocalDate.of(2024, 1, 31));
        purchaseOrder("D", north, LocalDate.of(2023, 12, 31));
        purchaseOrder("E", north, LocalDate.of(2024, 2, 1));
    }

    @Test
    void listsPurchaseOrdersOfCountryInPeriodOldestFirst() {
        fill();

        List<PurchaseOrder> orders = service.purchaseOrders("UK", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        assertEquals(List.of("B", "A", "C"), orders.stream().map(PurchaseOrder::number).toList());
        assertEquals(1, orders.get(0).lines().size());
        assertEquals(List.of("B", "C"), service.purchaseOrders("DE", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31))
                .stream().map(PurchaseOrder::number).toList());
        assertEquals(List.of(), service.purchaseOrders("FR", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
        assertEquals(List.of(), service.purchaseOrders("IT", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void countsPurchaseOrdersPerCountryHighestFirst() {
        fill();

        assertEquals(List.of(new CountryPurchaseOrderCount("UK", 3), new CountryPurchaseOrderCount("DE", 2)),
                service.countPerCountry(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)));
    }

    @Test
    void emptyPeriodGivesEmptyResults() {
        fill();

        assertEquals(List.of(), service.countPerCountry(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 1, 31)));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.countPerCountry(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.purchaseOrders("UK", null, LocalDate.of(2024, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.purchaseOrders(" ", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2)));
    }
}
