package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionOverview;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.service.RegionManagerService;
import com.acme.backoffice.service.SupportTicketService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegionTicketPriorityTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SupportTicketService tickets = new SupportTicketService(store);
    private final RegionManagerService managers = new RegionManagerService(store);
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer beta = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer west = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    @Test
    void regionOverviewListsHighPriorityTicketsFirst() {
        tickets.create("T-1", alpha.id(), null, "Question", TicketPriority.LOW, day);
        tickets.create("T-2", beta.id(), null, "Invoice copy", TicketPriority.NORMAL, day.plusDays(1));
        tickets.create("T-3", alpha.id(), null, "Broken delivery", TicketPriority.HIGH, day.plusDays(2));
        tickets.create("T-4", beta.id(), null, "Address change", TicketPriority.NORMAL, day.plusDays(3));
        tickets.create("T-5", beta.id(), null, "Wrong items", TicketPriority.HIGH, day.plusDays(4));
        tickets.create("T-6", alpha.id(), null, "Old issue", TicketPriority.HIGH, day.plusDays(5));
        tickets.changeStatus("T-6", TicketStatus.CLOSED, day.plusDays(6));
        tickets.create("T-7", west.id(), null, "Other region", TicketPriority.HIGH, day);
        managers.assign(Region.NORTH, anna.id());

        List<RegionOverview> overview = managers.overviewFor(anna.id(), day.plusDays(10));
        assertEquals(List.of("T-3", "T-5", "T-2", "T-4", "T-1"),
                overview.get(0).openTickets().stream().map(SupportTicket::number).toList());
    }
}
