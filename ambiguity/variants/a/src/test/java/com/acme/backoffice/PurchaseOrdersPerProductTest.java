package com.acme.backoffice;

import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductPurchaseOrders;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.service.PurchaseReceivingService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PurchaseOrdersPerProductTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PurchaseReceivingService service = new PurchaseReceivingService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.createProduct(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("6.00"), "piece", true));
    private final Supplier nordic = store.createSupplier(new Supplier(0, "Nordic Supply", "n@example.com", "Sweden", 3));
    private final Supplier baltic = store.createSupplier(new Supplier(0, "Baltic Trade", "b@example.com", "Latvia", 4));

    private void order(String number, Supplier supplier, String date, PurchaseOrderStatus status, PurchaseOrderLine... lines) {
        store.createPurchaseOrder(new PurchaseOrder(0, number, supplier.id(), LocalDate.parse(date), LocalDate.parse(date).plusDays(14),
                status, List.of(lines)));
    }

    private PurchaseOrderLine line(Product product, int quantity) {
        return new PurchaseOrderLine(product.id(), quantity, new BigDecimal("2.00"));
    }

    @Test
    void listsOrdersAndQuantitiesPerSupplierInThePeriod() {
        order("PO-3", nordic, "2024-03-20", PurchaseOrderStatus.SENT, line(widget, 3));
        order("PO-1", nordic, "2024-03-01", PurchaseOrderStatus.RECEIVED, line(widget, 2), line(gadget, 7));
        order("PO-2", baltic, "2024-03-31", PurchaseOrderStatus.DRAFT, line(widget, 9));
        order("PO-4", baltic, "2024-04-01", PurchaseOrderStatus.SENT, line(widget, 11));
        order("PO-0", nordic, "2024-02-29", PurchaseOrderStatus.SENT, line(widget, 13));
        order("PO-5", baltic, "2024-03-10", PurchaseOrderStatus.SENT, line(gadget, 20));

        ProductPurchaseOrders result = service.listPurchaseOrdersByProduct("W-1", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals("W-1", result.product().sku());
        assertEquals(List.of("PO-1", "PO-3", "PO-2"), result.orders().stream().map(o -> o.number()).toList());
        assertEquals(2, result.orders().get(0).lines().size());
        assertEquals(List.of("Baltic Trade:9", "Nordic Supply:5"),
                result.quantities().stream().map(q -> q.supplier().name() + ":" + q.quantity()).toList());
    }

    @Test
    void returnsNothingForAProductWithoutOrders() {
        order("PO-1", nordic, "2024-03-01", PurchaseOrderStatus.SENT, line(gadget, 4));
        ProductPurchaseOrders result = service.listPurchaseOrdersByProduct("W-1", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of(), result.orders());
        assertEquals(List.of(), result.quantities());
    }

    @Test
    void rejectsUnknownProductAndBadDates() {
        assertThrows(IllegalArgumentException.class, () -> service.listPurchaseOrdersByProduct("NOPE", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
        assertThrows(IllegalArgumentException.class, () -> service.listPurchaseOrdersByProduct("W-1", LocalDate.of(2024, 3, 31), LocalDate.of(2024, 3, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.listPurchaseOrdersByProduct("W-1", null, LocalDate.of(2024, 3, 1)));
    }
}
