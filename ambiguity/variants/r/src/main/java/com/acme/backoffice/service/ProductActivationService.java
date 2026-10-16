package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;

/** Deactivates and reactivates products; inactive products are hidden from searches, price lists and stock alerts. */
public class ProductActivationService {
    private final DataStore store;

    public ProductActivationService(DataStore store) {
        this.store = store;
    }

    /** Deactivates the product; returns false if it was already inactive. */
    public boolean deactivate(long productId) {
        requireProduct(productId);
        return store.products().setActive(productId, false);
    }

    /** Reactivates the product; returns false if it was already active. */
    public boolean reactivate(long productId) {
        requireProduct(productId);
        return store.products().setActive(productId, true);
    }

    private void requireProduct(long productId) {
        if (store.products().findById(productId).isEmpty()) {
            throw new IllegalArgumentException("Unknown product " + productId);
        }
    }
}
