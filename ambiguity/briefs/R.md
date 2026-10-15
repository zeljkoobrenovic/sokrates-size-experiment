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

Restructure the data-access code as a careful developer would: **one store class per entity or closely related
group** (`CustomerStore`, `OrderStore`, `InvoiceStore`, …) in the same package, each method named in the domain's
words, near-copies merged into one parameterized method or helper, repeated JDBC plumbing moved into a shared helper
class, so that a search for the words of a request lands on one place. `DataStore` may remain as the entry point
that hands out the stores (e.g. `store.orders().findOpen(...)`) or be replaced; callers and tests are updated.

## Constraints

- Behaviour-preserving: `./check.sh` must print `CHECK PASSED`; all tests pass.
- New source files are expected; non-data-access code changes only where it calls the stores.
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
