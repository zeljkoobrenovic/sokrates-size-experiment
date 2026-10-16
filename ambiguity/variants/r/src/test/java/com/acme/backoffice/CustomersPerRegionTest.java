package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionCustomers;
import com.acme.backoffice.model.SalesOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomersPerRegionTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Product item = store.products().create(new Product(0, "I-1", "Item", "Hardware", new BigDecimal("1.00"), "piece", true));
    private int counter;

    private Customer customer(String name, Region region, CustomerStatus status, LocalDate created) {
        Customer customer = store.customers().create(new Customer(0, name, name + "@example.com", null, "USA", region, status));
        if (created != null) {
            store.customers().updateCreatedDate(customer.id(), created);
        }
        return customer;
    }

    private void order(Customer customer, LocalDate date, OrderStatus status) {
        store.orders().create(new SalesOrder(0, "SO-" + (++counter), customer.id(), date, status,
                List.of(new OrderLine(item.id(), 1, new BigDecimal("1.00")))));
    }

    @Test
    void countsPerRegion() {
        Customer a = customer("A", Region.NORTH, CustomerStatus.ACTIVE, LocalDate.of(2024, 3, 1));
        customer("B", Region.NORTH, CustomerStatus.ACTIVE, LocalDate.of(2024, 3, 31));
        customer("C", Region.NORTH, CustomerStatus.ON_HOLD, LocalDate.of(2024, 4, 1));
        customer("D", Region.NORTH, CustomerStatus.ACTIVE, LocalDate.of(2024, 2, 29));
        customer("E", Region.NORTH, CustomerStatus.ACTIVE, null);
        Customer closed = customer("F", Region.NORTH, CustomerStatus.ACTIVE, LocalDate.of(2024, 3, 5));
        store.customers().close(closed.id(), LocalDate.of(2024, 3, 6));
        customer("G", Region.WEST, CustomerStatus.ON_HOLD, LocalDate.of(2024, 3, 10));
        order(a, LocalDate.of(2024, 5, 15), OrderStatus.CONFIRMED);

        List<RegionCustomers> result = store.customers().countPerRegion(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31), LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 31));
        assertEquals(List.of(
                new RegionCustomers(Region.NORTH, 4, 1, 2, 3),
                new RegionCustomers(Region.WEST, 0, 1, 1, 0)), result);
    }

    @Test
    void cancelledOrdersAndOrdersOutsideWindowDoNotCountAsActivity() {
        Customer a = customer("A", Region.EAST, CustomerStatus.ACTIVE, null);
        Customer b = customer("B", Region.EAST, CustomerStatus.ACTIVE, null);
        order(a, LocalDate.of(2024, 5, 15), OrderStatus.CANCELLED);
        order(b, LocalDate.of(2024, 4, 30), OrderStatus.CONFIRMED);
        List<RegionCustomers> result = store.customers().countPerRegion(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31), LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 31));
        assertEquals(List.of(new RegionCustomers(Region.EAST, 2, 0, 0, 2)), result);
    }
}
