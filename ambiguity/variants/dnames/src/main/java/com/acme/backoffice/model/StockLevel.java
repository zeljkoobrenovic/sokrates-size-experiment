package com.acme.backoffice.model;

/** A product with a stock quantity, either in one warehouse or in total, depending on the query. */
public record StockLevel(Product product, int quantity) {
}
