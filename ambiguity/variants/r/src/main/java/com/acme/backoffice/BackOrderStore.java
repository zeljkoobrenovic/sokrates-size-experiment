package com.acme.backoffice;

import com.acme.backoffice.model.BackOrder;
import com.acme.backoffice.model.CategoryBackOrders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Back orders: sales orders registered as waiting for stock. A back order counts as waiting while its order is still
 * confirmed; the lists (all, of a customer, with a product), the count on a date and the per-category report only
 * show waiting back orders, oldest first.
 */
public final class BackOrderStore {
    private static final String SELECT_BACK_ORDERS = "SELECT b.id, b.order_id, o.number, o.customer_id, b.back_order_date "
            + "FROM back_order b JOIN sales_order o ON o.id = b.order_id ";
    /** The condition that makes a back order waiting; shared by every list, the count and the per-category report. */
    private static final String WAITING = "o.status = 'CONFIRMED'";

    private final Jdbc jdbc;

    BackOrderStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    /** Registers the order as back order on the date; returns it with its generated id and the order's number and customer. */
    public BackOrder create(long orderId, LocalDate date) {
        String error = "Cannot create back order";
        // Not in a transaction, as originally: the insert and the read of the order are two separate statements.
        long id = jdbc.insert(error, "INSERT INTO back_order (order_id, back_order_date) VALUES (?, ?)", orderId, date);
        return jdbc.single(error, "SELECT number, customer_id FROM sales_order WHERE id = ?",
                rs -> new BackOrder(id, orderId, rs.getString("number"), rs.getLong("customer_id"), date), orderId);
    }

    /** The back order of the order, whatever the order's status (also when it is no longer waiting). */
    public Optional<BackOrder> findByOrder(long orderId) {
        return jdbc.first("Cannot find back order by order", SELECT_BACK_ORDERS + "WHERE b.order_id = ?",
                BackOrderStore::backOrder, orderId);
    }

    /** All waiting back orders, oldest first. */
    public List<BackOrder> list() {
        return listWaiting("Cannot list back orders", "");
    }

    /** Number of back orders registered on or before the date whose order is still confirmed. */
    public int countOnDate(LocalDate date) {
        return jdbc.queryInt("Cannot count back orders",
                "SELECT COUNT(*) FROM back_order b JOIN sales_order o ON o.id = b.order_id WHERE " + WAITING
                        + " AND b.back_order_date <= ?",
                date);
    }

    /** The waiting back orders of the customer, oldest first. */
    public List<BackOrder> listByCustomer(long customerId) {
        return listWaiting("Cannot list back orders by customer", " AND o.customer_id = ?", customerId);
    }

    /** The waiting back orders with a line of the product, oldest first. */
    public List<BackOrder> listByProduct(long productId) {
        return listWaiting("Cannot list back orders by product",
                " AND EXISTS (SELECT 1 FROM sales_order_line l WHERE l.order_id = o.id AND l.product_id = ?)", productId);
    }

    /**
     * Per product category, the number of waiting back orders (order still confirmed) with a line in the category and the
     * units ordered on those lines. Sorted by category; categories without back orders are left out.
     */
    public List<CategoryBackOrders> perCategory() {
        return jdbc.list("Cannot find back orders per category",
                "SELECT p.category AS category, COUNT(DISTINCT b.id) AS back_order_count, SUM(l.quantity) AS units "
                        + "FROM back_order b JOIN sales_order o ON o.id = b.order_id "
                        + "JOIN sales_order_line l ON l.order_id = o.id JOIN product p ON p.id = l.product_id "
                        + "WHERE " + WAITING + " GROUP BY p.category ORDER BY p.category",
                rs -> new CategoryBackOrders(rs.getString("category"), rs.getInt("back_order_count"), rs.getInt("units")));
    }

    /** The waiting back orders matching the extra condition (starting with " AND "), oldest first. */
    private List<BackOrder> listWaiting(String error, String andCondition, Object... params) {
        return jdbc.list(error, SELECT_BACK_ORDERS + "WHERE " + WAITING + andCondition + " ORDER BY b.back_order_date, b.id",
                BackOrderStore::backOrder, params);
    }

    private static BackOrder backOrder(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new BackOrder(rs.getLong("id"), rs.getLong("order_id"), rs.getString("number"), rs.getLong("customer_id"),
                rs.getObject("back_order_date", LocalDate.class));
    }
}
