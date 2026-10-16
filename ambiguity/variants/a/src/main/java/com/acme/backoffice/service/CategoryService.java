package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.Product;

import java.util.List;

/** Product categories: a code, a name and an optional parent category. */
public class CategoryService {
    private final DataStore store;

    public CategoryService(DataStore store) {
        this.store = store;
    }

    /** Creates a category. A null or blank parent code means a top-level category; the parent must already exist. */
    public Category createCategory(String code, String name, String parentCode) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Category code is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (store.findCategoryByCode(code).isPresent()) {
            throw new IllegalArgumentException("Duplicate category code: " + code);
        }
        String parent = parentCode == null || parentCode.isBlank() ? null : parentCode;
        if (parent != null && store.findCategoryByCode(parent).isEmpty()) {
            throw new IllegalArgumentException("Unknown parent category: " + parent);
        }
        return store.createCategory(new Category(code, name, parent));
    }

    /** All categories ordered by code. */
    public List<Category> listCategories() {
        return store.listCategories();
    }

    /** The direct children of a category, ordered by code. */
    public List<Category> listChildren(String code) {
        return store.listChildCategories(code);
    }

    /** The active products in the category and in all categories below it. */
    public List<Product> listProducts(String code) {
        return store.listProductsInCategoryTree(code);
    }
}
