package com.acme.backoffice;

import com.acme.backoffice.model.CategorySpend;
import com.acme.backoffice.model.CountryPurchaseOrderCount;
import com.acme.backoffice.model.LatePurchaseOrder;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.QuarterPurchaseOrders;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.SalesRepPurchaseOrderCount;
import com.acme.backoffice.model.SupplierPurchasedQuantity;
import com.acme.backoffice.model.WeekdayPurchaseOrders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Purchase orders placed with suppliers: creating them, finding and listing them (open, overdue, late, of a supplier,
 * warehouse, product, category or country, latest first, by search criteria), receiving them into a warehouse's stock,
 * and the purchasing figures: purchased quantity per supplier, spend per category and purchase order counts per
 * quarter, weekday, sales rep and country.
 *
 * <p>Purchase orders have no customer of their own; the "per sales rep" and "per country" figures relate a purchase
 * order to the customers in the region of the warehouse it is to be delivered to.
 */
public final class PurchaseOrderStore {
    /** The purchase order columns read by {@link Rows#purchaseOrder}, from {@code purchase_order po}. */
    private static final String SELECT = "SELECT " + Rows.PURCHASE_ORDER_COLUMNS + " FROM purchase_order po ";

    /** Open purchase orders: sent to the supplier and not received yet. */
    private static final String OPEN = "po.status = 'SENT'";
    private static final String NOT_CANCELLED = "po.status <> 'CANCELLED'";

    private static final String EARLIEST_EXPECTED_FIRST = " ORDER BY po.expected_date, po.id";
    private static final String OLDEST_FIRST = " ORDER BY po.order_date, po.id";
    private static final String NEWEST_FIRST = " ORDER BY po.order_date DESC, po.id DESC";

    /** The category and all categories below it, as {@code tree (code)}; one parameter: the category code. */
    private static final String CATEGORY_TREE = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
            + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) ";

    /**
     * Purchase orders into a warehouse ({@code w}) dated between two dates, inclusive (two parameters); a condition on
     * the customers {@code c} in the warehouse's region follows.
     */
    private static final String FOR_WAREHOUSE_REGION_IN_PERIOD = "FROM purchase_order po JOIN warehouse w ON w.id = po.warehouse_id "
            + "WHERE po.order_date >= ? AND po.order_date <= ? ";

    private final Jdbc jdbc;

    PurchaseOrderStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    // ---------------------------------------------------------------- writing

    public PurchaseOrder create(PurchaseOrder order) {
        String orderSql = "INSERT INTO purchase_order (number, supplier_id, order_date, expected_date, status, warehouse_id) VALUES (?, ?, ?, ?, ?, ?)";
        String lineSql = "INSERT INTO purchase_order_line (order_id, line_no, product_id, quantity, unit_cost) VALUES (?, ?, ?, ?, ?)";
        return jdbc.inTransaction("Cannot create purchase order", () -> {
            // a warehouse id of 0 means none and is stored as NULL; status().name() throws on a null status, as before
            long id = jdbc.insertRaw(orderSql, order.number(), order.supplierId(), order.orderDate(), order.expectedDate(),
                    order.status().name(), order.warehouseId() == 0 ? null : order.warehouseId());
            int lineNo = 1;
            for (PurchaseOrderLine line : order.lines()) {
                jdbc.updateRaw(lineSql, id, lineNo++, line.productId(), line.quantity(), line.unitCost());
            }
            return new PurchaseOrder(id, order.number(), order.supplierId(), order.orderDate(), order.expectedDate(),
                    order.status(), List.copyOf(order.lines()), order.warehouseId());
        });
    }

    /**
     * Adds the quantities of a sent purchase order to the stock of the warehouse and marks it as received on the date, all or nothing;
     * returns false, changing nothing, if the purchase order is not sent.
     */
    public boolean receive(long orderId, long warehouseId, LocalDate receivedDate) {
        String orderSql = "UPDATE purchase_order SET status = 'RECEIVED', received_date = ?, received_warehouse_id = ? "
                + "WHERE id = ? AND status = 'SENT'";
        String linesSql = "SELECT product_id, quantity FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        String updateSql = "UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)";
        String movementSql = "INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                + "VALUES (?, ?, ?, ?, 'RECEIPT')";
        return jdbc.inTransaction("Cannot receive purchase order", () -> {
            if (jdbc.updateRaw(orderSql, receivedDate, warehouseId, orderId) == 0) {
                jdbc.connection().rollback();
                return false;
            }
            List<ReceivedLine> lines = jdbc.listRaw(linesSql,
                    rs -> new ReceivedLine(rs.getLong("product_id"), rs.getInt("quantity")), orderId);
            for (ReceivedLine line : lines) {
                if (jdbc.updateRaw(updateSql, line.quantity(), warehouseId, line.productId()) == 0) {
                    jdbc.updateRaw(insertSql, warehouseId, line.productId(), line.quantity());
                }
                jdbc.updateRaw(movementSql, line.productId(), warehouseId, receivedDate, line.quantity());
            }
            return true;
        });
    }

    private record ReceivedLine(long productId, int quantity) {
    }

    // ---------------------------------------------------------------- single purchase orders

    public Optional<PurchaseOrder> findByNumber(String number) {
        return jdbc.first("Cannot find purchase order", SELECT + "WHERE po.number = ?", Rows.purchaseOrder(jdbc), number);
    }

    public Optional<LocalDate> findReceivedDate(long orderId) {
        return jdbc.first("Cannot find purchase order received date", "SELECT received_date FROM purchase_order WHERE id = ?",
                rs -> Optional.ofNullable(rs.getObject("received_date", LocalDate.class)), orderId).flatMap(date -> date);
    }

    // ---------------------------------------------------------------- lists

    /** The purchase orders of the supplier, newest first (then by highest id). */
    public List<PurchaseOrder> listOfSupplier(long supplierId) {
        return list("Cannot list purchase orders", "WHERE po.supplier_id = ?" + NEWEST_FIRST, supplierId);
    }

    /** Open (sent) purchase orders, earliest expected date first (then by id). */
    public List<PurchaseOrder> listOpen() {
        return listOpen("Cannot list open purchase orders", "");
    }

    /** Open (sent) purchase orders of the supplier, earliest expected date first (then by id). */
    public List<PurchaseOrder> listOpenOfSupplier(long supplierId) {
        return listOpen("Cannot list open purchase orders by supplier", " AND po.supplier_id = ?", supplierId);
    }

    /** Open (sent) purchase orders into the warehouse, earliest expected date first (then by id). */
    public List<PurchaseOrder> listOpenForWarehouse(long warehouseId) {
        return listOpen("Cannot list open purchase orders by warehouse", " AND po.warehouse_id = ?", warehouseId);
    }

    /** Sent purchase orders whose expected date is before the given date. */
    public List<PurchaseOrder> listOverdue(LocalDate date) {
        return listOpen("Cannot list overdue purchase orders", " AND po.expected_date < ?", date);
    }

    /** Sent purchase orders into the warehouse whose expected date is before the given date. */
    public List<PurchaseOrder> listOverdueForWarehouse(long warehouseId, LocalDate date) {
        return listOpen("Cannot list overdue purchase orders by warehouse",
                " AND po.warehouse_id = ? AND po.expected_date < ?", warehouseId, date);
    }

    /**
     * The purchase orders matching all the given criteria, earliest expected date first (then by id). Every criterion is
     * optional: a null supplier id, warehouse id, status, from date or to date is not applied. The dates are inclusive
     * and apply to the expected date.
     */
    public List<PurchaseOrder> search(Long supplierId, Long warehouseId, PurchaseOrderStatus status, LocalDate from, LocalDate to) {
        String where = "WHERE (CAST(? AS BIGINT) IS NULL OR po.supplier_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS BIGINT) IS NULL OR po.warehouse_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS VARCHAR) IS NULL OR po.status = CAST(? AS VARCHAR)) "
                + "AND (CAST(? AS DATE) IS NULL OR po.expected_date >= CAST(? AS DATE)) "
                + "AND (CAST(? AS DATE) IS NULL OR po.expected_date <= CAST(? AS DATE))";
        return list("Cannot search purchase orders", where + EARLIEST_EXPECTED_FIRST,
                supplierId, supplierId, warehouseId, warehouseId, status, status, from, from, to, to);
    }

    /** Purchase orders with a line of the product ordered between the two dates, inclusive, oldest first. */
    public List<PurchaseOrder> listOfProductInPeriod(long productId, LocalDate from, LocalDate to) {
        // unlike the category list, cancelled purchase orders are included here
        return list("Cannot list purchase orders of product",
                "WHERE EXISTS (SELECT 1 FROM purchase_order_line l WHERE l.order_id = po.id AND l.product_id = ?) "
                        + "AND po.order_date >= ? AND po.order_date <= ?" + OLDEST_FIRST, productId, from, to);
    }

    /**
     * The non-cancelled purchase orders with a line for a product of the category or of any category below it, ordered
     * between the two dates, inclusive, oldest first.
     */
    public List<PurchaseOrder> listOfCategoryInPeriod(String category, LocalDate from, LocalDate to) {
        String sql = CATEGORY_TREE + SELECT + "WHERE " + NOT_CANCELLED + " AND po.order_date >= ? AND po.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM purchase_order_line l JOIN product p ON p.id = l.product_id "
                + "WHERE l.order_id = po.id AND p.category IN (SELECT code FROM tree))" + OLDEST_FIRST;
        return jdbc.list("Cannot list purchase orders of category", sql, Rows.purchaseOrder(jdbc), category, from, to);
    }

    /**
     * The purchase orders dated between the two dates, inclusive, for the warehouses in the regions of the customers of
     * the country (purchase orders have no customer of their own), oldest first, then by id. A purchase order is listed
     * once, however many customers the country has in the region.
     */
    public List<PurchaseOrder> listForCountry(String country, LocalDate from, LocalDate to) {
        // all statuses, cancelled included, as for the counts per sales rep and per country
        String sql = "SELECT " + Rows.PURCHASE_ORDER_COLUMNS + " " + FOR_WAREHOUSE_REGION_IN_PERIOD
                + "AND EXISTS (SELECT 1 FROM customer c WHERE c.country = ? AND c.region = w.region)" + OLDEST_FIRST;
        return jdbc.list("Cannot find purchase orders by country", sql, Rows.purchaseOrder(jdbc), from, to, country);
    }

    /**
     * The purchase orders received into the warehouse between the two dates, inclusive, after their expected date, with the
     * days late, ordered by receiving date (then by id).
     */
    public List<LatePurchaseOrder> listLateReceivedIntoWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT " + Rows.PURCHASE_ORDER_COLUMNS + ", po.received_date AS received_date, "
                + "DATEDIFF('DAY', po.expected_date, po.received_date) AS days_late FROM purchase_order po "
                + "WHERE po.status = 'RECEIVED' AND po.received_warehouse_id = ? AND po.received_date >= ? AND po.received_date <= ? "
                + "AND po.received_date > po.expected_date ORDER BY po.received_date, po.id";
        Jdbc.RowMapper<PurchaseOrder> order = Rows.purchaseOrder(jdbc);
        return jdbc.list("Cannot list late purchase orders by warehouse", sql,
                rs -> new LatePurchaseOrder(order.map(rs), rs.getObject("received_date", LocalDate.class), rs.getInt("days_late")),
                warehouseId, from, to);
    }

    /**
     * The N most recent purchase orders (cancelled purchase orders are left out), newest first, then by highest id.
     */
    public List<PurchaseOrder> listLatest(int limit) {
        return listLatest("Cannot find latest purchase orders", "", "", limit);
    }

    /**
     * The N most recent purchase orders to be delivered to warehouses in the region (cancelled purchase orders are left
     * out), newest first, then by highest id.
     */
    public List<PurchaseOrder> listLatestInRegion(Region region, int limit) {
        return listLatest("Cannot find latest purchase orders by region", "JOIN warehouse w ON w.id = po.warehouse_id ",
                "w.region = ? AND ", region, limit);
    }

    /**
     * The N most recent purchase orders of the supplier (cancelled purchase orders are left out), newest first, then by
     * highest id.
     */
    public List<PurchaseOrder> listLatestOfSupplier(long supplierId, int limit) {
        return listLatest("Cannot find latest purchase orders by supplier", "", "po.supplier_id = ? AND ", supplierId, limit);
    }

    // ---------------------------------------------------------------- figures

    /**
     * The quantity of the product on purchase orders ordered between the two dates, inclusive, per supplier. Sorted by
     * quantity, highest first (then by supplier name, then id); suppliers without such orders are left out.
     */
    public List<SupplierPurchasedQuantity> purchasedQuantityPerSupplier(long productId, LocalDate from, LocalDate to) {
        // all statuses, cancelled included
        String sql = "SELECT s.id, s.name, s.contact_email, s.country, s.rating, SUM(l.quantity) AS purchased "
                + "FROM supplier s JOIN purchase_order po ON po.supplier_id = s.id "
                + "JOIN purchase_order_line l ON l.order_id = po.id "
                + "WHERE l.product_id = ? AND po.order_date >= ? AND po.order_date <= ? "
                + "GROUP BY s.id, s.name, s.contact_email, s.country, s.rating ORDER BY purchased DESC, s.name, s.id";
        return jdbc.list("Cannot find purchased quantity per supplier", sql,
                rs -> new SupplierPurchasedQuantity(Rows.supplier(rs), rs.getLong("purchased")), productId, from, to);
    }

    /**
     * The spend (quantity times unit cost) of the non-cancelled purchase orders ordered between the two dates,
     * inclusive, on products of the category and of all categories below it, per category of the product. Sorted by
     * spend, highest first (then by category); categories without such spend are left out.
     */
    public List<CategorySpend> spendPerCategory(String category, LocalDate from, LocalDate to) {
        String sql = CATEGORY_TREE + "SELECT p.category AS category, SUM(l.quantity * l.unit_cost) AS spend "
                + "FROM purchase_order po JOIN purchase_order_line l ON l.order_id = po.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE " + NOT_CANCELLED + " AND po.order_date >= ? AND po.order_date <= ? "
                + "AND p.category IN (SELECT code FROM tree) "
                + "GROUP BY p.category ORDER BY spend DESC, p.category";
        return jdbc.list("Cannot find purchase spend per category", sql,
                rs -> new CategorySpend(rs.getString("category"), rs.getBigDecimal("spend")), category, from, to);
    }

    /**
     * For each of the four quarters of the year, in order: the number of purchase orders placed in the quarter, in total
     * and per region of the warehouse they are to be delivered to (cancelled orders are not counted; orders without a
     * warehouse count only in the total). Quarters and regions without orders are included with zeros.
     */
    public List<QuarterPurchaseOrders> countPerQuarter(int year) {
        return Breakdowns.perQuarter(jdbc, "Cannot find purchase orders per quarter", COUNTED, year, QuarterPurchaseOrders::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of purchase orders placed between the two dates, inclusive,
     * in total and per region of the warehouse they are to be delivered to (cancelled orders are not counted; orders
     * without a warehouse count only in the total). Days and regions without orders are included with zeros.
     */
    public List<WeekdayPurchaseOrders> countPerWeekday(LocalDate from, LocalDate to) {
        return Breakdowns.perWeekday(jdbc, "Cannot find purchase orders per weekday", COUNTED, from, to, WeekdayPurchaseOrders::new);
    }

    /** Non-cancelled purchase orders by order date; the region is that of the warehouse they are to be delivered to. */
    private static final Breakdowns.Counted COUNTED = new Breakdowns.Counted("FROM purchase_order po",
            "FROM purchase_order po JOIN warehouse w ON w.id = po.warehouse_id", "po.order_date", " AND " + NOT_CANCELLED,
            "w.region");

    /**
     * The number of purchase orders dated between the two dates, inclusive, for the warehouses in the regions of the
     * customers of each sales rep (purchase orders have no customer of their own), highest first, then by name and id;
     * reps without purchase orders are included with zero. A purchase order counts once per rep.
     */
    public List<SalesRepPurchaseOrderCount> countPerSalesRep(LocalDate from, LocalDate to) {
        String count = "SELECT COUNT(*) " + FOR_WAREHOUSE_REGION_IN_PERIOD
                + "AND EXISTS (SELECT 1 FROM customer c WHERE c.sales_rep_id = r.id AND c.region = w.region)";
        return Breakdowns.perSalesRep(jdbc, "Cannot find purchase order count per sales rep", count, from, to,
                SalesRepPurchaseOrderCount::new);
    }

    /**
     * The number of purchase orders dated between the two dates, inclusive, for the warehouses in the regions of the
     * customers of each country, highest first, then by country; countries without purchase orders are left out. A
     * purchase order counts once per country.
     */
    public List<CountryPurchaseOrderCount> countPerCountry(LocalDate from, LocalDate to) {
        // joining the distinct (country, region) pairs of the customers matches a purchase order at most once per country
        String fromWhere = "FROM purchase_order po JOIN warehouse w ON w.id = po.warehouse_id "
                + "JOIN (SELECT DISTINCT country, region FROM customer) c ON c.region = w.region "
                + "WHERE po.order_date >= ? AND po.order_date <= ?";
        return Breakdowns.perCountry(jdbc, "Cannot find purchase order count per country", fromWhere, from, to,
                CountryPurchaseOrderCount::new);
    }

    // ---------------------------------------------------------------- shared queries

    private List<PurchaseOrder> list(String error, String whereAndOrder, Object... params) {
        return jdbc.list(error, SELECT + whereAndOrder, Rows.purchaseOrder(jdbc), params);
    }

    /** Open (sent) purchase orders matching the further condition, earliest expected date first. */
    private List<PurchaseOrder> listOpen(String error, String condition, Object... params) {
        return list(error, "WHERE " + OPEN + condition + EARLIEST_EXPECTED_FIRST, params);
    }

    /** The latest non-cancelled purchase orders; {@code condition} ends with " AND " and its parameters come before the limit. */
    private List<PurchaseOrder> listLatest(String error, String join, String condition, Object... paramsThenLimit) {
        return list(error, join + "WHERE " + condition + NOT_CANCELLED + NEWEST_FIRST + " LIMIT ?", paramsThenLimit);
    }
}
