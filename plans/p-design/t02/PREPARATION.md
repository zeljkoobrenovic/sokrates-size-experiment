# Preparation for task.md (bare per-extension entry names)

## What changed (behaviour-preserving)

All naming and keying of per-extension entries now goes through a few small, documented helpers in
`SourceCodeAspectUtils`. They sit right above `getAspectsPerExtensions`, so they all fit in one window:

- `perExtensionName(extension)`: the name of each per-extension entry/aspect. These names end up in
  `linesOfCodePerExtension` / `fileCountPerExtension` in `analysisResults.json`. Today it returns the decorated label.
  `getAspectsPerExtensions` now uses it instead of an inline `"  *." + extension`.
- `decoratedExtensionLabel(extension)`: the `"  *.java"` label. The indented lines of the textual summary use it.
- `extensionName(perExtensionName)`: strips the decoration to get the plain lower-cased extension.
  `AnalysisUtils` now uses it for the metric ids (`LINES_OF_CODE_MAIN_EXT_JAVA`, …) instead of an ad hoc
  `.replace("*.", "")`. `getMetricId` produces the same ids as before.
- `landscapeAggregationKey(perExtensionName)`: the key under which a landscape merges per-extension LOC entries. Today
  it returns `toLowerCase()`. Two places now use it: `LandscapeAnalysisResults.getLinesOfCodePerExtension` (main/test)
  and `merge(..)` (the "other" category: build and deployment, generated, other). Before, the first lower-cased the
  name and the second used it as is. Real analysis names are always lower-case, so the output is the same.
  `check.sh` confirms this: all 354 tests pass and all 906 report files are identical.

## Why this makes the later change cheaper

The task's words (`linesOfCodePerExtension`, `fileCountPerExtension`, `"  *.java"`, `LINES_OF_CODE_MAIN_EXT_JAVA`,
"merge", "other" category, landscape) now appear in comments right where the change goes. The whole production change
then becomes two one-line edits in one file, `SourceCodeAspectUtils`:
1. `perExtensionName` returns `extension`. The metric ids are unaffected because they go through `extensionName`, and
   the summary lines keep `decoratedExtensionLabel`.
2. `landscapeAggregationKey` returns `extensionName(perExtensionName)`. This covers both the main and the "other"
   aggregation.

After that, only the two existing tests listed in the task need their names updated.

## Commit

```
commit b72f112b893897bb382b6083c4d10f0573f8c54b
    Preparation for the change in task.md

 .../sourcecode/analysis/AnalysisUtils.java         |  5 ++--
 .../sourcecode/aspects/SourceCodeAspectUtils.java  | 27 +++++++++++++++++++++-
 .../analysis/LandscapeAnalysisResults.java         | 12 ++++++----
 3 files changed, 37 insertions(+), 7 deletions(-)
```

`./check.sh`: CHECK PASSED. `./accept.sh`: acceptance test FAILING (as required).
