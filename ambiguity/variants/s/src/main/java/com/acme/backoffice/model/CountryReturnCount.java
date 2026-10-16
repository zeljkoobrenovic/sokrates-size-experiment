package com.acme.backoffice.model;

/** The number of returns for the orders of the customers of a country dated in a period. */
public record CountryReturnCount(String country, int returnCount) {
}
