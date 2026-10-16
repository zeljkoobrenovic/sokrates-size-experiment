package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.StockAdjustment;
import com.acme.backoffice.model.WarehouseAdjustments;

import java.time.LocalDate;
import java.util.List;

/** Corrections of the stock after a count. */
public class StockAdjustmentService {
    private final DataStore store;

    public StockAdjustmentService(DataStore store) {
        this.store = store;
    }

    /** Sets the stock to the counted quantity and logs the difference as an adjustment movement, all or nothing. */
    public StockAdjustment adjust(long productId, long warehouseId, LocalDate date, int countedQuantity, String reason) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (countedQuantity < 0) {
            throw new IllegalArgumentException("Counted quantity must not be negative: " + countedQuantity);
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason is required");
        }
        return store.createStockAdjustment(new StockAdjustment(0, productId, warehouseId, date, 0, countedQuantity, reason));
    }

    /** The adjustments in the warehouse between the two dates, both included, oldest first. */
    public List<StockAdjustment> getWarehouseAdjustments(long warehouseId, LocalDate from, LocalDate to) {
        return store.findStockAdjustmentsByWarehouse(warehouseId, from, to);
    }

    /**
     * The adjustments that lowered the stock between the two dates, both included, per warehouse, biggest loss first.
     * Fails with IllegalArgumentException if a date is missing or the dates are in the wrong order.
     */
    public List<WarehouseAdjustments> getNegativeAdjustments(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before start date: " + to + " < " + from);
        }
        return store.findNegativeStockAdjustmentsPerWarehouse(from, to);
    }
}
