package com.acme.backoffice;

import com.acme.backoffice.model.CountryReturnCount;
import com.acme.backoffice.model.CustomerReturn;
import com.acme.backoffice.model.OrderReturns;
import com.acme.backoffice.model.ProductReturnedQuantity;
import com.acme.backoffice.model.ProductReturns;
import com.acme.backoffice.model.QuarterReturns;
import com.acme.backoffice.model.ReasonReturns;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionReasonReturns;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRepReturnCount;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.WarehouseReturn;
import com.acme.backoffice.model.WeekdayReturns;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Sales returns: creating a return (which puts the returned quantities back into the stock of the receiving warehouse
 * and records the stock movements), finding and listing the returns of an order, customer, product, country or
 * warehouse, searching them, the latest returns, the returned quantities per product, the returns per reason, customer,
 * region, quarter, weekday, sales rep and country, and the orders of a customer that have returns.
 *
 * <p>A return is valued at the unit prices of its order: each returned product at the unit price of the first line of
 * the order with that product (see {@link #unitPriceOnOrder}).
 */
public final class ReturnStore {
    private static final String SELECT_RETURN = "SELECT " + Rows.SALES_RETURN_COLUMNS + " FROM sales_return r";
    private static final String JOIN_ORDER = " JOIN sales_order o ON o.id = r.order_id";
    private static final String JOIN_ORDER_AND_CUSTOMER = JOIN_ORDER + " JOIN customer c ON c.id = o.customer_id";
    private static final String IN_PERIOD = "r.return_date >= ? AND r.return_date <= ?";
    private static final String OLDEST_FIRST = " ORDER BY r.return_date, r.id";
    private static final String LATEST_FIRST_LIMITED = " ORDER BY r.return_date DESC, r.id DESC LIMIT ?";

    /** What the per-quarter and per-weekday breakdowns count: returns, by region of the customer of the order. */
    private static final Breakdowns.Counted COUNTED = new Breakdowns.Counted(
            "FROM sales_return r" + JOIN_ORDER_AND_CUSTOMER, "r.return_date", "", "c.region");

    private final Jdbc jdbc;

    ReturnStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    // ---------------------------------------------------------------- writing

    /** Creates the return and adds its quantities to the stock of the warehouse, all or nothing. */
    public SalesReturn create(SalesReturn salesReturn, long warehouseId) {
        return jdbc.inTransaction("Cannot create sales return", () -> {
            long id = jdbc.insertRaw("INSERT INTO sales_return (number, order_id, return_date, reason, warehouse_id) "
                            + "VALUES (?, ?, ?, ?, ?)", salesReturn.number(), salesReturn.orderId(), salesReturn.returnDate(),
                    salesReturn.reason(), warehouseId);
            int lineNo = 1;
            for (ReturnLine line : salesReturn.lines()) {
                jdbc.updateRaw("INSERT INTO sales_return_line (return_id, line_no, product_id, quantity) VALUES (?, ?, ?, ?)",
                        id, lineNo++, line.productId(), line.quantity());
            }
            for (ReturnLine line : salesReturn.lines()) {
                // a stock row is inserted when the warehouse has none for the product yet
                if (jdbc.updateRaw("UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?",
                        line.quantity(), warehouseId, line.productId()) == 0) {
                    jdbc.updateRaw("INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)",
                            warehouseId, line.productId(), line.quantity());
                }
                jdbc.updateRaw("INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                        + "VALUES (?, ?, ?, ?, 'RETURN')", line.productId(), warehouseId, salesReturn.returnDate(), line.quantity());
            }
            return new SalesReturn(id, salesReturn.number(), salesReturn.orderId(), salesReturn.returnDate(),
                    salesReturn.reason(), List.copyOf(salesReturn.lines()));
        });
    }

    // ---------------------------------------------------------------- single returns and lists of returns

    /** The sales return with the number, if it exists. */
    public Optional<SalesReturn> findByNumber(String number) {
        return jdbc.first("Cannot find sales return", SELECT_RETURN + " WHERE r.number = ?", Rows.salesReturn(jdbc), number);
    }

    /** The sales returns of the order, oldest first (then by id). */
    public List<SalesReturn> listOfOrder(long orderId) {
        return listReturns("Cannot list sales returns of order", " WHERE r.order_id = ?" + OLDEST_FIRST, orderId);
    }

    /** The sales returns of the orders of the customer, oldest first (then by id). */
    public List<SalesReturn> listOfCustomer(long customerId) {
        return listReturns("Cannot list sales returns of customer", JOIN_ORDER + " WHERE o.customer_id = ?" + OLDEST_FIRST,
                customerId);
    }

    /**
     * The sales returns dated between the two dates, inclusive, with at least one line of the product, oldest first
     * (then by id).
     */
    public List<SalesReturn> listOfProduct(long productId, LocalDate from, LocalDate to) {
        return listReturns("Cannot list sales returns of product", " WHERE " + IN_PERIOD
                        + " AND EXISTS (SELECT 1 FROM sales_return_line pl WHERE pl.return_id = r.id AND pl.product_id = ?)"
                        + OLDEST_FIRST, from, to, productId);
    }

    /**
     * The sales returns dated between the two dates, inclusive, for the orders of the customers of the country, oldest
     * first, then by id.
     */
    public List<SalesReturn> listOfCountry(String country, LocalDate from, LocalDate to) {
        return listReturns("Cannot find returns by country", JOIN_ORDER_AND_CUSTOMER + " WHERE c.country = ? AND " + IN_PERIOD
                + OLDEST_FIRST, country, from, to);
    }

    /**
     * Searches sales returns by any combination of the customer of the order, a product among the lines, the exact
     * reason and a return date range; a null customer id, product id, reason, from date or to date is not applied.
     * The dates are inclusive. Oldest first.
     */
    public List<SalesReturn> search(Long customerId, Long productId, String reason, LocalDate from, LocalDate to) {
        return listReturns("Cannot search sales returns", JOIN_ORDER
                        + " WHERE (CAST(? AS BIGINT) IS NULL OR o.customer_id = CAST(? AS BIGINT)) "
                        + "AND (CAST(? AS BIGINT) IS NULL OR EXISTS (SELECT 1 FROM sales_return_line pl "
                        + "WHERE pl.return_id = r.id AND pl.product_id = CAST(? AS BIGINT))) "
                        + "AND (CAST(? AS VARCHAR) IS NULL OR r.reason = CAST(? AS VARCHAR)) "
                        + "AND (CAST(? AS DATE) IS NULL OR r.return_date >= CAST(? AS DATE)) "
                        + "AND (CAST(? AS DATE) IS NULL OR r.return_date <= CAST(? AS DATE))" + OLDEST_FIRST,
                customerId, customerId, productId, productId, reason, reason, from, from, to, to);
    }

    /** The N most recent sales returns, newest first, then by highest id. */
    public List<SalesReturn> listLatest(int limit) {
        return listReturns("Cannot find latest returns", LATEST_FIRST_LIMITED, limit);
    }

    /** The N most recent sales returns of customers in the region, newest first, then by highest id. */
    public List<SalesReturn> listLatestInRegion(Region region, int limit) {
        // region.name(): a null region fails with a NullPointerException, as before
        return listReturns("Cannot find latest returns by region", JOIN_ORDER_AND_CUSTOMER + " WHERE c.region = ?"
                + LATEST_FIRST_LIMITED, region.name(), limit);
    }

    /** The N most recent sales returns of the customer, newest first, then by highest id. */
    public List<SalesReturn> listLatestOfCustomer(long customerId, int limit) {
        return listReturns("Cannot find latest returns by customer", JOIN_ORDER + " WHERE o.customer_id = ?"
                + LATEST_FIRST_LIMITED, customerId, limit);
    }

    /**
     * The sales returns received into the warehouse, dated between the two dates, inclusive, with the number of the
     * order. Oldest first, then by id.
     */
    public List<WarehouseReturn> listOfWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.number AS number, o.number AS order_number, r.return_date AS return_date, "
                + "r.reason AS reason FROM sales_return r" + JOIN_ORDER + " WHERE r.warehouse_id = ? AND " + IN_PERIOD
                + OLDEST_FIRST;
        return jdbc.list("Cannot find returns of warehouse", sql,
                rs -> new WarehouseReturn(rs.getLong("id"), rs.getString("number"), rs.getString("order_number"),
                        rs.getObject("return_date", LocalDate.class), rs.getString("reason")),
                warehouseId, from, to);
    }

    /**
     * The orders of the customer placed between the two dates, inclusive, that have at least one return (whatever the
     * return date), oldest first. Each order is listed once, with its value, the value of its returned quantities at
     * the unit prices of the order (as for the returns per reason) and the returned share of the order value in percent.
     */
    public List<OrderReturns> listOrdersWithReturnsOfCustomer(long customerId, LocalDate from, LocalDate to) {
        String sql = "SELECT " + Rows.SALES_ORDER_COLUMNS + ", "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order_line l WHERE l.order_id = o.id) AS order_value, "
                + "(SELECT COALESCE(SUM(rl.quantity * " + unitPriceOnOrder("o.id", "rl.product_id") + "), 0) "
                + "FROM sales_return r JOIN sales_return_line rl ON rl.return_id = r.id WHERE r.order_id = o.id) AS returned_value "
                + "FROM sales_order o WHERE o.customer_id = ? AND o.order_date >= ? AND o.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM sales_return r WHERE r.order_id = o.id) ORDER BY o.order_date, o.id";
        Jdbc.RowMapper<SalesOrder> orderMapper = Rows.salesOrder(jdbc);
        return jdbc.list("Cannot list orders with returns", sql, rs -> {
            SalesOrder order = orderMapper.map(rs);
            BigDecimal orderValue = rs.getBigDecimal("order_value");
            BigDecimal returnedValue = rs.getBigDecimal("returned_value");
            BigDecimal returnedPercent = orderValue.signum() == 0 ? new BigDecimal("0.00")
                    : returnedValue.multiply(new BigDecimal("100")).divide(orderValue, 2, RoundingMode.HALF_UP);
            return new OrderReturns(order, orderValue, returnedValue, returnedPercent);
        }, customerId, from, to);
    }

    // ---------------------------------------------------------------- returned quantities

    /**
     * Units returned per product in the sales returns dated between the two dates, inclusive. Sorted by units
     * returned, highest first (then by SKU); products without returns are left out.
     */
    public List<ProductReturns> returnedQuantityPerProduct(LocalDate from, LocalDate to) {
        return returnedPerProduct("Cannot find returned quantity per product", "", "", "", ReturnStore::productReturns,
                from, to);
    }

    /**
     * Units returned per product, as for the returned quantity per product, restricted to the products of the
     * category and of all categories below it.
     */
    public List<ProductReturns> returnedQuantityPerProductInCategory(String category, LocalDate from, LocalDate to) {
        return returnedPerProduct("Cannot find returned quantity per product in category",
                "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) ",
                "", " AND p.category IN (SELECT code FROM tree)", ReturnStore::productReturns, category, from, to);
    }

    /**
     * The quantity per product of the sales returns received into the warehouse, dated between the two dates,
     * inclusive. Highest quantity first, then by SKU.
     */
    public List<ProductReturnedQuantity> returnedQuantityPerProductOfWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        // the warehouse condition comes first in the WHERE clause, so its parameter is bound before the period
        return returnedPerProduct("Cannot find returned quantities of warehouse", "", "r.warehouse_id = ? AND ", "",
                rs -> new ProductReturnedQuantity(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                        rs.getLong("units")), warehouseId, from, to);
    }

    // ---------------------------------------------------------------- reports

    /**
     * Number and value of the sales returns dated between the two dates, inclusive, per reason. The value is the
     * returned quantities at the unit prices of the order. Sorted by value, highest first (then by reason); reasons
     * without returns are left out.
     */
    public List<ReasonReturns> perReason(LocalDate from, LocalDate to) {
        return countAndValuePerReason("Cannot find returns per reason", "", "", "r.reason", "total DESC, r.reason",
                rs -> new ReasonReturns(rs.getString("reason"), rs.getInt("return_count"), rs.getBigDecimal("total")),
                from, to);
    }

    /**
     * Number and value of the sales returns dated between the two dates, inclusive, per reason and region of the
     * customer, valued as for the returns per reason. Sorted by region name, then value (highest first), then reason;
     * combinations without returns are left out.
     */
    public List<RegionReasonReturns> perReasonAndRegion(LocalDate from, LocalDate to) {
        return countAndValuePerReason("Cannot find returns per reason and region", "c.region AS region, ",
                JOIN_ORDER_AND_CUSTOMER, "c.region, r.reason", "c.region, total DESC, r.reason",
                rs -> new RegionReasonReturns(Region.valueOf(rs.getString("region")), rs.getString("reason"),
                        rs.getInt("return_count"), rs.getBigDecimal("total")),
                from, to);
    }

    /**
     * The sales returns dated between the two dates, inclusive, with the customer of the order and the value of each
     * return (the returned quantities at the unit prices of the order, as for the returns per reason). Customers with
     * the most returns first (then by name and id), each customer's returns oldest first (then by id).
     */
    public List<CustomerReturn> listPerCustomer(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, r.id AS id, r.number AS number, "
                + "r.return_date AS return_date, r.reason AS reason, "
                + "COALESCE((SELECT SUM(l.quantity * " + unitPriceOnOrder("r.order_id", "l.product_id") + ") "
                + "FROM sales_return_line l WHERE l.return_id = r.id), 0) AS returned_value "
                + "FROM sales_return r" + JOIN_ORDER_AND_CUSTOMER + " WHERE " + IN_PERIOD + " "
                + "ORDER BY (SELECT COUNT(*) FROM sales_return r2 JOIN sales_order o2 ON o2.id = r2.order_id "
                + "WHERE o2.customer_id = c.id AND r2.return_date >= ? AND r2.return_date <= ?) DESC, "
                + "c.name, c.id, r.return_date, r.id";
        return jdbc.list("Cannot find returns per customer", sql,
                rs -> new CustomerReturn(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                        rs.getString("customer_name"), rs.getObject("return_date", LocalDate.class),
                        rs.getString("reason"), rs.getBigDecimal("returned_value")),
                from, to, from, to);
    }

    /**
     * For each of the four quarters of the year, in order: the number of sales returns dated in the quarter, in total and
     * per region of the customer of the order. Quarters and regions without returns are included with zeros.
     */
    public List<QuarterReturns> countPerQuarter(int year) {
        return Breakdowns.perQuarter(jdbc, "Cannot find returns per quarter", COUNTED, year, QuarterReturns::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of sales returns dated between the two dates, inclusive, in
     * total and per region of the customer of the order. Days and regions without returns are included with zeros.
     */
    public List<WeekdayReturns> countPerWeekday(LocalDate from, LocalDate to) {
        return Breakdowns.perWeekday(jdbc, "Cannot find returns per weekday", COUNTED, from, to, WeekdayReturns::new);
    }

    /**
     * The number of returns dated between the two dates, inclusive, for the orders of the customers of each sales
     * rep, highest first, then by name and id; reps without returns are included with zero.
     */
    public List<SalesRepReturnCount> countPerSalesRep(LocalDate from, LocalDate to) {
        return Breakdowns.perSalesRep(jdbc, "Cannot find return count per sales rep",
                "SELECT COUNT(*) FROM sales_return sr JOIN sales_order o ON o.id = sr.order_id "
                        + "JOIN customer c ON c.id = o.customer_id "
                        + "WHERE c.sales_rep_id = r.id AND sr.return_date >= ? AND sr.return_date <= ?",
                from, to, SalesRepReturnCount::new);
    }

    /**
     * The number of returns dated between the two dates, inclusive, for the orders of the customers of each country
     * (countries without returns in the period are left out). Sorted by return count, highest first, then by country.
     */
    public List<CountryReturnCount> countPerCountry(LocalDate from, LocalDate to) {
        return Breakdowns.perCountry(jdbc, "Cannot find return count per country",
                "FROM sales_return r" + JOIN_ORDER_AND_CUSTOMER + " WHERE " + IN_PERIOD, from, to, CountryReturnCount::new);
    }

    // ---------------------------------------------------------------- shared queries

    /** The returns (with their lines) selected by the joins, conditions and ordering that follow {@code FROM sales_return r}. */
    private List<SalesReturn> listReturns(String error, String joinsWhereOrder, Object... params) {
        return jdbc.list(error, SELECT_RETURN + joinsWhereOrder, Rows.salesReturn(jdbc), params);
    }

    /**
     * The unit price at which a product was sold on an order: that of the first line of the order with the product.
     * This is how returned quantities are valued.
     */
    private static String unitPriceOnOrder(String orderIdColumn, String productIdColumn) {
        return "(SELECT ol.unit_price FROM sales_order_line ol WHERE ol.order_id = " + orderIdColumn
                + " AND ol.product_id = " + productIdColumn + " ORDER BY ol.line_no FETCH FIRST 1 ROW ONLY)";
    }

    /**
     * Units returned per product (columns id, sku, name, units) in the returns dated in the period, highest first, then
     * by SKU.
     *
     * @param with              a {@code WITH} clause placed before the query, or empty
     * @param leadingCondition  conditions placed before the period, ending with {@code " AND "}, or empty
     * @param trailingCondition conditions without parameters placed after the period, starting with {@code " AND "}, or empty
     * @param params            the parameters in SQL order: those of {@code with}, of {@code leadingCondition}, then
     *                          from and to
     */
    private <T> List<T> returnedPerProduct(String error, String with, String leadingCondition, String trailingCondition,
                                           Jdbc.RowMapper<T> mapper, Object... params) {
        String sql = with + "SELECT p.id AS id, p.sku AS sku, p.name AS name, SUM(l.quantity) AS units "
                + "FROM sales_return r JOIN sales_return_line l ON l.return_id = r.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE " + leadingCondition + IN_PERIOD + trailingCondition + " "
                + "GROUP BY p.id, p.sku, p.name ORDER BY units DESC, p.sku";
        return jdbc.list(error, sql, mapper, params);
    }

    private static ProductReturns productReturns(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new ProductReturns(rs.getLong("id"), rs.getString("sku"), rs.getString("name"), rs.getLong("units"));
    }

    /**
     * Number (return_count) and value (total) of the returns dated in the period, grouped by reason and possibly more.
     * Returns without lines count with value zero.
     */
    private <T> List<T> countAndValuePerReason(String error, String extraColumns, String joins, String groupBy,
                                               String orderBy, Jdbc.RowMapper<T> mapper, LocalDate from, LocalDate to) {
        String sql = "SELECT " + extraColumns + "r.reason AS reason, COUNT(DISTINCT r.id) AS return_count, "
                + "COALESCE(SUM(l.quantity * " + unitPriceOnOrder("r.order_id", "l.product_id") + "), 0) AS total "
                + "FROM sales_return r" + joins + " LEFT JOIN sales_return_line l ON l.return_id = r.id "
                + "WHERE " + IN_PERIOD + " GROUP BY " + groupBy + " ORDER BY " + orderBy;
        return jdbc.list(error, sql, mapper, from, to);
    }
}
