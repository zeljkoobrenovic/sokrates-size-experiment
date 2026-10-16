package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.PriorityTickets;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketsPerRegionTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer north = store.createCustomer(new Customer(0, "North", "n@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer south = store.createCustomer(new Customer(0, "South", "s@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));

    private void ticket(String number, Customer customer, TicketPriority priority, LocalDate opened, LocalDate closed) {
        store.createSupportTicket(new SupportTicket(0, number, customer.id(), null, "Subject " + number, priority,
                closed == null ? TicketStatus.OPEN : TicketStatus.CLOSED, opened, closed));
    }

    @Test
    void countsOpenTicketsPerPriorityOfRegion() {
        ticket("T-1", north, TicketPriority.HIGH, LocalDate.of(2024, 3, 1), null);
        ticket("T-2", north, TicketPriority.HIGH, LocalDate.of(2024, 3, 2), null);
        ticket("T-3", north, TicketPriority.LOW, LocalDate.of(2024, 3, 3), null);
        ticket("T-4", north, TicketPriority.NORMAL, LocalDate.of(2024, 3, 3), LocalDate.of(2024, 3, 4));
        ticket("T-5", south, TicketPriority.NORMAL, LocalDate.of(2024, 3, 3), null);

        List<PriorityTickets> counts = store.countOpenSupportTicketsInRegionPerPriority(Region.NORTH);

        assertEquals(2, counts.size());
        assertEquals(new PriorityTickets(TicketPriority.HIGH, 2), counts.stream().filter(c -> c.priority() == TicketPriority.HIGH).findFirst().orElseThrow());
        assertEquals(new PriorityTickets(TicketPriority.LOW, 1), counts.stream().filter(c -> c.priority() == TicketPriority.LOW).findFirst().orElseThrow());
        assertTrue(store.countOpenSupportTicketsInRegionPerPriority(Region.WEST).isEmpty());
    }

    @Test
    void listsOldHighPriorityTicketsOfRegionOnDate() {
        LocalDate date = LocalDate.of(2024, 4, 30);
        ticket("T-1", north, TicketPriority.HIGH, LocalDate.of(2024, 4, 16), null);
        ticket("T-2", north, TicketPriority.HIGH, LocalDate.of(2024, 4, 15), null);
        ticket("T-3", north, TicketPriority.HIGH, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 20));
        ticket("T-4", north, TicketPriority.HIGH, LocalDate.of(2024, 4, 2), LocalDate.of(2024, 5, 5));
        ticket("T-5", north, TicketPriority.NORMAL, LocalDate.of(2024, 4, 1), null);
        ticket("T-6", south, TicketPriority.HIGH, LocalDate.of(2024, 4, 1), null);

        List<String> numbers = store.listHighPrioritySupportTicketsInRegionOpenOnDateOlderThanDays(Region.NORTH, date, 14).stream()
                .map(SupportTicket::number).toList();

        assertEquals(List.of("T-4", "T-2"), numbers);
    }
}
