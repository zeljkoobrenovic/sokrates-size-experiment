You are in a folder with:

- `code/`: BackOffice, a Java 17 / Maven order-and-inventory system with an embedded H2 database, a git repository.
  All database access is in `src/main/java/com/acme/backoffice/DataStore.java`.
- `check.sh`: the behaviour check. It builds `code/`, runs every test, then runs the demo scenario of the built code
  and of the baseline (`reference/baseline.jar`) and compares their output. It prints `CHECK PASSED` or
  `CHECK FAILED`.

Work only with what is in this folder; do not look anywhere else on the disk and do not use the network. Do not
change `check.sh`, `reference/` or `tools/`.

## The goal

Coding agents will make many future changes to this codebase. What they cost is measured: it is set by how many
calls they make. An agent searches (grep) for words from the change request, opens windows of 40–70 lines around
the hits, edits, runs a test. Here, a search for the words of a typical request ("open orders in a region", "overdue
invoices of a customer") lands on many look-alike methods in `DataStore.java`, and the agent spends calls opening
them one after another to find the one meant, or misses one of several near-copies that all need the same change.

Make the code **unambiguous for such a search**, inside `DataStore.java`, without moving code to other files:
1. **Names.** Give every method a name that says what it does in the domain's words (entity, filter, ordering,
   period), so that a search for the words of a request finds the one method meant and two methods never differ only
   by a vague word or a number.
2. **Near-copies.** Where several methods are copies of each other that differ only in a filter, an ordering, a
   period or a column, make one method (or one private helper) with parameters, and let the callers use it, so that a
   rule shared by them lives in one place.
3. **Boilerplate.** Move the repeated JDBC plumbing (statement setup, row mapping, exception wrapping) into a few
   private helpers, so the query methods show their query and little else.
4. **Order.** Order the methods by entity, with a short comment line at the start of each entity's section.

## Constraints

- Behaviour-preserving: `./check.sh` must print `CHECK PASSED`; all tests pass.
- Keep `DataStore.java` one file and one class; do not move code to other files or classes, and do not create new
  source files. Other files change only where a renamed method is called. Parameter types a merged method needs
  (a filter, a query object) are nested classes, records or enums inside `DataStore`.
- Keep the public methods that tests call, or rename them and update the callers, including the tests: there is no
  external API to keep. The new names must say what the method does in the domain's words (entity, filter,
  ordering), so that two methods never differ only by a number or a vague word.
- Do not fix bugs; keep the behaviour, including its oddities.
- No new dependencies, no build changes.

Also write `renames.tsv` in this folder, one line per public `DataStore` method that you renamed, merged or removed:
the old method name and parameter types, a tab, and how a caller now gets exactly the same result (the new call,
e.g. `findOrders(OrderQuery.open().inRegion(region))`). Tests elsewhere that use the old names will be ported with it.

Commit when `./check.sh` passes: `git -C code add -A && git -C code commit -m "<what you did>"`, and write
`CHANGES.md`: what you changed, the numbers before and after (methods, near-copies merged, lines of
`DataStore.java`), and anything left as it was and why.
