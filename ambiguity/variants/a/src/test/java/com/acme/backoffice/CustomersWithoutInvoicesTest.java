package com.acme.backoffice;

import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
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

class CustomersWithoutInvoicesTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private Product item;
    private int counter;

    private Customer customer(String name, Region region, CustomerStatus status) {
        return store.createCustomer(new Customer(0, name, name + "@example.com", null, "X", region, status));
    }

    private SalesOrder order(Customer customer, LocalDate date) {
        counter++;
        return store.createSalesOrder(new SalesOrder(0, "SO-" + counter, customer.id(), date, OrderStatus.CONFIRMED,
                List.of(new OrderLine(item.id(), 1, item.unitPrice()))));
    }

    private void invoice(SalesOrder order, Customer customer, LocalDate date, InvoiceStatus status) {
        store.createInvoice(new Invoice(0, "INV-" + order.number(), order.id(), customer.id(), date, date.plusDays(30),
                new BigDecimal("5.00"), status));
    }

    private List<String> names(List<Customer> list) {
        return list.stream().map(c -> c.region() + ":" + c.name()).toList();
    }

    @BeforeEach
    void setUp() {
        new CategoryService(store).createCategory("CAT", "Category", null);
        item = store.createProduct(new Product(0, "I-1", "Item", "CAT", new BigDecimal("5.00"), "piece", true));
        Customer anna = customer("Anna", Region.NORTH, CustomerStatus.ACTIVE);
        Customer ben = customer("Ben", Region.NORTH, CustomerStatus.ACTIVE);
        Customer cleo = customer("Cleo", Region.SOUTH, CustomerStatus.ACTIVE);
        Customer dan = customer("Dan", Region.NORTH, CustomerStatus.ON_HOLD);
        Customer eve = customer("Eve", Region.EAST, CustomerStatus.ACTIVE);
        Customer fay = customer("Fay", Region.EAST, CustomerStatus.ACTIVE);
        customer("Gus", Region.WEST, CustomerStatus.ACTIVE);
        Customer hal = customer("Hal", Region.NORTH, CustomerStatus.ACTIVE);
        invoice(order(anna, LocalDate.of(2024, 3, 10)), anna, LocalDate.of(2024, 3, 12), InvoiceStatus.OPEN);
        order(ben, LocalDate.of(2024, 3, 11));
        order(cleo, LocalDate.of(2024, 3, 1));
        order(dan, LocalDate.of(2024, 3, 5));
        invoice(order(eve, LocalDate.of(2024, 3, 6)), eve, LocalDate.of(2024, 3, 8), InvoiceStatus.CANCELLED);
        order(fay, LocalDate.of(2024, 2, 20));
        invoice(order(hal, LocalDate.of(2024, 3, 7)), hal, LocalDate.of(2024, 4, 2), InvoiceStatus.OPEN);
        order(hal, LocalDate.of(2024, 3, 9));
        order(cleo, LocalDate.of(2024, 3, 3));
        orders.cancel("SO-9", CancellationReason.OTHER, LocalDate.of(2024, 3, 4));
    }

    @Test
    void listsActiveCustomersWithOrdersButNoInvoiceInPeriod() {
        List<Customer> result = orders.getCustomersWithoutInvoices(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        assertEquals(List.of("EAST:Eve", "NORTH:Ben", "NORTH:Hal", "SOUTH:Cleo"), names(result));
    }

    @Test
    void cancelledOrdersDoNotCountAndInvoicesOutsidePeriodDoNot() {
        assertEquals(List.of("NORTH:Ben"), names(orders.getCustomersWithoutInvoices(LocalDate.of(2024, 3, 11), LocalDate.of(2024, 3, 11))));
        assertTrue(orders.getCustomersWithoutInvoices(LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 30)).isEmpty());
    }

    @Test
    void rejectsBadInput() {
        LocalDate day = LocalDate.of(2024, 3, 1);
        assertThrows(IllegalArgumentException.class, () -> orders.getCustomersWithoutInvoices(null, day));
        assertThrows(IllegalArgumentException.class, () -> orders.getCustomersWithoutInvoices(day, null));
        assertThrows(IllegalArgumentException.class, () -> orders.getCustomersWithoutInvoices(day.plusDays(1), day));
    }
}
