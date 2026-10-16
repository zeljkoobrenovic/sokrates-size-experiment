package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.ReasonMovements;
import com.acme.backoffice.model.StockLevel;
import com.acme.backoffice.model.StockMovement;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.WarehouseMovements;

import java.time.LocalDate;
import java.util.List;

/** Stock levels per product and warehouse. A quantity never goes below zero. */
public class StockService {
    private final DataStore store;

    public StockService(DataStore store) {
        this.store = store;
    }

    public void setQuantity(long warehouseId, long productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must not be negative: " + quantity);
        }
        store.stock().setQuantity(warehouseId, productId, quantity);
    }

    public void addQuantity(long warehouseId, long productId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative: " + amount);
        }
        store.stock().addQuantity(warehouseId, productId, amount);
    }

    /** Fails with IllegalStateException, changing nothing, if there are fewer units than the amount. */
    public void takeQuantity(long warehouseId, long productId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative: " + amount);
        }
        if (!store.stock().removeQuantity(warehouseId, productId, amount)) {
            throw new IllegalStateException("Not enough stock: cannot take " + amount + " units, only "
                    + store.stock().findQuantity(warehouseId, productId) + " available");
        }
    }

    /** Sets the quantity and logs the difference to the earlier quantity as an adjustment on the date. */
    public void setQuantity(long warehouseId, long productId, int quantity, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        int before = store.stock().findQuantity(warehouseId, productId);
        setQuantity(warehouseId, productId, quantity);
        if (quantity != before) {
            store.stockMovements().create(productId, warehouseId, date, quantity - before, StockMovementReason.ADJUSTMENT);
        }
    }

    /** Adds the amount and logs it as an adjustment on the date. */
    public void addQuantity(long warehouseId, long productId, int amount, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        addQuantity(warehouseId, productId, amount);
        if (amount > 0) {
            store.stockMovements().create(productId, warehouseId, date, amount, StockMovementReason.ADJUSTMENT);
        }
    }

    /** Takes the amount and logs it as an adjustment on the date; fails like the version without a date. */
    public void takeQuantity(long warehouseId, long productId, int amount, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        takeQuantity(warehouseId, productId, amount);
        if (amount > 0) {
            store.stockMovements().create(productId, warehouseId, date, -amount, StockMovementReason.ADJUSTMENT);
        }
    }

    /** The movements of the product in all warehouses between the two dates, both included, oldest first. */
    public List<StockMovement> getProductMovements(long productId, LocalDate from, LocalDate to) {
        return store.stockMovements().listOfProduct(productId, from, to);
    }

    /** The movements of all products in the warehouse between the two dates, both included, oldest first. */
    public List<StockMovement> getWarehouseMovements(long warehouseId, LocalDate from, LocalDate to) {
        return store.stockMovements().listInWarehouse(warehouseId, from, to);
    }

    /** The movements of the product in the warehouse between the two dates, both included, oldest first. */
    public List<StockMovement> getMovements(long productId, long warehouseId, LocalDate from, LocalDate to) {
        return store.stockMovements().listOfProductInWarehouse(productId, warehouseId, from, to);
    }

    /**
     * The number and net quantity change of the movements between the two dates, both included, per reason, warehouse
     * and product category. Fails with IllegalArgumentException if a date is missing or the dates are in the wrong order.
     */
    public List<ReasonMovements> getMovementsPerReason(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before start date: " + to + " < " + from);
        }
        return store.stockMovements().countAndQuantityPerReasonWarehouseAndCategory(from, to);
    }

    /**
     * The movements of the products of the category between the two dates, both included, per warehouse. Fails with
     * IllegalArgumentException if the category or a date is missing or the dates are in the wrong order.
     */
    public List<WarehouseMovements> getMovementsOfCategory(String category, LocalDate from, LocalDate to) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category is required");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before start date: " + to + " < " + from);
        }
        return store.stockMovements().listOfCategoryPerWarehouse(category, from, to);
    }

    public int getQuantity(long warehouseId, long productId) {
        return store.stock().findQuantity(warehouseId, productId);
    }

    /** The stock of the product summed over all warehouses. */
    public int getTotalQuantity(long productId) {
        return store.stock().findTotalQuantity(productId);
    }

    /** The products stocked in the warehouse with their quantities, ordered by product name. */
    public List<StockLevel> getWarehouseStock(long warehouseId) {
        return store.stock().listInWarehouse(warehouseId);
    }

    /** The products whose total stock is below the threshold, including those without any stock. */
    public List<StockLevel> getProductsBelow(int threshold) {
        return store.stock().listActiveProductsWithTotalBelow(threshold);
    }
}
