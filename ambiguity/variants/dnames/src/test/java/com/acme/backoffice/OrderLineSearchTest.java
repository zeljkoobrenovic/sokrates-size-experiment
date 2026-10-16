package com.acme.backoffice;

import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderLineMatch;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderLineSearchTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product nail = store.createProduct(new Product(0, "N", "Nail", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product paint = store.createProduct(new Product(0, "P", "Paint", "Paints", new BigDecimal("10.00"), "piece", true));

    private SalesOrder order(String number, Customer customer, LocalDate date, OrderLine... lines) {
        return store.createSalesOrder(new SalesOrder(0, number, customer.id(), date, OrderStatus.CONFIRMED, List.of(lines)));
    }

    private List<String> keys(List<OrderLineMatch> matches) {
        return matches.stream().map(m -> m.orderNumber() + "/" + m.lineNo()).toList();
    }

    private void setUp() {
        order("SO-1", alpha, LocalDate.of(2024, 1, 10), new OrderLine(nail.id(), 50, new BigDecimal("1.00")),
                new OrderLine(paint.id(), 2, new BigDecimal("10.00")));
        order("SO-2", beta, LocalDate.of(2024, 2, 10), new OrderLine(nail.id(), 5, new BigDecimal("1.00")));
        order("SO-3", alpha, LocalDate.of(2024, 3, 10), new OrderLine(paint.id(), 10, new BigDecimal("10.00")));
    }

    @Test
    void withoutCriteriaReturnsAllLinesNewestFirst() {
        setUp();

        assertEquals(List.of("SO-3/1", "SO-2/1", "SO-1/1", "SO-1/2"), keys(store.searchSalesOrderLinesByOptionalCriteria(null, null, null, null, null, null)));
    }

    @Test
    void combinesCriteria() {
        setUp();

        assertEquals(List.of("SO-2/1", "SO-1/1"), keys(store.searchSalesOrderLinesByOptionalCriteria(nail.id(), null, null, null, null, null)));
        assertEquals(List.of("SO-3/1", "SO-1/2"), keys(store.searchSalesOrderLinesByOptionalCriteria(null, "Paints", alpha.id(), null, null, null)));
        assertEquals(List.of("SO-3/1", "SO-1/1"), keys(store.searchSalesOrderLinesByOptionalCriteria(null, null, alpha.id(), null, null, 5)));
        assertEquals(List.of("SO-1/1"), keys(store.searchSalesOrderLinesByOptionalCriteria(null, null, alpha.id(), null, null, 11)));
        assertEquals(List.of("SO-2/1"), keys(store.searchSalesOrderLinesByOptionalCriteria(nail.id(), null, null, LocalDate.of(2024, 2, 10), LocalDate.of(2024, 2, 10), null)));
        assertEquals(List.of("SO-1/1"), keys(store.searchSalesOrderLinesByOptionalCriteria(nail.id(), "Fasteners", alpha.id(), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31), 50)));
    }

    @Test
    void categoryIncludesSubcategories() {
        store.createCategory(new Category("Hardware", "Hardware", null));
        store.createCategory(new Category("Fasteners", "Fasteners", "Hardware"));
        setUp();

        assertEquals(List.of("SO-2/1", "SO-1/1"), keys(store.searchSalesOrderLinesByOptionalCriteria(null, "Hardware", null, null, null, null)));
    }

    @Test
    void returnsLineDetails() {
        setUp();

        OrderLineMatch match = store.searchSalesOrderLinesByOptionalCriteria(paint.id(), null, null, null, null, 10).get(0);
        assertEquals("Alpha", match.customerName());
        assertEquals(paint, match.product());
        assertEquals(10, match.quantity());
        assertEquals(new BigDecimal("10.00"), match.unitPrice());
    }
}
