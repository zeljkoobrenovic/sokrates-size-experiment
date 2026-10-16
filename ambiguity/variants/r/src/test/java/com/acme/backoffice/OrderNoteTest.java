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
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderNoteTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderNoteService service = new OrderNoteService(store);

    OrderNoteTest() {
        Customer customer = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
        for (String number : List.of("SO-1", "SO-2")) {
            store.orders().create(new SalesOrder(0, number, customer.id(), LocalDate.of(2024, 1, 1), OrderStatus.DRAFT,
                    List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        }
    }

    @Test
    void notesOfOrderAreListedNewestFirst() {
        service.addNote("SO-1", "anna", LocalDate.of(2024, 3, 1), "first");
        service.addNote("SO-1", "ben", LocalDate.of(2024, 3, 5), "third");
        service.addNote("SO-1", "anna", LocalDate.of(2024, 3, 5), "fourth");
        service.addNote("SO-2", "anna", LocalDate.of(2024, 3, 2), "other order");
        assertEquals(List.of("fourth", "third", "first"), service.listNotes("SO-1").stream().map(OrderNote::text).toList());
        assertEquals(List.of("other order"), service.listNotes("SO-2").stream().map(OrderNote::text).toList());
    }

    @Test
    void notesByAuthorAreFilteredByInclusiveDateRange() {
        service.addNote("SO-1", "anna", LocalDate.of(2024, 3, 1), "a");
        service.addNote("SO-2", "anna", LocalDate.of(2024, 3, 10), "b");
        service.addNote("SO-1", "anna", LocalDate.of(2024, 3, 11), "c");
        service.addNote("SO-1", "ben", LocalDate.of(2024, 3, 5), "d");
        List<OrderNote> found = service.listNotesByAuthor("anna", LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 10));
        assertEquals(List.of("b", "a"), found.stream().map(OrderNote::text).toList());
    }

    @Test
    void rejectsInvalidInput() {
        LocalDate date = LocalDate.of(2024, 3, 1);
        assertThrows(IllegalArgumentException.class, () -> service.addNote("SO-9", "anna", date, "x"));
        assertThrows(IllegalArgumentException.class, () -> service.addNote("SO-1", " ", date, "x"));
        assertThrows(IllegalArgumentException.class, () -> service.addNote("SO-1", "anna", date, ""));
        assertThrows(IllegalArgumentException.class, () -> service.listNotes("SO-9"));
        assertThrows(IllegalArgumentException.class, () -> service.listNotesByAuthor("anna", date, date.minusDays(1)));
    }
}
