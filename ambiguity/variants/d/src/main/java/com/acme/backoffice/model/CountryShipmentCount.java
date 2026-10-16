package com.acme.backoffice.model;

/** The number of shipments for the orders of the customers of a country shipped in a period. */
public record CountryShipmentCount(String country, int shipmentCount) {
}
