package com.acme.backoffice;

import com.acme.backoffice.model.OrderNote;

import java.time.LocalDate;
import java.util.List;

/**
 * Notes written on sales orders: adding a note, and the notes of an order, of an author (in a period), of a customer's
 * orders, or of an author on a customer's orders. Every list is newest first (note date, then id, descending).
 */
public final class OrderNoteStore {
    private static final String SELECT_NOTES = "SELECT n.id, n.order_id, n.author, n.note_date, n.text FROM order_note n ";
    private static final String JOIN_ORDER = "JOIN sales_order o ON o.id = n.order_id ";

    private final Jdbc jdbc;

    OrderNoteStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    /** Adds the note; returns it with its generated id. */
    public OrderNote create(OrderNote note) {
        long id = jdbc.insert("Cannot create order note",
                "INSERT INTO order_note (order_id, author, note_date, text) VALUES (?, ?, ?, ?)",
                note.orderId(), note.author(), note.date(), note.text());
        return new OrderNote(id, note.orderId(), note.author(), note.date(), note.text());
    }

    /** The notes of the order, newest first. */
    public List<OrderNote> listByOrder(long orderId) {
        return listNewestFirst("Cannot list order notes by order", "WHERE n.order_id = ?", orderId);
    }

    /** The notes of the author dated between the two dates, inclusive, newest first. */
    public List<OrderNote> listByAuthor(String author, LocalDate from, LocalDate to) {
        return listNewestFirst("Cannot list order notes by author",
                "WHERE n.author = ? AND n.note_date >= ? AND n.note_date <= ?", author, from, to);
    }

    /** The notes on the orders of the customer, newest first. */
    public List<OrderNote> listByCustomer(long customerId) {
        return listNewestFirst("Cannot list order notes by customer", JOIN_ORDER + "WHERE o.customer_id = ?", customerId);
    }

    /** The notes of the author on the orders of the customer, newest first. */
    public List<OrderNote> listByAuthorAndCustomer(String author, long customerId) {
        return listNewestFirst("Cannot list order notes by author and customer",
                JOIN_ORDER + "WHERE n.author = ? AND o.customer_id = ?", author, customerId);
    }

    /** The notes selected by the join/where clause (on alias n, and o when joined), newest first. */
    private List<OrderNote> listNewestFirst(String error, String joinAndWhere, Object... params) {
        return jdbc.list(error, SELECT_NOTES + joinAndWhere + " ORDER BY n.note_date DESC, n.id DESC",
                rs -> new OrderNote(rs.getLong("id"), rs.getLong("order_id"), rs.getString("author"),
                        rs.getObject("note_date", LocalDate.class), rs.getString("text")),
                params);
    }
}
