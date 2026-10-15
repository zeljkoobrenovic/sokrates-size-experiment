# Preparation for task.md (stop truncating landscape contributor lists)

## What changed
The `contributorsListLimit` truncation used to be written out in two places:
- `LandscapeContributorsReport.saveContributorsTable` (the tables of `contributors.html`, `contributors-recent.html`,
  bots and teams pages, plus the "The list is limited to N items (out of M)." note), and
- `LandscapeReportContributorsTab.addRecentContributorsSection` (the bar chart of recent commits per contributor).

Both now go through one new class, `reports/.../landscape/statichtml/LandscapeContributorsListLimit.java`, which
holds all of the limit logic: `getLimit`, `limit(results, list)` (truncates and keeps the order) and
`addLimitNote(...)` (the "limited to" paragraph). Its Javadoc uses the task's words (contributorsListLimit,
contributors.html, contributors-recent.html, "All Contributors", "Recently Active Contributors", bots, teams,
bar chart, "contributors sorted by recent commits", "The list is limited to"), and each call site has a comment
that points to it. Behaviour is unchanged: `./check.sh` passes (354 tests, report output identical across 906 files),
and `./accept.sh` still reports FAILING.

## Why the later change is cheaper
Grepping for `contributorsListLimit` / `ContributorsListLimit` or "limited to" now lands on a single file that
explains every list the setting affects. The whole change happens in that one file: make `limit` return the list
unchanged and remove the note from `addLimitNote` (or make it do nothing). Neither caller needs editing, so there's
no second place to forget.

## git -C code show --stat HEAD
```
commit 91bec7fc24999a1c604884ea72d19f6f8d79c292
    Preparation for the change in task.md

 .../statichtml/LandscapeContributorsListLimit.java | 51 ++++++++++++++++++++++
 .../statichtml/LandscapeContributorsReport.java    | 18 ++++----
 .../statichtml/LandscapeReportContributorsTab.java |  3 +-
 3 files changed, 61 insertions(+), 11 deletions(-)
```
