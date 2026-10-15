# Preparation

## What changed

In `reports/.../dataexporters/DataExporter.java`, the inline `new String[][]{...}` that built the
`all_files.zip` entries in `exportJson()` was pulled out into a new method,
`static String[][] aspectFileLists(CodeConfiguration configuration, File textDataFolder)`. The call site is now
one line: `ZipUtils.stringToZipFile(new File(zipFolder, "all_files.zip"), aspectFileLists(codeConfiguration, textDataFolder));`

The new method loops over a list of the five literal file names (`aspect_main.txt` … `aspect_other.txt`, same
order as before) and reads each one from the text data folder. Its javadoc uses the task's words ("all_files.zip",
"scope file lists", "main, test, generated, build and deployment, other", "configured aspect names"). It also
points to the writer (`saveSourceCodeAspect`), to the naming function
`DataExportUtils.getAspectFileListFileName(aspect, "")`, and to the `CodeConfiguration` getters for the five scope
aspects.

Behaviour is unchanged. The literals, the order and the enclosing try/catch are all as they were.
`./check.sh` prints CHECK PASSED (354 tests, report output identical to the baseline), and `./accept.sh` still
reports FAILING.

## Why this makes the later change cheaper

- Searching for `all_files.zip`, `aspect_main` or `aspect_build_and_deployment` lands on the call site and on the
  new method, which sit next to each other in one file, so a single read window covers both.
- The fix happens in one place: replace the literal-name list with the five configured aspects
  (`configuration.getMain()`, …) and use `DataExportUtils.getAspectFileListFileName(aspect, "")` as the file name.
  The configuration is already passed in, so the signature and the call site don't change.
- The javadoc names the naming function and the getters, so the agent doesn't need to search for how the loose
  files get their names.

## `git -C code show --stat HEAD`

```
commit 6060a7e3867be170d2183bd8759a3f5ada0f56ff
Author: Zeljko Obrenovic <zeljkoobrenovic@yahoo.com>
Date:   Thu Oct 15 20:45:49 2026 +0200

    Preparation for the change in task.md

 .../reports/dataexporters/DataExporter.java        | 31 +++++++++++++++++-----
 1 file changed, 24 insertions(+), 7 deletions(-)
```
