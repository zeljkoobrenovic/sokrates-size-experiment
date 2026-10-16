package com.acme.backoffice;

import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.TransferRoute;
import com.acme.backoffice.model.WarehouseTransfer;
import com.acme.backoffice.model.WarehouseTransferLine;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Warehouse transfers (stock moved from one warehouse to another, with product lines): creating them, which moves the
 * stock and logs a TRANSFER movement on both sides; listing them in a period (all, from or to a warehouse, containing a
 * product); and the transferred quantity of a product per route (from and to warehouse).
 */
public final class WarehouseTransferStore {
    private final Jdbc jdbc;
    private final StockStore stock;
    private final StockMovementStore movements;

    WarehouseTransferStore(Jdbc jdbc) {
        this.jdbc = jdbc;
        this.stock = new StockStore(jdbc);
        this.movements = new StockMovementStore(jdbc);
    }

    /** Moves the stock between the warehouses and logs both movements of every line, all or nothing (fails if the stock is short). */
    public WarehouseTransfer create(WarehouseTransfer transfer) {
        return jdbc.inTransaction("Cannot create warehouse transfer", () -> {
            long id = jdbc.insertRaw("INSERT INTO warehouse_transfer (number, from_warehouse_id, to_warehouse_id, transfer_date) "
                            + "VALUES (?, ?, ?, ?)",
                    transfer.number(), transfer.fromWarehouseId(), transfer.toWarehouseId(), transfer.transferDate());
            int lineNo = 1;
            for (WarehouseTransferLine line : transfer.lines()) {
                jdbc.updateRaw("INSERT INTO warehouse_transfer_line (transfer_id, line_no, product_id, quantity) VALUES (?, ?, ?, ?)",
                        id, lineNo++, line.productId(), line.quantity());
                if (!stock.removeQuantityRaw(transfer.fromWarehouseId(), line.productId(), line.quantity())) {
                    throw new SQLException("Not enough stock of product " + line.productId() + " in warehouse "
                            + transfer.fromWarehouseId());
                }
                stock.addQuantityRaw(transfer.toWarehouseId(), line.productId(), line.quantity());
                movements.createRaw(line.productId(), transfer.fromWarehouseId(), transfer.transferDate(), -line.quantity(),
                        StockMovementReason.TRANSFER);
                movements.createRaw(line.productId(), transfer.toWarehouseId(), transfer.transferDate(), line.quantity(),
                        StockMovementReason.TRANSFER);
            }
            return new WarehouseTransfer(id, transfer.number(), transfer.fromWarehouseId(), transfer.toWarehouseId(),
                    transfer.transferDate(), List.copyOf(transfer.lines()));
        });
    }

    /** The transfers from or to the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<WarehouseTransfer> listFromOrToWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return listTransfers("Cannot find warehouse transfers by warehouse",
                "(from_warehouse_id = ? OR to_warehouse_id = ?) AND ", warehouseId, warehouseId, from, to);
    }

    /** The transfers between the two dates, inclusive, oldest first (then by id). */
    public List<WarehouseTransfer> listInPeriod(LocalDate from, LocalDate to) {
        return listTransfers("Cannot find warehouse transfers by period", "", from, to);
    }

    /** The transfers containing the product between the two dates, inclusive, oldest first (then by id). */
    public List<WarehouseTransfer> listContainingProduct(long productId, LocalDate from, LocalDate to) {
        return listTransfers("Cannot find warehouse transfers by product",
                "id IN (SELECT transfer_id FROM warehouse_transfer_line WHERE product_id = ?) AND ", productId, from, to);
    }

    /** The transfers matching the filter (its parameters come first, then from, to), dated in the period, oldest first, with their lines. */
    private List<WarehouseTransfer> listTransfers(String error, String filter, Object... params) {
        return jdbc.list(error, "SELECT id, number, from_warehouse_id, to_warehouse_id, transfer_date FROM warehouse_transfer "
                        + "WHERE " + filter + "transfer_date >= ? AND transfer_date <= ? ORDER BY transfer_date, id",
                Rows.warehouseTransfer(jdbc), params);
    }

    /**
     * The quantity of the product transferred between the two dates, inclusive, per route (from and to warehouse),
     * sorted by the codes of the from and to warehouses.
     */
    public List<TransferRoute> transferredQuantityPerRoute(long productId, LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find transferred quantity per route",
                "SELECT t.from_warehouse_id, f.code AS from_code, t.to_warehouse_id, d.code AS to_code, "
                        + "SUM(l.quantity) AS total FROM warehouse_transfer t "
                        + "JOIN warehouse_transfer_line l ON l.transfer_id = t.id "
                        + "JOIN warehouse f ON f.id = t.from_warehouse_id JOIN warehouse d ON d.id = t.to_warehouse_id "
                        + "WHERE l.product_id = ? AND t.transfer_date >= ? AND t.transfer_date <= ? "
                        + "GROUP BY t.from_warehouse_id, f.code, t.to_warehouse_id, d.code "
                        + "ORDER BY f.code, d.code, t.from_warehouse_id, t.to_warehouse_id",
                rs -> new TransferRoute(rs.getLong("from_warehouse_id"), rs.getString("from_code"),
                        rs.getLong("to_warehouse_id"), rs.getString("to_code"), rs.getLong("total")),
                productId, from, to);
    }
}
