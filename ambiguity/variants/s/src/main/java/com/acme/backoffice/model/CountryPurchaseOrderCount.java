package com.acme.backoffice.model;

/** The number of purchase orders for the warehouse regions of the customers of a country in a period. */
public record CountryPurchaseOrderCount(String country, int purchaseOrderCount) {
}
