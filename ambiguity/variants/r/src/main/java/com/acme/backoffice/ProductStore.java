package com.acme.backoffice;

import com.acme.backoffice.model.PriceChange;
import com.acme.backoffice.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The product catalogue: creating and updating products, activating and deactivating them, their weight, catalogue
 * price changes with their history, and the product searches (active products by name, category or price range).
 * Category trees are in {@link CategoryStore}, bundles in {@link BundleStore}.
 */
public final class ProductStore {
    /** Condition of the active-product searches; they are all ordered by name, then id. */
    private static final String ACTIVE = "p.active = TRUE";

    private final Jdbc jdbc;

    ProductStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public Product create(Product product) {
        long id = jdbc.insert("Cannot create product",
                "INSERT INTO product (sku, name, category, unit_price, unit_of_measure, active) VALUES (?, ?, ?, ?, ?, ?)",
                product.sku(), product.name(), product.category(), product.unitPrice(), product.unitOfMeasure(),
                product.active());
        return new Product(id, product.sku(), product.name(), product.category(), product.unitPrice(),
                product.unitOfMeasure(), product.active());
    }

    public void update(Product product) {
        jdbc.update("Cannot update product",
                "UPDATE product SET sku = ?, name = ?, category = ?, unit_price = ?, unit_of_measure = ?, active = ? WHERE id = ?",
                product.sku(), product.name(), product.category(), product.unitPrice(), product.unitOfMeasure(),
                product.active(), product.id());
    }

    /** Deactivates or reactivates the product; returns false if it does not exist or already has that state. */
    public boolean setActive(long productId, boolean active) {
        return jdbc.update("Cannot change product active state", "UPDATE product SET active = ? WHERE id = ? AND active <> ?",
                active, productId, active) > 0;
    }

    /**
     * Sets the catalogue price and records the change (old and new price) in the history, all or nothing. Returns null
     * if the price is unchanged. An unknown product rolls back and throws {@code IllegalStateException("Unknown product: id")}.
     */
    public PriceChange changePrice(long productId, LocalDate date, BigDecimal newPrice) {
        return jdbc.inTransaction("Cannot change product price", () -> {
            List<BigDecimal> prices = jdbc.listRaw("SELECT unit_price FROM product WHERE id = ?",
                    rs -> rs.getBigDecimal("unit_price"), productId);
            if (prices.isEmpty()) {
                jdbc.connection().rollback();
                throw new IllegalStateException("Unknown product: " + productId);
            }
            BigDecimal before = prices.get(0);
            if (before.compareTo(newPrice) == 0) {
                return null;
            }
            jdbc.updateRaw("UPDATE product SET unit_price = ? WHERE id = ?", newPrice, productId);
            long id = jdbc.insertRaw(
                    "INSERT INTO product_price_change (product_id, change_date, old_price, new_price) VALUES (?, ?, ?, ?)",
                    productId, date, before, newPrice);
            return new PriceChange(id, productId, date, before, newPrice);
        });
    }

    /** The price changes of the product, oldest first (then by id). */
    public List<PriceChange> listPriceChanges(long productId) {
        return jdbc.list("Cannot find price changes by product",
                "SELECT id, product_id, change_date, old_price, new_price FROM product_price_change "
                        + "WHERE product_id = ? ORDER BY change_date, id",
                rs -> new PriceChange(rs.getLong("id"), rs.getLong("product_id"),
                        rs.getObject("change_date", LocalDate.class), rs.getBigDecimal("old_price"),
                        rs.getBigDecimal("new_price")),
                productId);
    }

    /** The products (active or not) with a price change between the two dates, inclusive, by name (then by id). */
    public List<Product> listWithPriceChange(LocalDate from, LocalDate to) {
        return CatalogSql.listProductsByName(jdbc, "Cannot find products with price change",
                "EXISTS (SELECT 1 FROM product_price_change c WHERE c.product_id = p.id AND c.change_date >= ? AND c.change_date <= ?)",
                from, to);
    }

    /** Sets the weight of the product, inserting or replacing it. */
    public void saveWeight(long productId, BigDecimal weightKg) {
        jdbc.update("Cannot save product weight", "MERGE INTO product_weight (product_id, weight_kg) KEY (product_id) VALUES (?, ?)",
                productId, weightKg);
    }

    public Optional<Product> findBySku(String sku) {
        return find("p.sku = ?", sku);
    }

    public Optional<Product> findById(long id) {
        return find("p.id = ?", id);
    }

    /** The active products, by name (then by id). */
    public List<Product> listActive() {
        return CatalogSql.listProductsByName(jdbc, "Cannot list active products", ACTIVE);
    }

    /** The active products whose name contains the text, ignoring case (% and _ match literally), by name (then by id). */
    public List<Product> searchActiveByName(String namePart) {
        String escaped = namePart.toLowerCase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return CatalogSql.listProductsByName(jdbc, "Cannot search products by name",
                ACTIVE + " AND LOWER(p.name) LIKE ? ESCAPE '\\'", "%" + escaped + "%");
    }

    /** The active products of exactly this category (not the categories below it), by name (then by id). */
    public List<Product> searchActiveByCategory(String category) {
        return CatalogSql.listProductsByName(jdbc, "Cannot search products by category", ACTIVE + " AND p.category = ?",
                category);
    }

    /** The active products with a unit price between the two prices, inclusive, by name (then by id). */
    public List<Product> searchActiveByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return CatalogSql.listProductsByName(jdbc, "Cannot search products by price range",
                ACTIVE + " AND p.unit_price >= ? AND p.unit_price <= ?", minPrice, maxPrice);
    }

    private Optional<Product> find(String condition, Object value) {
        return jdbc.first("Cannot find product", CatalogSql.SELECT_PRODUCTS + " WHERE " + condition, Rows::product, value);
    }
}
