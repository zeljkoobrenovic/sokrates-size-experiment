package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.LowStockAlert;
import com.acme.backoffice.model.PurchaseSuggestion;

import java.util.List;

/** Reorder levels per product and warehouse, and the low stock alerts derived from them. */
public class ReorderService {
    private final DataStore store;

    public ReorderService(DataStore store) {
        this.store = store;
    }

    public void setReorderLevel(long warehouseId, long productId, int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Reorder level must not be negative: " + level);
        }
        store.setReorderLevel(warehouseId, productId, level);
    }

    /** The products of the warehouse with stock at or below their reorder level; no stock counts as zero. */
    public List<LowStockAlert> getLowStock(long warehouseId) {
        return store.listLowStockAlertsInWarehouse(warehouseId);
    }

    /** The low stock alerts of all warehouses, ordered by warehouse code and product name. */
    public List<LowStockAlert> getLowStock() {
        return store.listLowStockAlertsInAllWarehouses();
    }

    /**
     * Purchase suggestions for the warehouse: for each product at or below its reorder level, the quantity needed to reach
     * twice the reorder level, with the supplier of lowest unit cost (none if no supplier delivers the product).
     */
    public List<PurchaseSuggestion> getPurchaseSuggestions(long warehouseId) {
        return store.listPurchaseSuggestionsForWarehouse(warehouseId);
    }
}
