package com.acme.backoffice.model;

import java.util.List;

/** The purchase orders received late into a warehouse in a period. */
public record WarehouseLatePurchaseOrders(Warehouse warehouse, List<LatePurchaseOrder> late) {
}
