package com.acme.backoffice;

import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * The product category tree: categories identified by code with an optional parent category, their children, and the
 * active products in a category or any category below it.
 */
public final class CategoryStore {
    private static final String SELECT_CATEGORIES = "SELECT code, name, parent_code FROM category";

    private final Jdbc jdbc;

    CategoryStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    /** Inserts the category; returns it unchanged (the code is the key). */
    public Category create(Category category) {
        jdbc.update("Cannot create category", "INSERT INTO category (code, name, parent_code) VALUES (?, ?, ?)",
                category.code(), category.name(), category.parentCode());
        return category;
    }

    public Optional<Category> findByCode(String code) {
        return jdbc.first("Cannot find category", SELECT_CATEGORIES + " WHERE code = ?", CategoryStore::category, code);
    }

    /** All categories, by code. */
    public List<Category> list() {
        return jdbc.list("Cannot list categories", SELECT_CATEGORIES + " ORDER BY code", CategoryStore::category);
    }

    /** The direct children of the category, by code. */
    public List<Category> listChildren(String parentCode) {
        return jdbc.list("Cannot list child categories", SELECT_CATEGORIES + " WHERE parent_code = ? ORDER BY code",
                CategoryStore::category, parentCode);
    }

    /** The active products in the category or in any category below it, by name (then by id). */
    public List<Product> listProductsInTree(String code) {
        return CatalogSql.listProductsByNameWithCte(jdbc, "Cannot list products in category tree", CatalogSql.CATEGORY_TREE,
                "p.active = TRUE AND " + CatalogSql.IN_CATEGORY_TREE, code);
    }

    private static Category category(ResultSet rs) throws SQLException {
        return new Category(rs.getString("code"), rs.getString("name"), rs.getString("parent_code"));
    }
}
