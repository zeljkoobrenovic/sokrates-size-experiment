package com.acme.backoffice;

import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.Warehouse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Row helpers shared by the inventory stores ({@link StockStore}, {@link StockMovementStore}): a warehouse read under
 * aliased column names (when it is joined with another table that also has {@code id} or {@code name}), and the
 * grouping of rows per warehouse.
 */
final class InventoryRows {
    private InventoryRows() {
    }

    /** Columns: warehouse_id, code, name, city, region (a warehouse joined with rows that have their own id). */
    static Warehouse warehouseWithIdAlias(ResultSet rs) throws SQLException {
        return new Warehouse(rs.getLong("warehouse_id"), rs.getString("code"), rs.getString("name"), rs.getString("city"),
                Region.valueOf(rs.getString("region")));
    }

    /** Columns: warehouse_id, code, warehouse_name, city, region (a warehouse joined with a product). */
    static Warehouse warehouseWithIdAndNameAlias(ResultSet rs) throws SQLException {
        return new Warehouse(rs.getLong("warehouse_id"), rs.getString("code"), rs.getString("warehouse_name"),
                rs.getString("city"), Region.valueOf(rs.getString("region")));
    }

    /** One row of a per-warehouse listing: the warehouse and one item of it. */
    record WarehouseItem<T>(Warehouse warehouse, T item) {
    }

    /**
     * Groups rows sorted by warehouse into one group per warehouse, in row order; warehouses without rows are absent.
     */
    static <T, G> List<G> groupPerWarehouse(List<WarehouseItem<T>> rows, BiFunction<Warehouse, List<T>, G> group) {
        List<G> result = new ArrayList<>();
        Warehouse current = null;
        List<T> items = null;
        for (WarehouseItem<T> row : rows) {
            if (current == null || current.id() != row.warehouse().id()) {
                current = row.warehouse();
                items = new ArrayList<>();
                result.add(group.apply(current, items));
            }
            items.add(row.item());
        }
        return result;
    }
}
