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

class DataStorePart01 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart01(DataStore store, java.sql.Connection connection) {
        this.store = store;
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
}
