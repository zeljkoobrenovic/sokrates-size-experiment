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

class DataStorePart10 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart10(DataStore store, java.sql.Connection connection) {
        this.store = store;
        this.connection = connection;
    }

    public Optional<BackOrder> findBackOrderByOrder(long orderId) {
        String sql = "SELECT b.id, b.order_id, o.number, o.customer_id, b.back_order_date FROM back_order b "
                + "JOIN sales_order o ON o.id = b.order_id WHERE b.order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new BackOrder(rs.getLong("id"), rs.getLong("order_id"), rs.getString("number"),
                            rs.getLong("customer_id"), rs.getObject("back_order_date", LocalDate.class)));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find back order by order", e);
        }
    }

    public List<BackOrder> listBackOrders() {
        String sql = "SELECT b.id, b.order_id, o.number, o.customer_id, b.back_order_date FROM back_order b "
                + "JOIN sales_order o ON o.id = b.order_id WHERE o.status = 'CONFIRMED' "
                + "ORDER BY b.back_order_date, b.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<BackOrder> backOrders = new ArrayList<>();
                while (rs.next()) {
                    backOrders.add(new BackOrder(rs.getLong("id"), rs.getLong("order_id"), rs.getString("number"),
                            rs.getLong("customer_id"), rs.getObject("back_order_date", LocalDate.class)));
                }
                return backOrders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list back orders", e);
        }
    }

    /** Number of back orders registered on or before the date whose order is still confirmed. */
    public int countBackOrdersOnDate(LocalDate date) {
        String sql = "SELECT COUNT(*) AS back_order_count FROM back_order b "
                + "JOIN sales_order o ON o.id = b.order_id WHERE o.status = 'CONFIRMED' AND b.back_order_date <= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt("back_order_count");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot count back orders", e);
        }
    }

    public List<BackOrder> listBackOrdersByCustomer(long customerId) {
        String sql = "SELECT b.id, b.order_id, o.number, o.customer_id, b.back_order_date FROM back_order b "
                + "JOIN sales_order o ON o.id = b.order_id WHERE o.customer_id = ? AND o.status = 'CONFIRMED' "
                + "ORDER BY b.back_order_date, b.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<BackOrder> backOrders = new ArrayList<>();
                while (rs.next()) {
                    backOrders.add(new BackOrder(rs.getLong("id"), rs.getLong("order_id"), rs.getString("number"),
                            rs.getLong("customer_id"), rs.getObject("back_order_date", LocalDate.class)));
                }
                return backOrders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list back orders by customer", e);
        }
    }

    public List<BackOrder> listBackOrdersByProduct(long productId) {
        String sql = "SELECT b.id, b.order_id, o.number, o.customer_id, b.back_order_date FROM back_order b "
                + "JOIN sales_order o ON o.id = b.order_id WHERE o.status = 'CONFIRMED' AND EXISTS "
                + "(SELECT 1 FROM sales_order_line l WHERE l.order_id = o.id AND l.product_id = ?) "
                + "ORDER BY b.back_order_date, b.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            try (ResultSet rs = statement.executeQuery()) {
                List<BackOrder> backOrders = new ArrayList<>();
                while (rs.next()) {
                    backOrders.add(new BackOrder(rs.getLong("id"), rs.getLong("order_id"), rs.getString("number"),
                            rs.getLong("customer_id"), rs.getObject("back_order_date", LocalDate.class)));
                }
                return backOrders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list back orders by product", e);
        }
    }

    /**
     * Per product category, the number of waiting back orders (order still confirmed) with a line in the category and the
     * units ordered on those lines. Sorted by category; categories without back orders are left out.
     */
    public List<CategoryBackOrders> findBackOrdersPerCategory() {
        String sql = "SELECT p.category AS category, COUNT(DISTINCT b.id) AS back_order_count, SUM(l.quantity) AS units "
                + "FROM back_order b JOIN sales_order o ON o.id = b.order_id "
                + "JOIN sales_order_line l ON l.order_id = o.id JOIN product p ON p.id = l.product_id "
                + "WHERE o.status = 'CONFIRMED' GROUP BY p.category ORDER BY p.category";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<CategoryBackOrders> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new CategoryBackOrders(rs.getString("category"), rs.getInt("back_order_count"), rs.getInt("units")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find back orders per category", e);
        }
    }

    /**
     * Per promotion, the number of lines of non-cancelled orders dated in the period it was applied to, the discount given
     * (list price less the price charged, derived from the promotion percentage; the catalogue price for a 100% promotion)
     * and the revenue of those lines. Sorted by code; promotions without such lines are left out.
     */
    public List<PromotionUsage> findPromotionUsage(LocalDate from, LocalDate to) {
        String sql = "SELECT pr.code AS code, COUNT(*) AS line_count, "
                + "COALESCE(ROUND(SUM(CASE WHEN pr.discount_percent < 100 "
                + "THEN l.quantity * l.unit_price * pr.discount_percent / (100 - pr.discount_percent) "
                + "ELSE l.quantity * p.unit_price END), 2), 0) AS discount, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN promotion pr ON pr.code = l.promotion_code JOIN product p ON p.id = l.product_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY pr.code ORDER BY pr.code";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<PromotionUsage> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new PromotionUsage(rs.getString("code"), rs.getInt("line_count"), rs.getBigDecimal("discount"),
                            rs.getBigDecimal("revenue")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find promotion usage", e);
        }
    }

    /**
     * The lines of non-cancelled orders of the customer dated in the period (inclusive) that got a promotion, with the discount
     * given on each (list price less the price charged, derived from the promotion percentage; the catalogue price for a 100%
     * promotion). Sorted by order date, order number and line number.
     */
    public List<CustomerPromotionLine> findCustomerPromotionLines(long customerId, LocalDate from, LocalDate to) {
        String sql = "SELECT o.number AS number, o.order_date AS order_date, l.line_no AS line_no, p.sku AS sku, "
                + "l.quantity AS quantity, l.unit_price AS unit_price, pr.code AS code, "
                + "ROUND(CASE WHEN pr.discount_percent < 100 "
                + "THEN l.quantity * l.unit_price * pr.discount_percent / (100 - pr.discount_percent) "
                + "ELSE l.quantity * p.unit_price END, 2) AS discount "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN promotion pr ON pr.code = l.promotion_code JOIN product p ON p.id = l.product_id "
                + "WHERE o.customer_id = ? AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "ORDER BY o.order_date, o.number, o.id, l.line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerPromotionLine> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new CustomerPromotionLine(rs.getString("number"), rs.getObject("order_date", LocalDate.class),
                            rs.getInt("line_no"), rs.getString("sku"), rs.getInt("quantity"), rs.getBigDecimal("unit_price"),
                            rs.getString("code"), rs.getBigDecimal("discount")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer promotion lines", e);
        }
    }

    /**
     * The promotions whose period overlaps the given period and that were applied to no line of a non-cancelled order dated
     * in the period. Sorted by code.
     */
    public List<Promotion> listUnusedPromotions(LocalDate from, LocalDate to) {
        String sql = "SELECT id, code, discount_percent, start_date, end_date, category FROM promotion pr "
                + "WHERE pr.start_date <= ? AND pr.end_date >= ? AND NOT EXISTS (SELECT 1 FROM sales_order o "
                + "JOIN sales_order_line l ON l.order_id = o.id WHERE l.promotion_code = pr.code "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') ORDER BY pr.code, pr.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, to);
            statement.setObject(2, from);
            statement.setObject(3, from);
            statement.setObject(4, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<Promotion> promotions = new ArrayList<>();
                while (rs.next()) {
                    promotions.add(new Promotion(rs.getLong("id"), rs.getString("code"), rs.getBigDecimal("discount_percent"),
                            rs.getObject("start_date", LocalDate.class), rs.getObject("end_date", LocalDate.class),
                            rs.getString("category")));
                }
                return promotions;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list unused promotions", e);
        }
    }

    /**
     * Per customer, the lines of non-cancelled orders dated in the period that were sold at a special price (the customer
     * had a price for the product valid on the order date): the line count, the revenue at the special prices and the revenue
     * the lines would have had at catalogue prices. Sorted by customer name, then id; customers without such lines are left out.
     */
    public List<PriceListUsage> findPriceListUsage(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS customer_id, c.name AS name, COUNT(*) AS line_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS special_revenue, "
                + "COALESCE(SUM(l.quantity * p.unit_price), 0) AS catalogue_revenue "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id JOIN customer c ON c.id = o.customer_id "
                + "JOIN product p ON p.id = l.product_id "
                + "JOIN customer_price cp ON cp.customer_id = o.customer_id AND cp.product_id = l.product_id "
                + "AND cp.start_date <= o.order_date AND cp.end_date >= o.order_date "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY c.id, c.name ORDER BY c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<PriceListUsage> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new PriceListUsage(rs.getLong("customer_id"), rs.getString("name"), rs.getInt("line_count"),
                            rs.getBigDecimal("special_revenue"), rs.getBigDecimal("catalogue_revenue")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find price list usage", e);
        }
    }

    public void createShipmentPart(long orderId, int lineNo, long warehouseId, long carrierId, String trackingNumber,
                                   LocalDate shipDate, int quantity) {
        String sql = "INSERT INTO shipment_part (order_id, line_no, warehouse_id, carrier_id, tracking_number, ship_date, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            statement.setInt(2, lineNo);
            statement.setLong(3, warehouseId);
            statement.setLong(4, carrierId);
            statement.setString(5, trackingNumber);
            statement.setObject(6, shipDate);
            statement.setInt(7, quantity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create shipment part", e);
        }
    }

    public List<RemainingLine> listRemainingLines(long orderId) {
        String sql = "SELECT l.line_no, l.product_id, l.quantity, "
                + "COALESCE((SELECT SUM(p.quantity) FROM shipment_part p WHERE p.order_id = l.order_id AND p.line_no = l.line_no), 0) AS shipped "
                + "FROM sales_order_line l WHERE l.order_id = ? ORDER BY l.line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                List<RemainingLine> lines = new ArrayList<>();
                while (rs.next()) {
                    int ordered = rs.getInt("quantity");
                    int shipped = rs.getInt("shipped");
                    if (shipped < ordered) {
                        lines.add(new RemainingLine(rs.getInt("line_no"), rs.getLong("product_id"), ordered, shipped, ordered - shipped));
                    }
                }
                return lines;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list remaining lines", e);
        }
    }

    /** Sets the exchange rate (units of the currency per 1 EUR) valid from the date on, replacing a rate set for the same date. */
    public ExchangeRate saveExchangeRate(ExchangeRate rate) {
        String sql = "MERGE INTO exchange_rate (currency, rate_date, per_eur) KEY (currency, rate_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, rate.currency().name());
            statement.setObject(2, rate.rateDate());
            statement.setBigDecimal(3, rate.perEur());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save exchange rate", e);
        }
        String idSql = "SELECT id FROM exchange_rate WHERE currency = ? AND rate_date = ?";
        try (PreparedStatement statement = connection.prepareStatement(idSql)) {
            statement.setString(1, rate.currency().name());
            statement.setObject(2, rate.rateDate());
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return new ExchangeRate(rs.getLong("id"), rate.currency(), rate.rateDate(), rate.perEur());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save exchange rate", e);
        }
    }

    /** The rate of the currency that applies on the date: the latest one set on or before it, if any. */
    public Optional<ExchangeRate> findExchangeRate(Currency currency, LocalDate asOf) {
        String sql = "SELECT id, currency, rate_date, per_eur FROM exchange_rate "
                + "WHERE currency = ? AND rate_date <= ? ORDER BY rate_date DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, currency.name());
            statement.setObject(2, asOf);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new ExchangeRate(rs.getLong("id"), Currency.valueOf(rs.getString("currency")),
                        rs.getObject("rate_date", LocalDate.class), rs.getBigDecimal("per_eur")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find exchange rate", e);
        }
    }

    public void saveCustomerCurrency(long customerId, Currency currency) {
        String sql = "MERGE INTO customer_currency (customer_id, currency) KEY (customer_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setString(2, currency.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save customer currency", e);
        }
    }

    /** The currency the customer is invoiced in, if one was set (callers use EUR otherwise). */
    public Optional<Currency> findCustomerCurrency(long customerId) {
        String sql = "SELECT currency FROM customer_currency WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(Currency.valueOf(rs.getString("currency")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer currency", e);
        }
    }

    public InvoiceCurrency createInvoiceCurrency(InvoiceCurrency invoiceCurrency) {
        String sql = "INSERT INTO invoice_currency (invoice_id, currency, amount) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, invoiceCurrency.invoiceId());
            statement.setString(2, invoiceCurrency.currency().name());
            statement.setBigDecimal(3, invoiceCurrency.amount());
            statement.executeUpdate();
            return invoiceCurrency;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create invoice currency", e);
        }
    }

    /** The currency and amount an invoice was issued in, if recorded (an invoice without one is in EUR). */
    public Optional<InvoiceCurrency> findInvoiceCurrency(long invoiceId) {
        String sql = "SELECT invoice_id, currency, amount FROM invoice_currency WHERE invoice_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, invoiceId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new InvoiceCurrency(rs.getLong("invoice_id"), Currency.valueOf(rs.getString("currency")),
                        rs.getBigDecimal("amount")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoice currency", e);
        }
    }

    /** Sets the VAT rate of the country valid from the date on, replacing a rate set for the same date. */
    public TaxRate saveTaxRate(TaxRate rate) {
        String sql = "MERGE INTO tax_rate (country, valid_from, rate_percent) KEY (country, valid_from) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, rate.country());
            statement.setObject(2, rate.validFrom());
            statement.setBigDecimal(3, rate.ratePercent());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save tax rate", e);
        }
        String idSql = "SELECT id FROM tax_rate WHERE country = ? AND valid_from = ?";
        try (PreparedStatement statement = connection.prepareStatement(idSql)) {
            statement.setString(1, rate.country());
            statement.setObject(2, rate.validFrom());
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return new TaxRate(rs.getLong("id"), rate.country(), rate.validFrom(), rate.ratePercent());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save tax rate", e);
        }
    }

    /** The VAT rate of the country that applies on the date: the latest one valid on or before it, if any. */
    public Optional<TaxRate> findTaxRate(String country, LocalDate asOf) {
        String sql = "SELECT id, country, valid_from, rate_percent FROM tax_rate "
                + "WHERE country = ? AND valid_from <= ? ORDER BY valid_from DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, country);
            statement.setObject(2, asOf);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new TaxRate(rs.getLong("id"), rs.getString("country"),
                        rs.getObject("valid_from", LocalDate.class), rs.getBigDecimal("rate_percent")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find tax rate", e);
        }
    }

    public void setOrderLineDiscount(long orderId, int lineNo, BigDecimal percent) {
        String sql = "MERGE INTO order_line_discount (order_id, line_no, discount_percent) KEY (order_id, line_no) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            statement.setInt(2, lineNo);
            statement.setBigDecimal(3, percent);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot set order line discount", e);
        }
    }

    /** True when a line of the order has a manual discount above the given percentage. */
    public boolean hasOrderLineDiscountAbove(long orderId, BigDecimal percent) {
        String sql = "SELECT COUNT(*) FROM order_line_discount WHERE order_id = ? AND discount_percent > ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            statement.setBigDecimal(2, percent);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getLong(1) > 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot check order line discounts", e);
        }
    }

    /** The draft orders with a line discount above the percentage and no approval decision yet, oldest first. */
    public List<SalesOrder> listSalesOrdersAwaitingApproval(BigDecimal percent) {
        String sql = "SELECT id, number, customer_id, order_date, status FROM sales_order o WHERE o.status = 'DRAFT' "
                + "AND EXISTS (SELECT 1 FROM order_line_discount d WHERE d.order_id = o.id AND d.discount_percent > ?) "
                + "AND NOT EXISTS (SELECT 1 FROM order_approval a WHERE a.order_id = o.id) "
                + "ORDER BY order_date, id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, percent);
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
            throw new IllegalStateException("Cannot list sales orders awaiting approval", e);
        }
    }

    /** Number of draft orders placed on or before the date with a line discount above the percentage and no approval decision. */
    public int countSalesOrdersAwaitingApprovalOnDate(BigDecimal percent, LocalDate date) {
        String sql = "SELECT COUNT(*) AS order_count FROM sales_order o WHERE o.status = 'DRAFT' AND o.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM order_line_discount d WHERE d.order_id = o.id AND d.discount_percent > ?) "
                + "AND NOT EXISTS (SELECT 1 FROM order_approval a WHERE a.order_id = o.id)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            statement.setBigDecimal(2, percent);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt("order_count");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot count sales orders awaiting approval", e);
        }
    }

    public OrderApproval saveOrderApproval(OrderApproval approval) {
        String sql = "INSERT INTO order_approval (order_id, approved, approver, decision_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, approval.orderId());
            statement.setBoolean(2, approval.approved());
            statement.setString(3, approval.approver());
            statement.setObject(4, approval.date());
            statement.executeUpdate();
            return approval;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save order approval", e);
        }
    }

    public Optional<OrderApproval> findOrderApproval(long orderId) {
        String sql = "SELECT order_id, approved, approver, decision_date FROM order_approval WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new OrderApproval(rs.getLong("order_id"), rs.getBoolean("approved"),
                        rs.getString("approver"), rs.getObject("decision_date", LocalDate.class)));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find order approval", e);
        }
    }

    public void deleteOrderApproval(long orderId) {
        String sql = "DELETE FROM order_approval WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot delete order approval", e);
        }
    }

    public SupportTicket createSupportTicket(SupportTicket ticket) {
        String sql = "INSERT INTO support_ticket (number, customer_id, order_id, subject, priority, status, opened_date, closed_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ticket.number());
            statement.setLong(2, ticket.customerId());
            statement.setObject(3, ticket.orderId());
            statement.setString(4, ticket.subject());
            statement.setString(5, ticket.priority().name());
            statement.setString(6, ticket.status().name());
            statement.setObject(7, ticket.openedDate());
            statement.setObject(8, ticket.closedDate());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new SupportTicket(keys.getLong(1), ticket.number(), ticket.customerId(), ticket.orderId(), ticket.subject(),
                        ticket.priority(), ticket.status(), ticket.openedDate(), ticket.closedDate());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create support ticket", e);
        }
    }

    public Optional<SupportTicket> findSupportTicketByNumber(String number) {
        String sql = "SELECT id, number, customer_id, order_id, subject, priority, status, opened_date, closed_date FROM support_ticket WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_id", Long.class), rs.getString("subject"),
                            TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                            rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class)));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find support ticket", e);
        }
    }

    public void updateSupportTicketStatus(long ticketId, TicketStatus status, LocalDate closedDate) {
        String sql = "UPDATE support_ticket SET status = ?, closed_date = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setObject(2, closedDate);
            statement.setLong(3, ticketId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update support ticket status", e);
        }
    }

    public List<SupportTicket> listOpenSupportTicketsByCustomer(long customerId) {
        String sql = "SELECT id, number, customer_id, order_id, subject, priority, status, opened_date, closed_date FROM support_ticket WHERE customer_id = ? AND status <> 'CLOSED' ORDER BY opened_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupportTicket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_id", Long.class), rs.getString("subject"),
                            TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                            rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class)));
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list open support tickets by customer", e);
        }
    }

    public List<SupportTicket> listOpenSupportTicketsByPriority(TicketPriority priority) {
        String sql = "SELECT id, number, customer_id, order_id, subject, priority, status, opened_date, closed_date FROM support_ticket WHERE priority = ? AND status <> 'CLOSED' ORDER BY opened_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, priority.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<SupportTicket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_id", Long.class), rs.getString("subject"),
                            TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                            rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class)));
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list open support tickets by priority", e);
        }
    }

    /** Tickets of customers in the region that are not closed, oldest first. */
    public List<SupportTicket> listOpenSupportTicketsByRegion(Region region) {
        String sql = "SELECT t.id, t.number, t.customer_id, t.order_id, t.subject, t.priority, t.status, t.opened_date, t.closed_date FROM support_ticket t "
                + "JOIN customer c ON c.id = t.customer_id WHERE c.region = ? AND t.status <> 'CLOSED' ORDER BY t.opened_date, t.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<SupportTicket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_id", Long.class), rs.getString("subject"),
                            TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                            rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class)));
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list open support tickets by region", e);
        }
    }

    /** Tickets of customers of the sales rep that are not closed, highest priority first, oldest first within a priority. */
    public List<SupportTicket> listOpenSupportTicketsBySalesRep(long salesRepId) {
        String sql = "SELECT t.id, t.number, t.customer_id, t.order_id, t.subject, t.priority, t.status, t.opened_date, t.closed_date FROM support_ticket t "
                + "JOIN customer c ON c.id = t.customer_id WHERE c.sales_rep_id = ? AND t.status <> 'CLOSED' "
                + "ORDER BY CASE t.priority WHEN 'HIGH' THEN 0 WHEN 'NORMAL' THEN 1 ELSE 2 END, t.opened_date, t.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupportTicket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_id", Long.class), rs.getString("subject"),
                            TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                            rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class)));
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list open support tickets by sales rep", e);
        }
    }

    /** Tickets of the customer that refer to an order, whatever their status, oldest first. */
    public List<SupportTicket> listSupportTicketsWithOrderByCustomer(long customerId) {
        String sql = "SELECT id, number, customer_id, order_id, subject, priority, status, opened_date, closed_date FROM support_ticket WHERE customer_id = ? AND order_id IS NOT NULL ORDER BY opened_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupportTicket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_id", Long.class), rs.getString("subject"),
                            TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                            rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class)));
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list support tickets with order by customer", e);
        }
    }

    /** Orders with more than one ticket and their number of tickets, most tickets first, then by order number. */
    public List<OrderTickets> findOrdersWithMultipleSupportTickets() {
        String sql = "SELECT o.id, o.number, o.customer_id, COUNT(*) AS ticket_count FROM support_ticket t JOIN sales_order o ON o.id = t.order_id "
                + "GROUP BY o.id, o.number, o.customer_id HAVING COUNT(*) > 1 ORDER BY ticket_count DESC, o.number";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderTickets> orders = new ArrayList<>();
                while (rs.next()) {
                    orders.add(new OrderTickets(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"), rs.getInt("ticket_count")));
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find orders with multiple support tickets", e);
        }
    }

    /** Per priority, the number of not closed tickets of customers in the region; priorities without such tickets are left out. */
    public List<PriorityTickets> findOpenSupportTicketCountsPerPriorityByRegion(Region region) {
        String sql = "SELECT t.priority, COUNT(*) AS ticket_count FROM support_ticket t JOIN customer c ON c.id = t.customer_id "
                + "WHERE c.region = ? AND t.status <> 'CLOSED' GROUP BY t.priority ORDER BY t.priority";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<PriorityTickets> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new PriorityTickets(TicketPriority.valueOf(rs.getString("priority")), rs.getInt("ticket_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot count open support tickets per priority by region", e);
        }
    }

    /** High-priority tickets of customers in the region that were open on the date and opened more than the given number of days before it, oldest first. */
    public List<SupportTicket> listHighPrioritySupportTicketsByRegionOlderThan(Region region, LocalDate date, int days) {
        String sql = "SELECT t.id, t.number, t.customer_id, t.order_id, t.subject, t.priority, t.status, t.opened_date, t.closed_date FROM support_ticket t "
                + "JOIN customer c ON c.id = t.customer_id WHERE c.region = ? AND t.priority = 'HIGH' AND t.opened_date < ? "
                + "AND (t.closed_date IS NULL OR t.closed_date > ?) ORDER BY t.opened_date, t.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setObject(2, date.minusDays(days));
            statement.setObject(3, date);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupportTicket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_id", Long.class), rs.getString("subject"),
                            TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                            rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class)));
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list old high-priority support tickets by region", e);
        }
    }

    /** Tickets that were open on the date (opened on or before it, not closed by then) and opened before the cutoff date, oldest first. */
    public List<SupportTicket> listSupportTicketsOpenOnDateOpenedBefore(LocalDate date, LocalDate cutoff) {
        String sql = "SELECT id, number, customer_id, order_id, subject, priority, status, opened_date, closed_date FROM support_ticket WHERE opened_date <= ? AND opened_date < ? AND (closed_date IS NULL OR closed_date > ?) ORDER BY opened_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            statement.setObject(2, cutoff);
            statement.setObject(3, date);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupportTicket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_id", Long.class), rs.getString("subject"),
                            TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                            rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class)));
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list support tickets open on date", e);
        }
    }

    /** Number of tickets that were open on the date: opened on or before it and not closed by then. */
    public int countSupportTicketsOpenOnDate(LocalDate date) {
        String sql = "SELECT COUNT(*) AS ticket_count FROM support_ticket WHERE opened_date <= ? AND (closed_date IS NULL OR closed_date > ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            statement.setObject(2, date);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt("ticket_count");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot count support tickets open on date", e);
        }
    }

    /** Number of tickets per current status; statuses without tickets are left out. */
    public List<TicketStatusCount> findSupportTicketCountsPerStatus() {
        String sql = "SELECT status, COUNT(*) AS ticket_count FROM support_ticket GROUP BY status ORDER BY status";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<TicketStatusCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new TicketStatusCount(TicketStatus.valueOf(rs.getString("status")), rs.getInt("ticket_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot count support tickets per status", e);
        }
    }

    /**
     * Per priority, the number of tickets closed between the two dates (inclusive) and the average days from opening to
     * closing, rounded to 2 decimals; priorities without such tickets are left out.
     */
    public List<TicketCloseTime> findSupportTicketCloseTimesPerPriority(LocalDate from, LocalDate to) {
        String sql = "SELECT priority, COUNT(*) AS closed_count, AVG(CAST(DATEDIFF('DAY', opened_date, closed_date) AS DECIMAL(12, 4))) AS average_days "
                + "FROM support_ticket WHERE status = 'CLOSED' AND closed_date >= ? AND closed_date <= ? GROUP BY priority ORDER BY priority";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<TicketCloseTime> times = new ArrayList<>();
                while (rs.next()) {
                    times.add(new TicketCloseTime(TicketPriority.valueOf(rs.getString("priority")), rs.getInt("closed_count"),
                            rs.getBigDecimal("average_days").setScale(2, RoundingMode.HALF_UP)));
                }
                return times;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find support ticket close times per priority", e);
        }
    }

    /** Tickets closed between the two dates (inclusive) with the days to close, by customer name, then priority, then closing date. */
    public List<ClosedTicket> listClosedSupportTickets(LocalDate from, LocalDate to) {
        String sql = "SELECT t.number, t.customer_id, c.name, t.priority, t.opened_date, t.closed_date, DATEDIFF('DAY', t.opened_date, t.closed_date) AS days_to_close "
                + "FROM support_ticket t JOIN customer c ON c.id = t.customer_id "
                + "WHERE t.status = 'CLOSED' AND t.closed_date >= ? AND t.closed_date <= ? ORDER BY c.name, t.customer_id, t.priority, t.closed_date, t.number";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ClosedTicket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(new ClosedTicket(rs.getString("number"), rs.getLong("customer_id"), rs.getString("name"),
                            TicketPriority.valueOf(rs.getString("priority")), rs.getObject("opened_date", LocalDate.class),
                            rs.getObject("closed_date", LocalDate.class), rs.getInt("days_to_close")));
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list closed support tickets", e);
        }
    }

    /** Names the product a ticket is about. */
    public void updateSupportTicketProduct(long ticketId, Long productId) {
        String sql = "UPDATE support_ticket SET product_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, productId);
            statement.setLong(2, ticketId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update support ticket product", e);
        }
    }

    /** Tickets about the product that are not closed, oldest first. */
    public List<SupportTicket> listOpenSupportTicketsByProduct(long productId) {
        String sql = "SELECT id, number, customer_id, order_id, subject, priority, status, opened_date, closed_date FROM support_ticket WHERE product_id = ? AND status <> 'CLOSED' ORDER BY opened_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupportTicket> tickets = new ArrayList<>();
                while (rs.next()) {
                    tickets.add(new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getObject("order_id", Long.class), rs.getString("subject"),
                            TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                            rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class)));
                }
                return tickets;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list open support tickets by product", e);
        }
    }

    /** Per product, the number of tickets opened between the two dates (inclusive); products without such tickets are left out. */
    public List<ProductTickets> findSupportTicketCountsPerProduct(LocalDate from, LocalDate to) {
        String sql = "SELECT p.id, p.sku, p.name, COUNT(*) AS ticket_count FROM support_ticket t JOIN product p ON p.id = t.product_id "
                + "WHERE t.opened_date >= ? AND t.opened_date <= ? GROUP BY p.id, p.sku, p.name ORDER BY p.sku";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ProductTickets> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new ProductTickets(rs.getLong("id"), rs.getString("sku"), rs.getString("name"), rs.getInt("ticket_count")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot count support tickets per product", e);
        }
    }

    /** Sets the target of the rep for the month starting at the given date, replacing an existing one. */
    public void saveSalesTarget(long salesRepId, LocalDate month, BigDecimal target) {
        String sql = "MERGE INTO sales_target (sales_rep_id, target_month, target_amount) KEY (sales_rep_id, target_month) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
            statement.setObject(2, month);
            statement.setBigDecimal(3, target);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save sales target", e);
        }
    }

    /**
     * For each rep with a target for the month starting at the first date: the target, the revenue of the
     * non-cancelled orders of the rep's customers placed between the two dates, inclusive, and the percentage reached
     * (rounded to 2 decimals). Sorted by rep name, then id.
     */
    public List<SalesTargetProgress> findSalesTargetProgress(LocalDate monthStart, LocalDate monthEnd) {
        String sql = "SELECT r.id AS id, r.name AS name, t.target_amount AS target, "
                + "COALESCE((SELECT SUM(l.quantity * l.unit_price) FROM sales_order o "
                + "JOIN customer c ON c.id = o.customer_id JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE c.sales_rep_id = r.id AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED'), 0) AS revenue "
                + "FROM sales_rep r JOIN sales_target t ON t.sales_rep_id = r.id AND t.target_month = ? "
                + "ORDER BY r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, monthStart);
            statement.setObject(2, monthEnd);
            statement.setObject(3, monthStart);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesTargetProgress> progress = new ArrayList<>();
                while (rs.next()) {
                    BigDecimal target = rs.getBigDecimal("target");
                    BigDecimal revenue = rs.getBigDecimal("revenue");
                    progress.add(new SalesTargetProgress(rs.getLong("id"), rs.getString("name"), target, revenue,
                            revenue.multiply(new BigDecimal("100")).divide(target, 2, RoundingMode.HALF_UP)));
                }
                return progress;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales target progress", e);
        }
    }

    /**
     * The total of the paid invoices of the rep's customers dated between the two dates, inclusive, and the rep's
     * target for the month starting at the first date (null if none). Empty if the rep does not exist.
     */
    public Optional<CommissionBasis> findCommissionBasis(long salesRepId, LocalDate monthStart, LocalDate monthEnd) {
        String sql = "SELECT r.id AS id, r.name AS name, "
                + "COALESCE((SELECT SUM(i.amount) FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE c.sales_rep_id = r.id AND i.status = 'PAID' AND i.invoice_date >= ? AND i.invoice_date <= ?), 0) AS paid, "
                + "t.target_amount AS target "
                + "FROM sales_rep r LEFT JOIN sales_target t ON t.sales_rep_id = r.id AND t.target_month = ? "
                + "WHERE r.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, monthStart);
            statement.setObject(2, monthEnd);
            statement.setObject(3, monthStart);
            statement.setLong(4, salesRepId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new CommissionBasis(rs.getLong("id"), rs.getString("name"),
                        rs.getBigDecimal("paid"), rs.getBigDecimal("target")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find commission basis", e);
        }
    }

    /** Like findCommissionBasis, for every rep, sorted by rep name, then id. */
    public List<CommissionBasis> listCommissionBases(LocalDate monthStart, LocalDate monthEnd) {
        String sql = "SELECT r.id AS id, r.name AS name, "
                + "COALESCE((SELECT SUM(i.amount) FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE c.sales_rep_id = r.id AND i.status = 'PAID' AND i.invoice_date >= ? AND i.invoice_date <= ?), 0) AS paid, "
                + "t.target_amount AS target "
                + "FROM sales_rep r LEFT JOIN sales_target t ON t.sales_rep_id = r.id AND t.target_month = ? "
                + "ORDER BY r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, monthStart);
            statement.setObject(2, monthEnd);
            statement.setObject(3, monthStart);
            try (ResultSet rs = statement.executeQuery()) {
                List<CommissionBasis> bases = new ArrayList<>();
                while (rs.next()) {
                    bases.add(new CommissionBasis(rs.getLong("id"), rs.getString("name"),
                            rs.getBigDecimal("paid"), rs.getBigDecimal("target")));
                }
                return bases;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list commission bases", e);
        }
    }
}
