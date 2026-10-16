package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.service.SupportTicketService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupportTicketTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SupportTicketService service = new SupportTicketService(store);
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final SalesOrder order;

    SupportTicketTest() {
        Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
        order = store.createSalesOrder(new SalesOrder(0, "SO-1", alpha.id(), LocalDate.of(2024, 1, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-2", beta.id(), LocalDate.of(2024, 1, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));
    }

    @Test
    void createWithAndWithoutOrder() {
        SupportTicket withOrder = service.create("T-1", alpha.id(), "SO-1", "Damaged", TicketPriority.HIGH, LocalDate.of(2024, 2, 1));
        SupportTicket without = service.create("T-2", alpha.id(), null, "Question", TicketPriority.LOW, LocalDate.of(2024, 2, 2));
        assertTrue(withOrder.id() > 0);
        assertEquals(order.id(), withOrder.orderId());
        assertEquals(TicketStatus.OPEN, withOrder.status());
        assertNull(withOrder.closedDate());
        assertNull(without.orderId());
        assertEquals(withOrder, store.findSupportTicketByNumber("T-1").orElseThrow());
    }

    @Test
    void statusChangesAndClosedDate() {
        service.create("T-1", alpha.id(), null, "Question", TicketPriority.NORMAL, LocalDate.of(2024, 2, 1));
        assertEquals(TicketStatus.IN_PROGRESS, service.changeStatus("T-1", TicketStatus.IN_PROGRESS, LocalDate.of(2024, 2, 2)).status());
        SupportTicket closed = service.changeStatus("T-1", TicketStatus.CLOSED, LocalDate.of(2024, 2, 3));
        assertEquals(LocalDate.of(2024, 2, 3), closed.closedDate());
        assertEquals(closed, store.findSupportTicketByNumber("T-1").orElseThrow());
        SupportTicket reopened = service.changeStatus("T-1", TicketStatus.OPEN, LocalDate.of(2024, 2, 4));
        assertNull(reopened.closedDate());
        assertNull(store.findSupportTicketByNumber("T-1").orElseThrow().closedDate());
    }

    @Test
    void listsOpenTicketsPerCustomerAndPriority() {
        service.create("T-1", alpha.id(), null, "A", TicketPriority.HIGH, LocalDate.of(2024, 2, 3));
        service.create("T-2", alpha.id(), null, "B", TicketPriority.NORMAL, LocalDate.of(2024, 2, 1));
        service.create("T-3", beta.id(), null, "C", TicketPriority.HIGH, LocalDate.of(2024, 2, 2));
        service.create("T-4", alpha.id(), null, "D", TicketPriority.HIGH, LocalDate.of(2024, 2, 4));
        service.changeStatus("T-2", TicketStatus.IN_PROGRESS, LocalDate.of(2024, 2, 5));
        service.changeStatus("T-4", TicketStatus.CLOSED, LocalDate.of(2024, 2, 5));
        assertEquals(List.of("T-2", "T-1"), service.listOpenByCustomer(alpha.id()).stream().map(SupportTicket::number).toList());
        assertEquals(List.of("T-3", "T-1"), service.listOpenByPriority(TicketPriority.HIGH).stream().map(SupportTicket::number).toList());
        assertTrue(service.listOpenByPriority(TicketPriority.LOW).isEmpty());
    }

    @Test
    void rejectsInvalidInput() {
        service.create("T-1", alpha.id(), null, "A", TicketPriority.HIGH, LocalDate.of(2024, 2, 3));
        LocalDate date = LocalDate.of(2024, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> service.create("T-1", alpha.id(), null, "Dup", TicketPriority.LOW, date));
        assertThrows(IllegalArgumentException.class, () -> service.create("T-9", 999, null, "A", TicketPriority.LOW, date));
        assertThrows(IllegalArgumentException.class, () -> service.create("T-9", alpha.id(), "SO-404", "A", TicketPriority.LOW, date));
        assertThrows(IllegalArgumentException.class, () -> service.create("T-9", alpha.id(), "SO-2", "A", TicketPriority.LOW, date));
        assertThrows(IllegalArgumentException.class, () -> service.create("T-9", alpha.id(), null, " ", TicketPriority.LOW, date));
        assertThrows(IllegalArgumentException.class, () -> service.create("T-9", alpha.id(), null, "A", null, date));
        assertThrows(IllegalArgumentException.class, () -> service.changeStatus("T-404", TicketStatus.CLOSED, date));
        assertThrows(IllegalArgumentException.class, () -> service.changeStatus("T-1", TicketStatus.CLOSED, date.minusDays(1)));
    }

    @Test
    void reportsOldOpenTicketsStatusCountsAndCloseTimes() {
        service.create("T-1", alpha.id(), null, "A", TicketPriority.HIGH, LocalDate.of(2024, 2, 1));
        service.create("T-2", alpha.id(), null, "B", TicketPriority.HIGH, LocalDate.of(2024, 2, 4));
        service.create("T-3", beta.id(), null, "C", TicketPriority.LOW, LocalDate.of(2024, 2, 2));
        service.create("T-4", beta.id(), null, "D", TicketPriority.HIGH, LocalDate.of(2024, 2, 10));
        service.create("T-5", beta.id(), null, "E", TicketPriority.LOW, LocalDate.of(2024, 2, 3));
        service.changeStatus("T-1", TicketStatus.CLOSED, LocalDate.of(2024, 2, 8));
        service.changeStatus("T-3", TicketStatus.CLOSED, LocalDate.of(2024, 2, 5));
        service.changeStatus("T-5", TicketStatus.CLOSED, LocalDate.of(2024, 2, 7));
        service.changeStatus("T-2", TicketStatus.IN_PROGRESS, LocalDate.of(2024, 2, 5));

        LocalDate date = LocalDate.of(2024, 2, 6);
        assertEquals(List.of("T-1", "T-5", "T-2"), numbers(service.listOpenOlderThan(date, 1)));
        assertEquals(List.of("T-1"), numbers(service.listOpenOlderThan(date, 3)));
        assertTrue(service.listOpenOlderThan(date, 5).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> service.listOpenOlderThan(date, -1));

        assertEquals(List.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS, TicketStatus.CLOSED),
                service.countPerStatus().stream().map(c -> c.status()).toList());
        assertEquals(List.of(1, 1, 3), service.countPerStatus().stream().map(c -> c.count()).toList());

        var times = service.averageDaysToClose(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29));
        assertEquals(List.of(TicketPriority.HIGH, TicketPriority.LOW), times.stream().map(t -> t.priority()).toList());
        assertEquals(new BigDecimal("7.00"), times.get(0).averageDays());
        assertEquals(new BigDecimal("3.50"), times.get(1).averageDays());
        assertEquals(2, times.get(1).closedCount());
        var narrow = service.averageDaysToClose(LocalDate.of(2024, 2, 6), LocalDate.of(2024, 2, 29));
        assertEquals(2, narrow.size());
        assertEquals(new BigDecimal("4.00"), narrow.get(1).averageDays());
        assertTrue(service.averageDaysToClose(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31)).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> service.averageDaysToClose(LocalDate.of(2024, 2, 2), LocalDate.of(2024, 2, 1)));
    }

    private static List<String> numbers(List<SupportTicket> tickets) {
        return tickets.stream().map(SupportTicket::number).toList();
    }
}
