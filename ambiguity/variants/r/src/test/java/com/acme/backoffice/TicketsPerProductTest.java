package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductTickets;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketsPerProductTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final Customer customer = store.customers().create(new Customer(0, "Alpha", "alpha@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.products().create(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final Product gadget = store.products().create(new Product(0, "G-1", "Gadget", "Hardware", new BigDecimal("9.00"), "piece", true));
    private final Product unused = store.products().create(new Product(0, "U-1", "Unused", "Hardware", new BigDecimal("1.00"), "piece", true));

    private SupportTicket ticket(String number, Product product, LocalDate opened, TicketStatus status) {
        SupportTicket ticket = store.supportTickets().create(new SupportTicket(0, number, customer.id(), null, "Subject " + number,
                TicketPriority.NORMAL, status, opened, status == TicketStatus.CLOSED ? opened.plusDays(1) : null));
        if (product != null) {
            store.supportTickets().updateProduct(ticket.id(), product.id());
        }
        return ticket;
    }

    @Test
    void listsOpenTicketsOfProductOldestFirst() {
        ticket("T-2", widget, LocalDate.of(2024, 3, 5), TicketStatus.OPEN);
        ticket("T-1", widget, LocalDate.of(2024, 3, 1), TicketStatus.IN_PROGRESS);
        ticket("T-3", widget, LocalDate.of(2024, 3, 2), TicketStatus.CLOSED);
        ticket("T-4", gadget, LocalDate.of(2024, 3, 3), TicketStatus.OPEN);
        ticket("T-5", null, LocalDate.of(2024, 3, 3), TicketStatus.OPEN);

        assertEquals(List.of("T-1", "T-2"), store.supportTickets().listOpenAboutProduct(widget.id()).stream().map(SupportTicket::number).toList());
        assertTrue(store.supportTickets().listOpenAboutProduct(unused.id()).isEmpty());
    }

    @Test
    void countsTicketsPerProductBetweenDatesInclusive() {
        ticket("T-1", widget, LocalDate.of(2024, 3, 1), TicketStatus.OPEN);
        ticket("T-2", widget, LocalDate.of(2024, 3, 31), TicketStatus.CLOSED);
        ticket("T-3", widget, LocalDate.of(2024, 4, 1), TicketStatus.OPEN);
        ticket("T-4", gadget, LocalDate.of(2024, 3, 15), TicketStatus.OPEN);
        ticket("T-5", null, LocalDate.of(2024, 3, 15), TicketStatus.OPEN);
        ticket("T-6", gadget, LocalDate.of(2024, 2, 29), TicketStatus.OPEN);

        List<ProductTickets> counts = store.supportTickets().countPerProduct(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));

        assertEquals(2, counts.size());
        assertEquals("G-1", counts.get(0).sku());
        assertEquals(1, counts.get(0).ticketCount());
        assertEquals("W-1", counts.get(1).sku());
        assertEquals("Widget", counts.get(1).name());
        assertEquals(2, counts.get(1).ticketCount());
    }
}
