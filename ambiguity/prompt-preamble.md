You are working in a Java 17 / Maven repository (BackOffice, an order-and-inventory system with an embedded H2 database). Make the change described below.

Rules:
- The project is already built, so `mvn -o` (offline) works. Run the tests you need, for example `mvn -q -o test -Dtest=<TestClass> -Dsurefire.failIfNoSpecifiedTests=false`, and fix what you break.
- Keep the change small and in the existing style. Do not refactor beyond what the task needs.
- Do not commit. When done, say in a few lines what you changed and where.

Task:
