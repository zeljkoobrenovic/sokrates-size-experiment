package com.acme.backoffice;

import com.acme.backoffice.model.BundleComponent;
import com.acme.backoffice.model.Product;

import java.util.List;

/**
 * Product bundles: a bundle is a product with components (other products, each with the quantity needed for one
 * bundle). Covers the components of a bundle, the bundles (containing a component) and how many bundles can be built
 * from the component stock, per warehouse or in total.
 */
public final class BundleStore {
    /** Bundles buildable from the stock of warehouse {@code s} for each component {@code c}; missing stock counts as 0. */
    private static final String BUILDABLE = "MIN(COALESCE(s.quantity, 0) / c.quantity)";

    private final Jdbc jdbc;

    BundleStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public void createComponent(long bundleId, long componentId, int quantity) {
        jdbc.update("Cannot create bundle component",
                "INSERT INTO bundle_component (bundle_id, component_id, quantity) VALUES (?, ?, ?)",
                bundleId, componentId, quantity);
    }

    /** The components of the bundle with the quantity needed for one bundle, ordered by product name; empty if the product is no bundle. */
    public List<BundleComponent> listComponents(long bundleId) {
        return jdbc.list("Cannot list bundle components",
                "SELECT " + Rows.PRODUCT_COLUMNS + ", c.quantity "
                        + "FROM bundle_component c JOIN product p ON p.id = c.component_id WHERE c.bundle_id = ? ORDER BY p.name, p.id",
                rs -> new BundleComponent(Rows.product(rs), rs.getInt("quantity")), bundleId);
    }

    /** The products that have components, ordered by name. */
    public List<Product> list() {
        return CatalogSql.listProductsByName(jdbc, "Cannot list bundles", "p.id IN (SELECT bundle_id FROM bundle_component)");
    }

    /** The bundles that contain the product as a component, ordered by name. */
    public List<Product> listContainingComponent(long componentId) {
        return CatalogSql.listProductsByName(jdbc, "Cannot list bundles by component",
                "p.id IN (SELECT bundle_id FROM bundle_component WHERE component_id = ?)", componentId);
    }

    /** How many bundles can be built from the stock of one warehouse; 0 if the product has no components. */
    public int findQuantityInWarehouse(long warehouseId, long bundleId) {
        return jdbc.queryInt("Cannot find bundle quantity",
                "SELECT COALESCE(" + BUILDABLE + ", 0) AS buildable FROM bundle_component c "
                        + "LEFT JOIN stock s ON s.product_id = c.component_id AND s.warehouse_id = ? WHERE c.bundle_id = ?",
                warehouseId, bundleId);
    }

    /** How many bundles can be built in total, the sum over the warehouses of what each can build from its own stock. */
    public int findTotalQuantity(long bundleId) {
        return jdbc.queryInt("Cannot find total bundle quantity",
                "SELECT COALESCE(SUM(buildable), 0) AS total FROM ("
                        + "SELECT " + BUILDABLE + " AS buildable FROM warehouse w CROSS JOIN bundle_component c "
                        + "LEFT JOIN stock s ON s.product_id = c.component_id AND s.warehouse_id = w.id WHERE c.bundle_id = ? GROUP BY w.id)",
                bundleId);
    }
}
