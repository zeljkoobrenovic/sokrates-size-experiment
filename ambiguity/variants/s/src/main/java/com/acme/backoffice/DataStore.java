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

    private DataStorePart01 part01;

    private DataStorePart01 part01() {
        if (part01 == null) {
            part01 = new DataStorePart01(this, connection);
        }
        return part01;
    }

    private DataStorePart02 part02;

    private DataStorePart02 part02() {
        if (part02 == null) {
            part02 = new DataStorePart02(this, connection);
        }
        return part02;
    }

    private DataStorePart03 part03;

    private DataStorePart03 part03() {
        if (part03 == null) {
            part03 = new DataStorePart03(this, connection);
        }
        return part03;
    }

    private DataStorePart04 part04;

    private DataStorePart04 part04() {
        if (part04 == null) {
            part04 = new DataStorePart04(this, connection);
        }
        return part04;
    }

    private DataStorePart05 part05;

    private DataStorePart05 part05() {
        if (part05 == null) {
            part05 = new DataStorePart05(this, connection);
        }
        return part05;
    }

    private DataStorePart06 part06;

    private DataStorePart06 part06() {
        if (part06 == null) {
            part06 = new DataStorePart06(this, connection);
        }
        return part06;
    }

    private DataStorePart07 part07;

    private DataStorePart07 part07() {
        if (part07 == null) {
            part07 = new DataStorePart07(this, connection);
        }
        return part07;
    }

    private DataStorePart08 part08;

    private DataStorePart08 part08() {
        if (part08 == null) {
            part08 = new DataStorePart08(this, connection);
        }
        return part08;
    }

    private DataStorePart09 part09;

    private DataStorePart09 part09() {
        if (part09 == null) {
            part09 = new DataStorePart09(this, connection);
        }
        return part09;
    }

    private DataStorePart10 part10;

    private DataStorePart10 part10() {
        if (part10 == null) {
            part10 = new DataStorePart10(this, connection);
        }
        return part10;
    }

    public Connection getConnection() {
        return part01().getConnection();
    }

    public Customer createCustomer(Customer customer) {
        return part01().createCustomer(customer);
    }

    public void updateCustomer(Customer customer) {
        part01().updateCustomer(customer);
    }

    public void updateCustomerCreditLimit(long customerId, BigDecimal creditLimit) {
        part01().updateCustomerCreditLimit(customerId, creditLimit);
    }

    public void updateCustomerCreatedDate(long customerId, LocalDate createdDate) {
        part01().updateCustomerCreatedDate(customerId, createdDate);
    }

    public Optional<BigDecimal> findCustomerCreditLimit(long customerId) {
        return part01().findCustomerCreditLimit(customerId);
    }

    public Optional<Customer> findCustomerById(long id) {
        return part01().findCustomerById(id);
    }

    public List<Customer> listCustomersByName() {
        return part01().listCustomersByName();
    }

    public boolean closeCustomer(long customerId, LocalDate closedDate) {
        return part01().closeCustomer(customerId, closedDate);
    }

    public boolean reopenCustomer(long customerId) {
        return part01().reopenCustomer(customerId);
    }

    public List<Customer> listClosedCustomers() {
        return part01().listClosedCustomers();
    }

    public List<Customer> searchCustomersByName(String namePart) {
        return part01().searchCustomersByName(namePart);
    }

    public List<Customer> searchCustomersByRegion(Region region) {
        return part01().searchCustomersByRegion(region);
    }

    public List<Customer> searchCustomersByCountry(String country) {
        return part01().searchCustomersByCountry(country);
    }

    public List<Customer> searchCustomersByStatus(CustomerStatus status) {
        return part01().searchCustomersByStatus(status);
    }

    public Product createProduct(Product product) {
        return part01().createProduct(product);
    }

    public void updateProduct(Product product) {
        part01().updateProduct(product);
    }

    public boolean setProductActive(long productId, boolean active) {
        return part01().setProductActive(productId, active);
    }

    public PriceChange changeProductPrice(long productId, LocalDate date, BigDecimal newPrice) {
        return part01().changeProductPrice(productId, date, newPrice);
    }

    public List<PriceChange> findPriceChangesByProduct(long productId) {
        return part01().findPriceChangesByProduct(productId);
    }

    public List<Product> findProductsWithPriceChange(LocalDate from, LocalDate to) {
        return part01().findProductsWithPriceChange(from, to);
    }

    public void saveProductWeight(long productId, BigDecimal weightKg) {
        part01().saveProductWeight(productId, weightKg);
    }

    public Optional<Product> findProductBySku(String sku) {
        return part01().findProductBySku(sku);
    }

    public Optional<Product> findProductById(long id) {
        return part01().findProductById(id);
    }

    public List<Product> listActiveProductsByName() {
        return part01().listActiveProductsByName();
    }

    public List<Product> searchProductsByName(String namePart) {
        return part01().searchProductsByName(namePart);
    }

    public List<Product> searchProductsByCategory(String category) {
        return part01().searchProductsByCategory(category);
    }

    public List<Product> searchProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return part01().searchProductsByPriceRange(minPrice, maxPrice);
    }

    public Supplier createSupplier(Supplier supplier) {
        return part01().createSupplier(supplier);
    }

    public void updateSupplier(Supplier supplier) {
        part01().updateSupplier(supplier);
    }

    public List<Supplier> listSuppliersByName() {
        return part01().listSuppliersByName();
    }

    public List<Supplier> listSuppliersByCountry(String country) {
        return part01().listSuppliersByCountry(country);
    }

    public List<Supplier> listSuppliersByMinimumRating(int minimumRating) {
        return part01().listSuppliersByMinimumRating(minimumRating);
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        return part01().createWarehouse(warehouse);
    }

    public List<Warehouse> listWarehousesByCode() {
        return part01().listWarehousesByCode();
    }

    public Optional<Warehouse> findWarehouseByCode(String code) {
        return part01().findWarehouseByCode(code);
    }

    public void setStockQuantity(long warehouseId, long productId, int quantity) {
        part01().setStockQuantity(warehouseId, productId, quantity);
    }

    public void addStockQuantity(long warehouseId, long productId, int amount) {
        part01().addStockQuantity(warehouseId, productId, amount);
    }

    public boolean removeStockQuantity(long warehouseId, long productId, int amount) {
        return part01().removeStockQuantity(warehouseId, productId, amount);
    }

    public void createStockMovement(long productId, long warehouseId, LocalDate movementDate, int quantityChange, StockMovementReason reason) {
        part01().createStockMovement(productId, warehouseId, movementDate, quantityChange, reason);
    }

    public List<StockMovement> findStockMovementsByProduct(long productId, LocalDate from, LocalDate to) {
        return part01().findStockMovementsByProduct(productId, from, to);
    }

    public List<StockMovement> findStockMovementsByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return part01().findStockMovementsByWarehouse(warehouseId, from, to);
    }

    public List<StockMovement> findStockMovementsByProductAndWarehouse(long productId, long warehouseId, LocalDate from, LocalDate to) {
        return part01().findStockMovementsByProductAndWarehouse(productId, warehouseId, from, to);
    }

    public List<WarehouseMovements> findStockMovementsByCategoryPerWarehouse(String category, LocalDate from, LocalDate to) {
        return part01().findStockMovementsByCategoryPerWarehouse(category, from, to);
    }

    public List<ReasonMovements> findStockMovementsPerReasonWarehouseAndCategory(LocalDate from, LocalDate to) {
        return part01().findStockMovementsPerReasonWarehouseAndCategory(from, to);
    }

    public StockAdjustment createStockAdjustment(StockAdjustment adjustment) {
        return part01().createStockAdjustment(adjustment);
    }

    public List<StockAdjustment> findStockAdjustmentsByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return part01().findStockAdjustmentsByWarehouse(warehouseId, from, to);
    }

    public List<WarehouseAdjustments> findNegativeStockAdjustmentsPerWarehouse(LocalDate from, LocalDate to) {
        return part01().findNegativeStockAdjustmentsPerWarehouse(from, to);
    }

    public WarehouseTransfer createWarehouseTransfer(WarehouseTransfer transfer) {
        return part01().createWarehouseTransfer(transfer);
    }

    public List<WarehouseTransfer> findWarehouseTransfersByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return part02().findWarehouseTransfersByWarehouse(warehouseId, from, to);
    }

    public List<WarehouseTransfer> findWarehouseTransfersByPeriod(LocalDate from, LocalDate to) {
        return part02().findWarehouseTransfersByPeriod(from, to);
    }

    public List<WarehouseTransfer> findWarehouseTransfersByProduct(long productId, LocalDate from, LocalDate to) {
        return part02().findWarehouseTransfersByProduct(productId, from, to);
    }

    public List<TransferRoute> findTransferredQuantityPerRoute(long productId, LocalDate from, LocalDate to) {
        return part02().findTransferredQuantityPerRoute(productId, from, to);
    }

    public void createBundleComponent(long bundleId, long componentId, int quantity) {
        part02().createBundleComponent(bundleId, componentId, quantity);
    }

    public List<BundleComponent> listBundleComponents(long bundleId) {
        return part02().listBundleComponents(bundleId);
    }

    public List<Product> listBundles() {
        return part02().listBundles();
    }

    public List<Product> listBundlesByComponent(long componentId) {
        return part02().listBundlesByComponent(componentId);
    }

    public int findBundleQuantity(long warehouseId, long bundleId) {
        return part02().findBundleQuantity(warehouseId, bundleId);
    }

    public int findTotalBundleQuantity(long bundleId) {
        return part02().findTotalBundleQuantity(bundleId);
    }

    public int findStockQuantity(long warehouseId, long productId) {
        return part02().findStockQuantity(warehouseId, productId);
    }

    public int findTotalStockQuantity(long productId) {
        return part02().findTotalStockQuantity(productId);
    }

    public List<StockLevel> listStockByWarehouse(long warehouseId) {
        return part02().listStockByWarehouse(warehouseId);
    }

    public List<StockLevel> listTotalStockBelow(int threshold) {
        return part02().listTotalStockBelow(threshold);
    }

    public List<WarehouseStockValue> listStockValueByWarehouse() {
        return part02().listStockValueByWarehouse();
    }

    public BigDecimal findTotalStockValue() {
        return part02().findTotalStockValue();
    }

    public List<CategoryWarehouseStock> findStockPerCategoryAndWarehouse() {
        return part02().findStockPerCategoryAndWarehouse();
    }

    public List<CategoryStock> findStockPerCategory() {
        return part02().findStockPerCategory();
    }

    public List<WarehouseCategory> findCategoriesWithoutStockPerWarehouse() {
        return part02().findCategoriesWithoutStockPerWarehouse();
    }

    public List<WarehouseSlowMovingStock> findSlowMovingStockPerWarehouse(LocalDate date, int days) {
        return part02().findSlowMovingStockPerWarehouse(date, days);
    }

    public List<SlowMovingStock> findSlowMovingStock(LocalDate date, int days) {
        return part02().findSlowMovingStock(date, days);
    }

    public void setReorderLevel(long warehouseId, long productId, int level) {
        part02().setReorderLevel(warehouseId, productId, level);
    }

    public List<LowStockAlert> listLowStockByWarehouse(long warehouseId) {
        return part02().listLowStockByWarehouse(warehouseId);
    }

    public List<PurchaseSuggestion> listPurchaseSuggestionsByWarehouse(long warehouseId) {
        return part02().listPurchaseSuggestionsByWarehouse(warehouseId);
    }

    public List<LowStockAlert> listLowStock() {
        return part02().listLowStock();
    }

    public SalesOrder createSalesOrder(SalesOrder order) {
        return part02().createSalesOrder(order);
    }

    public Optional<SalesOrder> findSalesOrderByNumber(String number) {
        return part02().findSalesOrderByNumber(number);
    }

    public List<SalesOrder> listSalesOrdersByCustomer(long customerId) {
        return part02().listSalesOrdersByCustomer(customerId);
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder order) {
        return part02().createPurchaseOrder(order);
    }

    public Optional<PurchaseOrder> findPurchaseOrderByNumber(String number) {
        return part02().findPurchaseOrderByNumber(number);
    }

    public List<PurchaseOrder> listPurchaseOrdersBySupplier(long supplierId) {
        return part02().listPurchaseOrdersBySupplier(supplierId);
    }

    public List<PurchaseOrder> listOpenPurchaseOrders() {
        return part02().listOpenPurchaseOrders();
    }

    public List<PurchaseOrder> listOpenPurchaseOrdersBySupplier(long supplierId) {
        return part02().listOpenPurchaseOrdersBySupplier(supplierId);
    }

    public List<PurchaseOrder> searchPurchaseOrders(Long supplierId, Long warehouseId, PurchaseOrderStatus status, LocalDate from, LocalDate to) {
        return part02().searchPurchaseOrders(supplierId, warehouseId, status, from, to);
    }

    public List<PurchaseOrder> listOverduePurchaseOrders(LocalDate date) {
        return part02().listOverduePurchaseOrders(date);
    }

    public List<PurchaseOrder> listOpenPurchaseOrdersByWarehouse(long warehouseId) {
        return part02().listOpenPurchaseOrdersByWarehouse(warehouseId);
    }

    public List<PurchaseOrder> listOverduePurchaseOrdersByWarehouse(long warehouseId, LocalDate date) {
        return part02().listOverduePurchaseOrdersByWarehouse(warehouseId, date);
    }

    public List<PurchaseOrder> listPurchaseOrdersByProductAndPeriod(long productId, LocalDate from, LocalDate to) {
        return part03().listPurchaseOrdersByProductAndPeriod(productId, from, to);
    }

    public List<SupplierPurchasedQuantity> findPurchasedQuantityPerSupplier(long productId, LocalDate from, LocalDate to) {
        return part03().findPurchasedQuantityPerSupplier(productId, from, to);
    }

    public List<PurchaseOrder> listPurchaseOrdersByCategoryAndPeriod(String category, LocalDate from, LocalDate to) {
        return part03().listPurchaseOrdersByCategoryAndPeriod(category, from, to);
    }

    public List<CategorySpend> findPurchaseSpendPerCategory(String category, LocalDate from, LocalDate to) {
        return part03().findPurchaseSpendPerCategory(category, from, to);
    }

    public boolean receivePurchaseOrder(long orderId, long warehouseId, LocalDate receivedDate) {
        return part03().receivePurchaseOrder(orderId, warehouseId, receivedDate);
    }

    public Optional<LocalDate> findPurchaseOrderReceivedDate(long orderId) {
        return part03().findPurchaseOrderReceivedDate(orderId);
    }

    public List<LatePurchaseOrder> listLatePurchaseOrdersByWarehouseAndPeriod(long warehouseId, LocalDate from, LocalDate to) {
        return part03().listLatePurchaseOrdersByWarehouseAndPeriod(warehouseId, from, to);
    }

    public List<SalesOrder> listOpenSalesOrders() {
        return part03().listOpenSalesOrders();
    }

    public int countOpenSalesOrdersOnDate(LocalDate date) {
        return part03().countOpenSalesOrdersOnDate(date);
    }

    public BigDecimal findOpenSalesOrderValueOnDate(LocalDate date) {
        return part03().findOpenSalesOrderValueOnDate(date);
    }

    public List<SalesOrder> listOpenSalesOrdersByCustomer(long customerId) {
        return part03().listOpenSalesOrdersByCustomer(customerId);
    }

    public List<SalesOrder> listOpenSalesOrdersByRegion(Region region) {
        return part03().listOpenSalesOrdersByRegion(region);
    }

    public List<SalesOrder> listSalesOrdersByPeriod(LocalDate from, LocalDate to) {
        return part03().listSalesOrdersByPeriod(from, to);
    }

    public List<ProductOrder> listOrdersByProduct(long productId, LocalDate from, LocalDate to) {
        return part03().listOrdersByProduct(productId, from, to);
    }

    public List<ProductOrder> listOrdersByProductPerCustomer(long productId, LocalDate from, LocalDate to) {
        return part03().listOrdersByProductPerCustomer(productId, from, to);
    }

    public List<ProductOrder> listOrdersByCategory(String category, LocalDate from, LocalDate to) {
        return part03().listOrdersByCategory(category, from, to);
    }

    public List<CategoryMonthRevenue> findRevenuePerCategoryAndMonth(LocalDate from, LocalDate to) {
        return part03().findRevenuePerCategoryAndMonth(from, to);
    }

    public void saveOrderChannel(long orderId, SalesChannel channel) {
        part03().saveOrderChannel(orderId, channel);
    }

    public List<SalesOrder> listSalesOrdersByChannel(SalesChannel channel, LocalDate from, LocalDate to) {
        return part03().listSalesOrdersByChannel(channel, from, to);
    }

    public List<SalesOrder> searchSalesOrders(Long customerId, OrderStatus status, Region region, LocalDate from, LocalDate to) {
        return part03().searchSalesOrders(customerId, status, region, from, to);
    }

    public List<SalesOrder> listSalesOrdersByPeriodAndStatus(LocalDate from, LocalDate to, OrderStatus status) {
        return part03().listSalesOrdersByPeriodAndStatus(from, to, status);
    }

    public void changeSalesOrderStatus(long orderId, OrderStatus status, LocalDate date) {
        part03().changeSalesOrderStatus(orderId, status, date);
    }

    public void cancelSalesOrder(long orderId, CancellationReason reason, LocalDate date) {
        part03().cancelSalesOrder(orderId, reason, date);
    }

    public List<CancellationCount> findCancellationsPerReasonAndCustomer(LocalDate from, LocalDate to) {
        return part03().findCancellationsPerReasonAndCustomer(from, to);
    }

    public List<CancelledOrder> listCancelledOrdersByCustomer(LocalDate from, LocalDate to) {
        return part03().listCancelledOrdersByCustomer(from, to);
    }

    public List<CancelledOrder> listCancelledOrdersByRegion(LocalDate from, LocalDate to) {
        return part03().listCancelledOrdersByRegion(from, to);
    }

    public List<DraftOrder> listStaleDraftOrdersByCustomer(LocalDate asOf, int days) {
        return part03().listStaleDraftOrdersByCustomer(asOf, days);
    }

    public List<DraftOrder> listStaleDraftOrdersBySalesRep(LocalDate asOf, int days) {
        return part03().listStaleDraftOrdersBySalesRep(asOf, days);
    }

    public int archiveSalesOrders(LocalDate cutoff, LocalDate archiveDate) {
        return part03().archiveSalesOrders(cutoff, archiveDate);
    }

    public List<SalesOrder> listArchivedSalesOrders() {
        return part04().listArchivedSalesOrders();
    }

    public List<SalesOrder> listArchivedSalesOrdersByCustomer(long customerId) {
        return part04().listArchivedSalesOrdersByCustomer(customerId);
    }

    public List<SalesOrder> listArchivedSalesOrdersByRegion(Region region) {
        return part04().listArchivedSalesOrdersByRegion(region);
    }

    public List<ArchivedYearRevenue> findArchivedRevenuePerYear() {
        return part04().findArchivedRevenuePerYear();
    }

    public List<OrderStatusChange> listSalesOrderStatusChanges(long orderId) {
        return part04().listSalesOrderStatusChanges(orderId);
    }

    public BigDecimal findSalesOrderTotal(String number) {
        return part04().findSalesOrderTotal(number);
    }

    public BigDecimal findCustomerOrderTotal(long customerId) {
        return part04().findCustomerOrderTotal(customerId);
    }

    public List<OrderHistoryEntry> listOrderHistoryByCustomer(long customerId) {
        return part04().listOrderHistoryByCustomer(customerId);
    }

    public List<OrderHistoryEntry> listOrderHistoryByRegion(Region region) {
        return part04().listOrderHistoryByRegion(region);
    }

    public Invoice createInvoice(Invoice invoice) {
        return part04().createInvoice(invoice);
    }

    public Optional<Invoice> findInvoiceByNumber(String number) {
        return part04().findInvoiceByNumber(number);
    }

    public Optional<Invoice> findActiveInvoiceByOrder(long orderId) {
        return part04().findActiveInvoiceByOrder(orderId);
    }

    public List<Invoice> listInvoicesByCustomer(long customerId) {
        return part04().listInvoicesByCustomer(customerId);
    }

    public List<Invoice> listInvoicesByCustomerAndPeriod(long customerId, LocalDate from, LocalDate to) {
        return part04().listInvoicesByCustomerAndPeriod(customerId, from, to);
    }

    public BigDecimal findInvoiceTotalBeforeByCustomer(long customerId, LocalDate before) {
        return part04().findInvoiceTotalBeforeByCustomer(customerId, before);
    }

    public List<Invoice> listOverdueInvoices(LocalDate asOf) {
        return part04().listOverdueInvoices(asOf);
    }

    public BigDecimal findOverdueInvoiceAmount(LocalDate asOf) {
        return part04().findOverdueInvoiceAmount(asOf);
    }

    public List<Invoice> searchInvoices(Long customerId, InvoiceStatus status, LocalDate dueFrom, LocalDate dueTo, BigDecimal minAmount) {
        return part04().searchInvoices(customerId, status, dueFrom, dueTo, minAmount);
    }

    public List<Invoice> listOverdueInvoicesByCustomer(LocalDate asOf, long customerId) {
        return part04().listOverdueInvoicesByCustomer(asOf, customerId);
    }

    public List<Invoice> listOverdueInvoicesByRegion(LocalDate asOf, Region region) {
        return part04().listOverdueInvoicesByRegion(asOf, region);
    }

    public List<Invoice> listOpenInvoicesBySalesRep(long salesRepId) {
        return part04().listOpenInvoicesBySalesRep(salesRepId);
    }

    public List<Invoice> listOverdueInvoicesBySalesRep(LocalDate asOf, long salesRepId) {
        return part04().listOverdueInvoicesBySalesRep(asOf, salesRepId);
    }

    public List<RepOverdueInvoices> findOverdueInvoicesPerSalesRep(LocalDate asOf) {
        return part04().findOverdueInvoicesPerSalesRep(asOf);
    }

    public List<Invoice> listPaidInvoicesBySalesRep(long salesRepId, LocalDate from, LocalDate to) {
        return part04().listPaidInvoicesBySalesRep(salesRepId, from, to);
    }

    public List<Invoice> listOpenAndPaidInvoicesByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        return part04().listOpenAndPaidInvoicesByRegionAndPeriod(region, from, to);
    }

    public List<InvoiceStatusTotal> findOpenAndPaidInvoiceTotalsByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        return part04().findOpenAndPaidInvoiceTotalsByRegionAndPeriod(region, from, to);
    }

    public List<RegionLargeInvoice> listLargeInvoicesByRegion(BigDecimal minAmount, LocalDate from, LocalDate to) {
        return part04().listLargeInvoicesByRegion(minAmount, from, to);
    }

    public List<LatePaidInvoice> findLatePaidInvoicesByPeriod(LocalDate from, LocalDate to) {
        return part04().findLatePaidInvoicesByPeriod(from, to);
    }

    public List<PartiallyPaidInvoice> findPartiallyPaidInvoices() {
        return part04().findPartiallyPaidInvoices();
    }

    public List<CurrencyOpenInvoice> findOpenInvoicesByCurrency() {
        return part04().findOpenInvoicesByCurrency();
    }

    public List<PaymentReceived> findPaymentsBySalesRepAndPeriod(long salesRepId, LocalDate from, LocalDate to) {
        return part04().findPaymentsBySalesRepAndPeriod(salesRepId, from, to);
    }

    public List<RepPayments> findPaymentsPerSalesRep(LocalDate from, LocalDate to) {
        return part04().findPaymentsPerSalesRep(from, to);
    }

    public int countInvoices() {
        return part04().countInvoices();
    }

    public Payment createPayment(Payment payment) {
        return part04().createPayment(payment);
    }

    public List<Payment> listPaymentsByInvoice(long invoiceId) {
        return part04().listPaymentsByInvoice(invoiceId);
    }

    public BigDecimal findPaymentTotalByInvoice(long invoiceId) {
        return part04().findPaymentTotalByInvoice(invoiceId);
    }

    public List<PaymentReceived> findPaymentsByPeriod(LocalDate from, LocalDate to) {
        return part04().findPaymentsByPeriod(from, to);
    }

    public List<PaymentReceived> findPaymentsByPeriodAndCustomer(long customerId, LocalDate from, LocalDate to) {
        return part04().findPaymentsByPeriodAndCustomer(customerId, from, to);
    }

    public List<PaymentReceived> findPaymentsByCustomerAndPeriodExcludingCancelled(long customerId, LocalDate from, LocalDate to) {
        return part04().findPaymentsByCustomerAndPeriodExcludingCancelled(customerId, from, to);
    }

    public BigDecimal findPaymentTotalBeforeByCustomer(long customerId, LocalDate before) {
        return part04().findPaymentTotalBeforeByCustomer(customerId, before);
    }

    public List<DailyPaymentTotal> findPaymentTotalsPerDay(LocalDate from, LocalDate to) {
        return part04().findPaymentTotalsPerDay(from, to);
    }

    public List<PaymentReceived> findPaymentsByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        return part04().findPaymentsByRegionAndPeriod(region, from, to);
    }

    public List<MonthlyPaymentTotal> findPaymentTotalsPerMonthByRegion(Region region, LocalDate from, LocalDate to) {
        return part04().findPaymentTotalsPerMonthByRegion(region, from, to);
    }

    public List<UnappliedPayment> findUnappliedPayments() {
        return part04().findUnappliedPayments();
    }

    public BigDecimal findOpenInvoiceBalanceByCustomer(long customerId) {
        return part04().findOpenInvoiceBalanceByCustomer(customerId);
    }

    public List<InvoiceAging> findInvoiceAging(LocalDate asOf) {
        return part05().findInvoiceAging(asOf);
    }

    public void updateInvoiceStatus(long invoiceId, InvoiceStatus status) {
        part05().updateInvoiceStatus(invoiceId, status);
    }

    public Shipment createShipment(Shipment shipment) {
        return part05().createShipment(shipment);
    }

    public Optional<Shipment> findShipmentByOrder(long orderId) {
        return part05().findShipmentByOrder(orderId);
    }

    public List<Shipment> listShipmentsByWarehouseAndPeriod(long warehouseId, LocalDate from, LocalDate to) {
        return part05().listShipmentsByWarehouseAndPeriod(warehouseId, from, to);
    }

    public boolean updateShipmentDeliveryDate(long orderId, LocalDate deliveryDate) {
        return part05().updateShipmentDeliveryDate(orderId, deliveryDate);
    }

    public List<Shipment> listShipmentsInTransit() {
        return part05().listShipmentsInTransit();
    }

    public List<Shipment> listShipmentsInTransitByCarrier(long carrierId) {
        return part05().listShipmentsInTransitByCarrier(carrierId);
    }

    public List<Shipment> listShipmentsInTransitShippedBefore(LocalDate shippedBefore) {
        return part05().listShipmentsInTransitShippedBefore(shippedBefore);
    }

    public List<Shipment> listShipmentsByCustomerAndPeriod(long customerId, LocalDate from, LocalDate to) {
        return part05().listShipmentsByCustomerAndPeriod(customerId, from, to);
    }

    public List<Shipment> searchShipments(Long carrierId, Long warehouseId, Long customerId, ShipmentStatus status, LocalDate from, LocalDate to) {
        return part05().searchShipments(carrierId, warehouseId, customerId, status, from, to);
    }

    public List<Shipment> listShipmentsInTransitByCustomer(long customerId) {
        return part05().listShipmentsInTransitByCustomer(customerId);
    }

    public List<Shipment> listLateShipmentsByCustomer(long customerId) {
        return part05().listLateShipmentsByCustomer(customerId);
    }

    public List<Shipment> listShipmentsInTransitByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        return part05().listShipmentsInTransitByRegionAndPeriod(region, from, to);
    }

    public List<Shipment> listLateShipmentsByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        return part05().listLateShipmentsByRegionAndPeriod(region, from, to);
    }

    public List<Shipment> listShipmentsByProductAndPeriod(long productId, LocalDate from, LocalDate to) {
        return part05().listShipmentsByProductAndPeriod(productId, from, to);
    }

    public List<WarehouseShippedQuantity> listShippedQuantityByProductAndWarehouse(long productId, LocalDate from, LocalDate to) {
        return part05().listShippedQuantityByProductAndWarehouse(productId, from, to);
    }

    public List<Shipment> listLateShipmentsByWarehouseAndPeriod(long warehouseId, LocalDate from, LocalDate to) {
        return part05().listLateShipmentsByWarehouseAndPeriod(warehouseId, from, to);
    }

    public List<WarehouseDeliveryCount> listDeliveryCountsByWarehouse(LocalDate from, LocalDate to) {
        return part05().listDeliveryCountsByWarehouse(from, to);
    }

    public List<CarrierShippedWeight> listShippedWeightByCarrier(LocalDate from, LocalDate to) {
        return part05().listShippedWeightByCarrier(from, to);
    }

    public List<WarehouseShippedWeight> listShippedWeightByWarehouse(LocalDate from, LocalDate to) {
        return part05().listShippedWeightByWarehouse(from, to);
    }

    public Carrier createCarrier(Carrier carrier) {
        return part05().createCarrier(carrier);
    }

    public List<Carrier> listCarriersByName() {
        return part05().listCarriersByName();
    }

    public List<Carrier> listCarriersByServiceLevel(ServiceLevel serviceLevel) {
        return part05().listCarriersByServiceLevel(serviceLevel);
    }

    public Optional<Carrier> findCarrierByName(String name) {
        return part05().findCarrierByName(name);
    }

    public Optional<Carrier> findCarrierById(long id) {
        return part05().findCarrierById(id);
    }

    public List<CarrierPerformance> listCarrierPerformance(LocalDate from, LocalDate to) {
        return part05().listCarrierPerformance(from, to);
    }

    public List<SalesOrder> listSalesOrdersByCarrierAndPeriod(long carrierId, LocalDate from, LocalDate to) {
        return part05().listSalesOrdersByCarrierAndPeriod(carrierId, from, to);
    }

    public List<CarrierOrders> listCarrierOrders(LocalDate from, LocalDate to) {
        return part05().listCarrierOrders(from, to);
    }

    public List<OrderReturns> listOrdersWithReturnsByCustomerAndPeriod(long customerId, LocalDate from, LocalDate to) {
        return part05().listOrdersWithReturnsByCustomerAndPeriod(customerId, from, to);
    }

    public List<LargeOrder> listLargeOrders(BigDecimal minValue, LocalDate from, LocalDate to) {
        return part05().listLargeOrders(minValue, from, to);
    }

    public List<SmallOrder> listSmallOrders(BigDecimal maxValue, LocalDate from, LocalDate to) {
        return part05().listSmallOrders(maxValue, from, to);
    }

    public List<RegionSmallOrderShare> listSmallOrderShareByRegion(BigDecimal maxValue, LocalDate from, LocalDate to) {
        return part05().listSmallOrderShareByRegion(maxValue, from, to);
    }

    public List<RegionLeadTime> listLeadTimeByRegion(LocalDate from, LocalDate to) {
        return part05().listLeadTimeByRegion(from, to);
    }

    public List<CarrierLeadTime> listLeadTimeByCarrier(LocalDate from, LocalDate to) {
        return part05().listLeadTimeByCarrier(from, to);
    }

    public SalesRep createSalesRep(SalesRep salesRep) {
        return part05().createSalesRep(salesRep);
    }

    public List<SalesRep> listSalesRepsByName() {
        return part05().listSalesRepsByName();
    }

    public List<SalesRep> listSalesRepsByRegion(Region region) {
        return part05().listSalesRepsByRegion(region);
    }

    public Optional<SalesRep> findSalesRepById(long salesRepId) {
        return part05().findSalesRepById(salesRepId);
    }

    public void saveRegionManager(Region region, long salesRepId) {
        part05().saveRegionManager(region, salesRepId);
    }

    public Optional<SalesRep> findRegionManager(Region region) {
        return part06().findRegionManager(region);
    }

    public List<Region> listRegionsManagedBy(long salesRepId) {
        return part06().listRegionsManagedBy(salesRepId);
    }

    public void assignCustomerToSalesRep(long customerId, long salesRepId) {
        part06().assignCustomerToSalesRep(customerId, salesRepId);
    }

    public List<Customer> listCustomersBySalesRep(long salesRepId) {
        return part06().listCustomersBySalesRep(salesRepId);
    }

    public RepPerformance findSalesRepPerformance(long salesRepId, LocalDate from, LocalDate to) {
        return part06().findSalesRepPerformance(salesRepId, from, to);
    }

    public List<RegionSales> findSalesPerRegion(LocalDate from, LocalDate to) {
        return part06().findSalesPerRegion(from, to);
    }

    public RegionReport findRegionReport(Region region, LocalDate from, LocalDate to) {
        return part06().findRegionReport(region, from, to);
    }

    public List<YearOverYearSales> findYearOverYearSalesPerRegion(YearMonth month) {
        return part06().findYearOverYearSalesPerRegion(month);
    }

    public YearOverYearSales findYearOverYearSales(YearMonth month) {
        return part06().findYearOverYearSales(month);
    }

    public List<RegionCustomers> findCustomersPerRegion(LocalDate newFrom, LocalDate newTo, LocalDate dormantFrom, LocalDate dormantTo) {
        return part06().findCustomersPerRegion(newFrom, newTo, dormantFrom, dormantTo);
    }

    public List<ChannelSales> findSalesPerChannel(LocalDate from, LocalDate to) {
        return part06().findSalesPerChannel(from, to);
    }

    public List<MonthlySales> findMonthlySales(int year) {
        return part06().findMonthlySales(year);
    }

    public List<QuarterCustomers> findCustomersPerQuarter(int year) {
        return part06().findCustomersPerQuarter(year);
    }

    public List<QuarterOrders> findOrdersPerQuarter(int year) {
        return part06().findOrdersPerQuarter(year);
    }

    public List<QuarterPurchaseOrders> findPurchaseOrdersPerQuarter(int year) {
        return part06().findPurchaseOrdersPerQuarter(year);
    }

    public List<WeekdayPurchaseOrders> findPurchaseOrdersPerWeekday(LocalDate from, LocalDate to) {
        return part06().findPurchaseOrdersPerWeekday(from, to);
    }

    public List<QuarterShipments> findShipmentsPerQuarter(int year) {
        return part06().findShipmentsPerQuarter(year);
    }

    public List<QuarterReturns> findReturnsPerQuarter(int year) {
        return part06().findReturnsPerQuarter(year);
    }

    public List<QuarterInvoices> findInvoicesPerQuarter(int year) {
        return part06().findInvoicesPerQuarter(year);
    }

    public List<WeekdayCustomers> findCustomersPerWeekday(LocalDate from, LocalDate to) {
        return part06().findCustomersPerWeekday(from, to);
    }

    public List<WeekdayOrders> findOrdersPerWeekday(LocalDate from, LocalDate to) {
        return part06().findOrdersPerWeekday(from, to);
    }

    public List<WeekdayShipments> findShipmentsPerWeekday(LocalDate from, LocalDate to) {
        return part06().findShipmentsPerWeekday(from, to);
    }

    public List<WeekdayReturns> findReturnsPerWeekday(LocalDate from, LocalDate to) {
        return part06().findReturnsPerWeekday(from, to);
    }

    public List<WeekdayInvoices> findInvoicesPerWeekday(LocalDate from, LocalDate to) {
        return part06().findInvoicesPerWeekday(from, to);
    }

    public List<Invoice> findLatestInvoices(int limit) {
        return part06().findLatestInvoices(limit);
    }

    public List<Invoice> findLatestInvoicesByRegion(Region region, int limit) {
        return part06().findLatestInvoicesByRegion(region, limit);
    }

    public List<Invoice> findLatestInvoicesByCustomer(long customerId, int limit) {
        return part06().findLatestInvoicesByCustomer(customerId, limit);
    }

    public List<Shipment> findLatestShipments(int limit) {
        return part06().findLatestShipments(limit);
    }

    public List<Shipment> findLatestShipmentsByRegion(Region region, int limit) {
        return part06().findLatestShipmentsByRegion(region, limit);
    }

    public List<Shipment> findLatestShipmentsByCustomer(long customerId, int limit) {
        return part06().findLatestShipmentsByCustomer(customerId, limit);
    }

    public List<Customer> findLatestCustomers(int limit) {
        return part06().findLatestCustomers(limit);
    }

    public List<Customer> findLatestCustomersByRegion(Region region, int limit) {
        return part06().findLatestCustomersByRegion(region, limit);
    }

    public List<Customer> findLatestCustomersBefore(long customerId, int limit) {
        return part06().findLatestCustomersBefore(customerId, limit);
    }

    public List<SalesOrder> findLatestOrders(int limit) {
        return part06().findLatestOrders(limit);
    }

    public List<SalesOrder> findLatestOrdersByRegion(Region region, int limit) {
        return part07().findLatestOrdersByRegion(region, limit);
    }

    public List<SalesOrder> findLatestOrdersByCustomer(long customerId, int limit) {
        return part07().findLatestOrdersByCustomer(customerId, limit);
    }

    public List<SalesRepCustomerCount> findCustomerCountPerSalesRep(LocalDate from, LocalDate to) {
        return part07().findCustomerCountPerSalesRep(from, to);
    }

    public List<SalesRepOrderCount> findOrderCountPerSalesRep(LocalDate from, LocalDate to) {
        return part07().findOrderCountPerSalesRep(from, to);
    }

    public List<SalesRepInvoiceCount> findInvoiceCountPerSalesRep(LocalDate from, LocalDate to) {
        return part07().findInvoiceCountPerSalesRep(from, to);
    }

    public List<SalesRepShipmentCount> findShipmentCountPerSalesRep(LocalDate from, LocalDate to) {
        return part07().findShipmentCountPerSalesRep(from, to);
    }

    public List<SalesRepReturnCount> findReturnCountPerSalesRep(LocalDate from, LocalDate to) {
        return part07().findReturnCountPerSalesRep(from, to);
    }

    public List<SalesRepPurchaseOrderCount> findPurchaseOrderCountPerSalesRep(LocalDate from, LocalDate to) {
        return part07().findPurchaseOrderCountPerSalesRep(from, to);
    }

    public List<PurchaseOrder> findPurchaseOrdersByCountry(String country, LocalDate from, LocalDate to) {
        return part07().findPurchaseOrdersByCountry(country, from, to);
    }

    public List<CountryPurchaseOrderCount> findPurchaseOrderCountPerCountry(LocalDate from, LocalDate to) {
        return part07().findPurchaseOrderCountPerCountry(from, to);
    }

    public List<SalesReturn> findReturnsByCountry(String country, LocalDate from, LocalDate to) {
        return part07().findReturnsByCountry(country, from, to);
    }

    public List<CountryReturnCount> findReturnCountPerCountry(LocalDate from, LocalDate to) {
        return part07().findReturnCountPerCountry(from, to);
    }

    public List<Shipment> findShipmentsByCountry(String country, LocalDate from, LocalDate to) {
        return part07().findShipmentsByCountry(country, from, to);
    }

    public List<CountryShipmentCount> findShipmentCountPerCountry(LocalDate from, LocalDate to) {
        return part07().findShipmentCountPerCountry(from, to);
    }

    public List<SalesOrder> findOrdersByCountry(String country, LocalDate from, LocalDate to) {
        return part07().findOrdersByCountry(country, from, to);
    }

    public List<CountryOrderCount> findOrderCountPerCountry(LocalDate from, LocalDate to) {
        return part07().findOrderCountPerCountry(from, to);
    }

    public List<Invoice> findInvoicesByCountry(String country, LocalDate from, LocalDate to) {
        return part07().findInvoicesByCountry(country, from, to);
    }

    public List<CountryInvoiceCount> findInvoiceCountPerCountry(LocalDate from, LocalDate to) {
        return part07().findInvoiceCountPerCountry(from, to);
    }

    public List<Customer> findCustomersByCountry(String country, LocalDate from, LocalDate to) {
        return part07().findCustomersByCountry(country, from, to);
    }

    public List<CountryCustomerCount> findCustomerCountPerCountry(LocalDate from, LocalDate to) {
        return part07().findCustomerCountPerCountry(from, to);
    }

    public List<ProductSales> findSalesPerProduct(LocalDate from, LocalDate to) {
        return part07().findSalesPerProduct(from, to);
    }

    public List<ProductMargin> findMarginPerProduct(LocalDate from, LocalDate to) {
        return part07().findMarginPerProduct(from, to);
    }

    public List<CategoryMargin> findMarginPerCategory(LocalDate from, LocalDate to) {
        return part07().findMarginPerCategory(from, to);
    }

    public List<CategoryMargin> findCategoriesBelowMargin(BigDecimal percent, LocalDate from, LocalDate to) {
        return part07().findCategoriesBelowMargin(percent, from, to);
    }

    public List<ProductSales> findSalesPerProductInCategory(String category, LocalDate from, LocalDate to) {
        return part07().findSalesPerProductInCategory(category, from, to);
    }

    public List<OrderLineMatch> searchOrderLines(Long productId, String category, Long customerId, LocalDate from, LocalDate to, Integer minQuantity) {
        return part07().searchOrderLines(productId, category, customerId, from, to, minQuantity);
    }

    public List<ProductReturns> findReturnedQuantityPerProduct(LocalDate from, LocalDate to) {
        return part07().findReturnedQuantityPerProduct(from, to);
    }

    public List<ProductReturns> findReturnedQuantityPerProductInCategory(String category, LocalDate from, LocalDate to) {
        return part07().findReturnedQuantityPerProductInCategory(category, from, to);
    }

    public List<ReasonReturns> findReturnsPerReason(LocalDate from, LocalDate to) {
        return part07().findReturnsPerReason(from, to);
    }

    public List<CustomerReturn> findReturnsPerCustomer(LocalDate from, LocalDate to) {
        return part07().findReturnsPerCustomer(from, to);
    }

    public List<WarehouseReturn> findReturnsByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return part07().findReturnsByWarehouse(warehouseId, from, to);
    }

    public List<ProductReturnedQuantity> findReturnedQuantityByWarehouse(long warehouseId, LocalDate from, LocalDate to) {
        return part07().findReturnedQuantityByWarehouse(warehouseId, from, to);
    }

    public List<RegionReasonReturns> findReturnsPerReasonAndRegion(LocalDate from, LocalDate to) {
        return part07().findReturnsPerReasonAndRegion(from, to);
    }

    public List<CustomerSales> findSalesPerCustomer(LocalDate from, LocalDate to) {
        return part07().findSalesPerCustomer(from, to);
    }

    public List<CustomerSales> findTopCustomersInRegion(Region region, int limit, LocalDate from, LocalDate to) {
        return part08().findTopCustomersInRegion(region, limit, from, to);
    }

    public List<RepeatCustomer> findRepeatCustomersByRegion(int minOrders, LocalDate from, LocalDate to) {
        return part08().findRepeatCustomersByRegion(minOrders, from, to);
    }

    public List<FirstTimeCustomer> findFirstTimeCustomers(LocalDate from, LocalDate to) {
        return part08().findFirstTimeCustomers(from, to);
    }

    public List<CustomerSales> findRevenuePerCustomerIncludingIdle(LocalDate from, LocalDate to) {
        return part08().findRevenuePerCustomerIncludingIdle(from, to);
    }

    public List<SalesRepCustomer> findCustomerReportBySalesRep(long salesRepId, LocalDate from, LocalDate to) {
        return part08().findCustomerReportBySalesRep(salesRepId, from, to);
    }

    public List<CustomerRegionSales> findRevenuePerCustomerWithRegionIncludingIdle(LocalDate from, LocalDate to) {
        return part08().findRevenuePerCustomerWithRegionIncludingIdle(from, to);
    }

    public List<Customer> findActiveCustomersWithoutOrders(LocalDate from, LocalDate to) {
        return part08().findActiveCustomersWithoutOrders(from, to);
    }

    public List<Customer> findActiveCustomersWithoutInvoices(LocalDate from, LocalDate to) {
        return part08().findActiveCustomersWithoutInvoices(from, to);
    }

    public CustomerActivity findCustomerActivity(long customerId) {
        return part08().findCustomerActivity(customerId);
    }

    public CustomerAddress createCustomerAddress(CustomerAddress address) {
        return part08().createCustomerAddress(address);
    }

    public void updateCustomerAddress(CustomerAddress address) {
        part08().updateCustomerAddress(address);
    }

    public Optional<CustomerAddress> findCustomerAddressById(long id) {
        return part08().findCustomerAddressById(id);
    }

    public List<CustomerAddress> listCustomerAddresses(long customerId) {
        return part08().listCustomerAddresses(customerId);
    }

    public void setDefaultShippingAddress(long customerId, long addressId) {
        part08().setDefaultShippingAddress(customerId, addressId);
    }

    public CustomerContact createCustomerContact(CustomerContact contact) {
        return part08().createCustomerContact(contact);
    }

    public Optional<CustomerContact> findCustomerContactByEmail(String email) {
        return part08().findCustomerContactByEmail(email);
    }

    public List<CustomerContact> listCustomerContacts(long customerId) {
        return part08().listCustomerContacts(customerId);
    }

    public List<CustomerContact> findCustomerContactsByRole(String role) {
        return part08().findCustomerContactsByRole(role);
    }

    public List<RegionContact> listRegionContacts(Region region) {
        return part08().listRegionContacts(region);
    }

    public SalesReturn createSalesReturn(SalesReturn salesReturn, long warehouseId) {
        return part08().createSalesReturn(salesReturn, warehouseId);
    }

    public Optional<SalesReturn> findSalesReturnByNumber(String number) {
        return part08().findSalesReturnByNumber(number);
    }

    public List<SalesReturn> listSalesReturnsByOrder(long orderId) {
        return part08().listSalesReturnsByOrder(orderId);
    }

    public List<SalesReturn> listSalesReturnsByCustomer(long customerId) {
        return part08().listSalesReturnsByCustomer(customerId);
    }

    public List<SalesReturn> findLatestReturns(int limit) {
        return part08().findLatestReturns(limit);
    }

    public List<SalesReturn> findLatestReturnsByRegion(Region region, int limit) {
        return part08().findLatestReturnsByRegion(region, limit);
    }

    public List<SalesReturn> findLatestReturnsByCustomer(long customerId, int limit) {
        return part08().findLatestReturnsByCustomer(customerId, limit);
    }

    public List<PurchaseOrder> findLatestPurchaseOrders(int limit) {
        return part08().findLatestPurchaseOrders(limit);
    }

    public List<PurchaseOrder> findLatestPurchaseOrdersByRegion(Region region, int limit) {
        return part08().findLatestPurchaseOrdersByRegion(region, limit);
    }

    public List<PurchaseOrder> findLatestPurchaseOrdersBySupplier(long supplierId, int limit) {
        return part08().findLatestPurchaseOrdersBySupplier(supplierId, limit);
    }

    public List<SalesReturn> listSalesReturnsByProduct(long productId, LocalDate from, LocalDate to) {
        return part08().listSalesReturnsByProduct(productId, from, to);
    }

    public List<SalesReturn> searchSalesReturns(Long customerId, Long productId, String reason, LocalDate from, LocalDate to) {
        return part08().searchSalesReturns(customerId, productId, reason, from, to);
    }

    public CreditNote createCreditNote(CreditNote note) {
        return part08().createCreditNote(note);
    }

    public List<CreditNote> listCreditNotesByCustomer(long customerId) {
        return part08().listCreditNotesByCustomer(customerId);
    }

    public void createCreditNoteApplication(long creditNoteId, long invoiceId, LocalDate appliedDate) {
        part08().createCreditNoteApplication(creditNoteId, invoiceId, appliedDate);
    }

    public Optional<CreditNote> findCreditNoteByNumber(String number) {
        return part08().findCreditNoteByNumber(number);
    }

    public boolean isCreditNoteApplied(long creditNoteId) {
        return part08().isCreditNoteApplied(creditNoteId);
    }

    public List<CustomerOpenCredit> findCustomersWithOpenCredit() {
        return part08().findCustomersWithOpenCredit();
    }

    public List<RegionOpenCredit> findOpenCreditPerRegion() {
        return part08().findOpenCreditPerRegion();
    }

    public List<CustomerOverLimit> findCustomersOverCreditLimit(LocalDate asOf) {
        return part09().findCustomersOverCreditLimit(asOf);
    }

    public List<RegionOverLimit> findOverCreditLimitPerRegion(LocalDate asOf) {
        return part09().findOverCreditLimitPerRegion(asOf);
    }

    public List<RepOverLimit> findOverCreditLimitPerSalesRep(LocalDate asOf) {
        return part09().findOverCreditLimitPerSalesRep(asOf);
    }

    public BigDecimal findCreditNoteTotalByCustomer(long customerId) {
        return part09().findCreditNoteTotalByCustomer(customerId);
    }

    public List<CreditNote> listCreditNotesByCustomerAndPeriod(long customerId, LocalDate from, LocalDate to) {
        return part09().listCreditNotesByCustomerAndPeriod(customerId, from, to);
    }

    public List<CreditNote> listCreditNotesByPeriod(LocalDate from, LocalDate to) {
        return part09().listCreditNotesByPeriod(from, to);
    }

    public List<CreditNote> listCreditNotesByRegionAndPeriod(Region region, LocalDate from, LocalDate to) {
        return part09().listCreditNotesByRegionAndPeriod(region, from, to);
    }

    public List<ReasonCredit> findCreditedPerReason(LocalDate from, LocalDate to) {
        return part09().findCreditedPerReason(from, to);
    }

    public BigDecimal findCreditNoteTotalBeforeByCustomer(long customerId, LocalDate before) {
        return part09().findCreditNoteTotalBeforeByCustomer(customerId, before);
    }

    public Promotion createPromotion(Promotion promotion) {
        return part09().createPromotion(promotion);
    }

    public Optional<Promotion> findPromotionByCode(String code) {
        return part09().findPromotionByCode(code);
    }

    public List<Promotion> listPromotionsActiveOn(LocalDate date) {
        return part09().listPromotionsActiveOn(date);
    }

    public void savePromotionRegion(long promotionId, Region region) {
        part09().savePromotionRegion(promotionId, region);
    }

    public List<Promotion> listPromotionsActiveOnInRegion(LocalDate date, Region region) {
        return part09().listPromotionsActiveOnInRegion(date, region);
    }

    public List<Promotion> listPromotionsByCategory(String category) {
        return part09().listPromotionsByCategory(category);
    }

    public Optional<Promotion> findBestPromotion(String category, LocalDate date) {
        return part09().findBestPromotion(category, date);
    }

    public CustomerPrice createCustomerPrice(CustomerPrice price) {
        return part09().createCustomerPrice(price);
    }

    public Optional<CustomerPrice> findCustomerPriceById(long id) {
        return part09().findCustomerPriceById(id);
    }

    public List<CustomerPrice> listCustomerPrices(long customerId) {
        return part09().listCustomerPrices(customerId);
    }

    public List<CustomerPrice> listCustomerPricesByCategory(long customerId, String category, LocalDate date) {
        return part09().listCustomerPricesByCategory(customerId, category, date);
    }

    public List<CustomerPrice> listExpiringCustomerPrices(LocalDate date, int days) {
        return part09().listExpiringCustomerPrices(date, days);
    }

    public Optional<CustomerPrice> findValidCustomerPrice(long customerId, long productId, LocalDate date) {
        return part09().findValidCustomerPrice(customerId, productId, date);
    }

    public List<CustomerPrice> listOverlappingCustomerPrices(long customerId, long productId, LocalDate startDate, LocalDate endDate) {
        return part09().listOverlappingCustomerPrices(customerId, productId, startDate, endDate);
    }

    public void deleteCustomerPrice(long id) {
        part09().deleteCustomerPrice(id);
    }

    public Category createCategory(Category category) {
        return part09().createCategory(category);
    }

    public Optional<Category> findCategoryByCode(String code) {
        return part09().findCategoryByCode(code);
    }

    public List<Category> listCategories() {
        return part09().listCategories();
    }

    public List<Category> listChildCategories(String parentCode) {
        return part09().listChildCategories(parentCode);
    }

    public List<Product> listProductsInCategoryTree(String code) {
        return part09().listProductsInCategoryTree(code);
    }

    public List<SupplierPerformance> listSupplierPerformance(LocalDate from, LocalDate to) {
        return part09().listSupplierPerformance(from, to);
    }

    public List<SupplierSpending> findSpendingPerSupplier(LocalDate from, LocalDate to) {
        return part09().findSpendingPerSupplier(from, to);
    }

    public List<CountrySpending> findSpendingPerCountry(LocalDate from, LocalDate to) {
        return part09().findSpendingPerCountry(from, to);
    }

    public Optional<Supplier> findSupplierById(long id) {
        return part09().findSupplierById(id);
    }

    public SupplierProduct createSupplierProduct(SupplierProduct supplierProduct) {
        return part09().createSupplierProduct(supplierProduct);
    }

    public Optional<SupplierProduct> findSupplierProduct(long supplierId, long productId) {
        return part09().findSupplierProduct(supplierId, productId);
    }

    public void updateSupplierProduct(SupplierProduct supplierProduct) {
        part09().updateSupplierProduct(supplierProduct);
    }

    public List<SupplierProduct> listSupplierProductsByProduct(long productId) {
        return part09().listSupplierProductsByProduct(productId);
    }

    public List<SupplierProduct> listSupplierProductsBySupplier(long supplierId) {
        return part09().listSupplierProductsBySupplier(supplierId);
    }

    public List<SupplierProduct> listSupplierProductsByCategory(String category) {
        return part09().listSupplierProductsByCategory(category);
    }

    public List<CategorySupplier> findCategoriesWithSingleSupplier(String category) {
        return part09().findCategoriesWithSingleSupplier(category);
    }

    public List<SupplierWarehouseStock> findStockPerWarehouseOfSupplier(long supplierId) {
        return part09().findStockPerWarehouseOfSupplier(supplierId);
    }

    public OrderNote createOrderNote(OrderNote note) {
        return part09().createOrderNote(note);
    }

    public List<OrderNote> listOrderNotesByOrder(long orderId) {
        return part09().listOrderNotesByOrder(orderId);
    }

    public List<OrderNote> listOrderNotesByAuthor(String author, LocalDate from, LocalDate to) {
        return part09().listOrderNotesByAuthor(author, from, to);
    }

    public List<OrderNote> listOrderNotesByCustomer(long customerId) {
        return part09().listOrderNotesByCustomer(customerId);
    }

    public List<OrderNote> listOrderNotesByAuthorAndCustomer(String author, long customerId) {
        return part09().listOrderNotesByAuthorAndCustomer(author, customerId);
    }

    public AuditEntry createAuditEntry(AuditEntry entry) {
        return part09().createAuditEntry(entry);
    }

    public List<AuditEntry> listAuditEntriesByEntity(AuditEntityType entityType, long entityId) {
        return part09().listAuditEntriesByEntity(entityType, entityId);
    }

    public List<AuditEntry> listAuditEntriesByUser(String user, LocalDate from, LocalDate to) {
        return part09().listAuditEntriesByUser(user, from, to);
    }

    public List<AuditEntry> listAuditEntriesByEntityType(AuditEntityType entityType, LocalDate from, LocalDate to) {
        return part09().listAuditEntriesByEntityType(entityType, from, to);
    }

    public BackOrder createBackOrder(long orderId, LocalDate date) {
        return part09().createBackOrder(orderId, date);
    }

    public Optional<BackOrder> findBackOrderByOrder(long orderId) {
        return part10().findBackOrderByOrder(orderId);
    }

    public List<BackOrder> listBackOrders() {
        return part10().listBackOrders();
    }

    public int countBackOrdersOnDate(LocalDate date) {
        return part10().countBackOrdersOnDate(date);
    }

    public List<BackOrder> listBackOrdersByCustomer(long customerId) {
        return part10().listBackOrdersByCustomer(customerId);
    }

    public List<BackOrder> listBackOrdersByProduct(long productId) {
        return part10().listBackOrdersByProduct(productId);
    }

    public List<CategoryBackOrders> findBackOrdersPerCategory() {
        return part10().findBackOrdersPerCategory();
    }

    public List<PromotionUsage> findPromotionUsage(LocalDate from, LocalDate to) {
        return part10().findPromotionUsage(from, to);
    }

    public List<CustomerPromotionLine> findCustomerPromotionLines(long customerId, LocalDate from, LocalDate to) {
        return part10().findCustomerPromotionLines(customerId, from, to);
    }

    public List<Promotion> listUnusedPromotions(LocalDate from, LocalDate to) {
        return part10().listUnusedPromotions(from, to);
    }

    public List<PriceListUsage> findPriceListUsage(LocalDate from, LocalDate to) {
        return part10().findPriceListUsage(from, to);
    }

    public void createShipmentPart(long orderId, int lineNo, long warehouseId, long carrierId, String trackingNumber, LocalDate shipDate, int quantity) {
        part10().createShipmentPart(orderId, lineNo, warehouseId, carrierId, trackingNumber, shipDate, quantity);
    }

    public List<RemainingLine> listRemainingLines(long orderId) {
        return part10().listRemainingLines(orderId);
    }

    public ExchangeRate saveExchangeRate(ExchangeRate rate) {
        return part10().saveExchangeRate(rate);
    }

    public Optional<ExchangeRate> findExchangeRate(Currency currency, LocalDate asOf) {
        return part10().findExchangeRate(currency, asOf);
    }

    public void saveCustomerCurrency(long customerId, Currency currency) {
        part10().saveCustomerCurrency(customerId, currency);
    }

    public Optional<Currency> findCustomerCurrency(long customerId) {
        return part10().findCustomerCurrency(customerId);
    }

    public InvoiceCurrency createInvoiceCurrency(InvoiceCurrency invoiceCurrency) {
        return part10().createInvoiceCurrency(invoiceCurrency);
    }

    public Optional<InvoiceCurrency> findInvoiceCurrency(long invoiceId) {
        return part10().findInvoiceCurrency(invoiceId);
    }

    public TaxRate saveTaxRate(TaxRate rate) {
        return part10().saveTaxRate(rate);
    }

    public Optional<TaxRate> findTaxRate(String country, LocalDate asOf) {
        return part10().findTaxRate(country, asOf);
    }

    public void setOrderLineDiscount(long orderId, int lineNo, BigDecimal percent) {
        part10().setOrderLineDiscount(orderId, lineNo, percent);
    }

    public boolean hasOrderLineDiscountAbove(long orderId, BigDecimal percent) {
        return part10().hasOrderLineDiscountAbove(orderId, percent);
    }

    public List<SalesOrder> listSalesOrdersAwaitingApproval(BigDecimal percent) {
        return part10().listSalesOrdersAwaitingApproval(percent);
    }

    public int countSalesOrdersAwaitingApprovalOnDate(BigDecimal percent, LocalDate date) {
        return part10().countSalesOrdersAwaitingApprovalOnDate(percent, date);
    }

    public OrderApproval saveOrderApproval(OrderApproval approval) {
        return part10().saveOrderApproval(approval);
    }

    public Optional<OrderApproval> findOrderApproval(long orderId) {
        return part10().findOrderApproval(orderId);
    }

    public void deleteOrderApproval(long orderId) {
        part10().deleteOrderApproval(orderId);
    }

    public SupportTicket createSupportTicket(SupportTicket ticket) {
        return part10().createSupportTicket(ticket);
    }

    public Optional<SupportTicket> findSupportTicketByNumber(String number) {
        return part10().findSupportTicketByNumber(number);
    }

    public void updateSupportTicketStatus(long ticketId, TicketStatus status, LocalDate closedDate) {
        part10().updateSupportTicketStatus(ticketId, status, closedDate);
    }

    public List<SupportTicket> listOpenSupportTicketsByCustomer(long customerId) {
        return part10().listOpenSupportTicketsByCustomer(customerId);
    }

    public List<SupportTicket> listOpenSupportTicketsByPriority(TicketPriority priority) {
        return part10().listOpenSupportTicketsByPriority(priority);
    }

    public List<SupportTicket> listOpenSupportTicketsByRegion(Region region) {
        return part10().listOpenSupportTicketsByRegion(region);
    }

    public List<SupportTicket> listOpenSupportTicketsBySalesRep(long salesRepId) {
        return part10().listOpenSupportTicketsBySalesRep(salesRepId);
    }

    public List<SupportTicket> listSupportTicketsWithOrderByCustomer(long customerId) {
        return part10().listSupportTicketsWithOrderByCustomer(customerId);
    }

    public List<OrderTickets> findOrdersWithMultipleSupportTickets() {
        return part10().findOrdersWithMultipleSupportTickets();
    }

    public List<PriorityTickets> findOpenSupportTicketCountsPerPriorityByRegion(Region region) {
        return part10().findOpenSupportTicketCountsPerPriorityByRegion(region);
    }

    public List<SupportTicket> listHighPrioritySupportTicketsByRegionOlderThan(Region region, LocalDate date, int days) {
        return part10().listHighPrioritySupportTicketsByRegionOlderThan(region, date, days);
    }

    public List<SupportTicket> listSupportTicketsOpenOnDateOpenedBefore(LocalDate date, LocalDate cutoff) {
        return part10().listSupportTicketsOpenOnDateOpenedBefore(date, cutoff);
    }

    public int countSupportTicketsOpenOnDate(LocalDate date) {
        return part10().countSupportTicketsOpenOnDate(date);
    }

    public List<TicketStatusCount> findSupportTicketCountsPerStatus() {
        return part10().findSupportTicketCountsPerStatus();
    }

    public List<TicketCloseTime> findSupportTicketCloseTimesPerPriority(LocalDate from, LocalDate to) {
        return part10().findSupportTicketCloseTimesPerPriority(from, to);
    }

    public List<ClosedTicket> listClosedSupportTickets(LocalDate from, LocalDate to) {
        return part10().listClosedSupportTickets(from, to);
    }

    public void updateSupportTicketProduct(long ticketId, Long productId) {
        part10().updateSupportTicketProduct(ticketId, productId);
    }

    public List<SupportTicket> listOpenSupportTicketsByProduct(long productId) {
        return part10().listOpenSupportTicketsByProduct(productId);
    }

    public List<ProductTickets> findSupportTicketCountsPerProduct(LocalDate from, LocalDate to) {
        return part10().findSupportTicketCountsPerProduct(from, to);
    }

    public void saveSalesTarget(long salesRepId, LocalDate month, BigDecimal target) {
        part10().saveSalesTarget(salesRepId, month, target);
    }

    public List<SalesTargetProgress> findSalesTargetProgress(LocalDate monthStart, LocalDate monthEnd) {
        return part10().findSalesTargetProgress(monthStart, monthEnd);
    }

    public Optional<CommissionBasis> findCommissionBasis(long salesRepId, LocalDate monthStart, LocalDate monthEnd) {
        return part10().findCommissionBasis(salesRepId, monthStart, monthEnd);
    }

    public List<CommissionBasis> listCommissionBases(LocalDate monthStart, LocalDate monthEnd) {
        return part10().listCommissionBases(monthStart, monthEnd);
    }

}
