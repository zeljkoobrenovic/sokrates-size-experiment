package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupplierProduct;
import com.acme.backoffice.service.SupplierProductService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SupplierProductTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SupplierProductService service = new SupplierProductService(store);
    private final Supplier nordic = store.createSupplier(new Supplier(0, "Nordic", "n@example.com", "Sweden", 3));
    private final Supplier acme = store.createSupplier(new Supplier(0, "Acme", "a@example.com", "USA", 5));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
    private final Product gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("20.00"), "piece", true));

    @Test
    void suppliersOfProductAreListedCheapestFirst() {
        service.record(nordic.id(), widget.id(), 14, new BigDecimal("3.00"));
        service.record(acme.id(), widget.id(), 5, new BigDecimal("2.50"));
        List<SupplierProduct> list = service.listSuppliersOfProduct(widget.id());
        assertEquals(List.of(acme.id(), nordic.id()), list.stream().map(SupplierProduct::supplierId).toList());
        assertEquals(5, list.get(0).leadTimeDays());
        assertEquals(List.of(), service.listSuppliersOfProduct(gadget.id()));
    }

    @Test
    void productsOfSupplierAreListed() {
        service.record(nordic.id(), gadget.id(), 10, new BigDecimal("9.00"));
        service.record(nordic.id(), widget.id(), 14, new BigDecimal("3.00"));
        service.record(acme.id(), widget.id(), 5, new BigDecimal("2.50"));
        assertEquals(List.of(widget.id(), gadget.id()),
                service.listProductsOfSupplier(nordic.id()).stream().map(SupplierProduct::productId).toList());
    }

    @Test
    void recordingAgainUpdatesLeadTimeAndCost() {
        service.record(nordic.id(), widget.id(), 14, new BigDecimal("3.00"));
        service.record(nordic.id(), widget.id(), 7, new BigDecimal("2.75"));
        List<SupplierProduct> list = service.listProductsOfSupplier(nordic.id());
        assertEquals(1, list.size());
        assertEquals(7, list.get(0).leadTimeDays());
        assertEquals(new BigDecimal("2.75"), list.get(0).lastUnitCost());
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.record(nordic.id(), widget.id(), -1, BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> service.record(nordic.id(), widget.id(), 1, new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class, () -> service.record(999, widget.id(), 1, BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> service.record(nordic.id(), 999, 1, BigDecimal.ONE));
    }
}
