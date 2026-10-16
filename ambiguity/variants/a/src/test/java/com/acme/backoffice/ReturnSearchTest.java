package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
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

class ReturnSearchTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
    private final Warehouse north = store.createWarehouse(new Warehouse(0, "WH-N", "North", "Oslo", Region.NORTH));
    private final Product bolt = store.createProduct(new Product(0, "B-1", "Bolt", "Fasteners", new BigDecimal("1.00"), "piece", true));
    private final Product nut = store.createProduct(new Product(0, "N-1", "Nut", "Fasteners", new BigDecimal("1.00"), "piece", true));

    private void fixture() {
        SalesOrder alphaOrder = store.createSalesOrder(new SalesOrder(0, "SO-1", alpha.id(), LocalDate.of(2024, 1, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(bolt.id(), 50, new BigDecimal("1.00")), new OrderLine(nut.id(), 50, new BigDecimal("1.00")))));
        SalesOrder betaOrder = store.createSalesOrder(new SalesOrder(0, "SO-2", beta.id(), LocalDate.of(2024, 1, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(bolt.id(), 50, new BigDecimal("1.00")))));
        store.createSalesReturn(new SalesReturn(0, "R-1", alphaOrder.id(), LocalDate.of(2024, 3, 10), "Damaged", List.of(new ReturnLine(bolt.id(), 3))), north.id());
        store.createSalesReturn(new SalesReturn(0, "R-2", betaOrder.id(), LocalDate.of(2024, 2, 1), "Damaged", List.of(new ReturnLine(bolt.id(), 4))), north.id());
        store.createSalesReturn(new SalesReturn(0, "R-3", alphaOrder.id(), LocalDate.of(2024, 3, 20), "Wrong item", List.of(new ReturnLine(nut.id(), 2))), north.id());
        store.createSalesReturn(new SalesReturn(0, "R-4", alphaOrder.id(), LocalDate.of(2024, 4, 1), "Damaged", List.of(new ReturnLine(nut.id(), 1), new ReturnLine(bolt.id(), 1))), north.id());
    }

    private List<String> numbers(List<SalesReturn> returns) {
        return returns.stream().map(SalesReturn::number).toList();
    }

    @Test
    void withoutCriteriaReturnsEverythingOldestFirst() {
        fixture();
        List<SalesReturn> all = store.searchSalesReturns(null, null, null, null, null);
        assertEquals(List.of("R-2", "R-1", "R-3", "R-4"), numbers(all));
        assertEquals(2, all.get(3).lines().size());
    }

    @Test
    void filtersBySingleCriterion() {
        fixture();
        assertEquals(List.of("R-1", "R-3", "R-4"), numbers(store.searchSalesReturns(alpha.id(), null, null, null, null)));
        assertEquals(List.of("R-3", "R-4"), numbers(store.searchSalesReturns(null, nut.id(), null, null, null)));
        assertEquals(List.of("R-2", "R-1", "R-4"), numbers(store.searchSalesReturns(null, null, "Damaged", null, null)));
        assertEquals(List.of("R-1", "R-3"), numbers(store.searchSalesReturns(null, null, null, LocalDate.of(2024, 3, 10), LocalDate.of(2024, 3, 20))));
        assertEquals(List.of("R-4"), numbers(store.searchSalesReturns(null, null, null, LocalDate.of(2024, 3, 21), null)));
        assertEquals(List.of("R-2"), numbers(store.searchSalesReturns(null, null, null, null, LocalDate.of(2024, 2, 29))));
    }

    @Test
    void combinesCriteria() {
        fixture();
        assertEquals(List.of("R-1", "R-4"), numbers(store.searchSalesReturns(alpha.id(), bolt.id(), "Damaged", null, null)));
        assertEquals(List.of("R-4"), numbers(store.searchSalesReturns(alpha.id(), bolt.id(), "Damaged", LocalDate.of(2024, 3, 11), null)));
        assertTrue(store.searchSalesReturns(beta.id(), nut.id(), null, null, null).isEmpty());
        assertTrue(store.searchSalesReturns(null, null, "Late", null, null).isEmpty());
    }
}
