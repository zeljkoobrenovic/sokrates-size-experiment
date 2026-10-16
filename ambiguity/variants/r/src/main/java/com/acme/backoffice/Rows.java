package com.acme.backoffice;

import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerPrice;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseTransfer;
import com.acme.backoffice.model.WarehouseTransferLine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * The row mappers of the entities that more than one store reads, with the column lists they expect. A query selects
 * the columns under exactly these names (alias them if needed) and passes the mapper to {@link Jdbc}.
 *
 * <p>The order-like entities ({@link SalesOrder}, {@link PurchaseOrder}, {@link SalesReturn},
 * {@link WarehouseTransfer}) load their lines with one extra query per row, in line order.
 */
final class Rows {
    private Rows() {
    }

    /** Columns: id, name, email, phone, country, region, status. */
    static final String CUSTOMER_COLUMNS = "c.id, c.name, c.email, c.phone, c.country, c.region, c.status";

    static Customer customer(ResultSet rs) throws SQLException {
        return new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"),
                rs.getString("country"), Region.valueOf(rs.getString("region")), CustomerStatus.valueOf(rs.getString("status")));
    }

    /** Columns: id, sku, name, category, unit_price, unit_of_measure, active. */
    static final String PRODUCT_COLUMNS = "p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active";

    static Product product(ResultSet rs) throws SQLException {
        return new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"), rs.getString("category"),
                rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"), rs.getBoolean("active"));
    }

    /** Columns: id, name, contact_email, country, rating. */
    static Supplier supplier(ResultSet rs) throws SQLException {
        return new Supplier(rs.getLong("id"), rs.getString("name"), rs.getString("contact_email"), rs.getString("country"),
                rs.getInt("rating"));
    }

    /** Columns: id, code, name, city, region. */
    static Warehouse warehouse(ResultSet rs) throws SQLException {
        return new Warehouse(rs.getLong("id"), rs.getString("code"), rs.getString("name"), rs.getString("city"),
                Region.valueOf(rs.getString("region")));
    }

    /** Columns: id, name, service_level, price_per_kg, promised_days. */
    static Carrier carrier(ResultSet rs) throws SQLException {
        return new Carrier(rs.getLong("id"), rs.getString("name"), ServiceLevel.valueOf(rs.getString("service_level")),
                rs.getBigDecimal("price_per_kg"), rs.getInt("promised_days"));
    }

    /** Columns: id, name, email, region. */
    static SalesRep salesRep(ResultSet rs) throws SQLException {
        return new SalesRep(rs.getLong("id"), rs.getString("name"), rs.getString("email"), Region.valueOf(rs.getString("region")));
    }

    /** Columns: id, number, order_id, customer_id, invoice_date, due_date, amount, status. */
    static final String INVOICE_COLUMNS = "i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status";

    static Invoice invoice(ResultSet rs) throws SQLException {
        return new Invoice(rs.getLong("id"), rs.getString("number"), rs.getLong("order_id"), rs.getLong("customer_id"),
                rs.getObject("invoice_date", LocalDate.class), rs.getObject("due_date", LocalDate.class),
                rs.getBigDecimal("amount"), InvoiceStatus.valueOf(rs.getString("status")));
    }

    /** Columns: id, order_id, warehouse_id, carrier_id, tracking_number, ship_date, delivery_date. */
    static final String SHIPMENT_COLUMNS = "s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date";

    static Shipment shipment(ResultSet rs) throws SQLException {
        return new Shipment(rs.getLong("id"), rs.getLong("order_id"), rs.getLong("warehouse_id"), rs.getLong("carrier_id"),
                rs.getString("tracking_number"), rs.getObject("ship_date", LocalDate.class),
                rs.getObject("delivery_date", LocalDate.class));
    }

    /** Columns: id, number, customer_id, return_id, credit_date, amount. */
    static CreditNote creditNote(ResultSet rs) throws SQLException {
        return new CreditNote(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"), rs.getLong("return_id"),
                rs.getObject("credit_date", LocalDate.class), rs.getBigDecimal("amount"));
    }

    /** Columns: id, number, customer_id, order_id, subject, priority, status, opened_date, closed_date. */
    static SupportTicket supportTicket(ResultSet rs) throws SQLException {
        return new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                rs.getObject("order_id", Long.class), rs.getString("subject"), TicketPriority.valueOf(rs.getString("priority")),
                TicketStatus.valueOf(rs.getString("status")), rs.getObject("opened_date", LocalDate.class),
                rs.getObject("closed_date", LocalDate.class));
    }

    /** Columns: id, customer_id, product_id, unit_price, start_date, end_date. */
    static CustomerPrice customerPrice(ResultSet rs) throws SQLException {
        return new CustomerPrice(rs.getLong("id"), rs.getLong("customer_id"), rs.getLong("product_id"),
                rs.getBigDecimal("unit_price"), rs.getObject("start_date", LocalDate.class),
                rs.getObject("end_date", LocalDate.class));
    }

    /** Columns: id, code, discount_percent, start_date, end_date, category. */
    static Promotion promotion(ResultSet rs) throws SQLException {
        return new Promotion(rs.getLong("id"), rs.getString("code"), rs.getBigDecimal("discount_percent"),
                rs.getObject("start_date", LocalDate.class), rs.getObject("end_date", LocalDate.class),
                rs.getString("category"));
    }

    /** Columns: id, number, customer_id, order_date, status; the lines are loaded per order. */
    static final String SALES_ORDER_COLUMNS = "o.id, o.number, o.customer_id, o.order_date, o.status";

    static Jdbc.RowMapper<SalesOrder> salesOrder(Jdbc jdbc) {
        return rs -> {
            long id = rs.getLong("id");
            return new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                    rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")),
                    jdbc.listRaw("SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line "
                                    + "WHERE order_id = ? ORDER BY line_no",
                            line -> new OrderLine(line.getLong("product_id"), line.getInt("quantity"),
                                    line.getBigDecimal("unit_price"), line.getString("promotion_code")), id));
        };
    }

    /**
     * Columns: id, number, supplier_id, order_date, expected_date, status, warehouse_id (0 when it has none); the lines
     * are loaded per order.
     */
    static final String PURCHASE_ORDER_COLUMNS =
            "po.id, po.number, po.supplier_id, po.order_date, po.expected_date, po.status, po.warehouse_id";

    static Jdbc.RowMapper<PurchaseOrder> purchaseOrder(Jdbc jdbc) {
        return rs -> {
            long id = rs.getLong("id");
            return new PurchaseOrder(id, rs.getString("number"), rs.getLong("supplier_id"),
                    rs.getObject("order_date", LocalDate.class), rs.getObject("expected_date", LocalDate.class),
                    PurchaseOrderStatus.valueOf(rs.getString("status")),
                    jdbc.listRaw("SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no",
                            line -> new PurchaseOrderLine(line.getLong("product_id"), line.getInt("quantity"),
                                    line.getBigDecimal("unit_cost")), id),
                    rs.getLong("warehouse_id"));
        };
    }

    /** Columns: id, number, order_id, return_date, reason; the lines are loaded per return. */
    static final String SALES_RETURN_COLUMNS = "r.id, r.number, r.order_id, r.return_date, r.reason";

    static Jdbc.RowMapper<SalesReturn> salesReturn(Jdbc jdbc) {
        return rs -> {
            long id = rs.getLong("id");
            return new SalesReturn(id, rs.getString("number"), rs.getLong("order_id"),
                    rs.getObject("return_date", LocalDate.class), rs.getString("reason"),
                    jdbc.listRaw("SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no",
                            line -> new ReturnLine(line.getLong("product_id"), line.getInt("quantity")), id));
        };
    }

    /** Columns: id, number, from_warehouse_id, to_warehouse_id, transfer_date; the lines are loaded per transfer. */
    static Jdbc.RowMapper<WarehouseTransfer> warehouseTransfer(Jdbc jdbc) {
        return rs -> {
            long id = rs.getLong("id");
            return new WarehouseTransfer(id, rs.getString("number"), rs.getLong("from_warehouse_id"),
                    rs.getLong("to_warehouse_id"), rs.getObject("transfer_date", LocalDate.class),
                    jdbc.listRaw("SELECT product_id, quantity FROM warehouse_transfer_line WHERE transfer_id = ? ORDER BY line_no",
                            line -> new WarehouseTransferLine(line.getLong("product_id"), line.getInt("quantity")), id));
        };
    }
}
