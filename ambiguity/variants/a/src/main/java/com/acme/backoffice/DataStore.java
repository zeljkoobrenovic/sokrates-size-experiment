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

    public Customer createCustomer(Customer customer) {
        String sql = "INSERT INTO customer (name, email, phone, country, region, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.name());
            statement.setString(2, customer.email());
            statement.setString(3, customer.phone());
            statement.setString(4, customer.country());
            statement.setString(5, customer.region().name());
            statement.setString(6, customer.status().name());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Customer(keys.getLong(1), customer.name(), customer.email(), customer.phone(),
                        customer.country(), customer.region(), customer.status());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create customer", e);
        }
    }

    public void updateCustomer(Customer customer) {
        String sql = "UPDATE customer SET name = ?, email = ?, phone = ?, country = ?, region = ?, status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.name());
            statement.setString(2, customer.email());
            statement.setString(3, customer.phone());
            statement.setString(4, customer.country());
            statement.setString(5, customer.region().name());
            statement.setString(6, customer.status().name());
            statement.setLong(7, customer.id());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update customer", e);
        }
    }

    /** Sets the credit limit of the customer; null removes the limit. */
    public void updateCustomerCreditLimit(long customerId, BigDecimal creditLimit) {
        String sql = "UPDATE customer SET credit_limit = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, creditLimit);
            statement.setLong(2, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update customer credit limit", e);
        }
    }

    /** Sets the date the customer was created; null removes it. */
    public void updateCustomerCreatedDate(long customerId, LocalDate createdDate) {
        String sql = "UPDATE customer SET created_date = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, createdDate);
            statement.setLong(2, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update customer created date", e);
        }
    }

    /** The credit limit of the customer; empty if the customer has no limit or does not exist. */
    public Optional<BigDecimal> findCustomerCreditLimit(long customerId) {
        String sql = "SELECT credit_limit FROM customer WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rs.getBigDecimal("credit_limit"));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer credit limit", e);
        }
    }

    public Optional<Customer> findCustomerById(long id) {
        String sql = "SELECT id, name, email, phone, country, region, status FROM customer WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                            rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                            CustomerStatus.valueOf(rs.getString("status"))));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer", e);
        }
    }

    public List<Customer> listCustomersByName() {
        String sql = "SELECT id, name, email, phone, country, region, status FROM customer WHERE NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = customer.id) ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                        CustomerStatus.valueOf(rs.getString("status"))));
            }
            return customers;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list customers", e);
        }
    }

    /** Closes the customer without deleting anything; returns false if it was already closed. */
    public boolean closeCustomer(long customerId, LocalDate closedDate) {
        String sql = "INSERT INTO customer_closure (customer_id, closed_date) SELECT c.id, ? FROM customer c "
                + "WHERE c.id = ? AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = c.id)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, closedDate);
            statement.setLong(2, customerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot close customer", e);
        }
    }

    /** Reopens a closed customer; returns false if it was not closed. */
    public boolean reopenCustomer(long customerId) {
        String sql = "DELETE FROM customer_closure WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot reopen customer", e);
        }
    }

    /** The closed customers, ordered by name. */
    public List<Customer> listClosedCustomers() {
        String sql = "SELECT c.id, c.name, c.email, c.phone, c.country, c.region, c.status FROM customer c "
                + "JOIN customer_closure cl ON cl.customer_id = c.id ORDER BY c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("phone"), rs.getString("country"), Region.valueOf(rs.getString("region")),
                        CustomerStatus.valueOf(rs.getString("status"))));
            }
            return customers;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list closed customers", e);
        }
    }

    public List<Customer> searchCustomersByName(String namePart) {
        String sql = "SELECT id, name, email, phone, country, region, status FROM customer WHERE LOWER(name) LIKE ? ESCAPE '\\' AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = customer.id) ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String escaped = namePart.toLowerCase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            statement.setString(1, "%" + escaped + "%");
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
            throw new IllegalStateException("Cannot search customers by name", e);
        }
    }

    public List<Customer> searchCustomersByRegion(Region region) {
        String sql = "SELECT id, name, email, phone, country, region, status FROM customer WHERE region = ? AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = customer.id) ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
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
            throw new IllegalStateException("Cannot search customers by region", e);
        }
    }

    public List<Customer> searchCustomersByCountry(String country) {
        String sql = "SELECT id, name, email, phone, country, region, status FROM customer WHERE country = ? AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = customer.id) ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, country);
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
            throw new IllegalStateException("Cannot search customers by country", e);
        }
    }

    public List<Customer> searchCustomersByStatus(CustomerStatus status) {
        String sql = "SELECT id, name, email, phone, country, region, status FROM customer WHERE status = ? AND NOT EXISTS (SELECT 1 FROM customer_closure cl WHERE cl.customer_id = customer.id) ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
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
            throw new IllegalStateException("Cannot search customers by status", e);
        }
    }

    public Product createProduct(Product product) {
        String sql = "INSERT INTO product (sku, name, category, unit_price, unit_of_measure, active) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.sku());
            statement.setString(2, product.name());
            statement.setString(3, product.category());
            statement.setBigDecimal(4, product.unitPrice());
            statement.setString(5, product.unitOfMeasure());
            statement.setBoolean(6, product.active());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Product(keys.getLong(1), product.sku(), product.name(), product.category(),
                        product.unitPrice(), product.unitOfMeasure(), product.active());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create product", e);
        }
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE product SET sku = ?, name = ?, category = ?, unit_price = ?, unit_of_measure = ?, active = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.sku());
            statement.setString(2, product.name());
            statement.setString(3, product.category());
            statement.setBigDecimal(4, product.unitPrice());
            statement.setString(5, product.unitOfMeasure());
            statement.setBoolean(6, product.active());
            statement.setLong(7, product.id());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update product", e);
        }
    }

    /** Sets the catalogue price and records the change (old and new price) in the history, all or nothing. Returns null if the price is unchanged. */
    /** Deactivates or reactivates the product; returns false if it does not exist or already has that state. */
    public boolean setProductActive(long productId, boolean active) {
        String sql = "UPDATE product SET active = ? WHERE id = ? AND active <> ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, active);
            statement.setLong(2, productId);
            statement.setBoolean(3, active);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot change product active state", e);
        }
    }

    public PriceChange changeProductPrice(long productId, LocalDate date, BigDecimal newPrice) {
        String selectSql = "SELECT unit_price FROM product WHERE id = ?";
        String updateSql = "UPDATE product SET unit_price = ? WHERE id = ?";
        String insertSql = "INSERT INTO product_price_change (product_id, change_date, old_price, new_price) VALUES (?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            BigDecimal before;
            try (PreparedStatement select = connection.prepareStatement(selectSql)) {
                select.setLong(1, productId);
                try (ResultSet rs = select.executeQuery()) {
                    if (!rs.next()) {
                        connection.rollback();
                        throw new IllegalStateException("Unknown product: " + productId);
                    }
                    before = rs.getBigDecimal("unit_price");
                }
            }
            if (before.compareTo(newPrice) == 0) {
                connection.commit();
                return null;
            }
            try (PreparedStatement update = connection.prepareStatement(updateSql)) {
                update.setBigDecimal(1, newPrice);
                update.setLong(2, productId);
                update.executeUpdate();
            }
            long id;
            try (PreparedStatement statement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, productId);
                statement.setObject(2, date);
                statement.setBigDecimal(3, before);
                statement.setBigDecimal(4, newPrice);
                statement.executeUpdate();
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getLong(1);
                }
            }
            connection.commit();
            return new PriceChange(id, productId, date, before, newPrice);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException("Cannot change product price", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    /** The price changes of the product, oldest first (then by id). */
    public List<PriceChange> findPriceChangesByProduct(long productId) {
        String sql = "SELECT id, product_id, change_date, old_price, new_price FROM product_price_change "
                + "WHERE product_id = ? ORDER BY change_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            try (ResultSet rs = statement.executeQuery()) {
                List<PriceChange> changes = new ArrayList<>();
                while (rs.next()) {
                    changes.add(new PriceChange(rs.getLong("id"), rs.getLong("product_id"),
                            rs.getObject("change_date", LocalDate.class), rs.getBigDecimal("old_price"),
                            rs.getBigDecimal("new_price")));
                }
                return changes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find price changes by product", e);
        }
    }

    /** The products with a price change between the two dates, inclusive, by name (then by id). */
    public List<Product> findProductsWithPriceChange(LocalDate from, LocalDate to) {
        String sql = "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product p WHERE EXISTS "
                + "(SELECT 1 FROM product_price_change c WHERE c.product_id = p.id AND c.change_date >= ? AND c.change_date <= ?) "
                + "ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find products with price change", e);
        }
    }

    public void saveProductWeight(long productId, BigDecimal weightKg) {
        String sql = "MERGE INTO product_weight (product_id, weight_kg) KEY (product_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setBigDecimal(2, weightKg);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save product weight", e);
        }
    }

    public Optional<Product> findProductBySku(String sku) {
        String sql = "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product WHERE sku = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sku);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find product", e);
        }
    }

    public Optional<Product> findProductById(long id) {
        String sql = "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find product", e);
        }
    }

    public List<Product> listActiveProductsByName() {
        String sql = "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product WHERE active = TRUE ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                        rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                        rs.getBoolean("active")));
            }
            return products;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list active products", e);
        }
    }

    public List<Product> searchProductsByName(String namePart) {
        String sql = "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product WHERE active = TRUE AND LOWER(name) LIKE ? ESCAPE '\\' ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String escaped = namePart.toLowerCase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            statement.setString(1, "%" + escaped + "%");
            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot search products by name", e);
        }
    }

    public List<Product> searchProductsByCategory(String category) {
        String sql = "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product WHERE active = TRUE AND category = ? ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot search products by category", e);
        }
    }

    public List<Product> searchProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product WHERE active = TRUE AND unit_price >= ? AND unit_price <= ? ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, minPrice);
            statement.setBigDecimal(2, maxPrice);
            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot search products by price range", e);
        }
    }

    public Supplier createSupplier(Supplier supplier) {
        String sql = "INSERT INTO supplier (name, contact_email, country, rating) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, supplier.name());
            statement.setString(2, supplier.contactEmail());
            statement.setString(3, supplier.country());
            statement.setInt(4, supplier.rating());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Supplier(keys.getLong(1), supplier.name(), supplier.contactEmail(), supplier.country(),
                        supplier.rating());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create supplier", e);
        }
    }

    public void updateSupplier(Supplier supplier) {
        String sql = "UPDATE supplier SET name = ?, contact_email = ?, country = ?, rating = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.name());
            statement.setString(2, supplier.contactEmail());
            statement.setString(3, supplier.country());
            statement.setInt(4, supplier.rating());
            statement.setLong(5, supplier.id());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update supplier", e);
        }
    }

    public List<Supplier> listSuppliersByName() {
        String sql = "SELECT id, name, contact_email, country, rating FROM supplier ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            List<Supplier> suppliers = new ArrayList<>();
            while (rs.next()) {
                suppliers.add(new Supplier(rs.getLong("id"), rs.getString("name"), rs.getString("contact_email"),
                        rs.getString("country"), rs.getInt("rating")));
            }
            return suppliers;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list suppliers", e);
        }
    }

    public List<Supplier> listSuppliersByCountry(String country) {
        String sql = "SELECT id, name, contact_email, country, rating FROM supplier WHERE country = ? ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, country);
            try (ResultSet rs = statement.executeQuery()) {
                List<Supplier> suppliers = new ArrayList<>();
                while (rs.next()) {
                    suppliers.add(new Supplier(rs.getLong("id"), rs.getString("name"), rs.getString("contact_email"),
                            rs.getString("country"), rs.getInt("rating")));
                }
                return suppliers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list suppliers by country", e);
        }
    }

    public List<Supplier> listSuppliersByMinimumRating(int minimumRating) {
        String sql = "SELECT id, name, contact_email, country, rating FROM supplier WHERE rating >= ? ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, minimumRating);
            try (ResultSet rs = statement.executeQuery()) {
                List<Supplier> suppliers = new ArrayList<>();
                while (rs.next()) {
                    suppliers.add(new Supplier(rs.getLong("id"), rs.getString("name"), rs.getString("contact_email"),
                            rs.getString("country"), rs.getInt("rating")));
                }
                return suppliers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list suppliers by rating", e);
        }
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        String sql = "INSERT INTO warehouse (code, name, city, region) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, warehouse.code());
            statement.setString(2, warehouse.name());
            statement.setString(3, warehouse.city());
            statement.setString(4, warehouse.region().name());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Warehouse(keys.getLong(1), warehouse.code(), warehouse.name(), warehouse.city(),
                        warehouse.region());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create warehouse", e);
        }
    }

    public List<Warehouse> listWarehousesByCode() {
        String sql = "SELECT id, code, name, city, region FROM warehouse ORDER BY code, id";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            List<Warehouse> warehouses = new ArrayList<>();
            while (rs.next()) {
                warehouses.add(new Warehouse(rs.getLong("id"), rs.getString("code"), rs.getString("name"),
                        rs.getString("city"), Region.valueOf(rs.getString("region"))));
            }
            return warehouses;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list warehouses", e);
        }
    }

    public Optional<Warehouse> findWarehouseByCode(String code) {
        String sql = "SELECT id, code, name, city, region FROM warehouse WHERE code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Warehouse(rs.getLong("id"), rs.getString("code"), rs.getString("name"),
                            rs.getString("city"), Region.valueOf(rs.getString("region"))));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find warehouse", e);
        }
    }

    public void setStockQuantity(long warehouseId, long productId, int quantity) {
        String sql = "MERGE INTO stock (warehouse_id, product_id, quantity) KEY (warehouse_id, product_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setLong(2, productId);
            statement.setInt(3, quantity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot set stock quantity", e);
        }
    }

    public void addStockQuantity(long warehouseId, long productId, int amount) {
        String updateSql = "UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement update = connection.prepareStatement(updateSql)) {
            update.setInt(1, amount);
            update.setLong(2, warehouseId);
            update.setLong(3, productId);
            if (update.executeUpdate() == 0) {
                try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
                    insert.setLong(1, warehouseId);
                    insert.setLong(2, productId);
                    insert.setInt(3, amount);
                    insert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot add stock quantity", e);
        }
    }

    /** Takes the amount from the stock; returns false, changing nothing, if there is less than that. */
    public boolean removeStockQuantity(long warehouseId, long productId, int amount) {
        String sql = "UPDATE stock SET quantity = quantity - ? WHERE warehouse_id = ? AND product_id = ? AND quantity >= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, amount);
            statement.setLong(2, warehouseId);
            statement.setLong(3, productId);
            statement.setInt(4, amount);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot remove stock quantity", e);
        }
    }

    /** Logs a stock change (positive when the stock grows, negative when it shrinks). */
    public void createStockMovement(long productId, long warehouseId, LocalDate movementDate, int quantityChange,
                                    StockMovementReason reason) {
        String sql = "INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setLong(2, warehouseId);
            statement.setObject(3, movementDate);
            statement.setInt(4, quantityChange);
            statement.setString(5, reason.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create stock movement", e);
        }
    }

    /** The stock movements of the product in all warehouses between the two dates, inclusive, oldest first (then by id). */
    public List<StockMovement> findStockMovementsByProduct(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, product_id, warehouse_id, movement_date, quantity_change, reason FROM stock_movement "
                + "WHERE product_id = ? AND movement_date >= ? AND movement_date <= ? ORDER BY movement_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<StockMovement> movements = new ArrayList<>();
                while (rs.next()) {
                    movements.add(new StockMovement(rs.getLong("id"), rs.getLong("product_id"), rs.getLong("warehouse_id"),
                            rs.getObject("movement_date", LocalDate.class), rs.getInt("quantity_change"),
                            StockMovementReason.valueOf(rs.getString("reason"))));
                }
                return movements;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock movements by product", e);
        }
    }

    /** The stock movements of all products in the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<StockMovement> findStockMovementsByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, product_id, warehouse_id, movement_date, quantity_change, reason FROM stock_movement "
                + "WHERE warehouse_id = ? AND movement_date >= ? AND movement_date <= ? ORDER BY movement_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<StockMovement> movements = new ArrayList<>();
                while (rs.next()) {
                    movements.add(new StockMovement(rs.getLong("id"), rs.getLong("product_id"), rs.getLong("warehouse_id"),
                            rs.getObject("movement_date", LocalDate.class), rs.getInt("quantity_change"),
                            StockMovementReason.valueOf(rs.getString("reason"))));
                }
                return movements;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock movements by warehouse", e);
        }
    }

    /** The stock movements of the product in the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<StockMovement> findStockMovementsByProductAndWarehouse(long productId, long warehouseId, LocalDate from,
                                                                       LocalDate to) {
        String sql = "SELECT id, product_id, warehouse_id, movement_date, quantity_change, reason FROM stock_movement "
                + "WHERE product_id = ? AND warehouse_id = ? AND movement_date >= ? AND movement_date <= ? "
                + "ORDER BY movement_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setLong(2, warehouseId);
            statement.setObject(3, from);
            statement.setObject(4, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<StockMovement> movements = new ArrayList<>();
                while (rs.next()) {
                    movements.add(new StockMovement(rs.getLong("id"), rs.getLong("product_id"), rs.getLong("warehouse_id"),
                            rs.getObject("movement_date", LocalDate.class), rs.getInt("quantity_change"),
                            StockMovementReason.valueOf(rs.getString("reason"))));
                }
                return movements;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock movements by product and warehouse", e);
        }
    }

    /**
     * The stock movements of the products of the category between the two dates, inclusive, per warehouse. Warehouses
     * are sorted by code, movements oldest first (then by id); warehouses without movements are left out.
     */
    public List<WarehouseMovements> findStockMovementsByCategoryPerWarehouse(String category, LocalDate from,
                                                                             LocalDate to) {
        String sql = "SELECT w.id AS warehouse_id, w.code, w.name, w.city, w.region, m.id, m.product_id, "
                + "m.movement_date, m.quantity_change, m.reason "
                + "FROM stock_movement m JOIN product p ON p.id = m.product_id JOIN warehouse w ON w.id = m.warehouse_id "
                + "WHERE p.category = ? AND m.movement_date >= ? AND m.movement_date <= ? "
                + "ORDER BY w.code, w.id, m.movement_date, m.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseMovements> result = new ArrayList<>();
                Warehouse current = null;
                List<StockMovement> movements = null;
                while (rs.next()) {
                    long warehouseId = rs.getLong("warehouse_id");
                    if (current == null || current.id() != warehouseId) {
                        current = new Warehouse(warehouseId, rs.getString("code"), rs.getString("name"),
                                rs.getString("city"), Region.valueOf(rs.getString("region")));
                        movements = new ArrayList<>();
                        result.add(new WarehouseMovements(current, movements));
                    }
                    movements.add(new StockMovement(rs.getLong("id"), rs.getLong("product_id"), warehouseId,
                            rs.getObject("movement_date", LocalDate.class), rs.getInt("quantity_change"),
                            StockMovementReason.valueOf(rs.getString("reason"))));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock movements by category per warehouse", e);
        }
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
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ReasonMovements> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new ReasonMovements(StockMovementReason.valueOf(rs.getString("reason")),
                            new Warehouse(rs.getLong("id"), rs.getString("code"), rs.getString("name"),
                                    rs.getString("city"), Region.valueOf(rs.getString("region"))),
                            rs.getString("category"), rs.getLong("movement_count"), rs.getLong("quantity_change")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock movements per reason, warehouse and category", e);
        }
    }

    /** Sets the stock to the counted quantity and logs the adjustment and its difference as a movement, all or nothing. */
    public StockAdjustment createStockAdjustment(StockAdjustment adjustment) {
        String selectSql = "SELECT quantity FROM stock WHERE warehouse_id = ? AND product_id = ?";
        String stockSql = "MERGE INTO stock (warehouse_id, product_id, quantity) KEY (warehouse_id, product_id) VALUES (?, ?, ?)";
        String adjustmentSql = "INSERT INTO stock_adjustment (product_id, warehouse_id, adjustment_date, quantity_before, "
                + "counted_quantity, reason) VALUES (?, ?, ?, ?, ?, ?)";
        String movementSql = "INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                + "VALUES (?, ?, ?, ?, 'ADJUSTMENT')";
        try {
            connection.setAutoCommit(false);
            int before = 0;
            try (PreparedStatement select = connection.prepareStatement(selectSql)) {
                select.setLong(1, adjustment.warehouseId());
                select.setLong(2, adjustment.productId());
                try (ResultSet rs = select.executeQuery()) {
                    if (rs.next()) {
                        before = rs.getInt("quantity");
                    }
                }
            }
            try (PreparedStatement stock = connection.prepareStatement(stockSql)) {
                stock.setLong(1, adjustment.warehouseId());
                stock.setLong(2, adjustment.productId());
                stock.setInt(3, adjustment.countedQuantity());
                stock.executeUpdate();
            }
            long id;
            try (PreparedStatement statement = connection.prepareStatement(adjustmentSql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, adjustment.productId());
                statement.setLong(2, adjustment.warehouseId());
                statement.setObject(3, adjustment.adjustmentDate());
                statement.setInt(4, before);
                statement.setInt(5, adjustment.countedQuantity());
                statement.setString(6, adjustment.reason());
                statement.executeUpdate();
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getLong(1);
                }
            }
            if (adjustment.countedQuantity() != before) {
                try (PreparedStatement movement = connection.prepareStatement(movementSql)) {
                    movement.setLong(1, adjustment.productId());
                    movement.setLong(2, adjustment.warehouseId());
                    movement.setObject(3, adjustment.adjustmentDate());
                    movement.setInt(4, adjustment.countedQuantity() - before);
                    movement.executeUpdate();
                }
            }
            connection.commit();
            return new StockAdjustment(id, adjustment.productId(), adjustment.warehouseId(), adjustment.adjustmentDate(),
                    before, adjustment.countedQuantity(), adjustment.reason());
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException("Cannot create stock adjustment", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    /** The stock adjustments in the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<StockAdjustment> findStockAdjustmentsByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, product_id, warehouse_id, adjustment_date, quantity_before, counted_quantity, reason "
                + "FROM stock_adjustment WHERE warehouse_id = ? AND adjustment_date >= ? AND adjustment_date <= ? "
                + "ORDER BY adjustment_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<StockAdjustment> adjustments = new ArrayList<>();
                while (rs.next()) {
                    adjustments.add(new StockAdjustment(rs.getLong("id"), rs.getLong("product_id"),
                            rs.getLong("warehouse_id"), rs.getObject("adjustment_date", LocalDate.class),
                            rs.getInt("quantity_before"), rs.getInt("counted_quantity"), rs.getString("reason")));
                }
                return adjustments;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock adjustments by warehouse", e);
        }
    }

    /**
     * The stock adjustments that lowered the stock between the two dates, inclusive, per warehouse. Warehouses are
     * sorted by code, adjustments by the size of the loss, biggest first (then oldest, then by id); warehouses without
     * negative adjustments are left out.
     */
    public List<WarehouseAdjustments> findNegativeStockAdjustmentsPerWarehouse(LocalDate from, LocalDate to) {
        String sql = "SELECT w.id AS warehouse_id, w.code, w.name, w.city, w.region, a.id, a.product_id, "
                + "a.adjustment_date, a.quantity_before, a.counted_quantity, a.reason "
                + "FROM stock_adjustment a JOIN warehouse w ON w.id = a.warehouse_id "
                + "WHERE a.counted_quantity < a.quantity_before AND a.adjustment_date >= ? AND a.adjustment_date <= ? "
                + "ORDER BY w.code, w.id, a.counted_quantity - a.quantity_before, a.adjustment_date, a.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseAdjustments> result = new ArrayList<>();
                Warehouse current = null;
                List<StockAdjustment> adjustments = null;
                while (rs.next()) {
                    long warehouseId = rs.getLong("warehouse_id");
                    if (current == null || current.id() != warehouseId) {
                        current = new Warehouse(warehouseId, rs.getString("code"), rs.getString("name"),
                                rs.getString("city"), Region.valueOf(rs.getString("region")));
                        adjustments = new ArrayList<>();
                        result.add(new WarehouseAdjustments(current, adjustments));
                    }
                    adjustments.add(new StockAdjustment(rs.getLong("id"), rs.getLong("product_id"), warehouseId,
                            rs.getObject("adjustment_date", LocalDate.class), rs.getInt("quantity_before"),
                            rs.getInt("counted_quantity"), rs.getString("reason")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find negative stock adjustments per warehouse", e);
        }
    }

    /** Moves the stock between the warehouses and logs both movements of every line, all or nothing (fails if the stock is short). */
    public WarehouseTransfer createWarehouseTransfer(WarehouseTransfer transfer) {
        String transferSql = "INSERT INTO warehouse_transfer (number, from_warehouse_id, to_warehouse_id, transfer_date) "
                + "VALUES (?, ?, ?, ?)";
        String lineSql = "INSERT INTO warehouse_transfer_line (transfer_id, line_no, product_id, quantity) VALUES (?, ?, ?, ?)";
        String takeSql = "UPDATE stock SET quantity = quantity - ? WHERE warehouse_id = ? AND product_id = ? AND quantity >= ?";
        String addSql = "UPDATE stock SET quantity = quantity + ? WHERE warehouse_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO stock (warehouse_id, product_id, quantity) VALUES (?, ?, ?)";
        String movementSql = "INSERT INTO stock_movement (product_id, warehouse_id, movement_date, quantity_change, reason) "
                + "VALUES (?, ?, ?, ?, 'TRANSFER')";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(transferSql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, transfer.number());
                statement.setLong(2, transfer.fromWarehouseId());
                statement.setLong(3, transfer.toWarehouseId());
                statement.setObject(4, transfer.transferDate());
                statement.executeUpdate();
                long id;
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getLong(1);
                }
                int lineNo = 1;
                for (WarehouseTransferLine line : transfer.lines()) {
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        lineStatement.setInt(2, lineNo++);
                        lineStatement.setLong(3, line.productId());
                        lineStatement.setInt(4, line.quantity());
                        lineStatement.executeUpdate();
                    }
                    try (PreparedStatement take = connection.prepareStatement(takeSql)) {
                        take.setInt(1, line.quantity());
                        take.setLong(2, transfer.fromWarehouseId());
                        take.setLong(3, line.productId());
                        take.setInt(4, line.quantity());
                        if (take.executeUpdate() == 0) {
                            throw new SQLException("Not enough stock of product " + line.productId() + " in warehouse "
                                    + transfer.fromWarehouseId());
                        }
                    }
                    try (PreparedStatement add = connection.prepareStatement(addSql)) {
                        add.setInt(1, line.quantity());
                        add.setLong(2, transfer.toWarehouseId());
                        add.setLong(3, line.productId());
                        if (add.executeUpdate() == 0) {
                            try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
                                insert.setLong(1, transfer.toWarehouseId());
                                insert.setLong(2, line.productId());
                                insert.setInt(3, line.quantity());
                                insert.executeUpdate();
                            }
                        }
                    }
                    try (PreparedStatement movement = connection.prepareStatement(movementSql)) {
                        movement.setLong(1, line.productId());
                        movement.setLong(2, transfer.fromWarehouseId());
                        movement.setObject(3, transfer.transferDate());
                        movement.setInt(4, -line.quantity());
                        movement.executeUpdate();
                        movement.setLong(1, line.productId());
                        movement.setLong(2, transfer.toWarehouseId());
                        movement.setObject(3, transfer.transferDate());
                        movement.setInt(4, line.quantity());
                        movement.executeUpdate();
                    }
                }
                connection.commit();
                return new WarehouseTransfer(id, transfer.number(), transfer.fromWarehouseId(), transfer.toWarehouseId(),
                        transfer.transferDate(), List.copyOf(transfer.lines()));
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException("Cannot create warehouse transfer", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    /** The transfers from or to the warehouse between the two dates, inclusive, oldest first (then by id). */
    public List<WarehouseTransfer> findWarehouseTransfersByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, from_warehouse_id, to_warehouse_id, transfer_date FROM warehouse_transfer "
                + "WHERE (from_warehouse_id = ? OR to_warehouse_id = ?) AND transfer_date >= ? AND transfer_date <= ? "
                + "ORDER BY transfer_date, id";
        String lineSql = "SELECT product_id, quantity FROM warehouse_transfer_line WHERE transfer_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setLong(2, warehouseId);
            statement.setObject(3, from);
            statement.setObject(4, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseTransfer> transfers = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<WarehouseTransferLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new WarehouseTransferLine(lineRs.getLong("product_id"), lineRs.getInt("quantity")));
                            }
                        }
                    }
                    transfers.add(new WarehouseTransfer(id, rs.getString("number"), rs.getLong("from_warehouse_id"),
                            rs.getLong("to_warehouse_id"), rs.getObject("transfer_date", LocalDate.class), lines));
                }
                return transfers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find warehouse transfers by warehouse", e);
        }
    }

    /** The transfers between the two dates, inclusive, oldest first (then by id). */
    public List<WarehouseTransfer> findWarehouseTransfersByPeriod(LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, from_warehouse_id, to_warehouse_id, transfer_date FROM warehouse_transfer "
                + "WHERE transfer_date >= ? AND transfer_date <= ? ORDER BY transfer_date, id";
        String lineSql = "SELECT product_id, quantity FROM warehouse_transfer_line WHERE transfer_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseTransfer> transfers = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<WarehouseTransferLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new WarehouseTransferLine(lineRs.getLong("product_id"), lineRs.getInt("quantity")));
                            }
                        }
                    }
                    transfers.add(new WarehouseTransfer(id, rs.getString("number"), rs.getLong("from_warehouse_id"),
                            rs.getLong("to_warehouse_id"), rs.getObject("transfer_date", LocalDate.class), lines));
                }
                return transfers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find warehouse transfers by period", e);
        }
    }

    /** The transfers containing the product between the two dates, inclusive, oldest first (then by id). */
    public List<WarehouseTransfer> findWarehouseTransfersByProduct(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, from_warehouse_id, to_warehouse_id, transfer_date FROM warehouse_transfer "
                + "WHERE id IN (SELECT transfer_id FROM warehouse_transfer_line WHERE product_id = ?) "
                + "AND transfer_date >= ? AND transfer_date <= ? ORDER BY transfer_date, id";
        String lineSql = "SELECT product_id, quantity FROM warehouse_transfer_line WHERE transfer_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseTransfer> transfers = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    List<WarehouseTransferLine> lines = new ArrayList<>();
                    try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                        lineStatement.setLong(1, id);
                        try (ResultSet lineRs = lineStatement.executeQuery()) {
                            while (lineRs.next()) {
                                lines.add(new WarehouseTransferLine(lineRs.getLong("product_id"), lineRs.getInt("quantity")));
                            }
                        }
                    }
                    transfers.add(new WarehouseTransfer(id, rs.getString("number"), rs.getLong("from_warehouse_id"),
                            rs.getLong("to_warehouse_id"), rs.getObject("transfer_date", LocalDate.class), lines));
                }
                return transfers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find warehouse transfers by product", e);
        }
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
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<TransferRoute> routes = new ArrayList<>();
                while (rs.next()) {
                    routes.add(new TransferRoute(rs.getLong("from_warehouse_id"), rs.getString("from_code"),
                            rs.getLong("to_warehouse_id"), rs.getString("to_code"), rs.getLong("total")));
                }
                return routes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find transferred quantity per route", e);
        }
    }

    public void createBundleComponent(long bundleId, long componentId, int quantity) {
        String sql = "INSERT INTO bundle_component (bundle_id, component_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bundleId);
            statement.setLong(2, componentId);
            statement.setInt(3, quantity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create bundle component", e);
        }
    }

    /** The components of the bundle with the quantity needed for one bundle, ordered by product name; empty if the product is no bundle. */
    public List<BundleComponent> listBundleComponents(long bundleId) {
        String sql = "SELECT p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, c.quantity "
                + "FROM bundle_component c JOIN product p ON p.id = c.component_id WHERE c.bundle_id = ? ORDER BY p.name, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bundleId);
            try (ResultSet rs = statement.executeQuery()) {
                List<BundleComponent> components = new ArrayList<>();
                while (rs.next()) {
                    components.add(new BundleComponent(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")), rs.getInt("quantity")));
                }
                return components;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list bundle components", e);
        }
    }

    /** The products that have components, ordered by name. */
    public List<Product> listBundles() {
        String sql = "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product "
                + "WHERE id IN (SELECT bundle_id FROM bundle_component) ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list bundles", e);
        }
    }

    /** The bundles that contain the product as a component, ordered by name. */
    public List<Product> listBundlesByComponent(long componentId) {
        String sql = "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product "
                + "WHERE id IN (SELECT bundle_id FROM bundle_component WHERE component_id = ?) ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, componentId);
            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list bundles by component", e);
        }
    }

    /** How many bundles can be built from the stock of one warehouse; 0 if the product has no components. */
    public int findBundleQuantity(long warehouseId, long bundleId) {
        String sql = "SELECT COALESCE(MIN(COALESCE(s.quantity, 0) / c.quantity), 0) AS buildable FROM bundle_component c "
                + "LEFT JOIN stock s ON s.product_id = c.component_id AND s.warehouse_id = ? WHERE c.bundle_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setLong(2, bundleId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt("buildable");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find bundle quantity", e);
        }
    }

    /** How many bundles can be built in total, the sum over the warehouses of what each can build from its own stock. */
    public int findTotalBundleQuantity(long bundleId) {
        String sql = "SELECT COALESCE(SUM(buildable), 0) AS total FROM ("
                + "SELECT MIN(COALESCE(s.quantity, 0) / c.quantity) AS buildable FROM warehouse w CROSS JOIN bundle_component c "
                + "LEFT JOIN stock s ON s.product_id = c.component_id AND s.warehouse_id = w.id WHERE c.bundle_id = ? GROUP BY w.id)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bundleId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find total bundle quantity", e);
        }
    }

    public int findStockQuantity(long warehouseId, long productId) {
        String sql = "SELECT quantity FROM stock WHERE warehouse_id = ? AND product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setLong(2, productId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt("quantity") : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock quantity", e);
        }
    }

    public int findTotalStockQuantity(long productId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) AS total FROM stock WHERE product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find total stock quantity", e);
        }
    }

    public List<StockLevel> listStockByWarehouse(long warehouseId) {
        String sql = "SELECT p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, s.quantity "
                + "FROM stock s JOIN product p ON p.id = s.product_id WHERE s.warehouse_id = ? ORDER BY p.name, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            try (ResultSet rs = statement.executeQuery()) {
                List<StockLevel> levels = new ArrayList<>();
                while (rs.next()) {
                    levels.add(new StockLevel(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")), rs.getInt("quantity")));
                }
                return levels;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list stock by warehouse", e);
        }
    }

    public List<StockLevel> listTotalStockBelow(int threshold) {
        String sql = "SELECT p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, "
                + "COALESCE(SUM(s.quantity), 0) AS total "
                + "FROM product p LEFT JOIN stock s ON s.product_id = p.id WHERE p.active = TRUE "
                + "GROUP BY p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active "
                + "HAVING COALESCE(SUM(s.quantity), 0) < ? ORDER BY p.name, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, threshold);
            try (ResultSet rs = statement.executeQuery()) {
                List<StockLevel> levels = new ArrayList<>();
                while (rs.next()) {
                    levels.add(new StockLevel(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")), rs.getInt("total")));
                }
                return levels;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list products with low stock", e);
        }
    }

    public List<WarehouseStockValue> listStockValueByWarehouse() {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, COALESCE(SUM(s.quantity * COALESCE("
                + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
                + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)), 0) AS stock_value "
                + "FROM warehouse w LEFT JOIN stock s ON s.warehouse_id = w.id "
                + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<WarehouseStockValue> values = new ArrayList<>();
            while (rs.next()) {
                values.add(new WarehouseStockValue(new Warehouse(rs.getLong("id"), rs.getString("code"),
                        rs.getString("name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                        rs.getBigDecimal("stock_value").setScale(2)));
            }
            return values;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list stock value by warehouse", e);
        }
    }

    /** The stock value over all warehouses, valued like listStockValueByWarehouse. */
    public BigDecimal findTotalStockValue() {
        String sql = "SELECT COALESCE(SUM(s.quantity * COALESCE("
                + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
                + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)), 0) AS stock_value "
                + "FROM stock s";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getBigDecimal("stock_value").setScale(2);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find total stock value", e);
        }
    }

    /** The stock quantity and value per category and warehouse, for the combinations with stock, valued like listStockValueByWarehouse. */
    public List<CategoryWarehouseStock> findStockPerCategoryAndWarehouse() {
        String sql = "SELECT p.category, w.id, w.code, w.name, w.city, w.region, SUM(s.quantity) AS quantity, "
                + "COALESCE(SUM(s.quantity * COALESCE("
                + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
                + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)), 0) AS stock_value "
                + "FROM stock s JOIN product p ON p.id = s.product_id JOIN warehouse w ON w.id = s.warehouse_id "
                + "WHERE s.quantity > 0 "
                + "GROUP BY p.category, w.id, w.code, w.name, w.city, w.region ORDER BY p.category, w.code, w.id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<CategoryWarehouseStock> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new CategoryWarehouseStock(rs.getString("category"), new Warehouse(rs.getLong("id"),
                        rs.getString("code"), rs.getString("name"), rs.getString("city"),
                        Region.valueOf(rs.getString("region"))), rs.getLong("quantity"),
                        rs.getBigDecimal("stock_value").setScale(2)));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock per category and warehouse", e);
        }
    }

    /** The stock quantity and value per category over all warehouses, for the categories with stock, valued like listStockValueByWarehouse. */
    public List<CategoryStock> findStockPerCategory() {
        String sql = "SELECT p.category, SUM(s.quantity) AS quantity, "
                + "COALESCE(SUM(s.quantity * COALESCE("
                + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
                + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)), 0) AS stock_value "
                + "FROM stock s JOIN product p ON p.id = s.product_id "
                + "WHERE s.quantity > 0 "
                + "GROUP BY p.category ORDER BY p.category";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<CategoryStock> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new CategoryStock(rs.getString("category"), rs.getLong("quantity"),
                        rs.getBigDecimal("stock_value").setScale(2)));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock per category", e);
        }
    }

    /** Every warehouse and product category (of any product) without stock in that warehouse, by warehouse code then category. */
    public List<WarehouseCategory> findCategoriesWithoutStockPerWarehouse() {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, c.category FROM warehouse w "
                + "CROSS JOIN (SELECT DISTINCT category FROM product) c "
                + "WHERE NOT EXISTS (SELECT 1 FROM stock s JOIN product p ON p.id = s.product_id "
                + "WHERE s.warehouse_id = w.id AND p.category = c.category AND s.quantity > 0) "
                + "ORDER BY w.code, w.id, c.category";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<WarehouseCategory> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new WarehouseCategory(new Warehouse(rs.getLong("id"), rs.getString("code"),
                        rs.getString("name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                        rs.getString("category")));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find categories without stock per warehouse", e);
        }
    }

    /**
     * The products with stock in a warehouse but not shipped from that warehouse (whole or partial shipments) on any day from
     * the given number of days before the date up to the day before it, by warehouse code then stock value, highest first;
     * valued like listStockValueByWarehouse.
     */
    public List<WarehouseSlowMovingStock> findSlowMovingStockPerWarehouse(LocalDate date, int days) {
        String sql = "SELECT w.id AS warehouse_id, w.code, w.name, w.city, w.region, p.id, p.sku, p.name AS product_name, p.category, "
                + "p.unit_price, p.unit_of_measure, p.active, s.quantity, "
                + "s.quantity * COALESCE("
                + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
                + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0) AS stock_value "
                + "FROM stock s JOIN product p ON p.id = s.product_id JOIN warehouse w ON w.id = s.warehouse_id "
                + "WHERE s.quantity > 0 "
                + "AND NOT EXISTS (SELECT 1 FROM shipment h JOIN sales_order_line ol ON ol.order_id = h.order_id "
                + "WHERE h.warehouse_id = s.warehouse_id AND ol.product_id = s.product_id AND h.ship_date >= ? AND h.ship_date < ?) "
                + "AND NOT EXISTS (SELECT 1 FROM shipment_part sp JOIN sales_order_line ol ON ol.order_id = sp.order_id AND ol.line_no = sp.line_no "
                + "WHERE sp.warehouse_id = s.warehouse_id AND ol.product_id = s.product_id AND sp.ship_date >= ? AND sp.ship_date < ?) "
                + "ORDER BY w.code, w.id, stock_value DESC, p.sku, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date.minusDays(days));
            statement.setObject(2, date);
            statement.setObject(3, date.minusDays(days));
            statement.setObject(4, date);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseSlowMovingStock> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new WarehouseSlowMovingStock(new Warehouse(rs.getLong("warehouse_id"), rs.getString("code"),
                            rs.getString("name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                            new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("product_name"),
                                    rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                                    rs.getBoolean("active")),
                            rs.getLong("quantity"), rs.getBigDecimal("stock_value").setScale(2)));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find slow-moving stock per warehouse", e);
        }
    }

    /**
     * The products with stock that were not shipped from any warehouse (whole or partial shipments) on any day from the
     * given number of days before the date up to the day before it, with the stock over all warehouses, by stock value,
     * highest first; valued like listStockValueByWarehouse.
     */
    public List<SlowMovingStock> findSlowMovingStock(LocalDate date, int days) {
        String sql = "SELECT p.id, p.sku, p.name AS product_name, p.category, p.unit_price, p.unit_of_measure, p.active, "
                + "SUM(s.quantity) AS quantity, "
                + "COALESCE(SUM(s.quantity * COALESCE("
                + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
                + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)), 0) AS stock_value "
                + "FROM stock s JOIN product p ON p.id = s.product_id "
                + "WHERE s.quantity > 0 "
                + "AND NOT EXISTS (SELECT 1 FROM shipment h JOIN sales_order_line ol ON ol.order_id = h.order_id "
                + "WHERE ol.product_id = s.product_id AND h.ship_date >= ? AND h.ship_date < ?) "
                + "AND NOT EXISTS (SELECT 1 FROM shipment_part sp JOIN sales_order_line ol ON ol.order_id = sp.order_id AND ol.line_no = sp.line_no "
                + "WHERE ol.product_id = s.product_id AND sp.ship_date >= ? AND sp.ship_date < ?) "
                + "GROUP BY p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active "
                + "ORDER BY stock_value DESC, p.sku, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date.minusDays(days));
            statement.setObject(2, date);
            statement.setObject(3, date.minusDays(days));
            statement.setObject(4, date);
            try (ResultSet rs = statement.executeQuery()) {
                List<SlowMovingStock> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new SlowMovingStock(new Product(rs.getLong("id"), rs.getString("sku"),
                            rs.getString("product_name"), rs.getString("category"), rs.getBigDecimal("unit_price"),
                            rs.getString("unit_of_measure"), rs.getBoolean("active")),
                            rs.getLong("quantity"), rs.getBigDecimal("stock_value").setScale(2)));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find slow-moving stock", e);
        }
    }

    public void setReorderLevel(long warehouseId, long productId, int level) {
        String sql = "MERGE INTO reorder_level (warehouse_id, product_id, level) KEY (warehouse_id, product_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setLong(2, productId);
            statement.setInt(3, level);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot set reorder level", e);
        }
    }

    public List<LowStockAlert> listLowStockByWarehouse(long warehouseId) {
        String sql = "SELECT w.id AS warehouse_id, w.code, w.name AS warehouse_name, w.city, w.region, "
                + "p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, "
                + "COALESCE(s.quantity, 0) AS quantity, r.level "
                + "FROM reorder_level r JOIN warehouse w ON w.id = r.warehouse_id JOIN product p ON p.id = r.product_id "
                + "LEFT JOIN stock s ON s.warehouse_id = r.warehouse_id AND s.product_id = r.product_id "
                + "WHERE p.active = TRUE AND r.warehouse_id = ? AND COALESCE(s.quantity, 0) <= r.level ORDER BY p.name, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            try (ResultSet rs = statement.executeQuery()) {
                List<LowStockAlert> alerts = new ArrayList<>();
                while (rs.next()) {
                    alerts.add(new LowStockAlert(new Warehouse(rs.getLong("warehouse_id"), rs.getString("code"),
                            rs.getString("warehouse_name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                            new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                                    rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                                    rs.getBoolean("active")), rs.getInt("quantity"), rs.getInt("level")));
                }
                return alerts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list low stock by warehouse", e);
        }
    }

    /**
     * Per product of the warehouse with stock at or below its reorder level (no stock counts as zero), the quantity needed to
     * reach twice the reorder level and the supplier with the lowest last unit cost (ties: shorter lead time, then lower
     * supplier id). Products that need nothing are left out; products without supplier have none. Sorted by product name.
     */
    public List<PurchaseSuggestion> listPurchaseSuggestionsByWarehouse(long warehouseId) {
        String sql = "SELECT w.id AS warehouse_id, w.code, w.name AS warehouse_name, w.city, w.region, "
                + "p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, "
                + "COALESCE(s.quantity, 0) AS quantity, r.level, "
                + "su.id AS supplier_id, su.name AS supplier_name, su.contact_email, su.country, su.rating, sp.last_unit_cost "
                + "FROM reorder_level r JOIN warehouse w ON w.id = r.warehouse_id JOIN product p ON p.id = r.product_id "
                + "LEFT JOIN stock s ON s.warehouse_id = r.warehouse_id AND s.product_id = r.product_id "
                + "LEFT JOIN supplier_product sp ON sp.id = (SELECT sp2.id FROM supplier_product sp2 "
                + "WHERE sp2.product_id = p.id ORDER BY sp2.last_unit_cost, sp2.lead_time_days, sp2.supplier_id LIMIT 1) "
                + "LEFT JOIN supplier su ON su.id = sp.supplier_id "
                + "WHERE p.active = TRUE AND r.warehouse_id = ? AND COALESCE(s.quantity, 0) <= r.level AND 2 * r.level - COALESCE(s.quantity, 0) > 0 "
                + "ORDER BY p.name, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            try (ResultSet rs = statement.executeQuery()) {
                List<PurchaseSuggestion> suggestions = new ArrayList<>();
                while (rs.next()) {
                    Supplier supplier = null;
                    if (rs.getObject("supplier_id") != null) {
                        supplier = new Supplier(rs.getLong("supplier_id"), rs.getString("supplier_name"),
                                rs.getString("contact_email"), rs.getString("country"), rs.getInt("rating"));
                    }
                    int quantity = rs.getInt("quantity");
                    int level = rs.getInt("level");
                    suggestions.add(new PurchaseSuggestion(new Warehouse(rs.getLong("warehouse_id"), rs.getString("code"),
                            rs.getString("warehouse_name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                            new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                                    rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                                    rs.getBoolean("active")), quantity, level, 2 * level - quantity, supplier,
                            rs.getBigDecimal("last_unit_cost")));
                }
                return suggestions;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list purchase suggestions by warehouse", e);
        }
    }

    public List<LowStockAlert> listLowStock() {
        String sql = "SELECT w.id AS warehouse_id, w.code, w.name AS warehouse_name, w.city, w.region, "
                + "p.id, p.sku, p.name, p.category, p.unit_price, p.unit_of_measure, p.active, "
                + "COALESCE(s.quantity, 0) AS quantity, r.level "
                + "FROM reorder_level r JOIN warehouse w ON w.id = r.warehouse_id JOIN product p ON p.id = r.product_id "
                + "LEFT JOIN stock s ON s.warehouse_id = r.warehouse_id AND s.product_id = r.product_id "
                + "WHERE p.active = TRUE AND COALESCE(s.quantity, 0) <= r.level ORDER BY w.code, p.name, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<LowStockAlert> alerts = new ArrayList<>();
            while (rs.next()) {
                alerts.add(new LowStockAlert(new Warehouse(rs.getLong("warehouse_id"), rs.getString("code"),
                        rs.getString("warehouse_name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                        new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                                rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                                rs.getBoolean("active")), rs.getInt("quantity"), rs.getInt("level")));
            }
            return alerts;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list low stock", e);
        }
    }

    public SalesOrder createSalesOrder(SalesOrder order) {
        String orderSql = "INSERT INTO sales_order (number, customer_id, order_date, status) VALUES (?, ?, ?, ?)";
        String lineSql = "INSERT INTO sales_order_line (order_id, line_no, product_id, quantity, unit_price, promotion_code) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, order.number());
                statement.setLong(2, order.customerId());
                statement.setObject(3, order.orderDate());
                statement.setString(4, order.status().name());
                statement.executeUpdate();
                long id;
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getLong(1);
                }
                try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                    int lineNo = 1;
                    for (OrderLine line : order.lines()) {
                        lineStatement.setLong(1, id);
                        lineStatement.setInt(2, lineNo++);
                        lineStatement.setLong(3, line.productId());
                        lineStatement.setInt(4, line.quantity());
                        lineStatement.setBigDecimal(5, line.unitPrice());
                        lineStatement.setString(6, line.promotionCode());
                        lineStatement.executeUpdate();
                    }
                }
                connection.commit();
                return new SalesOrder(id, order.number(), order.customerId(), order.orderDate(), order.status(),
                        List.copyOf(order.lines()));
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException("Cannot create sales order", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    public Optional<SalesOrder> findSalesOrderByNumber(String number) {
        String sql = "SELECT id, number, customer_id, order_date, status FROM sales_order WHERE number = ?";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
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
                return Optional.of(new SalesOrder(id, rs.getString("number"), rs.getLong("customer_id"),
                        rs.getObject("order_date", LocalDate.class), OrderStatus.valueOf(rs.getString("status")), lines));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales order", e);
        }
    }

    public List<SalesOrder> listSalesOrdersByCustomer(long customerId) {
        String sql = "SELECT id, number, customer_id, order_date, status FROM sales_order WHERE customer_id = ? "
                + "AND NOT EXISTS (SELECT 1 FROM order_archive ar WHERE ar.order_id = sales_order.id) "
                + "ORDER BY order_date DESC, id DESC";
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
            throw new IllegalStateException("Cannot list sales orders", e);
        }
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder order) {
        String orderSql = "INSERT INTO purchase_order (number, supplier_id, order_date, expected_date, status, warehouse_id) VALUES (?, ?, ?, ?, ?, ?)";
        String lineSql = "INSERT INTO purchase_order_line (order_id, line_no, product_id, quantity, unit_cost) VALUES (?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, order.number());
                statement.setLong(2, order.supplierId());
                statement.setObject(3, order.orderDate());
                statement.setObject(4, order.expectedDate());
                statement.setString(5, order.status().name());
                if (order.warehouseId() == 0) {
                    statement.setNull(6, java.sql.Types.BIGINT);
                } else {
                    statement.setLong(6, order.warehouseId());
                }
                statement.executeUpdate();
                long id;
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    id = keys.getLong(1);
                }
                try (PreparedStatement lineStatement = connection.prepareStatement(lineSql)) {
                    int lineNo = 1;
                    for (PurchaseOrderLine line : order.lines()) {
                        lineStatement.setLong(1, id);
                        lineStatement.setInt(2, lineNo++);
                        lineStatement.setLong(3, line.productId());
                        lineStatement.setInt(4, line.quantity());
                        lineStatement.setBigDecimal(5, line.unitCost());
                        lineStatement.executeUpdate();
                    }
                }
                connection.commit();
                return new PurchaseOrder(id, order.number(), order.supplierId(), order.orderDate(), order.expectedDate(),
                        order.status(), List.copyOf(order.lines()), order.warehouseId());
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
                // the original failure is the one to report
            }
            throw new IllegalStateException("Cannot create purchase order", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                // nothing more to do
            }
        }
    }

    public Optional<PurchaseOrder> findPurchaseOrderByNumber(String number) {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order WHERE number = ?";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
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
                return Optional.of(new PurchaseOrder(id, rs.getString("number"), rs.getLong("supplier_id"),
                        rs.getObject("order_date", LocalDate.class), rs.getObject("expected_date", LocalDate.class),
                        PurchaseOrderStatus.valueOf(rs.getString("status")), lines, rs.getLong("warehouse_id")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find purchase order", e);
        }
    }

    public List<PurchaseOrder> listPurchaseOrdersBySupplier(long supplierId) {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order WHERE supplier_id = ? "
                + "ORDER BY order_date DESC, id DESC";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, supplierId);
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
            throw new IllegalStateException("Cannot list purchase orders", e);
        }
    }

    public List<PurchaseOrder> listOpenPurchaseOrders() {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order WHERE status = 'SENT' "
                + "ORDER BY expected_date, id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
            throw new IllegalStateException("Cannot list open purchase orders", e);
        }
    }

    public List<PurchaseOrder> listOpenPurchaseOrdersBySupplier(long supplierId) {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order "
                + "WHERE status = 'SENT' AND supplier_id = ? ORDER BY expected_date, id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, supplierId);
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
            throw new IllegalStateException("Cannot list open purchase orders by supplier", e);
        }
    }

    /**
     * The purchase orders matching all the given criteria, earliest expected date first (then by id). Every criterion is
     * optional: a null supplier id, warehouse id, status, from date or to date is not applied. The dates are inclusive
     * and apply to the expected date.
     */
    public List<PurchaseOrder> searchPurchaseOrders(Long supplierId, Long warehouseId, PurchaseOrderStatus status,
                                                    LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order "
                + "WHERE (CAST(? AS BIGINT) IS NULL OR supplier_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS BIGINT) IS NULL OR warehouse_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS VARCHAR) IS NULL OR status = CAST(? AS VARCHAR)) "
                + "AND (CAST(? AS DATE) IS NULL OR expected_date >= CAST(? AS DATE)) "
                + "AND (CAST(? AS DATE) IS NULL OR expected_date <= CAST(? AS DATE)) "
                + "ORDER BY expected_date, id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String statusName = status == null ? null : status.name();
            statement.setObject(1, supplierId);
            statement.setObject(2, supplierId);
            statement.setObject(3, warehouseId);
            statement.setObject(4, warehouseId);
            statement.setObject(5, statusName);
            statement.setObject(6, statusName);
            statement.setObject(7, from);
            statement.setObject(8, from);
            statement.setObject(9, to);
            statement.setObject(10, to);
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
            throw new IllegalStateException("Cannot search purchase orders", e);
        }
    }

    /** Sent purchase orders whose expected date is before the given date. */
    public List<PurchaseOrder> listOverduePurchaseOrders(LocalDate date) {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order "
                + "WHERE status = 'SENT' AND expected_date < ? ORDER BY expected_date, id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
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
            throw new IllegalStateException("Cannot list overdue purchase orders", e);
        }
    }

    public List<PurchaseOrder> listOpenPurchaseOrdersByWarehouse(long warehouseId) {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order "
                + "WHERE status = 'SENT' AND warehouse_id = ? ORDER BY expected_date, id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
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
            throw new IllegalStateException("Cannot list open purchase orders by warehouse", e);
        }
    }

    /** Sent purchase orders into the warehouse whose expected date is before the given date. */
    public List<PurchaseOrder> listOverduePurchaseOrdersByWarehouse(long warehouseId, LocalDate date) {
        String sql = "SELECT id, number, supplier_id, order_date, expected_date, status, warehouse_id FROM purchase_order "
                + "WHERE status = 'SENT' AND warehouse_id = ? AND expected_date < ? ORDER BY expected_date, id";
        String lineSql = "SELECT product_id, quantity, unit_cost FROM purchase_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
            statement.setObject(2, date);
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
            throw new IllegalStateException("Cannot list overdue purchase orders by warehouse", e);
        }
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

    /**
     * The unpaid part of the open invoices per customer in aging buckets on the given date: not due (due date on or
     * after the date), 1-30, 31-60, 61-90 and more than 90 days overdue. Only invoices dated and payments made up to
     * the date count. Customers without such invoices are left out, ordered by customer name.
     */
    public List<InvoiceAging> findInvoiceAging(LocalDate asOf) {
        String sql = "SELECT c.id AS id, c.name AS name, "
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
                + "GROUP BY c.id, c.name ORDER BY c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
            statement.setObject(2, asOf);
            statement.setObject(3, asOf);
            try (ResultSet rs = statement.executeQuery()) {
                List<InvoiceAging> aging = new ArrayList<>();
                while (rs.next()) {
                    aging.add(new InvoiceAging(rs.getLong("id"), rs.getString("name"), rs.getBigDecimal("not_due"),
                            rs.getBigDecimal("days_1_30"), rs.getBigDecimal("days_31_60"),
                            rs.getBigDecimal("days_61_90"), rs.getBigDecimal("over_90")));
                }
                return aging;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find invoice aging", e);
        }
    }

    public void updateInvoiceStatus(long invoiceId, InvoiceStatus status) {
        String sql = "UPDATE invoice SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setLong(2, invoiceId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update invoice status", e);
        }
    }

    public Shipment createShipment(Shipment shipment) {
        String sql = "INSERT INTO shipment (order_id, warehouse_id, carrier_id, tracking_number, ship_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, shipment.orderId());
            statement.setLong(2, shipment.warehouseId());
            statement.setLong(3, shipment.carrierId());
            statement.setString(4, shipment.trackingNumber());
            statement.setObject(5, shipment.shipDate());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Shipment(keys.getLong(1), shipment.orderId(), shipment.warehouseId(), shipment.carrierId(),
                        shipment.trackingNumber(), shipment.shipDate());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create shipment", e);
        }
    }

    public Optional<Shipment> findShipmentByOrder(long orderId) {
        String sql = "SELECT id, order_id, warehouse_id, carrier_id, tracking_number, ship_date, delivery_date FROM shipment WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Shipment(rs.getLong("id"), rs.getLong("order_id"), rs.getLong("warehouse_id"),
                            rs.getLong("carrier_id"), rs.getString("tracking_number"),
                            rs.getObject("ship_date", LocalDate.class), rs.getObject("delivery_date", LocalDate.class)));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find shipment", e);
        }
    }

    public List<Shipment> listShipmentsByWarehouseAndPeriod(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, order_id, warehouse_id, carrier_id, tracking_number, ship_date, delivery_date FROM shipment "
                + "WHERE warehouse_id = ? AND ship_date BETWEEN ? AND ? ORDER BY ship_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
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
            throw new IllegalStateException("Cannot list shipments", e);
        }
    }

    public boolean updateShipmentDeliveryDate(long orderId, LocalDate deliveryDate) {
        String sql = "UPDATE shipment SET delivery_date = ? WHERE order_id = ? AND delivery_date IS NULL";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, deliveryDate);
            statement.setLong(2, orderId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update shipment delivery date", e);
        }
    }

    public List<Shipment> listShipmentsInTransit() {
        String sql = "SELECT id, order_id, warehouse_id, carrier_id, tracking_number, ship_date, delivery_date FROM shipment "
                + "WHERE delivery_date IS NULL ORDER BY ship_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
            throw new IllegalStateException("Cannot list shipments in transit", e);
        }
    }

    public List<Shipment> listShipmentsInTransitByCarrier(long carrierId) {
        String sql = "SELECT id, order_id, warehouse_id, carrier_id, tracking_number, ship_date, delivery_date FROM shipment "
                + "WHERE delivery_date IS NULL AND carrier_id = ? ORDER BY ship_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, carrierId);
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
            throw new IllegalStateException("Cannot list shipments in transit", e);
        }
    }

    public List<Shipment> listShipmentsInTransitShippedBefore(LocalDate shippedBefore) {
        String sql = "SELECT id, order_id, warehouse_id, carrier_id, tracking_number, ship_date, delivery_date FROM shipment "
                + "WHERE delivery_date IS NULL AND ship_date < ? ORDER BY ship_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, shippedBefore);
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
            throw new IllegalStateException("Cannot list shipments in transit", e);
        }
    }

    /** Shipments of the customer's orders shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> listShipmentsByCustomerAndPeriod(long customerId, LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "JOIN sales_order o ON o.id = s.order_id "
                + "WHERE o.customer_id = ? AND s.ship_date BETWEEN ? AND ? ORDER BY s.ship_date, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
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
            throw new IllegalStateException("Cannot list shipments of customer", e);
        }
    }

    /**
     * The shipments matching all the given criteria, oldest ship date first (then by id). Every criterion is optional:
     * a null carrier id, warehouse id, customer id, status, from date or to date is not applied. The dates are inclusive
     * and apply to the ship date.
     */
    public List<Shipment> searchShipments(Long carrierId, Long warehouseId, Long customerId, ShipmentStatus status,
                                          LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "JOIN sales_order o ON o.id = s.order_id "
                + "WHERE (CAST(? AS BIGINT) IS NULL OR s.carrier_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS BIGINT) IS NULL OR s.warehouse_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS BIGINT) IS NULL OR o.customer_id = CAST(? AS BIGINT)) "
                + "AND (CAST(? AS VARCHAR) IS NULL OR (CAST(? AS VARCHAR) = 'IN_TRANSIT' AND s.delivery_date IS NULL) "
                + "OR (CAST(? AS VARCHAR) = 'DELIVERED' AND s.delivery_date IS NOT NULL)) "
                + "AND (CAST(? AS DATE) IS NULL OR s.ship_date >= CAST(? AS DATE)) "
                + "AND (CAST(? AS DATE) IS NULL OR s.ship_date <= CAST(? AS DATE)) "
                + "ORDER BY s.ship_date, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String statusName = status == null ? null : status.name();
            statement.setObject(1, carrierId);
            statement.setObject(2, carrierId);
            statement.setObject(3, warehouseId);
            statement.setObject(4, warehouseId);
            statement.setObject(5, customerId);
            statement.setObject(6, customerId);
            statement.setObject(7, statusName);
            statement.setObject(8, statusName);
            statement.setObject(9, statusName);
            statement.setObject(10, from);
            statement.setObject(11, from);
            statement.setObject(12, to);
            statement.setObject(13, to);
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
            throw new IllegalStateException("Cannot search shipments", e);
        }
    }

    /** Shipments of the customer's orders shipped and not yet delivered, oldest first. */
    public List<Shipment> listShipmentsInTransitByCustomer(long customerId) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "JOIN sales_order o ON o.id = s.order_id "
                + "WHERE o.customer_id = ? AND s.delivery_date IS NULL ORDER BY s.ship_date, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
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
            throw new IllegalStateException("Cannot list shipments in transit of customer", e);
        }
    }

    /** Delivered shipments of the customer's orders that took more days than the promised days of the carrier, oldest first. */
    public List<Shipment> listLateShipmentsByCustomer(long customerId) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "JOIN sales_order o ON o.id = s.order_id "
                + "JOIN carrier c ON c.id = s.carrier_id "
                + "WHERE o.customer_id = ? AND s.delivery_date IS NOT NULL "
                + "AND DATEDIFF('DAY', s.ship_date, s.delivery_date) > c.promised_days ORDER BY s.ship_date, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
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
            throw new IllegalStateException("Cannot list late shipments of customer", e);
        }
    }

    /** Shipments to customers of the region shipped between the two dates, inclusive, and not yet delivered, oldest first. */
    public List<Shipment> listShipmentsInTransitByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "JOIN sales_order o ON o.id = s.order_id "
                + "JOIN customer cu ON cu.id = o.customer_id "
                + "WHERE cu.region = ? AND s.ship_date >= ? AND s.ship_date <= ? AND s.delivery_date IS NULL ORDER BY s.ship_date, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
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
            throw new IllegalStateException("Cannot list shipments in transit of region", e);
        }
    }

    /**
     * Delivered shipments to customers of the region shipped between the two dates, inclusive, that took more days than the
     * promised days of the carrier, oldest first.
     */
    public List<Shipment> listLateShipmentsByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "JOIN sales_order o ON o.id = s.order_id "
                + "JOIN customer cu ON cu.id = o.customer_id "
                + "JOIN carrier c ON c.id = s.carrier_id "
                + "WHERE cu.region = ? AND s.ship_date >= ? AND s.ship_date <= ? AND s.delivery_date IS NOT NULL "
                + "AND DATEDIFF('DAY', s.ship_date, s.delivery_date) > c.promised_days ORDER BY s.ship_date, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
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
            throw new IllegalStateException("Cannot list late shipments of region", e);
        }
    }

    /** Shipments of orders with a line of the product shipped between the two dates, inclusive, oldest first. */
    public List<Shipment> listShipmentsByProductAndPeriod(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "WHERE EXISTS (SELECT 1 FROM sales_order_line l WHERE l.order_id = s.order_id AND l.product_id = ?) "
                + "AND s.ship_date >= ? AND s.ship_date <= ? ORDER BY s.ship_date, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
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
            throw new IllegalStateException("Cannot list shipments of product", e);
        }
    }

    /**
     * The quantity of the product shipped from each warehouse between the two dates, inclusive, ordered by warehouse code. Orders
     * shipped in one go count the line quantity at the ship date of the shipment; orders shipped in parts count the parts.
     */
    public List<WarehouseShippedQuantity> listShippedQuantityByProductAndWarehouse(long productId, LocalDate from, LocalDate to) {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, SUM(q.quantity) AS shipped FROM ("
                + "SELECT s.warehouse_id, l.quantity FROM shipment s "
                + "JOIN sales_order_line l ON l.order_id = s.order_id "
                + "WHERE l.product_id = ? AND s.ship_date >= ? AND s.ship_date <= ? "
                + "AND NOT EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = s.order_id) "
                + "UNION ALL "
                + "SELECT sp.warehouse_id, sp.quantity FROM shipment_part sp "
                + "JOIN sales_order_line l ON l.order_id = sp.order_id AND l.line_no = sp.line_no "
                + "WHERE l.product_id = ? AND sp.ship_date >= ? AND sp.ship_date <= ?) q "
                + "JOIN warehouse w ON w.id = q.warehouse_id "
                + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            statement.setLong(4, productId);
            statement.setObject(5, from);
            statement.setObject(6, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseShippedQuantity> quantities = new ArrayList<>();
                while (rs.next()) {
                    quantities.add(new WarehouseShippedQuantity(new Warehouse(rs.getLong("id"), rs.getString("code"),
                            rs.getString("name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                            rs.getInt("shipped")));
                }
                return quantities;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list shipped quantity of product by warehouse", e);
        }
    }

    /**
     * Delivered shipments from the warehouse shipped between the two dates, inclusive, that took more days than the promised days
     * of the carrier, oldest first.
     */
    public List<Shipment> listLateShipmentsByWarehouseAndPeriod(long warehouseId, LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.order_id, s.warehouse_id, s.carrier_id, s.tracking_number, s.ship_date, s.delivery_date FROM shipment s "
                + "JOIN carrier c ON c.id = s.carrier_id "
                + "WHERE s.warehouse_id = ? AND s.ship_date >= ? AND s.ship_date <= ? AND s.delivery_date IS NOT NULL "
                + "AND DATEDIFF('DAY', s.ship_date, s.delivery_date) > c.promised_days ORDER BY s.ship_date, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, warehouseId);
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
            throw new IllegalStateException("Cannot list late shipments of warehouse", e);
        }
    }

    /**
     * For each warehouse with delivered shipments shipped between the two dates (inclusive), the number of delivered shipments and
     * how many of them took more days than the promised days of the carrier, ordered by warehouse code.
     */
    public List<WarehouseDeliveryCount> listDeliveryCountsByWarehouse(LocalDate from, LocalDate to) {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, COUNT(*) AS delivered, "
                + "SUM(CASE WHEN DATEDIFF('DAY', s.ship_date, s.delivery_date) > c.promised_days THEN 1 ELSE 0 END) AS late "
                + "FROM shipment s "
                + "JOIN warehouse w ON w.id = s.warehouse_id "
                + "JOIN carrier c ON c.id = s.carrier_id "
                + "WHERE s.ship_date >= ? AND s.ship_date <= ? AND s.delivery_date IS NOT NULL "
                + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseDeliveryCount> counts = new ArrayList<>();
                while (rs.next()) {
                    counts.add(new WarehouseDeliveryCount(new Warehouse(rs.getLong("id"), rs.getString("code"),
                            rs.getString("name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                            rs.getInt("delivered"), rs.getInt("late")));
                }
                return counts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot count deliveries by warehouse", e);
        }
    }

    /**
     * The weight in kg shipped by each carrier between the two dates, inclusive, ordered by carrier name. Orders shipped in one
     * go count the line quantities at the ship date of the shipment; orders shipped in parts count the parts. Products without
     * a weight count as zero; carriers that shipped no weight in the period are left out.
     */
    public List<CarrierShippedWeight> listShippedWeightByCarrier(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id, c.name, c.service_level, c.price_per_kg, c.promised_days, SUM(q.weight) AS weight FROM ("
                + "SELECT s.carrier_id, l.quantity * pw.weight_kg AS weight FROM shipment s "
                + "JOIN sales_order_line l ON l.order_id = s.order_id "
                + "JOIN product_weight pw ON pw.product_id = l.product_id "
                + "WHERE s.ship_date >= ? AND s.ship_date <= ? "
                + "AND NOT EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = s.order_id) "
                + "UNION ALL "
                + "SELECT sp.carrier_id, sp.quantity * pw.weight_kg AS weight FROM shipment_part sp "
                + "JOIN sales_order_line l ON l.order_id = sp.order_id AND l.line_no = sp.line_no "
                + "JOIN product_weight pw ON pw.product_id = l.product_id "
                + "WHERE sp.ship_date >= ? AND sp.ship_date <= ?) q "
                + "JOIN carrier c ON c.id = q.carrier_id "
                + "GROUP BY c.id, c.name, c.service_level, c.price_per_kg, c.promised_days HAVING SUM(q.weight) > 0 "
                + "ORDER BY c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setObject(3, from);
            statement.setObject(4, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CarrierShippedWeight> weights = new ArrayList<>();
                while (rs.next()) {
                    Carrier carrier = new Carrier(rs.getLong("id"), rs.getString("name"),
                            ServiceLevel.valueOf(rs.getString("service_level")), rs.getBigDecimal("price_per_kg"),
                            rs.getInt("promised_days"));
                    weights.add(new CarrierShippedWeight(carrier, rs.getBigDecimal("weight")));
                }
                return weights;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list shipped weight by carrier", e);
        }
    }

    /**
     * The weight in kg shipped from each warehouse between the two dates, inclusive, ordered by warehouse code. Orders shipped in
     * one go count the line quantities at the ship date of the shipment; orders shipped in parts count the parts. Products
     * without a weight count as zero; warehouses that shipped no weight in the period are left out.
     */
    public List<WarehouseShippedWeight> listShippedWeightByWarehouse(LocalDate from, LocalDate to) {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, SUM(q.weight) AS weight FROM ("
                + "SELECT s.warehouse_id, l.quantity * pw.weight_kg AS weight FROM shipment s "
                + "JOIN sales_order_line l ON l.order_id = s.order_id "
                + "JOIN product_weight pw ON pw.product_id = l.product_id "
                + "WHERE s.ship_date >= ? AND s.ship_date <= ? "
                + "AND NOT EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = s.order_id) "
                + "UNION ALL "
                + "SELECT sp.warehouse_id, sp.quantity * pw.weight_kg AS weight FROM shipment_part sp "
                + "JOIN sales_order_line l ON l.order_id = sp.order_id AND l.line_no = sp.line_no "
                + "JOIN product_weight pw ON pw.product_id = l.product_id "
                + "WHERE sp.ship_date >= ? AND sp.ship_date <= ?) q "
                + "JOIN warehouse w ON w.id = q.warehouse_id "
                + "GROUP BY w.id, w.code, w.name, w.city, w.region HAVING SUM(q.weight) > 0 ORDER BY w.code, w.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setObject(3, from);
            statement.setObject(4, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<WarehouseShippedWeight> weights = new ArrayList<>();
                while (rs.next()) {
                    weights.add(new WarehouseShippedWeight(new Warehouse(rs.getLong("id"), rs.getString("code"),
                            rs.getString("name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                            rs.getBigDecimal("weight")));
                }
                return weights;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list shipped weight by warehouse", e);
        }
    }

    public Carrier createCarrier(Carrier carrier) {
        String sql = "INSERT INTO carrier (name, service_level, price_per_kg, promised_days) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, carrier.name());
            statement.setString(2, carrier.serviceLevel().name());
            statement.setBigDecimal(3, carrier.pricePerKg());
            statement.setInt(4, carrier.promisedDays());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Carrier(keys.getLong(1), carrier.name(), carrier.serviceLevel(), carrier.pricePerKg(),
                        carrier.promisedDays());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create carrier", e);
        }
    }

    public List<Carrier> listCarriersByName() {
        String sql = "SELECT id, name, service_level, price_per_kg, promised_days FROM carrier ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            List<Carrier> carriers = new ArrayList<>();
            while (rs.next()) {
                carriers.add(new Carrier(rs.getLong("id"), rs.getString("name"),
                        ServiceLevel.valueOf(rs.getString("service_level")), rs.getBigDecimal("price_per_kg"),
                        rs.getInt("promised_days")));
            }
            return carriers;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list carriers", e);
        }
    }

    public List<Carrier> listCarriersByServiceLevel(ServiceLevel serviceLevel) {
        String sql = "SELECT id, name, service_level, price_per_kg, promised_days FROM carrier WHERE service_level = ? ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, serviceLevel.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<Carrier> carriers = new ArrayList<>();
                while (rs.next()) {
                    carriers.add(new Carrier(rs.getLong("id"), rs.getString("name"),
                            ServiceLevel.valueOf(rs.getString("service_level")), rs.getBigDecimal("price_per_kg"),
                            rs.getInt("promised_days")));
                }
                return carriers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list carriers by service level", e);
        }
    }

    public Optional<Carrier> findCarrierByName(String name) {
        String sql = "SELECT id, name, service_level, price_per_kg, promised_days FROM carrier WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Carrier(rs.getLong("id"), rs.getString("name"),
                            ServiceLevel.valueOf(rs.getString("service_level")), rs.getBigDecimal("price_per_kg"),
                            rs.getInt("promised_days")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find carrier", e);
        }
    }

    public Optional<Carrier> findCarrierById(long id) {
        String sql = "SELECT id, name, service_level, price_per_kg, promised_days FROM carrier WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Carrier(rs.getLong("id"), rs.getString("name"),
                            ServiceLevel.valueOf(rs.getString("service_level")), rs.getBigDecimal("price_per_kg"),
                            rs.getInt("promised_days")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find carrier", e);
        }
    }

    /**
     * Per carrier, the shipments shipped between the two dates, inclusive: their number, the average days from shipping to
     * delivery of the delivered ones (null if none is delivered) and the number delivered later than the promised days of
     * the carrier. Sorted by carrier name; carriers without shipments in the period are left out.
     */
    public List<CarrierPerformance> listCarrierPerformance(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id, c.name, c.service_level, c.price_per_kg, c.promised_days, COUNT(s.id) AS shipment_count, "
                + "AVG(CAST(DATEDIFF('DAY', s.ship_date, s.delivery_date) AS DECIMAL(10, 2))) AS average_days, "
                + "COALESCE(SUM(CASE WHEN s.delivery_date IS NOT NULL "
                + "AND DATEDIFF('DAY', s.ship_date, s.delivery_date) > c.promised_days THEN 1 ELSE 0 END), 0) AS late_count "
                + "FROM carrier c JOIN shipment s ON s.carrier_id = c.id "
                + "WHERE s.ship_date >= ? AND s.ship_date <= ? "
                + "GROUP BY c.id, c.name, c.service_level, c.price_per_kg, c.promised_days ORDER BY c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CarrierPerformance> performance = new ArrayList<>();
                while (rs.next()) {
                    Carrier carrier = new Carrier(rs.getLong("id"), rs.getString("name"),
                            ServiceLevel.valueOf(rs.getString("service_level")), rs.getBigDecimal("price_per_kg"),
                            rs.getInt("promised_days"));
                    BigDecimal averageDays = rs.getBigDecimal("average_days");
                    performance.add(new CarrierPerformance(carrier, rs.getInt("shipment_count"),
                            averageDays == null ? null : averageDays.setScale(2, RoundingMode.HALF_UP), rs.getInt("late_count")));
                }
                return performance;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list carrier performance", e);
        }
    }

    /**
     * Orders shipped with the carrier between the two dates, inclusive, sorted by order date. An order counts if the carrier
     * shipped it whole or any part of it in the period; each order is listed once.
     */
    public List<SalesOrder> listSalesOrdersByCarrierAndPeriod(long carrierId, LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status FROM sales_order o "
                + "WHERE (EXISTS (SELECT 1 FROM shipment s WHERE s.order_id = o.id AND s.carrier_id = ? "
                + "AND s.ship_date >= ? AND s.ship_date <= ?) "
                + "OR EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = o.id AND sp.carrier_id = ? "
                + "AND sp.ship_date >= ? AND sp.ship_date <= ?)) ORDER BY o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, carrierId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            statement.setLong(4, carrierId);
            statement.setObject(5, from);
            statement.setObject(6, to);
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
            throw new IllegalStateException("Cannot list sales orders by carrier", e);
        }
    }

    /**
     * Per carrier, the number and total value of the orders shipped with it between the two dates, inclusive. An order
     * counts for a carrier if the carrier shipped it whole or any part of it in the period (an order split over several
     * carriers counts for each of them, with its full value). Sorted by carrier name; carriers without orders in the
     * period are left out.
     */
    public List<CarrierOrders> listCarrierOrders(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id, c.name, c.service_level, c.price_per_kg, c.promised_days, "
                + "COUNT(DISTINCT o.id) AS order_count, COALESCE(SUM(l.quantity * l.unit_price), 0) AS total "
                + "FROM carrier c JOIN sales_order o ON (EXISTS (SELECT 1 FROM shipment s WHERE s.order_id = o.id "
                + "AND s.carrier_id = c.id AND s.ship_date >= ? AND s.ship_date <= ?) "
                + "OR EXISTS (SELECT 1 FROM shipment_part sp WHERE sp.order_id = o.id AND sp.carrier_id = c.id "
                + "AND sp.ship_date >= ? AND sp.ship_date <= ?)) "
                + "LEFT JOIN sales_order_line l ON l.order_id = o.id "
                + "GROUP BY c.id, c.name, c.service_level, c.price_per_kg, c.promised_days ORDER BY c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setObject(3, from);
            statement.setObject(4, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CarrierOrders> result = new ArrayList<>();
                while (rs.next()) {
                    Carrier carrier = new Carrier(rs.getLong("id"), rs.getString("name"),
                            ServiceLevel.valueOf(rs.getString("service_level")), rs.getBigDecimal("price_per_kg"),
                            rs.getInt("promised_days"));
                    result.add(new CarrierOrders(carrier, rs.getInt("order_count"), rs.getBigDecimal("total")));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list carrier orders", e);
        }
    }

    /**
     * The orders of the customer placed between the two dates, inclusive, that have at least one return (whatever the
     * return date), oldest first. Each order is listed once, with its value, the value of its returned quantities at
     * the unit prices of the order (as for the returns per reason) and the returned share of the order value in percent.
     */
    public List<OrderReturns> listOrdersWithReturnsByCustomerAndPeriod(long customerId, LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order_line l WHERE l.order_id = o.id) AS order_value, "
                + "(SELECT COALESCE(SUM(rl.quantity * (SELECT ol.unit_price FROM sales_order_line ol "
                + "WHERE ol.order_id = o.id AND ol.product_id = rl.product_id ORDER BY ol.line_no FETCH FIRST 1 ROW ONLY)), 0) "
                + "FROM sales_return r JOIN sales_return_line rl ON rl.return_id = r.id WHERE r.order_id = o.id) AS returned_value "
                + "FROM sales_order o WHERE o.customer_id = ? AND o.order_date >= ? AND o.order_date <= ? "
                + "AND EXISTS (SELECT 1 FROM sales_return r WHERE r.order_id = o.id) ORDER BY o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderReturns> result = new ArrayList<>();
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
                    BigDecimal orderValue = rs.getBigDecimal("order_value");
                    BigDecimal returnedValue = rs.getBigDecimal("returned_value");
                    BigDecimal returnedPercent = orderValue.signum() == 0 ? new BigDecimal("0.00")
                            : returnedValue.multiply(new BigDecimal("100")).divide(orderValue, 2, RoundingMode.HALF_UP);
                    result.add(new OrderReturns(order, orderValue, returnedValue, returnedPercent));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list orders with returns", e);
        }
    }

    /**
     * The non-cancelled orders placed between the two dates, inclusive, whose value (quantity times unit price of the
     * lines) is above the given value, with the customer who placed each. Grouped per region of the customer (regions
     * in alphabetical order), biggest order first within a region.
     */
    public List<LargeOrder> listLargeOrders(BigDecimal minValue, LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order_line l WHERE l.order_id = o.id) AS order_value, "
                + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "AND (SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order_line l WHERE l.order_id = o.id) > ? "
                + "ORDER BY c.region, order_value DESC, o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setBigDecimal(3, minValue);
            try (ResultSet rs = statement.executeQuery()) {
                List<LargeOrder> result = new ArrayList<>();
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
                    result.add(new LargeOrder(order, rs.getBigDecimal("order_value"), customer));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list large orders", e);
        }
    }

    /**
     * The non-cancelled orders placed between the two dates, inclusive, whose value (quantity times unit price of the
     * lines) is below the given value, with the customer who placed each. Grouped per customer (customers by name, then
     * id), oldest order first within a customer.
     */
    public List<SmallOrder> listSmallOrders(BigDecimal maxValue, LocalDate from, LocalDate to) {
        String sql = "SELECT o.id, o.number, o.customer_id, o.order_date, o.status, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order_line l WHERE l.order_id = o.id) AS order_value, "
                + "c.name, c.email, c.phone, c.country, c.region, c.status AS customer_status "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED' "
                + "AND (SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order_line l WHERE l.order_id = o.id) < ? "
                + "ORDER BY c.name, c.id, o.order_date, o.id";
        String lineSql = "SELECT product_id, quantity, unit_price, promotion_code FROM sales_order_line WHERE order_id = ? ORDER BY line_no";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            statement.setBigDecimal(3, maxValue);
            try (ResultSet rs = statement.executeQuery()) {
                List<SmallOrder> result = new ArrayList<>();
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
                    result.add(new SmallOrder(order, rs.getBigDecimal("order_value"), customer));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list small orders", e);
        }
    }

    /**
     * Per region of the customer, the number of non-cancelled orders placed between the two dates, inclusive, whose value
     * (quantity times unit price of the lines) is below the given value, the number of all such orders of the region and
     * the small share in percent, rounded to two decimals. Sorted by region; regions without orders in the period are
     * left out.
     */
    public List<RegionSmallOrderShare> listSmallOrderShareByRegion(BigDecimal maxValue, LocalDate from, LocalDate to) {
        String sql = "SELECT region, COUNT(*) AS total_count, "
                + "COALESCE(SUM(CASE WHEN order_value < ? THEN 1 ELSE 0 END), 0) AS small_count FROM ("
                + "SELECT c.region AS region, "
                + "(SELECT COALESCE(SUM(l.quantity * l.unit_price), 0) FROM sales_order_line l WHERE l.order_id = o.id) AS order_value "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND o.status <> 'CANCELLED') "
                + "GROUP BY region ORDER BY region";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, maxValue);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<RegionSmallOrderShare> result = new ArrayList<>();
                while (rs.next()) {
                    int totalCount = rs.getInt("total_count");
                    int smallCount = rs.getInt("small_count");
                    BigDecimal share = BigDecimal.valueOf(smallCount * 100L)
                            .divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);
                    result.add(new RegionSmallOrderShare(Region.valueOf(rs.getString("region")), smallCount, totalCount, share));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list small order share by region", e);
        }
    }

    /**
     * Per region of the customer, the number of delivered orders placed between the two dates, inclusive, and the average
     * days from the order date to the delivery date of the shipment. Orders not yet delivered are left out. Sorted by
     * region; regions without delivered orders in the period are left out.
     */
    public List<RegionLeadTime> listLeadTimeByRegion(LocalDate from, LocalDate to) {
        String sql = "SELECT c.region AS region, COUNT(*) AS order_count, "
                + "AVG(CAST(DATEDIFF('DAY', o.order_date, s.delivery_date) AS DECIMAL(10, 2))) AS average_days "
                + "FROM sales_order o JOIN customer c ON c.id = o.customer_id JOIN shipment s ON s.order_id = o.id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND s.delivery_date IS NOT NULL "
                + "GROUP BY c.region ORDER BY c.region";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<RegionLeadTime> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new RegionLeadTime(Region.valueOf(rs.getString("region")), rs.getInt("order_count"),
                            rs.getBigDecimal("average_days").setScale(2, RoundingMode.HALF_UP)));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list lead time by region", e);
        }
    }

    /**
     * Per carrier, the number of delivered orders placed between the two dates, inclusive, that it shipped and the average
     * days from the order date to the delivery date of the shipment. Orders not yet delivered are left out. Sorted by
     * carrier name; carriers without delivered orders in the period are left out.
     */
    public List<CarrierLeadTime> listLeadTimeByCarrier(LocalDate from, LocalDate to) {
        String sql = "SELECT c.id, c.name, c.service_level, c.price_per_kg, c.promised_days, COUNT(*) AS order_count, "
                + "AVG(CAST(DATEDIFF('DAY', o.order_date, s.delivery_date) AS DECIMAL(10, 2))) AS average_days "
                + "FROM sales_order o JOIN shipment s ON s.order_id = o.id JOIN carrier c ON c.id = s.carrier_id "
                + "WHERE o.order_date >= ? AND o.order_date <= ? AND s.delivery_date IS NOT NULL "
                + "GROUP BY c.id, c.name, c.service_level, c.price_per_kg, c.promised_days ORDER BY c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CarrierLeadTime> result = new ArrayList<>();
                while (rs.next()) {
                    Carrier carrier = new Carrier(rs.getLong("id"), rs.getString("name"),
                            ServiceLevel.valueOf(rs.getString("service_level")), rs.getBigDecimal("price_per_kg"),
                            rs.getInt("promised_days"));
                    result.add(new CarrierLeadTime(carrier, rs.getInt("order_count"),
                            rs.getBigDecimal("average_days").setScale(2, RoundingMode.HALF_UP)));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list lead time by carrier", e);
        }
    }

    public SalesRep createSalesRep(SalesRep salesRep) {
        String sql = "INSERT INTO sales_rep (name, email, region) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, salesRep.name());
            statement.setString(2, salesRep.email());
            statement.setString(3, salesRep.region().name());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new SalesRep(keys.getLong(1), salesRep.name(), salesRep.email(), salesRep.region());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create sales rep", e);
        }
    }

    public List<SalesRep> listSalesRepsByName() {
        String sql = "SELECT id, name, email, region FROM sales_rep ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            List<SalesRep> salesReps = new ArrayList<>();
            while (rs.next()) {
                salesReps.add(new SalesRep(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                        Region.valueOf(rs.getString("region"))));
            }
            return salesReps;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list sales reps", e);
        }
    }

    public List<SalesRep> listSalesRepsByRegion(Region region) {
        String sql = "SELECT id, name, email, region FROM sales_rep WHERE region = ? ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            try (ResultSet rs = statement.executeQuery()) {
                List<SalesRep> salesReps = new ArrayList<>();
                while (rs.next()) {
                    salesReps.add(new SalesRep(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                            Region.valueOf(rs.getString("region"))));
                }
                return salesReps;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list sales reps by region", e);
        }
    }

    public Optional<SalesRep> findSalesRepById(long salesRepId) {
        String sql = "SELECT id, name, email, region FROM sales_rep WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, salesRepId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new SalesRep(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
                        Region.valueOf(rs.getString("region"))));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find sales rep", e);
        }
    }

    /** Sets (or replaces) the manager of the region. */
    public void saveRegionManager(Region region, long salesRepId) {
        String sql = "MERGE INTO region_manager (region, sales_rep_id) KEY (region) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setLong(2, salesRepId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save region manager", e);
        }
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

    /**
     * The customers whose open invoice amount on the date exceeds their credit limit (customers without a limit never do).
     * Only invoices dated and payments made up to the date count. Sorted by region, then sales rep name (customers
     * without a rep last), then largest excess first, then name and id.
     */
    public List<CustomerOverLimit> findCustomersOverCreditLimit(LocalDate asOf) {
        String sql = "SELECT c.id AS id, c.name AS name, c.region AS region, r.name AS rep_name, "
                + "c.credit_limit AS credit_limit, x.balance AS balance "
                + "FROM (SELECT i.customer_id AS customer_id, SUM(i.amount - (SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id AND p.payment_date <= ?)) AS balance "
                + "FROM invoice i WHERE i.status = 'OPEN' AND i.invoice_date <= ? GROUP BY i.customer_id) x "
                + "JOIN customer c ON c.id = x.customer_id LEFT JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE c.credit_limit IS NOT NULL AND x.balance > c.credit_limit "
                + "ORDER BY c.region, r.name NULLS LAST, (x.balance - c.credit_limit) DESC, c.name, c.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
            statement.setObject(2, asOf);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerOverLimit> customers = new ArrayList<>();
                while (rs.next()) {
                    customers.add(new CustomerOverLimit(rs.getLong("id"), rs.getString("name"),
                            Region.valueOf(rs.getString("region")), rs.getString("rep_name"),
                            rs.getBigDecimal("credit_limit"), rs.getBigDecimal("balance")));
                }
                return customers;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customers over credit limit", e);
        }
    }

    /** Per region, the customers over their credit limit on the date and the total excess. Largest excess first, then region. */
    public List<RegionOverLimit> findOverCreditLimitPerRegion(LocalDate asOf) {
        String sql = "SELECT c.region AS region, COUNT(*) AS customer_count, SUM(x.balance - c.credit_limit) AS excess "
                + "FROM (SELECT i.customer_id AS customer_id, SUM(i.amount - (SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id AND p.payment_date <= ?)) AS balance "
                + "FROM invoice i WHERE i.status = 'OPEN' AND i.invoice_date <= ? GROUP BY i.customer_id) x "
                + "JOIN customer c ON c.id = x.customer_id "
                + "WHERE c.credit_limit IS NOT NULL AND x.balance > c.credit_limit "
                + "GROUP BY c.region ORDER BY excess DESC, c.region";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
            statement.setObject(2, asOf);
            try (ResultSet rs = statement.executeQuery()) {
                List<RegionOverLimit> regions = new ArrayList<>();
                while (rs.next()) {
                    regions.add(new RegionOverLimit(Region.valueOf(rs.getString("region")), rs.getInt("customer_count"),
                            rs.getBigDecimal("excess")));
                }
                return regions;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find over credit limit per region", e);
        }
    }

    /**
     * Per sales rep, the customers over their credit limit on the date and the total excess. Customers without a rep
     * are left out. Largest excess first, then rep name and id.
     */
    public List<RepOverLimit> findOverCreditLimitPerSalesRep(LocalDate asOf) {
        String sql = "SELECT r.id AS id, r.name AS name, COUNT(*) AS customer_count, SUM(x.balance - c.credit_limit) AS excess "
                + "FROM (SELECT i.customer_id AS customer_id, SUM(i.amount - (SELECT COALESCE(SUM(p.amount), 0) FROM payment p "
                + "WHERE p.invoice_id = i.id AND p.payment_date <= ?)) AS balance "
                + "FROM invoice i WHERE i.status = 'OPEN' AND i.invoice_date <= ? GROUP BY i.customer_id) x "
                + "JOIN customer c ON c.id = x.customer_id JOIN sales_rep r ON r.id = c.sales_rep_id "
                + "WHERE c.credit_limit IS NOT NULL AND x.balance > c.credit_limit "
                + "GROUP BY r.id, r.name ORDER BY excess DESC, r.name, r.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, asOf);
            statement.setObject(2, asOf);
            try (ResultSet rs = statement.executeQuery()) {
                List<RepOverLimit> reps = new ArrayList<>();
                while (rs.next()) {
                    reps.add(new RepOverLimit(rs.getLong("id"), rs.getString("name"), rs.getInt("customer_count"),
                            rs.getBigDecimal("excess")));
                }
                return reps;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find over credit limit per sales rep", e);
        }
    }

    /** The sum of the credit notes of the customer, zero if there are none. */
    public BigDecimal findCreditNoteTotalByCustomer(long customerId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM credit_note WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find credit note total", e);
        }
    }

    /** The credit notes of the customer dated between the two dates, inclusive, oldest first (then by id). */
    public List<CreditNote> listCreditNotesByCustomerAndPeriod(long customerId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, customer_id, return_id, credit_date, amount FROM credit_note "
                + "WHERE customer_id = ? AND credit_date >= ? AND credit_date <= ? ORDER BY credit_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CreditNote> notes = new ArrayList<>();
                while (rs.next()) {
                    notes.add(new CreditNote(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getLong("return_id"), rs.getObject("credit_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return notes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list credit notes by customer and period", e);
        }
    }

    /** The credit notes dated between the two dates, inclusive, oldest first (then by id). */
    public List<CreditNote> listCreditNotesByPeriod(LocalDate from, LocalDate to) {
        String sql = "SELECT id, number, customer_id, return_id, credit_date, amount FROM credit_note "
                + "WHERE credit_date >= ? AND credit_date <= ? ORDER BY credit_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CreditNote> notes = new ArrayList<>();
                while (rs.next()) {
                    notes.add(new CreditNote(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getLong("return_id"), rs.getObject("credit_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return notes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list credit notes by period", e);
        }
    }

    /** The credit notes of the customers of the region dated between the two dates, inclusive, oldest first (then by id). */
    public List<CreditNote> listCreditNotesByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        String sql = "SELECT n.id AS id, n.number AS number, n.customer_id AS customer_id, n.return_id AS return_id, "
                + "n.credit_date AS credit_date, n.amount AS amount FROM credit_note n "
                + "JOIN customer c ON c.id = n.customer_id "
                + "WHERE c.region = ? AND n.credit_date >= ? AND n.credit_date <= ? ORDER BY n.credit_date, n.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, region.name());
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CreditNote> notes = new ArrayList<>();
                while (rs.next()) {
                    notes.add(new CreditNote(rs.getLong("id"), rs.getString("number"), rs.getLong("customer_id"),
                            rs.getLong("return_id"), rs.getObject("credit_date", LocalDate.class), rs.getBigDecimal("amount")));
                }
                return notes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list credit notes by region and period", e);
        }
    }

    /**
     * Number and total amount of the credit notes dated between the two dates, inclusive, per reason of the underlying
     * return. Sorted by amount, highest first (then by reason); reasons without credit notes are left out.
     */
    public List<ReasonCredit> findCreditedPerReason(LocalDate from, LocalDate to) {
        String sql = "SELECT r.reason AS reason, COUNT(*) AS note_count, SUM(n.amount) AS total "
                + "FROM credit_note n JOIN sales_return r ON r.id = n.return_id "
                + "WHERE n.credit_date >= ? AND n.credit_date <= ? "
                + "GROUP BY r.reason ORDER BY total DESC, r.reason";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<ReasonCredit> credits = new ArrayList<>();
                while (rs.next()) {
                    credits.add(new ReasonCredit(rs.getString("reason"), rs.getInt("note_count"), rs.getBigDecimal("total")));
                }
                return credits;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find credited amount per reason", e);
        }
    }

    /** The sum of the credit notes of the customer dated before the date, zero if there are none. */
    public BigDecimal findCreditNoteTotalBeforeByCustomer(long customerId, LocalDate before) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM credit_note WHERE customer_id = ? AND credit_date < ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setObject(2, before);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find credit note total before date", e);
        }
    }

    public Promotion createPromotion(Promotion promotion) {
        String sql = "INSERT INTO promotion (code, discount_percent, start_date, end_date, category) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, promotion.code());
            statement.setBigDecimal(2, promotion.discountPercent());
            statement.setObject(3, promotion.startDate());
            statement.setObject(4, promotion.endDate());
            statement.setString(5, promotion.category());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Promotion(keys.getLong(1), promotion.code(), promotion.discountPercent(), promotion.startDate(),
                        promotion.endDate(), promotion.category());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create promotion", e);
        }
    }

    public Optional<Promotion> findPromotionByCode(String code) {
        String sql = "SELECT id, code, discount_percent, start_date, end_date, category FROM promotion WHERE code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Promotion(rs.getLong("id"), rs.getString("code"), rs.getBigDecimal("discount_percent"),
                            rs.getObject("start_date", LocalDate.class), rs.getObject("end_date", LocalDate.class),
                            rs.getString("category")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find promotion", e);
        }
    }

    public List<Promotion> listPromotionsActiveOn(LocalDate date) {
        String sql = "SELECT id, code, discount_percent, start_date, end_date, category FROM promotion "
                + "WHERE start_date <= ? AND end_date >= ? ORDER BY code, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            statement.setObject(2, date);
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
            throw new IllegalStateException("Cannot list active promotions", e);
        }
    }

    public void savePromotionRegion(long promotionId, Region region) {
        String sql = "MERGE INTO promotion_region (promotion_id, region) KEY (promotion_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, promotionId);
            statement.setString(2, region.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot save promotion region", e);
        }
    }

    public List<Promotion> listPromotionsActiveOnInRegion(LocalDate date, Region region) {
        String sql = "SELECT p.id, p.code, p.discount_percent, p.start_date, p.end_date, p.category FROM promotion p "
                + "LEFT JOIN promotion_region r ON r.promotion_id = p.id "
                + "WHERE p.start_date <= ? AND p.end_date >= ? AND (r.region IS NULL OR r.region = ?) ORDER BY p.code, p.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            statement.setObject(2, date);
            statement.setString(3, region.name());
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
            throw new IllegalStateException("Cannot list active promotions of region", e);
        }
    }

    public List<Promotion> listPromotionsByCategory(String category) {
        String sql = "SELECT id, code, discount_percent, start_date, end_date, category FROM promotion "
                + "WHERE category = ? ORDER BY start_date, code, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
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
            throw new IllegalStateException("Cannot list promotions of category", e);
        }
    }

    public Optional<Promotion> findBestPromotion(String category, LocalDate date) {
        String sql = "SELECT id, code, discount_percent, start_date, end_date, category FROM promotion "
                + "WHERE start_date <= ? AND end_date >= ? AND (category IS NULL OR category = ?) "
                + "ORDER BY discount_percent DESC, code, id LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            statement.setObject(2, date);
            statement.setString(3, category);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Promotion(rs.getLong("id"), rs.getString("code"), rs.getBigDecimal("discount_percent"),
                            rs.getObject("start_date", LocalDate.class), rs.getObject("end_date", LocalDate.class),
                            rs.getString("category")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find best promotion", e);
        }
    }

    public CustomerPrice createCustomerPrice(CustomerPrice price) {
        String sql = "INSERT INTO customer_price (customer_id, product_id, unit_price, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, price.customerId());
            statement.setLong(2, price.productId());
            statement.setBigDecimal(3, price.unitPrice());
            statement.setObject(4, price.startDate());
            statement.setObject(5, price.endDate());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new CustomerPrice(keys.getLong(1), price.customerId(), price.productId(), price.unitPrice(),
                        price.startDate(), price.endDate());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create customer price", e);
        }
    }

    public Optional<CustomerPrice> findCustomerPriceById(long id) {
        String sql = "SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CustomerPrice(rs.getLong("id"), rs.getLong("customer_id"), rs.getLong("product_id"),
                            rs.getBigDecimal("unit_price"), rs.getObject("start_date", LocalDate.class),
                            rs.getObject("end_date", LocalDate.class)));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find customer price", e);
        }
    }

    public List<CustomerPrice> listCustomerPrices(long customerId) {
        String sql = "SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                + "WHERE customer_id = ? AND product_id IN (SELECT id FROM product WHERE active = TRUE) "
                + "ORDER BY product_id, start_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerPrice> prices = new ArrayList<>();
                while (rs.next()) {
                    prices.add(new CustomerPrice(rs.getLong("id"), rs.getLong("customer_id"), rs.getLong("product_id"),
                            rs.getBigDecimal("unit_price"), rs.getObject("start_date", LocalDate.class),
                            rs.getObject("end_date", LocalDate.class)));
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list customer prices", e);
        }
    }

    /** The customer's special prices valid on the date for active products of the category or of any category below it. */
    public List<CustomerPrice> listCustomerPricesByCategory(long customerId, String category, LocalDate date) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                + "WHERE customer_id = ? AND start_date <= ? AND end_date >= ? "
                + "AND product_id IN (SELECT id FROM product WHERE active = TRUE AND category IN (SELECT code FROM tree)) "
                + "ORDER BY product_id, start_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            statement.setLong(2, customerId);
            statement.setObject(3, date);
            statement.setObject(4, date);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerPrice> prices = new ArrayList<>();
                while (rs.next()) {
                    prices.add(new CustomerPrice(rs.getLong("id"), rs.getLong("customer_id"), rs.getLong("product_id"),
                            rs.getBigDecimal("unit_price"), rs.getObject("start_date", LocalDate.class),
                            rs.getObject("end_date", LocalDate.class)));
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list customer prices by category", e);
        }
    }

    /** The special prices of all customers whose last valid day lies within the given days after the date, per customer. */
    public List<CustomerPrice> listExpiringCustomerPrices(LocalDate date, int days) {
        String sql = "SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                + "WHERE end_date > ? AND end_date <= ? "
                + "ORDER BY customer_id, end_date, product_id, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, date);
            statement.setObject(2, date.plusDays(days));
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerPrice> prices = new ArrayList<>();
                while (rs.next()) {
                    prices.add(new CustomerPrice(rs.getLong("id"), rs.getLong("customer_id"), rs.getLong("product_id"),
                            rs.getBigDecimal("unit_price"), rs.getObject("start_date", LocalDate.class),
                            rs.getObject("end_date", LocalDate.class)));
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list expiring customer prices", e);
        }
    }

    public Optional<CustomerPrice> findValidCustomerPrice(long customerId, long productId, LocalDate date) {
        String sql = "SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                + "WHERE customer_id = ? AND product_id = ? AND start_date <= ? AND end_date >= ? "
                + "ORDER BY start_date DESC, id LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setLong(2, productId);
            statement.setObject(3, date);
            statement.setObject(4, date);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CustomerPrice(rs.getLong("id"), rs.getLong("customer_id"), rs.getLong("product_id"),
                            rs.getBigDecimal("unit_price"), rs.getObject("start_date", LocalDate.class),
                            rs.getObject("end_date", LocalDate.class)));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find valid customer price", e);
        }
    }

    public List<CustomerPrice> listOverlappingCustomerPrices(long customerId, long productId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT id, customer_id, product_id, unit_price, start_date, end_date FROM customer_price "
                + "WHERE customer_id = ? AND product_id = ? AND start_date <= ? AND end_date >= ? ORDER BY start_date, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            statement.setLong(2, productId);
            statement.setObject(3, endDate);
            statement.setObject(4, startDate);
            try (ResultSet rs = statement.executeQuery()) {
                List<CustomerPrice> prices = new ArrayList<>();
                while (rs.next()) {
                    prices.add(new CustomerPrice(rs.getLong("id"), rs.getLong("customer_id"), rs.getLong("product_id"),
                            rs.getBigDecimal("unit_price"), rs.getObject("start_date", LocalDate.class),
                            rs.getObject("end_date", LocalDate.class)));
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list overlapping customer prices", e);
        }
    }

    public void deleteCustomerPrice(long id) {
        String sql = "DELETE FROM customer_price WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot delete customer price", e);
        }
    }

    public Category createCategory(Category category) {
        String sql = "INSERT INTO category (code, name, parent_code) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.code());
            statement.setString(2, category.name());
            statement.setString(3, category.parentCode());
            statement.executeUpdate();
            return category;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create category", e);
        }
    }

    public Optional<Category> findCategoryByCode(String code) {
        String sql = "SELECT code, name, parent_code FROM category WHERE code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Category(rs.getString("code"), rs.getString("name"), rs.getString("parent_code")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find category", e);
        }
    }

    public List<Category> listCategories() {
        String sql = "SELECT code, name, parent_code FROM category ORDER BY code";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                List<Category> categories = new ArrayList<>();
                while (rs.next()) {
                    categories.add(new Category(rs.getString("code"), rs.getString("name"), rs.getString("parent_code")));
                }
                return categories;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list categories", e);
        }
    }

    public List<Category> listChildCategories(String parentCode) {
        String sql = "SELECT code, name, parent_code FROM category WHERE parent_code = ? ORDER BY code";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, parentCode);
            try (ResultSet rs = statement.executeQuery()) {
                List<Category> categories = new ArrayList<>();
                while (rs.next()) {
                    categories.add(new Category(rs.getString("code"), rs.getString("name"), rs.getString("parent_code")));
                }
                return categories;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list child categories", e);
        }
    }

    public List<Product> listProductsInCategoryTree(String code) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT id, sku, name, category, unit_price, unit_of_measure, active FROM product "
                + "WHERE active = TRUE AND category IN (SELECT code FROM tree) ORDER BY name, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            try (ResultSet rs = statement.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(new Product(rs.getLong("id"), rs.getString("sku"), rs.getString("name"),
                            rs.getString("category"), rs.getBigDecimal("unit_price"), rs.getString("unit_of_measure"),
                            rs.getBoolean("active")));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list products in category tree", e);
        }
    }

    /**
     * Per supplier, the purchase orders received between the two dates, inclusive: their number, the average delay in days of
     * the received date against the expected date (negative if early) and the percentage received later than expected.
     * Sorted by supplier name; suppliers without received orders in the period are left out.
     */
    public List<SupplierPerformance> listSupplierPerformance(LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.name, s.contact_email, s.country, s.rating, COUNT(o.id) AS received_count, "
                + "AVG(CAST(DATEDIFF('DAY', o.expected_date, o.received_date) AS DECIMAL(10, 2))) AS average_delay, "
                + "COALESCE(SUM(CASE WHEN o.received_date > o.expected_date THEN 1 ELSE 0 END), 0) AS late_count "
                + "FROM supplier s JOIN purchase_order o ON o.supplier_id = s.id "
                + "WHERE o.status = 'RECEIVED' AND o.received_date >= ? AND o.received_date <= ? "
                + "GROUP BY s.id, s.name, s.contact_email, s.country, s.rating ORDER BY s.name, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupplierPerformance> performance = new ArrayList<>();
                while (rs.next()) {
                    Supplier supplier = new Supplier(rs.getLong("id"), rs.getString("name"), rs.getString("contact_email"),
                            rs.getString("country"), rs.getInt("rating"));
                    int receivedCount = rs.getInt("received_count");
                    BigDecimal lateShare = BigDecimal.valueOf(rs.getInt("late_count") * 100L)
                            .divide(BigDecimal.valueOf(receivedCount), 2, RoundingMode.HALF_UP);
                    performance.add(new SupplierPerformance(supplier, receivedCount,
                            rs.getBigDecimal("average_delay").setScale(2, RoundingMode.HALF_UP), lateShare));
                }
                return performance;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list supplier performance", e);
        }
    }

    /**
     * Number and value (quantity times unit cost of the lines) of the purchase orders received between the two dates,
     * inclusive, per supplier. Sorted by value, highest first (then by supplier name, then id); suppliers without
     * received orders are left out.
     */
    public List<SupplierSpending> findSpendingPerSupplier(LocalDate from, LocalDate to) {
        String sql = "SELECT s.id, s.name, s.contact_email, s.country, s.rating, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_cost), 0) AS total "
                + "FROM supplier s JOIN purchase_order o ON o.supplier_id = s.id "
                + "LEFT JOIN purchase_order_line l ON l.order_id = o.id "
                + "WHERE o.status = 'RECEIVED' AND o.received_date >= ? AND o.received_date <= ? "
                + "GROUP BY s.id, s.name, s.contact_email, s.country, s.rating ORDER BY total DESC, s.name, s.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupplierSpending> spending = new ArrayList<>();
                while (rs.next()) {
                    Supplier supplier = new Supplier(rs.getLong("id"), rs.getString("name"), rs.getString("contact_email"),
                            rs.getString("country"), rs.getInt("rating"));
                    spending.add(new SupplierSpending(supplier, rs.getInt("order_count"), rs.getBigDecimal("total")));
                }
                return spending;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find spending per supplier", e);
        }
    }

    /**
     * Number and value of the purchase orders received between the two dates, inclusive, per country of the supplier,
     * valued as for the spending per supplier. Sorted by value, highest first (then by country); countries without
     * received orders are left out.
     */
    public List<CountrySpending> findSpendingPerCountry(LocalDate from, LocalDate to) {
        String sql = "SELECT s.country AS country, COUNT(DISTINCT o.id) AS order_count, "
                + "COALESCE(SUM(l.quantity * l.unit_cost), 0) AS total "
                + "FROM supplier s JOIN purchase_order o ON o.supplier_id = s.id "
                + "LEFT JOIN purchase_order_line l ON l.order_id = o.id "
                + "WHERE o.status = 'RECEIVED' AND o.received_date >= ? AND o.received_date <= ? "
                + "GROUP BY s.country ORDER BY total DESC, s.country";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<CountrySpending> spending = new ArrayList<>();
                while (rs.next()) {
                    spending.add(new CountrySpending(rs.getString("country"), rs.getInt("order_count"),
                            rs.getBigDecimal("total")));
                }
                return spending;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find spending per country", e);
        }
    }

    public Optional<Supplier> findSupplierById(long id) {
        String sql = "SELECT id, name, contact_email, country, rating FROM supplier WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Supplier(rs.getLong("id"), rs.getString("name"), rs.getString("contact_email"),
                            rs.getString("country"), rs.getInt("rating")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find supplier", e);
        }
    }

    public SupplierProduct createSupplierProduct(SupplierProduct supplierProduct) {
        String sql = "INSERT INTO supplier_product (supplier_id, product_id, lead_time_days, last_unit_cost) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, supplierProduct.supplierId());
            statement.setLong(2, supplierProduct.productId());
            statement.setInt(3, supplierProduct.leadTimeDays());
            statement.setBigDecimal(4, supplierProduct.lastUnitCost());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new SupplierProduct(keys.getLong(1), supplierProduct.supplierId(), supplierProduct.productId(),
                        supplierProduct.leadTimeDays(), supplierProduct.lastUnitCost());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create supplier product", e);
        }
    }

    public Optional<SupplierProduct> findSupplierProduct(long supplierId, long productId) {
        String sql = "SELECT id, supplier_id, product_id, lead_time_days, last_unit_cost FROM supplier_product "
                + "WHERE supplier_id = ? AND product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, supplierId);
            statement.setLong(2, productId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new SupplierProduct(rs.getLong("id"), rs.getLong("supplier_id"), rs.getLong("product_id"),
                            rs.getInt("lead_time_days"), rs.getBigDecimal("last_unit_cost")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find supplier product", e);
        }
    }

    public void updateSupplierProduct(SupplierProduct supplierProduct) {
        String sql = "UPDATE supplier_product SET lead_time_days = ?, last_unit_cost = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierProduct.leadTimeDays());
            statement.setBigDecimal(2, supplierProduct.lastUnitCost());
            statement.setLong(3, supplierProduct.id());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot update supplier product", e);
        }
    }

    public List<SupplierProduct> listSupplierProductsByProduct(long productId) {
        String sql = "SELECT id, supplier_id, product_id, lead_time_days, last_unit_cost FROM supplier_product "
                + "WHERE product_id = ? ORDER BY last_unit_cost, lead_time_days, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupplierProduct> supplierProducts = new ArrayList<>();
                while (rs.next()) {
                    supplierProducts.add(new SupplierProduct(rs.getLong("id"), rs.getLong("supplier_id"), rs.getLong("product_id"),
                            rs.getInt("lead_time_days"), rs.getBigDecimal("last_unit_cost")));
                }
                return supplierProducts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list supplier products by product", e);
        }
    }

    public List<SupplierProduct> listSupplierProductsBySupplier(long supplierId) {
        String sql = "SELECT id, supplier_id, product_id, lead_time_days, last_unit_cost FROM supplier_product "
                + "WHERE supplier_id = ? ORDER BY product_id, id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, supplierId);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupplierProduct> supplierProducts = new ArrayList<>();
                while (rs.next()) {
                    supplierProducts.add(new SupplierProduct(rs.getLong("id"), rs.getLong("supplier_id"), rs.getLong("product_id"),
                            rs.getInt("lead_time_days"), rs.getBigDecimal("last_unit_cost")));
                }
                return supplierProducts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list supplier products by supplier", e);
        }
    }

    /**
     * The supplier products of the products in the category or in any category below it, lowest last unit cost first
     * (then shortest lead time).
     */
    public List<SupplierProduct> listSupplierProductsByCategory(String category) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT sp.id, sp.supplier_id, sp.product_id, sp.lead_time_days, sp.last_unit_cost "
                + "FROM supplier_product sp JOIN product p ON p.id = sp.product_id "
                + "WHERE p.category IN (SELECT code FROM tree) ORDER BY sp.last_unit_cost, sp.lead_time_days, sp.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupplierProduct> supplierProducts = new ArrayList<>();
                while (rs.next()) {
                    supplierProducts.add(new SupplierProduct(rs.getLong("id"), rs.getLong("supplier_id"), rs.getLong("product_id"),
                            rs.getInt("lead_time_days"), rs.getBigDecimal("last_unit_cost")));
                }
                return supplierProducts;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list supplier products by category", e);
        }
    }

    /**
     * The categories, the given one and those below it, whose products have supplier products from exactly one
     * supplier, with that supplier. Sorted by category; categories without supplier products are left out.
     */
    public List<CategorySupplier> findCategoriesWithSingleSupplier(String category) {
        String sql = "WITH RECURSIVE tree (code) AS (SELECT code FROM category WHERE code = ? "
                + "UNION ALL SELECT c.code FROM category c JOIN tree t ON c.parent_code = t.code) "
                + "SELECT p.category AS category, s.id, s.name, s.contact_email, s.country, s.rating "
                + "FROM supplier_product sp JOIN product p ON p.id = sp.product_id "
                + "JOIN supplier s ON s.id = sp.supplier_id "
                + "WHERE p.category IN (SELECT code FROM tree) "
                + "GROUP BY p.category, s.id, s.name, s.contact_email, s.country, s.rating "
                + "HAVING (SELECT COUNT(DISTINCT sp2.supplier_id) FROM supplier_product sp2 "
                + "JOIN product p2 ON p2.id = sp2.product_id WHERE p2.category = p.category) = 1 "
                + "ORDER BY p.category";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            try (ResultSet rs = statement.executeQuery()) {
                List<CategorySupplier> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new CategorySupplier(rs.getString("category"), new Supplier(rs.getLong("id"),
                            rs.getString("name"), rs.getString("contact_email"), rs.getString("country"), rs.getInt("rating"))));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find categories with a single supplier", e);
        }
    }

    /**
     * The stock quantity and value per warehouse of the products the supplier delivers (has a supplier product for),
     * for the warehouses with stock, valued like listStockValueByWarehouse. Sorted by warehouse code.
     */
    public List<SupplierWarehouseStock> findStockPerWarehouseOfSupplier(long supplierId) {
        String sql = "SELECT w.id, w.code, w.name, w.city, w.region, SUM(s.quantity) AS quantity, "
                + "COALESCE(SUM(s.quantity * COALESCE("
                + "(SELECT l.unit_cost FROM purchase_order_line l JOIN purchase_order o ON o.id = l.order_id "
                + "WHERE l.product_id = s.product_id AND o.status IN ('SENT', 'RECEIVED') "
                + "ORDER BY o.order_date DESC, o.id DESC, l.line_no DESC LIMIT 1), 0)), 0) AS stock_value "
                + "FROM stock s JOIN warehouse w ON w.id = s.warehouse_id "
                + "WHERE s.quantity > 0 AND s.product_id IN (SELECT sp.product_id FROM supplier_product sp WHERE sp.supplier_id = ?) "
                + "GROUP BY w.id, w.code, w.name, w.city, w.region ORDER BY w.code, w.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, supplierId);
            try (ResultSet rs = statement.executeQuery()) {
                List<SupplierWarehouseStock> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new SupplierWarehouseStock(new Warehouse(rs.getLong("id"), rs.getString("code"),
                            rs.getString("name"), rs.getString("city"), Region.valueOf(rs.getString("region"))),
                            rs.getLong("quantity"), rs.getBigDecimal("stock_value").setScale(2)));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find stock per warehouse of supplier", e);
        }
    }

    public OrderNote createOrderNote(OrderNote note) {
        String sql = "INSERT INTO order_note (order_id, author, note_date, text) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, note.orderId());
            statement.setString(2, note.author());
            statement.setObject(3, note.date());
            statement.setString(4, note.text());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new OrderNote(keys.getLong(1), note.orderId(), note.author(), note.date(), note.text());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create order note", e);
        }
    }

    public List<OrderNote> listOrderNotesByOrder(long orderId) {
        String sql = "SELECT id, order_id, author, note_date, text FROM order_note WHERE order_id = ? "
                + "ORDER BY note_date DESC, id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderNote> notes = new ArrayList<>();
                while (rs.next()) {
                    notes.add(new OrderNote(rs.getLong("id"), rs.getLong("order_id"), rs.getString("author"),
                            rs.getObject("note_date", LocalDate.class), rs.getString("text")));
                }
                return notes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list order notes by order", e);
        }
    }

    public List<OrderNote> listOrderNotesByAuthor(String author, LocalDate from, LocalDate to) {
        String sql = "SELECT id, order_id, author, note_date, text FROM order_note "
                + "WHERE author = ? AND note_date >= ? AND note_date <= ? ORDER BY note_date DESC, id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, author);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderNote> notes = new ArrayList<>();
                while (rs.next()) {
                    notes.add(new OrderNote(rs.getLong("id"), rs.getLong("order_id"), rs.getString("author"),
                            rs.getObject("note_date", LocalDate.class), rs.getString("text")));
                }
                return notes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list order notes by author", e);
        }
    }

    public List<OrderNote> listOrderNotesByCustomer(long customerId) {
        String sql = "SELECT n.id, n.order_id, n.author, n.note_date, n.text FROM order_note n "
                + "JOIN sales_order o ON o.id = n.order_id WHERE o.customer_id = ? "
                + "ORDER BY n.note_date DESC, n.id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderNote> notes = new ArrayList<>();
                while (rs.next()) {
                    notes.add(new OrderNote(rs.getLong("id"), rs.getLong("order_id"), rs.getString("author"),
                            rs.getObject("note_date", LocalDate.class), rs.getString("text")));
                }
                return notes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list order notes by customer", e);
        }
    }

    public List<OrderNote> listOrderNotesByAuthorAndCustomer(String author, long customerId) {
        String sql = "SELECT n.id, n.order_id, n.author, n.note_date, n.text FROM order_note n "
                + "JOIN sales_order o ON o.id = n.order_id WHERE n.author = ? AND o.customer_id = ? "
                + "ORDER BY n.note_date DESC, n.id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, author);
            statement.setLong(2, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderNote> notes = new ArrayList<>();
                while (rs.next()) {
                    notes.add(new OrderNote(rs.getLong("id"), rs.getLong("order_id"), rs.getString("author"),
                            rs.getObject("note_date", LocalDate.class), rs.getString("text")));
                }
                return notes;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list order notes by author and customer", e);
        }
    }

    public AuditEntry createAuditEntry(AuditEntry entry) {
        String sql = "INSERT INTO audit_log (user_name, change_date, entity_type, entity_id, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entry.user());
            statement.setObject(2, entry.date());
            statement.setString(3, entry.entityType().name());
            statement.setLong(4, entry.entityId());
            statement.setString(5, entry.description());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new AuditEntry(keys.getLong(1), entry.user(), entry.date(), entry.entityType(), entry.entityId(),
                        entry.description());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create audit entry", e);
        }
    }

    public List<AuditEntry> listAuditEntriesByEntity(AuditEntityType entityType, long entityId) {
        String sql = "SELECT id, user_name, change_date, entity_type, entity_id, description FROM audit_log "
                + "WHERE entity_type = ? AND entity_id = ? ORDER BY change_date DESC, id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entityType.name());
            statement.setLong(2, entityId);
            try (ResultSet rs = statement.executeQuery()) {
                List<AuditEntry> entries = new ArrayList<>();
                while (rs.next()) {
                    entries.add(new AuditEntry(rs.getLong("id"), rs.getString("user_name"),
                            rs.getObject("change_date", LocalDate.class), AuditEntityType.valueOf(rs.getString("entity_type")),
                            rs.getLong("entity_id"), rs.getString("description")));
                }
                return entries;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list audit entries by entity", e);
        }
    }

    public List<AuditEntry> listAuditEntriesByUser(String user, LocalDate from, LocalDate to) {
        String sql = "SELECT id, user_name, change_date, entity_type, entity_id, description FROM audit_log "
                + "WHERE user_name = ? AND change_date >= ? AND change_date <= ? ORDER BY change_date DESC, id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user);
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<AuditEntry> entries = new ArrayList<>();
                while (rs.next()) {
                    entries.add(new AuditEntry(rs.getLong("id"), rs.getString("user_name"),
                            rs.getObject("change_date", LocalDate.class), AuditEntityType.valueOf(rs.getString("entity_type")),
                            rs.getLong("entity_id"), rs.getString("description")));
                }
                return entries;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list audit entries by user", e);
        }
    }

    public List<AuditEntry> listAuditEntriesByEntityType(AuditEntityType entityType, LocalDate from, LocalDate to) {
        String sql = "SELECT id, user_name, change_date, entity_type, entity_id, description FROM audit_log "
                + "WHERE entity_type = ? AND change_date >= ? AND change_date <= ? ORDER BY change_date DESC, id DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entityType.name());
            statement.setObject(2, from);
            statement.setObject(3, to);
            try (ResultSet rs = statement.executeQuery()) {
                List<AuditEntry> entries = new ArrayList<>();
                while (rs.next()) {
                    entries.add(new AuditEntry(rs.getLong("id"), rs.getString("user_name"),
                            rs.getObject("change_date", LocalDate.class), AuditEntityType.valueOf(rs.getString("entity_type")),
                            rs.getLong("entity_id"), rs.getString("description")));
                }
                return entries;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot list audit entries by entity type", e);
        }
    }

    public BackOrder createBackOrder(long orderId, LocalDate date) {
        String sql = "INSERT INTO back_order (order_id, back_order_date) VALUES (?, ?)";
        String selectSql = "SELECT number, customer_id FROM sales_order WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, orderId);
            statement.setObject(2, date);
            statement.executeUpdate();
            long id;
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                id = keys.getLong(1);
            }
            try (PreparedStatement select = connection.prepareStatement(selectSql)) {
                select.setLong(1, orderId);
                try (ResultSet rs = select.executeQuery()) {
                    rs.next();
                    return new BackOrder(id, orderId, rs.getString("number"), rs.getLong("customer_id"), date);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create back order", e);
        }
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
