# Brief for writing the BackOffice tasks

`<EXP>` = /Users/zeljkoobrenovic/IdeaProjects/sokrates-size-experiment. The codebase is `<EXP>/ambiguity/variants/a`
(BackOffice, Java 17 / Maven / H2; all SQL in `src/main/java/com/acme/backoffice/DataStore.java`), with its git
history (one commit per feature) in `<EXP>/ambiguity/grow/history.bundle` if you want it. Never modify anything under
`<EXP>/ambiguity/variants/`; work in scratch copies under `<EXP>/work/<your name>/`. Do not read
`<EXP>/ambiguity/briefs/` (other than this file), `<EXP>/plans/`, `<EXP>/results/` or `<EXP>/docs/`.

Write the tasks into `<EXP>/ambiguity/tasks/<id>/`, each with:

1. `task.md`: the change request, as a product owner or a support ticket would write it: what is wrong or wanted, in
   the domain's words (customers, open orders, overdue invoices, regions, …), with a concrete observable outcome.
   Never name files, classes, methods, SQL, tables or columns. 3–8 sentences.
2. `meta.json`: `id`, `kind` (`target` or `control`), `task_kind` (1–4 below, or `control`), `title`,
   `test_command` (`mvn -q -o test -Dtest=<TestClass> -Dsurefire.failIfNoSpecifiedTests=false`), `terms` (3–6 search
   words a maintainer would grep for, taken from the task text, e.g. `["open orders", "region"]`), `notes` (what the
   reference change does; the verification results).
3. `acceptance/a/src/test/java/com/acme/backoffice/<TestClass>.java`: one JUnit 5 test class that fails on the
   untouched code (an assertion failure, not a compilation error) and passes with the reference change. Build the
   data in the test through the existing public API; assert on the observable outcome.
4. `reference/a.patch`: the reference change (unified diff, `a/` and `b/` prefixes relative to the codebase root, no
   test files), a minimal, sensible change.

The tasks, all in the data-access layer except the controls; each task touches behaviour that **several similar
methods** in `DataStore.java` share or that **one of several similar methods** has, so that a search for its words
is ambiguous in this code. Choose real look-alike methods you find; record in `notes` which similar methods exist.

- **3 × kind 1, fix one of many look-alikes:** introduce nothing; find a genuine inconsistency or define a corrected
  behaviour for exactly one of a family of similar queries (e.g. one listing includes cancelled orders where its
  siblings exclude them, a sort order, an off-by-one date bound), described as a user-visible bug.
- **3 × kind 2, change a rule the siblings share:** a rule that should hold in every one of a family of similar
  queries (e.g. "archived orders never show in order listings", "amounts are rounded to cents in every invoice
  report"), where today it is missing everywhere or in several places.
- **3 × kind 3, add a query next to similar ones:** a new listing or report closely modelled on existing ones
  (e.g. "the overdue invoices of a sales rep's customers"), with its own expected output.
- **3 × kind 4, thread a new field through one entity:** a new attribute of one entity that must be stored, read
  back and shown in that entity's existing listings (e.g. "orders get a customer reference number, shown in every
  order listing").
- **3 controls:** behaviour outside `DataStore.java` (a service rule, the demo scenario's formatting, a model
  class), so that the change does not touch the data-access code at all.

Verification (record in `notes`): in a scratch copy, add the test and run the test command: it must compile and FAIL;
apply the reference patch: it must PASS, and `mvn -q -o test` must be green. Use `-o` (offline) and never
`mvn install`. Then run `python3 <EXP>/scripts/ambiguity_measure.py <copy> --terms <the task's terms>` and record
the per-term methods hit in `notes`.

Report back per task: id, kind, title, the similar methods involved, the verification results, the patch size.
