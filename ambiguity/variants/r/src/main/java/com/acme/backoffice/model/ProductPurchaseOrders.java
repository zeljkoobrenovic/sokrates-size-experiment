package com.acme.backoffice.model;

import java.util.List;

/** The purchase orders containing a product in a period, and the purchased quantity of the product per supplier. */
public record ProductPurchaseOrders(Product product, List<PurchaseOrder> orders, List<SupplierPurchasedQuantity> quantities) {
}
