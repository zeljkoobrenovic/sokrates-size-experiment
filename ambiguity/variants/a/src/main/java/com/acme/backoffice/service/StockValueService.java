package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.WarehouseStockValue;

import java.math.BigDecimal;
import java.util.List;

/**
 * The value of the stock: quantity times the product's last purchase unit cost. The last purchase is the most recent
 * sent or received purchase order with the product; a product never purchased counts with a cost of zero.
 */
public class StockValueService {
    private final DataStore store;

    public StockValueService(DataStore store) {
        this.store = store;
    }

    /** The stock value of every warehouse, including those without stock, ordered by warehouse code. */
    public List<WarehouseStockValue> getValuePerWarehouse() {
        return store.listStockValueByWarehouse();
    }

    /** The stock value summed over all warehouses. */
    public BigDecimal getTotalValue() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (WarehouseStockValue entry : store.listStockValueByWarehouse()) {
            total = total.add(entry.value());
        }
        return total;
    }
}
