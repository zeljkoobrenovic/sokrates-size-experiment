package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.OrderTickets;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.service.SupportTicketService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketsPerOrderTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SupportTicketService tickets = new SupportTicketService(store);
    private final Customer alpha = store.customers().create(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.customers().create(new Customer(0, "Beta", "b@example.com", null, "USA", Region.NORTH, CustomerStatus.ACTIVE));
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    TicketsPerOrderTest() {
        order("SO-1", alpha);
        order("SO-2", alpha);
        order("SO-3", beta);
    }

    private void order(String number, Customer customer) {
        store.orders().create(new SalesOrder(0, number, customer.id(), day, OrderStatus.CONFIRMED, List.of()));
    }

    private List<String> numbers(List<SupportTicket> list) {
        return list.stream().map(SupportTicket::number).toList();
    }

    @Test
    void listsTicketsWithAnOrderOfTheCustomerOldestFirst() {
        tickets.create("T-1", alpha.id(), "SO-2", "a", TicketPriority.LOW, day.plusDays(3));
        tickets.create("T-2", alpha.id(), null, "b", TicketPriority.HIGH, day);
        tickets.create("T-3", alpha.id(), "SO-1", "c", TicketPriority.HIGH, day.plusDays(1));
        tickets.changeStatus("T-3", TicketStatus.CLOSED, day.plusDays(2));
        tickets.create("T-4", beta.id(), "SO-3", "d", TicketPriority.HIGH, day);
        assertEquals(List.of("T-3", "T-1"), numbers(tickets.listWithOrderByCustomer(alpha.id())));
        assertEquals(List.of("T-4"), numbers(tickets.listWithOrderByCustomer(beta.id())));
    }

    @Test
    void findsOrdersWithMoreThanOneTicketMostTicketsFirst() {
        tickets.create("T-1", alpha.id(), "SO-1", "a", TicketPriority.LOW, day);
        tickets.create("T-2", alpha.id(), "SO-1", "b", TicketPriority.LOW, day);
        tickets.create("T-3", alpha.id(), "SO-2", "c", TicketPriority.LOW, day);
        tickets.create("T-4", beta.id(), "SO-3", "d", TicketPriority.LOW, day);
        tickets.create("T-5", beta.id(), "SO-3", "e", TicketPriority.LOW, day);
        tickets.create("T-6", beta.id(), "SO-3", "f", TicketPriority.LOW, day);
        tickets.create("T-7", beta.id(), null, "g", TicketPriority.LOW, day);
        tickets.create("T-8", beta.id(), null, "h", TicketPriority.LOW, day);
        List<OrderTickets> result = tickets.listOrdersWithMultipleTickets();
        assertEquals(List.of("SO-3", "SO-1"), result.stream().map(OrderTickets::orderNumber).toList());
        assertEquals(List.of(3, 2), result.stream().map(OrderTickets::ticketCount).toList());
        assertEquals(beta.id(), result.get(0).customerId());
    }

    @Test
    void emptyWhenNoTickets() {
        assertTrue(tickets.listWithOrderByCustomer(alpha.id()).isEmpty());
        assertTrue(tickets.listOrdersWithMultipleTickets().isEmpty());
    }

    @Test
    void rejectsUnknownCustomer() {
        assertThrows(IllegalArgumentException.class, () -> tickets.listWithOrderByCustomer(999));
    }
}
