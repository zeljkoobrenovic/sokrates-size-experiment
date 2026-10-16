package com.acme.backoffice;

import com.acme.backoffice.model.CountryInvoiceCount;
import com.acme.backoffice.model.Currency;
import com.acme.backoffice.model.CurrencyOpenInvoice;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceAging;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.InvoiceStatusTotal;
import com.acme.backoffice.model.LatePaidInvoice;
import com.acme.backoffice.model.PartiallyPaidInvoice;
import com.acme.backoffice.model.QuarterInvoices;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionLargeInvoice;
import com.acme.backoffice.model.RepOverdueInvoices;
import com.acme.backoffice.model.SalesRepInvoiceCount;
import com.acme.backoffice.model.WeekdayInvoices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Customer invoices: creating them and changing their status, finding one by number or order, the invoices of a
 * customer, open and overdue invoices (all, of a customer, in a region, of a sales rep), invoice search, the latest
 * invoices, large, late-paid and partially paid invoices, open invoices per currency, the open balance and aging of
 * the customers, and the invoice counts per quarter, weekday, sales rep and country.
 */
public final class InvoiceStore {
    private static final String SELECT_INVOICES = "SELECT " + Rows.INVOICE_COLUMNS + " FROM invoice i ";
    private static final String JOIN_CUSTOMER = "JOIN customer c ON c.id = i.customer_id ";

    /** An open invoice whose due date is before the date (one parameter: the date). */
    private static final String OVERDUE = "i.status = 'OPEN' AND i.due_date < ?";

    /** An invoice that counts as issued: every status but cancelled. */
    private static final String NOT_CANCELLED = "i.status <> 'CANCELLED'";

    /** Invoice date between the two dates, inclusive (two parameters: from, to). */
    private static final String ISSUED_IN_PERIOD = "i.invoice_date >= ? AND i.invoice_date <= ?";

    /** Open and paid invoices of customers in a region dated in a period (parameters: region, from, to). */
    private static final String OPEN_AND_PAID_IN_REGION_AND_PERIOD = "FROM invoice i " + JOIN_CUSTOMER
            + "WHERE i.status IN ('OPEN', 'PAID') AND c.region = ? AND " + ISSUED_IN_PERIOD + " ";

    private static final Breakdowns.Counted ISSUED_INVOICES = new Breakdowns.Counted(
            "FROM invoice i " + JOIN_CUSTOMER, "i.invoice_date", " AND " + NOT_CANCELLED, "c.region");

    private final Jdbc jdbc;

    InvoiceStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public Invoice create(Invoice invoice) {
        long id = jdbc.insert("Cannot create invoice",
                "INSERT INTO invoice (number, order_id, customer_id, invoice_date, due_date, amount, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                invoice.number(), invoice.orderId(), invoice.customerId(), invoice.invoiceDate(), invoice.dueDate(),
                invoice.amount(), invoice.status());
        return new Invoice(id, invoice.number(), invoice.orderId(), invoice.customerId(),
                invoice.invoiceDate(), invoice.dueDate(), invoice.amount(), invoice.status());
    }

    public void updateStatus(long invoiceId, InvoiceStatus status) {
        jdbc.update("Cannot update invoice status", "UPDATE invoice SET status = ? WHERE id = ?", status, invoiceId);
    }

    public int count() {
        return jdbc.queryInt("Cannot count invoices", "SELECT COUNT(*) AS invoice_count FROM invoice");
    }

    public Optional<Invoice> findByNumber(String number) {
        return jdbc.first("Cannot find invoice", SELECT_INVOICES + "WHERE i.number = ?", Rows::invoice, number);
    }

    /** The invoice of the order that is not cancelled, if any. */
    public Optional<Invoice> findActiveOfOrder(long orderId) {
        return jdbc.first("Cannot find invoice of order",
                SELECT_INVOICES + "WHERE i.order_id = ? AND " + NOT_CANCELLED, Rows::invoice, orderId);
    }

    /** All invoices of the customer, newest invoice date first (then by highest id). */
    public List<Invoice> listOfCustomer(long customerId) {
        return list("Cannot list invoices", "WHERE i.customer_id = ? ORDER BY i.invoice_date DESC, i.id DESC", customerId);
    }

    /**
     * Invoices matching all the given criteria, oldest due date first. Every criterion is optional: null means no
     * restriction. The due dates are inclusive and the minimum amount is inclusive.
     */
    public List<Invoice> search(Long customerId, InvoiceStatus status, LocalDate dueFrom, LocalDate dueTo, BigDecimal minAmount) {
        StringBuilder where = new StringBuilder("WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();
        if (customerId != null) {
            where.append(" AND i.customer_id = ?");
            parameters.add(customerId);
        }
        if (status != null) {
            where.append(" AND i.status = ?");
            parameters.add(status.name());
        }
        if (dueFrom != null) {
            where.append(" AND i.due_date >= ?");
            parameters.add(dueFrom);
        }
        if (dueTo != null) {
            where.append(" AND i.due_date <= ?");
            parameters.add(dueTo);
        }
        if (minAmount != null) {
            where.append(" AND i.amount >= ?");
            parameters.add(minAmount);
        }
        where.append(" ORDER BY i.due_date, i.id");
        return list("Cannot search invoices", where.toString(), parameters.toArray());
    }

    // ---- open and overdue invoices: all, of a customer, in a region, of a sales rep ----

    /** Open invoices whose due date is before the given date, oldest due date first. */
    public List<Invoice> listOverdue(LocalDate asOf) {
        return listOpen("Cannot list overdue invoices", "", OVERDUE, asOf);
    }

    /** Open invoices of the customer whose due date is before the given date, oldest due date first. */
    public List<Invoice> listOverdueOfCustomer(LocalDate asOf, long customerId) {
        return listOpen("Cannot list overdue invoices of customer", "", OVERDUE + " AND i.customer_id = ?", asOf, customerId);
    }

    /** Open invoices of customers in the region whose due date is before the given date, oldest due date first. */
    public List<Invoice> listOverdueInRegion(LocalDate asOf, Region region) {
        return listOpen("Cannot list overdue invoices of region", JOIN_CUSTOMER, OVERDUE + " AND c.region = ?", asOf, region);
    }

    /** Open invoices of the customers of the sales rep whose due date is before the given date, oldest due date first. */
    public List<Invoice> listOverdueOfSalesRep(LocalDate asOf, long salesRepId) {
        return listOpen("Cannot list overdue invoices of sales rep", JOIN_CUSTOMER, OVERDUE + " AND c.sales_rep_id = ?",
                asOf, salesRepId);
    }

    /** Open invoices of the customers of the sales rep, oldest due date first. */
    public List<Invoice> listOpenOfSalesRep(long salesRepId) {
        return listOpen("Cannot list open invoices of sales rep", JOIN_CUSTOMER, "i.status = 'OPEN' AND c.sales_rep_id = ?",
                salesRepId);
    }

    /** Sum of the amounts of the open invoices issued on or before the date whose due date is before it. */
    public BigDecimal findOverdueAmount(LocalDate asOf) {
        // unlike the overdue lists, this also requires the invoice to be dated on or before the date
        return jdbc.queryAmount("Cannot find overdue invoice amount",
                "SELECT COALESCE(SUM(i.amount), 0) AS total FROM invoice i WHERE " + OVERDUE + " AND i.invoice_date <= ?",
                asOf, asOf);
    }

    /**
     * Per sales rep with overdue invoices, the number and total amount of the open invoices of the rep's customers whose
     * due date is before the given date, biggest amount first (then by rep name, then id).
     */
    public List<RepOverdueInvoices> overduePerSalesRep(LocalDate asOf) {
        String sql = "SELECT r.id AS id, r.name AS name, COUNT(*) AS invoice_count, SUM(i.amount) AS total FROM invoice i "
                + JOIN_CUSTOMER
                + "JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE " + OVERDUE + " "
                + "GROUP BY r.id, r.name ORDER BY total DESC, r.name, r.id";
        return jdbc.list("Cannot find overdue invoices per sales rep", sql,
                rs -> new RepOverdueInvoices(rs.getLong("id"), rs.getString("name"),
                        rs.getInt("invoice_count"), rs.getBigDecimal("total")),
                asOf);
    }

    // ---- invoices issued in a period ----

    /** The invoices of the customer dated between the two dates, inclusive, oldest first (then by id), without cancelled ones. */
    public List<Invoice> listOfCustomerInPeriod(long customerId, LocalDate from, LocalDate to) {
        return listIssuedInPeriod("Cannot list invoices by customer and period", "", "i.customer_id = ?",
                customerId, from, to);
    }

    /**
     * The invoices of the customers of the country issued between the two dates, inclusive (cancelled invoices are
     * left out), oldest first, then by id.
     */
    public List<Invoice> listOfCountryInPeriod(String country, LocalDate from, LocalDate to) {
        return listIssuedInPeriod("Cannot find invoices by country", JOIN_CUSTOMER, "c.country = ?", country, from, to);
    }

    /** Open and paid invoices of customers in the region dated between the two dates, inclusive, oldest invoice first (then by id). */
    public List<Invoice> listOpenAndPaidInRegion(Region region, LocalDate from, LocalDate to) {
        return jdbc.list("Cannot list open and paid invoices of region",
                "SELECT " + Rows.INVOICE_COLUMNS + " " + OPEN_AND_PAID_IN_REGION_AND_PERIOD + "ORDER BY i.invoice_date, i.id",
                Rows::invoice, region, from, to);
    }

    /** Number and total amount of the open and paid invoices of customers in the region dated between the two dates, inclusive, per status; statuses without invoices are left out. */
    public List<InvoiceStatusTotal> openAndPaidTotalsPerStatusInRegion(Region region, LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find invoice totals of region",
                "SELECT i.status AS status, COUNT(*) AS invoice_count, SUM(i.amount) AS total "
                        + OPEN_AND_PAID_IN_REGION_AND_PERIOD + "GROUP BY i.status ORDER BY i.status",
                rs -> new InvoiceStatusTotal(InvoiceStatus.valueOf(rs.getString("status")),
                        rs.getInt("invoice_count"), rs.getBigDecimal("total")),
                region, from, to);
    }

    /** Paid invoices of the customers of the sales rep whose last payment was made between the two dates, inclusive, oldest invoice first (then by id). */
    public List<Invoice> listPaidOfSalesRep(long salesRepId, LocalDate from, LocalDate to) {
        return list("Cannot list paid invoices of sales rep", JOIN_CUSTOMER
                        + "WHERE i.status = 'PAID' AND c.sales_rep_id = ? "
                        + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) >= ? "
                        + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) <= ? "
                        + "ORDER BY i.invoice_date, i.id",
                salesRepId, from, to);
    }

    /** Open and paid invoices dated between the two dates, inclusive, with an amount above the given amount, with the region of their customer: by region, biggest invoice first (then by id). */
    public List<RegionLargeInvoice> listLargePerRegion(BigDecimal minAmount, LocalDate from, LocalDate to) {
        String sql = "SELECT c.region AS region, " + Rows.INVOICE_COLUMNS + " FROM invoice i "
                + JOIN_CUSTOMER
                + "WHERE i.status IN ('OPEN', 'PAID') AND i.amount > ? AND " + ISSUED_IN_PERIOD + " "
                + "ORDER BY c.region, i.amount DESC, i.id";
        return jdbc.list("Cannot list large invoices per region", sql,
                rs -> new RegionLargeInvoice(Region.valueOf(rs.getString("region")), Rows.invoice(rs)),
                minAmount, from, to);
    }

    /** Paid invoices whose last payment was made after the due date and between the two dates, inclusive, by customer name (then customer id), then oldest due date first (then by invoice id). */
    public List<LatePaidInvoice> listLatePaid(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, i.number AS invoice_number, i.due_date AS due_date, "
                + "(SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) AS paid_date, "
                + "DATEDIFF('DAY', i.due_date, (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id)) AS days_late "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'PAID' "
                + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) > i.due_date "
                + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) >= ? "
                + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) <= ? "
                + "ORDER BY c.name, c.id, i.due_date, i.id";
        return jdbc.list("Cannot find late paid invoices", sql,
                rs -> new LatePaidInvoice(rs.getLong("customer_id"), rs.getString("customer_name"),
                        rs.getString("invoice_number"), rs.getObject("due_date", LocalDate.class),
                        rs.getObject("paid_date", LocalDate.class), rs.getInt("days_late")),
                from, to);
    }

    /** Open invoices with at least one payment, with the amount paid and still due, by customer region, customer name (then customer id), then oldest due date first (then by invoice id). */
    public List<PartiallyPaidInvoice> listPartiallyPaid() {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, c.region AS region, i.number AS invoice_number, "
                + "i.due_date AS due_date, i.amount AS amount, "
                + "(SELECT SUM(p.amount) FROM payment p WHERE p.invoice_id = i.id) AS paid_amount "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'OPEN' "
                + "AND EXISTS (SELECT 1 FROM payment p WHERE p.invoice_id = i.id) "
                + "ORDER BY c.region, c.name, c.id, i.due_date, i.id";
        return jdbc.list("Cannot find partially paid invoices", sql, rs -> {
            BigDecimal amount = rs.getBigDecimal("amount");
            BigDecimal paidAmount = rs.getBigDecimal("paid_amount");
            return new PartiallyPaidInvoice(rs.getLong("customer_id"), rs.getString("customer_name"),
                    Region.valueOf(rs.getString("region")), rs.getString("invoice_number"),
                    rs.getObject("due_date", LocalDate.class), amount, paidAmount, amount.subtract(paidAmount));
        });
    }

    /** Open invoices with the currency they were issued in (EUR when none is recorded) and their amount in it and in EUR, by currency, customer name (then customer id), then oldest due date first (then by invoice id). */
    public List<CurrencyOpenInvoice> listOpenPerCurrency() {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, COALESCE(ic.currency, 'EUR') AS currency, "
                + "i.number AS invoice_number, i.due_date AS due_date, COALESCE(ic.amount, i.amount) AS currency_amount, "
                + "i.amount AS eur_amount "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "LEFT JOIN invoice_currency ic ON ic.invoice_id = i.id "
                + "WHERE i.status = 'OPEN' "
                + "ORDER BY COALESCE(ic.currency, 'EUR'), c.name, c.id, i.due_date, i.id";
        return jdbc.list("Cannot find open invoices by currency", sql,
                rs -> new CurrencyOpenInvoice(rs.getLong("customer_id"), rs.getString("customer_name"),
                        Currency.valueOf(rs.getString("currency")), rs.getString("invoice_number"),
                        rs.getObject("due_date", LocalDate.class), rs.getBigDecimal("currency_amount"),
                        rs.getBigDecimal("eur_amount")));
    }

    // ---- latest invoices: all, in a region, of a customer ----

    /**
     * The N most recent invoices (cancelled invoices are left out), newest invoice date first, then by highest id.
     */
    public List<Invoice> listLatest(int limit) {
        return latestMatching("Cannot find latest invoices", "", "", limit);
    }

    /**
     * The N most recent invoices of customers in the region (cancelled invoices are left out), newest invoice date
     * first, then by highest id.
     */
    public List<Invoice> listLatestInRegion(Region region, int limit) {
        return latestMatching("Cannot find latest invoices by region", JOIN_CUSTOMER, " AND c.region = ?", region, limit);
    }

    /**
     * The N most recent invoices of the customer (cancelled invoices are left out), newest invoice date first, then by
     * highest id.
     */
    public List<Invoice> listLatestOfCustomer(long customerId, int limit) {
        return latestMatching("Cannot find latest invoices by customer", "", " AND i.customer_id = ?", customerId, limit);
    }

    // ---- totals and balances ----

    /** The sum of the invoices of the customer dated before the date, without cancelled ones, zero if there are none. */
    public BigDecimal findTotalOfCustomerBefore(long customerId, LocalDate before) {
        return jdbc.queryAmount("Cannot find invoice total before date",
                "SELECT COALESCE(SUM(i.amount), 0) AS total FROM invoice i "
                        + "WHERE i.customer_id = ? AND i.invoice_date < ? AND " + NOT_CANCELLED,
                customerId, before);
    }

    /** The unpaid part of all open invoices of the customer (invoice amounts minus their payments), zero if there are none. */
    public BigDecimal findOpenBalanceOfCustomer(long customerId) {
        return jdbc.queryAmount("Cannot find open invoice balance",
                "SELECT COALESCE(SUM(i.amount), 0) - COALESCE(SUM((SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                        + "WHERE p.invoice_id = i.id)), 0) AS balance FROM invoice i WHERE i.customer_id = ? AND i.status = 'OPEN'",
                customerId);
    }

    /**
     * The unpaid part of the open invoices per customer in aging buckets on the given date: not due (due date on or
     * after the date), 1-30, 31-60, 61-90 and more than 90 days overdue. Only invoices dated and payments made up to
     * the date count. Customers without such invoices are left out, ordered by customer name.
     */
    public List<InvoiceAging> agingPerCustomer(LocalDate asOf) {
        String sql = "SELECT c.id AS id, c.name AS name, "
                + "COALESCE(SUM(CASE WHEN x.days <= 0 THEN x.balance END), 0) AS not_due, "
                + "COALESCE(SUM(CASE WHEN x.days BETWEEN 1 AND 30 THEN x.balance END), 0) AS days_1_30, "
                + "COALESCE(SUM(CASE WHEN x.days BETWEEN 31 AND 60 THEN x.balance END), 0) AS days_31_60, "
                + "COALESCE(SUM(CASE WHEN x.days BETWEEN 61 AND 90 THEN x.balance END), 0) AS days_61_90, "
                + "COALESCE(SUM(CASE WHEN x.days > 90 THEN x.balance END), 0) AS over_90 "
                + "FROM (SELECT i.customer_id AS customer_id, DATEDIFF('DAY', i.due_date, ?) AS days, "
                + "i.amount - (SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id AND p.payment_date <= ?) AS balance "
                + "FROM invoice i WHERE i.status = 'OPEN' AND i.invoice_date <= ?) x "
                + "JOIN customer c ON c.id = x.customer_id "
                + "GROUP BY c.id, c.name ORDER BY c.name, c.id";
        return jdbc.list("Cannot find invoice aging", sql,
                rs -> new InvoiceAging(rs.getLong("id"), rs.getString("name"), rs.getBigDecimal("not_due"),
                        rs.getBigDecimal("days_1_30"), rs.getBigDecimal("days_31_60"),
                        rs.getBigDecimal("days_61_90"), rs.getBigDecimal("over_90")),
                asOf, asOf, asOf);
    }

    // ---- invoice counts per quarter, weekday, sales rep and country (cancelled invoices are never counted) ----

    /**
     * For each of the four quarters of the year, in order: the number of invoices issued in the quarter, in total and per
     * region of the customer (cancelled invoices are not counted). Quarters and regions without invoices are included
     * with zeros.
     */
    public List<QuarterInvoices> countPerQuarter(int year) {
        return Breakdowns.perQuarter(jdbc, "Cannot find invoices per quarter", ISSUED_INVOICES, year, QuarterInvoices::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of invoices issued between the two dates, inclusive, in
     * total and per region of the customer (cancelled invoices are not counted). Days and regions without invoices are
     * included with zeros.
     */
    public List<WeekdayInvoices> countPerWeekday(LocalDate from, LocalDate to) {
        return Breakdowns.perWeekday(jdbc, "Cannot find invoices per weekday", ISSUED_INVOICES, from, to, WeekdayInvoices::new);
    }

    /**
     * The number of invoices issued between the two dates, inclusive, to the customers of each sales rep (cancelled
     * invoices are not counted), highest first, then by name and id; reps without invoices are included with zero.
     */
    public List<SalesRepInvoiceCount> countPerSalesRep(LocalDate from, LocalDate to) {
        return Breakdowns.perSalesRep(jdbc, "Cannot find invoice count per sales rep",
                "SELECT COUNT(*) FROM invoice i " + JOIN_CUSTOMER + "WHERE c.sales_rep_id = r.id AND " + ISSUED_IN_PERIOD
                        + " AND " + NOT_CANCELLED,
                from, to, SalesRepInvoiceCount::new);
    }

    /**
     * The number of invoices issued between the two dates, inclusive, to the customers of each country (cancelled
     * invoices are not counted; countries without invoices in the period are left out). Sorted by invoice count,
     * highest first, then by country.
     */
    public List<CountryInvoiceCount> countPerCountry(LocalDate from, LocalDate to) {
        return Breakdowns.perCountry(jdbc, "Cannot find invoice count per country",
                "FROM invoice i " + JOIN_CUSTOMER + "WHERE " + ISSUED_IN_PERIOD + " AND " + NOT_CANCELLED,
                from, to, CountryInvoiceCount::new);
    }

    // ---- shared queries ----

    /** Invoices with the given joins, WHERE and ORDER BY (after {@code FROM invoice i}). */
    private List<Invoice> list(String error, String joinsWhereOrder, Object... params) {
        return jdbc.list(error, SELECT_INVOICES + joinsWhereOrder, Rows::invoice, params);
    }

    /** Open (or overdue) invoices matching the condition, oldest due date first (then by id). */
    private List<Invoice> listOpen(String error, String joins, String condition, Object... params) {
        return list(error, joins + "WHERE " + condition + " ORDER BY i.due_date, i.id", params);
    }

    /**
     * Invoices that are not cancelled, matching the filter (one parameter) and dated between the two dates, inclusive,
     * oldest first (then by id).
     */
    private List<Invoice> listIssuedInPeriod(String error, String joins, String filter, Object filterValue,
                                             LocalDate from, LocalDate to) {
        return list(error, joins + "WHERE " + filter + " AND " + ISSUED_IN_PERIOD + " AND " + NOT_CANCELLED
                + " ORDER BY i.invoice_date, i.id", filterValue, from, to);
    }

    /**
     * The N most recent invoices that are not cancelled and match the condition (starting with {@code " AND "}, or
     * empty), newest invoice date first, then by highest id. The last parameter is the limit.
     */
    private List<Invoice> latestMatching(String error, String joins, String condition, Object... paramsAndLimit) {
        return list(error, joins + "WHERE " + NOT_CANCELLED + condition
                + " ORDER BY i.invoice_date DESC, i.id DESC LIMIT ?", paramsAndLimit);
    }
}
