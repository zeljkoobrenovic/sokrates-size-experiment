package com.acme.backoffice.model;

import java.time.LocalDate;

/** A stock count: the counted quantity replaced the quantity before it; the difference is logged as a movement. */
public record StockAdjustment(long id, long productId, long warehouseId, LocalDate adjustmentDate, int quantityBefore,
                              int countedQuantity, String reason) {
    public int difference() {
        return countedQuantity - quantityBefore;
    }
}
