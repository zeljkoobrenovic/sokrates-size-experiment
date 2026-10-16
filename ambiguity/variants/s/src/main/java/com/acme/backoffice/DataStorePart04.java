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

class DataStorePart04 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart04(DataStore store, java.sql.Connection connection) {
        this.store = store;
        this.connection = connection;
    }

    /** The archived orders, oldest first. */
    public List<SalesOrder> listArchivedSalesOrders() {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "JOIN order_archive ar ON ar.order_id = o.id ORDER BY o.order_date, o.id";
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
            throw new IllegalStateException("Cannot list archived sales orders", e);
        }
    }

    /** The archived orders of the customer, oldest first. */
    public List<SalesOrder> listArchivedSalesOrdersByCustomer(long customerId) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "JOIN order_archive ar ON ar.order_id = o.id WHERE o.customer_id = ? ORDER BY o.order_date, o.id";
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
            throw new IllegalStateException("Cannot list archived sales orders of customer", e);
        }
    }

    /** The archived orders of the customers of the region, oldest first. */
    public List<SalesOrder> listArchivedSalesOrdersByRegion(Region region) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "JOIN order_archive ar ON ar.order_id = o.id JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.region = ? ORDER BY o.order_date, o.id";
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
            throw new IllegalStateException("Cannot list archived sales orders of region", e);
        }
    }

    /** The revenue of the archived, non-cancelled orders per year of the order date, oldest year first. */
    public List<ArchivedYearRevenue> findArchivedRevenuePerYear() {
        String sql = "SELECT YEAR(o.order_date) AS year_no, SUM(l.quantity * l.unit_price) AS revenue FROM sales_order o "
                + "JOIN order_archive ar ON ar.order_id = o.id JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.status <> 'CANCELLED' GROUP BY YEAR(o.order_date) ORDER BY year_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<ArchivedYearRevenue> years = new ArrayList<>();
                while (rs.next()) {
                    years.add(new ArchivedYearRevenue(rs.getInt("year_no"), rs.getBigDecimal("revenue")));
                }
                return years;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find archived revenue per year", e);
        }
    }

    public List<OrderStatusChange> listSalesOrderStatusChanges(long orderId) {
        String sql = "SELECT order_id, status, change_date FROM sales_order_status_change WHERE order_id = ? ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderStatusChange> changes = new ArrayList<>();
                while (rs.next()) {
                    changes.add(new OrderStatusChange(rs.getLong("order_id"), OrderStatus.valueOf(rs.getString("status")),
                            rs.getObject("change_date", LocalDate.class)));
                }
                return changes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list sales order status changes", e);
        }
    }

    /** Sum of quantity times unit price over the lines of the order; zero if there is no such order or no lines. */
    public BigDecimal findSalesOrderTotal(String number) {
        String sql = "SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) AS total FROM sales_order o "
                + "JOIN sales_order_line l ON l.order_id = o.id WHERE o.number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales order total", e);
        }
    }

    /** Total value of all orders of the customer that are not cancelled. */
    public BigDecimal findCustomerOrderTotal(long customerId) {
        String sql = "SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) AS total FROM sales_order o "
                + "JOIN sales_order_line l ON l.order_id = o.id WHERE o.customer_id = ? AND o.status <> 'CANCELLED'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer order total", e);
        }
    }

    /** Every order of the customer, archived and cancelled ones included, oldest first. */
    public List<OrderHistoryEntry> listOrderHistoryByCustomer(long customerId) {
        String sql = "SELECT o.number, o.customer_id, o.order_date, o.status, COUNT(l.line_no) AS line_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS total FROM sales_order o "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id WHERE o.customer_id = ? "
                + "GROUP BY o.id, o.number, o.customer_id, o.order_date, o.status ORDER BY o.order_date, o.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderHistoryEntry> entries = new ArrayList<>();
                while (rs.next()) {
                    entries.add(new OrderHistoryEntry(rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")),
                            rs.getInt("line_count"), rs.getBigDecimal("total")));
                }
                return entries;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list order history of customer", e);
        }
    }

    /** Every order of the customers of the region, archived and cancelled ones included, oldest first. */
    public List<OrderHistoryEntry> listOrderHistoryByRegion(Region region) {
        String sql = "SELECT o.number, o.customer_id, o.order_date, o.status, COUNT(l.line_no) AS line_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS total FROM sales_order o "
                + "JOIN customer c ON c.id = o.customer_id LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE c.region = ? "
                + "GROUP BY o.id, o.number, o.customer_id, o.order_date, o.status ORDER BY o.order_date, o.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderHistoryEntry> entries = new ArrayList<>();
                while (rs.next()) {
                    entries.add(new OrderHistoryEntry(rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")),
                            rs.getInt("line_count"), rs.getBigDecimal("total")));
                }
                return entries;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list order history of region", e);
        }
    }

    public Invoice createInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoice (number, order_id, customer_id, invoice_date, due_date, amount, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, invoice.number());
            statement.setLong(2, invoice.orderId());
            statement.setLong(3, invoice.customerId());
            statement.setObject(4, invoice.invoiceDate());
            statement.setObject(5, invoice.dueDate());
            statement.setBigDecimal(6, invoice.amount());
            statement.setString(7, invoice.status().name());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Invoice(keys.getLong(1), invoice.number(), invoice.orderId(), invoice.customerId(),
                        invoice.invoiceDate(), invoice.dueDate(), invoice.amount(), invoice.status());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create invoice", e);
        }
    }

    public Optional<Invoice> findInvoiceByNumber(String number) {
        String sql = "SELECT id, number, order_id, customer_id, invoice_date, due_date, amount, status FROM invoice "
                + "WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Invoice(rs.getLong("id"), rs.getString("number"), rs.getLong("order_id"),
                        rs.getLong("customer_id"), rs.getObject("invoice_date", LocalDate.class),
                        rs.getObject("due_date", LocalDate.class), rs.getBigDecimal("amount"),
                        InvoiceStatus.valueOf(rs.getString("status"))));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoice", e);
        }
    }

    /** The invoice of the order that is not cancelled, if any. */
    public Optional<Invoice> findActiveInvoiceByOrder(long orderId) {
        String sql = "SELECT id, number, order_id, customer_id, invoice_date, due_date, amount, status FROM invoice "
                + "WHERE order_id = ? AND status <> 'CANCELLED'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Invoice(rs.getLong("id"), rs.getString("number"), rs.getLong("order_id"),
                        rs.getLong("customer_id"), rs.getObject("invoice_date", LocalDate.class),
                        rs.getObject("due_date", LocalDate.class), rs.getBigDecimal("amount"),
                        InvoiceStatus.valueOf(rs.getString("status"))));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoice of order", e);
        }
    }

    public List<Invoice> listInvoicesByCustomer(long customerId) {
        String sql = "SELECT id, number, order_id, customer_id, invoice_date, due_date, amount, status FROM invoice "
                + "WHERE customer_id = ? ORDER BY invoice_date DESC, id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
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
            throw new IllegalStateException("Cannot list invoices", e);
        }
    }

    /** The invoices of the customer dated between the two dates, inclusive, oldest first (then by id), without cancelled ones. */
    public List<Invoice> listInvoicesByCustomerAndPeriod(long customerId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, order_id, customer_id, invoice_date, due_date, amount, status FROM invoice "
                + "WHERE customer_id = ? AND invoice_date >= ? AND invoice_date <= ? AND status <> 'CANCELLED' "
                + "ORDER BY invoice_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
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
            throw new IllegalStateException("Cannot list invoices by customer and period", e);
        }
    }

    /** The sum of the invoices of the customer dated before the date, without cancelled ones, zero if there are none. */
    public BigDecimal findInvoiceTotalBeforeByCustomer(long customerId, LocalDate before) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM invoice "
                + "WHERE customer_id = ? AND invoice_date < ? AND status <> 'CANCELLED'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setObject(2, before);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoice total before date", e);
        }
    }

    /** Open invoices whose due date is before the given date, oldest due date first. */
    public List<Invoice> listOverdueInvoices(LocalDate asOf) {
        String sql = "SELECT id, number, order_id, customer_id, invoice_date, due_date, amount, status FROM invoice "
                + "WHERE status = 'OPEN' AND due_date < ? ORDER BY due_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
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
            throw new IllegalStateException("Cannot list overdue invoices", e);
        }
    }

    /** Sum of the amounts of the open invoices issued on or before the date whose due date is before it. */
    public BigDecimal findOverdueInvoiceAmount(LocalDate asOf) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM invoice "
                + "WHERE status = 'OPEN' AND invoice_date <= ? AND due_date < ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
            statement.setObject(2, asOf);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find overdue invoice amount", e);
        }
    }

    /**
     * Invoices matching all the given criteria, oldest due date first. Every criterion is optional: null means no
     * restriction. The due dates are inclusive and the minimum amount is inclusive.
     */
    public List<Invoice> searchInvoices(Long customerId, InvoiceStatus status, LocalDate dueFrom, LocalDate dueTo, BigDecimal minAmount) {
        StringBuilder sql = new StringBuilder("SELECT id, number, order_id, customer_id, invoice_date, due_date, amount, status FROM invoice "
                + "WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();
        if (customerId != null) {
            sql.append(" AND customer_id = ?");
            parameters.add(customerId);
        }
        if (status != null) {
            sql.append(" AND status = ?");
            parameters.add(status.name());
        }
        if (dueFrom != null) {
            sql.append(" AND due_date >= ?");
            parameters.add(dueFrom);
        }
        if (dueTo != null) {
            sql.append(" AND due_date <= ?");
            parameters.add(dueTo);
        }
        if (minAmount != null) {
            sql.append(" AND amount >= ?");
            parameters.add(minAmount);
        }
        sql.append(" ORDER BY due_date, id");
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
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
            throw new IllegalStateException("Cannot search invoices", e);
        }
    }

    /** Open invoices of the customer whose due date is before the given date, oldest due date first. */
    public List<Invoice> listOverdueInvoicesByCustomer(LocalDate asOf, long customerId) {
        String sql = "SELECT id, number, order_id, customer_id, invoice_date, due_date, amount, status FROM invoice "
                + "WHERE status = 'OPEN' AND due_date < ? AND customer_id = ? ORDER BY due_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
            statement.setLong(2, customerId);
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
            throw new IllegalStateException("Cannot list overdue invoices of customer", e);
        }
    }

    /** Open invoices of customers in the region whose due date is before the given date, oldest due date first. */
    public List<Invoice> listOverdueInvoicesByRegion(LocalDate asOf, Region region) {
        String sql = "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'OPEN' AND i.due_date < ? AND c.region = ? ORDER BY i.due_date, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
            statement.setString(2, region.name());
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
            throw new IllegalStateException("Cannot list overdue invoices of region", e);
        }
    }

    /** Open invoices of the customers of the sales rep, oldest due date first. */
    public List<Invoice> listOpenInvoicesBySalesRep(long salesRepId) {
        String sql = "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'OPEN' AND c.sales_rep_id = ? ORDER BY i.due_date, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
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
            throw new IllegalStateException("Cannot list open invoices of sales rep", e);
        }
    }

    /** Open invoices of the customers of the sales rep whose due date is before the given date, oldest due date first. */
    public List<Invoice> listOverdueInvoicesBySalesRep(LocalDate asOf, long salesRepId) {
        String sql = "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'OPEN' AND i.due_date < ? AND c.sales_rep_id = ? ORDER BY i.due_date, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
            statement.setLong(2, salesRepId);
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
            throw new IllegalStateException("Cannot list overdue invoices of sales rep", e);
        }
    }

    /**
     * Per sales rep with overdue invoices, the number and total amount of the open invoices of the rep's customers whose
     * due date is before the given date, biggest amount first (then by rep name, then id).
     */
    public List<RepOverdueInvoices> findOverdueInvoicesPerSalesRep(LocalDate asOf) {
        String sql = "SELECT r.id AS id, r.name AS name, COUNT(*) AS invoice_count, SUM(i.amount) AS total FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE i.status = 'OPEN' AND i.due_date < ? "
                + "GROUP BY r.id, r.name ORDER BY total DESC, r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
            try (ResultSet rs = statement.executeQuery()) {
                List<RepOverdueInvoices> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new RepOverdueInvoices(rs.getLong("id"), rs.getString("name"),
                            rs.getInt("invoice_count"), rs.getBigDecimal("total")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find overdue invoices per sales rep", e);
        }
    }

    /** Paid invoices of the customers of the sales rep whose last payment was made between the two dates, inclusive, oldest invoice first (then by id). */
    public List<Invoice> listPaidInvoicesBySalesRep(long salesRepId, LocalDate from, LocalDate to) {
        String sql = "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'PAID' AND c.sales_rep_id = ? "
                + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) >= ? "
                + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) <= ? "
                + "ORDER BY i.invoice_date, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
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
            throw new IllegalStateException("Cannot list paid invoices of sales rep", e);
        }
    }

    /** Open and paid invoices of customers in the region dated between the two dates, inclusive, oldest invoice first (then by id). */
    public List<Invoice> listOpenAndPaidInvoicesByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status IN ('OPEN', 'PAID') AND c.region = ? AND i.invoice_date >= ? AND i.invoice_date <= ? "
                + "ORDER BY i.invoice_date, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
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
            throw new IllegalStateException("Cannot list open and paid invoices of region", e);
        }
    }

    /** Number and total amount of the open and paid invoices of customers in the region dated between the two dates, inclusive, per status; statuses without invoices are left out. */
    public List<InvoiceStatusTotal> findOpenAndPaidInvoiceTotalsByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT i.status AS status, COUNT(*) AS invoice_count, SUM(i.amount) AS total FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status IN ('OPEN', 'PAID') AND c.region = ? AND i.invoice_date >= ? AND i.invoice_date <= ? "
                + "GROUP BY i.status ORDER BY i.status";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<InvoiceStatusTotal> totals = new ArrayList<>();
                while (rs.next()) {
                    totals.add(new InvoiceStatusTotal(InvoiceStatus.valueOf(rs.getString("status")),
                            rs.getInt("invoice_count"), rs.getBigDecimal("total")));
                }
                return totals;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoice totals of region", e);
        }
    }

    /** Open and paid invoices dated between the two dates, inclusive, with an amount above the given amount, with the region of their customer: by region, biggest invoice first (then by id). */
    public List<RegionLargeInvoice> listLargeInvoicesByRegion(BigDecimal minAmount, LocalDate from, LocalDate to) {
        String sql = "SELECT c.region AS region, i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status IN ('OPEN', 'PAID') AND i.amount > ? AND i.invoice_date >= ? AND i.invoice_date <= ? "
                + "ORDER BY c.region, i.amount DESC, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, minAmount);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<RegionLargeInvoice> invoices = new ArrayList<>();
                while (rs.next()) {
                    invoices.add(new RegionLargeInvoice(Region.valueOf(rs.getString("region")),
                            new Invoice(rs.getLong("id"), rs.getString("number"), rs.getLong("order_id"),
                                    rs.getLong("customer_id"), rs.getObject("invoice_date", LocalDate.class),
                                    rs.getObject("due_date", LocalDate.class), rs.getBigDecimal("amount"),
                                    InvoiceStatus.valueOf(rs.getString("status")))));
                }
                return invoices;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list large invoices per region", e);
        }
    }

    /** Paid invoices whose last payment was made after the due date and between the two dates, inclusive, by customer name (then customer id), then oldest due date first (then by invoice id). */
    public List<LatePaidInvoice> findLatePaidInvoicesByPeriod(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, i.number AS invoice_number, i.due_date AS due_date, "
                + "(SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) AS paid_date, "
                + "DATEDIFF('DAY', i.due_date, (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id)) AS days_late "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'PAID' "
                + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) > i.due_date "
                + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) >= ? "
                + "AND (SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id) <= ? "
                + "ORDER BY c.name, c.id, i.due_date, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<LatePaidInvoice> invoices = new ArrayList<>();
                while (rs.next()) {
                    invoices.add(new LatePaidInvoice(rs.getLong("customer_id"), rs.getString("customer_name"),
                            rs.getString("invoice_number"), rs.getObject("due_date", LocalDate.class),
                            rs.getObject("paid_date", LocalDate.class), rs.getInt("days_late")));
                }
                return invoices;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find late paid invoices", e);
        }
    }

    /** Open invoices with at least one payment, with the amount paid and still due, by customer region, customer name (then customer id), then oldest due date first (then by invoice id). */
    public List<PartiallyPaidInvoice> findPartiallyPaidInvoices() {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, c.region AS region, i.number AS invoice_number, "
                + "i.due_date AS due_date, i.amount AS amount, "
                + "(SELECT SUM(p.amount) FROM payment p WHERE p.invoice_id = i.id) AS paid_amount "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'OPEN' "
                + "AND EXISTS (SELECT 1 FROM payment p WHERE p.invoice_id = i.id) "
                + "ORDER BY c.region, c.name, c.id, i.due_date, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<PartiallyPaidInvoice> invoices = new ArrayList<>();
                while (rs.next()) {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    BigDecimal paidAmount = rs.getBigDecimal("paid_amount");
                    invoices.add(new PartiallyPaidInvoice(rs.getLong("customer_id"), rs.getString("customer_name"),
                            Region.valueOf(rs.getString("region")), rs.getString("invoice_number"),
                            rs.getObject("due_date", LocalDate.class), amount, paidAmount, amount.subtract(paidAmount)));
                }
                return invoices;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find partially paid invoices", e);
        }
    }

    /** Open invoices with the currency they were issued in (EUR when none is recorded) and their amount in it and in EUR, by currency, customer name (then customer id), then oldest due date first (then by invoice id). */
    public List<CurrencyOpenInvoice> findOpenInvoicesByCurrency() {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, COALESCE(ic.currency, 'EUR') AS currency, "
                + "i.number AS invoice_number, i.due_date AS due_date, COALESCE(ic.amount, i.amount) AS currency_amount, "
                + "i.amount AS eur_amount "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "LEFT JOIN invoice_currency ic ON ic.invoice_id = i.id "
                + "WHERE i.status = 'OPEN' "
                + "ORDER BY COALESCE(ic.currency, 'EUR'), c.name, c.id, i.due_date, i.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<CurrencyOpenInvoice> invoices = new ArrayList<>();
                while (rs.next()) {
                    invoices.add(new CurrencyOpenInvoice(rs.getLong("customer_id"), rs.getString("customer_name"),
                            Currency.valueOf(rs.getString("currency")), rs.getString("invoice_number"),
                            rs.getObject("due_date", LocalDate.class), rs.getBigDecimal("currency_amount"),
                            rs.getBigDecimal("eur_amount")));
                }
                return invoices;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find open invoices by currency", e);
        }
    }

    /** The payments of the customers of the sales rep received between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> findPaymentsBySalesRepAndPeriod(long salesRepId, LocalDate from, LocalDate to) {
        String sql = "SELECT p.id AS id, i.number AS invoice_number, c.id AS customer_id, c.name AS customer_name, "
                + "p.payment_date AS payment_date, p.amount AS amount "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE c.sales_rep_id = ? AND p.payment_date >= ? AND p.payment_date <= ? ORDER BY p.payment_date, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<PaymentReceived> payments = new ArrayList<>();
                while (rs.next()) {
                    payments.add(new PaymentReceived(rs.getLong("id"), rs.getString("invoice_number"),
                            rs.getLong("customer_id"), rs.getString("customer_name"),
                            rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return payments;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payments of sales rep by period", e);
        }
    }

    /**
     * Per sales rep with payments, the number and total amount of the payments of the rep's customers received between
     * the two dates, inclusive, biggest amount first (then by rep name, then id).
     */
    public List<RepPayments> findPaymentsPerSalesRep(LocalDate from, LocalDate to) {
        String sql = "SELECT r.id AS id, r.name AS name, COUNT(*) AS payment_count, SUM(p.amount) AS total FROM payment p "
                + "JOIN invoice i ON i.id = p.invoice_id "
                + "JOIN customer c ON c.id = i.customer_id "
                + "JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE p.payment_date >= ? AND p.payment_date <= ? "
                + "GROUP BY r.id, r.name ORDER BY total DESC, r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<RepPayments> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new RepPayments(rs.getLong("id"), rs.getString("name"),
                            rs.getInt("payment_count"), rs.getBigDecimal("total")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payments per sales rep", e);
        }
    }

    public int countInvoices() {
        String sql = "SELECT COUNT(*) AS invoice_count FROM invoice";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getInt("invoice_count");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot count invoices", e);
        }
    }

    public Payment createPayment(Payment payment) {
        String sql = "INSERT INTO payment (invoice_id, payment_date, amount) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, payment.invoiceId());
            statement.setObject(2, payment.paymentDate());
            statement.setBigDecimal(3, payment.amount());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Payment(keys.getLong(1), payment.invoiceId(), payment.paymentDate(), payment.amount());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create payment", e);
        }
    }

    public List<Payment> listPaymentsByInvoice(long invoiceId) {
        String sql = "SELECT id, invoice_id, payment_date, amount FROM payment "
                + "WHERE invoice_id = ? ORDER BY payment_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, invoiceId);
            try (ResultSet rs = statement.executeQuery()) {
                List<Payment> payments = new ArrayList<>();
                while (rs.next()) {
                    payments.add(new Payment(rs.getLong("id"), rs.getLong("invoice_id"),
                            rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return payments;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list payments", e);
        }
    }

    /** The sum of the payments of the invoice, zero if there are none. */
    public BigDecimal findPaymentTotalByInvoice(long invoiceId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM payment WHERE invoice_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, invoiceId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payment total", e);
        }
    }

    /** The payments received between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> findPaymentsByPeriod(LocalDate from, LocalDate to) {
        String sql = "SELECT p.id AS id, i.number AS invoice_number, c.id AS customer_id, c.name AS customer_name, "
                + "p.payment_date AS payment_date, p.amount AS amount "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE p.payment_date >= ? AND p.payment_date <= ? ORDER BY p.payment_date, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<PaymentReceived> payments = new ArrayList<>();
                while (rs.next()) {
                    payments.add(new PaymentReceived(rs.getLong("id"), rs.getString("invoice_number"),
                            rs.getLong("customer_id"), rs.getString("customer_name"),
                            rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return payments;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payments by period", e);
        }
    }

    /** The payments of the customer received between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> findPaymentsByPeriodAndCustomer(long customerId, LocalDate from, LocalDate to) {
        String sql = "SELECT p.id AS id, i.number AS invoice_number, c.id AS customer_id, c.name AS customer_name, "
                + "p.payment_date AS payment_date, p.amount AS amount "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE p.payment_date >= ? AND p.payment_date <= ? AND c.id = ? ORDER BY p.payment_date, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setLong(3, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<PaymentReceived> payments = new ArrayList<>();
                while (rs.next()) {
                    payments.add(new PaymentReceived(rs.getLong("id"), rs.getString("invoice_number"),
                            rs.getLong("customer_id"), rs.getString("customer_name"),
                            rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return payments;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payments by period and customer", e);
        }
    }

    /** The payments of the customer received between the two dates, inclusive, oldest first (then by id), without those of cancelled invoices. */
    public List<PaymentReceived> findPaymentsByCustomerAndPeriodExcludingCancelled(long customerId, LocalDate from, LocalDate to) {
        String sql = "SELECT p.id AS id, i.number AS invoice_number, c.id AS customer_id, c.name AS customer_name, "
                + "p.payment_date AS payment_date, p.amount AS amount "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE p.payment_date >= ? AND p.payment_date <= ? AND c.id = ? AND i.status <> 'CANCELLED' "
                + "ORDER BY p.payment_date, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setLong(3, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<PaymentReceived> payments = new ArrayList<>();
                while (rs.next()) {
                    payments.add(new PaymentReceived(rs.getLong("id"), rs.getString("invoice_number"),
                            rs.getLong("customer_id"), rs.getString("customer_name"),
                            rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return payments;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payments by customer and period", e);
        }
    }

    /** The sum of the payments of the customer made before the date, without those of cancelled invoices, zero if there are none. */
    public BigDecimal findPaymentTotalBeforeByCustomer(long customerId, LocalDate before) {
        String sql = "SELECT COALESCE(SUM(p.amount), 0) AS total FROM payment p JOIN invoice i ON i.id = p.invoice_id "
                + "WHERE i.customer_id = ? AND p.payment_date < ? AND i.status <> 'CANCELLED'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setObject(2, before);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payment total before date", e);
        }
    }

    /**
     * Number and total of the payments received per day between the two dates, inclusive, oldest day first; days
     * without payments are left out.
     */
    public List<DailyPaymentTotal> findPaymentTotalsPerDay(LocalDate from, LocalDate to) {
        String sql = "SELECT payment_date, COUNT(*) AS payment_count, SUM(amount) AS total FROM payment "
                + "WHERE payment_date >= ? AND payment_date <= ? GROUP BY payment_date ORDER BY payment_date";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<DailyPaymentTotal> totals = new ArrayList<>();
                while (rs.next()) {
                    totals.add(new DailyPaymentTotal(rs.getObject("payment_date", LocalDate.class),
                            rs.getInt("payment_count"), rs.getBigDecimal("total")));
                }
                return totals;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payment totals per day", e);
        }
    }

    /** The payments received from customers of the region between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> findPaymentsByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT p.id AS id, i.number AS invoice_number, c.id AS customer_id, c.name AS customer_name, "
                + "p.payment_date AS payment_date, p.amount AS amount "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE p.payment_date >= ? AND p.payment_date <= ? AND c.region = ? ORDER BY p.payment_date, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setString(3, region.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<PaymentReceived> payments = new ArrayList<>();
                while (rs.next()) {
                    payments.add(new PaymentReceived(rs.getLong("id"), rs.getString("invoice_number"),
                            rs.getLong("customer_id"), rs.getString("customer_name"),
                            rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return payments;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payments by region and period", e);
        }
    }

    /**
     * Number and total of the payments received from customers of the region per calendar month between the two
     * dates, inclusive, oldest month first; months without payments are left out.
     */
    public List<MonthlyPaymentTotal> findPaymentTotalsPerMonthByRegion(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT YEAR(p.payment_date) AS year_no, MONTH(p.payment_date) AS month_no, "
                + "COUNT(*) AS payment_count, SUM(p.amount) AS total "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE p.payment_date >= ? AND p.payment_date <= ? AND c.region = ? "
                + "GROUP BY YEAR(p.payment_date), MONTH(p.payment_date) ORDER BY YEAR(p.payment_date), MONTH(p.payment_date)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setString(3, region.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<MonthlyPaymentTotal> totals = new ArrayList<>();
                while (rs.next()) {
                    totals.add(new MonthlyPaymentTotal(rs.getInt("year_no"), rs.getInt("month_no"),
                            rs.getInt("payment_count"), rs.getBigDecimal("total")));
                }
                return totals;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find payment totals per month by region", e);
        }
    }

    /**
     * Payments that exceed the amount due of their invoice (the invoice amount minus the earlier payments), with the
     * part that exceeds it, by customer name (then customer id), then oldest payment first (then by payment id).
     */
    public List<UnappliedPayment> findUnappliedPayments() {
        String sql = "SELECT c.id AS customer_id, c.name AS customer_name, p.id AS id, i.number AS invoice_number, "
                + "p.payment_date AS payment_date, p.amount AS amount, "
                + "LEAST(p.amount, (SELECT SUM(q.amount) FROM payment q WHERE q.invoice_id = p.invoice_id "
                + "AND (q.payment_date < p.payment_date OR (q.payment_date = p.payment_date AND q.id <= p.id))) - i.amount) AS unapplied_amount "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE (SELECT SUM(q.amount) FROM payment q WHERE q.invoice_id = p.invoice_id "
                + "AND (q.payment_date < p.payment_date OR (q.payment_date = p.payment_date AND q.id <= p.id))) > i.amount "
                + "ORDER BY c.name, c.id, p.payment_date, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<UnappliedPayment> payments = new ArrayList<>();
                while (rs.next()) {
                    payments.add(new UnappliedPayment(rs.getLong("id"), rs.getString("invoice_number"),
                            rs.getLong("customer_id"), rs.getString("customer_name"),
                            rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount"),
                            rs.getBigDecimal("unapplied_amount")));
                }
                return payments;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find unapplied payments", e);
        }
    }

    /** The unpaid part of all open invoices of the customer (invoice amounts minus their payments), zero if there are none. */
    public BigDecimal findOpenInvoiceBalanceByCustomer(long customerId) {
        String sql = "SELECT COALESCE(SUM(i.amount), 0) - COALESCE(SUM((SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id)), 0) AS balance FROM invoice i WHERE i.customer_id = ? AND i.status = 'OPEN'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("balance");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find open invoice balance", e);
        }
    }
}
