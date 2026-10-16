package com.acme.backoffice.model;

import java.math.BigDecimal;

/**
 * A product that a supplier delivers, with the lead time in days and the last unit cost paid. The id is 0 until the
 * record has been created in the database.
 */
public record SupplierProduct(long id, long supplierId, long productId, int leadTimeDays, BigDecimal lastUnitCost) {
}
