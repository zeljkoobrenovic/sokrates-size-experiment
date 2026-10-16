package com.acme.backoffice.model;

/** The number of customers of a country created in a period. */
public record CountryCustomerCount(String country, int customerCount) {
}
