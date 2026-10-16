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

class DataStorePart06 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart06(DataStore store, java.sql.Connection connection) {
        this.store = store;
        this.connection = connection;
    }

    public Optional<SalesRep> findRegionManager(Region region) {
        String sql = "SELECT r.id, r.name, r.email, r.region FROM sales_rep r "
                + "JOIN region_manager m ON m.sales_rep_id = r.id WHERE m.region = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new SalesRep(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                        Region.valueOf(rs.getString("region"))));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find region manager", e);
        }
    }

    /** The regions managed by the rep, in name order. */
    public List<Region> listRegionsManagedBy(long salesRepId) {
        String sql = "SELECT region FROM region_manager WHERE sales_rep_id = ? ORDER BY region";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
            try (ResultSet rs = statement.executeQuery()) {
                List<Region> regions = new ArrayList<>();
                while (rs.next()) {
                    regions.add(Region.valueOf(rs.getString("region")));
                }
                return regions;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list regions managed by sales rep", e);
        }
    }

    public void assignCustomerToSalesRep(long customerId, long salesRepId) {
        String sql = "UPDATE customer SET sales_rep_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
            statement.setLong(2, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot assign customer to sales rep", e);
        }
    }

    public List<Customer> listCustomersBySalesRep(long salesRepId) {
        String sql = "SELECT id, name, email, phone, country, region, status FROM customer WHERE sales_rep_id = ? AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = customer.id) ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
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
            throw new IllegalStateException("Cannot list customers by sales rep", e);
        }
    }

    /** Number and total value of the non-cancelled orders of the rep's customers placed between the two dates, inclusive. */
    public RepPerformance findSalesRepPerformance(long salesRepId, LocalDate from, LocalDate to) {
        String sql = "SELECT COUNT(DISTINCT o.id) AS order_count, COALESCE(SUM(l.quantity * l.unit_price), 0) AS total "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE c.sales_rep_id = ? AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return new RepPerformance(rs.getInt("order_count"), rs.getBigDecimal("total"));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales rep performance", e);
        }
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, per region of the
     * customer. Sorted by total value, highest first (then by region name); regions without orders are left out.
     */
    public List<RegionSales> findSalesPerRegion(LocalDate from, LocalDate to) {
        String sql = "SELECT c.region AS region, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS total "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY c.region ORDER BY total DESC, c.region";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<RegionSales> sales = new ArrayList<>();
                while (rs.next()) {
                    sales.add(new RegionSales(Region.valueOf(rs.getString("region")), rs.getInt("order_count"),
                            rs.getBigDecimal("total")));
                }
                return sales;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales per region", e);
        }
    }

    /**
     * Figures of the region for the period, inclusive: customers of the region, non-cancelled orders placed in the period
     * with their value, open invoices dated in the period with the amount of those due before the end date, shipments
     * shipped, returns made and not closed tickets opened in the period, all for customers of the region.
     */
    public RegionReport findRegionReport(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM customer c WHERE c.region = ?) AS customer_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.region = ? AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') AS order_count, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE c.region = ? AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') AS revenue, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE c.region = ? AND i.invoice_date >= ? AND i.invoice_date <= ? AND i.status = 'OPEN') AS open_invoice_count, "
                + "(SELECT COALESCE(SUM(i.amount), 0) FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE c.region = ? AND i.invoice_date >= ? AND i.invoice_date <= ? AND i.status = 'OPEN' AND i.due_date < ?) AS overdue_amount, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.region = ? AND s.ship_date >= ? AND s.ship_date <= ?) AS shipment_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id "
                + "WHERE c.region = ? AND r.return_date >= ? AND r.return_date <= ?) AS return_count, "
                + "(SELECT COUNT(*) FROM support_ticket t JOIN customer c ON c.id = t.customer_id "
                + "WHERE c.region = ? AND t.opened_date >= ? AND t.opened_date <= ? AND t.status <> 'CLOSED') AS open_ticket_count";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int index = 1;
            statement.setString(index++, region.name());
            statement.setString(index++, region.name());
            statement.setObject(index++, from);
            statement.setObject(index++, to);
            statement.setString(index++, region.name());
            statement.setObject(index++, from);
            statement.setObject(index++, to);
            statement.setString(index++, region.name());
            statement.setObject(index++, from);
            statement.setObject(index++, to);
            statement.setString(index++, region.name());
            statement.setObject(index++, from);
            statement.setObject(index++, to);
            statement.setObject(index++, to);
            statement.setString(index++, region.name());
            statement.setObject(index++, from);
            statement.setObject(index++, to);
            statement.setString(index++, region.name());
            statement.setObject(index++, from);
            statement.setObject(index++, to);
            statement.setString(index++, region.name());
            statement.setObject(index++, from);
            statement.setObject(index++, to);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return new RegionReport(region, from, to, rs.getInt("customer_count"), rs.getInt("order_count"),
                        rs.getBigDecimal("revenue"), rs.getInt("open_invoice_count"), rs.getBigDecimal("overdue_amount"),
                        rs.getInt("shipment_count"), rs.getInt("return_count"), rs.getInt("open_ticket_count"));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find report of region", e);
        }
    }

    /**
     * Number and total value of the non-cancelled orders placed in the given month and in the same month of the
     * previous year, per region of the customer. Sorted by region name; regions without orders in either month are
     * left out.
     */
    public List<YearOverYearSales> findYearOverYearSalesPerRegion(YearMonth month) {
        String sql = "SELECT c.region AS region, "
                + "COUNT(DISTINCT CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN o.id END) AS order_count, "
                + "COALESCE(SUM(CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN l.quantity * l.unit_price END), 0) AS total, "
                + "COUNT(DISTINCT CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN o.id END) AS previous_order_count, "
                + "COALESCE(SUM(CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN l.quantity * l.unit_price END), 0) AS previous_total "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.status <> 'CANCELLED' AND ((o.order_date >= ? AND o.order_date <= ?) OR (o.order_date >= ? AND o.order_date <= ?)) "
                + "GROUP BY c.region ORDER BY c.region";
        YearMonth previous = month.minusYears(1);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, month.atDay(1));
            statement.setObject(2, month.atEndOfMonth());
            statement.setObject(3, month.atDay(1));
            statement.setObject(4, month.atEndOfMonth());
            statement.setObject(5, previous.atDay(1));
            statement.setObject(6, previous.atEndOfMonth());
            statement.setObject(7, previous.atDay(1));
            statement.setObject(8, previous.atEndOfMonth());
            statement.setObject(9, month.atDay(1));
            statement.setObject(10, month.atEndOfMonth());
            statement.setObject(11, previous.atDay(1));
            statement.setObject(12, previous.atEndOfMonth());
            try (ResultSet rs = statement.executeQuery()) {
                List<YearOverYearSales> sales = new ArrayList<>();
                while (rs.next()) {
                    sales.add(new YearOverYearSales(Region.valueOf(rs.getString("region")), rs.getInt("order_count"),
                            rs.getBigDecimal("total"), rs.getInt("previous_order_count"), rs.getBigDecimal("previous_total")));
                }
                return sales;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find year-over-year sales per region", e);
        }
    }

    /**
     * Number and total value of the non-cancelled orders placed in the given month and in the same month of the
     * previous year, over all regions. The region of the result is null; months without orders give zeros.
     */
    public YearOverYearSales findYearOverYearSales(YearMonth month) {
        String sql = "SELECT "
                + "COUNT(DISTINCT CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN o.id END) AS order_count, "
                + "COALESCE(SUM(CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN l.quantity * l.unit_price END), 0) AS total, "
                + "COUNT(DISTINCT CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN o.id END) AS previous_order_count, "
                + "COALESCE(SUM(CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN l.quantity * l.unit_price END), 0) AS previous_total "
                + "FROM sales_order o LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.status <> 'CANCELLED'";
        YearMonth previous = month.minusYears(1);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, month.atDay(1));
            statement.setObject(2, month.atEndOfMonth());
            statement.setObject(3, month.atDay(1));
            statement.setObject(4, month.atEndOfMonth());
            statement.setObject(5, previous.atDay(1));
            statement.setObject(6, previous.atEndOfMonth());
            statement.setObject(7, previous.atDay(1));
            statement.setObject(8, previous.atEndOfMonth());
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return new YearOverYearSales(null, rs.getInt("order_count"), rs.getBigDecimal("total"),
                        rs.getInt("previous_order_count"), rs.getBigDecimal("previous_total"));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find year-over-year sales", e);
        }
    }

    /**
     * Per region of the customer, not counting closed customers: the number of active customers, customers on hold,
     * customers created between the two dates, inclusive (customers without a created date are not new), and active
     * customers without a non-cancelled order placed between the two dormant dates, inclusive. Sorted by region name;
     * regions without customers are left out.
     */
    public List<RegionCustomers> findCustomersPerRegion(LocalDate newFrom, LocalDate newTo, LocalDate dormantFrom, LocalDate dormantTo) {
        String sql = "SELECT c.region AS region, "
                + "SUM(CASE WHEN c.status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_count, "
                + "SUM(CASE WHEN c.status = 'ON_HOLD' THEN 1 ELSE 0 END) AS on_hold_count, "
                + "SUM(CASE WHEN c.created_date >= ? AND c.created_date <= ? THEN 1 ELSE 0 END) AS new_count, "
                + "SUM(CASE WHEN c.status = 'ACTIVE' AND NOT EXISTS (SELECT 1 FROM sales_order o WHERE o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') THEN 1 ELSE 0 END) AS dormant_count "
                + "FROM customer c "
                + "WHERE NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "GROUP BY c.region ORDER BY c.region";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, newFrom);
            statement.setObject(2, newTo);
            statement.setObject(3, dormantFrom);
            statement.setObject(4, dormantTo);
            try (ResultSet rs = statement.executeQuery()) {
                List<RegionCustomers> customers = new ArrayList<>();
                while (rs.next()) {
                    customers.add(new RegionCustomers(Region.valueOf(rs.getString("region")), rs.getInt("active_count"),
                            rs.getInt("on_hold_count"), rs.getInt("new_count"), rs.getInt("dormant_count")));
                }
                return customers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customers per region", e);
        }
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, per sales channel.
     * Sorted by revenue, highest first (then by channel name); channels without orders, and orders without a recorded
     * channel, are left out.
     */
    public List<ChannelSales> findSalesPerChannel(LocalDate from, LocalDate to) {
        String sql = "SELECT oc.channel AS channel, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM sales_order o JOIN order_channel oc ON oc.order_id = o.id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY oc.channel ORDER BY revenue DESC, oc.channel";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ChannelSales> sales = new ArrayList<>();
                while (rs.next()) {
                    sales.add(new ChannelSales(SalesChannel.valueOf(rs.getString("channel")), rs.getInt("order_count"),
                            rs.getBigDecimal("revenue")));
                }
                return sales;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales per channel", e);
        }
    }

    /**
     * For each of the twelve months of the year, in calendar order: the number and total value of the non-cancelled
     * orders placed in the month, the number of customers created in the month (customers without a created date are
     * not counted), the number of returns dated in the month and the total amount of the credit notes dated in the
     * month. Months without activity are included with zeros.
     */
    public List<MonthlySales> findMonthlySales(int year) {
        String sql = "SELECT m.x AS month_no,"
                + "(SELECT COUNT(*) FROM sales_order o WHERE YEAR(o.order_date) = ? AND MONTH(o.order_date) = m.x "
                + "AND o.status <> 'CANCELLED') AS order_count, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE YEAR(o.order_date) = ? AND MONTH(o.order_date) = m.x AND o.status <> 'CANCELLED') AS revenue, "
                + "(SELECT COUNT(*) FROM customer c WHERE YEAR(c.created_date) = ? AND MONTH(c.created_date) = m.x) AS new_customers, "
                + "(SELECT COUNT(*) FROM sales_return r WHERE YEAR(r.return_date) = ? AND MONTH(r.return_date) = m.x) AS return_count, "
                + "(SELECT COALESCE(SUM(n.amount), 0) FROM credit_note n WHERE YEAR(n.credit_date) = ? AND MONTH(n.credit_date) = m.x) AS credited "
                + "FROM SYSTEM_RANGE(1, 12) m ORDER BY m.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                statement.setInt(i, year);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<MonthlySales> months = new ArrayList<>();
                while (rs.next()) {
                    months.add(new MonthlySales(rs.getInt("month_no"), rs.getInt("order_count"), rs.getBigDecimal("revenue"),
                            rs.getInt("new_customers"), rs.getInt("return_count"), rs.getBigDecimal("credited")));
                }
                return months;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find monthly sales", e);
        }
    }

    /**
     * For each of the four quarters of the year, in order: the number of customers created in the quarter, in total and
     * per region (customers without a created date are not counted). Quarters and regions without customers are
     * included with zeros.
     */
    public List<QuarterCustomers> findCustomersPerQuarter(int year) {
        String sql = "SELECT q.x AS quarter_no, "
                + "(SELECT COUNT(*) FROM customer c WHERE YEAR(c.created_date) = ? AND QUARTER(c.created_date) = q.x) AS total_count, "
                + "(SELECT COUNT(*) FROM customer c WHERE YEAR(c.created_date) = ? AND QUARTER(c.created_date) = q.x AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM customer c WHERE YEAR(c.created_date) = ? AND QUARTER(c.created_date) = q.x AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM customer c WHERE YEAR(c.created_date) = ? AND QUARTER(c.created_date) = q.x AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM customer c WHERE YEAR(c.created_date) = ? AND QUARTER(c.created_date) = q.x AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 4) q ORDER BY q.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                statement.setInt(i, year);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<QuarterCustomers> quarters = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    quarters.add(new QuarterCustomers(rs.getInt("quarter_no"), rs.getInt("total_count"), perRegion));
                }
                return quarters;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customers per quarter", e);
        }
    }

    /**
     * For each of the four quarters of the year, in order: the number of orders placed in the quarter, in total and per
     * region of the customer (cancelled orders are not counted). Quarters and regions without orders are included with
     * zeros.
     */
    public List<QuarterOrders> findOrdersPerQuarter(int year) {
        String sql = "SELECT q.x AS quarter_no, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE YEAR(o.order_date) = ? AND QUARTER(o.order_date) = q.x AND o.status <> 'CANCELLED') AS total_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE YEAR(o.order_date) = ? AND QUARTER(o.order_date) = q.x AND o.status <> 'CANCELLED' AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE YEAR(o.order_date) = ? AND QUARTER(o.order_date) = q.x AND o.status <> 'CANCELLED' AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE YEAR(o.order_date) = ? AND QUARTER(o.order_date) = q.x AND o.status <> 'CANCELLED' AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE YEAR(o.order_date) = ? AND QUARTER(o.order_date) = q.x AND o.status <> 'CANCELLED' AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 4) q ORDER BY q.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                statement.setInt(i, year);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<QuarterOrders> quarters = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    quarters.add(new QuarterOrders(rs.getInt("quarter_no"), rs.getInt("total_count"), perRegion));
                }
                return quarters;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find orders per quarter", e);
        }
    }

    /**
     * For each of the four quarters of the year, in order: the number of purchase orders placed in the quarter, in total
     * and per region of the warehouse they are to be delivered to (cancelled orders are not counted; orders without a
     * warehouse count only in the total). Quarters and regions without orders are included with zeros.
     */
    public List<QuarterPurchaseOrders> findPurchaseOrdersPerQuarter(int year) {
        String sql = "SELECT q.x AS quarter_no, "
                + "(SELECT COUNT(*) FROM purchase_order p WHERE YEAR(p.order_date) = ? AND QUARTER(p.order_date) = q.x AND p.status <> 'CANCELLED') AS total_count, "
                + "(SELECT COUNT(*) FROM purchase_order p JOIN warehouse w ON w.id = p.warehouse_id WHERE YEAR(p.order_date) = ? AND QUARTER(p.order_date) = q.x AND p.status <> 'CANCELLED' AND w.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM purchase_order p JOIN warehouse w ON w.id = p.warehouse_id WHERE YEAR(p.order_date) = ? AND QUARTER(p.order_date) = q.x AND p.status <> 'CANCELLED' AND w.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM purchase_order p JOIN warehouse w ON w.id = p.warehouse_id WHERE YEAR(p.order_date) = ? AND QUARTER(p.order_date) = q.x AND p.status <> 'CANCELLED' AND w.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM purchase_order p JOIN warehouse w ON w.id = p.warehouse_id WHERE YEAR(p.order_date) = ? AND QUARTER(p.order_date) = q.x AND p.status <> 'CANCELLED' AND w.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 4) q ORDER BY q.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                statement.setInt(i, year);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<QuarterPurchaseOrders> quarters = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    quarters.add(new QuarterPurchaseOrders(rs.getInt("quarter_no"), rs.getInt("total_count"), perRegion));
                }
                return quarters;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find purchase orders per quarter", e);
        }
    }

    /**
     * For each day of the week, Monday to Sunday: the number of purchase orders placed between the two dates, inclusive,
     * in total and per region of the warehouse they are to be delivered to (cancelled orders are not counted; orders
     * without a warehouse count only in the total). Days and regions without orders are included with zeros.
     */
    public List<WeekdayPurchaseOrders> findPurchaseOrdersPerWeekday(LocalDate from, LocalDate to) {
        String sql = "SELECT d.x AS day_no, "
                + "(SELECT COUNT(*) FROM purchase_order p WHERE p.order_date >= ? AND p.order_date <= ? AND ISO_DAY_OF_WEEK(p.order_date) = d.x AND p.status <> 'CANCELLED') AS total_count, "
                + "(SELECT COUNT(*) FROM purchase_order p JOIN warehouse w ON w.id = p.warehouse_id WHERE p.order_date >= ? AND p.order_date <= ? AND ISO_DAY_OF_WEEK(p.order_date) = d.x AND p.status <> 'CANCELLED' AND w.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM purchase_order p JOIN warehouse w ON w.id = p.warehouse_id WHERE p.order_date >= ? AND p.order_date <= ? AND ISO_DAY_OF_WEEK(p.order_date) = d.x AND p.status <> 'CANCELLED' AND w.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM purchase_order p JOIN warehouse w ON w.id = p.warehouse_id WHERE p.order_date >= ? AND p.order_date <= ? AND ISO_DAY_OF_WEEK(p.order_date) = d.x AND p.status <> 'CANCELLED' AND w.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM purchase_order p JOIN warehouse w ON w.id = p.warehouse_id WHERE p.order_date >= ? AND p.order_date <= ? AND ISO_DAY_OF_WEEK(p.order_date) = d.x AND p.status <> 'CANCELLED' AND w.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 7) d ORDER BY d.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 9; i += 2) {
                statement.setObject(i, from);
                statement.setObject(i + 1, to);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<WeekdayPurchaseOrders> days = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    days.add(new WeekdayPurchaseOrders(DayOfWeek.of(rs.getInt("day_no")), rs.getInt("total_count"), perRegion));
                }
                return days;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find purchase orders per weekday", e);
        }
    }

    /**
     * For each of the four quarters of the year, in order: the number of shipments sent in the quarter, in total and per
     * region of the customer of the order. Quarters and regions without shipments are included with zeros.
     */
    public List<QuarterShipments> findShipmentsPerQuarter(int year) {
        String sql = "SELECT q.x AS quarter_no, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(s.ship_date) = ? AND QUARTER(s.ship_date) = q.x) AS total_count, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(s.ship_date) = ? AND QUARTER(s.ship_date) = q.x AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(s.ship_date) = ? AND QUARTER(s.ship_date) = q.x AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(s.ship_date) = ? AND QUARTER(s.ship_date) = q.x AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(s.ship_date) = ? AND QUARTER(s.ship_date) = q.x AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 4) q ORDER BY q.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                statement.setInt(i, year);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<QuarterShipments> quarters = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    quarters.add(new QuarterShipments(rs.getInt("quarter_no"), rs.getInt("total_count"), perRegion));
                }
                return quarters;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find shipments per quarter", e);
        }
    }

    /**
     * For each of the four quarters of the year, in order: the number of sales returns dated in the quarter, in total and
     * per region of the customer of the order. Quarters and regions without returns are included with zeros.
     */
    public List<QuarterReturns> findReturnsPerQuarter(int year) {
        String sql = "SELECT q.x AS quarter_no, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(r.return_date) = ? AND QUARTER(r.return_date) = q.x) AS total_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(r.return_date) = ? AND QUARTER(r.return_date) = q.x AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(r.return_date) = ? AND QUARTER(r.return_date) = q.x AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(r.return_date) = ? AND QUARTER(r.return_date) = q.x AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE YEAR(r.return_date) = ? AND QUARTER(r.return_date) = q.x AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 4) q ORDER BY q.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                statement.setInt(i, year);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<QuarterReturns> quarters = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    quarters.add(new QuarterReturns(rs.getInt("quarter_no"), rs.getInt("total_count"), perRegion));
                }
                return quarters;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returns per quarter", e);
        }
    }

    /**
     * For each of the four quarters of the year, in order: the number of invoices issued in the quarter, in total and per
     * region of the customer (cancelled invoices are not counted). Quarters and regions without invoices are included
     * with zeros.
     */
    public List<QuarterInvoices> findInvoicesPerQuarter(int year) {
        String sql = "SELECT q.x AS quarter_no, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE YEAR(i.invoice_date) = ? AND QUARTER(i.invoice_date) = q.x AND i.status <> 'CANCELLED') AS total_count, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE YEAR(i.invoice_date) = ? AND QUARTER(i.invoice_date) = q.x AND i.status <> 'CANCELLED' AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE YEAR(i.invoice_date) = ? AND QUARTER(i.invoice_date) = q.x AND i.status <> 'CANCELLED' AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE YEAR(i.invoice_date) = ? AND QUARTER(i.invoice_date) = q.x AND i.status <> 'CANCELLED' AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE YEAR(i.invoice_date) = ? AND QUARTER(i.invoice_date) = q.x AND i.status <> 'CANCELLED' AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 4) q ORDER BY q.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                statement.setInt(i, year);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<QuarterInvoices> quarters = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    quarters.add(new QuarterInvoices(rs.getInt("quarter_no"), rs.getInt("total_count"), perRegion));
                }
                return quarters;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoices per quarter", e);
        }
    }

    /**
     * For each day of the week, Monday to Sunday: the number of customers created between the two dates, inclusive, in
     * total and per region (customers without a created date are not counted). Days and regions without customers are
     * included with zeros.
     */
    public List<WeekdayCustomers> findCustomersPerWeekday(LocalDate from, LocalDate to) {
        String sql = "SELECT d.x AS day_no, "
                + "(SELECT COUNT(*) FROM customer c WHERE c.created_date >= ? AND c.created_date <= ? AND ISO_DAY_OF_WEEK(c.created_date) = d.x) AS total_count, "
                + "(SELECT COUNT(*) FROM customer c WHERE c.created_date >= ? AND c.created_date <= ? AND ISO_DAY_OF_WEEK(c.created_date) = d.x AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM customer c WHERE c.created_date >= ? AND c.created_date <= ? AND ISO_DAY_OF_WEEK(c.created_date) = d.x AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM customer c WHERE c.created_date >= ? AND c.created_date <= ? AND ISO_DAY_OF_WEEK(c.created_date) = d.x AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM customer c WHERE c.created_date >= ? AND c.created_date <= ? AND ISO_DAY_OF_WEEK(c.created_date) = d.x AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 7) d ORDER BY d.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 9; i += 2) {
                statement.setObject(i, from);
                statement.setObject(i + 1, to);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<WeekdayCustomers> days = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    days.add(new WeekdayCustomers(DayOfWeek.of(rs.getInt("day_no")), rs.getInt("total_count"), perRegion));
                }
                return days;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customers per weekday", e);
        }
    }

    /**
     * For each day of the week, Monday to Sunday: the number of orders placed between the two dates, inclusive, in
     * total and per region of the customer (cancelled orders are not counted). Days and regions without orders are
     * included with zeros.
     */
    public List<WeekdayOrders> findOrdersPerWeekday(LocalDate from, LocalDate to) {
        String sql = "SELECT d.x AS day_no, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE o.order_date >= ? AND o.order_date <= ? AND ISO_DAY_OF_WEEK(o.order_date) = d.x AND o.status <> 'CANCELLED') AS total_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE o.order_date >= ? AND o.order_date <= ? AND ISO_DAY_OF_WEEK(o.order_date) = d.x AND o.status <> 'CANCELLED' AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE o.order_date >= ? AND o.order_date <= ? AND ISO_DAY_OF_WEEK(o.order_date) = d.x AND o.status <> 'CANCELLED' AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE o.order_date >= ? AND o.order_date <= ? AND ISO_DAY_OF_WEEK(o.order_date) = d.x AND o.status <> 'CANCELLED' AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM sales_order o JOIN customer c ON c.id = o.customer_id WHERE o.order_date >= ? AND o.order_date <= ? AND ISO_DAY_OF_WEEK(o.order_date) = d.x AND o.status <> 'CANCELLED' AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 7) d ORDER BY d.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 9; i += 2) {
                statement.setObject(i, from);
                statement.setObject(i + 1, to);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<WeekdayOrders> days = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    days.add(new WeekdayOrders(DayOfWeek.of(rs.getInt("day_no")), rs.getInt("total_count"), perRegion));
                }
                return days;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find orders per weekday", e);
        }
    }

    /**
     * For each day of the week, Monday to Sunday: the number of shipments sent between the two dates, inclusive, in
     * total and per region of the customer of the order. Days and regions without shipments are included with zeros.
     */
    public List<WeekdayShipments> findShipmentsPerWeekday(LocalDate from, LocalDate to) {
        String sql = "SELECT d.x AS day_no, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE s.ship_date >= ? AND s.ship_date <= ? AND ISO_DAY_OF_WEEK(s.ship_date) = d.x) AS total_count, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE s.ship_date >= ? AND s.ship_date <= ? AND ISO_DAY_OF_WEEK(s.ship_date) = d.x AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE s.ship_date >= ? AND s.ship_date <= ? AND ISO_DAY_OF_WEEK(s.ship_date) = d.x AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE s.ship_date >= ? AND s.ship_date <= ? AND ISO_DAY_OF_WEEK(s.ship_date) = d.x AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE s.ship_date >= ? AND s.ship_date <= ? AND ISO_DAY_OF_WEEK(s.ship_date) = d.x AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 7) d ORDER BY d.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 9; i += 2) {
                statement.setObject(i, from);
                statement.setObject(i + 1, to);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<WeekdayShipments> days = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    days.add(new WeekdayShipments(DayOfWeek.of(rs.getInt("day_no")), rs.getInt("total_count"), perRegion));
                }
                return days;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find shipments per weekday", e);
        }
    }

    /**
     * For each day of the week, Monday to Sunday: the number of sales returns dated between the two dates, inclusive, in
     * total and per region of the customer of the order. Days and regions without returns are included with zeros.
     */
    public List<WeekdayReturns> findReturnsPerWeekday(LocalDate from, LocalDate to) {
        String sql = "SELECT d.x AS day_no, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE r.return_date >= ? AND r.return_date <= ? AND ISO_DAY_OF_WEEK(r.return_date) = d.x) AS total_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE r.return_date >= ? AND r.return_date <= ? AND ISO_DAY_OF_WEEK(r.return_date) = d.x AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE r.return_date >= ? AND r.return_date <= ? AND ISO_DAY_OF_WEEK(r.return_date) = d.x AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE r.return_date >= ? AND r.return_date <= ? AND ISO_DAY_OF_WEEK(r.return_date) = d.x AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id WHERE r.return_date >= ? AND r.return_date <= ? AND ISO_DAY_OF_WEEK(r.return_date) = d.x AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 7) d ORDER BY d.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 9; i += 2) {
                statement.setObject(i, from);
                statement.setObject(i + 1, to);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<WeekdayReturns> days = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    days.add(new WeekdayReturns(DayOfWeek.of(rs.getInt("day_no")), rs.getInt("total_count"), perRegion));
                }
                return days;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find returns per weekday", e);
        }
    }

    /**
     * For each day of the week, Monday to Sunday: the number of invoices issued between the two dates, inclusive, in
     * total and per region of the customer (cancelled invoices are not counted). Days and regions without invoices are
     * included with zeros.
     */
    public List<WeekdayInvoices> findInvoicesPerWeekday(LocalDate from, LocalDate to) {
        String sql = "SELECT d.x AS day_no, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE i.invoice_date >= ? AND i.invoice_date <= ? AND ISO_DAY_OF_WEEK(i.invoice_date) = d.x AND i.status <> 'CANCELLED') AS total_count, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE i.invoice_date >= ? AND i.invoice_date <= ? AND ISO_DAY_OF_WEEK(i.invoice_date) = d.x AND i.status <> 'CANCELLED' AND c.region = 'NORTH') AS north_count, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE i.invoice_date >= ? AND i.invoice_date <= ? AND ISO_DAY_OF_WEEK(i.invoice_date) = d.x AND i.status <> 'CANCELLED' AND c.region = 'SOUTH') AS south_count, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE i.invoice_date >= ? AND i.invoice_date <= ? AND ISO_DAY_OF_WEEK(i.invoice_date) = d.x AND i.status <> 'CANCELLED' AND c.region = 'EAST') AS east_count, "
                + "(SELECT COUNT(*) FROM invoice i JOIN customer c ON c.id = i.customer_id WHERE i.invoice_date >= ? AND i.invoice_date <= ? AND ISO_DAY_OF_WEEK(i.invoice_date) = d.x AND i.status <> 'CANCELLED' AND c.region = 'WEST') AS west_count "
                + "FROM SYSTEM_RANGE(1, 7) d ORDER BY d.x";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 9; i += 2) {
                statement.setObject(i, from);
                statement.setObject(i + 1, to);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<WeekdayInvoices> days = new ArrayList<>();
                while (rs.next()) {
                    Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
                    perRegion.put(Region.NORTH, rs.getInt("north_count"));
                    perRegion.put(Region.SOUTH, rs.getInt("south_count"));
                    perRegion.put(Region.EAST, rs.getInt("east_count"));
                    perRegion.put(Region.WEST, rs.getInt("west_count"));
                    days.add(new WeekdayInvoices(DayOfWeek.of(rs.getInt("day_no")), rs.getInt("total_count"), perRegion));
                }
                return days;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoices per weekday", e);
        }
    }

    /**
     * The N most recent invoices (cancelled invoices are left out), newest invoice date first, then by highest id.
     */
    public List<Invoice> findLatestInvoices(int limit) {
        String sql = "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "WHERE i.status <> 'CANCELLED' "
                + "ORDER BY i.invoice_date DESC, i.id DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
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
            throw new IllegalStateException("Cannot find latest invoices", e);
        }
    }

    /**
     * The N most recent invoices of customers in the region (cancelled invoices are left out), newest invoice date
     * first, then by highest id.
     */
    public List<Invoice> findLatestInvoicesByRegion(Region region, int limit) {
        String sql = "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id WHERE c.region = ? AND i.status <> 'CANCELLED' "
                + "ORDER BY i.invoice_date DESC, i.id DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest invoices by region", e);
        }
    }

    /**
     * The N most recent invoices of the customer (cancelled invoices are left out), newest invoice date first, then by
     * highest id.
     */
    public List<Invoice> findLatestInvoicesByCustomer(long customerId, int limit) {
        String sql = "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "WHERE i.customer_id = ? AND i.status <> 'CANCELLED' "
                + "ORDER BY i.invoice_date DESC, i.id DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest invoices by customer", e);
        }
    }

    /**
     * The N most recent shipments, newest ship date first, then by highest id.
     */
    public List<Shipment> findLatestShipments(int limit) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "ORDER BY s.ship_date DESC, s.id DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
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
            throw new IllegalStateException("Cannot find latest shipments", e);
        }
    }

    /**
     * The N most recent shipments of orders of customers in the region, newest ship date first, then by highest id.
     */
    public List<Shipment> findLatestShipmentsByRegion(Region region, int limit) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id WHERE c.region = ? "
                + "ORDER BY s.ship_date DESC, s.id DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest shipments by region", e);
        }
    }

    /**
     * The N most recent shipments of orders of the customer, newest ship date first, then by highest id.
     */
    public List<Shipment> findLatestShipmentsByCustomer(long customerId, int limit) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "JOIN sales_order o ON o.id = s.order_id WHERE o.customer_id = ? "
                + "ORDER BY s.ship_date DESC, s.id DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest shipments by customer", e);
        }
    }

    /**
     * The N most recently created customers (customers without a created date and closed customers are left out),
     * newest first, then by highest id.
     */
    public List<Customer> findLatestCustomers(int limit) {
        String sql = "SELECT c.id, c.name, c.email, c.phone, c.country, c.region, c.status FROM customer c "
                + "WHERE c.created_date IS NOT NULL AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "ORDER BY c.created_date DESC, c.id DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
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
            throw new IllegalStateException("Cannot find latest customers", e);
        }
    }

    /**
     * The N most recently created customers in the region (customers without a created date and closed customers are
     * left out), newest first, then by highest id.
     */
    public List<Customer> findLatestCustomersByRegion(Region region, int limit) {
        String sql = "SELECT c.id, c.name, c.email, c.phone, c.country, c.region, c.status FROM customer c "
                + "WHERE c.region = ? AND c.created_date IS NOT NULL AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "ORDER BY c.created_date DESC, c.id DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest customers by region", e);
        }
    }

    /**
     * The N customers created most recently before the given customer, i.e. the ones that joined just ahead of it (the
     * customer itself, customers without a created date and closed customers are left out), newest first, then by
     * highest id. Customers created on the same date count as earlier when their id is lower.
     */
    public List<Customer> findLatestCustomersBefore(long customerId, int limit) {
        String sql = "SELECT c.id, c.name, c.email, c.phone, c.country, c.region, c.status FROM customer c, customer ref "
                + "WHERE ref.id = ? AND c.created_date IS NOT NULL AND ref.created_date IS NOT NULL "
                + "AND (c.created_date < ref.created_date OR (c.created_date = ref.created_date AND c.id < ref.id)) "
                + "AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "ORDER BY c.created_date DESC, c.id DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setInt(2, limit);
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
            throw new IllegalStateException("Cannot find latest customers before customer", e);
        }
    }

    /**
     * The N most recently placed orders (cancelled orders are left out), newest first, then by highest id.
     */
    public List<SalesOrder> findLatestOrders(int limit) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "WHERE o.status <> 'CANCELLED' "
                + "ORDER BY o.order_date DESC, o.id DESC LIMIT ?";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
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
            throw new IllegalStateException("Cannot find latest orders", e);
        }
    }
}
