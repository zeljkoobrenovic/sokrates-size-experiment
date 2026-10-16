package com.acme.backoffice;

import com.acme.backoffice.model.CustomerPrice;
import com.acme.backoffice.model.PriceListUsage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The customer price list: special prices (customer-specific unit prices of a product with a validity period). Creating
 * and deleting them, finding the price valid on a date, overlapping and expiring prices, and how often non-cancelled
 * orders were sold at a special price (price list usage).
 */
public final class PriceListStore {
    private static final String SELECT_PRICE =
            "SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price ";
    /** The price is valid on the date (inclusive at both ends); binds the date twice. */
    private static final String VALID_ON = "start_date <= ? AND end_date >= ?";
    private static final String ACTIVE_PRODUCTS = "SELECT id FROM product WHERE active = TRUE";

    private final Jdbc jdbc;

    PriceListStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public CustomerPrice create(CustomerPrice price) {
        long id = jdbc.insert("Cannot create customer price",
                "INSERT INTO customer_price (customer_id, product_id, unit_price, start_date, end_date) VALUES (?, ?, ?, ?, ?)",
                price.customerId(), price.productId(), price.unitPrice(), price.startDate(), price.endDate());
        return new CustomerPrice(id, price.customerId(), price.productId(), price.unitPrice(), price.startDate(),
                price.endDate());
    }

    public Optional<CustomerPrice> findById(long id) {
        return jdbc.first("Cannot find customer price", SELECT_PRICE + "WHERE id = ?", Rows::customerPrice, id);
    }

    public void delete(long id) {
        jdbc.update("Cannot delete customer price", "DELETE FROM customer_price WHERE id = ?", id);
    }

    /** All special prices of the customer for active products, whatever their period, by product, then start date. */
    public List<CustomerPrice> listOfCustomer(long customerId) {
        return list("Cannot list customer prices", "",
                "WHERE customer_id = ? AND product_id IN (" + ACTIVE_PRODUCTS + ") ORDER BY product_id, start_date, id",
                customerId);
    }

    /** The customer's special prices valid on the date for active products of the category or of any category below it. */
    public List<CustomerPrice> listValidOfCustomerInCategory(long customerId, String category, LocalDate date) {
        return list("Cannot list customer prices by category",
                "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) ",
                "WHERE customer_id = ? AND " + VALID_ON + " "
                        + "AND product_id IN (" + ACTIVE_PRODUCTS + " AND category IN (SELECT code FROM tree)) "
                        + "ORDER BY product_id, start_date, id",
                category, customerId, date, date);
    }

    /** The special prices of all customers whose last valid day lies within the given days after the date, per customer. */
    public List<CustomerPrice> listExpiring(LocalDate date, int days) {
        return list("Cannot list expiring customer prices", "",
                "WHERE end_date > ? AND end_date <= ? ORDER BY customer_id, end_date, product_id, id",
                date, date.plusDays(days));
    }

    /** The customer's special price for the product valid on the date; the latest-starting one when several are. */
    public Optional<CustomerPrice> findValid(long customerId, long productId, LocalDate date) {
        return jdbc.first("Cannot find valid customer price",
                SELECT_PRICE + "WHERE customer_id = ? AND product_id = ? AND " + VALID_ON
                        + " ORDER BY start_date DESC, id LIMIT 1",
                Rows::customerPrice, customerId, productId, date, date);
    }

    /** The customer's special prices for the product whose period overlaps the given period (inclusive), by start date. */
    public List<CustomerPrice> listOverlapping(long customerId, long productId, LocalDate startDate, LocalDate endDate) {
        return list("Cannot list overlapping customer prices", "",
                "WHERE customer_id = ? AND product_id = ? AND start_date <= ? AND end_date >= ? ORDER BY start_date, id",
                customerId, productId, endDate, startDate);
    }

    /**
     * Price list usage per customer: the lines of non-cancelled orders dated in the period that were sold at a special price
     * (the customer had a price for the product valid on the order date): the line count, the revenue at the special prices
     * and the revenue the lines would have had at catalogue prices. Sorted by customer name, then id; customers without such
     * lines are left out.
     */
    public List<PriceListUsage> usagePerCustomer(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find price list usage",
                "SELECT c.id AS customer_id, c.name AS name, COUNT(*) AS line_count, "
                        + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS special_revenue, "
                        + "COALESCE(SUM(l.quantity * p.unit_price), 0) AS catalogue_revenue "
                        + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id JOIN customer c ON c.id = o.customer_id "
                        + "JOIN product p ON p.id = l.product_id "
                        + "JOIN customer_price cp ON cp.customer_id = o.customer_id AND cp.product_id = l.product_id "
                        + "AND cp.start_date <= o.order_date AND cp.end_date >= o.order_date "
                        + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                        + "GROUP BY c.id, c.name ORDER BY c.name, c.id",
                rs -> new PriceListUsage(rs.getLong("customer_id"), rs.getString("name"), rs.getInt("line_count"),
                        rs.getBigDecimal("special_revenue"), rs.getBigDecimal("catalogue_revenue")),
                from, to);
    }

    /** The special prices selected by the WHERE and ORDER BY clauses, after an optional WITH clause. */
    private List<CustomerPrice> list(String error, String with, String whereAndOrder, Object... params) {
        return jdbc.list(error, with + SELECT_PRICE + whereAndOrder, Rows::customerPrice, params);
    }
}
