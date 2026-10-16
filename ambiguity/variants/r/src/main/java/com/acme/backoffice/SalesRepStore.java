package com.acme.backoffice;

import com.acme.backoffice.model.CommissionBasis;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RepPerformance;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesTargetProgress;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Sales reps: the reps themselves, the managers of the regions, a rep's performance (orders of the rep's customers),
 * the monthly sales targets and the progress towards them, and the commission basis (paid invoices of the rep's
 * customers against the monthly target).
 */
public final class SalesRepStore {
    private static final String SELECT_REPS = "SELECT r.id, r.name, r.email, r.region FROM sales_rep r ";

    /**
     * Per rep: id, name, the paid invoices of the rep's customers dated between the two bound dates (inclusive) and the
     * target of the month starting at the third bound date (null when none). Shared by the single and the list query.
     */
    private static final String SELECT_COMMISSION_BASES = "SELECT r.id AS id, r.name AS name, "
            + "COALESCE((SELECT SUM(i.amount) FROM invoice i JOIN customer c ON c.id = i.customer_id "
            + "WHERE c.sales_rep_id = r.id AND i.status = 'PAID' AND i.invoice_date >= ? AND i.invoice_date <= ?), 0) AS paid, "
            + "t.target_amount AS target "
            + "FROM sales_rep r LEFT JOIN sales_target t ON t.sales_rep_id = r.id AND t.target_month = ? ";

    private final Jdbc jdbc;

    SalesRepStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    /** Adds the rep; returns it with its generated id. */
    public SalesRep create(SalesRep salesRep) {
        long id = jdbc.insert("Cannot create sales rep", "INSERT INTO sales_rep (name, email, region) VALUES (?, ?, ?)",
                salesRep.name(), salesRep.email(), salesRep.region());
        return new SalesRep(id, salesRep.name(), salesRep.email(), salesRep.region());
    }

    /** All reps, sorted by name, then id. */
    public List<SalesRep> listByName() {
        return jdbc.list("Cannot list sales reps", SELECT_REPS + "ORDER BY r.name, r.id", Rows::salesRep);
    }

    /** The reps of the region, sorted by name, then id. */
    public List<SalesRep> listByRegion(Region region) {
        return jdbc.list("Cannot list sales reps by region", SELECT_REPS + "WHERE r.region = ? ORDER BY r.name, r.id",
                Rows::salesRep, region);
    }

    public Optional<SalesRep> findById(long salesRepId) {
        return jdbc.first("Cannot find sales rep", SELECT_REPS + "WHERE r.id = ?", Rows::salesRep, salesRepId);
    }

    /** Sets (or replaces) the manager of the region. */
    public void saveRegionManager(Region region, long salesRepId) {
        jdbc.update("Cannot save region manager",
                "MERGE INTO region_manager (region, sales_rep_id) KEY (region) VALUES (?, ?)", region, salesRepId);
    }

    /** The rep managing the region, if any. */
    public Optional<SalesRep> findRegionManager(Region region) {
        return jdbc.first("Cannot find region manager",
                SELECT_REPS + "JOIN region_manager m ON m.sales_rep_id = r.id WHERE m.region = ?", Rows::salesRep, region);
    }

    /** The regions managed by the rep, in name order. */
    public List<Region> listRegionsManagedBy(long salesRepId) {
        return jdbc.list("Cannot list regions managed by sales rep",
                "SELECT region FROM region_manager WHERE sales_rep_id = ? ORDER BY region",
                rs -> Region.valueOf(rs.getString("region")), salesRepId);
    }

    /** Number and total value of the non-cancelled orders of the rep's customers placed between the two dates, inclusive. */
    public RepPerformance findPerformance(long salesRepId, LocalDate from, LocalDate to) {
        // Left join on lines: orders without lines count; listTargetProgress below sums lines only (inner join).
        return jdbc.single("Cannot find sales rep performance",
                "SELECT COUNT(DISTINCT o.id) AS order_count, COALESCE(SUM(l.quantity * l.unit_price), 0) AS total "
                        + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                        + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                        + "WHERE c.sales_rep_id = ? AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED'",
                rs -> new RepPerformance(rs.getInt("order_count"), rs.getBigDecimal("total")),
                salesRepId, from, to);
    }

    /** Sets the target of the rep for the month starting at the given date, replacing an existing one. */
    public void saveTarget(long salesRepId, LocalDate month, BigDecimal target) {
        jdbc.update("Cannot save sales target",
                "MERGE INTO sales_target (sales_rep_id, target_month, target_amount) KEY (sales_rep_id, target_month) VALUES (?, ?, ?)",
                salesRepId, month, target);
    }

    /**
     * For each rep with a target for the month starting at the first date: the target, the revenue of the
     * non-cancelled orders of the rep's customers placed between the two dates, inclusive, and the percentage reached
     * (rounded to 2 decimals). Sorted by rep name, then id.
     */
    public List<SalesTargetProgress> listTargetProgress(LocalDate monthStart, LocalDate monthEnd) {
        return jdbc.list("Cannot find sales target progress",
                "SELECT r.id AS id, r.name AS name, t.target_amount AS target, "
                        + "COALESCE((SELECT SUM(l.quantity * l.unit_price) FROM sales_order o "
                        + "JOIN customer c ON c.id = o.customer_id JOIN sales_order_line l ON l.order_id = o.id "
                        + "WHERE c.sales_rep_id = r.id AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED'), 0) AS revenue "
                        + "FROM sales_rep r JOIN sales_target t ON t.sales_rep_id = r.id AND t.target_month = ? "
                        + "ORDER BY r.name, r.id",
                rs -> {
                    BigDecimal target = rs.getBigDecimal("target");
                    BigDecimal revenue = rs.getBigDecimal("revenue");
                    return new SalesTargetProgress(rs.getLong("id"), rs.getString("name"), target, revenue,
                            revenue.multiply(new BigDecimal("100")).divide(target, 2, RoundingMode.HALF_UP));
                },
                monthStart, monthEnd, monthStart);
    }

    /**
     * The total of the paid invoices of the rep's customers dated between the two dates, inclusive, and the rep's
     * target for the month starting at the first date (null if none). Empty if the rep does not exist.
     */
    public Optional<CommissionBasis> findCommissionBasis(long salesRepId, LocalDate monthStart, LocalDate monthEnd) {
        return jdbc.first("Cannot find commission basis", SELECT_COMMISSION_BASES + "WHERE r.id = ?",
                SalesRepStore::commissionBasis, monthStart, monthEnd, monthStart, salesRepId);
    }

    /** Like findCommissionBasis, for every rep, sorted by rep name, then id. */
    public List<CommissionBasis> listCommissionBases(LocalDate monthStart, LocalDate monthEnd) {
        return jdbc.list("Cannot list commission bases", SELECT_COMMISSION_BASES + "ORDER BY r.name, r.id",
                SalesRepStore::commissionBasis, monthStart, monthEnd, monthStart);
    }

    private static CommissionBasis commissionBasis(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new CommissionBasis(rs.getLong("id"), rs.getString("name"), rs.getBigDecimal("paid"),
                rs.getBigDecimal("target"));
    }
}
