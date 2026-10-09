You are working in a Java 17 / Maven multi-module repository (Sokrates, a source-code analysis tool that generates HTML reports). Make the change described below.

Rules:
- The project is already built and installed, so `mvn -o` (offline) works. Run the tests of the module(s) you change, for example `mvn -q -o -pl <module> test -Dtest=<TestClass> -Dsurefire.failIfNoSpecifiedTests=false`, and fix what you break. Do not run a full `mvn install` unless you need it.
- Keep the change small and in the existing style. Do not refactor beyond what the task needs.
- Do not commit. When done, say in a few lines what you changed and where.

Task:
