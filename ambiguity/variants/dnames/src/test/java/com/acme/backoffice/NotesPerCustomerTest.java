package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderNote;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.service.OrderNoteService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotesPerCustomerTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderNoteService service = new OrderNoteService(store);
    private final Customer alpha;
    private final Customer beta;

    NotesPerCustomerTest() {
        alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-1", alpha.id(), LocalDate.of(2024, 1, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-2", alpha.id(), LocalDate.of(2024, 1, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-3", beta.id(), LocalDate.of(2024, 1, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        service.addNote("SO-1", "anna", LocalDate.of(2024, 3, 1), "a");
        service.addNote("SO-2", "ben", LocalDate.of(2024, 3, 5), "b");
        service.addNote("SO-2", "anna", LocalDate.of(2024, 3, 5), "c");
        service.addNote("SO-3", "anna", LocalDate.of(2024, 3, 9), "other customer");
    }

    @Test
    void notesOfAllOrdersOfCustomerAreListedNewestFirst() {
        assertEquals(List.of("c", "b", "a"), service.listNotesByCustomer(alpha.id()).stream().map(OrderNote::text).toList());
        assertEquals(List.of("other customer"), service.listNotesByCustomer(beta.id()).stream().map(OrderNote::text).toList());
    }

    @Test
    void notesOfAuthorForCustomerAreListedNewestFirst() {
        assertEquals(List.of("c", "a"), service.listNotesByAuthorAndCustomer("anna", alpha.id()).stream().map(OrderNote::text).toList());
        assertEquals(List.of(), service.listNotesByAuthorAndCustomer("ben", beta.id()));
    }
}
