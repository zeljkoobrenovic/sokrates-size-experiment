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

/** All database access of BackOffice. */
public class DataStore {
    private final Connection connection;

    public DataStore(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    // ===== JDBC plumbing: statement setup, parameter binding, row mapping, exception wrapping, transactions =====

    /** Maps the current row of a result set to a value. */
    @FunctionalInterface
    private interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    /** A unit of database work that may throw SQLException; used for transactions. */
    @FunctionalInterface
    private interface SqlWork<T> {
        T run() throws SQLException;
    }

    /** Binds the parameters in order: enums as their name, null as SQL NULL, everything else as itself. */
    private static void bind(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof Enum<?> e) {
                statement.setString(i + 1, e.name());
            } else {
                statement.setObject(i + 1, param);
            }
        }
    }

    private static IllegalStateException failure(String sql, SQLException e) {
        return new IllegalStateException("Cannot execute: " + sql, e);
    }

    /** All rows of the query, each mapped by the mapper. */
    private <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, params);
            try (ResultSet rs = statement.executeQuery()) {
                List<T> rows = new ArrayList<>();
                while (rs.next()) {
                    rows.add(mapper.map(rs));
                }
                return rows;
            }
        } catch (SQLException e) {
            throw failure(sql, e);
        }
    }

    /** The first row of the query mapped by the mapper; empty if there is no row. */
    private <T> Optional<T> queryOptional(String sql, RowMapper<T> mapper, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, params);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? Optional.of(mapper.map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw failure(sql, e);
        }
    }

    /** The first row of a query that always returns a row (an aggregate), mapped by the mapper. */
    private <T> T querySingle(String sql, RowMapper<T> mapper, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, params);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return mapper.map(rs);
            }
        } catch (SQLException e) {
            throw failure(sql, e);
        }
    }

    /** Runs an INSERT, UPDATE or DELETE; returns the number of rows changed. */
    private int update(String sql, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bind(statement, params);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw failure(sql, e);
        }
    }

    /** Runs an INSERT and returns the generated id. */
    private long insert(String sql, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(statement, params);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return keys.getLong(1);
            }
        } catch (SQLException e) {
            throw failure(sql, e);
        }
    }

    /**
     * Runs the work in one transaction: commits when it returns, rolls back when it throws (a SQLException is wrapped
     * in an IllegalStateException, a RuntimeException is rethrown), and restores auto-commit afterwards.
     */
    private <T> T inTransaction(SqlWork<T> work) {
        try {
            connection.setAutoCommit(false);
            T result = work.run();
            connection.commit();
            return result;
        } catch (SQLException e) {
            rollbackQuietly();
            throw new IllegalStateException("Transaction failed", e);
        } catch (RuntimeException e) {
            rollbackQuietly();
            throw e;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    /**
     * Runs the work in one transaction the way the order, stock-transfer and other multi-statement writes have always
     * behaved: commits when the work returns; on a database failure (SQLException, or the IllegalStateException of the
     * query helpers) rolls back and throws IllegalStateException. Unlike {@link #inTransaction}, any other
     * RuntimeException (e.g. a null argument) is passed on WITHOUT a rollback, so what ran before it is committed when
     * auto-commit is restored.
     */
    private <T> T inTransactionRollingBackOnDatabaseFailureOnly(SqlWork<T> work) {
        try {
            connection.setAutoCommit(false);
            T result = work.run();
            connection.commit();
            return result;
        } catch (SQLException | IllegalStateException e) {
            rollbackQuietly();
            throw new IllegalStateException("Transaction failed", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    private void rollbackQuietly() {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
            // the original failure is the one to report
        }
    }

    /** Escapes a LIKE pattern part and wraps it in %...% for a case-insensitive "contains" match (use ESCAPE '\'). */
    private static String containsPattern(String part) {
        return "%" + part.toLowerCase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
    }

    // ===== Row mappers shared by several entities (expect the plain column names of the entity) =====

    /** Columns: id, name, email, phone, country, region, status. */
    private static Customer customerRow(ResultSet rs) throws SQLException {
        return new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"),
                rs.getString("country"), Region.valueOf(rs.getString("region")), CustomerStatus.valueOf(rs.getString("status")));
    }

    /** Columns: id, sku, name, category, unit_price, unit_of_measure, active. */
    private static Product productRow(ResultSet rs) throws SQLException {
        return new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"), rs.getString("category"),
                rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"), rs.getBoolean("active"));
    }

    /** Columns: id, number, order_id, customer_id, invoice_date, due_date, amount, status. */
    private static Invoice invoiceRow(ResultSet rs) throws SQLException {
        return new Invoice(rs.getLong("id"), rs.getString("number"), rs.getLong("order_id"), rs.getLong("customer_id"),
                rs.getObject("invoice_date", LocalDate.class), rs.getObject("due_date", LocalDate.class),
                rs.getBigDecimal("amount"), InvoiceStatus.valueOf(rs.getString("status")));
    }

    /** Columns: id, order_id, warehouse_id, carrier_id, tracking_number, ship_date, delivery_date. */
    private static Shipment shipmentRow(ResultSet rs) throws SQLException {
        return new Shipment(rs.getLong("id"), rs.getLong("order_id"), rs.getLong("warehouse_id"), rs.getLong("carrier_id"),
                rs.getString("tracking_number"), rs.getObject("ship_date", LocalDate.class),
                rs.getObject("delivery_date", LocalDate.class));
    }

    /** Columns: id, number, customer_id, order_date, status; loads the order's lines with a second query. */
    private SalesOrder salesOrderRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        return new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), salesOrderLines(id));
    }

    private List<OrderLine> salesOrderLines(long orderId) {
        return queryList("SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no",
                rs -> new OrderLine(rs.getLong("product_id"), rs.getInt("quantity"), rs.getBigDecimal("unit_price"),
                        rs.getString("promotion_code")), orderId);
    }

    /** Columns: id, number, supplier_id, order_date, expected_date, status, warehouse_id; loads the lines too. */
    private PurchaseOrder purchaseOrderRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        return new PurchaseOrder(id, rs.getString("number"), rs.getLong("supplier_id"),
                rs.getObject("order_date", LocalDate.class), rs.getObject("expected_date", LocalDate.class),
                PurchaseOrderStatus.valueOf(rs.getString("status")), purchaseOrderLines(id), rs.getLong("warehouse_id"));
    }

    private List<PurchaseOrderLine> purchaseOrderLines(long orderId) {
        return queryList("SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no",
                rs -> new PurchaseOrderLine(rs.getLong("product_id"), rs.getInt("quantity"), rs.getBigDecimal("unit_cost")),
                orderId);
    }

    // ===== Customers: lookups, lists, closing, credit limit, currency, customer sales reports =====

    public Customer createCustomer(Customer customer) {
        long id = insert("INSERT INTO customer (name, email, phone, country, region, status) VALUES (?, ?, ?, ?, ?, ?)",
                customer.name(), customer.email(), customer.phone(), customer.country(), customer.region().name(),
                customer.status().name());
        return new Customer(id, customer.name(), customer.email(), customer.phone(), customer.country(),
                customer.region(), customer.status());
    }

    public void updateCustomer(Customer customer) {
        update("UPDATE customer SET name = ?, email = ?, phone = ?, country = ?, region = ?, status = ? WHERE id = ?",
                customer.name(), customer.email(), customer.phone(), customer.country(), customer.region().name(),
                customer.status().name(), customer.id());
    }

    /** Sets the credit limit of the customer; null removes the limit. */
    public void updateCustomerCreditLimit(long customerId, BigDecimal creditLimit) {
        update("UPDATE customer SET credit_limit = ? WHERE id = ?", creditLimit, customerId);
    }

    /** Sets the date the customer was created; null removes it. */
    public void updateCustomerCreatedDate(long customerId, LocalDate createdDate) {
        update("UPDATE customer SET created_date = ? WHERE id = ?", createdDate, customerId);
    }

    /** The credit limit of the customer; empty if the customer has no limit or does not exist. */
    public Optional<BigDecimal> findCustomerCreditLimit(long customerId) {
        return queryOptional("SELECT credit_limit FROM customer WHERE id = ?",
                rs -> Optional.ofNullable(rs.getBigDecimal("credit_limit")), customerId).flatMap(limit -> limit);
    }

    public Optional<Customer> findCustomerById(long id) {
        return queryOptional("SELECT id, name, email, phone, country, region, status FROM customer WHERE id = ?",
                DataStore::customerRow, id);
    }

    /** All customers that are not closed, as for {@link #findCustomers(CustomerQuery)}. */
    public List<Customer> findNotClosedCustomersByName() {
        return findCustomers(CustomerQuery.notClosed());
    }

    /**
     * The customers that are not closed whose name contains the part, ignoring case (LIKE wildcards in the part match
     * literally), as for {@link #findCustomers(CustomerQuery)}.
     */
    public List<Customer> findNotClosedCustomersWithNameContaining(String namePart) {
        return findCustomers(CustomerQuery.notClosed().nameContaining(namePart));
    }

    /** The customers of the region that are not closed, as for {@link #findCustomers(CustomerQuery)}. */
    public List<Customer> findNotClosedCustomersInRegion(Region region) {
        return findCustomers(CustomerQuery.notClosed().inRegion(region));
    }

    /** The customers of the country (exact match) that are not closed, as for {@link #findCustomers(CustomerQuery)}. */
    public List<Customer> findNotClosedCustomersInCountry(String country) {
        return findCustomers(CustomerQuery.notClosed().inCountry(country));
    }

    /** The customers with the status that are not closed, as for {@link #findCustomers(CustomerQuery)}. */
    public List<Customer> findNotClosedCustomersWithStatus(CustomerStatus status) {
        return findCustomers(CustomerQuery.notClosed().withStatus(status));
    }

    /** The customers assigned to the sales rep that are not closed, as for {@link #findCustomers(CustomerQuery)}. */
    public List<Customer> findNotClosedCustomersOfSalesRep(long salesRepId) {
        return findCustomers(CustomerQuery.notClosed().assignedToSalesRep(salesRepId));
    }

    /**
     * Selects customers for {@link #findCustomers(CustomerQuery)}. Every query leaves closed customers (those with a
     * customer_closure row) out; the filters narrow it further and combine with AND.
     */
    private static final class CustomerQuery {
        private final StringBuilder conditions = new StringBuilder();
        private final List<Object> params = new ArrayList<>();

        private CustomerQuery() {
        }

        /** All customers that are not closed. */
        static CustomerQuery notClosed() {
            return new CustomerQuery();
        }

        /** Only customers whose name contains the part, ignoring case (LIKE wildcards in the part match literally). */
        CustomerQuery nameContaining(String namePart) {
            return where("LOWER(name) LIKE ? ESCAPE '\\'", containsPattern(namePart));
        }

        /** Only customers of the region. */
        CustomerQuery inRegion(Region region) {
            return where("region = ?", region.name());
        }

        /** Only customers of the country (exact match). */
        CustomerQuery inCountry(String country) {
            return where("country = ?", country);
        }

        /** Only customers with the status. */
        CustomerQuery withStatus(CustomerStatus status) {
            return where("status = ?", status.name());
        }

        /** Only customers assigned to the sales rep. */
        CustomerQuery assignedToSalesRep(long salesRepId) {
            return where("sales_rep_id = ?", salesRepId);
        }

        private CustomerQuery where(String condition, Object param) {
            conditions.append(condition).append(" AND ");
            params.add(param);
            return this;
        }
    }

    /** The customers selected by the query (never closed ones), ordered by name, then id. */
    private List<Customer> findCustomers(CustomerQuery query) {
        return queryList("SELECT id, name, email, phone, country, region, status FROM customer WHERE " + query.conditions
                + "NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = customer.id) ORDER BY name, id",
                DataStore::customerRow, query.params.toArray());
    }

    /** Closes the customer without deleting anything; returns false if it was already closed. */
    public boolean closeCustomer(long customerId, LocalDate closedDate) {
        return update("INSERT INTO customer_closure (customer_id, closed_date) SELECT c.id, ? FROM customer c "
                + "WHERE c.id = ? AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id)",
                closedDate, customerId) > 0;
    }

    /** Reopens a closed customer; returns false if it was not closed. */
    public boolean reopenCustomer(long customerId) {
        return update("DELETE FROM customer_closure WHERE customer_id = ?", customerId) > 0;
    }

    /** The closed customers, ordered by name, then id. */
    public List<Customer> findClosedCustomers() {
        return queryList("SELECT c.id, c.name, c.email, c.phone, c.country, c.region, c.status FROM customer c "
                + "JOIN customer_closure cl ON cl.customer_id = c.id ORDER BY c.name, c.id", DataStore::customerRow);
    }

    /** The N most recently created customers, as for {@link #latestCreatedCustomers}. */
    public List<Customer> findLatestCreatedCustomers(int limit) {
        return latestCreatedCustomers("", "", limit);
    }

    /** The N most recently created customers in the region, as for {@link #latestCreatedCustomers}. */
    public List<Customer> findLatestCreatedCustomersInRegion(Region region, int limit) {
        return latestCreatedCustomers("", "c.region = ? AND ", limit, region.name());
    }

    /**
     * The N customers created most recently before the given customer, i.e. the ones that joined just ahead of it (the
     * customer itself is left out, and nothing is found when it has no created date), as for
     * {@link #latestCreatedCustomers}. Customers created on the same date count as earlier when their id is lower.
     */
    public List<Customer> findCustomersCreatedJustBefore(long customerId, int limit) {
        return latestCreatedCustomers(", customer ref",
                "ref.id = ? AND ref.created_date IS NOT NULL "
                        + "AND (c.created_date < ref.created_date OR (c.created_date = ref.created_date AND c.id < ref.id)) AND ",
                limit, customerId);
    }

    /**
     * The N most recently created customers that match the extra condition (customers without a created date and
     * closed customers are left out), newest first, then by highest id. The condition ends with "AND "; its params come
     * before the limit.
     */
    private List<Customer> latestCreatedCustomers(String extraTables, String condition, int limit, Object... conditionParams) {
        Object[] params = java.util.Arrays.copyOf(conditionParams, conditionParams.length + 1);
        params[conditionParams.length] = limit;
        return queryList("SELECT c.id, c.name, c.email, c.phone, c.country, c.region, c.status FROM customer c" + extraTables
                + " WHERE " + condition + "c.created_date IS NOT NULL "
                + "AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "ORDER BY c.created_date DESC, c.id DESC LIMIT ?", DataStore::customerRow, params);
    }

    /**
     * The customers of the country created between the two dates, inclusive (customers without a created date are left
     * out; closed customers are included), oldest first, then by id.
     */
    public List<Customer> findCustomersInCountryCreatedBetween(String country, LocalDate from, LocalDate to) {
        return queryList("SELECT c.id, c.name, c.email, c.phone, c.country, c.region, c.status FROM customer c "
                + "WHERE c.country = ? AND c.created_date >= ? AND c.created_date <= ? "
                + "ORDER BY c.created_date, c.id", DataStore::customerRow, country, from, to);
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, per customer with
     * such orders, as for {@link #salesPerCustomerWithOrders}.
     */
    public List<CustomerSales> findSalesPerCustomerWithOrders(LocalDate from, LocalDate to) {
        return salesPerCustomerWithOrders(null, null, from, to);
    }

    /**
     * The customers of the region with the highest revenue, as for {@link #salesPerCustomerWithOrders}, at most the
     * given number of them.
     */
    public List<CustomerSales> findTopSalesPerCustomerWithOrdersInRegion(Region region, int limit, LocalDate from, LocalDate to) {
        return salesPerCustomerWithOrders(region.name(), limit, from, to);
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, per customer.
     * Customers without such orders are left out; closed customers are included. Sorted by revenue, highest first (then
     * by customer name, then id). Optionally only customers of one region (null: all) and at most a number of rows
     * (null: all).
     */
    private List<CustomerSales> salesPerCustomerWithOrders(String regionName, Integer limit, LocalDate from, LocalDate to) {
        List<Object> params = new ArrayList<>();
        params.add(from);
        params.add(to);
        String regionCondition = "";
        if (regionName != null) {
            regionCondition = "AND c.region = ? ";
            params.add(regionName);
        }
        String limitClause = "";
        if (limit != null) {
            limitClause = " LIMIT ?";
            params.add(limit);
        }
        return queryList("SELECT c.id AS id, c.name AS name, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' " + regionCondition
                + "GROUP BY c.id, c.name ORDER BY revenue DESC, c.name, c.id" + limitClause,
                rs -> new CustomerSales(rs.getLong("id"), rs.getString("name"), rs.getInt("order_count"),
                        rs.getBigDecimal("revenue")), params.toArray());
    }

    /**
     * The customers with at least the given number of non-cancelled orders placed between the two dates, inclusive,
     * with their order counts. Grouped per region of the customer (regions in alphabetical order), most orders first
     * within a region (then by customer name, then id).
     */
    public List<RepeatCustomer> findRepeatCustomersPerRegion(int minOrders, LocalDate from, LocalDate to) {
        return queryList("SELECT c.id AS id, c.name AS name, c.region AS region, COUNT(DISTINCT o.id) AS order_count "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY c.id, c.name, c.region HAVING COUNT(DISTINCT o.id) >= ? "
                + "ORDER BY c.region, order_count DESC, c.name, c.id",
                rs -> new RepeatCustomer(Region.valueOf(rs.getString("region")), rs.getLong("id"), rs.getString("name"),
                        rs.getInt("order_count")), from, to, minOrders);
    }

    /**
     * The customers whose first non-cancelled order (over all time) was placed between the two dates, inclusive, with
     * the date of that order and the customer's sales rep (null when none). Grouped per region of the customer
     * (alphabetical), then per sales rep name (customers without a rep last), then first order date, customer name, id.
     */
    public List<FirstTimeCustomer> findFirstTimeCustomers(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id AS id, c.name AS name, c.region AS region, MIN(o.order_date) AS first_order, "
                + "r.id AS rep_id, r.name AS rep_name, r.email AS rep_email, r.region AS rep_region "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id LEFT JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE o.status <> 'CANCELLED' "
                + "GROUP BY c.id, c.name, c.region, r.id, r.name, r.email, r.region "
                + "HAVING MIN(o.order_date) >= ? AND MIN(o.order_date) <= ? "
                + "ORDER BY c.region, r.name NULLS LAST, r.id, first_order, c.name, c.id", rs -> {
                    SalesRep salesRep = null;
                    if (rs.getObject("rep_id") != null) {
                        salesRep = new SalesRep(rs.getLong("rep_id"), rs.getString("rep_name"), rs.getString("rep_email"),
                                Region.valueOf(rs.getString("rep_region")));
                    }
                    return new FirstTimeCustomer(Region.valueOf(rs.getString("region")), salesRep, rs.getLong("id"),
                            rs.getString("name"), rs.getObject("first_order", LocalDate.class));
                }, from, to);
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, for every customer
     * that is not closed, as for {@link #salesPerNotClosedCustomerIncludingIdle}.
     */
    public List<CustomerSales> findSalesPerNotClosedCustomerIncludingIdle(LocalDate from, LocalDate to) {
        return salesPerNotClosedCustomerIncludingIdle(rs -> new CustomerSales(rs.getLong("id"), rs.getString("name"),
                rs.getInt("order_count"), rs.getBigDecimal("revenue")), from, to);
    }

    /**
     * The customers of the sales rep that are not closed, with their revenue from non-cancelled orders placed between
     * the two dates (inclusive, 0 if none), the date of their last non-cancelled order (null if none) and the unpaid
     * part of their open invoices (0 if none). Sorted by customer name, then id.
     */
    public List<SalesRepCustomer> findCustomerReportForSalesRep(long salesRepId, LocalDate from, LocalDate to) {
        return queryList("SELECT c.id AS id, c.name AS name, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o "
                + "JOIN sales_order_line l ON l.order_id = o.id WHERE o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') AS revenue, "
                + "(SELECT MAX(o.order_date) FROM sales_order o WHERE o.customer_id = c.id AND o.status <> 'CANCELLED') AS last_order, "
                + "(SELECT COALESCE(SUM(i.amount - (SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id)), 0) FROM invoice i WHERE i.customer_id = c.id AND i.status = 'OPEN') AS open_amount "
                + "FROM customer c WHERE c.sales_rep_id = ? "
                + "AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "ORDER BY c.name, c.id",
                rs -> new SalesRepCustomer(rs.getLong("id"), rs.getString("name"), rs.getBigDecimal("revenue"),
                        rs.getObject("last_order", LocalDate.class), rs.getBigDecimal("open_amount")),
                from, to, salesRepId);
    }

    /**
     * Number and total value of the non-cancelled orders placed between the two dates, inclusive, for every customer
     * that is not closed, with the region of the customer, as for {@link #salesPerNotClosedCustomerIncludingIdle}.
     */
    public List<CustomerRegionSales> findSalesWithRegionPerNotClosedCustomerIncludingIdle(LocalDate from, LocalDate to) {
        return salesPerNotClosedCustomerIncludingIdle(rs -> new CustomerRegionSales(rs.getLong("id"), rs.getString("name"),
                Region.valueOf(rs.getString("region")), rs.getInt("order_count"), rs.getBigDecimal("revenue")), from, to);
    }

    /**
     * Number and total value (columns id, name, region, order_count, revenue) of the non-cancelled orders placed between
     * the two dates, inclusive, for every customer that is not closed; customers without such orders are included with
     * order count and revenue 0. Sorted by revenue, highest first (then by customer name, then id).
     */
    private <T> List<T> salesPerNotClosedCustomerIncludingIdle(RowMapper<T> mapper, LocalDate from, LocalDate to) {
        return queryList("SELECT c.id AS id, c.name AS name, c.region AS region, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                + "FROM customer c LEFT JOIN sales_order o ON o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "GROUP BY c.id, c.name, c.region ORDER BY revenue DESC, c.name, c.id", mapper, from, to);
    }

    /**
     * The active customers without a non-cancelled order placed between the two dates, inclusive (closed customers are
     * left out). Sorted by region, then by name, then id.
     */
    public List<Customer> findActiveCustomersWithoutOrders(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id AS id, c.name AS name, c.email AS email, c.phone AS phone, c.country AS country, "
                + "c.region AS region, c.status AS status FROM customer c "
                + "WHERE c.status = 'ACTIVE' AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "AND NOT EXISTS (SELECT 1 FROM sales_order o WHERE o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') "
                + "ORDER BY c.region, c.name, c.id", DataStore::customerRow, from, to);
    }

    /**
     * The active customers with a non-cancelled order placed between the two dates, inclusive, but without a
     * non-cancelled invoice dated between the two dates, inclusive (closed customers are left out). Sorted by region,
     * then by name, then id.
     */
    public List<Customer> findActiveCustomersWithOrdersButWithoutInvoices(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id AS id, c.name AS name, c.email AS email, c.phone AS phone, c.country AS country, "
                + "c.region AS region, c.status AS status FROM customer c "
                + "WHERE c.status = 'ACTIVE' AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id) "
                + "AND EXISTS (SELECT 1 FROM sales_order o WHERE o.customer_id = c.id "
                + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') "
                + "AND NOT EXISTS (SELECT 1 FROM invoice i WHERE i.customer_id = c.id "
                + "AND i.invoice_date >= ? AND i.invoice_date <= ? AND i.status <> 'CANCELLED') "
                + "ORDER BY c.region, c.name, c.id", DataStore::customerRow, from, to, from, to);
    }

    /**
     * First and last order date, number and total value of the non-cancelled orders of the customer, the number of
     * its returns, and the sum of its payments and credit notes. Dates are null and the rest zero if there is nothing.
     */
    public CustomerActivity findCustomerActivity(long customerId) {
        return querySingle("SELECT "
                + "(SELECT MIN(order_date) FROM sales_order WHERE customer_id = ? AND status <> 'CANCELLED') AS first_order, "
                + "(SELECT MAX(order_date) FROM sales_order WHERE customer_id = ? AND status <> 'CANCELLED') AS last_order, "
                + "(SELECT COUNT(*) FROM sales_order WHERE customer_id = ? AND status <> 'CANCELLED') AS order_count, "
                + "(SELECT COUNT(*) FROM sales_return r JOIN sales_order o ON o.id = r.order_id WHERE o.customer_id = ?) AS return_count, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.customer_id = ? AND o.status <> 'CANCELLED') AS ordered, "
                + "(SELECT COALESCE(SUM(p.amount), 0) FROM payment p JOIN invoice i ON i.id = p.invoice_id WHERE i.customer_id = ?) AS paid, "
                + "(SELECT COALESCE(SUM(amount), 0) FROM credit_note WHERE customer_id = ?) AS credited",
                rs -> new CustomerActivity(rs.getObject("first_order", LocalDate.class),
                        rs.getObject("last_order", LocalDate.class), rs.getInt("order_count"), rs.getInt("return_count"),
                        rs.getBigDecimal("ordered"), rs.getBigDecimal("paid"), rs.getBigDecimal("credited")),
                customerId, customerId, customerId, customerId, customerId, customerId, customerId);
    }

    /** Sets the currency the customer is invoiced in, replacing an earlier one. */
    public void saveCustomerCurrency(long customerId, Currency currency) {
        update("MERGE INTO customer_currency (customer_id, currency) KEY (customer_id) VALUES (?, ?)", customerId,
                currency.name());
    }

    /** The currency the customer is invoiced in, if one was set (callers use EUR otherwise). */
    public Optional<Currency> findCustomerCurrency(long customerId) {
        return queryOptional("SELECT currency FROM customer_currency WHERE customer_id = ?",
                rs -> Currency.valueOf(rs.getString("currency")), customerId);
    }

    // ===== Customer addresses and contacts =====

    /** Columns: id, customer_id, type, street, city, postal_code, country, default_shipping. */
    private static CustomerAddress customerAddressRow(ResultSet rs) throws SQLException {
        return new CustomerAddress(rs.getLong("id"), rs.getLong("customer_id"), AddressType.valueOf(rs.getString("type")),
                rs.getString("street"), rs.getString("city"), rs.getString("postal_code"), rs.getString("country"),
                rs.getBoolean("default_shipping"));
    }

    public CustomerAddress createCustomerAddress(CustomerAddress address) {
        long id = insert("INSERT INTO customer_address (customer_id, type, street, city, postal_code, country, default_shipping) VALUES (?, ?, ?, ?, ?, ?, ?)",
                address.customerId(), address.type().name(), address.street(), address.city(), address.postalCode(),
                address.country(), address.defaultShipping());
        return new CustomerAddress(id, address.customerId(), address.type(), address.street(), address.city(),
                address.postalCode(), address.country(), address.defaultShipping());
    }

    public void updateCustomerAddress(CustomerAddress address) {
        update("UPDATE customer_address SET type = ?, street = ?, city = ?, postal_code = ?, country = ? WHERE id = ?",
                address.type().name(), address.street(), address.city(), address.postalCode(), address.country(),
                address.id());
    }

    public Optional<CustomerAddress> findCustomerAddressById(long id) {
        return queryOptional("SELECT id, customer_id, type, street, city, postal_code, country, default_shipping FROM customer_address WHERE id = ?",
                DataStore::customerAddressRow, id);
    }

    /** The addresses of the customer, ordered by id. */
    public List<CustomerAddress> findCustomerAddresses(long customerId) {
        return queryList("SELECT id, customer_id, type, street, city, postal_code, country, default_shipping FROM customer_address WHERE customer_id = ? ORDER BY id",
                DataStore::customerAddressRow, customerId);
    }

    /** Makes the given address the only default among the shipping addresses of the customer. */
    public void setDefaultShippingAddress(long customerId, long addressId) {
        update("UPDATE customer_address SET default_shipping = (id = ?) WHERE customer_id = ? AND type = 'SHIPPING'",
                addressId, customerId);
    }

    /** Columns: id, customer_id, name, role, email, phone. */
    private static CustomerContact customerContactRow(ResultSet rs) throws SQLException {
        return new CustomerContact(rs.getLong("id"), rs.getLong("customer_id"), rs.getString("name"), rs.getString("role"),
                rs.getString("email"), rs.getString("phone"));
    }

    public CustomerContact createCustomerContact(CustomerContact contact) {
        long id = insert("INSERT INTO customer_contact (customer_id, name, role, email, phone) VALUES (?, ?, ?, ?, ?)",
                contact.customerId(), contact.name(), contact.role(), contact.email(), contact.phone());
        return new CustomerContact(id, contact.customerId(), contact.name(), contact.role(), contact.email(), contact.phone());
    }

    /** A contact (of any customer) with the email, ignoring case; any one of them if several match. */
    public Optional<CustomerContact> findCustomerContactByEmail(String email) {
        return queryOptional("SELECT id, customer_id, name, role, email, phone FROM customer_contact WHERE LOWER(email) = LOWER(?)",
                DataStore::customerContactRow, email);
    }

    /** The contacts of one customer, ordered by id. */
    public List<CustomerContact> findCustomerContactsOfCustomer(long customerId) {
        return queryList("SELECT id, customer_id, name, role, email, phone FROM customer_contact WHERE customer_id = ? ORDER BY id",
                DataStore::customerContactRow, customerId);
    }

    /** The contacts (of all customers) with the role, ignoring case, ordered by customer id, then contact id. */
    public List<CustomerContact> findCustomerContactsByRole(String role) {
        return queryList("SELECT id, customer_id, name, role, email, phone FROM customer_contact WHERE LOWER(role) = LOWER(?) ORDER BY customer_id, id",
                DataStore::customerContactRow, role);
    }

    /** Contacts of the customers of the region, sorted by role ignoring case, then customer name, customer id and contact id. */
    public List<RegionContact> findCustomerContactsInRegion(Region region) {
        return queryList("SELECT k.id, k.customer_id, k.name, k.role, k.email, k.phone, c.name AS customer_name "
                + "FROM customer_contact k JOIN customer c ON c.id = k.customer_id WHERE c.region = ? "
                + "ORDER BY LOWER(k.role), c.name, c.id, k.id",
                rs -> new RegionContact(rs.getString("customer_name"), customerContactRow(rs)), region.name());
    }

    // ===== Sales reps, region managers, sales targets and commission =====

    /** Columns: id, name, email, region. */
    private static SalesRep salesRepRow(ResultSet rs) throws SQLException {
        return new SalesRep(rs.getLong("id"), rs.getString("name"), rs.getString("email"), Region.valueOf(rs.getString("region")));
    }

    public SalesRep createSalesRep(SalesRep salesRep) {
        long id = insert("INSERT INTO sales_rep (name, email, region) VALUES (?, ?, ?)",
                salesRep.name(), salesRep.email(), salesRep.region().name());
        return new SalesRep(id, salesRep.name(), salesRep.email(), salesRep.region());
    }

    /** All sales reps, sorted by name, then id. */
    public List<SalesRep> findAllSalesRepsOrderedByName() {
        return queryList("SELECT id, name, email, region FROM sales_rep ORDER BY name, id", DataStore::salesRepRow);
    }

    /** The sales reps of the region, sorted by name, then id. */
    public List<SalesRep> findSalesRepsInRegionOrderedByName(Region region) {
        return queryList("SELECT id, name, email, region FROM sales_rep WHERE region = ? ORDER BY name, id",
                DataStore::salesRepRow, region.name());
    }

    public Optional<SalesRep> findSalesRepById(long salesRepId) {
        return queryOptional("SELECT id, name, email, region FROM sales_rep WHERE id = ?", DataStore::salesRepRow, salesRepId);
    }

    /** Sets (or replaces) the manager of the region. */
    public void saveRegionManager(Region region, long salesRepId) {
        update("MERGE INTO region_manager (region, sales_rep_id) KEY (region) VALUES (?, ?)", region.name(), salesRepId);
    }

    /** The sales rep managing the region; empty if the region has no manager. */
    public Optional<SalesRep> findRegionManager(Region region) {
        return queryOptional("SELECT r.id, r.name, r.email, r.region FROM sales_rep r "
                + "JOIN region_manager m ON m.sales_rep_id = r.id WHERE m.region = ?", DataStore::salesRepRow, region.name());
    }

    /** The regions managed by the rep, in name order. */
    public List<Region> findRegionsManagedBySalesRep(long salesRepId) {
        return queryList("SELECT region FROM region_manager WHERE sales_rep_id = ? ORDER BY region",
                rs -> Region.valueOf(rs.getString("region")), salesRepId);
    }

    public void assignCustomerToSalesRep(long customerId, long salesRepId) {
        update("UPDATE customer SET sales_rep_id = ? WHERE id = ?", salesRepId, customerId);
    }

    /** Number and total value of the non-cancelled orders of the rep's customers placed between the two dates, inclusive. */
    public RepPerformance findSalesRepPerformance(long salesRepId, LocalDate from, LocalDate to) {
        String sql = "SELECT COUNT(DISTINCT o.id) AS order_count, COALESCE(SUM(l.quantity * l.unit_price), 0) AS total "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE c.sales_rep_id = ? AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED'";
        return querySingle(sql, rs -> new RepPerformance(rs.getInt("order_count"), rs.getBigDecimal("total")),
                salesRepId, from, to);
    }

    /** Sets the target of the rep for the month starting at the given date, replacing an existing one. */
    public void saveSalesTarget(long salesRepId, LocalDate month, BigDecimal target) {
        update("MERGE INTO sales_target (sales_rep_id, target_month, target_amount) KEY (sales_rep_id, target_month) VALUES (?, ?, ?)",
                salesRepId, month, target);
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
        return queryList(sql, rs -> {
            BigDecimal target = rs.getBigDecimal("target");
            BigDecimal revenue = rs.getBigDecimal("revenue");
            return new SalesTargetProgress(rs.getLong("id"), rs.getString("name"), target, revenue,
                    revenue.multiply(new BigDecimal("100")).divide(target, 2, RoundingMode.HALF_UP));
        }, monthStart, monthEnd, monthStart);
    }

    /**
     * Commission basis of a rep: the total of the paid invoices of the rep's customers dated between the two dates
     * (parameters 1 and 2), inclusive, and the rep's target for the month starting at the first date (parameter 3;
     * null if none). Selects every rep; append a WHERE or ORDER BY.
     */
    private static final String COMMISSION_BASIS_SELECT = "SELECT r.id AS id, r.name AS name, "
            + "COALESCE((SELECT SUM(i.amount) FROM invoice i JOIN customer c ON c.id = i.customer_id "
            + "WHERE c.sales_rep_id = r.id AND i.status = 'PAID' AND i.invoice_date >= ? AND i.invoice_date <= ?), 0) AS paid, "
            + "t.target_amount AS target "
            + "FROM sales_rep r LEFT JOIN sales_target t ON t.sales_rep_id = r.id AND t.target_month = ? ";

    /** Columns: id, name, paid, target. */
    private static CommissionBasis commissionBasisRow(ResultSet rs) throws SQLException {
        return new CommissionBasis(rs.getLong("id"), rs.getString("name"), rs.getBigDecimal("paid"), rs.getBigDecimal("target"));
    }

    /**
     * The total of the paid invoices of the rep's customers dated between the two dates, inclusive, and the rep's
     * target for the month starting at the first date (null if none). Empty if the rep does not exist.
     */
    public Optional<CommissionBasis> findCommissionBasisOfSalesRep(long salesRepId, LocalDate monthStart, LocalDate monthEnd) {
        return queryOptional(COMMISSION_BASIS_SELECT + "WHERE r.id = ?", DataStore::commissionBasisRow,
                monthStart, monthEnd, monthStart, salesRepId);
    }

    /** Like findCommissionBasisOfSalesRep, for every rep, sorted by rep name, then id. */
    public List<CommissionBasis> findCommissionBasisPerSalesRep(LocalDate monthStart, LocalDate monthEnd) {
        return queryList(COMMISSION_BASIS_SELECT + "ORDER BY r.name, r.id", DataStore::commissionBasisRow,
                monthStart, monthEnd, monthStart);
    }

    // ===== Products, price changes, bundles, categories, product sales and margins =====

    public Product createProduct(Product product) {
        long id = insert("INSERT INTO product (sku, name, category, unit_price, unit_of_measure, active) VALUES (?, ?, ?, ?, ?, ?)",
                product.sku(), product.name(), product.category(), product.unitPrice(), product.unitOfMeasure(), product.active());
        return new Product(id, product.sku(), product.name(), product.category(), product.unitPrice(),
                product.unitOfMeasure(), product.active());
    }

    public void updateProduct(Product product) {
        update("UPDATE product SET sku = ?, name = ?, category = ?, unit_price = ?, unit_of_measure = ?, active = ? WHERE id = ?",
                product.sku(), product.name(), product.category(), product.unitPrice(), product.unitOfMeasure(),
                product.active(), product.id());
    }

    /** Deactivates or reactivates the product; returns false if it does not exist or already has that state. */
    public boolean setProductActive(long productId, boolean active) {
        return update("UPDATE product SET active = ? WHERE id = ? AND active <> ?", active, productId, active) > 0;
    }

    /**
     * Sets the catalogue price and records the change (old and new price) in the history, all or nothing. Returns null
     * if the price is unchanged; throws IllegalStateException("Unknown product: id") if the product does not exist.
     */
    public PriceChange changeProductPrice(long productId, LocalDate date, BigDecimal newPrice) {
        return inTransaction(() -> {
            BigDecimal before = queryOptional("SELECT unit_price FROM product WHERE id = ?",
                    rs -> rs.getBigDecimal("unit_price"), productId)
                    .orElseThrow(() -> new IllegalStateException("Unknown product: " + productId));
            if (before.compareTo(newPrice) == 0) {
                return null;
            }
            update("UPDATE product SET unit_price = ? WHERE id = ?", newPrice, productId);
            long id = insert("INSERT INTO product_price_change (product_id, change_date, old_price, new_price) VALUES (?, ?, ?, ?)",
                    productId, date, before, newPrice);
            return new PriceChange(id, productId, date, before, newPrice);
        });
    }

    /** The price changes of the product, oldest first (then by id). */
    public List<PriceChange> findPriceChangesByProduct(long productId) {
        return queryList("SELECT id, product_id, change_date, old_price, new_price FROM product_price_change "
                        + "WHERE product_id = ? ORDER BY change_date, id",
                rs -> new PriceChange(rs.getLong("id"), rs.getLong("product_id"), rs.getObject("change_date", LocalDate.class),
                        rs.getBigDecimal("old_price"), rs.getBigDecimal("new_price")),
                productId);
    }

    /** The active products, ordered by name (then by id). */
    public List<Product> findActiveProducts() {
        return findProductsMatching(ProductQuery.activeProducts());
    }

    /** The active products whose name contains the text, ignoring case (no wildcards), ordered by name (then by id). */
    public List<Product> findActiveProductsByNameContaining(String namePart) {
        return findProductsMatching(ProductQuery.activeProducts().nameContains(namePart));
    }

    /** The active products of exactly this category (not of the categories below it), ordered by name (then by id). */
    public List<Product> findActiveProductsInCategory(String category) {
        return findProductsMatching(ProductQuery.activeProducts().inCategory(category));
    }

    /** The active products of the category or of any category below it, ordered by name (then by id). */
    public List<Product> findActiveProductsInCategoryTree(String categoryCode) {
        return findProductsMatching(ProductQuery.activeProducts().inCategoryTree(categoryCode));
    }

    /** The active products whose catalogue price lies between the two prices, inclusive, ordered by name (then by id). */
    public List<Product> findActiveProductsByUnitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return findProductsMatching(ProductQuery.activeProducts().unitPriceBetween(minPrice, maxPrice));
    }

    /** The products (active or not) with a price change between the two dates, inclusive, by name (then by id). */
    public List<Product> findProductsWithPriceChangeBetween(LocalDate from, LocalDate to) {
        return findProductsMatching(ProductQuery.allProducts().withPriceChangeBetween(from, to));
    }

    /** The bundles (products, active or not, that have components), ordered by name (then by id). */
    public List<Product> findBundles() {
        return findProductsMatching(ProductQuery.allProducts().bundles());
    }

    /** The bundles (active or not) that contain the product as a component, ordered by name (then by id). */
    public List<Product> findBundlesContainingComponent(long componentId) {
        return findProductsMatching(ProductQuery.allProducts().bundlesContainingComponent(componentId));
    }

    /**
     * Which products {@link #findProductsMatching} returns: start with {@link #activeProducts()} or
     * {@link #allProducts()} and add filters; all filters must match.
     */
    private static final class ProductQuery {
        private final boolean activeOnly;
        private final List<String> conditions = new ArrayList<>();
        private final List<Object> params = new ArrayList<>();
        private boolean inCategoryTree;
        private String categoryTreeRoot;

        private ProductQuery(boolean activeOnly) {
            this.activeOnly = activeOnly;
        }

        /** Only active products. */
        public static ProductQuery activeProducts() {
            return new ProductQuery(true);
        }

        /** Active and inactive products. */
        public static ProductQuery allProducts() {
            return new ProductQuery(false);
        }

        /** Products whose name contains the text, ignoring case (no wildcards). */
        public ProductQuery nameContains(String namePart) {
            return where("LOWER(name) LIKE ? ESCAPE '\\'", containsPattern(namePart));
        }

        /** Products of exactly this category (not of the categories below it). */
        public ProductQuery inCategory(String category) {
            return where("category = ?", category);
        }

        /** Products of the category or of any category below it. */
        public ProductQuery inCategoryTree(String categoryCode) {
            this.inCategoryTree = true;
            this.categoryTreeRoot = categoryCode;
            conditions.add("category IN (SELECT code FROM tree)");
            return this;
        }

        /** Products whose catalogue price lies between the two prices, inclusive. */
        public ProductQuery unitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
            return where("unit_price >= ? AND unit_price <= ?", minPrice, maxPrice);
        }

        /** Products with a price change between the two dates, inclusive. */
        public ProductQuery withPriceChangeBetween(LocalDate from, LocalDate to) {
            return where("EXISTS (SELECT 1 FROM product_price_change c WHERE c.product_id = p.id "
                    + "AND c.change_date >= ? AND c.change_date <= ?)", from, to);
        }

        /** Bundles: products that have components. */
        public ProductQuery bundles() {
            return where("id IN (SELECT bundle_id FROM bundle_component)");
        }

        /** Bundles that contain the product as a component. */
        public ProductQuery bundlesContainingComponent(long componentId) {
            return where("id IN (SELECT bundle_id FROM bundle_component WHERE component_id = ?)", componentId);
        }

        private ProductQuery where(String condition, Object... values) {
            conditions.add(condition);
            for (Object value : values) {
                params.add(value);
            }
            return this;
        }
    }

    /** The products selected by the query, ordered by name (then by id). */
    private List<Product> findProductsMatching(ProductQuery query) {
        List<String> conditions = new ArrayList<>();
        if (query.activeOnly) {
            conditions.add("active = TRUE");
        }
        conditions.addAll(query.conditions);
        List<Object> params = new ArrayList<>();
        String sql = "";
        if (query.inCategoryTree) {
            sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                    + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) ";
            params.add(query.categoryTreeRoot);
        }
        params.addAll(query.params);
        sql += "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product p"
                + (conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions)) + " ORDER BY name, id";
        return queryList(sql, DataStore::productRow, params.toArray());
    }

    public void saveProductWeight(long productId, BigDecimal weightKg) {
        update("MERGE INTO product_weight (product_id, weight_kg) KEY (product_id) VALUES (?, ?)", productId, weightKg);
    }

    public Optional<Product> findProductBySku(String sku) {
        return queryOptional("SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product WHERE sku = ?",
                DataStore::productRow, sku);
    }

    public Optional<Product> findProductById(long id) {
        return queryOptional("SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product WHERE id = ?",
                DataStore::productRow, id);
    }

    public void createBundleComponent(long bundleId, long componentId, int quantity) {
        update("INSERT INTO bundle_component (bundle_id, component_id, quantity) VALUES (?, ?, ?)", bundleId, componentId, quantity);
    }

    /** The components of the bundle with the quantity needed for one bundle, ordered by product name; empty if the product is no bundle. */
    public List<BundleComponent> findBundleComponents(long bundleId) {
        return queryList("SELECT p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, c.quantity "
                        + "FROM bundle_component c JOIN product p ON p.id = c.component_id WHERE c.bundle_id = ? ORDER BY p.name, p.id",
                rs -> new BundleComponent(productRow(rs), rs.getInt("quantity")), bundleId);
    }

    /**
     * Units sold and revenue per product in the non-cancelled orders placed between the two dates, inclusive. Sorted by
     * units sold, highest first (then by revenue, highest first, then by SKU); products without sales are left out.
     */
    public List<ProductSales> findSalesPerProduct(LocalDate from, LocalDate to) {
        return salesPerProduct(false, null, from, to);
    }

    /** The sales per product; if inCategoryTree, only of the products of the category or of any category below it. */
    private List<ProductSales> salesPerProduct(boolean inCategoryTree, String category, LocalDate from, LocalDate to) {
        String sql = (inCategoryTree ? "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) " : "")
                + "SELECT p.id AS id, p.sku AS sku, p.name AS name, SUM(l.quantity) AS units, "
                + "SUM(l.quantity * l.unit_price) AS revenue "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + (inCategoryTree ? "AND p.category IN (SELECT code FROM tree) " : "")
                + "GROUP BY p.id, p.sku, p.name ORDER BY units DESC, revenue DESC, p.sku";
        Object[] params = inCategoryTree ? new Object[] {category, from, to} : new Object[] {from, to};
        return queryList(sql, rs -> new ProductSales(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                rs.getLong("units"), rs.getBigDecimal("revenue")), params);
    }

    /**
     * The purchase cost of the units of sales order line {@code l} of product {@code p}: the quantity times the last
     * purchase cost (latest sent or received purchase order line; 0 if the product was never purchased).
     */
    private static final String SALES_LINE_LAST_PURCHASE_COST = "l.quantity * COALESCE("
            + "(SELECT pl.unit_cost FROM purchase_order_line pl JOIN purchase_order po ON po.id = pl.order_id "
            + "WHERE pl.product_id = p.id AND po.status IN ('SENT', 'RECEIVED') "
            + "ORDER BY po.order_date DESC, po.id DESC, pl.line_no DESC LIMIT 1), 0)";

    /**
     * Revenue, purchase cost and margin per product in the non-cancelled orders placed between the two dates,
     * inclusive. The cost is the units sold times the last purchase cost (latest sent or received purchase order line;
     * 0 if the product was never purchased), the margin is revenue minus cost. Sorted by margin, lowest first (then by
     * SKU); products without sales are left out.
     */
    public List<ProductMargin> findMarginPerProduct(LocalDate from, LocalDate to) {
        return queryList("SELECT id, sku, name, revenue, cost FROM (SELECT p.id AS id, p.sku AS sku, p.name AS name, "
                        + "SUM(l.quantity * l.unit_price) AS revenue, "
                        + "SUM(" + SALES_LINE_LAST_PURCHASE_COST + ") AS cost "
                        + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                        + "JOIN product p ON p.id = l.product_id "
                        + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                        + "GROUP BY p.id, p.sku, p.name) ORDER BY revenue - cost, sku",
                rs -> {
                    BigDecimal revenue = rs.getBigDecimal("revenue").setScale(2);
                    BigDecimal cost = rs.getBigDecimal("cost").setScale(2);
                    return new ProductMargin(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            revenue, cost, revenue.subtract(cost));
                },
                from, to);
    }

    /**
     * Revenue, purchase cost, margin and margin percentage per product category in the non-cancelled orders placed
     * between the two dates, inclusive, with cost and margin as for the margin per product. The percentage is the margin
     * as a share of the revenue, rounded to two decimals (0 if the revenue is 0). Sorted by margin, lowest first (then by
     * category); categories without sales are left out.
     */
    public List<CategoryMargin> findMarginPerCategory(LocalDate from, LocalDate to) {
        return marginPerCategory(false, null, from, to);
    }

    /**
     * The categories of the margin per category whose margin percentage is below the given percentage (compared
     * exactly, not after rounding), in the same order.
     */
    public List<CategoryMargin> findMarginPerCategoryBelowPercent(BigDecimal percent, LocalDate from, LocalDate to) {
        return marginPerCategory(true, percent, from, to);
    }

    /** The margin per category; if onlyBelowPercent, only the categories whose margin percentage is below the percent. */
    private List<CategoryMargin> marginPerCategory(boolean onlyBelowPercent, BigDecimal percent, LocalDate from, LocalDate to) {
        String sql = "SELECT category, revenue, cost FROM (SELECT p.category AS category, "
                + "SUM(l.quantity * l.unit_price) AS revenue, "
                + "SUM(" + SALES_LINE_LAST_PURCHASE_COST + ") AS cost "
                + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "GROUP BY p.category) "
                + (onlyBelowPercent ? "WHERE (revenue - cost) * 100 < ? * revenue " : "")
                + "ORDER BY revenue - cost, category";
        Object[] params = onlyBelowPercent ? new Object[] {from, to, percent} : new Object[] {from, to};
        return queryList(sql, rs -> {
            BigDecimal revenue = rs.getBigDecimal("revenue").setScale(2);
            BigDecimal cost = rs.getBigDecimal("cost").setScale(2);
            BigDecimal margin = revenue.subtract(cost);
            BigDecimal marginPercent = revenue.signum() == 0 ? new BigDecimal("0.00")
                    : margin.multiply(new BigDecimal("100")).divide(revenue, 2, RoundingMode.HALF_UP);
            return new CategoryMargin(rs.getString("category"), revenue, cost, margin, marginPercent);
        }, params);
    }

    /**
     * Units sold and revenue per product, as for the sales per product, restricted to the products of the category
     * and of all categories below it.
     */
    public List<ProductSales> findSalesPerProductInCategoryTree(String category, LocalDate from, LocalDate to) {
        return salesPerProduct(true, category, from, to);
    }

    public Category createCategory(Category category) {
        update("INSERT INTO category (code, name, parent_code) VALUES (?, ?, ?)", category.code(), category.name(),
                category.parentCode());
        return category;
    }

    /** Columns: code, name, parent_code. */
    private static Category categoryRow(ResultSet rs) throws SQLException {
        return new Category(rs.getString("code"), rs.getString("name"), rs.getString("parent_code"));
    }

    public Optional<Category> findCategoryByCode(String code) {
        return queryOptional("SELECT code, name, parent_code FROM category WHERE code = ?", DataStore::categoryRow, code);
    }

    /** All categories, by code. */
    public List<Category> findAllCategories() {
        return queryList("SELECT code, name, parent_code FROM category ORDER BY code", DataStore::categoryRow);
    }

    /** The categories directly below the parent (not their children), by code. */
    public List<Category> findChildCategories(String parentCode) {
        return queryList("SELECT code, name, parent_code FROM category WHERE parent_code = ? ORDER BY code",
                DataStore::categoryRow, parentCode);
    }

    // ===== Promotions, customer price lists, exchange rates and tax rates =====

    public Promotion createPromotion(Promotion promotion) {
        long id = insert("INSERT INTO promotion (code, discount_percent, start_date, end_date, category) VALUES (?, ?, ?, ?, ?)",
                promotion.code(), promotion.discountPercent(), promotion.startDate(), promotion.endDate(), promotion.category());
        return new Promotion(id, promotion.code(), promotion.discountPercent(), promotion.startDate(), promotion.endDate(),
                promotion.category());
    }

    /** Columns: id, code, discount_percent, start_date, end_date, category. */
    private static Promotion promotionRow(ResultSet rs) throws SQLException {
        return new Promotion(rs.getLong("id"), rs.getString("code"), rs.getBigDecimal("discount_percent"),
                rs.getObject("start_date", LocalDate.class), rs.getObject("end_date", LocalDate.class), rs.getString("category"));
    }

    public Optional<Promotion> findPromotionByCode(String code) {
        return queryOptional("SELECT id, code, discount_percent, start_date, end_date, category FROM promotion WHERE code = ?",
                DataStore::promotionRow, code);
    }

    /** The promotions active on the date (start and end date inclusive), in all regions, by code (then by id). */
    public List<Promotion> findPromotionsActiveOn(LocalDate date) {
        return queryList("SELECT id, code, discount_percent, start_date, end_date, category FROM promotion "
                        + "WHERE start_date <= ? AND end_date >= ? ORDER BY code, id",
                DataStore::promotionRow, date, date);
    }

    /** Restricts the promotion to one region; a promotion without a region applies in all regions. */
    public void savePromotionRegion(long promotionId, Region region) {
        update("MERGE INTO promotion_region (promotion_id, region) KEY (promotion_id) VALUES (?, ?)", promotionId, region.name());
    }

    /**
     * The promotions active on the date (as for {@link #findPromotionsActiveOn}) that apply in the region: those
     * restricted to the region and those without a region. By code (then by id).
     */
    public List<Promotion> findPromotionsActiveOnInRegion(LocalDate date, Region region) {
        return queryList("SELECT p.id, p.code, p.discount_percent, p.start_date, p.end_date, p.category FROM promotion p "
                        + "LEFT JOIN promotion_region r ON r.promotion_id = p.id "
                        + "WHERE p.start_date <= ? AND p.end_date >= ? AND (r.region IS NULL OR r.region = ?) ORDER BY p.code, p.id",
                DataStore::promotionRow, date, date, region.name());
    }

    /** The promotions of exactly the category, whatever their dates, by start date (then by code and id). */
    public List<Promotion> findPromotionsByCategory(String category) {
        return queryList("SELECT id, code, discount_percent, start_date, end_date, category FROM promotion "
                        + "WHERE category = ? ORDER BY start_date, code, id",
                DataStore::promotionRow, category);
    }

    /**
     * The promotion with the highest discount active on the date for the category: promotions of the category and
     * promotions without a category; on equal discount the lowest code (then id) wins.
     */
    public Optional<Promotion> findBestPromotionForCategoryOn(String category, LocalDate date) {
        return queryOptional("SELECT id, code, discount_percent, start_date, end_date, category FROM promotion "
                        + "WHERE start_date <= ? AND end_date >= ? AND (category IS NULL OR category = ?) "
                        + "ORDER BY discount_percent DESC, code, id LIMIT 1",
                DataStore::promotionRow, date, date, category);
    }

    public CustomerPrice createCustomerPrice(CustomerPrice price) {
        long id = insert("INSERT INTO customer_price (customer_id, product_id, unit_price, start_date, end_date) VALUES (?, ?, ?, ?, ?)",
                price.customerId(), price.productId(), price.unitPrice(), price.startDate(), price.endDate());
        return new CustomerPrice(id, price.customerId(), price.productId(), price.unitPrice(), price.startDate(),
                price.endDate());
    }

    /** Columns: id, customer_id, product_id, unit_price, start_date, end_date. */
    private static CustomerPrice customerPriceRow(ResultSet rs) throws SQLException {
        return new CustomerPrice(rs.getLong("id"), rs.getLong("customer_id"), rs.getLong("product_id"),
                rs.getBigDecimal("unit_price"), rs.getObject("start_date", LocalDate.class),
                rs.getObject("end_date", LocalDate.class));
    }

    public Optional<CustomerPrice> findCustomerPriceById(long id) {
        return queryOptional("SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price WHERE id = ?",
                DataStore::customerPriceRow, id);
    }

    /** All special prices of the customer (whatever their dates) for active products, by product, start date and id. */
    public List<CustomerPrice> findCustomerPricesForActiveProducts(long customerId) {
        return queryList("SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                        + "WHERE customer_id = ? AND product_id IN (SELECT id FROM product WHERE active = TRUE) "
                        + "ORDER BY product_id, start_date, id",
                DataStore::customerPriceRow, customerId);
    }

    /**
     * The customer's special prices valid on the date for active products of the category or of any category below it,
     * by product, start date and id.
     */
    public List<CustomerPrice> findCustomerPricesValidOnInCategoryTree(long customerId, String category, LocalDate date) {
        return queryList("WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                        + "SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                        + "WHERE customer_id = ? AND start_date <= ? AND end_date >= ? "
                        + "AND product_id IN (SELECT id FROM product WHERE active = TRUE AND category IN (SELECT code FROM tree)) "
                        + "ORDER BY product_id, start_date, id",
                DataStore::customerPriceRow, category, customerId, date, date);
    }

    /**
     * The special prices of all customers whose last valid day lies within the given days after the date (after the
     * date, up to date + days inclusive), per customer (then by end date, product and id).
     */
    public List<CustomerPrice> findCustomerPricesExpiringWithinDays(LocalDate date, int days) {
        return queryList("SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                        + "WHERE end_date > ? AND end_date <= ? "
                        + "ORDER BY customer_id, end_date, product_id, id",
                DataStore::customerPriceRow, date, date.plusDays(days));
    }

    /** The customer's special price for the product valid on the date; of several, the one that started latest (then lowest id). */
    public Optional<CustomerPrice> findCustomerPriceValidOn(long customerId, long productId, LocalDate date) {
        return queryOptional("SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                        + "WHERE customer_id = ? AND product_id = ? AND start_date <= ? AND end_date >= ? "
                        + "ORDER BY start_date DESC, id LIMIT 1",
                DataStore::customerPriceRow, customerId, productId, date, date);
    }

    /** The customer's special prices for the product whose validity overlaps the period (inclusive), by start date and id. */
    public List<CustomerPrice> findCustomerPricesOverlappingPeriod(long customerId, long productId, LocalDate startDate, LocalDate endDate) {
        return queryList("SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                        + "WHERE customer_id = ? AND product_id = ? AND start_date <= ? AND end_date >= ? ORDER BY start_date, id",
                DataStore::customerPriceRow, customerId, productId, endDate, startDate);
    }

    public void deleteCustomerPrice(long id) {
        update("DELETE FROM customer_price WHERE id = ?", id);
    }

    /**
     * Per promotion, the number of lines of non-cancelled orders dated in the period it was applied to, the discount given
     * (list price less the price charged, derived from the promotion percentage; the catalogue price for a 100% promotion)
     * and the revenue of those lines. Sorted by code; promotions without such lines are left out.
     */
    /**
     * The discount given on sales order line {@code l} with promotion {@code pr} for product {@code p}: list price less
     * the price charged, derived from the promotion percentage; the catalogue price for a 100% promotion. Not rounded.
     */
    private static final String PROMOTION_LINE_DISCOUNT = "CASE WHEN pr.discount_percent < 100 "
            + "THEN l.quantity * l.unit_price * pr.discount_percent / (100 - pr.discount_percent) "
            + "ELSE l.quantity * p.unit_price END";

    /**
     * Per promotion, the number of lines of non-cancelled orders dated in the period it was applied to, the discount given
     * (list price less the price charged, derived from the promotion percentage; the catalogue price for a 100% promotion)
     * and the revenue of those lines. Sorted by code; promotions without such lines are left out.
     */
    public List<PromotionUsage> findPromotionUsage(LocalDate from, LocalDate to) {
        return queryList("SELECT pr.code AS code, COUNT(*) AS line_count, "
                        + "COALESCE(ROUND(SUM(" + PROMOTION_LINE_DISCOUNT + "), 2), 0) AS discount, "
                        + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS revenue "
                        + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                        + "JOIN promotion pr ON pr.code = l.promotion_code JOIN product p ON p.id = l.product_id "
                        + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                        + "GROUP BY pr.code ORDER BY pr.code",
                rs -> new PromotionUsage(rs.getString("code"), rs.getInt("line_count"), rs.getBigDecimal("discount"),
                        rs.getBigDecimal("revenue")),
                from, to);
    }

    /**
     * The lines of non-cancelled orders of the customer dated in the period (inclusive) that got a promotion, with the discount
     * given on each (list price less the price charged, derived from the promotion percentage; the catalogue price for a 100%
     * promotion), rounded to two decimals. Sorted by order date, order number and line number.
     */
    public List<CustomerPromotionLine> findCustomerPromotionLines(long customerId, LocalDate from, LocalDate to) {
        return queryList("SELECT o.number AS number, o.order_date AS order_date, l.line_no AS line_no, p.sku AS sku, "
                        + "l.quantity AS quantity, l.unit_price AS unit_price, pr.code AS code, "
                        + "ROUND(" + PROMOTION_LINE_DISCOUNT + ", 2) AS discount "
                        + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                        + "JOIN promotion pr ON pr.code = l.promotion_code JOIN product p ON p.id = l.product_id "
                        + "WHERE o.customer_id = ? AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                        + "ORDER BY o.order_date, o.number, o.id, l.line_no",
                rs -> new CustomerPromotionLine(rs.getString("number"), rs.getObject("order_date", LocalDate.class),
                        rs.getInt("line_no"), rs.getString("sku"), rs.getInt("quantity"), rs.getBigDecimal("unit_price"),
                        rs.getString("code"), rs.getBigDecimal("discount")),
                customerId, from, to);
    }

    /**
     * The promotions whose period overlaps the given period and that were applied to no line of a non-cancelled order dated
     * in the period. Sorted by code.
     */
    public List<Promotion> findUnusedPromotionsBetween(LocalDate from, LocalDate to) {
        return queryList("SELECT id, code, discount_percent, start_date, end_date, category FROM promotion pr "
                        + "WHERE pr.start_date <= ? AND pr.end_date >= ? AND NOT EXISTS (SELECT 1 FROM sales_order o "
                        + "JOIN sales_order_line l ON l.order_id = o.id WHERE l.promotion_code = pr.code "
                        + "AND o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') ORDER BY pr.code, pr.id",
                DataStore::promotionRow, to, from, from, to);
    }

    /**
     * Per customer, the lines of non-cancelled orders dated in the period that were sold at a special price (the customer
     * had a price for the product valid on the order date): the line count, the revenue at the special prices and the revenue
     * the lines would have had at catalogue prices. Sorted by customer name, then id; customers without such lines are left out.
     */
    public List<PriceListUsage> findPriceListUsagePerCustomer(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id AS customer_id, c.name AS name, COUNT(*) AS line_count, "
                        + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS special_revenue, "
                        + "COALESCE(SUM(l.quantity * p.unit_price), 0) AS catalogue_revenue "
                        + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id JOIN customer c ON c.id = o.customer_id "
                        + "JOIN product p ON p.id = l.product_id "
                        + "JOIN customer_price cp ON cp.customer_id = o.customer_id AND cp.product_id = l.product_id "
                        + "AND cp.start_date <= o.order_date AND cp.end_date >= o.order_date "
                        + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                        + "GROUP BY c.id, c.name ORDER BY c.name, c.id",
                rs -> new PriceListUsage(rs.getLong("customer_id"), rs.getString("name"), rs.getInt("line_count"),
                        rs.getBigDecimal("special_revenue"), rs.getBigDecimal("catalogue_revenue")),
                from, to);
    }

    /** Sets the exchange rate (units of the currency per 1 EUR) valid from the date on, replacing a rate set for the same date. */
    public ExchangeRate saveExchangeRate(ExchangeRate rate) {
        update("MERGE INTO exchange_rate (currency, rate_date, per_eur) KEY (currency, rate_date) VALUES (?, ?, ?)",
                rate.currency().name(), rate.rateDate(), rate.perEur());
        long id = querySingle("SELECT id FROM exchange_rate WHERE currency = ? AND rate_date = ?",
                rs -> rs.getLong("id"), rate.currency().name(), rate.rateDate());
        return new ExchangeRate(id, rate.currency(), rate.rateDate(), rate.perEur());
    }

    /** The rate of the currency that applies on the date: the latest one set on or before it, if any. */
    public Optional<ExchangeRate> findExchangeRate(Currency currency, LocalDate asOf) {
        return queryOptional("SELECT id, currency, rate_date, per_eur FROM exchange_rate "
                        + "WHERE currency = ? AND rate_date <= ? ORDER BY rate_date DESC LIMIT 1",
                rs -> new ExchangeRate(rs.getLong("id"), Currency.valueOf(rs.getString("currency")),
                        rs.getObject("rate_date", LocalDate.class), rs.getBigDecimal("per_eur")),
                currency.name(), asOf);
    }

    /** Sets the VAT rate of the country valid from the date on, replacing a rate set for the same date. */
    public TaxRate saveTaxRate(TaxRate rate) {
        update("MERGE INTO tax_rate (country, valid_from, rate_percent) KEY (country, valid_from) VALUES (?, ?, ?)",
                rate.country(), rate.validFrom(), rate.ratePercent());
        long id = querySingle("SELECT id FROM tax_rate WHERE country = ? AND valid_from = ?",
                rs -> rs.getLong("id"), rate.country(), rate.validFrom());
        return new TaxRate(id, rate.country(), rate.validFrom(), rate.ratePercent());
    }

    /** The VAT rate of the country that applies on the date: the latest one valid on or before it, if any. */
    public Optional<TaxRate> findTaxRate(String country, LocalDate asOf) {
        return queryOptional("SELECT id, country, valid_from, rate_percent FROM tax_rate "
                        + "WHERE country = ? AND valid_from <= ? ORDER BY valid_from DESC LIMIT 1",
                rs -> new TaxRate(rs.getLong("id"), rs.getString("country"),
                        rs.getObject("valid_from", LocalDate.class), rs.getBigDecimal("rate_percent")),
                country, asOf);
    }

    // ===== Suppliers and supplier products =====

    public Supplier createSupplier(Supplier supplier) {
        long id = insert("INSERT INTO supplier (name, contact_email, country, rating) VALUES (?, ?, ?, ?)",
                supplier.name(), supplier.contactEmail(), supplier.country(), supplier.rating());
        return new Supplier(id, supplier.name(), supplier.contactEmail(), supplier.country(), supplier.rating());
    }

    public void updateSupplier(Supplier supplier) {
        update("UPDATE supplier SET name = ?, contact_email = ?, country = ?, rating = ? WHERE id = ?",
                supplier.name(), supplier.contactEmail(), supplier.country(), supplier.rating(), supplier.id());
    }

    /** The suppliers matching the condition (an SQL condition on the supplier table, or TRUE), sorted by name, then id. */
    private List<Supplier> findSuppliersSortedByName(String condition, Object... params) {
        return queryList("SELECT id, name, contact_email, country, rating FROM supplier WHERE " + condition
                + " ORDER BY name, id", DataStore::supplierRow, params);
    }

    /** All suppliers, sorted by name (then id). */
    public List<Supplier> findAllSuppliers() {
        return findSuppliersSortedByName("TRUE");
    }

    /** The suppliers of the country, sorted by name (then id). */
    public List<Supplier> findSuppliersInCountry(String country) {
        return findSuppliersSortedByName("country = ?", country);
    }

    /** The suppliers with a rating of at least the given one, sorted by name (then id). */
    public List<Supplier> findSuppliersWithRatingAtLeast(int minimumRating) {
        return findSuppliersSortedByName("rating >= ?", minimumRating);
    }

    public Optional<Supplier> findSupplierById(long id) {
        return queryOptional("SELECT id, name, contact_email, country, rating FROM supplier WHERE id = ?",
                DataStore::supplierRow, id);
    }

    /** Columns: id, supplier_id, product_id, lead_time_days, last_unit_cost. */
    private static SupplierProduct supplierProductRow(ResultSet rs) throws SQLException {
        return new SupplierProduct(rs.getLong("id"), rs.getLong("supplier_id"), rs.getLong("product_id"),
                rs.getInt("lead_time_days"), rs.getBigDecimal("last_unit_cost"));
    }

    public SupplierProduct createSupplierProduct(SupplierProduct supplierProduct) {
        long id = insert("INSERT INTO supplier_product (supplier_id, product_id, lead_time_days, last_unit_cost) VALUES (?, ?, ?, ?)",
                supplierProduct.supplierId(), supplierProduct.productId(), supplierProduct.leadTimeDays(),
                supplierProduct.lastUnitCost());
        return new SupplierProduct(id, supplierProduct.supplierId(), supplierProduct.productId(),
                supplierProduct.leadTimeDays(), supplierProduct.lastUnitCost());
    }

    /** The supplier product of the supplier for the product, if the supplier delivers it. */
    public Optional<SupplierProduct> findSupplierProduct(long supplierId, long productId) {
        return queryOptional("SELECT id, supplier_id, product_id, lead_time_days, last_unit_cost FROM supplier_product "
                + "WHERE supplier_id = ? AND product_id = ?", DataStore::supplierProductRow, supplierId, productId);
    }

    /** Updates the lead time and last unit cost of the supplier product (by id). */
    public void updateSupplierProduct(SupplierProduct supplierProduct) {
        update("UPDATE supplier_product SET lead_time_days = ?, last_unit_cost = ? WHERE id = ?",
                supplierProduct.leadTimeDays(), supplierProduct.lastUnitCost(), supplierProduct.id());
    }

    /** The supplier products of the product (its suppliers), lowest last unit cost first (then shortest lead time, then id). */
    public List<SupplierProduct> findSupplierProductsOfProductCheapestFirst(long productId) {
        return queryList("SELECT id, supplier_id, product_id, lead_time_days, last_unit_cost FROM supplier_product "
                + "WHERE product_id = ? ORDER BY last_unit_cost, lead_time_days, id", DataStore::supplierProductRow, productId);
    }

    /** The supplier products of the supplier (the products it delivers), sorted by product id (then id). */
    public List<SupplierProduct> findSupplierProductsOfSupplier(long supplierId) {
        return queryList("SELECT id, supplier_id, product_id, lead_time_days, last_unit_cost FROM supplier_product "
                + "WHERE supplier_id = ? ORDER BY product_id, id", DataStore::supplierProductRow, supplierId);
    }

    /**
     * The supplier products of the products in the category or in any category below it, lowest last unit cost first
     * (then shortest lead time, then id).
     */
    public List<SupplierProduct> findSupplierProductsInCategoryTreeCheapestFirst(String category) {
        return queryList("WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                        + "SELECT sp.id, sp.supplier_id, sp.product_id, sp.lead_time_days, sp.last_unit_cost "
                        + "FROM supplier_product sp JOIN product p ON p.id = sp.product_id "
                        + "WHERE p.category IN (SELECT code FROM tree) ORDER BY sp.last_unit_cost, sp.lead_time_days, sp.id",
                DataStore::supplierProductRow, category);
    }

    /**
     * The categories, the given one and those below it, whose products have supplier products from exactly one
     * supplier, with that supplier. Sorted by category; categories without supplier products are left out.
     */
    public List<CategorySupplier> findCategoriesWithSingleSupplier(String category) {
        return queryList("WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                        + "SELECT p.category AS category, s.id, s.name, s.contact_email, s.country, s.rating "
                        + "FROM supplier_product sp JOIN product p ON p.id = sp.product_id "
                        + "JOIN supplier s ON s.id = sp.supplier_id "
                        + "WHERE p.category IN (SELECT code FROM tree) "
                        + "GROUP BY p.category, s.id, s.name, s.contact_email, s.country, s.rating "
                        + "HAVING (SELECT COUNT(DISTINCT sp2.supplier_id) FROM supplier_product sp2 "
                        + "JOIN product p2 ON p2.id = sp2.product_id WHERE p2.category = p.category) = 1 "
                        + "ORDER BY p.category",
                rs -> new CategorySupplier(rs.getString("category"), supplierRow(rs)), category);
    }

    /**
     * The stock quantity and value per warehouse of the products the supplier delivers (has a supplier product for),
     * for the warehouses with stock, valued like listStockValueByWarehouse. Sorted by warehouse code.
     */
    public List<SupplierWarehouseStock> findStockPerWarehouseOfSupplier(long supplierId) {
        return queryList("SELECT w.id, w.code, w.name, w.city, w.region, SUM(s.quantity) AS quantity, "
                        + "COALESCE(SUM(s.quantity * COALESCE("
                        + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
                        + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
                        + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)), 0) AS stock_value "
                        + "FROM stock s JOIN warehouse w ON w.id = s.warehouse_id "
                        + "WHERE s.quantity > 0 AND s.product_id IN (SELECT sp.product_id FROM supplier_product sp WHERE sp.supplier_id = ?) "
                        + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id",
                rs -> new SupplierWarehouseStock(new Warehouse(rs.getLong("id"), rs.getString("code"),
                        rs.getString("name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                        rs.getLong("quantity"), rs.getBigDecimal("stock_value").setScale(2)), supplierId);
    }

    /** Columns: id, name, contact_email, country, rating. */
    private static Supplier supplierRow(ResultSet rs) throws SQLException {
        return new Supplier(rs.getLong("id"), rs.getString("name"), rs.getString("contact_email"),
                rs.getString("country"), rs.getInt("rating"));
    }

    // ===== Warehouses, stock levels, stock movements, adjustments, transfers, low and slow-moving stock =====

    /** Columns: id, code, name, city, region. */
    private static Warehouse warehouseRow(ResultSet rs) throws SQLException {
        return new Warehouse(rs.getLong("id"), rs.getString("code"), rs.getString("name"), rs.getString("city"),
                Region.valueOf(rs.getString("region")));
    }

    /**
     * For queries that also select another entity's id and name. Columns: warehouse_id, warehouse_code,
     * warehouse_name, warehouse_city, warehouse_region.
     */
    private static Warehouse prefixedWarehouseRow(ResultSet rs) throws SQLException {
        return new Warehouse(rs.getLong("warehouse_id"), rs.getString("warehouse_code"), rs.getString("warehouse_name"),
                rs.getString("warehouse_city"), Region.valueOf(rs.getString("warehouse_region")));
    }

    /** The select list of {@link #prefixedWarehouseRow} for a warehouse with the alias w. */
    private static final String PREFIXED_WAREHOUSE_COLUMNS = "w.id AS warehouse_id, w.code AS warehouse_code, "
            + "w.name AS warehouse_name, w.city AS warehouse_city, w.region AS warehouse_region";

    /** Columns: id, product_id, warehouse_id, movement_date, quantity_change, reason. */
    private static StockMovement stockMovementRow(ResultSet rs) throws SQLException {
        return new StockMovement(rs.getLong("id"), rs.getLong("product_id"), rs.getLong("warehouse_id"),
                rs.getObject("movement_date", LocalDate.class), rs.getInt("quantity_change"),
                StockMovementReason.valueOf(rs.getString("reason")));
    }

    /** Columns: id, product_id, warehouse_id, adjustment_date, quantity_before, counted_quantity, reason. */
    private static StockAdjustment stockAdjustmentRow(ResultSet rs) throws SQLException {
        return new StockAdjustment(rs.getLong("id"), rs.getLong("product_id"), rs.getLong("warehouse_id"),
                rs.getObject("adjustment_date", LocalDate.class), rs.getInt("quantity_before"),
                rs.getInt("counted_quantity"), rs.getString("reason"));
    }

    /** One row of a query sorted by warehouse: the warehouse and the item of the row. */
    private record StockRowOfWarehouse<T>(Warehouse warehouse, T item) {
    }

    /**
     * Groups rows that are sorted by warehouse into one group per warehouse (a new group starts whenever the warehouse
     * id changes), keeping the row order; warehouses without rows get no group.
     */
    private static <T, G> List<G> groupStockRowsPerWarehouse(List<StockRowOfWarehouse<T>> rows,
                                                             java.util.function.BiFunction<Warehouse, List<T>, G> group) {
        List<G> groups = new ArrayList<>();
        Warehouse current = null;
        List<T> items = null;
        for (StockRowOfWarehouse<T> row : rows) {
            if (current == null || current.id() != row.warehouse().id()) {
                current = row.warehouse();
                items = new ArrayList<>();
                groups.add(group.apply(current, items));
            }
            items.add(row.item());
        }
        return groups;
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        long id = insert("INSERT INTO warehouse (code, name, city, region) VALUES (?, ?, ?, ?)",
                warehouse.code(), warehouse.name(), warehouse.city(), warehouse.region().name());
        return new Warehouse(id, warehouse.code(), warehouse.name(), warehouse.city(), warehouse.region());
    }

    /** All warehouses, sorted by code. */
    public List<Warehouse> findAllWarehousesOrderedByCode() {
        return queryList("SELECT id, code, name, city, region FROM warehouse ORDER BY code, id", DataStore::warehouseRow);
    }

    public Optional<Warehouse> findWarehouseByCode(String code) {
        return queryOptional("SELECT id, code, name, city, region FROM warehouse WHERE code = ?",
                DataStore::warehouseRow, code);
    }

    public void setStockQuantity(long warehouseId, long productId, int quantity) {
        update("MERGE INTO stock (warehouse_id, product_id, quantity) KEY (warehouse_id, product_id) VALUES (?, ?, ?)",
                warehouseId, productId, quantity);
    }

    /** Adds the amount to the stock of the product in the warehouse, creating the stock row if there is none. */
    public void addStockQuantity(long warehouseId, long productId, int amount) {
        if (update("UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?",
                amount, warehouseId, productId) == 0) {
            update("INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)", warehouseId, productId, amount);
        }
    }

    /** Takes the amount from the stock; returns false, changing nothing, if there is less than that. */
    public boolean removeStockQuantity(long warehouseId, long productId, int amount) {
        return update("UPDATE stock SET quantity = quantity - ? WHERE warehouse_id = ? AND product_id = ? AND quantity >= ?",
                amount, warehouseId, productId, amount) > 0;
    }

    /** Logs a stock change (positive when the stock grows, negative when it shrinks). */
    public void createStockMovement(long productId, long warehouseId, LocalDate movementDate, int quantityChange,
                                    StockMovementReason reason) {
        update("INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                + "VALUES (?, ?, ?, ?, ?)", productId, warehouseId, movementDate, quantityChange, reason.name());
    }

    /**
     * Selects stock movements between two dates, inclusive, optionally only of one product and/or only in one
     * warehouse.
     */
    private static final class StockMovementQuery {
        private final LocalDate from;
        private final LocalDate to;
        private final Long productId;
        private final Long warehouseId;

        private StockMovementQuery(LocalDate from, LocalDate to, Long productId, Long warehouseId) {
            this.from = from;
            this.to = to;
            this.productId = productId;
            this.warehouseId = warehouseId;
        }

        /** The movements of all products in all warehouses between the two dates, inclusive. */
        public static StockMovementQuery between(LocalDate from, LocalDate to) {
            return new StockMovementQuery(from, to, null, null);
        }

        /** Only the movements of the product. */
        public StockMovementQuery ofProduct(long productId) {
            return new StockMovementQuery(from, to, productId, warehouseId);
        }

        /** Only the movements in the warehouse. */
        public StockMovementQuery inWarehouse(long warehouseId) {
            return new StockMovementQuery(from, to, productId, warehouseId);
        }
    }

    /** The stock movements selected by the query, oldest first (then by id). */
    private List<StockMovement> findStockMovements(StockMovementQuery query) {
        StringBuilder sql = new StringBuilder("SELECT id, product_id, warehouse_id, movement_date, quantity_change, reason "
                + "FROM stock_movement WHERE movement_date >= ? AND movement_date <= ?");
        List<Object> params = new ArrayList<>();
        params.add(query.from);
        params.add(query.to);
        if (query.productId != null) {
            sql.append(" AND product_id = ?");
            params.add(query.productId);
        }
        if (query.warehouseId != null) {
            sql.append(" AND warehouse_id = ?");
            params.add(query.warehouseId);
        }
        sql.append(" ORDER BY movement_date, id");
        return queryList(sql.toString(), DataStore::stockMovementRow, params.toArray());
    }

    /** The stock movements of the product in all warehouses between the two dates, inclusive, oldest first (then by id). */
    public List<StockMovement> findStockMovementsByProduct(long productId, LocalDate from, LocalDate to) {
        return findStockMovements(StockMovementQuery.between(from, to).ofProduct(productId));
    }

    /** The stock movements of all products in the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<StockMovement> findStockMovementsByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return findStockMovements(StockMovementQuery.between(from, to).inWarehouse(warehouseId));
    }

    /** The stock movements of the product in the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<StockMovement> findStockMovementsByProductAndWarehouse(long productId, long warehouseId, LocalDate from,
                                                                       LocalDate to) {
        return findStockMovements(StockMovementQuery.between(from, to).ofProduct(productId).inWarehouse(warehouseId));
    }

    /**
     * The stock movements of the products of the category between the two dates, inclusive, per warehouse. Warehouses
     * are sorted by code, movements oldest first (then by id); warehouses without movements are left out.
     */
    public List<WarehouseMovements> findStockMovementsByCategoryPerWarehouse(String category, LocalDate from,
                                                                             LocalDate to) {
        String sql = "SELECT " + PREFIXED_WAREHOUSE_COLUMNS + ", m.id, m.product_id, "
                + "m.movement_date, m.quantity_change, m.reason "
                + "FROM stock_movement m JOIN product p ON p.id = m.product_id JOIN warehouse w ON w.id = m.warehouse_id "
                + "WHERE p.category = ? AND m.movement_date >= ? AND m.movement_date <= ? "
                + "ORDER BY w.code, w.id, m.movement_date, m.id";
        List<StockRowOfWarehouse<StockMovement>> rows = queryList(sql,
                rs -> new StockRowOfWarehouse<>(prefixedWarehouseRow(rs), stockMovementRow(rs)), category, from, to);
        return groupStockRowsPerWarehouse(rows, WarehouseMovements::new);
    }

    /**
     * The number and the net quantity change of the stock movements between the two dates, inclusive, per reason,
     * warehouse and product category. Sorted by reason, warehouse code and category.
     */
    public List<ReasonMovements> findStockMovementsPerReasonWarehouseAndCategory(LocalDate from, LocalDate to) {
        String sql = "SELECT m.reason, w.id, w.code, w.name, w.city, w.region, p.category, COUNT(*) AS movement_count, "
                + "SUM(m.quantity_change) AS quantity_change "
                + "FROM stock_movement m JOIN product p ON p.id = m.product_id JOIN warehouse w ON w.id = m.warehouse_id "
                + "WHERE m.movement_date >= ? AND m.movement_date <= ? "
                + "GROUP BY m.reason, w.id, w.code, w.name, w.city, w.region, p.category "
                + "ORDER BY m.reason, w.code, w.id, p.category";
        return queryList(sql, rs -> new ReasonMovements(StockMovementReason.valueOf(rs.getString("reason")),
                warehouseRow(rs), rs.getString("category"), rs.getLong("movement_count"), rs.getLong("quantity_change")),
                from, to);
    }

    /** Sets the stock to the counted quantity and logs the adjustment and its difference as a movement, all or nothing. */
    public StockAdjustment createStockAdjustment(StockAdjustment adjustment) {
        return inTransaction(() -> {
            int before = findStockQuantity(adjustment.warehouseId(), adjustment.productId());
            setStockQuantity(adjustment.warehouseId(), adjustment.productId(), adjustment.countedQuantity());
            long id = insert("INSERT INTO stock_adjustment (product_id, warehouse_id, adjustment_date, quantity_before, "
                            + "counted_quantity, reason) VALUES (?, ?, ?, ?, ?, ?)", adjustment.productId(),
                    adjustment.warehouseId(), adjustment.adjustmentDate(), before, adjustment.countedQuantity(),
                    adjustment.reason());
            if (adjustment.countedQuantity() != before) {
                update("INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                                + "VALUES (?, ?, ?, ?, 'ADJUSTMENT')", adjustment.productId(), adjustment.warehouseId(),
                        adjustment.adjustmentDate(), adjustment.countedQuantity() - before);
            }
            return new StockAdjustment(id, adjustment.productId(), adjustment.warehouseId(), adjustment.adjustmentDate(),
                    before, adjustment.countedQuantity(), adjustment.reason());
        });
    }

    /** The stock adjustments in the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<StockAdjustment> findStockAdjustmentsByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return queryList("SELECT id, product_id, warehouse_id, adjustment_date, quantity_before, counted_quantity, reason "
                + "FROM stock_adjustment WHERE warehouse_id = ? AND adjustment_date >= ? AND adjustment_date <= ? "
                + "ORDER BY adjustment_date, id", DataStore::stockAdjustmentRow, warehouseId, from, to);
    }

    /**
     * The stock adjustments that lowered the stock between the two dates, inclusive, per warehouse. Warehouses are
     * sorted by code, adjustments by the size of the loss, biggest first (then oldest, then by id); warehouses without
     * negative adjustments are left out.
     */
    public List<WarehouseAdjustments> findNegativeStockAdjustmentsPerWarehouse(LocalDate from, LocalDate to) {
        String sql = "SELECT " + PREFIXED_WAREHOUSE_COLUMNS + ", a.id, a.product_id, "
                + "a.adjustment_date, a.quantity_before, a.counted_quantity, a.reason "
                + "FROM stock_adjustment a JOIN warehouse w ON w.id = a.warehouse_id "
                + "WHERE a.counted_quantity < a.quantity_before AND a.adjustment_date >= ? AND a.adjustment_date <= ? "
                + "ORDER BY w.code, w.id, a.counted_quantity - a.quantity_before, a.adjustment_date, a.id";
        List<StockRowOfWarehouse<StockAdjustment>> rows = queryList(sql,
                rs -> new StockRowOfWarehouse<>(prefixedWarehouseRow(rs), stockAdjustmentRow(rs)), from, to);
        return groupStockRowsPerWarehouse(rows, WarehouseAdjustments::new);
    }

    /** Moves the stock between the warehouses and logs both movements of every line, all or nothing (fails if the stock is short). */
    public WarehouseTransfer createWarehouseTransfer(WarehouseTransfer transfer) {
        return inTransactionRollingBackOnDatabaseFailureOnly(() -> {
            long id = insert("INSERT INTO warehouse_transfer (number, from_warehouse_id, to_warehouse_id, transfer_date) "
                    + "VALUES (?, ?, ?, ?)", transfer.number(), transfer.fromWarehouseId(), transfer.toWarehouseId(),
                    transfer.transferDate());
            String movementSql = "INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                    + "VALUES (?, ?, ?, ?, 'TRANSFER')";
            int lineNo = 1;
            for (WarehouseTransferLine line : transfer.lines()) {
                update("INSERT INTO warehouse_transfer_line (transfer_id, line_no, product_id, quantity) VALUES (?, ?, ?, ?)",
                        id, lineNo++, line.productId(), line.quantity());
                if (!removeStockQuantity(transfer.fromWarehouseId(), line.productId(), line.quantity())) {
                    throw new SQLException("Not enough stock of product " + line.productId() + " in warehouse "
                            + transfer.fromWarehouseId());
                }
                addStockQuantity(transfer.toWarehouseId(), line.productId(), line.quantity());
                update(movementSql, line.productId(), transfer.fromWarehouseId(), transfer.transferDate(), -line.quantity());
                update(movementSql, line.productId(), transfer.toWarehouseId(), transfer.transferDate(), line.quantity());
            }
            return new WarehouseTransfer(id, transfer.number(), transfer.fromWarehouseId(), transfer.toWarehouseId(),
                    transfer.transferDate(), List.copyOf(transfer.lines()));
        });
    }

    /** Columns: id, number, from_warehouse_id, to_warehouse_id, transfer_date; loads the transfer's lines too. */
    private WarehouseTransfer warehouseTransferRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        List<WarehouseTransferLine> lines = queryList(
                "SELECT product_id, quantity FROM warehouse_transfer_line WHERE transfer_id = ? ORDER BY line_no",
                lineRs -> new WarehouseTransferLine(lineRs.getLong("product_id"), lineRs.getInt("quantity")), id);
        return new WarehouseTransfer(id, rs.getString("number"), rs.getLong("from_warehouse_id"),
                rs.getLong("to_warehouse_id"), rs.getObject("transfer_date", LocalDate.class), lines);
    }

    /**
     * Selects warehouse transfers by transfer date between two dates, inclusive, optionally only those from or to one
     * warehouse and/or only those with a line of one product.
     */
    private static final class WarehouseTransferQuery {
        private final LocalDate from;
        private final LocalDate to;
        private final Long warehouseId;
        private final Long productId;

        private WarehouseTransferQuery(LocalDate from, LocalDate to, Long warehouseId, Long productId) {
            this.from = from;
            this.to = to;
            this.warehouseId = warehouseId;
            this.productId = productId;
        }

        /** All transfers between the two dates, inclusive. */
        public static WarehouseTransferQuery between(LocalDate from, LocalDate to) {
            return new WarehouseTransferQuery(from, to, null, null);
        }

        /** Only the transfers from or to the warehouse. */
        public WarehouseTransferQuery fromOrToWarehouse(long warehouseId) {
            return new WarehouseTransferQuery(from, to, warehouseId, productId);
        }

        /** Only the transfers with a line of the product (all their lines are loaded). */
        public WarehouseTransferQuery containingProduct(long productId) {
            return new WarehouseTransferQuery(from, to, warehouseId, productId);
        }
    }

    /** The warehouse transfers selected by the query with their lines, oldest first (then by id). */
    private List<WarehouseTransfer> findWarehouseTransfers(WarehouseTransferQuery query) {
        StringBuilder sql = new StringBuilder("SELECT id, number, from_warehouse_id, to_warehouse_id, transfer_date "
                + "FROM warehouse_transfer WHERE transfer_date >= ? AND transfer_date <= ?");
        List<Object> params = new ArrayList<>();
        params.add(query.from);
        params.add(query.to);
        if (query.warehouseId != null) {
            sql.append(" AND (from_warehouse_id = ? OR to_warehouse_id = ?)");
            params.add(query.warehouseId);
            params.add(query.warehouseId);
        }
        if (query.productId != null) {
            sql.append(" AND id IN (SELECT transfer_id FROM warehouse_transfer_line WHERE product_id = ?)");
            params.add(query.productId);
        }
        sql.append(" ORDER BY transfer_date, id");
        return queryList(sql.toString(), this::warehouseTransferRow, params.toArray());
    }

    /** The transfers from or to the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<WarehouseTransfer> findWarehouseTransfersByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return findWarehouseTransfers(WarehouseTransferQuery.between(from, to).fromOrToWarehouse(warehouseId));
    }

    /** The transfers between the two dates, inclusive, oldest first (then by id). */
    public List<WarehouseTransfer> findWarehouseTransfersByPeriod(LocalDate from, LocalDate to) {
        return findWarehouseTransfers(WarehouseTransferQuery.between(from, to));
    }

    /** The transfers containing the product between the two dates, inclusive, oldest first (then by id). */
    public List<WarehouseTransfer> findWarehouseTransfersByProduct(long productId, LocalDate from, LocalDate to) {
        return findWarehouseTransfers(WarehouseTransferQuery.between(from, to).containingProduct(productId));
    }

    /**
     * The quantity of the product transferred between the two dates, inclusive, per route (from and to warehouse),
     * sorted by the codes of the from and to warehouses.
     */
    public List<TransferRoute> findTransferredQuantityPerRoute(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT t.from_warehouse_id, f.code AS from_code, t.to_warehouse_id, d.code AS to_code, "
                + "SUM(l.quantity) AS total FROM warehouse_transfer t "
                + "JOIN warehouse_transfer_line l ON l.transfer_id = t.id "
                + "JOIN warehouse f ON f.id = t.from_warehouse_id JOIN warehouse d ON d.id = t.to_warehouse_id "
                + "WHERE l.product_id = ? AND t.transfer_date >= ? AND t.transfer_date <= ? "
                + "GROUP BY t.from_warehouse_id, f.code, t.to_warehouse_id, d.code "
                + "ORDER BY f.code, d.code, t.from_warehouse_id, t.to_warehouse_id";
        return queryList(sql, rs -> new TransferRoute(rs.getLong("from_warehouse_id"), rs.getString("from_code"),
                rs.getLong("to_warehouse_id"), rs.getString("to_code"), rs.getLong("total")), productId, from, to);
    }

    /** How many bundles can be built from the stock of one warehouse; 0 if the product has no components. */
    public int countBuildableBundlesInWarehouse(long warehouseId, long bundleId) {
        return querySingle("SELECT COALESCE(MIN(COALESCE(s.quantity, 0) / c.quantity), 0) AS buildable FROM bundle_component c "
                        + "LEFT JOIN stock s ON s.product_id = c.component_id AND s.warehouse_id = ? WHERE c.bundle_id = ?",
                rs -> rs.getInt("buildable"), warehouseId, bundleId);
    }

    /** How many bundles can be built in total, the sum over the warehouses of what each can build from its own stock. */
    public int countBuildableBundlesInAllWarehouses(long bundleId) {
        return querySingle("SELECT COALESCE(SUM(buildable), 0) AS total FROM ("
                        + "SELECT MIN(COALESCE(s.quantity, 0) / c.quantity) AS buildable FROM warehouse w CROSS JOIN bundle_component c "
                        + "LEFT JOIN stock s ON s.product_id = c.component_id AND s.warehouse_id = w.id WHERE c.bundle_id = ? GROUP BY w.id)",
                rs -> rs.getInt("total"), bundleId);
    }

    /** The stock quantity of the product in the warehouse; 0 if there is no stock row. */
    public int findStockQuantity(long warehouseId, long productId) {
        return queryOptional("SELECT quantity FROM stock WHERE warehouse_id = ? AND product_id = ?",
                rs -> rs.getInt("quantity"), warehouseId, productId).orElse(0);
    }

    /** The stock quantity of the product summed over all warehouses; 0 if there is none. */
    public int findTotalStockQuantity(long productId) {
        return querySingle("SELECT COALESCE(SUM(quantity), 0) AS total FROM stock WHERE product_id = ?",
                rs -> rs.getInt("total"), productId);
    }

    /** The stock rows of the warehouse (including zero quantities) with their products, sorted by product name. */
    public List<StockLevel> findStockLevelsByWarehouse(long warehouseId) {
        return queryList("SELECT p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, s.quantity "
                        + "FROM stock s JOIN product p ON p.id = s.product_id WHERE s.warehouse_id = ? ORDER BY p.name, p.id",
                rs -> new StockLevel(productRow(rs), rs.getInt("quantity")), warehouseId);
    }

    /**
     * The active products whose stock summed over all warehouses (no stock counts as zero) is below the threshold,
     * with that total, sorted by product name.
     */
    public List<StockLevel> findActiveProductsWithTotalStockBelow(int threshold) {
        String sql = "SELECT p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, "
                + "COALESCE(SUM(s.quantity), 0) AS total "
                + "FROM product p LEFT JOIN stock s ON s.product_id = p.id WHERE p.active = TRUE "
                + "GROUP BY p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active "
                + "HAVING COALESCE(SUM(s.quantity), 0) < ? ORDER BY p.name, p.id";
        return queryList(sql, rs -> new StockLevel(productRow(rs), rs.getInt("total")), threshold);
    }

    /**
     * The stock value rule, used by every stock value: the value of one stock row (alias s) is its quantity times the
     * unit cost of the product's latest purchase order line on a SENT or RECEIVED purchase order (latest order date,
     * then highest order id, then highest line number), or 0 if there is no such line. Results are given with scale 2.
     */
    private static final String STOCK_ROW_VALUE_SQL = "s.quantity * COALESCE("
            + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
            + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
            + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)";

    /** The summed value of the stock rows (0 without rows), as column stock_value; see {@link #STOCK_ROW_VALUE_SQL}. */
    private static final String SUMMED_STOCK_VALUE_SQL = "COALESCE(SUM(" + STOCK_ROW_VALUE_SQL + "), 0) AS stock_value";

    /** The stock value per warehouse (0 for a warehouse without stock), every warehouse, sorted by code; see {@link #STOCK_ROW_VALUE_SQL}. */
    public List<WarehouseStockValue> findStockValuePerWarehouse() {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, " + SUMMED_STOCK_VALUE_SQL + " "
                + "FROM warehouse w LEFT JOIN stock s ON s.warehouse_id = w.id "
                + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id";
        return queryList(sql, rs -> new WarehouseStockValue(warehouseRow(rs), rs.getBigDecimal("stock_value").setScale(2)));
    }

    /** The stock value over all warehouses; see {@link #STOCK_ROW_VALUE_SQL}. */
    public BigDecimal findTotalStockValue() {
        return querySingle("SELECT " + SUMMED_STOCK_VALUE_SQL + " FROM stock s",
                rs -> rs.getBigDecimal("stock_value").setScale(2));
    }

    /** The stock quantity and value per category and warehouse, for the combinations with stock; see {@link #STOCK_ROW_VALUE_SQL}. */
    public List<CategoryWarehouseStock> findStockPerCategoryAndWarehouse() {
        String sql = "SELECT p.category, w.id, w.code, w.name, w.city, w.region, SUM(s.quantity) AS quantity, "
                + SUMMED_STOCK_VALUE_SQL + " "
                + "FROM stock s JOIN product p ON p.id = s.product_id JOIN warehouse w ON w.id = s.warehouse_id "
                + "WHERE s.quantity > 0 "
                + "GROUP BY p.category, w.id, w.code, w.name, w.city, w.region ORDER BY p.category, w.code, w.id";
        return queryList(sql, rs -> new CategoryWarehouseStock(rs.getString("category"), warehouseRow(rs),
                rs.getLong("quantity"), rs.getBigDecimal("stock_value").setScale(2)));
    }

    /** The stock quantity and value per category over all warehouses, for the categories with stock; see {@link #STOCK_ROW_VALUE_SQL}. */
    public List<CategoryStock> findStockPerCategory() {
        String sql = "SELECT p.category, SUM(s.quantity) AS quantity, " + SUMMED_STOCK_VALUE_SQL + " "
                + "FROM stock s JOIN product p ON p.id = s.product_id "
                + "WHERE s.quantity > 0 "
                + "GROUP BY p.category ORDER BY p.category";
        return queryList(sql, rs -> new CategoryStock(rs.getString("category"), rs.getLong("quantity"),
                rs.getBigDecimal("stock_value").setScale(2)));
    }

    /** Every warehouse and product category (of any product) without stock in that warehouse, by warehouse code then category. */
    public List<WarehouseCategory> findCategoriesWithoutStockPerWarehouse() {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, c.category FROM warehouse w "
                + "CROSS JOIN (SELECT DISTINCT category FROM product) c "
                + "WHERE NOT EXISTS (SELECT 1 FROM stock s JOIN product p ON p.id = s.product_id "
                + "WHERE s.warehouse_id = w.id AND p.category = c.category AND s.quantity > 0) "
                + "ORDER BY w.code, w.id, c.category";
        return queryList(sql, rs -> new WarehouseCategory(warehouseRow(rs), rs.getString("category")));
    }

    /**
     * The slow-moving rule: the SQL condition that the product of the stock row s was not shipped (whole shipments nor
     * shipment parts) on any day from the given number of days before the date up to the day before it; with
     * fromTheStockWarehouseOnly only shipments from the stock row's warehouse count, otherwise shipments from any
     * warehouse. Bind it with {@link #slowMovingPeriodParams}.
     */
    private static String notShippedInSlowMovingPeriodSql(boolean fromTheStockWarehouseOnly) {
        String shipmentWarehouse = fromTheStockWarehouseOnly ? "h.warehouse_id = s.warehouse_id AND " : "";
        String partWarehouse = fromTheStockWarehouseOnly ? "sp.warehouse_id = s.warehouse_id AND " : "";
        return "NOT EXISTS (SELECT 1 FROM shipment h JOIN sales_order_line ol ON ol.order_id = h.order_id "
                + "WHERE " + shipmentWarehouse + "ol.product_id = s.product_id AND h.ship_date >= ? AND h.ship_date < ?) "
                + "AND NOT EXISTS (SELECT 1 FROM shipment_part sp JOIN sales_order_line ol ON ol.order_id = sp.order_id AND ol.line_no = sp.line_no "
                + "WHERE " + partWarehouse + "ol.product_id = s.product_id AND sp.ship_date >= ? AND sp.ship_date < ?)";
    }

    /** The parameters of {@link #notShippedInSlowMovingPeriodSql}. */
    private static Object[] slowMovingPeriodParams(LocalDate date, int days) {
        return new Object[] {date.minusDays(days), date, date.minusDays(days), date};
    }

    /**
     * The products with stock in a warehouse but not shipped from that warehouse (whole or partial shipments) on any day from
     * the given number of days before the date up to the day before it, by warehouse code then stock value, highest first;
     * valued by {@link #STOCK_ROW_VALUE_SQL}.
     */
    public List<WarehouseSlowMovingStock> findSlowMovingStockPerWarehouse(LocalDate date, int days) {
        String sql = "SELECT " + PREFIXED_WAREHOUSE_COLUMNS + ", p.id, p.sku, p.name, p.category, "
                + "p.unit_price, p.unit_of_measure, p.active, s.quantity, " + STOCK_ROW_VALUE_SQL + " AS stock_value "
                + "FROM stock s JOIN product p ON p.id = s.product_id JOIN warehouse w ON w.id = s.warehouse_id "
                + "WHERE s.quantity > 0 AND " + notShippedInSlowMovingPeriodSql(true) + " "
                + "ORDER BY w.code, w.id, stock_value DESC, p.sku, p.id";
        return queryList(sql, rs -> new WarehouseSlowMovingStock(prefixedWarehouseRow(rs), productRow(rs),
                rs.getLong("quantity"), rs.getBigDecimal("stock_value").setScale(2)), slowMovingPeriodParams(date, days));
    }

    /**
     * The products with stock that were not shipped from any warehouse (whole or partial shipments) on any day from the
     * given number of days before the date up to the day before it, with the stock over all warehouses, by stock value,
     * highest first; valued by {@link #STOCK_ROW_VALUE_SQL}.
     */
    public List<SlowMovingStock> findSlowMovingStock(LocalDate date, int days) {
        String sql = "SELECT p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, "
                + "SUM(s.quantity) AS quantity, " + SUMMED_STOCK_VALUE_SQL + " "
                + "FROM stock s JOIN product p ON p.id = s.product_id "
                + "WHERE s.quantity > 0 AND " + notShippedInSlowMovingPeriodSql(false) + " "
                + "GROUP BY p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active "
                + "ORDER BY stock_value DESC, p.sku, p.id";
        return queryList(sql, rs -> new SlowMovingStock(productRow(rs), rs.getLong("quantity"),
                rs.getBigDecimal("stock_value").setScale(2)), slowMovingPeriodParams(date, days));
    }

    public void setReorderLevel(long warehouseId, long productId, int level) {
        update("MERGE INTO reorder_level (warehouse_id, product_id, level) KEY (warehouse_id, product_id) VALUES (?, ?, ?)",
                warehouseId, productId, level);
    }

    /** The reorder levels r with their warehouse w, product p and stock row s (if any), for {@link #LOW_STOCK_RULE_SQL}. */
    private static final String REORDER_LEVEL_WITH_STOCK_FROM_SQL = "FROM reorder_level r JOIN warehouse w ON w.id = r.warehouse_id "
            + "JOIN product p ON p.id = r.product_id "
            + "LEFT JOIN stock s ON s.warehouse_id = r.warehouse_id AND s.product_id = r.product_id ";

    /**
     * The low-stock rule, shared by the low-stock alerts and the purchase suggestions: the reorder level r of an active
     * product in a warehouse is reached when its stock s there (no stock row counts as zero) is at or below the level.
     */
    private static final String LOW_STOCK_RULE_SQL = "p.active = TRUE AND COALESCE(s.quantity, 0) <= r.level";

    /** The low-stock alerts that also match the extra condition (" AND ...", or empty), in the given order. */
    private List<LowStockAlert> queryLowStockAlerts(String andCondition, String orderBy, Object... params) {
        String sql = "SELECT " + PREFIXED_WAREHOUSE_COLUMNS + ", "
                + "p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, "
                + "COALESCE(s.quantity, 0) AS quantity, r.level " + REORDER_LEVEL_WITH_STOCK_FROM_SQL
                + "WHERE " + LOW_STOCK_RULE_SQL + andCondition + " ORDER BY " + orderBy;
        return queryList(sql, rs -> new LowStockAlert(prefixedWarehouseRow(rs), productRow(rs), rs.getInt("quantity"),
                rs.getInt("level")), params);
    }

    /** The products of the warehouse at or below their reorder level (see {@link #LOW_STOCK_RULE_SQL}), sorted by product name. */
    public List<LowStockAlert> findLowStockAlertsByWarehouse(long warehouseId) {
        return queryLowStockAlerts(" AND r.warehouse_id = ?", "p.name, p.id", warehouseId);
    }

    /**
     * Per product of the warehouse with stock at or below its reorder level (see {@link #LOW_STOCK_RULE_SQL}), the
     * quantity needed to reach twice the reorder level and the supplier with the lowest last unit cost (ties: shorter lead
     * time, then lower supplier id). Products that need nothing are left out; products without supplier have none. Sorted
     * by product name.
     */
    public List<PurchaseSuggestion> findPurchaseSuggestionsByWarehouse(long warehouseId) {
        String sql = "SELECT " + PREFIXED_WAREHOUSE_COLUMNS + ", "
                + "p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, "
                + "COALESCE(s.quantity, 0) AS quantity, r.level, "
                + "su.id AS supplier_id, su.name AS supplier_name, su.contact_email, su.country, su.rating, sp.last_unit_cost "
                + REORDER_LEVEL_WITH_STOCK_FROM_SQL
                + "LEFT JOIN supplier_product sp ON sp.id = (SELECT sp2.id FROM supplier_product sp2 "
                + "WHERE sp2.product_id = p.id ORDER BY sp2.last_unit_cost, sp2.lead_time_days, sp2.supplier_id LIMIT 1) "
                + "LEFT JOIN supplier su ON su.id = sp.supplier_id "
                + "WHERE " + LOW_STOCK_RULE_SQL + " AND r.warehouse_id = ? AND 2 * r.level - COALESCE(s.quantity, 0) > 0 "
                + "ORDER BY p.name, p.id";
        return queryList(sql, rs -> {
            Supplier supplier = null;
            if (rs.getObject("supplier_id") != null) {
                supplier = new Supplier(rs.getLong("supplier_id"), rs.getString("supplier_name"),
                        rs.getString("contact_email"), rs.getString("country"), rs.getInt("rating"));
            }
            int quantity = rs.getInt("quantity");
            int level = rs.getInt("level");
            return new PurchaseSuggestion(prefixedWarehouseRow(rs), productRow(rs), quantity, level, 2 * level - quantity,
                    supplier, rs.getBigDecimal("last_unit_cost"));
        }, warehouseId);
    }

    /** The products at or below their reorder level in all warehouses (see {@link #LOW_STOCK_RULE_SQL}), sorted by warehouse code, then product name. */
    public List<LowStockAlert> findLowStockAlerts() {
        return queryLowStockAlerts("", "w.code, p.name, p.id");
    }

    // ===== Purchase orders and receiving =====

    /**
     * Inserts the purchase order and its lines (numbered from 1) in one transaction; a warehouse id of 0 is stored as
     * no warehouse.
     */
    public PurchaseOrder createPurchaseOrder(PurchaseOrder order) {
        return inTransactionRollingBackOnDatabaseFailureOnly(() -> {
            long id = insert("INSERT INTO purchase_order (number, supplier_id, order_date, expected_date, status, warehouse_id) "
                            + "VALUES (?, ?, ?, ?, ?, ?)",
                    order.number(), order.supplierId(), order.orderDate(), order.expectedDate(), order.status().name(),
                    order.warehouseId() == 0 ? null : order.warehouseId());
            int lineNo = 1;
            for (PurchaseOrderLine line : order.lines()) {
                update("INSERT INTO purchase_order_line (order_id, line_no, product_id, quantity, unit_cost) VALUES (?, ?, ?, ?, ?)",
                        id, lineNo++, line.productId(), line.quantity(), line.unitCost());
            }
            return new PurchaseOrder(id, order.number(), order.supplierId(), order.orderDate(), order.expectedDate(),
                    order.status(), List.copyOf(order.lines()), order.warehouseId());
        });
    }

    public Optional<PurchaseOrder> findPurchaseOrderByNumber(String number) {
        return queryOptional("SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id "
                + "FROM purchase_order WHERE number = ?", this::purchaseOrderRow, number);
    }

    /**
     * Which purchase orders {@link #findPurchaseOrders} returns, and in which order. A factory fixes the base selection
     * and the sort order; the chainable filters narrow it further and all apply together. The statuses: an open
     * purchase order is SENT (sent to the supplier, not yet received); DRAFT, RECEIVED and CANCELLED are not open.
     */
    private static final class PurchaseOrderQuery {
        private static final String EARLIEST_EXPECTED_FIRST = "o.expected_date, o.id";
        private static final String OLDEST_ORDER_FIRST = "o.order_date, o.id";
        private static final String NEWEST_ORDER_FIRST = "o.order_date DESC, o.id DESC";

        private final List<String> conditions = new ArrayList<>();
        private final List<Object> params = new ArrayList<>();
        private String categoryTreeRoot;
        private String sortOrder;
        private Integer limit;

        private PurchaseOrderQuery(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        private PurchaseOrderQuery where(String condition, Object... conditionParams) {
            conditions.add(condition);
            for (Object param : conditionParams) {
                params.add(param);
            }
            return this;
        }

        /** Every purchase order, earliest expected date first (then by id). */
        public static PurchaseOrderQuery all() {
            return new PurchaseOrderQuery(EARLIEST_EXPECTED_FIRST);
        }

        /** The open (SENT) purchase orders, earliest expected date first (then by id). */
        public static PurchaseOrderQuery open() {
            return all().withStatus(PurchaseOrderStatus.SENT);
        }

        /** The overdue purchase orders: open (SENT) with an expected date before the date; earliest expected date first. */
        public static PurchaseOrderQuery overdueOn(LocalDate date) {
            return open().where("o.expected_date < ?", date);
        }

        /** The purchase orders ordered between the two dates, inclusive, oldest order date first (then by id). */
        public static PurchaseOrderQuery orderedBetween(LocalDate from, LocalDate to) {
            return new PurchaseOrderQuery(OLDEST_ORDER_FIRST).where("o.order_date >= ? AND o.order_date <= ?", from, to);
        }

        /** The N most recent purchase orders, cancelled ones left out, newest order date first, then by highest id. */
        public static PurchaseOrderQuery latest(int limit) {
            PurchaseOrderQuery query = new PurchaseOrderQuery(NEWEST_ORDER_FIRST).notCancelled();
            query.limit = limit;
            return query;
        }

        /**
         * The purchase orders matching all the given criteria, earliest expected date first (then by id). Every
         * criterion is optional: a null supplier id, warehouse id, status, from date or to date is not applied. The
         * dates are inclusive and apply to the expected date.
         */
        public static PurchaseOrderQuery matching(Long supplierId, Long warehouseId, PurchaseOrderStatus status,
                                                  LocalDate expectedFrom, LocalDate expectedTo) {
            PurchaseOrderQuery query = all();
            if (supplierId != null) {
                query.ofSupplier(supplierId);
            }
            if (warehouseId != null) {
                query.intoWarehouse(warehouseId);
            }
            if (status != null) {
                query.withStatus(status);
            }
            if (expectedFrom != null) {
                query.expectedFrom(expectedFrom);
            }
            if (expectedTo != null) {
                query.expectedUntil(expectedTo);
            }
            return query;
        }

        /** Only the purchase orders of the supplier. */
        public PurchaseOrderQuery ofSupplier(long supplierId) {
            return where("o.supplier_id = ?", supplierId);
        }

        /** Only the purchase orders to be delivered into the warehouse (their warehouse_id). */
        public PurchaseOrderQuery intoWarehouse(long warehouseId) {
            return where("o.warehouse_id = ?", warehouseId);
        }

        /** Only the purchase orders to be delivered into a warehouse of the region. */
        public PurchaseOrderQuery intoWarehouseInRegion(Region region) {
            return where("EXISTS (SELECT 1 FROM warehouse w WHERE w.id = o.warehouse_id AND w.region = ?)", region.name());
        }

        /**
         * Only the purchase orders to be delivered into a warehouse in a region where the country has customers
         * (purchase orders have no customer of their own); listed once, however many such customers there are.
         */
        public PurchaseOrderQuery intoWarehouseInRegionOfCustomersInCountry(String country) {
            return where("EXISTS (SELECT 1 FROM warehouse w JOIN customer c ON c.region = w.region "
                    + "WHERE w.id = o.warehouse_id AND c.country = ?)", country);
        }

        /** Only the purchase orders with the status. */
        public PurchaseOrderQuery withStatus(PurchaseOrderStatus status) {
            return where("o.status = ?", status);
        }

        /** Leaves the cancelled purchase orders out. */
        public PurchaseOrderQuery notCancelled() {
            return where("o.status <> 'CANCELLED'");
        }

        /** Only the purchase orders expected on or after the date. */
        public PurchaseOrderQuery expectedFrom(LocalDate from) {
            return where("o.expected_date >= ?", from);
        }

        /** Only the purchase orders expected on or before the date. */
        public PurchaseOrderQuery expectedUntil(LocalDate to) {
            return where("o.expected_date <= ?", to);
        }

        /** Only the purchase orders with a line of the product. */
        public PurchaseOrderQuery withLineOfProduct(long productId) {
            return where("EXISTS (SELECT 1 FROM purchase_order_line l WHERE l.order_id = o.id AND l.product_id = ?)", productId);
        }

        /** Only the purchase orders with a line for a product of the category or of any category below it (one category per query). */
        public PurchaseOrderQuery withLineInCategoryTree(String category) {
            categoryTreeRoot = category;
            return where("EXISTS (SELECT 1 FROM purchase_order_line l JOIN product p ON p.id = l.product_id "
                    + "WHERE l.order_id = o.id AND p.category IN (SELECT code FROM tree))");
        }

        /** Sorts newest order date first, then by highest id. */
        public PurchaseOrderQuery newestFirst() {
            sortOrder = NEWEST_ORDER_FIRST;
            return this;
        }
    }

    /** The purchase orders selected by the query, in its order, with their lines. */
    private List<PurchaseOrder> findPurchaseOrders(PurchaseOrderQuery query) {
        List<Object> params = new ArrayList<>();
        String sql = "";
        if (query.categoryTreeRoot != null) {
            sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                    + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) ";
            params.add(query.categoryTreeRoot);
        }
        sql += "SELECT o.id, o.number, o.supplier_id, o.order_date, o.expected_date, o.status, o.warehouse_id "
                + "FROM purchase_order o"
                + (query.conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", query.conditions))
                + " ORDER BY " + query.sortOrder;
        params.addAll(query.params);
        if (query.limit != null) {
            sql += " LIMIT ?";
            params.add(query.limit);
        }
        return queryList(sql, this::purchaseOrderRow, params.toArray());
    }

    /** The purchase orders of the supplier, newest order date first, then by highest id. */
    public List<PurchaseOrder> findPurchaseOrdersOfSupplierNewestFirst(long supplierId) {
        return findPurchaseOrders(PurchaseOrderQuery.all().ofSupplier(supplierId).newestFirst());
    }

    /** The open (SENT) purchase orders, earliest expected date first (then by id). */
    public List<PurchaseOrder> findOpenPurchaseOrders() {
        return findPurchaseOrders(PurchaseOrderQuery.open());
    }

    /** The open (SENT) purchase orders of the supplier, earliest expected date first (then by id). */
    public List<PurchaseOrder> findOpenPurchaseOrdersOfSupplier(long supplierId) {
        return findPurchaseOrders(PurchaseOrderQuery.open().ofSupplier(supplierId));
    }

    /** The open (SENT) purchase orders to be delivered into the warehouse, earliest expected date first (then by id). */
    public List<PurchaseOrder> findOpenPurchaseOrdersIntoWarehouse(long warehouseId) {
        return findPurchaseOrders(PurchaseOrderQuery.open().intoWarehouse(warehouseId));
    }

    /** Sent purchase orders whose expected date is before the given date, earliest expected date first (then by id). */
    public List<PurchaseOrder> findOverduePurchaseOrders(LocalDate date) {
        return findPurchaseOrders(PurchaseOrderQuery.overdueOn(date));
    }

    /** Sent purchase orders into the warehouse whose expected date is before the given date, earliest expected first. */
    public List<PurchaseOrder> findOverduePurchaseOrdersIntoWarehouse(long warehouseId, LocalDate date) {
        return findPurchaseOrders(PurchaseOrderQuery.overdueOn(date).intoWarehouse(warehouseId));
    }

    /**
     * The purchase orders matching all the given criteria, earliest expected date first (then by id). Every criterion is
     * optional: a null supplier id, warehouse id, status, from date or to date is not applied. The dates are inclusive
     * and apply to the expected date.
     */
    public List<PurchaseOrder> findPurchaseOrdersMatching(Long supplierId, Long warehouseId, PurchaseOrderStatus status,
                                                          LocalDate from, LocalDate to) {
        return findPurchaseOrders(PurchaseOrderQuery.matching(supplierId, warehouseId, status, from, to));
    }

    /** Purchase orders with a line of the product ordered between the two dates, inclusive, oldest first (then by id). */
    public List<PurchaseOrder> findPurchaseOrdersWithProductOrderedBetween(long productId, LocalDate from, LocalDate to) {
        return findPurchaseOrders(PurchaseOrderQuery.orderedBetween(from, to).withLineOfProduct(productId));
    }

    /**
     * The non-cancelled purchase orders with a line for a product of the category or of any category below it, ordered
     * between the two dates, inclusive, oldest first (then by id).
     */
    public List<PurchaseOrder> findNonCancelledPurchaseOrdersInCategoryTreeOrderedBetween(String category, LocalDate from,
                                                                                          LocalDate to) {
        return findPurchaseOrders(PurchaseOrderQuery.orderedBetween(from, to).notCancelled().withLineInCategoryTree(category));
    }

    /**
     * The purchase orders dated between the two dates, inclusive, for the warehouses in the regions of the customers of
     * the country (purchase orders have no customer of their own), oldest first, then by id. A purchase order is listed
     * once, however many customers the country has in the region; cancelled ones are included.
     */
    public List<PurchaseOrder> findPurchaseOrdersForCustomerCountryOrderedBetween(String country, LocalDate from, LocalDate to) {
        return findPurchaseOrders(PurchaseOrderQuery.orderedBetween(from, to).intoWarehouseInRegionOfCustomersInCountry(country));
    }

    /**
     * The quantity of the product on purchase orders ordered between the two dates, inclusive, per supplier. Sorted by
     * quantity, highest first (then by supplier name, then id); suppliers without such orders are left out.
     */
    public List<SupplierPurchasedQuantity> findPurchasedQuantityPerSupplier(long productId, LocalDate from, LocalDate to) {
        return queryList("SELECT s.id, s.name, s.contact_email, s.country, s.rating, SUM(l.quantity) AS purchased "
                        + "FROM supplier s JOIN purchase_order o ON o.supplier_id = s.id "
                        + "JOIN purchase_order_line l ON l.order_id = o.id "
                        + "WHERE l.product_id = ? AND o.order_date >= ? AND o.order_date <= ? "
                        + "GROUP BY s.id, s.name, s.contact_email, s.country, s.rating ORDER BY purchased DESC, s.name, s.id",
                rs -> new SupplierPurchasedQuantity(supplierRow(rs), rs.getLong("purchased")), productId, from, to);
    }

    /**
     * The spend (quantity times unit cost) of the non-cancelled purchase orders ordered between the two dates,
     * inclusive, on products of the category and of all categories below it, per category of the product. Sorted by
     * spend, highest first (then by category); categories without such spend are left out.
     */
    public List<CategorySpend> findNonCancelledPurchaseSpendPerCategory(String category, LocalDate from, LocalDate to) {
        return queryList("WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                        + "SELECT p.category AS category, SUM(l.quantity * l.unit_cost) AS spend "
                        + "FROM purchase_order o JOIN purchase_order_line l ON l.order_id = o.id "
                        + "JOIN product p ON p.id = l.product_id "
                        + "WHERE o.status <> 'CANCELLED' AND o.order_date >= ? AND o.order_date <= ? "
                        + "AND p.category IN (SELECT code FROM tree) "
                        + "GROUP BY p.category ORDER BY spend DESC, p.category",
                rs -> new CategorySpend(rs.getString("category"), rs.getBigDecimal("spend")), category, from, to);
    }

    /**
     * Adds the quantities of a sent purchase order to the stock of the warehouse (creating missing stock rows), records
     * a RECEIPT stock movement per line and marks the order as received into the warehouse on the date, all or
     * nothing; returns false, changing nothing, if the purchase order is not sent.
     */
    public boolean receivePurchaseOrder(long orderId, long warehouseId, LocalDate receivedDate) {
        return inTransaction(() -> {
            int marked = update("UPDATE purchase_order SET status = 'RECEIVED', received_date = ?, received_warehouse_id = ? "
                    + "WHERE id = ? AND status = 'SENT'", receivedDate, warehouseId, orderId);
            if (marked == 0) {
                return false;
            }
            for (PurchaseOrderLine line : purchaseOrderLines(orderId)) {
                if (update("UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?",
                        line.quantity(), warehouseId, line.productId()) == 0) {
                    update("INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)",
                            warehouseId, line.productId(), line.quantity());
                }
                update("INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                        + "VALUES (?, ?, ?, ?, 'RECEIPT')", line.productId(), warehouseId, receivedDate, line.quantity());
            }
            return true;
        });
    }

    public Optional<LocalDate> findPurchaseOrderReceivedDate(long orderId) {
        return queryOptional("SELECT received_date FROM purchase_order WHERE id = ?",
                rs -> Optional.ofNullable(rs.getObject("received_date", LocalDate.class)), orderId).flatMap(date -> date);
    }

    /**
     * The purchase orders received into the warehouse (received_warehouse_id) between the two dates, inclusive, after
     * their expected date, with the days late, ordered by receiving date (then by id).
     */
    public List<LatePurchaseOrder> findLatePurchaseOrdersReceivedIntoWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return queryList("SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id, received_date, "
                        + "DATEDIFF('DAY', expected_date, received_date) AS days_late FROM purchase_order "
                        + "WHERE status = 'RECEIVED' AND received_warehouse_id = ? AND received_date >= ? AND received_date <= ? "
                        + "AND received_date > expected_date ORDER BY received_date, id",
                rs -> new LatePurchaseOrder(purchaseOrderRow(rs), rs.getObject("received_date", LocalDate.class),
                        rs.getInt("days_late")), warehouseId, from, to);
    }

    /** The N most recent purchase orders (cancelled ones left out), newest first, then by highest id. */
    public List<PurchaseOrder> findLatestPurchaseOrders(int limit) {
        return findPurchaseOrders(PurchaseOrderQuery.latest(limit));
    }

    /**
     * The N most recent purchase orders to be delivered to warehouses in the region (cancelled ones left out), newest
     * first, then by highest id.
     */
    public List<PurchaseOrder> findLatestPurchaseOrdersIntoRegion(Region region, int limit) {
        return findPurchaseOrders(PurchaseOrderQuery.latest(limit).intoWarehouseInRegion(region));
    }

    /** The N most recent purchase orders of the supplier (cancelled ones left out), newest first, then by highest id. */
    public List<PurchaseOrder> findLatestPurchaseOrdersOfSupplier(long supplierId, int limit) {
        return findPurchaseOrders(PurchaseOrderQuery.latest(limit).ofSupplier(supplierId));
    }

    /**
     * Per supplier, the purchase orders received between the two dates, inclusive: their number, the average delay in days of
     * the received date against the expected date (negative if early) and the percentage received later than expected.
     * Sorted by supplier name; suppliers without received orders in the period are left out.
     */
    public List<SupplierPerformance> findDeliveryPerformancePerSupplier(LocalDate from, LocalDate to) {
        return queryList("SELECT s.id, s.name, s.contact_email, s.country, s.rating, COUNT(o.id) AS received_count, "
                        + "AVG(CAST(DATEDIFF('DAY', o.expected_date, o.received_date) AS DECIMAL(10, 2))) AS average_delay, "
                        + "COALESCE(SUM(CASE WHEN o.received_date > o.expected_date THEN 1 ELSE 0 END), 0) AS late_count "
                        + "FROM supplier s JOIN purchase_order o ON o.supplier_id = s.id "
                        + "WHERE o.status = 'RECEIVED' AND o.received_date >= ? AND o.received_date <= ? "
                        + "GROUP BY s.id, s.name, s.contact_email, s.country, s.rating ORDER BY s.name, s.id",
                rs -> {
                    int receivedCount = rs.getInt("received_count");
                    BigDecimal lateShare = BigDecimal.valueOf(rs.getInt("late_count") * 100L)
                            .divide(BigDecimal.valueOf(receivedCount), 2, RoundingMode.HALF_UP);
                    return new SupplierPerformance(supplierRow(rs), receivedCount,
                            rs.getBigDecimal("average_delay").setScale(2, RoundingMode.HALF_UP), lateShare);
                }, from, to);
    }

    /**
     * The lines of the purchase orders received between the two dates, inclusive, joined to their supplier (s), order
     * (o) and lines (l, LEFT JOIN so that an order without lines still counts); the value of a line is quantity times
     * unit cost. Shared by the received purchase spending per supplier and per supplier country.
     */
    private static final String RECEIVED_PURCHASE_ORDER_LINES_IN_PERIOD = "FROM supplier s JOIN purchase_order o ON o.supplier_id = s.id "
            + "LEFT JOIN purchase_order_line l ON l.order_id = o.id "
            + "WHERE o.status = 'RECEIVED' AND o.received_date >= ? AND o.received_date <= ? ";

    /**
     * Number and value (quantity times unit cost of the lines) of the purchase orders received between the two dates,
     * inclusive, per supplier. Sorted by value, highest first (then by supplier name, then id); suppliers without
     * received orders are left out.
     */
    public List<SupplierSpending> findReceivedPurchaseSpendingPerSupplier(LocalDate from, LocalDate to) {
        return queryList("SELECT s.id, s.name, s.contact_email, s.country, s.rating, COUNT(DISTINCT o.id) AS order_count, "
                        + "COALESCE(SUM(l.quantity * l.unit_cost), 0) AS total "
                        + RECEIVED_PURCHASE_ORDER_LINES_IN_PERIOD
                        + "GROUP BY s.id, s.name, s.contact_email, s.country, s.rating ORDER BY total DESC, s.name, s.id",
                rs -> new SupplierSpending(supplierRow(rs), rs.getInt("order_count"), rs.getBigDecimal("total")), from, to);
    }

    /**
     * Number and value of the purchase orders received between the two dates, inclusive, per country of the supplier,
     * valued as for the spending per supplier. Sorted by value, highest first (then by country); countries without
     * received orders are left out.
     */
    public List<CountrySpending> findReceivedPurchaseSpendingPerSupplierCountry(LocalDate from, LocalDate to) {
        return queryList("SELECT s.country AS country, COUNT(DISTINCT o.id) AS order_count, "
                        + "COALESCE(SUM(l.quantity * l.unit_cost), 0) AS total "
                        + RECEIVED_PURCHASE_ORDER_LINES_IN_PERIOD
                        + "GROUP BY s.country ORDER BY total DESC, s.country",
                rs -> new CountrySpending(rs.getString("country"), rs.getInt("order_count"), rs.getBigDecimal("total")),
                from, to);
    }

    // ===== Sales orders: creation, lists, status changes, cancellation, drafts, archive, history, line discounts and approvals =====

    /** Inserts the order and its lines (numbered from 1 in list order) in one transaction. */
    public SalesOrder createSalesOrder(SalesOrder order) {
        return inTransactionRollingBackOnDatabaseFailureOnly(() -> {
            long id = insert("INSERT INTO sales_order (number, customer_id, order_date, status) VALUES (?, ?, ?, ?)",
                    order.number(), order.customerId(), order.orderDate(), order.status().name());
            int lineNo = 1;
            for (OrderLine line : order.lines()) {
                update("INSERT INTO sales_order_line (order_id, line_no, product_id, quantity, unit_price, promotion_code) "
                        + "VALUES (?, ?, ?, ?, ?, ?)",
                        id, lineNo++, line.productId(), line.quantity(), line.unitPrice(), line.promotionCode());
            }
            return new SalesOrder(id, order.number(), order.customerId(), order.orderDate(), order.status(),
                    List.copyOf(order.lines()));
        });
    }

    /** The order with the number, archived or not, whatever its status. */
    public Optional<SalesOrder> findSalesOrderByNumber(String number) {
        return queryOptional("SELECT id, number, customer_id, order_date, status FROM sales_order WHERE number = ?",
                this::salesOrderRow, number);
    }

    /**
     * The one place of the selection rules of the sales order lists, counts and values below (used through
     * {@link #querySalesOrders}, {@link #querySalesOrderCount} and {@link #querySalesOrderValue}). Start with a factory
     * (it fixes the status rule and whether archived orders count), then narrow with filters; all filters must hold.
     * Orders come oldest first (order date, then id) unless {@link #newestFirst()} is called.
     */
    private static final class SalesOrderQuery {
        private static final String NOT_ARCHIVED = "NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = o.id)";

        private boolean joinArchive;
        private boolean joinCustomer;
        private boolean joinChannel;
        private final List<String> conditions = new ArrayList<>();
        private final List<Object> params = new ArrayList<>();
        private boolean newestFirst;
        private Integer limit;

        private SalesOrderQuery() {
        }

        private SalesOrderQuery where(String condition, Object... values) {
            conditions.add(condition);
            for (Object value : values) {
                params.add(value);
            }
            return this;
        }

        /** Open orders: status CONFIRMED and not archived. */
        public static SalesOrderQuery open() {
            return new SalesOrderQuery().where("o.status = 'CONFIRMED'").where(NOT_ARCHIVED);
        }

        /** Orders of any status that are not archived. */
        public static SalesOrderQuery notArchived() {
            return new SalesOrderQuery().where(NOT_ARCHIVED);
        }

        /** Only the archived orders, of any status. */
        public static SalesOrderQuery archived() {
            SalesOrderQuery query = new SalesOrderQuery();
            query.joinArchive = true;
            return query;
        }

        /** All orders of any status, archived ones included. */
        public static SalesOrderQuery includingArchived() {
            return new SalesOrderQuery();
        }

        /** Orders that are not CANCELLED, archived ones included. */
        public static SalesOrderQuery notCancelled() {
            return new SalesOrderQuery().where("o.status <> 'CANCELLED'");
        }

        /** The N most recently placed orders that are not CANCELLED (archived ones included), newest first, then by highest id. */
        public static SalesOrderQuery latest(int limit) {
            SalesOrderQuery query = notCancelled().newestFirst();
            query.limit = limit;
            return query;
        }

        /**
         * Orders awaiting a discount approval: DRAFT orders with a line discount above the percentage and no approval
         * decision yet (archived ones included).
         */
        public static SalesOrderQuery awaitingApproval(BigDecimal percent) {
            return new SalesOrderQuery().where("o.status = 'DRAFT'")
                    .where("EXISTS (SELECT 1 FROM order_line_discount d WHERE d.order_id = o.id AND d.discount_percent > ?)", percent)
                    .where("NOT EXISTS (SELECT 1 FROM order_approval a WHERE a.order_id = o.id)");
        }

        /**
         * The order search: not archived orders (of customers that exist) matching all the given criteria, newest first.
         * Every criterion is optional: null means no restriction. The dates are inclusive.
         */
        public static SalesOrderQuery search(Long customerId, OrderStatus status, Region region, LocalDate from, LocalDate to) {
            SalesOrderQuery query = notArchived().newestFirst();
            query.joinCustomer = true;
            if (customerId != null) {
                query.ofCustomer(customerId);
            }
            if (status != null) {
                query.withStatus(status);
            }
            if (region != null) {
                query.inRegion(region);
            }
            if (from != null) {
                query.where("o.order_date >= ?", from);
            }
            if (to != null) {
                query.where("o.order_date <= ?", to);
            }
            return query;
        }

        /** Only orders of the customer. */
        public SalesOrderQuery ofCustomer(long customerId) {
            return where("o.customer_id = ?", customerId);
        }

        /** Only orders of customers in the region. */
        public SalesOrderQuery inRegion(Region region) {
            joinCustomer = true;
            return where("c.region = ?", region.name());
        }

        /** Only orders of customers in the country. */
        public SalesOrderQuery inCountry(String country) {
            joinCustomer = true;
            return where("c.country = ?", country);
        }

        /** Only orders with the status. */
        public SalesOrderQuery withStatus(OrderStatus status) {
            return where("o.status = ?", status.name());
        }

        /** Only orders that came from the channel. */
        public SalesOrderQuery fromChannel(SalesChannel channel) {
            joinChannel = true;
            return where("oc.channel = ?", channel.name());
        }

        /** Only the order with the number. */
        public SalesOrderQuery withNumber(String number) {
            return where("o.number = ?", number);
        }

        /** Only orders placed between the two dates, inclusive. */
        public SalesOrderQuery placedBetween(LocalDate from, LocalDate to) {
            return where("o.order_date >= ? AND o.order_date <= ?", from, to);
        }

        /** Only orders placed on or before the date. */
        public SalesOrderQuery placedOnOrBefore(LocalDate date) {
            return where("o.order_date <= ?", date);
        }

        /** Newest first (order date descending, then highest id) instead of oldest first. */
        public SalesOrderQuery newestFirst() {
            newestFirst = true;
            return this;
        }

        /** The FROM and WHERE clauses of the selected orders (alias o), with the extra joins added before WHERE. */
        private String fromAndWhere(String extraJoins) {
            return " FROM sales_order o"
                    + (joinArchive ? " JOIN order_archive ar ON ar.order_id = o.id" : "")
                    + (joinCustomer ? " JOIN customer c ON c.id = o.customer_id" : "")
                    + (joinChannel ? " JOIN order_channel oc ON oc.order_id = o.id" : "")
                    + extraJoins
                    + (conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions));
        }

        private String selectSql() {
            return "SELECT o.id, o.number, o.customer_id, o.order_date, o.status" + fromAndWhere("")
                    + (newestFirst ? " ORDER BY o.order_date DESC, o.id DESC" : " ORDER BY o.order_date, o.id")
                    + (limit != null ? " LIMIT ?" : "");
        }

        private Object[] selectParams() {
            List<Object> all = new ArrayList<>(params);
            if (limit != null) {
                all.add(limit);
            }
            return all.toArray();
        }
    }

    /** The sales orders selected by the query, with their lines. */
    private List<SalesOrder> querySalesOrders(SalesOrderQuery query) {
        return queryList(query.selectSql(), this::salesOrderRow, query.selectParams());
    }

    /** Number of sales orders selected by the query (its ordering and limit don't apply). */
    private int querySalesOrderCount(SalesOrderQuery query) {
        return querySingle("SELECT COUNT(*) AS order_count" + query.fromAndWhere(""), rs -> rs.getInt("order_count"),
                query.params.toArray());
    }

    /**
     * Sum of quantity times unit price over the lines of the sales orders selected by the query (its ordering and limit
     * don't apply); zero if there are no such orders or no lines.
     */
    private BigDecimal querySalesOrderValue(SalesOrderQuery query) {
        return querySingle("SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) AS total"
                        + query.fromAndWhere(" JOIN sales_order_line l ON l.order_id = o.id"),
                rs -> rs.getBigDecimal("total"), query.params.toArray());
    }

    /** The not archived sales orders of the customer, of any status, newest first (then by highest id). */
    public List<SalesOrder> findSalesOrdersOfCustomer(long customerId) {
        return querySalesOrders(SalesOrderQuery.notArchived().ofCustomer(customerId).newestFirst());
    }

    /** The open sales orders (see {@link SalesOrderQuery#open()}), oldest first. */
    public List<SalesOrder> findOpenSalesOrders() {
        return querySalesOrders(SalesOrderQuery.open());
    }

    /** Number of open sales orders (confirmed, not archived) placed on or before the date. */
    public int countOpenSalesOrdersOnDate(LocalDate date) {
        return querySalesOrderCount(SalesOrderQuery.open().placedOnOrBefore(date));
    }

    /** Sum of quantity times unit price over the lines of the orders counted by countOpenSalesOrdersOnDate. */
    public BigDecimal sumOpenSalesOrderValueOnDate(LocalDate date) {
        return querySalesOrderValue(SalesOrderQuery.open().placedOnOrBefore(date));
    }

    /** The open sales orders (confirmed, not archived) of the customer, oldest first. */
    public List<SalesOrder> findOpenSalesOrdersOfCustomer(long customerId) {
        return querySalesOrders(SalesOrderQuery.open().ofCustomer(customerId));
    }

    /** The open sales orders (confirmed, not archived) of the customers in the region, oldest first. */
    public List<SalesOrder> findOpenSalesOrdersInRegion(Region region) {
        return querySalesOrders(SalesOrderQuery.open().inRegion(region));
    }

    /** Sales orders of any status, archived ones included, placed between the two dates, inclusive, oldest first. */
    public List<SalesOrder> findSalesOrdersPlacedBetween(LocalDate from, LocalDate to) {
        return querySalesOrders(SalesOrderQuery.includingArchived().placedBetween(from, to));
    }

    /** Columns: customer_id, name, email, phone, country, region, customer_status (the customer of a sales order row). */
    private static Customer salesOrderCustomerRow(ResultSet rs) throws SQLException {
        return new Customer(rs.getLong("customer_id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"),
                rs.getString("country"), Region.valueOf(rs.getString("region")), CustomerStatus.valueOf(rs.getString("customer_status")));
    }

    /** The order columns (for salesOrderRow) and the customer columns (for salesOrderCustomerRow) of o and c. */
    private static final String SALES_ORDER_AND_CUSTOMER_COLUMNS = "o.id, o.number, o.customer_id, o.order_date, o.status, "
            + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status";

    /** The shared rule of the orders of a product or category: not cancelled, placed between the two dates, inclusive. */
    private static final String PRODUCT_ORDERS_FROM_AND_WHERE = "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
            + "WHERE o.status <> 'CANCELLED' AND o.order_date >= ? AND o.order_date <= ? ";

    private ProductOrder productOrderRow(ResultSet rs) throws SQLException {
        return new ProductOrder(salesOrderRow(rs), salesOrderCustomerRow(rs));
    }

    /**
     * The non-cancelled sales orders containing the product, placed between the two dates, inclusive, with the
     * customer who placed each, in the given order.
     */
    private List<ProductOrder> querySalesOrdersWithProduct(long productId, LocalDate from, LocalDate to, String orderBy) {
        return queryList("SELECT " + SALES_ORDER_AND_CUSTOMER_COLUMNS + " " + PRODUCT_ORDERS_FROM_AND_WHERE
                        + "AND EXISTS (SELECT 1 FROM sales_order_line l WHERE l.order_id = o.id AND l.product_id = ?) "
                        + "ORDER BY " + orderBy,
                this::productOrderRow, from, to, productId);
    }

    /**
     * The non-cancelled sales orders containing the product, placed between the two dates, inclusive, with the
     * customer who placed each, newest first (then by highest id).
     */
    public List<ProductOrder> findSalesOrdersWithProduct(long productId, LocalDate from, LocalDate to) {
        return querySalesOrdersWithProduct(productId, from, to, "o.order_date DESC, o.id DESC");
    }

    /**
     * The non-cancelled sales orders containing the product, placed between the two dates, inclusive, with the
     * customer who placed each. Grouped per customer (customers by name, then id), newest first within a customer.
     */
    public List<ProductOrder> findSalesOrdersWithProductPerCustomer(long productId, LocalDate from, LocalDate to) {
        return querySalesOrdersWithProduct(productId, from, to, "c.name, c.id, o.order_date DESC, o.id DESC");
    }

    /**
     * The non-cancelled sales orders containing a product of the category or of any category below it, placed between
     * the two dates, inclusive, with the customer who placed each, newest first.
     */
    public List<ProductOrder> findSalesOrdersWithCategory(String category, LocalDate from, LocalDate to) {
        return queryList("WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                        + "SELECT " + SALES_ORDER_AND_CUSTOMER_COLUMNS + " " + PRODUCT_ORDERS_FROM_AND_WHERE
                        + "AND EXISTS (SELECT 1 FROM sales_order_line l JOIN product p ON p.id = l.product_id "
                        + "WHERE l.order_id = o.id AND p.category IN (SELECT code FROM tree)) "
                        + "ORDER BY o.order_date DESC, o.id DESC",
                this::productOrderRow, category, from, to);
    }

    /**
     * The revenue per product category and calendar month of the non-cancelled sales orders placed between the two
     * dates, inclusive (quantity times unit price of the lines, by the category of the product itself). Only
     * combinations with sales; by category, then month.
     */
    public List<CategoryMonthRevenue> findSalesOrderRevenuePerCategoryAndMonth(LocalDate from, LocalDate to) {
        return queryList("SELECT p.category AS category, YEAR(o.order_date) AS year_no, MONTH(o.order_date) AS month_no, "
                        + "SUM(l.quantity * l.unit_price) AS revenue "
                        + "FROM sales_order o JOIN sales_order_line l ON l.order_id = o.id "
                        + "JOIN product p ON p.id = l.product_id "
                        + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                        + "GROUP BY p.category, YEAR(o.order_date), MONTH(o.order_date) "
                        + "ORDER BY p.category, YEAR(o.order_date), MONTH(o.order_date)",
                rs -> new CategoryMonthRevenue(rs.getString("category"),
                        YearMonth.of(rs.getInt("year_no"), rs.getInt("month_no")), rs.getBigDecimal("revenue")),
                from, to);
    }

    /** Sets (or replaces) the channel the sales order came from. */
    public void saveSalesOrderChannel(long orderId, SalesChannel channel) {
        update("MERGE INTO order_channel (order_id, channel) KEY (order_id) VALUES (?, ?)", orderId, channel.name());
    }

    /** Sales orders of the channel, any status, archived ones included, placed between the two dates, inclusive, oldest first. */
    public List<SalesOrder> findSalesOrdersFromChannel(SalesChannel channel, LocalDate from, LocalDate to) {
        return querySalesOrders(SalesOrderQuery.includingArchived().fromChannel(channel).placedBetween(from, to));
    }

    /**
     * The order search: not archived sales orders matching all the given criteria, newest first. Every criterion is
     * optional: null means no restriction. The dates are inclusive.
     */
    public List<SalesOrder> findSalesOrdersMatching(Long customerId, OrderStatus status, Region region, LocalDate from, LocalDate to) {
        return querySalesOrders(SalesOrderQuery.search(customerId, status, region, from, to));
    }

    /** Sales orders with the status, archived ones included, placed between the two dates, inclusive, oldest first. */
    public List<SalesOrder> findSalesOrdersPlacedBetweenWithStatus(LocalDate from, LocalDate to, OrderStatus status) {
        return querySalesOrders(SalesOrderQuery.includingArchived().placedBetween(from, to).withStatus(status));
    }

    /** Sets the status of the order and adds a status history entry, in one transaction. */
    public void changeSalesOrderStatus(long orderId, OrderStatus status, LocalDate date) {
        inTransactionRollingBackOnDatabaseFailureOnly(() -> {
            update("UPDATE sales_order SET status = ? WHERE id = ?", status.name(), orderId);
            update("INSERT INTO sales_order_status_change (order_id, status, change_date) VALUES (?, ?, ?)",
                    orderId, status.name(), date);
            return null;
        });
    }

    /** Sets the order to CANCELLED, with a status history entry and the cancellation reason, in one transaction. */
    public void cancelSalesOrder(long orderId, CancellationReason reason, LocalDate date) {
        inTransactionRollingBackOnDatabaseFailureOnly(() -> {
            update("UPDATE sales_order SET status = 'CANCELLED' WHERE id = ?", orderId);
            update("INSERT INTO sales_order_status_change (order_id, status, change_date) VALUES (?, 'CANCELLED', ?)",
                    orderId, date);
            update("INSERT INTO order_cancellation (order_id, reason, cancel_date) VALUES (?, ?, ?)",
                    orderId, reason.name(), date);
            return null;
        });
    }

    /**
     * Number of sales orders cancelled between the two dates (inclusive), per reason and customer. Sorted by reason,
     * then customer name, then customer id; combinations without cancellations are left out.
     */
    public List<CancellationCount> findCancelledSalesOrderCountPerReasonAndCustomer(LocalDate from, LocalDate to) {
        return queryList("SELECT x.reason AS reason, c.id AS id, c.name AS name, COUNT(*) AS order_count "
                        + "FROM order_cancellation x JOIN sales_order o ON o.id = x.order_id JOIN customer c ON c.id = o.customer_id "
                        + "WHERE x.cancel_date >= ? AND x.cancel_date <= ? "
                        + "GROUP BY x.reason, c.id, c.name ORDER BY x.reason, c.name, c.id",
                rs -> new CancellationCount(CancellationReason.valueOf(rs.getString("reason")), rs.getLong("id"),
                        rs.getString("name"), rs.getInt("order_count")),
                from, to);
    }

    /**
     * The sales orders cancelled between the two dates, inclusive (by cancellation date), with the cancellation date
     * and reason and the customer who placed each, in the given order.
     */
    private List<CancelledOrder> queryCancelledSalesOrders(LocalDate from, LocalDate to, String orderBy) {
        return queryList("SELECT " + SALES_ORDER_AND_CUSTOMER_COLUMNS + ", x.cancel_date, x.reason "
                        + "FROM order_cancellation x JOIN sales_order o ON o.id = x.order_id JOIN customer c ON c.id = o.customer_id "
                        + "WHERE x.cancel_date >= ? AND x.cancel_date <= ? "
                        + "ORDER BY " + orderBy,
                rs -> new CancelledOrder(salesOrderRow(rs), rs.getObject("cancel_date", LocalDate.class),
                        CancellationReason.valueOf(rs.getString("reason")), salesOrderCustomerRow(rs)),
                from, to);
    }

    /**
     * The sales orders cancelled between the two dates, inclusive (by cancellation date), with the customer who placed
     * each. Grouped per customer (customers by name, then id), newest cancellation first within a customer.
     */
    public List<CancelledOrder> findCancelledSalesOrdersPerCustomer(LocalDate from, LocalDate to) {
        return queryCancelledSalesOrders(from, to, "c.name, c.id, x.cancel_date DESC, o.id DESC");
    }

    /**
     * The sales orders cancelled between the two dates, inclusive (by cancellation date), with the customer who placed
     * each. Grouped per region of the customer (regions in alphabetical order), newest cancellation first within a region.
     */
    public List<CancelledOrder> findCancelledSalesOrdersPerRegion(LocalDate from, LocalDate to) {
        return queryCancelledSalesOrders(from, to, "c.region, x.cancel_date DESC, o.id DESC");
    }

    /**
     * The draft sales orders older than the given number of days on the date (order date before the date minus the
     * days), with their age in days, the customer and the sales rep of the customer (null if none), in the given order.
     */
    private List<DraftOrder> queryStaleDraftSalesOrders(LocalDate asOf, int days, String orderBy) {
        return queryList("SELECT " + SALES_ORDER_AND_CUSTOMER_COLUMNS + ", "
                        + "r.id AS rep_id, r.name AS rep_name, r.email AS rep_email, r.region AS rep_region "
                        + "FROM sales_order o JOIN customer c ON c.id = o.customer_id LEFT JOIN sales_rep r ON r.id = c.sales_rep_id "
                        + "WHERE o.status = 'DRAFT' AND o.order_date < ? "
                        + "ORDER BY " + orderBy,
                rs -> {
                    SalesOrder order = salesOrderRow(rs);
                    Customer customer = salesOrderCustomerRow(rs);
                    SalesRep salesRep = rs.getObject("rep_id") == null ? null : new SalesRep(rs.getLong("rep_id"),
                            rs.getString("rep_name"), rs.getString("rep_email"), Region.valueOf(rs.getString("rep_region")));
                    return new DraftOrder(order, ChronoUnit.DAYS.between(order.orderDate(), asOf), customer, salesRep);
                },
                asOf.minusDays(days));
    }

    /**
     * The stale draft sales orders (see queryStaleDraftSalesOrders) grouped per customer (customers by name, then id),
     * oldest order first within a customer.
     */
    public List<DraftOrder> findStaleDraftSalesOrdersPerCustomer(LocalDate asOf, int days) {
        return queryStaleDraftSalesOrders(asOf, days, "c.name, c.id, o.order_date, o.id");
    }

    /**
     * The stale draft sales orders (see queryStaleDraftSalesOrders) grouped per sales rep of the customer (reps by
     * name, then id; customers without a rep last), then by customer, oldest order first.
     */
    public List<DraftOrder> findStaleDraftSalesOrdersPerSalesRep(LocalDate asOf, int days) {
        return queryStaleDraftSalesOrders(asOf, days, "r.name NULLS LAST, r.id, c.name, c.id, o.order_date, o.id");
    }

    /** Archives the delivered or cancelled orders whose last status change is before the cutoff date; returns how many. */
    public int archiveSalesOrders(LocalDate cutoff, LocalDate archiveDate) {
        return update("INSERT INTO order_archive (order_id, archive_date) SELECT o.id, ? FROM sales_order o "
                + "WHERE o.status IN ('DELIVERED', 'CANCELLED') "
                + "AND NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = o.id) "
                + "AND COALESCE((SELECT MAX(h.change_date) FROM sales_order_status_change h WHERE h.order_id = o.id "
                + "AND h.status = o.status), o.order_date) < ?", archiveDate, cutoff);
    }

    /** The archived sales orders, of any status, oldest first. */
    public List<SalesOrder> findArchivedSalesOrders() {
        return querySalesOrders(SalesOrderQuery.archived());
    }

    /** The archived sales orders of the customer, of any status, oldest first. */
    public List<SalesOrder> findArchivedSalesOrdersOfCustomer(long customerId) {
        return querySalesOrders(SalesOrderQuery.archived().ofCustomer(customerId));
    }

    /** The archived sales orders of the customers of the region, of any status, oldest first. */
    public List<SalesOrder> findArchivedSalesOrdersInRegion(Region region) {
        return querySalesOrders(SalesOrderQuery.archived().inRegion(region));
    }

    /** The revenue of the archived, non-cancelled sales orders per year of the order date, oldest year first. */
    public List<ArchivedYearRevenue> findArchivedSalesOrderRevenuePerYear() {
        return queryList("SELECT YEAR(o.order_date) AS year_no, SUM(l.quantity * l.unit_price) AS revenue FROM sales_order o "
                        + "JOIN order_archive ar ON ar.order_id = o.id JOIN sales_order_line l ON l.order_id = o.id "
                        + "WHERE o.status <> 'CANCELLED' GROUP BY YEAR(o.order_date) ORDER BY year_no",
                rs -> new ArchivedYearRevenue(rs.getInt("year_no"), rs.getBigDecimal("revenue")));
    }

    /** The status history of the sales order, in the order the changes were recorded. */
    public List<OrderStatusChange> findSalesOrderStatusChanges(long orderId) {
        return queryList("SELECT order_id, status, change_date FROM sales_order_status_change WHERE order_id = ? ORDER BY id",
                rs -> new OrderStatusChange(rs.getLong("order_id"), OrderStatus.valueOf(rs.getString("status")),
                        rs.getObject("change_date", LocalDate.class)),
                orderId);
    }

    /**
     * Sum of quantity times unit price over the lines of the sales order with the number (whatever its status, archived
     * or not); zero if there is no such order or no lines.
     */
    public BigDecimal sumSalesOrderValue(String number) {
        return querySalesOrderValue(SalesOrderQuery.includingArchived().withNumber(number));
    }

    /** Total value of all sales orders of the customer that are not cancelled, archived ones included. */
    public BigDecimal sumNotCancelledSalesOrderValueOfCustomer(long customerId) {
        return querySalesOrderValue(SalesOrderQuery.notCancelled().ofCustomer(customerId));
    }

    /**
     * The order history entries (number, customer, date, status, line count, value) of every sales order matched by
     * the join and filter, archived and cancelled ones included, oldest first.
     */
    private List<OrderHistoryEntry> querySalesOrderHistory(String customerJoin, String filter, Object param) {
        return queryList("SELECT o.number, o.customer_id, o.order_date, o.status, COUNT(l.line_no) AS line_count, "
                        + "COALESCE(SUM(l.quantity * l.unit_price), 0) AS total FROM sales_order o "
                        + customerJoin + "LEFT JOIN sales_order_line l ON l.order_id = o.id WHERE " + filter + " "
                        + "GROUP BY o.id, o.number, o.customer_id, o.order_date, o.status ORDER BY o.order_date, o.id",
                rs -> new OrderHistoryEntry(rs.getString("number"), rs.getLong("customer_id"),
                        rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")),
                        rs.getInt("line_count"), rs.getBigDecimal("total")),
                param);
    }

    /** Every sales order of the customer, archived and cancelled ones included, oldest first. */
    public List<OrderHistoryEntry> findSalesOrderHistoryOfCustomer(long customerId) {
        return querySalesOrderHistory("", "o.customer_id = ?", customerId);
    }

    /** Every sales order of the customers of the region, archived and cancelled ones included, oldest first. */
    public List<OrderHistoryEntry> findSalesOrderHistoryOfRegion(Region region) {
        return querySalesOrderHistory("JOIN customer c ON c.id = o.customer_id ", "c.region = ?", region.name());
    }

    /** The value of the sales order o: quantity times unit price over its lines, zero without lines. */
    private static final String SALES_ORDER_VALUE =
            "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order_line l WHERE l.order_id = o.id)";

    /**
     * The sales orders of the customer placed between the two dates, inclusive, that have at least one return
     * (whatever the return date), oldest first. Each order is listed once, with its value, the value of its returned
     * quantities at the unit prices of the order (as for the returns per reason) and the returned share of the order
     * value in percent.
     */
    public List<OrderReturns> findSalesOrdersWithReturnsOfCustomer(long customerId, LocalDate from, LocalDate to) {
        return queryList("SELECT o.id, o.number, o.customer_id, o.order_date, o.status, "
                        + SALES_ORDER_VALUE + " AS order_value, "
                        + "(SELECT COALESCE(SUM(rl.quantity * (SELECT ol.unit_price FROM sales_order_line ol "
                        + "WHERE ol.order_id = o.id AND ol.product_id = rl.product_id ORDER BY ol.line_no FETCH FIRST 1 ROW ONLY)), 0) "
                        + "FROM sales_return r JOIN sales_return_line rl ON rl.return_id = r.id WHERE r.order_id = o.id) AS returned_value "
                        + "FROM sales_order o WHERE o.customer_id = ? AND o.order_date >= ? AND o.order_date <= ? "
                        + "AND EXISTS (SELECT 1 FROM sales_return r WHERE r.order_id = o.id) ORDER BY o.order_date, o.id",
                rs -> {
                    SalesOrder order = salesOrderRow(rs);
                    BigDecimal orderValue = rs.getBigDecimal("order_value");
                    BigDecimal returnedValue = rs.getBigDecimal("returned_value");
                    BigDecimal returnedPercent = orderValue.signum() == 0 ? new BigDecimal("0.00")
                            : returnedValue.multiply(new BigDecimal("100")).divide(orderValue, 2, RoundingMode.HALF_UP);
                    return new OrderReturns(order, orderValue, returnedValue, returnedPercent);
                },
                customerId, from, to);
    }

    /**
     * The non-cancelled sales orders placed between the two dates, inclusive, whose value compares to the given value
     * as asked (">" or "<"), with order_value and the customer columns, in the given order. Parameters: from, to, value.
     */
    private static String nonCancelledSalesOrdersByValueSql(String comparison, String orderBy) {
        return "SELECT " + SALES_ORDER_AND_CUSTOMER_COLUMNS + ", " + SALES_ORDER_VALUE + " AS order_value "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "AND " + SALES_ORDER_VALUE + " " + comparison + " ? "
                + "ORDER BY " + orderBy;
    }

    /**
     * The non-cancelled sales orders placed between the two dates, inclusive, whose value (quantity times unit price of
     * the lines) is above the given value, with the customer who placed each. Grouped per region of the customer
     * (regions in alphabetical order), biggest order first within a region.
     */
    public List<LargeOrder> findLargeSalesOrders(BigDecimal minValue, LocalDate from, LocalDate to) {
        return queryList(nonCancelledSalesOrdersByValueSql(">", "c.region, order_value DESC, o.order_date, o.id"),
                rs -> new LargeOrder(salesOrderRow(rs), rs.getBigDecimal("order_value"), salesOrderCustomerRow(rs)),
                from, to, minValue);
    }

    /**
     * The non-cancelled sales orders placed between the two dates, inclusive, whose value (quantity times unit price of
     * the lines) is below the given value, with the customer who placed each. Grouped per customer (customers by name,
     * then id), oldest order first within a customer.
     */
    public List<SmallOrder> findSmallSalesOrders(BigDecimal maxValue, LocalDate from, LocalDate to) {
        return queryList(nonCancelledSalesOrdersByValueSql("<", "c.name, c.id, o.order_date, o.id"),
                rs -> new SmallOrder(salesOrderRow(rs), rs.getBigDecimal("order_value"), salesOrderCustomerRow(rs)),
                from, to, maxValue);
    }

    /**
     * Per region of the customer, the number of non-cancelled sales orders placed between the two dates, inclusive,
     * whose value (quantity times unit price of the lines) is below the given value, the number of all such orders of
     * the region and the small share in percent, rounded to two decimals. Sorted by region; regions without orders in
     * the period are left out.
     */
    public List<RegionSmallOrderShare> findSmallSalesOrderSharePerRegion(BigDecimal maxValue, LocalDate from, LocalDate to) {
        return queryList("SELECT region, COUNT(*) AS total_count, "
                        + "COALESCE(SUM(CASE WHEN order_value < ? THEN 1 ELSE 0 END), 0) AS small_count FROM ("
                        + "SELECT c.region AS region, " + SALES_ORDER_VALUE + " AS order_value "
                        + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                        + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') "
                        + "GROUP BY region ORDER BY region",
                rs -> {
                    int totalCount = rs.getInt("total_count");
                    int smallCount = rs.getInt("small_count");
                    BigDecimal share = BigDecimal.valueOf(smallCount * 100L)
                            .divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);
                    return new RegionSmallOrderShare(Region.valueOf(rs.getString("region")), smallCount, totalCount, share);
                },
                maxValue, from, to);
    }

    /** The N latest sales orders (see {@link SalesOrderQuery#latest(int)}: cancelled ones left out), newest first. */
    public List<SalesOrder> findLatestSalesOrders(int limit) {
        return querySalesOrders(SalesOrderQuery.latest(limit));
    }

    /** The N latest sales orders of customers in the region (cancelled ones left out), newest first, then by highest id. */
    public List<SalesOrder> findLatestSalesOrdersInRegion(Region region, int limit) {
        return querySalesOrders(SalesOrderQuery.latest(limit).inRegion(region));
    }

    /** The N latest sales orders of the customer (cancelled ones left out), newest first, then by highest id. */
    public List<SalesOrder> findLatestSalesOrdersOfCustomer(long customerId, int limit) {
        return querySalesOrders(SalesOrderQuery.latest(limit).ofCustomer(customerId));
    }

    /**
     * The sales orders of the customers of the country placed between the two dates, inclusive (cancelled orders are
     * left out, archived ones included), oldest first, then by id.
     */
    public List<SalesOrder> findNotCancelledSalesOrdersInCountry(String country, LocalDate from, LocalDate to) {
        return querySalesOrders(SalesOrderQuery.notCancelled().inCountry(country).placedBetween(from, to));
    }

    /**
     * The sales order lines matching all the given criteria, newest order first (then by order number and line number).
     * Every criterion is optional: a null product id, category, customer id, from date, to date or minimum quantity is
     * not applied. The category also matches the categories below it; the dates are inclusive and apply to the order date.
     */
    public List<OrderLineMatch> findSalesOrderLinesMatching(Long productId, String category, Long customerId, LocalDate from,
                                                            LocalDate to, Integer minQuantity) {
        return queryList("WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = CAST(? AS VARCHAR) "
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
                        + "ORDER BY o.order_date DESC, o.number DESC, o.id DESC, l.line_no",
                rs -> new OrderLineMatch(rs.getLong("order_id"), rs.getString("number"),
                        rs.getObject("order_date", LocalDate.class), rs.getLong("customer_id"),
                        rs.getString("customer_name"), rs.getInt("line_no"),
                        new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("product_name"),
                                rs.getString("category"), rs.getBigDecimal("product_price"),
                                rs.getString("unit_of_measure"), rs.getBoolean("active")),
                        rs.getInt("quantity"), rs.getBigDecimal("unit_price"), rs.getString("promotion_code")),
                category, productId, productId, category, category, customerId, customerId, from, from, to, to,
                minQuantity, minQuantity);
    }

    /** Sets (or replaces) the manual discount percentage of the sales order line. */
    public void setSalesOrderLineDiscount(long orderId, int lineNo, BigDecimal percent) {
        update("MERGE INTO order_line_discount (order_id, line_no, discount_percent) KEY (order_id, line_no) VALUES (?, ?, ?)",
                orderId, lineNo, percent);
    }

    /** True when a line of the sales order has a manual discount above the given percentage. */
    public boolean hasSalesOrderLineDiscountAbove(long orderId, BigDecimal percent) {
        return querySingle("SELECT COUNT(*) FROM order_line_discount WHERE order_id = ? AND discount_percent > ?",
                rs -> rs.getLong(1) > 0, orderId, percent);
    }

    /** The sales orders awaiting a discount approval (see {@link SalesOrderQuery#awaitingApproval}), oldest first. */
    public List<SalesOrder> findSalesOrdersAwaitingApproval(BigDecimal percent) {
        return querySalesOrders(SalesOrderQuery.awaitingApproval(percent));
    }

    /** Number of sales orders awaiting a discount approval that were placed on or before the date. */
    public int countSalesOrdersAwaitingApprovalOnDate(BigDecimal percent, LocalDate date) {
        return querySalesOrderCount(SalesOrderQuery.awaitingApproval(percent).placedOnOrBefore(date));
    }

    /** Records the approval decision on the discounts of the sales order (fails if there already is one); returns it. */
    public OrderApproval createSalesOrderApproval(OrderApproval approval) {
        update("INSERT INTO order_approval (order_id, approved, approver, decision_date) VALUES (?, ?, ?, ?)",
                approval.orderId(), approval.approved(), approval.approver(), approval.date());
        return approval;
    }

    /** The approval decision on the discounts of the sales order; empty if none was made. */
    public Optional<OrderApproval> findSalesOrderApproval(long orderId) {
        return queryOptional("SELECT order_id, approved, approver, decision_date FROM order_approval WHERE order_id = ?",
                rs -> new OrderApproval(rs.getLong("order_id"), rs.getBoolean("approved"),
                        rs.getString("approver"), rs.getObject("decision_date", LocalDate.class)),
                orderId);
    }

    /** Removes the approval decision of the sales order, if any. */
    public void deleteSalesOrderApproval(long orderId) {
        update("DELETE FROM order_approval WHERE order_id = ?", orderId);
    }

    // ===== Order notes =====

    public OrderNote createOrderNote(OrderNote note) {
        long id = insert("INSERT INTO order_note (order_id, author, note_date, text) VALUES (?, ?, ?, ?)",
                note.orderId(), note.author(), note.date(), note.text());
        return new OrderNote(id, note.orderId(), note.author(), note.date(), note.text());
    }

    /** The notes of the sales order, newest first, then by highest id. */
    public List<OrderNote> findOrderNotesOfSalesOrder(long orderId) {
        return findOrderNotesNewestFirst("FROM order_note n WHERE n.order_id = ?", orderId);
    }

    /**
     * The order notes of the FROM/WHERE clause (alias {@code n} for order_note), newest first, then by highest id. The
     * single ordering rule of all order note lists.
     */
    private List<OrderNote> findOrderNotesNewestFirst(String fromAndWhere, Object... params) {
        return queryList("SELECT n.id, n.order_id, n.author, n.note_date, n.text " + fromAndWhere
                + " ORDER BY n.note_date DESC, n.id DESC", DataStore::orderNoteRow, params);
    }

    /** Columns: id, order_id, author, note_date, text. */
    private static OrderNote orderNoteRow(ResultSet rs) throws SQLException {
        return new OrderNote(rs.getLong("id"), rs.getLong("order_id"), rs.getString("author"),
                rs.getObject("note_date", LocalDate.class), rs.getString("text"));
    }

    /** The notes the author wrote between the two dates, inclusive, newest first, then by highest id. */
    public List<OrderNote> findOrderNotesByAuthorWrittenBetween(String author, LocalDate from, LocalDate to) {
        return findOrderNotesNewestFirst("FROM order_note n WHERE n.author = ? AND n.note_date >= ? AND n.note_date <= ?",
                author, from, to);
    }

    /** The notes on the sales orders of the customer, newest first, then by highest id. */
    public List<OrderNote> findOrderNotesOfCustomer(long customerId) {
        return findOrderNotesNewestFirst("FROM order_note n JOIN sales_order o ON o.id = n.order_id WHERE o.customer_id = ?",
                customerId);
    }

    /** The notes the author wrote on the sales orders of the customer, newest first, then by highest id. */
    public List<OrderNote> findOrderNotesByAuthorOfCustomer(String author, long customerId) {
        return findOrderNotesNewestFirst("FROM order_note n JOIN sales_order o ON o.id = n.order_id "
                + "WHERE n.author = ? AND o.customer_id = ?", author, customerId);
    }

    // ===== Back orders =====

    public BackOrder createBackOrder(long orderId, LocalDate date) {
        long id = insert("INSERT INTO back_order (order_id, back_order_date) VALUES (?, ?)", orderId, date);
        return querySingle("SELECT number, customer_id FROM sales_order WHERE id = ?",
                rs -> new BackOrder(id, orderId, rs.getString("number"), rs.getLong("customer_id"), date), orderId);
    }

    /** The back order of the sales order, whatever the order's status. */
    public Optional<BackOrder> findBackOrderOfSalesOrder(long orderId) {
        return queryOptional("SELECT b.id, b.order_id, o.number, o.customer_id, b.back_order_date FROM back_order b "
                + "JOIN sales_order o ON o.id = b.order_id WHERE b.order_id = ?", DataStore::backOrderRow, orderId);
    }

    /** Columns: id, order_id, number (of the order), customer_id, back_order_date. */
    private static BackOrder backOrderRow(ResultSet rs) throws SQLException {
        return new BackOrder(rs.getLong("id"), rs.getLong("order_id"), rs.getString("number"),
                rs.getLong("customer_id"), rs.getObject("back_order_date", LocalDate.class));
    }

    /** The waiting back orders (order still confirmed), oldest first, then by id. */
    public List<BackOrder> findWaitingBackOrders() {
        return findWaitingBackOrdersWhere("");
    }

    /**
     * The waiting back orders (their sales order {@code o} is still CONFIRMED) that the extra condition keeps, oldest
     * first, then by id. The single rule of "waiting" for all back order lists.
     */
    private List<BackOrder> findWaitingBackOrdersWhere(String extraCondition, Object... params) {
        return queryList("SELECT b.id, b.order_id, o.number, o.customer_id, b.back_order_date FROM back_order b "
                + "JOIN sales_order o ON o.id = b.order_id WHERE o.status = 'CONFIRMED'" + extraCondition + " "
                + "ORDER BY b.back_order_date, b.id", DataStore::backOrderRow, params);
    }

    /** Number of back orders registered on or before the date whose order is still confirmed. */
    public int countWaitingBackOrdersRegisteredOnOrBefore(LocalDate date) {
        return querySingle("SELECT COUNT(*) AS back_order_count FROM back_order b "
                        + "JOIN sales_order o ON o.id = b.order_id WHERE o.status = 'CONFIRMED' AND b.back_order_date <= ?",
                rs -> rs.getInt("back_order_count"), date);
    }

    /** The waiting back orders of the customer, oldest first, then by id. */
    public List<BackOrder> findWaitingBackOrdersOfCustomer(long customerId) {
        return findWaitingBackOrdersWhere(" AND o.customer_id = ?", customerId);
    }

    /** The waiting back orders with an order line of the product, oldest first, then by id. */
    public List<BackOrder> findWaitingBackOrdersWithProduct(long productId) {
        return findWaitingBackOrdersWhere(" AND EXISTS "
                + "(SELECT 1 FROM sales_order_line l WHERE l.order_id = o.id AND l.product_id = ?)", productId);
    }

    /**
     * Per product category, the number of waiting back orders (order still confirmed) with a line in the category and the
     * units ordered on those lines. Sorted by category; categories without back orders are left out.
     */
    public List<CategoryBackOrders> findWaitingBackOrderCountAndUnitsPerCategory() {
        return queryList("SELECT p.category AS category, COUNT(DISTINCT b.id) AS back_order_count, SUM(l.quantity) AS units "
                        + "FROM back_order b JOIN sales_order o ON o.id = b.order_id "
                        + "JOIN sales_order_line l ON l.order_id = o.id JOIN product p ON p.id = l.product_id "
                        + "WHERE o.status = 'CONFIRMED' GROUP BY p.category ORDER BY p.category",
                rs -> new CategoryBackOrders(rs.getString("category"), rs.getInt("back_order_count"), rs.getInt("units")));
    }

    // ===== Invoices =====

    public Invoice createInvoice(Invoice invoice) {
        long id = insert("INSERT INTO invoice (number, order_id, customer_id, invoice_date, due_date, amount, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                invoice.number(), invoice.orderId(), invoice.customerId(), invoice.invoiceDate(), invoice.dueDate(),
                invoice.amount(), invoice.status().name());
        return new Invoice(id, invoice.number(), invoice.orderId(), invoice.customerId(),
                invoice.invoiceDate(), invoice.dueDate(), invoice.amount(), invoice.status());
    }

    public Optional<Invoice> findInvoiceByNumber(String number) {
        return queryOptional("SELECT id, number, order_id, customer_id, invoice_date, due_date, amount, status FROM invoice "
                + "WHERE number = ?", DataStore::invoiceRow, number);
    }

    /** The invoice of the order that is not cancelled, if any. */
    public Optional<Invoice> findActiveInvoiceByOrder(long orderId) {
        return queryOptional("SELECT id, number, order_id, customer_id, invoice_date, due_date, amount, status FROM invoice "
                + "WHERE order_id = ? AND status <> 'CANCELLED'", DataStore::invoiceRow, orderId);
    }

    /** The last payment date of invoice {@code i} (NULL if it has no payments). */
    private static final String LAST_PAYMENT_DATE_OF_INVOICE = "(SELECT MAX(p.payment_date) FROM payment p WHERE p.invoice_id = i.id)";

    /**
     * Which invoices {@link #findInvoices} returns and in which order. Start with a status factory, then chain filters;
     * every filter narrows the selection. Without an ordering method the invoices come oldest due date first (then by
     * id). Filters on the customer's region, sales rep or country join the customer.
     */
    private static final class InvoiceQuery {
        private final List<String> conditions = new ArrayList<>();
        private final List<Object> params = new ArrayList<>();
        private boolean joinsCustomer;
        private String orderBy = "i.due_date, i.id";
        private Integer limit;

        private InvoiceQuery(String statusCondition) {
            if (statusCondition != null) {
                conditions.add(statusCondition);
            }
        }

        private InvoiceQuery where(String condition, Object param) {
            conditions.add(condition);
            params.add(param);
            return this;
        }

        private InvoiceQuery whereCustomer(String condition, Object param) {
            joinsCustomer = true;
            return where(condition, param);
        }

        /** Invoices of every status, cancelled ones included. */
        public static InvoiceQuery all() {
            return new InvoiceQuery(null);
        }

        /** Invoices that are not cancelled. */
        public static InvoiceQuery notCancelled() {
            return new InvoiceQuery("i.status <> 'CANCELLED'");
        }

        /** Open invoices. */
        public static InvoiceQuery open() {
            return new InvoiceQuery("i.status = 'OPEN'");
        }

        /** Overdue invoices: open invoices whose due date is before the date. */
        public static InvoiceQuery overdue(LocalDate asOf) {
            return open().where("i.due_date < ?", asOf);
        }

        /** Paid invoices. */
        public static InvoiceQuery paid() {
            return new InvoiceQuery("i.status = 'PAID'");
        }

        /** Open and paid invoices. */
        public static InvoiceQuery openOrPaid() {
            return new InvoiceQuery("i.status IN ('OPEN', 'PAID')");
        }

        /** Only invoices with the status; null means no restriction. */
        public InvoiceQuery withStatus(InvoiceStatus status) {
            return status == null ? this : where("i.status = ?", status);
        }

        /** Only invoices of the customer; null means no restriction. */
        public InvoiceQuery ofCustomer(Long customerId) {
            return customerId == null ? this : where("i.customer_id = ?", customerId);
        }

        /** Only invoices of customers in the region. */
        public InvoiceQuery inRegion(Region region) {
            return whereCustomer("c.region = ?", region.name());
        }

        /** Only invoices of the customers of the sales rep. */
        public InvoiceQuery ofSalesRep(long salesRepId) {
            return whereCustomer("c.sales_rep_id = ?", salesRepId);
        }

        /** Only invoices of customers in the country. */
        public InvoiceQuery inCountry(String country) {
            return whereCustomer("c.country = ?", country);
        }

        /** Only invoices dated between the two dates, inclusive. */
        public InvoiceQuery datedBetween(LocalDate from, LocalDate to) {
            return where("i.invoice_date >= ?", from).where("i.invoice_date <= ?", to);
        }

        /** Only invoices due on or after the date; null means no restriction. */
        public InvoiceQuery dueOnOrAfter(LocalDate dueFrom) {
            return dueFrom == null ? this : where("i.due_date >= ?", dueFrom);
        }

        /** Only invoices due on or before the date; null means no restriction. */
        public InvoiceQuery dueOnOrBefore(LocalDate dueTo) {
            return dueTo == null ? this : where("i.due_date <= ?", dueTo);
        }

        /** Only invoices with an amount of at least the given amount (inclusive); null means no restriction. */
        public InvoiceQuery amountAtLeast(BigDecimal minAmount) {
            return minAmount == null ? this : where("i.amount >= ?", minAmount);
        }

        /** Only invoices whose last payment was made between the two dates, inclusive. */
        public InvoiceQuery lastPaidBetween(LocalDate from, LocalDate to) {
            return where(LAST_PAYMENT_DATE_OF_INVOICE + " >= ?", from).where(LAST_PAYMENT_DATE_OF_INVOICE + " <= ?", to);
        }

        /** Orders oldest invoice date first (then by id). */
        public InvoiceQuery oldestInvoiceFirst() {
            orderBy = "i.invoice_date, i.id";
            return this;
        }

        /** Orders newest invoice date first (then by highest id). */
        public InvoiceQuery newestFirst() {
            orderBy = "i.invoice_date DESC, i.id DESC";
            return this;
        }

        /** The N most recent invoices: newest invoice date first (then by highest id), at most the limit. */
        public InvoiceQuery latest(int limit) {
            this.limit = limit;
            return newestFirst();
        }

        private String sql() {
            return "SELECT i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                    + (joinsCustomer ? "JOIN customer c ON c.id = i.customer_id " : "")
                    + (conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", conditions) + " ")
                    + "ORDER BY " + orderBy + (limit == null ? "" : " LIMIT ?");
        }

        private Object[] params() {
            List<Object> all = new ArrayList<>(params);
            if (limit != null) {
                all.add(limit);
            }
            return all.toArray();
        }
    }

    /** The invoices selected by the query, in the query's order. All public invoice lists below delegate here. */
    private List<Invoice> findInvoices(InvoiceQuery query) {
        return queryList(query.sql(), DataStore::invoiceRow, query.params());
    }

    /** All invoices of the customer, cancelled ones included, newest invoice date first (then by highest id). */
    public List<Invoice> findAllInvoicesOfCustomerNewestFirst(long customerId) {
        return findInvoices(InvoiceQuery.all().ofCustomer(customerId).newestFirst());
    }

    /** The invoices of the customer dated between the two dates, inclusive, oldest first (then by id), without cancelled ones. */
    public List<Invoice> findInvoicesOfCustomerDatedBetween(long customerId, LocalDate from, LocalDate to) {
        return findInvoices(InvoiceQuery.notCancelled().ofCustomer(customerId).datedBetween(from, to).oldestInvoiceFirst());
    }

    /**
     * The invoices of the customers of the country issued between the two dates, inclusive (cancelled invoices are
     * left out), oldest first, then by id.
     */
    public List<Invoice> findInvoicesInCountryDatedBetween(String country, LocalDate from, LocalDate to) {
        return findInvoices(InvoiceQuery.notCancelled().inCountry(country).datedBetween(from, to).oldestInvoiceFirst());
    }

    /**
     * Invoices matching all the given criteria, oldest due date first. Every criterion is optional: null means no
     * restriction. The due dates are inclusive and the minimum amount is inclusive.
     */
    public List<Invoice> findInvoicesByCriteria(Long customerId, InvoiceStatus status, LocalDate dueFrom, LocalDate dueTo, BigDecimal minAmount) {
        return findInvoices(InvoiceQuery.all().ofCustomer(customerId).withStatus(status)
                .dueOnOrAfter(dueFrom).dueOnOrBefore(dueTo).amountAtLeast(minAmount));
    }

    /** Overdue invoices (open, due date before the given date), oldest due date first (then by id). */
    public List<Invoice> findOverdueInvoices(LocalDate asOf) {
        return findInvoices(InvoiceQuery.overdue(asOf));
    }

    /** Overdue invoices (open, due date before the given date) of the customer, oldest due date first (then by id). */
    public List<Invoice> findOverdueInvoicesOfCustomer(LocalDate asOf, long customerId) {
        return findInvoices(InvoiceQuery.overdue(asOf).ofCustomer(customerId));
    }

    /** Overdue invoices (open, due date before the given date) of customers in the region, oldest due date first (then by id). */
    public List<Invoice> findOverdueInvoicesInRegion(LocalDate asOf, Region region) {
        return findInvoices(InvoiceQuery.overdue(asOf).inRegion(region));
    }

    /** Open invoices of the customers of the sales rep, oldest due date first (then by id). */
    public List<Invoice> findOpenInvoicesOfSalesRep(long salesRepId) {
        return findInvoices(InvoiceQuery.open().ofSalesRep(salesRepId));
    }

    /** Overdue invoices (open, due date before the given date) of the customers of the sales rep, oldest due date first (then by id). */
    public List<Invoice> findOverdueInvoicesOfSalesRep(LocalDate asOf, long salesRepId) {
        return findInvoices(InvoiceQuery.overdue(asOf).ofSalesRep(salesRepId));
    }

    /** Paid invoices of the customers of the sales rep whose last payment was made between the two dates, inclusive, oldest invoice first (then by id). */
    public List<Invoice> findInvoicesOfSalesRepPaidBetween(long salesRepId, LocalDate from, LocalDate to) {
        return findInvoices(InvoiceQuery.paid().ofSalesRep(salesRepId).lastPaidBetween(from, to).oldestInvoiceFirst());
    }

    /** Open and paid invoices of customers in the region dated between the two dates, inclusive, oldest invoice first (then by id). */
    public List<Invoice> findOpenAndPaidInvoicesInRegionDatedBetween(Region region, LocalDate from, LocalDate to) {
        return findInvoices(InvoiceQuery.openOrPaid().inRegion(region).datedBetween(from, to).oldestInvoiceFirst());
    }

    /** The N most recent invoices (cancelled invoices are left out), newest invoice date first, then by highest id. */
    public List<Invoice> findLatestInvoices(int limit) {
        return findInvoices(InvoiceQuery.notCancelled().latest(limit));
    }

    /**
     * The N most recent invoices of customers in the region (cancelled invoices are left out), newest invoice date
     * first, then by highest id.
     */
    public List<Invoice> findLatestInvoicesInRegion(Region region, int limit) {
        return findInvoices(InvoiceQuery.notCancelled().inRegion(region).latest(limit));
    }

    /**
     * The N most recent invoices of the customer (cancelled invoices are left out), newest invoice date first, then by
     * highest id.
     */
    public List<Invoice> findLatestInvoicesOfCustomer(long customerId, int limit) {
        return findInvoices(InvoiceQuery.notCancelled().ofCustomer(customerId).latest(limit));
    }

    /** The sum of the invoices of the customer dated before the date, without cancelled ones, zero if there are none. */
    public BigDecimal sumInvoicesOfCustomerDatedBefore(long customerId, LocalDate before) {
        return querySingle("SELECT COALESCE(SUM(amount), 0) AS total FROM invoice "
                + "WHERE customer_id = ? AND invoice_date < ? AND status <> 'CANCELLED'", rs -> rs.getBigDecimal("total"),
                customerId, before);
    }

    /** Sum of the amounts of the open invoices issued on or before the date whose due date is before it. */
    public BigDecimal sumOverdueInvoiceAmount(LocalDate asOf) {
        return querySingle("SELECT COALESCE(SUM(amount), 0) AS total FROM invoice "
                + "WHERE status = 'OPEN' AND invoice_date <= ? AND due_date < ?", rs -> rs.getBigDecimal("total"), asOf, asOf);
    }

    /**
     * Per sales rep with overdue invoices, the number and total amount of the open invoices of the rep's customers whose
     * due date is before the given date, biggest amount first (then by rep name, then id).
     */
    public List<RepOverdueInvoices> findOverdueInvoiceTotalsPerSalesRep(LocalDate asOf) {
        return queryList("SELECT r.id AS id, r.name AS name, COUNT(*) AS invoice_count, SUM(i.amount) AS total FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE i.status = 'OPEN' AND i.due_date < ? "
                + "GROUP BY r.id, r.name ORDER BY total DESC, r.name, r.id",
                rs -> new RepOverdueInvoices(rs.getLong("id"), rs.getString("name"),
                        rs.getInt("invoice_count"), rs.getBigDecimal("total")), asOf);
    }

    /** Number and total amount of the open and paid invoices of customers in the region dated between the two dates, inclusive, per status; statuses without invoices are left out. */
    public List<InvoiceStatusTotal> findOpenAndPaidInvoiceTotalsPerStatus(Region region, LocalDate from, LocalDate to) {
        return queryList("SELECT i.status AS status, COUNT(*) AS invoice_count, SUM(i.amount) AS total FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status IN ('OPEN', 'PAID') AND c.region = ? AND i.invoice_date >= ? AND i.invoice_date <= ? "
                + "GROUP BY i.status ORDER BY i.status",
                rs -> new InvoiceStatusTotal(InvoiceStatus.valueOf(rs.getString("status")),
                        rs.getInt("invoice_count"), rs.getBigDecimal("total")), region.name(), from, to);
    }

    /** Open and paid invoices dated between the two dates, inclusive, with an amount above (exclusive) the given amount, with the region of their customer: by region, biggest invoice first (then by id). */
    public List<RegionLargeInvoice> findLargeInvoicesSortedByRegion(BigDecimal minAmount, LocalDate from, LocalDate to) {
        return queryList("SELECT c.region AS region, i.id, i.number, i.order_id, i.customer_id, i.invoice_date, i.due_date, i.amount, i.status FROM invoice i "
                + "JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status IN ('OPEN', 'PAID') AND i.amount > ? AND i.invoice_date >= ? AND i.invoice_date <= ? "
                + "ORDER BY c.region, i.amount DESC, i.id",
                rs -> new RegionLargeInvoice(Region.valueOf(rs.getString("region")), invoiceRow(rs)), minAmount, from, to);
    }

    /** Paid invoices whose last payment was made after the due date and between the two dates, inclusive, by customer name (then customer id), then oldest due date first (then by invoice id). */
    public List<LatePaidInvoice> findLatePaidInvoicesByPeriod(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id AS customer_id, c.name AS customer_name, i.number AS invoice_number, i.due_date AS due_date, "
                + LAST_PAYMENT_DATE_OF_INVOICE + " AS paid_date, "
                + "DATEDIFF('DAY', i.due_date, " + LAST_PAYMENT_DATE_OF_INVOICE + ") AS days_late "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'PAID' "
                + "AND " + LAST_PAYMENT_DATE_OF_INVOICE + " > i.due_date "
                + "AND " + LAST_PAYMENT_DATE_OF_INVOICE + " >= ? "
                + "AND " + LAST_PAYMENT_DATE_OF_INVOICE + " <= ? "
                + "ORDER BY c.name, c.id, i.due_date, i.id",
                rs -> new LatePaidInvoice(rs.getLong("customer_id"), rs.getString("customer_name"),
                        rs.getString("invoice_number"), rs.getObject("due_date", LocalDate.class),
                        rs.getObject("paid_date", LocalDate.class), rs.getInt("days_late")), from, to);
    }

    /** Open invoices with at least one payment, with the amount paid and still due, by customer region, customer name (then customer id), then oldest due date first (then by invoice id). */
    public List<PartiallyPaidInvoice> findPartiallyPaidInvoices() {
        return queryList("SELECT c.id AS customer_id, c.name AS customer_name, c.region AS region, i.number AS invoice_number, "
                + "i.due_date AS due_date, i.amount AS amount, "
                + "(SELECT SUM(p.amount) FROM payment p WHERE p.invoice_id = i.id) AS paid_amount "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "WHERE i.status = 'OPEN' "
                + "AND EXISTS (SELECT 1 FROM payment p WHERE p.invoice_id = i.id) "
                + "ORDER BY c.region, c.name, c.id, i.due_date, i.id", rs -> {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    BigDecimal paidAmount = rs.getBigDecimal("paid_amount");
                    return new PartiallyPaidInvoice(rs.getLong("customer_id"), rs.getString("customer_name"),
                            Region.valueOf(rs.getString("region")), rs.getString("invoice_number"),
                            rs.getObject("due_date", LocalDate.class), amount, paidAmount, amount.subtract(paidAmount));
                });
    }

    /** Open invoices with the currency they were issued in (EUR when none is recorded) and their amount in it and in EUR, by currency, customer name (then customer id), then oldest due date first (then by invoice id). */
    public List<CurrencyOpenInvoice> findOpenInvoicesWithCurrency() {
        return queryList("SELECT c.id AS customer_id, c.name AS customer_name, COALESCE(ic.currency, 'EUR') AS currency, "
                + "i.number AS invoice_number, i.due_date AS due_date, COALESCE(ic.amount, i.amount) AS currency_amount, "
                + "i.amount AS eur_amount "
                + "FROM invoice i JOIN customer c ON c.id = i.customer_id "
                + "LEFT JOIN invoice_currency ic ON ic.invoice_id = i.id "
                + "WHERE i.status = 'OPEN' "
                + "ORDER BY COALESCE(ic.currency, 'EUR'), c.name, c.id, i.due_date, i.id",
                rs -> new CurrencyOpenInvoice(rs.getLong("customer_id"), rs.getString("customer_name"),
                        Currency.valueOf(rs.getString("currency")), rs.getString("invoice_number"),
                        rs.getObject("due_date", LocalDate.class), rs.getBigDecimal("currency_amount"),
                        rs.getBigDecimal("eur_amount")));
    }

    public int countInvoices() {
        return querySingle("SELECT COUNT(*) AS invoice_count FROM invoice", rs -> rs.getInt("invoice_count"));
    }

    /** The unpaid part of all open invoices of the customer (invoice amounts minus their payments), zero if there are none. */
    public BigDecimal sumOpenInvoiceBalanceOfCustomer(long customerId) {
        return querySingle("SELECT COALESCE(SUM(i.amount), 0) - COALESCE(SUM((SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id)), 0) AS balance FROM invoice i WHERE i.customer_id = ? AND i.status = 'OPEN'",
                rs -> rs.getBigDecimal("balance"), customerId);
    }

    /**
     * The unpaid part of the open invoices per customer in aging buckets on the given date: not due (due date on or
     * after the date), 1-30, 31-60, 61-90 and more than 90 days overdue. Only invoices dated and payments made up to
     * the date count. Customers without such invoices are left out, ordered by customer name.
     */
    public List<InvoiceAging> findInvoiceAging(LocalDate asOf) {
        return queryList("SELECT c.id AS id, c.name AS name, "
                + "COALESCE(SUM(CASE WHEN x.days <= 0 THEN x.balance END), 0) AS not_due, "
                + "COALESCE(SUM(CASE WHEN x.days BETWEEN 1 AND 30 THEN x.balance END), 0) AS days_1_30, "
                + "COALESCE(SUM(CASE WHEN x.days BETWEEN 31 AND 60 THEN x.balance END), 0) AS days_31_60, "
                + "COALESCE(SUM(CASE WHEN x.days BETWEEN 61 AND 90 THEN x.balance END), 0) AS days_61_90, "
                + "COALESCE(SUM(CASE WHEN x.days > 90 THEN x.balance END), 0) AS over_90 "
                + "FROM (SELECT i.customer_id AS customer_id, DATEDIFF('DAY', i.due_date, ?) AS days, "
                + "i.amount - (SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id AND p.payment_date <= ?) AS balance "
                + "FROM invoice i WHERE i.status = 'OPEN' AND i.invoice_date <= ?) x "
                + "JOIN customer c ON c.id = x.customer_id "
                + "GROUP BY c.id, c.name ORDER BY c.name, c.id",
                rs -> new InvoiceAging(rs.getLong("id"), rs.getString("name"), rs.getBigDecimal("not_due"),
                        rs.getBigDecimal("days_1_30"), rs.getBigDecimal("days_31_60"),
                        rs.getBigDecimal("days_61_90"), rs.getBigDecimal("over_90")), asOf, asOf, asOf);
    }

    public void updateInvoiceStatus(long invoiceId, InvoiceStatus status) {
        update("UPDATE invoice SET status = ? WHERE id = ?", status.name(), invoiceId);
    }

    public InvoiceCurrency createInvoiceCurrency(InvoiceCurrency invoiceCurrency) {
        update("INSERT INTO invoice_currency (invoice_id, currency, amount) VALUES (?, ?, ?)",
                invoiceCurrency.invoiceId(), invoiceCurrency.currency().name(), invoiceCurrency.amount());
        return invoiceCurrency;
    }

    /** The currency and amount an invoice was issued in, if recorded (an invoice without one is in EUR). */
    public Optional<InvoiceCurrency> findInvoiceCurrency(long invoiceId) {
        return queryOptional("SELECT invoice_id, currency, amount FROM invoice_currency WHERE invoice_id = ?",
                rs -> new InvoiceCurrency(rs.getLong("invoice_id"), Currency.valueOf(rs.getString("currency")),
                        rs.getBigDecimal("amount")), invoiceId);
    }

    // ===== Payments =====

    /**
     * Which received payments {@link #findPaymentsReceived} returns: always those received in a period (both dates
     * inclusive), narrowed by the chained filters.
     */
    private static final class PaymentReceivedQuery {
        private final List<String> conditions = new ArrayList<>();
        private final List<Object> params = new ArrayList<>();

        private PaymentReceivedQuery(LocalDate from, LocalDate to) {
            where("p.payment_date >= ?", from).where("p.payment_date <= ?", to);
        }

        private PaymentReceivedQuery where(String condition, Object param) {
            conditions.add(condition);
            params.add(param);
            return this;
        }

        /** The payments received between the two dates, inclusive. */
        public static PaymentReceivedQuery between(LocalDate from, LocalDate to) {
            return new PaymentReceivedQuery(from, to);
        }

        /** Only payments of the customer. */
        public PaymentReceivedQuery ofCustomer(long customerId) {
            return where("c.id = ?", customerId);
        }

        /** Only payments of customers in the region. */
        public PaymentReceivedQuery inRegion(Region region) {
            return where("c.region = ?", region.name());
        }

        /** Only payments of the customers of the sales rep. */
        public PaymentReceivedQuery ofSalesRep(long salesRepId) {
            return where("c.sales_rep_id = ?", salesRepId);
        }

        /** Leaves out the payments of cancelled invoices. */
        public PaymentReceivedQuery excludingCancelledInvoices() {
            conditions.add("i.status <> 'CANCELLED'");
            return this;
        }
    }

    /** The payments received in the period between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> findPaymentsReceivedBetween(LocalDate from, LocalDate to) {
        return findPaymentsReceived(PaymentReceivedQuery.between(from, to));
    }

    /** The payments of the customer received between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> findPaymentsReceivedFromCustomerBetween(long customerId, LocalDate from, LocalDate to) {
        return findPaymentsReceived(PaymentReceivedQuery.between(from, to).ofCustomer(customerId));
    }

    /** The payments of the customer received between the two dates, inclusive, oldest first (then by id), without those of cancelled invoices. */
    public List<PaymentReceived> findPaymentsReceivedFromCustomerBetweenExcludingCancelledInvoices(long customerId, LocalDate from, LocalDate to) {
        return findPaymentsReceived(PaymentReceivedQuery.between(from, to).ofCustomer(customerId).excludingCancelledInvoices());
    }

    /** The payments received from customers of the region between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> findPaymentsReceivedInRegionBetween(Region region, LocalDate from, LocalDate to) {
        return findPaymentsReceived(PaymentReceivedQuery.between(from, to).inRegion(region));
    }

    /** The payments of the customers of the sales rep received between the two dates, inclusive, oldest first (then by id). */
    public List<PaymentReceived> findPaymentsReceivedOfSalesRepBetween(long salesRepId, LocalDate from, LocalDate to) {
        return findPaymentsReceived(PaymentReceivedQuery.between(from, to).ofSalesRep(salesRepId));
    }

    /** The payments selected by the query, with their invoice number and customer, oldest first (then by id). */
    private List<PaymentReceived> findPaymentsReceived(PaymentReceivedQuery query) {
        return queryList("SELECT p.id AS id, i.number AS invoice_number, c.id AS customer_id, c.name AS customer_name, "
                + "p.payment_date AS payment_date, p.amount AS amount "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE " + String.join(" AND ", query.conditions) + " ORDER BY p.payment_date, p.id",
                rs -> new PaymentReceived(rs.getLong("id"), rs.getString("invoice_number"),
                        rs.getLong("customer_id"), rs.getString("customer_name"),
                        rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount")),
                query.params.toArray());
    }

    /**
     * Per sales rep with payments, the number and total amount of the payments of the rep's customers received between
     * the two dates, inclusive, biggest amount first (then by rep name, then id).
     */
    public List<RepPayments> findPaymentTotalsPerSalesRep(LocalDate from, LocalDate to) {
        return queryList("SELECT r.id AS id, r.name AS name, COUNT(*) AS payment_count, SUM(p.amount) AS total FROM payment p "
                + "JOIN invoice i ON i.id = p.invoice_id "
                + "JOIN customer c ON c.id = i.customer_id "
                + "JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE p.payment_date >= ? AND p.payment_date <= ? "
                + "GROUP BY r.id, r.name ORDER BY total DESC, r.name, r.id",
                rs -> new RepPayments(rs.getLong("id"), rs.getString("name"),
                        rs.getInt("payment_count"), rs.getBigDecimal("total")), from, to);
    }

    public Payment createPayment(Payment payment) {
        long id = insert("INSERT INTO payment (invoice_id, payment_date, amount) VALUES (?, ?, ?)",
                payment.invoiceId(), payment.paymentDate(), payment.amount());
        return new Payment(id, payment.invoiceId(), payment.paymentDate(), payment.amount());
    }

    /** Columns: id, invoice_id, payment_date, amount. */
    private static Payment paymentRow(ResultSet rs) throws SQLException {
        return new Payment(rs.getLong("id"), rs.getLong("invoice_id"),
                rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount"));
    }

    /** The payments of the invoice, oldest first (then by id). */
    public List<Payment> findPaymentsOfInvoice(long invoiceId) {
        return queryList("SELECT id, invoice_id, payment_date, amount FROM payment "
                + "WHERE invoice_id = ? ORDER BY payment_date, id", DataStore::paymentRow, invoiceId);
    }

    /** The sum of the payments of the invoice, zero if there are none. */
    public BigDecimal sumPaymentsOfInvoice(long invoiceId) {
        return querySingle("SELECT COALESCE(SUM(amount), 0) AS total FROM payment WHERE invoice_id = ?",
                rs -> rs.getBigDecimal("total"), invoiceId);
    }

    /** The sum of the payments of the customer made before the date, without those of cancelled invoices, zero if there are none. */
    public BigDecimal sumPaymentsOfCustomerMadeBefore(long customerId, LocalDate before) {
        return querySingle("SELECT COALESCE(SUM(p.amount), 0) AS total FROM payment p JOIN invoice i ON i.id = p.invoice_id "
                + "WHERE i.customer_id = ? AND p.payment_date < ? AND i.status <> 'CANCELLED'",
                rs -> rs.getBigDecimal("total"), customerId, before);
    }

    /**
     * Number and total of the payments received per day between the two dates, inclusive, oldest day first; days
     * without payments are left out.
     */
    public List<DailyPaymentTotal> findPaymentTotalsPerDay(LocalDate from, LocalDate to) {
        return queryList("SELECT payment_date, COUNT(*) AS payment_count, SUM(amount) AS total FROM payment "
                + "WHERE payment_date >= ? AND payment_date <= ? GROUP BY payment_date ORDER BY payment_date",
                rs -> new DailyPaymentTotal(rs.getObject("payment_date", LocalDate.class),
                        rs.getInt("payment_count"), rs.getBigDecimal("total")), from, to);
    }

    /**
     * Number and total of the payments received from customers of the region per calendar month between the two
     * dates, inclusive, oldest month first; months without payments are left out.
     */
    public List<MonthlyPaymentTotal> findPaymentTotalsPerMonthByRegion(Region region, LocalDate from, LocalDate to) {
        return queryList("SELECT YEAR(p.payment_date) AS year_no, MONTH(p.payment_date) AS month_no, "
                + "COUNT(*) AS payment_count, SUM(p.amount) AS total "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE p.payment_date >= ? AND p.payment_date <= ? AND c.region = ? "
                + "GROUP BY YEAR(p.payment_date), MONTH(p.payment_date) ORDER BY YEAR(p.payment_date), MONTH(p.payment_date)",
                rs -> new MonthlyPaymentTotal(rs.getInt("year_no"), rs.getInt("month_no"),
                        rs.getInt("payment_count"), rs.getBigDecimal("total")), from, to, region.name());
    }

    /**
     * Payments that exceed the amount due of their invoice (the invoice amount minus the earlier payments), with the
     * part that exceeds it, by customer name (then customer id), then oldest payment first (then by payment id).
     */
    public List<UnappliedPayment> findUnappliedPayments() {
        String paidUpToThisPayment = "(SELECT SUM(q.amount) FROM payment q WHERE q.invoice_id = p.invoice_id "
                + "AND (q.payment_date < p.payment_date OR (q.payment_date = p.payment_date AND q.id <= p.id)))";
        return queryList("SELECT c.id AS customer_id, c.name AS customer_name, p.id AS id, i.number AS invoice_number, "
                + "p.payment_date AS payment_date, p.amount AS amount, "
                + "LEAST(p.amount, " + paidUpToThisPayment + " - i.amount) AS unapplied_amount "
                + "FROM payment p JOIN invoice i ON i.id = p.invoice_id JOIN customer c ON c.id = i.customer_id "
                + "WHERE " + paidUpToThisPayment + " > i.amount "
                + "ORDER BY c.name, c.id, p.payment_date, p.id",
                rs -> new UnappliedPayment(rs.getLong("id"), rs.getString("invoice_number"),
                        rs.getLong("customer_id"), rs.getString("customer_name"),
                        rs.getObject("payment_date", LocalDate.class), rs.getBigDecimal("amount"),
                        rs.getBigDecimal("unapplied_amount")));
    }

    // ===== Credit notes, open credit and credit limits =====

    public CreditNote createCreditNote(CreditNote note) {
        long id = insert("INSERT INTO credit_note (number, customer_id, return_id, credit_date, amount) VALUES (?, ?, ?, ?, ?)",
                note.number(), note.customerId(), note.returnId(), note.creditDate(), note.amount());
        return new CreditNote(id, note.number(), note.customerId(), note.returnId(), note.creditDate(), note.amount());
    }

    /** Columns: id, number, customer_id, return_id, credit_date, amount. */
    private static CreditNote creditNoteRow(ResultSet rs) throws SQLException {
        return new CreditNote(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                rs.getLong("return_id"), rs.getObject("credit_date", LocalDate.class), rs.getBigDecimal("amount"));
    }

    /**
     * Which credit notes {@link #findCreditNotes} returns: start with {@link #all()} and chain filters; every filter
     * narrows the selection. Filtering on the region joins the customer.
     */
    private static final class CreditNoteQuery {
        private final List<String> conditions = new ArrayList<>();
        private final List<Object> params = new ArrayList<>();
        private boolean joinsCustomer;

        private CreditNoteQuery() {
        }

        private CreditNoteQuery where(String condition, Object param) {
            conditions.add(condition);
            params.add(param);
            return this;
        }

        /** All credit notes. */
        public static CreditNoteQuery all() {
            return new CreditNoteQuery();
        }

        /** Only credit notes of the customer. */
        public CreditNoteQuery ofCustomer(long customerId) {
            return where("n.customer_id = ?", customerId);
        }

        /** Only credit notes of customers in the region. */
        public CreditNoteQuery inRegion(Region region) {
            joinsCustomer = true;
            return where("c.region = ?", region.name());
        }

        /** Only credit notes dated between the two dates, inclusive. */
        public CreditNoteQuery datedBetween(LocalDate from, LocalDate to) {
            return where("n.credit_date >= ?", from).where("n.credit_date <= ?", to);
        }
    }

    /** The credit notes of the customer, oldest first (then by id). */
    public List<CreditNote> findCreditNotesOfCustomer(long customerId) {
        return findCreditNotes(CreditNoteQuery.all().ofCustomer(customerId));
    }

    /** The credit notes of the customer dated between the two dates, inclusive, oldest first (then by id). */
    public List<CreditNote> findCreditNotesOfCustomerDatedBetween(long customerId, LocalDate from, LocalDate to) {
        return findCreditNotes(CreditNoteQuery.all().ofCustomer(customerId).datedBetween(from, to));
    }

    /** The credit notes dated between the two dates, inclusive, oldest first (then by id). */
    public List<CreditNote> findCreditNotesDatedBetween(LocalDate from, LocalDate to) {
        return findCreditNotes(CreditNoteQuery.all().datedBetween(from, to));
    }

    /** The credit notes of the customers of the region dated between the two dates, inclusive, oldest first (then by id). */
    public List<CreditNote> findCreditNotesInRegionDatedBetween(Region region, LocalDate from, LocalDate to) {
        return findCreditNotes(CreditNoteQuery.all().inRegion(region).datedBetween(from, to));
    }

    /** The credit notes selected by the query, oldest first (then by id). */
    private List<CreditNote> findCreditNotes(CreditNoteQuery query) {
        return queryList("SELECT n.id AS id, n.number AS number, n.customer_id AS customer_id, n.return_id AS return_id, "
                + "n.credit_date AS credit_date, n.amount AS amount FROM credit_note n "
                + (query.joinsCustomer ? "JOIN customer c ON c.id = n.customer_id " : "")
                + (query.conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", query.conditions) + " ")
                + "ORDER BY n.credit_date, n.id", DataStore::creditNoteRow, query.params.toArray());
    }

    /** Records that the credit note has been used against the invoice. A credit note can be used only once. */
    public void createCreditNoteApplication(long creditNoteId, long invoiceId, LocalDate appliedDate) {
        update("INSERT INTO credit_note_application (credit_note_id, invoice_id, applied_date) VALUES (?, ?, ?)",
                creditNoteId, invoiceId, appliedDate);
    }

    /** The credit note with the number, if it exists. */
    public Optional<CreditNote> findCreditNoteByNumber(String number) {
        return queryOptional("SELECT id, number, customer_id, return_id, credit_date, amount FROM credit_note WHERE number = ?",
                DataStore::creditNoteRow, number);
    }

    /** Whether the credit note has already been used against an invoice. */
    public boolean isCreditNoteApplied(long creditNoteId) {
        return querySingle("SELECT COUNT(*) AS cnt FROM credit_note_application WHERE credit_note_id = ?",
                rs -> rs.getInt("cnt") > 0, creditNoteId);
    }

    /** Open credit: credit note {@code n} has not been used against an invoice. */
    private static final String CREDIT_NOTE_NOT_APPLIED =
            "NOT EXISTS (SELECT 1 FROM credit_note_application a WHERE a.credit_note_id = n.id) ";

    /**
     * The customers with credit notes not used against an invoice, with the number and total of those notes.
     * Sorted by region, then largest open amount first, then name and id.
     */
    public List<CustomerOpenCredit> findCustomersWithOpenCredit() {
        return queryList("SELECT c.id AS id, c.name AS name, c.region AS region, COUNT(n.id) AS note_count, "
                + "SUM(n.amount) AS open_amount FROM credit_note n JOIN customer c ON c.id = n.customer_id "
                + "WHERE " + CREDIT_NOTE_NOT_APPLIED
                + "GROUP BY c.id, c.name, c.region ORDER BY c.region, open_amount DESC, c.name, c.id",
                rs -> new CustomerOpenCredit(rs.getLong("id"), rs.getString("name"),
                        Region.valueOf(rs.getString("region")), rs.getInt("note_count"), rs.getBigDecimal("open_amount")));
    }

    /** The open (unused) credit per region: customers, notes and total amount. Largest amount first, then region. */
    public List<RegionOpenCredit> findOpenCreditPerRegion() {
        return queryList("SELECT c.region AS region, COUNT(DISTINCT c.id) AS customer_count, COUNT(n.id) AS note_count, "
                + "SUM(n.amount) AS open_amount FROM credit_note n JOIN customer c ON c.id = n.customer_id "
                + "WHERE " + CREDIT_NOTE_NOT_APPLIED
                + "GROUP BY c.region ORDER BY open_amount DESC, c.region",
                rs -> new RegionOpenCredit(Region.valueOf(rs.getString("region")), rs.getInt("customer_count"),
                        rs.getInt("note_count"), rs.getBigDecimal("open_amount")));
    }

    /**
     * Derived table {@code x(customer_id, balance)}: per customer, the unpaid part of the open invoices on a date. Only
     * invoices dated and payments made up to the date count. Takes two parameters, both the date.
     */
    private static final String OPEN_BALANCE_PER_CUSTOMER_AS_OF =
            "(SELECT i.customer_id AS customer_id, SUM(i.amount - (SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                    + "WHERE p.invoice_id = i.id AND p.payment_date <= ?)) AS balance "
                    + "FROM invoice i WHERE i.status = 'OPEN' AND i.invoice_date <= ? GROUP BY i.customer_id) x ";

    /** Customer {@code c} is over the credit limit: its balance in {@code x} exceeds it (customers without a limit never are). */
    private static final String OVER_CREDIT_LIMIT = "c.credit_limit IS NOT NULL AND x.balance > c.credit_limit ";

    /**
     * The customers whose open invoice amount on the date exceeds their credit limit (customers without a limit never do).
     * Only invoices dated and payments made up to the date count. Sorted by region, then sales rep name (customers
     * without a rep last), then largest excess first, then name and id.
     */
    public List<CustomerOverLimit> findCustomersOverCreditLimit(LocalDate asOf) {
        return queryList("SELECT c.id AS id, c.name AS name, c.region AS region, r.name AS rep_name, "
                + "c.credit_limit AS credit_limit, x.balance AS balance "
                + "FROM " + OPEN_BALANCE_PER_CUSTOMER_AS_OF
                + "JOIN customer c ON c.id = x.customer_id LEFT JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE " + OVER_CREDIT_LIMIT
                + "ORDER BY c.region, r.name NULLS LAST, (x.balance - c.credit_limit) DESC, c.name, c.id",
                rs -> new CustomerOverLimit(rs.getLong("id"), rs.getString("name"),
                        Region.valueOf(rs.getString("region")), rs.getString("rep_name"),
                        rs.getBigDecimal("credit_limit"), rs.getBigDecimal("balance")), asOf, asOf);
    }

    /** Per region, the customers over their credit limit on the date and the total excess. Largest excess first, then region. */
    public List<RegionOverLimit> findOverCreditLimitPerRegion(LocalDate asOf) {
        return queryList("SELECT c.region AS region, COUNT(*) AS customer_count, SUM(x.balance - c.credit_limit) AS excess "
                + "FROM " + OPEN_BALANCE_PER_CUSTOMER_AS_OF
                + "JOIN customer c ON c.id = x.customer_id "
                + "WHERE " + OVER_CREDIT_LIMIT
                + "GROUP BY c.region ORDER BY excess DESC, c.region",
                rs -> new RegionOverLimit(Region.valueOf(rs.getString("region")), rs.getInt("customer_count"),
                        rs.getBigDecimal("excess")), asOf, asOf);
    }

    /**
     * Per sales rep, the customers over their credit limit on the date and the total excess. Customers without a rep
     * are left out. Largest excess first, then rep name and id.
     */
    public List<RepOverLimit> findOverCreditLimitPerSalesRep(LocalDate asOf) {
        return queryList("SELECT r.id AS id, r.name AS name, COUNT(*) AS customer_count, SUM(x.balance - c.credit_limit) AS excess "
                + "FROM " + OPEN_BALANCE_PER_CUSTOMER_AS_OF
                + "JOIN customer c ON c.id = x.customer_id JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE " + OVER_CREDIT_LIMIT
                + "GROUP BY r.id, r.name ORDER BY excess DESC, r.name, r.id",
                rs -> new RepOverLimit(rs.getLong("id"), rs.getString("name"), rs.getInt("customer_count"),
                        rs.getBigDecimal("excess")), asOf, asOf);
    }

    /** The sum of the credit notes of the customer, zero if there are none. */
    public BigDecimal sumCreditNotesOfCustomer(long customerId) {
        return querySingle("SELECT COALESCE(SUM(amount), 0) AS total FROM credit_note WHERE customer_id = ?",
                rs -> rs.getBigDecimal("total"), customerId);
    }

    /**
     * Number and total amount of the credit notes dated between the two dates, inclusive, per reason of the underlying
     * return. Sorted by amount, highest first (then by reason); reasons without credit notes are left out.
     */
    public List<ReasonCredit> findCreditNoteTotalsPerReturnReason(LocalDate from, LocalDate to) {
        return queryList("SELECT r.reason AS reason, COUNT(*) AS note_count, SUM(n.amount) AS total "
                + "FROM credit_note n JOIN sales_return r ON r.id = n.return_id "
                + "WHERE n.credit_date >= ? AND n.credit_date <= ? "
                + "GROUP BY r.reason ORDER BY total DESC, r.reason",
                rs -> new ReasonCredit(rs.getString("reason"), rs.getInt("note_count"), rs.getBigDecimal("total")), from, to);
    }

    /** The sum of the credit notes of the customer dated before the date, zero if there are none. */
    public BigDecimal sumCreditNotesOfCustomerDatedBefore(long customerId, LocalDate before) {
        return querySingle("SELECT COALESCE(SUM(amount), 0) AS total FROM credit_note WHERE customer_id = ? AND credit_date < ?",
                rs -> rs.getBigDecimal("total"), customerId, before);
    }

    // ===== Shipments, partial shipments and carriers =====

    public Shipment createShipment(Shipment shipment) {
        long id = insert("INSERT INTO shipment (order_id, warehouse_id, carrier_id, tracking_number, ship_date) VALUES (?, ?, ?, ?, ?)",
                shipment.orderId(), shipment.warehouseId(), shipment.carrierId(), shipment.trackingNumber(), shipment.shipDate());
        return new Shipment(id, shipment.orderId(), shipment.warehouseId(), shipment.carrierId(), shipment.trackingNumber(),
                shipment.shipDate());
    }

    /** The shipment of the sales order (an order is shipped in one go at most once). */
    public Optional<Shipment> findShipmentBySalesOrder(long orderId) {
        return queryOptional("SELECT id, order_id, warehouse_id, carrier_id, tracking_number, ship_date, delivery_date "
                + "FROM shipment WHERE order_id = ?", DataStore::shipmentRow, orderId);
    }

    /**
     * SQL condition (aliases s = shipment, c = carrier): the delivered shipment took more days from ship date to delivery
     * date than the promised days of its carrier. The single definition of a late shipment.
     */
    private static final String SHIPMENT_DELIVERED_LATE = "DATEDIFF('DAY', s.ship_date, s.delivery_date) > c.promised_days";

    /**
     * Which shipments findShipments returns: start with a factory, then narrow with the chainable filters (all filters
     * apply together). Shipments come oldest ship date first, then by id; latest reverses that and limits the count.
     * Dates of the filters are inclusive unless the name says otherwise.
     */
    private static final class ShipmentQuery {
        private final List<String> conditions = new ArrayList<>();
        private final List<Object> params = new ArrayList<>();
        private boolean joinSalesOrder;
        private boolean joinCustomer;
        private boolean joinCarrier;
        private Integer latestLimit;

        private ShipmentQuery() {
        }

        /** All shipments. */
        public static ShipmentQuery all() {
            return new ShipmentQuery();
        }

        /** Shipments in transit: shipped and not yet delivered (no delivery date). */
        public static ShipmentQuery inTransit() {
            return all().where("s.delivery_date IS NULL");
        }

        /** Delivered shipments (with a delivery date). */
        public static ShipmentQuery delivered() {
            return all().where("s.delivery_date IS NOT NULL");
        }

        /** Late shipments: delivered, and took more days from ship date to delivery than the promised days of the carrier. */
        public static ShipmentQuery deliveredLate() {
            ShipmentQuery query = delivered().where(SHIPMENT_DELIVERED_LATE);
            query.joinCarrier = true;
            return query;
        }

        /** The N most recent shipments: newest ship date first, then by highest id, at most {@code limit} of them. */
        public static ShipmentQuery latest(int limit) {
            ShipmentQuery query = all();
            query.latestLimit = limit;
            return query;
        }

        /**
         * The shipments matching all the given criteria. Every criterion is optional: a null carrier id, warehouse id,
         * customer id, status, from date or to date is not applied. The dates apply to the ship date. (Only shipments of
         * existing sales orders are considered.)
         */
        public static ShipmentQuery matching(Long carrierId, Long warehouseId, Long customerId, ShipmentStatus status,
                                             LocalDate from, LocalDate to) {
            ShipmentQuery query = status == null ? all() : status == ShipmentStatus.IN_TRANSIT ? inTransit() : delivered();
            query.joinSalesOrder = true;
            if (carrierId != null) {
                query.byCarrier(carrierId);
            }
            if (warehouseId != null) {
                query.fromWarehouse(warehouseId);
            }
            if (customerId != null) {
                query.ofCustomer(customerId);
            }
            if (from != null) {
                query.where("s.ship_date >= ?", from);
            }
            if (to != null) {
                query.where("s.ship_date <= ?", to);
            }
            return query;
        }

        /** Only shipments shipped by the carrier. */
        public ShipmentQuery byCarrier(long carrierId) {
            return where("s.carrier_id = ?", carrierId);
        }

        /** Only shipments shipped from the warehouse. */
        public ShipmentQuery fromWarehouse(long warehouseId) {
            return where("s.warehouse_id = ?", warehouseId);
        }

        /** Only shipments of sales orders of the customer. */
        public ShipmentQuery ofCustomer(long customerId) {
            joinSalesOrder = true;
            return where("o.customer_id = ?", customerId);
        }

        /** Only shipments of sales orders of customers in the region. */
        public ShipmentQuery ofCustomersInRegion(Region region) {
            joinSalesOrder = true;
            joinCustomer = true;
            return where("cu.region = ?", region.name());
        }

        /** Only shipments of sales orders of customers in the country. */
        public ShipmentQuery ofCustomersInCountry(String country) {
            joinSalesOrder = true;
            joinCustomer = true;
            return where("cu.country = ?", country);
        }

        /** Only shipments of sales orders with a line of the product. */
        public ShipmentQuery withProduct(long productId) {
            return where("EXISTS (SELECT 1 FROM sales_order_line l WHERE l.order_id = s.order_id AND l.product_id = ?)", productId);
        }

        /** Only shipments shipped between the two dates, inclusive. */
        public ShipmentQuery shippedBetween(LocalDate from, LocalDate to) {
            return where("s.ship_date >= ?", from).where("s.ship_date <= ?", to);
        }

        /** Only shipments shipped strictly before the date. */
        public ShipmentQuery shippedBefore(LocalDate date) {
            return where("s.ship_date < ?", date);
        }

        private ShipmentQuery where(String condition, Object... conditionParams) {
            conditions.add(condition);
            for (Object param : conditionParams) {
                params.add(param); // may be null: binds SQL NULL, as a null date matches nothing
            }
            return this;
        }

        private String sql() {
            StringBuilder sql = new StringBuilder("SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, "
                    + "s.ship_date, s.delivery_date FROM shipment s");
            if (joinSalesOrder) {
                sql.append(" JOIN sales_order o ON o.id = s.order_id");
            }
            if (joinCustomer) {
                sql.append(" JOIN customer cu ON cu.id = o.customer_id");
            }
            if (joinCarrier) {
                sql.append(" JOIN carrier c ON c.id = s.carrier_id");
            }
            if (!conditions.isEmpty()) {
                sql.append(" WHERE ").append(String.join(" AND ", conditions));
            }
            sql.append(latestLimit == null ? " ORDER BY s.ship_date, s.id" : " ORDER BY s.ship_date DESC, s.id DESC LIMIT ?");
            return sql.toString();
        }

        private Object[] params() {
            List<Object> all = new ArrayList<>(params);
            if (latestLimit != null) {
                all.add(latestLimit);
            }
            return all.toArray();
        }
    }

    /** The shipments selected by the query; the single shipment-list query behind the find…Shipments… methods. */
    private List<Shipment> findShipments(ShipmentQuery query) {
        return queryList(query.sql(), DataStore::shipmentRow, query.params());
    }

    /** Shipments from the warehouse shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> findShipmentsFromWarehouseShippedBetween(long warehouseId, LocalDate from, LocalDate to) {
        return findShipments(ShipmentQuery.all().fromWarehouse(warehouseId).shippedBetween(from, to));
    }

    /** Shipments in transit (shipped, not yet delivered), oldest first. */
    public List<Shipment> findShipmentsInTransit() {
        return findShipments(ShipmentQuery.inTransit());
    }

    /** Shipments of the carrier in transit, oldest first. */
    public List<Shipment> findShipmentsInTransitByCarrier(long carrierId) {
        return findShipments(ShipmentQuery.inTransit().byCarrier(carrierId));
    }

    /** Shipments in transit shipped strictly before the date, oldest first. */
    public List<Shipment> findShipmentsInTransitShippedBefore(LocalDate shippedBefore) {
        return findShipments(ShipmentQuery.inTransit().shippedBefore(shippedBefore));
    }

    /** Shipments of the customer's orders shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> findShipmentsOfCustomerShippedBetween(long customerId, LocalDate from, LocalDate to) {
        return findShipments(ShipmentQuery.all().ofCustomer(customerId).shippedBetween(from, to));
    }

    /**
     * The shipments matching all the given criteria, oldest ship date first (then by id). Every criterion is optional:
     * a null carrier id, warehouse id, customer id, status, from date or to date is not applied. The dates are inclusive
     * and apply to the ship date.
     */
    public List<Shipment> findShipmentsMatching(Long carrierId, Long warehouseId, Long customerId, ShipmentStatus status,
                                                LocalDate from, LocalDate to) {
        return findShipments(ShipmentQuery.matching(carrierId, warehouseId, customerId, status, from, to));
    }

    /** Shipments of the customer's orders in transit, oldest first. */
    public List<Shipment> findShipmentsInTransitOfCustomer(long customerId) {
        return findShipments(ShipmentQuery.inTransit().ofCustomer(customerId));
    }

    /** Late shipments (delivered after the promised days of the carrier) of the customer's orders, oldest first. */
    public List<Shipment> findLateShipmentsOfCustomer(long customerId) {
        return findShipments(ShipmentQuery.deliveredLate().ofCustomer(customerId));
    }

    /** Shipments to customers of the region shipped between the two dates, inclusive, and still in transit, oldest first. */
    public List<Shipment> findShipmentsInTransitInRegionShippedBetween(Region region, LocalDate from, LocalDate to) {
        return findShipments(ShipmentQuery.inTransit().ofCustomersInRegion(region).shippedBetween(from, to));
    }

    /** Late shipments to customers of the region shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> findLateShipmentsInRegionShippedBetween(Region region, LocalDate from, LocalDate to) {
        return findShipments(ShipmentQuery.deliveredLate().ofCustomersInRegion(region).shippedBetween(from, to));
    }

    /** Shipments of orders with a line of the product shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> findShipmentsWithProductShippedBetween(long productId, LocalDate from, LocalDate to) {
        return findShipments(ShipmentQuery.all().withProduct(productId).shippedBetween(from, to));
    }

    /** Late shipments from the warehouse shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> findLateShipmentsFromWarehouseShippedBetween(long warehouseId, LocalDate from, LocalDate to) {
        return findShipments(ShipmentQuery.deliveredLate().fromWarehouse(warehouseId).shippedBetween(from, to));
    }

    public Carrier createCarrier(Carrier carrier) {
        long id = insert("INSERT INTO carrier (name, service_level, price_per_kg, promised_days) VALUES (?, ?, ?, ?)",
                carrier.name(), carrier.serviceLevel().name(), carrier.pricePerKg(), carrier.promisedDays());
        return new Carrier(id, carrier.name(), carrier.serviceLevel(), carrier.pricePerKg(), carrier.promisedDays());
    }

    /** Columns: id, name, service_level, price_per_kg, promised_days. */
    private static Carrier carrierRow(ResultSet rs) throws SQLException {
        return new Carrier(rs.getLong("id"), rs.getString("name"), ServiceLevel.valueOf(rs.getString("service_level")),
                rs.getBigDecimal("price_per_kg"), rs.getInt("promised_days"));
    }

    /** All carriers, sorted by name. */
    public List<Carrier> findCarriersOrderedByName() {
        return queryList("SELECT id, name, service_level, price_per_kg, promised_days FROM carrier ORDER BY name, id",
                DataStore::carrierRow);
    }

    /** The carriers with the service level, sorted by name. */
    public List<Carrier> findCarriersByServiceLevel(ServiceLevel serviceLevel) {
        return queryList("SELECT id, name, service_level, price_per_kg, promised_days FROM carrier WHERE service_level = ? "
                + "ORDER BY name, id", DataStore::carrierRow, serviceLevel.name());
    }

    public Optional<Carrier> findCarrierByName(String name) {
        return queryOptional("SELECT id, name, service_level, price_per_kg, promised_days FROM carrier WHERE name = ?",
                DataStore::carrierRow, name);
    }

    public Optional<Carrier> findCarrierById(long id) {
        return queryOptional("SELECT id, name, service_level, price_per_kg, promised_days FROM carrier WHERE id = ?",
                DataStore::carrierRow, id);
    }

    /**
     * Per carrier, the shipments shipped between the two dates, inclusive: their number, the average days from shipping to
     * delivery of the delivered ones (null if none is delivered) and the number delivered later than the promised days of
     * the carrier (SHIPMENT_DELIVERED_LATE). Sorted by carrier name; carriers without shipments in the period are left out.
     */
    public List<CarrierPerformance> findShipmentPerformancePerCarrier(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id, c.name, c.service_level, c.price_per_kg, c.promised_days, COUNT(s.id) AS shipment_count, "
                        + "AVG(CAST(DATEDIFF('DAY', s.ship_date, s.delivery_date) AS DECIMAL(10, 2))) AS average_days, "
                        + "COALESCE(SUM(CASE WHEN s.delivery_date IS NOT NULL "
                        + "AND " + SHIPMENT_DELIVERED_LATE + " THEN 1 ELSE 0 END), 0) AS late_count "
                        + "FROM carrier c JOIN shipment s ON s.carrier_id = c.id "
                        + "WHERE s.ship_date >= ? AND s.ship_date <= ? "
                        + "GROUP BY c.id, c.name, c.service_level, c.price_per_kg, c.promised_days ORDER BY c.name, c.id",
                rs -> {
                    BigDecimal averageDays = rs.getBigDecimal("average_days");
                    return new CarrierPerformance(carrierRow(rs), rs.getInt("shipment_count"),
                            averageDays == null ? null : averageDays.setScale(2, RoundingMode.HALF_UP), rs.getInt("late_count"));
                }, from, to);
    }

    /**
     * SQL condition (alias o = sales_order): the carrier shipped the order between two dates, inclusive, whole or any part
     * of it. The carrier is given as an SQL expression ("?" or a column); parameters in order: [carrier,] from, to,
     * [carrier,] from, to.
     */
    private static String salesOrderShippedByCarrierBetween(String carrier) {
        return "(EXISTS (SELECT 1 FROM shipment s WHERE s.order_id = o.id AND s.carrier_id = " + carrier + " "
                + "AND s.ship_date >= ? AND s.ship_date <= ?) "
                + "OR EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = o.id AND sp.carrier_id = " + carrier + " "
                + "AND sp.ship_date >= ? AND sp.ship_date <= ?))";
    }

    /**
     * Sales orders shipped with the carrier between the two dates, inclusive, sorted by order date. An order counts if the
     * carrier shipped it whole or any part of it in the period; each order is listed once.
     */
    public List<SalesOrder> findSalesOrdersShippedByCarrier(long carrierId, LocalDate from, LocalDate to) {
        return queryList("SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                        + "WHERE " + salesOrderShippedByCarrierBetween("?") + " ORDER BY o.order_date, o.id",
                this::salesOrderRow, carrierId, from, to, carrierId, from, to);
    }

    /**
     * Per carrier, the number and total value of the sales orders shipped with it between the two dates, inclusive. An order
     * counts for a carrier if the carrier shipped it whole or any part of it in the period (an order split over several
     * carriers counts for each of them, with its full value). Sorted by carrier name; carriers without orders in the
     * period are left out.
     */
    public List<CarrierOrders> findSalesOrderCountAndValuePerCarrier(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id, c.name, c.service_level, c.price_per_kg, c.promised_days, "
                        + "COUNT(DISTINCT o.id) AS order_count, COALESCE(SUM(l.quantity * l.unit_price), 0) AS total "
                        + "FROM carrier c JOIN sales_order o ON " + salesOrderShippedByCarrierBetween("c.id") + " "
                        + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                        + "GROUP BY c.id, c.name, c.service_level, c.price_per_kg, c.promised_days ORDER BY c.name, c.id",
                rs -> new CarrierOrders(carrierRow(rs), rs.getInt("order_count"), rs.getBigDecimal("total")),
                from, to, from, to);
    }

    /**
     * The lead time of delivered sales orders (aliases o = sales_order, s = shipment): select list part giving order_count
     * and average_days, the average days from the order date to the delivery date of the shipment. Use with
     * DELIVERED_SALES_ORDERS_PLACED_BETWEEN and map the average with leadTimeAverageDays.
     */
    private static final String SALES_ORDER_COUNT_AND_AVERAGE_LEAD_TIME =
            "COUNT(*) AS order_count, AVG(CAST(DATEDIFF('DAY', o.order_date, s.delivery_date) AS DECIMAL(10, 2))) AS average_days";

    /** SQL condition: the order was placed between two dates (parameters from, to), inclusive, and its shipment is delivered. */
    private static final String DELIVERED_SALES_ORDERS_PLACED_BETWEEN =
            "o.order_date >= ? AND o.order_date <= ? AND s.delivery_date IS NOT NULL";

    private static BigDecimal leadTimeAverageDays(ResultSet rs) throws SQLException {
        return rs.getBigDecimal("average_days").setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Per region of the customer, the number of delivered sales orders placed between the two dates, inclusive, and the
     * average days from the order date to the delivery date of the shipment. Orders not yet delivered are left out. Sorted
     * by region; regions without delivered orders in the period are left out.
     */
    public List<RegionLeadTime> findSalesOrderLeadTimePerRegion(LocalDate from, LocalDate to) {
        return queryList("SELECT c.region AS region, " + SALES_ORDER_COUNT_AND_AVERAGE_LEAD_TIME + " "
                        + "FROM sales_order o JOIN customer c ON c.id = o.customer_id JOIN shipment s ON s.order_id = o.id "
                        + "WHERE " + DELIVERED_SALES_ORDERS_PLACED_BETWEEN + " GROUP BY c.region ORDER BY c.region",
                rs -> new RegionLeadTime(Region.valueOf(rs.getString("region")), rs.getInt("order_count"), leadTimeAverageDays(rs)),
                from, to);
    }

    /**
     * Per carrier, the number of delivered sales orders placed between the two dates, inclusive, that it shipped and the
     * average days from the order date to the delivery date of the shipment. Orders not yet delivered are left out. Sorted
     * by carrier name; carriers without delivered orders in the period are left out.
     */
    public List<CarrierLeadTime> findSalesOrderLeadTimePerCarrier(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id, c.name, c.service_level, c.price_per_kg, c.promised_days, "
                        + SALES_ORDER_COUNT_AND_AVERAGE_LEAD_TIME + " "
                        + "FROM sales_order o JOIN shipment s ON s.order_id = o.id JOIN carrier c ON c.id = s.carrier_id "
                        + "WHERE " + DELIVERED_SALES_ORDERS_PLACED_BETWEEN + " "
                        + "GROUP BY c.id, c.name, c.service_level, c.price_per_kg, c.promised_days ORDER BY c.name, c.id",
                rs -> new CarrierLeadTime(carrierRow(rs), rs.getInt("order_count"), leadTimeAverageDays(rs)), from, to);
    }

    /** The N most recent shipments, newest ship date first, then by highest id. */
    public List<Shipment> findLatestShipments(int limit) {
        return findShipments(ShipmentQuery.latest(limit));
    }

    /** The N most recent shipments of orders of customers in the region, newest ship date first, then by highest id. */
    public List<Shipment> findLatestShipmentsInRegion(Region region, int limit) {
        return findShipments(ShipmentQuery.latest(limit).ofCustomersInRegion(region));
    }

    /** The N most recent shipments of orders of the customer, newest ship date first, then by highest id. */
    public List<Shipment> findLatestShipmentsOfCustomer(long customerId, int limit) {
        return findShipments(ShipmentQuery.latest(limit).ofCustomer(customerId));
    }

    /** Shipments for orders of customers of the country shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> findShipmentsInCountryShippedBetween(String country, LocalDate from, LocalDate to) {
        return findShipments(ShipmentQuery.all().ofCustomersInCountry(country).shippedBetween(from, to));
    }

    /**
     * Sets the delivery date of the sales order's shipment, but only while it is in transit (a delivery date already
     * set is kept). Returns whether a shipment was changed.
     */
    public boolean setShipmentDeliveryDateIfInTransit(long orderId, LocalDate deliveryDate) {
        return update("UPDATE shipment SET delivery_date = ? WHERE order_id = ? AND delivery_date IS NULL", deliveryDate, orderId) > 0;
    }

    /**
     * SQL of a derived table of shipped quantities with columns {@code <keyColumn>} (taken from the shipment or the part,
     * e.g. "warehouse_id" or "carrier_id"), product_id and quantity, shipped between two dates, inclusive. Parameters:
     * from, to, from, to. Orders shipped in one go count the line quantities at the ship date of the shipment; orders
     * shipped in parts count the parts (at their own ship dates) instead.
     */
    private static String shippedQuantitiesBetween(String keyColumn) {
        return "SELECT s." + keyColumn + ", l.product_id, l.quantity FROM shipment s "
                + "JOIN sales_order_line l ON l.order_id = s.order_id "
                + "WHERE s.ship_date >= ? AND s.ship_date <= ? "
                + "AND NOT EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = s.order_id) "
                + "UNION ALL "
                + "SELECT sp." + keyColumn + ", l.product_id, sp.quantity FROM shipment_part sp "
                + "JOIN sales_order_line l ON l.order_id = sp.order_id AND l.line_no = sp.line_no "
                + "WHERE sp.ship_date >= ? AND sp.ship_date <= ?";
    }

    /**
     * The quantity of the product shipped from each warehouse between the two dates, inclusive, ordered by warehouse code.
     * Orders shipped in one go count the line quantity at the ship date of the shipment; orders shipped in parts count
     * the parts (see shippedQuantitiesBetween).
     */
    public List<WarehouseShippedQuantity> findShippedQuantityOfProductPerWarehouse(long productId, LocalDate from, LocalDate to) {
        return queryList("SELECT w.id, w.code, w.name, w.city, w.region, SUM(q.quantity) AS shipped FROM ("
                        + shippedQuantitiesBetween("warehouse_id") + ") q "
                        + "JOIN warehouse w ON w.id = q.warehouse_id WHERE q.product_id = ? "
                        + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id",
                rs -> new WarehouseShippedQuantity(warehouseRow(rs), rs.getInt("shipped")),
                from, to, from, to, productId);
    }

    /**
     * For each warehouse with delivered shipments shipped between the two dates (inclusive), the number of delivered shipments and
     * how many of them took more days than the promised days of the carrier (SHIPMENT_DELIVERED_LATE), ordered by
     * warehouse code.
     */
    public List<WarehouseDeliveryCount> findDeliveredAndLateCountPerWarehouse(LocalDate from, LocalDate to) {
        return queryList("SELECT w.id, w.code, w.name, w.city, w.region, COUNT(*) AS delivered, "
                        + "SUM(CASE WHEN " + SHIPMENT_DELIVERED_LATE + " THEN 1 ELSE 0 END) AS late "
                        + "FROM shipment s "
                        + "JOIN warehouse w ON w.id = s.warehouse_id "
                        + "JOIN carrier c ON c.id = s.carrier_id "
                        + "WHERE s.ship_date >= ? AND s.ship_date <= ? AND s.delivery_date IS NOT NULL "
                        + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id",
                rs -> new WarehouseDeliveryCount(warehouseRow(rs), rs.getInt("delivered"), rs.getInt("late")),
                from, to);
    }

    /**
     * The weight in kg shipped by each carrier between the two dates, inclusive, ordered by carrier name. Orders shipped in
     * one go count the line quantities at the ship date of the shipment; orders shipped in parts count the parts (see
     * shippedQuantitiesBetween). Products without a weight count as zero; carriers that shipped no weight in the period
     * are left out.
     */
    public List<CarrierShippedWeight> findShippedWeightPerCarrier(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id, c.name, c.service_level, c.price_per_kg, c.promised_days, "
                        + "SUM(q.quantity * pw.weight_kg) AS weight FROM (" + shippedQuantitiesBetween("carrier_id") + ") q "
                        + "JOIN product_weight pw ON pw.product_id = q.product_id "
                        + "JOIN carrier c ON c.id = q.carrier_id "
                        + "GROUP BY c.id, c.name, c.service_level, c.price_per_kg, c.promised_days "
                        + "HAVING SUM(q.quantity * pw.weight_kg) > 0 ORDER BY c.name, c.id",
                rs -> new CarrierShippedWeight(carrierRow(rs), rs.getBigDecimal("weight")), from, to, from, to);
    }

    /**
     * The weight in kg shipped from each warehouse between the two dates, inclusive, ordered by warehouse code. Orders
     * shipped in one go count the line quantities at the ship date of the shipment; orders shipped in parts count the
     * parts (see shippedQuantitiesBetween). Products without a weight count as zero; warehouses that shipped no weight in
     * the period are left out.
     */
    public List<WarehouseShippedWeight> findShippedWeightPerWarehouse(LocalDate from, LocalDate to) {
        return queryList("SELECT w.id, w.code, w.name, w.city, w.region, "
                        + "SUM(q.quantity * pw.weight_kg) AS weight FROM (" + shippedQuantitiesBetween("warehouse_id") + ") q "
                        + "JOIN product_weight pw ON pw.product_id = q.product_id "
                        + "JOIN warehouse w ON w.id = q.warehouse_id "
                        + "GROUP BY w.id, w.code, w.name, w.city, w.region "
                        + "HAVING SUM(q.quantity * pw.weight_kg) > 0 ORDER BY w.code, w.id",
                rs -> new WarehouseShippedWeight(warehouseRow(rs), rs.getBigDecimal("weight")), from, to, from, to);
    }

    /** Records a part of a sales order line shipped on its own (an order shipped in parts). */
    public void createShipmentPart(long orderId, int lineNo, long warehouseId, long carrierId, String trackingNumber,
                                   LocalDate shipDate, int quantity) {
        update("INSERT INTO shipment_part (order_id, line_no, warehouse_id, carrier_id, tracking_number, ship_date, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)", orderId, lineNo, warehouseId, carrierId, trackingNumber, shipDate, quantity);
    }

    /**
     * The lines of the sales order not yet fully shipped in parts, by line number: ordered, shipped (sum of the shipment
     * parts) and remaining quantity.
     */
    public List<RemainingLine> findSalesOrderLinesNotFullyShippedInParts(long orderId) {
        List<RemainingLine> lines = queryList("SELECT l.line_no, l.product_id, l.quantity, "
                        + "COALESCE((SELECT SUM(p.quantity) FROM shipment_part p WHERE p.order_id = l.order_id AND p.line_no = l.line_no), 0) AS shipped "
                        + "FROM sales_order_line l WHERE l.order_id = ? ORDER BY l.line_no",
                rs -> new RemainingLine(rs.getInt("line_no"), rs.getLong("product_id"), rs.getInt("quantity"), rs.getInt("shipped"),
                        rs.getInt("quantity") - rs.getInt("shipped")), orderId);
        lines.removeIf(line -> line.shipped() >= line.ordered());
        return lines;
    }

    // ===== Sales returns =====

    /**
     * Which sales returns to select: one of the static factories, optionally narrowed to a return date period with
     * {@link #returnedBetween}. The order of the return and its customer are joined only when the selection needs them.
     */
    private static final class SalesReturnQuery {
        private boolean joinOrder;
        private boolean joinCustomer;
        private final List<String> conditions = new ArrayList<>();
        private final List<Object> params = new ArrayList<>();

        private SalesReturnQuery() {
        }

        /** All sales returns. */
        public static SalesReturnQuery all() {
            return new SalesReturnQuery();
        }

        /**
         * Sales returns matching any combination of the customer of the order, a product among the lines, the exact
         * reason and a return date range; a null customer id, product id, reason, from date or to date is not applied.
         * The dates are inclusive. Always joins the order of the return.
         */
        public static SalesReturnQuery matching(Long customerId, Long productId, String reason, LocalDate from, LocalDate to) {
            return all().joiningOrder().where("(CAST(? AS BIGINT) IS NULL OR o.customer_id = CAST(? AS BIGINT)) "
                            + "AND (CAST(? AS BIGINT) IS NULL OR EXISTS (SELECT 1 FROM sales_return_line pl "
                            + "WHERE pl.return_id = r.id AND pl.product_id = CAST(? AS BIGINT))) "
                            + "AND (CAST(? AS VARCHAR) IS NULL OR r.reason = CAST(? AS VARCHAR)) "
                            + "AND (CAST(? AS DATE) IS NULL OR r.return_date >= CAST(? AS DATE)) "
                            + "AND (CAST(? AS DATE) IS NULL OR r.return_date <= CAST(? AS DATE))",
                    customerId, customerId, productId, productId, reason, reason, from, from, to, to);
        }

        /** Returns for the sales order. */
        public static SalesReturnQuery ofOrder(long orderId) {
            return all().where("r.order_id = ?", orderId);
        }

        /** Returns for the orders of the customer. */
        public static SalesReturnQuery ofCustomer(long customerId) {
            return all().joiningOrder().where("o.customer_id = ?", customerId);
        }

        /** Returns for the orders of customers in the region. */
        public static SalesReturnQuery ofCustomersInRegion(Region region) {
            return all().joiningOrderAndCustomer().where("c.region = ?", region.name());
        }

        /** Returns for the orders of customers in the country. */
        public static SalesReturnQuery ofCustomersInCountry(String country) {
            return all().joiningOrderAndCustomer().where("c.country = ?", country);
        }

        /** Returns with at least one line of the product. */
        public static SalesReturnQuery containingProduct(long productId) {
            return all().where("EXISTS (SELECT 1 FROM sales_return_line pl WHERE pl.return_id = r.id AND pl.product_id = ?)",
                    productId);
        }

        /** Returns dated between the two dates, inclusive. */
        public SalesReturnQuery returnedBetween(LocalDate from, LocalDate to) {
            return where("r.return_date >= ? AND r.return_date <= ?", from, to);
        }

        private SalesReturnQuery joiningOrder() {
            joinOrder = true;
            return this;
        }

        private SalesReturnQuery joiningOrderAndCustomer() {
            joinOrder = true;
            joinCustomer = true;
            return this;
        }

        private SalesReturnQuery where(String condition, Object... values) {
            conditions.add(condition);
            for (Object value : values) {
                params.add(value);
            }
            return this;
        }

        private String fromAndWhere() {
            return "FROM sales_return r"
                    + (joinOrder ? " JOIN sales_order o ON o.id = r.order_id" : "")
                    + (joinCustomer ? " JOIN customer c ON c.id = o.customer_id" : "")
                    + (conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions));
        }
    }

    /** The sales returns selected by the query, with their lines, oldest first, then by id. */
    private List<SalesReturn> querySalesReturnsOldestFirst(SalesReturnQuery query) {
        return queryList("SELECT r.id, r.number, r.order_id, r.return_date, r.reason " + query.fromAndWhere()
                + " ORDER BY r.return_date, r.id", this::salesReturnRow, query.params.toArray());
    }

    /** The N most recent sales returns selected by the query, with their lines, newest first, then by highest id. */
    private List<SalesReturn> queryLatestSalesReturns(SalesReturnQuery query, int limit) {
        List<Object> params = new ArrayList<>(query.params);
        params.add(limit);
        return queryList("SELECT r.id, r.number, r.order_id, r.return_date, r.reason " + query.fromAndWhere()
                + " ORDER BY r.return_date DESC, r.id DESC LIMIT ?", this::salesReturnRow, params.toArray());
    }

    /** The sales returns of the sales order, with their lines, oldest first, then by id. */
    public List<SalesReturn> findSalesReturnsOfSalesOrder(long orderId) {
        return querySalesReturnsOldestFirst(SalesReturnQuery.ofOrder(orderId));
    }

    /** The sales returns for the orders of the customer, with their lines, oldest first, then by id. */
    public List<SalesReturn> findSalesReturnsOfCustomer(long customerId) {
        return querySalesReturnsOldestFirst(SalesReturnQuery.ofCustomer(customerId));
    }

    /**
     * The sales returns dated between the two dates, inclusive, for the orders of the customers of the country, oldest
     * first, then by id.
     */
    public List<SalesReturn> findSalesReturnsOfCustomersInCountryReturnedBetween(String country, LocalDate from, LocalDate to) {
        return querySalesReturnsOldestFirst(SalesReturnQuery.ofCustomersInCountry(country).returnedBetween(from, to));
    }

    /**
     * The sales returns dated between the two dates, inclusive, with at least one line of the product, oldest first,
     * then by id.
     */
    public List<SalesReturn> findSalesReturnsContainingProductReturnedBetween(long productId, LocalDate from, LocalDate to) {
        return querySalesReturnsOldestFirst(SalesReturnQuery.containingProduct(productId).returnedBetween(from, to));
    }

    /**
     * Searches sales returns by any combination of the customer of the order, a product among the lines, the exact
     * reason and a return date range; a null customer id, product id, reason, from date or to date is not applied.
     * The dates are inclusive. Oldest first.
     */
    public List<SalesReturn> findSalesReturnsMatching(Long customerId, Long productId, String reason, LocalDate from, LocalDate to) {
        return querySalesReturnsOldestFirst(SalesReturnQuery.matching(customerId, productId, reason, from, to));
    }

    /** The N most recent sales returns, newest first, then by highest id. */
    public List<SalesReturn> findLatestSalesReturns(int limit) {
        return queryLatestSalesReturns(SalesReturnQuery.all(), limit);
    }

    /** The N most recent sales returns of customers in the region, newest first, then by highest id. */
    public List<SalesReturn> findLatestSalesReturnsOfCustomersInRegion(Region region, int limit) {
        return queryLatestSalesReturns(SalesReturnQuery.ofCustomersInRegion(region), limit);
    }

    /** The N most recent sales returns of the customer, newest first, then by highest id. */
    public List<SalesReturn> findLatestSalesReturnsOfCustomer(long customerId, int limit) {
        return queryLatestSalesReturns(SalesReturnQuery.ofCustomer(customerId), limit);
    }

    /**
     * Units returned per product in the sales returns dated between the two dates, inclusive. Sorted by units
     * returned, highest first (then by SKU); products without returns are left out.
     */
    public List<ProductReturns> findReturnedQuantityPerProduct(LocalDate from, LocalDate to) {
        return findReturnedUnitsPerProduct("", "", DataStore::productReturnsRow, from, to);
    }

    /**
     * The shared query of the returned units per product: the sales return lines dated between the two dates,
     * inclusive, that the extra condition keeps, summed per product, highest first, then by SKU. Columns: id, sku,
     * name, units. The WITH clause goes in front of the query; the params are in the order of the SQL text.
     */
    private <T> List<T> findReturnedUnitsPerProduct(String withClause, String extraCondition, RowMapper<T> mapper,
            Object... params) {
        return queryList(withClause + "SELECT p.id AS id, p.sku AS sku, p.name AS name, SUM(l.quantity) AS units "
                + "FROM sales_return r JOIN sales_return_line l ON l.return_id = r.id "
                + "JOIN product p ON p.id = l.product_id "
                + "WHERE r.return_date >= ? AND r.return_date <= ?" + extraCondition + " "
                + "GROUP BY p.id, p.sku, p.name ORDER BY units DESC, p.sku", mapper, params);
    }

    private static ProductReturns productReturnsRow(ResultSet rs) throws SQLException {
        return new ProductReturns(rs.getLong("id"), rs.getString("sku"), rs.getString("name"), rs.getLong("units"));
    }

    /**
     * Units returned per product, as for the returned quantity per product, restricted to the products of the
     * category and of all categories below it.
     */
    public List<ProductReturns> findReturnedQuantityPerProductInCategory(String category, LocalDate from, LocalDate to) {
        return findReturnedUnitsPerProduct("WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                        + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) ",
                " AND p.category IN (SELECT code FROM tree)", DataStore::productReturnsRow, category, from, to);
    }

    /**
     * SQL value of one sales return line (alias {@code l}) of a sales return (alias {@code r}): the returned quantity at
     * the unit price of the first line of the order with the same product. The single rule for valuing returns.
     */
    private static final String SALES_RETURN_LINE_VALUE = "l.quantity * (SELECT ol.unit_price FROM sales_order_line ol "
            + "WHERE ol.order_id = r.order_id AND ol.product_id = l.product_id "
            + "ORDER BY ol.line_no FETCH FIRST 1 ROW ONLY)";

    /**
     * Number and value of the sales returns dated between the two dates, inclusive, per reason. The value is the
     * returned quantities at the unit prices of the order. Sorted by value, highest first (then by reason); reasons
     * without returns are left out.
     */
    public List<ReasonReturns> findSalesReturnCountAndValuePerReason(LocalDate from, LocalDate to) {
        return queryList("SELECT r.reason AS reason, COUNT(DISTINCT r.id) AS return_count, "
                        + "COALESCE(SUM(" + SALES_RETURN_LINE_VALUE + "), 0) AS total "
                        + "FROM sales_return r LEFT JOIN sales_return_line l ON l.return_id = r.id "
                        + "WHERE r.return_date >= ? AND r.return_date <= ? "
                        + "GROUP BY r.reason ORDER BY total DESC, r.reason",
                rs -> new ReasonReturns(rs.getString("reason"), rs.getInt("return_count"), rs.getBigDecimal("total")),
                from, to);
    }

    /**
     * The sales returns dated between the two dates, inclusive, with the customer of the order and the value of each
     * return (the returned quantities at the unit prices of the order, as for the returns per reason). Customers with
     * the most returns first (then by name and id), each customer's returns oldest first (then by id).
     */
    public List<CustomerReturn> findSalesReturnsWithValuePerCustomer(LocalDate from, LocalDate to) {
        return queryList("SELECT c.id AS customer_id, c.name AS customer_name, r.id AS id, r.number AS number, "
                        + "r.return_date AS return_date, r.reason AS reason, "
                        + "COALESCE((SELECT SUM(" + SALES_RETURN_LINE_VALUE + ") "
                        + "FROM sales_return_line l WHERE l.return_id = r.id), 0) AS returned_value "
                        + "FROM sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id "
                        + "WHERE r.return_date >= ? AND r.return_date <= ? "
                        + "ORDER BY (SELECT COUNT(*) FROM sales_return r2 JOIN sales_order o2 ON o2.id = r2.order_id "
                        + "WHERE o2.customer_id = c.id AND r2.return_date >= ? AND r2.return_date <= ?) DESC, "
                        + "c.name, c.id, r.return_date, r.id",
                rs -> new CustomerReturn(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                        rs.getString("customer_name"), rs.getObject("return_date", LocalDate.class),
                        rs.getString("reason"), rs.getBigDecimal("returned_value")),
                from, to, from, to);
    }

    /**
     * The sales returns received into the warehouse, dated between the two dates, inclusive, with the number of the
     * order. Oldest first, then by id.
     */
    public List<WarehouseReturn> findSalesReturnsReceivedIntoWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return queryList("SELECT r.id AS id, r.number AS number, o.number AS order_number, r.return_date AS return_date, "
                        + "r.reason AS reason FROM sales_return r JOIN sales_order o ON o.id = r.order_id "
                        + "WHERE r.warehouse_id = ? AND r.return_date >= ? AND r.return_date <= ? "
                        + "ORDER BY r.return_date, r.id",
                rs -> new WarehouseReturn(rs.getLong("id"), rs.getString("number"), rs.getString("order_number"),
                        rs.getObject("return_date", LocalDate.class), rs.getString("reason")),
                warehouseId, from, to);
    }

    /**
     * Units returned per product, as for the returned quantity per product, restricted to the sales returns received
     * into the warehouse. Highest quantity first, then by SKU.
     */
    public List<ProductReturnedQuantity> findReturnedQuantityPerProductReceivedIntoWarehouse(long warehouseId,
            LocalDate from, LocalDate to) {
        return findReturnedUnitsPerProduct("", " AND r.warehouse_id = ?",
                rs -> new ProductReturnedQuantity(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                        rs.getLong("units")),
                from, to, warehouseId);
    }

    /**
     * Number and value of the sales returns dated between the two dates, inclusive, per reason and region of the
     * customer, valued as for the returns per reason. Sorted by region name, then value (highest first), then reason;
     * combinations without returns are left out.
     */
    public List<RegionReasonReturns> findSalesReturnCountAndValuePerRegionAndReason(LocalDate from, LocalDate to) {
        return queryList("SELECT c.region AS region, r.reason AS reason, COUNT(DISTINCT r.id) AS return_count, "
                        + "COALESCE(SUM(" + SALES_RETURN_LINE_VALUE + "), 0) AS total "
                        + "FROM sales_return r JOIN sales_order o ON o.id = r.order_id "
                        + "JOIN customer c ON c.id = o.customer_id "
                        + "LEFT JOIN sales_return_line l ON l.return_id = r.id "
                        + "WHERE r.return_date >= ? AND r.return_date <= ? "
                        + "GROUP BY c.region, r.reason ORDER BY c.region, total DESC, r.reason",
                rs -> new RegionReasonReturns(Region.valueOf(rs.getString("region")), rs.getString("reason"),
                        rs.getInt("return_count"), rs.getBigDecimal("total")),
                from, to);
    }

    /** Creates the return and adds its quantities to the stock of the warehouse, all or nothing. */
    public SalesReturn createSalesReturn(SalesReturn salesReturn, long warehouseId) {
        return inTransactionRollingBackOnDatabaseFailureOnly(() -> {
            long id = insert("INSERT INTO sales_return (number, order_id, return_date, reason, warehouse_id) VALUES (?, ?, ?, ?, ?)",
                    salesReturn.number(), salesReturn.orderId(), salesReturn.returnDate(), salesReturn.reason(), warehouseId);
            int lineNo = 1;
            for (ReturnLine line : salesReturn.lines()) {
                update("INSERT INTO sales_return_line (return_id, line_no, product_id, quantity) VALUES (?, ?, ?, ?)",
                        id, lineNo++, line.productId(), line.quantity());
            }
            for (ReturnLine line : salesReturn.lines()) {
                if (update("UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?",
                        line.quantity(), warehouseId, line.productId()) == 0) {
                    update("INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)",
                            warehouseId, line.productId(), line.quantity());
                }
                update("INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                        + "VALUES (?, ?, ?, ?, 'RETURN')", line.productId(), warehouseId, salesReturn.returnDate(), line.quantity());
            }
            return new SalesReturn(id, salesReturn.number(), salesReturn.orderId(), salesReturn.returnDate(),
                    salesReturn.reason(), List.copyOf(salesReturn.lines()));
        });
    }

    public Optional<SalesReturn> findSalesReturnByNumber(String number) {
        return queryOptional("SELECT id, number, order_id, return_date, reason FROM sales_return WHERE number = ?",
                this::salesReturnRow, number);
    }

    /** Columns: id, number, order_id, return_date, reason; loads the return's lines with a second query. */
    private SalesReturn salesReturnRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        return new SalesReturn(id, rs.getString("number"), rs.getLong("order_id"),
                rs.getObject("return_date", LocalDate.class), rs.getString("reason"), salesReturnLines(id));
    }

    private List<ReturnLine> salesReturnLines(long returnId) {
        return queryList("SELECT product_id, quantity FROM sales_return_line WHERE return_id = ? ORDER BY line_no",
                rs -> new ReturnLine(rs.getLong("product_id"), rs.getInt("quantity")), returnId);
    }

    // ===== Support tickets =====

    public SupportTicket createSupportTicket(SupportTicket ticket) {
        long id = insert("INSERT INTO support_ticket (number, customer_id, order_id, subject, priority, status, opened_date, closed_date) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                ticket.number(), ticket.customerId(), ticket.orderId(), ticket.subject(), ticket.priority().name(),
                ticket.status().name(), ticket.openedDate(), ticket.closedDate());
        return new SupportTicket(id, ticket.number(), ticket.customerId(), ticket.orderId(), ticket.subject(),
                ticket.priority(), ticket.status(), ticket.openedDate(), ticket.closedDate());
    }

    public Optional<SupportTicket> findSupportTicketByNumber(String number) {
        return queryOptional("SELECT id, number, customer_id, order_id, subject, priority, status, opened_date, closed_date "
                + "FROM support_ticket WHERE number = ?", DataStore::supportTicketRow, number);
    }

    /** Columns: id, number, customer_id, order_id, subject, priority, status, opened_date, closed_date. */
    private static SupportTicket supportTicketRow(ResultSet rs) throws SQLException {
        return new SupportTicket(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                rs.getObject("order_id", Long.class), rs.getString("subject"),
                TicketPriority.valueOf(rs.getString("priority")), TicketStatus.valueOf(rs.getString("status")),
                rs.getObject("opened_date", LocalDate.class), rs.getObject("closed_date", LocalDate.class));
    }

    public void updateSupportTicketStatus(long ticketId, TicketStatus status, LocalDate closedDate) {
        update("UPDATE support_ticket SET status = ?, closed_date = ? WHERE id = ?", status.name(), closedDate, ticketId);
    }

    /**
     * Which support tickets to select (used by the public support ticket lists below): start with {@link #all()} or
     * {@link #open()}, then narrow down with the filters (every filter is ANDed). Oldest first (opening date, then id) unless {@link #highestPriorityFirst()} is chosen. The
     * customer of the ticket is joined only when a filter needs it.
     */
    private static final class SupportTicketQuery {
        private boolean joinCustomer;
        private boolean highestPriorityFirst;
        private final List<String> conditions = new ArrayList<>();
        private final List<Object> params = new ArrayList<>();

        private SupportTicketQuery() {
        }

        /** All tickets, whatever their status. */
        public static SupportTicketQuery all() {
            return new SupportTicketQuery();
        }

        /** Tickets that are not closed now (status OPEN or IN_PROGRESS, i.e. not CLOSED). */
        public static SupportTicketQuery open() {
            return new SupportTicketQuery().where("t.status <> 'CLOSED'");
        }

        /** Tickets of the customer. */
        public SupportTicketQuery ofCustomer(long customerId) {
            return where("t.customer_id = ?", customerId);
        }

        /** Tickets of customers in the region. */
        public SupportTicketQuery ofCustomersInRegion(Region region) {
            joinCustomer = true;
            return where("c.region = ?", region.name());
        }

        /** Tickets of the customers of the sales rep. */
        public SupportTicketQuery ofCustomersOfSalesRep(long salesRepId) {
            joinCustomer = true;
            return where("c.sales_rep_id = ?", salesRepId);
        }

        /** Tickets with the priority. */
        public SupportTicketQuery withPriority(TicketPriority priority) {
            return where("t.priority = ?", priority.name());
        }

        /** Tickets about the product. */
        public SupportTicketQuery aboutProduct(long productId) {
            return where("t.product_id = ?", productId);
        }

        /** Tickets that refer to an order. */
        public SupportTicketQuery referringToOrder() {
            return where("t.order_id IS NOT NULL");
        }

        /** Tickets opened on or before the date. */
        public SupportTicketQuery openedOnOrBefore(LocalDate date) {
            return where("t.opened_date <= ?", date);
        }

        /** Tickets opened strictly before the date. */
        public SupportTicketQuery openedBefore(LocalDate date) {
            return where("t.opened_date < ?", date);
        }

        /** Tickets not closed by the date: no closing date, or closed after it. */
        public SupportTicketQuery notClosedBy(LocalDate date) {
            return where("(t.closed_date IS NULL OR t.closed_date > ?)", date);
        }

        /** Sorts highest priority first (HIGH, NORMAL, then LOW), oldest first within a priority. */
        public SupportTicketQuery highestPriorityFirst() {
            highestPriorityFirst = true;
            return this;
        }

        private SupportTicketQuery where(String condition, Object... values) {
            conditions.add(condition);
            for (Object value : values) {
                params.add(value);
            }
            return this;
        }
    }

    /** The support tickets selected by the query, in the query's order (oldest first, then by id, by default). */
    private List<SupportTicket> querySupportTickets(SupportTicketQuery query) {
        return queryList("SELECT t.id, t.number, t.customer_id, t.order_id, t.subject, t.priority, t.status, t.opened_date, "
                        + "t.closed_date FROM support_ticket t"
                        + (query.joinCustomer ? " JOIN customer c ON c.id = t.customer_id" : "")
                        + (query.conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", query.conditions))
                        + " ORDER BY "
                        + (query.highestPriorityFirst ? "CASE t.priority WHEN 'HIGH' THEN 0 WHEN 'NORMAL' THEN 1 ELSE 2 END, " : "")
                        + "t.opened_date, t.id",
                DataStore::supportTicketRow, query.params.toArray());
    }

    /** Tickets of the customer that are not closed, oldest first. */
    public List<SupportTicket> findOpenSupportTicketsOfCustomer(long customerId) {
        return querySupportTickets(SupportTicketQuery.open().ofCustomer(customerId));
    }

    /** Tickets with the priority that are not closed, oldest first. */
    public List<SupportTicket> findOpenSupportTicketsWithPriority(TicketPriority priority) {
        return querySupportTickets(SupportTicketQuery.open().withPriority(priority));
    }

    /** Tickets of customers in the region that are not closed, oldest first. */
    public List<SupportTicket> findOpenSupportTicketsOfCustomersInRegion(Region region) {
        return querySupportTickets(SupportTicketQuery.open().ofCustomersInRegion(region));
    }

    /** Tickets of customers of the sales rep that are not closed, highest priority first, oldest first within a priority. */
    public List<SupportTicket> findOpenSupportTicketsOfSalesRepHighestPriorityFirst(long salesRepId) {
        return querySupportTickets(SupportTicketQuery.open().ofCustomersOfSalesRep(salesRepId).highestPriorityFirst());
    }

    /** Tickets about the product that are not closed, oldest first. */
    public List<SupportTicket> findOpenSupportTicketsAboutProduct(long productId) {
        return querySupportTickets(SupportTicketQuery.open().aboutProduct(productId));
    }

    /** Tickets of the customer that refer to an order, whatever their status, oldest first. */
    public List<SupportTicket> findSupportTicketsReferringToOrderOfCustomer(long customerId) {
        return querySupportTickets(SupportTicketQuery.all().ofCustomer(customerId).referringToOrder());
    }

    /**
     * High-priority tickets of customers in the region that were open on the date and opened more than the given number
     * of days before it (opened before the date minus the days, not closed by the date), oldest first.
     */
    public List<SupportTicket> findHighPrioritySupportTicketsInRegionOpenLongerThan(Region region, LocalDate date, int days) {
        return querySupportTickets(SupportTicketQuery.all().ofCustomersInRegion(region).withPriority(TicketPriority.HIGH)
                .openedBefore(date.minusDays(days)).notClosedBy(date));
    }

    /**
     * Tickets that were open on the date (opened on or before it, not closed by then) and opened before the cutoff date,
     * oldest first.
     */
    public List<SupportTicket> findSupportTicketsOpenOnDateOpenedBefore(LocalDate date, LocalDate cutoff) {
        return querySupportTickets(SupportTicketQuery.all().openedOnOrBefore(date).openedBefore(cutoff).notClosedBy(date));
    }

    /** Sales orders with more than one ticket and their number of tickets, most tickets first, then by order number. */
    public List<OrderTickets> findSalesOrdersWithMultipleSupportTickets() {
        return queryList("SELECT o.id, o.number, o.customer_id, COUNT(*) AS ticket_count FROM support_ticket t "
                        + "JOIN sales_order o ON o.id = t.order_id "
                        + "GROUP BY o.id, o.number, o.customer_id HAVING COUNT(*) > 1 ORDER BY ticket_count DESC, o.number",
                rs -> new OrderTickets(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                        rs.getInt("ticket_count")));
    }

    /** Per priority, the number of not closed tickets of customers in the region; priorities without such tickets are left out. */
    public List<PriorityTickets> findOpenSupportTicketCountsPerPriorityByRegion(Region region) {
        return queryList("SELECT t.priority, COUNT(*) AS ticket_count FROM support_ticket t JOIN customer c ON c.id = t.customer_id "
                        + "WHERE c.region = ? AND t.status <> 'CLOSED' GROUP BY t.priority ORDER BY t.priority",
                rs -> new PriorityTickets(TicketPriority.valueOf(rs.getString("priority")), rs.getInt("ticket_count")),
                region.name());
    }

    /** Number of tickets that were open on the date: opened on or before it and not closed by then. */
    public int countSupportTicketsOpenOnDate(LocalDate date) {
        return querySingle("SELECT COUNT(*) AS ticket_count FROM support_ticket "
                + "WHERE opened_date <= ? AND (closed_date IS NULL OR closed_date > ?)", rs -> rs.getInt("ticket_count"), date, date);
    }

    /** Number of tickets per current status; statuses without tickets are left out. */
    public List<TicketStatusCount> findSupportTicketCountsPerStatus() {
        return queryList("SELECT status, COUNT(*) AS ticket_count FROM support_ticket GROUP BY status ORDER BY status",
                rs -> new TicketStatusCount(TicketStatus.valueOf(rs.getString("status")), rs.getInt("ticket_count")));
    }

    /**
     * Per priority, the number of tickets closed between the two dates (inclusive) and the average days from opening to
     * closing, rounded to 2 decimals; priorities without such tickets are left out.
     */
    public List<TicketCloseTime> findSupportTicketCloseTimesPerPriority(LocalDate from, LocalDate to) {
        return queryList("SELECT priority, COUNT(*) AS closed_count, "
                        + "AVG(CAST(DATEDIFF('DAY', opened_date, closed_date) AS DECIMAL(12, 4))) AS average_days "
                        + "FROM support_ticket WHERE status = 'CLOSED' AND closed_date >= ? AND closed_date <= ? "
                        + "GROUP BY priority ORDER BY priority",
                rs -> new TicketCloseTime(TicketPriority.valueOf(rs.getString("priority")), rs.getInt("closed_count"),
                        rs.getBigDecimal("average_days").setScale(2, RoundingMode.HALF_UP)),
                from, to);
    }

    /** Tickets closed between the two dates (inclusive) with the days to close, by customer name, then priority, then closing date. */
    public List<ClosedTicket> findSupportTicketsClosedBetween(LocalDate from, LocalDate to) {
        return queryList("SELECT t.number, t.customer_id, c.name, t.priority, t.opened_date, t.closed_date, "
                        + "DATEDIFF('DAY', t.opened_date, t.closed_date) AS days_to_close "
                        + "FROM support_ticket t JOIN customer c ON c.id = t.customer_id "
                        + "WHERE t.status = 'CLOSED' AND t.closed_date >= ? AND t.closed_date <= ? "
                        + "ORDER BY c.name, t.customer_id, t.priority, t.closed_date, t.number",
                rs -> new ClosedTicket(rs.getString("number"), rs.getLong("customer_id"), rs.getString("name"),
                        TicketPriority.valueOf(rs.getString("priority")), rs.getObject("opened_date", LocalDate.class),
                        rs.getObject("closed_date", LocalDate.class), rs.getInt("days_to_close")),
                from, to);
    }

    /** Names the product a ticket is about. */
    public void updateSupportTicketProduct(long ticketId, Long productId) {
        update("UPDATE support_ticket SET product_id = ? WHERE id = ?", productId, ticketId);
    }

    /** Per product, the number of tickets opened between the two dates (inclusive); products without such tickets are left out. */
    public List<ProductTickets> findSupportTicketCountsPerProduct(LocalDate from, LocalDate to) {
        return queryList("SELECT p.id, p.sku, p.name, COUNT(*) AS ticket_count FROM support_ticket t JOIN product p ON p.id = t.product_id "
                        + "WHERE t.opened_date >= ? AND t.opened_date <= ? GROUP BY p.id, p.sku, p.name ORDER BY p.sku",
                rs -> new ProductTickets(rs.getLong("id"), rs.getString("sku"), rs.getString("name"), rs.getInt("ticket_count")),
                from, to);
    }

    // ===== Audit entries =====

    public AuditEntry createAuditEntry(AuditEntry entry) {
        long id = insert("INSERT INTO audit_log (user_name, change_date, entity_type, entity_id, description) VALUES (?, ?, ?, ?, ?)",
                entry.user(), entry.date(), entry.entityType().name(), entry.entityId(), entry.description());
        return new AuditEntry(id, entry.user(), entry.date(), entry.entityType(), entry.entityId(), entry.description());
    }

    /** The changes of the entity, newest first, then by highest id. */
    public List<AuditEntry> findAuditEntriesOfEntity(AuditEntityType entityType, long entityId) {
        return findAuditEntriesNewestFirst("entity_type = ? AND entity_id = ?", entityType.name(), entityId);
    }

    /**
     * The audit entries the WHERE condition keeps, newest first, then by highest id. The single ordering rule of all
     * audit entry lists.
     */
    private List<AuditEntry> findAuditEntriesNewestFirst(String condition, Object... params) {
        return queryList("SELECT id, user_name, change_date, entity_type, entity_id, description FROM audit_log "
                + "WHERE " + condition + " ORDER BY change_date DESC, id DESC", DataStore::auditEntryRow, params);
    }

    /** Columns: id, user_name, change_date, entity_type, entity_id, description. */
    private static AuditEntry auditEntryRow(ResultSet rs) throws SQLException {
        return new AuditEntry(rs.getLong("id"), rs.getString("user_name"), rs.getObject("change_date", LocalDate.class),
                AuditEntityType.valueOf(rs.getString("entity_type")), rs.getLong("entity_id"), rs.getString("description"));
    }

    /** The changes the user made between the two dates, inclusive, newest first, then by highest id. */
    public List<AuditEntry> findAuditEntriesByUserChangedBetween(String user, LocalDate from, LocalDate to) {
        return findAuditEntriesNewestFirst("user_name = ? AND change_date >= ? AND change_date <= ?", user, from, to);
    }

    /** The changes of entities of the type made between the two dates, inclusive, newest first, then by highest id. */
    public List<AuditEntry> findAuditEntriesOfEntityTypeChangedBetween(AuditEntityType entityType, LocalDate from, LocalDate to) {
        return findAuditEntriesNewestFirst("entity_type = ? AND change_date >= ? AND change_date <= ?",
                entityType.name(), from, to);
    }

    // ===== Cross-entity reports: record counts per quarter, weekday, sales rep and country; sales per region, channel, month and year over year =====

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
        return queryList(sql, rs -> new RegionSales(Region.valueOf(rs.getString("region")), rs.getInt("order_count"),
                rs.getBigDecimal("total")), from, to);
    }

    /**
     * Figures of the region for the period, inclusive: customers of the region, non-cancelled orders placed in the period
     * with their value, open invoices dated in the period with the amount of those due before the end date, shipments
     * shipped, returns made and not closed tickets opened in the period, all for customers of the region.
     */
    public RegionReport findRegionReport(Region region, LocalDate from, LocalDate to) {
        String regionName = region.name();
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
        return querySingle(sql, rs -> new RegionReport(region, from, to, rs.getInt("customer_count"), rs.getInt("order_count"),
                        rs.getBigDecimal("revenue"), rs.getInt("open_invoice_count"), rs.getBigDecimal("overdue_amount"),
                        rs.getInt("shipment_count"), rs.getInt("return_count"), rs.getInt("open_ticket_count")),
                regionName,
                regionName, from, to,
                regionName, from, to,
                regionName, from, to,
                regionName, from, to, to,
                regionName, from, to,
                regionName, from, to,
                regionName, from, to);
    }

    /**
     * Year-over-year measures of non-cancelled sales orders o (with their lines l): order_count and total of the month,
     * previous_order_count and previous_total of the same month of the previous year. Takes the parameters
     * yearOverYearParams(month) followed by the parameters of the FROM / WHERE part.
     */
    private static final String YEAR_OVER_YEAR_SALES_COLUMNS =
            "COUNT(DISTINCT CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN o.id END) AS order_count, "
            + "COALESCE(SUM(CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN l.quantity * l.unit_price END), 0) AS total, "
            + "COUNT(DISTINCT CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN o.id END) AS previous_order_count, "
            + "COALESCE(SUM(CASE WHEN o.order_date >= ? AND o.order_date <= ? THEN l.quantity * l.unit_price END), 0) AS previous_total ";

    /** The parameters of YEAR_OVER_YEAR_SALES_COLUMNS: the month's first and last day twice, then those of the previous year twice. */
    private static Object[] yearOverYearParams(YearMonth month) {
        YearMonth previous = month.minusYears(1);
        return new Object[] {month.atDay(1), month.atEndOfMonth(), month.atDay(1), month.atEndOfMonth(),
                previous.atDay(1), previous.atEndOfMonth(), previous.atDay(1), previous.atEndOfMonth()};
    }

    /** Columns of YEAR_OVER_YEAR_SALES_COLUMNS, for the given region (null for all regions). */
    private static YearOverYearSales yearOverYearSalesRow(Region region, ResultSet rs) throws SQLException {
        return new YearOverYearSales(region, rs.getInt("order_count"), rs.getBigDecimal("total"),
                rs.getInt("previous_order_count"), rs.getBigDecimal("previous_total"));
    }

    /**
     * Number and total value of the non-cancelled orders placed in the given month and in the same month of the
     * previous year, per region of the customer. Sorted by region name; regions without orders in either month are
     * left out.
     */
    public List<YearOverYearSales> findYearOverYearSalesPerRegion(YearMonth month) {
        String sql = "SELECT c.region AS region, " + YEAR_OVER_YEAR_SALES_COLUMNS
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.status <> 'CANCELLED' AND ((o.order_date >= ? AND o.order_date <= ?) OR (o.order_date >= ? AND o.order_date <= ?)) "
                + "GROUP BY c.region ORDER BY c.region";
        Object[] columnParams = yearOverYearParams(month);
        Object[] params = new Object[columnParams.length + 4];
        System.arraycopy(columnParams, 0, params, 0, columnParams.length);
        params[columnParams.length] = month.atDay(1);
        params[columnParams.length + 1] = month.atEndOfMonth();
        params[columnParams.length + 2] = month.minusYears(1).atDay(1);
        params[columnParams.length + 3] = month.minusYears(1).atEndOfMonth();
        return queryList(sql, rs -> yearOverYearSalesRow(Region.valueOf(rs.getString("region")), rs), params);
    }

    /**
     * Number and total value of the non-cancelled orders placed in the given month and in the same month of the
     * previous year, over all regions (orders are not joined to their customer). The region of the result is null;
     * months without orders give zeros.
     */
    public YearOverYearSales findYearOverYearSalesOverAllRegions(YearMonth month) {
        String sql = "SELECT " + YEAR_OVER_YEAR_SALES_COLUMNS
                + "FROM sales_order o LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "WHERE o.status <> 'CANCELLED'";
        return querySingle(sql, rs -> yearOverYearSalesRow(null, rs), yearOverYearParams(month));
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
        return queryList(sql, rs -> new RegionCustomers(Region.valueOf(rs.getString("region")), rs.getInt("active_count"),
                rs.getInt("on_hold_count"), rs.getInt("new_count"), rs.getInt("dormant_count")),
                newFrom, newTo, dormantFrom, dormantTo);
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
        return queryList(sql, rs -> new ChannelSales(SalesChannel.valueOf(rs.getString("channel")), rs.getInt("order_count"),
                rs.getBigDecimal("revenue")), from, to);
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
        return queryList(sql, rs -> new MonthlySales(rs.getInt("month_no"), rs.getInt("order_count"), rs.getBigDecimal("revenue"),
                rs.getInt("new_customers"), rs.getInt("return_count"), rs.getBigDecimal("credited")),
                year, year, year, year, year);
    }

    /**
     * The records counted by the per-quarter and per-weekday reports (findXCountPerQuarter, findXCountPerWeekday):
     * which rows count in the total, which rows count in a region (and by which region column), the date that puts a
     * record into a quarter or weekday, and which records are not counted at all. Records whose date is null are never
     * counted.
     */
    private enum RegionCountedRecords {
        /** Customers by created date, per region of the customer. */
        NEW_CUSTOMERS("customer c", "customer c", "c.region", "c.created_date", ""),
        /** Sales orders by order date, per region of the customer; cancelled orders are not counted. */
        SALES_ORDERS("sales_order o JOIN customer c ON c.id = o.customer_id",
                "sales_order o JOIN customer c ON c.id = o.customer_id", "c.region", "o.order_date",
                " AND o.status <> 'CANCELLED'"),
        /**
         * Purchase orders by order date, per region of the warehouse they are to be delivered to; cancelled orders are
         * not counted, orders without a warehouse count only in the total.
         */
        PURCHASE_ORDERS("purchase_order p", "purchase_order p JOIN warehouse w ON w.id = p.warehouse_id", "w.region",
                "p.order_date", " AND p.status <> 'CANCELLED'"),
        /** Shipments by ship date, per region of the customer of the order. */
        SHIPMENTS("shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id",
                "shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id", "c.region",
                "s.ship_date", ""),
        /** Sales returns by return date, per region of the customer of the order. */
        SALES_RETURNS("sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id",
                "sales_return r JOIN sales_order o ON o.id = r.order_id JOIN customer c ON c.id = o.customer_id", "c.region",
                "r.return_date", ""),
        /** Invoices by invoice date, per region of the customer; cancelled invoices are not counted. */
        INVOICES("invoice i JOIN customer c ON c.id = i.customer_id", "invoice i JOIN customer c ON c.id = i.customer_id",
                "c.region", "i.invoice_date", " AND i.status <> 'CANCELLED'");

        private final String totalFrom;
        private final String regionFrom;
        private final String regionColumn;
        private final String dateColumn;
        private final String exclusion;

        RegionCountedRecords(String totalFrom, String regionFrom, String regionColumn, String dateColumn, String exclusion) {
            this.totalFrom = totalFrom;
            this.regionFrom = regionFrom;
            this.regionColumn = regionColumn;
            this.dateColumn = dateColumn;
            this.exclusion = exclusion;
        }
    }

    /** Creates a per-quarter report row (e.g. a QuarterInvoices constructor). */
    @FunctionalInterface
    private interface QuarterCountsRow<T> {
        T of(int quarter, int total, Map<Region, Integer> perRegion);
    }

    /** Creates a per-weekday report row (e.g. a WeekdayInvoices constructor). */
    @FunctionalInterface
    private interface WeekdayCountsRow<T> {
        T of(DayOfWeek weekday, int total, Map<Region, Integer> perRegion);
    }

    /**
     * Select columns total_count and one &lt;region&gt;_count per region, counting the records whose date column
     * matches the period condition (in which "{date}" stands for the date column). Each column takes the parameters
     * of the period condition once.
     */
    private static String regionCountColumns(RegionCountedRecords counted, String periodCondition) {
        String where = periodCondition.replace("{date}", counted.dateColumn) + counted.exclusion;
        StringBuilder columns = new StringBuilder("(SELECT COUNT(*) FROM " + counted.totalFrom + " WHERE " + where + ") AS total_count");
        for (Region region : Region.values()) {
            columns.append(", (SELECT COUNT(*) FROM ").append(counted.regionFrom).append(" WHERE ").append(where)
                    .append(" AND ").append(counted.regionColumn).append(" = '").append(region.name()).append("') AS ")
                    .append(region.name().toLowerCase()).append("_count");
        }
        return columns.toString();
    }

    /** Reads the &lt;region&gt;_count columns of regionCountColumns; every region is present. */
    private static Map<Region, Integer> regionCounts(ResultSet rs) throws SQLException {
        Map<Region, Integer> perRegion = new EnumMap<>(Region.class);
        for (Region region : Region.values()) {
            perRegion.put(region, rs.getInt(region.name().toLowerCase() + "_count"));
        }
        return perRegion;
    }

    /**
     * For each of the four quarters of the year, in order: the number of counted records dated in the quarter, in
     * total and per region (see RegionCountedRecords). Quarters and regions without records are included with zeros.
     */
    private <T> List<T> findCountPerQuarter(RegionCountedRecords counted, int year, QuarterCountsRow<T> row) {
        String sql = "SELECT q.x AS quarter_no, "
                + regionCountColumns(counted, "YEAR({date}) = ? AND QUARTER({date}) = q.x")
                + " FROM SYSTEM_RANGE(1, 4) q ORDER BY q.x";
        Object[] params = new Object[1 + Region.values().length];
        for (int i = 0; i < params.length; i++) {
            params[i] = year;
        }
        return queryList(sql, rs -> row.of(rs.getInt("quarter_no"), rs.getInt("total_count"), regionCounts(rs)), params);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of counted records dated between the two dates,
     * inclusive, on that weekday, in total and per region (see RegionCountedRecords). Days and regions without records
     * are included with zeros.
     */
    private <T> List<T> findCountPerWeekday(RegionCountedRecords counted, LocalDate from, LocalDate to, WeekdayCountsRow<T> row) {
        String sql = "SELECT d.x AS day_no, "
                + regionCountColumns(counted, "{date} >= ? AND {date} <= ? AND ISO_DAY_OF_WEEK({date}) = d.x")
                + " FROM SYSTEM_RANGE(1, 7) d ORDER BY d.x";
        Object[] params = new Object[2 * (1 + Region.values().length)];
        for (int i = 0; i < params.length; i += 2) {
            params[i] = from;
            params[i + 1] = to;
        }
        return queryList(sql, rs -> row.of(DayOfWeek.of(rs.getInt("day_no")), rs.getInt("total_count"), regionCounts(rs)), params);
    }

    /**
     * For each of the four quarters of the year, in order: the number of customers created in the quarter, in total and
     * per region (customers without a created date are not counted). Quarters and regions without customers are
     * included with zeros.
     */
    public List<QuarterCustomers> findNewCustomerCountPerQuarter(int year) {
        return findCountPerQuarter(RegionCountedRecords.NEW_CUSTOMERS, year, QuarterCustomers::new);
    }

    /**
     * For each of the four quarters of the year, in order: the number of sales orders placed in the quarter, in total
     * and per region of the customer (cancelled orders are not counted). Quarters and regions without orders are
     * included with zeros.
     */
    public List<QuarterOrders> findSalesOrderCountPerQuarter(int year) {
        return findCountPerQuarter(RegionCountedRecords.SALES_ORDERS, year, QuarterOrders::new);
    }

    /**
     * For each of the four quarters of the year, in order: the number of purchase orders placed in the quarter, in total
     * and per region of the warehouse they are to be delivered to (cancelled orders are not counted; orders without a
     * warehouse count only in the total). Quarters and regions without orders are included with zeros.
     */
    public List<QuarterPurchaseOrders> findPurchaseOrderCountPerQuarter(int year) {
        return findCountPerQuarter(RegionCountedRecords.PURCHASE_ORDERS, year, QuarterPurchaseOrders::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of purchase orders placed between the two dates, inclusive,
     * in total and per region of the warehouse they are to be delivered to (cancelled orders are not counted; orders
     * without a warehouse count only in the total). Days and regions without orders are included with zeros.
     */
    public List<WeekdayPurchaseOrders> findPurchaseOrderCountPerWeekday(LocalDate from, LocalDate to) {
        return findCountPerWeekday(RegionCountedRecords.PURCHASE_ORDERS, from, to, WeekdayPurchaseOrders::new);
    }

    /**
     * For each of the four quarters of the year, in order: the number of shipments sent in the quarter, in total and per
     * region of the customer of the order. Quarters and regions without shipments are included with zeros.
     */
    public List<QuarterShipments> findShipmentCountPerQuarter(int year) {
        return findCountPerQuarter(RegionCountedRecords.SHIPMENTS, year, QuarterShipments::new);
    }

    /**
     * For each of the four quarters of the year, in order: the number of sales returns dated in the quarter, in total and
     * per region of the customer of the order. Quarters and regions without returns are included with zeros.
     */
    public List<QuarterReturns> findSalesReturnCountPerQuarter(int year) {
        return findCountPerQuarter(RegionCountedRecords.SALES_RETURNS, year, QuarterReturns::new);
    }

    /**
     * For each of the four quarters of the year, in order: the number of invoices issued in the quarter, in total and per
     * region of the customer (cancelled invoices are not counted). Quarters and regions without invoices are included
     * with zeros.
     */
    public List<QuarterInvoices> findInvoiceCountPerQuarter(int year) {
        return findCountPerQuarter(RegionCountedRecords.INVOICES, year, QuarterInvoices::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of customers created between the two dates, inclusive, in
     * total and per region (customers without a created date are not counted). Days and regions without customers are
     * included with zeros.
     */
    public List<WeekdayCustomers> findNewCustomerCountPerWeekday(LocalDate from, LocalDate to) {
        return findCountPerWeekday(RegionCountedRecords.NEW_CUSTOMERS, from, to, WeekdayCustomers::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of sales orders placed between the two dates, inclusive,
     * in total and per region of the customer (cancelled orders are not counted). Days and regions without orders are
     * included with zeros.
     */
    public List<WeekdayOrders> findSalesOrderCountPerWeekday(LocalDate from, LocalDate to) {
        return findCountPerWeekday(RegionCountedRecords.SALES_ORDERS, from, to, WeekdayOrders::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of shipments sent between the two dates, inclusive, in
     * total and per region of the customer of the order. Days and regions without shipments are included with zeros.
     */
    public List<WeekdayShipments> findShipmentCountPerWeekday(LocalDate from, LocalDate to) {
        return findCountPerWeekday(RegionCountedRecords.SHIPMENTS, from, to, WeekdayShipments::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of sales returns dated between the two dates, inclusive, in
     * total and per region of the customer of the order. Days and regions without returns are included with zeros.
     */
    public List<WeekdayReturns> findSalesReturnCountPerWeekday(LocalDate from, LocalDate to) {
        return findCountPerWeekday(RegionCountedRecords.SALES_RETURNS, from, to, WeekdayReturns::new);
    }

    /**
     * For each day of the week, Monday to Sunday: the number of invoices issued between the two dates, inclusive, in
     * total and per region of the customer (cancelled invoices are not counted). Days and regions without invoices are
     * included with zeros.
     */
    public List<WeekdayInvoices> findInvoiceCountPerWeekday(LocalDate from, LocalDate to) {
        return findCountPerWeekday(RegionCountedRecords.INVOICES, from, to, WeekdayInvoices::new);
    }

    /**
     * The records counted by the per-sales-rep and per-country count reports (findXCountPerSalesRep,
     * findXCountPerCountry): the rows that attribute a record to a customer c (whose sales rep and country count), the
     * date that puts a record into the period, and which records are not counted at all. Records whose date is null
     * are never counted; a record counts once per rep or country, however many of its customers are involved.
     */
    private enum CustomerAttributedRecords {
        /** Customers by created date (customers without a sales rep count for no rep). */
        NEW_CUSTOMERS("customer c", "c.id", "c.created_date", ""),
        /** Sales orders of the customer by order date; cancelled orders are not counted. */
        SALES_ORDERS("sales_order o JOIN customer c ON c.id = o.customer_id", "o.id", "o.order_date",
                " AND o.status <> 'CANCELLED'"),
        /** Invoices of the customer by invoice date; cancelled invoices are not counted. */
        INVOICES("invoice i JOIN customer c ON c.id = i.customer_id", "i.id", "i.invoice_date",
                " AND i.status <> 'CANCELLED'"),
        /** Shipments of the orders of the customer by ship date. */
        SHIPMENTS("shipment s JOIN sales_order o ON o.id = s.order_id JOIN customer c ON c.id = o.customer_id", "s.id",
                "s.ship_date", ""),
        /** Sales returns of the orders of the customer by return date. */
        SALES_RETURNS("sales_return sr JOIN sales_order o ON o.id = sr.order_id JOIN customer c ON c.id = o.customer_id",
                "sr.id", "sr.return_date", ""),
        /**
         * Purchase orders by order date, attributed to every customer in the region of the warehouse they are to be
         * delivered to (purchase orders have no customer of their own; orders without a warehouse are not counted).
         * Cancelled purchase orders are counted too.
         */
        PURCHASE_ORDERS("purchase_order po JOIN warehouse w ON w.id = po.warehouse_id JOIN customer c ON c.region = w.region",
                "po.id", "po.order_date", "");

        private final String from;
        private final String idColumn;
        private final String dateColumn;
        private final String exclusion;

        CustomerAttributedRecords(String from, String idColumn, String dateColumn, String exclusion) {
            this.from = from;
            this.idColumn = idColumn;
            this.dateColumn = dateColumn;
            this.exclusion = exclusion;
        }
    }

    /** Creates a per-sales-rep count row (e.g. a SalesRepInvoiceCount constructor). */
    @FunctionalInterface
    private interface SalesRepCountRow<T> {
        T of(long salesRepId, String name, int count);
    }

    /**
     * The number of counted records dated between the two dates, inclusive, attributed to the customers of each sales
     * rep (see CustomerAttributedRecords). Sorted by count, highest first, then by rep name, then id; reps without
     * records in the period are included with zero.
     */
    private <T> List<T> findCountPerSalesRep(CustomerAttributedRecords counted, LocalDate from, LocalDate to, SalesRepCountRow<T> row) {
        String sql = "SELECT r.id AS id, r.name AS name, "
                + "(SELECT COUNT(DISTINCT " + counted.idColumn + ") FROM " + counted.from + " WHERE c.sales_rep_id = r.id "
                + "AND " + counted.dateColumn + " >= ? AND " + counted.dateColumn + " <= ?" + counted.exclusion + ") AS record_count "
                + "FROM sales_rep r ORDER BY record_count DESC, r.name, r.id";
        return queryList(sql, rs -> row.of(rs.getLong("id"), rs.getString("name"), rs.getInt("record_count")), from, to);
    }

    /**
     * The number of customers created between the two dates, inclusive, per sales rep (customers without a created date
     * or without a sales rep are not counted). Sorted by customer count, highest first, then by rep name, then id; reps
     * without customers in the period are included with 0.
     */
    public List<SalesRepCustomerCount> findNewCustomerCountPerSalesRep(LocalDate from, LocalDate to) {
        return findCountPerSalesRep(CustomerAttributedRecords.NEW_CUSTOMERS, from, to, SalesRepCustomerCount::new);
    }

    /**
     * The number of sales orders placed between the two dates, inclusive, by the customers of each sales rep (cancelled
     * orders are not counted), highest first, then by name and id; reps without orders are included with zero.
     */
    public List<SalesRepOrderCount> findSalesOrderCountPerSalesRep(LocalDate from, LocalDate to) {
        return findCountPerSalesRep(CustomerAttributedRecords.SALES_ORDERS, from, to, SalesRepOrderCount::new);
    }

    /**
     * The number of invoices issued between the two dates, inclusive, to the customers of each sales rep (cancelled
     * invoices are not counted), highest first, then by name and id; reps without invoices are included with zero.
     */
    public List<SalesRepInvoiceCount> findInvoiceCountPerSalesRep(LocalDate from, LocalDate to) {
        return findCountPerSalesRep(CustomerAttributedRecords.INVOICES, from, to, SalesRepInvoiceCount::new);
    }

    /**
     * The number of shipments shipped between the two dates, inclusive, for the orders of the customers of each sales
     * rep, highest first, then by name and id; reps without shipments are included with zero.
     */
    public List<SalesRepShipmentCount> findShipmentCountPerSalesRep(LocalDate from, LocalDate to) {
        return findCountPerSalesRep(CustomerAttributedRecords.SHIPMENTS, from, to, SalesRepShipmentCount::new);
    }

    /**
     * The number of sales returns dated between the two dates, inclusive, for the orders of the customers of each sales
     * rep, highest first, then by name and id; reps without returns are included with zero.
     */
    public List<SalesRepReturnCount> findSalesReturnCountPerSalesRep(LocalDate from, LocalDate to) {
        return findCountPerSalesRep(CustomerAttributedRecords.SALES_RETURNS, from, to, SalesRepReturnCount::new);
    }

    /**
     * The number of purchase orders dated between the two dates, inclusive, for the warehouses in the regions of the
     * customers of each sales rep (purchase orders have no customer of their own), highest first, then by name and id;
     * reps without purchase orders are included with zero. A purchase order counts once per rep.
     */
    public List<SalesRepPurchaseOrderCount> findPurchaseOrderCountPerSalesRep(LocalDate from, LocalDate to) {
        return findCountPerSalesRep(CustomerAttributedRecords.PURCHASE_ORDERS, from, to, SalesRepPurchaseOrderCount::new);
    }

    /** Creates a per-country count row (e.g. a CountryInvoiceCount constructor). */
    @FunctionalInterface
    private interface CountryCountRow<T> {
        T of(String country, int count);
    }

    /**
     * The number of counted records dated between the two dates, inclusive, attributed to the customers of each
     * country (see CustomerAttributedRecords). Sorted by count, highest first, then by country; countries without
     * records in the period are left out.
     */
    private <T> List<T> findCountPerCountry(CustomerAttributedRecords counted, LocalDate from, LocalDate to, CountryCountRow<T> row) {
        String sql = "SELECT c.country AS country, COUNT(DISTINCT " + counted.idColumn + ") AS record_count FROM " + counted.from
                + " WHERE " + counted.dateColumn + " >= ? AND " + counted.dateColumn + " <= ?" + counted.exclusion
                + " GROUP BY c.country ORDER BY record_count DESC, c.country";
        return queryList(sql, rs -> row.of(rs.getString("country"), rs.getInt("record_count")), from, to);
    }

    /**
     * The number of purchase orders dated between the two dates, inclusive, for the warehouses in the regions of the
     * customers of each country, highest first, then by country; countries without purchase orders are left out. A
     * purchase order counts once per country.
     */
    public List<CountryPurchaseOrderCount> findPurchaseOrderCountPerCountry(LocalDate from, LocalDate to) {
        return findCountPerCountry(CustomerAttributedRecords.PURCHASE_ORDERS, from, to, CountryPurchaseOrderCount::new);
    }

    /**
     * The number of sales returns dated between the two dates, inclusive, for the orders of the customers of each
     * country (countries without returns in the period are left out). Sorted by return count, highest first, then by
     * country.
     */
    public List<CountryReturnCount> findSalesReturnCountPerCountry(LocalDate from, LocalDate to) {
        return findCountPerCountry(CustomerAttributedRecords.SALES_RETURNS, from, to, CountryReturnCount::new);
    }

    /**
     * The number of shipments shipped between the two dates, inclusive, for the orders of the customers of each
     * country (countries without shipments in the period are left out). Sorted by shipment count, highest first, then
     * by country.
     */
    public List<CountryShipmentCount> findShipmentCountPerCountry(LocalDate from, LocalDate to) {
        return findCountPerCountry(CustomerAttributedRecords.SHIPMENTS, from, to, CountryShipmentCount::new);
    }

    /**
     * The number of sales orders placed between the two dates, inclusive, by the customers of each country (cancelled
     * orders are not counted; countries without orders in the period are left out). Sorted by order count, highest
     * first, then by country.
     */
    public List<CountryOrderCount> findSalesOrderCountPerCountry(LocalDate from, LocalDate to) {
        return findCountPerCountry(CustomerAttributedRecords.SALES_ORDERS, from, to, CountryOrderCount::new);
    }

    /**
     * The number of invoices issued between the two dates, inclusive, to the customers of each country (cancelled
     * invoices are not counted; countries without invoices in the period are left out). Sorted by invoice count,
     * highest first, then by country.
     */
    public List<CountryInvoiceCount> findInvoiceCountPerCountry(LocalDate from, LocalDate to) {
        return findCountPerCountry(CustomerAttributedRecords.INVOICES, from, to, CountryInvoiceCount::new);
    }

    /**
     * The number of customers created between the two dates, inclusive, per country (customers without a created date
     * are not counted; countries without customers in the period are left out). Sorted by customer count, highest
     * first, then by country.
     */
    public List<CountryCustomerCount> findNewCustomerCountPerCountry(LocalDate from, LocalDate to) {
        return findCountPerCountry(CustomerAttributedRecords.NEW_CUSTOMERS, from, to, CountryCustomerCount::new);
    }
}