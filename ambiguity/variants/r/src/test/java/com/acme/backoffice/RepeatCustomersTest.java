package com.acme.backoffice;

import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RepeatCustomer;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.CategoryService;
import com.acme.backoffice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepeatCustomersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private Product item;
    private int counter;

    private Customer customer(String name, Region region) {
        return store.customers().create(new Customer(0, name, name + "@example.com", null, "X", region, CustomerStatus.ACTIVE));
    }

    private void order(Customer customer, LocalDate date) {
        counter++;
        store.orders().create(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.CONFIRMED,
                List.of(new OrderLine(item.id(), 1, item.unitPrice()))));
    }

    private List<String> names(List<RepeatCustomer> list) {
        return list.stream().map(r -> r.region() + ":" + r.name() + ":" + r.orderCount()).toList();
    }

    @BeforeEach
    void setUp() {
        new CategoryService(store).createCategory("CAT", "Category", null);
        item = store.products().create(new Product(0, "I-1", "Item", "CAT", new BigDecimal("5.00"), "piece", true));
        Customer anna = customer("Anna", Region.NORTH);
        Customer ben = customer("Ben", Region.NORTH);
        Customer cleo = customer("Cleo", Region.SOUTH);
        Customer dan = customer("Dan", Region.EAST);
        Customer eve = customer("Eve", Region.NORTH);
        order(anna, LocalDate.of(2024, 3, 1));
        order(anna, LocalDate.of(2024, 3, 15));
        order(ben, LocalDate.of(2024, 3, 2));
        order(ben, LocalDate.of(2024, 3, 31));
        order(ben, LocalDate.of(2024, 3, 10));
        order(cleo, LocalDate.of(2024, 3, 3));
        order(cleo, LocalDate.of(2024, 3, 4));
        order(dan, LocalDate.of(2024, 3, 5));
        order(dan, LocalDate.of(2024, 3, 6));
        order(dan, LocalDate.of(2024, 4, 1));
        order(eve, LocalDate.of(2024, 3, 7));
        order(eve, LocalDate.of(2024, 3, 8));
        orders.cancel("SO-12", CancellationReason.OTHER, LocalDate.of(2024, 3, 9));
    }

    @Test
    void listsRepeatCustomersPerRegionMostOrdersFirst() {
        List<RepeatCustomer> result = orders.getRepeatCustomersByRegion(2, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("EAST:Dan:2", "NORTH:Ben:3", "NORTH:Anna:2", "SOUTH:Cleo:2"), names(result));
    }

    @Test
    void higherMinimumFiltersCustomers() {
        List<RepeatCustomer> result = orders.getRepeatCustomersByRegion(3, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("NORTH:Ben:3"), names(result));
        assertTrue(orders.getRepeatCustomersByRegion(4, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31)).isEmpty());
    }

    @Test
    void periodIsInclusiveAndCancelledOrdersAreSkipped() {
        List<RepeatCustomer> result = orders.getRepeatCustomersByRegion(2, LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("EAST:Dan:2", "NORTH:Ben:3", "SOUTH:Cleo:2"), names(result));
        assertEquals(List.of("EAST:Dan:3"), names(orders.getRepeatCustomersByRegion(3, LocalDate.of(2024, 3, 5), LocalDate.of(2024, 4, 1))));
    }

    @Test
    void rejectsBadInput() {
        LocalDate day = LocalDate.of(2024, 3, 1);
        assertThrows(IllegalArgumentException.class, () -> orders.getRepeatCustomersByRegion(0, day, day));
        assertThrows(IllegalArgumentException.class, () -> orders.getRepeatCustomersByRegion(2, null, day));
        assertThrows(IllegalArgumentException.class, () -> orders.getRepeatCustomersByRegion(2, day, null));
        assertThrows(IllegalArgumentException.class, () -> orders.getRepeatCustomersByRegion(2, day.plusDays(1), day));
    }
}
