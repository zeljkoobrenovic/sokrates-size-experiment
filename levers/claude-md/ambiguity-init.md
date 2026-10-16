# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

Maven project, Java 17, no wrapper (use system `mvn`).

- Build + all tests: `mvn test`
- Single test class: `mvn test -Dtest=TaxRateTest`
- Single test method: `mvn test -Dtest=TaxRateTest#methodName`
- Run the demo: `mvn compile exec:java -Dexec.mainClass=com.acme.backoffice.DemoScenario` (no exec plugin is configured in `pom.xml`, so this needs the full plugin coordinates, or run `DemoScenario.main` from the IDE)

No linter or formatter is configured.

## Architecture

A back-office (orders, invoicing, stock, purchasing, returns, support) library on an in-memory H2 database, using plain JDBC. There is no app server and no ORM. Dependencies are only H2 and JUnit 5.

Three layers under `com.acme.backoffice`:

- **`Database`**: `openInMemory()` creates a fresh uniquely named H2 instance and runs `src/main/resources/schema.sql`. The script is split on `;` and `--` comment lines are stripped. So don't put a literal `;` inside a statement or string in the schema, and keep comments on their own lines.
- **`DataStore`** (about 10k lines): the single persistence class. Every SQL query lives here, including the aggregate and report queries. It is constructed with a `Connection`, and it maps rows to model records.
- **`service/*Service`**: thin business-logic classes, each constructed with the `DataStore`. They validate input (throwing `IllegalArgumentException`), apply rules such as VAT or credit limits, and delegate to `store.*` methods. Services don't touch JDBC directly. One service roughly corresponds to one feature, and many report features follow a family pattern (`Country*`, `Quarter*`, `Weekday*`, `Latest*`, `SalesRep*Count`).
- **`model/`**: immutable Java records (the project targets 17), including a large number of small result-row records for reports (e.g. `CountryOrderCount`, `CategoryMargin`).

`DemoScenario` (about 3.5k lines) is a runnable walkthrough that exercises most services against a store. `DemoScenarioTest` covers it.

## Adding a feature

A feature usually touches all layers: tables or columns in `schema.sql`, record(s) in `model/`, query methods in `DataStore`, a `service/` class, and a `src/test/java/.../XxxTest.java`.

## Tests

- One test class per feature, in the flat package `com.acme.backoffice` (not mirroring `service/`).
- Tests build their own state, with no shared fixtures or test resources: `new DataStore(Database.openInMemory())`, then create customers, products and so on through the store, and call the service. Each test instance gets an isolated database.
- Money is `BigDecimal`. Tests compare with `compareTo` (see `assertAmount` in `TaxRateTest`) rather than `equals`, to avoid scale mismatches.
