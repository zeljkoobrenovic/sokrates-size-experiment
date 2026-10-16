package com.acme.backoffice.model;

import java.util.List;

/** The returns received into a warehouse in a period, and the returned quantity per product. */
public record WarehouseReturns(Warehouse warehouse, List<WarehouseReturn> returns, List<ProductReturnedQuantity> quantities) {
}
