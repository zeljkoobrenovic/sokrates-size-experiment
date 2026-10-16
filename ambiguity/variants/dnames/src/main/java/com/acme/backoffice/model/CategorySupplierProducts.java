package com.acme.backoffice.model;

import java.util.List;

/** The supplier products of a category, cheapest first, and the categories with a single supplier. */
public record CategorySupplierProducts(Category category, List<SupplierProduct> supplierProducts, List<CategorySupplier> singleSupplier) {
}
