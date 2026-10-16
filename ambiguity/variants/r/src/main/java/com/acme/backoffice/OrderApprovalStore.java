package com.acme.backoffice;

import com.acme.backoffice.model.OrderApproval;
import com.acme.backoffice.model.SalesOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Discount approval of sales orders: the manual discounts set on order lines, the draft orders whose discount is above
 * a threshold and that still wait for an approval decision, and the approval decisions themselves.
 */
public final class OrderApprovalStore {
    /**
     * A draft order with a line discount above the bound percentage and no approval decision yet. Shared by the list
     * and the count of orders awaiting approval; binds one parameter (the percentage).
     */
    private static final String AWAITING_APPROVAL = "o.status = 'DRAFT' "
            + "AND EXISTS (SELECT 1 FROM order_line_discount d WHERE d.order_id = o.id AND d.discount_percent > ?) "
            + "AND NOT EXISTS (SELECT 1 FROM order_approval a WHERE a.order_id = o.id)";

    private final Jdbc jdbc;

    OrderApprovalStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    /** Sets (or replaces) the manual discount percentage of a line of the order. */
    public void setLineDiscount(long orderId, int lineNo, BigDecimal percent) {
        jdbc.update("Cannot set order line discount",
                "MERGE INTO order_line_discount (order_id, line_no, discount_percent) KEY (order_id, line_no) VALUES (?, ?, ?)",
                orderId, lineNo, percent);
    }

    /** True when a line of the order has a manual discount above the given percentage. */
    public boolean hasLineDiscountAbove(long orderId, BigDecimal percent) {
        return jdbc.single("Cannot check order line discounts",
                "SELECT COUNT(*) FROM order_line_discount WHERE order_id = ? AND discount_percent > ?",
                rs -> rs.getLong(1) > 0, orderId, percent);
    }

    /** The draft orders with a line discount above the percentage and no approval decision yet, oldest first. */
    public List<SalesOrder> listOrdersAwaitingApproval(BigDecimal percent) {
        return jdbc.list("Cannot list sales orders awaiting approval",
                "SELECT " + Rows.SALES_ORDER_COLUMNS + " FROM sales_order o WHERE " + AWAITING_APPROVAL
                        + " ORDER BY o.order_date, o.id",
                Rows.salesOrder(jdbc), percent);
    }

    /** Number of draft orders placed on or before the date with a line discount above the percentage and no approval decision. */
    public int countOrdersAwaitingApprovalOnDate(BigDecimal percent, LocalDate date) {
        return jdbc.queryInt("Cannot count sales orders awaiting approval",
                "SELECT COUNT(*) FROM sales_order o WHERE " + AWAITING_APPROVAL + " AND o.order_date <= ?",
                percent, date);
    }

    /** Records the approval decision of an order; returns the given approval. */
    public OrderApproval saveApproval(OrderApproval approval) {
        jdbc.update("Cannot save order approval",
                "INSERT INTO order_approval (order_id, approved, approver, decision_date) VALUES (?, ?, ?, ?)",
                approval.orderId(), approval.approved(), approval.approver(), approval.date());
        return approval;
    }

    /** The approval decision of the order, if one was made. */
    public Optional<OrderApproval> findApproval(long orderId) {
        return jdbc.first("Cannot find order approval",
                "SELECT order_id, approved, approver, decision_date FROM order_approval WHERE order_id = ?",
                rs -> new OrderApproval(rs.getLong("order_id"), rs.getBoolean("approved"), rs.getString("approver"),
                        rs.getObject("decision_date", LocalDate.class)),
                orderId);
    }

    /** Removes the approval decision of the order. */
    public void deleteApproval(long orderId) {
        jdbc.update("Cannot delete order approval", "DELETE FROM order_approval WHERE order_id = ?", orderId);
    }
}
