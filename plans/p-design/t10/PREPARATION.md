# Preparation for task.md

Both defects in the task are in one file, `LogicalComponentsReportGenerator.java`, but they are about 300 lines
apart. The code also used different words from the task: it said "key2" where the task says "reverse direction",
and it computed the percentage inline where the task says "share of the referring component's lines of code".
This refactoring changes no behaviour and does not fix either bug.

What changed:

1. **Group links.** The loop body of `getGroupDependencies` is now its own method,
   `mergeIntoGroupLink(groupDependencies, groupDependenciesMap, from, to, count)`. That method holds the whole
   same-direction / reverse-direction / new-link logic, and the reverse-direction branch is marked
   `// reverse direction`. Both methods have Javadoc in the task's words: "Group Dependencies", "component groups",
   "one link per pair of groups", "summed file count", "reverse direction of one already seen". Fix 1 is now a
   one-token edit (`key1` → `key2`) on the line under that comment.
2. **Coverage percentage.** The division is now its own method,
   `getShareOfComponentLinesOfCode(dependentLinesOfCode, componentLinesOfCode)`, which is the single place that
   needs the zero guard. `getFromDependencyCoverageSvg` now has Javadoc that names the
   "Show more details about dependencies..." table, "share", "lines of code" and the "3 files, 10 LOC (12%)" text.
   Fix 2 is a one-line edit in that helper.

Searching for "Group Dependencies", "reverse direction", "Show more details about dependencies" or "lines of code"
now leads straight to the code to edit, and each fix is a single edit in a single place.

Verification: `./check.sh` prints `CHECK PASSED` (354 tests passed, report output identical to the baseline across
906 files). `./accept.sh` reports `acceptance test FAILING (as it must)`.

```
$ git -C code show --stat HEAD
commit f3babf1948049104a92af78a659cab5be0461c72
Author: Zeljko Obrenovic <zeljkoobrenovic@yahoo.com>
Date:   Thu Oct 15 20:50:18 2026 +0200

    Preparation for the change in task.md

 .../LogicalComponentsReportGenerator.java          | 52 +++++++++++++++-------
 1 file changed, 37 insertions(+), 15 deletions(-)
```
