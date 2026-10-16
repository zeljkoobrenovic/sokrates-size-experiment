package com.acme.backoffice;

import com.acme.backoffice.model.CategoryMargin;
import com.acme.backoffice.model.CategoryMonthRevenue;
import com.acme.backoffice.model.ChannelSales;
import com.acme.backoffice.model.CustomerRegionSales;
import com.acme.backoffice.model.CustomerSales;
import com.acme.backoffice.model.FirstTimeCustomer;
import com.acme.backoffice.model.MonthlySales;
import com.acme.backoffice.model.OrderLineMatch;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductMargin;
import com.acme.backoffice.model.ProductSales;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionReport;
import com.acme.backoffice.model.RegionSales;
import com.acme.backoffice.model.RepeatCustomer;
import com.acme.backoffice.model.SalesChannel;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesRepCustomer;
import com.acme.backoffice.model.YearOverYearSales;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Sales analytics over the sales orders: revenue (quantity times unit price of the order lines) and order counts per
 * category and month, region, channel, product and customer; the region report, year-over-year and monthly sales;
 * margins against the last purchase cost; top, repeat and first-time customers; revenue per customer including idle
 * ones; a sales rep's customer report; and the search over order lines.
 *
 * <p>Unless said otherwise, the figures count the non-cancelled orders placed between two dates, inclusive.
 */
public final class SalesReportStore {
    /** Non-cancelled orders {@code o} placed between two dates, inclusive (two parameters: from, to). */
    private static final String ORDERS_IN_PERIOD = "o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED'";

    /** The lines {@code l} of the orders {@code o} with their product {@code p}. */
    private static final String ORDER_LINES_WITH_PRODUCT = "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
            + "JOIN product p ON p.id = l.product_id ";

    /** The category and all categories below it, as {@code tree (code)}; one parameter: the category code. */
    private static final String CATEGORY_TREE = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
            + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) ";

    /** Customers that are not closed. */
    private static final String NOT_CLOSED = "NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id)";

    private final Jdbc jdbc;

    SalesReportStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    // ---------------------------------------------------------------- per category, region, channel, month

    /**
     * The revenue per product category and calendar month of the non-cancelled orders placed between the two dates,
     * inclusive (quantity times unit price of the lines, by the category of the product itself). Only combinations
     * with sales; by category, then month.
     */
    public List<CategoryMonthRevenue> revenuePerCategoryAndMonth(LocalDate from, LocalDate to) {
        String sql = "SELECT p.category AS category, YEAR(o.order_date) AS year_no, MONTH(o.order_date) AS month_no, "
                + "SUM(l.quantity * l.unit_price) AS revenue " + ORDER_LINES_WITH_PRODUCT
                + "WHERE " + ORDERS_IN_PERIOD + " "
                + "GROUP BY p.category, YEAR(o.order_date), MONTH(o.order_date) "
                + "ORDER BY p.category, YEAR(o.order_date), MONTH(o.order_date)";
        return jdbc.list("Cannot find revenue per category and month", sql,
                rs -> new CategoryMonthRevenue(rs.getString("category"),
                        YearMonth.of(rs.getInt("year_no"), rs.getInt("month_no")), rs.getBigDecimal("revenue")),
                from, to);
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, per region of the
     * customer. Sorted by total value, highest first (then by region name); regions without orders are left out.
     */
    public List<RegionSales> salesPerRegion(LocalDate from, LocalDate to) {
        String sql = "SELECT c.region AS region, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS total "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE " + ORDERS_IN_PERIOD + " "
                + "GROUP BY c.region ORDER BY total DESC, c.region";
        return jdbc.list("Cannot find sales per region", sql,
                rs -> new RegionSales(Region.valueOf(rs.getString("region")), rs.getInt("order_count"), rs.getBigDecimal("total")),
                from, to);
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, per sales channel.
     * Sorted by revenue, highest first (then by channel name); channels without orders, and orders without a recorded
     * channel, are left out.
     */
    public List<ChannelSales> salesPerChannel(LocalDate from, LocalDate to) {
        String sql = "SELECT oc.channel AS channel, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM sales_order o JOIN order_channel oc ON oc.order_id = o.id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE " + ORDERS_IN_PERIOD + " "
                + "GROUP BY oc.channel ORDER BY revenue DESC, oc.channel";
        return jdbc.list("Cannot find sales per channel", sql,
                rs -> new ChannelSales(SalesChannel.valueOf(rs.getString("channel")), rs.getInt("order_count"),
                        rs.getBigDecimal("revenue")),
                from, to);
    }

    /**
     * Figures of the region for the period, inclusive: customers of the region, non-cancelled orders placed in the period
     * with their value, open invoices dated in the period with the amount of those due before the end date, shipments
     * shipped, returns made and not closed tickets opened in the period, all for customers of the region.
     */
    public RegionReport findRegionReport(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM customer c WHERE c.region = ?) AS customer_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.region = ? AND " + ORDERS_IN_PERIOD + ") AS order_count, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE c.region = ? AND " + ORDERS_IN_PERIOD + ") AS revenue, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE c.region = ? AND i.invoice_date >= ? AND i.invoice_date <= ? AND i.status = 'OPEN') AS open_invoice_count, "
                + "(SELECT COALESCE(SUM(i.amount), 0) FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE c.region = ? AND i.invoice_date >= ? AND i.invoice_date <= ? AND i.status = 'OPEN' AND i.due_date < ?) AS overdue_amount, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.region = ? AND s.ship_date >= ? AND s.ship_date <= ?) AS shipment_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.region = ? AND r.return_date >= ? AND r.return_date <= ?) AS return_count, "
                + "(SELECT COUNT(*) FROM support_ticket t JOIN customer c ON c.id = t.customer_id "
                + "WHERE c.region = ? AND t.opened_date >= ? AND t.opened_date <= ? AND t.status <> 'CLOSED') AS open_ticket_count";
        return jdbc.single("Cannot find report of region", sql,
                rs -> new RegionReport(region, from, to, rs.getInt("customer_count"), rs.getInt("order_count"),
                        rs.getBigDecimal("revenue"), rs.getInt("open_invoice_count"), rs.getBigDecimal("overdue_amount"),
                        rs.getInt("shipment_count"), rs.getInt("return_count"), rs.getInt("open_ticket_count")),
                region,
                region, from, to,
                region, from, to,
                region, from, to,
                region, from, to, to,
                region, from, to,
                region, from, to,
                region, from, to);
    }

    /**
     * For each of the twelve months of the year, in calendar order: the number and total value of the non-cancelled
     * orders placed in the month, the number of customers created in the month (customers without a created date are
     * not counted), the number of returns dated in the month and the total amount of the credit notes dated in the
     * month. Months without activity are included with zeros.
     */
    public List<MonthlySales> monthlySales(int year) {
        String sql = "SELECT m.x AS month_no,"
                + "(SELECT COUNT(*) FROM sales_order o WHERE YEAR(o.order_date) = ? AND MONTH(o.order_date) = m.x "
                + "AND o.status <> 'CANCELLED') AS order_count, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE YEAR(o.order_date) = ? AND MONTH(o.order_date) = m.x AND o.status <> 'CANCELLED') AS revenue, "
                + "(SELECT COUNT(*) FROM customer c WHERE YEAR(c.created_date) = ? AND MONTH(c.created_date) = m.x) AS new_customers, "
                + "(SELECT COUNT(*) FROM sales_return r WHERE YEAR(r.return_date) = ? AND MONTH(r.return_date) = m.x) AS return_count, "
                + "(SELECT COALESCE(SUM(n.amount), 0) FROM credit_note n WHERE YEAR(n.credit_date) = ? AND MONTH(n.credit_date) = m.x) AS credited "
                + "FROM SYSTEM_RANGE(1, 12) m ORDER BY m.x";
        return jdbc.list("Cannot find monthly sales", sql,
                rs -> new MonthlySales(rs.getInt("month_no"), rs.getInt("order_count"), rs.getBigDecimal("revenue"),
                        rs.getInt("new_customers"), rs.getInt("return_count"), rs.getBigDecimal("credited")),
                year, year, year, year, year);
    }

    // ---------------------------------------------------------------- year over year

    /**
     * Number and total value of the non-cancelled orders placed in the given month and in the same month of the
     * previous year, per region of the customer. Sorted by region name; regions without orders in either month are
     * left out.
     */
    public List<YearOverYearSales> yearOverYearSalesPerRegion(YearMonth month) {
        YearMonth previous = month.minusYears(1);
        String sql = "SELECT c.region AS region, " + YEAR_OVER_YEAR_COLUMNS
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.status <> 'CANCELLED' AND ((o.order_date >= ? AND o.order_date <= ?) OR (o.order_date >= ? AND o.order_date <= ?)) "
                + "GROUP BY c.region ORDER BY c.region";
        return jdbc.list("Cannot find year-over-year sales per region", sql,
                rs -> yearOverYear(rs, Region.valueOf(rs.getString("region"))),
                month.atDay(1), month.atEndOfMonth(), month.atDay(1), month.atEndOfMonth(),
                previous.atDay(1), previous.atEndOfMonth(), previous.atDay(1), previous.atEndOfMonth(),
                month.atDay(1), month.atEndOfMonth(), previous.atDay(1), previous.atEndOfMonth());
    }

    /**
     * Number and total value of the non-cancelled orders placed in the given month and in the same month of the
     * previous year, over all regions. The region of the result is null; months without orders give zeros.
     */
    public YearOverYearSales findYearOverYearSales(YearMonth month) {
        YearMonth previous = month.minusYears(1);
        // no customer join and no date filter here (the per-region query has both); the CASEs select the months
        String sql = "SELECT " + YEAR_OVER_YEAR_COLUMNS
                + "FROM sales_order o LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.status <> 'CANCELLED'";
        return jdbc.single("Cannot find year-over-year sales", sql, rs -> yearOverYear(rs, null),
                month.atDay(1), month.atEndOfMonth(), month.atDay(1), month.atEndOfMonth(),
                previous.atDay(1), previous.atEndOfMonth(), previous.atDay(1), previous.atEndOfMonth());
    }

    /** Count and value in the month (two parameters, twice) and in the previous month (two parameters, twice). */
    private static final String YEAR_OVER_YEAR_COLUMNS =
            "COUNT(DISTINCT CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN o.id END) AS order_count, "
            + "COALESCE(SUM(CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN l.quantity * l.unit_price END), 0) AS total, "
            + "COUNT(DISTINCT CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN o.id END) AS previous_order_count, "
            + "COALESCE(SUM(CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN l.quantity * l.unit_price END), 0) AS previous_total ";

    private static YearOverYearSales yearOverYear(ResultSet rs, Region region) throws SQLException {
        return new YearOverYearSales(region, rs.getInt("order_count"), rs.getBigDecimal("total"),
                rs.getInt("previous_order_count"), rs.getBigDecimal("previous_total"));
    }

    // ---------------------------------------------------------------- per product, margins

    /**
     * Units sold and revenue per product in the non-cancelled orders placed between the two dates, inclusive. Sorted by
     * units sold, highest first (then by revenue, highest first, then by SKU); products without sales are left out.
     */
    public List<ProductSales> salesPerProduct(LocalDate from, LocalDate to) {
        return productSales("Cannot find sales per product", "", "", from, to);
    }

    /**
     * Units sold and revenue per product, as for the sales per product, restricted to the products of the category
     * and of all categories below it.
     */
    public List<ProductSales> salesPerProductInCategory(String category, LocalDate from, LocalDate to) {
        return productSales("Cannot find sales per product in category", CATEGORY_TREE,
                "AND p.category IN (SELECT code FROM tree) ", category, from, to);
    }

    private List<ProductSales> productSales(String error, String with, String condition, Object... params) {
        String sql = with + "SELECT p.id AS id, p.sku AS sku, p.name AS name, SUM(l.quantity) AS units, "
                + "SUM(l.quantity * l.unit_price) AS revenue " + ORDER_LINES_WITH_PRODUCT
                + "WHERE " + ORDERS_IN_PERIOD + " " + condition
                + "GROUP BY p.id, p.sku, p.name ORDER BY units DESC, revenue DESC, p.sku";
        return jdbc.list(error, sql, rs -> new ProductSales(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                rs.getLong("units"), rs.getBigDecimal("revenue")), params);
    }

    /**
     * Revenue and cost of the lines of the non-cancelled orders placed between the two dates (two parameters), the cost
     * being the units times the last purchase cost of the product (latest sent or received purchase order line; 0 if
     * never purchased); the caller adds the GROUP BY.
     */
    private static final String REVENUE_AND_COST = "SUM(l.quantity * l.unit_price) AS revenue, "
            + "SUM(l.quantity * COALESCE("
            + "(SELECT pl.unit_cost FROM purchase_order_line pl JOIN purchase_order po ON po.id = pl.order_id "
            + "WHERE pl.product_id = p.id AND po.status IN ('SENT', 'RECEIVED') "
            + "ORDER BY po.order_date DESC, po.id DESC, pl.line_no DESC LIMIT 1), 0)) AS cost "
            + ORDER_LINES_WITH_PRODUCT + "WHERE " + ORDERS_IN_PERIOD + " ";

    /**
     * Revenue, purchase cost and margin per product in the non-cancelled orders placed between the two dates,
     * inclusive. The cost is the units sold times the last purchase cost (latest sent or received purchase order line;
     * 0 if the product was never purchased), the margin is revenue minus cost. Sorted by margin, lowest first (then by
     * SKU); products without sales are left out.
     */
    public List<ProductMargin> marginPerProduct(LocalDate from, LocalDate to) {
        String sql = "SELECT id, sku, name, revenue, cost FROM (SELECT p.id AS id, p.sku AS sku, p.name AS name, "
                + REVENUE_AND_COST + "GROUP BY p.id, p.sku, p.name) ORDER BY revenue - cost, sku";
        return jdbc.list("Cannot find margin per product", sql, rs -> {
            BigDecimal revenue = rs.getBigDecimal("revenue").setScale(2);
            BigDecimal cost = rs.getBigDecimal("cost").setScale(2);
            return new ProductMargin(rs.getLong("id"), rs.getString("sku"), rs.getString("name"), revenue, cost,
                    revenue.subtract(cost));
        }, from, to);
    }

    /**
     * Revenue, purchase cost, margin and margin percentage per product category in the non-cancelled orders placed
     * between the two dates, inclusive, with cost and margin as for the margin per product. The percentage is the margin
     * as a share of the revenue, rounded to two decimals (0 if the revenue is 0). Sorted by margin, lowest first (then by
     * category); categories without sales are left out.
     */
    public List<CategoryMargin> marginPerCategory(LocalDate from, LocalDate to) {
        return categoryMargins("Cannot find margin per category", "", from, to);
    }

    /**
     * The categories of the margin per category whose margin percentage is below the given percentage (compared
     * exactly, not after rounding), in the same order.
     */
    public List<CategoryMargin> categoriesBelowMargin(BigDecimal percent, LocalDate from, LocalDate to) {
        return categoryMargins("Cannot find categories below margin", "WHERE (revenue - cost) * 100 < ? * revenue ",
                from, to, percent);
    }

    private List<CategoryMargin> categoryMargins(String error, String filter, Object... params) {
        String sql = "SELECT category, revenue, cost FROM (SELECT p.category AS category, "
                + REVENUE_AND_COST + "GROUP BY p.category) " + filter + "ORDER BY revenue - cost, category";
        return jdbc.list(error, sql, rs -> {
            BigDecimal revenue = rs.getBigDecimal("revenue").setScale(2);
            BigDecimal cost = rs.getBigDecimal("cost").setScale(2);
            BigDecimal margin = revenue.subtract(cost);
            BigDecimal marginPercent = revenue.signum() == 0 ? new BigDecimal("0.00")
                    : margin.multiply(new BigDecimal("100")).divide(revenue, 2, RoundingMode.HALF_UP);
            return new CategoryMargin(rs.getString("category"), revenue, cost, margin, marginPercent);
        }, params);
    }

    // ---------------------------------------------------------------- order lines

    /**
     * The sales order lines matching all the given criteria, newest order first (then by order number and line number).
     * Every criterion is optional: a null product id, category, customer id, from date, to date or minimum quantity is
     * not applied. The category also matches the categories below it; the dates are inclusive and apply to the order date.
     */
    public List<OrderLineMatch> searchOrderLines(Long productId, String category, Long customerId, LocalDate from,
                                                 LocalDate to, Integer minQuantity) {
        // all statuses, cancelled orders included
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = CAST(? AS VARCHAR) "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT o.id AS order_id, o.number, o.order_date, c.id AS customer_id, c.name AS customer_name, l.line_no, "
                + "p.id, p.sku, p.name AS product_name, p.category, p.unit_price AS product_price, p.unit_of_measure, p.active, "
                + "l.quantity, l.unit_price, l.promotion_code "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id JOIN product p ON p.id = l.product_id "
                + "JOIN customer c ON c.id = o.customer_id "
                + "WHERE (CAST(? AS BIGINT) IS NULL OR l.product_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS VARCHAR) IS NULL OR p.category = CAST(? AS VARCHAR) OR p.category IN (SELECT code FROM tree)) "
                + "AND (CAST(? AS BIGINT) IS NULL OR o.customer_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS DATE) IS NULL OR o.order_date >= CAST(? AS DATE)) "
                + "AND (CAST(? AS DATE) IS NULL OR o.order_date <= CAST(? AS DATE)) "
                + "AND (CAST(? AS INT) IS NULL OR l.quantity >= CAST(? AS INT)) "
                + "ORDER BY o.order_date DESC, o.number DESC, o.id DESC, l.line_no";
        return jdbc.list("Cannot search order lines", sql,
                rs -> new OrderLineMatch(rs.getLong("order_id"), rs.getString("number"),
                        rs.getObject("order_date", LocalDate.class), rs.getLong("customer_id"),
                        rs.getString("customer_name"), rs.getInt("line_no"),
                        new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("product_name"),
                                rs.getString("category"), rs.getBigDecimal("product_price"),
                                rs.getString("unit_of_measure"), rs.getBoolean("active")),
                        rs.getInt("quantity"), rs.getBigDecimal("unit_price"), rs.getString("promotion_code")),
                category, productId, productId, category, category, customerId, customerId, from, from, to, to,
                minQuantity, minQuantity);
    }

    // ---------------------------------------------------------------- per customer

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, per customer. Sorted
     * by revenue, highest first (then by customer name, then id); customers without orders are left out.
     */
    public List<CustomerSales> salesPerCustomer(LocalDate from, LocalDate to) {
        return customerSales("Cannot find sales per customer", "", "", from, to);
    }

    /**
     * The customers of the region with the highest revenue, as for the sales per customer, at most the given number of
     * them.
     */
    public List<CustomerSales> topCustomersInRegion(Region region, int limit, LocalDate from, LocalDate to) {
        return customerSales("Cannot find top customers in region", "AND c.region = ? ", " LIMIT ?", from, to, region, limit);
    }

    private List<CustomerSales> customerSales(String error, String condition, String limit, Object... params) {
        String sql = "SELECT c.id AS id, c.name AS name, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE " + ORDERS_IN_PERIOD + " " + condition
                + "GROUP BY c.id, c.name ORDER BY revenue DESC, c.name, c.id" + limit;
        return jdbc.list(error, sql, SalesReportStore::customerSalesRow, params);
    }

    private static CustomerSales customerSalesRow(ResultSet rs) throws SQLException {
        return new CustomerSales(rs.getLong("id"), rs.getString("name"), rs.getInt("order_count"), rs.getBigDecimal("revenue"));
    }

    /**
     * Every customer that is not closed, left-joined to its non-cancelled orders placed between the two dates (two
     * parameters) and their lines; the caller adds the GROUP BY.
     */
    private static final String NOT_CLOSED_CUSTOMERS_WITH_ORDERS_IN_PERIOD = "FROM customer c LEFT JOIN sales_order o ON o.customer_id = c.id "
            + "AND " + ORDERS_IN_PERIOD + " "
            + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
            + "WHERE " + NOT_CLOSED + " ";

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, for every customer,
     * as for the sales per customer, but customers without orders are included with revenue 0. Sorted by revenue,
     * highest first (then by customer name, then id).
     */
    public List<CustomerSales> revenuePerCustomerIncludingIdle(LocalDate from, LocalDate to) {
        // closed customers are left out here too, although this javadoc says "every customer"
        String sql = "SELECT c.id AS id, c.name AS name, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue " + NOT_CLOSED_CUSTOMERS_WITH_ORDERS_IN_PERIOD
                + "GROUP BY c.id, c.name ORDER BY revenue DESC, c.name, c.id";
        return jdbc.list("Cannot find revenue per customer", sql, SalesReportStore::customerSalesRow, from, to);
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, for every customer
     * that is not closed, with the region of the customer; customers without orders are included with revenue 0.
     * Sorted by revenue, highest first (then by customer name, then id).
     */
    public List<CustomerRegionSales> revenuePerCustomerWithRegionIncludingIdle(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue " + NOT_CLOSED_CUSTOMERS_WITH_ORDERS_IN_PERIOD
                + "GROUP BY c.id, c.name, c.region ORDER BY revenue DESC, c.name, c.id";
        return jdbc.list("Cannot find revenue per customer with region", sql,
                rs -> new CustomerRegionSales(rs.getLong("id"), rs.getString("name"),
                        Region.valueOf(rs.getString("region")), rs.getInt("order_count"), rs.getBigDecimal("revenue")),
                from, to);
    }

    /**
     * The customers with at least the given number of non-cancelled orders placed between the two dates, inclusive,
     * with their order counts. Grouped per region of the customer (regions in alphabetical order), most orders first
     * within a region (then by customer name, then id).
     */
    public List<RepeatCustomer> repeatCustomersByRegion(int minOrders, LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, COUNT(DISTINCT o.id) AS order_count "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE " + ORDERS_IN_PERIOD + " "
                + "GROUP BY c.id, c.name, c.region HAVING COUNT(DISTINCT o.id) >= ? "
                + "ORDER BY c.region, order_count DESC, c.name, c.id";
        return jdbc.list("Cannot find repeat customers by region", sql,
                rs -> new RepeatCustomer(Region.valueOf(rs.getString("region")), rs.getLong("id"), rs.getString("name"),
                        rs.getInt("order_count")),
                from, to, minOrders);
    }

    /**
     * The customers whose first non-cancelled order (over all time) was placed between the two dates, inclusive, with
     * the date of that order and the customer's sales rep (null when none). Grouped per region of the customer
     * (alphabetical), then per sales rep name (customers without a rep last), then first order date, customer name, id.
     */
    public List<FirstTimeCustomer> firstTimeCustomers(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, MIN(o.order_date) AS first_order, "
                + "r.id AS rep_id, r.name AS rep_name, r.email AS rep_email, r.region AS rep_region "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id LEFT JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE o.status <> 'CANCELLED' "
                + "GROUP BY c.id, c.name, c.region, r.id, r.name, r.email, r.region "
                + "HAVING MIN(o.order_date) >= ? AND MIN(o.order_date) <= ? "
                + "ORDER BY c.region, r.name NULLS LAST, r.id, first_order, c.name, c.id";
        return jdbc.list("Cannot find first-time customers", sql, rs -> {
            SalesRep salesRep = null;
            if (rs.getObject("rep_id") != null) {
                salesRep = new SalesRep(rs.getLong("rep_id"), rs.getString("rep_name"), rs.getString("rep_email"),
                        Region.valueOf(rs.getString("rep_region")));
            }
            return new FirstTimeCustomer(Region.valueOf(rs.getString("region")), salesRep, rs.getLong("id"),
                    rs.getString("name"), rs.getObject("first_order", LocalDate.class));
        }, from, to);
    }

    /**
     * The customers of the sales rep that are not closed, with their revenue from non-cancelled orders placed between
     * the two dates (inclusive, 0 if none), the date of their last non-cancelled order (null if none) and the unpaid
     * part of their open invoices (0 if none). Sorted by customer name, then id.
     */
    public List<SalesRepCustomer> customerReportOfSalesRep(long salesRepId, LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o "
                + "JOIN sales_order_line l ON l.order_id = o.id WHERE o.customer_id = c.id "
                + "AND " + ORDERS_IN_PERIOD + ") AS revenue, "
                + "(SELECT MAX(o.order_date) FROM sales_order o WHERE o.customer_id = c.id AND o.status <> 'CANCELLED') AS last_order, "
                + "(SELECT COALESCE(SUM(i.amount - (SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id)), 0) FROM invoice i WHERE i.customer_id = c.id AND i.status = 'OPEN') AS open_amount "
                + "FROM customer c WHERE c.sales_rep_id = ? "
                + "AND " + NOT_CLOSED + " "
                + "ORDER BY c.name, c.id";
        return jdbc.list("Cannot find customer report of sales rep", sql,
                rs -> new SalesRepCustomer(rs.getLong("id"), rs.getString("name"), rs.getBigDecimal("revenue"),
                        rs.getObject("last_order", LocalDate.class), rs.getBigDecimal("open_amount")),
                from, to, salesRepId);
    }
}
