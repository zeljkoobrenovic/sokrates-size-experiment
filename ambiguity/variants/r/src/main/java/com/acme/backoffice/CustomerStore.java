package com.acme.backoffice;

import com.acme.backoffice.model.CountryCustomerCount;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerActivity;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.QuarterCustomers;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionCustomers;
import com.acme.backoffice.model.SalesRepCustomerCount;
import com.acme.backoffice.model.WeekdayCustomers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Customers: creating and updating them, their status, credit limit and created date, closing and reopening them,
 * assigning them to a sales rep, searching them (by name, region, country, status, sales rep), the latest customers,
 * active customers without orders or without invoices (dormant customers), the activity of a customer, and the customer
 * counts per region, quarter, weekday, sales rep and country.
 *
 * <p>"Open" customers are the ones not closed (no row in {@code customer_closure}).
 */
public final class CustomerStore {
    /** Condition on {@code c} that leaves closed customers out. */
    private static final String NOT_CLOSED = "NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id)";

    /** Condition on {@code c}: no non-cancelled order placed between two dates (from, to), inclusive. */
    private static final String NO_ORDER_IN_PERIOD = "NOT EXISTS (SELECT 1 FROM sales_order o WHERE o.customer_id = c.id "
            + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED')";

    /** What the customer counts per quarter and weekday count: customers by created date. */
    private static final Breakdowns.Counted CREATED = new Breakdowns.Counted("FROM customer c", "c.created_date", "", "c.region");

    private final Jdbc jdbc;

    CustomerStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public Customer create(Customer customer) {
        long id = jdbc.insert("Cannot create customer",
                "INSERT INTO customer (name, email, phone, country, region, status) VALUES (?, ?, ?, ?, ?, ?)",
                customer.name(), customer.email(), customer.phone(), customer.country(), customer.region().name(),
                customer.status().name());
        return new Customer(id, customer.name(), customer.email(), customer.phone(), customer.country(), customer.region(),
                customer.status());
    }

    public void update(Customer customer) {
        jdbc.update("Cannot update customer",
                "UPDATE customer SET name = ?, email = ?, phone = ?, country = ?, region = ?, status = ? WHERE id = ?",
                customer.name(), customer.email(), customer.phone(), customer.country(), customer.region().name(),
                customer.status().name(), customer.id());
    }

    /** Sets the credit limit of the customer; null removes the limit. */
    public void updateCreditLimit(long customerId, BigDecimal creditLimit) {
        jdbc.update("Cannot update customer credit limit", "UPDATE customer SET credit_limit = ? WHERE id = ?",
                creditLimit, customerId);
    }

    /** Sets the date the customer was created; null removes it. */
    public void updateCreatedDate(long customerId, LocalDate createdDate) {
        jdbc.update("Cannot update customer created date", "UPDATE customer SET created_date = ? WHERE id = ?",
                createdDate, customerId);
    }

    /** The credit limit of the customer; empty if the customer has no limit or does not exist. */
    public Optional<BigDecimal> findCreditLimit(long customerId) {
        return jdbc.first("Cannot find customer credit limit", "SELECT credit_limit FROM customer WHERE id = ?",
                rs -> Optional.ofNullable(rs.getBigDecimal("credit_limit")), customerId).flatMap(limit -> limit);
    }

    public Optional<Customer> findById(long id) {
        return jdbc.first("Cannot find customer", "SELECT " + Rows.CUSTOMER_COLUMNS + " FROM customer c WHERE c.id = ?",
                Rows::customer, id);
    }

    /** Assigns the customer to the sales rep. */
    public void assignToSalesRep(long customerId, long salesRepId) {
        jdbc.update("Cannot assign customer to sales rep", "UPDATE customer SET sales_rep_id = ? WHERE id = ?",
                salesRepId, customerId);
    }

    // ---- closing ----

    /** Closes the customer without deleting anything; returns false if it was already closed. */
    public boolean close(long customerId, LocalDate closedDate) {
        return jdbc.update("Cannot close customer",
                "INSERT INTO customer_closure (customer_id, closed_date) SELECT c.id, ? FROM customer c WHERE c.id = ? AND "
                        + NOT_CLOSED, closedDate, customerId) > 0;
    }

    /** Reopens a closed customer; returns false if it was not closed. */
    public boolean reopen(long customerId) {
        return jdbc.update("Cannot reopen customer", "DELETE FROM customer_closure WHERE customer_id = ?", customerId) > 0;
    }

    /** The closed customers, ordered by name. */
    public List<Customer> listClosed() {
        return jdbc.list("Cannot list closed customers", "SELECT " + Rows.CUSTOMER_COLUMNS + " FROM customer c "
                + "JOIN customer_closure cl ON cl.customer_id = c.id ORDER BY c.name, c.id", Rows::customer);
    }

    // ---- open customers by name: listing and searching ----

    /** The open (not closed) customers, ordered by name, then id. */
    public List<Customer> listOpen() {
        return listOpenOrderedByName("Cannot list customers", "", new Object[0]);
    }

    /** The open customers whose name contains the text, ignoring case (taken literally, no wildcards), ordered by name. */
    public List<Customer> searchByName(String namePart) {
        String escaped = namePart.toLowerCase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return listOpenOrderedByName("Cannot search customers by name", "LOWER(c.name) LIKE ? ESCAPE '\\' AND ",
                new Object[] {"%" + escaped + "%"});
    }

    /** The open customers of the region, ordered by name. */
    public List<Customer> listInRegion(Region region) {
        return listOpenOrderedByName("Cannot search customers by region", "c.region = ? AND ", new Object[] {region.name()});
    }

    /** The open customers of the country, ordered by name. */
    public List<Customer> listInCountry(String country) {
        return listOpenOrderedByName("Cannot search customers by country", "c.country = ? AND ", new Object[] {country});
    }

    /** The open customers with the status, ordered by name. */
    public List<Customer> listWithStatus(CustomerStatus status) {
        return listOpenOrderedByName("Cannot search customers by status", "c.status = ? AND ", new Object[] {status.name()});
    }

    /** The open customers assigned to the sales rep, ordered by name. */
    public List<Customer> listOfSalesRep(long salesRepId) {
        return listOpenOrderedByName("Cannot list customers by sales rep", "c.sales_rep_id = ? AND ", new Object[] {salesRepId});
    }

    /** Open customers matching the condition (ending with {@code " AND "}, or empty), ordered by name, then id. */
    private List<Customer> listOpenOrderedByName(String error, String condition, Object[] params) {
        return jdbc.list(error, "SELECT " + Rows.CUSTOMER_COLUMNS + " FROM customer c WHERE " + condition + NOT_CLOSED
                + " ORDER BY c.name, c.id", Rows::customer, params);
    }

    // ---- latest customers ----

    /**
     * The N most recently created customers (customers without a created date and closed customers are left out),
     * newest first, then by highest id.
     */
    public List<Customer> listLatest(int limit) {
        return queryLatest("Cannot find latest customers", "", "", limit);
    }

    /**
     * The N most recently created customers in the region (customers without a created date and closed customers are
     * left out), newest first, then by highest id.
     */
    public List<Customer> listLatestInRegion(Region region, int limit) {
        return queryLatest("Cannot find latest customers by region", "", "c.region = ? AND ", limit, region.name());
    }

    /**
     * The N customers created most recently before the given customer, i.e. the ones that joined just ahead of it (the
     * customer itself, customers without a created date and closed customers are left out), newest first, then by
     * highest id. Customers created on the same date count as earlier when their id is lower.
     */
    public List<Customer> listLatestBefore(long customerId, int limit) {
        return queryLatest("Cannot find latest customers before customer", ", customer ref",
                "ref.id = ? AND ref.created_date IS NOT NULL "
                        + "AND (c.created_date < ref.created_date OR (c.created_date = ref.created_date AND c.id < ref.id)) AND ",
                limit, customerId);
    }

    /**
     * Open customers with a created date matching the condition (ending with {@code " AND "}, or empty), newest first,
     * then by highest id, at most {@code limit}; {@code join} adds tables to the FROM clause.
     */
    private List<Customer> queryLatest(String error, String join, String condition, int limit, Object... params) {
        String sql = "SELECT " + Rows.CUSTOMER_COLUMNS + " FROM customer c" + join + " WHERE " + condition
                + "c.created_date IS NOT NULL AND " + NOT_CLOSED + " ORDER BY c.created_date DESC, c.id DESC LIMIT ?";
        Object[] all = new Object[params.length + 1];
        System.arraycopy(params, 0, all, 0, params.length);
        all[params.length] = limit;
        return jdbc.list(error, sql, Rows::customer, all);
    }

    // ---- created in a period ----

    /**
     * The customers of the country created between the two dates, inclusive (customers without a created date are left
     * out), oldest first, then by id.
     */
    public List<Customer> listCreatedInCountry(String country, LocalDate from, LocalDate to) {
        // Unlike the other lists, closed customers are included here (as in the original).
        return jdbc.list("Cannot find customers by country", "SELECT " + Rows.CUSTOMER_COLUMNS + " FROM customer c "
                        + "WHERE c.country = ? AND c.created_date >= ? AND c.created_date <= ? ORDER BY c.created_date, c.id",
                Rows::customer, country, from, to);
    }

    // ---- dormant customers ----

    /**
     * The active customers without a non-cancelled order placed between the two dates, inclusive (dormant customers).
     * Sorted by region, then by name, then id.
     */
    public List<Customer> listActiveWithoutOrders(LocalDate from, LocalDate to) {
        return listActiveOpen("Cannot find dormant customers", NO_ORDER_IN_PERIOD, from, to);
    }

    /**
     * The active customers with a non-cancelled order placed between the two dates, inclusive, but without a
     * non-cancelled invoice dated between the two dates, inclusive. Sorted by region, then by name, then id.
     */
    public List<Customer> listActiveWithoutInvoices(LocalDate from, LocalDate to) {
        return listActiveOpen("Cannot find customers without invoices",
                "EXISTS (SELECT 1 FROM sales_order o WHERE o.customer_id = c.id "
                        + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') "
                        + "AND NOT EXISTS (SELECT 1 FROM invoice i WHERE i.customer_id = c.id "
                        + "AND i.invoice_date >= ? AND i.invoice_date <= ? AND i.status <> 'CANCELLED')",
                from, to, from, to);
    }

    /** Active open customers matching the condition, sorted by region, then by name, then id. */
    private List<Customer> listActiveOpen(String error, String condition, Object... params) {
        return jdbc.list(error, "SELECT " + Rows.CUSTOMER_COLUMNS + " FROM customer c WHERE c.status = 'ACTIVE' AND "
                + NOT_CLOSED + " AND " + condition + " ORDER BY c.region, c.name, c.id", Rows::customer, params);
    }

    // ---- activity of one customer ----

    /**
     * First and last order date, number and total value of the non-cancelled orders of the customer, the number of
     * its returns, and the sum of its payments and credit notes. Dates are null and the rest zero if there is nothing.
     */
    public CustomerActivity findActivity(long customerId) {
        String sql = "SELECT "
                + "(SELECT MIN(order_date) FROM sales_order WHERE customer_id = ? AND status <> 'CANCELLED') AS first_order, "
                + "(SELECT MAX(order_date) FROM sales_order WHERE customer_id = ? AND status <> 'CANCELLED') AS last_order, "
                + "(SELECT COUNT(*) FROM sales_order WHERE customer_id = ? AND status <> 'CANCELLED') AS order_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id WHERE o.customer_id = ?) AS return_count, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.customer_id = ? AND o.status <> 'CANCELLED') AS ordered, "
                + "(SELECT COALESCE(SUM(p.amount), 0) FROM payment p JOIN invoice i ON i.id = p.invoice_id WHERE i.customer_id = ?) AS paid, "
                + "(SELECT COALESCE(SUM(amount), 0) FROM credit_note WHERE customer_id = ?) AS credited";
        return jdbc.single("Cannot find customer activity", sql,
                rs -> new CustomerActivity(rs.getObject("first_order", LocalDate.class),
                        rs.getObject("last_order", LocalDate.class), rs.getInt("order_count"), rs.getInt("return_count"),
                        rs.getBigDecimal("ordered"), rs.getBigDecimal("paid"), rs.getBigDecimal("credited")),
                customerId, customerId, customerId, customerId, customerId, customerId, customerId);
    }

    // ---- counts ----

    /**
     * Per region of the customer, not counting closed customers: the number of active customers, customers on hold,
     * customers created between the two dates, inclusive (customers without a created date are not new), and active
     * customers without a non-cancelled order placed between the two dormant dates, inclusive. Sorted by region name;
     * regions without customers are left out.
     */
    public List<RegionCustomers> countPerRegion(LocalDate newFrom, LocalDate newTo, LocalDate dormantFrom, LocalDate dormantTo) {
        String sql = "SELECT c.region AS region, "
                + "SUM(CASE WHEN c.status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_count, "
                + "SUM(CASE WHEN c.status = 'ON_HOLD' THEN 1 ELSE 0 END) AS on_hold_count, "
                + "SUM(CASE WHEN c.created_date >= ? AND c.created_date <= ? THEN 1 ELSE 0 END) AS new_count, "
                + "SUM(CASE WHEN c.status = 'ACTIVE' AND " + NO_ORDER_IN_PERIOD + " THEN 1 ELSE 0 END) AS dormant_count "
                + "FROM customer c WHERE " + NOT_CLOSED + " GROUP BY c.region ORDER BY c.region";
        return jdbc.list("Cannot find customers per region", sql,
                rs -> new RegionCustomers(Region.valueOf(rs.getString("region")), rs.getInt("active_count"),
                        rs.getInt("on_hold_count"), rs.getInt("new_count"), rs.getInt("dormant_count")),
                newFrom, newTo, dormantFrom, dormantTo);
    }

    /**
     * For each of the four quarters of the year, in order: the number of customers created in the quarter, in total and
     * per region (customers without a created date are not counted). Quarters and regions without customers are
     * included with zeros.
     */
    public List<QuarterCustomers> countPerQuarter(int year) {
        return Breakdowns.perQuarter(jdbc, "Cannot find customers per quarter", CREATED, year, QuarterCustomers::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of customers created between the two dates, inclusive, in
     * total and per region (customers without a created date are not counted). Days and regions without customers are
     * included with zeros.
     */
    public List<WeekdayCustomers> countPerWeekday(LocalDate from, LocalDate to) {
        return Breakdowns.perWeekday(jdbc, "Cannot find customers per weekday", CREATED, from, to, WeekdayCustomers::new);
    }

    /**
     * The number of customers created between the two dates, inclusive, per sales rep (customers without a created date
     * or without a sales rep are not counted). Sorted by customer count, highest first, then by rep name, then id; reps
     * without customers in the period are included with 0.
     */
    public List<SalesRepCustomerCount> countPerSalesRep(LocalDate from, LocalDate to) {
        return Breakdowns.perSalesRep(jdbc, "Cannot find customer count per sales rep",
                "SELECT COUNT(*) FROM customer c WHERE c.sales_rep_id = r.id AND c.created_date >= ? AND c.created_date <= ?",
                from, to, SalesRepCustomerCount::new);
    }

    /**
     * The number of customers created between the two dates, inclusive, per country (customers without a created date
     * are not counted; countries without customers in the period are left out). Sorted by customer count, highest
     * first, then by country.
     */
    public List<CountryCustomerCount> countPerCountry(LocalDate from, LocalDate to) {
        return Breakdowns.perCountry(jdbc, "Cannot find customer count per country",
                "FROM customer c WHERE c.created_date >= ? AND c.created_date <= ?", from, to, CountryCustomerCount::new);
    }
}
