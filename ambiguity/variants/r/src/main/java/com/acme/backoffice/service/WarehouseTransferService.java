package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.TransferRoute;
import com.acme.backoffice.model.WarehouseTransfer;
import com.acme.backoffice.model.WarehouseTransferLine;

import java.time.LocalDate;
import java.util.List;

/** Moves of stock between warehouses. */
public class WarehouseTransferService {
    private final DataStore store;

    public WarehouseTransferService(DataStore store) {
        this.store = store;
    }

    /** Takes the stock from one warehouse and adds it to the other, logging both movements, all or nothing. */
    public WarehouseTransfer transfer(String number, long fromWarehouseId, long toWarehouseId, LocalDate date,
                                      List<WarehouseTransferLine> lines) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Number is required");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (fromWarehouseId == toWarehouseId) {
            throw new IllegalArgumentException("A transfer needs two different warehouses");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("A transfer needs at least one line");
        }
        for (WarehouseTransferLine line : lines) {
            if (line.quantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be positive: " + line.quantity());
            }
        }
        return store.transfers().create(new WarehouseTransfer(0, number, fromWarehouseId, toWarehouseId, date, lines));
    }

    /** The transfers from or to the warehouse between the two dates, both included, oldest first. */
    public List<WarehouseTransfer> listByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return store.transfers().listFromOrToWarehouse(warehouseId, from, to);
    }

    /** The transfers between the two dates, both included, oldest first. */
    public List<WarehouseTransfer> listByPeriod(LocalDate from, LocalDate to) {
        return store.transfers().listInPeriod(from, to);
    }

    /**
     * The transfers containing the product between the two dates, both included, oldest first.
     * Fails with IllegalArgumentException if a date is missing or the dates are in the wrong order.
     */
    public List<WarehouseTransfer> listByProduct(long productId, LocalDate from, LocalDate to) {
        checkDates(from, to);
        return store.transfers().listContainingProduct(productId, from, to);
    }

    /** The quantity of the product transferred between the two dates, both included, per route. */
    public List<TransferRoute> quantityPerRoute(long productId, LocalDate from, LocalDate to) {
        checkDates(from, to);
        return store.transfers().transferredQuantityPerRoute(productId, from, to);
    }

    private void checkDates(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before start date: " + to + " < " + from);
        }
    }
}
