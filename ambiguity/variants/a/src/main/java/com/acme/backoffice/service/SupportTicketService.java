package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.ClosedTicket;
import com.acme.backoffice.model.OrderTickets;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketCloseTime;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.model.TicketStatusCount;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Support tickets: raised by a customer, optionally about one of the customer's orders; open until closed. */
public class SupportTicketService {
    private final DataStore store;

    public SupportTicketService(DataStore store) {
        this.store = store;
    }

    /** Creates an open ticket. The order number is optional but, when given, must be an order of the customer. */
    public SupportTicket create(String number, long customerId, String orderNumber, String subject, TicketPriority priority,
            LocalDate date) {
        if (number == null || number.isBlank() || subject == null || subject.isBlank() || priority == null || date == null) {
            throw new IllegalArgumentException("Number, subject, priority and date are required");
        }
        if (store.findCustomerById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer: " + customerId);
        }
        if (store.findSupportTicketByNumber(number.trim()).isPresent()) {
            throw new IllegalArgumentException("Ticket number already used: " + number);
        }
        Long orderId = null;
        if (orderNumber != null) {
            SalesOrder order = store.findSalesOrderByNumber(orderNumber)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + orderNumber));
            if (order.customerId() != customerId) {
                throw new IllegalArgumentException("Order " + orderNumber + " belongs to another customer");
            }
            orderId = order.id();
        }
        return store.createSupportTicket(new SupportTicket(0, number.trim(), customerId, orderId, subject.trim(), priority,
                TicketStatus.OPEN, date, null));
    }

    /** Sets the status. Closing records the date; reopening a closed ticket clears it. */
    public SupportTicket changeStatus(String number, TicketStatus status, LocalDate date) {
        if (status == null || date == null) {
            throw new IllegalArgumentException("Status and date are required");
        }
        SupportTicket ticket = store.findSupportTicketByNumber(number)
                .orElseThrow(() -> new IllegalArgumentException("Unknown ticket: " + number));
        if (status == TicketStatus.CLOSED && date.isBefore(ticket.openedDate())) {
            throw new IllegalArgumentException("A ticket cannot be closed before it was opened");
        }
        LocalDate closedDate = status == TicketStatus.CLOSED ? date : null;
        store.updateSupportTicketStatus(ticket.id(), status, closedDate);
        return new SupportTicket(ticket.id(), ticket.number(), ticket.customerId(), ticket.orderId(), ticket.subject(),
                ticket.priority(), status, ticket.openedDate(), closedDate);
    }

    /** Tickets of the customer that are open or in progress, oldest first. */
    public List<SupportTicket> listOpenByCustomer(long customerId) {
        return store.listOpenSupportTicketsByCustomer(customerId);
    }

    /** Open or in progress tickets of the sales rep's customers per priority, highest priority first, oldest first; priorities without tickets are left out. */
    public Map<TicketPriority, List<SupportTicket>> listOpenBySalesRepPerPriority(long salesRepId) {
        if (store.findSalesRepById(salesRepId).isEmpty()) {
            throw new IllegalArgumentException("Unknown sales rep: " + salesRepId);
        }
        Map<TicketPriority, List<SupportTicket>> perPriority = new LinkedHashMap<>();
        for (SupportTicket ticket : store.listOpenSupportTicketsBySalesRep(salesRepId)) {
            perPriority.computeIfAbsent(ticket.priority(), p -> new ArrayList<>()).add(ticket);
        }
        return perPriority;
    }

    /** Tickets of the customer that refer to an order, whatever their status, oldest first. */
    public List<SupportTicket> listWithOrderByCustomer(long customerId) {
        if (store.findCustomerById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Unknown customer: " + customerId);
        }
        return store.listSupportTicketsWithOrderByCustomer(customerId);
    }

    /** Orders that have more than one ticket, most tickets first, then by order number. */
    public List<OrderTickets> listOrdersWithMultipleTickets() {
        return store.findOrdersWithMultipleSupportTickets();
    }

    /** Tickets of all customers with the priority that are open or in progress, oldest first. */
    public List<SupportTicket> listOpenByPriority(TicketPriority priority) {
        return store.listOpenSupportTicketsByPriority(priority);
    }

    /** Tickets that were open on the date and had been open for more than the given number of days, oldest first. */
    public List<SupportTicket> listOpenOlderThan(LocalDate date, int days) {
        if (date == null || days < 0) {
            throw new IllegalArgumentException("A date and a non-negative number of days are required");
        }
        return store.listSupportTicketsOpenOnDateOpenedBefore(date, date.minusDays(days));
    }

    /** Number of tickets per current status, with a zero for every status that has none, in status order. */
    public List<TicketStatusCount> countPerStatus() {
        List<TicketStatusCount> found = store.findSupportTicketCountsPerStatus();
        List<TicketStatusCount> counts = new ArrayList<>();
        for (TicketStatus status : TicketStatus.values()) {
            int count = found.stream().filter(c -> c.status() == status).mapToInt(TicketStatusCount::count).sum();
            counts.add(new TicketStatusCount(status, count));
        }
        return counts;
    }

    /** Average days to close per priority for tickets closed between the two dates, inclusive, highest priority first. */
    public List<TicketCloseTime> averageDaysToClose(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("A valid period is required");
        }
        List<TicketCloseTime> times = new ArrayList<>(store.findSupportTicketCloseTimesPerPriority(from, to));
        times.sort(Comparator.comparing(TicketCloseTime::priority).reversed());
        return times;
    }

    /** Tickets closed between the two dates, inclusive, by customer name, then highest priority first, then closing date. */
    public List<ClosedTicket> listClosed(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("A valid period is required");
        }
        List<ClosedTicket> tickets = new ArrayList<>(store.listClosedSupportTickets(from, to));
        tickets.sort(Comparator.comparing(ClosedTicket::customerName).thenComparing(ClosedTicket::customerId)
                .thenComparing(ClosedTicket::priority, Comparator.reverseOrder()).thenComparing(ClosedTicket::closedDate)
                .thenComparing(ClosedTicket::number));
        return tickets;
    }
}
