package com.acme.backoffice;

import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.CategoryPurchaseOrders;
import com.acme.backoffice.model.Product;
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

class PurchaseOrdersPerCategoryTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final PurchaseReceivingService service = new PurchaseReceivingService(store);
    private final Supplier nordic = store.suppliers().create(new Supplier(0, "Nordic Supply", "n@example.com", "Sweden", 3));
    private Product tool;
    private Product drill;
    private Product paint;

    private void setUp() {
        store.categories().create(new Category("TOOLS", "Tools", null));
        store.categories().create(new Category("DRILLS", "Drills", "TOOLS"));
        store.categories().create(new Category("PAINT", "Paint", null));
        tool = store.products().create(new Product(0, "T-1", "Hammer", "TOOLS", new BigDecimal("4.50"), "piece", true));
        drill = store.products().create(new Product(0, "D-1", "Drill", "DRILLS", new BigDecimal("6.00"), "piece", true));
        paint = store.products().create(new Product(0, "P-1", "Paint", "PAINT", new BigDecimal("6.00"), "piece", true));
    }

    private void order(String number, String date, PurchaseOrderStatus status, PurchaseOrderLine... lines) {
        store.purchaseOrders().create(new PurchaseOrder(0, number, nordic.id(), LocalDate.parse(date), LocalDate.parse(date).plusDays(14),
                status, List.of(lines)));
    }

    private PurchaseOrderLine line(Product product, int quantity, String cost) {
        return new PurchaseOrderLine(product.id(), quantity, new BigDecimal(cost));
    }

    @Test
    void listsOrdersAndSpendPerCategoryInThePeriod() {
        setUp();
        order("PO-3", "2024-03-20", PurchaseOrderStatus.SENT, line(tool, 3, "2.00"));
        order("PO-1", "2024-03-01", PurchaseOrderStatus.RECEIVED, line(tool, 2, "2.00"), line(paint, 7, "1.00"));
        order("PO-2", "2024-03-31", PurchaseOrderStatus.DRAFT, line(drill, 4, "10.00"));
        order("PO-4", "2024-04-01", PurchaseOrderStatus.SENT, line(tool, 11, "2.00"));
        order("PO-5", "2024-03-10", PurchaseOrderStatus.SENT, line(paint, 20, "1.00"));
        order("PO-6", "2024-03-11", PurchaseOrderStatus.CANCELLED, line(tool, 50, "2.00"));

        CategoryPurchaseOrders result = service.listPurchaseOrdersByCategory("TOOLS", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals("TOOLS", result.category().code());
        assertEquals(List.of("PO-1", "PO-3", "PO-2"), result.orders().stream().map(o -> o.number()).toList());
        assertEquals(2, result.orders().get(0).lines().size());
        assertEquals(List.of("DRILLS:40.00", "TOOLS:10.00"),
                result.spend().stream().map(s -> s.category() + ":" + s.spend()).toList());
    }

    @Test
    void returnsNothingForACategoryWithoutOrders() {
        setUp();
        order("PO-1", "2024-03-01", PurchaseOrderStatus.SENT, line(paint, 4, "1.00"));
        CategoryPurchaseOrders result = service.listPurchaseOrdersByCategory("TOOLS", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of(), result.orders());
        assertEquals(List.of(), result.spend());
    }

    @Test
    void rejectsUnknownCategoryAndBadDates() {
        setUp();
        assertThrows(IllegalArgumentException.class, () -> service.listPurchaseOrdersByCategory("NOPE", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)));
        assertThrows(IllegalArgumentException.class, () -> service.listPurchaseOrdersByCategory("TOOLS", LocalDate.of(2024, 3, 31), LocalDate.of(2024, 3, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.listPurchaseOrdersByCategory("TOOLS", null, LocalDate.of(2024, 3, 1)));
    }
}
