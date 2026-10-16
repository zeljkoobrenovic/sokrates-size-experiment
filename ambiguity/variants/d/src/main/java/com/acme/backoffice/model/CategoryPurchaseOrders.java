package com.acme.backoffice.model;

import java.util.List;

/** The purchase orders containing products of a category in a period, and the spend per category. */
public record CategoryPurchaseOrders(Category category, List<PurchaseOrder> orders, List<CategorySpend> spend) {
}
