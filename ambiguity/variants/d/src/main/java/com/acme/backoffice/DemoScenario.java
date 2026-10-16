package com.acme.backoffice;

import com.acme.backoffice.model.AddressType;
import com.acme.backoffice.model.ChannelSales;
import com.acme.backoffice.model.SalesChannel;
import com.acme.backoffice.model.AuditEntityType;
import com.acme.backoffice.model.AuditEntry;
import com.acme.backoffice.model.BackOrder;
import com.acme.backoffice.model.CategoryBackOrders;
import com.acme.backoffice.model.CancellationCount;
import com.acme.backoffice.model.CancellationReason;
import com.acme.backoffice.model.CancelledOrder;
import com.acme.backoffice.model.DraftOrder;
import com.acme.backoffice.model.Carrier;
import com.acme.backoffice.model.CarrierOrders;
import com.acme.backoffice.model.CarrierShippedWeight;
import com.acme.backoffice.model.CarrierPerformance;
import com.acme.backoffice.model.Category;
import com.acme.backoffice.model.CreditNote;
import com.acme.backoffice.model.CustomerOpenCredit;
import com.acme.backoffice.model.CustomerOverLimit;
import com.acme.backoffice.model.RegionOverLimit;
import com.acme.backoffice.model.RepOverLimit;
import com.acme.backoffice.model.RegionOpenCredit;
import com.acme.backoffice.model.Currency;
import com.acme.backoffice.model.CurrencyInvoices;
import com.acme.backoffice.model.CurrencyOpenInvoice;
import com.acme.backoffice.model.CustomerCurrencyInvoices;
import com.acme.backoffice.model.RepCommission;
import com.acme.backoffice.model.CategoryMargin;
import com.acme.backoffice.model.CategoryPurchaseOrders;
import com.acme.backoffice.model.CategorySpend;
import com.acme.backoffice.model.CategorySupplier;
import com.acme.backoffice.model.CategorySupplierProducts;
import com.acme.backoffice.model.CategoryMonthRevenue;
import com.acme.backoffice.model.CategoryStock;
import com.acme.backoffice.model.CategoryWarehouseStock;
import com.acme.backoffice.model.ClosedTicket;
import com.acme.backoffice.model.Customer;
import com.acme.backoffice.model.Dashboard;
import com.acme.backoffice.model.CustomerOpenAmount;
import com.acme.backoffice.model.CustomerActivity;
import com.acme.backoffice.model.CustomerAddress;
import com.acme.backoffice.model.CustomerContact;
import com.acme.backoffice.model.CustomerPrice;
import com.acme.backoffice.model.CustomerSales;
import com.acme.backoffice.model.CustomerRegionSales;
import com.acme.backoffice.model.CustomerSegment;
import com.acme.backoffice.model.SegmentRegionCustomers;
import com.acme.backoffice.model.CustomerStatement;
import com.acme.backoffice.model.CustomerStatus;
import com.acme.backoffice.model.DailyPaymentTotal;
import com.acme.backoffice.model.Invoice;
import com.acme.backoffice.model.InvoiceAging;
import com.acme.backoffice.model.InvoiceCurrency;
import com.acme.backoffice.model.InvoiceTax;
import com.acme.backoffice.model.InvoiceStatus;
import com.acme.backoffice.model.InvoiceStatusTotal;
import com.acme.backoffice.model.RegionInvoices;
import com.acme.backoffice.model.RegionLargeInvoices;
import com.acme.backoffice.model.MonthlyPaymentTotal;
import com.acme.backoffice.model.RegionPayments;
import com.acme.backoffice.model.CustomerUnappliedPayments;
import com.acme.backoffice.model.UnappliedPayment;
import com.acme.backoffice.model.CustomerLateInvoices;
import com.acme.backoffice.model.LatePaidInvoice;
import com.acme.backoffice.model.CustomerPartiallyPaidInvoices;
import com.acme.backoffice.model.PartiallyPaidInvoice;
import com.acme.backoffice.model.RegionPartiallyPaidInvoices;
import com.acme.backoffice.model.LargeOrder;
import com.acme.backoffice.model.LowStockAlert;
import com.acme.backoffice.model.CarrierLeadTime;
import com.acme.backoffice.model.RegionLeadTime;
import com.acme.backoffice.model.RegionSmallOrderShare;
import com.acme.backoffice.model.SmallOrder;
import com.acme.backoffice.model.CustomerPromotionLine;
import com.acme.backoffice.model.CustomerPromotions;
import com.acme.backoffice.model.OrderLine;
import com.acme.backoffice.model.OrderLineMatch;
import com.acme.backoffice.model.OrderApproval;
import com.acme.backoffice.model.OrderNote;
import com.acme.backoffice.model.CustomerReturn;
import com.acme.backoffice.model.CustomerReturns;
import com.acme.backoffice.model.ProductPurchaseOrders;
import com.acme.backoffice.model.ProductReturnedQuantity;
import com.acme.backoffice.model.SupplierPurchasedQuantity;
import com.acme.backoffice.model.WarehouseReturn;
import com.acme.backoffice.model.WarehouseReturns;
import com.acme.backoffice.model.OrderReturns;
import com.acme.backoffice.model.OrderStatus;
import com.acme.backoffice.model.OrderStatusChange;
import com.acme.backoffice.model.Payment;
import com.acme.backoffice.model.PaymentReceived;
import com.acme.backoffice.model.PriceChange;
import com.acme.backoffice.model.PriceListUsage;
import com.acme.backoffice.model.Product;
import com.acme.backoffice.model.ProductOrder;
import com.acme.backoffice.model.PriorityTickets;
import com.acme.backoffice.model.OrderTickets;
import com.acme.backoffice.model.ProductTickets;
import com.acme.backoffice.model.ProductMargin;
import com.acme.backoffice.model.ProductReturns;
import com.acme.backoffice.model.ReasonCredit;
import com.acme.backoffice.model.ReasonReturns;
import com.acme.backoffice.model.RegionCustomers;
import com.acme.backoffice.model.RegionReasonReturns;
import com.acme.backoffice.model.ProductSales;
import com.acme.backoffice.model.Promotion;
import com.acme.backoffice.model.PromotionUsage;
import com.acme.backoffice.model.PurchaseOrder;
import com.acme.backoffice.model.PurchaseOrderLine;
import com.acme.backoffice.model.PurchaseOrderStatus;
import com.acme.backoffice.model.PurchaseSuggestion;
import com.acme.backoffice.model.Region;
import com.acme.backoffice.model.RegionContact;
import com.acme.backoffice.model.RoleContacts;
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
import com.acme.backoffice.model.RegionOverview;
import com.acme.backoffice.model.RegionReport;
import com.acme.backoffice.model.RegionSales;
import com.acme.backoffice.model.FirstTimeCustomer;
import com.acme.backoffice.model.RepeatCustomer;
import com.acme.backoffice.model.RemainingLine;
import com.acme.backoffice.model.RepOverdueInvoices;
import com.acme.backoffice.model.RepPayments;
import com.acme.backoffice.model.RepPerformance;
import com.acme.backoffice.model.SalesOrder;
import com.acme.backoffice.model.ReturnLine;
import com.acme.backoffice.model.SalesRep;
import com.acme.backoffice.model.SalesReturn;
import com.acme.backoffice.model.MonthlySales;
import com.acme.backoffice.model.YearOverYearSales;
import com.acme.backoffice.model.SalesTargetProgress;
import com.acme.backoffice.model.ServiceLevel;
import com.acme.backoffice.model.ProductShipments;
import com.acme.backoffice.model.RegionShipments;
import com.acme.backoffice.model.LatePurchaseOrder;
import com.acme.backoffice.model.WarehouseLatePurchaseOrders;
import com.acme.backoffice.model.WarehouseLateShipments;
import com.acme.backoffice.model.WarehouseShippedQuantity;
import com.acme.backoffice.model.WarehouseShippedWeight;
import com.acme.backoffice.model.Shipment;
import com.acme.backoffice.model.ShipmentStatus;
import com.acme.backoffice.model.StatementEntry;
import com.acme.backoffice.model.StockAdjustment;
import com.acme.backoffice.model.StockLevel;
import com.acme.backoffice.model.ReasonMovements;
import com.acme.backoffice.model.StockMovement;
import com.acme.backoffice.model.StockMovementReason;
import com.acme.backoffice.model.WarehouseAdjustments;
import com.acme.backoffice.model.TransferRoute;
import com.acme.backoffice.model.WarehouseMovements;
import com.acme.backoffice.model.Supplier;
import com.acme.backoffice.model.SupplierPerformance;
import com.acme.backoffice.model.SupplierSpending;
import com.acme.backoffice.model.CountrySpending;
import com.acme.backoffice.model.SupplierProduct;
import com.acme.backoffice.model.SupplierStock;
import com.acme.backoffice.model.SupplierWarehouseStock;
import com.acme.backoffice.model.SlowMovingStock;
import com.acme.backoffice.model.SupportTicket;
import com.acme.backoffice.model.TicketPriority;
import com.acme.backoffice.model.TicketStatus;
import com.acme.backoffice.model.Warehouse;
import com.acme.backoffice.model.WarehouseCategory;
import com.acme.backoffice.model.WarehouseSlowMovingStock;
import com.acme.backoffice.model.WarehouseStockValue;
import com.acme.backoffice.model.WarehouseTransfer;
import com.acme.backoffice.model.WarehouseTransferLine;
import com.acme.backoffice.service.AddressService;
import com.acme.backoffice.service.BundleService;
import com.acme.backoffice.service.OrderFulfillmentService;
import com.acme.backoffice.model.WarehouseOrderFulfillment;
import com.acme.backoffice.model.OrderHistoryEntry;
import com.acme.backoffice.service.ContactService;
import com.acme.backoffice.service.BackOrderService;
import com.acme.backoffice.service.CategoryService;
import com.acme.backoffice.service.CreditService;
import com.acme.backoffice.service.DashboardService;
import com.acme.backoffice.service.CurrencyService;
import com.acme.backoffice.service.CustomerActivityService;
import com.acme.backoffice.service.CustomerClosureService;
import com.acme.backoffice.service.CustomerSegmentService;
import com.acme.backoffice.service.DiscountApprovalService;
import com.acme.backoffice.service.DormantCustomerService;
import com.acme.backoffice.service.InvoiceService;
import com.acme.backoffice.service.OrderNoteService;
import com.acme.backoffice.service.OrderService;
import com.acme.backoffice.service.RegionManagerService;
import com.acme.backoffice.service.QuarterCustomersService;
import com.acme.backoffice.service.QuarterInvoicesService;
import com.acme.backoffice.service.QuarterOrdersService;
import com.acme.backoffice.service.QuarterPurchaseOrdersService;
import com.acme.backoffice.service.QuarterReturnsService;
import com.acme.backoffice.service.QuarterShipmentsService;
import com.acme.backoffice.service.WeekdayCustomersService;
import com.acme.backoffice.service.WeekdayInvoicesService;
import com.acme.backoffice.service.WeekdayOrdersService;
import com.acme.backoffice.service.WeekdayPurchaseOrdersService;
import com.acme.backoffice.service.WeekdayReturnsService;
import com.acme.backoffice.service.WeekdayShipmentsService;
import com.acme.backoffice.service.LatestCustomersService;
import com.acme.backoffice.service.LatestOrdersService;
import com.acme.backoffice.service.LatestPurchaseOrdersService;
import com.acme.backoffice.service.LatestReturnsService;
import com.acme.backoffice.service.LatestInvoicesService;
import com.acme.backoffice.service.LatestShipmentsService;
import com.acme.backoffice.service.SalesRepCustomerCountService;
import com.acme.backoffice.service.SalesRepOrderCountService;
import com.acme.backoffice.service.CountryCustomersService;
import com.acme.backoffice.model.CountryCustomerCount;
import com.acme.backoffice.model.CountryOrderCount;
import com.acme.backoffice.model.CountryInvoiceCount;
import com.acme.backoffice.service.CountryInvoicesService;
import com.acme.backoffice.service.CountryOrdersService;
import com.acme.backoffice.model.SalesRepCustomerCount;
import com.acme.backoffice.model.SalesRepOrderCount;
import com.acme.backoffice.model.SalesRepInvoiceCount;
import com.acme.backoffice.service.SalesRepInvoiceCountService;
import com.acme.backoffice.model.SalesRepShipmentCount;
import com.acme.backoffice.service.SalesRepShipmentCountService;
import com.acme.backoffice.model.SalesRepReturnCount;
import com.acme.backoffice.service.SalesRepReturnCountService;
import com.acme.backoffice.model.SalesRepPurchaseOrderCount;
import com.acme.backoffice.service.SalesRepPurchaseOrderCountService;
import com.acme.backoffice.model.CountryShipmentCount;
import com.acme.backoffice.model.CountryReturnCount;
import com.acme.backoffice.model.CountryPurchaseOrderCount;
import com.acme.backoffice.service.CountryPurchaseOrdersService;
import com.acme.backoffice.service.CountryReturnsService;
import com.acme.backoffice.service.CountryShipmentsService;
import com.acme.backoffice.service.RegionReportService;
import com.acme.backoffice.service.PaymentService;
import com.acme.backoffice.service.TaxService;
import com.acme.backoffice.service.OrderEntryService;
import com.acme.backoffice.service.PriceListService;
import com.acme.backoffice.service.ProductActivationService;
import com.acme.backoffice.service.ReorderService;
import com.acme.backoffice.service.PromotionService;
import com.acme.backoffice.service.PurchaseReceivingService;
import com.acme.backoffice.service.ReturnService;
import com.acme.backoffice.service.ShipmentService;
import com.acme.backoffice.service.PriceHistoryService;
import com.acme.backoffice.service.AuditService;
import com.acme.backoffice.service.CommissionService;
import com.acme.backoffice.service.SalesTargetService;
import com.acme.backoffice.service.StatementService;
import com.acme.backoffice.service.StockAdjustmentService;
import com.acme.backoffice.service.StockService;
import com.acme.backoffice.service.StockValueService;
import com.acme.backoffice.service.SupplierProductService;
import com.acme.backoffice.service.SupportTicketService;
import com.acme.backoffice.service.WarehouseTransferService;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * A fixed demo of BackOffice: creates a small data set and prints what the features show for it.
 * Its output must be deterministic: no current dates, no random values.
 */
public class DemoScenario {
    public static void run(DataStore store, PrintStream out) {
        out.println("BackOffice demo");

        out.println("Customers:");
        Customer zeta = store.createCustomer(new Customer(0, "Zeta Trading", "info@zeta.example", "+1 555 0101", "USA", Region.WEST, CustomerStatus.ACTIVE));
        store.createCustomer(new Customer(0, "Alpha Foods", "sales@alpha.example", "+44 20 7946 0000", "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.updateCustomer(new Customer(zeta.id(), zeta.name(), zeta.email(), zeta.phone(), zeta.country(), Region.EAST, CustomerStatus.ON_HOLD));
        for (Customer customer : store.findNotClosedCustomersByName()) {
            out.println("  " + customer.id() + " " + customer.name() + " " + customer.country() + " " + customer.region() + " " + customer.status());
        }
        out.println("  Found by id " + zeta.id() + ": " + store.findCustomerById(zeta.id()).map(Customer::name).orElse("none"));

        out.println("Customer search:");
        store.createCustomer(new Customer(0, "Alpha Trading", "alpha.t@example.com", null, "UK", Region.NORTH, CustomerStatus.CLOSED));
        out.println("  name 'TRAD': " + store.findNotClosedCustomersWithNameContaining("TRAD").stream().map(Customer::name).toList());
        out.println("  region NORTH: " + store.findNotClosedCustomersInRegion(Region.NORTH).stream().map(Customer::name).toList());
        out.println("  country UK: " + store.findNotClosedCustomersInCountry("UK").stream().map(Customer::name).toList());
        out.println("  status ON_HOLD: " + store.findNotClosedCustomersWithStatus(CustomerStatus.ON_HOLD).stream().map(Customer::name).toList());

        out.println("Products:");
        Product widget = store.createProduct(new Product(0, "WID-001", "Widget", "Hardware", new BigDecimal("4.50"), "piece", true));
        store.createProduct(new Product(0, "APP-010", "Apples", "Food", new BigDecimal("12.00"), "kg", true));
        Product gadget = store.createProduct(new Product(0, "GAD-002", "Gadget", "Hardware", new BigDecimal("30.00"), "piece", true));
        store.updateProduct(new Product(widget.id(), widget.sku(), widget.name(), widget.category(), new BigDecimal("4.75"), widget.unitOfMeasure(), true));
        store.updateProduct(new Product(gadget.id(), gadget.sku(), gadget.name(), gadget.category(), gadget.unitPrice(), gadget.unitOfMeasure(), false));
        for (Product product : store.findActiveProducts()) {
            out.println("  " + product.sku() + " " + product.name() + " " + product.category() + " " + product.unitPrice() + " per " + product.unitOfMeasure());
        }
        out.println("  Found by SKU GAD-002: " + store.findProductBySku("GAD-002").map(p -> p.name() + " active=" + p.active()).orElse("none"));

        out.println("Product search:");
        store.createProduct(new Product(0, "WID-002", "Widget Deluxe", "Hardware", new BigDecimal("9.99"), "piece", true));
        out.println("  name 'WIDG': " + store.findActiveProductsByNameContaining("WIDG").stream().map(Product::name).toList());
        out.println("  category Hardware: " + store.findActiveProductsInCategory("Hardware").stream().map(Product::name).toList());
        out.println("  price 4.75 to 12.00: " + store.findActiveProductsByUnitPriceBetween(new BigDecimal("4.75"), new BigDecimal("12.00")).stream().map(Product::name).toList());

        out.println("Suppliers:");
        Supplier nordic = store.createSupplier(new Supplier(0, "Nordic Supply", "orders@nordic.example", "Sweden", 3));
        store.createSupplier(new Supplier(0, "Acme Parts", "sales@acmeparts.example", "USA", 5));
        store.createSupplier(new Supplier(0, "Baltic Goods", "info@baltic.example", "Sweden", 2));
        store.updateSupplier(new Supplier(nordic.id(), nordic.name(), "purchasing@nordic.example", nordic.country(), 4));
        for (Supplier supplier : store.findAllSuppliers()) {
            out.println("  " + supplier.name() + " " + supplier.contactEmail() + " " + supplier.country() + " " + supplier.rating());
        }
        out.println("  country Sweden: " + store.findSuppliersInCountry("Sweden").stream().map(Supplier::name).toList());
        out.println("  rating at least 4: " + store.findSuppliersWithRatingAtLeast(4).stream().map(Supplier::name).toList());

        out.println("Warehouses:");
        store.createWarehouse(new Warehouse(0, "WH-NORTH", "Northern Hub", "Oslo", Region.NORTH));
        store.createWarehouse(new Warehouse(0, "WH-EAST", "Eastern Depot", "Warsaw", Region.EAST));
        for (Warehouse warehouse : store.findAllWarehousesOrderedByCode()) {
            out.println("  " + warehouse.code() + " " + warehouse.name() + " " + warehouse.city() + " " + warehouse.region());
        }
        out.println("  Found by code WH-EAST: " + store.findWarehouseByCode("WH-EAST").map(Warehouse::name).orElse("none"));
        out.println("  Found by code WH-NONE: " + store.findWarehouseByCode("WH-NONE").map(Warehouse::name).orElse("none"));

        out.println("Stock levels:");
        Warehouse north = store.findWarehouseByCode("WH-NORTH").orElseThrow();
        Warehouse east = store.findWarehouseByCode("WH-EAST").orElseThrow();
        StockService stock = new StockService(store);
        stock.setQuantity(north.id(), widget.id(), 100);
        stock.addQuantity(north.id(), widget.id(), 25);
        stock.addQuantity(east.id(), widget.id(), 10);
        stock.takeQuantity(north.id(), widget.id(), 40);
        out.println("  Widget in WH-NORTH: " + stock.getQuantity(north.id(), widget.id()));
        out.println("  Widget in WH-EAST: " + stock.getQuantity(east.id(), widget.id()));
        out.println("  Gadget in WH-EAST: " + stock.getQuantity(east.id(), gadget.id()));
        try {
            stock.takeQuantity(east.id(), widget.id(), 11);
        } catch (IllegalStateException e) {
            out.println("  Taking 11 Widgets from WH-EAST failed: " + e.getMessage());
        }
        out.println("  Widget in WH-EAST: " + stock.getQuantity(east.id(), widget.id()));

        out.println("Stock overview:");
        stock.setQuantity(north.id(), gadget.id(), 5);
        stock.setQuantity(east.id(), gadget.id(), 7);
        out.println("  Widget in total: " + stock.getTotalQuantity(widget.id()));
        out.println("  Gadget in total: " + stock.getTotalQuantity(gadget.id()));
        for (StockLevel level : stock.getWarehouseStock(north.id())) {
            out.println("  WH-NORTH " + level.product().name() + ": " + level.quantity());
        }
        for (StockLevel level : stock.getProductsBelow(20)) {
            out.println("  Below 20: " + level.product().name() + " " + level.quantity());
        }

        out.println("Sales orders:");
        store.createSalesOrder(new SalesOrder(0, "SO-1001", zeta.id(), LocalDate.of(2024, 3, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("4.75")))));
        store.createSalesOrder(new SalesOrder(0, "SO-1002", zeta.id(), LocalDate.of(2024, 5, 15), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 3, new BigDecimal("4.75")), new OrderLine(gadget.id(), 2, new BigDecimal("30.00")))));
        for (SalesOrder order : store.findSalesOrdersOfCustomer(zeta.id())) {
            out.println("  " + order.number() + " " + order.orderDate() + " " + order.status() + " " + order.lines().size() + " lines");
        }
        SalesOrder found = store.findSalesOrderByNumber("SO-1002").orElseThrow();
        for (OrderLine line : found.lines()) {
            out.println("  " + found.number() + " product " + line.productId() + " x" + line.quantity() + " at " + line.unitPrice());
        }

        out.println("Order status:");
        OrderService orders = new OrderService(store);
        orders.confirm("SO-1002", LocalDate.of(2024, 5, 16));
        orders.ship("SO-1002", LocalDate.of(2024, 5, 20));
        try {
            orders.cancel("SO-1002", CancellationReason.OTHER, LocalDate.of(2024, 5, 21));
        } catch (IllegalStateException e) {
            out.println("  Cancelling SO-1002 failed: " + e.getMessage());
        }
        orders.deliver("SO-1002", LocalDate.of(2024, 5, 25));
        orders.cancel("SO-1001", CancellationReason.CUSTOMER_REQUEST, LocalDate.of(2024, 3, 2));
        for (String number : List.of("SO-1001", "SO-1002")) {
            for (OrderStatusChange change : orders.getHistory(number)) {
                out.println("  " + number + " " + change.status() + " on " + change.changeDate());
            }
        }

        out.println("Open orders:");
        Customer beta = store.createCustomer(new Customer(0, "Beta Retail", "beta@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        store.createSalesOrder(new SalesOrder(0, "SO-1003", zeta.id(), LocalDate.of(2024, 6, 2), OrderStatus.CONFIRMED,
                List.of(new OrderLine(gadget.id(), 1, new BigDecimal("30.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-1004", beta.id(), LocalDate.of(2024, 6, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 5, new BigDecimal("4.75")))));
        store.createSalesOrder(new SalesOrder(0, "SO-1005", beta.id(), LocalDate.of(2024, 6, 3), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.75")))));
        for (SalesOrder order : store.findOpenSalesOrders()) {
            out.println("  All: " + order.number() + " " + order.orderDate());
        }
        for (SalesOrder order : store.findOpenSalesOrdersOfCustomer(zeta.id())) {
            out.println("  Zeta: " + order.number() + " " + order.orderDate());
        }
        for (SalesOrder order : store.findOpenSalesOrdersInRegion(Region.SOUTH)) {
            out.println("  SOUTH: " + order.number() + " " + order.orderDate());
        }

        out.println("Order totals:");
        for (String number : List.of("SO-1001", "SO-1002", "SO-1003")) {
            out.println("  " + number + " total: " + store.sumSalesOrderValue(number));
        }
        out.println("  Zeta total (not cancelled): " + store.sumNotCancelledSalesOrderValueOfCustomer(zeta.id()));
        out.println("  Beta total (not cancelled): " + store.sumNotCancelledSalesOrderValueOfCustomer(beta.id()));

        out.println("Orders by period:");
        for (SalesOrder order : store.findSalesOrdersPlacedBetween(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 2))) {
            out.println("  Jun 1-2: " + order.number() + " " + order.orderDate() + " " + order.status());
        }
        for (SalesOrder order : store.findSalesOrdersPlacedBetweenWithStatus(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), OrderStatus.CONFIRMED)) {
            out.println("  June CONFIRMED: " + order.number() + " " + order.orderDate());
        }

        out.println("Invoices:");
        InvoiceService invoices = new InvoiceService(store);
        Invoice invoice = invoices.invoice("SO-1002", LocalDate.of(2024, 6, 10));
        out.println("  " + invoice.number() + " order " + invoice.orderId() + " " + invoice.invoiceDate() + " due " + invoice.dueDate()
                + " " + invoice.amount() + " " + invoice.status());
        try {
            invoices.invoice("SO-1003", LocalDate.of(2024, 6, 10));
        } catch (IllegalStateException e) {
            out.println("  Invoicing SO-1003 failed: " + e.getMessage());
        }
        out.println("  Found INV-0001: " + invoices.find("INV-0001").map(Invoice::amount).orElse(null));
        for (Invoice listed : invoices.listByCustomer(zeta.id())) {
            out.println("  Zeta: " + listed.number() + " " + listed.amount() + " " + listed.status());
        }

        out.println("Payments:");
        PaymentService payments = new PaymentService(store);
        payments.pay("INV-0001", LocalDate.of(2024, 6, 15), new BigDecimal("20.00"));
        out.println("  Due after first payment: " + payments.amountDue("INV-0001"));
        try {
            payments.pay("INV-0001", LocalDate.of(2024, 6, 16), new BigDecimal("100.00"));
        } catch (IllegalStateException e) {
            out.println("  Overpaying INV-0001 failed: " + e.getMessage());
        }
        payments.pay("INV-0001", LocalDate.of(2024, 6, 20), payments.amountDue("INV-0001"));
        for (Payment payment : payments.listByInvoice("INV-0001")) {
            out.println("  INV-0001 paid " + payment.amount() + " on " + payment.paymentDate());
        }
        out.println("  Due now: " + payments.amountDue("INV-0001"));
        out.println("  INV-0001 status: " + invoices.find("INV-0001").map(Invoice::status).orElse(null));

        out.println("Overdue invoices:");
        orders.ship("SO-1003", LocalDate.of(2024, 5, 1));
        orders.ship("SO-1004", LocalDate.of(2024, 6, 1));
        invoices.invoice("SO-1003", LocalDate.of(2024, 5, 1));
        invoices.invoice("SO-1004", LocalDate.of(2024, 6, 1));
        LocalDate asOf = LocalDate.of(2024, 6, 20);
        for (Invoice overdue : invoices.listOverdue(asOf)) {
            out.println("  All: " + overdue.number() + " due " + overdue.dueDate() + " " + overdue.amount());
        }
        for (Invoice overdue : invoices.listOverdueByCustomer(asOf, zeta.id())) {
            out.println("  Zeta: " + overdue.number() + " due " + overdue.dueDate());
        }
        for (Invoice overdue : invoices.listOverdueByRegion(LocalDate.of(2024, 7, 5), Region.SOUTH)) {
            out.println("  SOUTH on Jul 5: " + overdue.number() + " due " + overdue.dueDate());
        }

        out.println("Carriers:");
        store.createCarrier(new Carrier(0, "RoadRunner", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        store.createCarrier(new Carrier(0, "FastFreight", ServiceLevel.EXPRESS, new BigDecimal("2.50"), 3));
        store.createCarrier(new Carrier(0, "AirDirect", ServiceLevel.EXPRESS, new BigDecimal("3.20")));
        for (Carrier carrier : store.findCarriersOrderedByName()) {
            out.println("  " + carrier.name() + " " + carrier.serviceLevel() + " " + carrier.pricePerKg());
        }
        out.println("  EXPRESS: " + store.findCarriersByServiceLevel(ServiceLevel.EXPRESS).stream().map(Carrier::name).toList());
        out.println("  STANDARD: " + store.findCarriersByServiceLevel(ServiceLevel.STANDARD).stream().map(Carrier::name).toList());
        out.println("  Found by name AirDirect: " + store.findCarrierByName("AirDirect").map(Carrier::pricePerKg).orElse(null));
        out.println("  Found by name Nobody: " + store.findCarrierByName("Nobody").map(Carrier::pricePerKg).orElse(null));

        out.println("Shipments:");
        store.createSalesOrder(new SalesOrder(0, "SO-1006", beta.id(), LocalDate.of(2024, 7, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 5, new BigDecimal("4.75")))));
        store.createSalesOrder(new SalesOrder(0, "SO-1007", beta.id(), LocalDate.of(2024, 7, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 20, new BigDecimal("4.75")))));
        store.createSalesOrder(new SalesOrder(0, "SO-1008", zeta.id(), LocalDate.of(2024, 7, 2), OrderStatus.CONFIRMED,
                List.of(new OrderLine(gadget.id(), 2, new BigDecimal("30.00")))));
        ShipmentService shipments = new ShipmentService(store);
        Shipment shipment = shipments.ship("SO-1006", "WH-NORTH", "FastFreight", "FF-1001", LocalDate.of(2024, 7, 3));
        out.println("  SO-1006 shipped by " + store.findCarrierById(shipment.carrierId()).orElseThrow().name() + " " + shipment.trackingNumber() + " on " + shipment.shipDate());
        out.println("  Widget in WH-NORTH: " + stock.getQuantity(north.id(), widget.id()));
        try {
            shipments.ship("SO-1007", "WH-EAST", "FastFreight", "FF-1002", LocalDate.of(2024, 7, 4));
        } catch (IllegalStateException e) {
            out.println("  Shipping SO-1007 failed: " + e.getMessage());
        }
        shipments.ship("SO-1008", "WH-NORTH", "RoadRunner", "RR-2001", LocalDate.of(2024, 7, 10));
        out.println("  Shipment of SO-1006: " + shipments.findByOrder("SO-1006").map(Shipment::trackingNumber).orElse("none"));
        out.println("  Shipment of SO-1007: " + shipments.findByOrder("SO-1007").map(Shipment::trackingNumber).orElse("none"));
        for (Shipment listed : shipments.listByWarehouse("WH-NORTH", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 5))) {
            out.println("  WH-NORTH Jul 1-5: order " + listed.orderId() + " " + store.findCarrierById(listed.carrierId()).orElseThrow().name() + " " + listed.shipDate());
        }

        out.println("Purchase orders:");
        Supplier baltic = store.findSuppliersInCountry("Sweden").stream().filter(s -> s.name().equals("Baltic Goods")).findFirst().orElseThrow();
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-2001", nordic.id(), LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 15), PurchaseOrderStatus.SENT,
                List.of(new PurchaseOrderLine(widget.id(), 200, new BigDecimal("2.10")), new PurchaseOrderLine(gadget.id(), 50, new BigDecimal("18.00")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-2002", nordic.id(), LocalDate.of(2024, 7, 8), LocalDate.of(2024, 7, 22), PurchaseOrderStatus.DRAFT,
                List.of(new PurchaseOrderLine(widget.id(), 100, new BigDecimal("2.15")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-2003", baltic.id(), LocalDate.of(2024, 7, 5), LocalDate.of(2024, 7, 12), PurchaseOrderStatus.RECEIVED,
                List.of(new PurchaseOrderLine(gadget.id(), 10, new BigDecimal("19.50")))));
        PurchaseOrder purchase = store.findPurchaseOrderByNumber("PO-2001").orElseThrow();
        out.println("  " + purchase.number() + " " + purchase.orderDate() + " expected " + purchase.expectedDate() + " " + purchase.status());
        for (PurchaseOrderLine line : purchase.lines()) {
            out.println("  " + purchase.number() + " product " + line.productId() + " x" + line.quantity() + " at " + line.unitCost());
        }
        out.println("  Found PO-9999: " + store.findPurchaseOrderByNumber("PO-9999").map(PurchaseOrder::number).orElse("none"));
        for (PurchaseOrder listed : store.findPurchaseOrdersOfSupplierNewestFirst(nordic.id())) {
            out.println("  Nordic Supply: " + listed.number() + " " + listed.orderDate() + " " + listed.status() + " " + listed.lines().size() + " lines");
        }

        out.println("Receiving goods:");
        PurchaseReceivingService receiving = new PurchaseReceivingService(store);
        out.println("  Widget in WH-NORTH before: " + stock.getQuantity(north.id(), widget.id()));
        receiving.receive("PO-2001", "WH-NORTH", LocalDate.of(2024, 7, 12));
        out.println("  PO-2001 " + store.findPurchaseOrderByNumber("PO-2001").orElseThrow().status() + " on " + receiving.findReceivedDate("PO-2001").orElse(null));
        out.println("  Widget in WH-NORTH after: " + stock.getQuantity(north.id(), widget.id()));
        out.println("  Gadget in WH-NORTH after: " + stock.getQuantity(north.id(), gadget.id()));
        for (String number : List.of("PO-2001", "PO-2002")) {
            try {
                receiving.receive(number, "WH-NORTH", LocalDate.of(2024, 7, 13));
            } catch (IllegalStateException e) {
                out.println("  Receiving " + number + " failed: " + e.getMessage());
            }
        }
        out.println("  PO-2002 received date: " + receiving.findReceivedDate("PO-2002").map(LocalDate::toString).orElse("none"));

        out.println("Open purchase orders:");
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-2004", baltic.id(), LocalDate.of(2024, 7, 10), LocalDate.of(2024, 7, 20), PurchaseOrderStatus.SENT,
                List.of(new PurchaseOrderLine(widget.id(), 30, new BigDecimal("2.20"))), north.id()));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-2005", nordic.id(), LocalDate.of(2024, 7, 11), LocalDate.of(2024, 8, 5), PurchaseOrderStatus.SENT,
                List.of(new PurchaseOrderLine(gadget.id(), 5, new BigDecimal("18.50"))), north.id()));
        for (PurchaseOrder open : store.findOpenPurchaseOrders()) {
            out.println("  Open: " + open.number() + " expected " + open.expectedDate());
        }
        for (PurchaseOrder open : store.findOpenPurchaseOrdersOfSupplier(nordic.id())) {
            out.println("  Open at Nordic Supply: " + open.number());
        }
        for (PurchaseOrder overdue : store.findOverduePurchaseOrders(LocalDate.of(2024, 7, 25))) {
            out.println("  Overdue on 2024-07-25: " + overdue.number() + " expected " + overdue.expectedDate());
        }
        for (PurchaseOrder open : store.findOpenPurchaseOrdersIntoWarehouse(north.id())) {
            out.println("  Open for WH-NORTH: " + open.number() + " expected " + open.expectedDate());
        }
        for (PurchaseOrder overdue : store.findOverduePurchaseOrdersIntoWarehouse(north.id(), LocalDate.of(2024, 7, 25))) {
            out.println("  Overdue for WH-NORTH on 2024-07-25: " + overdue.number() + " expected " + overdue.expectedDate());
        }
        out.println("  Open for WH-EAST: " + store.findOpenPurchaseOrdersIntoWarehouse(east.id()).size());

        out.println("Sales reps:");
        SalesRep anna = store.createSalesRep(new SalesRep(0, "Anna Berg", "anna@acme.example", Region.NORTH));
        SalesRep carl = store.createSalesRep(new SalesRep(0, "Carl Diaz", "carl@acme.example", Region.WEST));
        store.createSalesRep(new SalesRep(0, "Bea Chen", "bea@acme.example", Region.NORTH));
        for (SalesRep rep : store.findAllSalesRepsOrderedByName()) {
            out.println("  " + rep.name() + " " + rep.email() + " " + rep.region());
        }
        out.println("  NORTH: " + store.findSalesRepsInRegionOrderedByName(Region.NORTH).stream().map(SalesRep::name).toList());
        out.println("  SOUTH: " + store.findSalesRepsInRegionOrderedByName(Region.SOUTH).stream().map(SalesRep::name).toList());
        for (Customer customer : store.findNotClosedCustomersByName()) {
            if (customer.region() == Region.NORTH) {
                store.assignCustomerToSalesRep(customer.id(), anna.id());
            } else if (customer.region() == Region.WEST) {
                store.assignCustomerToSalesRep(customer.id(), carl.id());
            }
        }
        out.println("  Customers of Anna Berg: " + store.findNotClosedCustomersOfSalesRep(anna.id()).stream().map(Customer::name).toList());
        out.println("  Customers of Carl Diaz: " + store.findNotClosedCustomersOfSalesRep(carl.id()).stream().map(Customer::name).toList());

        out.println("Rep performance:");
        for (SalesRep rep : List.of(anna, carl)) {
            RepPerformance performance = store.findSalesRepPerformance(rep.id(), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 31));
            out.println("  " + rep.name() + " 2024-06-01..2024-07-31: " + performance.orderCount() + " orders, "
                    + performance.totalValue());
        }

        out.println("Customer addresses:");
        AddressService addressService = new AddressService(store);
        CustomerAddress billing = addressService.addAddress(zeta.id(), AddressType.BILLING, "1 Main St", "Austin", "73301", "USA");
        CustomerAddress dock = addressService.addAddress(zeta.id(), AddressType.SHIPPING, "9 Dock Rd", "Houston", "77001", "USA");
        CustomerAddress depot = addressService.addAddress(zeta.id(), AddressType.SHIPPING, "4 Depot Ln", "Dallas", "75001", "USA");
        addressService.updateAddress(billing.id(), AddressType.BILLING, "2 Main St", "Austin", "73301", "USA");
        addressService.setDefaultShippingAddress(dock.id());
        addressService.setDefaultShippingAddress(depot.id());
        for (CustomerAddress address : addressService.listAddresses(zeta.id())) {
            out.println("  " + address.type() + " " + address.street() + ", " + address.postalCode() + " " + address.city() + ", "
                    + address.country() + (address.defaultShipping() ? " (default)" : ""));
        }

        out.println("Returns:");
        ReturnService returns = new ReturnService(store);
        out.println("  Widget in WH-NORTH before: " + stock.getQuantity(north.id(), widget.id()));
        returns.createReturn("RET-1", "SO-1002", LocalDate.of(2024, 6, 1), "Damaged in transit", "WH-NORTH",
                List.of(new ReturnLine(widget.id(), 2), new ReturnLine(gadget.id(), 1)));
        returns.createReturn("RET-2", "SO-1002", LocalDate.of(2024, 6, 5), "Wrong item", "WH-NORTH",
                List.of(new ReturnLine(widget.id(), 1)));
        out.println("  Widget in WH-NORTH after: " + stock.getQuantity(north.id(), widget.id()));
        SalesReturn ret = returns.findReturn("RET-1").orElseThrow();
        out.println("  RET-1 " + ret.returnDate() + " " + ret.reason() + " " + ret.lines().size() + " lines");
        for (SalesReturn listed : returns.listByOrder("SO-1002")) {
            out.println("  Order SO-1002: " + listed.number() + " " + listed.returnDate());
        }
        for (SalesReturn listed : returns.listByCustomer(zeta.id())) {
            out.println("  Zeta: " + listed.number() + " " + listed.reason());
        }
        try {
            returns.createReturn("RET-3", "SO-1002", LocalDate.of(2024, 6, 6), "Too many", "WH-NORTH",
                    List.of(new ReturnLine(widget.id(), 1)));
        } catch (IllegalArgumentException e) {
            out.println("  RET-3 failed: " + e.getMessage());
        }
        out.println("Credit notes:");
        for (CreditNote note : returns.listCreditNotes(zeta.id())) {
            out.println("  " + note.number() + " " + note.creditDate() + " " + note.amount());
        }
        out.println("  Total credited to Zeta: " + returns.totalCredited(zeta.id()));

        PromotionService promotions = new PromotionService(store);
        promotions.createPromotion("SUMMER10", new BigDecimal("10"), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 8, 31), null);
        promotions.createPromotion("HW25", new BigDecimal("25"), LocalDate.of(2024, 6, 15), LocalDate.of(2024, 6, 30), "Hardware");
        promotions.createPromotion("HW5", new BigDecimal("5"), LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 30), "Hardware");
        out.println("Promotions:");
        for (Promotion promotion : promotions.listActive(LocalDate.of(2024, 6, 20))) {
            out.println("  Active on 2024-06-20: " + promotion.code() + " " + promotion.discountPercent() + "% "
                    + (promotion.category() == null ? "all products" : promotion.category()));
        }
        for (Promotion promotion : promotions.listByCategory("Hardware")) {
            out.println("  Hardware: " + promotion.code() + " " + promotion.startDate() + " to " + promotion.endDate());
        }

        out.println("Applying promotions:");
        OrderEntryService orderEntry = new OrderEntryService(store);
        Product apples = store.findProductBySku("APP-010").orElseThrow();
        for (LocalDate date : List.of(LocalDate.of(2024, 5, 20), LocalDate.of(2024, 6, 20), LocalDate.of(2024, 7, 20))) {
            SalesOrder placed = orderEntry.placeOrder("SO-P" + date.getMonthValue(), beta.id(), date, OrderStatus.DRAFT,
                    List.of(new OrderLine(widget.id(), 10, new BigDecimal("4.75")), new OrderLine(apples.id(), 2, new BigDecimal("12.00"))));
            for (OrderLine line : placed.lines()) {
                out.println("  " + placed.number() + " " + date + " product " + line.productId() + " at " + line.unitPrice()
                        + " " + (line.promotionCode() == null ? "no promotion" : line.promotionCode()));
            }
        }
        out.println("  Stored SO-P6 first line: " + store.findSalesOrderByNumber("SO-P6").orElseThrow().lines().get(0).promotionCode());

        out.println("Price lists:");
        PriceListService priceLists = new PriceListService(store);
        priceLists.addPrice(beta.id(), widget.id(), new BigDecimal("3.90"), LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        priceLists.addPrice(beta.id(), widget.id(), new BigDecimal("4.10"), LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31));
        for (CustomerPrice price : priceLists.listPrices(beta.id())) {
            out.println("  Beta product " + price.productId() + " " + price.unitPrice() + " " + price.startDate() + " to " + price.endDate());
        }
        for (LocalDate date : List.of(LocalDate.of(2024, 5, 20), LocalDate.of(2024, 6, 20), LocalDate.of(2024, 7, 20), LocalDate.of(2024, 8, 20))) {
            out.println("  Price of widget for Beta on " + date + ": " + priceLists.priceFor(beta.id(), widget.id(), date));
        }
        out.println("  Price of widget for Zeta on 2024-06-20: " + priceLists.priceFor(zeta.id(), widget.id(), LocalDate.of(2024, 6, 20)));
        SalesOrder special = orderEntry.placeOrder("SO-PL1", beta.id(), LocalDate.of(2024, 6, 20), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("4.75"))));
        out.println("  SO-PL1 line at " + special.lines().get(0).unitPrice() + " "
                + (special.lines().get(0).promotionCode() == null ? "no promotion" : special.lines().get(0).promotionCode()));

        out.println("Product categories:");
        CategoryService categories = new CategoryService(store);
        categories.createCategory("Hardware", "Hardware", null);
        categories.createCategory("Food", "Food and drink", null);
        categories.createCategory("Fasteners", "Fasteners", "Hardware");
        for (Category category : categories.listCategories()) {
            out.println("  " + category.code() + " " + category.name() + " parent " + (category.parentCode() == null ? "none" : category.parentCode()));
        }
        out.println("  Children of Hardware: " + categories.listChildren("Hardware").stream().map(Category::code).toList());
        store.createProduct(new Product(0, "SCR-001", "Screw", "Fasteners", new BigDecimal("0.10"), "piece", true));
        out.println("  Products in Hardware: " + categories.listProducts("Hardware").stream().map(Product::name).toList());
        out.println("  Products in Fasteners: " + categories.listProducts("Fasteners").stream().map(Product::name).toList());
        out.println("  Products in Food: " + categories.listProducts("Food").stream().map(Product::name).toList());

        out.println("Low stock alerts:");
        ReorderService reorder = new ReorderService(store);
        stock.setQuantity(north.id(), widget.id(), 50);
        stock.setQuantity(east.id(), widget.id(), 30);
        stock.setQuantity(north.id(), gadget.id(), 5);
        stock.setQuantity(east.id(), gadget.id(), 7);
        reorder.setReorderLevel(north.id(), widget.id(), 50);
        reorder.setReorderLevel(east.id(), widget.id(), 20);
        reorder.setReorderLevel(north.id(), gadget.id(), 10);
        reorder.setReorderLevel(east.id(), gadget.id(), 7);
        for (LowStockAlert alert : reorder.getLowStock(north.id())) {
            out.println("  WH-NORTH " + alert.product().name() + ": " + alert.quantity() + " at reorder level " + alert.reorderLevel());
        }
        for (LowStockAlert alert : reorder.getLowStock()) {
            out.println("  All: " + alert.warehouse().code() + " " + alert.product().name() + " " + alert.quantity()
                    + "/" + alert.reorderLevel());
        }

        out.println("Supplier products:");
        SupplierProductService supplierProducts = new SupplierProductService(store);
        Supplier acmeParts = store.findSuppliersInCountry("USA").get(0);
        supplierProducts.record(nordic.id(), widget.id(), 14, new BigDecimal("2.80"));
        supplierProducts.record(acmeParts.id(), widget.id(), 5, new BigDecimal("3.10"));
        supplierProducts.record(baltic.id(), widget.id(), 21, new BigDecimal("2.50"));
        supplierProducts.record(nordic.id(), gadget.id(), 10, new BigDecimal("18.00"));
        supplierProducts.record(acmeParts.id(), widget.id(), 7, new BigDecimal("2.95"));
        for (SupplierProduct sp : supplierProducts.listSuppliersOfProduct(widget.id())) {
            out.println("  Widget from " + store.findSupplierById(sp.supplierId()).orElseThrow().name() + ": cost "
                    + sp.lastUnitCost() + ", lead time " + sp.leadTimeDays() + " days");
        }
        for (SupplierProduct sp : supplierProducts.listProductsOfSupplier(nordic.id())) {
            out.println("  Nordic Supply delivers " + store.findProductById(sp.productId()).orElseThrow().name() + ": cost "
                    + sp.lastUnitCost() + ", lead time " + sp.leadTimeDays() + " days");
        }

        out.println("Credit limits:");
        CreditService credit = new CreditService(store);
        Customer gamma = store.createCustomer(new Customer(0, "Gamma Stores", "gamma@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        credit.setCreditLimit(gamma.id(), new BigDecimal("100.00"));
        out.println("  Gamma available credit: " + credit.availableCredit(gamma.id()).orElseThrow());
        store.createSalesOrder(new SalesOrder(0, "SO-CR1", gamma.id(), LocalDate.of(2024, 7, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("6.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-CR2", gamma.id(), LocalDate.of(2024, 7, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("5.00")))));
        orders.confirm("SO-CR2", LocalDate.of(2024, 7, 2));
        orders.ship("SO-CR2", LocalDate.of(2024, 7, 3));
        invoices.invoice("SO-CR2", LocalDate.of(2024, 7, 3));
        out.println("  Gamma available credit after invoice: " + credit.availableCredit(gamma.id()).orElseThrow());
        try {
            orders.confirm("SO-CR1", LocalDate.of(2024, 7, 4));
        } catch (IllegalStateException e) {
            out.println("  Confirming SO-CR1 failed: " + e.getMessage());
        }
        out.println("  Zeta available credit: " + credit.availableCredit(zeta.id()).map(BigDecimal::toString).orElse("no limit"));

        out.println("Order notes:");
        OrderNoteService orderNotes = new OrderNoteService(store);
        orderNotes.addNote("SO-CR2", "anna", LocalDate.of(2024, 7, 2), "Customer asked for early delivery");
        orderNotes.addNote("SO-CR2", "ben", LocalDate.of(2024, 7, 3), "Shipped with express carrier");
        orderNotes.addNote("SO-CR1", "anna", LocalDate.of(2024, 7, 5), "Waiting for credit approval");
        orderNotes.addNote("SO-CR2", "anna", LocalDate.of(2024, 7, 9), "Invoice sent");
        for (OrderNote note : orderNotes.listNotes("SO-CR2")) {
            out.println("  SO-CR2 " + note.date() + " " + note.author() + ": " + note.text());
        }
        for (OrderNote note : orderNotes.listNotesByAuthor("anna", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 6))) {
            out.println("  anna until 2024-07-06: " + note.date() + " " + note.text());
        }

        out.println("Customer activity:");
        CustomerActivityService activityService = new CustomerActivityService(store);
        for (Customer customer : List.of(zeta, gamma)) {
            CustomerActivity activity = activityService.activity(customer.id());
            out.println("  " + customer.name() + ": first order " + activity.firstOrderDate() + ", last order "
                    + activity.lastOrderDate() + ", orders " + activity.orderCount() + ", returns " + activity.returnCount()
                    + ", ordered " + activity.totalOrdered() + ", paid " + activity.totalPaid()
                    + ", credited " + activity.totalCredited());
        }

        out.println("Sales per region:");
        for (RegionSales sales : store.findSalesPerRegion(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 31))) {
            out.println("  " + sales.region() + " 2024-06-01..2024-07-31: " + sales.orderCount() + " orders, "
                    + sales.totalValue());
        }

        out.println("Sales per product:");
        for (ProductSales sales : store.findSalesPerProduct(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 31))) {
            out.println("  " + sales.sku() + " " + sales.name() + " 2024-06-01..2024-07-31: " + sales.unitsSold()
                    + " units, " + sales.revenue());
        }
        for (ProductSales sales : store.findSalesPerProductInCategoryTree("Hardware", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 31))) {
            out.println("  Hardware " + sales.sku() + " " + sales.name() + ": " + sales.unitsSold() + " units, "
                    + sales.revenue());
        }

        out.println("Sales per customer:");
        for (CustomerSales sales : store.findSalesPerCustomerWithOrders(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 31))) {
            out.println("  " + sales.name() + " 2024-06-01..2024-07-31: " + sales.orderCount() + " orders, "
                    + sales.revenue());
        }
        for (CustomerSales sales : store.findTopSalesPerCustomerWithOrdersInRegion(Region.NORTH, 2, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 31))) {
            out.println("  Top NORTH " + sales.name() + ": " + sales.orderCount() + " orders, " + sales.revenue());
        }

        out.println("Invoice aging:");
        for (InvoiceAging aging : store.findInvoiceAging(LocalDate.of(2024, 10, 1))) {
            out.println("  " + aging.name() + " on 2024-10-01: not due " + aging.notDue() + ", 1-30 " + aging.days1To30()
                    + ", 31-60 " + aging.days31To60() + ", 61-90 " + aging.days61To90() + ", >90 " + aging.over90());
        }

        out.println("Payments per period:");
        LocalDate paymentsFrom = LocalDate.of(2024, 6, 1);
        LocalDate paymentsTo = LocalDate.of(2024, 6, 30);
        for (PaymentReceived received : store.findPaymentsReceivedBetween(paymentsFrom, paymentsTo)) {
            out.println("  " + received.paymentDate() + " " + received.customerName() + " " + received.invoiceNumber()
                    + ": " + received.amount());
        }
        long payingCustomerId = store.findInvoiceByNumber("INV-0001").orElseThrow().customerId();
        for (PaymentReceived received : store.findPaymentsReceivedFromCustomerBetween(payingCustomerId, paymentsFrom, paymentsTo)) {
            out.println("  Customer of INV-0001 " + received.paymentDate() + ": " + received.amount());
        }
        for (DailyPaymentTotal daily : store.findPaymentTotalsPerDay(paymentsFrom, paymentsTo)) {
            out.println("  Total " + daily.paymentDate() + ": " + daily.paymentCount() + " payments, " + daily.total());
        }

        out.println("Stock movements:");
        LocalDate movementsFrom = LocalDate.of(2024, 1, 1);
        LocalDate movementsTo = LocalDate.of(2024, 12, 31);
        stock.addQuantity(north.id(), widget.id(), 3, LocalDate.of(2024, 11, 1));
        stock.takeQuantity(north.id(), widget.id(), 1, LocalDate.of(2024, 11, 2));
        stock.setQuantity(east.id(), widget.id(), 12, LocalDate.of(2024, 11, 3));
        for (StockMovement movement : stock.getProductMovements(widget.id(), movementsFrom, movementsTo)) {
            out.println("  Widget " + movement.movementDate() + " warehouse " + movement.warehouseId() + " "
                    + movement.reason() + ": " + movement.quantityChange());
        }
        for (StockMovement movement : stock.getWarehouseMovements(east.id(), movementsFrom, movementsTo)) {
            out.println("  WH-EAST " + movement.movementDate() + " product " + movement.productId() + " "
                    + movement.reason() + ": " + movement.quantityChange());
        }
        for (StockMovement movement : stock.getMovements(widget.id(), north.id(), LocalDate.of(2024, 11, 1), movementsTo)) {
            out.println("  Widget in WH-NORTH " + movement.movementDate() + " " + movement.reason() + ": "
                    + movement.quantityChange());
        }

        out.println("Stock adjustments:");
        StockAdjustmentService adjustments = new StockAdjustmentService(store);
        StockAdjustment counted = adjustments.adjust(widget.id(), north.id(), LocalDate.of(2024, 11, 10), 70, "Annual count");
        out.println("  Counted " + counted.countedQuantity() + " (was " + counted.quantityBefore() + ", difference "
                + counted.difference() + "): " + counted.reason());
        adjustments.adjust(widget.id(), east.id(), LocalDate.of(2024, 11, 11), 12, "Recount");
        for (StockAdjustment adjustment : adjustments.getWarehouseAdjustments(north.id(), movementsFrom, movementsTo)) {
            out.println("  WH-NORTH " + adjustment.adjustmentDate() + " product " + adjustment.productId() + ": "
                    + adjustment.quantityBefore() + " -> " + adjustment.countedQuantity());
        }
        out.println("  Widget in WH-NORTH: " + stock.getQuantity(north.id(), widget.id()));
        for (StockMovement movement : stock.getMovements(widget.id(), north.id(), LocalDate.of(2024, 11, 10), movementsTo)) {
            out.println("  Movement " + movement.movementDate() + " " + movement.reason() + ": "
                    + movement.quantityChange());
        }

        out.println("Stock value:");
        StockValueService stockValue = new StockValueService(store);
        for (WarehouseStockValue entry : stockValue.getValuePerWarehouse()) {
            out.println("  " + entry.warehouse().code() + ": " + entry.value());
        }
        out.println("  Total: " + stockValue.getTotalValue());

        out.println("Back orders:");
        store.createSalesOrder(new SalesOrder(0, "SO-2001", zeta.id(), LocalDate.of(2024, 12, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 100000, new BigDecimal("4.75")))));
        store.createSalesOrder(new SalesOrder(0, "SO-2002", beta.id(), LocalDate.of(2024, 12, 2), OrderStatus.CONFIRMED,
                List.of(new OrderLine(gadget.id(), 100000, new BigDecimal("30.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-2003", beta.id(), LocalDate.of(2024, 12, 3), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 100000, new BigDecimal("4.75")))));
        BackOrderService backOrders = new BackOrderService(store);
        backOrders.putOnBackOrder("SO-2003", LocalDate.of(2024, 12, 4));
        backOrders.putOnBackOrder("SO-2001", LocalDate.of(2024, 12, 5));
        backOrders.putOnBackOrder("SO-2002", LocalDate.of(2024, 12, 5));
        for (BackOrder backOrder : backOrders.listAll()) {
            out.println("  " + backOrder.backOrderDate() + " " + backOrder.orderNumber());
        }
        for (BackOrder backOrder : backOrders.listByCustomer(beta.id())) {
            out.println("  Beta " + backOrder.backOrderDate() + " " + backOrder.orderNumber());
        }
        for (BackOrder backOrder : backOrders.listByProduct(widget.id())) {
            out.println("  Widget " + backOrder.backOrderDate() + " " + backOrder.orderNumber());
        }

        out.println("Partial shipments:");
        store.createSalesOrder(new SalesOrder(0, "SO-2101", zeta.id(), LocalDate.of(2024, 12, 10), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 6, new BigDecimal("4.75")), new OrderLine(gadget.id(), 1, new BigDecimal("30.00")))));
        ShipmentService partial = new ShipmentService(store);
        partial.shipPart("SO-2101", 1, 4, "WH-NORTH", "FastFreight", "FF-3001", LocalDate.of(2024, 12, 11));
        out.println("  Status after first part: " + store.findSalesOrderByNumber("SO-2101").orElseThrow().status());
        for (RemainingLine remaining : partial.getRemaining("SO-2101")) {
            out.println("  Line " + remaining.lineNo() + ": ordered " + remaining.ordered() + ", shipped " + remaining.shipped()
                    + ", remaining " + remaining.remaining());
        }
        try {
            partial.shipPart("SO-2101", 1, 3, "WH-NORTH", "FastFreight", "FF-3002", LocalDate.of(2024, 12, 12));
        } catch (IllegalStateException e) {
            out.println("  Shipping part failed: " + e.getMessage());
        }
        partial.shipPart("SO-2101", 2, 1, "WH-NORTH", "RoadRunner", "RR-3003", LocalDate.of(2024, 12, 12));
        partial.shipPart("SO-2101", 1, 2, "WH-NORTH", "RoadRunner", "RR-3004", LocalDate.of(2024, 12, 13));
        out.println("  Status after last part: " + store.findSalesOrderByNumber("SO-2101").orElseThrow().status());
        out.println("  Remaining lines: " + partial.getRemaining("SO-2101").size());
        out.println("  Shipment of SO-2101: " + partial.findByOrder("SO-2101").map(Shipment::trackingNumber).orElse("none"));

        out.println("Delivery tracking:");
        ShipmentService tracking = new ShipmentService(store);
        out.println("  In transit: " + tracking.listInTransit().size());
        tracking.markDelivered("SO-2101", LocalDate.of(2024, 12, 16));
        for (Shipment inTransit : tracking.listInTransit()) {
            out.println("  Transit " + inTransit.shipDate() + " " + inTransit.trackingNumber());
        }
        for (Shipment carried : tracking.listInTransitByCarrier("FastFreight")) {
            out.println("  FastFreight " + carried.shipDate() + " " + carried.trackingNumber());
        }
        for (Shipment late : tracking.listInTransitLongerThan(30, LocalDate.of(2025, 1, 20))) {
            out.println("  Over 30 days on 2025-01-20: " + late.trackingNumber());
        }

        out.println("Carrier performance:");
        store.createSalesOrder(new SalesOrder(0, "SO-2201", zeta.id(), LocalDate.of(2024, 12, 14), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.75")))));
        store.createSalesOrder(new SalesOrder(0, "SO-2202", zeta.id(), LocalDate.of(2024, 12, 14), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.75")))));
        ShipmentService performance = new ShipmentService(store);
        performance.ship("SO-2201", "WH-NORTH", "FastFreight", "FF-4001", LocalDate.of(2024, 12, 15));
        performance.ship("SO-2202", "WH-NORTH", "FastFreight", "FF-4002", LocalDate.of(2024, 12, 15));
        performance.markDelivered("SO-2201", LocalDate.of(2024, 12, 17));
        performance.markDelivered("SO-2202", LocalDate.of(2024, 12, 30));
        for (CarrierPerformance entry : performance.getCarrierPerformance(LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 31))) {
            out.println("  " + entry.carrier().name() + " (promises " + entry.carrier().promisedDays() + " days): "
                    + entry.shipmentCount() + " shipments, average " + entry.averageDaysToDeliver() + " days, "
                    + entry.lateCount() + " late");
        }

        out.println("Supplier performance:");
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-2006", baltic.id(), LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 10), PurchaseOrderStatus.SENT,
                List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.20")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-2007", baltic.id(), LocalDate.of(2024, 8, 2), LocalDate.of(2024, 8, 12), PurchaseOrderStatus.SENT,
                List.of(new PurchaseOrderLine(widget.id(), 10, new BigDecimal("2.20")))));
        receiving.receive("PO-2006", "WH-NORTH", LocalDate.of(2024, 8, 14));
        receiving.receive("PO-2007", "WH-NORTH", LocalDate.of(2024, 8, 11));
        for (SupplierPerformance entry : receiving.getSupplierPerformance(LocalDate.of(2024, 7, 1), LocalDate.of(2024, 8, 31))) {
            out.println("  " + entry.supplier().name() + ": " + entry.receivedCount() + " received, average delay "
                    + entry.averageDelayDays() + " days, " + entry.lateShare() + "% late");
        }

        out.println("Supplier spending:");
        for (SupplierSpending entry : store.findReceivedPurchaseSpendingPerSupplier(LocalDate.of(2024, 7, 1), LocalDate.of(2024, 8, 31))) {
            out.println("  " + entry.supplier().name() + " 2024-07..08: " + entry.orderCount() + " orders, " + entry.totalValue());
        }
        for (CountrySpending entry : store.findReceivedPurchaseSpendingPerSupplierCountry(LocalDate.of(2024, 7, 1), LocalDate.of(2024, 8, 31))) {
            out.println("  " + entry.country() + " 2024-07..08: " + entry.orderCount() + " orders, " + entry.totalValue());
        }

        out.println("Purchase suggestions:");
        stock.setQuantity(north.id(), widget.id(), 30);
        stock.setQuantity(north.id(), gadget.id(), 4);
        for (PurchaseSuggestion suggestion : reorder.getPurchaseSuggestions(north.id())) {
            out.println("  WH-NORTH " + suggestion.product().name() + ": order " + suggestion.suggestedQuantity() + " from "
                    + (suggestion.supplier() == null ? "no supplier" : suggestion.supplier().name() + " at " + suggestion.unitCost()));
        }

        out.println("Customer segments:");
        CustomerSegmentService customerSegments = new CustomerSegmentService(store);
        LocalDate segmentDate = LocalDate.of(2025, 1, 1);
        out.println("  Counts on " + segmentDate + ": " + customerSegments.countPerSegment(segmentDate));
        for (CustomerSegment segment : CustomerSegment.values()) {
            for (CustomerSales sales : customerSegments.listCustomers(segment, segmentDate)) {
                out.println("  " + segment + " " + sales.name() + " " + sales.revenue());
            }
        }

        out.println("Sales rep customers report:");
        for (com.acme.backoffice.model.SalesRepCustomerReportRow row : customerSegments.salesRepCustomerReport(anna.id(), segmentDate)) {
            out.println("  " + row.name() + " " + row.segment() + " " + row.lastOrderDate() + " " + row.openInvoiceAmount());
        }

        out.println("Customers per segment and region:");
        for (SegmentRegionCustomers group : customerSegments.listPerSegmentAndRegion(segmentDate)) {
            out.println("  " + group.segment() + " " + group.region() + ": " + group.customerCount());
            for (CustomerRegionSales sales : group.customers()) {
                out.println("    " + sales.name() + " " + sales.revenue());
            }
        }

        out.println("Dormant customers:");
        DormantCustomerService dormantCustomers = new DormantCustomerService(store);
        LocalDate dormantDate = LocalDate.of(2025, 1, 1);
        for (Map.Entry<Region, List<Customer>> entry : dormantCustomers.listDormantPerRegion(dormantDate, 90).entrySet()) {
            out.println("  " + entry.getKey() + ": " + entry.getValue().stream().map(Customer::name).toList());
        }
        out.println("  Total: " + dormantCustomers.listDormant(dormantDate, 90).size());

        out.println("Order search:");
        out.println("  All: " + store.findSalesOrdersMatching(null, null, null, null, null).stream().map(SalesOrder::number).toList());
        out.println("  WEST: " + store.findSalesOrdersMatching(null, null, Region.WEST, null, null).stream().map(SalesOrder::number).toList());
        out.println("  Zeta in 2024: " + store.findSalesOrdersMatching(zeta.id(), null, null, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))
                .stream().map(SalesOrder::number).toList());
        out.println("  DELIVERED in 2024-06: " + store.findSalesOrdersMatching(null, OrderStatus.DELIVERED, null, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30))
                .stream().map(SalesOrder::number).toList());

        out.println("Invoice search:");
        out.println("  All: " + store.findInvoicesByCriteria(null, null, null, null, null).stream().map(Invoice::number).toList());
        out.println("  Zeta: " + store.findInvoicesByCriteria(zeta.id(), null, null, null, null).stream().map(Invoice::number).toList());
        out.println("  OPEN due in 2024-06: " + store.findInvoicesByCriteria(null, InvoiceStatus.OPEN, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30), null)
                .stream().map(Invoice::number).toList());
        out.println("  Minimum amount 100: " + store.findInvoicesByCriteria(null, null, null, null, new BigDecimal("100"))
                .stream().map(Invoice::number).toList());

        out.println("Price history:");
        PriceHistoryService priceHistory = new PriceHistoryService(store);
        priceHistory.changePrice(widget.id(), LocalDate.of(2025, 2, 1), new BigDecimal("6.00"));
        priceHistory.changePrice(widget.id(), LocalDate.of(2025, 4, 1), new BigDecimal("5.50"));
        priceHistory.changePrice(gadget.id(), LocalDate.of(2025, 4, 15), new BigDecimal("30.00"));
        for (PriceChange change : priceHistory.getHistory(widget.id())) {
            out.println("  " + widget.name() + " " + change.changeDate() + ": " + change.oldPrice() + " -> " + change.newPrice());
        }
        out.println("  Changed in 2025-01..2025-03: " + priceHistory.getProductsWithPriceChange(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 31))
                .stream().map(Product::name).toList());
        out.println("  Changed in 2025-04: " + priceHistory.getProductsWithPriceChange(LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 30))
                .stream().map(Product::name).toList());

        out.println("Currency:");
        CurrencyService currencies = new CurrencyService(store);
        currencies.setRate(Currency.USD, LocalDate.of(2025, 1, 1), new BigDecimal("1.100000"));
        currencies.setRate(Currency.USD, LocalDate.of(2025, 6, 1), new BigDecimal("1.200000"));
        currencies.setRate(Currency.GBP, LocalDate.of(2025, 1, 1), new BigDecimal("0.850000"));
        out.println("  USD rate on 2025-03-15: " + store.findExchangeRate(Currency.USD, LocalDate.of(2025, 3, 15)).orElseThrow().perEur());
        out.println("  USD rate on 2025-06-01: " + store.findExchangeRate(Currency.USD, LocalDate.of(2025, 6, 1)).orElseThrow().perEur());
        Customer delta = store.createCustomer(new Customer(0, "Delta Imports", "delta@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        currencies.setCustomerCurrency(delta.id(), Currency.USD);
        store.createSalesOrder(new SalesOrder(0, "SO-CUR1", delta.id(), LocalDate.of(2025, 3, 1), OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-CUR2", delta.id(), LocalDate.of(2025, 7, 1), OrderStatus.SHIPPED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("5.00")))));
        Invoice usdInvoice = currencies.invoice("SO-CUR1", LocalDate.of(2025, 3, 15), null);
        Invoice gbpInvoice = currencies.invoice("SO-CUR2", LocalDate.of(2025, 7, 15), Currency.GBP);
        for (Invoice currencyInvoice : List.of(usdInvoice, gbpInvoice)) {
            InvoiceCurrency recorded = currencies.getInvoiceCurrency(currencyInvoice);
            out.println("  " + currencyInvoice.number() + ": " + currencyInvoice.amount() + " EUR = " + recorded.amount() + " " + recorded.currency());
        }
        CustomerOpenAmount open = currencies.openAmount(delta.id(), LocalDate.of(2025, 7, 20));
        out.println("  Delta open amount: " + open.amount() + " " + open.currency());
        new PaymentService(store).pay(usdInvoice.number(), LocalDate.of(2025, 4, 1), new BigDecimal("40.00"));
        open = currencies.openAmount(delta.id(), LocalDate.of(2025, 7, 20));
        out.println("  Delta open amount after paying 40.00 EUR: " + open.amount() + " " + open.currency());

        out.println("Tax rates:");
        TaxService taxes = new TaxService(store);
        taxes.setRate("USA", LocalDate.of(2025, 1, 1), new BigDecimal("20.00"));
        taxes.setRate("USA", LocalDate.of(2025, 6, 1), new BigDecimal("25.00"));
        for (Invoice taxInvoice : List.of(usdInvoice, gbpInvoice)) {
            InvoiceTax tax = taxes.getInvoiceTax(taxInvoice);
            out.println("  " + taxInvoice.number() + " (" + taxInvoice.invoiceDate() + "): net " + tax.net() + ", VAT " + tax.vat()
                    + " at " + tax.ratePercent() + "%, gross " + tax.gross());
        }

        out.println("Discount approvals:");
        DiscountApprovalService approvals = new DiscountApprovalService(store);
        for (String number : List.of("SO-APP1", "SO-APP2", "SO-APP3")) {
            store.createSalesOrder(new SalesOrder(0, number, delta.id(), LocalDate.of(2025, 8, 1), OrderStatus.DRAFT,
                    List.of(new OrderLine(widget.id(), 2, new BigDecimal("5.00")))));
        }
        approvals.setLineDiscount("SO-APP1", 1, new BigDecimal("20.00"));
        approvals.setLineDiscount("SO-APP2", 1, new BigDecimal("30.00"));
        approvals.setLineDiscount("SO-APP3", 1, new BigDecimal("15.00"));
        out.println("  Waiting: " + approvals.listAwaitingApproval().stream().map(SalesOrder::number).toList());
        OrderService approvalOrders = new OrderService(store);
        try {
            approvalOrders.confirm("SO-APP1", LocalDate.of(2025, 8, 2));
        } catch (IllegalStateException e) {
            out.println("  Refused: " + e.getMessage());
        }
        OrderApproval approved = approvals.approve("SO-APP1", "Maria", LocalDate.of(2025, 8, 3));
        OrderApproval rejected = approvals.reject("SO-APP2", "Maria", LocalDate.of(2025, 8, 3));
        out.println("  SO-APP1 approved=" + approved.approved() + " by " + approved.approver() + " on " + approved.date());
        out.println("  SO-APP2 approved=" + rejected.approved() + " by " + rejected.approver() + " on " + rejected.date());
        out.println("  Waiting: " + approvals.listAwaitingApproval().stream().map(SalesOrder::number).toList());
        out.println("  SO-APP1 " + approvalOrders.confirm("SO-APP1", LocalDate.of(2025, 8, 4)).status());
        out.println("  SO-APP3 " + approvalOrders.confirm("SO-APP3", LocalDate.of(2025, 8, 4)).status());

        out.println("Customer contacts:");
        ContactService contactService = new ContactService(store);
        contactService.addContact(zeta.id(), "Ann Lee", "Buyer", "ann@zeta.example", "+1 555 0111");
        contactService.addContact(zeta.id(), "Bob Ray", "Accountant", "bob@zeta.example", null);
        contactService.addContact(delta.id(), "Cy Dunn", "buyer", "cy@delta.example", "+1 555 0122");
        for (CustomerContact contact : contactService.listContacts(zeta.id())) {
            out.println("  " + contact.name() + " (" + contact.role() + ") " + contact.email() + " " + contact.phone());
        }
        out.println("  Buyers: " + contactService.findByRole("Buyer").stream().map(CustomerContact::name).toList());
        out.println("  By email CY@delta.example: "
                + contactService.findByEmail("CY@delta.example").map(CustomerContact::name).orElse("none"));
        out.println("  By email nobody@x.example: "
                + contactService.findByEmail("nobody@x.example").map(CustomerContact::name).orElse("none"));

        out.println("Support tickets:");
        SupportTicketService tickets = new SupportTicketService(store);
        tickets.create("T-1", zeta.id(), "SO-1001", "Damaged goods", TicketPriority.HIGH, LocalDate.of(2025, 9, 1));
        tickets.create("T-2", zeta.id(), null, "Question about invoice", TicketPriority.NORMAL, LocalDate.of(2025, 9, 2));
        tickets.create("T-3", delta.id(), null, "Late delivery", TicketPriority.HIGH, LocalDate.of(2025, 9, 3));
        tickets.create("T-4", zeta.id(), null, "Catalogue request", TicketPriority.LOW, LocalDate.of(2025, 9, 4));
        out.println("  T-2 " + tickets.changeStatus("T-2", TicketStatus.IN_PROGRESS, LocalDate.of(2025, 9, 5)).status());
        SupportTicket closedTicket = tickets.changeStatus("T-4", TicketStatus.CLOSED, LocalDate.of(2025, 9, 6));
        out.println("  T-4 " + closedTicket.status() + " on " + closedTicket.closedDate());
        out.println("  Open for Zeta: " + tickets.listOpenByCustomer(zeta.id()).stream().map(SupportTicket::number).toList());
        out.println("  Open high: " + tickets.listOpenByPriority(TicketPriority.HIGH).stream().map(SupportTicket::number).toList());
        out.println("  Open low: " + tickets.listOpenByPriority(TicketPriority.LOW).stream().map(SupportTicket::number).toList());

        out.println("Ticket reports:");
        out.println("  Open older than 3 days on 2025-09-06: "
                + tickets.listOpenOlderThan(LocalDate.of(2025, 9, 6), 3).stream().map(SupportTicket::number).toList());
        out.println("  Open older than 3 days on 2025-09-05: "
                + tickets.listOpenOlderThan(LocalDate.of(2025, 9, 5), 3).stream().map(SupportTicket::number).toList());
        tickets.countPerStatus().forEach(c -> out.println("  " + c.status() + ": " + c.count()));
        tickets.averageDaysToClose(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 30))
                .forEach(t -> out.println("  Average days to close " + t.priority() + ": " + t.averageDays() + " (" + t.closedCount() + ")"));

        out.println("Warehouse transfers:");
        WarehouseTransferService transfers = new WarehouseTransferService(store);
        stock.setQuantity(north.id(), widget.id(), 50);
        stock.setQuantity(east.id(), widget.id(), 5);
        stock.setQuantity(north.id(), gadget.id(), 8);
        stock.setQuantity(east.id(), gadget.id(), 0);
        transfers.transfer("TR-1", north.id(), east.id(), LocalDate.of(2025, 10, 1),
                List.of(new WarehouseTransferLine(widget.id(), 20), new WarehouseTransferLine(gadget.id(), 3)));
        transfers.transfer("TR-2", east.id(), north.id(), LocalDate.of(2025, 10, 8),
                List.of(new WarehouseTransferLine(widget.id(), 5)));
        out.println("  Widget in WH-NORTH: " + stock.getQuantity(north.id(), widget.id()));
        out.println("  Widget in WH-EAST: " + stock.getQuantity(east.id(), widget.id()));
        out.println("  Gadget in WH-NORTH: " + stock.getQuantity(north.id(), gadget.id()));
        out.println("  Gadget in WH-EAST: " + stock.getQuantity(east.id(), gadget.id()));
        try {
            transfers.transfer("TR-3", east.id(), north.id(), LocalDate.of(2025, 10, 9),
                    List.of(new WarehouseTransferLine(gadget.id(), 4)));
        } catch (IllegalStateException e) {
            out.println("  TR-3 rejected, stock short");
        }
        for (WarehouseTransfer t : transfers.listByWarehouse(east.id(), LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31))) {
            out.println("  WH-EAST " + t.number() + " " + t.transferDate() + " lines " + t.lines().size());
        }
        out.println("  Transfers 2025-10-05..2025-10-31: "
                + transfers.listByPeriod(LocalDate.of(2025, 10, 5), LocalDate.of(2025, 10, 31)).stream()
                .map(WarehouseTransfer::number).toList());
        out.println("  Widget movements in WH-EAST: " + stock.getMovements(widget.id(), east.id(),
                LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31)).stream()
                .map(m -> m.quantityChange() + " " + m.reason()).toList());

        out.println("Product bundles:");
        BundleService bundles = new BundleService(store);
        Product starterKit = store.createProduct(new Product(0, "KIT-001", "Starter Kit", "Kits", new BigDecimal("55.00"), "piece", true));
        bundles.addComponent(starterKit.id(), widget.id(), 4);
        bundles.addComponent(starterKit.id(), gadget.id(), 1);
        stock.setQuantity(north.id(), widget.id(), 14);
        stock.setQuantity(north.id(), gadget.id(), 5);
        stock.setQuantity(east.id(), widget.id(), 9);
        stock.setQuantity(east.id(), gadget.id(), 3);
        out.println("  Bundles: " + bundles.listBundles().stream().map(Product::name).toList());
        out.println("  Components: " + bundles.getComponents(starterKit.id()).stream()
                .map(c -> c.quantity() + " x " + c.component().name()).toList());
        out.println("  Starter Kit in WH-NORTH: " + bundles.getQuantity(north.id(), starterKit.id()));
        out.println("  Starter Kit in WH-EAST: " + bundles.getQuantity(east.id(), starterKit.id()));
        out.println("  Starter Kit in total: " + bundles.getTotalQuantity(starterKit.id()));
        store.createSalesOrder(new SalesOrder(0, "SO-BUNDLE", zeta.id(), LocalDate.of(2025, 11, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(starterKit.id(), 3, new BigDecimal("55.00")))));
        shipments.ship("SO-BUNDLE", "WH-NORTH", "FastFreight", "FF-BUNDLE", LocalDate.of(2025, 11, 2));
        out.println("  Widget in WH-NORTH after shipping 3 kits: " + stock.getQuantity(north.id(), widget.id()));
        out.println("  Gadget in WH-NORTH after shipping 3 kits: " + stock.getQuantity(north.id(), gadget.id()));
        out.println("  Starter Kit in WH-NORTH after shipping: " + bundles.getQuantity(north.id(), starterKit.id()));

        out.println("Order cancellation reasons:");
        for (String number : List.of("SO-CANC-1", "SO-CANC-2", "SO-CANC-3")) {
            store.createSalesOrder(new SalesOrder(0, number, number.equals("SO-CANC-3") ? beta.id() : zeta.id(),
                    LocalDate.of(2025, 12, 1), OrderStatus.DRAFT, List.of(new OrderLine(widget.id(), 1, new BigDecimal("4.75")))));
        }
        orders.cancel("SO-CANC-1", CancellationReason.OUT_OF_STOCK, LocalDate.of(2025, 12, 2));
        orders.cancel("SO-CANC-2", CancellationReason.OUT_OF_STOCK, LocalDate.of(2025, 12, 5));
        orders.cancel("SO-CANC-3", CancellationReason.DUPLICATE, LocalDate.of(2025, 12, 20));
        try {
            orders.cancel("SO-CANC-3", null, LocalDate.of(2025, 12, 21));
        } catch (IllegalArgumentException e) {
            out.println("  Cancelling without reason failed: " + e.getMessage());
        }
        for (CancellationCount count : orders.getCancellations(LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 31))) {
            out.println("  " + count.reason() + " " + count.name() + ": " + count.orderCount());
        }
        out.println("  Up to 2025-12-10: " + orders.getCancellations(LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 10)).stream()
                .map(c -> c.reason() + "/" + c.name() + "=" + c.orderCount()).toList());

        out.println("Customer statements:");
        Customer statementCustomer = store.createCustomer(new Customer(0, "Delta Traders", "delta@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        LocalDate[] invoiceDates = {LocalDate.of(2026, 1, 10), LocalDate.of(2026, 2, 5), LocalDate.of(2026, 2, 20)};
        String[] invoiceAmounts = {"300.00", "150.00", "80.00"};
        List<Invoice> statementInvoices = new java.util.ArrayList<>();
        for (int n = 0; n < invoiceDates.length; n++) {
            SalesOrder statementOrder = store.createSalesOrder(new SalesOrder(0, "SO-ST-" + (n + 1), statementCustomer.id(),
                    invoiceDates[n], OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
            statementInvoices.add(store.createInvoice(new Invoice(0, "INV-ST-" + (n + 1), statementOrder.id(), statementCustomer.id(),
                    invoiceDates[n], invoiceDates[n].plusDays(30), new BigDecimal(invoiceAmounts[n]), InvoiceStatus.OPEN)));
        }
        store.createPayment(new Payment(0, statementInvoices.get(0).id(), LocalDate.of(2026, 1, 25), new BigDecimal("100.00")));
        store.createPayment(new Payment(0, statementInvoices.get(0).id(), LocalDate.of(2026, 2, 12), new BigDecimal("200.00")));
        SalesOrder statementReturnOrder = store.createSalesOrder(new SalesOrder(0, "SO-ST-R", statementCustomer.id(),
                LocalDate.of(2026, 1, 12), OrderStatus.DELIVERED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
        SalesReturn statementReturn = store.createSalesReturn(new SalesReturn(0, "R-ST-1", statementReturnOrder.id(),
                LocalDate.of(2026, 2, 15), "Damaged", List.of(new ReturnLine(widget.id(), 1))), north.id());
        store.createCreditNote(new CreditNote(0, "CN-ST-1", statementCustomer.id(), statementReturn.id(),
                LocalDate.of(2026, 2, 15), new BigDecimal("25.00")));
        CustomerStatement statement = new StatementService(store).statement(statementCustomer.id(),
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28));
        out.println("  " + statement.customerName() + " " + statement.from() + " to " + statement.to());
        out.println("  Opening balance: " + statement.openingBalance());
        for (StatementEntry entry : statement.entries()) {
            out.println("  " + entry.date() + " " + entry.type() + " " + entry.reference() + " " + entry.amount()
                    + " balance " + entry.balance());
        }
        out.println("  Closing balance: " + statement.closingBalance());

        out.println("Sales targets:");
        SalesRep targetAnna = store.createSalesRep(new SalesRep(0, "Target Anna", "tanna@acme.example", Region.NORTH));
        SalesRep targetBob = store.createSalesRep(new SalesRep(0, "Target Bob", "tbob@acme.example", Region.WEST));
        Customer targetCustomerA = store.createCustomer(new Customer(0, "Target Cust A", "tca@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer targetCustomerB = store.createCustomer(new Customer(0, "Target Cust B", "tcb@example.com", null, "USA", Region.WEST, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(targetCustomerA.id(), targetAnna.id());
        store.assignCustomerToSalesRep(targetCustomerB.id(), targetBob.id());
        store.createSalesOrder(new SalesOrder(0, "SO-TG-1", targetCustomerA.id(), LocalDate.of(2026, 3, 5), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 10, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-TG-2", targetCustomerA.id(), LocalDate.of(2026, 3, 31), OrderStatus.DELIVERED,
                List.of(new OrderLine(widget.id(), 5, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-TG-3", targetCustomerA.id(), LocalDate.of(2026, 3, 10), OrderStatus.CANCELLED,
                List.of(new OrderLine(widget.id(), 50, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-TG-4", targetCustomerB.id(), LocalDate.of(2026, 3, 12), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 7, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-TG-5", targetCustomerB.id(), LocalDate.of(2026, 4, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(widget.id(), 100, new BigDecimal("10.00")))));
        SalesTargetService targets = new SalesTargetService(store);
        targets.setTarget(targetAnna.id(), YearMonth.of(2026, 3), new BigDecimal("140.00"));
        targets.setTarget(targetBob.id(), YearMonth.of(2026, 3), new BigDecimal("100.00"));
        targets.setTarget(targetBob.id(), YearMonth.of(2026, 3), new BigDecimal("125.00"));
        try {
            targets.setTarget(targetBob.id(), YearMonth.of(2026, 3), BigDecimal.ZERO);
        } catch (IllegalArgumentException e) {
            out.println("  Zero target failed: " + e.getMessage());
        }
        for (SalesTargetProgress p : targets.progress(YearMonth.of(2026, 3))) {
            out.println("  " + p.salesRepName() + " target " + p.target() + " revenue " + p.revenue() + " reached " + p.percentReached() + "%");
        }
        out.println("  Below 80%: " + targets.repsBelowTarget(YearMonth.of(2026, 3)).stream().map(SalesTargetProgress::salesRepName).toList());

        out.println("Commission:");
        SalesRep commissionCarl = store.createSalesRep(new SalesRep(0, "Commission Carl", "ccarl@acme.example", Region.NORTH));
        Customer commissionCustomer = store.createCustomer(new Customer(0, "Commission Cust", "cc@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(commissionCustomer.id(), commissionCarl.id());
        String[] commissionAmounts = {"600.00", "300.00", "500.00", "900.00", "400.00"};
        LocalDate[] commissionDates = {LocalDate.of(2026, 5, 3), LocalDate.of(2026, 5, 31), LocalDate.of(2026, 5, 15),
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 5, 20)};
        InvoiceStatus[] commissionStatuses = {InvoiceStatus.PAID, InvoiceStatus.PAID, InvoiceStatus.OPEN, InvoiceStatus.PAID, InvoiceStatus.CANCELLED};
        for (int n = 0; n < commissionAmounts.length; n++) {
            SalesOrder commissionOrder = store.createSalesOrder(new SalesOrder(0, "SO-CM-" + (n + 1), commissionCustomer.id(),
                    commissionDates[n], OrderStatus.SHIPPED, List.of(new OrderLine(widget.id(), 1, new BigDecimal("1.00")))));
            store.createInvoice(new Invoice(0, "INV-CM-" + (n + 1), commissionOrder.id(), commissionCustomer.id(),
                    commissionDates[n], commissionDates[n].plusDays(30), new BigDecimal(commissionAmounts[n]), commissionStatuses[n]));
        }
        targets.setTarget(commissionCarl.id(), YearMonth.of(2026, 5), new BigDecimal("500.00"));
        CommissionService commissions = new CommissionService(store);
        RepCommission carlCommission = commissions.commissionOf(commissionCarl.id(), YearMonth.of(2026, 5));
        out.println("  " + carlCommission.salesRepName() + " paid " + carlCommission.paidAmount() + " target " + carlCommission.target()
                + " commission " + carlCommission.commission());
        for (RepCommission c : commissions.commissionOfAll(YearMonth.of(2026, 5))) {
            out.println("  " + c.salesRepName() + " commission " + c.commission());
        }

        out.println("Audit log:");
        AuditService audit = new AuditService(store);
        audit.record("alice", LocalDate.of(2026, 7, 1), AuditEntityType.CUSTOMER, commissionCustomer.id(), "Changed email");
        audit.record("bob", LocalDate.of(2026, 7, 2), AuditEntityType.CUSTOMER, commissionCustomer.id(), "Set status to ACTIVE");
        audit.record("alice", LocalDate.of(2026, 7, 3), AuditEntityType.INVOICE, 1, "Marked invoice as paid");
        audit.record("alice", LocalDate.of(2026, 8, 1), AuditEntityType.ORDER, 1, "Cancelled order");
        for (AuditEntry e : audit.listEntries(AuditEntityType.CUSTOMER, commissionCustomer.id())) {
            out.println("  " + e.date() + " " + e.user() + " " + e.entityType() + " " + e.description());
        }
        for (AuditEntry e : audit.listEntriesByUser("alice", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31))) {
            out.println("  alice in July: " + e.date() + " " + e.entityType() + " #" + e.entityId() + " " + e.description());
        }
        for (AuditEntry e : audit.listEntriesByEntityType(AuditEntityType.CUSTOMER, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31))) {
            out.println("  customer entries in July: " + e.date() + " " + e.user() + " " + e.description());
        }

        out.println("Archiving:");
        Customer archiveCustomer = store.createCustomer(new Customer(0, "Archive Cust", "ar@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        OrderService archiveOrders = new OrderService(store);
        for (String number : List.of("SO-AR-1", "SO-AR-2", "SO-AR-3")) {
            store.createSalesOrder(new SalesOrder(0, number, archiveCustomer.id(), LocalDate.of(2023, 1, 5), OrderStatus.DRAFT,
                    List.of(new OrderLine(widget.id(), 1, new BigDecimal("2.00")))));
        }
        archiveOrders.cancel("SO-AR-1", CancellationReason.CREDIT, LocalDate.of(2023, 1, 10));
        archiveOrders.confirm("SO-AR-2", LocalDate.of(2023, 1, 6));
        archiveOrders.ship("SO-AR-2", LocalDate.of(2023, 1, 7));
        archiveOrders.deliver("SO-AR-2", LocalDate.of(2023, 1, 9));
        archiveOrders.cancel("SO-AR-3", CancellationReason.OTHER, LocalDate.of(2025, 1, 10));
        out.println("  archived " + archiveOrders.archive(LocalDate.of(2026, 7, 1)));
        for (SalesOrder o : archiveOrders.listArchived()) {
            out.println("  archived order " + o.number() + " " + o.status());
        }
        out.println("  remaining orders of customer: " + store.findSalesOrdersOfCustomer(archiveCustomer.id()).size());
        for (SalesOrder o : archiveOrders.listArchivedByCustomer(archiveCustomer.id())) {
            out.println("  archived of customer: " + o.number());
        }
        for (SalesOrder o : archiveOrders.listArchivedByRegion(Region.NORTH)) {
            out.println("  archived in NORTH: " + o.number());
        }
        for (com.acme.backoffice.model.ArchivedYearRevenue y : archiveOrders.archivedRevenuePerYear()) {
            out.println("  archived revenue " + y.year() + ": " + y.revenue());
        }

        out.println("Customer closing:");
        CustomerClosureService closures = new CustomerClosureService(store);
        Customer closable = store.createCustomer(new Customer(0, "Closable Cust", "cl@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.createSalesOrder(new SalesOrder(0, "SO-CL-1", closable.id(), LocalDate.of(2026, 3, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("2.00")))));
        out.println("  closed: " + closures.close(closable.id(), LocalDate.of(2026, 8, 1)));
        out.println("  listed after closing: " + store.findNotClosedCustomersByName().stream().anyMatch(c -> c.id() == closable.id()));
        out.println("  found by search: " + store.findNotClosedCustomersWithNameContaining("Closable").size());
        out.println("  closed customers: " + closures.listClosed().stream().map(Customer::name).toList());
        out.println("  orders kept: " + store.findSalesOrdersOfCustomer(closable.id()).size());
        out.println("  reopened: " + closures.reopen(closable.id()));
        out.println("  found by search after reopening: " + store.findNotClosedCustomersWithNameContaining("Closable").size());

        out.println("Product deactivation:");
        ProductActivationService activation = new ProductActivationService(store);
        Product retired = store.createProduct(new Product(0, "RET-001", "Retiring Item", "Hardware", new BigDecimal("6.00"), "piece", true));
        Customer retiredBuyer = store.createCustomer(new Customer(0, "Retired Buyer", "rb@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.createSalesOrder(new SalesOrder(0, "SO-RET-1", retiredBuyer.id(), LocalDate.of(2026, 3, 2), OrderStatus.DRAFT,
                List.of(new OrderLine(retired.id(), 2, new BigDecimal("6.00")))));
        store.createCustomerPrice(new CustomerPrice(0, retiredBuyer.id(), retired.id(), new BigDecimal("5.00"),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31)));
        store.setReorderLevel(north.id(), retired.id(), 5);
        out.println("  low stock entries before: " + store.findLowStockAlerts().stream().filter(a -> a.product().id() == retired.id()).count());
        out.println("  deactivated: " + activation.deactivate(retired.id()));
        out.println("  deactivated again: " + activation.deactivate(retired.id()));
        out.println("  found by search: " + store.findActiveProductsByNameContaining("Retiring").size());
        out.println("  customer prices: " + store.findCustomerPricesForActiveProducts(retiredBuyer.id()).size());
        out.println("  low stock entries: " + store.findLowStockAlerts().stream().filter(a -> a.product().id() == retired.id()).count());
        out.println("  suggestions: " + store.findPurchaseSuggestionsByWarehouse(north.id()).stream().filter(s -> s.product().id() == retired.id()).count());
        out.println("  orders kept: " + store.findSalesOrdersOfCustomer(retiredBuyer.id()).size());
        out.println("  reactivated: " + activation.reactivate(retired.id()));
        out.println("  found by search after reactivating: " + store.findActiveProductsByNameContaining("Retiring").size());
        out.println("  customer prices after reactivating: " + store.findCustomerPricesForActiveProducts(retiredBuyer.id()).size());

        out.println("Region managers:");
        RegionManagerService regionManagers = new RegionManagerService(store);
        SalesRep regionBoss = store.createSalesRep(new SalesRep(0, "Rita Boss", "rita@acme.example", Region.EAST));
        Customer eastCustomer = store.createCustomer(new Customer(0, "East Cust", "east@example.com", null, "Poland", Region.EAST, CustomerStatus.ACTIVE));
        store.createSalesOrder(new SalesOrder(0, "SO-RM-1", eastCustomer.id(), LocalDate.of(2026, 5, 2), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-RM-2", eastCustomer.id(), LocalDate.of(2026, 5, 3), OrderStatus.DRAFT,
                List.of(new OrderLine(widget.id(), 3, new BigDecimal("2.00")))));
        OrderService regionOrders = new OrderService(store);
        regionOrders.confirm("SO-RM-1", LocalDate.of(2026, 5, 2));
        regionOrders.confirm("SO-RM-2", LocalDate.of(2026, 5, 3));
        regionOrders.ship("SO-RM-2", LocalDate.of(2026, 5, 4));
        new InvoiceService(store).invoice("SO-RM-2", LocalDate.of(2026, 5, 5));
        new SupportTicketService(store).create("T-RM-1", eastCustomer.id(), null, "Where is my order", TicketPriority.NORMAL, LocalDate.of(2026, 5, 6));
        regionManagers.assign(Region.EAST, regionBoss.id());
        out.println("  EAST manager: " + regionManagers.findManager(Region.EAST).orElseThrow().name());
        for (RegionOverview overview : regionManagers.overviewFor(regionBoss.id(), LocalDate.of(2026, 7, 1))) {
            out.println("  " + overview.region() + " open orders: " + overview.openOrders().stream().map(SalesOrder::number).toList());
            out.println("  " + overview.region() + " overdue invoices: " + overview.overdueInvoices().stream().map(Invoice::number).toList());
            out.println("  " + overview.region() + " open tickets: " + overview.openTickets().stream().map(SupportTicket::number).toList());
        }

        out.println("Dashboard:");
        Dashboard dashboard = new DashboardService(store).summarize(LocalDate.of(2026, 7, 1));
        out.println("  date: " + dashboard.date());
        out.println("  open orders: " + dashboard.openOrders() + " worth " + dashboard.openOrderValue());
        out.println("  overdue invoice amount: " + dashboard.overdueInvoiceAmount());
        out.println("  stock value: " + dashboard.stockValue());
        out.println("  open tickets: " + dashboard.openTickets());
        out.println("  back orders: " + dashboard.backOrders());
        out.println("  orders awaiting discount approval: " + dashboard.ordersAwaitingApproval());

        out.println("Orders per sales channel:");
        Customer channelCustomer = store.createCustomer(new Customer(0, "Channel Cust", "chan@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        String[] channelNumbers = {"SO-CH-1", "SO-CH-2", "SO-CH-3", "SO-CH-4"};
        SalesChannel[] channels = {SalesChannel.WEB, SalesChannel.PHONE, SalesChannel.WEB, SalesChannel.WEB};
        for (int i = 0; i < channelNumbers.length; i++) {
            SalesOrder channelOrder = store.createSalesOrder(new SalesOrder(0, channelNumbers[i], channelCustomer.id(),
                    LocalDate.of(2026, 8, i + 1), OrderStatus.DRAFT,
                    List.of(new OrderLine(widget.id(), i + 1, new BigDecimal("2.00")))));
            store.saveSalesOrderChannel(channelOrder.id(), channels[i]);
        }
        new OrderService(store).cancel("SO-CH-4", CancellationReason.CUSTOMER_REQUEST, LocalDate.of(2026, 8, 5));
        out.println("  WEB orders 2-4 Aug: " + store.findSalesOrdersFromChannel(SalesChannel.WEB, LocalDate.of(2026, 8, 2), LocalDate.of(2026, 8, 4))
                .stream().map(SalesOrder::number).toList());
        for (ChannelSales sales : store.findSalesPerChannel(LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31))) {
            out.println("  " + sales.channel() + ": " + sales.orderCount() + " orders, revenue " + sales.revenue());
        }

        out.println("Open orders per warehouse:");
        Customer fulfillCustomer = store.createCustomer(new Customer(0, "Fulfill Cust", "fulfill@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product fulfillProduct = store.createProduct(new Product(0, "FUL-001", "Fulfill Item", "Hardware", new BigDecimal("3.00"), "piece", true));
        store.setStockQuantity(north.id(), fulfillProduct.id(), 10);
        store.setStockQuantity(east.id(), fulfillProduct.id(), 4);
        int[] fulfillQuantities = {3, 8, 20};
        for (int i = 0; i < fulfillQuantities.length; i++) {
            store.createSalesOrder(new SalesOrder(0, "SO-FU-" + (i + 1), fulfillCustomer.id(), LocalDate.of(2026, 9, i + 1), OrderStatus.CONFIRMED,
                    List.of(new OrderLine(fulfillProduct.id(), fulfillQuantities[i], new BigDecimal("3.00")))));
        }
        for (WarehouseOrderFulfillment fulfillment : new OrderFulfillmentService(store).listOpenOrdersPerWarehouse()) {
            out.println("  " + fulfillment.warehouse().code() + " can ship: " + fulfillment.shippable().stream()
                    .map(SalesOrder::number).filter(n -> n.startsWith("SO-FU-")).toList());
            out.println("  " + fulfillment.warehouse().code() + " cannot ship: " + fulfillment.notShippable().stream()
                    .map(SalesOrder::number).filter(n -> n.startsWith("SO-FU-")).toList());
        }

        out.println("Customer order history:");
        Customer historyCustomer = store.createCustomer(new Customer(0, "History Cust", "history@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Product historyProduct = store.createProduct(new Product(0, "HIS-001", "History Item", "Hardware", new BigDecimal("2.50"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-HI-2", historyCustomer.id(), LocalDate.of(2026, 9, 10), OrderStatus.CONFIRMED,
                List.of(new OrderLine(historyProduct.id(), 4, new BigDecimal("2.50")), new OrderLine(historyProduct.id(), 2, new BigDecimal("3.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-HI-1", historyCustomer.id(), LocalDate.of(2026, 9, 2), OrderStatus.DRAFT,
                List.of(new OrderLine(historyProduct.id(), 1, new BigDecimal("2.50")))));
        for (OrderHistoryEntry entry : store.findSalesOrderHistoryOfCustomer(historyCustomer.id())) {
            out.println("  " + entry.number() + " " + entry.orderDate() + " " + entry.status() + " lines " + entry.lineCount() + " total " + entry.total());
        }
        out.println("  Region EAST history orders of History Cust: " + store.findSalesOrderHistoryOfRegion(Region.EAST).stream()
                .filter(e -> e.customerId() == historyCustomer.id()).map(OrderHistoryEntry::number).toList());

        out.println("Invoices per sales rep:");
        SalesRep invRep = store.createSalesRep(new SalesRep(0, "Invoice Rep", "invrep@example.com", Region.EAST));
        Customer invCustomer = store.createCustomer(new Customer(0, "Invoice Rep Cust", "invrep-cust@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(invCustomer.id(), invRep.id());
        Product invProduct = store.createProduct(new Product(0, "IRP-001", "Invoice Rep Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        String[] invNumbers = {"INV-IR-1", "INV-IR-2", "INV-IR-3"};
        LocalDate[] invDates = {LocalDate.of(2026, 1, 10), LocalDate.of(2026, 2, 10), LocalDate.of(2026, 3, 10)};
        for (int i = 0; i < invNumbers.length; i++) {
            SalesOrder invOrder = store.createSalesOrder(new SalesOrder(0, "SO-IR-" + (i + 1), invCustomer.id(), invDates[i], OrderStatus.SHIPPED,
                    List.of(new OrderLine(invProduct.id(), 1, new BigDecimal("1.00")))));
            store.createInvoice(new Invoice(0, invNumbers[i], invOrder.id(), invCustomer.id(), invDates[i], invDates[i].plusDays(30),
                    new BigDecimal("50.00"), InvoiceStatus.OPEN));
        }
        new PaymentService(store).pay("INV-IR-1", LocalDate.of(2026, 2, 1), new BigDecimal("50.00"));
        out.println("  Open: " + store.findOpenInvoicesOfSalesRep(invRep.id()).stream().map(Invoice::number).toList());
        out.println("  Overdue at 2026-04-01: " + store.findOverdueInvoicesOfSalesRep(LocalDate.of(2026, 4, 1), invRep.id()).stream().map(Invoice::number).toList());
        out.println("  Paid in 2026-02: " + store.findInvoicesOfSalesRepPaidBetween(invRep.id(), LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28)).stream().map(Invoice::number).toList());

        out.println("Invoices per region:");
        Customer regionInvCustomer = store.createCustomer(new Customer(0, "Region Invoice Cust", "reginv-cust@example.com", null, "UK", Region.WEST, CustomerStatus.ACTIVE));
        Product regionInvProduct = store.createProduct(new Product(0, "RIV-001", "Region Invoice Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        LocalDate[] regionInvDates = {LocalDate.of(2028, 1, 10), LocalDate.of(2028, 2, 10), LocalDate.of(2028, 3, 10)};
        for (int i = 0; i < regionInvDates.length; i++) {
            SalesOrder regionInvOrder = store.createSalesOrder(new SalesOrder(0, "SO-RIV-" + (i + 1), regionInvCustomer.id(), regionInvDates[i], OrderStatus.SHIPPED,
                    List.of(new OrderLine(regionInvProduct.id(), 1, new BigDecimal("1.00")))));
            store.createInvoice(new Invoice(0, "INV-RIV-" + (i + 1), regionInvOrder.id(), regionInvCustomer.id(), regionInvDates[i],
                    regionInvDates[i].plusDays(30), new BigDecimal("40.00").add(new BigDecimal(10 * i)), InvoiceStatus.OPEN));
        }
        new PaymentService(store).pay("INV-RIV-1", LocalDate.of(2028, 2, 1), new BigDecimal("40.00"));
        RegionInvoices regionInvoices = new InvoiceService(store).listByRegionAndPeriod(Region.WEST, LocalDate.of(2028, 1, 1), LocalDate.of(2028, 2, 28));
        for (Invoice regionInvoice : regionInvoices.invoices()) {
            out.println("  " + regionInvoice.number() + " " + regionInvoice.status() + " " + regionInvoice.amount());
        }
        for (InvoiceStatusTotal total : regionInvoices.totals()) {
            out.println("  " + total.status() + ": " + total.invoiceCount() + " " + total.total());
        }

        out.println("Paid late invoices:");
        Customer lateCustomer = store.createCustomer(new Customer(0, "Late Payer Cust", "latepay-cust@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Product lateProduct = store.createProduct(new Product(0, "LPI-001", "Late Payer Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        int[] latePayOffsets = {45, 20, 30};
        for (int i = 0; i < latePayOffsets.length; i++) {
            LocalDate lateDate = LocalDate.of(2029, 1, 10).plusMonths(i);
            SalesOrder lateOrder = store.createSalesOrder(new SalesOrder(0, "SO-LPI-" + (i + 1), lateCustomer.id(), lateDate, OrderStatus.SHIPPED,
                    List.of(new OrderLine(lateProduct.id(), 1, new BigDecimal("1.00")))));
            store.createInvoice(new Invoice(0, "INV-LPI-" + (i + 1), lateOrder.id(), lateCustomer.id(), lateDate,
                    lateDate.plusDays(30), new BigDecimal("60.00"), InvoiceStatus.OPEN));
            new PaymentService(store).pay("INV-LPI-" + (i + 1), lateDate.plusDays(latePayOffsets[i]), new BigDecimal("60.00"));
        }
        for (CustomerLateInvoices lateInvoices : new InvoiceService(store).listPaidLateByPeriod(LocalDate.of(2029, 1, 1), LocalDate.of(2029, 12, 31))) {
            out.println("  " + lateInvoices.customerName());
            for (LatePaidInvoice late : lateInvoices.invoices()) {
                out.println("    " + late.invoiceNumber() + " due " + late.dueDate() + " paid " + late.paidDate() + " " + late.daysLate() + " days late");
            }
        }

        out.println("Partially paid invoices:");
        Region[] partialRegions = {Region.NORTH, Region.NORTH, Region.SOUTH};
        BigDecimal[] partialPayments = {new BigDecimal("25.00"), new BigDecimal("100.00"), new BigDecimal("10.00")};
        Product partialProduct = store.createProduct(new Product(0, "PPI-001", "Partial Payment Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        for (int i = 0; i < partialRegions.length; i++) {
            Customer partialCustomer = store.createCustomer(new Customer(0, "Partial Payer " + (i + 1), "partial" + (i + 1) + "@example.com", null, "UK", partialRegions[i], CustomerStatus.ACTIVE));
            LocalDate partialDate = LocalDate.of(2030, 3, 1).plusDays(i);
            SalesOrder partialOrder = store.createSalesOrder(new SalesOrder(0, "SO-PPI-" + (i + 1), partialCustomer.id(), partialDate, OrderStatus.SHIPPED,
                    List.of(new OrderLine(partialProduct.id(), 1, new BigDecimal("1.00")))));
            store.createInvoice(new Invoice(0, "INV-PPI-" + (i + 1), partialOrder.id(), partialCustomer.id(), partialDate,
                    partialDate.plusDays(30), new BigDecimal("100.00"), InvoiceStatus.OPEN));
            new PaymentService(store).pay("INV-PPI-" + (i + 1), partialDate.plusDays(5), partialPayments[i]);
        }
        for (RegionPartiallyPaidInvoices partialRegion : new InvoiceService(store).listPartiallyPaid()) {
            out.println("  " + partialRegion.region() + " due " + partialRegion.dueAmount());
            for (CustomerPartiallyPaidInvoices partialCust : partialRegion.customers()) {
                out.println("    " + partialCust.customerName() + " due " + partialCust.dueAmount());
                for (PartiallyPaidInvoice partialInvoice : partialCust.invoices()) {
                    out.println("      " + partialInvoice.invoiceNumber() + " paid " + partialInvoice.paidAmount() + " of " + partialInvoice.amount() + ", due " + partialInvoice.dueAmount());
                }
            }
        }

        out.println("Large invoices per region:");
        Product largeInvProduct = store.createProduct(new Product(0, "LIV-001", "Large Invoice Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Region[] largeInvRegions = {Region.NORTH, Region.SOUTH, Region.NORTH, Region.SOUTH, Region.NORTH};
        BigDecimal[] largeInvAmounts = {new BigDecimal("500.00"), new BigDecimal("900.00"), new BigDecimal("700.00"), new BigDecimal("1200.00"), new BigDecimal("50.00")};
        for (int i = 0; i < largeInvAmounts.length; i++) {
            Customer largeInvCustomer = store.createCustomer(new Customer(0, "Large Invoice Cust " + (i + 1), "largeinv" + (i + 1) + "@example.com", null, "UK", largeInvRegions[i], CustomerStatus.ACTIVE));
            LocalDate largeInvDate = LocalDate.of(2031, 5, 1).plusDays(i);
            SalesOrder largeInvOrder = store.createSalesOrder(new SalesOrder(0, "SO-LIV-" + (i + 1), largeInvCustomer.id(), largeInvDate, OrderStatus.SHIPPED,
                    List.of(new OrderLine(largeInvProduct.id(), 1, new BigDecimal("1.00")))));
            store.createInvoice(new Invoice(0, "INV-LIV-" + (i + 1), largeInvOrder.id(), largeInvCustomer.id(), largeInvDate,
                    largeInvDate.plusDays(30), largeInvAmounts[i], InvoiceStatus.OPEN));
        }
        for (RegionLargeInvoices largeInvRegion : new InvoiceService(store).listLargeByRegion(new BigDecimal("100.00"), LocalDate.of(2031, 5, 1), LocalDate.of(2031, 5, 4))) {
            out.println("  " + largeInvRegion.region());
            for (Invoice largeInv : largeInvRegion.invoices()) {
                out.println("    " + largeInv.number() + " " + largeInv.amount());
            }
        }

        out.println("Payments per region:");
        Product regPayProduct = store.createProduct(new Product(0, "RPY-001", "Region Payment Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Region[] regPayRegions = {Region.NORTH, Region.SOUTH, Region.NORTH};
        LocalDate[] regPayDates = {LocalDate.of(2032, 1, 20), LocalDate.of(2032, 1, 25), LocalDate.of(2032, 2, 10)};
        PaymentService regPayments = new PaymentService(store);
        for (int i = 0; i < regPayRegions.length; i++) {
            Customer regPayCustomer = store.createCustomer(new Customer(0, "Region Payment Cust " + (i + 1), "regpay" + (i + 1) + "@example.com", null, "UK", regPayRegions[i], CustomerStatus.ACTIVE));
            SalesOrder regPayOrder = store.createSalesOrder(new SalesOrder(0, "SO-RPY-" + (i + 1), regPayCustomer.id(), LocalDate.of(2032, 1, 10), OrderStatus.SHIPPED,
                    List.of(new OrderLine(regPayProduct.id(), 1, new BigDecimal("1.00")))));
            store.createInvoice(new Invoice(0, "INV-RPY-" + (i + 1), regPayOrder.id(), regPayCustomer.id(), LocalDate.of(2032, 1, 10),
                    LocalDate.of(2032, 2, 10), new BigDecimal("200.00"), InvoiceStatus.OPEN));
            regPayments.pay("INV-RPY-" + (i + 1), regPayDates[i], new BigDecimal("50.00").add(new BigDecimal(i * 25)));
        }
        regPayments.pay("INV-RPY-1", LocalDate.of(2032, 2, 5), new BigDecimal("30.00"));
        RegionPayments regPaid = regPayments.listByRegion(Region.NORTH, LocalDate.of(2032, 1, 1), LocalDate.of(2032, 2, 28));
        out.println("  " + regPaid.region());
        for (PaymentReceived regPaid1 : regPaid.payments()) {
            out.println("    " + regPaid1.paymentDate() + " " + regPaid1.invoiceNumber() + " " + regPaid1.amount());
        }
        for (MonthlyPaymentTotal regPaidMonth : regPaid.monthlyTotals()) {
            out.println("    " + regPaidMonth.year() + "-" + regPaidMonth.month() + ": " + regPaidMonth.paymentCount() + " payments, " + regPaidMonth.total());
        }

        out.println("Unapplied payments:");
        Product unappliedProduct = store.createProduct(new Product(0, "UNP-001", "Unapplied Payment Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        BigDecimal[][] unappliedAmounts = {{new BigDecimal("80.00"), new BigDecimal("40.00")}, {new BigDecimal("100.00")}, {new BigDecimal("130.00")}};
        for (int i = 0; i < unappliedAmounts.length; i++) {
            Customer unappliedCustomer = store.createCustomer(new Customer(0, "Unapplied Cust " + (i + 1), "unapplied" + (i + 1) + "@example.com", null, "UK", Region.WEST, CustomerStatus.ACTIVE));
            SalesOrder unappliedOrder = store.createSalesOrder(new SalesOrder(0, "SO-UNP-" + (i + 1), unappliedCustomer.id(), LocalDate.of(2033, 1, 10), OrderStatus.SHIPPED,
                    List.of(new OrderLine(unappliedProduct.id(), 1, new BigDecimal("1.00")))));
            Invoice unappliedInvoice = store.createInvoice(new Invoice(0, "INV-UNP-" + (i + 1), unappliedOrder.id(), unappliedCustomer.id(), LocalDate.of(2033, 1, 10),
                    LocalDate.of(2033, 2, 10), new BigDecimal("100.00"), InvoiceStatus.PAID));
            for (int j = 0; j < unappliedAmounts[i].length; j++) {
                store.createPayment(new Payment(0, unappliedInvoice.id(), LocalDate.of(2033, 1, 20).plusDays(j), unappliedAmounts[i][j]));
            }
        }
        for (CustomerUnappliedPayments unappliedCust : new PaymentService(store).listUnapplied()) {
            out.println("  " + unappliedCust.customerName() + " unapplied " + unappliedCust.unappliedAmount());
            for (UnappliedPayment unapplied : unappliedCust.payments()) {
                out.println("    " + unapplied.paymentDate() + " " + unapplied.invoiceNumber() + " paid " + unapplied.amount() + ", unapplied " + unapplied.unappliedAmount());
            }
        }

        out.println("Open invoices per currency:");
        Currency[] openCurrencies = {Currency.USD, Currency.GBP, Currency.USD, Currency.EUR};
        BigDecimal[] openCurrencyAmounts = {new BigDecimal("110.00"), new BigDecimal("85.00"), new BigDecimal("55.00"), new BigDecimal("100.00")};
        Product openCurrencyProduct = store.createProduct(new Product(0, "OCI-001", "Open Currency Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        for (int i = 0; i < openCurrencies.length; i++) {
            Customer openCurrencyCustomer = store.createCustomer(new Customer(0, "Currency Payer " + (i + 1), "currpay" + (i + 1) + "@example.com", null, "UK", Region.WEST, CustomerStatus.ACTIVE));
            LocalDate openCurrencyDate = LocalDate.of(2030, 4, 1).plusDays(i);
            SalesOrder openCurrencyOrder = store.createSalesOrder(new SalesOrder(0, "SO-OCI-" + (i + 1), openCurrencyCustomer.id(), openCurrencyDate, OrderStatus.SHIPPED,
                    List.of(new OrderLine(openCurrencyProduct.id(), 1, new BigDecimal("1.00")))));
            Invoice openCurrencyInvoice = store.createInvoice(new Invoice(0, "INV-OCI-" + (i + 1), openCurrencyOrder.id(), openCurrencyCustomer.id(), openCurrencyDate,
                    openCurrencyDate.plusDays(30), new BigDecimal("100.00"), InvoiceStatus.OPEN));
            if (openCurrencies[i] != Currency.EUR) {
                store.createInvoiceCurrency(new InvoiceCurrency(openCurrencyInvoice.id(), openCurrencies[i], openCurrencyAmounts[i]));
            }
        }
        for (CurrencyInvoices openCurrency : new InvoiceService(store).listOpenByCurrency()) {
            out.println("  " + openCurrency.currency() + " " + openCurrency.currencyAmount() + " (EUR " + openCurrency.eurAmount() + ")");
            for (CustomerCurrencyInvoices openCurrencyCust : openCurrency.customers()) {
                out.println("    " + openCurrencyCust.customerName() + " " + openCurrencyCust.currencyAmount() + " (EUR " + openCurrencyCust.eurAmount() + ")");
                for (CurrencyOpenInvoice openCurrencyInv : openCurrencyCust.invoices()) {
                    out.println("      " + openCurrencyInv.invoiceNumber() + " " + openCurrencyInv.currencyAmount() + " " + openCurrencyInv.currency() + " = EUR " + openCurrencyInv.eurAmount());
                }
            }
        }

        out.println("Shipments per customer:");
        Customer shipCustomer = store.createCustomer(new Customer(0, "Shipments Cust", "shipcust@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Product shipProduct = store.createProduct(new Product(0, "SHC-001", "Shipments Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Warehouse shipWarehouse = store.createWarehouse(new Warehouse(0, "WH-SHC", "Shipments Hub", "Riga", Region.EAST));
        Carrier shipCarrier = store.createCarrier(new Carrier(0, "ShipCustCarrier", ServiceLevel.EXPRESS, new BigDecimal("1.00"), 3));
        LocalDate[] shipDates = {LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 10), LocalDate.of(2026, 6, 1)};
        LocalDate[] shipDeliveries = {LocalDate.of(2026, 5, 7), LocalDate.of(2026, 5, 12), null};
        for (int i = 0; i < shipDates.length; i++) {
            SalesOrder shipOrder = store.createSalesOrder(new SalesOrder(0, "SO-SHC-" + (i + 1), shipCustomer.id(), shipDates[i], OrderStatus.SHIPPED,
                    List.of(new OrderLine(shipProduct.id(), 1, new BigDecimal("1.00")))));
            store.createShipment(new Shipment(0, shipOrder.id(), shipWarehouse.id(), shipCarrier.id(), "SHC-" + (i + 1), shipDates[i]));
            if (shipDeliveries[i] != null) {
                store.setShipmentDeliveryDateIfInTransit(shipOrder.id(), shipDeliveries[i]);
            }
        }
        out.println("  Shipped in 2026-05: " + store.findShipmentsOfCustomerShippedBetween(shipCustomer.id(), LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31)).stream().map(Shipment::trackingNumber).toList());
        out.println("  In transit: " + store.findShipmentsInTransitOfCustomer(shipCustomer.id()).stream().map(Shipment::trackingNumber).toList());
        out.println("  Delivered late: " + store.findLateShipmentsOfCustomer(shipCustomer.id()).stream().map(Shipment::trackingNumber).toList());

        out.println("Returns per product:");
        Customer retCustomer = store.createCustomer(new Customer(0, "Returns Cust", "retcust@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Product retBolt = store.createProduct(new Product(0, "RPP-001", "Return Bolt", "Fasteners", new BigDecimal("1.00"), "piece", true));
        Product retRice = store.createProduct(new Product(0, "RPP-002", "Return Rice", "Food", new BigDecimal("1.00"), "kg", true));
        Warehouse retWarehouse = store.createWarehouse(new Warehouse(0, "WH-RPP", "Returns Hub", "Riga", Region.EAST));
        SalesOrder retOrder = store.createSalesOrder(new SalesOrder(0, "SO-RPP-1", retCustomer.id(), LocalDate.of(2026, 7, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(retBolt.id(), 20, new BigDecimal("1.00")), new OrderLine(retRice.id(), 20, new BigDecimal("1.00")))));
        store.createSalesReturn(new SalesReturn(0, "R-RPP-1", retOrder.id(), LocalDate.of(2026, 7, 5), "Damaged",
                List.of(new ReturnLine(retBolt.id(), 3), new ReturnLine(retRice.id(), 5))), retWarehouse.id());
        store.createSalesReturn(new SalesReturn(0, "R-RPP-2", retOrder.id(), LocalDate.of(2026, 7, 9), "Wrong item",
                List.of(new ReturnLine(retBolt.id(), 4))), retWarehouse.id());
        store.createSalesReturn(new SalesReturn(0, "R-RPP-3", retOrder.id(), LocalDate.of(2026, 8, 2), "Late",
                List.of(new ReturnLine(retBolt.id(), 1))), retWarehouse.id());
        out.println("  Returns with " + retBolt.sku() + " in 2026-07: " + store.findSalesReturnsContainingProductReturnedBetween(retBolt.id(), LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31)).stream().map(SalesReturn::number).toList());
        for (ProductReturns returned : store.findReturnedQuantityPerProduct(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31))) {
            out.println("  " + returned.sku() + " " + returned.name() + " 2026-07: " + returned.unitsReturned() + " units");
        }
        for (ProductReturns returned : store.findReturnedQuantityPerProductInCategory("Fasteners", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31))) {
            out.println("  Fasteners " + returned.sku() + " 2026-07: " + returned.unitsReturned() + " units");
        }

        out.println("Product margin:");
        Customer marCustomer = store.createCustomer(new Customer(0, "Margin Cust", "marcust@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Supplier marSupplier = store.createSupplier(new Supplier(0, "Margin Supply", "marsup@example.com", "Latvia", 4));
        Product marBolt = store.createProduct(new Product(0, "PM-001", "Margin Bolt", "Fasteners", new BigDecimal("5.00"), "piece", true));
        Product marNut = store.createProduct(new Product(0, "PM-002", "Margin Nut", "Fasteners", new BigDecimal("3.00"), "piece", true));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-PM-1", marSupplier.id(), LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 10), PurchaseOrderStatus.SENT,
                List.of(new PurchaseOrderLine(marBolt.id(), 100, new BigDecimal("2.00")), new PurchaseOrderLine(marNut.id(), 100, new BigDecimal("2.50")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-PM-2", marSupplier.id(), LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 10), PurchaseOrderStatus.SENT,
                List.of(new PurchaseOrderLine(marBolt.id(), 100, new BigDecimal("4.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-PM-1", marCustomer.id(), LocalDate.of(2026, 7, 3), OrderStatus.DELIVERED,
                List.of(new OrderLine(marBolt.id(), 10, new BigDecimal("5.00")), new OrderLine(marNut.id(), 20, new BigDecimal("3.00")))));
        for (ProductMargin entry : store.findMarginPerProduct(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31))) {
            out.println("  " + entry.sku() + " " + entry.name() + " 2026-07: revenue " + entry.revenue() + ", cost " + entry.cost() + ", margin " + entry.margin());
        }

        out.println("Category margin:");
        for (CategoryMargin entry : store.findMarginPerCategory(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31))) {
            out.println("  " + entry.category() + " 2026-07: revenue " + entry.revenue() + ", cost " + entry.cost() + ", margin " + entry.margin() + " (" + entry.marginPercent() + "%)");
        }
        for (CategoryMargin entry : store.findMarginPerCategoryBelowPercent(new BigDecimal("50"), LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31))) {
            out.println("  Below 50% 2026-07: " + entry.category() + " " + entry.marginPercent() + "%");
        }

        out.println("Customers per region:");
        Customer cprActive = store.createCustomer(new Customer(0, "CPR Active", "cpra@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer cprHold = store.createCustomer(new Customer(0, "CPR Hold", "cprb@example.com", null, "UK", Region.SOUTH, CustomerStatus.ON_HOLD));
        store.updateCustomerCreatedDate(cprActive.id(), LocalDate.of(2026, 8, 10));
        store.updateCustomerCreatedDate(cprHold.id(), LocalDate.of(2026, 8, 20));
        for (RegionCustomers entry : store.findCustomersPerRegion(LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31), LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30))) {
            out.println("  " + entry.region() + ": " + entry.activeCount() + " active, " + entry.onHoldCount() + " on hold, "
                    + entry.newCount() + " new 2026-08, " + entry.dormantCount() + " dormant");
        }

        out.println("Overdue invoices per sales rep:");
        SalesRep odBig = store.createSalesRep(new SalesRep(0, "OD Big", "odbig@example.com", Region.EAST));
        SalesRep odSmall = store.createSalesRep(new SalesRep(0, "OD Small", "odsmall@example.com", Region.EAST));
        Customer odBigCust = store.createCustomer(new Customer(0, "OD Big Cust", "odbigc@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Customer odSmallCust = store.createCustomer(new Customer(0, "OD Small Cust", "odsmallc@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(odBigCust.id(), odBig.id());
        store.assignCustomerToSalesRep(odSmallCust.id(), odSmall.id());
        Product odProduct = store.createProduct(new Product(0, "OD-001", "Overdue Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Customer[] odCustomers = {odBigCust, odBigCust, odSmallCust};
        String[] odAmounts = {"300.00", "200.00", "100.00"};
        for (int i = 0; i < odCustomers.length; i++) {
            SalesOrder odOrder = store.createSalesOrder(new SalesOrder(0, "SO-OD-" + (i + 1), odCustomers[i].id(), LocalDate.of(2027, 1, 10), OrderStatus.SHIPPED,
                    List.of(new OrderLine(odProduct.id(), 1, new BigDecimal("1.00")))));
            store.createInvoice(new Invoice(0, "INV-OD-" + (i + 1), odOrder.id(), odCustomers[i].id(), LocalDate.of(2027, 1, 10), LocalDate.of(2027, 2, 10),
                    new BigDecimal(odAmounts[i]), InvoiceStatus.OPEN));
        }
        for (RepOverdueInvoices entry : store.findOverdueInvoiceTotalsPerSalesRep(LocalDate.of(2027, 3, 1))) {
            out.println("  " + entry.name() + " overdue on 2027-03-01: " + entry.invoiceCount() + " invoices, " + entry.totalAmount());
        }

        out.println("Returns per reason:");
        for (ReasonReturns reasonReturns : store.findSalesReturnCountAndValuePerReason(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31))) {
            out.println("  " + reasonReturns.reason() + " 2026-07: " + reasonReturns.returnCount() + " returns, " + reasonReturns.returnedValue());
        }
        for (RegionReasonReturns regionReturns : store.findSalesReturnCountAndValuePerRegionAndReason(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31))) {
            out.println("  " + regionReturns.region() + " " + regionReturns.reason() + " 2026-07: " + regionReturns.returnCount() + " returns, " + regionReturns.returnedValue());
        }

        out.println("Back orders per warehouse:");
        Customer boCustomer = store.createCustomer(new Customer(0, "BO Cust", "bo@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product boPaint = store.createProduct(new Product(0, "BO-P", "BO Paint", "BO-Paints", new BigDecimal("5.00"), "piece", true));
        Product boTool = store.createProduct(new Product(0, "BO-T", "BO Tool", "BO-Tools", new BigDecimal("7.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-BO-1", boCustomer.id(), LocalDate.of(2027, 2, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(boPaint.id(), 4, new BigDecimal("5.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-BO-2", boCustomer.id(), LocalDate.of(2027, 2, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(boPaint.id(), 2, new BigDecimal("5.00")), new OrderLine(boTool.id(), 8, new BigDecimal("7.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-BO-3", boCustomer.id(), LocalDate.of(2027, 2, 2), OrderStatus.CONFIRMED,
                List.of(new OrderLine(boTool.id(), 100, new BigDecimal("7.00")))));
        BackOrderService boService = new BackOrderService(store);
        boService.putOnBackOrder("SO-BO-1", LocalDate.of(2027, 2, 3));
        boService.putOnBackOrder("SO-BO-2", LocalDate.of(2027, 2, 4));
        boService.putOnBackOrder("SO-BO-3", LocalDate.of(2027, 2, 5));
        store.setStockQuantity(north.id(), boPaint.id(), 5);
        store.setStockQuantity(east.id(), boPaint.id(), 2);
        store.setStockQuantity(east.id(), boTool.id(), 9);
        for (String code : new String[]{"WH-NORTH", "WH-EAST"}) {
            out.println("  " + code + " can ship: " + boService.listShippableFrom(code).stream()
                    .map(BackOrder::orderNumber).filter(n -> n.startsWith("SO-BO-")).toList());
        }
        for (CategoryBackOrders entry : boService.listPerCategory()) {
            if (entry.category().startsWith("BO-")) {
                out.println("  " + entry.category() + ": " + entry.backOrderCount() + " back orders, " + entry.units() + " units");
            }
        }

        out.println("Promotion usage:");
        Customer puCustomer = store.createCustomer(new Customer(0, "PU Cust", "pu@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product puPaint = store.createProduct(new Product(0, "PU-P", "PU Paint", "PU-Paints", new BigDecimal("10.00"), "piece", true));
        Product puTool = store.createProduct(new Product(0, "PU-T", "PU Tool", "PU-Tools", new BigDecimal("20.00"), "piece", true));
        PromotionService puPromotions = new PromotionService(store);
        puPromotions.createPromotion("PU-PAINT20", new BigDecimal("20"), LocalDate.of(2028, 3, 1), LocalDate.of(2028, 3, 31), "PU-Paints");
        puPromotions.createPromotion("PU-TOOL10", new BigDecimal("10"), LocalDate.of(2028, 3, 1), LocalDate.of(2028, 3, 31), "PU-Tools");
        puPromotions.createPromotion("PU-ALL5", new BigDecimal("5"), LocalDate.of(2028, 3, 15), LocalDate.of(2028, 3, 20), "PU-Nothing");
        OrderEntryService puEntry = new OrderEntryService(store);
        puEntry.placeOrder("SO-PU-1", puCustomer.id(), LocalDate.of(2028, 3, 5), OrderStatus.CONFIRMED,
                List.of(new OrderLine(puPaint.id(), 10, new BigDecimal("10.00")), new OrderLine(puTool.id(), 1, new BigDecimal("20.00"))));
        puEntry.placeOrder("SO-PU-2", puCustomer.id(), LocalDate.of(2028, 3, 10), OrderStatus.CONFIRMED,
                List.of(new OrderLine(puPaint.id(), 5, new BigDecimal("10.00"))));
        puEntry.placeOrder("SO-PU-3", puCustomer.id(), LocalDate.of(2028, 3, 12), OrderStatus.CANCELLED,
                List.of(new OrderLine(puTool.id(), 4, new BigDecimal("20.00"))));
        for (PromotionUsage usage : puPromotions.usage(LocalDate.of(2028, 3, 1), LocalDate.of(2028, 3, 31))) {
            out.println("  " + usage.code() + " 2028-03: " + usage.lineCount() + " lines, discount " + usage.discount() + ", revenue " + usage.revenue());
        }
        for (Promotion unused : puPromotions.listUnused(LocalDate.of(2028, 3, 1), LocalDate.of(2028, 3, 31))) {
            if (unused.code().startsWith("PU-")) {
                out.println("  Unused: " + unused.code());
            }
        }

        out.println("Price list usage:");
        Customer plCustomer = store.createCustomer(new Customer(0, "PL Cust", "pl@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer plOther = store.createCustomer(new Customer(0, "PL Other", "plo@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product plBolt = store.createProduct(new Product(0, "PL-B", "PL Bolt", "PL-Parts", new BigDecimal("10.00"), "piece", true));
        Product plNut = store.createProduct(new Product(0, "PL-N", "PL Nut", "PL-Parts", new BigDecimal("4.00"), "piece", true));
        PriceListService plPrices = new PriceListService(store);
        plPrices.addPrice(plCustomer.id(), plBolt.id(), new BigDecimal("8.00"), LocalDate.of(2028, 4, 1), LocalDate.of(2028, 4, 30));
        OrderEntryService plEntry = new OrderEntryService(store);
        plEntry.placeOrder("SO-PL-1", plCustomer.id(), LocalDate.of(2028, 4, 5), OrderStatus.CONFIRMED,
                List.of(new OrderLine(plBolt.id(), 10, new BigDecimal("10.00")), new OrderLine(plNut.id(), 5, new BigDecimal("4.00"))));
        plEntry.placeOrder("SO-PL-2", plCustomer.id(), LocalDate.of(2028, 4, 10), OrderStatus.CANCELLED,
                List.of(new OrderLine(plBolt.id(), 3, new BigDecimal("10.00"))));
        plEntry.placeOrder("SO-PL-3", plOther.id(), LocalDate.of(2028, 4, 6), OrderStatus.CONFIRMED,
                List.of(new OrderLine(plBolt.id(), 2, new BigDecimal("10.00"))));
        for (PriceListUsage usage : plPrices.usage(LocalDate.of(2028, 4, 1), LocalDate.of(2028, 4, 30))) {
            if (usage.customerName().startsWith("PL ")) {
                out.println("  " + usage.customerName() + " 2028-04: " + usage.lineCount() + " lines, special " + usage.specialRevenue()
                        + ", catalogue " + usage.catalogueRevenue());
            }
        }

        out.println("Payments per sales rep:");
        SalesRep ppBig = store.createSalesRep(new SalesRep(0, "PP Big", "ppbig@example.com", Region.EAST));
        SalesRep ppSmall = store.createSalesRep(new SalesRep(0, "PP Small", "ppsmall@example.com", Region.EAST));
        Customer ppBigCust = store.createCustomer(new Customer(0, "PP Big Cust", "ppbigc@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Customer ppSmallCust = store.createCustomer(new Customer(0, "PP Small Cust", "ppsmallc@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(ppBigCust.id(), ppBig.id());
        store.assignCustomerToSalesRep(ppSmallCust.id(), ppSmall.id());
        Product ppProduct = store.createProduct(new Product(0, "PP-001", "Payment Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Customer[] ppCustomers = {ppBigCust, ppBigCust, ppSmallCust};
        String[] ppAmounts = {"300.00", "200.00", "100.00"};
        LocalDate[] ppDates = {LocalDate.of(2027, 4, 5), LocalDate.of(2027, 4, 20), LocalDate.of(2027, 5, 2)};
        for (int i = 0; i < ppCustomers.length; i++) {
            SalesOrder ppOrder = store.createSalesOrder(new SalesOrder(0, "SO-PP-" + (i + 1), ppCustomers[i].id(), LocalDate.of(2027, 3, 1), OrderStatus.SHIPPED,
                    List.of(new OrderLine(ppProduct.id(), 1, new BigDecimal("1.00")))));
            Invoice ppInvoice = store.createInvoice(new Invoice(0, "INV-PP-" + (i + 1), ppOrder.id(), ppCustomers[i].id(), LocalDate.of(2027, 3, 1), LocalDate.of(2027, 3, 31),
                    new BigDecimal(ppAmounts[i]), InvoiceStatus.OPEN));
            store.createPayment(new Payment(0, ppInvoice.id(), ppDates[i], new BigDecimal(ppAmounts[i])));
        }
        for (PaymentReceived payment : store.findPaymentsReceivedOfSalesRepBetween(ppBig.id(), LocalDate.of(2027, 4, 1), LocalDate.of(2027, 4, 30))) {
            out.println("  PP Big " + payment.invoiceNumber() + " " + payment.paymentDate() + " " + payment.amount());
        }
        for (RepPayments entry : store.findPaymentTotalsPerSalesRep(LocalDate.of(2027, 4, 1), LocalDate.of(2027, 5, 31))) {
            out.println("  " + entry.name() + " 2027-04..05: " + entry.paymentCount() + " payments, " + entry.totalAmount());
        }

        out.println("Tickets per region:");
        Customer trWest = store.createCustomer(new Customer(0, "TR West", "trw@example.com", null, "UK", Region.WEST, CustomerStatus.ACTIVE));
        Customer trEast = store.createCustomer(new Customer(0, "TR East", "tre@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        String[] trNumbers = {"TR-1", "TR-2", "TR-3", "TR-4", "TR-5"};
        Customer[] trCustomers = {trWest, trWest, trWest, trWest, trEast};
        TicketPriority[] trPriorities = {TicketPriority.HIGH, TicketPriority.HIGH, TicketPriority.LOW, TicketPriority.HIGH, TicketPriority.HIGH};
        LocalDate[] trDates = {LocalDate.of(2027, 8, 1), LocalDate.of(2027, 8, 25), LocalDate.of(2027, 8, 2), LocalDate.of(2027, 8, 3), LocalDate.of(2027, 8, 1)};
        for (int i = 0; i < trNumbers.length; i++) {
            store.createSupportTicket(new SupportTicket(0, trNumbers[i], trCustomers[i].id(), null, "Problem " + trNumbers[i],
                    trPriorities[i], TicketStatus.OPEN, trDates[i], null));
        }
        store.updateSupportTicketStatus(store.findSupportTicketByNumber("TR-4").orElseThrow().id(), TicketStatus.CLOSED, LocalDate.of(2027, 8, 10));
        for (PriorityTickets entry : store.findOpenSupportTicketCountsPerPriorityByRegion(Region.WEST)) {
            out.println("  WEST " + entry.priority() + ": " + entry.ticketCount());
        }
        out.println("  WEST high older than 14 days on 2027-09-01: "
                + store.findHighPrioritySupportTicketsInRegionOpenLongerThan(Region.WEST, LocalDate.of(2027, 9, 1), 14).stream().map(SupportTicket::number).toList());

        out.println("Tickets per product:");
        Customer tpCust = store.createCustomer(new Customer(0, "TP Cust", "tpc@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Product tpA = store.createProduct(new Product(0, "TP-A", "Ticket Item A", "Hardware", new BigDecimal("1.00"), "piece", true));
        Product tpB = store.createProduct(new Product(0, "TP-B", "Ticket Item B", "Hardware", new BigDecimal("1.00"), "piece", true));
        String[] tpNumbers = {"TP-1", "TP-2", "TP-3", "TP-4"};
        Product[] tpProducts = {tpA, tpA, tpA, tpB};
        LocalDate[] tpDates = {LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 10), LocalDate.of(2027, 7, 5), LocalDate.of(2027, 6, 15)};
        for (int i = 0; i < tpNumbers.length; i++) {
            SupportTicket tpTicket = store.createSupportTicket(new SupportTicket(0, tpNumbers[i], tpCust.id(), null, "Problem " + tpNumbers[i],
                    TicketPriority.NORMAL, TicketStatus.OPEN, tpDates[i], null));
            store.updateSupportTicketProduct(tpTicket.id(), tpProducts[i].id());
        }
        store.updateSupportTicketStatus(store.findSupportTicketByNumber("TP-2").orElseThrow().id(), TicketStatus.CLOSED, LocalDate.of(2027, 6, 12));
        out.println("  Open for TP-A: " + store.findOpenSupportTicketsAboutProduct(tpA.id()).stream().map(SupportTicket::number).toList());
        for (ProductTickets entry : store.findSupportTicketCountsPerProduct(LocalDate.of(2027, 6, 1), LocalDate.of(2027, 6, 30))) {
            out.println("  " + entry.sku() + " 2027-06: " + entry.ticketCount());
        }

        out.println("Credit notes per period:");
        Customer cnNorth = store.createCustomer(new Customer(0, "CN North", "cnn@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer cnSouth = store.createCustomer(new Customer(0, "CN South", "cns@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product cnProduct = store.createProduct(new Product(0, "CN-P", "CN Item", "CN-Parts", new BigDecimal("5.00"), "piece", true));
        store.createWarehouse(new Warehouse(0, "WH-CN", "CN Warehouse", "Oslo", Region.NORTH));
        store.createSalesOrder(new SalesOrder(0, "SO-CN-1", cnNorth.id(), LocalDate.of(2028, 5, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(cnProduct.id(), 20, new BigDecimal("5.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-CN-2", cnSouth.id(), LocalDate.of(2028, 5, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(cnProduct.id(), 20, new BigDecimal("5.00")))));
        ReturnService cnReturns = new ReturnService(store);
        cnReturns.createReturn("R-CN-1", "SO-CN-1", LocalDate.of(2028, 5, 10), "Damaged", "WH-CN", List.of(new ReturnLine(cnProduct.id(), 4)));
        cnReturns.createReturn("R-CN-2", "SO-CN-2", LocalDate.of(2028, 5, 12), "Damaged", "WH-CN", List.of(new ReturnLine(cnProduct.id(), 2)));
        cnReturns.createReturn("R-CN-3", "SO-CN-2", LocalDate.of(2028, 5, 20), "Wrong item", "WH-CN", List.of(new ReturnLine(cnProduct.id(), 6)));
        cnReturns.createReturn("R-CN-4", "SO-CN-1", LocalDate.of(2028, 6, 2), "Late", "WH-CN", List.of(new ReturnLine(cnProduct.id(), 1)));
        for (CreditNote note : cnReturns.listCreditNotes(LocalDate.of(2028, 5, 1), LocalDate.of(2028, 5, 31))) {
            out.println("  " + note.number() + " " + note.creditDate() + " " + note.amount());
        }
        for (CreditNote note : cnReturns.listCreditNotes(Region.NORTH, LocalDate.of(2028, 5, 1), LocalDate.of(2028, 5, 31))) {
            out.println("  NORTH " + note.number() + " " + note.amount());
        }
        for (ReasonCredit reasonCredit : cnReturns.creditedPerReason(LocalDate.of(2028, 5, 1), LocalDate.of(2028, 5, 31))) {
            out.println("  " + reasonCredit.reason() + " 2028-05: " + reasonCredit.noteCount() + " notes, " + reasonCredit.creditedAmount());
        }

        out.println("Stock per category:");
        Supplier scSupplier = store.createSupplier(new Supplier(0, "SC Supply", "sc@example.com", "Sweden", 3));
        Warehouse scA = store.createWarehouse(new Warehouse(0, "WH-SC-A", "SC Warehouse A", "Oslo", Region.NORTH));
        Warehouse scB = store.createWarehouse(new Warehouse(0, "WH-SC-B", "SC Warehouse B", "Warsaw", Region.EAST));
        Product scNail = store.createProduct(new Product(0, "SC-N", "SC Nail", "SC-Fasteners", new BigDecimal("1.00"), "piece", true));
        Product scScrew = store.createProduct(new Product(0, "SC-S", "SC Screw", "SC-Fasteners", new BigDecimal("2.00"), "piece", true));
        Product scPaint = store.createProduct(new Product(0, "SC-P", "SC Paint", "SC-Paints", new BigDecimal("10.00"), "piece", true));
        Product scTool = store.createProduct(new Product(0, "SC-T", "SC Tool", "SC-Tools", new BigDecimal("20.00"), "piece", true));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-SC-1", scSupplier.id(), LocalDate.of(2028, 7, 1), LocalDate.of(2028, 7, 10), PurchaseOrderStatus.RECEIVED,
                List.of(new PurchaseOrderLine(scNail.id(), 100, new BigDecimal("0.50")), new PurchaseOrderLine(scScrew.id(), 100, new BigDecimal("1.50")),
                        new PurchaseOrderLine(scPaint.id(), 100, new BigDecimal("6.00")))));
        store.setStockQuantity(scA.id(), scNail.id(), 40);
        store.setStockQuantity(scA.id(), scScrew.id(), 10);
        store.setStockQuantity(scA.id(), scPaint.id(), 5);
        store.setStockQuantity(scB.id(), scNail.id(), 20);
        store.setStockQuantity(scB.id(), scTool.id(), 0);
        for (CategoryWarehouseStock entry : store.findStockPerCategoryAndWarehouse()) {
            if (entry.category().startsWith("SC-")) {
                out.println("  " + entry.category() + " in " + entry.warehouse().code() + ": " + entry.quantity() + " pieces, " + entry.value());
            }
        }
        for (CategoryStock entry : store.findStockPerCategory()) {
            if (entry.category().startsWith("SC-")) {
                out.println("  " + entry.category() + " total: " + entry.quantity() + " pieces, " + entry.value());
            }
        }
        for (WarehouseCategory entry : store.findCategoriesWithoutStockPerWarehouse()) {
            if (entry.category().startsWith("SC-") && entry.warehouse().code().startsWith("WH-SC-")) {
                out.println("  No " + entry.category() + " in " + entry.warehouse().code());
            }
        }

        out.println("Slow-moving stock:");
        Supplier smSupplier = store.createSupplier(new Supplier(0, "SM Supply", "sm@example.com", "Sweden", 3));
        Warehouse smA = store.createWarehouse(new Warehouse(0, "WH-SM-A", "SM Warehouse A", "Oslo", Region.NORTH));
        Warehouse smB = store.createWarehouse(new Warehouse(0, "WH-SM-B", "SM Warehouse B", "Warsaw", Region.EAST));
        Carrier smCarrier = store.createCarrier(new Carrier(0, "SM Carrier", ServiceLevel.STANDARD, new BigDecimal("1.00")));
        Customer smCustomer = store.createCustomer(new Customer(0, "SM Customer", "smc@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Product smNail = store.createProduct(new Product(0, "SM-N", "SM Nail", "SM-Fasteners", new BigDecimal("1.00"), "piece", true));
        Product smPaint = store.createProduct(new Product(0, "SM-P", "SM Paint", "SM-Paints", new BigDecimal("10.00"), "piece", true));
        Product smTool = store.createProduct(new Product(0, "SM-T", "SM Tool", "SM-Tools", new BigDecimal("20.00"), "piece", true));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-SM-1", smSupplier.id(), LocalDate.of(2028, 8, 1), LocalDate.of(2028, 8, 10), PurchaseOrderStatus.RECEIVED,
                List.of(new PurchaseOrderLine(smNail.id(), 100, new BigDecimal("0.50")), new PurchaseOrderLine(smPaint.id(), 100, new BigDecimal("6.00")),
                        new PurchaseOrderLine(smTool.id(), 100, new BigDecimal("12.00")))));
        store.setStockQuantity(smA.id(), smNail.id(), 40);
        store.setStockQuantity(smA.id(), smPaint.id(), 5);
        store.setStockQuantity(smB.id(), smPaint.id(), 10);
        store.setStockQuantity(smB.id(), smTool.id(), 3);
        SalesOrder smOrder = store.createSalesOrder(new SalesOrder(0, "SO-SM-1", smCustomer.id(), LocalDate.of(2028, 9, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(smPaint.id(), 1, new BigDecimal("10.00")))));
        store.createShipment(new Shipment(0, smOrder.id(), smA.id(), smCarrier.id(), "SM-TRK-1", LocalDate.of(2028, 9, 20)));
        for (WarehouseSlowMovingStock entry : store.findSlowMovingStockPerWarehouse(LocalDate.of(2028, 10, 1), 30)) {
            if (entry.warehouse().code().startsWith("WH-SM-")) {
                out.println("  " + entry.warehouse().code() + " " + entry.product().sku() + ": " + entry.quantity() + " pieces, " + entry.value());
            }
        }
        for (SlowMovingStock entry : store.findSlowMovingStock(LocalDate.of(2028, 10, 1), 30)) {
            if (entry.product().sku().startsWith("SM-")) {
                out.println("  Total " + entry.product().sku() + ": " + entry.quantity() + " pieces, " + entry.value());
            }
        }

        out.println("Order line search:");
        Customer olsA = store.createCustomer(new Customer(0, "OLS Customer A", "olsa@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer olsB = store.createCustomer(new Customer(0, "OLS Customer B", "olsb@example.com", null, "Poland", Region.EAST, CustomerStatus.ACTIVE));
        Product olsNail = store.createProduct(new Product(0, "OLS-N", "OLS Nail", "OLS-Fasteners", new BigDecimal("1.00"), "piece", true));
        Product olsPaint = store.createProduct(new Product(0, "OLS-P", "OLS Paint", "OLS-Paints", new BigDecimal("10.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-OLS-1", olsA.id(), LocalDate.of(2028, 3, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(olsNail.id(), 50, new BigDecimal("1.00")), new OrderLine(olsPaint.id(), 2, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OLS-2", olsB.id(), LocalDate.of(2028, 4, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(olsNail.id(), 5, new BigDecimal("1.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OLS-3", olsA.id(), LocalDate.of(2028, 5, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(olsPaint.id(), 10, new BigDecimal("10.00")))));
        out.println("  Nail lines:");
        for (OrderLineMatch match : store.findSalesOrderLinesMatching(olsNail.id(), null, null, null, null, null)) {
            out.println("    " + match.orderNumber() + " " + match.orderDate() + " " + match.customerName() + " x" + match.quantity());
        }
        out.println("  Customer A, from 2028-03-15:");
        for (OrderLineMatch match : store.findSalesOrderLinesMatching(null, null, olsA.id(), LocalDate.of(2028, 3, 15), null, null)) {
            out.println("    " + match.orderNumber() + " " + match.product().sku() + " x" + match.quantity());
        }
        out.println("  OLS-Paints, at least 5:");
        for (OrderLineMatch match : store.findSalesOrderLinesMatching(null, "OLS-Paints", null, null, LocalDate.of(2028, 12, 31), 5)) {
            out.println("    " + match.orderNumber() + " " + match.product().sku() + " x" + match.quantity());
        }

        out.println("Shipment search:");
        Customer ssA = store.createCustomer(new Customer(0, "SS Customer A", "ssa@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer ssB = store.createCustomer(new Customer(0, "SS Customer B", "ssb@example.com", null, "Poland", Region.EAST, CustomerStatus.ACTIVE));
        Warehouse ssWarehouseN = store.createWarehouse(new Warehouse(0, "SS-WH-N", "SS North", "Oslo", Region.NORTH));
        Warehouse ssWarehouseE = store.createWarehouse(new Warehouse(0, "SS-WH-E", "SS East", "Warsaw", Region.EAST));
        Carrier ssCarrier1 = store.createCarrier(new Carrier(0, "SS Carrier 1", ServiceLevel.STANDARD, new BigDecimal("1.00")));
        Carrier ssCarrier2 = store.createCarrier(new Carrier(0, "SS Carrier 2", ServiceLevel.EXPRESS, new BigDecimal("2.00")));
        SalesOrder ssOrder1 = store.createSalesOrder(new SalesOrder(0, "SO-SS-1", ssA.id(), LocalDate.of(2028, 10, 1), OrderStatus.CONFIRMED, List.of()));
        SalesOrder ssOrder2 = store.createSalesOrder(new SalesOrder(0, "SO-SS-2", ssB.id(), LocalDate.of(2028, 10, 1), OrderStatus.CONFIRMED, List.of()));
        SalesOrder ssOrder3 = store.createSalesOrder(new SalesOrder(0, "SO-SS-3", ssA.id(), LocalDate.of(2028, 10, 1), OrderStatus.CONFIRMED, List.of()));
        store.createShipment(new Shipment(0, ssOrder1.id(), ssWarehouseN.id(), ssCarrier1.id(), "SS-TRK-1", LocalDate.of(2028, 10, 2)));
        store.createShipment(new Shipment(0, ssOrder2.id(), ssWarehouseE.id(), ssCarrier2.id(), "SS-TRK-2", LocalDate.of(2028, 10, 5)));
        store.createShipment(new Shipment(0, ssOrder3.id(), ssWarehouseN.id(), ssCarrier2.id(), "SS-TRK-3", LocalDate.of(2028, 10, 9)));
        store.setShipmentDeliveryDateIfInTransit(ssOrder1.id(), LocalDate.of(2028, 10, 4));
        store.setShipmentDeliveryDateIfInTransit(ssOrder2.id(), LocalDate.of(2028, 10, 8));
        out.println("  Carrier 2:");
        for (Shipment ssFound : store.findShipmentsMatching(ssCarrier2.id(), null, null, null, null, null)) {
            out.println("    " + ssFound.trackingNumber() + " " + ssFound.shipDate());
        }
        out.println("  Warehouse North, customer A, in transit:");
        for (Shipment ssFound : store.findShipmentsMatching(null, ssWarehouseN.id(), ssA.id(), ShipmentStatus.IN_TRANSIT, null, null)) {
            out.println("    " + ssFound.trackingNumber() + " " + ssFound.shipDate());
        }
        out.println("  Delivered, shipped 2028-10-01 to 2028-10-06:");
        for (Shipment ssFound : store.findShipmentsMatching(null, null, null, ShipmentStatus.DELIVERED, LocalDate.of(2028, 10, 1), LocalDate.of(2028, 10, 6))) {
            out.println("    " + ssFound.trackingNumber() + " " + ssFound.shipDate());
        }

        out.println("Purchase order search:");
        Supplier posS1 = store.createSupplier(new Supplier(0, "POS Supplier 1", "pos1@example.com", "Norway", 3));
        Supplier posS2 = store.createSupplier(new Supplier(0, "POS Supplier 2", "pos2@example.com", "Poland", 4));
        Warehouse posWarehouseN = store.createWarehouse(new Warehouse(0, "POS-WH-N", "POS North", "Oslo", Region.NORTH));
        Warehouse posWarehouseE = store.createWarehouse(new Warehouse(0, "POS-WH-E", "POS East", "Warsaw", Region.EAST));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POS-1", posS1.id(), LocalDate.of(2028, 10, 1), LocalDate.of(2028, 10, 10),
                PurchaseOrderStatus.SENT, List.of(), posWarehouseN.id()));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POS-2", posS2.id(), LocalDate.of(2028, 10, 1), LocalDate.of(2028, 10, 20),
                PurchaseOrderStatus.DRAFT, List.of(), posWarehouseE.id()));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POS-3", posS1.id(), LocalDate.of(2028, 10, 2), LocalDate.of(2028, 11, 5),
                PurchaseOrderStatus.SENT, List.of(), posWarehouseE.id()));
        out.println("  Supplier 1:");
        for (PurchaseOrder posFound : store.findPurchaseOrdersMatching(posS1.id(), null, null, null, null)) {
            out.println("    " + posFound.number() + " expected " + posFound.expectedDate());
        }
        out.println("  Warehouse East, sent:");
        for (PurchaseOrder posFound : store.findPurchaseOrdersMatching(null, posWarehouseE.id(), PurchaseOrderStatus.SENT, null, null)) {
            out.println("    " + posFound.number() + " expected " + posFound.expectedDate());
        }
        out.println("  Expected 2028-10-05 to 2028-10-31:");
        for (PurchaseOrder posFound : store.findPurchaseOrdersMatching(null, null, null, LocalDate.of(2028, 10, 5), LocalDate.of(2028, 10, 31))) {
            out.println("    " + posFound.number() + " expected " + posFound.expectedDate());
        }

        out.println("Return search:");
        Customer rsA = store.createCustomer(new Customer(0, "RS Customer A", "rsa@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer rsB = store.createCustomer(new Customer(0, "RS Customer B", "rsb@example.com", null, "Poland", Region.EAST, CustomerStatus.ACTIVE));
        Warehouse rsWarehouse = store.createWarehouse(new Warehouse(0, "RS-WH", "RS Warehouse", "Oslo", Region.NORTH));
        Product rsBolt = store.createProduct(new Product(0, "RS-BOLT", "RS Bolt", "Fasteners", new BigDecimal("1.00"), "piece", true));
        Product rsNut = store.createProduct(new Product(0, "RS-NUT", "RS Nut", "Fasteners", new BigDecimal("1.00"), "piece", true));
        SalesOrder rsOrderA = store.createSalesOrder(new SalesOrder(0, "SO-RS-A", rsA.id(), LocalDate.of(2028, 10, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(rsBolt.id(), 20, new BigDecimal("1.00")), new OrderLine(rsNut.id(), 20, new BigDecimal("1.00")))));
        SalesOrder rsOrderB = store.createSalesOrder(new SalesOrder(0, "SO-RS-B", rsB.id(), LocalDate.of(2028, 10, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(rsBolt.id(), 20, new BigDecimal("1.00")))));
        store.createSalesReturn(new SalesReturn(0, "RET-RS-1", rsOrderA.id(), LocalDate.of(2028, 10, 5), "Damaged",
                List.of(new ReturnLine(rsBolt.id(), 2))), rsWarehouse.id());
        store.createSalesReturn(new SalesReturn(0, "RET-RS-2", rsOrderB.id(), LocalDate.of(2028, 10, 8), "Damaged",
                List.of(new ReturnLine(rsBolt.id(), 3))), rsWarehouse.id());
        store.createSalesReturn(new SalesReturn(0, "RET-RS-3", rsOrderA.id(), LocalDate.of(2028, 10, 12), "Wrong item",
                List.of(new ReturnLine(rsNut.id(), 4))), rsWarehouse.id());
        out.println("  Customer A:");
        for (SalesReturn rsFound : store.findSalesReturnsMatching(rsA.id(), null, null, null, null)) {
            out.println("    " + rsFound.number() + " " + rsFound.returnDate() + " " + rsFound.reason());
        }
        out.println("  Bolt, damaged:");
        for (SalesReturn rsFound : store.findSalesReturnsMatching(null, rsBolt.id(), "Damaged", null, null)) {
            out.println("    " + rsFound.number() + " " + rsFound.returnDate() + " " + rsFound.reason());
        }
        out.println("  Returned 2028-10-06 to 2028-10-31:");
        for (SalesReturn rsFound : store.findSalesReturnsMatching(null, null, null, LocalDate.of(2028, 10, 6), LocalDate.of(2028, 10, 31))) {
            out.println("    " + rsFound.number() + " " + rsFound.returnDate() + " " + rsFound.reason());
        }

        out.println("Monthly sales 2029:");
        Customer msA = store.createCustomer(new Customer(0, "MS Customer A", "msa@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer msB = store.createCustomer(new Customer(0, "MS Customer B", "msb@example.com", null, "Poland", Region.EAST, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(msA.id(), LocalDate.of(2029, 1, 10));
        store.updateCustomerCreatedDate(msB.id(), LocalDate.of(2029, 2, 3));
        Warehouse msWarehouse = store.createWarehouse(new Warehouse(0, "MS-WH", "MS Warehouse", "Oslo", Region.NORTH));
        Product msBolt = store.createProduct(new Product(0, "MS-BOLT", "MS Bolt", "Fasteners", new BigDecimal("2.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-MS-1", msA.id(), LocalDate.of(2029, 1, 15), OrderStatus.DELIVERED,
                List.of(new OrderLine(msBolt.id(), 10, new BigDecimal("2.00")))));
        SalesOrder msOrder2 = store.createSalesOrder(new SalesOrder(0, "SO-MS-2", msB.id(), LocalDate.of(2029, 2, 20), OrderStatus.DELIVERED,
                List.of(new OrderLine(msBolt.id(), 5, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-MS-3", msB.id(), LocalDate.of(2029, 2, 21), OrderStatus.CANCELLED,
                List.of(new OrderLine(msBolt.id(), 50, new BigDecimal("2.00")))));
        SalesReturn msReturn = store.createSalesReturn(new SalesReturn(0, "RET-MS-1", msOrder2.id(), LocalDate.of(2029, 3, 2), "Damaged",
                List.of(new ReturnLine(msBolt.id(), 2))), msWarehouse.id());
        store.createCreditNote(new CreditNote(0, "CN-MS-1", msB.id(), msReturn.id(), LocalDate.of(2029, 3, 4), new BigDecimal("4.00")));
        for (MonthlySales msMonth : store.findMonthlySales(2029).subList(0, 3)) {
            out.println("  month " + msMonth.month() + ": " + msMonth.orderCount() + " orders, revenue " + msMonth.revenue()
                    + ", " + msMonth.newCustomers() + " new customers, " + msMonth.returnCount() + " returns, credited "
                    + msMonth.creditedAmount());
        }

        out.println("Year-over-year February 2030:");
        Customer yoyA = store.createCustomer(new Customer(0, "YoY Customer A", "yoya@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer yoyB = store.createCustomer(new Customer(0, "YoY Customer B", "yoyb@example.com", null, "Poland", Region.EAST, CustomerStatus.ACTIVE));
        Product yoyBolt = store.createProduct(new Product(0, "YOY-BOLT", "YoY Bolt", "Fasteners", new BigDecimal("2.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-YOY-1", yoyA.id(), LocalDate.of(2029, 2, 10), OrderStatus.DELIVERED,
                List.of(new OrderLine(yoyBolt.id(), 10, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-YOY-2", yoyB.id(), LocalDate.of(2029, 2, 12), OrderStatus.DELIVERED,
                List.of(new OrderLine(yoyBolt.id(), 5, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-YOY-3", yoyA.id(), LocalDate.of(2030, 2, 5), OrderStatus.DELIVERED,
                List.of(new OrderLine(yoyBolt.id(), 15, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-YOY-4", yoyA.id(), LocalDate.of(2030, 2, 6), OrderStatus.DELIVERED,
                List.of(new OrderLine(yoyBolt.id(), 5, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-YOY-5", yoyB.id(), LocalDate.of(2030, 2, 7), OrderStatus.CANCELLED,
                List.of(new OrderLine(yoyBolt.id(), 50, new BigDecimal("2.00")))));
        YearOverYearSales yoyTotal = store.findYearOverYearSalesOverAllRegions(YearMonth.of(2030, 2));
        out.println("  all: " + yoyTotal.orderCount() + " orders (was " + yoyTotal.previousOrderCount() + "), revenue "
                + yoyTotal.revenue() + " (was " + yoyTotal.previousRevenue() + "), average " + yoyTotal.averageOrderValue()
                + " (was " + yoyTotal.previousAverageOrderValue() + "), revenue change " + yoyTotal.revenueChangePercent() + "%");
        for (YearOverYearSales yoyRegion : store.findYearOverYearSalesPerRegion(YearMonth.of(2030, 2))) {
            out.println("  " + yoyRegion.region() + ": " + yoyRegion.orderCount() + " orders (was " + yoyRegion.previousOrderCount()
                    + "), revenue " + yoyRegion.revenue() + " (was " + yoyRegion.previousRevenue() + "), average "
                    + yoyRegion.averageOrderValue() + " (was " + yoyRegion.previousAverageOrderValue() + "), revenue change "
                    + yoyRegion.revenueChangePercent() + "%");
        }

        out.println("Orders per carrier October 2030:");
        Customer cocCustomer = store.createCustomer(new Customer(0, "COC Customer", "coc@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Product cocBolt = store.createProduct(new Product(0, "COC-BOLT", "COC Bolt", "Fasteners", new BigDecimal("2.00"), "piece", true));
        Warehouse cocWarehouse = store.createWarehouse(new Warehouse(0, "COC-WH", "COC Warehouse", "Oslo", Region.NORTH));
        Carrier cocFast = store.createCarrier(new Carrier(0, "COC Fast", ServiceLevel.EXPRESS, new BigDecimal("2.00"), 2));
        Carrier cocSlow = store.createCarrier(new Carrier(0, "COC Slow", ServiceLevel.STANDARD, new BigDecimal("0.50"), 6));
        store.createCarrier(new Carrier(0, "COC Idle", ServiceLevel.STANDARD, new BigDecimal("0.50"), 6));
        SalesOrder coc1 = store.createSalesOrder(new SalesOrder(0, "SO-COC-1", cocCustomer.id(), LocalDate.of(2030, 10, 1), OrderStatus.SHIPPED,
                List.of(new OrderLine(cocBolt.id(), 10, new BigDecimal("2.00")))));
        SalesOrder coc2 = store.createSalesOrder(new SalesOrder(0, "SO-COC-2", cocCustomer.id(), LocalDate.of(2030, 10, 2), OrderStatus.SHIPPED,
                List.of(new OrderLine(cocBolt.id(), 5, new BigDecimal("2.00")))));
        SalesOrder coc3 = store.createSalesOrder(new SalesOrder(0, "SO-COC-3", cocCustomer.id(), LocalDate.of(2030, 10, 3), OrderStatus.SHIPPED,
                List.of(new OrderLine(cocBolt.id(), 20, new BigDecimal("2.00")), new OrderLine(cocBolt.id(), 10, new BigDecimal("3.00")))));
        SalesOrder coc4 = store.createSalesOrder(new SalesOrder(0, "SO-COC-4", cocCustomer.id(), LocalDate.of(2030, 10, 4), OrderStatus.SHIPPED,
                List.of(new OrderLine(cocBolt.id(), 50, new BigDecimal("2.00")))));
        store.createShipment(new Shipment(0, coc1.id(), cocWarehouse.id(), cocFast.id(), "COC-TRK-1", LocalDate.of(2030, 10, 5)));
        store.createShipment(new Shipment(0, coc2.id(), cocWarehouse.id(), cocSlow.id(), "COC-TRK-2", LocalDate.of(2030, 10, 6)));
        store.createShipmentPart(coc3.id(), 1, cocWarehouse.id(), cocFast.id(), "COC-TRK-3A", LocalDate.of(2030, 10, 7), 20);
        store.createShipmentPart(coc3.id(), 2, cocWarehouse.id(), cocSlow.id(), "COC-TRK-3B", LocalDate.of(2030, 10, 8), 10);
        store.createShipment(new Shipment(0, coc4.id(), cocWarehouse.id(), cocFast.id(), "COC-TRK-4", LocalDate.of(2030, 11, 2)));
        for (SalesOrder cocOrder : store.findSalesOrdersShippedByCarrier(cocFast.id(), LocalDate.of(2030, 10, 1), LocalDate.of(2030, 10, 31))) {
            out.println("  COC Fast: " + cocOrder.number() + " placed " + cocOrder.orderDate());
        }
        for (CarrierOrders cocEntry : store.findSalesOrderCountAndValuePerCarrier(LocalDate.of(2030, 10, 1), LocalDate.of(2030, 10, 31))) {
            out.println("  " + cocEntry.carrier().name() + ": " + cocEntry.orderCount() + " orders, value " + cocEntry.value());
        }

        out.println("Orders with returns November 2030:");
        Customer owrCustomer = store.createCustomer(new Customer(0, "OWR Customer", "owr@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Product owrBolt = store.createProduct(new Product(0, "OWR-BOLT", "OWR Bolt", "Fasteners", new BigDecimal("2.00"), "piece", true));
        Warehouse owrWarehouse = store.createWarehouse(new Warehouse(0, "OWR-WH", "OWR Warehouse", "Oslo", Region.NORTH));
        SalesOrder owr1 = store.createSalesOrder(new SalesOrder(0, "SO-OWR-1", owrCustomer.id(), LocalDate.of(2030, 11, 3), OrderStatus.DELIVERED,
                List.of(new OrderLine(owrBolt.id(), 50, new BigDecimal("2.00")))));
        SalesOrder owr2 = store.createSalesOrder(new SalesOrder(0, "SO-OWR-2", owrCustomer.id(), LocalDate.of(2030, 11, 10), OrderStatus.DELIVERED,
                List.of(new OrderLine(owrBolt.id(), 10, new BigDecimal("3.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OWR-3", owrCustomer.id(), LocalDate.of(2030, 11, 12), OrderStatus.DELIVERED,
                List.of(new OrderLine(owrBolt.id(), 10, new BigDecimal("3.00")))));
        store.createSalesReturn(new SalesReturn(0, "R-OWR-1", owr1.id(), LocalDate.of(2030, 11, 8), "Damaged",
                List.of(new ReturnLine(owrBolt.id(), 5))), owrWarehouse.id());
        store.createSalesReturn(new SalesReturn(0, "R-OWR-2", owr1.id(), LocalDate.of(2030, 11, 9), "Wrong item",
                List.of(new ReturnLine(owrBolt.id(), 5))), owrWarehouse.id());
        store.createSalesReturn(new SalesReturn(0, "R-OWR-3", owr2.id(), LocalDate.of(2030, 11, 15), "Late",
                List.of(new ReturnLine(owrBolt.id(), 3))), owrWarehouse.id());
        for (OrderReturns owrEntry : store.findSalesOrdersWithReturnsOfCustomer(owrCustomer.id(), LocalDate.of(2030, 11, 1), LocalDate.of(2030, 11, 30))) {
            out.println("  " + owrEntry.order().number() + ": value " + owrEntry.orderValue() + ", returned "
                    + owrEntry.returnedValue() + " (" + owrEntry.returnedPercent() + "%)");
        }

        out.println("Large orders above 100.00 in December 2030:");
        Customer loNorth = store.createCustomer(new Customer(0, "LO North", "lon@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer loSouth = store.createCustomer(new Customer(0, "LO South", "los@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Product loBolt = store.createProduct(new Product(0, "LO-BOLT", "LO Bolt", "Fasteners", new BigDecimal("2.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-LO-1", loNorth.id(), LocalDate.of(2030, 12, 2), OrderStatus.CONFIRMED,
                List.of(new OrderLine(loBolt.id(), 100, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-LO-2", loNorth.id(), LocalDate.of(2030, 12, 5), OrderStatus.CONFIRMED,
                List.of(new OrderLine(loBolt.id(), 200, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-LO-3", loSouth.id(), LocalDate.of(2030, 12, 9), OrderStatus.CONFIRMED,
                List.of(new OrderLine(loBolt.id(), 51, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-LO-4", loSouth.id(), LocalDate.of(2030, 12, 31), OrderStatus.CONFIRMED,
                List.of(new OrderLine(loBolt.id(), 300, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-LO-5", loSouth.id(), LocalDate.of(2031, 1, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(loBolt.id(), 400, new BigDecimal("2.00")))));
        for (LargeOrder loEntry : store.findLargeSalesOrders(new BigDecimal("100.00"), LocalDate.of(2030, 12, 1), LocalDate.of(2030, 12, 31))) {
            out.println("  " + loEntry.customer().region() + " " + loEntry.order().number() + ": " + loEntry.value()
                    + " by " + loEntry.customer().name());
        }

        out.println("Small orders below 100.00 in November 2030:");
        Customer soNorth = store.createCustomer(new Customer(0, "SO North", "son@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer soSouth = store.createCustomer(new Customer(0, "SO South", "sos@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Product soNut = store.createProduct(new Product(0, "SO-NUT", "SO Nut", "Fasteners", new BigDecimal("2.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-SMALL-1", soNorth.id(), LocalDate.of(2030, 11, 2), OrderStatus.CONFIRMED,
                List.of(new OrderLine(soNut.id(), 10, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-SMALL-2", soNorth.id(), LocalDate.of(2030, 11, 5), OrderStatus.CONFIRMED,
                List.of(new OrderLine(soNut.id(), 49, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-SMALL-3", soNorth.id(), LocalDate.of(2030, 11, 9), OrderStatus.CONFIRMED,
                List.of(new OrderLine(soNut.id(), 50, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-SMALL-4", soSouth.id(), LocalDate.of(2030, 11, 30), OrderStatus.CONFIRMED,
                List.of(new OrderLine(soNut.id(), 5, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-SMALL-5", soSouth.id(), LocalDate.of(2030, 11, 30), OrderStatus.CONFIRMED,
                List.of(new OrderLine(soNut.id(), 80, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-SMALL-6", soSouth.id(), LocalDate.of(2030, 12, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(soNut.id(), 1, new BigDecimal("2.00")))));
        for (SmallOrder soEntry : store.findSmallSalesOrders(new BigDecimal("100.00"), LocalDate.of(2030, 11, 1), LocalDate.of(2030, 11, 30))) {
            out.println("  " + soEntry.customer().name() + " " + soEntry.order().number() + ": " + soEntry.value());
        }
        for (RegionSmallOrderShare soShare : store.findSmallSalesOrderSharePerRegion(new BigDecimal("100.00"), LocalDate.of(2030, 11, 1), LocalDate.of(2030, 11, 30))) {
            out.println("  " + soShare.region() + ": " + soShare.smallOrders() + " of " + soShare.totalOrders()
                    + " (" + soShare.sharePercent() + "%)");
        }

        out.println("Order lead time in December 2030:");
        Customer ltNorth = store.createCustomer(new Customer(0, "LT North", "ltn@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer ltSouth = store.createCustomer(new Customer(0, "LT South", "lts@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Product ltNut = store.createProduct(new Product(0, "LT-NUT", "LT Nut", "Fasteners", new BigDecimal("2.00"), "piece", true));
        store.createWarehouse(new Warehouse(0, "WH-LT", "LT Warehouse", "Oslo", Region.NORTH));
        new StockService(store).setQuantity(store.findWarehouseByCode("WH-LT").orElseThrow().id(), ltNut.id(), 100);
        store.createCarrier(new Carrier(0, "LT Express", ServiceLevel.EXPRESS, new BigDecimal("2.00"), 2));
        store.createCarrier(new Carrier(0, "LT Road", ServiceLevel.STANDARD, new BigDecimal("0.50"), 6));
        String[] ltNumbers = {"LT-1", "LT-2", "LT-3", "LT-4", "LT-5", "LT-6"};
        Customer[] ltCustomers = {ltNorth, ltNorth, ltSouth, ltSouth, ltSouth, ltNorth};
        int[] ltOrderDays = {1, 2, 3, 4, 5, 20};
        for (int i = 0; i < ltNumbers.length; i++) {
            store.createSalesOrder(new SalesOrder(0, ltNumbers[i], ltCustomers[i].id(), LocalDate.of(2030, 12, ltOrderDays[i]),
                    OrderStatus.CONFIRMED, List.of(new OrderLine(ltNut.id(), 1, new BigDecimal("2.00")))));
        }
        ShipmentService leadTime = new ShipmentService(store);
        leadTime.ship("LT-1", "WH-LT", "LT Express", "LTE-1", LocalDate.of(2030, 12, 2));
        leadTime.ship("LT-2", "WH-LT", "LT Road", "LTR-2", LocalDate.of(2030, 12, 3));
        leadTime.ship("LT-3", "WH-LT", "LT Road", "LTR-3", LocalDate.of(2030, 12, 4));
        leadTime.ship("LT-4", "WH-LT", "LT Express", "LTE-4", LocalDate.of(2030, 12, 5));
        leadTime.ship("LT-6", "WH-LT", "LT Road", "LTR-6", LocalDate.of(2030, 12, 21));
        leadTime.markDelivered("LT-1", LocalDate.of(2030, 12, 4));
        leadTime.markDelivered("LT-2", LocalDate.of(2030, 12, 9));
        leadTime.markDelivered("LT-3", LocalDate.of(2030, 12, 10));
        leadTime.markDelivered("LT-4", LocalDate.of(2030, 12, 8));
        for (RegionLeadTime ltRegion : leadTime.getLeadTimeByRegion(LocalDate.of(2030, 12, 1), LocalDate.of(2030, 12, 31))) {
            out.println("  " + ltRegion.region() + ": " + ltRegion.orderCount() + " orders, average " + ltRegion.averageDays() + " days");
        }
        for (CarrierLeadTime ltCarrier : leadTime.getLeadTimeByCarrier(LocalDate.of(2030, 12, 1), LocalDate.of(2030, 12, 31))) {
            out.println("  " + ltCarrier.carrier().name() + ": " + ltCarrier.orderCount() + " orders, average " + ltCarrier.averageDays() + " days");
        }

        out.println("Cancelled orders in January 2031:");
        Customer coNorth = store.createCustomer(new Customer(0, "CO North", "con@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer coSouth = store.createCustomer(new Customer(0, "CO South", "cos@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Product coNut = store.createProduct(new Product(0, "CO-NUT", "CO Nut", "Fasteners", new BigDecimal("3.00"), "piece", true));
        String[] coNumbers = {"CO-1", "CO-2", "CO-3", "CO-4", "CO-5"};
        Customer[] coCustomers = {coNorth, coSouth, coNorth, coSouth, coNorth};
        int[] coCancelDays = {5, 10, 15, 20, 28};
        OrderService coOrders = new OrderService(store);
        for (int i = 0; i < coNumbers.length; i++) {
            store.createSalesOrder(new SalesOrder(0, coNumbers[i], coCustomers[i].id(), LocalDate.of(2031, 1, 1 + i),
                    OrderStatus.CONFIRMED, List.of(new OrderLine(coNut.id(), 2, new BigDecimal("3.00")))));
            coOrders.cancel(coNumbers[i], CancellationReason.CUSTOMER_REQUEST, LocalDate.of(2031, 1, coCancelDays[i]));
        }
        LocalDate coFrom = LocalDate.of(2031, 1, 1);
        LocalDate coTo = LocalDate.of(2031, 1, 25);
        for (CancelledOrder coOrder : coOrders.getCancelledOrdersByCustomer(coFrom, coTo)) {
            out.println("  customer " + coOrder.customer().name() + ": " + coOrder.order().number() + " cancelled " + coOrder.cancelDate());
        }
        for (CancelledOrder coOrder : coOrders.getCancelledOrdersByRegion(coFrom, coTo)) {
            out.println("  region " + coOrder.customer().region() + ": " + coOrder.order().number() + " cancelled " + coOrder.cancelDate());
        }

        out.println("Draft orders older than 30 days on 2031-03-01:");
        SalesRep sdRep = store.createSalesRep(new SalesRep(0, "SD Rep", "sdrep@example.com", Region.NORTH));
        Customer sdAssigned = store.createCustomer(new Customer(0, "SD Assigned", "sda@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer sdLoose = store.createCustomer(new Customer(0, "SD Loose", "sdl@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(sdAssigned.id(), sdRep.id());
        Product sdNut = store.createProduct(new Product(0, "SD-NUT", "SD Nut", "Fasteners", new BigDecimal("3.00"), "piece", true));
        String[] sdNumbers = {"SD-1", "SD-2", "SD-3", "SD-4"};
        Customer[] sdCustomers = {sdAssigned, sdLoose, sdAssigned, sdLoose};
        LocalDate[] sdDates = {LocalDate.of(2031, 1, 10), LocalDate.of(2031, 1, 20), LocalDate.of(2031, 1, 30), LocalDate.of(2031, 2, 15)};
        for (int i = 0; i < sdNumbers.length; i++) {
            store.createSalesOrder(new SalesOrder(0, sdNumbers[i], sdCustomers[i].id(), sdDates[i], OrderStatus.DRAFT,
                    List.of(new OrderLine(sdNut.id(), 1, new BigDecimal("3.00")))));
        }
        OrderService sdOrders = new OrderService(store);
        LocalDate sdDate = LocalDate.of(2031, 3, 1);
        for (DraftOrder sdOrder : sdOrders.getStaleDraftOrdersByCustomer(sdDate, 30)) {
            out.println("  customer " + sdOrder.customer().name() + ": " + sdOrder.order().number() + " " + sdOrder.ageDays() + " days");
        }
        for (DraftOrder sdOrder : sdOrders.getStaleDraftOrdersBySalesRep(sdDate, 30)) {
            out.println("  rep " + (sdOrder.salesRep() == null ? "none" : sdOrder.salesRep().name()) + ": " + sdOrder.order().number() + " " + sdOrder.ageDays() + " days");
        }

        out.println("Orders containing a product in February 2031:");
        Customer opAnna = store.createCustomer(new Customer(0, "OP Anna", "opa@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer opBert = store.createCustomer(new Customer(0, "OP Bert", "opb@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        Product opBolt = store.createProduct(new Product(0, "OP-BOLT", "OP Bolt", "Fasteners", new BigDecimal("1.50"), "piece", true));
        Product opOther = store.createProduct(new Product(0, "OP-OTHER", "OP Other", "Fasteners", new BigDecimal("2.50"), "piece", true));
        String[] opNumbers = {"OP-1", "OP-2", "OP-3", "OP-4", "OP-5"};
        Customer[] opCustomers = {opAnna, opBert, opAnna, opBert, opAnna};
        Product[] opProducts = {opBolt, opBolt, opBolt, opOther, opBolt};
        int[] opDays = {3, 9, 12, 15, 25};
        OrderService opOrders = new OrderService(store);
        for (int i = 0; i < opNumbers.length; i++) {
            store.createSalesOrder(new SalesOrder(0, opNumbers[i], opCustomers[i].id(), LocalDate.of(2031, 2, opDays[i]),
                    OrderStatus.CONFIRMED, List.of(new OrderLine(opProducts[i].id(), 4, opProducts[i].unitPrice()))));
        }
        opOrders.cancel("OP-3", CancellationReason.OTHER, LocalDate.of(2031, 2, 13));
        LocalDate opFrom = LocalDate.of(2031, 2, 1);
        LocalDate opTo = LocalDate.of(2031, 2, 20);
        for (ProductOrder opOrder : opOrders.getOrdersByProduct(opBolt.id(), opFrom, opTo)) {
            out.println("  newest first: " + opOrder.order().number() + " " + opOrder.order().orderDate() + " " + opOrder.customer().name());
        }
        for (ProductOrder opOrder : opOrders.getOrdersByProductPerCustomer(opBolt.id(), opFrom, opTo)) {
            out.println("  customer " + opOrder.customer().name() + ": " + opOrder.order().number() + " " + opOrder.order().orderDate());
        }

        out.println("Orders per category in March 2031:");
        Customer ocCust = store.createCustomer(new Customer(0, "OC Cust", "occ@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        new CategoryService(store).createCategory("OC-TOOLS", "OC Tools", null);
        new CategoryService(store).createCategory("OC-HAND", "OC Hand tools", "OC-TOOLS");
        Product ocHammer = store.createProduct(new Product(0, "OC-HAMMER", "OC Hammer", "OC-HAND", new BigDecimal("10.00"), "piece", true));
        Product ocPaint = store.createProduct(new Product(0, "OC-PAINT", "OC Paint", "OC-PAINT", new BigDecimal("5.00"), "piece", true));
        String[] ocNumbers = {"OC-1", "OC-2", "OC-3", "OC-4"};
        Product[] ocProducts = {ocHammer, ocPaint, ocHammer, ocHammer};
        LocalDate[] ocDates = {LocalDate.of(2031, 3, 5), LocalDate.of(2031, 3, 10), LocalDate.of(2031, 4, 2), LocalDate.of(2031, 3, 20)};
        OrderService ocOrders = new OrderService(store);
        for (int i = 0; i < ocNumbers.length; i++) {
            store.createSalesOrder(new SalesOrder(0, ocNumbers[i], ocCust.id(), ocDates[i], OrderStatus.CONFIRMED,
                    List.of(new OrderLine(ocProducts[i].id(), 3, ocProducts[i].unitPrice()))));
        }
        ocOrders.cancel("OC-4", CancellationReason.OTHER, LocalDate.of(2031, 3, 21));
        for (ProductOrder ocOrder : ocOrders.getOrdersByCategory("OC-TOOLS", LocalDate.of(2031, 3, 1), LocalDate.of(2031, 3, 31))) {
            out.println("  " + ocOrder.order().number() + " " + ocOrder.order().orderDate() + " " + ocOrder.customer().name());
        }
        for (CategoryMonthRevenue ocRevenue : ocOrders.getRevenuePerCategoryAndMonth(LocalDate.of(2031, 3, 1), LocalDate.of(2031, 4, 30))) {
            if (ocRevenue.category().startsWith("OC-")) {
                out.println("  revenue " + ocRevenue.category() + " " + ocRevenue.month() + ": " + ocRevenue.revenue());
            }
        }

        out.println("Repeat customers with at least 2 orders in March 2032:");
        Customer rcAnna = store.createCustomer(new Customer(0, "RC Anna", "rca@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer rcBen = store.createCustomer(new Customer(0, "RC Ben", "rcb@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer rcCleo = store.createCustomer(new Customer(0, "RC Cleo", "rcc@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        new CategoryService(store).createCategory("RC-CAT", "RC Category", null);
        Product rcItem = store.createProduct(new Product(0, "RC-ITEM", "RC Item", "RC-CAT", new BigDecimal("4.00"), "piece", true));
        Customer[] rcCustomers = {rcAnna, rcAnna, rcAnna, rcBen, rcBen, rcCleo, rcCleo, rcCleo};
        LocalDate[] rcDates = {LocalDate.of(2032, 3, 1), LocalDate.of(2032, 3, 15), LocalDate.of(2032, 3, 31),
                LocalDate.of(2032, 3, 2), LocalDate.of(2032, 4, 1), LocalDate.of(2032, 3, 3), LocalDate.of(2032, 3, 4), LocalDate.of(2032, 3, 5)};
        OrderService rcOrders = new OrderService(store);
        for (int i = 0; i < rcCustomers.length; i++) {
            store.createSalesOrder(new SalesOrder(0, "RC-" + (i + 1), rcCustomers[i].id(), rcDates[i], OrderStatus.CONFIRMED,
                    List.of(new OrderLine(rcItem.id(), 1, rcItem.unitPrice()))));
        }
        rcOrders.cancel("RC-8", CancellationReason.OTHER, LocalDate.of(2032, 3, 6));
        for (RepeatCustomer rcRepeat : rcOrders.getRepeatCustomersByRegion(2, LocalDate.of(2032, 3, 1), LocalDate.of(2032, 3, 31))) {
            out.println("  " + rcRepeat.region() + " " + rcRepeat.name() + ": " + rcRepeat.orderCount());
        }

        out.println("First-time customers in March 2032 (first two days):");
        SalesRep ftRep = store.createSalesRep(new SalesRep(0, "FT Rep", "ftrep@example.com", Region.NORTH));
        store.assignCustomerToSalesRep(rcAnna.id(), ftRep.id());
        store.assignCustomerToSalesRep(rcBen.id(), ftRep.id());
        for (FirstTimeCustomer ftCustomer : rcOrders.getFirstTimeCustomers(LocalDate.of(2032, 3, 1), LocalDate.of(2032, 3, 2))) {
            out.println("  " + ftCustomer.region() + " " + (ftCustomer.salesRep() == null ? "-" : ftCustomer.salesRep().name())
                    + " " + ftCustomer.name() + ": " + ftCustomer.firstOrderDate());
        }

        out.println("Active customers with orders but no invoice in March 2032:");
        SalesOrder wiOrder = store.findSalesOrderByNumber("RC-1").orElseThrow();
        store.createInvoice(new Invoice(0, "INV-WI-1", wiOrder.id(), rcAnna.id(), LocalDate.of(2032, 3, 20),
                LocalDate.of(2032, 4, 19), new BigDecimal("4.00"), InvoiceStatus.OPEN));
        for (Customer wiCustomer : rcOrders.getCustomersWithoutInvoices(LocalDate.of(2032, 3, 1), LocalDate.of(2032, 3, 31))) {
            out.println("  " + wiCustomer.region() + " " + wiCustomer.name());
        }

        out.println("Customers with open credit:");
        store.createWarehouse(new Warehouse(0, "WH-OC", "OC Warehouse", "Oslo", Region.NORTH));
        Product ocItem = store.createProduct(new Product(0, "OC-1", "OC Item", "SC-Fasteners", new BigDecimal("5.00"), "piece", true));
        Customer ocNorth = store.createCustomer(new Customer(0, "OC North", "ocn@example.com", null, "Norway", Region.NORTH, CustomerStatus.ACTIVE));
        Customer ocSouth = store.createCustomer(new Customer(0, "OC South", "ocs@example.com", null, "Spain", Region.SOUTH, CustomerStatus.ACTIVE));
        store.createSalesOrder(new SalesOrder(0, "SO-OC-1", ocNorth.id(), LocalDate.of(2032, 5, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(ocItem.id(), 20, new BigDecimal("5.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OC-2", ocSouth.id(), LocalDate.of(2032, 5, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(ocItem.id(), 20, new BigDecimal("5.00")))));
        ReturnService ocReturns = new ReturnService(store);
        ocReturns.createReturn("R-OC-1", "SO-OC-1", LocalDate.of(2032, 5, 5), "Damaged", "WH-OC", List.of(new ReturnLine(ocItem.id(), 2)));
        ocReturns.createReturn("R-OC-2", "SO-OC-1", LocalDate.of(2032, 5, 6), "Damaged", "WH-OC", List.of(new ReturnLine(ocItem.id(), 3)));
        ocReturns.createReturn("R-OC-3", "SO-OC-2", LocalDate.of(2032, 5, 7), "Damaged", "WH-OC", List.of(new ReturnLine(ocItem.id(), 4)));
        SalesOrder ocOrder = store.findSalesOrderByNumber("SO-OC-1").orElseThrow();
        store.createInvoice(new Invoice(0, "INV-OC-1", ocOrder.id(), ocNorth.id(), LocalDate.of(2032, 5, 10),
                LocalDate.of(2032, 6, 9), new BigDecimal("100.00"), InvoiceStatus.OPEN));
        ocReturns.applyCreditNote("CN-R-OC-1", "INV-OC-1", LocalDate.of(2032, 5, 11));
        for (CustomerOpenCredit ocCustomer : ocReturns.getCustomersWithOpenCredit()) {
            out.println("  " + ocCustomer.region() + " " + ocCustomer.name() + ": " + ocCustomer.noteCount() + " notes, "
                    + ocCustomer.openAmount());
        }
        for (RegionOpenCredit ocRegion : ocReturns.getOpenCreditPerRegion()) {
            out.println("  " + ocRegion.region() + " total: " + ocRegion.openAmount());
        }

        out.println("Customers over credit limit:");
        CreditService olCredit = new CreditService(store);
        SalesRep olRep = store.createSalesRep(new SalesRep(0, "OL Rep", "olrep@acme.example", Region.EAST));
        Customer olOne = store.createCustomer(new Customer(0, "OL One", "ol1@example.com", null, "Poland", Region.EAST, CustomerStatus.ACTIVE));
        Customer olTwo = store.createCustomer(new Customer(0, "OL Two", "ol2@example.com", null, "Poland", Region.EAST, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(olOne.id(), olRep.id());
        olCredit.setCreditLimit(olOne.id(), new BigDecimal("50.00"));
        olCredit.setCreditLimit(olTwo.id(), new BigDecimal("500.00"));
        store.createSalesOrder(new SalesOrder(0, "SO-OL-1", olOne.id(), LocalDate.of(2032, 7, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(ocItem.id(), 20, new BigDecimal("5.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OL-2", olTwo.id(), LocalDate.of(2032, 7, 1), OrderStatus.DELIVERED,
                List.of(new OrderLine(ocItem.id(), 20, new BigDecimal("5.00")))));
        Invoice olInvoice = store.createInvoice(new Invoice(0, "INV-OL-1", store.findSalesOrderByNumber("SO-OL-1").orElseThrow().id(),
                olOne.id(), LocalDate.of(2032, 7, 2), LocalDate.of(2032, 8, 1), new BigDecimal("100.00"), InvoiceStatus.OPEN));
        store.createInvoice(new Invoice(0, "INV-OL-2", store.findSalesOrderByNumber("SO-OL-2").orElseThrow().id(),
                olTwo.id(), LocalDate.of(2032, 7, 2), LocalDate.of(2032, 8, 1), new BigDecimal("100.00"), InvoiceStatus.OPEN));
        store.createPayment(new Payment(0, olInvoice.id(), LocalDate.of(2032, 7, 20), new BigDecimal("30.00")));
        for (LocalDate olDate : List.of(LocalDate.of(2032, 7, 10), LocalDate.of(2032, 7, 31))) {
            out.println("  On " + olDate + ":");
            for (CustomerOverLimit olCustomer : olCredit.getCustomersOverLimit(olDate)) {
                out.println("    " + olCustomer.region() + " " + olCustomer.salesRepName() + " " + olCustomer.name() + ": "
                        + olCustomer.openAmount() + " of " + olCustomer.creditLimit());
            }
            for (RegionOverLimit olRegion : olCredit.getOverLimitPerRegion(olDate)) {
                out.println("    " + olRegion.region() + " excess: " + olRegion.excessAmount());
            }
            for (RepOverLimit olRepTotal : olCredit.getOverLimitPerSalesRep(olDate)) {
                out.println("    " + olRepTotal.name() + " excess: " + olRepTotal.excessAmount());
            }
        }

        out.println("Shipments per region:");
        ShipmentService rgShipments = new ShipmentService(store);
        Warehouse rgWarehouse = store.createWarehouse(new Warehouse(0, "WH-RG", "RG Warehouse", "Lyon", Region.WEST));
        Carrier rgCarrier = store.createCarrier(new Carrier(0, "RG Carrier", ServiceLevel.STANDARD, new BigDecimal("1.00"), 2));
        Product rgItem = store.createProduct(new Product(0, "RG-1", "RG Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Customer rgWest = store.createCustomer(new Customer(0, "RG West", "rgw@example.com", null, "France", Region.WEST, CustomerStatus.ACTIVE));
        Customer rgEast = store.createCustomer(new Customer(0, "RG East", "rge@example.com", null, "Poland", Region.EAST, CustomerStatus.ACTIVE));
        LocalDate[][] rgDates = {{LocalDate.of(2034, 3, 3), null}, {LocalDate.of(2034, 3, 5), LocalDate.of(2034, 3, 9)},
                {LocalDate.of(2034, 3, 6), LocalDate.of(2034, 3, 7)}, {LocalDate.of(2034, 3, 8), null}};
        for (int i = 0; i < rgDates.length; i++) {
            Customer rgCustomer = i == 3 ? rgEast : rgWest;
            SalesOrder rgOrder = store.createSalesOrder(new SalesOrder(0, "SO-RG-" + (i + 1), rgCustomer.id(), LocalDate.of(2034, 3, 1),
                    OrderStatus.SHIPPED, List.of(new OrderLine(rgItem.id(), 1, new BigDecimal("1.00")))));
            store.createShipment(new Shipment(0, rgOrder.id(), rgWarehouse.id(), rgCarrier.id(), "RG-" + (i + 1), rgDates[i][0]));
            if (rgDates[i][1] != null) {
                store.setShipmentDeliveryDateIfInTransit(rgOrder.id(), rgDates[i][1]);
            }
        }
        RegionShipments rgResult = rgShipments.listByRegion(Region.WEST, LocalDate.of(2034, 3, 1), LocalDate.of(2034, 3, 31));
        for (Shipment rgShipment : rgResult.inTransit()) {
            out.println("  " + rgResult.region() + " in transit: " + rgShipment.trackingNumber() + " shipped " + rgShipment.shipDate());
        }
        for (Shipment rgShipment : rgResult.late()) {
            out.println("  " + rgResult.region() + " late: " + rgShipment.trackingNumber() + " shipped " + rgShipment.shipDate()
                    + ", delivered " + rgShipment.deliveryDate());
        }

        out.println("Shipments per product:");
        ShipmentService ppShipments = new ShipmentService(store);
        Warehouse ppWarehouseA = store.createWarehouse(new Warehouse(0, "WH-PPA", "PP Warehouse A", "Graz", Region.WEST));
        Warehouse ppWarehouseB = store.createWarehouse(new Warehouse(0, "WH-PPB", "PP Warehouse B", "Linz", Region.WEST));
        Carrier ppCarrier = store.createCarrier(new Carrier(0, "PP Carrier", ServiceLevel.STANDARD, new BigDecimal("1.00"), 2));
        Product ppItem = store.createProduct(new Product(0, "PP-1", "PP Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Customer ppCustomer = store.createCustomer(new Customer(0, "PP Customer", "pp@example.com", null, "Austria", Region.WEST, CustomerStatus.ACTIVE));
        Warehouse[] ppWarehouses = {ppWarehouseA, ppWarehouseB, ppWarehouseA};
        int[] ppQuantities = {5, 3, 4};
        for (int i = 0; i < ppWarehouses.length; i++) {
            SalesOrder ppOrder = store.createSalesOrder(new SalesOrder(0, "SO-PRODSHIP-" + (i + 1), ppCustomer.id(), LocalDate.of(2035, 4, 1),
                    OrderStatus.SHIPPED, List.of(new OrderLine(ppItem.id(), ppQuantities[i], new BigDecimal("1.00")))));
            store.createShipment(new Shipment(0, ppOrder.id(), ppWarehouses[i].id(), ppCarrier.id(), "PP-" + (i + 1),
                    LocalDate.of(2035, 4, 3 + i * 3)));
        }
        ProductShipments ppResult = ppShipments.listByProduct("PP-1", LocalDate.of(2035, 4, 1), LocalDate.of(2035, 4, 8));
        for (Shipment ppShipment : ppResult.shipments()) {
            out.println("  " + ppResult.product().sku() + " shipment: " + ppShipment.trackingNumber() + " shipped " + ppShipment.shipDate());
        }
        for (WarehouseShippedQuantity ppQuantity : ppResult.quantities()) {
            out.println("  " + ppResult.product().sku() + " from " + ppQuantity.warehouse().code() + ": " + ppQuantity.quantity());
        }

        out.println("Late shipments per warehouse:");
        ShipmentService lwShipments = new ShipmentService(store);
        Warehouse lwWarehouseA = store.createWarehouse(new Warehouse(0, "WH-LWA", "LW Warehouse A", "Graz", Region.WEST));
        Warehouse lwWarehouseB = store.createWarehouse(new Warehouse(0, "WH-LWB", "LW Warehouse B", "Linz", Region.WEST));
        Carrier lwCarrier = store.createCarrier(new Carrier(0, "LW Carrier", ServiceLevel.STANDARD, new BigDecimal("1.00"), 2));
        Product lwItem = store.createProduct(new Product(0, "LW-1", "LW Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Customer lwCustomer = store.createCustomer(new Customer(0, "LW Customer", "lw@example.com", null, "Austria", Region.WEST, CustomerStatus.ACTIVE));
        Warehouse[] lwWarehouses = {lwWarehouseA, lwWarehouseA, lwWarehouseA, lwWarehouseB, lwWarehouseB};
        int[] lwDays = {4, 1, 2, 3, 0};
        for (int i = 0; i < lwWarehouses.length; i++) {
            SalesOrder lwOrder = store.createSalesOrder(new SalesOrder(0, "SO-LATEWH-" + (i + 1), lwCustomer.id(), LocalDate.of(2036, 5, 1),
                    OrderStatus.SHIPPED, List.of(new OrderLine(lwItem.id(), 1, new BigDecimal("1.00")))));
            LocalDate lwShipDate = LocalDate.of(2036, 5, 2 + i);
            store.createShipment(new Shipment(0, lwOrder.id(), lwWarehouses[i].id(), lwCarrier.id(), "LW-" + (i + 1), lwShipDate));
            if (i < 4) {
                store.setShipmentDeliveryDateIfInTransit(lwOrder.id(), lwShipDate.plusDays(lwDays[i]));
            }
        }
        for (WarehouseLateShipments lwResult : lwShipments.listLateByWarehouse(LocalDate.of(2036, 5, 1), LocalDate.of(2036, 5, 31))) {
            out.println("  " + lwResult.warehouse().code() + " late share: " + lwResult.latePercent() + "% of " + lwResult.delivered());
            for (Shipment lwShipment : lwResult.late()) {
                out.println("    late: " + lwShipment.trackingNumber() + " shipped " + lwShipment.shipDate()
                        + ", delivered " + lwShipment.deliveryDate());
            }
        }

        out.println("Shipment weight:");
        ShipmentService swShipments = new ShipmentService(store);
        Warehouse swWarehouseA = store.createWarehouse(new Warehouse(0, "WH-SWA", "SW Warehouse A", "Graz", Region.WEST));
        Warehouse swWarehouseB = store.createWarehouse(new Warehouse(0, "WH-SWB", "SW Warehouse B", "Linz", Region.WEST));
        Carrier swCarrierA = store.createCarrier(new Carrier(0, "SW Carrier A", ServiceLevel.STANDARD, new BigDecimal("1.00"), 2));
        Carrier swCarrierB = store.createCarrier(new Carrier(0, "SW Carrier B", ServiceLevel.EXPRESS, new BigDecimal("2.00"), 1));
        Product swHeavy = store.createProduct(new Product(0, "SW-H", "SW Heavy", "Hardware", new BigDecimal("1.00"), "piece", true));
        Product swLight = store.createProduct(new Product(0, "SW-L", "SW Light", "Hardware", new BigDecimal("1.00"), "piece", true));
        swShipments.setProductWeight("SW-H", new BigDecimal("2.500"));
        swShipments.setProductWeight("SW-L", new BigDecimal("0.400"));
        Customer swCustomer = store.createCustomer(new Customer(0, "SW Customer", "sw@example.com", null, "Austria", Region.WEST, CustomerStatus.ACTIVE));
        Warehouse[] swWarehouses = {swWarehouseA, swWarehouseB, swWarehouseA};
        Carrier[] swCarriers = {swCarrierA, swCarrierA, swCarrierB};
        int[] swHeavyQuantities = {2, 1, 3};
        for (int i = 0; i < swWarehouses.length; i++) {
            SalesOrder swOrder = store.createSalesOrder(new SalesOrder(0, "SO-SHIPWT-" + (i + 1), swCustomer.id(), LocalDate.of(2036, 6, 1),
                    OrderStatus.SHIPPED, List.of(new OrderLine(swHeavy.id(), swHeavyQuantities[i], new BigDecimal("1.00")),
                            new OrderLine(swLight.id(), 5, new BigDecimal("1.00")))));
            store.createShipment(new Shipment(0, swOrder.id(), swWarehouses[i].id(), swCarriers[i].id(), "SW-" + (i + 1),
                    LocalDate.of(2036, 6, 2 + i)));
        }
        for (CarrierShippedWeight swResult : swShipments.shippedWeightByCarrier(LocalDate.of(2036, 6, 1), LocalDate.of(2036, 6, 30))) {
            out.println("  carrier " + swResult.carrier().name() + ": " + swResult.weightKg() + " kg");
        }
        for (WarehouseShippedWeight swResult : swShipments.shippedWeightByWarehouse(LocalDate.of(2036, 6, 1), LocalDate.of(2036, 6, 30))) {
            out.println("  warehouse " + swResult.warehouse().code() + ": " + swResult.weightKg() + " kg");
        }

        out.println("Returns per customer:");
        store.createWarehouse(new Warehouse(0, "WH-RPC", "RPC Warehouse", "Graz", Region.WEST));
        Product rcProduct = store.createProduct(new Product(0, "RPC-1", "RPC Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        ReturnService rcReturns = new ReturnService(store);
        String[] rcNames = {"RPC Alpha", "RPC Beta"};
        int[][] rcQuantities = {{2}, {1, 3, 4}};
        for (int i = 0; i < rcNames.length; i++) {
            Customer rcCustomer = store.createCustomer(new Customer(0, rcNames[i], "rpc" + (i + 1) + "@example.com", null, "Austria", Region.WEST, CustomerStatus.ACTIVE));
            store.createSalesOrder(new SalesOrder(0, "SO-RPC-" + (i + 1), rcCustomer.id(), LocalDate.of(2037, 3, 1), OrderStatus.SHIPPED,
                    List.of(new OrderLine(rcProduct.id(), 20, new BigDecimal("2.50")))));
            for (int j = 0; j < rcQuantities[i].length; j++) {
                rcReturns.createReturn("R-RPC-" + (i + 1) + "-" + (j + 1), "SO-RPC-" + (i + 1), LocalDate.of(2037, 3, 10 + j), "Damaged",
                        "WH-RPC", List.of(new ReturnLine(rcProduct.id(), rcQuantities[i][j])));
            }
        }
        for (CustomerReturns rcResult : rcReturns.listReturnsPerCustomer(LocalDate.of(2037, 3, 1), LocalDate.of(2037, 3, 31))) {
            out.println("  " + rcResult.customerName() + ": " + rcResult.returns().size() + " returns, " + rcResult.returnedValue());
            for (CustomerReturn rcReturn : rcResult.returns()) {
                out.println("    " + rcReturn.returnDate() + " " + rcReturn.number() + " " + rcReturn.returnedValue());
            }
        }

        out.println("Returns per warehouse:");
        store.createWarehouse(new Warehouse(0, "WH-RPW1", "RPW Warehouse 1", "Linz", Region.WEST));
        store.createWarehouse(new Warehouse(0, "WH-RPW2", "RPW Warehouse 2", "Wels", Region.WEST));
        Product rwProduct1 = store.createProduct(new Product(0, "RPW-1", "RPW Item 1", "Hardware", new BigDecimal("1.00"), "piece", true));
        Product rwProduct2 = store.createProduct(new Product(0, "RPW-2", "RPW Item 2", "Hardware", new BigDecimal("1.00"), "piece", true));
        ReturnService rwReturns = new ReturnService(store);
        Customer rwCustomer = store.createCustomer(new Customer(0, "RPW Customer", "rpw@example.com", null, "Austria", Region.WEST, CustomerStatus.ACTIVE));
        store.createSalesOrder(new SalesOrder(0, "SO-RPW-1", rwCustomer.id(), LocalDate.of(2037, 4, 1), OrderStatus.SHIPPED,
                List.of(new OrderLine(rwProduct1.id(), 20, new BigDecimal("2.50")), new OrderLine(rwProduct2.id(), 20, new BigDecimal("3.50")))));
        rwReturns.createReturn("R-RPW-1", "SO-RPW-1", LocalDate.of(2037, 4, 10), "Damaged", "WH-RPW1",
                List.of(new ReturnLine(rwProduct1.id(), 2), new ReturnLine(rwProduct2.id(), 5)));
        rwReturns.createReturn("R-RPW-2", "SO-RPW-1", LocalDate.of(2037, 4, 12), "Wrong item", "WH-RPW1",
                List.of(new ReturnLine(rwProduct1.id(), 4)));
        rwReturns.createReturn("R-RPW-3", "SO-RPW-1", LocalDate.of(2037, 4, 15), "Damaged", "WH-RPW2",
                List.of(new ReturnLine(rwProduct1.id(), 1)));
        rwReturns.createReturn("R-RPW-4", "SO-RPW-1", LocalDate.of(2037, 5, 2), "Damaged", "WH-RPW1",
                List.of(new ReturnLine(rwProduct2.id(), 3)));
        WarehouseReturns rwResult = rwReturns.listReturnsByWarehouse("WH-RPW1", LocalDate.of(2037, 4, 1), LocalDate.of(2037, 4, 30));
        out.println("  warehouse " + rwResult.warehouse().code() + ": " + rwResult.returns().size() + " returns");
        for (WarehouseReturn rwReturn : rwResult.returns()) {
            out.println("    " + rwReturn.returnDate() + " " + rwReturn.number() + " " + rwReturn.orderNumber() + " " + rwReturn.reason());
        }
        for (ProductReturnedQuantity rwQuantity : rwResult.quantities()) {
            out.println("    " + rwQuantity.sku() + ": " + rwQuantity.quantity());
        }

        out.println("Purchase orders per product:");
        Product popProduct = store.createProduct(new Product(0, "POP-1", "POP Item 1", "Hardware", new BigDecimal("1.00"), "piece", true));
        Product popOther = store.createProduct(new Product(0, "POP-2", "POP Item 2", "Hardware", new BigDecimal("1.00"), "piece", true));
        Supplier popSupplier1 = store.createSupplier(new Supplier(0, "POP Supplier 1", "pop1@example.com", "Austria", 4));
        Supplier popSupplier2 = store.createSupplier(new Supplier(0, "POP Supplier 2", "pop2@example.com", "Germany", 3));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POP-1", popSupplier1.id(), LocalDate.of(2038, 2, 3), LocalDate.of(2038, 2, 20),
                PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(popProduct.id(), 30, new BigDecimal("1.20")),
                new PurchaseOrderLine(popOther.id(), 5, new BigDecimal("2.00")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POP-2", popSupplier2.id(), LocalDate.of(2038, 2, 10), LocalDate.of(2038, 2, 25),
                PurchaseOrderStatus.DRAFT, List.of(new PurchaseOrderLine(popProduct.id(), 50, new BigDecimal("1.10")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POP-3", popSupplier1.id(), LocalDate.of(2038, 2, 17), LocalDate.of(2038, 3, 1),
                PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(popProduct.id(), 15, new BigDecimal("1.25")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POP-4", popSupplier2.id(), LocalDate.of(2038, 3, 5), LocalDate.of(2038, 3, 20),
                PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(popProduct.id(), 99, new BigDecimal("1.10")))));
        ProductPurchaseOrders popResult = new PurchaseReceivingService(store).listPurchaseOrdersByProduct("POP-1",
                LocalDate.of(2038, 2, 1), LocalDate.of(2038, 2, 28));
        out.println("  product " + popResult.product().sku() + ": " + popResult.orders().size() + " purchase orders");
        for (PurchaseOrder popOrder : popResult.orders()) {
            out.println("    " + popOrder.orderDate() + " " + popOrder.number() + " " + popOrder.status());
        }
        for (SupplierPurchasedQuantity popQuantity : popResult.quantities()) {
            out.println("    " + popQuantity.supplier().name() + ": " + popQuantity.quantity());
        }

        out.println("Purchase orders per category:");
        store.createCategory(new Category("POC", "POC Root", null));
        store.createCategory(new Category("POC-SUB", "POC Sub", "POC"));
        Product pocRoot = store.createProduct(new Product(0, "POC-1", "POC Item 1", "POC", new BigDecimal("1.00"), "piece", true));
        Product pocSub = store.createProduct(new Product(0, "POC-2", "POC Item 2", "POC-SUB", new BigDecimal("1.00"), "piece", true));
        Product pocOther = store.createProduct(new Product(0, "POC-3", "POC Item 3", "Hardware", new BigDecimal("1.00"), "piece", true));
        Supplier pocSupplier = store.createSupplier(new Supplier(0, "POC Supplier", "poc@example.com", "Austria", 4));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POC-1", pocSupplier.id(), LocalDate.of(2039, 2, 3), LocalDate.of(2039, 2, 20),
                PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(pocRoot.id(), 10, new BigDecimal("2.00")),
                new PurchaseOrderLine(pocOther.id(), 5, new BigDecimal("9.00")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POC-2", pocSupplier.id(), LocalDate.of(2039, 2, 10), LocalDate.of(2039, 2, 25),
                PurchaseOrderStatus.DRAFT, List.of(new PurchaseOrderLine(pocSub.id(), 4, new BigDecimal("3.50")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POC-3", pocSupplier.id(), LocalDate.of(2039, 2, 12), LocalDate.of(2039, 2, 26),
                PurchaseOrderStatus.CANCELLED, List.of(new PurchaseOrderLine(pocRoot.id(), 100, new BigDecimal("2.00")))));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-POC-4", pocSupplier.id(), LocalDate.of(2039, 3, 5), LocalDate.of(2039, 3, 20),
                PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(pocRoot.id(), 99, new BigDecimal("2.00")))));
        CategoryPurchaseOrders pocResult = new PurchaseReceivingService(store).listPurchaseOrdersByCategory("POC",
                LocalDate.of(2039, 2, 1), LocalDate.of(2039, 2, 28));
        out.println("  category " + pocResult.category().code() + ": " + pocResult.orders().size() + " purchase orders");
        for (PurchaseOrder pocOrder : pocResult.orders()) {
            out.println("    " + pocOrder.orderDate() + " " + pocOrder.number() + " " + pocOrder.status());
        }
        for (CategorySpend pocSpend : pocResult.spend()) {
            out.println("    " + pocSpend.category() + ": " + pocSpend.spend());
        }

        out.println("Late purchase orders per warehouse:");
        store.createWarehouse(new Warehouse(0, "WH-LPO", "LPO Hub", "Tallinn", Region.NORTH));
        store.createWarehouse(new Warehouse(0, "WH-LPO2", "LPO Depot", "Vilnius", Region.EAST));
        Product lpoProduct = store.createProduct(new Product(0, "LPO-1", "LPO Item", "Hardware", new BigDecimal("1.00"), "piece", true));
        Supplier lpoSupplier = store.createSupplier(new Supplier(0, "LPO Supplier", "lpo@example.com", "Estonia", 5));
        String[][] lpoData = {{"PO-LPO-1", "2040-04-20", "2040-05-04", "WH-LPO", "2040-05-07"},
                {"PO-LPO-2", "2040-04-25", "2040-05-10", "WH-LPO", "2040-05-10"},
                {"PO-LPO-3", "2040-04-25", "2040-05-02", "WH-LPO", "2040-05-12"},
                {"PO-LPO-4", "2040-04-25", "2040-05-10", "WH-LPO2", "2040-05-20"},
                {"PO-LPO-5", "2040-05-20", "2040-06-01", "WH-LPO", "2040-06-05"}};
        PurchaseReceivingService lpoReceiving = new PurchaseReceivingService(store);
        for (String[] lpo : lpoData) {
            store.createPurchaseOrder(new PurchaseOrder(0, lpo[0], lpoSupplier.id(), LocalDate.parse(lpo[1]), LocalDate.parse(lpo[2]),
                    PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(lpoProduct.id(), 10, new BigDecimal("1.00")))));
            lpoReceiving.receive(lpo[0], lpo[3], LocalDate.parse(lpo[4]));
        }
        WarehouseLatePurchaseOrders lpoResult = lpoReceiving.listLateByWarehouse("WH-LPO",
                LocalDate.of(2040, 5, 1), LocalDate.of(2040, 5, 31));
        out.println("  warehouse " + lpoResult.warehouse().code() + ": " + lpoResult.late().size() + " late purchase orders");
        for (LatePurchaseOrder lpoLate : lpoResult.late()) {
            out.println("    " + lpoLate.receivedDate() + " " + lpoLate.order().number() + " " + lpoLate.daysLate() + " days late");
        }

        out.println("Supplier products per category:");
        store.createCategory(new Category("SPC", "SPC Root", null));
        store.createCategory(new Category("SPC-SUB", "SPC Sub", "SPC"));
        Product spcRoot = store.createProduct(new Product(0, "SPC-1", "SPC Item 1", "SPC", new BigDecimal("1.00"), "piece", true));
        Product spcSub = store.createProduct(new Product(0, "SPC-2", "SPC Item 2", "SPC-SUB", new BigDecimal("1.00"), "piece", true));
        Supplier spcFirst = store.createSupplier(new Supplier(0, "SPC First", "spc1@example.com", "Spain", 4));
        Supplier spcSecond = store.createSupplier(new Supplier(0, "SPC Second", "spc2@example.com", "Portugal", 3));
        SupplierProductService spcService = new SupplierProductService(store);
        spcService.record(spcFirst.id(), spcRoot.id(), 5, new BigDecimal("2.50"));
        spcService.record(spcSecond.id(), spcRoot.id(), 3, new BigDecimal("1.75"));
        spcService.record(spcFirst.id(), spcSub.id(), 7, new BigDecimal("3.00"));
        CategorySupplierProducts spcResult = spcService.listSupplierProductsOfCategory("SPC");
        out.println("  category " + spcResult.category().code() + ": " + spcResult.supplierProducts().size() + " supplier products");
        for (SupplierProduct spcProduct : spcResult.supplierProducts()) {
            out.println("    " + spcProduct.lastUnitCost() + " supplier " + spcProduct.supplierId() + " product "
                    + spcProduct.productId() + " " + spcProduct.leadTimeDays() + " days");
        }
        for (CategorySupplier spcSingle : spcResult.singleSupplier()) {
            out.println("    single supplier " + spcSingle.category() + ": " + spcSingle.supplier().name());
        }

        out.println("Stock per supplier:");
        Warehouse sksA = store.createWarehouse(new Warehouse(0, "SKS-A", "SKS Alpha", "Oslo", Region.NORTH));
        Warehouse sksB = store.createWarehouse(new Warehouse(0, "SKS-B", "SKS Beta", "Warsaw", Region.EAST));
        Product sksNail = store.createProduct(new Product(0, "SKS-1", "SKS Nail", "SKS", new BigDecimal("1.00"), "piece", true));
        Product sksScrew = store.createProduct(new Product(0, "SKS-2", "SKS Screw", "SKS", new BigDecimal("2.00"), "piece", true));
        Product sksPaint = store.createProduct(new Product(0, "SKS-3", "SKS Paint", "SKS", new BigDecimal("10.00"), "piece", true));
        Supplier sksSupplier = store.createSupplier(new Supplier(0, "SKS Supplier", "sks@example.com", "Sweden", 4));
        SupplierProductService sksService = new SupplierProductService(store);
        sksService.record(sksSupplier.id(), sksNail.id(), 5, new BigDecimal("0.50"));
        sksService.record(sksSupplier.id(), sksScrew.id(), 5, new BigDecimal("1.50"));
        store.createPurchaseOrder(new PurchaseOrder(0, "PO-SKS-1", sksSupplier.id(), LocalDate.of(2028, 8, 1), LocalDate.of(2028, 8, 10), PurchaseOrderStatus.RECEIVED,
                List.of(new PurchaseOrderLine(sksNail.id(), 100, new BigDecimal("0.50")), new PurchaseOrderLine(sksScrew.id(), 100, new BigDecimal("1.50")),
                        new PurchaseOrderLine(sksPaint.id(), 100, new BigDecimal("6.00")))));
        store.setStockQuantity(sksA.id(), sksNail.id(), 40);
        store.setStockQuantity(sksA.id(), sksScrew.id(), 10);
        store.setStockQuantity(sksA.id(), sksPaint.id(), 5);
        store.setStockQuantity(sksB.id(), sksNail.id(), 20);
        SupplierStock sksResult = sksService.getStockOfSupplier(sksSupplier.id());
        out.println("  supplier " + sksResult.supplier().name());
        for (SupplierWarehouseStock sksEntry : sksResult.warehouses()) {
            out.println("    " + sksEntry.warehouse().code() + ": " + sksEntry.quantity() + " pieces, " + sksEntry.value());
        }

        out.println("Stock movements per reason:");
        Warehouse smrA = store.createWarehouse(new Warehouse(0, "SMR-A", "SMR Alpha", "Oslo", Region.NORTH));
        Warehouse smrB = store.createWarehouse(new Warehouse(0, "SMR-B", "SMR Beta", "Warsaw", Region.EAST));
        Product smrNail = store.createProduct(new Product(0, "SMR-1", "SMR Nail", "SMR-Fasteners", new BigDecimal("1.00"), "piece", true));
        Product smrPaint = store.createProduct(new Product(0, "SMR-2", "SMR Paint", "SMR-Paints", new BigDecimal("10.00"), "piece", true));
        store.createStockMovement(smrNail.id(), smrA.id(), LocalDate.of(2029, 3, 1), 100, StockMovementReason.RECEIPT);
        store.createStockMovement(smrPaint.id(), smrA.id(), LocalDate.of(2029, 3, 2), 20, StockMovementReason.RECEIPT);
        store.createStockMovement(smrNail.id(), smrA.id(), LocalDate.of(2029, 3, 5), -30, StockMovementReason.SHIPMENT);
        store.createStockMovement(smrNail.id(), smrB.id(), LocalDate.of(2029, 3, 6), -10, StockMovementReason.SHIPMENT);
        store.createStockMovement(smrNail.id(), smrB.id(), LocalDate.of(2029, 3, 7), -5, StockMovementReason.SHIPMENT);
        store.createStockMovement(smrNail.id(), smrB.id(), LocalDate.of(2029, 4, 1), -50, StockMovementReason.SHIPMENT);
        for (ReasonMovements smrEntry : new StockService(store).getMovementsPerReason(LocalDate.of(2029, 3, 1), LocalDate.of(2029, 3, 31))) {
            out.println("  " + smrEntry.reason() + " " + smrEntry.warehouse().code() + " " + smrEntry.category() + ": "
                    + smrEntry.movementCount() + " movements, " + smrEntry.quantityChange());
        }

        out.println("Stock movements per category:");
        Warehouse smcA = store.createWarehouse(new Warehouse(0, "SMC-A", "SMC Alpha", "Oslo", Region.NORTH));
        Warehouse smcB = store.createWarehouse(new Warehouse(0, "SMC-B", "SMC Beta", "Warsaw", Region.EAST));
        Product smcNail = store.createProduct(new Product(0, "SMC-1", "SMC Nail", "SMC-Fasteners", new BigDecimal("1.00"), "piece", true));
        Product smcPaint = store.createProduct(new Product(0, "SMC-2", "SMC Paint", "SMC-Paints", new BigDecimal("10.00"), "piece", true));
        store.createStockMovement(smcNail.id(), smcB.id(), LocalDate.of(2030, 3, 6), -10, StockMovementReason.SHIPMENT);
        store.createStockMovement(smcNail.id(), smcA.id(), LocalDate.of(2030, 3, 5), 100, StockMovementReason.RECEIPT);
        store.createStockMovement(smcPaint.id(), smcA.id(), LocalDate.of(2030, 3, 5), 20, StockMovementReason.RECEIPT);
        store.createStockMovement(smcNail.id(), smcA.id(), LocalDate.of(2030, 3, 8), -30, StockMovementReason.SHIPMENT);
        store.createStockMovement(smcNail.id(), smcA.id(), LocalDate.of(2030, 4, 1), -50, StockMovementReason.SHIPMENT);
        for (WarehouseMovements smcEntry : new StockService(store).getMovementsOfCategory("SMC-Fasteners", LocalDate.of(2030, 3, 1), LocalDate.of(2030, 3, 31))) {
            out.println("  " + smcEntry.warehouse().code() + ":");
            for (StockMovement smcMovement : smcEntry.movements()) {
                out.println("    " + smcMovement.movementDate() + " " + smcMovement.reason() + " " + smcMovement.quantityChange());
            }
        }

        out.println("Negative adjustments:");
        Warehouse naA = store.createWarehouse(new Warehouse(0, "NA-A", "NA Alpha", "Oslo", Region.NORTH));
        Warehouse naB = store.createWarehouse(new Warehouse(0, "NA-B", "NA Beta", "Warsaw", Region.EAST));
        Product naNail = store.createProduct(new Product(0, "NA-1", "NA Nail", "NA-Fasteners", new BigDecimal("1.00"), "piece", true));
        Product naPaint = store.createProduct(new Product(0, "NA-2", "NA Paint", "NA-Paints", new BigDecimal("10.00"), "piece", true));
        store.setStockQuantity(naA.id(), naNail.id(), 100);
        store.setStockQuantity(naA.id(), naPaint.id(), 50);
        store.setStockQuantity(naB.id(), naNail.id(), 40);
        StockAdjustmentService naAdjustments = new StockAdjustmentService(store);
        naAdjustments.adjust(naNail.id(), naA.id(), LocalDate.of(2031, 3, 5), 95, "Damaged");
        naAdjustments.adjust(naPaint.id(), naA.id(), LocalDate.of(2031, 3, 6), 30, "Leaked");
        naAdjustments.adjust(naNail.id(), naB.id(), LocalDate.of(2031, 3, 7), 45, "Found");
        naAdjustments.adjust(naPaint.id(), naA.id(), LocalDate.of(2031, 3, 8), 35, "Found");
        naAdjustments.adjust(naNail.id(), naB.id(), LocalDate.of(2031, 3, 9), 30, "Lost");
        naAdjustments.adjust(naNail.id(), naA.id(), LocalDate.of(2031, 4, 2), 0, "Out of period");
        for (WarehouseAdjustments naEntry : naAdjustments.getNegativeAdjustments(LocalDate.of(2031, 3, 1), LocalDate.of(2031, 3, 31))) {
            out.println("  " + naEntry.warehouse().code() + ":");
            for (StockAdjustment naAdjustment : naEntry.adjustments()) {
                out.println("    " + naAdjustment.adjustmentDate() + " product " + naAdjustment.productId() + ": "
                        + naAdjustment.difference() + " (" + naAdjustment.reason() + ")");
            }
        }

        out.println("Transfers per product:");
        Warehouse xfpA = store.createWarehouse(new Warehouse(0, "XFP-A", "XFP Alpha", "Oslo", Region.NORTH));
        Warehouse xfpB = store.createWarehouse(new Warehouse(0, "XFP-B", "XFP Beta", "Warsaw", Region.EAST));
        Product xfpNail = store.createProduct(new Product(0, "XFP-1", "XFP Nail", "XFP-Fasteners", new BigDecimal("1.00"), "piece", true));
        Product xfpPaint = store.createProduct(new Product(0, "XFP-2", "XFP Paint", "XFP-Paints", new BigDecimal("10.00"), "piece", true));
        store.setStockQuantity(xfpA.id(), xfpNail.id(), 100);
        store.setStockQuantity(xfpA.id(), xfpPaint.id(), 50);
        store.setStockQuantity(xfpB.id(), xfpNail.id(), 100);
        WarehouseTransferService xfpTransfers = new WarehouseTransferService(store);
        xfpTransfers.transfer("XFP-1", xfpA.id(), xfpB.id(), LocalDate.of(2031, 5, 2),
                List.of(new WarehouseTransferLine(xfpNail.id(), 10), new WarehouseTransferLine(xfpPaint.id(), 4)));
        xfpTransfers.transfer("XFP-2", xfpB.id(), xfpA.id(), LocalDate.of(2031, 5, 4),
                List.of(new WarehouseTransferLine(xfpNail.id(), 3)));
        xfpTransfers.transfer("XFP-3", xfpA.id(), xfpB.id(), LocalDate.of(2031, 5, 6),
                List.of(new WarehouseTransferLine(xfpNail.id(), 7)));
        xfpTransfers.transfer("XFP-4", xfpA.id(), xfpB.id(), LocalDate.of(2031, 5, 8),
                List.of(new WarehouseTransferLine(xfpPaint.id(), 5)));
        xfpTransfers.transfer("XFP-5", xfpA.id(), xfpB.id(), LocalDate.of(2031, 6, 2),
                List.of(new WarehouseTransferLine(xfpNail.id(), 20)));
        for (WarehouseTransfer xfpTransfer : xfpTransfers.listByProduct(xfpNail.id(), LocalDate.of(2031, 5, 1), LocalDate.of(2031, 5, 31))) {
            out.println("  " + xfpTransfer.number() + " " + xfpTransfer.transferDate() + " lines " + xfpTransfer.lines().size());
        }
        for (TransferRoute xfpRoute : xfpTransfers.quantityPerRoute(xfpNail.id(), LocalDate.of(2031, 5, 1), LocalDate.of(2031, 5, 31))) {
            out.println("  " + xfpRoute.fromWarehouseCode() + " -> " + xfpRoute.toWarehouseCode() + ": " + xfpRoute.quantity());
        }

        out.println("Promotions per region:");
        PromotionService prrPromotions = new PromotionService(store);
        prrPromotions.createPromotion("PRR-ALL", new BigDecimal("5"), LocalDate.of(2032, 3, 1), LocalDate.of(2032, 3, 31), null);
        prrPromotions.createPromotion("PRR-N", new BigDecimal("10"), LocalDate.of(2032, 3, 10), LocalDate.of(2032, 3, 20), null);
        prrPromotions.createPromotion("PRR-S", new BigDecimal("15"), LocalDate.of(2032, 3, 1), LocalDate.of(2032, 3, 31), null);
        prrPromotions.limitToRegion("PRR-N", Region.NORTH);
        prrPromotions.limitToRegion("PRR-S", Region.SOUTH);
        for (Region prrRegion : Region.values()) {
            for (Promotion prrPromotion : prrPromotions.listActive(LocalDate.of(2032, 3, 15), prrRegion)) {
                out.println("  " + prrRegion + ": " + prrPromotion.code() + " " + prrPromotion.discountPercent() + "%");
            }
        }

        out.println("Promotions per customer:");
        Customer pcCustomer = store.createCustomer(new Customer(0, "PC Cust", "pc@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer pcOther = store.createCustomer(new Customer(0, "PC Other", "pco@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product pcPaint = store.createProduct(new Product(0, "PC-P", "PC Paint", "PC-Paints", new BigDecimal("10.00"), "piece", true));
        PromotionService pcPromotions = new PromotionService(store);
        pcPromotions.createPromotion("PC-PAINT20", new BigDecimal("20"), LocalDate.of(2033, 3, 1), LocalDate.of(2033, 3, 31), "PC-Paints");
        OrderEntryService pcEntry = new OrderEntryService(store);
        pcEntry.placeOrder("SO-PC-1", pcCustomer.id(), LocalDate.of(2033, 3, 5), OrderStatus.CONFIRMED,
                List.of(new OrderLine(pcPaint.id(), 10, new BigDecimal("10.00"))));
        pcEntry.placeOrder("SO-PC-2", pcCustomer.id(), LocalDate.of(2033, 3, 12), OrderStatus.CONFIRMED,
                List.of(new OrderLine(pcPaint.id(), 5, new BigDecimal("10.00"))));
        pcEntry.placeOrder("SO-PC-3", pcOther.id(), LocalDate.of(2033, 3, 8), OrderStatus.CONFIRMED,
                List.of(new OrderLine(pcPaint.id(), 1, new BigDecimal("10.00"))));
        CustomerPromotions pcResult = pcPromotions.promotionsOfCustomer(pcCustomer.id(), LocalDate.of(2033, 3, 1), LocalDate.of(2033, 3, 31));
        for (CustomerPromotionLine pcLine : pcResult.lines()) {
            out.println("  " + pcLine.orderNumber() + " line " + pcLine.lineNo() + " " + pcLine.sku() + " x" + pcLine.quantity()
                    + " " + pcLine.promotionCode() + " discount " + pcLine.discount());
        }
        out.println("  Total discount: " + pcResult.discount());

        out.println("Price list per category:");
        Customer plcCustomer = store.createCustomer(new Customer(0, "PLC Cust", "plc@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        store.createCategory(new Category("PLC-Tools", "PLC Tools", null));
        store.createCategory(new Category("PLC-Hand", "PLC Hand tools", "PLC-Tools"));
        store.createCategory(new Category("PLC-Other", "PLC Other", null));
        Product plcRoller = store.createProduct(new Product(0, "PLC-R", "PLC Roller", "PLC-Hand", new BigDecimal("10.00"), "piece", true));
        Product plcBrush = store.createProduct(new Product(0, "PLC-B", "PLC Brush", "PLC-Tools", new BigDecimal("4.00"), "piece", true));
        Product plcTape = store.createProduct(new Product(0, "PLC-T", "PLC Tape", "PLC-Other", new BigDecimal("2.00"), "piece", true));
        PriceListService plcPrices = new PriceListService(store);
        plcPrices.addPrice(plcCustomer.id(), plcRoller.id(), new BigDecimal("8.50"), LocalDate.of(2034, 1, 1), LocalDate.of(2034, 6, 30));
        plcPrices.addPrice(plcCustomer.id(), plcBrush.id(), new BigDecimal("3.25"), LocalDate.of(2034, 1, 1), LocalDate.of(2034, 3, 31));
        plcPrices.addPrice(plcCustomer.id(), plcTape.id(), new BigDecimal("1.50"), LocalDate.of(2034, 1, 1), LocalDate.of(2034, 6, 30));
        for (LocalDate plcDate : List.of(LocalDate.of(2034, 2, 1), LocalDate.of(2034, 5, 1))) {
            out.println("  PLC-Tools on " + plcDate + ":");
            for (CustomerPrice plcPrice : plcPrices.listPricesByCategory(plcCustomer.id(), "PLC-Tools", plcDate)) {
                out.println("    product " + store.findProductById(plcPrice.productId()).orElseThrow().sku() + " " + plcPrice.unitPrice());
            }
        }

        out.println("Expiring price lists:");
        Customer epCustomer = store.createCustomer(new Customer(0, "EP Cust", "ep@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer epOther = store.createCustomer(new Customer(0, "EP Other", "epo@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product epProduct = store.createProduct(new Product(0, "EP-P", "EP Product", "PLC-Other", new BigDecimal("5.00"), "piece", true));
        PriceListService epPrices = new PriceListService(store);
        epPrices.addPrice(epOther.id(), epProduct.id(), new BigDecimal("4.00"), LocalDate.of(2035, 1, 1), LocalDate.of(2035, 1, 20));
        epPrices.addPrice(epCustomer.id(), epProduct.id(), new BigDecimal("4.50"), LocalDate.of(2035, 1, 1), LocalDate.of(2035, 1, 10));
        epPrices.addPrice(epCustomer.id(), epProduct.id(), new BigDecimal("4.25"), LocalDate.of(2035, 1, 11), LocalDate.of(2035, 3, 31));
        for (CustomerPrice epPrice : epPrices.listExpiringPrices(LocalDate.of(2035, 1, 5), 30)) {
            out.println("  customer " + store.findCustomerById(epPrice.customerId()).orElseThrow().name()
                    + " " + epPrice.unitPrice() + " expires " + epPrice.endDate());
        }

        out.println("Tickets per sales rep:");
        SalesRep tsRep = store.createSalesRep(new SalesRep(0, "TS Rep", "tsrep@example.com", Region.SOUTH));
        Customer tsFirst = store.createCustomer(new Customer(0, "TS First", "tsf@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer tsSecond = store.createCustomer(new Customer(0, "TS Second", "tss@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer tsOther = store.createCustomer(new Customer(0, "TS Other", "tso@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(tsFirst.id(), tsRep.id());
        store.assignCustomerToSalesRep(tsSecond.id(), tsRep.id());
        SupportTicketService tsTickets = new SupportTicketService(store);
        tsTickets.create("TS-1", tsFirst.id(), null, "Low one", TicketPriority.LOW, LocalDate.of(2035, 2, 1));
        tsTickets.create("TS-2", tsSecond.id(), null, "High newer", TicketPriority.HIGH, LocalDate.of(2035, 2, 5));
        tsTickets.create("TS-3", tsFirst.id(), null, "High older", TicketPriority.HIGH, LocalDate.of(2035, 2, 3));
        tsTickets.create("TS-4", tsSecond.id(), null, "Closed", TicketPriority.NORMAL, LocalDate.of(2035, 2, 2));
        tsTickets.changeStatus("TS-4", TicketStatus.CLOSED, LocalDate.of(2035, 2, 4));
        tsTickets.create("TS-5", tsOther.id(), null, "Other rep", TicketPriority.HIGH, LocalDate.of(2035, 2, 1));
        for (Map.Entry<TicketPriority, List<SupportTicket>> tsEntry : tsTickets.listOpenBySalesRepPerPriority(tsRep.id()).entrySet()) {
            out.println("  " + tsEntry.getKey() + ": " + tsEntry.getValue().stream().map(SupportTicket::number).toList());
        }

        out.println("Tickets per order:");
        Customer toFirst = store.createCustomer(new Customer(0, "TO First", "tof@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer toSecond = store.createCustomer(new Customer(0, "TO Second", "tos@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        store.createSalesOrder(new SalesOrder(0, "TO-SO-1", toFirst.id(), LocalDate.of(2036, 3, 1), OrderStatus.CONFIRMED, List.of()));
        store.createSalesOrder(new SalesOrder(0, "TO-SO-2", toSecond.id(), LocalDate.of(2036, 3, 1), OrderStatus.CONFIRMED, List.of()));
        SupportTicketService toTickets = new SupportTicketService(store);
        toTickets.create("TO-1", toFirst.id(), "TO-SO-1", "Damaged", TicketPriority.HIGH, LocalDate.of(2036, 3, 2));
        toTickets.create("TO-2", toFirst.id(), null, "No order", TicketPriority.LOW, LocalDate.of(2036, 3, 3));
        toTickets.create("TO-3", toFirst.id(), "TO-SO-1", "Missing", TicketPriority.NORMAL, LocalDate.of(2036, 3, 4));
        toTickets.create("TO-4", toSecond.id(), "TO-SO-2", "Late", TicketPriority.NORMAL, LocalDate.of(2036, 3, 5));
        for (Customer toCustomer : List.of(toFirst, toSecond)) {
            out.println("  " + toCustomer.name() + ": " + toTickets.listWithOrderByCustomer(toCustomer.id()).stream().map(SupportTicket::number).toList());
        }
        for (OrderTickets toEntry : toTickets.listOrdersWithMultipleTickets()) {
            out.println("  " + toEntry.orderNumber() + " has " + toEntry.ticketCount() + " tickets");
        }

        out.println("Closed tickets:");
        Customer ctFirst = store.createCustomer(new Customer(0, "CT First", "ctf@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer ctSecond = store.createCustomer(new Customer(0, "CT Second", "cts@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        SupportTicketService ctTickets = new SupportTicketService(store);
        ctTickets.create("CT-1", ctFirst.id(), null, "Low", TicketPriority.LOW, LocalDate.of(2037, 4, 1));
        ctTickets.changeStatus("CT-1", TicketStatus.CLOSED, LocalDate.of(2037, 4, 11));
        ctTickets.create("CT-2", ctFirst.id(), null, "High", TicketPriority.HIGH, LocalDate.of(2037, 4, 2));
        ctTickets.changeStatus("CT-2", TicketStatus.CLOSED, LocalDate.of(2037, 4, 4));
        ctTickets.create("CT-3", ctSecond.id(), null, "Normal", TicketPriority.NORMAL, LocalDate.of(2037, 4, 3));
        ctTickets.changeStatus("CT-3", TicketStatus.CLOSED, LocalDate.of(2037, 4, 3));
        ctTickets.create("CT-4", ctSecond.id(), null, "Too late", TicketPriority.HIGH, LocalDate.of(2037, 4, 3));
        ctTickets.changeStatus("CT-4", TicketStatus.CLOSED, LocalDate.of(2037, 5, 3));
        ctTickets.create("CT-5", ctSecond.id(), null, "Still open", TicketPriority.HIGH, LocalDate.of(2037, 4, 3));
        for (ClosedTicket ctEntry : ctTickets.listClosed(LocalDate.of(2037, 4, 1), LocalDate.of(2037, 4, 30))) {
            out.println("  " + ctEntry.customerName() + " " + ctEntry.priority() + " " + ctEntry.number() + " closed in " + ctEntry.daysToClose() + " days");
        }

        out.println("Contacts per region:");
        Customer crFirst = store.createCustomer(new Customer(0, "CR First", "crf@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Customer crSecond = store.createCustomer(new Customer(0, "CR Second", "crs@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Customer crOther = store.createCustomer(new Customer(0, "CR Other", "cro@example.com", null, "UK", Region.WEST, CustomerStatus.ACTIVE));
        ContactService crContacts = new ContactService(store);
        crContacts.addContact(crFirst.id(), "Una Cole", "Buyer", "una@crfirst.example", null);
        crContacts.addContact(crFirst.id(), "Vic Moon", "Accountant", "vic@crfirst.example", null);
        crContacts.addContact(crSecond.id(), "Wes Hall", "buyer", "wes@crsecond.example", null);
        crContacts.addContact(crOther.id(), "Xia Fox", "Director", "xia@crother.example", null);
        for (RoleContacts crEntry : crContacts.listContactsPerRole(Region.EAST)) {
            out.println("  " + crEntry.role() + ":");
            for (RegionContact crContact : crEntry.contacts()) {
                out.println("    " + crContact.contact().name() + " of " + crContact.customerName());
            }
        }

        out.println("Notes per customer:");
        Customer ncCustomer = store.createCustomer(new Customer(0, "NC Customer", "nc@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Customer ncOther = store.createCustomer(new Customer(0, "NC Other", "nco@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Product ncProduct = store.createProduct(new Product(0, "NC-1", "NC Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-NC1", ncCustomer.id(), LocalDate.of(2038, 1, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(ncProduct.id(), 1, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-NC2", ncCustomer.id(), LocalDate.of(2038, 1, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(ncProduct.id(), 1, new BigDecimal("10.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-NC3", ncOther.id(), LocalDate.of(2038, 1, 1), OrderStatus.DRAFT,
                List.of(new OrderLine(ncProduct.id(), 1, new BigDecimal("10.00")))));
        OrderNoteService ncNotes = new OrderNoteService(store);
        ncNotes.addNote("SO-NC1", "anna", LocalDate.of(2038, 1, 2), "First call");
        ncNotes.addNote("SO-NC2", "ben", LocalDate.of(2038, 1, 5), "Sent quote");
        ncNotes.addNote("SO-NC2", "anna", LocalDate.of(2038, 1, 7), "Quote accepted");
        ncNotes.addNote("SO-NC3", "anna", LocalDate.of(2038, 1, 6), "Other customer");
        for (OrderNote ncNote : ncNotes.listNotesByCustomer(ncCustomer.id())) {
            out.println("  all " + ncNote.date() + " " + ncNote.author() + ": " + ncNote.text());
        }
        for (OrderNote ncNote : ncNotes.listNotesByAuthorAndCustomer("anna", ncCustomer.id())) {
            out.println("  anna " + ncNote.date() + ": " + ncNote.text());
        }

        out.println("Region report:");
        Customer rrCustomer = store.createCustomer(new Customer(0, "RR Customer", "rr@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product rrProduct = store.createProduct(new Product(0, "RR-1", "RR Widget", "Hardware", new BigDecimal("10.00"), "piece", true));
        SalesOrder rrOrder = store.createSalesOrder(new SalesOrder(0, "SO-RR1", rrCustomer.id(), LocalDate.of(2039, 3, 5), OrderStatus.CONFIRMED,
                List.of(new OrderLine(rrProduct.id(), 4, new BigDecimal("10.00")))));
        store.createInvoice(new Invoice(0, "INV-RR1", rrOrder.id(), rrCustomer.id(), LocalDate.of(2039, 3, 6), LocalDate.of(2039, 3, 20),
                new BigDecimal("40.00"), InvoiceStatus.OPEN));
        store.createSupportTicket(new SupportTicket(0, "T-RR1", rrCustomer.id(), null, "Late", TicketPriority.NORMAL,
                TicketStatus.OPEN, LocalDate.of(2039, 3, 10), null));
        RegionReport rrReport = new RegionReportService(store).report(Region.SOUTH, LocalDate.of(2039, 3, 1), LocalDate.of(2039, 3, 31));
        out.println("  " + rrReport.region() + " customers " + rrReport.customerCount() + ", orders " + rrReport.orderCount()
                + ", revenue " + rrReport.revenue() + ", open invoices " + rrReport.openInvoiceCount()
                + ", overdue " + rrReport.overdueAmount() + ", shipments " + rrReport.shipmentCount()
                + ", returns " + rrReport.returnCount() + ", open tickets " + rrReport.openTicketCount());

        out.println("Customers per quarter:");
        Customer cqFirst = store.createCustomer(new Customer(0, "CQ First", "cq1@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(cqFirst.id(), LocalDate.of(2041, 2, 10));
        Customer cqSecond = store.createCustomer(new Customer(0, "CQ Second", "cq2@example.com", null, "UK", Region.WEST, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(cqSecond.id(), LocalDate.of(2041, 5, 20));
        Customer cqThird = store.createCustomer(new Customer(0, "CQ Third", "cq3@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(cqThird.id(), LocalDate.of(2041, 6, 30));
        for (QuarterCustomers cqQuarter : new QuarterCustomersService(store).report(2041)) {
            out.println("  Q" + cqQuarter.quarter() + " total " + cqQuarter.total() + ", " + cqQuarter.perRegion());
        }

        out.println("Orders per quarter:");
        Customer oqEast = store.createCustomer(new Customer(0, "OQ East", "oq1@example.com", null, "UK", Region.EAST, CustomerStatus.ACTIVE));
        Customer oqWest = store.createCustomer(new Customer(0, "OQ West", "oq2@example.com", null, "UK", Region.WEST, CustomerStatus.ACTIVE));
        Product oqProduct = store.createProduct(new Product(0, "OQ-1", "OQ Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-OQ1", oqEast.id(), LocalDate.of(2043, 1, 15), OrderStatus.CONFIRMED,
                List.of(new OrderLine(oqProduct.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OQ2", oqWest.id(), LocalDate.of(2043, 3, 31), OrderStatus.CONFIRMED,
                List.of(new OrderLine(oqProduct.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OQ3", oqEast.id(), LocalDate.of(2043, 4, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(oqProduct.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OQ4", oqWest.id(), LocalDate.of(2043, 11, 5), OrderStatus.CANCELLED,
                List.of(new OrderLine(oqProduct.id(), 1, new BigDecimal("2.00")))));
        for (QuarterOrders oqQuarter : new QuarterOrdersService(store).report(2043)) {
            out.println("  Q" + oqQuarter.quarter() + " total " + oqQuarter.total() + ", " + oqQuarter.perRegion());
        }

        out.println("Customers per weekday:");
        Customer cwMon = store.createCustomer(new Customer(0, "CW Monday", "cw1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(cwMon.id(), LocalDate.of(2042, 3, 3));
        Customer cwMon2 = store.createCustomer(new Customer(0, "CW Monday Two", "cw2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(cwMon2.id(), LocalDate.of(2042, 3, 10));
        Customer cwSun = store.createCustomer(new Customer(0, "CW Sunday", "cw3@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(cwSun.id(), LocalDate.of(2042, 3, 9));
        for (WeekdayCustomers cwDay : new WeekdayCustomersService(store).report(LocalDate.of(2042, 3, 1), LocalDate.of(2042, 3, 31))) {
            out.println("  " + cwDay.weekday() + " total " + cwDay.total() + ", " + cwDay.perRegion());
        }

        out.println("Orders per weekday:");
        Customer owNorth = store.createCustomer(new Customer(0, "OW North", "ow1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer owSouth = store.createCustomer(new Customer(0, "OW South", "ow2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product owProduct = store.createProduct(new Product(0, "OW-1", "OW Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-OW1", owNorth.id(), LocalDate.of(2044, 3, 1), OrderStatus.CONFIRMED,
                List.of(new OrderLine(owProduct.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OW2", owSouth.id(), LocalDate.of(2044, 3, 8), OrderStatus.CONFIRMED,
                List.of(new OrderLine(owProduct.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OW3", owNorth.id(), LocalDate.of(2044, 3, 6), OrderStatus.CONFIRMED,
                List.of(new OrderLine(owProduct.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-OW4", owSouth.id(), LocalDate.of(2044, 3, 15), OrderStatus.CANCELLED,
                List.of(new OrderLine(owProduct.id(), 1, new BigDecimal("2.00")))));
        for (WeekdayOrders owDay : new WeekdayOrdersService(store).report(LocalDate.of(2044, 3, 1), LocalDate.of(2044, 3, 31))) {
            out.println("  " + owDay.weekday() + " total " + owDay.total() + ", " + owDay.perRegion());
        }

        out.println("Latest orders:");
        Customer lnNorth = store.createCustomer(new Customer(0, "LN North", "ln1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer lnSouth = store.createCustomer(new Customer(0, "LN South", "ln2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Product lnProduct = store.createProduct(new Product(0, "LN-1", "LN Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        store.createSalesOrder(new SalesOrder(0, "SO-LN1", lnNorth.id(), LocalDate.of(2045, 1, 10), OrderStatus.CONFIRMED,
                List.of(new OrderLine(lnProduct.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-LN2", lnSouth.id(), LocalDate.of(2045, 2, 10), OrderStatus.CONFIRMED,
                List.of(new OrderLine(lnProduct.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-LN3", lnNorth.id(), LocalDate.of(2045, 3, 10), OrderStatus.CONFIRMED,
                List.of(new OrderLine(lnProduct.id(), 1, new BigDecimal("2.00")))));
        store.createSalesOrder(new SalesOrder(0, "SO-LN4", lnSouth.id(), LocalDate.of(2045, 4, 10), OrderStatus.CANCELLED,
                List.of(new OrderLine(lnProduct.id(), 1, new BigDecimal("2.00")))));
        LatestOrdersService lnService = new LatestOrdersService(store);
        for (SalesOrder lnOrder : lnService.overall(2)) {
            out.println("  overall " + lnOrder.number());
        }
        for (SalesOrder lnOrder : lnService.forRegion(Region.SOUTH, 2)) {
            out.println("  SOUTH " + lnOrder.number());
        }
        for (SalesOrder lnOrder : lnService.forCustomer(lnNorth.id(), 2)) {
            out.println("  LN North: " + lnOrder.number());
        }

        out.println("Latest customers:");
        Customer lcOne = store.createCustomer(new Customer(0, "LC One", "lc1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(lcOne.id(), LocalDate.of(2043, 1, 10));
        Customer lcTwo = store.createCustomer(new Customer(0, "LC Two", "lc2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(lcTwo.id(), LocalDate.of(2043, 2, 10));
        Customer lcThree = store.createCustomer(new Customer(0, "LC Three", "lc3@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        store.updateCustomerCreatedDate(lcThree.id(), LocalDate.of(2043, 3, 10));
        LatestCustomersService lcService = new LatestCustomersService(store);
        for (Customer lcCustomer : lcService.overall(2)) {
            out.println("  overall " + lcCustomer.name());
        }
        for (Customer lcCustomer : lcService.forRegion(Region.NORTH, 2)) {
            out.println("  NORTH " + lcCustomer.name());
        }
        for (Customer lcCustomer : lcService.beforeCustomer(lcThree.id(), 2)) {
            out.println("  before LC Three: " + lcCustomer.name());
        }

        out.println("Customers per sales rep:");
        SalesRep scRepA = store.createSalesRep(new SalesRep(0, "SC Alice", "sc-a@example.com", Region.NORTH));
        SalesRep scRepB = store.createSalesRep(new SalesRep(0, "SC Bob", "sc-b@example.com", Region.SOUTH));
        store.createSalesRep(new SalesRep(0, "SC Cara", "sc-c@example.com", Region.EAST));
        SalesRep[] scReps = new SalesRep[] {scRepB, scRepB, scRepA};
        for (int i = 0; i < scReps.length; i++) {
            Customer scCustomer = store.createCustomer(new Customer(0, "SC Customer " + i, "sc" + i + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
            store.updateCustomerCreatedDate(scCustomer.id(), LocalDate.of(2044, 5, 10 + i));
            store.assignCustomerToSalesRep(scCustomer.id(), scReps[i].id());
        }
        for (SalesRepCustomerCount scCount : new SalesRepCustomerCountService(store).report(LocalDate.of(2044, 5, 1), LocalDate.of(2044, 5, 31))) {
            if (scCount.name().startsWith("SC ")) {
                out.println("  " + scCount.name() + ": " + scCount.customerCount());
            }
        }

        out.println("Orders per sales rep:");
        SalesRep soRepA = store.createSalesRep(new SalesRep(0, "SO Alice", "so-a@example.com", Region.NORTH));
        SalesRep soRepB = store.createSalesRep(new SalesRep(0, "SO Bob", "so-b@example.com", Region.SOUTH));
        Product soProduct = store.createProduct(new Product(0, "SO-1", "SO Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        SalesRep[] soReps = new SalesRep[] {soRepA, soRepB};
        int[] soOrders = new int[] {1, 3};
        for (int i = 0; i < soReps.length; i++) {
            Customer soCustomer = store.createCustomer(new Customer(0, "SO Customer " + i, "so" + i + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
            store.assignCustomerToSalesRep(soCustomer.id(), soReps[i].id());
            for (int j = 0; j < soOrders[i]; j++) {
                store.createSalesOrder(new SalesOrder(0, "SO-SR" + i + "-" + j, soCustomer.id(), LocalDate.of(2046, 7, 10 + j), OrderStatus.CONFIRMED,
                        List.of(new OrderLine(soProduct.id(), 1, new BigDecimal("2.00")))));
            }
        }
        for (SalesRepOrderCount soCount : new SalesRepOrderCountService(store).report(LocalDate.of(2046, 7, 1), LocalDate.of(2046, 7, 31))) {
            if (soCount.name().startsWith("SO ")) {
                out.println("  " + soCount.name() + ": " + soCount.orderCount());
            }
        }

        out.println("Customers per country:");
        String[] ccCountries = new String[] {"Freedonia", "Sylvania", "Freedonia"};
        for (int i = 0; i < ccCountries.length; i++) {
            Customer ccCustomer = store.createCustomer(new Customer(0, "CC Customer " + i, "cc" + i + "@example.com", null, ccCountries[i], Region.NORTH, CustomerStatus.ACTIVE));
            store.updateCustomerCreatedDate(ccCustomer.id(), LocalDate.of(2045, 6, 10 + i));
        }
        CountryCustomersService ccService = new CountryCustomersService(store);
        for (Customer ccCustomer : ccService.customers("Freedonia", LocalDate.of(2045, 6, 1), LocalDate.of(2045, 6, 30))) {
            out.println("  Freedonia: " + ccCustomer.name());
        }
        for (CountryCustomerCount ccCount : ccService.countPerCountry(LocalDate.of(2045, 6, 1), LocalDate.of(2045, 6, 30))) {
            out.println("  " + ccCount.country() + ": " + ccCount.customerCount());
        }

        out.println("Orders per country:");
        Product coProduct = store.createProduct(new Product(0, "CO-1", "CO Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        String[] coCountries = new String[] {"Ruritania", "Genovia", "Ruritania"};
        for (int i = 0; i < coCountries.length; i++) {
            Customer coCustomer = store.createCustomer(new Customer(0, "CO Customer " + i, "co" + i + "@example.com", null, coCountries[i], Region.NORTH, CustomerStatus.ACTIVE));
            store.createSalesOrder(new SalesOrder(0, "CTRY-" + i, coCustomer.id(), LocalDate.of(2047, 8, 10 + i), OrderStatus.CONFIRMED,
                    List.of(new OrderLine(coProduct.id(), 1, new BigDecimal("2.00")))));
        }
        CountryOrdersService coService = new CountryOrdersService(store);
        for (SalesOrder coOrder : coService.orders("Ruritania", LocalDate.of(2047, 8, 1), LocalDate.of(2047, 8, 31))) {
            out.println("  Ruritania: " + coOrder.number());
        }
        for (CountryOrderCount coCount : coService.countPerCountry(LocalDate.of(2047, 8, 1), LocalDate.of(2047, 8, 31))) {
            out.println("  " + coCount.country() + ": " + coCount.orderCount());
        }

        out.println("Invoices per quarter:");
        Product iqProduct = store.createProduct(new Product(0, "IQ-1", "IQ Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Customer iqNorth = store.createCustomer(new Customer(0, "IQ North", "iq1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer iqSouth = store.createCustomer(new Customer(0, "IQ South", "iq2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer[] iqCustomers = new Customer[] {iqNorth, iqNorth, iqSouth, iqSouth};
        LocalDate[] iqDates = new LocalDate[] {LocalDate.of(2048, 2, 1), LocalDate.of(2048, 3, 31), LocalDate.of(2048, 7, 1), LocalDate.of(2048, 8, 1)};
        InvoiceStatus[] iqStatuses = new InvoiceStatus[] {InvoiceStatus.OPEN, InvoiceStatus.PAID, InvoiceStatus.OPEN, InvoiceStatus.CANCELLED};
        for (int i = 0; i < iqDates.length; i++) {
            SalesOrder iqOrder = store.createSalesOrder(new SalesOrder(0, "SO-IQ" + (i + 1), iqCustomers[i].id(), iqDates[i], OrderStatus.SHIPPED,
                    List.of(new OrderLine(iqProduct.id(), 1, new BigDecimal("2.00")))));
            store.createInvoice(new Invoice(0, "INV-IQ" + (i + 1), iqOrder.id(), iqCustomers[i].id(), iqDates[i], iqDates[i].plusDays(30),
                    new BigDecimal("2.00"), iqStatuses[i]));
        }
        for (QuarterInvoices iqQuarter : new QuarterInvoicesService(store).report(2048)) {
            out.println("  Q" + iqQuarter.quarter() + " total " + iqQuarter.total() + ", " + iqQuarter.perRegion());
        }

        invoicesPerWeekday(store, out);
        latestInvoices(store, out);
        invoicesPerSalesRep(store, out);
        invoicesPerCountry(store, out);
        shipmentsPerQuarter(store, out);
    }

    private static void returnsPerQuarter(DataStore store, PrintStream out) {
        out.println("Returns per quarter:");
        Product rqProduct = store.createProduct(new Product(0, "RQ-1", "RQ Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Customer rqNorth = store.createCustomer(new Customer(0, "RQ North", "rq1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer rqSouth = store.createCustomer(new Customer(0, "RQ South", "rq2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Warehouse rqWarehouse = store.createWarehouse(new Warehouse(0, "WH-RQ", "RQ Warehouse", "Oslo", Region.NORTH));
        Customer[] rqCustomers =new Customer[] {rqNorth, rqNorth, rqSouth, rqSouth};
        LocalDate[] rqDates = new LocalDate[] {LocalDate.of(2052, 2, 1), LocalDate.of(2052, 3, 31), LocalDate.of(2052, 7, 1), LocalDate.of(2053, 1, 1)};
        for (int i = 0; i < rqDates.length; i++) {
            SalesOrder rqOrder = store.createSalesOrder(new SalesOrder(0, "SO-RQ" + (i + 1), rqCustomers[i].id(), rqDates[i], OrderStatus.DELIVERED,
                    List.of(new OrderLine(rqProduct.id(), 1, new BigDecimal("2.00")))));
            store.createSalesReturn(new SalesReturn(0, "RET-RQ" + (i + 1), rqOrder.id(), rqDates[i], "Damaged",
                    List.of(new ReturnLine(rqProduct.id(), 1))), rqWarehouse.id());
        }
        for (QuarterReturns rqQuarter : new QuarterReturnsService(store).report(2052)) {
            out.println("  Q" + rqQuarter.quarter() + " total " + rqQuarter.total() + ", " + rqQuarter.perRegion());
        }
    }

    private static void shipmentsPerWeekday(DataStore store, PrintStream out) {
        out.println("Shipments per weekday:");
        Product swProduct = store.createProduct(new Product(0, "SW-1", "SW Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Customer swNorth = store.createCustomer(new Customer(0, "SW North", "sw1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer swSouth = store.createCustomer(new Customer(0, "SW South", "sw2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Warehouse swWarehouse = store.createWarehouse(new Warehouse(0, "WH-SW", "SW Warehouse", "Oslo", Region.NORTH));
        Carrier swCarrier = store.createCarrier(new Carrier(0, "SW Carrier", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        Customer[] swCustomers = new Customer[] {swNorth, swNorth, swSouth, swSouth};
        LocalDate[] swDates = new LocalDate[] {LocalDate.of(2052, 3, 4), LocalDate.of(2052, 3, 11), LocalDate.of(2052, 3, 6), LocalDate.of(2052, 4, 1)};
        for (int i = 0; i < swDates.length; i++) {
            SalesOrder swOrder = store.createSalesOrder(new SalesOrder(0, "SO-SW" + (i + 1), swCustomers[i].id(), swDates[i], OrderStatus.SHIPPED,
                    List.of(new OrderLine(swProduct.id(), 1, new BigDecimal("2.00")))));
            store.createShipment(new Shipment(0, swOrder.id(), swWarehouse.id(), swCarrier.id(), "SW-" + (i + 1), swDates[i]));
        }
        for (WeekdayShipments swDay : new WeekdayShipmentsService(store).report(LocalDate.of(2052, 3, 1), LocalDate.of(2052, 3, 31))) {
            out.println("  " + swDay.weekday() + " total " + swDay.total() + ", " + swDay.perRegion());
        }
        latestShipments(store, out);
        shipmentsPerSalesRep(store, out);
        shipmentsPerCountry(store, out);
        returnsPerQuarter(store, out);
        returnsPerWeekday(store, out);
        latestReturns(store, out);
        returnsPerSalesRep(store, out);
        returnsPerCountry(store, out);
        purchaseOrdersPerQuarter(store, out);
        purchaseOrdersPerWeekday(store, out);
        latestPurchaseOrders(store, out);
        purchaseOrdersPerSalesRep(store, out);
        purchaseOrdersPerCountry(store, out);
    }

    private static void purchaseOrdersPerCountry(DataStore store, PrintStream out) {
        out.println("Purchase orders per country:");
        Product pcProduct = store.createProduct(new Product(0, "PC-1", "PC Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Supplier pcSupplier = store.createSupplier(new Supplier(0, "PC Supplier", "pc@example.com", "Sweden", 3));
        Warehouse pcNorth = store.createWarehouse(new Warehouse(0, "WH-PCN", "PC North", "Oslo", Region.NORTH));
        Warehouse pcSouth = store.createWarehouse(new Warehouse(0, "WH-PCS", "PC South", "Rome", Region.SOUTH));
        store.createCustomer(new Customer(0, "PC Customer 1", "pc1@example.com", null, "Ruritania", Region.NORTH, CustomerStatus.ACTIVE));
        store.createCustomer(new Customer(0, "PC Customer 2", "pc2@example.com", null, "Ruritania", Region.NORTH, CustomerStatus.ACTIVE));
        store.createCustomer(new Customer(0, "PC Customer 3", "pc3@example.com", null, "Genovia", Region.SOUTH, CustomerStatus.ACTIVE));
        Warehouse[] pcWarehouses = new Warehouse[] {pcNorth, pcSouth, pcNorth};
        for (int i = 0; i < pcWarehouses.length; i++) {
            LocalDate pcDate = LocalDate.of(2064, 5, 10 + i);
            store.createPurchaseOrder(new PurchaseOrder(0, "PO-PC" + (i + 1), pcSupplier.id(), pcDate, pcDate.plusDays(14),
                    PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(pcProduct.id(), 5, new BigDecimal("1.00"))), pcWarehouses[i].id()));
        }
        CountryPurchaseOrdersService pcService = new CountryPurchaseOrdersService(store);
        for (PurchaseOrder pcOrder : pcService.purchaseOrders("Ruritania", LocalDate.of(2064, 5, 1), LocalDate.of(2064, 5, 31))) {
            out.println("  Ruritania: " + pcOrder.number());
        }
        for (CountryPurchaseOrderCount pcCount : pcService.countPerCountry(LocalDate.of(2064, 5, 1), LocalDate.of(2064, 5, 31))) {
            out.println("  " + pcCount.country() + ": " + pcCount.purchaseOrderCount());
        }
    }

    private static void purchaseOrdersPerSalesRep(DataStore store, PrintStream out) {
        out.println("Purchase orders per sales rep:");
        SalesRep ppRepA = store.createSalesRep(new SalesRep(0, "PP Alice", "pp-a@example.com", Region.NORTH));
        SalesRep ppRepB = store.createSalesRep(new SalesRep(0, "PP Bob", "pp-b@example.com", Region.SOUTH));
        Product ppProduct = store.createProduct(new Product(0, "PP-SR1", "PP Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Supplier ppSupplier = store.createSupplier(new Supplier(0, "PP Supplier", "pp@example.com", "Sweden", 3));
        Warehouse ppNorth = store.createWarehouse(new Warehouse(0, "WH-PPN", "PP North", "Oslo", Region.NORTH));
        Warehouse ppSouth = store.createWarehouse(new Warehouse(0, "WH-PPS", "PP South", "Rome", Region.SOUTH));
        Customer ppCustomerA = store.createCustomer(new Customer(0, "PP Customer A", "ppa@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer ppCustomerB = store.createCustomer(new Customer(0, "PP Customer B", "ppb@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        store.assignCustomerToSalesRep(ppCustomerA.id(), ppRepA.id());
        store.assignCustomerToSalesRep(ppCustomerB.id(), ppRepB.id());
        Warehouse[] ppWarehouses = new Warehouse[] {ppNorth, ppSouth, ppSouth, ppSouth};
        for (int i = 0; i < ppWarehouses.length; i++) {
            LocalDate ppDate = LocalDate.of(2063, 4, 10 + i);
            store.createPurchaseOrder(new PurchaseOrder(0, "PO-PP" + (i + 1), ppSupplier.id(), ppDate, ppDate.plusDays(14),
                    PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(ppProduct.id(), 5, new BigDecimal("1.00"))), ppWarehouses[i].id()));
        }
        for (SalesRepPurchaseOrderCount ppCount : new SalesRepPurchaseOrderCountService(store).report(LocalDate.of(2063, 4, 1), LocalDate.of(2063, 4, 30))) {
            if (ppCount.name().startsWith("PP ")) {
                out.println("  " + ppCount.name() + ": " + ppCount.purchaseOrderCount());
            }
        }
    }

    private static void latestPurchaseOrders(DataStore store, PrintStream out) {
        out.println("Latest purchase orders:");
        Product lpProduct = store.createProduct(new Product(0, "LP-1", "LP Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Supplier lpFirst = store.createSupplier(new Supplier(0, "LP First", "lp1@example.com", "Sweden", 3));
        Supplier lpSecond = store.createSupplier(new Supplier(0, "LP Second", "lp2@example.com", "Norway", 3));
        Warehouse lpNorth = store.createWarehouse(new Warehouse(0, "WH-LPN", "LP North", "Oslo", Region.NORTH));
        Warehouse lpSouth = store.createWarehouse(new Warehouse(0, "WH-LPS", "LP South", "Rome", Region.SOUTH));
        Supplier[] lpSuppliers = new Supplier[] {lpFirst, lpSecond, lpFirst, lpSecond};
        Warehouse[] lpWarehouses = new Warehouse[] {lpNorth, lpSouth, lpNorth, lpNorth};
        LocalDate[] lpDates = new LocalDate[] {LocalDate.of(2062, 5, 1), LocalDate.of(2062, 5, 4), LocalDate.of(2062, 5, 3), LocalDate.of(2062, 5, 4)};
        for (int i = 0; i < lpDates.length; i++) {
            store.createPurchaseOrder(new PurchaseOrder(0, "PO-LP" + (i + 1), lpSuppliers[i].id(), lpDates[i], lpDates[i].plusDays(14),
                    PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(lpProduct.id(), 5, new BigDecimal("1.00"))), lpWarehouses[i].id()));
        }
        LatestPurchaseOrdersService lpService = new LatestPurchaseOrdersService(store);
        for (PurchaseOrder lpOrder : lpService.overall(3)) {
            out.println("  overall " + lpOrder.number() + " " + lpOrder.orderDate());
        }
        for (PurchaseOrder lpOrder : lpService.forRegion(Region.NORTH, 2)) {
            out.println("  north " + lpOrder.number() + " " + lpOrder.orderDate());
        }
        for (PurchaseOrder lpOrder : lpService.forSupplier(lpSecond.id(), 1)) {
            out.println("  supplier " + lpOrder.number() + " " + lpOrder.orderDate());
        }
    }

    private static void returnsPerCountry(DataStore store, PrintStream out) {
        out.println("Returns per country:");
        Product rcProduct = store.createProduct(new Product(0, "RC-1", "RC Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Warehouse rcWarehouse = store.createWarehouse(new Warehouse(0, "WH-RC", "RC Warehouse", "Oslo", Region.NORTH));
        String[] rcCountries = new String[] {"Ruritania", "Genovia", "Ruritania"};
        for (int i = 0; i < rcCountries.length; i++) {
            Customer rcCustomer = store.createCustomer(new Customer(0, "RC Customer " + i, "rc" + i + "@example.com", null, rcCountries[i], Region.NORTH, CustomerStatus.ACTIVE));
            LocalDate rcDate = LocalDate.of(2056, 3, 10 + i);
            SalesOrder rcOrder = store.createSalesOrder(new SalesOrder(0, "SO-RC" + i, rcCustomer.id(), rcDate, OrderStatus.DELIVERED,
                    List.of(new OrderLine(rcProduct.id(), 1, new BigDecimal("2.00")))));
            store.createSalesReturn(new SalesReturn(0, "RET-RC" + i, rcOrder.id(), rcDate, "Damaged",
                    List.of(new ReturnLine(rcProduct.id(), 1))), rcWarehouse.id());
        }
        CountryReturnsService rcService = new CountryReturnsService(store);
        for (SalesReturn rcReturn : rcService.returns("Ruritania", LocalDate.of(2056, 3, 1), LocalDate.of(2056, 3, 31))) {
            out.println("  Ruritania: " + rcReturn.number());
        }
        for (CountryReturnCount rcCount : rcService.countPerCountry(LocalDate.of(2056, 3, 1), LocalDate.of(2056, 3, 31))) {
            out.println("  " + rcCount.country() + ": " + rcCount.returnCount());
        }
    }

    private static void returnsPerSalesRep(DataStore store, PrintStream out) {
        out.println("Returns per sales rep:");
        SalesRep rrRepA = store.createSalesRep(new SalesRep(0, "RR Alice", "rr-a@example.com", Region.NORTH));
        SalesRep rrRepB = store.createSalesRep(new SalesRep(0, "RR Bob", "rr-b@example.com", Region.SOUTH));
        Product rrProduct = store.createProduct(new Product(0, "RR-2", "RR Widget 2", "Hardware", new BigDecimal("2.00"), "piece", true));
        Warehouse rrWarehouse = store.createWarehouse(new Warehouse(0, "WH-RR", "RR Warehouse", "Oslo", Region.NORTH));
        SalesRep[] rrReps = new SalesRep[] {rrRepA, rrRepB};
        int[] rrReturns = new int[] {1, 3};
        for (int i = 0; i < rrReps.length; i++) {
            Customer rrCustomer = store.createCustomer(new Customer(0, "RR Customer " + i, "rr" + i + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
            store.assignCustomerToSalesRep(rrCustomer.id(), rrReps[i].id());
            for (int j = 0; j < rrReturns[i]; j++) {
                LocalDate rrDate = LocalDate.of(2054, 7, 10 + j);
                SalesOrder rrOrder = store.createSalesOrder(new SalesOrder(0, "SO-RR" + i + "-" + j, rrCustomer.id(), rrDate, OrderStatus.DELIVERED,
                        List.of(new OrderLine(rrProduct.id(), 1, new BigDecimal("2.00")))));
                store.createSalesReturn(new SalesReturn(0, "RET-RR" + i + "-" + j, rrOrder.id(), rrDate, "Damaged",
                        List.of(new ReturnLine(rrProduct.id(), 1))), rrWarehouse.id());
            }
        }
        for (SalesRepReturnCount rrCount : new SalesRepReturnCountService(store).report(LocalDate.of(2054, 7, 1), LocalDate.of(2054, 7, 31))) {
            if (rrCount.name().startsWith("RR ")) {
                out.println("  " + rrCount.name() + ": " + rrCount.returnCount());
            }
        }
    }

    private static void latestReturns(DataStore store, PrintStream out) {
        out.println("Latest returns:");
        Product lrProduct = store.createProduct(new Product(0, "LR-1", "LR Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Customer lrNorth = store.createCustomer(new Customer(0, "LR North", "lr1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer lrSouth = store.createCustomer(new Customer(0, "LR South", "lr2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Warehouse lrWarehouse = store.createWarehouse(new Warehouse(0, "WH-LR", "LR Warehouse", "Oslo", Region.NORTH));
        Customer[] lrCustomers = new Customer[] {lrNorth, lrSouth, lrNorth, lrSouth};
        LocalDate[] lrDates = new LocalDate[] {LocalDate.of(2054, 5, 1), LocalDate.of(2054, 5, 4), LocalDate.of(2054, 5, 3), LocalDate.of(2054, 5, 4)};
        for (int i = 0; i < lrDates.length; i++) {
            SalesOrder lrOrder = store.createSalesOrder(new SalesOrder(0, "SO-LR" + (i + 1), lrCustomers[i].id(), lrDates[i], OrderStatus.DELIVERED,
                    List.of(new OrderLine(lrProduct.id(), 1, new BigDecimal("2.00")))));
            store.createSalesReturn(new SalesReturn(0, "RET-LR" + (i + 1), lrOrder.id(), lrDates[i], "Damaged",
                    List.of(new ReturnLine(lrProduct.id(), 1))), lrWarehouse.id());
        }
        LatestReturnsService lrService = new LatestReturnsService(store);
        for (SalesReturn lrReturn : lrService.overall(3)) {
            out.println("  overall " + lrReturn.number() + " " + lrReturn.returnDate());
        }
        for (SalesReturn lrReturn : lrService.forRegion(Region.NORTH, 2)) {
            out.println("  north " + lrReturn.number() + " " + lrReturn.returnDate());
        }
        for (SalesReturn lrReturn : lrService.forCustomer(lrSouth.id(), 1)) {
            out.println("  customer " + lrReturn.number() + " " + lrReturn.returnDate());
        }
    }

    private static void returnsPerWeekday(DataStore store, PrintStream out) {
        out.println("Returns per weekday:");
        Product rwProduct = store.createProduct(new Product(0, "RW-1", "RW Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Customer rwNorth = store.createCustomer(new Customer(0, "RW North", "rw1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer rwSouth = store.createCustomer(new Customer(0, "RW South", "rw2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Warehouse rwWarehouse = store.createWarehouse(new Warehouse(0, "WH-RW", "RW Warehouse", "Oslo", Region.NORTH));
        Customer[] rwCustomers = new Customer[] {rwNorth, rwNorth, rwSouth, rwSouth};
        LocalDate[] rwDates = new LocalDate[] {LocalDate.of(2053, 3, 3), LocalDate.of(2053, 3, 10), LocalDate.of(2053, 3, 5), LocalDate.of(2053, 4, 1)};
        for (int i = 0; i < rwDates.length; i++) {
            SalesOrder rwOrder = store.createSalesOrder(new SalesOrder(0, "SO-RW" + (i + 1), rwCustomers[i].id(), rwDates[i], OrderStatus.DELIVERED,
                    List.of(new OrderLine(rwProduct.id(), 1, new BigDecimal("2.00")))));
            store.createSalesReturn(new SalesReturn(0, "RET-RW" + (i + 1), rwOrder.id(), rwDates[i], "Damaged",
                    List.of(new ReturnLine(rwProduct.id(), 1))), rwWarehouse.id());
        }
        for (WeekdayReturns rwDay : new WeekdayReturnsService(store).report(LocalDate.of(2053, 3, 1), LocalDate.of(2053, 3, 31))) {
            out.println("  " + rwDay.weekday() + " total " + rwDay.total() + ", " + rwDay.perRegion());
        }
    }

    private static void shipmentsPerCountry(DataStore store, PrintStream out) {
        out.println("Shipments per country:");
        Product scProduct = store.createProduct(new Product(0, "SC-1", "SC Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Warehouse scWarehouse = store.createWarehouse(new Warehouse(0, "WH-SC", "SC Warehouse", "Oslo", Region.NORTH));
        Carrier scCarrier = store.createCarrier(new Carrier(0, "SC Carrier", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        String[] scCountries = new String[] {"Ruritania", "Genovia", "Ruritania"};
        for (int i = 0; i < scCountries.length; i++) {
            Customer scCustomer = store.createCustomer(new Customer(0, "SC Customer " + i, "sc" + i + "@example.com", null, scCountries[i], Region.NORTH, CustomerStatus.ACTIVE));
            LocalDate scDate = LocalDate.of(2055, 3, 10 + i);
            SalesOrder scOrder = store.createSalesOrder(new SalesOrder(0, "SO-SC" + i, scCustomer.id(), scDate, OrderStatus.SHIPPED,
                    List.of(new OrderLine(scProduct.id(), 1, new BigDecimal("2.00")))));
            store.createShipment(new Shipment(0, scOrder.id(), scWarehouse.id(), scCarrier.id(), "SC-" + i, scDate));
        }
        CountryShipmentsService scService = new CountryShipmentsService(store);
        for (Shipment scShipment : scService.shipments("Ruritania", LocalDate.of(2055, 3, 1), LocalDate.of(2055, 3, 31))) {
            out.println("  Ruritania: " + scShipment.trackingNumber());
        }
        for (CountryShipmentCount scCount : scService.countPerCountry(LocalDate.of(2055, 3, 1), LocalDate.of(2055, 3, 31))) {
            out.println("  " + scCount.country() + ": " + scCount.shipmentCount());
        }
    }

    private static void shipmentsPerSalesRep(DataStore store, PrintStream out) {
        out.println("Shipments per sales rep:");
        SalesRep srRepA = store.createSalesRep(new SalesRep(0, "SR Alice", "sr-a@example.com", Region.NORTH));
        SalesRep srRepB = store.createSalesRep(new SalesRep(0, "SR Bob", "sr-b@example.com", Region.SOUTH));
        Product srProduct = store.createProduct(new Product(0, "SR-1", "SR Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Warehouse srWarehouse = store.createWarehouse(new Warehouse(0, "WH-SR", "SR Warehouse", "Oslo", Region.NORTH));
        Carrier srCarrier = store.createCarrier(new Carrier(0, "SR Carrier", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        SalesRep[] srReps = new SalesRep[] {srRepA, srRepB};
        int[] srShipments = new int[] {1, 3};
        for (int i = 0; i < srReps.length; i++) {
            Customer srCustomer = store.createCustomer(new Customer(0, "SR Customer " + i, "sr" + i + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
            store.assignCustomerToSalesRep(srCustomer.id(), srReps[i].id());
            for (int j = 0; j < srShipments[i]; j++) {
                LocalDate srDate = LocalDate.of(2054, 6, 10 + j);
                SalesOrder srOrder = store.createSalesOrder(new SalesOrder(0, "SO-SHR" + i + "-" + j, srCustomer.id(), srDate, OrderStatus.SHIPPED,
                        List.of(new OrderLine(srProduct.id(), 1, new BigDecimal("2.00")))));
                store.createShipment(new Shipment(0, srOrder.id(), srWarehouse.id(), srCarrier.id(), "SR-" + i + "-" + j, srDate));
            }
        }
        for (SalesRepShipmentCount srCount : new SalesRepShipmentCountService(store).report(LocalDate.of(2054, 6, 1), LocalDate.of(2054, 6, 30))) {
            if (srCount.name().startsWith("SR ")) {
                out.println("  " + srCount.name() + ": " + srCount.shipmentCount());
            }
        }
    }

    private static void latestShipments(DataStore store, PrintStream out) {
        out.println("Latest shipments:");
        Product lsProduct = store.createProduct(new Product(0, "LS-1", "LS Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Customer lsNorth = store.createCustomer(new Customer(0, "LS North", "ls1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer lsSouth = store.createCustomer(new Customer(0, "LS South", "ls2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Warehouse lsWarehouse = store.createWarehouse(new Warehouse(0, "WH-LS", "LS Warehouse", "Oslo", Region.NORTH));
        Carrier lsCarrier = store.createCarrier(new Carrier(0, "LS Carrier", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        Customer[] lsCustomers = new Customer[] {lsNorth, lsSouth, lsNorth, lsSouth};
        LocalDate[] lsDates = new LocalDate[] {LocalDate.of(2053, 1, 10), LocalDate.of(2053, 2, 10), LocalDate.of(2053, 3, 10), LocalDate.of(2053, 4, 10)};
        for (int i = 0; i < lsDates.length; i++) {
            SalesOrder lsOrder = store.createSalesOrder(new SalesOrder(0, "SO-LS" + (i + 1), lsCustomers[i].id(), lsDates[i], OrderStatus.SHIPPED,
                    List.of(new OrderLine(lsProduct.id(), 1, new BigDecimal("2.00")))));
            store.createShipment(new Shipment(0, lsOrder.id(), lsWarehouse.id(), lsCarrier.id(), "LS-" + (i + 1), lsDates[i]));
        }
        LatestShipmentsService lsService = new LatestShipmentsService(store);
        for (Shipment lsShipment : lsService.overall(2)) {
            out.println("  overall " + lsShipment.trackingNumber());
        }
        for (Shipment lsShipment : lsService.forRegion(Region.SOUTH, 2)) {
            out.println("  SOUTH " + lsShipment.trackingNumber());
        }
        for (Shipment lsShipment : lsService.forCustomer(lsNorth.id(), 2)) {
            out.println("  LS North: " + lsShipment.trackingNumber());
        }
    }

    private static void purchaseOrdersPerQuarter(DataStore store, PrintStream out) {
        out.println("Purchase orders per quarter:");
        Supplier pqSupplier = store.createSupplier(new Supplier(0, "PQ Supplier", "pq@example.com", "Sweden", 3));
        Product pqProduct = store.createProduct(new Product(0, "PQ-1", "PQ Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Warehouse pqNorth = store.createWarehouse(new Warehouse(0, "WH-PQN", "PQ North", "Oslo", Region.NORTH));
        Warehouse pqWest = store.createWarehouse(new Warehouse(0, "WH-PQW", "PQ West", "Dublin", Region.WEST));
        Warehouse[] pqWarehouses = new Warehouse[] {pqNorth, pqNorth, pqWest, pqNorth};
        LocalDate[] pqDates = new LocalDate[] {LocalDate.of(2060, 2, 1), LocalDate.of(2060, 3, 31), LocalDate.of(2060, 7, 1), LocalDate.of(2061, 1, 1)};
        for (int i = 0; i < pqDates.length; i++) {
            store.createPurchaseOrder(new PurchaseOrder(0, "PO-PQ" + (i + 1), pqSupplier.id(), pqDates[i], pqDates[i].plusDays(14),
                    PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(pqProduct.id(), 5, new BigDecimal("1.00"))), pqWarehouses[i].id()));
        }
        for (QuarterPurchaseOrders pqQuarter : new QuarterPurchaseOrdersService(store).report(2060)) {
            out.println("  Q" + pqQuarter.quarter() + " total " + pqQuarter.total() + ", " + pqQuarter.perRegion());
        }
    }

    private static void purchaseOrdersPerWeekday(DataStore store, PrintStream out) {
        out.println("Purchase orders per weekday:");
        Supplier pwSupplier = store.createSupplier(new Supplier(0, "PW Supplier", "pw@example.com", "Sweden", 3));
        Product pwProduct = store.createProduct(new Product(0, "PW-1", "PW Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Warehouse pwNorth = store.createWarehouse(new Warehouse(0, "WH-PWN", "PW North", "Oslo", Region.NORTH));
        Warehouse pwWest = store.createWarehouse(new Warehouse(0, "WH-PWW", "PW West", "Dublin", Region.WEST));
        Warehouse[] pwWarehouses = new Warehouse[] {pwNorth, pwNorth, pwWest, pwNorth};
        LocalDate[] pwDates = new LocalDate[] {LocalDate.of(2061, 3, 3), LocalDate.of(2061, 3, 10), LocalDate.of(2061, 3, 5), LocalDate.of(2061, 4, 1)};
        for (int i = 0; i < pwDates.length; i++) {
            store.createPurchaseOrder(new PurchaseOrder(0, "PO-PW" + (i + 1), pwSupplier.id(), pwDates[i], pwDates[i].plusDays(14),
                    PurchaseOrderStatus.SENT, List.of(new PurchaseOrderLine(pwProduct.id(), 5, new BigDecimal("1.00"))), pwWarehouses[i].id()));
        }
        for (WeekdayPurchaseOrders pwDay : new WeekdayPurchaseOrdersService(store).report(LocalDate.of(2061, 3, 1), LocalDate.of(2061, 3, 31))) {
            out.println("  " + pwDay.weekday() + " total " + pwDay.total() + ", " + pwDay.perRegion());
        }
    }

    private static void shipmentsPerQuarter(DataStore store, PrintStream out) {
        out.println("Shipments per quarter:");
        Product sqProduct = store.createProduct(new Product(0, "SQ-1", "SQ Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Customer sqNorth = store.createCustomer(new Customer(0, "SQ North", "sq1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer sqSouth = store.createCustomer(new Customer(0, "SQ South", "sq2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Warehouse sqWarehouse = store.createWarehouse(new Warehouse(0, "WH-SQ", "SQ Warehouse", "Oslo", Region.NORTH));
        Carrier sqCarrier = store.createCarrier(new Carrier(0, "SQ Carrier", ServiceLevel.STANDARD, new BigDecimal("0.80")));
        Customer[] sqCustomers = new Customer[] {sqNorth, sqNorth, sqSouth, sqSouth};
        LocalDate[] sqDates = new LocalDate[] {LocalDate.of(2050, 2, 1), LocalDate.of(2050, 3, 31), LocalDate.of(2050, 7, 1), LocalDate.of(2051, 1, 1)};
        for (int i = 0; i < sqDates.length; i++) {
            SalesOrder sqOrder = store.createSalesOrder(new SalesOrder(0, "SO-SQ" + (i + 1), sqCustomers[i].id(), sqDates[i], OrderStatus.SHIPPED,
                    List.of(new OrderLine(sqProduct.id(), 1, new BigDecimal("2.00")))));
            store.createShipment(new Shipment(0, sqOrder.id(), sqWarehouse.id(), sqCarrier.id(), "SQ-" + (i + 1), sqDates[i]));
        }
        for (QuarterShipments sqQuarter : new QuarterShipmentsService(store).report(2050)) {
            out.println("  Q" + sqQuarter.quarter() + " total " + sqQuarter.total() + ", " + sqQuarter.perRegion());
        }
        shipmentsPerWeekday(store, out);
    }

    private static void invoicesPerCountry(DataStore store, PrintStream out) {
        out.println("Invoices per country:");
        Product icProduct = store.createProduct(new Product(0, "IC-1", "IC Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        String[] icCountries = new String[] {"Ruritania", "Genovia", "Ruritania"};
        for (int i = 0; i < icCountries.length; i++) {
            Customer icCustomer = store.createCustomer(new Customer(0, "IC Customer " + i, "ic" + i + "@example.com", null, icCountries[i], Region.NORTH, CustomerStatus.ACTIVE));
            LocalDate icDate = LocalDate.of(2052, 9, 10 + i);
            SalesOrder icOrder = store.createSalesOrder(new SalesOrder(0, "SO-IC" + i, icCustomer.id(), icDate, OrderStatus.SHIPPED,
                    List.of(new OrderLine(icProduct.id(), 1, new BigDecimal("2.00")))));
            store.createInvoice(new Invoice(0, "INV-IC" + i, icOrder.id(), icCustomer.id(), icDate, icDate.plusDays(30),
                    new BigDecimal("2.00"), InvoiceStatus.OPEN));
        }
        CountryInvoicesService icService = new CountryInvoicesService(store);
        for (Invoice icInvoice : icService.invoices("Ruritania", LocalDate.of(2052, 9, 1), LocalDate.of(2052, 9, 30))) {
            out.println("  Ruritania: " + icInvoice.number());
        }
        for (CountryInvoiceCount icCount : icService.countPerCountry(LocalDate.of(2052, 9, 1), LocalDate.of(2052, 9, 30))) {
            out.println("  " + icCount.country() + ": " + icCount.invoiceCount());
        }
    }

    private static void invoicesPerSalesRep(DataStore store, PrintStream out) {
        out.println("Invoices per sales rep:");
        SalesRep irRepA = store.createSalesRep(new SalesRep(0, "IR Alice", "ir-a@example.com", Region.NORTH));
        SalesRep irRepB = store.createSalesRep(new SalesRep(0, "IR Bob", "ir-b@example.com", Region.SOUTH));
        Product irProduct = store.createProduct(new Product(0, "IR-1", "IR Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        SalesRep[] irReps = new SalesRep[] {irRepA, irRepB};
        int[] irInvoices = new int[] {1, 3};
        for (int i = 0; i < irReps.length; i++) {
            Customer irCustomer = store.createCustomer(new Customer(0, "IR Customer " + i, "ir" + i + "@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
            store.assignCustomerToSalesRep(irCustomer.id(), irReps[i].id());
            for (int j = 0; j < irInvoices[i]; j++) {
                LocalDate irDate = LocalDate.of(2051, 5, 10 + j);
                SalesOrder irOrder = store.createSalesOrder(new SalesOrder(0, "SO-IR" + i + "-" + j, irCustomer.id(), irDate, OrderStatus.SHIPPED,
                        List.of(new OrderLine(irProduct.id(), 1, new BigDecimal("2.00")))));
                store.createInvoice(new Invoice(0, "INV-IR" + i + "-" + j, irOrder.id(), irCustomer.id(), irDate, irDate.plusDays(30),
                        new BigDecimal("2.00"), InvoiceStatus.OPEN));
            }
        }
        for (SalesRepInvoiceCount irCount : new SalesRepInvoiceCountService(store).report(LocalDate.of(2051, 5, 1), LocalDate.of(2051, 5, 31))) {
            if (irCount.name().startsWith("IR ")) {
                out.println("  " + irCount.name() + ": " + irCount.invoiceCount());
            }
        }
    }

    private static void latestInvoices(DataStore store, PrintStream out) {
        out.println("Latest invoices:");
        Product liProduct = store.createProduct(new Product(0, "LI-1", "LI Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Customer liNorth = store.createCustomer(new Customer(0, "LI North", "li1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer liSouth = store.createCustomer(new Customer(0, "LI South", "li2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer[] liCustomers = new Customer[] {liNorth, liSouth, liNorth, liSouth};
        LocalDate[] liDates = new LocalDate[] {LocalDate.of(2050, 1, 10), LocalDate.of(2050, 2, 10), LocalDate.of(2050, 3, 10), LocalDate.of(2050, 4, 10)};
        InvoiceStatus[] liStatuses = new InvoiceStatus[] {InvoiceStatus.OPEN, InvoiceStatus.PAID, InvoiceStatus.OPEN, InvoiceStatus.CANCELLED};
        for (int i = 0; i < liDates.length; i++) {
            SalesOrder liOrder = store.createSalesOrder(new SalesOrder(0, "SO-LI" + (i + 1), liCustomers[i].id(), liDates[i], OrderStatus.SHIPPED,
                    List.of(new OrderLine(liProduct.id(), 1, new BigDecimal("2.00")))));
            store.createInvoice(new Invoice(0, "INV-LI" + (i + 1), liOrder.id(), liCustomers[i].id(), liDates[i], liDates[i].plusDays(30),
                    new BigDecimal("2.00"), liStatuses[i]));
        }
        LatestInvoicesService liService = new LatestInvoicesService(store);
        for (Invoice liInvoice : liService.overall(2)) {
            out.println("  overall " + liInvoice.number());
        }
        for (Invoice liInvoice : liService.forRegion(Region.SOUTH, 2)) {
            out.println("  SOUTH " + liInvoice.number());
        }
        for (Invoice liInvoice : liService.forCustomer(liNorth.id(), 2)) {
            out.println("  LI North: " + liInvoice.number());
        }
    }

    private static void invoicesPerWeekday(DataStore store, PrintStream out) {
        out.println("Invoices per weekday:");
        Product iwProduct = store.createProduct(new Product(0, "IW-1", "IW Widget", "Hardware", new BigDecimal("2.00"), "piece", true));
        Customer iwNorth = store.createCustomer(new Customer(0, "IW North", "iw1@example.com", null, "UK", Region.NORTH, CustomerStatus.ACTIVE));
        Customer iwSouth = store.createCustomer(new Customer(0, "IW South", "iw2@example.com", null, "UK", Region.SOUTH, CustomerStatus.ACTIVE));
        Customer[] iwCustomers = new Customer[] {iwNorth, iwSouth, iwNorth, iwSouth};
        LocalDate[] iwDates = new LocalDate[] {LocalDate.of(2049, 4, 5), LocalDate.of(2049, 4, 12), LocalDate.of(2049, 4, 9), LocalDate.of(2049, 4, 13)};
        InvoiceStatus[] iwStatuses = new InvoiceStatus[] {InvoiceStatus.OPEN, InvoiceStatus.PAID, InvoiceStatus.OPEN, InvoiceStatus.CANCELLED};
        for (int i = 0; i < iwDates.length; i++) {
            SalesOrder iwOrder = store.createSalesOrder(new SalesOrder(0, "SO-IW" + (i + 1), iwCustomers[i].id(), iwDates[i], OrderStatus.SHIPPED,
                    List.of(new OrderLine(iwProduct.id(), 1, new BigDecimal("2.00")))));
            store.createInvoice(new Invoice(0, "INV-IW" + (i + 1), iwOrder.id(), iwCustomers[i].id(), iwDates[i], iwDates[i].plusDays(30),
                    new BigDecimal("2.00"), iwStatuses[i]));
        }
        for (WeekdayInvoices iwDay : new WeekdayInvoicesService(store).report(LocalDate.of(2049, 4, 1), LocalDate.of(2049, 4, 30))) {
            out.println("  " + iwDay.weekday() + " total " + iwDay.total() + ", " + iwDay.perRegion());
        }
    }

    public static void main(String[] args) {
        run(new DataStore(Database.openInMemory()), System.out);
    }
}
