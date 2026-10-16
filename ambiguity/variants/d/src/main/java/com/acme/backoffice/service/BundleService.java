package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.BundleComponent;
import com.acme.backoffice.model.Product;

import java.util.List;

/** Bundle products, made of other products in fixed quantities. Shipping a bundle takes the stock of its components. */
public class BundleService {
    private final DataStore store;

    public BundleService(DataStore store) {
        this.store = store;
    }

    /**
     * Adds the component to the bundle. Bundles cannot be nested: fails with IllegalStateException if the component is a bundle,
     * the bundle is itself a component of another bundle, or the component is already part of the bundle.
     */
    public void addComponent(long bundleId, long componentId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + quantity);
        }
        if (bundleId == componentId) {
            throw new IllegalArgumentException("A bundle cannot contain itself");
        }
        store.findProductById(bundleId).orElseThrow(() -> new IllegalArgumentException("Unknown product: " + bundleId));
        store.findProductById(componentId).orElseThrow(() -> new IllegalArgumentException("Unknown product: " + componentId));
        if (!store.findBundleComponents(componentId).isEmpty()) {
            throw new IllegalStateException("Product " + componentId + " is a bundle and cannot be a component");
        }
        if (!store.findBundlesContainingComponent(bundleId).isEmpty()) {
            throw new IllegalStateException("Product " + bundleId + " is a component and cannot be a bundle");
        }
        if (store.findBundleComponents(bundleId).stream().anyMatch(c -> c.component().id() == componentId)) {
            throw new IllegalStateException("Product " + componentId + " is already a component of " + bundleId);
        }
        store.createBundleComponent(bundleId, componentId, quantity);
    }

    /** The bundles, ordered by name. */
    public List<Product> listBundles() {
        return store.findBundles();
    }

    /** The components of the bundle, ordered by name; empty if the product is not a bundle. */
    public List<BundleComponent> getComponents(long bundleId) {
        return store.findBundleComponents(bundleId);
    }

    /** How many bundles can be built from the stock of the warehouse. */
    public int getQuantity(long warehouseId, long bundleId) {
        return store.countBuildableBundlesInWarehouse(warehouseId, bundleId);
    }

    /** How many bundles can be built in total, counting each warehouse on its own stock, as a bundle ships from one warehouse. */
    public int getTotalQuantity(long bundleId) {
        return store.countBuildableBundlesInAllWarehouses(bundleId);
    }
}
