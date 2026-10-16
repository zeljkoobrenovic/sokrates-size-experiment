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

class DataStorePart07 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart07(DataStore store, java.sql.Connection connection) {
        this.store = store;
        this.connection = connection;
    }

    /**
     * The N most recently placed orders of customers in the region (cancelled orders are left out), newest first, then
     * by highest id.
     */
    public List<SalesOrder> findLatestOrdersByRegion(Region region, int limit) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "JOIN customer c ON c.id = o.customer_id WHERE c.region = ? AND o.status <> 'CANCELLED' "
                + "ORDER BY o.order_date DESC, o.id DESC LIMIT ?";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest orders by region", e);
        }
    }

    /**
     * The N most recently placed orders of the customer (cancelled orders are left out), newest first, then by highest
     * id.
     */
    public List<SalesOrder> findLatestOrdersByCustomer(long customerId, int limit) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "WHERE o.customer_id = ? AND o.status <> 'CANCELLED' "
                + "ORDER BY o.order_date DESC, o.id DESC LIMIT ?";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setInt(2, limit);
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
                    orders.add(new SalesOrder(id, rs.getString("number"),rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find latest orders by customer", e);
        }
    }

    /**
     * The number of customers created between the two dates, inclusive, per sales rep (customers without a created date
     * or without a sales rep are not counted). Sorted by customer count, highest first, then by rep name, then id; reps
     * without customers in the period are included with 0.
     */
    public List<SalesRepCustomerCount> findCustomerCountPerSalesRep(LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.name AS name, "
                + "(SELECT COUNT(*) FROM customer c WHERE c.sales_rep_id = r.id "
                + "AND c.created_date >= ? AND c.created_date <= ?) AS customer_count "
                + "FROM sales_rep r ORDER BY customer_count DESC, r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesRepCustomerCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new SalesRepCustomerCount(rs.getLong("id"), rs.getString("name"), rs.getInt("customer_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer count per sales rep", e);
        }
    }

    /**
     * The number of orders placed between the two dates, inclusive, by the customers of each sales rep (cancelled
     * orders are not counted), highest first, then by name and id; reps without orders are included with zero.
     */
    public List<SalesRepOrderCount> findOrderCountPerSalesRep(LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.name AS name, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE c.sales_rep_id = r.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') AS order_count "
                + "FROM sales_rep r ORDER BY order_count DESC, r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesRepOrderCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new SalesRepOrderCount(rs.getLong("id"), rs.getString("name"), rs.getInt("order_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find order count per sales rep", e);
        }
    }

    /**
     * The number of invoices issued between the two dates, inclusive, to the customers of each sales rep (cancelled
     * invoices are not counted), highest first, then by name and id; reps without invoices are included with zero.
     */
    public List<SalesRepInvoiceCount> findInvoiceCountPerSalesRep(LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.name AS name, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE c.sales_rep_id = r.id "
                + "AND i.invoice_date >= ? AND i.invoice_date <= ? AND i.status <> 'CANCELLED') AS invoice_count "
                + "FROM sales_rep r ORDER BY invoice_count DESC, r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesRepInvoiceCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new SalesRepInvoiceCount(rs.getLong("id"), rs.getString("name"), rs.getInt("invoice_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoice count per sales rep", e);
        }
    }

    /**
     * The number of shipments shipped between the two dates, inclusive, for the orders of the customers of each sales
     * rep, highest first, then by name and id; reps without shipments are included with zero.
     */
    public List<SalesRepShipmentCount> findShipmentCountPerSalesRep(LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.name AS name, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.sales_rep_id = r.id AND s.ship_date >= ? AND s.ship_date <= ?) AS shipment_count "
                + "FROM sales_rep r ORDER BY shipment_count DESC, r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesRepShipmentCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new SalesRepShipmentCount(rs.getLong("id"), rs.getString("name"), rs.getInt("shipment_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find shipment count per sales rep", e);
        }
    }

    /**
     * The number of returns dated between the two dates, inclusive, for the orders of the customers of each sales
     * rep, highest first, then by name and id; reps without returns are included with zero.
     */
    public List<SalesRepReturnCount> findReturnCountPerSalesRep(LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.name AS name, "
                + "(SELECT COUNT(*) FROM sales_return sr JOIN sales_order o ON o.id = sr.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.sales_rep_id = r.id AND sr.return_date >= ? AND sr.return_date <= ?) AS return_count "
                + "FROM sales_rep r ORDER BY return_count DESC, r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesRepReturnCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new SalesRepReturnCount(rs.getLong("id"), rs.getString("name"), rs.getInt("return_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find return count per sales rep", e);
        }
    }

    /**
     * The number of purchase orders dated between the two dates, inclusive, for the warehouses in the regions of the
     * customers of each sales rep (purchase orders have no customer of their own), highest first, then by name and id;
     * reps without purchase orders are included with zero. A purchase order counts once per rep.
     */
    public List<SalesRepPurchaseOrderCount> findPurchaseOrderCountPerSalesRep(LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.name AS name, "
                + "(SELECT COUNT(*) FROM purchase_order po JOIN warehouse w ON w.id = po.warehouse_id "
                + "WHERE po.order_date >= ? AND po.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM customer c WHERE c.sales_rep_id = r.id AND c.region = w.region)) AS purchase_order_count "
                + "FROM sales_rep r ORDER BY purchase_order_count DESC, r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesRepPurchaseOrderCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new SalesRepPurchaseOrderCount(rs.getLong("id"), rs.getString("name"), rs.getInt("purchase_order_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find purchase order count per sales rep", e);
        }
    }

    /**
     * The purchase orders dated between the two dates, inclusive, for the warehouses in the regions of the customers of
     * the country (purchase orders have no customer of their own), oldest first, then by id. A purchase order is listed
     * once, however many customers the country has in the region.
     */
    public List<PurchaseOrder> findPurchaseOrdersByCountry(String country, LocalDate from, LocalDate to) {
        String sql = "SELECT po.id, po.number, po.supplier_id, po.order_date, po.expected_date, po.status, po.warehouse_id "
                + "FROM purchase_order po JOIN warehouse w ON w.id = po.warehouse_id "
                + "WHERE po.order_date >= ? AND po.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM customer c WHERE c.country = ? AND c.region = w.region) "
                + "ORDER BY po.order_date, po.id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setString(3, country);
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
            throw new IllegalStateException("Cannot find purchase orders by country", e);
        }
    }

    /**
     * The number of purchase orders dated between the two dates, inclusive, for the warehouses in the regions of the
     * customers of each country, highest first, then by country; countries without purchase orders are left out. A
     * purchase order counts once per country.
     */
    public List<CountryPurchaseOrderCount> findPurchaseOrderCountPerCountry(LocalDate from, LocalDate to) {
        String sql = "SELECT country, purchase_order_count FROM ("
                + "SELECT d.country AS country, "
                + "(SELECT COUNT(*) FROM purchase_order po JOIN warehouse w ON w.id = po.warehouse_id "
                + "WHERE po.order_date >= ? AND po.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM customer c WHERE c.country = d.country AND c.region = w.region)) AS purchase_order_count "
                + "FROM (SELECT DISTINCT country FROM customer) d) "
                + "WHERE purchase_order_count > 0 ORDER BY purchase_order_count DESC, country";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CountryPurchaseOrderCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new CountryPurchaseOrderCount(rs.getString("country"), rs.getInt("purchase_order_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find purchase order count per country", e);
        }
    }

    /**
     * The sales returns dated between the two dates, inclusive, for the orders of the customers of the country, oldest
     * first, then by id.
     */
    public List<SalesReturn> findReturnsByCountry(String country, LocalDate from, LocalDate to) {
        String sql = "SELECT r.id, r.number, r.order_id, r.return_date, r.reason "
                + "FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.country = ? AND r.return_date >= ? AND r.return_date <= ? "
                + "ORDER BY r.return_date, r.id";
        String lineSql = "SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, country);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesReturn> returns = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<ReturnLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new ReturnLine(lineRs.getLong("product_id"), lineRs.getInt("quantity")));
                            }
                        }
                    }
                    returns.add(new SalesReturn(id, rs.getString("number"), rs.getLong("order_id"),
                            rs.getObject("return_date", LocalDate.class), rs.getString("reason"), lines));
                }
                return returns;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returns by country", e);
        }
    }

    /**
     * The number of returns dated between the two dates, inclusive, for the orders of the customers of each country
     * (countries without returns in the period are left out). Sorted by return count, highest first, then by country.
     */
    public List<CountryReturnCount> findReturnCountPerCountry(LocalDate from, LocalDate to) {
        String sql = "SELECT c.country AS country, COUNT(*) AS return_count FROM sales_return r "
                + "JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE r.return_date >= ? AND r.return_date <= ? "
                + "GROUP BY c.country ORDER BY return_count DESC, c.country";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CountryReturnCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new CountryReturnCount(rs.getString("country"), rs.getInt("return_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find return count per country", e);
        }
    }

    /**
     * The shipments shipped between the two dates, inclusive, for the orders of the customers of the country, oldest
     * first, then by id.
     */
    public List<Shipment> findShipmentsByCountry(String country, LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date "
                + "FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.country = ? AND s.ship_date >= ? AND s.ship_date <= ? "
                + "ORDER BY s.ship_date, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, country);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<Shipment> shipments = new ArrayList<>();
                while (rs.next()) {
                    shipments.add(new Shipment(rs.getLong("id"), rs.getLong("order_id"), rs.getLong("warehouse_id"),
                            rs.getLong("carrier_id"), rs.getString("tracking_number"),
                            rs.getObject("ship_date", LocalDate.class), rs.getObject("delivery_date", LocalDate.class)));
                }
                return shipments;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find shipments by country", e);
        }
    }

    /**
     * The number of shipments shipped between the two dates, inclusive, for the orders of the customers of each
     * country (countries without shipments in the period are left out). Sorted by shipment count, highest first, then
     * by country.
     */
    public List<CountryShipmentCount> findShipmentCountPerCountry(LocalDate from, LocalDate to) {
        String sql = "SELECT c.country AS country, COUNT(*) AS shipment_count FROM shipment s "
                + "JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE s.ship_date >= ? AND s.ship_date <= ? "
                + "GROUP BY c.country ORDER BY shipment_count DESC, c.country";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CountryShipmentCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new CountryShipmentCount(rs.getString("country"), rs.getInt("shipment_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find shipment count per country", e);
        }
    }

    /**
     * The orders of the customers of the country placed between the two dates, inclusive (cancelled orders are left
     * out), oldest first, then by id.
     */
    public List<SalesOrder> findOrdersByCountry(String country, LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.country = ? AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "ORDER BY o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, country);
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
            throw new IllegalStateException("Cannot find orders by country", e);
        }
    }

    /**
     * The number of orders placed between the two dates, inclusive, by the customers of each country (cancelled orders
     * are not counted; countries without orders in the period are left out). Sorted by order count, highest first,
     * then by country.
     */
    public List<CountryOrderCount> findOrderCountPerCountry(LocalDate from, LocalDate to) {
        String sql = "SELECT c.country AS country, COUNT(*) AS order_count FROM sales_order o "
                + "JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY c.country ORDER BY order_count DESC, c.country";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CountryOrderCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new CountryOrderCount(rs.getString("country"), rs.getInt("order_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find order count per country", e);
        }
    }

    /**
     * The invoices of the customers of the country issued between the two dates, inclusive (cancelled invoices are
     * left out), oldest first, then by id.
     */
    public List<Invoice> findInvoicesByCountry(String country, LocalDate from, LocalDate to) {
        String sql = "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE c.country = ? AND i.invoice_date >= ? AND i.invoice_date <= ? AND i.status <> 'CANCELLED' "
                + "ORDER BY i.invoice_date, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, country);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<Invoice> invoices = new ArrayList<>();
                while (rs.next()) {
                    invoices.add(new Invoice(rs.getLong("id"), rs.getString("number"), rs.getLong("order_id"),
                            rs.getLong("customer_id"), rs.getObject("invoice_date", LocalDate.class),
                            rs.getObject("due_date", LocalDate.class), rs.getBigDecimal("amount"),
                            InvoiceStatus.valueOf(rs.getString("status"))));
                }
                return invoices;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoices by country", e);
        }
    }

    /**
     * The number of invoices issued between the two dates, inclusive, to the customers of each country (cancelled
     * invoices are not counted; countries without invoices in the period are left out). Sorted by invoice count,
     * highest first, then by country.
     */
    public List<CountryInvoiceCount> findInvoiceCountPerCountry(LocalDate from, LocalDate to) {
        String sql = "SELECT c.country AS country, COUNT(*) AS invoice_count FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.invoice_date >= ? AND i.invoice_date <= ? AND i.status <> 'CANCELLED' "
                + "GROUP BY c.country ORDER BY invoice_count DESC, c.country";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CountryInvoiceCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new CountryInvoiceCount(rs.getString("country"), rs.getInt("invoice_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoice count per country", e);
        }
    }

    /**
     * The customers of the country created between the two dates, inclusive (customers without a created date are left
     * out), oldest first, then by id.
     */
    public List<Customer> findCustomersByCountry(String country, LocalDate from, LocalDate to) {
        String sql = "SELECT c.id, c.name, c.email, c.phone, c.country, c.region, c.status FROM customer c "
                + "WHERE c.country = ? AND c.created_date >= ? AND c.created_date <= ? "
                + "ORDER BY c.created_date, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, country);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<Customer> customers = new ArrayList<>();
                while (rs.next()) {
                    customers.add(new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                            CustomerStatus.valueOf(rs.getString("status"))));
                }
                return customers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customers by country", e);
        }
    }

    /**
     * The number of customers created between the two dates, inclusive, per country (customers without a created date
     * are not counted; countries without customers in the period are left out). Sorted by customer count, highest
     * first, then by country.
     */
    public List<CountryCustomerCount> findCustomerCountPerCountry(LocalDate from, LocalDate to) {
        String sql = "SELECT c.country AS country, COUNT(*) AS customer_count FROM customer c "
                + "WHERE c.created_date >= ? AND c.created_date <= ? "
                + "GROUP BY c.country ORDER BY customer_count DESC, c.country";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CountryCustomerCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new CountryCustomerCount(rs.getString("country"), rs.getInt("customer_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer count per country", e);
        }
    }

    /**
     * Units sold and revenue per product in the non-cancelled orders placed between the two dates, inclusive. Sorted by
     * units sold, highest first (then by revenue, highest first, then by SKU); products without sales are left out.
     */
    public List<ProductSales> findSalesPerProduct(LocalDate from, LocalDate to) {
        String sql = "SELECT p.id AS id, p.sku AS sku, p.name AS name, SUM(l.quantity) AS units, "
                + "SUM(l.quantity * l.unit_price) AS revenue "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY p.id, p.sku, p.name ORDER BY units DESC, revenue DESC, p.sku";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductSales> sales = new ArrayList<>();
                while (rs.next()) {
                    sales.add(new ProductSales(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getLong("units"), rs.getBigDecimal("revenue")));
                }
                return sales;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales per product", e);
        }
    }

    /**
     * Revenue, purchase cost and margin per product in the non-cancelled orders placed between the two dates,
     * inclusive. The cost is the units sold times the last purchase cost (latest sent or received purchase order line;
     * 0 if the product was never purchased), the margin is revenue minus cost. Sorted by margin, lowest first (then by
     * SKU); products without sales are left out.
     */
    public List<ProductMargin> findMarginPerProduct(LocalDate from, LocalDate to) {
        String sql = "SELECT id, sku, name, revenue, cost FROM (SELECT p.id AS id, p.sku AS sku, p.name AS name, "
                + "SUM(l.quantity * l.unit_price) AS revenue, "
                + "SUM(l.quantity * COALESCE("
                + "(SELECT pl.unit_cost FROM purchase_order_line pl JOIN purchase_order po ON po.id = pl.order_id "
                + "WHERE pl.product_id = p.id AND po.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY po.order_date DESC, po.id DESC, pl.line_no DESC LIMIT 1), 0)) AS cost "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY p.id, p.sku, p.name) ORDER BY revenue - cost, sku";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductMargin> margins = new ArrayList<>();
                while (rs.next()) {
                    BigDecimal revenue = rs.getBigDecimal("revenue").setScale(2);
                    BigDecimal cost = rs.getBigDecimal("cost").setScale(2);
                    margins.add(new ProductMargin(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            revenue, cost, revenue.subtract(cost)));
                }
                return margins;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find margin per product", e);
        }
    }

    /**
     * Revenue, purchase cost, margin and margin percentage per product category in the non-cancelled orders placed
     * between the two dates, inclusive, with cost and margin as for the margin per product. The percentage is the margin
     * as a share of the revenue, rounded to two decimals (0 if the revenue is 0). Sorted by margin, lowest first (then by
     * category); categories without sales are left out.
     */
    public List<CategoryMargin> findMarginPerCategory(LocalDate from, LocalDate to) {
        String sql = "SELECT category, revenue, cost FROM (SELECT p.category AS category, "
                + "SUM(l.quantity * l.unit_price) AS revenue, "
                + "SUM(l.quantity * COALESCE("
                + "(SELECT pl.unit_cost FROM purchase_order_line pl JOIN purchase_order po ON po.id = pl.order_id "
                + "WHERE pl.product_id = p.id AND po.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY po.order_date DESC, po.id DESC, pl.line_no DESC LIMIT 1), 0)) AS cost "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY p.category) ORDER BY revenue - cost, category";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CategoryMargin> margins = new ArrayList<>();
                while (rs.next()) {
                    BigDecimal revenue = rs.getBigDecimal("revenue").setScale(2);
                    BigDecimal cost = rs.getBigDecimal("cost").setScale(2);
                    BigDecimal margin = revenue.subtract(cost);
                    BigDecimal marginPercent = revenue.signum() == 0 ? new BigDecimal("0.00")
                            : margin.multiply(new BigDecimal("100")).divide(revenue, 2, RoundingMode.HALF_UP);
                    margins.add(new CategoryMargin(rs.getString("category"), revenue, cost, margin, marginPercent));
                }
                return margins;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find margin per category", e);
        }
    }

    /**
     * The categories of the margin per category whose margin percentage is below the given percentage (compared
     * exactly, not after rounding), in the same order.
     */
    public List<CategoryMargin> findCategoriesBelowMargin(BigDecimal percent, LocalDate from, LocalDate to) {
        String sql = "SELECT category, revenue, cost FROM (SELECT p.category AS category, "
                + "SUM(l.quantity * l.unit_price) AS revenue, "
                + "SUM(l.quantity * COALESCE("
                + "(SELECT pl.unit_cost FROM purchase_order_line pl JOIN purchase_order po ON po.id = pl.order_id "
                + "WHERE pl.product_id = p.id AND po.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY po.order_date DESC, po.id DESC, pl.line_no DESC LIMIT 1), 0)) AS cost "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY p.category) WHERE (revenue - cost) * 100 < ? * revenue ORDER BY revenue - cost, category";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setBigDecimal(3, percent);
            try (ResultSet rs = statement.executeQuery()) {
                List<CategoryMargin> margins = new ArrayList<>();
                while (rs.next()) {
                    BigDecimal revenue = rs.getBigDecimal("revenue").setScale(2);
                    BigDecimal cost = rs.getBigDecimal("cost").setScale(2);
                    BigDecimal margin = revenue.subtract(cost);
                    BigDecimal marginPercent = revenue.signum() == 0 ? new BigDecimal("0.00")
                            : margin.multiply(new BigDecimal("100")).divide(revenue, 2, RoundingMode.HALF_UP);
                    margins.add(new CategoryMargin(rs.getString("category"), revenue, cost, margin, marginPercent));
                }
                return margins;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find categories below margin", e);
        }
    }

    /**
     * Units sold and revenue per product, as for the sales per product, restricted to the products of the category
     * and of all categories below it.
     */
    public List<ProductSales> findSalesPerProductInCategory(String category, LocalDate from, LocalDate to) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT p.id AS id, p.sku AS sku, p.name AS name, SUM(l.quantity) AS units, "
                + "SUM(l.quantity * l.unit_price) AS revenue "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "AND p.category IN (SELECT code FROM tree) "
                + "GROUP BY p.id, p.sku, p.name ORDER BY units DESC, revenue DESC, p.sku";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductSales> sales = new ArrayList<>();
                while (rs.next()) {
                    sales.add(new ProductSales(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getLong("units"), rs.getBigDecimal("revenue")));
                }
                return sales;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales per product in category", e);
        }
    }

    /**
     * The sales order lines matching all the given criteria, newest order first (then by order number and line number).
     * Every criterion is optional: a null product id, category, customer id, from date, to date or minimum quantity is
     * not applied. The category also matches the categories below it; the dates are inclusive and apply to the order date.
     */
    public List<OrderLineMatch> searchOrderLines(Long productId, String category, Long customerId, LocalDate from,
                                                 LocalDate to, Integer minQuantity) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = CAST(? AS VARCHAR) "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT o.id AS order_id, o.number, o.order_date, c.id AS customer_id, c.name AS customer_name, l.line_no, "
                + "p.id, p.sku, p.name AS product_name, p.category, p.unit_price AS product_price, p.unit_of_measure, p.active, "
                + "l.quantity, l.unit_price, l.promotion_code "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id JOIN product p ON p.id = l.product_id "
                + "JOIN customer c ON c.id = o.customer_id "
                + "WHERE (CAST(? AS BIGINT) IS NULL OR l.product_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS VARCHAR) IS NULL OR p.category = CAST(? AS VARCHAR) OR p.category IN (SELECT code FROM tree)) "
                + "AND (CAST(? AS BIGINT) IS NULL OR o.customer_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS DATE) IS NULL OR o.order_date >= CAST(? AS DATE)) "
                + "AND (CAST(? AS DATE) IS NULL OR o.order_date <= CAST(? AS DATE)) "
                + "AND (CAST(? AS INT) IS NULL OR l.quantity >= CAST(? AS INT)) "
                + "ORDER BY o.order_date DESC, o.number DESC, o.id DESC, l.line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, category);
            statement.setObject(2, productId);
            statement.setObject(3, productId);
            statement.setObject(4, category);
            statement.setObject(5, category);
            statement.setObject(6, customerId);
            statement.setObject(7, customerId);
            statement.setObject(8, from);
            statement.setObject(9, from);
            statement.setObject(10, to);
            statement.setObject(11, to);
            statement.setObject(12, minQuantity);
            statement.setObject(13, minQuantity);
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderLineMatch> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new OrderLineMatch(rs.getLong("order_id"), rs.getString("number"),
                            rs.getObject("order_date", LocalDate.class), rs.getLong("customer_id"),
                            rs.getString("customer_name"), rs.getInt("line_no"),
                            new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("product_name"),
                                    rs.getString("category"), rs.getBigDecimal("product_price"),
                                    rs.getString("unit_of_measure"), rs.getBoolean("active")),
                            rs.getInt("quantity"), rs.getBigDecimal("unit_price"), rs.getString("promotion_code")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot search order lines", e);
        }
    }

    /**
     * Units returned per product in the sales returns dated between the two dates, inclusive. Sorted by units
     * returned, highest first (then by SKU); products without returns are left out.
     */
    public List<ProductReturns> findReturnedQuantityPerProduct(LocalDate from, LocalDate to) {
        String sql = "SELECT p.id AS id, p.sku AS sku, p.name AS name, SUM(l.quantity) AS units "
                + "FROM sales_return r JOIN sales_return_line l ON l.return_id = r.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE r.return_date >= ? AND r.return_date <= ? "
                + "GROUP BY p.id, p.sku, p.name ORDER BY units DESC, p.sku";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductReturns> returned = new ArrayList<>();
                while (rs.next()) {
                    returned.add(new ProductReturns(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getLong("units")));
                }
                return returned;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returned quantity per product", e);
        }
    }

    /**
     * Units returned per product, as for the returned quantity per product, restricted to the products of the
     * category and of all categories below it.
     */
    public List<ProductReturns> findReturnedQuantityPerProductInCategory(String category, LocalDate from, LocalDate to) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT p.id AS id, p.sku AS sku, p.name AS name, SUM(l.quantity) AS units "
                + "FROM sales_return r JOIN sales_return_line l ON l.return_id = r.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE r.return_date >= ? AND r.return_date <= ? "
                + "AND p.category IN (SELECT code FROM tree) "
                + "GROUP BY p.id, p.sku, p.name ORDER BY units DESC, p.sku";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductReturns> returned = new ArrayList<>();
                while (rs.next()) {
                    returned.add(new ProductReturns(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getLong("units")));
                }
                return returned;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returned quantity per product in category", e);
        }
    }

    /**
     * Number and value of the sales returns dated between the two dates, inclusive, per reason. The value is the
     * returned quantities at the unit prices of the order. Sorted by value, highest first (then by reason); reasons
     * without returns are left out.
     */
    public List<ReasonReturns> findReturnsPerReason(LocalDate from, LocalDate to) {
        String sql = "SELECT r.reason AS reason, COUNT(DISTINCT r.id) AS return_count, "
                + "COALESCE(SUM(l.quantity * (SELECT ol.unit_price FROM sales_order_line ol "
                + "WHERE ol.order_id = r.order_id AND ol.product_id = l.product_id "
                + "ORDER BY ol.line_no FETCH FIRST 1 ROW ONLY)), 0) AS total "
                + "FROM sales_return r LEFT JOIN sales_return_line l ON l.return_id = r.id "
                + "WHERE r.return_date >= ? AND r.return_date <= ? "
                + "GROUP BY r.reason ORDER BY total DESC, r.reason";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ReasonReturns> returns = new ArrayList<>();
                while (rs.next()) {
                    returns.add(new ReasonReturns(rs.getString("reason"), rs.getInt("return_count"),
                            rs.getBigDecimal("total")));
                }
                return returns;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returns per reason", e);
        }
    }

    /**
     * The sales returns dated between the two dates, inclusive, with the customer of the order and the value of each
     * return (the returned quantities at the unit prices of the order, as for the returns per reason). Customers with
     * the most returns first (then by name and id), each customer's returns oldest first (then by id).
     */
    public List<CustomerReturn> findReturnsPerCustomer(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, r.id AS id, r.number AS number, "
                + "r.return_date AS return_date, r.reason AS reason, "
                + "COALESCE((SELECT SUM(l.quantity * (SELECT ol.unit_price FROM sales_order_line ol "
                + "WHERE ol.order_id = r.order_id AND ol.product_id = l.product_id "
                + "ORDER BY ol.line_no FETCH FIRST 1 ROW ONLY)) FROM sales_return_line l WHERE l.return_id = r.id), 0) AS returned_value "
                + "FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE r.return_date >= ? AND r.return_date <= ? "
                + "ORDER BY (SELECT COUNT(*) FROM sales_return r2 JOIN sales_order o2 ON o2.id = r2.order_id "
                + "WHERE o2.customer_id = c.id AND r2.return_date >= ? AND r2.return_date <= ?) DESC, "
                + "c.name, c.id, r.return_date, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setObject(3, from);
            statement.setObject(4, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerReturn> returns = new ArrayList<>();
                while (rs.next()) {
                    returns.add(new CustomerReturn(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getString("customer_name"), rs.getObject("return_date", LocalDate.class),
                            rs.getString("reason"), rs.getBigDecimal("returned_value")));
                }
                return returns;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returns per customer", e);
        }
    }

    /**
     * The sales returns received into the warehouse, dated between the two dates, inclusive, with the number of the
     * order. Oldest first, then by id.
     */
    public List<WarehouseReturn> findReturnsByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.number AS number, o.number AS order_number, r.return_date AS return_date, "
                + "r.reason AS reason FROM sales_return r JOIN sales_order o ON o.id = r.order_id "
                + "WHERE r.warehouse_id = ? AND r.return_date >= ? AND r.return_date <= ? "
                + "ORDER BY r.return_date, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseReturn> returns = new ArrayList<>();
                while (rs.next()) {
                    returns.add(new WarehouseReturn(rs.getLong("id"), rs.getString("number"),
                            rs.getString("order_number"), rs.getObject("return_date", LocalDate.class),
                            rs.getString("reason")));
                }
                return returns;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returns of warehouse", e);
        }
    }

    /**
     * The quantity per product of the sales returns received into the warehouse, dated between the two dates,
     * inclusive. Highest quantity first, then by SKU.
     */
    public List<ProductReturnedQuantity> findReturnedQuantityByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT p.id AS product_id, p.sku AS sku, p.name AS name, SUM(l.quantity) AS quantity "
                + "FROM sales_return r JOIN sales_return_line l ON l.return_id = r.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE r.warehouse_id = ? AND r.return_date >= ? AND r.return_date <= ? "
                + "GROUP BY p.id, p.sku, p.name ORDER BY SUM(l.quantity) DESC, p.sku";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductReturnedQuantity> quantities = new ArrayList<>();
                while (rs.next()) {
                    quantities.add(new ProductReturnedQuantity(rs.getLong("product_id"), rs.getString("sku"),
                            rs.getString("name"), rs.getLong("quantity")));
                }
                return quantities;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returned quantities of warehouse", e);
        }
    }

    /**
     * Number and value of the sales returns dated between the two dates, inclusive, per reason and region of the
     * customer, valued as for the returns per reason. Sorted by region name, then value (highest first), then reason;
     * combinations without returns are left out.
     */
    public List<RegionReasonReturns> findReturnsPerReasonAndRegion(LocalDate from, LocalDate to) {
        String sql = "SELECT c.region AS region, r.reason AS reason, COUNT(DISTINCT r.id) AS return_count, "
                + "COALESCE(SUM(l.quantity * (SELECT ol.unit_price FROM sales_order_line ol "
                + "WHERE ol.order_id = r.order_id AND ol.product_id = l.product_id "
                + "ORDER BY ol.line_no FETCH FIRST 1 ROW ONLY)), 0) AS total "
                + "FROM sales_return r JOIN sales_order o ON o.id = r.order_id "
                + "JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_return_line l ON l.return_id = r.id "
                + "WHERE r.return_date >= ? AND r.return_date <= ? "
                + "GROUP BY c.region, r.reason ORDER BY c.region, total DESC, r.reason";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<RegionReasonReturns> returns = new ArrayList<>();
                while (rs.next()) {
                    returns.add(new RegionReasonReturns(Region.valueOf(rs.getString("region")), rs.getString("reason"),
                            rs.getInt("return_count"), rs.getBigDecimal("total")));
                }
                return returns;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returns per reason and region", e);
        }
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, per customer. Sorted
     * by revenue, highest first (then by customer name, then id); customers without orders are left out.
     */
    public List<CustomerSales> findSalesPerCustomer(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY c.id, c.name ORDER BY revenue DESC, c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerSales> sales = new ArrayList<>();
                while (rs.next()) {
                    sales.add(new CustomerSales(rs.getLong("id"), rs.getString("name"), rs.getInt("order_count"),
                            rs.getBigDecimal("revenue")));
                }
                return sales;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales per customer", e);
        }
    }
}
