package com.acme.backoffice;

import com.acme.backoffice.model.CustomerOpenCredit;
import com.acme.backoffice.model.CustomerOverLimit;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionOpenCredit;
import com.acme.backoffice.model.RegionOverLimit;
import com.acme.backoffice.model.RepOverLimit;

import java.time.LocalDate;
import java.util.List;

/**
 * Customer credit: the open credit of customers (credit notes not yet applied to an invoice), per customer and per
 * region, and the customers whose open invoice balance exceeds their credit limit, per customer, region and sales rep.
 */
public final class CreditLimitStore {
    /** Credit notes not used against an invoice, with their customer {@code c}. */
    private static final String FROM_OPEN_CREDIT_NOTES = "FROM credit_note n JOIN customer c ON c.id = n.customer_id "
            + "WHERE NOT EXISTS (SELECT 1 FROM credit_note_application a WHERE a.credit_note_id = n.id)";

    /**
     * The open invoice balance per customer on a date ({@code x.customer_id}, {@code x.balance}): open invoices dated up
     * to the date less the payments made up to the date. Two parameters: the date, twice.
     */
    private static final String FROM_BALANCES = "FROM (SELECT i.customer_id AS customer_id, SUM(i.amount - "
            + "(SELECT COALESCE(SUM(p.amount), 0) FROM payment p WHERE p.invoice_id = i.id AND p.payment_date <= ?)) AS balance "
            + "FROM invoice i WHERE i.status = 'OPEN' AND i.invoice_date <= ? GROUP BY i.customer_id) x "
            + "JOIN customer c ON c.id = x.customer_id";

    /** Customers with a credit limit whose balance exceeds it (customers without a limit never do). */
    private static final String OVER_LIMIT = " WHERE c.credit_limit IS NOT NULL AND x.balance > c.credit_limit";

    private final Jdbc jdbc;

    CreditLimitStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * The customers with credit notes not used against an invoice, with the number and total of those notes.
     * Sorted by region, then largest open amount first, then name and id.
     */
    public List<CustomerOpenCredit> listCustomersWithOpenCredit() {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, COUNT(n.id) AS note_count, "
                + "SUM(n.amount) AS open_amount " + FROM_OPEN_CREDIT_NOTES
                + " GROUP BY c.id, c.name, c.region ORDER BY c.region, open_amount DESC, c.name, c.id";
        return jdbc.list("Cannot find customers with open credit", sql,
                rs -> new CustomerOpenCredit(rs.getLong("id"), rs.getString("name"), Region.valueOf(rs.getString("region")),
                        rs.getInt("note_count"), rs.getBigDecimal("open_amount")));
    }

    /** The open (unused) credit per region: customers, notes and total amount. Largest amount first, then region. */
    public List<RegionOpenCredit> openCreditPerRegion() {
        String sql = "SELECT c.region AS region, COUNT(DISTINCT c.id) AS customer_count, COUNT(n.id) AS note_count, "
                + "SUM(n.amount) AS open_amount " + FROM_OPEN_CREDIT_NOTES
                + " GROUP BY c.region ORDER BY open_amount DESC, c.region";
        return jdbc.list("Cannot find open credit per region", sql,
                rs -> new RegionOpenCredit(Region.valueOf(rs.getString("region")), rs.getInt("customer_count"),
                        rs.getInt("note_count"), rs.getBigDecimal("open_amount")));
    }

    /**
     * The customers whose open invoice amount on the date exceeds their credit limit (customers without a limit never do).
     * Only invoices dated and payments made up to the date count. Sorted by region, then sales rep name (customers
     * without a rep last), then largest excess first, then name and id.
     */
    public List<CustomerOverLimit> listCustomersOverLimit(LocalDate asOf) {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, r.name AS rep_name, "
                + "c.credit_limit AS credit_limit, x.balance AS balance " + FROM_BALANCES
                + " LEFT JOIN sales_rep r ON r.id = c.sales_rep_id" + OVER_LIMIT
                + " ORDER BY c.region, r.name NULLS LAST, (x.balance - c.credit_limit) DESC, c.name, c.id";
        return jdbc.list("Cannot find customers over credit limit", sql,
                rs -> new CustomerOverLimit(rs.getLong("id"), rs.getString("name"), Region.valueOf(rs.getString("region")),
                        rs.getString("rep_name"), rs.getBigDecimal("credit_limit"), rs.getBigDecimal("balance")),
                asOf, asOf);
    }

    /** Per region, the customers over their credit limit on the date and the total excess. Largest excess first, then region. */
    public List<RegionOverLimit> overLimitPerRegion(LocalDate asOf) {
        String sql = "SELECT c.region AS region, COUNT(*) AS customer_count, SUM(x.balance - c.credit_limit) AS excess "
                + FROM_BALANCES + OVER_LIMIT + " GROUP BY c.region ORDER BY excess DESC, c.region";
        return jdbc.list("Cannot find over credit limit per region", sql,
                rs -> new RegionOverLimit(Region.valueOf(rs.getString("region")), rs.getInt("customer_count"),
                        rs.getBigDecimal("excess")),
                asOf, asOf);
    }

    /**
     * Per sales rep, the customers over their credit limit on the date and the total excess. Customers without a rep
     * are left out. Largest excess first, then rep name and id.
     */
    public List<RepOverLimit> overLimitPerSalesRep(LocalDate asOf) {
        String sql = "SELECT r.id AS id, r.name AS name, COUNT(*) AS customer_count, SUM(x.balance - c.credit_limit) AS excess "
                + FROM_BALANCES + " JOIN sales_rep r ON r.id = c.sales_rep_id" + OVER_LIMIT
                + " GROUP BY r.id, r.name ORDER BY excess DESC, r.name, r.id";
        return jdbc.list("Cannot find over credit limit per sales rep", sql,
                rs -> new RepOverLimit(rs.getLong("id"), rs.getString("name"), rs.getInt("customer_count"),
                        rs.getBigDecimal("excess")),
                asOf, asOf);
    }
}
