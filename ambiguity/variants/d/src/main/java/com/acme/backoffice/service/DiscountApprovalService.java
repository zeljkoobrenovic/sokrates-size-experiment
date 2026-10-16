package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.OrderApproval;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.SalesOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Order lines with a manual discount above 15% need the approval of a sales manager before the order can be confirmed. */
public class DiscountApprovalService {
    public static final BigDecimal APPROVAL_THRESHOLD = new BigDecimal("15");

    private final DataStore store;

    public DiscountApprovalService(DataStore store) {
        this.store = store;
    }

    /** Records the manual discount in percent of an order line (line numbers start at 1); only a draft order can change. */
    public void setLineDiscount(String orderNumber, int lineNo, BigDecimal percent) {
        SalesOrder order = find(orderNumber);
        if (percent == null || percent.signum() < 0 || percent.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("The discount must be between 0 and 100 percent");
        }
        if (lineNo < 1 || lineNo > order.lines().size()) {
            throw new IllegalArgumentException("Unknown line " + lineNo + " of order " + orderNumber);
        }
        if (order.status() != OrderStatus.DRAFT) {
            throw new IllegalStateException("The discount of order " + orderNumber + " cannot change, it is " + order.status());
        }
        store.setSalesOrderLineDiscount(order.id(), lineNo, percent);
        store.deleteSalesOrderApproval(order.id());
    }

    /** The draft orders with a discount above the threshold that nobody has approved or rejected yet. */
    public List<SalesOrder> listAwaitingApproval() {
        return store.findSalesOrdersAwaitingApproval(APPROVAL_THRESHOLD);
    }

    public OrderApproval approve(String orderNumber, String approver, LocalDate date) {
        return decide(orderNumber, approver, date, true);
    }

    public OrderApproval reject(String orderNumber, String approver, LocalDate date) {
        return decide(orderNumber, approver, date, false);
    }

    public Optional<OrderApproval> getApproval(String orderNumber) {
        return store.findSalesOrderApproval(find(orderNumber).id());
    }

    /** Refused when the order has a discount above the threshold that has not been approved. */
    public void checkOrder(SalesOrder order) {
        if (!store.hasSalesOrderLineDiscountAbove(order.id(), APPROVAL_THRESHOLD)) {
            return;
        }
        Optional<OrderApproval> approval = store.findSalesOrderApproval(order.id());
        if (approval.isEmpty() || !approval.get().approved()) {
            throw new IllegalStateException("Order " + order.number() + " has a discount above " + APPROVAL_THRESHOLD
                    + "% that is not approved");
        }
    }

    private OrderApproval decide(String orderNumber, String approver, LocalDate date, boolean approved) {
        if (approver == null || approver.isBlank()) {
            throw new IllegalArgumentException("The approver must not be empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("The date must not be empty");
        }
        SalesOrder order = find(orderNumber);
        if (!store.hasSalesOrderLineDiscountAbove(order.id(), APPROVAL_THRESHOLD)) {
            throw new IllegalStateException("Order " + orderNumber + " needs no approval");
        }
        if (store.findSalesOrderApproval(order.id()).isPresent()) {
            throw new IllegalStateException("Order " + orderNumber + " has already been decided");
        }
        return store.createSalesOrderApproval(new OrderApproval(order.id(), approved, approver, date));
    }

    private SalesOrder find(String number) {
        return store.findSalesOrderByNumber(number)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + number));
    }
}
