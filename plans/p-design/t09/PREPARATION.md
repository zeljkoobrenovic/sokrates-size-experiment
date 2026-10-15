# Preparation for task.md (`analyzeLandscape` command)

## What changed

1. **`CommandLineInterface.updateLandscape(String[] args, String commandName, String commandDescription)`**
   The landscape handler used to hard-code `Commands.UPDATE_LANDSCAPE` / `UPDATE_LANDSCAPE_DESCRIPTION` in its
   `-help` branch. It now takes the command name and description as parameters, so `-help` prints the usage of
   whichever command name the dispatcher passes in. The `updateLandscape` dispatch branch in `run()` passes the
   `updateLandscape` constants, so behaviour is unchanged. The old one-argument method was private and is now
   gone, which leaves one landscape handler instead of two overloads.
2. **Comments that use the task's words** (landscape command, landscape report, analysis root, `-analysisRoot`,
   `-recursive`, `-confFile`, `-help`, usage, no-argument usage overview) are now at the three places the change
   touches: the dispatch branch in `CommandLineInterface.run`, the `UPDATE_LANDSCAPE` constants in `Commands`, and
   the `usageInfo()` entry in `Commands`.

## Why it makes the later change cheaper

The later change is now one handler that is already parameterised plus three one-line additions next to each
other in two files:
- a constant and description for `ANALYZE_LANDSCAPE` beside `UPDATE_LANDSCAPE`,
- a dispatch branch that calls `updateLandscape(args, Commands.ANALYZE_LANDSCAPE, Commands.ANALYZE_LANDSCAPE_DESCRIPTION)`,
- a `CommandUsage` line in `usageInfo()`.

The handler itself doesn't need to change. A search for "landscape" or "updateLandscape" lands on all three places.
The README has no list of commands (it only mentions `init`), so I didn't prepare anything there.

Verification: `./check.sh` prints `CHECK PASSED` (354 tests pass, and the 906 report files are identical to the
baseline). `./accept.sh` reports `acceptance test FAILING (as it must)`.

## `git -C code show --stat HEAD`

```
commit a97c8b4339a2ccf890fe21250a0b70473d3d01fb

    Preparation for the change in task.md

 .../main/java/nl/obren/sokrates/cli/CommandLineInterface.java | 11 ++++++++---
 cli/src/main/java/nl/obren/sokrates/cli/Commands.java         |  2 ++
 2 files changed, 10 insertions(+), 3 deletions(-)
```
