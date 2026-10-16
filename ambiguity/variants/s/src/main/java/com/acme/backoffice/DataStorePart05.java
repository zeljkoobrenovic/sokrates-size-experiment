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

class DataStorePart05 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart05(DataStore store, java.sql.Connection connection) {
        this.store = store;
        this.connection = connection;
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
}
