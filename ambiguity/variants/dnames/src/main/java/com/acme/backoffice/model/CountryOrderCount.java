package com.acme.backoffice.model;

/** The number of orders of the customers of a country placed in a period. */
public record CountryOrderCount(String country, int orderCount) {
}
