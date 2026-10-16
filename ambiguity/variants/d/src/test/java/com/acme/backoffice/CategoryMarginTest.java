package com.acme.backoffice;

import com.acme.backoffice.model.CategoryMargin;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.Supplier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryMarginTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer customer = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Supplier supplier = store.createSupplier(new Supplier(0, "Nordic", "n@example.com", "Sweden", 3));
    private final Product bolt = store.createProduct(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("5.00"), "piece", true));
    private final Product nut = store.createProduct(new Product(0, "N-1", "Nut", "Fasteners", new BigDecimal("3.00"), "piece", true));
    private final Product rice = store.createProduct(new Product(0, "R-1", "Rice", "Food", new BigDecimal("1.00"), "kg", true));
    private int counter;

    private void purchase(Product product, LocalDate date, PurchaseOrderStatus status, String unitCost) {
        counter++;
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-" + counter, supplier.id(), date, date.plusDays(7), status,
                List.of(new PurchaseOrderLine(product.id(), 10, new BigDecimal(unitCost)))));
    }

    private void sell(LocalDate date, OrderStatus status, OrderLine... lines) {
        counter++;
        store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date, status, List.of(lines)));
    }

    private void fixture() {
        purchase(bolt, LocalDate.of(2024, 1, 1), PurchaseOrderStatus.RECEIVED, "1.00");
        purchase(bolt, LocalDate.of(2024, 2, 1), PurchaseOrderStatus.SENT, "4.00");
        purchase(bolt, LocalDate.of(2024, 3, 1), PurchaseOrderStatus.DRAFT, "0.10");
        purchase(nut, LocalDate.of(2024, 1, 1), PurchaseOrderStatus.SENT, "2.00");
        sell(LocalDate.of(2024, 4, 1), OrderStatus.DELIVERED, new OrderLine(bolt.id(), 10, new BigDecimal("5.00")),
                new OrderLine(nut.id(), 10, new BigDecimal("3.00")), new OrderLine(rice.id(), 4, new BigDecimal("2.50")));
        sell(LocalDate.of(2024, 4, 30), OrderStatus.CONFIRMED, new OrderLine(bolt.id(), 5, new BigDecimal("6.00")));
        sell(LocalDate.of(2024, 4, 15), OrderStatus.CANCELLED, new OrderLine(nut.id(), 100, new BigDecimal("3.00")));
        sell(LocalDate.of(2024, 5, 1), OrderStatus.CONFIRMED, new OrderLine(bolt.id(), 100, new BigDecimal("5.00")));
    }

    @Test
    void marginPerCategorySortsLowestFirstWithPercentage() {
        fixture();
        List<CategoryMargin> margins = store.findMarginPerCategory(LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30));
        assertEquals(List.of("Food", "Fasteners"), margins.stream().map(CategoryMargin::category).toList());
        assertEquals(List.of(new BigDecimal("10.00"), new BigDecimal("110.00")),
                margins.stream().map(CategoryMargin::revenue).toList());
        assertEquals(List.of(new BigDecimal("0.00"), new BigDecimal("80.00")),
                margins.stream().map(CategoryMargin::cost).toList());
        assertEquals(List.of(new BigDecimal("10.00"), new BigDecimal("30.00")),
                margins.stream().map(CategoryMargin::margin).toList());
        assertEquals(List.of(new BigDecimal("100.00"), new BigDecimal("27.27")),
                margins.stream().map(CategoryMargin::marginPercent).toList());
    }

    @Test
    void categoriesBelowAPercentageExcludeTheThresholdItself() {
        fixture();
        LocalDate from = LocalDate.of(2024, 4, 1);
        LocalDate to = LocalDate.of(2024, 4, 30);
        assertEquals(List.of("Fasteners"), store.findMarginPerCategoryBelowPercent(new BigDecimal("50"), from, to).stream()
                .map(CategoryMargin::category).toList());
        assertEquals(List.of("Fasteners"), store.findMarginPerCategoryBelowPercent(new BigDecimal("100"), from, to).stream()
                .map(CategoryMargin::category).toList());
        assertEquals(List.of("Food", "Fasteners"), store.findMarginPerCategoryBelowPercent(new BigDecimal("100.01"), from, to).stream()
                .map(CategoryMargin::category).toList());
        assertTrue(store.findMarginPerCategoryBelowPercent(new BigDecimal("27"), from, to).isEmpty());
    }

    @Test
    void emptyPeriodGivesNothing() {
        fixture();
        assertTrue(store.findMarginPerCategory(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31)).isEmpty());
        assertTrue(store.findMarginPerCategoryBelowPercent(new BigDecimal("50"), LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31)).isEmpty());
    }
}
