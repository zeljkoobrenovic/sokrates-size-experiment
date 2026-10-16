package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.CountryPurchaseOrderCount;
import com.acme.backoffice.model.PurchaseOrder;

import java.time.LocalDate;
import java.util.List;

/**
 * The purchase orders for the warehouse regions of the customers of a country dated between two dates, and the number
 * of purchase orders per country.
 */
public class CountryPurchaseOrdersService {
    private final DataStore store;

    public CountryPurchaseOrdersService(DataStore store) {
        this.store = store;
    }

    public List<PurchaseOrder> purchaseOrders(String country, LocalDate from, LocalDate to) {
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country is required");
        }
        checkRange(from, to);
        return store.findPurchaseOrdersByCountry(country, from, to);
    }

    public List<CountryPurchaseOrderCount> countPerCountry(LocalDate from, LocalDate to) {
        checkRange(from, to);
        return store.findPurchaseOrderCountPerCountry(from, to);
    }

    private void checkRange(LocalDate from, LocalDate to) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
    }
}
