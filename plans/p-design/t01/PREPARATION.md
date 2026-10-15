# Preparation for task.md (Undefined Team keeps only active contributors)

## What changed

In `LandscapeAnalysisResults.getAllTeams()`, the inline rule that decides whether an unmatched
contributor goes into the "Undefined Team" remainder was moved into its own method:

```java
/**
 * Decides whether a contributor matched by no configured team (TeamsConfig email patterns) is added to
 * the remainder "Undefined Team". Configured teams do not use this check; they keep all their members.
 */
private boolean belongsToUndefinedTeam(ContributorRepositories contributor, ContributorRepositories undefinedTeam) {
    String email = contributor.getContributor().getEmail();
    return !undefinedTeam.getMembers().contains(email);
}
```

A comment next to the `new Contributor("Undefined Team")` line points to this method. The logic is unchanged.
The existing `contains(email)` check, which compares a String against a list of `ContributorRepositories`,
was kept exactly as it was, because fixing bugs is out of scope.

## Why it makes the later change cheaper

- **Findable:** grepping the words in the task ("Undefined Team", "TeamsConfig", "remainder") lands in
  `LandscapeAnalysisResults.java`, and the hits sit in one 40–70 line window: the remainder's creation,
  the call site, and the method's javadoc. The javadoc also says that configured teams are not affected.
- **One place:** the later change is one condition added to `belongsToUndefinedTeam`,
  `contributor.getContributor().isActive()`. `isActive()` already treats a blank date as active.
  No other file or call site has to change.

## Verification

- `./check.sh`: `CHECK PASSED` (354 tests, report output identical to the baseline across 906 files)
- `./accept.sh`: `acceptance test FAILING (as it must)`

## `git -C code show --stat HEAD`

```
commit 898ce793336b8fed0197c8e116cb2f76b54426ba
Author: Zeljko Obrenovic <zeljkoobrenovic@yahoo.com>
Date:   Thu Oct 15 20:29:55 2026 +0200

    Preparation for the change in task.md

 .../landscape/analysis/LandscapeAnalysisResults.java         | 12 +++++++++++-
 1 file changed, 11 insertions(+), 1 deletion(-)
```
