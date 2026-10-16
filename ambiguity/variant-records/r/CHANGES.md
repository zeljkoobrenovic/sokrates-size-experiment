# Data access split into stores

## What changed

`DataStore.java` (10,387 lines, 406 public data-access methods, every one with its own JDBC plumbing) is now the
entry point that hands out 30 stores, one per entity or closely related group, in the same package:

| Area        | Stores (accessor on `DataStore`)                                                                                  |
|-------------|-------------------------------------------------------------------------------------------------------------------|
| Customers   | `CustomerStore` (`customers()`), `CustomerContactStore` (`customerContacts()`: addresses, contacts), `CreditLimitStore` (`creditLimits()`) |
| Catalog     | `ProductStore`, `CategoryStore`, `BundleStore`, `SupplierStore`                                                   |
| Pricing     | `PromotionStore`, `PriceListStore` (customer prices), `CurrencyStore` (exchange rates, customer/invoice currency), `TaxRateStore` |
| Inventory   | `WarehouseStore`, `StockStore`, `StockMovementStore` (movements, adjustments), `WarehouseTransferStore` (`transfers()`) |
| Purchasing  | `PurchaseOrderStore` (incl. receiving)                                                                            |
| Sales       | `OrderStore` (`orders()`), `OrderApprovalStore`, `OrderNoteStore`, `BackOrderStore`, `SalesRepStore`, `SalesReportStore` |
| Shipping    | `ShipmentStore`, `CarrierStore`                                                                                   |
| Billing     | `InvoiceStore`, `PaymentStore`, `CreditNoteStore`                                                                 |
| After sales | `ReturnStore`, `SupportTicketStore`, `AuditStore`                                                                 |

Calls read `store.invoices().listOverdueOfCustomer(asOf, customerId)`,
`store.orders().listOpenInRegion(region)`, `store.shipments().listLatest(limit)`, `store.customers().countPerQuarter(year)`.
Every old method maps to exactly one new call; the full list is in `renames.tsv` (406 lines).

Shared code, package-private:

- `Jdbc`: the JDBC plumbing that was repeated in every method: binding parameters, running a query/update/insert,
  mapping rows, wrapping `SQLException` in `IllegalStateException` with the method's original message, and
  `inTransaction` (commit, rollback, restore auto-commit). `...Raw` variants are for use inside a transaction.
- `Rows`: row mappers for the entities several stores read (customer, product, invoice, shipment, …), and the loaders
  of order-like entities with their lines (sales order, purchase order, sales return, warehouse transfer).
- `Breakdowns`: the count families that every entity had as near-copies: per quarter and per weekday (in total and
  per region), per sales rep, per country. 24 public methods across customers, orders, purchase orders, shipments,
  returns and invoices now each describe what they count in one line, and the SQL shape exists once.
- `CatalogSql`, `InventoryRows`: small helpers shared within the catalog and inventory stores.

Callers (services, `DemoScenario`, 1214 tests) were rewritten mechanically from the mapping: 2,748 call sites.
`DemoScenario.run` grew past the JVM's 64 KB method limit because each call now has one more accessor call, so its last
block of report sections (customers/orders/invoices per quarter, weekday, sales rep, country; latest orders/customers)
moved into a private section method, like the other sections the file already has. Its output is unchanged.

## Numbers

|                                         | Before                    | After                                         |
|-----------------------------------------|---------------------------|-----------------------------------------------|
| `DataStore.java`                        | 10,387 lines              | 220 lines (constructor + 30 accessors + `getConnection`) |
| Data-access code in total               | 10,387 lines in 1 class   | 6,415 lines in 36 classes                     |
| Largest class                           | 10,387 lines              | `OrderStore`, 615 lines                       |
| Public data-access methods              | 406 on one class          | 406 on 30 stores (one per question callers ask) |
| Private helpers / shared implementations | 0                        | 100                                           |
| Call sites rewritten                    |                           | 2,748                                         |

Near-copies merged into shared implementations (per group, from the notes of each extraction):

| Group                                  | Original methods folded | Into |
|----------------------------------------|------------------------:|-----:|
| Customers, contacts, audit             | 21 | 6 |
| Products, categories, bundles, suppliers | 24 | 7 |
| Promotions, price lists, support tickets | 22 | 3 (+ "active on date", "valid on date", "open on date", "closed between dates" each written once) |
| Warehouses, stock, movements, transfers | 12 | 4 (+ stock value expression: 6 copies → 1) |
| Purchase orders                        | 16 | 1 query (open/overdue 5 → 1, latest 3 → 1) |
| Sales reports                          | 10 | 5 |
| Sales orders                           | 28 | 7 (15 order lists → 1 query; "open" defined once) |
| Order approvals, notes, back orders, sales reps | 15 | 5 |
| Invoices and payments                  | 17 | 5 ("overdue" defined once; payments received 5 → 1) |
| Shipments and carriers                 | 38 | 21 ("in transit", "late", "shipped in period" each written once) |
| Returns, credit notes, credit limits   | 26 | 7 |
| Per quarter / weekday / sales rep / country (all entities) | 24 | `Breakdowns` (4 methods) |

Distinct questions stayed distinct public methods (`listOverdue`, `listOverdueOfCustomer`, `listOverdueInRegion`,
`listOverdueOfSalesRep`), each a line or two over the shared query, so the call sites stay readable while a change to
what "overdue" means is one edit.

## Behaviour

`./check.sh`: `CHECK PASSED`: build and 1214 tests pass, demo output identical to the baseline (1106 lines).

Kept as it was, on purpose (do not fix bugs):

- Differences between near-copies that look accidental are kept as parameters or separate conditions, with a comment:
  some order lists exclude archived orders and some do not; some count cancelled orders and some do not; the overdue
  amount also requires `invoice_date <= asOf` while the overdue lists do not; the over-limit customer list includes
  customers without a sales rep while the per-rep report does not; `listCreatedInCountry` includes closed customers;
  `listInTransitShippedBefore` is exclusive; the old-high-priority-tickets query has no "opened on or before" check;
  the total year-over-year query has no customer join while the per-region one has.
- Error messages are the original ones per method, even where the wording no longer matches the new name.
- Null region/status/reason arguments still throw `NullPointerException` (they were bound with `.name()`).
- Transactions keep their original boundaries and side effects (stock rows inserted when missing, movements recorded,
  status history rows); saves that were two statements without a transaction (exchange rate, tax rate, back order)
  still are.

## Left as it was, and why

- One-off reports with no look-alikes (region report, customer activity, invoice aging, year-over-year, commission
  basis, the recursive category-tree query) keep their SQL nearly verbatim; there was nothing to merge.
- `DataStore.getConnection()` is kept although nothing calls it; it is the one public method of the old class that is
  not data access and not in `renames.tsv`.
- Model types, services and the schema are unchanged; services only changed where they call the stores.
- A few stores use `listBy…` (order notes, back orders, sales reps, orders by product/channel) where most use
  `listOf…`/`listIn…`; both read as domain words, so they were not renamed again.
- Helper scripts used for the mechanical rewrite (inventory, mapping check, call-site rewriter) and the per-group notes
  are in `.work/`, outside the repository.
