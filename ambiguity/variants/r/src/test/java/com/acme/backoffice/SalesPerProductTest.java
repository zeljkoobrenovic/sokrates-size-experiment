package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductSales;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.CategoryService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalesPerProductTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product bolt = store.products().create(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product cake = store.products().create(new Product(0, "C-1", "Cake", "Food", new BigDecimal("3.00"), "piece", true));

    private void order(String number, LocalDate date, OrderStatus status, OrderLine... lines) {
        store.orders().create(new SalesOrder(0, number, alpha.id(), date, status, List.of(lines)));
    }

    private OrderLine line(Product product, int quantity, String price) {
        return new OrderLine(product.id(), quantity, new BigDecimal(price));
    }

    private void fill() {
        CategoryService categories = new CategoryService(store);
        categories.createCategory("Hardware", "Hardware", null);
        categories.createCategory("Fasteners", "Fasteners", "Hardware");
        categories.createCategory("Food", "Food", null);
        order("SO-1", LocalDate.of(2024, 6, 1), OrderStatus.CONFIRMED, line(widget, 2, "10.00"), line(bolt, 10, "1.00"));
        order("SO-2", LocalDate.of(2024, 6, 30), OrderStatus.DELIVERED, line(widget, 3, "8.00"), line(cake, 5, "3.00"));
        order("SO-3", LocalDate.of(2024, 6, 15), OrderStatus.CANCELLED, line(cake, 50, "3.00"));
        order("SO-4", LocalDate.of(2024, 7, 1), OrderStatus.CONFIRMED, line(cake, 50, "3.00"));
        order("SO-5", LocalDate.of(2024, 5, 31), OrderStatus.CONFIRMED, line(cake, 50, "3.00"));
    }

    @Test
    void sumsUnitsAndRevenuePerProductBestSellersFirst() {
        fill();
        List<ProductSales> result = store.salesReports().salesPerProduct(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals(List.of("B-1", "W-1", "C-1"), result.stream().map(ProductSales::sku).toList());
        assertEquals(10, result.get(0).unitsSold());
        assertEquals(0, new BigDecimal("10.00").compareTo(result.get(0).revenue()));
        assertEquals(5, result.get(1).unitsSold());
        assertEquals(0, new BigDecimal("44.00").compareTo(result.get(1).revenue()));
        assertEquals(5, result.get(2).unitsSold());
        assertEquals(0, new BigDecimal("15.00").compareTo(result.get(2).revenue()));
    }

    @Test
    void categoryIncludesSubcategories() {
        fill();
        List<ProductSales> hardware = store.salesReports().salesPerProductInCategory("Hardware", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals(List.of("B-1", "W-1"), hardware.stream().map(ProductSales::sku).toList());
        List<ProductSales> fasteners = store.salesReports().salesPerProductInCategory("Fasteners", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        assertEquals(List.of("B-1"), fasteners.stream().map(ProductSales::sku).toList());
        List<ProductSales> food = store.salesReports().salesPerProductInCategory("Food", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 1));
        assertEquals(List.of("C-1"), food.stream().map(ProductSales::sku).toList());
        assertEquals(55, food.get(0).unitsSold());
    }

    @Test
    void emptyResultIsEmptyList() {
        fill();
        assertTrue(store.salesReports().salesPerProduct(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)).isEmpty());
        assertTrue(store.salesReports().salesPerProductInCategory("Nope", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)).isEmpty());
    }
}
