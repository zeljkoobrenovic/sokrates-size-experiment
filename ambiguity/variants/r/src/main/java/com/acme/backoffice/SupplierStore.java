package com.acme.backoffice;

import com.acme.backoffice.model.CategorySupplier;
import com.acme.backoffice.model.CountrySpending;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupplierPerformance;
import com.acme.backoffice.model.SupplierProduct;
import com.acme.backoffice.model.SupplierSpending;
import com.acme.backoffice.model.SupplierWarehouseStock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Suppliers and what they deliver: suppliers (by name, country, minimum rating), supplier products (the lead time and
 * last unit cost of a product from a supplier), the categories with a single supplier, the stock per warehouse of the
 * products of a supplier, and the supplier reports on received purchase orders (performance against the expected date,
 * spending per supplier and per supplier country).
 */
public final class SupplierStore {
    private static final String SELECT_SUPPLIERS = "SELECT id, name, contact_email, country, rating FROM supplier";

    private static final String SELECT_SUPPLIER_PRODUCTS =
            "SELECT sp.id, sp.supplier_id, sp.product_id, sp.lead_time_days, sp.last_unit_cost FROM supplier_product sp";

    /** The suppliers {@code s} with their purchase orders {@code o}; shared by the performance and spending reports. */
    private static final String FROM_SUPPLIER_ORDERS = "FROM supplier s JOIN purchase_order o ON o.supplier_id = s.id ";

    /** The orders received between the two dates (first two parameters), inclusive. */
    private static final String WHERE_RECEIVED_BETWEEN =
            "WHERE o.status = 'RECEIVED' AND o.received_date >= ? AND o.received_date <= ? ";

    /** Received orders joined with their lines {@code l}, for the spending reports (an order without lines counts with value 0). */
    private static final String RECEIVED_ORDER_LINES_BETWEEN = FROM_SUPPLIER_ORDERS
            + "LEFT JOIN purchase_order_line l ON l.order_id = o.id " + WHERE_RECEIVED_BETWEEN;

    /** Order count and value (quantity times unit cost of the lines) of the spending reports. */
    private static final String SPENDING_TOTALS = "COUNT(DISTINCT o.id) AS order_count, "
            + "COALESCE(SUM(l.quantity * l.unit_cost), 0) AS total ";

    private static final String SUPPLIER_GROUP = "s.id, s.name, s.contact_email, s.country, s.rating";

    private final Jdbc jdbc;

    SupplierStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    // --- suppliers ---

    public Supplier create(Supplier supplier) {
        long id = jdbc.insert("Cannot create supplier", "INSERT INTO supplier (name, contact_email, country, rating) VALUES (?, ?, ?, ?)",
                supplier.name(), supplier.contactEmail(), supplier.country(), supplier.rating());
        return new Supplier(id, supplier.name(), supplier.contactEmail(), supplier.country(), supplier.rating());
    }

    public void update(Supplier supplier) {
        jdbc.update("Cannot update supplier", "UPDATE supplier SET name = ?, contact_email = ?, country = ?, rating = ? WHERE id = ?",
                supplier.name(), supplier.contactEmail(), supplier.country(), supplier.rating(), supplier.id());
    }

    public Optional<Supplier> findById(long id) {
        return jdbc.first("Cannot find supplier", SELECT_SUPPLIERS + " WHERE id = ?", Rows::supplier, id);
    }

    /** All suppliers, by name (then by id). */
    public List<Supplier> list() {
        return listByName("Cannot list suppliers", "");
    }

    /** The suppliers of the country, by name (then by id). */
    public List<Supplier> listInCountry(String country) {
        return listByName("Cannot list suppliers by country", "WHERE country = ? ", country);
    }

    /** The suppliers with at least the given rating, by name (then by id). */
    public List<Supplier> listWithMinimumRating(int minimumRating) {
        return listByName("Cannot list suppliers by rating", "WHERE rating >= ? ", minimumRating);
    }

    private List<Supplier> listByName(String error, String where, Object... params) {
        return jdbc.list(error, SELECT_SUPPLIERS + " " + where + "ORDER BY name, id", Rows::supplier, params);
    }

    // --- supplier products ---

    public SupplierProduct createSupplierProduct(SupplierProduct supplierProduct) {
        long id = jdbc.insert("Cannot create supplier product",
                "INSERT INTO supplier_product (supplier_id, product_id, lead_time_days, last_unit_cost) VALUES (?, ?, ?, ?)",
                supplierProduct.supplierId(), supplierProduct.productId(), supplierProduct.leadTimeDays(),
                supplierProduct.lastUnitCost());
        return new SupplierProduct(id, supplierProduct.supplierId(), supplierProduct.productId(),
                supplierProduct.leadTimeDays(), supplierProduct.lastUnitCost());
    }

    /** Updates the lead time and last unit cost of the supplier product. */
    public void updateSupplierProduct(SupplierProduct supplierProduct) {
        jdbc.update("Cannot update supplier product", "UPDATE supplier_product SET lead_time_days = ?, last_unit_cost = ? WHERE id = ?",
                supplierProduct.leadTimeDays(), supplierProduct.lastUnitCost(), supplierProduct.id());
    }

    public Optional<SupplierProduct> findSupplierProduct(long supplierId, long productId) {
        return jdbc.first("Cannot find supplier product",
                SELECT_SUPPLIER_PRODUCTS + " WHERE sp.supplier_id = ? AND sp.product_id = ?",
                SupplierStore::supplierProduct, supplierId, productId);
    }

    /** The supplier products of the product, lowest last unit cost first (then shortest lead time, then id). */
    public List<SupplierProduct> listSupplierProductsOfProduct(long productId) {
        return jdbc.list("Cannot list supplier products by product",
                SELECT_SUPPLIER_PRODUCTS + " WHERE sp.product_id = ? ORDER BY sp.last_unit_cost, sp.lead_time_days, sp.id",
                SupplierStore::supplierProduct, productId);
    }

    /** The supplier products of the supplier, by product id (then id). */
    public List<SupplierProduct> listSupplierProductsOfSupplier(long supplierId) {
        return jdbc.list("Cannot list supplier products by supplier",
                SELECT_SUPPLIER_PRODUCTS + " WHERE sp.supplier_id = ? ORDER BY sp.product_id, sp.id",
                SupplierStore::supplierProduct, supplierId);
    }

    /**
     * The supplier products of the products in the category or in any category below it, lowest last unit cost first
     * (then shortest lead time).
     */
    public List<SupplierProduct> listSupplierProductsInCategoryTree(String category) {
        return jdbc.list("Cannot list supplier products by category",
                CatalogSql.CATEGORY_TREE + SELECT_SUPPLIER_PRODUCTS + " JOIN product p ON p.id = sp.product_id "
                        + "WHERE " + CatalogSql.IN_CATEGORY_TREE + " ORDER BY sp.last_unit_cost, sp.lead_time_days, sp.id",
                SupplierStore::supplierProduct, category);
    }

    /**
     * The categories, the given one and those below it, whose products have supplier products from exactly one
     * supplier, with that supplier. Sorted by category; categories without supplier products are left out.
     */
    public List<CategorySupplier> listCategoriesWithSingleSupplier(String category) {
        return jdbc.list("Cannot find categories with a single supplier",
                CatalogSql.CATEGORY_TREE
                        + "SELECT p.category AS category, s.id, s.name, s.contact_email, s.country, s.rating "
                        + "FROM supplier_product sp JOIN product p ON p.id = sp.product_id "
                        + "JOIN supplier s ON s.id = sp.supplier_id "
                        + "WHERE " + CatalogSql.IN_CATEGORY_TREE + " "
                        + "GROUP BY p.category, " + SUPPLIER_GROUP + " "
                        + "HAVING (SELECT COUNT(DISTINCT sp2.supplier_id) FROM supplier_product sp2 "
                        + "JOIN product p2 ON p2.id = sp2.product_id WHERE p2.category = p.category) = 1 "
                        + "ORDER BY p.category",
                rs -> new CategorySupplier(rs.getString("category"), Rows.supplier(rs)), category);
    }

    /**
     * The stock quantity and value per warehouse of the products the supplier delivers (has a supplier product for),
     * for the warehouses with stock, valued like the stock value per warehouse (last cost of a sent or received
     * purchase order line, 0 if none). Sorted by warehouse code.
     */
    public List<SupplierWarehouseStock> stockPerWarehouse(long supplierId) {
        return jdbc.list("Cannot find stock per warehouse of supplier",
                "SELECT w.id, w.code, w.name, w.city, w.region, SUM(s.quantity) AS quantity, "
                        + "COALESCE(SUM(s.quantity * COALESCE("
                        + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
                        + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
                        + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)), 0) AS stock_value "
                        + "FROM stock s JOIN warehouse w ON w.id = s.warehouse_id "
                        + "WHERE s.quantity > 0 AND s.product_id IN (SELECT sp.product_id FROM supplier_product sp WHERE sp.supplier_id = ?) "
                        + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id",
                // setScale(2) without rounding mode, as before: throws if the value has more than 2 decimals
                rs -> new SupplierWarehouseStock(Rows.warehouse(rs), rs.getLong("quantity"),
                        rs.getBigDecimal("stock_value").setScale(2)),
                supplierId);
    }

    // --- reports on received purchase orders ---

    /**
     * Per supplier, the purchase orders received between the two dates, inclusive: their number, the average delay in days of
     * the received date against the expected date (negative if early) and the percentage received later than expected.
     * Sorted by supplier name; suppliers without received orders in the period are left out.
     */
    public List<SupplierPerformance> listPerformance(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot list supplier performance",
                "SELECT " + SUPPLIER_GROUP + ", COUNT(o.id) AS received_count, "
                        + "AVG(CAST(DATEDIFF('DAY', o.expected_date, o.received_date) AS DECIMAL(10, 2))) AS average_delay, "
                        + "COALESCE(SUM(CASE WHEN o.received_date > o.expected_date THEN 1 ELSE 0 END), 0) AS late_count "
                        + FROM_SUPPLIER_ORDERS + WHERE_RECEIVED_BETWEEN
                        + "GROUP BY " + SUPPLIER_GROUP + " ORDER BY s.name, s.id",
                rs -> {
                    int receivedCount = rs.getInt("received_count");
                    BigDecimal lateShare = BigDecimal.valueOf(rs.getInt("late_count") * 100L)
                            .divide(BigDecimal.valueOf(receivedCount), 2, RoundingMode.HALF_UP);
                    return new SupplierPerformance(Rows.supplier(rs), receivedCount,
                            rs.getBigDecimal("average_delay").setScale(2, RoundingMode.HALF_UP), lateShare);
                },
                from, to);
    }

    /**
     * Number and value (quantity times unit cost of the lines) of the purchase orders received between the two dates,
     * inclusive, per supplier. Sorted by value, highest first (then by supplier name, then id); suppliers without
     * received orders are left out.
     */
    public List<SupplierSpending> spendingPerSupplier(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find spending per supplier",
                "SELECT " + SUPPLIER_GROUP + ", " + SPENDING_TOTALS + RECEIVED_ORDER_LINES_BETWEEN
                        + "GROUP BY " + SUPPLIER_GROUP + " ORDER BY total DESC, s.name, s.id",
                rs -> new SupplierSpending(Rows.supplier(rs), rs.getInt("order_count"), rs.getBigDecimal("total")),
                from, to);
    }

    /**
     * Number and value of the purchase orders received between the two dates, inclusive, per country of the supplier,
     * valued as for the spending per supplier. Sorted by value, highest first (then by country); countries without
     * received orders are left out.
     */
    public List<CountrySpending> spendingPerCountry(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find spending per country",
                "SELECT s.country AS country, " + SPENDING_TOTALS + RECEIVED_ORDER_LINES_BETWEEN
                        + "GROUP BY s.country ORDER BY total DESC, s.country",
                rs -> new CountrySpending(rs.getString("country"), rs.getInt("order_count"), rs.getBigDecimal("total")),
                from, to);
    }

    private static SupplierProduct supplierProduct(ResultSet rs) throws SQLException {
        return new SupplierProduct(rs.getLong("id"), rs.getLong("supplier_id"), rs.getLong("product_id"),
                rs.getInt("lead_time_days"), rs.getBigDecimal("last_unit_cost"));
    }
}
