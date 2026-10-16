package com.acme.backoffice.model;

import java.util.List;

/** The open orders a warehouse can ship in full from its own stock, and those it cannot, each oldest first. */
public record WarehouseOrderFulfillment(Warehouse warehouse, List<SalesOrder> shippable, List<SalesOrder> notShippable) {
}
