package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.CustomerClosureService;
import com.acme.backoffice.service.CustomerSegmentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerClosureTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final CustomerClosureService closures = new CustomerClosureService(store);
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final LocalDate day = LocalDate.of(2026, 8, 1);

    private List<String> names(List<Customer> customers) {
        return customers.stream().map(Customer::name).toList();
    }

    @Test
    void closedCustomerIsHiddenFromListsAndSearches() {
        assertTrue(closures.close(alpha.id(), day));

        assertEquals(List.of("Beta"), names(store.listNotClosedCustomersOrderedByName()));
        assertEquals(List.of(), store.listNotClosedCustomersWithNameContaining("alpha"));
        assertEquals(List.of("Beta"), names(store.listNotClosedCustomersInRegion(Region.WEST)));
        assertEquals(List.of("Beta"), names(store.listNotClosedCustomersInCountry("USA")));
        assertEquals(List.of("Beta"), names(store.listNotClosedCustomersWithStatus(CustomerStatus.ACTIVE)));
        assertEquals(List.of("Alpha"), names(closures.listClosed()));
    }

    @Test
    void closedCustomerIsHiddenFromSegmentsAndDormantReports() {
        closures.close(alpha.id(), day);

        CustomerSegmentService segments = new CustomerSegmentService(store);
        assertEquals(1, segments.countPerSegment(LocalDate.of(2026, 9, 1)).values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(List.of("Beta"), names(store.listActiveCustomersWithoutOrdersBetween(day, day)));
    }

    @Test
    void ordersRemainAfterClosing() {
        Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-1", alpha.id(), LocalDate.of(2026, 3, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));

        closures.close(alpha.id(), day);

        assertEquals(1, store.listNotArchivedSalesOrdersOfCustomerNewestFirst(alpha.id()).size());
        assertTrue(store.findCustomerById(alpha.id()).isPresent());
    }

    @Test
    void reopenedCustomerIsVisibleAgain() {
        closures.close(alpha.id(), day);

        assertTrue(closures.reopen(alpha.id()));
        assertFalse(closures.reopen(alpha.id()));

        assertEquals(List.of("Alpha", "Beta"), names(store.listNotClosedCustomersOrderedByName()));
        assertEquals(List.of(), closures.listClosed());
    }

    @Test
    void closingTwiceReturnsFalse() {
        assertTrue(closures.close(alpha.id(), day));
        assertFalse(closures.close(alpha.id(), day));
    }

    @Test
    void closingUnknownCustomerOrWithoutDateFails() {
        assertThrows(IllegalArgumentException.class, () -> closures.close(999, day));
        assertThrows(IllegalArgumentException.class, () -> closures.close(alpha.id(), null));
    }
}
