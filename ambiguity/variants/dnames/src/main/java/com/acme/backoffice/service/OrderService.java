package com.acme.backoffice.service;

import com.acme.backoffice.DataStore;
import com.acme.backoffice.model.ArchivedYearRevenue;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.CancellationCount;
import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.CancelledOrder;
import com.acme.backoffice.model.CategoryMonthRevenue;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.DraftOrder;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.OrderStatusChange;
import com.acme.backoffice.model.ProductOrder;
import com.acme.backoffice.model.FirstTimeCustomer;
import com.acme.backoffice.model.RepeatCustomer;
import com.acme.backoffice.model.SalesOrder;

import java.time.LocalDate;
import java.util.List;

/** Moves orders through their statuses: DRAFT, CONFIRMED, SHIPPED, DELIVERED; DRAFT and CONFIRMED can be CANCELLED. */
public class OrderService {
    private final DataStore store;

    public OrderService(DataStore store) {
        this.store = store;
    }

    /** Refused when a discount above 15% is not approved, or when the customer's open invoices plus the value of this order would exceed the credit limit. */
    public SalesOrder confirm(String number, LocalDate date) {
        SalesOrder order = find(number);
        if (order.status() == OrderStatus.DRAFT) {
            new DiscountApprovalService(store).checkOrder(order);
            new CreditService(store).checkOrder(order);
        }
        return change(number, OrderStatus.DRAFT, OrderStatus.CONFIRMED, date);
    }

    public SalesOrder ship(String number, LocalDate date) {
        return change(number, OrderStatus.CONFIRMED, OrderStatus.SHIPPED, date);
    }

    public SalesOrder deliver(String number, LocalDate date) {
        return change(number, OrderStatus.SHIPPED, OrderStatus.DELIVERED, date);
    }

    /** Only a draft or confirmed order can be cancelled; a reason is required. */
    public SalesOrder cancel(String number, CancellationReason reason, LocalDate date) {
        if (reason == null) {
            throw new IllegalArgumentException("Cancellation reason is required");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        SalesOrder order = find(number);
        if (order.status() != OrderStatus.DRAFT && order.status() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order " + number + " cannot be cancelled, it is " + order.status());
        }
        store.cancelSalesOrder(order.id(), reason, date);
        return new SalesOrder(order.id(), order.number(), order.customerId(), order.orderDate(), OrderStatus.CANCELLED, order.lines());
    }

    /** Cancelled orders between the two dates (inclusive) per reason and customer. */
    public List<CancellationCount> getCancellations(LocalDate from, LocalDate to) {
        return store.countCancelledSalesOrdersPerReasonAndCustomerBetween(from, to);
    }

    /** Orders cancelled between the two dates (inclusive), per customer, newest first. */
    public List<CancelledOrder> getCancelledOrdersByCustomer(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listCancelledSalesOrdersBetweenGroupedPerCustomer(from, to);
    }

    /** Orders cancelled between the two dates (inclusive), per region, newest first. */
    public List<CancelledOrder> getCancelledOrdersByRegion(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listCancelledSalesOrdersBetweenGroupedPerRegion(from, to);
    }

    /** Customers whose first non-cancelled order falls between the two dates (inclusive), per region and sales rep. */
    public List<FirstTimeCustomer> getFirstTimeCustomers(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listFirstTimeCustomersWithFirstOrderBetween(from, to);
    }

    /** Active customers with orders but no invoice between the two dates (inclusive), per region and name. */
    public List<Customer> getCustomersWithoutInvoices(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listActiveCustomersWithOrdersButWithoutInvoicesBetween(from, to);
    }

    /** Customers with at least the given number of non-cancelled orders between the two dates (inclusive), per region, most orders first. */
    public List<RepeatCustomer> getRepeatCustomersByRegion(int minOrders, LocalDate from, LocalDate to) {
        if (minOrders < 1) {
            throw new IllegalArgumentException("Minimum number of orders must be at least 1");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listRepeatCustomersWithMinOrdersPerRegionBetween(minOrders, from, to);
    }

    /** Draft orders older than the given number of days on the date, per customer. */
    public List<DraftOrder> getStaleDraftOrdersByCustomer(LocalDate asOf, int days) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (days < 0) {
            throw new IllegalArgumentException("Days must not be negative");
        }
        return store.listStaleDraftSalesOrdersGroupedPerCustomer(asOf, days);
    }

    /** Draft orders older than the given number of days on the date, per sales rep. */
    public List<DraftOrder> getStaleDraftOrdersBySalesRep(LocalDate asOf, int days) {
        if (asOf == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (days < 0) {
            throw new IllegalArgumentException("Days must not be negative");
        }
        return store.listStaleDraftSalesOrdersGroupedPerSalesRep(asOf, days);
    }

    /** Non-cancelled orders containing the product between the two dates (inclusive), newest first. */
    public List<ProductOrder> getOrdersByProduct(long productId, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listNonCancelledSalesOrdersWithProductPlacedBetweenNewestFirst(productId, from, to);
    }

    /** Non-cancelled orders containing the product between the two dates (inclusive), per customer, newest first. */
    public List<ProductOrder> getOrdersByProductPerCustomer(long productId, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listNonCancelledSalesOrdersWithProductPlacedBetweenPerCustomer(productId, from, to);
    }

    /** Non-cancelled orders containing a product of the category (or a category below it) between the two dates (inclusive), newest first. */
    public List<ProductOrder> getOrdersByCategory(String category, LocalDate from, LocalDate to) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category is required");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.listNonCancelledSalesOrdersInCategoryTreePlacedBetween(category, from, to);
    }

    /** Revenue per product category and month of the non-cancelled orders between the two dates (inclusive). */
    public List<CategoryMonthRevenue> getRevenuePerCategoryAndMonth(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("End date is before the start date");
        }
        return store.sumNonCancelledRevenuePerExactCategoryAndMonthBetween(from, to);
    }

    /** Archives the orders delivered or cancelled more than two years before the date; returns how many. */
    public int archive(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        return store.archiveDeliveredAndCancelledSalesOrdersBefore(date.minusYears(2), date);
    }

    /** The archived orders, oldest first. */
    public List<SalesOrder> listArchived() {
        return store.listArchivedSalesOrders();
    }

    /** The archived orders of the customer, oldest first. */
    public List<SalesOrder> listArchivedByCustomer(long customerId) {
        return store.listArchivedSalesOrdersOfCustomer(customerId);
    }

    /** The archived orders of the customers of the region, oldest first. */
    public List<SalesOrder> listArchivedByRegion(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("Region is required");
        }
        return store.listArchivedSalesOrdersInRegion(region);
    }

    /** The revenue of the archived, non-cancelled orders per year, oldest year first. */
    public List<ArchivedYearRevenue> archivedRevenuePerYear() {
        return store.sumArchivedNonCancelledRevenuePerYear();
    }

    /** The status changes of the order, oldest first. */
    public List<OrderStatusChange> getHistory(String number) {
        return store.listStatusChangesOfSalesOrder(find(number).id());
    }

    private SalesOrder change(String number, OrderStatus from, OrderStatus to, LocalDate date) {
        SalesOrder order = find(number);
        if (order.status() != from) {
            throw new IllegalStateException("Order " + number + " cannot become " + to + ", it is " + order.status());
        }
        return apply(order, to, date);
    }

    private SalesOrder apply(SalesOrder order, OrderStatus to, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        store.changeSalesOrderStatus(order.id(), to, date);
        return new SalesOrder(order.id(), order.number(), order.customerId(), order.orderDate(), to, order.lines());
    }

    private SalesOrder find(String number) {
        return store.findSalesOrderByNumber(number)
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + number));
    }
}
