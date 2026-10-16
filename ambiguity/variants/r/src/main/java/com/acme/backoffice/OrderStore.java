package com.acme.backoffice;

import com.acme.backoffice.model.ArchivedYearRevenue;
import com.acme.backoffice.model.CancellationCount;
import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.CancelledOrder;
import com.acme.backoffice.model.CountryOrderCount;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.DraftOrder;
import com.acme.backoffice.model.LargeOrder;
import com.acme.backoffice.model.OrderHistoryEntry;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.OrderStatusChange;
import com.acme.backoffice.model.ProductOrder;
import com.acme.backoffice.model.QuarterOrders;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionSmallOrderShare;
import com.acme.backoffice.model.SalesChannel;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesRepOrderCount;
import com.acme.backoffice.model.SmallOrder;
import com.acme.backoffice.model.WeekdayOrders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sales orders: creating them, finding one by number, the order lists (of a customer, of a period, channel, product,
 * category, carrier or country; open, archived and latest orders, of all customers, of one customer or of the customers
 * of a region), searching, status changes and their history, cancellation and the cancellation reports, stale draft
 * orders, archiving, order totals and order history, large and small orders, and the order count breakdowns (per
 * quarter, weekday, sales rep and country).
 *
 * <p>"Open" means CONFIRMED and not archived. Most lists of whole orders share one query ({@link #listOrders}); the
 * open / archived / latest / history families take a {@link Scope} (all customers, one customer, one region) so each
 * family's conditions and ordering are written once.
 */
public final class OrderStore {
    private static final String CUSTOMER_JOIN = " JOIN customer c ON c.id = o.customer_id";
    private static final String NOT_ARCHIVED = "NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = o.id)";
    /** An open order: confirmed and not archived. */
    private static final String OPEN = "o.status = 'CONFIRMED' AND " + NOT_ARCHIVED;
    private static final String NOT_CANCELLED = "o.status <> 'CANCELLED'";
    private static final String PLACED_BETWEEN = "o.order_date >= ? AND o.order_date <= ?";
    private static final String OLDEST_FIRST = "o.order_date, o.id";
    private static final String NEWEST_FIRST = "o.order_date DESC, o.id DESC";
    /** The customer columns next to the order columns (the customer's status as customer_status). */
    private static final String CUSTOMER_OF_ORDER_COLUMNS =
            "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status";
    private static final String ORDER_VALUE =
            "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order_line l WHERE l.order_id = o.id)";

    /** Whose orders a list covers: all customers, one customer, or the customers of one region. */
    private record Scope(String join, String condition, Object... params) {
        static final Scope ALL = new Scope("", null);

        static Scope ofCustomer(long customerId) {
            return new Scope("", "o.customer_id = ?", customerId);
        }

        static Scope inRegion(Region region) {
            return new Scope(CUSTOMER_JOIN, "c.region = ?", region.name());
        }
    }

    @FunctionalInterface
    private interface ValuedOrderRow<T> {
        T of(SalesOrder order, BigDecimal value, Customer customer);
    }

    private final Jdbc jdbc;
    private final Jdbc.RowMapper<SalesOrder> salesOrder;

    OrderStore(Jdbc jdbc) {
        this.jdbc = jdbc;
        this.salesOrder = Rows.salesOrder(jdbc);
    }

    // ---------------------------------------------------------------- create, find, status

    public SalesOrder create(SalesOrder order) {
        return jdbc.inTransaction("Cannot create sales order", () -> {
            long id = jdbc.insertRaw("INSERT INTO sales_order (number, customer_id, order_date, status) VALUES (?, ?, ?, ?)",
                    order.number(), order.customerId(), order.orderDate(), order.status().name());
            int lineNo = 1;
            for (OrderLine line : order.lines()) {
                jdbc.updateRaw("INSERT INTO sales_order_line (order_id, line_no, product_id, quantity, unit_price, promotion_code) "
                                + "VALUES (?, ?, ?, ?, ?, ?)",
                        id, lineNo++, line.productId(), line.quantity(), line.unitPrice(), line.promotionCode());
            }
            return new SalesOrder(id, order.number(), order.customerId(), order.orderDate(), order.status(),
                    List.copyOf(order.lines()));
        });
    }

    public Optional<SalesOrder> findByNumber(String number) {
        return jdbc.first("Cannot find sales order",
                "SELECT " + Rows.SALES_ORDER_COLUMNS + " FROM sales_order o WHERE o.number = ?", salesOrder, number);
    }

    public void changeStatus(long orderId, OrderStatus status, LocalDate date) {
        jdbc.inTransaction("Cannot change sales order status", () -> {
            jdbc.updateRaw("UPDATE sales_order SET status = ? WHERE id = ?", status.name(), orderId);
            jdbc.updateRaw("INSERT INTO sales_order_status_change (order_id, status, change_date) VALUES (?, ?, ?)",
                    orderId, status.name(), date);
            return null;
        });
    }

    public List<OrderStatusChange> listStatusChanges(long orderId) {
        return jdbc.list("Cannot list sales order status changes",
                "SELECT order_id, status, change_date FROM sales_order_status_change WHERE order_id = ? ORDER BY id",
                rs -> new OrderStatusChange(rs.getLong("order_id"), OrderStatus.valueOf(rs.getString("status")),
                        rs.getObject("change_date", LocalDate.class)), orderId);
    }

    /** Sets (or replaces) the channel the order came from. */
    public void saveChannel(long orderId, SalesChannel channel) {
        jdbc.update("Cannot save order channel", "MERGE INTO order_channel (order_id, channel) KEY (order_id) VALUES (?, ?)",
                orderId, channel.name());
    }

    // ---------------------------------------------------------------- lists of orders

    /** The not archived orders of the customer, newest first. */
    public List<SalesOrder> listOfCustomer(long customerId) {
        return listOrders("Cannot list sales orders", Scope.ofCustomer(customerId), "", NOT_ARCHIVED, NEWEST_FIRST);
    }

    /** Orders placed between the two dates, inclusive, sorted by date. */
    public List<SalesOrder> listByPeriod(LocalDate from, LocalDate to) {
        return listOrders("Cannot list sales orders by period", Scope.ALL, "", PLACED_BETWEEN, OLDEST_FIRST, from, to);
    }

    /** Orders with the given status placed between the two dates, inclusive, sorted by date. */
    public List<SalesOrder> listByPeriodAndStatus(LocalDate from, LocalDate to, OrderStatus status) {
        return listOrders("Cannot list sales orders by period and status", Scope.ALL, "",
                PLACED_BETWEEN + " AND o.status = ?", OLDEST_FIRST, from, to, status.name());
    }

    /** Orders of the channel placed between the two dates, inclusive, sorted by date. */
    public List<SalesOrder> listByChannel(SalesChannel channel, LocalDate from, LocalDate to) {
        return listOrders("Cannot list sales orders by channel", Scope.ALL, " JOIN order_channel oc ON oc.order_id = o.id",
                "oc.channel = ? AND " + PLACED_BETWEEN, OLDEST_FIRST, channel.name(), from, to);
    }

    /**
     * Orders shipped with the carrier between the two dates, inclusive, sorted by order date. An order counts if the carrier
     * shipped it whole or any part of it in the period; each order is listed once.
     */
    public List<SalesOrder> listByCarrierAndPeriod(long carrierId, LocalDate from, LocalDate to) {
        return listOrders("Cannot list sales orders by carrier", Scope.ALL, "",
                "(EXISTS (SELECT 1 FROM shipment s WHERE s.order_id = o.id AND s.carrier_id = ? "
                        + "AND s.ship_date >= ? AND s.ship_date <= ?) "
                        + "OR EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = o.id AND sp.carrier_id = ? "
                        + "AND sp.ship_date >= ? AND sp.ship_date <= ?))",
                OLDEST_FIRST, carrierId, from, to, carrierId, from, to);
    }

    /**
     * The orders of the customers of the country placed between the two dates, inclusive (cancelled orders are left
     * out), oldest first, then by id.
     */
    public List<SalesOrder> listInCountry(String country, LocalDate from, LocalDate to) {
        return listOrders("Cannot find orders by country", Scope.ALL, CUSTOMER_JOIN,
                "c.country = ? AND " + PLACED_BETWEEN + " AND " + NOT_CANCELLED, OLDEST_FIRST, country, from, to);
    }

    /**
     * Orders matching all the given criteria, newest first. Every criterion is optional: null means no restriction.
     * The dates are inclusive. Archived orders are left out.
     */
    public List<SalesOrder> search(Long customerId, OrderStatus status, Region region, LocalDate from, LocalDate to) {
        StringBuilder condition = new StringBuilder(NOT_ARCHIVED);
        List<Object> params = new ArrayList<>();
        if (customerId != null) {
            condition.append(" AND o.customer_id = ?");
            params.add(customerId);
        }
        if (status != null) {
            condition.append(" AND o.status = ?");
            params.add(status.name());
        }
        if (region != null) {
            condition.append(" AND c.region = ?");
            params.add(region.name());
        }
        if (from != null) {
            condition.append(" AND o.order_date >= ?");
            params.add(from);
        }
        if (to != null) {
            condition.append(" AND o.order_date <= ?");
            params.add(to);
        }
        return listOrders("Cannot search sales orders", Scope.ALL, CUSTOMER_JOIN, condition.toString(), NEWEST_FIRST,
                params.toArray());
    }

    // ---------------------------------------------------------------- open orders (confirmed, not archived)

    /** The open orders (confirmed, not archived), oldest first. */
    public List<SalesOrder> listOpen() {
        return listOpen(Scope.ALL);
    }

    /** The open orders (confirmed, not archived) of the customer, oldest first. */
    public List<SalesOrder> listOpenOfCustomer(long customerId) {
        return listOpen(Scope.ofCustomer(customerId));
    }

    /** The open orders (confirmed, not archived) of the customers of the region, oldest first. */
    public List<SalesOrder> listOpenInRegion(Region region) {
        return listOpen(Scope.inRegion(region));
    }

    private List<SalesOrder> listOpen(Scope scope) {
        return listOrders("Cannot list open sales orders", scope, "", OPEN, OLDEST_FIRST);
    }

    /** Number of confirmed, not archived orders placed on or before the date. */
    public int countOpenOnDate(LocalDate date) {
        return jdbc.queryInt("Cannot count open sales orders",
                "SELECT COUNT(*) FROM sales_order o WHERE " + OPEN + " AND o.order_date <= ?", date);
    }

    /** Sum of quantity times unit price over the lines of the orders counted by countOpenOnDate. */
    public BigDecimal findOpenValueOnDate(LocalDate date) {
        return jdbc.single("Cannot find open sales order value",
                "SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) AS total FROM sales_order o "
                        + "JOIN sales_order_line l ON l.order_id = o.id WHERE " + OPEN + " AND o.order_date <= ?",
                rs -> rs.getBigDecimal("total"), date);
    }

    // ---------------------------------------------------------------- latest orders (not cancelled)

    /** The N most recently placed orders (cancelled orders are left out), newest first, then by highest id. */
    public List<SalesOrder> listLatest(int limit) {
        return listLatest("Cannot find latest orders", Scope.ALL, limit);
    }

    /**
     * The N most recently placed orders of customers in the region (cancelled orders are left out), newest first, then
     * by highest id.
     */
    public List<SalesOrder> listLatestInRegion(Region region, int limit) {
        return listLatest("Cannot find latest orders by region", Scope.inRegion(region), limit);
    }

    /**
     * The N most recently placed orders of the customer (cancelled orders are left out), newest first, then by highest
     * id.
     */
    public List<SalesOrder> listLatestOfCustomer(long customerId, int limit) {
        return listLatest("Cannot find latest orders by customer", Scope.ofCustomer(customerId), limit);
    }

    private List<SalesOrder> listLatest(String error, Scope scope, int limit) {
        return listOrders(error, scope, "", NOT_CANCELLED, NEWEST_FIRST + " LIMIT ?", limit);
    }

    // ---------------------------------------------------------------- archiving and archived orders

    /** Archives the delivered or cancelled orders whose last status change is before the cutoff date; returns how many. */
    public int archive(LocalDate cutoff, LocalDate archiveDate) {
        return jdbc.update("Cannot archive sales orders",
                "INSERT INTO order_archive (order_id, archive_date) SELECT o.id, ? FROM sales_order o "
                        + "WHERE o.status IN ('DELIVERED', 'CANCELLED') AND " + NOT_ARCHIVED + " "
                        + "AND COALESCE((SELECT MAX(h.change_date) FROM sales_order_status_change h WHERE h.order_id = o.id "
                        + "AND h.status = o.status), o.order_date) < ?",
                archiveDate, cutoff);
    }

    /** The archived orders, oldest first. */
    public List<SalesOrder> listArchived() {
        return listArchived("Cannot list archived sales orders", Scope.ALL);
    }

    /** The archived orders of the customer, oldest first. */
    public List<SalesOrder> listArchivedOfCustomer(long customerId) {
        return listArchived("Cannot list archived sales orders of customer", Scope.ofCustomer(customerId));
    }

    /** The archived orders of the customers of the region, oldest first. */
    public List<SalesOrder> listArchivedInRegion(Region region) {
        return listArchived("Cannot list archived sales orders of region", Scope.inRegion(region));
    }

    private List<SalesOrder> listArchived(String error, Scope scope) {
        return listOrders(error, scope, " JOIN order_archive ar ON ar.order_id = o.id", null, OLDEST_FIRST);
    }

    /** The revenue of the archived, non-cancelled orders per year of the order date, oldest year first. */
    public List<ArchivedYearRevenue> archivedRevenuePerYear() {
        return jdbc.list("Cannot find archived revenue per year",
                "SELECT YEAR(o.order_date) AS year_no, SUM(l.quantity * l.unit_price) AS revenue FROM sales_order o "
                        + "JOIN order_archive ar ON ar.order_id = o.id JOIN sales_order_line l ON l.order_id = o.id "
                        + "WHERE " + NOT_CANCELLED + " GROUP BY YEAR(o.order_date) ORDER BY year_no",
                rs -> new ArchivedYearRevenue(rs.getInt("year_no"), rs.getBigDecimal("revenue")));
    }

    // ---------------------------------------------------------------- totals and order history

    /** Sum of quantity times unit price over the lines of the order; zero if there is no such order or no lines. */
    public BigDecimal findTotal(String number) {
        return orderLinesTotal("Cannot find sales order total", "o.number = ?", number);
    }

    /** Total value of all orders of the customer that are not cancelled. */
    public BigDecimal findTotalOfCustomer(long customerId) {
        return orderLinesTotal("Cannot find customer order total", "o.customer_id = ? AND " + NOT_CANCELLED, customerId);
    }

    private BigDecimal orderLinesTotal(String error, String condition, Object param) {
        return jdbc.single(error, "SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) AS total FROM sales_order o "
                + "JOIN sales_order_line l ON l.order_id = o.id WHERE " + condition, rs -> rs.getBigDecimal("total"), param);
    }

    /** Every order of the customer, archived and cancelled ones included, oldest first. */
    public List<OrderHistoryEntry> listHistoryOfCustomer(long customerId) {
        return listHistory("Cannot list order history of customer", Scope.ofCustomer(customerId));
    }

    /** Every order of the customers of the region, archived and cancelled ones included, oldest first. */
    public List<OrderHistoryEntry> listHistoryInRegion(Region region) {
        return listHistory("Cannot list order history of region", Scope.inRegion(region));
    }

    private List<OrderHistoryEntry> listHistory(String error, Scope scope) {
        String sql = "SELECT o.number, o.customer_id, o.order_date, o.status, COUNT(l.line_no) AS line_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS total FROM sales_order o" + scope.join()
                + " LEFT JOIN sales_order_line l ON l.order_id = o.id" + where(scope.condition())
                + " GROUP BY o.id, o.number, o.customer_id, o.order_date, o.status ORDER BY " + OLDEST_FIRST;
        return jdbc.list(error, sql, rs -> new OrderHistoryEntry(rs.getString("number"), rs.getLong("customer_id"),
                rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")),
                rs.getInt("line_count"), rs.getBigDecimal("total")), scope.params());
    }

    // ---------------------------------------------------------------- orders of a product or category

    /** The non-cancelled orders containing the product, placed between the two dates, inclusive, newest first. */
    public List<ProductOrder> listByProduct(long productId, LocalDate from, LocalDate to) {
        return listProductOrders("Cannot list orders by product", "",
                "EXISTS (SELECT 1 FROM sales_order_line l WHERE l.order_id = o.id AND l.product_id = ?)", NEWEST_FIRST,
                from, to, productId);
    }

    /**
     * The non-cancelled orders containing the product, placed between the two dates, inclusive.
     * Grouped per customer (customers by name, then id), newest first within a customer.
     */
    public List<ProductOrder> listByProductPerCustomer(long productId, LocalDate from, LocalDate to) {
        return listProductOrders("Cannot list orders by product per customer", "",
                "EXISTS (SELECT 1 FROM sales_order_line l WHERE l.order_id = o.id AND l.product_id = ?)",
                "c.name, c.id, " + NEWEST_FIRST, from, to, productId);
    }

    /**
     * The non-cancelled orders containing a product of the category or of any category below it, placed between the
     * two dates, inclusive, newest first.
     */
    public List<ProductOrder> listByCategory(String category, LocalDate from, LocalDate to) {
        return listProductOrders("Cannot list orders by category",
                "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) ",
                "EXISTS (SELECT 1 FROM sales_order_line l JOIN product p ON p.id = l.product_id "
                        + "WHERE l.order_id = o.id AND p.category IN (SELECT code FROM tree))",
                NEWEST_FIRST, category, from, to);
    }

    /**
     * The non-cancelled orders placed in the period that match the containment condition, with their customer.
     *
     * @param with   a WITH clause put before the SELECT, or empty
     * @param params in SQL order: the WITH clause's, then from and to, then the containment condition's
     */
    private List<ProductOrder> listProductOrders(String error, String with, String contains, String orderBy,
                                                 Object... params) {
        String sql = with + "SELECT " + Rows.SALES_ORDER_COLUMNS + ", " + CUSTOMER_OF_ORDER_COLUMNS
                + " FROM sales_order o" + CUSTOMER_JOIN
                + " WHERE " + NOT_CANCELLED + " AND " + PLACED_BETWEEN + " AND " + contains + " ORDER BY " + orderBy;
        return jdbc.list(error, sql, rs -> new ProductOrder(salesOrder.map(rs), customerOfOrder(rs)), params);
    }

    // ---------------------------------------------------------------- cancellation

    /** Sets the order to CANCELLED, with a status history entry and the cancellation reason, in one transaction. */
    public void cancel(long orderId, CancellationReason reason, LocalDate date) {
        jdbc.inTransaction("Cannot cancel sales order", () -> {
            jdbc.updateRaw("UPDATE sales_order SET status = 'CANCELLED' WHERE id = ?", orderId);
            jdbc.updateRaw("INSERT INTO sales_order_status_change (order_id, status, change_date) VALUES (?, 'CANCELLED', ?)",
                    orderId, date);
            jdbc.updateRaw("INSERT INTO order_cancellation (order_id, reason, cancel_date) VALUES (?, ?, ?)",
                    orderId, reason.name(), date);
            return null;
        });
    }

    /**
     * Number of orders cancelled between the two dates (inclusive), per reason and customer. Sorted by reason, then
     * customer name, then customer id; combinations without cancellations are left out.
     */
    public List<CancellationCount> countCancellationsPerReasonAndCustomer(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find cancellations per reason and customer",
                "SELECT x.reason AS reason, c.id AS id, c.name AS name, COUNT(*) AS order_count "
                        + "FROM order_cancellation x JOIN sales_order o ON o.id = x.order_id" + CUSTOMER_JOIN + " "
                        + "WHERE x.cancel_date >= ? AND x.cancel_date <= ? "
                        + "GROUP BY x.reason, c.id, c.name ORDER BY x.reason, c.name, c.id",
                rs -> new CancellationCount(CancellationReason.valueOf(rs.getString("reason")), rs.getLong("id"),
                        rs.getString("name"), rs.getInt("order_count")), from, to);
    }

    /**
     * The orders cancelled between the two dates, inclusive (by cancellation date), with the customer who placed each.
     * Grouped per customer (customers by name, then id), newest cancellation first within a customer.
     */
    public List<CancelledOrder> listCancelledPerCustomer(LocalDate from, LocalDate to) {
        return listCancelled("Cannot list cancelled orders by customer", "c.name, c.id", from, to);
    }

    /**
     * The orders cancelled between the two dates, inclusive (by cancellation date), with the customer who placed each.
     * Grouped per region of the customer (regions in alphabetical order), newest cancellation first within a region.
     */
    public List<CancelledOrder> listCancelledPerRegion(LocalDate from, LocalDate to) {
        return listCancelled("Cannot list cancelled orders by region", "c.region", from, to);
    }

    private List<CancelledOrder> listCancelled(String error, String groupOrder, LocalDate from, LocalDate to) {
        String sql = "SELECT " + Rows.SALES_ORDER_COLUMNS + ", x.cancel_date, x.reason, " + CUSTOMER_OF_ORDER_COLUMNS
                + " FROM order_cancellation x JOIN sales_order o ON o.id = x.order_id" + CUSTOMER_JOIN
                + " WHERE x.cancel_date >= ? AND x.cancel_date <= ?"
                + " ORDER BY " + groupOrder + ", x.cancel_date DESC, o.id DESC";
        return jdbc.list(error, sql, rs -> new CancelledOrder(salesOrder.map(rs), rs.getObject("cancel_date", LocalDate.class),
                CancellationReason.valueOf(rs.getString("reason")), customerOfOrder(rs)), from, to);
    }

    // ---------------------------------------------------------------- stale draft orders

    /**
     * The draft orders older than the given number of days on the date (order date before the date minus the days),
     * with the customer and the sales rep of the customer. Grouped per customer (customers by name, then id),
     * oldest order first within a customer.
     */
    public List<DraftOrder> listStaleDraftsPerCustomer(LocalDate asOf, int days) {
        return listStaleDrafts("Cannot list stale draft orders by customer", "c.name, c.id", asOf, days);
    }

    /**
     * The draft orders older than the given number of days on the date (order date before the date minus the days),
     * with the customer and the sales rep of the customer. Grouped per sales rep of the customer (reps by name,
     * then id; customers without a rep last), then by customer, oldest order first.
     */
    public List<DraftOrder> listStaleDraftsPerSalesRep(LocalDate asOf, int days) {
        return listStaleDrafts("Cannot list stale draft orders by sales rep", "r.name NULLS LAST, r.id, c.name, c.id",
                asOf, days);
    }

    private List<DraftOrder> listStaleDrafts(String error, String groupOrder, LocalDate asOf, int days) {
        String sql = "SELECT " + Rows.SALES_ORDER_COLUMNS + ", " + CUSTOMER_OF_ORDER_COLUMNS + ", "
                + "r.id AS rep_id, r.name AS rep_name, r.email AS rep_email, r.region AS rep_region "
                + "FROM sales_order o" + CUSTOMER_JOIN + " LEFT JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE o.status = 'DRAFT' AND o.order_date < ? "
                + "ORDER BY " + groupOrder + ", " + OLDEST_FIRST;
        return jdbc.list(error, sql, rs -> {
            SalesOrder order = salesOrder.map(rs);
            SalesRep salesRep = null;
            if (rs.getObject("rep_id") != null) {
                salesRep = new SalesRep(rs.getLong("rep_id"), rs.getString("rep_name"), rs.getString("rep_email"),
                        Region.valueOf(rs.getString("rep_region")));
            }
            return new DraftOrder(order, ChronoUnit.DAYS.between(order.orderDate(), asOf), customerOfOrder(rs), salesRep);
        }, asOf.minusDays(days));
    }

    // ---------------------------------------------------------------- large and small orders

    /**
     * The non-cancelled orders placed between the two dates, inclusive, whose value (quantity times unit price of the
     * lines) is above the given value, with the customer who placed each. Grouped per region of the customer (regions
     * in alphabetical order), biggest order first within a region.
     */
    public List<LargeOrder> listLarge(BigDecimal minValue, LocalDate from, LocalDate to) {
        return listValued("Cannot list large orders", ">", "c.region, order_value DESC, " + OLDEST_FIRST, minValue, from, to,
                LargeOrder::new);
    }

    /**
     * The non-cancelled orders placed between the two dates, inclusive, whose value (quantity times unit price of the
     * lines) is below the given value, with the customer who placed each. Grouped per customer (customers by name, then
     * id), oldest order first within a customer.
     */
    public List<SmallOrder> listSmall(BigDecimal maxValue, LocalDate from, LocalDate to) {
        return listValued("Cannot list small orders", "<", "c.name, c.id, " + OLDEST_FIRST, maxValue, from, to,
                SmallOrder::new);
    }

    private <T> List<T> listValued(String error, String comparison, String orderBy, BigDecimal threshold, LocalDate from,
                                   LocalDate to, ValuedOrderRow<T> row) {
        String sql = "SELECT " + Rows.SALES_ORDER_COLUMNS + ", " + ORDER_VALUE + " AS order_value, "
                + CUSTOMER_OF_ORDER_COLUMNS + " FROM sales_order o" + CUSTOMER_JOIN
                + " WHERE " + PLACED_BETWEEN + " AND " + NOT_CANCELLED + " AND " + ORDER_VALUE + " " + comparison + " ?"
                + " ORDER BY " + orderBy;
        return jdbc.list(error, sql, rs -> row.of(salesOrder.map(rs), rs.getBigDecimal("order_value"), customerOfOrder(rs)),
                from, to, threshold);
    }

    /**
     * Per region of the customer, the number of non-cancelled orders placed between the two dates, inclusive, whose value
     * (quantity times unit price of the lines) is below the given value, the number of all such orders of the region and
     * the small share in percent, rounded to two decimals. Sorted by region; regions without orders in the period are
     * left out.
     */
    public List<RegionSmallOrderShare> smallOrderSharePerRegion(BigDecimal maxValue, LocalDate from, LocalDate to) {
        String sql = "SELECT region, COUNT(*) AS total_count, "
                + "COALESCE(SUM(CASE WHEN order_value < ? THEN 1 ELSE 0 END), 0) AS small_count FROM ("
                + "SELECT c.region AS region, " + ORDER_VALUE + " AS order_value "
                + "FROM sales_order o" + CUSTOMER_JOIN + " WHERE " + PLACED_BETWEEN + " AND " + NOT_CANCELLED + ") "
                + "GROUP BY region ORDER BY region";
        return jdbc.list("Cannot list small order share by region", sql, rs -> {
            int totalCount = rs.getInt("total_count");
            int smallCount = rs.getInt("small_count");
            BigDecimal share = BigDecimal.valueOf(smallCount * 100L)
                    .divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);
            return new RegionSmallOrderShare(Region.valueOf(rs.getString("region")), smallCount, totalCount, share);
        }, maxValue, from, to);
    }

    // ---------------------------------------------------------------- count breakdowns (cancelled orders not counted)

    private static final Breakdowns.Counted ORDERS_COUNTED = new Breakdowns.Counted(
            "FROM sales_order o" + CUSTOMER_JOIN, "o.order_date", " AND " + NOT_CANCELLED, "c.region");

    /**
     * For each of the four quarters of the year, in order: the number of orders placed in the quarter, in total and per
     * region of the customer (cancelled orders are not counted). Quarters and regions without orders are included with
     * zeros.
     */
    public List<QuarterOrders> countPerQuarter(int year) {
        return Breakdowns.perQuarter(jdbc, "Cannot find orders per quarter", ORDERS_COUNTED, year, QuarterOrders::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of orders placed between the two dates, inclusive, in
     * total and per region of the customer (cancelled orders are not counted). Days and regions without orders are
     * included with zeros.
     */
    public List<WeekdayOrders> countPerWeekday(LocalDate from, LocalDate to) {
        return Breakdowns.perWeekday(jdbc, "Cannot find orders per weekday", ORDERS_COUNTED, from, to, WeekdayOrders::new);
    }

    /**
     * The number of orders placed between the two dates, inclusive, by the customers of each sales rep (cancelled
     * orders are not counted), highest first, then by name and id; reps without orders are included with zero.
     */
    public List<SalesRepOrderCount> countPerSalesRep(LocalDate from, LocalDate to) {
        return Breakdowns.perSalesRep(jdbc, "Cannot find order count per sales rep",
                "SELECT COUNT(*) FROM sales_order o" + CUSTOMER_JOIN + " WHERE c.sales_rep_id = r.id AND "
                        + PLACED_BETWEEN + " AND " + NOT_CANCELLED,
                from, to, SalesRepOrderCount::new);
    }

    /**
     * The number of orders placed between the two dates, inclusive, by the customers of each country (cancelled orders
     * are not counted; countries without orders in the period are left out). Sorted by order count, highest first,
     * then by country.
     */
    public List<CountryOrderCount> countPerCountry(LocalDate from, LocalDate to) {
        return Breakdowns.perCountry(jdbc, "Cannot find order count per country",
                "FROM sales_order o" + CUSTOMER_JOIN + " WHERE " + PLACED_BETWEEN + " AND " + NOT_CANCELLED,
                from, to, CountryOrderCount::new);
    }

    // ---------------------------------------------------------------- shared query plumbing

    /**
     * The one query behind every list of whole orders: the order columns, the scope's join and condition, further
     * joins and condition, and the ordering (which may end in {@code LIMIT ?}).
     *
     * @param params in SQL order after the scope's own parameters
     */
    private List<SalesOrder> listOrders(String error, Scope scope, String joins, String condition, String orderBy,
                                        Object... params) {
        String sql = "SELECT " + Rows.SALES_ORDER_COLUMNS + " FROM sales_order o" + scope.join() + joins
                + where(scope.condition(), condition) + " ORDER BY " + orderBy;
        Object[] all = Stream.concat(Arrays.stream(scope.params()), Arrays.stream(params)).toArray();
        return jdbc.list(error, sql, salesOrder, all);
    }

    /** " WHERE a AND b" of the non-null conditions, or empty when there are none. */
    private static String where(String... conditions) {
        String joined = Arrays.stream(conditions).filter(c -> c != null && !c.isEmpty()).collect(Collectors.joining(" AND "));
        return joined.isEmpty() ? "" : " WHERE " + joined;
    }

    /** The customer of an order row selected with {@link #CUSTOMER_OF_ORDER_COLUMNS}; its id is the order's customer_id. */
    private static Customer customerOfOrder(ResultSet rs) throws SQLException {
        return new Customer(rs.getLong("customer_id"), rs.getString("name"), rs.getString("email"),
                rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                CustomerStatus.valueOf(rs.getString("customer_status")));
    }
}
