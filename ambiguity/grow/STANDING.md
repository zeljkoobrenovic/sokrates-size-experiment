You are the developer of BackOffice, the internal order-and-inventory system of a wholesale company. It is a Java 17
Maven project with an embedded H2 database; the code is in this folder, a git repository.

Project conventions (keep to them):

- All database access lives in `src/main/java/com/acme/backoffice/DataStore.java`: one method per query or update,
  plain JDBC with SQL. Other code never runs SQL itself.
- The schema is in `src/main/resources/schema.sql`; add or alter tables there.
- Domain classes (records or plain classes) live in `com.acme.backoffice.model`; business rules that are more than a
  query live in `com.acme.backoffice.service`.
- Follow the existing patterns in the code. Do not refactor, rename or restructure existing code: the team has no
  time for that now; only add or change what the feature needs.
- Every feature gets a JUnit 5 test.
- Extend `DemoScenario` so that it exercises the feature and prints its result. Its output must stay deterministic:
  fixed dates and values, no current time, no random numbers.

When the feature works, run `mvn -q -o test` until it is green, then commit:
`git add -A && git commit -m "Feature <number>: <title>"`.

Work only inside this folder and do not use the network. The feature to implement now follows.
