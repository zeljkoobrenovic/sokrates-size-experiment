package com.acme.backoffice;

import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.CategoryMonthRevenue;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductOrder;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.CategoryService;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersPerCategoryTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private Product hammer;
    private Product paint;

    private void order(String number, Customer customer, LocalDate date, Product product, int quantity) {
        store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, OrderStatus.CONFIRMED,
                List.of(new OrderLine(product.id(), quantity, product.unitPrice()))));
    }

    private List<String> numbers(List<ProductOrder> list) {
        return list.stream().map(p -> p.order().number()).toList();
    }

    @BeforeEach
    void setUp() {
        CategoryService categories = new CategoryService(store);
        categories.createCategory("TOOLS", "Tools", null);
        categories.createCategory("HAND", "Hand tools", "TOOLS");
        categories.createCategory("PAINT", "Paint", null);
        Customer nord = store.createCustomer(new Customer(0, "Nord", "n@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        hammer = store.createProduct(new Product(0, "H-1", "Hammer", "HAND", new BigDecimal("10.00"), "piece", true));
        paint = store.createProduct(new Product(0, "P-1", "Paint", "PAINT", new BigDecimal("5.00"), "piece", true));
        order("SO-1", nord, LocalDate.of(2024, 1, 31), hammer, 2);
        order("SO-2", nord, LocalDate.of(2024, 2, 1), hammer, 1);
        order("SO-3", nord, LocalDate.of(2024, 2, 10), paint, 4);
        order("SO-4", nord, LocalDate.of(2024, 2, 20), hammer, 3);
        order("SO-5", nord, LocalDate.of(2024, 3, 1), hammer, 1);
        orders.cancel("SO-4", CancellationReason.OTHER, LocalDate.of(2024, 2, 21));
    }

    @Test
    void listsOrdersOfCategoryAndSubcategoriesNewestFirst() {
        List<ProductOrder> result = orders.getOrdersByCategory("TOOLS", LocalDate.of(2024, 1, 31), LocalDate.of(2024, 3, 1));
        assertEquals(List.of("SO-5", "SO-2", "SO-1"), numbers(result));
        assertEquals("Nord", result.get(0).customer().name());
        assertEquals(List.of("SO-3"), numbers(orders.getOrdersByCategory("PAINT", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 28))));
    }

    @Test
    void periodIsInclusive() {
        assertEquals(List.of("SO-2"), numbers(orders.getOrdersByCategory("HAND", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 1))));
        assertTrue(orders.getOrdersByCategory("HAND", LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30)).isEmpty());
    }

    @Test
    void revenuePerCategoryAndMonthSkipsCancelled() {
        List<CategoryMonthRevenue> result = orders.getRevenuePerCategoryAndMonth(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 31));
        assertEquals(4, result.size());
        assertEquals("HAND", result.get(0).category());
        assertEquals(YearMonth.of(2024, 1), result.get(0).month());
        assertEquals(0, new BigDecimal("20.00").compareTo(result.get(0).revenue()));
        assertEquals(YearMonth.of(2024, 2), result.get(1).month());
        assertEquals(0, new BigDecimal("10.00").compareTo(result.get(1).revenue()));
        assertEquals(YearMonth.of(2024, 3), result.get(2).month());
        assertEquals("PAINT", result.get(3).category());
        assertEquals(0, new BigDecimal("20.00").compareTo(result.get(3).revenue()));
    }

    @Test
    void rejectsBadInput() {
        LocalDate day = LocalDate.of(2024, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> orders.getOrdersByCategory(" ", day, day));
        assertThrows(IllegalArgumentException.class, () -> orders.getOrdersByCategory("HAND", null, day));
        assertThrows(IllegalArgumentException.class, () -> orders.getOrdersByCategory("HAND", day.plusDays(1), day));
        assertThrows(IllegalArgumentException.class, () -> orders.getRevenuePerCategoryAndMonth(day, null));
        assertThrows(IllegalArgumentException.class, () -> orders.getRevenuePerCategoryAndMonth(day.plusDays(1), day));
    }
}
