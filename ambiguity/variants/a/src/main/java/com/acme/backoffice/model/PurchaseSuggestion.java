package com.acme.backoffice.model;

import java.math.BigDecimal;

/**
 * A suggested purchase for a product in a warehouse whose stock is at or below its reorder level: the quantity needed to
 * reach twice the reorder level, and the supplier with the lowest last unit cost for the product. Supplier and unit cost
 * are null if no supplier delivers the product.
 */
public record PurchaseSuggestion(Warehouse warehouse, Product product, int quantity, int reorderLevel, int suggestedQuantity,
                                 Supplier supplier, BigDecimal unitCost) {
}
