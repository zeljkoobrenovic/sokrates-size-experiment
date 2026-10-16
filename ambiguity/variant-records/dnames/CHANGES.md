# CHANGES

## What changed

Names only, in `DataStore.java`. 302 of the 407 public methods were renamed so that each name says what the
method does in the domain's words: entity, filter, period and, where siblings differ in it, ordering or grouping.
The callers (services, `DemoScenario`, tests) were updated only where they call a renamed method (122 files).
Method bodies, order, comments and file length are unchanged. The only text changes inside `DataStore.java`
besides declarations are existing Javadoc references to renamed methods (e.g. "valued like
`listStockValuePerWarehouse`").

The naming conventions used:

| Part     | Words used                                                                                                                                                                               |
|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Verb     | `find` = one value / Optional, `list` = rows, `count` = counts, `sum` = totals/aggregates, `search…ByOptionalCriteria` = null-means-any searches                                          |
| Filter   | `OfCustomer`, `OfSupplier`, `OfSalesRepCustomers`, `InRegion`, `InCountry`, `InWarehouse` / `FromWarehouse` / `IntoWarehouse`, `WithProduct`, `InCategoryTree` vs `OfExactCategory`        |
| Status   | hidden filters made explicit: `NotClosed` customers, `NotArchived` / `Open` (confirmed, not archived) / `NonCancelled` sales orders, `Waiting` back orders, `Open` = not closed tickets |
| Period   | the date that is filtered: `PlacedBetween`, `DatedBetween`, `ShippedBetween`, `ReceivedBetween`, `LastPaidBetween`, `PlacedOnOrBefore`, `ExpectedBefore`                                 |
| Ordering | `OrderedByName`, `NewestFirst`, `CheapestFirst`; `Per<X>` = one row per X, `GroupedPer<X>` = rows sorted into X groups                                                                   |

The vague `By` (used for both filtering and sorting) is gone. For example, `listCustomersByName()` (sorting)
and `searchCustomersByName(String)` (filtering) are now `listNotClosedCustomersOrderedByName()` and
`listNotClosedCustomersWithNameContaining(namePart)`. Names that said less than the method does were also fixed:
`listCancelledOrdersByCustomer(from, to)` takes no customer and is now
`listCancelledSalesOrdersBetweenGroupedPerCustomer`. `listInvoicesByCustomer` includes cancelled invoices but
`listInvoicesByCustomerAndPeriod` does not, so they are now `listAllInvoicesOfCustomerNewestFirst` and
`listNonCancelledInvoicesOfCustomerDatedBetween`. The typical requests now land on one method each:
"open orders in a region" → `listOpenSalesOrdersInRegion`, "overdue invoices of a customer" →
`listOverdueInvoicesOfCustomer`.

`renames.tsv` has one line per renamed method: the old name with parameter types, then the new call.

## Numbers

|                              | before | after  |
|------------------------------|--------|--------|
| methods in `DataStore` (public, plus constructor) | 408    | 408    |
| methods renamed              | –      | 302    |
| near-copies merged           | –      | 0 (merging was out of scope: names only) |
| lines of `DataStore.java`    | 10,387 | 10,387 |

`./check.sh`: 1214 tests passed, demo output identical to the baseline, CHECK PASSED.

## Left as it was, and why

- 105 public methods kept their names because they were already unambiguous: the CRUD methods
  (`create…`, `update…`, `save…`, `find…ById`, `find…ByNumber`, `find…ByCode`), and specific ones like
  `closeCustomer`, `receivePurchaseOrder`, `cancelSalesOrder`, `listOpenSalesOrders`, `listOverdueInvoices`,
  `listShipmentsInTransit`, `getConnection`.
- Service methods with the same names as old `DataStore` methods (`BundleService.listBundles`,
  `CategoryService.listCategories`) were not renamed: they are not `DataStore` methods.
- No private helpers exist in `DataStore`, and local variable names were already clear (`rs`, `statement`,
  `sql`, the row lists), so none were renamed.
- The near-copies (for example the Quarter/Weekday/Latest/Country families and the repeated row mapping) remain
  as separate methods. The task was names only; merging or extracting helpers was not allowed.
- Oddities were kept as they are, for example `listCustomersInCountryCreatedBetween` includes closed customers
  while `listNotClosedCustomersInCountry` does not (the names now say so). There is also a stray Javadoc line
  ("Sets the catalogue price…") above `setProductActive` that belongs to `changeProductPrice`; moving it would
  change comments, so it was left.
