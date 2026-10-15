# Refactoring proposal: make future changes to Sokrates cheaper for a coding agent

## Basis

**History.** These figures come from `sokrates-reports/data/text/mainFilesWithHistory.txt` and the `temporal_dependencies*.txt` files. Changes concentrate in four areas:

| Area | Files (commits) | Last change |
|---|---|---|
| Report pages and their wiring | `LandscapeReportGenerator` (130), `ReportFileExporter` (94), `DataExporter` (68), `SummaryUtils` (60), `RichTextReport` (42), `BasicSourceCodeReportGenerator` (35) | 2025-04 to 05 |
| Contributors and git history | `LandscapeAnalysisResults` (67), `GitHistoryUtils` (29), `ContributorsReportGenerator` (36), `LandscapeContributorsReport` (27), `LandscapeIndividualContributorsReports` (24), `LandscapeAnalysisCommands` (24), `EmailTransformations`, `PeopleConfig`, `PersonConfig` | 2025-07 to 09; all of the last 90 days of activity |
| File-level reports | `FileSizeReportGenerator` (29), `FileChurnReportGenerator` (21), `FileAgeReportGenerator` (17) | FileSize and FileChurn changed together on 2025-09-20 (the last commit) |
| Configuration and CLI | `CommandLineInterface` (67), `ScopingConventions` (60), `LandscapeConfiguration` (54), `CodeConfiguration` (43), `AnalysisConfig` (33) | |

**Co-change.** The strongest co-change pairs (`temporal_dependencies.txt`) all cross file or module boundaries:
- `LandscapeContributorsReport`, `LandscapeIndividualContributorsReports` and `LandscapeReportGenerator` each change with `LandscapeAnalysisResults` (5 shared commits each).
- `PeopleConfig` changes with `EmailTransformations` (3/3).
- `FileSizeReportGenerator` changes with `FileChurnReportGenerator` (3).
- `ContributorConnectionUtils` (codeanalyzer) changes with `FileSize`/`FileChurnReportGenerator` (reports): 2 each.
- `LandscapeReportGenerator` changes with `ReportFileExporter` (3) and with `DuplicationReportGenerator` (3).

**Duplication.** `duplicates.txt` lists 1071 duplicated blocks.
- Most are in the ~40 language analyzers under `sourcecode/lang/`. That code has not changed since 2022 (`LanguageAnalyzerFactory`: last change 2022-03-20).
- The duplication in files that do change is in the landscape reports and in the file-level report generators.

**Selection rule.** I picked refactorings where the history says changes happen *and* a change currently needs several searches or several files:
- a literal or rule that appears in 4–11 places,
- logic copied between two classes that both change,
- feature words that a grep does not find.

Every refactoring keeps all existing public and package-private signatures; old members delegate to the new code. File:line references are relative to `code/`.

Abbreviations:

| Short name | File |
|---|---|
| `RFE` | `reports/.../reports/core/ReportFileExporter.java` |
| `LAR` | `codeanalyzer/.../sourcecode/landscape/analysis/LandscapeAnalysisResults.java` |
| `Gen` | `reports/.../landscape/statichtml/LandscapeReportGenerator.java` |
| `Tab` | `reports/.../landscape/statichtml/LandscapeReportContributorsTab.java` |

---

## R1: One catalogue of report pages (`ReportPages`)

**What.** Add `reports/.../reports/core/ReportPages.java`: an enum, one constant per HTML report page. Each constant holds:
- the file name (`"FileChangeFrequency.html"`),
- the page title (`"File Change Frequency"`),
- the menu title, which differs for some pages (`"Component Dependencies*"` vs `"Static Component Dependencies"`, `"Unit Size*"` vs `"Unit Size"`),
- the icon key (`"change"`),
- a Javadoc line with the words people use for the page ("churn", "change frequency", "file history", "age", "freshness").

Replace the literals with references to it:
- the 17 `new RichTextReport(title, file)` fields,
- the two lists in `RFE.getReportsList`: the visible entries and the greyed-out `{"", title, icon}` entries,
- the `href`s in `SummaryUtils.summarize*`,
- the links in `RFE.addVisuals` and in `LandscapeRepositoriesReport`.

`templates/Structure.html` is a template and keeps its literals. The enum Javadoc lists it as "also linked from".

Also merge the three copies of `getIconSvg` (`RFE:1030`, `BasicSourceCodeReportGenerator:54`, `SummaryUtils:43`) into one method. The `SummaryUtils` copy swaps width and height, so it must call the shared method with the arguments swapped, to keep output identical.

**Where.**
- `BasicSourceCodeReportGenerator` (24-40, `report()` 79-128, `createBasicReport()` 157-245)
- `RFE.getReportsList` (1114-1191) and `RFE.addVisuals` (367-792)
- `SummaryUtils` (58-127, 375-434, 599)
- `LandscapeRepositoriesReport` (858-878)

**Evidence.** Each page's file name and title are repeated as literals across files:

| Literal | Copies | Where |
|---|---|---|
| `"FileAge.html"` | 11 | `BasicSourceCodeReportGenerator:30`, `RFE:164-166,437,1133`, `SummaryUtils:376`, `Structure.html:238,263`, `LandscapeRepositoriesReport:874` |
| `"FileChangeFrequency.html"` | 8 | |
| `"FileSize.html"` | 7 | `BasicSourceCodeReportGenerator:29`, `RFE:388,1130`, `SummaryUtils:113`, `Structure.html:213`, `LandscapeRepositoriesReport:862` |
| `"Duplication.html"` | 6 | |

- **Titles:** `"File Size"` appears at `BasicSourceCodeReportGenerator:29` and `RFE:1130,1164`.
- **Visibility rule:** the rule for whether a page appears is written twice in `RFE.getReportsList`: once for the link and once for the greyed-out entry.
- **Vocabulary mismatch:** the generator is called `FileChurnReportGenerator`, but the page is `FileChangeFrequency.html`, the title is "File Change Frequency", and the icon key is `"change"`. A grep for "churn" does not find the page wiring, and a grep for "change frequency" does not find the generator.
- **Co-change:** the files involved change often and together. `RFE` (94 commits) changes with `Gen`, `ContributorsReportGenerator`, `DuplicationReportGenerator` and `LogicalComponentsReportGenerator` (2–3 shared commits each). `LogicalComponentsReportGenerator` changes with `BasicSourceCodeReportGenerator` and `CommandLineInterface`.

**Expected effect.** Covers "add a report page", "rename or retitle a page", "move a page in the menu" and "link to page X from Y". Today these need about 5 files: generator holder, `RFE` (twice), `SummaryUtils`, `LandscapeRepositoriesReport`. Each is found by a separate grep on a file name the task description does not contain. After the change, a grep for the page's words finds `ReportPages`. From there the agent follows references, and the file name lives in one constant. Commits that touched `RFE` or `BasicSourceCodeReportGenerator` (≈130 together) are the upper bound on how often this happened.

**Size and risk.** About 150 lines touched (≈60 new). Risk is low:
- The strings are copied verbatim, so the HTML output is identical.
- The one trap is the swapped width/height in `SummaryUtils.getIconSvg`, which must be kept.

---

## R2: One catalogue of data-export and visual file names (`ReportDataFiles`)

**What.** Add `reports/.../reports/dataexporters/ReportDataFiles.java`, holding constants (with Javadoc) for:
- every file `DataExporter` writes: `mainFilesWithHistory.txt`, `duplicates.txt`/`.json`, `unit_duplicates.txt`, `units.txt`, `contributors.txt`, `excluded_files_ignored_*.txt`, `temporal_dependencies*.txt`, `files.json`, `git-history.zip`, …;
- the visual files that the CLI and the generators both name, such as `zoomable_circles_main_<type>_coloring.html` and `files_3d.html`. These become static builder methods where the name is computed.

Wire them in:
- `DataExporter` (write side), `RFE.addData` (link side) and every generator that links a data file use the constants.
- `DataExporter:648,651` and `GitHistoryExtractor:36` use the existing `GitHistoryUtils.GIT_HISTORY_FILE_NAME` instead of re-typing it.
- `CommandLineInterface:638,670,684,277,281` uses the existing `Commands.ARG_HTML_REPORTS_FOLDER_NAME` and a new `DATA_FOLDER` constant for `"html"`/`"data"`.

**Where.**
- `DataExporter` (133-288, 371, 451, 587-656, 811)
- `RFE.addData` (794-897) and `RFE` visuals (315-319, 367-792)
- `FileChurnReportGenerator` (88, 99, 218, 227, 236), `FileAgeReportGenerator:97`, `FileSizeReportGenerator`
- `DuplicationReportGenerator` (140, 187, 481), `UnitsSizeReportGenerator:71`, `ConditionalComplexityReportGenerator:63`
- `ContributorsReportGenerator:184`, `CommitsReportGenerator:116`, `OverviewReportGenerator:215-216`, `FileTemporalDependenciesReportGenerator:90,101`
- `LandscapeAnalyzer:285-286`
- `CommandLineInterface` (748, 751, 852)

**Evidence.**
- `"mainFilesWithHistory.txt"` appears 8 times: 5 in `FileChurnReportGenerator` alone, plus `DataExporter:611`, `RFE:811`, `FileAgeReportGenerator:97`.
- `units.txt` and `contributors.txt` appear 4 times each; `duplicates.*` 6 times.
- Visual file names appear in 28 literal copies in `RFE` (11), `Structure.html` (5), FileChurn (4), FileAge (4) and FileSize (3). The CLI builds the same names by concatenation (`CommandLineInterface:748,751`), so a grep for the literal file name misses the producer.
- `DataExporter` has 68 commits and `RFE` 94.

**Expected effect.** Covers "export a new data file and link it" and "rename or move an export". The agent writes the file and adds the link using one constant, instead of searching producer and consumers separately. A grep for the file name now hits one definition instead of 4–8 places. It also removes a silent failure mode: renaming on one side leaves a dead link that no test catches, which costs the weaker model fix-up rounds.

**Size and risk.** About 120 lines touched. Risk is low: the string values are unchanged. The constants must keep exact case and the computed suffixes.

---

## R3: Remove the copied contributor charts between `LandscapeReportGenerator` and `LandscapeReportContributorsTab`

**What.** Move the copied private methods into one package-private helper, `reports/.../landscape/statichtml/LandscapeContributorsCharts.java`. It is constructed with `RichTextReport`, `LandscapeAnalysisResults`, the contributors list and `LandscapeReportContributorsTab.Type`. Methods that move:

| Method | `Tab` | `Gen` | Notes |
|---|---|---|---|
| `addContributorsPerYear(boolean)` | 741-828 | 1677-1764 | 88 lines; the only difference is `contributors.size()` vs `landscapeAnalysisResults.getContributors().size()`, which becomes a parameter |
| `addContributorDependencies` | 322-377 | 1121-1174 | Tab's ending `if (isContributorReport())` becomes a flag |
| `addContributorsPerExtension` | 266-316 | 1087-1119 | Tab's version is the general one |
| info-block helpers (`addInfoBlockWithColor`, `addPeopleInfoBlockWithColor`, `addWorkloadInfoBlockWithColor`, `addSmallInfoBlock`, `getExtraPeopleInfo`, …) | 650-739 | 1528-1652 | `addInfoBlockWithColor` also has a third copy in `LandscapeReportPeopleTopologyTab:241-257` |
| `getContributorsCountPerYear`, `populateTimeSlotMaps`, `updateTimeSlotMap` | 1105-1157 | 1766-1808 | third copy in `LandscapeReportPeopleTopologyTab:259-297` |

- The public static methods `Gen.getContributionDays/Weeks/Years` (558-593) and `Tab.getContribution*` (131-165) stay. Both become one-line delegates to a single implementation in the helper.
- The shared chart constants also move to the helper: `PEOPLE_COLOR` (defined in both `Gen:110` and `Tab:61`), `"#343434"`/`"#989898"`, the bar height `64.0`, the "show more" cutoff `25` and the dependency limit `100`.

**Where.** `Gen`, `Tab`, `LandscapeReportPeopleTopologyTab`.

**Evidence.**
- `duplicates.txt` blocks 4, 5 and 7 (38, 38 and 34 cleaned lines) are between `Tab` and `Gen`. In total about 300 lines are duplicated across the three files.
- `Gen` is the most-changed file in the codebase (130 commits, line churn 9359). `Gen`, `LandscapeContributorsReport` and `LandscapeIndividualContributorsReports` co-change with `LAR` (5 commits each). `LandscapeReportContributorsTab` co-changes with `LAR` 3 times out of its 4 commits.

**Expected effect.** Covers "change how contributors per year, per extension or contributor dependencies are shown". Today a grep for "contributors per year" returns two identical methods. The agent must read both to decide which one renders the page in the task. Usually it is both: the landscape overview page and the contributors tab. If it edits one, the other page stays wrong, which leads to a failed test and a second round. After the change there is one method to find and one edit.

**Size and risk.** About 450 lines touched, net about −300. The risk is medium: the moved code reads instance fields of both classes (`landscapeAnalysisResults`, `contributors`, `type`). Each difference between the copies must become an explicit parameter so that HTML output stays byte-identical. Check by regenerating a landscape report and diffing it.

---

## R4: One place that decides who a contributor is (`ContributorIdentity`)

**What.** Add `codeanalyzer/.../sourcecode/contributors/ContributorIdentity.java`, built from `LandscapeConfiguration` (or from `FileHistoryAnalysisConfig`) and `PeopleConfig`. It has these methods:
- `String resolve(String email)`: ignore-check → email transformation → people-config mapping → ignore-check again. This is the exact sequence at `LAR:789-793`.
- `boolean isIgnored(email)`
- `boolean isBot(email)`
- `Optional<PersonConfig> person(name)`
- `String team(email)`

Callers to switch:
- `LAR:650,656,662`: the same 5-line loop three times, for all, 30-day and 90-day committers. This becomes one private loop over `resolve`.
- `LAR:789-793` (`getAllContributors`).
- `ContributorPerExtensionHelper:35`. This call has odd semantics (see the appendix). It is converted with exactly the same semantics, plus a `// NOTE` comment.
- `LAR:576-578` and `LAR:615-617`: the same commit-count filter in `getContributors` and `getBots`.
- `LAR:606-608` (`isBot`).
- The email → team regex loop, which exists four times: `TeamsConfig:26`, `LAR:727`, `LandscapeContributorsReport:248-256` and `LandscapeReportPeopleTopologyTab:180-190`.
- The avatar lookup by person name, near-identical in `LandscapeContributorsReport:136-154` and `LandscapeIndividualContributorsReports:58-77`.

`GitHistoryUtils.parseLine` (97-152) applies a different variant at the single-repository level: ignore → anonymize or transform (`ComplexOperation`, without `PeopleConfig`) → ignore → bot. It keeps its behaviour. It gets a Javadoc cross-reference, and `ContributorIdentity` documents why the two differ.

`EmailTransformations.transformEmail`, `GitHistoryUtils.shouldIgnore` and `isBot` stay public and delegate to the new class, or the new class delegates to them. Behaviour, including the static cache, is unchanged.

The class Javadoc names the words users use: "alias", "merge emails", "same person", "bot", "ignore contributor", "anonymize", "team", "people config", "avatar".

**Where.**
- `LAR`, `EmailTransformations`, `PeopleConfig`, `PersonConfig`, `TeamsConfig`, `GitHistoryUtils`
- `ContributorPerExtensionHelper`, `LandscapeContributorsReport`, `LandscapeIndividualContributorsReports`, `LandscapeReportPeopleTopologyTab`, `LandscapeAnalysisCommands`

**Evidence.**
- This is where the most recent work happened. `EmailTransformations` (last change 2025-09-20), `GitHistoryUtils` (2025-09-20, 42 lines of churn in the last 30 days), `PeopleConfig`/`PersonConfig` (2025-07-20) and `Contributor` and `GitContributorsUtil` (2025-08-26) are the files changed in the last 90 days.
- `temporal_dependencies.txt` shows `PeopleConfig`↔`EmailTransformations` 3/3. It also shows `PersonConfig`, `PeopleConfig` and `EmailTransformations` each co-changing with `LandscapeAnalysisCommands`, `LandscapeContributorsReport` and `LandscapeIndividualContributorsReports` (2 commits each). So one feature ("map emails to people") was spread over 6–7 files in two modules.

**Expected effect.** Covers "treat X as the same person", "exclude bots or contributors matching Y from Z", "assign people to teams differently" and "add avatars or links for people". These are the most likely next changes, given the trend. Today the agent has to:
- find two different transformation mechanisms (`ComplexOperation` in `GitHistoryUtils`, `EmailTransformations` in landscape),
- read four team-matching loops,
- decide which of the three `LAR` loops applies.

After the change, a grep for "bot", "ignore", "team" or "transform email" lands on one class that holds the rule.

**Size and risk.** About 160 lines touched. The risk is medium:
- The order of ignore and transform calls, the static cache in `EmailTransformations`, and the cache mix-up in `PeopleConfig.getPersonByName` (see the appendix) must all be reproduced exactly.
- The existing `GitHistoryUtilsTest.shouldIgnore` keeps covering the delegate.
- Add a unit test for `resolve` that pins the current order.

---

## R5: Name the activity windows (30/90/180/365 days) once

**What.** Add `codeanalyzer/.../sourcecode/filehistory/ActivityWindows.java`. It holds:
- the constants `DAYS_30`, `DAYS_90`, `DAYS_180` and `DAYS_365`,
- the display labels used in reports ("past 30 days", "3 months", "6 months", "12 months window") and the `_30d` file-name suffix,
- a Javadoc that maps the words "recent", "active", "last month", "last quarter", "last year", "rookie" and "recently active" to a window.

Make the three existing 30-day constants alias the new one, keeping them public:
- `LAR.RECENT_THRESHOLD_DAYS` (`LAR:34`)
- `Gen.RECENT_THRESHOLD_DAYS` (`Gen:60`)
- `Contributor.RECENTLY_ACTIVITY_THRESHOLD_DAYS` (`Contributor.java:18`)

Replace the inline literals with the constants:
- `LAR:1058,1062`
- `LandscapeAnalyzer:155-168,212`
- `ContributorsAnalyzer:55-65`
- `FileHistoryAnalyzer:58-72,130-146`
- `ContributorsReportGenerator:136-178,193-195`
- `CommitsReportGenerator:90,99-101`
- `ContributorsReportUtils:146-173`
- `FileSizeReportGenerator:111-129`
- `FileTemporalDependenciesReportGenerator:43-66`
- `CommandLineInterface:694-704`
- `RFE:520-581`
- `LandscapeRepositoriesReport:797-798`
- `LandscapeReportPeopleTopologyTab:113-121,307`

In the same pass:
- Route the six raw `"yyyy-MM-dd"` literals (`DateUtils:219`, `Contributor:96,111`, `AuthorCommit:88`, `FileHistoryUtils:16`, `TrendAnalysisConfig:84`) through `DateUtils.DATE_FORMAT`.
- Replace the four copies of the first/latest-date min/max update (`Contributor:47-52`, `GitContributorsUtil:30-35`, `ContributorRepositories:24-29`, `LAR:769-774`) with one `DateUtils` helper.

The configurable thresholds in `threshold/Thresholds.java` stay where they are. The `ActivityWindows` Javadoc points to them, to explain the difference: fixed reporting windows versus configurable risk thresholds.

The ~40 public `...30Days/90Days/180Days` accessors in `LAR` (1070-1349) must keep their signatures. They get a short Javadoc block saying they are the per-window variants of one metric.

**Where.** About 15 files in `codeanalyzer` and `reports`, listed above.

**Evidence.**
- 30 days is defined as a constant three times under different names.
- The labels "(past 30/90/180 days)" and "(3 months)/(6 months)" are written separately in `Tab:192-196,656-657` and `Gen:863,1534-1535`.
- Identifiers containing `30Days`, `90Days`, `180Days` or `365Days` occur 290 times across 13 codeanalyzer files.
- The files involved are all in the contributor and history cluster that is changing now (see R4).

**Expected effect.** Covers "add a 60-day or 1-year view", "change what 'recent' means" and "rename a window label". Task descriptions say "recent" or "last month". The code says `30`, `RECENT_THRESHOLD_DAYS` or `RECENTLY_ACTIVITY_THRESHOLD_DAYS`, and a grep for `30` is useless. After the change, one search for the window word finds the defining class. From there, "find usages" of the constant lists every place, instead of 4–6 exploratory greps.

**Size and risk.** About 150 lines touched, mostly one-token replacements. Risk is low as long as only literals that mean a window are replaced: a `30` that is a pixel size or a list limit must stay a number. Tests that hard-code dates are unaffected.

---

## R6: One renderer for the five-bucket "risk distribution" sections of the file reports

**What.** Add `reports/.../generators/statichtml/FileRiskDistributionSection.java`. It renders the sections that `FileSizeReportGenerator`, `FileChurnReportGenerator` and `FileAgeReportGenerator` each write by hand:

| Section | FileSize | FileChurn | FileAge | Size |
|---|---|---|---|---|
| Five-bucket list plus pie chart (VeryHigh/High/Medium/Low/Negligible × count/value) | 146-173 | 104-131, 134-162 | 135-165, 174-199 | 5 copies of ~28 lines |
| Per-extension chart wrapper | 186-190 | 165-169 | 208-212 | |
| Per-logical-decomposition charts | 192-202 | 171-212 | 214-255 | ~60 lines |
| Top-N file tables | 204-220 | 214-239 (×3) | 257-287 (×4) | ~55 lines |
| "explore:" link rows | | | | ~25 lines |

The generators keep their public methods and call the section with:
- their thresholds (already in `threshold/Thresholds`),
- their labels,
- their file-name constants (from R2).

`SummaryUtils.summarizeFileSize`, `summarizeFileChangeHistory` and `summarizeFileUpdateFrequency` (104-127, 375-434) repeat the five-bucket pattern a fourth time. They use the same helper for the bucket list.

**Where.** The three generators and `SummaryUtils`.

**Evidence.**
- About 250–300 parallel lines across the three generators (~680 LOC in total).
- `FileSizeReportGenerator`↔`FileChurnReportGenerator` is the strongest same-module co-change pair after the landscape cluster (3 commits). It includes the very last commit (2025-09-20, 46 and 33 lines of churn).
- Both also co-change with `ContributorConnectionUtils` (2 each).
- FileSize has 29 commits, FileChurn 21 and FileAge 17.

**Expected effect.** Covers "change how the file risk distribution is shown or labelled", "add a column or link to the top-N lists" and "add per-component breakdowns". Today such a change is made three to five times, and a missed copy fails a test only on one page. After the change: one place, one edit. The co-change data shows such cross-generator edits really happen.

**Size and risk.** About 350 lines touched, net about −200. The risk is medium. The copies differ in small ways, which must become parameters:
- the unused `logicalDecompositionCounter`,
- different wording,
- the corrupt text `"lines of codeclsfd_ftr_w_mp_ins)"` at `FileSize:164`, which must be kept to keep the report identical.

Do this after R2, so that the shared code uses the file-name constants.

---

## R7: Make feature words findable: a short "what lives here" Javadoc on the 15 entry-point classes

**What.** Add class-level Javadoc. No code change. Each Javadoc:
- names the report page or feature in the words a user would use,
- lists the synonyms the code does *not* use,
- lists the 2–4 other places that must change with the class, according to the co-change data.

| Class | Words to add |
|---|---|
| `FileChurnReportGenerator` | "churn = change frequency = how often files change"; output `FileChangeFrequency.html` |
| `FileAgeReportGenerator` (`fileHistoryReport` field) | "age, freshness, last modified, stale files"; output `FileAge.html` |
| `FileTemporalDependenciesReportGenerator` | "files changed together, co-change, coupling" |
| `LogicalComponentsReportGenerator` | "logical decomposition = components = modules" |
| `ScopingConventions` | "test / generated / build / vendor / ignored file patterns"; the category → method index (`getOtherFiles` 65-215, build 217-265, generated 267-334, test 336-401, ignore 403-594); plus the note that a new category also needs `CustomScopingConventions:32-44`, `ScopeCreator:87-91` and `addConventions` 52-63 |
| `LandscapeReportGenerator`, `LAR`, `LandscapeAnalyzer` | "landscape = portfolio / multi-repository / organisation view" |
| `Contributor` and `GitContributorsUtil` | "contributor = author = committer = developer = person" |
| `CommandLineInterface` and `Commands` | "command → handler" table, with the note that `usageInfo()` (110-124) must list new commands; `extractFiles` is currently missing |
| `LanguageAnalyzerFactory` | "add a language: analyzer class + `put` lines here + `ExtensionGroupExtractor.knownSourceExtensions` (20-120) + optional icon in `DataImageUtils`" |
| `DataExporter` | points to `ReportDataFiles` (R2) |

**Where.** Class headers of the files above. Each gets 5–12 comment lines.

**Evidence.**
- The page and generator names disagree: churn vs change frequency, and the `fileHistoryReport` field vs `FileAge.html`.
- "landscape" is Sokrates jargon; a task will often say "across repositories".
- The scoping change for a new category touches 6 places that nothing links together.
- `ScopingConventions` (60 commits) and `LanguageAnalyzerFactory` (35) are frequent targets of "support X" requests.

**Expected effect.** Directly addresses the measured cost item "greps that miss because the code uses other words than the task description". The first grep for the user's word hits the class Javadoc, which also names the companion files. That saves both the repeated search and the follow-the-calls reading.

**Size and risk.** About 120 comment lines, no risk.

---

## R8: `CommandLineInterface`: one dispatch table and named default paths

**What.**
- Replace the `if/else equalsIgnoreCase` chain (`CommandLineInterface:98-128`) with a private `Map<String, Handler>`, built from the `Commands` name constants. The lookup must stay case-insensitive.
- Pull the parse-args-then-check-help prologue, repeated in 7 handlers (e.g. 138-146, 163-171, 286-294), into one private method.
- Move the default paths `"./_sokrates/config.json"` (507) and `"./_sokrates/reports"` (526) into `Commands` constants. The same paths are repeated in option help text at `Commands:83,86`.
- Move the CLI's own file-size colouring (`CommandLineInterface:855-867`) onto the existing `PathStringsToTreeStructure.getColor`, if a diff shows the buckets are identical. Otherwise leave it and add a comment.

**Where.** `cli/.../CommandLineInterface.java` and `Commands.java`.

**Evidence.**
- `CommandLineInterface` has 67 commits; it co-changes with `ContributorsReportGenerator`, `RFE` and `LogicalComponentsReportGenerator` (2 each).
- Two commands' option sets are already inconsistent with the parsing code. `updateConfig` reads `skipDuplication`, `skipCorrelations` and `enableDuplication` (419-429) and `srcRoot` (395), but these options are not registered (209-230). This is the kind of slip a "one table" layout prevents.

**Expected effect.** Covers "add a command or option to the CLI". The agent sees all commands and their handlers in one 20-line table next to the names. It does not have to read a 30-line chain plus a copy of the prologue to imitate.

**Size and risk.** About 100 lines touched. Risk is low:
- The case-insensitive matching must be preserved.
- The broken option registration must *not* be fixed in this step, because fixing it is a behaviour change.

---

## R9: Model → export-DTO mapping next to the DTO, with a field-parity test

**What.** Give each export DTO a static `from(model)` factory that holds the whole field copy:

| Export DTO | Mapping today |
|---|---|
| `UnitInfoExport` | `UnitListExporter:23-40` |
| `DuplicateFileBlockExportInfo` | `DuplicationExporter:38-45` |
| `DependencyAnchorExportInfo` | `getInstance` 19-30 |
| `ContributorRepositoryInfoExport` | its constructor 21-33 |

The existing mappers and constructors delegate to `from(model)`.

Add one test per pair. It compares the model's bean properties with the DTO's by reflection, with an explicit exclusion list for the fields that differ today (e.g. `SourceFile` → `FileExportInfo`). A field added to the model then fails a test that names the DTO.

**Where.** `reports/.../dataexporters/{units,duplication,dependencies}/*`, `reports/.../landscape/data/ContributorRepositoryInfoExport.java`, plus 4 new tests.

**Evidence.** `intercomponent_duplicates_1.txt` shows four model/DTO pairs copied between codeanalyzer and reports:

| Model | Export DTO | Duplicated lines |
|---|---|---|
| `UnitInfo` | `UnitInfoExport` | 32 + 8 + 8 |
| `ContributorRepositoryInfo` | `ContributorRepositoryInfoExport` | 32 |
| `DuplicatedFileBlock` | `DuplicateFileBlockExportInfo` | 20 |
| `DependencyAnchor` | `DependencyAnchorExportInfo` | 12 |

`DataExporter` has 68 commits.

**Expected effect.** Covers "include field X in the JSON export". Today the agent must find the DTO, the model and a mapper that lives in a third class. With the mapping in the DTO there are two files to open, and the test points to any place it missed. That removes the failed-test fix-up loop the weaker model hits.

**Size and risk.** About 140 lines, of which about 80 are tests. Risk is low: JSON field order is set by the DTO's field order, which does not change.

---

## R10: One base for the "keyword … end" heuristic unit extractors (Julia, Lua, ObjectPascal)

**What.** Extract `lang/EndKeywordHeuristicUnitsExtractor` (abstract) holding:
- the shared fields and constructor,
- the `extractUnits` loop,
- `updateUnit`, `endUnit` and `initUnit`,
- the comment cleaner,
- the parameter and McCabe skeleton.

The three extractors keep their public classes and override four hooks:
- `isFunctionStartLine`,
- `isUnitEnd`,
- the parameter separator,
- the McCabe keyword list.

Keep the current quirks exactly; they are bugs, but fixing them changes behaviour (see the appendix):
- ObjectPascal lower-cases the body and then counts `" AND"`.
- Julia passes the regex-like `" \\? "` to `countMatches`.

**Where.** `lang/julia/JuliaHeuristicUnitsExtractor`, `lang/lua/LuaHeuristicUnitsExtractor`, `lang/objectpascal/ObjectPascalHeuristicUnitsExtractor`.

**Evidence.** These are the largest duplicated blocks in the codebase: `duplicates.txt` blocks 1–3 have 52, 39 and 39 cleaned lines. However, these files have little history; the closest relative, `PythonHeuristicUnitsExtractor`, has had 3 commits.

**Expected effect.** Covers "fix unit or complexity detection for one of these languages" and "add another end-delimited language". Small expected frequency, so this is ranked last. It is included because it is the cleanest way to stop the agent from editing one of three copies and leaving the other two inconsistent.

**Size and risk.** About 250 lines touched, net about −150. Risk is low to medium; covered by the existing per-language tests in `codeanalyzer/src/test/.../lang`.

---

## Deliberately not proposed

- **Splitting `LandscapeReportGenerator` (1587 LOC), `LAR` (1133) or `RFE` (1062) by size.** Measured not to matter. R3 shrinks `Gen` only as a side effect of removing copies.
- **Rewriting the ~40 `*Analyzer` classes onto a common base**, which accounts for most of the 1071 duplicate blocks. That code has not changed since 2022, so the cost would be large and the expected benefit small. R7 instead documents the 3-place "add a language" recipe.
- **Collapsing the ~40 `...30Days/90Days/180Days` public accessors into a map.** That would break public signatures. R5 documents them instead.

## Appendix: defects found while reading

These were found while reading the code and are **not fixed** by any refactoring above, which must preserve them. They are worth separate, behaviour-changing tickets.

- `ContributorRepositories.java:33`: `setCommits365Days(getCommits180Days() + commits365Days)` should use `getCommits365Days()`.
- `PeopleConfig.getPersonByName` (49-65) reads `nameCache` but writes `cache`.
- `ContributorPerExtensionHelper:35`: the filter lambda ignores `stats`.
- `LanguageAnalyzerFactory`:
  - `"pp"` is mapped twice (251 ObjectPascal, 267 Puppet), so Puppet silently wins;
  - mixed-case keys such as `"JSON-tmLanguage"` and `"stTheme"` can never match a lower-cased lookup (634).
- `FileSizeReportGenerator:164` prints `"lines of codeclsfd_ftr_w_mp_ins)"`.
- `CommandLineInterface` `updateConfig` reads options it never registers (395, 419-429).
- `FileHistoryAnalyzer.summarize` uses `new Date()` (90) instead of the analysis-date override.

## Ranking by expected benefit per line changed

| Rank | Id | Title | Lines | Why here |
|---|---|---|---|---|
| 1 | R7 | Feature-word Javadoc on entry-point classes | ~120 (comments) | Hits the "grep misses" cost directly; zero risk; covers every area that changes |
| 2 | R1 | `ReportPages` catalogue | ~150 | 6–11 copies per page name; touches the two most-changed report files (`RFE` 94, `SummaryUtils` 60) |
| 3 | R4 | `ContributorIdentity` | ~160 | The area changing right now (all last-90-day commits); 6–7 files per change today |
| 4 | R2 | `ReportDataFiles` catalogue | ~120 | 4–8 copies per export name; producer and consumer found with one search |
| 5 | R5 | `ActivityWindows` | ~150 | Same hot cluster as R4; turns a `30` into a searchable word; mostly one-token edits |
| 6 | R3 | Merge contributor charts from `Gen` and `Tab` | ~450 (net −300) | Hottest file (130 commits); removes "edit one of two copies" failures; medium risk |
| 7 | R6 | `FileRiskDistributionSection` | ~350 (net −200) | Confirmed co-change incl. the latest commit; 3–5 copies per edit |
| 8 | R9 | DTO `from(model)` + parity tests | ~140 | Removes a 3-file hop and the silent "forgot the export" failure |
| 9 | R8 | CLI dispatch table and default paths | ~100 | 67 commits, but changes there are already mostly local |
| 10 | R10 | Shared end-keyword unit extractor | ~250 (net −150) | Largest duplicates, but low history |

**Suggested order.** R7 first, since it is free. Then R1 → R2 → R6, because R6 uses R2's constants. Then R4 → R5 → R3, because R3's helper uses R5's window constants. For each step, regenerate the reports for `code/` and a small landscape, and diff them against the baseline to prove the output is identical.
