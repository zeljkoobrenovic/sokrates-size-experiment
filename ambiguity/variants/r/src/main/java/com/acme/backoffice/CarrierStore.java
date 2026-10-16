package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CarrierLeadTime;
import com.acme.backoffice.model.CarrierOrders;
import com.acme.backoffice.model.CarrierPerformance;
import com.acme.backoffice.model.ServiceLevel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Carriers (the transport companies that ship orders, with their service level, price per kg and promised delivery
 * days): creating and finding them, and the per-carrier reports: shipment performance (count, average days to deliver,
 * late deliveries), orders shipped and lead time from order to delivery.
 */
public final class CarrierStore {
    /** The columns {@link Rows#carrier} reads, with the carrier table as {@code c}; also used as GROUP BY list. */
    static final String CARRIER_COLUMNS = "c.id, c.name, c.service_level, c.price_per_kg, c.promised_days";
    private static final String SELECT = "SELECT " + CARRIER_COLUMNS + " FROM carrier c ";
    private static final String BY_NAME = " ORDER BY c.name, c.id";

    private final Jdbc jdbc;

    CarrierStore(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    /** Creates the carrier; returns it with its id. */
    public Carrier create(Carrier carrier) {
        long id = jdbc.insert("Cannot create carrier",
                "INSERT INTO carrier (name, service_level, price_per_kg, promised_days) VALUES (?, ?, ?, ?)",
                carrier.name(), carrier.serviceLevel(), carrier.pricePerKg(), carrier.promisedDays());
        return new Carrier(id, carrier.name(), carrier.serviceLevel(), carrier.pricePerKg(), carrier.promisedDays());
    }

    /** All carriers, ordered by name. */
    public List<Carrier> listAllByName() {
        return jdbc.list("Cannot list carriers", SELECT + BY_NAME, Rows::carrier);
    }

    /** The carriers of the service level, ordered by name. */
    public List<Carrier> listByServiceLevel(ServiceLevel serviceLevel) {
        return jdbc.list("Cannot list carriers by service level", SELECT + "WHERE c.service_level = ?" + BY_NAME,
                Rows::carrier, serviceLevel);
    }

    /** The carrier with the name, if any. */
    public Optional<Carrier> findByName(String name) {
        return jdbc.first("Cannot find carrier", SELECT + "WHERE c.name = ?", Rows::carrier, name);
    }

    /** The carrier with the id, if any. */
    public Optional<Carrier> findById(long id) {
        return jdbc.first("Cannot find carrier", SELECT + "WHERE c.id = ?", Rows::carrier, id);
    }

    /**
     * Per carrier, the shipments shipped between the two dates, inclusive: their number, the average days from shipping to
     * delivery of the delivered ones (null if none is delivered) and the number delivered later than the promised days of
     * the carrier. Sorted by carrier name; carriers without shipments in the period are left out.
     */
    public List<CarrierPerformance> performancePerCarrier(LocalDate from, LocalDate to) {
        String sql = "SELECT " + CARRIER_COLUMNS + ", COUNT(s.id) AS shipment_count, "
                + "AVG(CAST(DATEDIFF('DAY', s.ship_date, s.delivery_date) AS DECIMAL(10, 2))) AS average_days, "
                + "COALESCE(SUM(CASE WHEN " + ShipmentStore.LATE + " THEN 1 ELSE 0 END), 0) AS late_count "
                + "FROM carrier c JOIN shipment s ON s.carrier_id = c.id "
                + "WHERE s.ship_date >= ? AND s.ship_date <= ? "
                + "GROUP BY " + CARRIER_COLUMNS + BY_NAME;
        return jdbc.list("Cannot list carrier performance", sql, rs -> {
            BigDecimal averageDays = rs.getBigDecimal("average_days");
            return new CarrierPerformance(Rows.carrier(rs), rs.getInt("shipment_count"),
                    averageDays == null ? null : averageDays.setScale(2, RoundingMode.HALF_UP), rs.getInt("late_count"));
        }, from, to);
    }

    /**
     * Per carrier, the number and total value of the orders shipped with it between the two dates, inclusive. An order
     * counts for a carrier if the carrier shipped it whole or any part of it in the period (an order split over several
     * carriers counts for each of them, with its full value). Sorted by carrier name; carriers without orders in the
     * period are left out.
     */
    public List<CarrierOrders> ordersPerCarrier(LocalDate from, LocalDate to) {
        String sql = "SELECT " + CARRIER_COLUMNS + ", "
                + "COUNT(DISTINCT o.id) AS order_count, COALESCE(SUM(l.quantity * l.unit_price), 0) AS total "
                + "FROM carrier c JOIN sales_order o ON (EXISTS (SELECT 1 FROM shipment s WHERE s.order_id = o.id "
                + "AND s.carrier_id = c.id AND s.ship_date >= ? AND s.ship_date <= ?) "
                + "OR EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = o.id AND sp.carrier_id = c.id "
                + "AND sp.ship_date >= ? AND sp.ship_date <= ?)) "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "GROUP BY " + CARRIER_COLUMNS + BY_NAME;
        return jdbc.list("Cannot list carrier orders", sql,
                rs -> new CarrierOrders(Rows.carrier(rs), rs.getInt("order_count"), rs.getBigDecimal("total")),
                from, to, from, to);
    }

    /**
     * Per carrier, the number of delivered orders placed between the two dates, inclusive, that it shipped and the average
     * days from the order date to the delivery date of the shipment. Orders not yet delivered are left out. Sorted by
     * carrier name; carriers without delivered orders in the period are left out.
     */
    public List<CarrierLeadTime> leadTimePerCarrier(LocalDate from, LocalDate to) {
        String sql = ShipmentStore.leadTimeQuery(CARRIER_COLUMNS, "JOIN carrier c ON c.id = s.carrier_id ",
                "GROUP BY " + CARRIER_COLUMNS + BY_NAME);
        return jdbc.list("Cannot list lead time by carrier", sql,
                rs -> new CarrierLeadTime(Rows.carrier(rs), rs.getInt("order_count"),
                        rs.getBigDecimal("average_days").setScale(2, RoundingMode.HALF_UP)), from, to);
    }
}
