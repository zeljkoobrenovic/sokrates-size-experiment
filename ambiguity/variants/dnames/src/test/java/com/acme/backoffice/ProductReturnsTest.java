package com.acme.backoffice;

import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductReturns;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.Warehouse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductReturnsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer customer = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Product bolt = store.createProduct(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product nut = store.createProduct(new Product(0, "N-1", "Nut", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product rice = store.createProduct(new Product(0, "R-1", "Rice", "Food", new BigDecimal("1.00"), "kg", true));
    private final SalesOrder order;
    private int counter;

    ProductReturnsTest() {
        store.createCategory(new Category("Hardware", "Hardware", null));
        store.createCategory(new Category("Fasteners", "Fasteners", "Hardware"));
        store.createCategory(new Category("Food", "Food", null));
        order = store.createSalesOrder(new SalesOrder(0, "SO-1", customer.id(), LocalDate.of(2024, 1, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(bolt.id(), 50, new BigDecimal("1.00")))));
    }

    private void giveBack(LocalDate date, ReturnLine... lines) {
        counter++;
        store.createSalesReturn(new SalesReturn(0, "R-" + counter, order.id(), date, "Damaged", List.of(lines)), north.id());
    }

    private void fixture() {
        giveBack(LocalDate.of(2024, 3, 10), new ReturnLine(bolt.id(), 3), new ReturnLine(rice.id(), 5));
        giveBack(LocalDate.of(2024, 3, 1), new ReturnLine(bolt.id(), 4));
        giveBack(LocalDate.of(2024, 2, 28), new ReturnLine(bolt.id(), 9));
        giveBack(LocalDate.of(2024, 3, 31), new ReturnLine(nut.id(), 7));
        giveBack(LocalDate.of(2024, 3, 12), new ReturnLine(nut.id(), 2), new ReturnLine(bolt.id(), 1));
    }

    @Test
    void listsReturnsContainingTheProductBetweenTwoDatesInclusive() {
        fixture();
        List<SalesReturn> returns = store.listSalesReturnsWithProductDatedBetween(bolt.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("R-2", "R-1", "R-5"), returns.stream().map(SalesReturn::number).toList());
        assertEquals(2, returns.get(1).lines().size());
        assertEquals(List.of("R-2"), store.listSalesReturnsWithProductDatedBetween(bolt.id(), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 1))
                .stream().map(SalesReturn::number).toList());
        assertTrue(store.listSalesReturnsWithProductDatedBetween(rice.id(), LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30)).isEmpty());
    }

    @Test
    void reportsReturnedQuantityPerProductMostReturnedFirst() {
        fixture();
        List<ProductReturns> returned = store.sumReturnedQuantityPerProductBetween(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("N-1", "B-1", "R-1"), returned.stream().map(ProductReturns::sku).toList());
        assertEquals(List.of(9L, 8L, 5L), returned.stream().map(ProductReturns::unitsReturned).toList());
        assertTrue(store.sumReturnedQuantityPerProductBetween(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31)).isEmpty());
    }

    @Test
    void reportsReturnedQuantityForACategoryIncludingSubcategories() {
        fixture();
        LocalDate from = LocalDate.of(2024, 3, 1);
        LocalDate to = LocalDate.of(2024, 3, 31);
        assertEquals(List.of("N-1", "B-1"), store.sumReturnedQuantityPerProductInCategoryTreeBetween("Fasteners", from, to).stream().map(ProductReturns::sku).toList());
        assertEquals(List.of("N-1", "B-1"), store.sumReturnedQuantityPerProductInCategoryTreeBetween("Hardware", from, to).stream().map(ProductReturns::sku).toList());
        assertEquals(List.of(5L), store.sumReturnedQuantityPerProductInCategoryTreeBetween("Food", from, to).stream().map(ProductReturns::unitsReturned).toList());
    }
}
