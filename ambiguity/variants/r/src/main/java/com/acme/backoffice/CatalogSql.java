package com.acme.backoffice;

import com.acme.backoffice.model.Product;

import java.util.List;

/**
 * SQL shared by the catalogue stores ({@link ProductStore}, {@link CategoryStore}, {@link BundleStore},
 * {@link SupplierStore}): the product list ordered by name and the recursive category tree (a category and every
 * category below it).
 */
final class CatalogSql {
    private CatalogSql() {
    }

    /**
     * Prefix of a query that defines {@code tree (code)}: the category bound to the first parameter and all categories
     * below it. Use as {@code CATEGORY_TREE + "SELECT ... WHERE p.category IN (SELECT code FROM tree)"}.
     */
    static final String CATEGORY_TREE = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
            + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) ";

    /** Condition on {@code p.category}: the product is in the category tree defined by {@link #CATEGORY_TREE}. */
    static final String IN_CATEGORY_TREE = "p.category IN (SELECT code FROM tree)";

    /** The SELECT of all product columns from {@code product p}, without conditions. */
    static final String SELECT_PRODUCTS = "SELECT " + Rows.PRODUCT_COLUMNS + " FROM product p";

    /** The products (alias {@code p}) matching the condition, ordered by name, then id. */
    static List<Product> listProductsByName(Jdbc jdbc, String error, String condition, Object... params) {
        return listProductsByNameWithCte(jdbc, error, "", condition, params);
    }

    /**
     * The products (alias {@code p}) matching the condition, ordered by name, then id; {@code with} is a WITH clause
     * put before the SELECT (such as {@link #CATEGORY_TREE}) or empty. (Not an overload of
     * {@link #listProductsByName}: a String parameter would silently bind to {@code condition}.)
     */
    static List<Product> listProductsByNameWithCte(Jdbc jdbc, String error, String with, String condition, Object... params) {
        return jdbc.list(error, with + SELECT_PRODUCTS + " WHERE " + condition + " ORDER BY p.name, p.id",
                Rows::product, params);
    }
}
