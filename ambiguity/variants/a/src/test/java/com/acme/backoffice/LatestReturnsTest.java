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
import com.acme.backoffice.service.LatestReturnsService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LatestReturnsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final LatestReturnsService service = new LatestReturnsService(store);
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Customer north = store.createCustomer(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.createCustomer(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
    private final Warehouse warehouse = store.createWarehouse(new Warehouse(0, "WH-1", "Main", "Oslo", Region.NORTH));

    private void ret(String number, Customer customer, LocalDate date) {
        SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-" + number, customer.id(), date, OrderStatus.DELIVERED,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("10.00")))));
        store.createSalesReturn(new SalesReturn(0, number, order.id(), date, "Damaged",
                List.of(new ReturnLine(widget.id(), 1))), warehouse.id());
    }

    private List<String> numbers(List<SalesReturn> returns) {
        return returns.stream().map(SalesReturn::number).toList();
    }

    private void fill() {
        ret("A", north, LocalDate.of(2024, 1, 1));
        ret("B", south, LocalDate.of(2024, 1, 5));
        ret("C", north, LocalDate.of(2024, 1, 3));
        ret("D", south, LocalDate.of(2024, 1, 9));
        ret("E", south, LocalDate.of(2024, 1, 5));
    }

    @Test
    void listsLatestReturnsOverall() {
        fill();
        assertEquals(List.of("D", "E", "B"), numbers(service.overall(3)));
        assertEquals(5, service.overall(10).size());
        assertEquals(1, service.overall(1).get(0).lines().size());
    }

    @Test
    void listsLatestReturnsPerRegion() {
        fill();
        assertEquals(List.of("C", "A"), numbers(service.forRegion(Region.NORTH, 5)));
        assertEquals(List.of("D"), numbers(service.forRegion(Region.SOUTH, 1)));
        assertEquals(List.of(), numbers(service.forRegion(Region.EAST, 5)));
    }

    @Test
    void listsLatestReturnsPerCustomer() {
        fill();
        assertEquals(List.of("D", "E"), numbers(service.forCustomer(south.id(), 2)));
        assertEquals(List.of("C"), numbers(service.forCustomer(north.id(), 1)));
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.overall(0));
        assertThrows(IllegalArgumentException.class, () -> service.forRegion(null, 1));
        assertThrows(IllegalArgumentException.class, () -> service.forCustomer(9999, 1));
    }
}
