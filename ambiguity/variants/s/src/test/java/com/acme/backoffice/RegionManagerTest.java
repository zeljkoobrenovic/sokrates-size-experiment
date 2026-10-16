package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionOverview;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.OrderService;
import com.acme.backoffice.service.RegionManagerService;
import com.acme.backoffice.service.SupportTicketService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegionManagerTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final OrderService orders = new OrderService(store);
    private final InvoiceService invoices = new InvoiceService(store);
    private final SupportTicketService tickets = new SupportTicketService(store);
    private final RegionManagerService managers = new RegionManagerService(store);
    private final Customer north = store.createCustomer(new Customer(0, "Beta", "b@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
    private final Customer west = store.createCustomer(new Customer(0, "Zeta", "z@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
    private final SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna", "anna@example.com", Region.NORTH));
    private final SalesRep carl = store.createSalesRep(new SalesRep(0, "Carl", "carl@example.com", Region.WEST));
    private final LocalDate day = LocalDate.of(2024, 6, 1);

    private void order(String number, Customer owner) {
        store.createSalesOrder(new SalesOrder(0, number, owner.id(), day, OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 2, new BigDecimal("4.50")))));
    }

    private void invoice(String number, Customer owner) {
        order(number, owner);
        orders.ship(number, day);
        invoices.invoice(number, day);
    }

    @Test
    void overviewHasOpenOrdersOverdueInvoicesAndOpenTicketsOfTheRegion() {
        order("SO-1", north);
        order("SO-2", west);
        invoice("SO-3", north);
        invoice("SO-4", west);
        tickets.create("T-1", north.id(), null, "Late", TicketPriority.HIGH, day);
        tickets.create("T-2", north.id(), null, "Done", TicketPriority.LOW, day);
        tickets.changeStatus("T-2", TicketStatus.CLOSED, day.plusDays(1));
        tickets.create("T-3", west.id(), null, "Other region", TicketPriority.LOW, day);
        managers.assign(Region.NORTH, anna.id());

        List<RegionOverview> overview = managers.overviewFor(anna.id(), LocalDate.of(2024, 8, 1));
        assertEquals(1, overview.size());
        assertEquals(Region.NORTH, overview.get(0).region());
        assertEquals(List.of("SO-1"), overview.get(0).openOrders().stream().map(SalesOrder::number).toList());
        assertEquals(List.of("INV-0001"), overview.get(0).overdueInvoices().stream().map(Invoice::number).toList());
        assertEquals(List.of("T-1"), overview.get(0).openTickets().stream().map(SupportTicket::number).toList());
    }

    @Test
    void invoicesNotYetDueAreNotListed() {
        invoice("SO-1", north);
        managers.assign(Region.NORTH, anna.id());
        assertTrue(managers.overviewFor(anna.id(), day).get(0).overdueInvoices().isEmpty());
    }

    @Test
    void managerOfSeveralRegionsGetsOneOverviewEach() {
        managers.assign(Region.WEST, anna.id());
        managers.assign(Region.NORTH, anna.id());
        assertEquals(List.of(Region.NORTH, Region.WEST),
                managers.overviewFor(anna.id(), day).stream().map(RegionOverview::region).toList());
    }

    @Test
    void assigningReplacesTheManager() {
        managers.assign(Region.NORTH, anna.id());
        managers.assign(Region.NORTH, carl.id());
        assertEquals("Carl", managers.findManager(Region.NORTH).orElseThrow().name());
        assertTrue(managers.overviewFor(anna.id(), day).isEmpty());
        assertTrue(managers.findManager(Region.SOUTH).isEmpty());
    }

    @Test
    void invalidInputIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> managers.assign(Region.NORTH, 999));
        assertThrows(IllegalArgumentException.class, () -> managers.assign(null, anna.id()));
        assertThrows(IllegalArgumentException.class, () -> managers.overviewFor(999, day));
        assertThrows(IllegalArgumentException.class, () -> managers.overviewFor(anna.id(), null));
    }
}
