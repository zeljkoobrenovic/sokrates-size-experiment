package com.acme.backoffice;

import com.acme.backoffice.model.ReasonMovements;
import com.acme.backoffice.model.StockAdjustment;
import com.acme.backoffice.model.StockMovement;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.WarehouseAdjustments;
import com.acme.backoffice.model.WarehouseMovements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * The stock movements (the log of every stock change: date, quantity change, reason) and the stock adjustments (a
 * counted quantity replacing the stock, logged as an ADJUSTMENT movement): recording them, listing them per product,
 * warehouse or category in a period, the movements per reason, warehouse and category, and the negative adjustments
 * (stock losses) per warehouse.
 */
public final class StockMovementStore {
    /** Columns of a stock movement {@code m} under the names {@link #movement} reads. */
    private static final String MOVEMENT_COLUMNS = "m.id, m.product_id, m.warehouse_id, m.movement_date, m.quantity_change, m.reason";
    /** Columns of a stock adjustment {@code a} under the names {@link #adjustment} reads. */
    private static final String ADJUSTMENT_COLUMNS =
            "a.id, a.product_id, a.warehouse_id, a.adjustment_date, a.quantity_before, a.counted_quantity, a.reason";

    private final Jdbc jdbc;
    private final StockStore stock;

    StockMovementStore(Jdbc jdbc) {
        this.jdbc = jdbc;
        this.stock = new StockStore(jdbc);
    }

    // ---- stock movements --------------------------------------------------------------------------------------

    /** Logs a stock change (positive when the stock grows, negative when it shrinks). */
    public void create(long productId, long warehouseId, LocalDate movementDate, int quantityChange,
                       StockMovementReason reason) {
        try {
            createRaw(productId, warehouseId, movementDate, quantityChange, reason);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create stock movement", e);
        }
    }

    /** The stock movements of the product in all warehouses between the two dates, inclusive, oldest first (then by id). */
    public List<StockMovement> listOfProduct(long productId, LocalDate from, LocalDate to) {
        return listMovements("Cannot find stock movements by product", "m.product_id = ?", productId, from, to);
    }

    /** The stock movements of all products in the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<StockMovement> listInWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return listMovements("Cannot find stock movements by warehouse", "m.warehouse_id = ?", warehouseId, from, to);
    }

    /** The stock movements of the product in the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<StockMovement> listOfProductInWarehouse(long productId, long warehouseId, LocalDate from, LocalDate to) {
        return listMovements("Cannot find stock movements by product and warehouse",
                "m.product_id = ? AND m.warehouse_id = ?", productId, warehouseId, from, to);
    }

    /** The movements matching the filter (its parameters come first, then from, to), dated in the period, oldest first. */
    private List<StockMovement> listMovements(String error, String filter, Object... params) {
        return jdbc.list(error, "SELECT " + MOVEMENT_COLUMNS + " FROM stock_movement m WHERE " + filter
                + " AND m.movement_date >= ? AND m.movement_date <= ? ORDER BY m.movement_date, m.id",
                StockMovementStore::movement, params);
    }

    /**
     * The stock movements of the products of the category between the two dates, inclusive, per warehouse. Warehouses
     * are sorted by code, movements oldest first (then by id); warehouses without movements are left out.
     */
    public List<WarehouseMovements> listOfCategoryPerWarehouse(String category, LocalDate from, LocalDate to) {
        // m.warehouse_id is the same as w.id, so the movement mapper reads it under the warehouse's alias
        List<InventoryRows.WarehouseItem<StockMovement>> rows = jdbc.list("Cannot find stock movements by category per warehouse",
                "SELECT w.id AS warehouse_id, w.code, w.name, w.city, w.region, m.id, m.product_id, "
                        + "m.movement_date, m.quantity_change, m.reason "
                        + "FROM stock_movement m JOIN product p ON p.id = m.product_id JOIN warehouse w ON w.id = m.warehouse_id "
                        + "WHERE p.category = ? AND m.movement_date >= ? AND m.movement_date <= ? "
                        + "ORDER BY w.code, w.id, m.movement_date, m.id",
                rs -> new InventoryRows.WarehouseItem<StockMovement>(InventoryRows.warehouseWithIdAlias(rs), movement(rs)),
                category, from, to);
        return InventoryRows.groupPerWarehouse(rows, WarehouseMovements::new);
    }

    /**
     * The number and the net quantity change of the stock movements between the two dates, inclusive, per reason,
     * warehouse and product category. Sorted by reason, warehouse code and category.
     */
    public List<ReasonMovements> countAndQuantityPerReasonWarehouseAndCategory(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find stock movements per reason, warehouse and category",
                "SELECT m.reason, w.id, w.code, w.name, w.city, w.region, p.category, COUNT(*) AS movement_count, "
                        + "SUM(m.quantity_change) AS quantity_change "
                        + "FROM stock_movement m JOIN product p ON p.id = m.product_id JOIN warehouse w ON w.id = m.warehouse_id "
                        + "WHERE m.movement_date >= ? AND m.movement_date <= ? "
                        + "GROUP BY m.reason, w.id, w.code, w.name, w.city, w.region, p.category "
                        + "ORDER BY m.reason, w.code, w.id, p.category",
                rs -> new ReasonMovements(StockMovementReason.valueOf(rs.getString("reason")), Rows.warehouse(rs),
                        rs.getString("category"), rs.getLong("movement_count"), rs.getLong("quantity_change")),
                from, to);
    }

    // ---- stock adjustments ------------------------------------------------------------------------------------

    /** Sets the stock to the counted quantity and logs the adjustment and its difference as a movement, all or nothing. */
    public StockAdjustment createAdjustment(StockAdjustment adjustment) {
        return jdbc.inTransaction("Cannot create stock adjustment", () -> {
            int before = stock.findQuantityRaw(adjustment.warehouseId(), adjustment.productId());
            stock.setQuantityRaw(adjustment.warehouseId(), adjustment.productId(), adjustment.countedQuantity());
            long id = jdbc.insertRaw("INSERT INTO stock_adjustment (product_id, warehouse_id, adjustment_date, "
                            + "quantity_before, counted_quantity, reason) VALUES (?, ?, ?, ?, ?, ?)",
                    adjustment.productId(), adjustment.warehouseId(), adjustment.adjustmentDate(), before,
                    adjustment.countedQuantity(), adjustment.reason());
            if (adjustment.countedQuantity() != before) {
                createRaw(adjustment.productId(), adjustment.warehouseId(), adjustment.adjustmentDate(),
                        adjustment.countedQuantity() - before, StockMovementReason.ADJUSTMENT);
            }
            return new StockAdjustment(id, adjustment.productId(), adjustment.warehouseId(), adjustment.adjustmentDate(),
                    before, adjustment.countedQuantity(), adjustment.reason());
        });
    }

    /** The stock adjustments in the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<StockAdjustment> listAdjustmentsInWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find stock adjustments by warehouse",
                "SELECT " + ADJUSTMENT_COLUMNS + " FROM stock_adjustment a "
                        + "WHERE a.warehouse_id = ? AND a.adjustment_date >= ? AND a.adjustment_date <= ? "
                        + "ORDER BY a.adjustment_date, a.id",
                StockMovementStore::adjustment, warehouseId, from, to);
    }

    /**
     * The stock adjustments that lowered the stock between the two dates, inclusive, per warehouse. Warehouses are
     * sorted by code, adjustments by the size of the loss, biggest first (then oldest, then by id); warehouses without
     * negative adjustments are left out.
     */
    public List<WarehouseAdjustments> listNegativeAdjustmentsPerWarehouse(LocalDate from, LocalDate to) {
        // a.warehouse_id is the same as w.id, so the adjustment mapper reads it under the warehouse's alias
        List<InventoryRows.WarehouseItem<StockAdjustment>> rows = jdbc.list("Cannot find negative stock adjustments per warehouse",
                "SELECT w.id AS warehouse_id, w.code, w.name, w.city, w.region, a.id, a.product_id, "
                        + "a.adjustment_date, a.quantity_before, a.counted_quantity, a.reason "
                        + "FROM stock_adjustment a JOIN warehouse w ON w.id = a.warehouse_id "
                        + "WHERE a.counted_quantity < a.quantity_before AND a.adjustment_date >= ? AND a.adjustment_date <= ? "
                        + "ORDER BY w.code, w.id, a.counted_quantity - a.quantity_before, a.adjustment_date, a.id",
                rs -> new InventoryRows.WarehouseItem<StockAdjustment>(InventoryRows.warehouseWithIdAlias(rs), adjustment(rs)),
                from, to);
        return InventoryRows.groupPerWarehouse(rows, WarehouseAdjustments::new);
    }

    // ---- shared with the warehouse transfers -------------------------------------------------------------------

    /** {@link #create}, throwing the {@link SQLException} (for use inside a transaction). */
    void createRaw(long productId, long warehouseId, LocalDate movementDate, int quantityChange, StockMovementReason reason)
            throws SQLException {
        jdbc.updateRaw("INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                + "VALUES (?, ?, ?, ?, ?)", productId, warehouseId, movementDate, quantityChange, reason.name());
    }

    /** Columns: id, product_id, warehouse_id, movement_date, quantity_change, reason. */
    private static StockMovement movement(ResultSet rs) throws SQLException {
        return new StockMovement(rs.getLong("id"), rs.getLong("product_id"), rs.getLong("warehouse_id"),
                rs.getObject("movement_date", LocalDate.class), rs.getInt("quantity_change"),
                StockMovementReason.valueOf(rs.getString("reason")));
    }

    /** Columns: id, product_id, warehouse_id, adjustment_date, quantity_before, counted_quantity, reason. */
    private static StockAdjustment adjustment(ResultSet rs) throws SQLException {
        return new StockAdjustment(rs.getLong("id"), rs.getLong("product_id"), rs.getLong("warehouse_id"),
                rs.getObject("adjustment_date", LocalDate.class), rs.getInt("quantity_before"),
                rs.getInt("counted_quantity"), rs.getString("reason"));
    }
}
