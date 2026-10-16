package com.acme.backoffice;

import com.acme.backoffice.model.ClosedTicket;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.service.SupportTicketService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClosedTicketsTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SupportTicketService service = new SupportTicketService(store);
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "beta@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "alpha@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));

    private void closed(String number, Customer customer, TicketPriority priority, LocalDate opened, LocalDate closed) {
        service.create(number, customer.id(), null, "Subject " + number, priority, opened);
        service.changeStatus(number, TicketStatus.CLOSED, closed);
    }

    @Test
    void listsTicketsClosedInPeriodPerCustomerAndPriority() {
        closed("T-1", beta, TicketPriority.HIGH, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 6));
        closed("T-2", alpha, TicketPriority.LOW, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 11));
        closed("T-3", alpha, TicketPriority.HIGH, LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 2));
        closed("T-4", alpha, TicketPriority.HIGH, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29));
        closed("T-5", alpha, TicketPriority.HIGH, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 4, 1));
        closed("T-6", beta, TicketPriority.NORMAL, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        service.create("T-7", beta.id(), null, "Open", TicketPriority.HIGH, LocalDate.of(2024, 3, 1));

        List<ClosedTicket> tickets = service.listClosed(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals(List.of("T-3", "T-2", "T-1", "T-6"), tickets.stream().map(ClosedTicket::number).toList());
        assertEquals(List.of(0, 10, 5, 30), tickets.stream().map(ClosedTicket::daysToClose).toList());
        assertEquals("Alpha", tickets.get(0).customerName());
        assertEquals(alpha.id(), tickets.get(0).customerId());
        assertEquals(TicketPriority.HIGH, tickets.get(0).priority());
        assertEquals(LocalDate.of(2024, 3, 2), tickets.get(0).openedDate());
    }

    @Test
    void rejectsInvalidPeriod() {
        assertThrows(IllegalArgumentException.class, () -> service.listClosed(LocalDate.of(2024, 3, 2), LocalDate.of(2024, 3, 1)));
        assertThrows(IllegalArgumentException.class, () -> service.listClosed(null, LocalDate.of(2024, 3, 1)));
    }
}
