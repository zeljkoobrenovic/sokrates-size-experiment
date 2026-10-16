package com.acme.backoffice;

import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.service.SupportTicketService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A support ticket records the support agent handling it (none when not yet assigned), and every ticket listing shows
 * it. The new attribute is reached by reflection so that this test compiles against the code without it.
 */
class TicketAssignedAgentTest {
    private final DataStore store = new DataStore(Database.openInMemory());
    private final SalesRep rep = store.createSalesRep(new SalesRep(0, "Rita", "rita@example.com", Region.NORTH));
    private final Customer alpha = store.createCustomer(new Customer(0, "Alpha", "a@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
    private final Product widget = store.createProduct(new Product(0, "W-1", "Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
    private final SalesOrder order = store.createSalesOrder(new SalesOrder(0, "SO-1", alpha.id(), LocalDate.of(2024, 1, 2),
            OrderStatus.CONFIRMED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("10.00")))));

    private static SupportTicket ticket(String number, long customerId, Long orderId, TicketPriority priority, LocalDate opened,
            String agent) {
        try {
            return SupportTicket.class.getConstructor(long.class, String.class, long.class, Long.class, String.class,
                            TicketPriority.class, TicketStatus.class, LocalDate.class, LocalDate.class, String.class)
                    .newInstance(0L, number, customerId, orderId, "Problem " + number, priority, TicketStatus.OPEN, opened, null, agent);
        } catch (NoSuchMethodException e) {
            return fail("A support ticket cannot be assigned to an agent");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String agent(SupportTicket ticket) {
        try {
            Method accessor = SupportTicket.class.getMethod("assignedAgent");
            return (String) accessor.invoke(ticket);
        } catch (NoSuchMethodException e) {
            return fail("A support ticket shows no assigned agent");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static List<String> agents(List<SupportTicket> tickets) {
        return tickets.stream().map(TicketAssignedAgentTest::agent).toList();
    }

    @Test
    void assignedAgentIsShownInEveryTicketListing() {
        store.assignCustomerToSalesRep(alpha.id(), rep.id());
        SupportTicket first = store.createSupportTicket(ticket("T-1", alpha.id(), order.id(), TicketPriority.HIGH,
                LocalDate.of(2024, 1, 3), "Sam"));
        store.createSupportTicket(ticket("T-2", alpha.id(), null, TicketPriority.HIGH, LocalDate.of(2024, 1, 4), null));
        SupportTicket third = store.createSupportTicket(ticket("T-3", alpha.id(), order.id(), TicketPriority.HIGH,
                LocalDate.of(2024, 1, 5), "Kim"));
        store.updateSupportTicketProduct(first.id(), widget.id());
        store.updateSupportTicketProduct(third.id(), widget.id());

        assertEquals("Sam", agent(first));
        assertEquals("Sam", agent(store.findSupportTicketByNumber("T-1").orElseThrow()));
        List<String> all = Arrays.asList("Sam", null, "Kim");
        assertEquals(all, agents(store.listOpenSupportTicketsOfCustomer(alpha.id())));
        assertEquals(all, agents(store.listOpenSupportTicketsWithPriority(TicketPriority.HIGH)));
        assertEquals(all, agents(store.listOpenSupportTicketsInRegion(Region.NORTH)));
        assertEquals(all, agents(store.listOpenSupportTicketsOfSalesRepCustomers(rep.id())));
        assertEquals(List.of("Sam", "Kim"), agents(store.listSupportTicketsReferringToOrderOfCustomer(alpha.id())));
        assertEquals(List.of("Sam", "Kim"), agents(store.listOpenSupportTicketsAboutProduct(widget.id())));
        assertEquals(all, agents(store.listHighPrioritySupportTicketsInRegionOpenOnDateOlderThanDays(Region.NORTH, LocalDate.of(2024, 2, 1), 7)));
        assertEquals(all, agents(store.listSupportTicketsOpenOnDateOpenedBefore(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 1))));
    }

    @Test
    void changingTheStatusKeepsTheAgent() {
        store.createSupportTicket(ticket("T-1", alpha.id(), null, TicketPriority.NORMAL, LocalDate.of(2024, 1, 3), "Sam"));
        SupportTicket inProgress = new SupportTicketService(store).changeStatus("T-1", TicketStatus.IN_PROGRESS, LocalDate.of(2024, 1, 4));
        assertEquals("Sam", agent(inProgress));
        assertEquals("Sam", agent(store.findSupportTicketByNumber("T-1").orElseThrow()));
    }
}
