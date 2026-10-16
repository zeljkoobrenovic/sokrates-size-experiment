package com.acme.backoffice;

import com.acme.backoffice.model.DailyPaymentTotal;
import com.acme.backoffice.model.MonthlyPaymentTotal;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.PaymentReceived;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RepPayments;
import com.acme.backoffice.model.UnappliedPayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Payments on invoices: recording them, the payments of an invoice and their total, the payments received in a period
 * (all, of a customer, in a region, of a sales rep), payment totals per day, per month and per sales rep, the total
 * paid by a customer before a date, and unapplied payments (paying more than the invoice still had due).
 */
public final class PaymentStore {
    /** A payment with its invoice and the invoice's customer, as {@code p}, {@code i} and {@code c}. */
    private static final String FROM_PAYMENT_INVOICE_CUSTOMER =
            "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id ";

    /** Payment date between the two dates, inclusive (two parameters: from, to). */
    private static final String RECEIVED_IN_PERIOD = "p.payment_date >= ? AND p.payment_date <= ?";

    private final Jdbc jdbc;

    PaymentStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public Payment create(Payment payment) {
        long id = jdbc.insert("Cannot create payment", "INSERT INTO payment (invoice_id, payment_date, amount) VALUES (?, ?, ?)",
                payment.invoiceId(), payment.paymentDate(), payment.amount());
        return new Payment(id, payment.invoiceId(), payment.paymentDate(), payment.amount());
    }

    /** The payments of the invoice, oldest first (then by id). */
    public List<Payment> listOfInvoice(long invoiceId) {
        return jdbc.list("Cannot list payments",
                "SELECT id, invoice_id, payment_date, amount FROM payment WHERE invoice_id = ? ORDER BY payment_date, id",
                rs -> new Payment(rs.getLong("id"), rs.getLong("invoice_id"),
                        rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount")),
                invoiceId);
    }

    /** The sum of the payments of the invoice, zero if there are none. */
    public BigDecimal findTotalOfInvoice(long invoiceId) {
        return jdbc.queryAmount("Cannot find payment total",
                "SELECT COALESCE(SUM(amount), 0) AS total FROM payment WHERE invoice_id = ?", invoiceId);
    }

    /** The sum of the payments of the customer made before the date, without those of cancelled invoices, zero if there are none. */
    public BigDecimal findTotalOfCustomerBefore(long customerId, LocalDate before) {
        return jdbc.queryAmount("Cannot find payment total before date",
                "SELECT COALESCE(SUM(p.amount), 0) AS total FROM payment p JOIN invoice i ON i.id = p.invoice_id "
                        + "WHERE i.customer_id = ? AND p.payment_date < ? AND i.status <> 'CANCELLED'",
                customerId, before);
    }

    // ---- payments received in a period: all, of a customer, in a region, of a sales rep ----

    /** The payments received between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> listReceived(LocalDate from, LocalDate to) {
        return listReceivedMatching("Cannot find payments by period", "", from, to);
    }

    /** The payments of the customer received between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> listReceivedOfCustomer(long customerId, LocalDate from, LocalDate to) {
        return listReceivedMatching("Cannot find payments by period and customer", " AND c.id = ?", from, to, customerId);
    }

    /** The payments of the customer received between the two dates, inclusive, oldest first (then by id), without those of cancelled invoices. */
    public List<PaymentReceived> listReceivedOfCustomerExcludingCancelled(long customerId, LocalDate from, LocalDate to) {
        // the only one of the "payments received" lists that leaves out the payments of cancelled invoices
        return listReceivedMatching("Cannot find payments by customer and period",
                " AND c.id = ? AND i.status <> 'CANCELLED'", from, to, customerId);
    }

    /** The payments received from customers of the region between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> listReceivedInRegion(Region region, LocalDate from, LocalDate to) {
        return listReceivedMatching("Cannot find payments by region and period", " AND c.region = ?", from, to, region);
    }

    /** The payments of the customers of the sales rep received between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> listReceivedOfSalesRep(long salesRepId, LocalDate from, LocalDate to) {
        return listReceivedMatching("Cannot find payments of sales rep by period", " AND c.sales_rep_id = ?",
                from, to, salesRepId);
    }

    // ---- payment totals ----

    /**
     * Number and total of the payments received per day between the two dates, inclusive, oldest day first; days
     * without payments are left out.
     */
    public List<DailyPaymentTotal> totalsPerDay(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find payment totals per day",
                "SELECT payment_date, COUNT(*) AS payment_count, SUM(amount) AS total FROM payment "
                        + "WHERE payment_date >= ? AND payment_date <= ? GROUP BY payment_date ORDER BY payment_date",
                rs -> new DailyPaymentTotal(rs.getObject("payment_date", LocalDate.class),
                        rs.getInt("payment_count"), rs.getBigDecimal("total")),
                from, to);
    }

    /**
     * Number and total of the payments received from customers of the region per calendar month between the two
     * dates, inclusive, oldest month first; months without payments are left out.
     */
    public List<MonthlyPaymentTotal> totalsPerMonthInRegion(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT YEAR(p.payment_date) AS year_no, MONTH(p.payment_date) AS month_no, "
                + "COUNT(*) AS payment_count, SUM(p.amount) AS total "
                + FROM_PAYMENT_INVOICE_CUSTOMER
                + "WHERE " + RECEIVED_IN_PERIOD + " AND c.region = ? "
                + "GROUP BY YEAR(p.payment_date), MONTH(p.payment_date) ORDER BY YEAR(p.payment_date), MONTH(p.payment_date)";
        return jdbc.list("Cannot find payment totals per month by region", sql,
                rs -> new MonthlyPaymentTotal(rs.getInt("year_no"), rs.getInt("month_no"),
                        rs.getInt("payment_count"), rs.getBigDecimal("total")),
                from, to, region);
    }

    /**
     * Per sales rep with payments, the number and total amount of the payments of the rep's customers received between
     * the two dates, inclusive, biggest amount first (then by rep name, then id).
     */
    public List<RepPayments> receivedPerSalesRep(LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.name AS name, COUNT(*) AS payment_count, SUM(p.amount) AS total "
                + FROM_PAYMENT_INVOICE_CUSTOMER
                + "JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE " + RECEIVED_IN_PERIOD + " "
                + "GROUP BY r.id, r.name ORDER BY total DESC, r.name, r.id";
        return jdbc.list("Cannot find payments per sales rep", sql,
                rs -> new RepPayments(rs.getLong("id"), rs.getString("name"),
                        rs.getInt("payment_count"), rs.getBigDecimal("total")),
                from, to);
    }

    /**
     * Payments that exceed the amount due of their invoice (the invoice amount minus the earlier payments), with the
     * part that exceeds it, by customer name (then customer id), then oldest payment first (then by payment id).
     */
    public List<UnappliedPayment> listUnapplied() {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, p.id AS id, i.number AS invoice_number, "
                + "p.payment_date AS payment_date, p.amount AS amount, "
                + "LEAST(p.amount, (SELECT SUM(q.amount) FROM payment q WHERE q.invoice_id = p.invoice_id "
                + "AND (q.payment_date < p.payment_date OR (q.payment_date = p.payment_date AND q.id <= p.id))) - i.amount) AS unapplied_amount "
                + FROM_PAYMENT_INVOICE_CUSTOMER
                + "WHERE (SELECT SUM(q.amount) FROM payment q WHERE q.invoice_id = p.invoice_id "
                + "AND (q.payment_date < p.payment_date OR (q.payment_date = p.payment_date AND q.id <= p.id))) > i.amount "
                + "ORDER BY c.name, c.id, p.payment_date, p.id";
        return jdbc.list("Cannot find unapplied payments", sql,
                rs -> new UnappliedPayment(rs.getLong("id"), rs.getString("invoice_number"),
                        rs.getLong("customer_id"), rs.getString("customer_name"),
                        rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount"),
                        rs.getBigDecimal("unapplied_amount")));
    }

    /**
     * The payments received between the two dates, inclusive, that match the condition (starting with
     * {@code " AND "}, or empty; its parameters follow the dates), oldest first (then by id).
     */
    private List<PaymentReceived> listReceivedMatching(String error, String condition, LocalDate from, LocalDate to,
                                                       Object... conditionParams) {
        String sql = "SELECT p.id AS id, i.number AS invoice_number, c.id AS customer_id, c.name AS customer_name, "
                + "p.payment_date AS payment_date, p.amount AS amount "
                + FROM_PAYMENT_INVOICE_CUSTOMER
                + "WHERE " + RECEIVED_IN_PERIOD + condition + " ORDER BY p.payment_date, p.id";
        Object[] params = new Object[2 + conditionParams.length];
        params[0] = from;
        params[1] = to;
        System.arraycopy(conditionParams, 0, params, 2, conditionParams.length);
        return jdbc.list(error, sql,
                rs -> new PaymentReceived(rs.getLong("id"), rs.getString("invoice_number"),
                        rs.getLong("customer_id"), rs.getString("customer_name"),
                        rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount")),
                params);
    }
}
