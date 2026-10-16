package com.acme.backoffice;

import com.acme.backoffice.model.AddressType;
import com.acme.backoffice.model.AuditEntityType;
import com.acme.backoffice.model.AuditEntry;
import com.acme.backoffice.model.BackOrder;
import com.acme.backoffice.model.ArchivedYearRevenue;
import com.acme.backoffice.model.CategoryBackOrders;
import com.acme.backoffice.model.BundleComponent;
import com.acme.backoffice.model.CancellationCount;
import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.CancelledOrder;
import com.acme.backoffice.model.DraftOrder;
import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CarrierLeadTime;
import com.acme.backoffice.model.CarrierOrders;
import com.acme.backoffice.model.CarrierPerformance;
import com.acme.backoffice.model.CarrierShippedWeight;
import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.CategoryMargin;
import com.acme.backoffice.model.CategorySpend;
import com.acme.backoffice.model.CategorySupplier;
import com.acme.backoffice.model.CategoryMonthRevenue;
import com.acme.backoffice.model.CategoryStock;
import com.acme.backoffice.model.CategoryWarehouseStock;
import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.CustomerOpenCredit;
import com.acme.backoffice.model.CustomerRegionSales;
import com.acme.backoffice.model.CustomerOverLimit;
import com.acme.backoffice.model.RegionOverLimit;
import com.acme.backoffice.model.RepOverLimit;
import com.acme.backoffice.model.RegionOpenCredit;
import com.acme.backoffice.model.Currency;
import com.acme.backoffice.model.CurrencyOpenInvoice;
import com.acme.backoffice.model.CommissionBasis;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.CustomerActivity;
import com.acme.backoffice.model.CustomerPrice;
import com.acme.backoffice.model.CustomerSales;
import com.acme.backoffice.model.CustomerAddress;
import com.acme.backoffice.model.CustomerContact;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.DailyPaymentTotal;
import com.acme.backoffice.model.MonthlyPaymentTotal;
import com.acme.backoffice.model.UnappliedPayment;
import com.acme.backoffice.model.ExchangeRate;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceAging;
import com.acme.backoffice.model.InvoiceCurrency;
import com.acme.backoffice.model.TaxRate;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.InvoiceStatusTotal;
import com.acme.backoffice.model.RegionLargeInvoice;
import com.acme.backoffice.model.LargeOrder;
import com.acme.backoffice.model.LatePaidInvoice;
import com.acme.backoffice.model.LowStockAlert;
import com.acme.backoffice.model.PartiallyPaidInvoice;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.RegionLeadTime;
import com.acme.backoffice.model.RegionSmallOrderShare;
import com.acme.backoffice.model.SalesChannel;
import com.acme.backoffice.model.SmallOrder;
import com.acme.backoffice.model.ChannelSales;
import com.acme.backoffice.model.MonthlySales;
import com.acme.backoffice.model.OrderHistoryEntry;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderLineMatch;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.OrderApproval;
import com.acme.backoffice.model.OrderNote;
import com.acme.backoffice.model.CustomerReturn;
import com.acme.backoffice.model.ProductReturnedQuantity;
import com.acme.backoffice.model.WarehouseReturn;
import com.acme.backoffice.model.OrderReturns;
import com.acme.backoffice.model.OrderStatusChange;
import com.acme.backoffice.model.PaymentReceived;
import com.acme.backoffice.model.PriceChange;
import com.acme.backoffice.model.PriceListUsage;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductOrder;
import com.acme.backoffice.model.ProductMargin;
import com.acme.backoffice.model.ProductReturns;
import com.acme.backoffice.model.ProductSales;
import com.acme.backoffice.model.CustomerPromotionLine;
import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.model.PromotionUsage;
import com.acme.backoffice.model.LatePurchaseOrder;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.PurchaseSuggestion;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionContact;
import com.acme.backoffice.model.ReasonCredit;
import com.acme.backoffice.model.ReasonReturns;
import com.acme.backoffice.model.QuarterCustomers;
import com.acme.backoffice.model.QuarterInvoices;
import com.acme.backoffice.model.QuarterOrders;
import com.acme.backoffice.model.QuarterPurchaseOrders;
import com.acme.backoffice.model.QuarterReturns;
import com.acme.backoffice.model.QuarterShipments;
import com.acme.backoffice.model.WeekdayCustomers;
import com.acme.backoffice.model.WeekdayInvoices;
import com.acme.backoffice.model.WeekdayOrders;
import com.acme.backoffice.model.WeekdayPurchaseOrders;
import com.acme.backoffice.model.WeekdayReturns;
import com.acme.backoffice.model.WeekdayShipments;
import com.acme.backoffice.model.RegionCustomers;
import com.acme.backoffice.model.RegionReasonReturns;
import com.acme.backoffice.model.RegionReport;
import com.acme.backoffice.model.RegionSales;
import com.acme.backoffice.model.FirstTimeCustomer;
import com.acme.backoffice.model.RepeatCustomer;
import com.acme.backoffice.model.YearOverYearSales;
import com.acme.backoffice.model.RemainingLine;
import com.acme.backoffice.model.RepOverdueInvoices;
import com.acme.backoffice.model.RepPayments;
import com.acme.backoffice.model.RepPerformance;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesRepCustomer;
import com.acme.backoffice.model.SalesRepCustomerCount;
import com.acme.backoffice.model.SalesRepOrderCount;
import com.acme.backoffice.model.SalesRepInvoiceCount;
import com.acme.backoffice.model.SalesRepShipmentCount;
import com.acme.backoffice.model.SalesRepReturnCount;
import com.acme.backoffice.model.SalesRepPurchaseOrderCount;
import com.acme.backoffice.model.CountryShipmentCount;
import com.acme.backoffice.model.CountryReturnCount;
import com.acme.backoffice.model.CountryPurchaseOrderCount;
import com.acme.backoffice.model.CountryCustomerCount;
import com.acme.backoffice.model.CountryOrderCount;
import com.acme.backoffice.model.CountryInvoiceCount;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.SalesTargetProgress;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.ShipmentStatus;
import com.acme.backoffice.model.SlowMovingStock;
import com.acme.backoffice.model.StockAdjustment;
import com.acme.backoffice.model.StockLevel;
import com.acme.backoffice.model.ReasonMovements;
import com.acme.backoffice.model.WarehouseAdjustments;
import com.acme.backoffice.model.WarehouseMovements;
import com.acme.backoffice.model.StockMovement;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupplierPerformance;
import com.acme.backoffice.model.SupplierPurchasedQuantity;
import com.acme.backoffice.model.SupplierSpending;
import com.acme.backoffice.model.CountrySpending;
import com.acme.backoffice.model.SupplierProduct;
import com.acme.backoffice.model.SupplierWarehouseStock;
import com.acme.backoffice.model.ClosedTicket;
import com.acme.backoffice.model.OrderTickets;
import com.acme.backoffice.model.PriorityTickets;
import com.acme.backoffice.model.ProductTickets;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketCloseTime;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.model.TicketStatusCount;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseCategory;
import com.acme.backoffice.model.WarehouseSlowMovingStock;
import com.acme.backoffice.model.WarehouseDeliveryCount;
import com.acme.backoffice.model.WarehouseShippedQuantity;
import com.acme.backoffice.model.WarehouseShippedWeight;
import com.acme.backoffice.model.WarehouseStockValue;
import com.acme.backoffice.model.TransferRoute;
import com.acme.backoffice.model.WarehouseTransfer;
import com.acme.backoffice.model.WarehouseTransferLine;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class DataStorePart03 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart03(DataStore store, java.sql.Connection connection) {
        this.store = store;
        this.connection = connection;
    }

    /** Purchase orders with a line of the product ordered between the two dates, inclusive, oldest first. */
    public List<PurchaseOrder> listPurchaseOrdersByProductAndPeriod(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order o "
                + "WHERE EXISTS (SELECT 1 FROM purchase_order_line l WHERE l.order_id = o.id AND l.product_id = ?) "
                + "AND o.order_date >= ? AND o.order_date <= ? ORDER BY o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<PurchaseOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<PurchaseOrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new PurchaseOrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_cost")));
                            }
                        }
                    }
                    orders.add(new PurchaseOrder(id, rs.getString("number"), rs.getLong("supplier_id"),
                            rs.getObject("order_date", LocalDate.class), rs.getObject("expected_date", LocalDate.class),
                            PurchaseOrderStatus.valueOf(rs.getString("status")), lines, rs.getLong("warehouse_id")));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list purchase orders of product", e);
        }
    }

    /**
     * The quantity of the product on purchase orders ordered between the two dates, inclusive, per supplier. Sorted by
     * quantity, highest first (then by supplier name, then id); suppliers without such orders are left out.
     */
    public List<SupplierPurchasedQuantity> findPurchasedQuantityPerSupplier(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.name, s.contact_email, s.country, s.rating, SUM(l.quantity) AS purchased "
                + "FROM supplier s JOIN purchase_order o ON o.supplier_id = s.id "
                + "JOIN purchase_order_line l ON l.order_id = o.id "
                + "WHERE l.product_id = ? AND o.order_date >= ? AND o.order_date <= ? "
                + "GROUP BY s.id, s.name, s.contact_email, s.country, s.rating ORDER BY purchased DESC, s.name, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupplierPurchasedQuantity> quantities = new ArrayList<>();
                while (rs.next()) {
                    Supplier supplier = new Supplier(rs.getLong("id"), rs.getString("name"), rs.getString("contact_email"),
                            rs.getString("country"), rs.getInt("rating"));
                    quantities.add(new SupplierPurchasedQuantity(supplier, rs.getLong("purchased")));
                }
                return quantities;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find purchased quantity per supplier", e);
        }
    }

    /**
     * The non-cancelled purchase orders with a line for a product of the category or of any category below it, ordered
     * between the two dates, inclusive, oldest first.
     */
    public List<PurchaseOrder> listPurchaseOrdersByCategoryAndPeriod(String category, LocalDate from, LocalDate to) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order o "
                + "WHERE o.status <> 'CANCELLED' AND o.order_date >= ? AND o.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM purchase_order_line l JOIN product p ON p.id = l.product_id "
                + "WHERE l.order_id = o.id AND p.category IN (SELECT code FROM tree)) ORDER BY o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<PurchaseOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<PurchaseOrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new PurchaseOrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_cost")));
                            }
                        }
                    }
                    orders.add(new PurchaseOrder(id, rs.getString("number"), rs.getLong("supplier_id"),
                            rs.getObject("order_date", LocalDate.class), rs.getObject("expected_date", LocalDate.class),
                            PurchaseOrderStatus.valueOf(rs.getString("status")), lines, rs.getLong("warehouse_id")));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list purchase orders of category", e);
        }
    }

    /**
     * The spend (quantity times unit cost) of the non-cancelled purchase orders ordered between the two dates,
     * inclusive, on products of the category and of all categories below it, per category of the product. Sorted by
     * spend, highest first (then by category); categories without such spend are left out.
     */
    public List<CategorySpend> findPurchaseSpendPerCategory(String category, LocalDate from, LocalDate to) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT p.category AS category, SUM(l.quantity * l.unit_cost) AS spend "
                + "FROM purchase_order o JOIN purchase_order_line l ON l.order_id = o.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE o.status <> 'CANCELLED' AND o.order_date >= ? AND o.order_date <= ? "
                + "AND p.category IN (SELECT code FROM tree) "
                + "GROUP BY p.category ORDER BY spend DESC, p.category";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CategorySpend> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new CategorySpend(rs.getString("category"), rs.getBigDecimal("spend")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find purchase spend per category", e);
        }
    }

    /**
     * Adds the quantities of a sent purchase order to the stock of the warehouse and marks it as received on the date, all or nothing;
     * returns false, changing nothing, if the purchase order is not sent.
     */
    public boolean receivePurchaseOrder(long orderId, long warehouseId, LocalDate receivedDate) {
        String orderSql = "UPDATE purchase_order SET status = 'RECEIVED', received_date = ?, received_warehouse_id = ? "
                + "WHERE id = ? AND status = 'SENT'";
        String linesSql = "SELECT product_id, quantity FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        String updateSql = "UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)";
        String movementSql = "INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                + "VALUES (?, ?, ?, ?, 'RECEIPT')";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(orderSql)) {
                statement.setObject(1, receivedDate);
                statement.setLong(2, warehouseId);
                statement.setLong(3, orderId);
                if (statement.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
                try (PreparedStatement linesStatement = connection.prepareStatement(linesSql)) {
                    linesStatement.setLong(1, orderId);
                    try (ResultSet rs = linesStatement.executeQuery()) {
                        while (rs.next()) {
                            long productId = rs.getLong("product_id");
                            int quantity = rs.getInt("quantity");
                            try (PreparedStatement update = connection.prepareStatement(updateSql)) {
                                update.setInt(1, quantity);
                                update.setLong(2, warehouseId);
                                update.setLong(3, productId);
                                if (update.executeUpdate() == 0) {
                                    try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
                                        insert.setLong(1, warehouseId);
                                        insert.setLong(2, productId);
                                        insert.setInt(3, quantity);
                                        insert.executeUpdate();
                                    }
                                }
                            }
                            try (PreparedStatement movement = connection.prepareStatement(movementSql)) {
                                movement.setLong(1, productId);
                                movement.setLong(2, warehouseId);
                                movement.setObject(3, receivedDate);
                                movement.setInt(4, quantity);
                                movement.executeUpdate();
                            }
                        }
                    }
                }
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException("Cannot receive purchase order", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    public Optional<LocalDate> findPurchaseOrderReceivedDate(long orderId) {
        String sql = "SELECT received_date FROM purchase_order WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.ofNullable(rs.getObject("received_date", LocalDate.class));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find purchase order received date", e);
        }
    }

    /**
     * The purchase orders received into the warehouse between the two dates, inclusive, after their expected date, with the
     * days late, ordered by receiving date (then by id).
     */
    public List<LatePurchaseOrder> listLatePurchaseOrdersByWarehouseAndPeriod(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id, received_date, "
                + "DATEDIFF('DAY', expected_date, received_date) AS days_late FROM purchase_order "
                + "WHERE status = 'RECEIVED' AND received_warehouse_id = ? AND received_date >= ? AND received_date <= ? "
                + "AND received_date > expected_date ORDER BY received_date, id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<LatePurchaseOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<PurchaseOrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new PurchaseOrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_cost")));
                            }
                        }
                    }
                    PurchaseOrder order = new PurchaseOrder(id, rs.getString("number"), rs.getLong("supplier_id"),
                            rs.getObject("order_date", LocalDate.class), rs.getObject("expected_date", LocalDate.class),
                            PurchaseOrderStatus.valueOf(rs.getString("status")), lines, rs.getLong("warehouse_id"));
                    orders.add(new LatePurchaseOrder(order, rs.getObject("received_date", LocalDate.class), rs.getInt("days_late")));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list late purchase orders by warehouse", e);
        }
    }

    public List<SalesOrder> listOpenSalesOrders() {
        String sql = "SELECT id, number, customer_id, order_date, status FROM sales_order WHERE status = 'CONFIRMED' "
                + "AND NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = sales_order.id) "
                + "ORDER BY order_date, id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    orders.add(new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list open sales orders", e);
        }
    }

    /** Number of confirmed, not archived orders placed on or before the date. */
    public int countOpenSalesOrdersOnDate(LocalDate date) {
        String sql = "SELECT COUNT(*) AS order_count FROM sales_order WHERE status = 'CONFIRMED' AND order_date <= ? "
                + "AND NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = sales_order.id)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt("order_count");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot count open sales orders", e);
        }
    }

    /** Sum of quantity times unit price over the lines of the orders counted by countOpenSalesOrdersOnDate. */
    public BigDecimal findOpenSalesOrderValueOnDate(LocalDate date) {
        String sql = "SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) AS total FROM sales_order o "
                + "JOIN sales_order_line l ON l.order_id = o.id WHERE o.status = 'CONFIRMED' AND o.order_date <= ? "
                + "AND NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = o.id)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find open sales order value", e);
        }
    }

    public List<SalesOrder> listOpenSalesOrdersByCustomer(long customerId) {
        String sql = "SELECT id, number, customer_id, order_date, status FROM sales_order "
                + "WHERE status = 'CONFIRMED' AND customer_id = ? "
                + "AND NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = sales_order.id) "
                + "ORDER BY order_date, id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    orders.add(new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list open sales orders", e);
        }
    }

    public List<SalesOrder> listOpenSalesOrdersByRegion(Region region) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "JOIN customer c ON c.id = o.customer_id WHERE o.status = 'CONFIRMED' AND c.region = ? "
                + "AND NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = o.id) "
                + "ORDER BY o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    orders.add(new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list open sales orders", e);
        }
    }

    /** Orders placed between the two dates, inclusive, sorted by date. */
    public List<SalesOrder> listSalesOrdersByPeriod(LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, customer_id, order_date, status FROM sales_order "
                + "WHERE order_date >= ? AND order_date <= ? ORDER BY order_date, id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    orders.add(new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list sales orders by period", e);
        }
    }

    /** The non-cancelled orders containing the product, placed between the two dates, inclusive, newest first. */
    public List<ProductOrder> listOrdersByProduct(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, "
                + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.status <> 'CANCELLED' AND o.order_date >= ? AND o.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM sales_order_line l WHERE l.order_id = o.id AND l.product_id = ?) "
                + "ORDER BY o.order_date DESC, o.id DESC";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setLong(3, productId);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductOrder> result = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    SalesOrder order = new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines);
                    Customer customer = new Customer(rs.getLong("customer_id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                            CustomerStatus.valueOf(rs.getString("customer_status")));
                    result.add(new ProductOrder(order, customer));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list orders by product", e);
        }
    }

    /**
     * The non-cancelled orders containing the product, placed between the two dates, inclusive.
     * Grouped per customer (customers by name, then id), newest first within a customer.
     */
    public List<ProductOrder> listOrdersByProductPerCustomer(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, "
                + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.status <> 'CANCELLED' AND o.order_date >= ? AND o.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM sales_order_line l WHERE l.order_id = o.id AND l.product_id = ?) "
                + "ORDER BY c.name, c.id, o.order_date DESC, o.id DESC";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setLong(3, productId);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductOrder> result = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    SalesOrder order = new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines);
                    Customer customer = new Customer(rs.getLong("customer_id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                            CustomerStatus.valueOf(rs.getString("customer_status")));
                    result.add(new ProductOrder(order, customer));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list orders by product per customer", e);
        }
    }

    /**
     * The non-cancelled orders containing a product of the category or of any category below it, placed between the
     * two dates, inclusive, newest first.
     */
    public List<ProductOrder> listOrdersByCategory(String category, LocalDate from, LocalDate to) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, "
                + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.status <> 'CANCELLED' AND o.order_date >= ? AND o.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM sales_order_line l JOIN product p ON p.id = l.product_id "
                + "WHERE l.order_id = o.id AND p.category IN (SELECT code FROM tree)) "
                + "ORDER BY o.order_date DESC, o.id DESC";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductOrder> result = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    SalesOrder order = new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines);
                    Customer customer = new Customer(rs.getLong("customer_id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                            CustomerStatus.valueOf(rs.getString("customer_status")));
                    result.add(new ProductOrder(order, customer));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list orders by category", e);
        }
    }

    /**
     * The revenue per product category and calendar month of the non-cancelled orders placed between the two dates,
     * inclusive (quantity times unit price of the lines, by the category of the product itself). Only combinations
     * with sales; by category, then month.
     */
    public List<CategoryMonthRevenue> findRevenuePerCategoryAndMonth(LocalDate from, LocalDate to) {
        String sql = "SELECT p.category AS category, YEAR(o.order_date) AS year_no, MONTH(o.order_date) AS month_no, "
                + "SUM(l.quantity * l.unit_price) AS revenue "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY p.category, YEAR(o.order_date), MONTH(o.order_date) "
                + "ORDER BY p.category, YEAR(o.order_date), MONTH(o.order_date)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CategoryMonthRevenue> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new CategoryMonthRevenue(rs.getString("category"),
                            YearMonth.of(rs.getInt("year_no"), rs.getInt("month_no")), rs.getBigDecimal("revenue")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find revenue per category and month", e);
        }
    }

    /** Sets (or replaces) the channel the order came from. */
    public void saveOrderChannel(long orderId, SalesChannel channel) {
        String sql = "MERGE INTO order_channel (order_id, channel) KEY (order_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            statement.setString(2, channel.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save order channel", e);
        }
    }

    /** Orders of the channel placed between the two dates, inclusive, sorted by date. */
    public List<SalesOrder> listSalesOrdersByChannel(SalesChannel channel, LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "JOIN order_channel oc ON oc.order_id = o.id "
                + "WHERE oc.channel = ? AND o.order_date >= ? AND o.order_date <= ? ORDER BY o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, channel.name());
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    orders.add(new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list sales orders by channel", e);
        }
    }

    /**
     * Orders matching all the given criteria, newest first. Every criterion is optional: null means no restriction.
     * The dates are inclusive.
     */
    public List<SalesOrder> searchSalesOrders(Long customerId, OrderStatus status, Region region, LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder("SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "JOIN customer c ON c.id = o.customer_id "
                + "WHERE NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = o.id)");
        List<Object> parameters = new ArrayList<>();
        if (customerId != null) {
            sql.append(" AND o.customer_id = ?");
            parameters.add(customerId);
        }
        if (status != null) {
            sql.append(" AND o.status = ?");
            parameters.add(status.name());
        }
        if (region != null) {
            sql.append(" AND c.region = ?");
            parameters.add(region.name());
        }
        if (from != null) {
            sql.append(" AND o.order_date >= ?");
            parameters.add(from);
        }
        if (to != null) {
            sql.append(" AND o.order_date <= ?");
            parameters.add(to);
        }
        sql.append(" ORDER BY o.order_date DESC, o.id DESC");
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    orders.add(new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot search sales orders", e);
        }
    }

    /** Orders with the given status placed between the two dates, inclusive, sorted by date. */
    public List<SalesOrder> listSalesOrdersByPeriodAndStatus(LocalDate from, LocalDate to, OrderStatus status) {
        String sql = "SELECT id, number, customer_id, order_date, status FROM sales_order "
                + "WHERE order_date >= ? AND order_date <= ? AND status = ? ORDER BY order_date, id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setString(3, status.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesOrder> orders = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    orders.add(new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list sales orders by period and status", e);
        }
    }

    public void changeSalesOrderStatus(long orderId, OrderStatus status, LocalDate date) {
        String sql = "UPDATE sales_order SET status = ? WHERE id = ?";
        String historySql = "INSERT INTO sales_order_status_change (order_id, status, change_date) VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, status.name());
                statement.setLong(2, orderId);
                statement.executeUpdate();
                try (PreparedStatement historyStatement = connection.prepareStatement(historySql)) {
                    historyStatement.setLong(1, orderId);
                    historyStatement.setString(2, status.name());
                    historyStatement.setObject(3, date);
                    historyStatement.executeUpdate();
                }
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException("Cannot change sales order status", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    /** Sets the order to CANCELLED, with a status history entry and the cancellation reason, in one transaction. */
    public void cancelSalesOrder(long orderId, CancellationReason reason, LocalDate date) {
        String sql = "UPDATE sales_order SET status = 'CANCELLED' WHERE id = ?";
        String historySql = "INSERT INTO sales_order_status_change (order_id, status, change_date) VALUES (?, 'CANCELLED', ?)";
        String reasonSql = "INSERT INTO order_cancellation (order_id, reason, cancel_date) VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, orderId);
                statement.executeUpdate();
                try (PreparedStatement historyStatement = connection.prepareStatement(historySql)) {
                    historyStatement.setLong(1, orderId);
                    historyStatement.setObject(2, date);
                    historyStatement.executeUpdate();
                }
                try (PreparedStatement reasonStatement = connection.prepareStatement(reasonSql)) {
                    reasonStatement.setLong(1, orderId);
                    reasonStatement.setString(2, reason.name());
                    reasonStatement.setObject(3, date);
                    reasonStatement.executeUpdate();
                }
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException("Cannot cancel sales order", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    /**
     * Number of orders cancelled between the two dates (inclusive), per reason and customer. Sorted by reason, then
     * customer name, then customer id; combinations without cancellations are left out.
     */
    public List<CancellationCount> findCancellationsPerReasonAndCustomer(LocalDate from, LocalDate to) {
        String sql = "SELECT x.reason AS reason, c.id AS id, c.name AS name, COUNT(*) AS order_count "
                + "FROM order_cancellation x JOIN sales_order o ON o.id = x.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE x.cancel_date >= ? AND x.cancel_date <= ? "
                + "GROUP BY x.reason, c.id, c.name ORDER BY x.reason, c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CancellationCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new CancellationCount(CancellationReason.valueOf(rs.getString("reason")), rs.getLong("id"),
                            rs.getString("name"), rs.getInt("order_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find cancellations per reason and customer", e);
        }
    }

    /**
     * The orders cancelled between the two dates, inclusive (by cancellation date), with the customer who placed each.
     * Grouped per customer (customers by name, then id), newest cancellation first within a customer.
     */
    public List<CancelledOrder> listCancelledOrdersByCustomer(LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, x.cancel_date, x.reason, "
                + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status "
                + "FROM order_cancellation x JOIN sales_order o ON o.id = x.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE x.cancel_date >= ? AND x.cancel_date <= ? "
                + "ORDER BY c.name, c.id, x.cancel_date DESC, o.id DESC";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CancelledOrder> result = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    SalesOrder order = new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines);
                    Customer customer = new Customer(rs.getLong("customer_id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                            CustomerStatus.valueOf(rs.getString("customer_status")));
                    result.add(new CancelledOrder(order, rs.getObject("cancel_date", LocalDate.class),
                            CancellationReason.valueOf(rs.getString("reason")), customer));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list cancelled orders by customer", e);
        }
    }

    /**
     * The orders cancelled between the two dates, inclusive (by cancellation date), with the customer who placed each.
     * Grouped per region of the customer (regions in alphabetical order), newest cancellation first within a region.
     */
    public List<CancelledOrder> listCancelledOrdersByRegion(LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, x.cancel_date, x.reason, "
                + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status "
                + "FROM order_cancellation x JOIN sales_order o ON o.id = x.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE x.cancel_date >= ? AND x.cancel_date <= ? "
                + "ORDER BY c.region, x.cancel_date DESC, o.id DESC";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CancelledOrder> result = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    SalesOrder order = new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines);
                    Customer customer = new Customer(rs.getLong("customer_id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                            CustomerStatus.valueOf(rs.getString("customer_status")));
                    result.add(new CancelledOrder(order, rs.getObject("cancel_date", LocalDate.class),
                            CancellationReason.valueOf(rs.getString("reason")), customer));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list cancelled orders by region", e);
        }
    }

    /**
     * The draft orders older than the given number of days on the date (order date before the date minus the days),
     * with the customer and the sales rep of the customer. Grouped per customer (customers by name, then id),
     * oldest order first within a customer.
     */
    public List<DraftOrder> listStaleDraftOrdersByCustomer(LocalDate asOf, int days) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, "
                + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status, "
                + "r.id AS rep_id, r.name AS rep_name, r.email AS rep_email, r.region AS rep_region "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id LEFT JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE o.status = 'DRAFT' AND o.order_date < ? "
                + "ORDER BY c.name, c.id, o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf.minusDays(days));
            try (ResultSet rs = statement.executeQuery()) {
                List<DraftOrder> result = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    LocalDate orderDate = rs.getObject("order_date", LocalDate.class);
                    SalesOrder order = new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            orderDate, OrderStatus.valueOf(rs.getString("status")), lines);
                    Customer customer = new Customer(rs.getLong("customer_id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                            CustomerStatus.valueOf(rs.getString("customer_status")));
                    SalesRep salesRep = null;
                    if (rs.getObject("rep_id") != null) {
                        salesRep = new SalesRep(rs.getLong("rep_id"), rs.getString("rep_name"), rs.getString("rep_email"),
                                Region.valueOf(rs.getString("rep_region")));
                    }
                    result.add(new DraftOrder(order, ChronoUnit.DAYS.between(orderDate, asOf), customer, salesRep));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list stale draft orders by customer", e);
        }
    }

    /**
     * The draft orders older than the given number of days on the date (order date before the date minus the days),
     * with the customer and the sales rep of the customer. Grouped per sales rep of the customer (reps by name,
     * then id; customers without a rep last), then by customer, oldest order first.
     */
    public List<DraftOrder> listStaleDraftOrdersBySalesRep(LocalDate asOf, int days) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, "
                + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status, "
                + "r.id AS rep_id, r.name AS rep_name, r.email AS rep_email, r.region AS rep_region "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id LEFT JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE o.status = 'DRAFT' AND o.order_date < ? "
                + "ORDER BY r.name NULLS LAST, r.id, c.name, c.id, o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf.minusDays(days));
            try (ResultSet rs = statement.executeQuery()) {
                List<DraftOrder> result = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<OrderLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new OrderLine(lineRs.getLong("product_id"), lineRs.getInt("quantity"),
                                        lineRs.getBigDecimal("unit_price"), lineRs.getString("promotion_code")));
                            }
                        }
                    }
                    LocalDate orderDate = rs.getObject("order_date", LocalDate.class);
                    SalesOrder order = new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                            orderDate, OrderStatus.valueOf(rs.getString("status")), lines);
                    Customer customer = new Customer(rs.getLong("customer_id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                            CustomerStatus.valueOf(rs.getString("customer_status")));
                    SalesRep salesRep = null;
                    if (rs.getObject("rep_id") != null) {
                        salesRep = new SalesRep(rs.getLong("rep_id"), rs.getString("rep_name"), rs.getString("rep_email"),
                                Region.valueOf(rs.getString("rep_region")));
                    }
                    result.add(new DraftOrder(order, ChronoUnit.DAYS.between(orderDate, asOf), customer, salesRep));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list stale draft orders by sales rep", e);
        }
    }

    /** Archives the delivered or cancelled orders whose last status change is before the cutoff date; returns how many. */
    public int archiveSalesOrders(LocalDate cutoff, LocalDate archiveDate) {
        String sql = "INSERT INTO order_archive (order_id, archive_date) SELECT o.id, ? FROM sales_order o "
                + "WHERE o.status IN ('DELIVERED', 'CANCELLED') "
                + "AND NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = o.id) "
                + "AND COALESCE((SELECT MAX(h.change_date) FROM sales_order_status_change h WHERE h.order_id = o.id "
                + "AND h.status = o.status), o.order_date) < ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, archiveDate);
            statement.setObject(2, cutoff);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot archive sales orders", e);
        }
    }
}
