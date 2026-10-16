package com.acme.backoffice;

import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.FirstTimeCustomer;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
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

class FirstTimeCustomersTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private Product item;
    private int counter;

    private Customer customer(String name, Region region, SalesRep rep) {
        Customer c = store.createCustomer(new Customer(0, name, name + "@example.com", null, "X", region, CustomerStatus.ACTIVE));
        if (rep != null) {
            store.assignCustomerToSalesRep(c.id(), rep.id());
        }
        return c;
    }

    private void order(Customer customer, LocalDate date) {
        counter++;
        store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.CONFIRMED,
                List.of(new OrderLine(item.id(), 1, item.unitPrice()))));
    }

    private List<String> names(List<FirstTimeCustomer> list) {
        return list.stream().map(f -> f.region() + ":" + (f.salesRep() == null ? "-" : f.salesRep().name()) + ":"
                + f.name() + ":" + f.firstOrderDate()).toList();
    }

    @BeforeEach
    void setUp() {
        new CategoryService(store).createCategory("CAT", "Category", null);
        item = store.createProduct(new Product(0, "I-1", "Item", "CAT", new BigDecimal("5.00"), "piece", true));
        SalesRep rita = store.createSalesRep(new SalesRep(0, "Rita", "rita@example.com", Region.NORTH));
        SalesRep sam = store.createSalesRep(new SalesRep(0, "Sam", "sam@example.com", Region.NORTH));
        Customer anna = customer("Anna", Region.NORTH, sam);
        Customer ben = customer("Ben", Region.NORTH, rita);
        Customer cleo = customer("Cleo", Region.SOUTH, null);
        Customer dan = customer("Dan", Region.NORTH, rita);
        Customer eve = customer("Eve", Region.NORTH, rita);
        customer("Fay", Region.EAST, null);
        order(anna, LocalDate.of(2024, 3, 10));
        order(anna, LocalDate.of(2024, 3, 12));
        order(ben, LocalDate.of(2024, 2, 20));
        order(ben, LocalDate.of(2024, 3, 11));
        order(cleo, LocalDate.of(2024, 3, 1));
        order(dan, LocalDate.of(2024, 3, 31));
        order(eve, LocalDate.of(2024, 3, 5));
        order(eve, LocalDate.of(2024, 3, 20));
        orders.cancel("SO-7", CancellationReason.OTHER, LocalDate.of(2024, 3, 6));
    }

    @Test
    void listsFirstTimeCustomersPerRegionAndSalesRep() {
        List<FirstTimeCustomer> result = orders.getFirstTimeCustomers(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("NORTH:Rita:Eve:2024-03-20", "NORTH:Rita:Dan:2024-03-31", "NORTH:Sam:Anna:2024-03-10",
                "SOUTH:-:Cleo:2024-03-01"), names(result));
    }

    @Test
    void earlierOrdersExcludeCustomerAndCancelledOrdersDoNotCount() {
        List<FirstTimeCustomer> result = orders.getFirstTimeCustomers(LocalDate.of(2024, 3, 5), LocalDate.of(2024, 3, 10));
        assertEquals(List.of("NORTH:Sam:Anna:2024-03-10"), names(result));
        assertTrue(orders.getFirstTimeCustomers(LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30)).isEmpty());
    }

    @Test
    void rejectsBadInput() {
        LocalDate day = LocalDate.of(2024, 3, 1);
        assertThrows(IllegalArgumentException.class, () -> orders.getFirstTimeCustomers(null, day));
        assertThrows(IllegalArgumentException.class, () -> orders.getFirstTimeCustomers(day, null));
        assertThrows(IllegalArgumentException.class, () -> orders.getFirstTimeCustomers(day.plusDays(1), day));
    }
}
