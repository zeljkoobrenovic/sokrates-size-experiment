package com.acme.backoffice;

import java.sql.Connection;

/**
 * The entry point to all database access of BackOffice: hands out one store per entity or closely related group, all
 * sharing one connection, e.g. {@code store.invoices().listOverdueOfCustomer(asOf, customerId)}.
 *
 * <ul>
 *   <li>Customers: {@link #customers()}, {@link #customerContacts()} (addresses, contact persons),
 *       {@link #creditLimits()} (open credit, over the credit limit)</li>
 *   <li>Catalog: {@link #products()}, {@link #categories()}, {@link #bundles()}, {@link #suppliers()}</li>
 *   <li>Pricing: {@link #promotions()}, {@link #priceLists()} (customer-specific prices), {@link #currencies()}
 *       (exchange rates, customer and invoice currency), {@link #taxRates()}</li>
 *   <li>Inventory: {@link #warehouses()}, {@link #stock()}, {@link #stockMovements()} (movements, adjustments),
 *       {@link #transfers()} (warehouse transfers)</li>
 *   <li>Purchasing: {@link #purchaseOrders()} (incl. receiving)</li>
 *   <li>Sales: {@link #orders()} (sales orders), {@link #orderApprovals()} (line discounts, approvals),
 *       {@link #orderNotes()}, {@link #backOrders()}, {@link #salesReps()} (region managers, targets, commission),
 *       {@link #salesReports()} (revenue, margin, customer sales analytics)</li>
 *   <li>Shipping: {@link #shipments()}, {@link #carriers()}</li>
 *   <li>Billing: {@link #invoices()}, {@link #payments()}, {@link #creditNotes()}</li>
 *   <li>After sales: {@link #returns()} (sales returns), {@link #supportTickets()}, {@link #audit()}</li>
 * </ul>
 *
 * <p>The shared JDBC plumbing is in {@link Jdbc}, the shared row mappers in {@link Rows}, and the count breakdowns
 * every entity has (per quarter, weekday, sales rep, country) in {@link Breakdowns}.
 */
public class DataStore {
    private final Connection connection;
    private final CustomerStore customers;
    private final CustomerContactStore customerContacts;
    private final CreditLimitStore creditLimits;
    private final ProductStore products;
    private final CategoryStore categories;
    private final BundleStore bundles;
    private final SupplierStore suppliers;
    private final PromotionStore promotions;
    private final PriceListStore priceLists;
    private final CurrencyStore currencies;
    private final TaxRateStore taxRates;
    private final WarehouseStore warehouses;
    private final StockStore stock;
    private final StockMovementStore stockMovements;
    private final WarehouseTransferStore transfers;
    private final PurchaseOrderStore purchaseOrders;
    private final OrderStore orders;
    private final OrderApprovalStore orderApprovals;
    private final OrderNoteStore orderNotes;
    private final BackOrderStore backOrders;
    private final SalesRepStore salesReps;
    private final SalesReportStore salesReports;
    private final ShipmentStore shipments;
    private final CarrierStore carriers;
    private final InvoiceStore invoices;
    private final PaymentStore payments;
    private final CreditNoteStore creditNotes;
    private final ReturnStore returns;
    private final SupportTicketStore supportTickets;
    private final AuditStore audit;

    public DataStore(Connection connection) {
        this.connection = connection;
        Jdbc jdbc = new Jdbc(connection);
        this.customers = new CustomerStore(jdbc);
        this.customerContacts = new CustomerContactStore(jdbc);
        this.creditLimits = new CreditLimitStore(jdbc);
        this.products = new ProductStore(jdbc);
        this.categories = new CategoryStore(jdbc);
        this.bundles = new BundleStore(jdbc);
        this.suppliers = new SupplierStore(jdbc);
        this.promotions = new PromotionStore(jdbc);
        this.priceLists = new PriceListStore(jdbc);
        this.currencies = new CurrencyStore(jdbc);
        this.taxRates = new TaxRateStore(jdbc);
        this.warehouses = new WarehouseStore(jdbc);
        this.stock = new StockStore(jdbc);
        this.stockMovements = new StockMovementStore(jdbc);
        this.transfers = new WarehouseTransferStore(jdbc);
        this.purchaseOrders = new PurchaseOrderStore(jdbc);
        this.orders = new OrderStore(jdbc);
        this.orderApprovals = new OrderApprovalStore(jdbc);
        this.orderNotes = new OrderNoteStore(jdbc);
        this.backOrders = new BackOrderStore(jdbc);
        this.salesReps = new SalesRepStore(jdbc);
        this.salesReports = new SalesReportStore(jdbc);
        this.shipments = new ShipmentStore(jdbc);
        this.carriers = new CarrierStore(jdbc);
        this.invoices = new InvoiceStore(jdbc);
        this.payments = new PaymentStore(jdbc);
        this.creditNotes = new CreditNoteStore(jdbc);
        this.returns = new ReturnStore(jdbc);
        this.supportTickets = new SupportTicketStore(jdbc);
        this.audit = new AuditStore(jdbc);
    }

    public Connection getConnection() {
        return connection;
    }

    public CustomerStore customers() {
        return customers;
    }

    public CustomerContactStore customerContacts() {
        return customerContacts;
    }

    public CreditLimitStore creditLimits() {
        return creditLimits;
    }

    public ProductStore products() {
        return products;
    }

    public CategoryStore categories() {
        return categories;
    }

    public BundleStore bundles() {
        return bundles;
    }

    public SupplierStore suppliers() {
        return suppliers;
    }

    public PromotionStore promotions() {
        return promotions;
    }

    public PriceListStore priceLists() {
        return priceLists;
    }

    public CurrencyStore currencies() {
        return currencies;
    }

    public TaxRateStore taxRates() {
        return taxRates;
    }

    public WarehouseStore warehouses() {
        return warehouses;
    }

    public StockStore stock() {
        return stock;
    }

    public StockMovementStore stockMovements() {
        return stockMovements;
    }

    public WarehouseTransferStore transfers() {
        return transfers;
    }

    public PurchaseOrderStore purchaseOrders() {
        return purchaseOrders;
    }

    public OrderStore orders() {
        return orders;
    }

    public OrderApprovalStore orderApprovals() {
        return orderApprovals;
    }

    public OrderNoteStore orderNotes() {
        return orderNotes;
    }

    public BackOrderStore backOrders() {
        return backOrders;
    }

    public SalesRepStore salesReps() {
        return salesReps;
    }

    public SalesReportStore salesReports() {
        return salesReports;
    }

    public ShipmentStore shipments() {
        return shipments;
    }

    public CarrierStore carriers() {
        return carriers;
    }

    public InvoiceStore invoices() {
        return invoices;
    }

    public PaymentStore payments() {
        return payments;
    }

    public CreditNoteStore creditNotes() {
        return creditNotes;
    }

    public ReturnStore returns() {
        return returns;
    }

    public SupportTicketStore supportTickets() {
        return supportTickets;
    }

    public AuditStore audit() {
        return audit;
    }
}
