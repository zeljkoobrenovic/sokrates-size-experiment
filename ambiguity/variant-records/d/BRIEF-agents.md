# Shared brief for the DataStore refactoring agents

You work in your own git worktree of BackOffice (Java 17, Maven, embedded H2). All database access is in
`src/main/java/com/acme/backoffice/DataStore.java` (~10,500 lines, ~406 public methods). Nine agents work in
parallel, each on a disjoint GROUP of DataStore methods (yours is listed in your prompt). Your branch is merged with
the others afterwards, so stay strictly inside your group.

## Why
Coding agents will later change this code by grepping for words of a change request ("open orders in a region",
"overdue invoices of a customer") and opening 40-70 line windows around the hits. Today such a search lands on many
look-alike methods. The code must become unambiguous for such a search.

## What to do with the methods of YOUR group
1. **Names.** Every public method gets a name that says what it does in the domain's words (entity, filter, ordering,
   period). Two methods must never differ only by a vague word (list/find/search/get) or a number. Conventions:
   reads start with `find` (Optional for a single entity by key, List for lists), `count…` for counts, `sum…`/`total…`
   for amounts, `is…`/`has…` for booleans; writes use `create…`, `update…`, `delete…`, `save…` (upsert), `set…`,
   `add…`, `remove…`, or the domain verb (`cancel…`, `archive…`, `receive…`). Always include the entity name
   (`SalesOrder`, `PurchaseOrder`, `Invoice`, …), not just "Order" when it is ambiguous. Per-group aggregates read
   like `find<Measure>Per<Grouping>` (e.g. `findInvoiceCountPerSalesRep`). Keep a good existing name as it is.
2. **Near-copies.** Where several methods are copies differing only in a filter, an ordering, a period, a limit or a
   column, make ONE public method with a parameter object (a nested `record`/final class/enum inside DataStore, e.g.
   `public static final class SalesOrderQuery` with static factories and chainable filters such as
   `SalesOrderQuery.open().inRegion(region)`, or an enum like `InvoicePeriodBasis`) or ONE private helper called by
   thin, well-named public methods. The rule they share must then live in one place. Pick whichever makes call sites
   read clearly in domain words. Don't merge things that only look alike but differ in rules; then name them so the
   difference is visible.
3. **Boilerplate.** Use the private helpers at the top of DataStore (already there, do not change them):
   `queryList(sql, mapper, params...)`, `queryOptional(...)`, `querySingle(...)` (aggregates that always return a
   row), `update(sql, params...)` (returns rows changed), `insert(sql, params...)` (returns generated id),
   `inTransaction(() -> {...})`, `containsPattern(part)` (LIKE escaping, use with `ESCAPE '\\'`), and the row mappers
   `customerRow`, `productRow`, `invoiceRow`, `shipmentRow`, `salesOrderRow` (loads lines), `purchaseOrderRow`
   (loads lines). `bind` passes enums as `name()`, null as SQL NULL, other values via `setObject`. Add mappers for
   your own entities as `private static <Entity> <entity>Row(ResultSet rs)` next to your methods. If you need another
   generic helper, add it in your area with a name specific to your group (other agents may add their own).
   The query methods should show their SQL and little else.
4. **Order.** Do NOT move methods around the file to reorder them; the coordinator reorders everything by entity
   after merging. Rewrite each method where it is; a merged method goes where the first of its copies was, its nested
   parameter type directly above it.

## Hard constraints
- **Behaviour-preserving**, including oddities and apparent bugs: same SQL semantics (filters, joins, NULL handling,
  ORDER BY and tie-breaks, LIMIT), same results, same transaction behaviour, same return values (e.g. a method that
  returns null, or false, in some case keeps doing so). Do not fix bugs. If variants of a family differ in a subtle
  rule (one excludes archived orders, one doesn't; one uses `<`, another `<=`), the merged version must reproduce each
  variant exactly — encode the difference as an explicit option, or don't merge. Read every copy before merging.
- The exception messages of DataStore are not observable (only the type IllegalStateException is); the helpers wrap
  SQLException generically. Exceptions thrown on purpose (e.g. "Unknown product: …") must stay identical. Check every
  transaction method (setAutoCommit/commit/rollback) carefully before using `inTransaction`: e.g. some originals
  throw a RuntimeException after an explicit rollback, or commit and return null — keep exactly that outcome; if
  `inTransaction` can't express it, keep explicit code (but tidy).
- Keep DataStore one file and one class; no new source files; no code moved to other classes; no new dependencies;
  no build changes. Other files (services, `DemoScenario.java`, tests) change ONLY where a renamed/merged method is
  called. Update every caller including tests (`grep -rn "\.oldName(" src`). Don't weaken or delete tests.
- Touch only your group's methods. If a line calls methods of two groups, change only your part.
- Keep (adapted) Javadoc comments; they carry rules. A merged method's Javadoc states the shared rule once; the
  query type's factories/filters say what each one selects.

## Verify (in your worktree, from its root)
1. `mvn -o -q verify 2>&1 | tail -40` — must build and pass all tests (~1214).
2. Demo output must be identical to the baseline:
   `java -cp "target/classes:$HOME/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar" com.acme.backoffice.DemoScenario > /tmp/demo-<group>.txt && diff /private/tmp/claude-501/-Users-zeljkoobrenovic-IdeaProjects-sokrates-size-experiment/90ff86d9-b4c8-465f-af35-64a8c470eea8/scratchpad/amb-d/.check/baseline.txt /tmp/demo-<group>.txt && echo DEMO SAME`
   (If `$HOME` is refused, use `ls ~/.m2/repository/com/h2database/h2/2.2.224/` to find the jar.)
Do NOT run `check.sh` and do not touch anything outside your worktree except /tmp/demo-<group>.txt.

## Deliverables
- Commit on your worktree branch: `git add -A && git commit -m "<group>: ..."` (only when verify passes).
- Write `renames-<group>.tsv` in the ROOT of your worktree (committed): one line per old public method of your group
  that you renamed, merged or removed: `oldName(ParamType, ParamType)` TAB `how a caller now gets exactly the same
  result`, e.g. `listOpenSalesOrdersByRegion(Region)	findSalesOrders(SalesOrderQuery.open().inRegion(region))`.
  Use simple type names (`long`, `LocalDate`, `Region`, `Long`, …). Unchanged methods are not listed.
- Final report (short): methods of your group before → after (public and private), near-copy families merged (which
  ones, how many methods each), anything left as it was and why, any risk you see for the merge.
