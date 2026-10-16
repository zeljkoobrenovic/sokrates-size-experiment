# DataStore: unambiguous names, merged near-copies, JDBC plumbing in helpers, ordered by entity

Behaviour is unchanged. `./check.sh` prints `CHECK PASSED`: all 1214 tests pass and the demo output is identical to
the baseline (1106 lines). Everything is still in `DataStore.java`, one file and one class. Other files changed only
where a renamed method is called (services, `DemoScenario`, tests). `renames.tsv` maps every renamed public method,
245 lines, to the call that now gives the same result.

## Numbers, before → after

| | before | after |
|---|---|---|
| Lines of `DataStore.java` | 10,387 | 5,990 |
| Public methods (incl. constructor and `getConnection`) | 408 | 408 |
| … renamed | – | 245 |
| … one-line delegations to a shared private helper | 0 | ~152 |
| Private methods | 0 | 91 |
| Nested private types (query objects, enums, row interfaces, records) | 0 | 21 |
| `prepareStatement` calls | 488 | 5 (all in the helpers) |
| `catch (SQLException …)` blocks | 426 | 10 (helpers and transaction code) |
| Entity sections with a comment line | 0 | 19 (plus 2 for the plumbing) |

## What changed

1. **Plumbing.** The top of the class now holds the JDBC plumbing (`JDBC plumbing` section):
   - query helpers `queryList`, `queryOptional`, `querySingle`, `update` and `insert` (which returns the generated id);
   - `bind` (enums bound as their `name()`, null as SQL NULL);
   - `failure` (SQLException wrapping);
   - `inTransaction` and `inTransactionRollingBackOnDatabaseFailureOnly`;
   - `containsPattern` (LIKE escaping);
   - row mappers shared by several entities (`customerRow`, `productRow`, `invoiceRow`, `shipmentRow`, `salesOrderRow`
     and `purchaseOrderRow`, the last two with their lines).

   Each entity section adds its own mappers (`supplierRow`, `warehouseRow`, `supportTicketRow`, …). Query methods now
   show their SQL and parameters and little else.
2. **Names.** Every public method's name now says, in domain words:
   - the entity (`SalesOrder`, `PurchaseOrder`, `SalesReturn`, `Invoice`, … rather than "Order"/"Return");
   - the filter (`InRegion`, `OfCustomer`, `IntoWarehouse`, `NotClosed`, `Open`, `Overdue`, …);
   - the period basis (`PlacedBetween`, `ShippedBetween`, `DatedBetween`, `ReturnedBetween`, …);
   - the ordering, where it differs from the family's default (`NewestFirst`, `CheapestFirst`, `HighestPriorityFirst`).

   The verbs are consistent too: `find…` for reads, `count…`, `sum…`, `is…`/`has…`, and
   `create…`/`update…`/`save…`/`set…` or the domain verb for writes. No two methods differ only by list/find/search
   or by a number. Examples: `listOpenSalesOrdersByRegion` → `findOpenSalesOrdersInRegion`,
   `listOverdueInvoicesByCustomer` → `findOverdueInvoicesOfCustomer`, `findOrdersPerQuarter` →
   `findSalesOrderCountPerQuarter`, `searchCustomersByName` → `findNotClosedCustomersWithNameContaining`.
3. **Near-copies merged.** About 44 families covering about 205 methods now share one private query object or helper,
   so each shared rule lives in one place (the open / not-archived rule, the late-delivery rule, the open-balance
   subquery, the region bucketing, …):

   | Family | Methods | Shared by |
   |---|---|---|
   | sales order lists, counts and values (open, by customer/region/period/channel/status, archived, latest, by country, awaiting approval, search) | 22 | `SalesOrderQuery` + `querySalesOrders/Count/Value` |
   | shipment lists (in transit, late, by warehouse/customer/region/product/country, latest, search) | 16 | `ShipmentQuery` + `findShipments`, rule `SHIPMENT_DELIVERED_LATE` |
   | invoice lists (by customer/period, overdue all/customer/region/rep, open/paid of rep, latest, by country, search) | 14 | `InvoiceQuery` + `findInvoices` |
   | purchase order lists (by supplier, open, overdue, into warehouse, by product/category/country, latest, search) | 13 | `PurchaseOrderQuery` + `findPurchaseOrders` |
   | record counts per quarter / per weekday (customers, sales orders, purchase orders, shipments, returns, invoices) | 12 | enum `RegionCountedRecords` + `findCountPerQuarter/Weekday` |
   | record counts per sales rep / per country (same six entities) | 12 | enum `CustomerAttributedRecords` + `findCountPerSalesRep/Country` |
   | product lists (active, by name, category, category tree, price range, price change, bundles) | 8 | `ProductQuery` + `findProductsMatching` |
   | sales return lists; open support ticket lists | 8 + 8 | `SalesReturnQuery`, `SupportTicketQuery` |
   | customer lists (not closed: by name, region, country, status, sales rep) | 6 | `CustomerQuery` + `findCustomers` |
   | payments received (period, customer, region, rep, excl. cancelled); credit notes (customer, period, region) | 5 + 4 | `PaymentReceivedQuery`, `CreditNoteQuery` |
   | stock movements; warehouse transfers; low stock; slow-moving stock; stock value | 3+3+2+2+6 | query objects / SQL constants |
   | latest customers; sales per customer (2 families); sales and margin per product/category | 3+2+2+2+2 | private helpers |
   | orders with product, cancelled, stale drafts, history, large/small orders | 3+2+2+2+2 | private helpers |
   | over-credit-limit / open credit; shipped quantities/weights; carrier orders; lead time | 5+3+2+2 | SQL constants / helpers |
   | returned units per product; audit entries; order notes; back orders; year over year; commission | 3+3+4+3+2+2 | private helpers |

4. **Order.** Members are ordered by entity, and each section starts with a `// ===== … =====` comment line:
   - plumbing, then shared row mappers;
   - customers; customer addresses and contacts; sales reps; products and categories; promotions and prices;
   - suppliers; warehouses and stock; purchase orders; sales orders; order notes; back orders;
   - invoices; payments; credit; shipments and carriers; sales returns; support tickets; audit entries;
   - cross-entity reports.

   A private helper or query type sits in the section of the methods that use it.

## Left as it was, and why

- **Public methods stay single calls with the same arguments.** `DemoScenario.run()` is one ~2,800-line method a few
  bytes under the JVM's 64 KB per-method limit. A call site that grows even slightly breaks the build with "code too
  large". This rules out passing query objects such as `findOrders(OrderQuery.open().inRegion(r))` from callers, so
  the query objects are private. Each variant keeps a thin, well-named public method that delegates to the merged
  helper in one line. A grep for "open orders in a region" lands on `findOpenSalesOrdersInRegion`, whose one line
  points at `SalesOrderQuery`, where the rule lives. This is also why the public method count did not drop.
- **Methods that only look alike were not merged.** They were named so the difference is visible, for example:
  - revenue per customer "WithOrders" (includes closed customers) vs. "NotClosed…IncludingIdle";
  - slow-moving stock per warehouse vs. in total (different "shipped from" rule);
  - the three `sum…DatedBefore` totals;
  - the supplier-product lists;
  - `findSalesPerRegion`, `findRegionReport`, `findMonthlySales`, `findSalesTargetProgress` and other one-off reports.
- **Exception messages of the JDBC wrapping are now generic** (`Cannot execute: <sql>`, `Transaction failed`). Only
  the type (IllegalStateException) was observable; no test or demo output uses those messages. Messages thrown on
  purpose (e.g. `Unknown product: …`) are unchanged.
- **Oddities kept on purpose:**
  - null enum arguments still throw NullPointerException; the search methods still accept null;
  - parameter orders like `(LocalDate asOf, long customerId)` are unchanged;
  - transaction outcomes are unchanged. The multi-statement writes that only rolled back on a database failure still
    do (`inTransactionRollingBackOnDatabaseFailureOnly`), so a null line still leaves earlier inserts committed.
- **SQL rewritten rather than copied:** the per-sales-rep and per-country counts now use `COUNT(DISTINCT …)` and, for
  purchase orders, a join instead of `EXISTS`. The joins are many-to-one and the counted columns are NOT NULL, so the
  results are the same; tests and demo agree. Some region/country filters became `EXISTS` instead of a join on a
  unique key.
