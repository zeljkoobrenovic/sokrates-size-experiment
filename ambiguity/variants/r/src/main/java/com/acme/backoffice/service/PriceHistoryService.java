package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.PriceChange;
import com.acme.backoffice.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Changes of the catalogue price, with their history. */
public class PriceHistoryService {
    private final DataStore store;

    public PriceHistoryService(DataStore store) {
        this.store = store;
    }

    /** Sets the catalogue price and records the change; a change to the same price records nothing and returns null. */
    public PriceChange changePrice(long productId, LocalDate date, BigDecimal newPrice) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (newPrice == null || newPrice.signum() < 0) {
            throw new IllegalArgumentException("Price must not be negative: " + newPrice);
        }
        return store.products().changePrice(productId, date, newPrice);
    }

    /** The price changes of the product, oldest first. */
    public List<PriceChange> getHistory(long productId) {
        return store.products().listPriceChanges(productId);
    }

    /** The products with a price change between the two dates, both included, by name. */
    public List<Product> getProductsWithPriceChange(LocalDate from, LocalDate to) {
        return store.products().listWithPriceChange(from, to);
    }
}
