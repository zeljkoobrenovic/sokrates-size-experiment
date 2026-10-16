package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerPrice;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.service.ProductActivationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductActivationTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final ProductActivationService activation = new ProductActivationService(store);
    private final Customer customer = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
    private final Product gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("20.00"), "piece", true));
    private final Warehouse warehouse = store.warehouses().create(new Warehouse(0, "WH-1", "Main", "Berlin", Region.WEST));

    @Test
    void inactiveProductIsHiddenFromSearchAndPriceList() {
        store.priceLists().create(new CustomerPrice(0, customer.id(), widget.id(), new BigDecimal("7.00"),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31)));

        assertTrue(activation.deactivate(widget.id()));

        assertEquals(List.of("Gadget"), store.products().searchActiveByCategory("Hardware").stream().map(Product::name).toList());
        assertEquals(List.of(), store.priceLists().listOfCustomer(customer.id()));
    }

    @Test
    void inactiveProductIsHiddenFromStockAlertsAndPurchaseSuggestions() {
        store.stock().setReorderLevel(warehouse.id(), widget.id(), 10);
        store.stock().setReorderLevel(warehouse.id(), gadget.id(), 10);

        activation.deactivate(widget.id());

        assertEquals(List.of("Gadget"), store.stock().listLowStock().stream().map(a -> a.product().name()).toList());
        assertEquals(List.of("Gadget"), store.stock().listLowStockInWarehouse(warehouse.id()).stream().map(a -> a.product().name()).toList());
        assertEquals(List.of("Gadget"), store.stock().listPurchaseSuggestionsForWarehouse(warehouse.id()).stream().map(s -> s.product().name()).toList());
        assertEquals(List.of("Gadget"), store.stock().listActiveProductsWithTotalBelow(5).stream().map(l -> l.product().name()).toList());
    }

    @Test
    void historicalOrdersKeepInactiveProduct() {
        store.orders().create(new SalesOrder(0, "SO-1", customer.id(), LocalDate.of(2026, 3, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("10.00")))));

        activation.deactivate(widget.id());

        assertEquals(1, store.orders().listOfCustomer(customer.id()).size());
        assertEquals(1, store.salesReports().salesPerProduct(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31)).size());
        assertFalse(store.products().findById(widget.id()).orElseThrow().active());
    }

    @Test
    void reactivatingRestoresProduct() {
        store.stock().setReorderLevel(warehouse.id(), widget.id(), 10);
        activation.deactivate(widget.id());
        assertEquals(0, store.stock().listLowStock().size());

        assertTrue(activation.reactivate(widget.id()));

        assertEquals(1, store.products().searchActiveByName("widget").size());
        assertEquals(1, store.stock().listLowStock().size());
    }

    @Test
    void repeatedChangesReturnFalseAndUnknownProductIsRejected() {
        assertFalse(activation.reactivate(widget.id()));
        assertTrue(activation.deactivate(widget.id()));
        assertFalse(activation.deactivate(widget.id()));
        assertThrows(IllegalArgumentException.class, () -> activation.deactivate(9999));
    }
}
