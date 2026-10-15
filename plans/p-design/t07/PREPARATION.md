# Preparation for task.md (past 90d block shows the wrong LOC)

## What changed
The change is already small and sits in one place: the "past 90d" `addInfoBlock` call in
`LandscapeReportGenerator`. The fix replaces one variable. So the preparation is deliberately small:

- I moved the "active repositories" row (all time / past 365d / past 180d / past 90d / past 30d) out of
  `addBigRepositoriesSummary` into its own private method, `addActiveRepositoriesInfoBlocks(...)`.
  `addBigRepositoriesSummary` now calls it and then adds the "size (LOC)" and "1y code activity" groups as before.
- I added a Javadoc to the new method in the task's own words ("active repositories", "Overview tab",
  "past 90d", "main lines of code", "share of all main LOC").

## Why it makes the later change cheaper
- Searching for the task's words ("active repositories", "past 90d", "main LOC") now lands on one short method
  with a description of what it renders, instead of the middle of a long method mixed with unrelated LOC and
  code-activity blocks.
- Every per-period count, LOC sum and info block sits in that one method within ~35 lines, so one read window
  shows the bug (`loc180Days` passed to the "past 90d" block) next to the correct neighbouring blocks.
  The fix is still a one-token edit there.

I did not make the fix itself: `./accept.sh` reports FAILING and `./check.sh` prints CHECK PASSED
(354 tests, report output identical to the baseline).

## git -C code show --stat HEAD
```
commit 170a9c913b55639c1c5ca06c441af10be370ce90
Author: Zeljko Obrenovic <zeljkoobrenovic@yahoo.com>
Date:   Thu Oct 15 20:43:34 2026 +0200

    Preparation for the change in task.md

 .../statichtml/LandscapeReportGenerator.java       | 33 ++++++++++++++--------
 1 file changed, 21 insertions(+), 12 deletions(-)
```
