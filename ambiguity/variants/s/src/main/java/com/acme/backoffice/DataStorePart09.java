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

class DataStorePart09 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart09(DataStore store, java.sql.Connection connection) {
        this.store = store;
        this.connection = connection;
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
}
