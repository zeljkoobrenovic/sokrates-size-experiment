package com.acme.backoffice;

import com.acme.backoffice.model.CarrierShippedWeight;
import com.acme.backoffice.model.CountryShipmentCount;
import com.acme.backoffice.model.QuarterShipments;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionLeadTime;
import com.acme.backoffice.model.RemainingLine;
import com.acme.backoffice.model.SalesRepShipmentCount;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.ShipmentStatus;
import com.acme.backoffice.model.WarehouseDeliveryCount;
import com.acme.backoffice.model.WarehouseShippedQuantity;
import com.acme.backoffice.model.WarehouseShippedWeight;
import com.acme.backoffice.model.WeekdayShipments;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Shipments of sales orders: creating a shipment (whole order) or a shipment part (partial shipment of an order line),
 * the order lines still remaining to ship, recording the delivery date, and finding shipments: in transit (shipped, not
 * yet delivered), late (delivered after the carrier's promised days), of a customer, region, country, product,
 * carrier or warehouse, the latest ones and a free search. Also the shipping reports: shipped quantity and weight,
 * delivery counts per warehouse, lead time per region, and shipment counts per quarter, weekday, sales rep and country.
 */
public final class ShipmentStore {
    /** Every shipment list selects the shipment table as {@code s}. */
    private static final String SELECT = "SELECT " + Rows.SHIPMENT_COLUMNS + " FROM shipment s ";
    private static final String JOIN_ORDER = "JOIN sales_order o ON o.id = s.order_id ";
    private static final String JOIN_ORDER_CUSTOMER = JOIN_ORDER + "JOIN customer cu ON cu.id = o.customer_id ";
    /** Needed by {@link #LATE}. */
    private static final String JOIN_CARRIER = "JOIN carrier c ON c.id = s.carrier_id ";
    private static final String OLDEST_FIRST = " ORDER BY s.ship_date, s.id";
    private static final String NEWEST_FIRST_LIMIT = " ORDER BY s.ship_date DESC, s.id DESC LIMIT ?";
    /** Shipped between the two dates, inclusive (two parameters: from, to). */
    private static final String SHIPPED_IN_PERIOD = "s.ship_date >= ? AND s.ship_date <= ?";

    /** In transit: shipped and not yet delivered. */
    static final String IN_TRANSIT = "s.delivery_date IS NULL";
    /**
     * Late: delivered after more days than the promised days of the carrier. Needs the shipment as {@code s} and its
     * carrier as {@code c}.
     */
    static final String LATE = "s.delivery_date IS NOT NULL AND DATEDIFF('DAY', s.ship_date, s.delivery_date) > c.promised_days";

    private final Jdbc jdbc;

    ShipmentStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    // ---- writes ----

    /** Creates the shipment of a whole order (not yet delivered); returns it with its id. */
    public Shipment create(Shipment shipment) {
        long id = jdbc.insert("Cannot create shipment",
                "INSERT INTO shipment (order_id, warehouse_id, carrier_id, tracking_number, ship_date) VALUES (?, ?, ?, ?, ?)",
                shipment.orderId(), shipment.warehouseId(), shipment.carrierId(), shipment.trackingNumber(),
                shipment.shipDate());
        return new Shipment(id, shipment.orderId(), shipment.warehouseId(), shipment.carrierId(),
                shipment.trackingNumber(), shipment.shipDate());
    }

    /** Records a partial shipment: the given quantity of one line of the order. */
    public void createPart(long orderId, int lineNo, long warehouseId, long carrierId, String trackingNumber,
                           LocalDate shipDate, int quantity) {
        jdbc.update("Cannot create shipment part",
                "INSERT INTO shipment_part (order_id, line_no, warehouse_id, carrier_id, tracking_number, ship_date, quantity) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                orderId, lineNo, warehouseId, carrierId, trackingNumber, shipDate, quantity);
    }

    /**
     * Sets the delivery date of the order's shipment if it is still in transit; true if a shipment was updated.
     */
    public boolean updateDeliveryDate(long orderId, LocalDate deliveryDate) {
        return jdbc.update("Cannot update shipment delivery date",
                "UPDATE shipment SET delivery_date = ? WHERE order_id = ? AND delivery_date IS NULL",
                deliveryDate, orderId) > 0;
    }

    // ---- one shipment, partial shipments ----

    /** The shipment of the order, if any. */
    public Optional<Shipment> findByOrder(long orderId) {
        return jdbc.first("Cannot find shipment", SELECT + "WHERE s.order_id = ?", Rows::shipment, orderId);
    }

    /**
     * The lines of the order not yet fully shipped in shipment parts, in line order, with the ordered, shipped and
     * remaining quantity.
     */
    public List<RemainingLine> listRemainingLines(long orderId) {
        String sql = "SELECT l.line_no, l.product_id, l.quantity, "
                + "COALESCE((SELECT SUM(p.quantity) FROM shipment_part p WHERE p.order_id = l.order_id AND p.line_no = l.line_no), 0) AS shipped "
                + "FROM sales_order_line l WHERE l.order_id = ? ORDER BY l.line_no";
        List<RemainingLine> lines = jdbc.list("Cannot list remaining lines", sql, rs -> {
            int ordered = rs.getInt("quantity");
            int shipped = rs.getInt("shipped");
            return new RemainingLine(rs.getInt("line_no"), rs.getLong("product_id"), ordered, shipped, ordered - shipped);
        }, orderId);
        lines.removeIf(line -> line.shipped() >= line.ordered());
        return lines;
    }

    // ---- shipped in a period, oldest first ----

    /** Shipments from the warehouse shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> listOfWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return oldestFirst("Cannot list shipments", "", "s.warehouse_id = ? AND " + SHIPPED_IN_PERIOD, warehouseId, from, to);
    }

    /** Shipments of the customer's orders shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> listOfCustomer(long customerId, LocalDate from, LocalDate to) {
        return oldestFirst("Cannot list shipments of customer", JOIN_ORDER, "o.customer_id = ? AND " + SHIPPED_IN_PERIOD,
                customerId, from, to);
    }

    /** Shipments of orders with a line of the product shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> listOfProduct(long productId, LocalDate from, LocalDate to) {
        return oldestFirst("Cannot list shipments of product", "",
                "EXISTS (SELECT 1 FROM sales_order_line l WHERE l.order_id = s.order_id AND l.product_id = ?) AND "
                        + SHIPPED_IN_PERIOD, productId, from, to);
    }

    /**
     * The shipments shipped between the two dates, inclusive, for the orders of the customers of the country, oldest
     * first, then by id.
     */
    public List<Shipment> listInCountry(String country, LocalDate from, LocalDate to) {
        return oldestFirst("Cannot find shipments by country", JOIN_ORDER_CUSTOMER,
                "cu.country = ? AND " + SHIPPED_IN_PERIOD, country, from, to);
    }

    // ---- in transit ----

    /** All shipments in transit (not yet delivered), oldest first. */
    public List<Shipment> listInTransit() {
        return oldestFirst("Cannot list shipments in transit", "", IN_TRANSIT);
    }

    /** Shipments of the carrier in transit, oldest first. */
    public List<Shipment> listInTransitOfCarrier(long carrierId) {
        return oldestFirst("Cannot list shipments in transit", "", IN_TRANSIT + " AND s.carrier_id = ?", carrierId);
    }

    /** Shipments in transit shipped before the date (exclusive), oldest first. */
    public List<Shipment> listInTransitShippedBefore(LocalDate shippedBefore) {
        return oldestFirst("Cannot list shipments in transit", "", IN_TRANSIT + " AND s.ship_date < ?", shippedBefore);
    }

    /** Shipments of the customer's orders shipped and not yet delivered, oldest first. */
    public List<Shipment> listInTransitOfCustomer(long customerId) {
        return oldestFirst("Cannot list shipments in transit of customer", JOIN_ORDER,
                "o.customer_id = ? AND " + IN_TRANSIT, customerId);
    }

    /** Shipments to customers of the region shipped between the two dates, inclusive, and not yet delivered, oldest first. */
    public List<Shipment> listInTransitInRegion(Region region, LocalDate from, LocalDate to) {
        return oldestFirst("Cannot list shipments in transit of region", JOIN_ORDER_CUSTOMER,
                "cu.region = ? AND " + SHIPPED_IN_PERIOD + " AND " + IN_TRANSIT, region, from, to);
    }

    // ---- late ----

    /** Delivered shipments of the customer's orders that took more days than the promised days of the carrier, oldest first. */
    public List<Shipment> listLateOfCustomer(long customerId) {
        return oldestFirst("Cannot list late shipments of customer", JOIN_ORDER + JOIN_CARRIER,
                "o.customer_id = ? AND " + LATE, customerId);
    }

    /**
     * Delivered shipments to customers of the region shipped between the two dates, inclusive, that took more days than the
     * promised days of the carrier, oldest first.
     */
    public List<Shipment> listLateInRegion(Region region, LocalDate from, LocalDate to) {
        return oldestFirst("Cannot list late shipments of region", JOIN_ORDER_CUSTOMER + JOIN_CARRIER,
                "cu.region = ? AND " + SHIPPED_IN_PERIOD + " AND " + LATE, region, from, to);
    }

    /**
     * Delivered shipments from the warehouse shipped between the two dates, inclusive, that took more days than the promised days
     * of the carrier, oldest first.
     */
    public List<Shipment> listLateOfWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return oldestFirst("Cannot list late shipments of warehouse", JOIN_CARRIER,
                "s.warehouse_id = ? AND " + SHIPPED_IN_PERIOD + " AND " + LATE, warehouseId, from, to);
    }

    // ---- search ----

    /**
     * The shipments matching all the given criteria, oldest ship date first (then by id). Every criterion is optional:
     * a null carrier id, warehouse id, customer id, status, from date or to date is not applied. The dates are inclusive
     * and apply to the ship date.
     */
    public List<Shipment> search(Long carrierId, Long warehouseId, Long customerId, ShipmentStatus status,
                                 LocalDate from, LocalDate to) {
        String where = "(CAST(? AS BIGINT) IS NULL OR s.carrier_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS BIGINT) IS NULL OR s.warehouse_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS BIGINT) IS NULL OR o.customer_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS VARCHAR) IS NULL OR (CAST(? AS VARCHAR) = 'IN_TRANSIT' AND s.delivery_date IS NULL) "
                + "OR (CAST(? AS VARCHAR) = 'DELIVERED' AND s.delivery_date IS NOT NULL)) "
                + "AND (CAST(? AS DATE) IS NULL OR s.ship_date >= CAST(? AS DATE)) "
                + "AND (CAST(? AS DATE) IS NULL OR s.ship_date <= CAST(? AS DATE))";
        // each optional criterion is bound twice (status three times); a null status binds SQL NULL
        return oldestFirst("Cannot search shipments", JOIN_ORDER, where,
                carrierId, carrierId, warehouseId, warehouseId, customerId, customerId,
                status, status, status, from, from, to, to);
    }

    // ---- latest ----

    /** The N most recent shipments, newest ship date first, then by highest id. */
    public List<Shipment> listLatest(int limit) {
        return newestFirst("Cannot find latest shipments", "", "", limit);
    }

    /** The N most recent shipments of orders of customers in the region, newest ship date first, then by highest id. */
    public List<Shipment> listLatestInRegion(Region region, int limit) {
        return newestFirst("Cannot find latest shipments by region", JOIN_ORDER_CUSTOMER, "WHERE cu.region = ?", region, limit);
    }

    /** The N most recent shipments of orders of the customer, newest ship date first, then by highest id. */
    public List<Shipment> listLatestOfCustomer(long customerId, int limit) {
        return newestFirst("Cannot find latest shipments by customer", JOIN_ORDER, "WHERE o.customer_id = ?", customerId, limit);
    }

    // ---- shipped quantity and weight, deliveries ----

    /**
     * The quantity of the product shipped from each warehouse between the two dates, inclusive, ordered by warehouse code. Orders
     * shipped in one go count the line quantity at the ship date of the shipment; orders shipped in parts count the parts.
     */
    public List<WarehouseShippedQuantity> shippedQuantityOfProductPerWarehouse(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, SUM(q.quantity) AS shipped FROM ("
                + shippedLines("warehouse_id", "%s.quantity AS quantity", "", "l.product_id = ? AND ") + ") q "
                + "JOIN warehouse w ON w.id = q.warehouse_id "
                + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id";
        return jdbc.list("Cannot list shipped quantity of product by warehouse", sql,
                rs -> new WarehouseShippedQuantity(Rows.warehouse(rs), rs.getInt("shipped")),
                productId, from, to, productId, from, to);
    }

    /**
     * The weight in kg shipped from each warehouse between the two dates, inclusive, ordered by warehouse code. Orders shipped in
     * one go count the line quantities at the ship date of the shipment; orders shipped in parts count the parts. Products
     * without a weight count as zero; warehouses that shipped no weight in the period are left out.
     */
    public List<WarehouseShippedWeight> shippedWeightPerWarehouse(LocalDate from, LocalDate to) {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, SUM(q.weight) AS weight FROM ("
                + shippedWeightLines("warehouse_id") + ") q "
                + "JOIN warehouse w ON w.id = q.warehouse_id "
                + "GROUP BY w.id, w.code, w.name, w.city, w.region HAVING SUM(q.weight) > 0 ORDER BY w.code, w.id";
        return jdbc.list("Cannot list shipped weight by warehouse", sql,
                rs -> new WarehouseShippedWeight(Rows.warehouse(rs), rs.getBigDecimal("weight")), from, to, from, to);
    }

    /**
     * The weight in kg shipped by each carrier between the two dates, inclusive, ordered by carrier name. Orders shipped in one
     * go count the line quantities at the ship date of the shipment; orders shipped in parts count the parts. Products without
     * a weight count as zero; carriers that shipped no weight in the period are left out.
     */
    public List<CarrierShippedWeight> shippedWeightPerCarrier(LocalDate from, LocalDate to) {
        String sql = "SELECT " + CarrierStore.CARRIER_COLUMNS + ", SUM(q.weight) AS weight FROM ("
                + shippedWeightLines("carrier_id") + ") q "
                + "JOIN carrier c ON c.id = q.carrier_id "
                + "GROUP BY " + CarrierStore.CARRIER_COLUMNS + " HAVING SUM(q.weight) > 0 "
                + "ORDER BY c.name, c.id";
        return jdbc.list("Cannot list shipped weight by carrier", sql,
                rs -> new CarrierShippedWeight(Rows.carrier(rs), rs.getBigDecimal("weight")), from, to, from, to);
    }

    /**
     * The weight lines (column {@code weight}) shipped between two dates, inclusive, with the given shipment column, for
     * a {@code FROM (...) q} subquery; four parameters: from, to, from, to. Products without a weight are left out (they
     * count as zero).
     */
    private static String shippedWeightLines(String column) {
        return shippedLines(column, "%s.quantity * pw.weight_kg AS weight",
                "JOIN product_weight pw ON pw.product_id = l.product_id ", "");
    }

    /**
     * For each warehouse with delivered shipments shipped between the two dates (inclusive), the number of delivered shipments and
     * how many of them took more days than the promised days of the carrier, ordered by warehouse code.
     */
    public List<WarehouseDeliveryCount> deliveryCountsPerWarehouse(LocalDate from, LocalDate to) {
        // LATE repeats "delivery_date IS NOT NULL", already required by the WHERE clause
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, COUNT(*) AS delivered, "
                + "SUM(CASE WHEN " + LATE + " THEN 1 ELSE 0 END) AS late "
                + "FROM shipment s "
                + "JOIN warehouse w ON w.id = s.warehouse_id "
                + JOIN_CARRIER
                + "WHERE " + SHIPPED_IN_PERIOD + " AND s.delivery_date IS NOT NULL "
                + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id";
        return jdbc.list("Cannot count deliveries by warehouse", sql,
                rs -> new WarehouseDeliveryCount(Rows.warehouse(rs), rs.getInt("delivered"), rs.getInt("late")), from, to);
    }

    // ---- lead time ----

    /**
     * Per region of the customer, the number of delivered orders placed between the two dates, inclusive, and the average
     * days from the order date to the delivery date of the shipment. Orders not yet delivered are left out. Sorted by
     * region; regions without delivered orders in the period are left out.
     */
    public List<RegionLeadTime> leadTimePerRegion(LocalDate from, LocalDate to) {
        String sql = leadTimeQuery("c.region AS region", "JOIN customer c ON c.id = o.customer_id ",
                "GROUP BY c.region ORDER BY c.region");
        return jdbc.list("Cannot list lead time by region", sql,
                rs -> new RegionLeadTime(Region.valueOf(rs.getString("region")), rs.getInt("order_count"),
                        rs.getBigDecimal("average_days").setScale(2, RoundingMode.HALF_UP)), from, to);
    }

    /**
     * The lead time query: per group, {@code order_count} delivered orders placed between two dates, inclusive (two
     * parameters: from, to), and {@code average_days} from order date to delivery date. The order is {@code o}, its
     * shipment {@code s}.
     */
    static String leadTimeQuery(String groupColumns, String join, String groupAndOrder) {
        return "SELECT " + groupColumns + ", COUNT(*) AS order_count, "
                + "AVG(CAST(DATEDIFF('DAY', o.order_date, s.delivery_date) AS DECIMAL(10, 2))) AS average_days "
                + "FROM sales_order o JOIN shipment s ON s.order_id = o.id " + join
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND s.delivery_date IS NOT NULL "
                + groupAndOrder;
    }

    // ---- count breakdowns ----

    private static final String SHIPMENT_CUSTOMER_FROM =
            "FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id";

    /**
     * For each of the four quarters of the year, in order: the number of shipments sent in the quarter, in total and per
     * region of the customer of the order. Quarters and regions without shipments are included with zeros.
     */
    public List<QuarterShipments> countPerQuarter(int year) {
        return Breakdowns.perQuarter(jdbc, "Cannot find shipments per quarter",
                new Breakdowns.Counted(SHIPMENT_CUSTOMER_FROM, "s.ship_date", "", "c.region"), year, QuarterShipments::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of shipments sent between the two dates, inclusive, in
     * total and per region of the customer of the order. Days and regions without shipments are included with zeros.
     */
    public List<WeekdayShipments> countPerWeekday(LocalDate from, LocalDate to) {
        return Breakdowns.perWeekday(jdbc, "Cannot find shipments per weekday",
                new Breakdowns.Counted(SHIPMENT_CUSTOMER_FROM, "s.ship_date", "", "c.region"), from, to, WeekdayShipments::new);
    }

    /**
     * The number of shipments shipped between the two dates, inclusive, for the orders of the customers of each sales
     * rep, highest first, then by name and id; reps without shipments are included with zero.
     */
    public List<SalesRepShipmentCount> countPerSalesRep(LocalDate from, LocalDate to) {
        return Breakdowns.perSalesRep(jdbc, "Cannot find shipment count per sales rep",
                "SELECT COUNT(*) " + SHIPMENT_CUSTOMER_FROM + " WHERE c.sales_rep_id = r.id AND " + SHIPPED_IN_PERIOD,
                from, to, SalesRepShipmentCount::new);
    }

    /**
     * The number of shipments shipped between the two dates, inclusive, for the orders of the customers of each
     * country (countries without shipments in the period are left out). Sorted by shipment count, highest first, then
     * by country.
     */
    public List<CountryShipmentCount> countPerCountry(LocalDate from, LocalDate to) {
        return Breakdowns.perCountry(jdbc, "Cannot find shipment count per country",
                SHIPMENT_CUSTOMER_FROM + " WHERE " + SHIPPED_IN_PERIOD, from, to, CountryShipmentCount::new);
    }

    // ---- shared queries ----

    /** Shipments with the given joins and WHERE condition, oldest ship date first, then by id. */
    private List<Shipment> oldestFirst(String error, String joins, String condition, Object... params) {
        return jdbc.list(error, SELECT + joins + "WHERE " + condition + OLDEST_FIRST, Rows::shipment, params);
    }

    /** The first N shipments with the given joins and WHERE clause (or none), newest ship date first, then by highest id. */
    private List<Shipment> newestFirst(String error, String joins, String where, Object... paramsThenLimit) {
        return jdbc.list(error, SELECT + joins + where + NEWEST_FIRST_LIMIT, Rows::shipment, paramsThenLimit);
    }

    /**
     * The shipped order lines between two dates, inclusive, as a UNION ALL for a {@code FROM (...) q} subquery: orders
     * shipped in one go (no shipment part) count the line at the shipment's ship date, orders shipped in parts count each
     * part at its ship date. Each branch selects the shipment column ({@code s.} or {@code sp.}) and the measure.
     * Parameters per branch: those of {@code condition}, then from and to.
     *
     * @param column    shipment column grouped on, e.g. {@code warehouse_id}
     * @param measure   select expression with {@code %s} for the quantity's table ({@code l} or {@code sp})
     * @param join      further joins on the line {@code l}, or empty
     * @param condition condition ending with {@code " AND "}, or empty
     */
    private static String shippedLines(String column, String measure, String join, String condition) {
        return "SELECT s." + column + ", " + measure.formatted("l") + " FROM shipment s "
                + "JOIN sales_order_line l ON l.order_id = s.order_id " + join
                + "WHERE " + condition + "s.ship_date >= ? AND s.ship_date <= ? "
                + "AND NOT EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = s.order_id) "
                + "UNION ALL "
                + "SELECT sp." + column + ", " + measure.formatted("sp") + " FROM shipment_part sp "
                + "JOIN sales_order_line l ON l.order_id = sp.order_id AND l.line_no = sp.line_no " + join
                + "WHERE " + condition + "sp.ship_date >= ? AND sp.ship_date <= ?";
    }
}
