# Preparation for task.md (missing sub-landscape config.json)

## What changed
All changes are in `reports/.../landscape/statichtml/LandscapeReportGenerator.java`. Behaviour is unchanged.

- I moved the logo cell of each Sub-landscapes row out of the large `forEach` in `addSubLandscapeSection` and
  into a new method, `addSubLandscapeLogoCell(subLandscape, labelText, href)`. This method calls
  `getSubLandscapeConfig`, reads its `Metadata` and renders the logo `<img>`. The dereference that throws the
  NullPointerException (`subLandscapeConfig.getMetadata()`) is now in this short method.
- I placed that method directly above `getSubLandscapeConfig`. Both now have Javadoc that uses the task's
  words: "Sub-landscapes tab", "logo", "sub-landscape's own config.json", "missing or cannot be read",
  "returns null", "default landscape logo".
- The default logo URL appeared twice; it is now one constant, `DEFAULT_LANDSCAPE_LOGO`, with the comment
  "the default landscape logo, shown when no logo is configured".

## Why this makes the later change cheaper
A grep for `config.json`, `logo` or `default landscape logo` lands on one 20-line window. That window has the
method that returns null and the code that dereferences it. The fix is a single null fallback in
`addSubLandscapeLogoCell`, for example `if (subLandscapeConfig == null) subLandscapeConfig = new LandscapeConfiguration();`.
The agent does not have to read the 130-line row-rendering lambda.

## Verification
- `./check.sh`: 354 tests passed, report output identical to the baseline (906 files), CHECK PASSED.
- `./accept.sh`: acceptance test FAILING (as it must).

## `git -C code show --stat HEAD`
```
commit 63f46c6843c3d9d25291d2f1454feb2dda7b67fa
Author: Zeljko Obrenovic <zeljkoobrenovic@yahoo.com>
Date:   Thu Oct 15 20:39:06 2026 +0200

    Preparation for the change in task.md

 .../statichtml/LandscapeReportGenerator.java       | 33 ++++++++++++++++------
 1 file changed, 24 insertions(+), 9 deletions(-)
```
