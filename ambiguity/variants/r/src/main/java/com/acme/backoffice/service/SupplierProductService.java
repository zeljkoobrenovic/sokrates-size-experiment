package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.CategorySupplierProducts;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupplierProduct;
import com.acme.backoffice.model.SupplierStock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/** Which suppliers deliver which product. A supplier has one record per product; recording again updates it. */
public class SupplierProductService {
    private final DataStore store;

    public SupplierProductService(DataStore store) {
        this.store = store;
    }

    public SupplierProduct record(long supplierId, long productId, int leadTimeDays, BigDecimal lastUnitCost) {
        if (leadTimeDays < 0) {
            throw new IllegalArgumentException("The lead time must not be negative");
        }
        if (lastUnitCost == null || lastUnitCost.signum() < 0) {
            throw new IllegalArgumentException("The cost must not be negative");
        }
        if (store.suppliers().findById(supplierId).isEmpty()) {
            throw new IllegalArgumentException("Unknown supplier: " + supplierId);
        }
        if (store.products().findById(productId).isEmpty()) {
            throw new IllegalArgumentException("Unknown product: " + productId);
        }
        Optional<SupplierProduct> existing = store.suppliers().findSupplierProduct(supplierId, productId);
        if (existing.isPresent()) {
            SupplierProduct updated = new SupplierProduct(existing.get().id(), supplierId, productId, leadTimeDays, lastUnitCost);
            store.suppliers().updateSupplierProduct(updated);
            return updated;
        }
        return store.suppliers().createSupplierProduct(new SupplierProduct(0, supplierId, productId, leadTimeDays, lastUnitCost));
    }

    /** The suppliers of the product, lowest last unit cost first. */
    public List<SupplierProduct> listSuppliersOfProduct(long productId) {
        return store.suppliers().listSupplierProductsOfProduct(productId);
    }

    public List<SupplierProduct> listProductsOfSupplier(long supplierId) {
        return store.suppliers().listSupplierProductsOfSupplier(supplierId);
    }

    /**
     * The supplier products of the category and its subcategories, lowest last unit cost first, and the categories
     * among them with a single supplier. Fails with IllegalArgumentException for an unknown category.
     */
    public CategorySupplierProducts listSupplierProductsOfCategory(String code) {
        Category category = store.categories().findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + code));
        return new CategorySupplierProducts(category, store.suppliers().listSupplierProductsInCategoryTree(code),
                store.suppliers().listCategoriesWithSingleSupplier(code));
    }

    /**
     * The stock quantity and value of the products the supplier delivers, per warehouse. Fails with
     * IllegalArgumentException for an unknown supplier.
     */
    public SupplierStock getStockOfSupplier(long supplierId) {
        Supplier supplier = store.suppliers().findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown supplier: " + supplierId));
        return new SupplierStock(supplier, store.suppliers().stockPerWarehouse(supplierId));
    }
}
