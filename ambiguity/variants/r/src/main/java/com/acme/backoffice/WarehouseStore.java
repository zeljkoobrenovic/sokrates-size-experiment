package com.acme.backoffice;

import com.acme.backoffice.model.Warehouse;

import java.util.List;
import java.util.Optional;

/**
 * Warehouses (code, name, city, region): creating them, listing them by code and finding one by its code. The stock
 * they hold is in {@link StockStore}, their movements and adjustments in {@link StockMovementStore} and the transfers
 * between them in {@link WarehouseTransferStore}.
 */
public final class WarehouseStore {
    private static final String SELECT_WAREHOUSE = "SELECT id, code, name, city, region FROM warehouse ";

    private final Jdbc jdbc;

    WarehouseStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public Warehouse create(Warehouse warehouse) {
        long id = jdbc.insert("Cannot create warehouse", "INSERT INTO warehouse (code, name, city, region) VALUES (?, ?, ?, ?)",
                warehouse.code(), warehouse.name(), warehouse.city(), warehouse.region().name());
        return new Warehouse(id, warehouse.code(), warehouse.name(), warehouse.city(), warehouse.region());
    }

    /** All warehouses, sorted by code (then by id). */
    public List<Warehouse> listByCode() {
        return jdbc.list("Cannot list warehouses", SELECT_WAREHOUSE + "ORDER BY code, id", Rows::warehouse);
    }

    public Optional<Warehouse> findByCode(String code) {
        return jdbc.first("Cannot find warehouse", SELECT_WAREHOUSE + "WHERE code = ?", Rows::warehouse, code);
    }
}
