package com.acme.backoffice;

import com.acme.backoffice.model.CustomerPromotionLine;
import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.model.PromotionUsage;
import com.acme.backoffice.model.Region;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Promotions (discount codes with a validity period, optionally limited to a product category and to a region): creating
 * them, finding the promotions active on a date or the best one, and how promotions were used on the lines of
 * non-cancelled sales orders (usage per promotion, a customer's promotion lines, unused promotions).
 */
public final class PromotionStore {
    /** Every promotion query selects from {@code promotion p}. */
    private static final String SELECT_PROMOTION =
            "SELECT p.id, p.code, p.discount_percent, p.start_date, p.end_date, p.category FROM promotion p ";
    /** The promotion is active on the date; binds the date twice. */
    private static final String ACTIVE_ON = "p.start_date <= ? AND p.end_date >= ?";

    /**
     * The order lines that got a promotion ({@code o}, {@code l}, {@code pr}, {@code p} = product); the lines of
     * non-cancelled orders dated in the period (inclusive) follow in {@link #ORDER_IN_PERIOD}.
     */
    private static final String PROMOTION_LINES = "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
            + "JOIN promotion pr ON pr.code = l.promotion_code JOIN product p ON p.id = l.product_id ";
    /** Non-cancelled order dated in the period (inclusive); binds from, to. */
    private static final String ORDER_IN_PERIOD = "o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED'";
    /**
     * The discount given on a line: list price less the price charged, derived from the promotion percentage; the
     * catalogue price for a 100% promotion. Not rounded.
     */
    private static final String LINE_DISCOUNT = "CASE WHEN pr.discount_percent < 100 "
            + "THEN l.quantity * l.unit_price * pr.discount_percent / (100 - pr.discount_percent) "
            + "ELSE l.quantity * p.unit_price END";

    private final Jdbc jdbc;

    PromotionStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public Promotion create(Promotion promotion) {
        long id = jdbc.insert("Cannot create promotion",
                "INSERT INTO promotion (code, discount_percent, start_date, end_date, category) VALUES (?, ?, ?, ?, ?)",
                promotion.code(), promotion.discountPercent(), promotion.startDate(), promotion.endDate(),
                promotion.category());
        return new Promotion(id, promotion.code(), promotion.discountPercent(), promotion.startDate(),
                promotion.endDate(), promotion.category());
    }

    public Optional<Promotion> findByCode(String code) {
        return jdbc.first("Cannot find promotion", SELECT_PROMOTION + "WHERE p.code = ?", Rows::promotion, code);
    }

    /** Limits the promotion to a region (replacing its previous region); a promotion without a region applies everywhere. */
    public void saveRegion(long promotionId, Region region) {
        jdbc.update("Cannot save promotion region",
                "MERGE INTO promotion_region (promotion_id, region) KEY (promotion_id) VALUES (?, ?)",
                promotionId, region.name());
    }

    /** The promotions active on the date, by code. */
    public List<Promotion> listActiveOn(LocalDate date) {
        return list("Cannot list active promotions", "WHERE " + ACTIVE_ON + " ORDER BY p.code, p.id", date, date);
    }

    /** The promotions active on the date that apply in the region (limited to it or to no region), by code. */
    public List<Promotion> listActiveOnInRegion(LocalDate date, Region region) {
        return list("Cannot list active promotions of region",
                "LEFT JOIN promotion_region r ON r.promotion_id = p.id "
                        + "WHERE " + ACTIVE_ON + " AND (r.region IS NULL OR r.region = ?) ORDER BY p.code, p.id",
                date, date, region.name());
    }

    /** The promotions of the category, by start date, then code. */
    public List<Promotion> listByCategory(String category) {
        return list("Cannot list promotions of category", "WHERE p.category = ? ORDER BY p.start_date, p.code, p.id", category);
    }

    /** The promotion with the highest discount active on the date for the category (or for any category), lowest code on a tie. */
    public Optional<Promotion> findBest(String category, LocalDate date) {
        return jdbc.first("Cannot find best promotion", SELECT_PROMOTION
                        + "WHERE " + ACTIVE_ON + " AND (p.category IS NULL OR p.category = ?) "
                        + "ORDER BY p.discount_percent DESC, p.code, p.id LIMIT 1",
                Rows::promotion, date, date, category);
    }

    /**
     * The promotions whose period overlaps the given period and that were applied to no line of a non-cancelled order dated
     * in the period. Sorted by code.
     */
    public List<Promotion> listUnused(LocalDate from, LocalDate to) {
        return list("Cannot list unused promotions",
                "WHERE p.start_date <= ? AND p.end_date >= ? AND NOT EXISTS (SELECT 1 FROM sales_order o "
                        + "JOIN sales_order_line l ON l.order_id = o.id WHERE l.promotion_code = p.code AND "
                        + ORDER_IN_PERIOD + ") ORDER BY p.code, p.id",
                to, from, from, to);
    }

    /**
     * Promotion usage: per promotion, the number of lines of non-cancelled orders dated in the period it was applied to,
     * the discount given (list price less the price charged, derived from the promotion percentage; the catalogue price for
     * a 100% promotion) and the revenue of those lines. Sorted by code; promotions without such lines are left out.
     */
    public List<PromotionUsage> usagePerPromotion(LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find promotion usage",
                "SELECT pr.code AS code, COUNT(*) AS line_count, "
                        + "COALESCE(ROUND(SUM(" + LINE_DISCOUNT + "), 2), 0) AS discount, "
                        + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                        + PROMOTION_LINES + "WHERE " + ORDER_IN_PERIOD + " GROUP BY pr.code ORDER BY pr.code",
                rs -> new PromotionUsage(rs.getString("code"), rs.getInt("line_count"), rs.getBigDecimal("discount"),
                        rs.getBigDecimal("revenue")),
                from, to);
    }

    /**
     * The promotion lines of a customer: the lines of non-cancelled orders of the customer dated in the period (inclusive)
     * that got a promotion, with the discount given on each (list price less the price charged, derived from the promotion
     * percentage; the catalogue price for a 100% promotion). Sorted by order date, order number and line number.
     */
    public List<CustomerPromotionLine> listLinesOfCustomer(long customerId, LocalDate from, LocalDate to) {
        return jdbc.list("Cannot find customer promotion lines",
                "SELECT o.number AS number, o.order_date AS order_date, l.line_no AS line_no, p.sku AS sku, "
                        + "l.quantity AS quantity, l.unit_price AS unit_price, pr.code AS code, "
                        + "ROUND(" + LINE_DISCOUNT + ", 2) AS discount "
                        + PROMOTION_LINES + "WHERE o.customer_id = ? AND " + ORDER_IN_PERIOD + " "
                        + "ORDER BY o.order_date, o.number, o.id, l.line_no",
                rs -> new CustomerPromotionLine(rs.getString("number"), rs.getObject("order_date", LocalDate.class),
                        rs.getInt("line_no"), rs.getString("sku"), rs.getInt("quantity"), rs.getBigDecimal("unit_price"),
                        rs.getString("code"), rs.getBigDecimal("discount")),
                customerId, from, to);
    }

    private List<Promotion> list(String error, String joinsWhereAndOrder, Object... params) {
        return jdbc.list(error, SELECT_PROMOTION + joinsWhereAndOrder, Rows::promotion, params);
    }
}
