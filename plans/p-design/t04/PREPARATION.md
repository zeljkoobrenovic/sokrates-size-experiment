# Preparation for task.md (mock folders are test code)

## What I found
The rule the task changes already sits in one place: the two `Convention` patterns
`.*/__mock[a-zA-Z0-9_\- ]+/.*` and `.*/mock[a-zA-Z0-9_\- ]+/.*` in
`ScopingConventions.addTestConventions()`. No other Java file mentions "mock". The task also mentions
Windows backslash paths, and those are already handled generically in `SourceFileFilter.pathMatches`, which a
later agent might otherwise go and read to check.

## What I changed (behaviour-preserving)
- Moved the four "Mock resources" conventions (`mockapi`, `__mock…`, `mock…`, `*_mock.*`) into a new private
  method, `ScopingConventions.addMockResourcesConventions()`. It is called from the same spot in
  `addTestConventions()`, so the order of the conventions, and so the reports, stay the same.
- Added a Javadoc and a comment that use the task's own words ("mock-resources folder", "test code", "starts
  with `mock` or `__mock`", "Windows-style paths with backslashes"). They say that backslash paths are already
  normalised by `SourceFileFilter.pathMatches`, so only the `/` patterns need to change.

## Why this makes the later change cheaper
A grep for "mock-resources folder", "Mock resources" or "__mock" now leads straight to one small method that
holds the two patterns to change and notes about the separators. The agent doesn't need to open
`SourceFileFilter` to check how backslashes are handled. The fix stays a two-line edit in one place.

## Verification
- `./check.sh`: CHECK PASSED (354 tests, the reports match the baseline in all 906 files)
- `./accept.sh`: acceptance test FAILING (as it must)

## git -C code show --stat HEAD
```
commit 639c1d06a6a291ed6b6eaeb0b20dc8db08f2baaf
Author: Zeljko Obrenovic <zeljkoobrenovic@yahoo.com>
Date:   Thu Oct 15 20:37:18 2026 +0200

    Preparation for the change in task.md

 .../sourcecode/scoping/ScopingConventions.java        | 19 +++++++++++++++----
 1 file changed, 15 insertions(+), 4 deletions(-)
```
