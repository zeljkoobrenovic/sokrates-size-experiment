package com.acme.backoffice.model;

/** The number of purchase orders for the warehouse regions of the customers of a sales rep in a period. */
public record SalesRepPurchaseOrderCount(long salesRepId, String name, int purchaseOrderCount) {
}
