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

class DataStorePart08 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart08(DataStore store, java.sql.Connection connection) {
        this.store = store;
        this.connection = connection;
    }

    /**
     * The customers of the region with the highest revenue, as for the sales per customer, at most the given number of
     * them.
     */
    public List<CustomerSales> findTopCustomersInRegion(Region region, int limit, LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' AND c.region = ? "
                + "GROUP BY c.id, c.name ORDER BY revenue DESC, c.name, c.id LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setString(3, region.name());
            statement.setInt(4, limit);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerSales> sales = new ArrayList<>();
                while (rs.next()) {
                    sales.add(new CustomerSales(rs.getLong("id"), rs.getString("name"), rs.getInt("order_count"),
                            rs.getBigDecimal("revenue")));
                }
                return sales;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find top customers in region", e);
        }
    }

    /**
     * The customers with at least the given number of non-cancelled orders placed between the two dates, inclusive,
     * with their order counts. Grouped per region of the customer (regions in alphabetical order), most orders first
     * within a region (then by customer name, then id).
     */
    public List<RepeatCustomer> findRepeatCustomersByRegion(int minOrders, LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, COUNT(DISTINCT o.id) AS order_count "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY c.id, c.name, c.region HAVING COUNT(DISTINCT o.id) >= ? "
                + "ORDER BY c.region, order_count DESC, c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setInt(3, minOrders);
            try (ResultSet rs = statement.executeQuery()) {
                List<RepeatCustomer> customers = new ArrayList<>();
                while (rs.next()) {
                    customers.add(new RepeatCustomer(Region.valueOf(rs.getString("region")), rs.getLong("id"),
                            rs.getString("name"), rs.getInt("order_count")));
                }
                return customers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find repeat customers by region", e);
        }
    }

    /**
     * The customers whose first non-cancelled order (over all time) was placed between the two dates, inclusive, with
     * the date of that order and the customer's sales rep (null when none). Grouped per region of the customer
     * (alphabetical), then per sales rep name (customers without a rep last), then first order date, customer name, id.
     */
    public List<FirstTimeCustomer> findFirstTimeCustomers(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, MIN(o.order_date) AS first_order, "
                + "r.id AS rep_id, r.name AS rep_name, r.email AS rep_email, r.region AS rep_region "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id LEFT JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE o.status <> 'CANCELLED' "
                + "GROUP BY c.id, c.name, c.region, r.id, r.name, r.email, r.region "
                + "HAVING MIN(o.order_date) >= ? AND MIN(o.order_date) <= ? "
                + "ORDER BY c.region, r.name NULLS LAST, r.id, first_order, c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<FirstTimeCustomer> customers = new ArrayList<>();
                while (rs.next()) {
                    SalesRep salesRep = null;
                    if (rs.getObject("rep_id") != null) {
                        salesRep = new SalesRep(rs.getLong("rep_id"), rs.getString("rep_name"), rs.getString("rep_email"),
                                Region.valueOf(rs.getString("rep_region")));
                    }
                    customers.add(new FirstTimeCustomer(Region.valueOf(rs.getString("region")), salesRep, rs.getLong("id"),
                            rs.getString("name"), rs.getObject("first_order", LocalDate.class)));
                }
                return customers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find first-time customers", e);
        }
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, for every customer,
     * as for the sales per customer, but customers without orders are included with revenue 0. Sorted by revenue,
     * highest first (then by customer name, then id).
     */
    public List<CustomerSales> findRevenuePerCustomerIncludingIdle(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM customer c LEFT JOIN sales_order o ON o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
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
            throw new IllegalStateException("Cannot find revenue per customer", e);
        }
    }

    /**
     * The customers of the sales rep that are not closed, with their revenue from non-cancelled orders placed between
     * the two dates (inclusive, 0 if none), the date of their last non-cancelled order (null if none) and the unpaid
     * part of their open invoices (0 if none). Sorted by customer name, then id.
     */
    public List<SalesRepCustomer> findCustomerReportBySalesRep(long salesRepId, LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o "
                + "JOIN sales_order_line l ON l.order_id = o.id WHERE o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') AS revenue, "
                + "(SELECT MAX(o.order_date) FROM sales_order o WHERE o.customer_id = c.id AND o.status <> 'CANCELLED') AS last_order, "
                + "(SELECT COALESCE(SUM(i.amount - (SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id)), 0) FROM invoice i WHERE i.customer_id = c.id AND i.status = 'OPEN') AS open_amount "
                + "FROM customer c WHERE c.sales_rep_id = ? "
                + "AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "ORDER BY c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setLong(3, salesRepId);
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesRepCustomer> customers = new ArrayList<>();
                while (rs.next()) {
                    customers.add(new SalesRepCustomer(rs.getLong("id"), rs.getString("name"),
                            rs.getBigDecimal("revenue"), rs.getObject("last_order", LocalDate.class),
                            rs.getBigDecimal("open_amount")));
                }
                return customers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer report of sales rep", e);
        }
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, for every customer
     * that is not closed, with the region of the customer; customers without orders are included with revenue 0.
     * Sorted by revenue, highest first (then by customer name, then id).
     */
    public List<CustomerRegionSales> findRevenuePerCustomerWithRegionIncludingIdle(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM customer c LEFT JOIN sales_order o ON o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "GROUP BY c.id, c.name, c.region ORDER BY revenue DESC, c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerRegionSales> sales = new ArrayList<>();
                while (rs.next()) {
                    sales.add(new CustomerRegionSales(rs.getLong("id"), rs.getString("name"),
                            Region.valueOf(rs.getString("region")), rs.getInt("order_count"), rs.getBigDecimal("revenue")));
                }
                return sales;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find revenue per customer with region", e);
        }
    }

    /**
     * The active customers without a non-cancelled order placed between the two dates, inclusive. Sorted by region,
     * then by name, then id.
     */
    public List<Customer> findActiveCustomersWithoutOrders(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, c.email AS email, c.phone AS phone, c.country AS country, "
                + "c.region AS region, c.status AS status FROM customer c "
                + "WHERE c.status = 'ACTIVE' AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "AND NOT EXISTS (SELECT 1 FROM sales_order o WHERE o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') "
                + "ORDER BY c.region, c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
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
            throw new IllegalStateException("Cannot find dormant customers", e);
        }
    }

    /**
     * The active customers with a non-cancelled order placed between the two dates, inclusive, but without a
     * non-cancelled invoice dated between the two dates, inclusive. Sorted by region, then by name, then id.
     */
    public List<Customer> findActiveCustomersWithoutInvoices(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id AS id, c.name AS name, c.email AS email, c.phone AS phone, c.country AS country, "
                + "c.region AS region, c.status AS status FROM customer c "
                + "WHERE c.status = 'ACTIVE' AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "AND EXISTS (SELECT 1 FROM sales_order o WHERE o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') "
                + "AND NOT EXISTS (SELECT 1 FROM invoice i WHERE i.customer_id = c.id "
                + "AND i.invoice_date >= ? AND i.invoice_date <= ? AND i.status <> 'CANCELLED') "
                + "ORDER BY c.region, c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setObject(3, from);
            statement.setObject(4, to);
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
            throw new IllegalStateException("Cannot find customers without invoices", e);
        }
    }

    /**
     * First and last order date, number and total value of the non-cancelled orders of the customer, the number of
     * its returns, and the sum of its payments and credit notes. Dates are null and the rest zero if there is nothing.
     */
    public CustomerActivity findCustomerActivity(long customerId) {
        String sql = "SELECT "
                + "(SELECT MIN(order_date) FROM sales_order WHERE customer_id = ? AND status <> 'CANCELLED') AS first_order, "
                + "(SELECT MAX(order_date) FROM sales_order WHERE customer_id = ? AND status <> 'CANCELLED') AS last_order, "
                + "(SELECT COUNT(*) FROM sales_order WHERE customer_id = ? AND status <> 'CANCELLED') AS order_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id WHERE o.customer_id = ?) AS return_count, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.customer_id = ? AND o.status <> 'CANCELLED') AS ordered, "
                + "(SELECT COALESCE(SUM(p.amount), 0) FROM payment p JOIN invoice i ON i.id = p.invoice_id WHERE i.customer_id = ?) AS paid, "
                + "(SELECT COALESCE(SUM(amount), 0) FROM credit_note WHERE customer_id = ?) AS credited";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 7; i++) {
                statement.setLong(i, customerId);
            }
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return new CustomerActivity(rs.getObject("first_order", LocalDate.class),
                        rs.getObject("last_order", LocalDate.class), rs.getInt("order_count"), rs.getInt("return_count"),
                        rs.getBigDecimal("ordered"), rs.getBigDecimal("paid"), rs.getBigDecimal("credited"));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer activity", e);
        }
    }

    public CustomerAddress createCustomerAddress(CustomerAddress address) {
        String sql = "INSERT INTO customer_address (customer_id, type, street, city, postal_code, country, default_shipping) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, address.customerId());
            statement.setString(2, address.type().name());
            statement.setString(3, address.street());
            statement.setString(4, address.city());
            statement.setString(5, address.postalCode());
            statement.setString(6, address.country());
            statement.setBoolean(7, address.defaultShipping());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new CustomerAddress(keys.getLong(1), address.customerId(), address.type(), address.street(), address.city(),
                        address.postalCode(), address.country(), address.defaultShipping());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create customer address", e);
        }
    }

    public void updateCustomerAddress(CustomerAddress address) {
        String sql = "UPDATE customer_address SET type = ?, street = ?, city = ?, postal_code = ?, country = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, address.type().name());
            statement.setString(2, address.street());
            statement.setString(3, address.city());
            statement.setString(4, address.postalCode());
            statement.setString(5, address.country());
            statement.setLong(6, address.id());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update customer address", e);
        }
    }

    public Optional<CustomerAddress> findCustomerAddressById(long id) {
        String sql = "SELECT id, customer_id, type, street, city, postal_code, country, default_shipping FROM customer_address WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CustomerAddress(rs.getLong("id"), rs.getLong("customer_id"),
                            AddressType.valueOf(rs.getString("type")), rs.getString("street"), rs.getString("city"),
                            rs.getString("postal_code"), rs.getString("country"), rs.getBoolean("default_shipping")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer address", e);
        }
    }

    public List<CustomerAddress> listCustomerAddresses(long customerId) {
        String sql = "SELECT id, customer_id, type, street, city, postal_code, country, default_shipping FROM customer_address WHERE customer_id = ? ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerAddress> addresses = new ArrayList<>();
                while (rs.next()) {
                    addresses.add(new CustomerAddress(rs.getLong("id"), rs.getLong("customer_id"),
                            AddressType.valueOf(rs.getString("type")), rs.getString("street"), rs.getString("city"),
                            rs.getString("postal_code"), rs.getString("country"), rs.getBoolean("default_shipping")));
                }
                return addresses;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list customer addresses", e);
        }
    }

    /** Makes the given address the only default among the shipping addresses of the customer. */
    public void setDefaultShippingAddress(long customerId, long addressId) {
        String sql = "UPDATE customer_address SET default_shipping = (id = ?) WHERE customer_id = ? AND type = 'SHIPPING'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, addressId);
            statement.setLong(2, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot set default shipping address", e);
        }
    }

    public CustomerContact createCustomerContact(CustomerContact contact) {
        String sql = "INSERT INTO customer_contact (customer_id, name, role, email, phone) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, contact.customerId());
            statement.setString(2, contact.name());
            statement.setString(3, contact.role());
            statement.setString(4, contact.email());
            statement.setString(5, contact.phone());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new CustomerContact(keys.getLong(1), contact.customerId(), contact.name(), contact.role(), contact.email(),
                        contact.phone());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create customer contact", e);
        }
    }

    public Optional<CustomerContact> findCustomerContactByEmail(String email) {
        String sql = "SELECT id, customer_id, name, role, email, phone FROM customer_contact WHERE LOWER(email) = LOWER(?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CustomerContact(rs.getLong("id"), rs.getLong("customer_id"), rs.getString("name"),
                            rs.getString("role"), rs.getString("email"), rs.getString("phone")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer contact", e);
        }
    }

    public List<CustomerContact> listCustomerContacts(long customerId) {
        String sql = "SELECT id, customer_id, name, role, email, phone FROM customer_contact WHERE customer_id = ? ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerContact> contacts = new ArrayList<>();
                while (rs.next()) {
                    contacts.add(new CustomerContact(rs.getLong("id"), rs.getLong("customer_id"), rs.getString("name"),
                            rs.getString("role"), rs.getString("email"), rs.getString("phone")));
                }
                return contacts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list customer contacts", e);
        }
    }

    public List<CustomerContact> findCustomerContactsByRole(String role) {
        String sql = "SELECT id, customer_id, name, role, email, phone FROM customer_contact WHERE LOWER(role) = LOWER(?) ORDER BY customer_id, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, role);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerContact> contacts = new ArrayList<>();
                while (rs.next()) {
                    contacts.add(new CustomerContact(rs.getLong("id"), rs.getLong("customer_id"), rs.getString("name"),
                            rs.getString("role"), rs.getString("email"), rs.getString("phone")));
                }
                return contacts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer contacts by role", e);
        }
    }

    /** Contacts of the customers of the region, sorted by role ignoring case, then customer name, customer id and contact id. */
    public List<RegionContact> listRegionContacts(Region region) {
        String sql = "SELECT k.id, k.customer_id, k.name, k.role, k.email, k.phone, c.name AS customer_name "
                + "FROM customer_contact k JOIN customer c ON c.id = k.customer_id WHERE c.region = ? "
                + "ORDER BY LOWER(k.role), c.name, c.id, k.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<RegionContact> contacts = new ArrayList<>();
                while (rs.next()) {
                    contacts.add(new RegionContact(rs.getString("customer_name"),
                            new CustomerContact(rs.getLong("id"), rs.getLong("customer_id"), rs.getString("name"),
                                    rs.getString("role"), rs.getString("email"), rs.getString("phone"))));
                }
                return contacts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list region contacts", e);
        }
    }

    /** Creates the return and adds its quantities to the stock of the warehouse, all or nothing. */
    public SalesReturn createSalesReturn(SalesReturn salesReturn, long warehouseId) {
        String returnSql = "INSERT INTO sales_return (number, order_id, return_date, reason, warehouse_id) VALUES (?, ?, ?, ?, ?)";
        String lineSql = "INSERT INTO sales_return_line (return_id, line_no, product_id, quantity) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)";
        String movementSql = "INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                + "VALUES (?, ?, ?, ?, 'RETURN')";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(returnSql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, salesReturn.number());
                statement.setLong(2, salesReturn.orderId());
                statement.setObject(3, salesReturn.returnDate());
                statement.setString(4, salesReturn.reason());
                statement.setLong(5, warehouseId);
                statement.executeUpdate();
                long id;
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getLong(1);
                }
                try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                    int lineNo = 1;
                    for (ReturnLine line : salesReturn.lines()) {
                        lineStatement.setLong(1, id);
                        lineStatement.setInt(2, lineNo++);
                        lineStatement.setLong(3, line.productId());
                        lineStatement.setInt(4, line.quantity());
                        lineStatement.executeUpdate();
                    }
                }
                for (ReturnLine line : salesReturn.lines()) {
                    try (PreparedStatement update = connection.prepareStatement(updateSql)) {
                        update.setInt(1, line.quantity());
                        update.setLong(2, warehouseId);
                        update.setLong(3, line.productId());
                        if (update.executeUpdate() == 0) {
                            try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
                                insert.setLong(1, warehouseId);
                                insert.setLong(2, line.productId());
                                insert.setInt(3, line.quantity());
                                insert.executeUpdate();
                            }
                        }
                    }
                    try (PreparedStatement movement = connection.prepareStatement(movementSql)) {
                        movement.setLong(1, line.productId());
                        movement.setLong(2, warehouseId);
                        movement.setObject(3, salesReturn.returnDate());
                        movement.setInt(4, line.quantity());
                        movement.executeUpdate();
                    }
                }
                connection.commit();
                return new SalesReturn(id, salesReturn.number(), salesReturn.orderId(), salesReturn.returnDate(),
                        salesReturn.reason(), List.copyOf(salesReturn.lines()));
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException("Cannot create sales return", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    public Optional<SalesReturn> findSalesReturnByNumber(String number) {
        String sql = "SELECT id, number, order_id, return_date, reason FROM sales_return WHERE number = ?";
        String lineSql = "SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
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
                return Optional.of(new SalesReturn(id, rs.getString("number"), rs.getLong("order_id"),
                        rs.getObject("return_date", LocalDate.class), rs.getString("reason"), lines));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales return", e);
        }
    }

    public List<SalesReturn> listSalesReturnsByOrder(long orderId) {
        String sql = "SELECT id, number, order_id, return_date, reason FROM sales_return WHERE order_id = ? "
                + "ORDER BY return_date, id";
        String lineSql = "SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
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
            throw new IllegalStateException("Cannot list sales returns of order", e);
        }
    }

    public List<SalesReturn> listSalesReturnsByCustomer(long customerId) {
        String sql = "SELECT r.id, r.number, r.order_id, r.return_date, r.reason FROM sales_return r "
                + "JOIN sales_order o ON o.id = r.order_id WHERE o.customer_id = ? ORDER BY r.return_date, r.id";
        String lineSql = "SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
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
            throw new IllegalStateException("Cannot list sales returns of customer", e);
        }
    }

    /**
     * The N most recent sales returns, newest first, then by highest id.
     */
    public List<SalesReturn> findLatestReturns(int limit) {
        String sql = "SELECT r.id, r.number, r.order_id, r.return_date, r.reason FROM sales_return r "
                + "ORDER BY r.return_date DESC, r.id DESC LIMIT ?";
        String lineSql = "SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
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
            throw new IllegalStateException("Cannot find latest returns", e);
        }
    }

    /**
     * The N most recent sales returns of customers in the region, newest first, then by highest id.
     */
    public List<SalesReturn> findLatestReturnsByRegion(Region region, int limit) {
        String sql = "SELECT r.id, r.number, r.order_id, r.return_date, r.reason FROM sales_return r "
                + "JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE c.region = ? "
                + "ORDER BY r.return_date DESC, r.id DESC LIMIT ?";
        String lineSql = "SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest returns by region", e);
        }
    }

    /**
     * The N most recent sales returns of the customer, newest first, then by highest id.
     */
    public List<SalesReturn> findLatestReturnsByCustomer(long customerId, int limit) {
        String sql = "SELECT r.id, r.number, r.order_id, r.return_date, r.reason FROM sales_return r "
                + "JOIN sales_order o ON o.id = r.order_id WHERE o.customer_id = ? "
                + "ORDER BY r.return_date DESC, r.id DESC LIMIT ?";
        String lineSql = "SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest returns by customer", e);
        }
    }

    /**
     * The N most recent purchase orders (cancelled purchase orders are left out), newest first, then by highest id.
     */
    public List<PurchaseOrder> findLatestPurchaseOrders(int limit) {
        String sql = "SELECT p.id, p.number, p.supplier_id, p.order_date, p.expected_date, p.status, p.warehouse_id FROM purchase_order p "
                + "WHERE p.status <> 'CANCELLED' "
                + "ORDER BY p.order_date DESC, p.id DESC LIMIT ?";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
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
            throw new IllegalStateException("Cannot find latest purchase orders", e);
        }
    }

    /**
     * The N most recent purchase orders to be delivered to warehouses in the region (cancelled purchase orders are left
     * out), newest first, then by highest id.
     */
    public List<PurchaseOrder> findLatestPurchaseOrdersByRegion(Region region, int limit) {
        String sql = "SELECT p.id, p.number, p.supplier_id, p.order_date, p.expected_date, p.status, p.warehouse_id FROM purchase_order p "
                + "JOIN warehouse w ON w.id = p.warehouse_id WHERE w.region = ? AND p.status <> 'CANCELLED' "
                + "ORDER BY p.order_date DESC, p.id DESC LIMIT ?";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest purchase orders by region", e);
        }
    }

    /**
     * The N most recent purchase orders of the supplier (cancelled purchase orders are left out), newest first, then by
     * highest id.
     */
    public List<PurchaseOrder> findLatestPurchaseOrdersBySupplier(long supplierId, int limit) {
        String sql = "SELECT p.id, p.number, p.supplier_id, p.order_date, p.expected_date, p.status, p.warehouse_id FROM purchase_order p "
                + "WHERE p.supplier_id = ? AND p.status <> 'CANCELLED' "
                + "ORDER BY p.order_date DESC, p.id DESC LIMIT ?";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, supplierId);
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest purchase orders by supplier", e);
        }
    }

    public List<SalesReturn> listSalesReturnsByProduct(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT r.id, r.number, r.order_id, r.return_date, r.reason FROM sales_return r "
                + "WHERE r.return_date >= ? AND r.return_date <= ? "
                + "AND EXISTS (SELECT 1 FROM sales_return_line pl WHERE pl.return_id = r.id AND pl.product_id = ?) "
                + "ORDER BY r.return_date, r.id";
        String lineSql = "SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setLong(3, productId);
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
            throw new IllegalStateException("Cannot list sales returns of product", e);
        }
    }

    /**
     * Searches sales returns by any combination of the customer of the order, a product among the lines, the exact
     * reason and a return date range; a null customer id, product id, reason, from date or to date is not applied.
     * The dates are inclusive. Oldest first.
     */
    public List<SalesReturn> searchSalesReturns(Long customerId, Long productId, String reason, LocalDate from, LocalDate to) {
        String sql = "SELECT r.id, r.number, r.order_id, r.return_date, r.reason FROM sales_return r "
                + "JOIN sales_order o ON o.id = r.order_id "
                + "WHERE (CAST(? AS BIGINT) IS NULL OR o.customer_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS BIGINT) IS NULL OR EXISTS (SELECT 1 FROM sales_return_line pl "
                + "WHERE pl.return_id = r.id AND pl.product_id = CAST(? AS BIGINT))) "
                + "AND (CAST(? AS VARCHAR) IS NULL OR r.reason = CAST(? AS VARCHAR)) "
                + "AND (CAST(? AS DATE) IS NULL OR r.return_date >= CAST(? AS DATE)) "
                + "AND (CAST(? AS DATE) IS NULL OR r.return_date <= CAST(? AS DATE)) "
                + "ORDER BY r.return_date, r.id";
        String lineSql = "SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, customerId);
            statement.setObject(2, customerId);
            statement.setObject(3, productId);
            statement.setObject(4, productId);
            statement.setObject(5, reason);
            statement.setObject(6, reason);
            statement.setObject(7, from);
            statement.setObject(8, from);
            statement.setObject(9, to);
            statement.setObject(10, to);
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
            throw new IllegalStateException("Cannot search sales returns", e);
        }
    }

    public CreditNote createCreditNote(CreditNote note) {
        String sql = "INSERT INTO credit_note (number, customer_id, return_id, credit_date, amount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, note.number());
            statement.setLong(2, note.customerId());
            statement.setLong(3, note.returnId());
            statement.setObject(4, note.creditDate());
            statement.setBigDecimal(5, note.amount());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new CreditNote(keys.getLong(1), note.number(), note.customerId(), note.returnId(), note.creditDate(),
                        note.amount());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create credit note", e);
        }
    }

    public List<CreditNote> listCreditNotesByCustomer(long customerId) {
        String sql = "SELECT id, number, customer_id, return_id, credit_date, amount FROM credit_note "
                + "WHERE customer_id = ? ORDER BY credit_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<CreditNote> notes = new ArrayList<>();
                while (rs.next()) {
                    notes.add(new CreditNote(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getLong("return_id"), rs.getObject("credit_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return notes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list credit notes", e);
        }
    }

    /** Records that the credit note has been used against the invoice. A credit note can be used only once. */
    public void createCreditNoteApplication(long creditNoteId, long invoiceId, LocalDate appliedDate) {
        String sql = "INSERT INTO credit_note_application (credit_note_id, invoice_id, applied_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, creditNoteId);
            statement.setLong(2, invoiceId);
            statement.setObject(3, appliedDate);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot apply credit note", e);
        }
    }

    /** The credit note with the number, if it exists. */
    public Optional<CreditNote> findCreditNoteByNumber(String number) {
        String sql = "SELECT id, number, customer_id, return_id, credit_date, amount FROM credit_note WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new CreditNote(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                        rs.getLong("return_id"), rs.getObject("credit_date", LocalDate.class), rs.getBigDecimal("amount")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find credit note", e);
        }
    }

    /** Whether the credit note has already been used against an invoice. */
    public boolean isCreditNoteApplied(long creditNoteId) {
        String sql = "SELECT COUNT(*) AS cnt FROM credit_note_application WHERE credit_note_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, creditNoteId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot check credit note", e);
        }
    }

    /**
     * The customers with credit notes not used against an invoice, with the number and total of those notes.
     * Sorted by region, then largest open amount first, then name and id.
     */
    public List<CustomerOpenCredit> findCustomersWithOpenCredit() {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, COUNT(n.id) AS note_count, "
                + "SUM(n.amount) AS open_amount FROM credit_note n JOIN customer c ON c.id = n.customer_id "
                + "WHERE NOT EXISTS (SELECT 1 FROM credit_note_application a WHERE a.credit_note_id = n.id) "
                + "GROUP BY c.id, c.name, c.region ORDER BY c.region, open_amount DESC, c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerOpenCredit> customers = new ArrayList<>();
                while (rs.next()) {
                    customers.add(new CustomerOpenCredit(rs.getLong("id"), rs.getString("name"),
                            Region.valueOf(rs.getString("region")), rs.getInt("note_count"), rs.getBigDecimal("open_amount")));
                }
                return customers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customers with open credit", e);
        }
    }

    /** The open (unused) credit per region: customers, notes and total amount. Largest amount first, then region. */
    public List<RegionOpenCredit> findOpenCreditPerRegion() {
        String sql = "SELECT c.region AS region, COUNT(DISTINCT c.id) AS customer_count, COUNT(n.id) AS note_count, "
                + "SUM(n.amount) AS open_amount FROM credit_note n JOIN customer c ON c.id = n.customer_id "
                + "WHERE NOT EXISTS (SELECT 1 FROM credit_note_application a WHERE a.credit_note_id = n.id) "
                + "GROUP BY c.region ORDER BY open_amount DESC, c.region";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<RegionOpenCredit> regions = new ArrayList<>();
                while (rs.next()) {
                    regions.add(new RegionOpenCredit(Region.valueOf(rs.getString("region")), rs.getInt("customer_count"),
                            rs.getInt("note_count"), rs.getBigDecimal("open_amount")));
                }
                return regions;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find open credit per region", e);
        }
    }
}
