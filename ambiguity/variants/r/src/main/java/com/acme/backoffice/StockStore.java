package com.acme.backoffice;

import com.acme.backoffice.model.CategoryStock;
import com.acme.backoffice.model.CategoryWarehouseStock;
import com.acme.backoffice.model.LowStockAlert;
import com.acme.backoffice.model.PurchaseSuggestion;
import com.acme.backoffice.model.SlowMovingStock;
import com.acme.backoffice.model.StockLevel;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.WarehouseCategory;
import com.acme.backoffice.model.WarehouseSlowMovingStock;
import com.acme.backoffice.model.WarehouseStockValue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * The stock: the quantity of each product in each warehouse (set, add, remove, find), the total over all warehouses,
 * the stock value (per warehouse, in total, per category and per category and warehouse), categories without stock,
 * slow-moving stock (not shipped for a number of days), reorder levels with the low-stock alerts and purchase
 * suggestions they give.
 *
 * <p>Stock is valued at the product's last purchase unit cost (the latest line of a SENT or RECEIVED purchase order;
 * zero when there is none), see {@link #UNIT_COST}.
 */
public final class StockStore {
    /**
     * The unit cost a stock row {@code s} is valued at: the unit cost of the latest line of a SENT or RECEIVED purchase
     * order of the product (by order date, then order id, then line number), zero when there is none.
     */
    private static final String UNIT_COST = "COALESCE("
            + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
            + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
            + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)";
    /** The value of one stock row {@code s}. */
    private static final String STOCK_VALUE = "s.quantity * " + UNIT_COST;
    /** The value of the stock rows {@code s} of a group; zero when there are none. */
    private static final String SUM_STOCK_VALUE = "COALESCE(SUM(" + STOCK_VALUE + "), 0)";

    /** The warehouse ({@code warehouse_id}, {@code warehouse_name}), product, quantity (no stock row = 0) and reorder level. */
    private static final String SELECT_REORDER_LEVEL = "SELECT w.id AS warehouse_id, w.code, w.name AS warehouse_name, "
            + "w.city, w.region, p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, "
            + "COALESCE(s.quantity, 0) AS quantity, r.level";
    private static final String FROM_REORDER_LEVEL = " FROM reorder_level r JOIN warehouse w ON w.id = r.warehouse_id "
            + "JOIN product p ON p.id = r.product_id "
            + "LEFT JOIN stock s ON s.warehouse_id = r.warehouse_id AND s.product_id = r.product_id ";
    /** Low stock: an active product whose stock (no stock row = 0) is at or below its reorder level. */
    private static final String LOW_STOCK = "p.active = TRUE AND COALESCE(s.quantity, 0) <= r.level";

    private final Jdbc jdbc;

    StockStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    // ---- quantities -------------------------------------------------------------------------------------------

    public void setQuantity(long warehouseId, long productId, int quantity) {
        jdbc.update("Cannot set stock quantity", SET_QUANTITY, warehouseId, productId, quantity);
    }

    /** Adds the amount to the stock, creating the stock row if the warehouse has none for the product. */
    public void addQuantity(long warehouseId, long productId, int amount) {
        try {
            addQuantityRaw(warehouseId, productId, amount);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot add stock quantity", e);
        }
    }

    /** Takes the amount from the stock; returns false, changing nothing, if there is less than that. */
    public boolean removeQuantity(long warehouseId, long productId, int amount) {
        try {
            return removeQuantityRaw(warehouseId, productId, amount);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot remove stock quantity", e);
        }
    }

    /** The stock of the product in the warehouse; 0 when there is no stock row. */
    public int findQuantity(long warehouseId, long productId) {
        try {
            return findQuantityRaw(warehouseId, productId);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock quantity", e);
        }
    }

    /** The stock of the product over all warehouses; 0 when there is none. */
    public int findTotalQuantity(long productId) {
        return jdbc.queryInt("Cannot find total stock quantity",
                "SELECT COALESCE(SUM(quantity), 0) AS total FROM stock WHERE product_id = ?", productId);
    }

    /** The stock rows of the warehouse with their product, by product name (then id). */
    public List<StockLevel> listInWarehouse(long warehouseId) {
        return jdbc.list("Cannot list stock by warehouse",
                "SELECT " + Rows.PRODUCT_COLUMNS + ", s.quantity "
                        + "FROM stock s JOIN product p ON p.id = s.product_id WHERE s.warehouse_id = ? ORDER BY p.name, p.id",
                rs -> new StockLevel(Rows.product(rs), rs.getInt("quantity")), warehouseId);
    }

    /** The active products whose stock over all warehouses (none = 0) is below the threshold, by name (then id). */
    public List<StockLevel> listActiveProductsWithTotalBelow(int threshold) {
        return jdbc.list("Cannot list products with low stock",
                "SELECT " + Rows.PRODUCT_COLUMNS + ", COALESCE(SUM(s.quantity), 0) AS total "
                        + "FROM product p LEFT JOIN stock s ON s.product_id = p.id WHERE p.active = TRUE "
                        + "GROUP BY p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active "
                        + "HAVING COALESCE(SUM(s.quantity), 0) < ? ORDER BY p.name, p.id",
                rs -> new StockLevel(Rows.product(rs), rs.getInt("total")), threshold);
    }

    // ---- stock value ------------------------------------------------------------------------------------------

    /** The stock value of every warehouse (zero when it has no stock), by warehouse code (then id), with 2 decimals. */
    public List<WarehouseStockValue> valuePerWarehouse() {
        return jdbc.list("Cannot list stock value by warehouse",
                "SELECT w.id, w.code, w.name, w.city, w.region, " + SUM_STOCK_VALUE + " AS stock_value "
                        + "FROM warehouse w LEFT JOIN stock s ON s.warehouse_id = w.id "
                        + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id",
                rs -> new WarehouseStockValue(Rows.warehouse(rs), rs.getBigDecimal("stock_value").setScale(2)));
    }

    /** The stock value over all warehouses, valued like {@link #valuePerWarehouse()}. */
    public BigDecimal findTotalValue() {
        return jdbc.single("Cannot find total stock value",
                "SELECT " + SUM_STOCK_VALUE + " AS stock_value FROM stock s",
                rs -> rs.getBigDecimal("stock_value").setScale(2));
    }

    /** The stock quantity and value per category and warehouse, for the combinations with stock, valued like {@link #valuePerWarehouse()}. */
    public List<CategoryWarehouseStock> quantityAndValuePerCategoryAndWarehouse() {
        return jdbc.list("Cannot find stock per category and warehouse",
                "SELECT p.category, w.id, w.code, w.name, w.city, w.region, SUM(s.quantity) AS quantity, "
                        + SUM_STOCK_VALUE + " AS stock_value "
                        + "FROM stock s JOIN product p ON p.id = s.product_id JOIN warehouse w ON w.id = s.warehouse_id "
                        + "WHERE s.quantity > 0 "
                        + "GROUP BY p.category, w.id, w.code, w.name, w.city, w.region ORDER BY p.category, w.code, w.id",
                rs -> new CategoryWarehouseStock(rs.getString("category"), Rows.warehouse(rs), rs.getLong("quantity"),
                        rs.getBigDecimal("stock_value").setScale(2)));
    }

    /** The stock quantity and value per category over all warehouses, for the categories with stock, valued like {@link #valuePerWarehouse()}. */
    public List<CategoryStock> quantityAndValuePerCategory() {
        return jdbc.list("Cannot find stock per category",
                "SELECT p.category, SUM(s.quantity) AS quantity, " + SUM_STOCK_VALUE + " AS stock_value "
                        + "FROM stock s JOIN product p ON p.id = s.product_id "
                        + "WHERE s.quantity > 0 "
                        + "GROUP BY p.category ORDER BY p.category",
                rs -> new CategoryStock(rs.getString("category"), rs.getLong("quantity"),
                        rs.getBigDecimal("stock_value").setScale(2)));
    }

    /** Every warehouse and product category (of any product) without stock in that warehouse, by warehouse code then category. */
    public List<WarehouseCategory> categoriesWithoutStockPerWarehouse() {
        return jdbc.list("Cannot find categories without stock per warehouse",
                "SELECT w.id, w.code, w.name, w.city, w.region, c.category FROM warehouse w "
                        + "CROSS JOIN (SELECT DISTINCT category FROM product) c "
                        + "WHERE NOT EXISTS (SELECT 1 FROM stock s JOIN product p ON p.id = s.product_id "
                        + "WHERE s.warehouse_id = w.id AND p.category = c.category AND s.quantity > 0) "
                        + "ORDER BY w.code, w.id, c.category",
                rs -> new WarehouseCategory(Rows.warehouse(rs), rs.getString("category")));
    }

    // ---- slow-moving stock ------------------------------------------------------------------------------------

    /**
     * The products with stock in a warehouse but not shipped from that warehouse (whole or partial shipments) on any day from
     * the given number of days before the date up to the day before it, by warehouse code then stock value, highest first;
     * valued like {@link #valuePerWarehouse()}.
     */
    public List<WarehouseSlowMovingStock> listSlowMovingPerWarehouse(LocalDate date, int days) {
        return jdbc.list("Cannot find slow-moving stock per warehouse",
                "SELECT w.id AS warehouse_id, w.code, w.name AS warehouse_name, w.city, w.region, " + Rows.PRODUCT_COLUMNS
                        + ", s.quantity, " + STOCK_VALUE + " AS stock_value "
                        + "FROM stock s JOIN product p ON p.id = s.product_id JOIN warehouse w ON w.id = s.warehouse_id "
                        + "WHERE s.quantity > 0 " + notShippedInPeriod(true)
                        + "ORDER BY w.code, w.id, stock_value DESC, p.sku, p.id",
                rs -> new WarehouseSlowMovingStock(InventoryRows.warehouseWithIdAndNameAlias(rs), Rows.product(rs),
                        rs.getLong("quantity"), rs.getBigDecimal("stock_value").setScale(2)),
                date.minusDays(days), date, date.minusDays(days), date);
    }

    /**
     * The products with stock that were not shipped from any warehouse (whole or partial shipments) on any day from the
     * given number of days before the date up to the day before it, with the stock over all warehouses, by stock value,
     * highest first; valued like {@link #valuePerWarehouse()}.
     */
    public List<SlowMovingStock> listSlowMoving(LocalDate date, int days) {
        return jdbc.list("Cannot find slow-moving stock",
                "SELECT " + Rows.PRODUCT_COLUMNS + ", SUM(s.quantity) AS quantity, " + SUM_STOCK_VALUE + " AS stock_value "
                        + "FROM stock s JOIN product p ON p.id = s.product_id "
                        + "WHERE s.quantity > 0 " + notShippedInPeriod(false)
                        + "GROUP BY p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active "
                        + "ORDER BY stock_value DESC, p.sku, p.id",
                rs -> new SlowMovingStock(Rows.product(rs), rs.getLong("quantity"),
                        rs.getBigDecimal("stock_value").setScale(2)),
                date.minusDays(days), date, date.minusDays(days), date);
    }

    /**
     * The condition that the product of stock row {@code s} was not shipped (whole shipments nor shipment parts) on a day
     * from the first bound (inclusive) to the second (exclusive); from the stock row's warehouse only, or from any.
     * Binds from, to, from, to.
     */
    private static String notShippedInPeriod(boolean fromSameWarehouse) {
        String shipmentWarehouse = fromSameWarehouse ? "h.warehouse_id = s.warehouse_id AND " : "";
        String partWarehouse = fromSameWarehouse ? "sp.warehouse_id = s.warehouse_id AND " : "";
        return "AND NOT EXISTS (SELECT 1 FROM shipment h JOIN sales_order_line ol ON ol.order_id = h.order_id "
                + "WHERE " + shipmentWarehouse + "ol.product_id = s.product_id AND h.ship_date >= ? AND h.ship_date < ?) "
                + "AND NOT EXISTS (SELECT 1 FROM shipment_part sp JOIN sales_order_line ol "
                + "ON ol.order_id = sp.order_id AND ol.line_no = sp.line_no "
                + "WHERE " + partWarehouse + "ol.product_id = s.product_id AND sp.ship_date >= ? AND sp.ship_date < ?) ";
    }

    // ---- reorder levels, low stock, purchase suggestions -------------------------------------------------------

    public void setReorderLevel(long warehouseId, long productId, int level) {
        jdbc.update("Cannot set reorder level",
                "MERGE INTO reorder_level (warehouse_id, product_id, level) KEY (warehouse_id, product_id) VALUES (?, ?, ?)",
                warehouseId, productId, level);
    }

    /** The low-stock alerts of the warehouse (active products at or below their reorder level), by product name (then id). */
    public List<LowStockAlert> listLowStockInWarehouse(long warehouseId) {
        return listLowStock("Cannot list low stock by warehouse", "AND r.warehouse_id = ? ", "p.name, p.id", warehouseId);
    }

    /** The low-stock alerts of all warehouses, by warehouse code, then product name (then id). */
    public List<LowStockAlert> listLowStock() {
        return listLowStock("Cannot list low stock", "", "w.code, p.name, p.id");
    }

    private List<LowStockAlert> listLowStock(String error, String filter, String ordering, Object... params) {
        return jdbc.list(error,
                SELECT_REORDER_LEVEL + FROM_REORDER_LEVEL + "WHERE " + LOW_STOCK + " " + filter + "ORDER BY " + ordering,
                rs -> new LowStockAlert(InventoryRows.warehouseWithIdAndNameAlias(rs), Rows.product(rs),
                        rs.getInt("quantity"), rs.getInt("level")),
                params);
    }

    /**
     * Per product of the warehouse with stock at or below its reorder level (no stock counts as zero), the quantity needed to
     * reach twice the reorder level and the supplier with the lowest last unit cost (ties: shorter lead time, then lower
     * supplier id). Products that need nothing are left out; products without supplier have none. Sorted by product name.
     */
    public List<PurchaseSuggestion> listPurchaseSuggestionsForWarehouse(long warehouseId) {
        return jdbc.list("Cannot list purchase suggestions by warehouse",
                SELECT_REORDER_LEVEL + ", su.id AS supplier_id, su.name AS supplier_name, su.contact_email, su.country, "
                        + "su.rating, sp.last_unit_cost" + FROM_REORDER_LEVEL
                        + "LEFT JOIN supplier_product sp ON sp.id = (SELECT sp2.id FROM supplier_product sp2 "
                        + "WHERE sp2.product_id = p.id ORDER BY sp2.last_unit_cost, sp2.lead_time_days, sp2.supplier_id LIMIT 1) "
                        + "LEFT JOIN supplier su ON su.id = sp.supplier_id "
                        + "WHERE " + LOW_STOCK + " AND r.warehouse_id = ? AND 2 * r.level - COALESCE(s.quantity, 0) > 0 "
                        + "ORDER BY p.name, p.id",
                rs -> {
                    Supplier supplier = null;
                    if (rs.getObject("supplier_id") != null) {
                        supplier = new Supplier(rs.getLong("supplier_id"), rs.getString("supplier_name"),
                                rs.getString("contact_email"), rs.getString("country"), rs.getInt("rating"));
                    }
                    int quantity = rs.getInt("quantity");
                    int level = rs.getInt("level");
                    return new PurchaseSuggestion(InventoryRows.warehouseWithIdAndNameAlias(rs), Rows.product(rs),
                            quantity, level, 2 * level - quantity, supplier, rs.getBigDecimal("last_unit_cost"));
                },
                warehouseId);
    }

    // ---- statements shared with the inventory transactions (stock adjustments, warehouse transfers) ----------------

    private static final String SET_QUANTITY =
            "MERGE INTO stock (warehouse_id, product_id, quantity) KEY (warehouse_id, product_id) VALUES (?, ?, ?)";

    /** {@link #setQuantity}, throwing the {@link SQLException} (for use inside a transaction). */
    void setQuantityRaw(long warehouseId, long productId, int quantity) throws SQLException {
        jdbc.updateRaw(SET_QUANTITY, warehouseId, productId, quantity);
    }

    /** {@link #addQuantity}, throwing the {@link SQLException} (for use inside a transaction). */
    void addQuantityRaw(long warehouseId, long productId, int amount) throws SQLException {
        if (jdbc.updateRaw("UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?",
                amount, warehouseId, productId) == 0) {
            jdbc.updateRaw("INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)",
                    warehouseId, productId, amount);
        }
    }

    /** {@link #removeQuantity}, throwing the {@link SQLException} (for use inside a transaction). */
    boolean removeQuantityRaw(long warehouseId, long productId, int amount) throws SQLException {
        return jdbc.updateRaw("UPDATE stock SET quantity = quantity - ? WHERE warehouse_id = ? AND product_id = ? AND quantity >= ?",
                amount, warehouseId, productId, amount) > 0;
    }

    /** {@link #findQuantity}, throwing the {@link SQLException} (for use inside a transaction). */
    int findQuantityRaw(long warehouseId, long productId) throws SQLException {
        return jdbc.firstRaw("SELECT quantity FROM stock WHERE warehouse_id = ? AND product_id = ?",
                rs -> rs.getInt("quantity"), warehouseId, productId).orElse(0);
    }
}
