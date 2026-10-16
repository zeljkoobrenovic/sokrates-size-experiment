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

class DataStorePart02 {
    private final DataStore store;
    private final java.sql.Connection connection;

    DataStorePart02(DataStore store, java.sql.Connection connection) {
        this.store = store;
        this.connection = connection;
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
}
