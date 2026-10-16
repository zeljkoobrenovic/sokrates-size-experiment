package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.service.SupportTicketService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketsPerSalesRepTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SupportTicketService tickets = new SupportTicketService(store);
    private final SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final SalesRep carl = store.createSalesRep(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "USA", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer zeta = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer loner = store.createCustomer(new Customer(0, "Loner", "l@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    TicketsPerSalesRepTest() {
        store.assignCustomerToSalesRep(alpha.id(), anna.id());
        store.assignCustomerToSalesRep(zeta.id(), anna.id());
        store.assignCustomerToSalesRep(loner.id(), carl.id());
    }

    private List<String> numbers(List<SupportTicket> list) {
        return list.stream().map(SupportTicket::number).toList();
    }

    @Test
    void groupsOpenTicketsPerPriorityOldestFirst() {
        tickets.create("T-1", alpha.id(), null, "a", TicketPriority.LOW, day);
        tickets.create("T-2", zeta.id(), null, "b", TicketPriority.HIGH, day.plusDays(5));
        tickets.create("T-3", alpha.id(), null, "c", TicketPriority.HIGH, day.plusDays(2));
        tickets.create("T-4", zeta.id(), null, "d", TicketPriority.NORMAL, day);
        tickets.changeStatus("T-4", TicketStatus.IN_PROGRESS, day.plusDays(1));
        tickets.create("T-5", zeta.id(), null, "e", TicketPriority.NORMAL, day.plusDays(1));
        tickets.changeStatus("T-5", TicketStatus.CLOSED, day.plusDays(2));
        tickets.create("T-6", loner.id(), null, "f", TicketPriority.HIGH, day);
        Map<TicketPriority, List<SupportTicket>> result = tickets.listOpenBySalesRepPerPriority(anna.id());
        assertEquals(List.of(TicketPriority.HIGH, TicketPriority.NORMAL, TicketPriority.LOW), List.copyOf(result.keySet()));
        assertEquals(List.of("T-3", "T-2"), numbers(result.get(TicketPriority.HIGH)));
        assertEquals(List.of("T-4"), numbers(result.get(TicketPriority.NORMAL)));
        assertEquals(List.of("T-1"), numbers(result.get(TicketPriority.LOW)));
    }

    @Test
    void repWithoutOpenTicketsGetsEmptyResult() {
        assertTrue(tickets.listOpenBySalesRepPerPriority(carl.id()).isEmpty());
    }

    @Test
    void rejectsUnknownSalesRep() {
        assertThrows(IllegalArgumentException.class, () -> tickets.listOpenBySalesRepPerPriority(999));
    }
}
