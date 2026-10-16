package com.acme.backoffice;

import com.acme.backoffice.model.ClosedTicket;
import com.acme.backoffice.model.OrderTickets;
import com.acme.backoffice.model.PriorityTickets;
import com.acme.backoffice.model.ProductTickets;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketCloseTime;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.model.TicketStatusCount;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Customer support tickets: opening and updating them, the open (not closed) tickets of a customer, priority, region,
 * sales rep or product, tickets open on a date, closed tickets and their close times, and ticket counts per status,
 * priority, order and product.
 */
public final class SupportTicketStore {
    /** Every ticket list selects from {@code support_ticket t}. */
    private static final String SELECT_TICKET = "SELECT t.id, t.number, t.customer_id, t.order_id, t.subject, t.priority, "
            + "t.status, t.opened_date, t.closed_date FROM support_ticket t ";
    private static final String JOIN_CUSTOMER = "JOIN customer c ON c.id = t.customer_id ";
    /** A ticket is open when its status is not CLOSED. */
    private static final String OPEN = "t.status <> 'CLOSED'";
    private static final String OLDEST_FIRST = "t.opened_date, t.id";
    /** The ticket was not closed by the end of the date; binds the date. */
    private static final String NOT_CLOSED_BY = "(t.closed_date IS NULL OR t.closed_date > ?)";
    /** The ticket was open on the date: opened on or before it and not closed by then; binds the date twice. */
    private static final String OPEN_ON_DATE = "t.opened_date <= ? AND " + NOT_CLOSED_BY;
    /** The ticket was closed between the two dates (inclusive); binds from, to. */
    private static final String CLOSED_BETWEEN = "t.status = 'CLOSED' AND t.closed_date >= ? AND t.closed_date <= ?";

    private final Jdbc jdbc;

    SupportTicketStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public SupportTicket create(SupportTicket ticket) {
        long id = jdbc.insert("Cannot create support ticket",
                "INSERT INTO support_ticket (number, customer_id, order_id, subject, priority, status, opened_date, closed_date) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                ticket.number(), ticket.customerId(), ticket.orderId(), ticket.subject(), ticket.priority().name(),
                ticket.status().name(), ticket.openedDate(), ticket.closedDate());
        return new SupportTicket(id, ticket.number(), ticket.customerId(), ticket.orderId(), ticket.subject(),
                ticket.priority(), ticket.status(), ticket.openedDate(), ticket.closedDate());
    }

    public Optional<SupportTicket> findByNumber(String number) {
        return jdbc.first("Cannot find support ticket", SELECT_TICKET + "WHERE t.number = ?", Rows::supportTicket, number);
    }

    public void updateStatus(long ticketId, TicketStatus status, LocalDate closedDate) {
        jdbc.update("Cannot update support ticket status", "UPDATE support_ticket SET status = ?, closed_date = ? WHERE id = ?",
                status.name(), closedDate, ticketId);
    }

    /** Names the product a ticket is about. */
    public void updateProduct(long ticketId, Long productId) {
        jdbc.update("Cannot update support ticket product", "UPDATE support_ticket SET product_id = ? WHERE id = ?",
                productId, ticketId);
    }

    /** Tickets of the customer that are not closed, oldest first. */
    public List<SupportTicket> listOpenOfCustomer(long customerId) {
        return listOpen("Cannot list open support tickets by customer", "", "t.customer_id = ?", OLDEST_FIRST, customerId);
    }

    /** Tickets of the priority that are not closed, oldest first. */
    public List<SupportTicket> listOpenWithPriority(TicketPriority priority) {
        return listOpen("Cannot list open support tickets by priority", "", "t.priority = ?", OLDEST_FIRST, priority.name());
    }

    /** Tickets of customers in the region that are not closed, oldest first. */
    public List<SupportTicket> listOpenInRegion(Region region) {
        return listOpen("Cannot list open support tickets by region", JOIN_CUSTOMER, "c.region = ?", OLDEST_FIRST,
                region.name());
    }

    /** Tickets of customers of the sales rep that are not closed, highest priority first, oldest first within a priority. */
    public List<SupportTicket> listOpenOfSalesRep(long salesRepId) {
        return listOpen("Cannot list open support tickets by sales rep", JOIN_CUSTOMER, "c.sales_rep_id = ?",
                "CASE t.priority WHEN 'HIGH' THEN 0 WHEN 'NORMAL' THEN 1 ELSE 2 END, " + OLDEST_FIRST, salesRepId);
    }

    /** Tickets about the product that are not closed, oldest first. */
    public List<SupportTicket> listOpenAboutProduct(long productId) {
        return listOpen("Cannot list open support tickets by product", "", "t.product_id = ?", OLDEST_FIRST, productId);
    }

    /** Tickets of the customer that refer to an order, whatever their status, oldest first. */
    public List<SupportTicket> listWithOrderOfCustomer(long customerId) {
        return list("Cannot list support tickets with order by customer",
                "WHERE t.customer_id = ? AND t.order_id IS NOT NULL ORDER BY " + OLDEST_FIRST, customerId);
    }

    /** High-priority tickets of customers in the region that were open on the date and opened more than the given number of days before it, oldest first. */
    public List<SupportTicket> listHighPriorityInRegionOlderThan(Region region, LocalDate date, int days) {
        // Only "opened before date - days" is checked, not "opened on or before the date" (as in OPEN_ON_DATE): differs
        // for a negative number of days; kept as it was.
        return list("Cannot list old high-priority support tickets by region",
                JOIN_CUSTOMER + "WHERE c.region = ? AND t.priority = 'HIGH' AND t.opened_date < ? AND " + NOT_CLOSED_BY
                        + " ORDER BY " + OLDEST_FIRST,
                region.name(), date.minusDays(days), date);
    }

    /** Tickets that were open on the date (opened on or before it, not closed by then) and opened before the cutoff date, oldest first. */
    public List<SupportTicket> listOpenOnDateOpenedBefore(LocalDate date, LocalDate cutoff) {
        return list("Cannot list support tickets open on date",
                "WHERE " + OPEN_ON_DATE + " AND t.opened_date < ? ORDER BY " + OLDEST_FIRST, date, date, cutoff);
    }

    /** Number of tickets that were open on the date: opened on or before it and not closed by then. */
    public int countOpenOnDate(LocalDate date) {
        return jdbc.queryInt("Cannot count support tickets open on date",
                "SELECT COUNT(*) AS ticket_count FROM support_ticket t WHERE " + OPEN_ON_DATE, date, date);
    }

    /** Tickets closed between the two dates (inclusive) with the days to close, by customer name, then priority, then closing date. */
    public List<ClosedTicket> listClosed(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot list closed support tickets",
                "SELECT t.number, t.customer_id, c.name, t.priority, t.opened_date, t.closed_date, "
                        + "DATEDIFF('DAY', t.opened_date, t.closed_date) AS days_to_close FROM support_ticket t " + JOIN_CUSTOMER
                        + "WHERE " + CLOSED_BETWEEN + " ORDER BY c.name, t.customer_id, t.priority, t.closed_date, t.number",
                rs -> new ClosedTicket(rs.getString("number"), rs.getLong("customer_id"), rs.getString("name"),
                        TicketPriority.valueOf(rs.getString("priority")), rs.getObject("opened_date", LocalDate.class),
                        rs.getObject("closed_date", LocalDate.class), rs.getInt("days_to_close")),
                from, to);
    }

    /**
     * Per priority, the number of tickets closed between the two dates (inclusive) and the average days from opening to
     * closing, rounded to 2 decimals; priorities without such tickets are left out.
     */
    public List<TicketCloseTime> closeTimesPerPriority(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find support ticket close times per priority",
                "SELECT t.priority, COUNT(*) AS closed_count, "
                        + "AVG(CAST(DATEDIFF('DAY', t.opened_date, t.closed_date) AS DECIMAL(12, 4))) AS average_days "
                        + "FROM support_ticket t WHERE " + CLOSED_BETWEEN + " GROUP BY t.priority ORDER BY t.priority",
                rs -> new TicketCloseTime(TicketPriority.valueOf(rs.getString("priority")), rs.getInt("closed_count"),
                        rs.getBigDecimal("average_days").setScale(2, RoundingMode.HALF_UP)),
                from, to);
    }

    /** Per priority, the number of not closed tickets of customers in the region; priorities without such tickets are left out. */
    public List<PriorityTickets> countOpenPerPriorityInRegion(Region region) {
        return jdbc.list("Cannot count open support tickets per priority by region",
                "SELECT t.priority, COUNT(*) AS ticket_count FROM support_ticket t " + JOIN_CUSTOMER
                        + "WHERE c.region = ? AND " + OPEN + " GROUP BY t.priority ORDER BY t.priority",
                rs -> new PriorityTickets(TicketPriority.valueOf(rs.getString("priority")), rs.getInt("ticket_count")),
                region.name());
    }

    /** Number of tickets per current status; statuses without tickets are left out. */
    public List<TicketStatusCount> countPerStatus() {
        return jdbc.list("Cannot count support tickets per status",
                "SELECT status, COUNT(*) AS ticket_count FROM support_ticket GROUP BY status ORDER BY status",
                rs -> new TicketStatusCount(TicketStatus.valueOf(rs.getString("status")), rs.getInt("ticket_count")));
    }

    /** Per product, the number of tickets opened between the two dates (inclusive); products without such tickets are left out. */
    public List<ProductTickets> countPerProduct(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot count support tickets per product",
                "SELECT p.id, p.sku, p.name, COUNT(*) AS ticket_count FROM support_ticket t JOIN product p ON p.id = t.product_id "
                        + "WHERE t.opened_date >= ? AND t.opened_date <= ? GROUP BY p.id, p.sku, p.name ORDER BY p.sku",
                rs -> new ProductTickets(rs.getLong("id"), rs.getString("sku"), rs.getString("name"), rs.getInt("ticket_count")),
                from, to);
    }

    /** Orders with more than one ticket and their number of tickets, most tickets first, then by order number. */
    public List<OrderTickets> listOrdersWithMultipleTickets() {
        return jdbc.list("Cannot find orders with multiple support tickets",
                "SELECT o.id, o.number, o.customer_id, COUNT(*) AS ticket_count FROM support_ticket t "
                        + "JOIN sales_order o ON o.id = t.order_id "
                        + "GROUP BY o.id, o.number, o.customer_id HAVING COUNT(*) > 1 ORDER BY ticket_count DESC, o.number",
                rs -> new OrderTickets(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                        rs.getInt("ticket_count")));
    }

    /** The open tickets matching the filter (after the optional join), in the given order. */
    private List<SupportTicket> listOpen(String error, String join, String filter, String orderBy, Object param) {
        return list(error, join + "WHERE " + filter + " AND " + OPEN + " ORDER BY " + orderBy, param);
    }

    private List<SupportTicket> list(String error, String joinsWhereAndOrder, Object... params) {
        return jdbc.list(error, SELECT_TICKET + joinsWhereAndOrder, Rows::supportTicket, params);
    }
}
