# Implementation log

## R7: Make feature words findable: a short "what lives here" Javadoc on the 15 entry-point classes

**Status:** done (commit `17681e7`). `./check.sh` (full, with tests): 354 tests passed, report output identical
to the baseline (906 files), `CHECK PASSED`.

**What I did.** I added class-level Javadoc only. There are no code changes. Each Javadoc names the page or
feature in the words a user would use, lists synonyms the code does not use, and names the classes that change
with it. I added one block to each of the 14 classes in the proposal's table:

- `reports/.../generators/statichtml/FileChurnReportGenerator`: churn = change frequency; output `FileChangeFrequency.html`.
- `reports/.../generators/statichtml/FileAgeReportGenerator`: age, freshness, last modified, stale; output `FileAge.html`;
  notes that the page lives in the `fileHistoryReport` field of `BasicSourceCodeReportGenerator`.
- `reports/.../generators/statichtml/FileTemporalDependenciesReportGenerator`: files changed together, co-change, coupling.
- `reports/.../generators/statichtml/LogicalComponentsReportGenerator`: logical decomposition = components = modules;
  outputs `Components.html` / `ComponentsAndDependencies.html`.
- `codeanalyzer/.../scoping/ScopingConventions`: test / generated / build / vendor / ignored file patterns, a
  category → method index, and the companion places a new category needs.
- `reports/.../landscape/statichtml/LandscapeReportGenerator`, `codeanalyzer/.../landscape/analysis/LandscapeAnalysisResults`,
  `codeanalyzer/.../landscape/analysis/LandscapeAnalyzer`: landscape = portfolio / multi-repository / organisation view.
- `codeanalyzer/.../contributors/Contributor`, `codeanalyzer/.../contributors/GitContributorsUtil`:
  contributor = author = committer = developer = person.
- `cli/.../CommandLineInterface`: the command → handler table. `cli/.../Commands`: a new command must also go in
  `usageInfo()`, which currently leaves out `extractFiles`.
- `codeanalyzer/.../lang/LanguageAnalyzerFactory`: the add-a-language steps (analyzer class, `put` lines,
  `ExtensionGroupExtractor.knownSourceExtensions`, optional icon in `DataImageUtils`).
- `reports/.../dataexporters/DataExporter`: the output folders and file names.

**Differences from the proposal.**
- **Method names instead of line numbers.** The Javadoc points to methods, not line ranges, because line numbers
  go stale (adding the Javadoc itself shifts them):
  - in `ScopingConventions`, the category index names `addOtherConventions`, `addBuildAndDeploymentConventions`,
    `addGeneratedConventions`, `addTestConventions`, `addIgnoreConventions` and `addConventions`. The proposal's
    "`getOtherFiles` 65-215" is actually the private `addOtherConventions()`;
  - the new-category note names `CustomScopingConventions` fields and `ScopeCreator.expandScopeWithCustomConventions`;
  - the CLI notes name `Commands.usageInfo()` and `CommandLineInterface.run`.
- **`DataExporter` does not point to `ReportDataFiles`.** That class comes from R2, which is not implemented yet.
  The Javadoc lists the output folders and example file names instead. It also says that the same literal names
  are read elsewhere, so a search for the file name finds both the producer and the consumer. Once R2 is done,
  this Javadoc should point to `ReportDataFiles`.
- **14 classes, not 15.** The table names 14 classes; the "15" in the title is not matched by any additional
  class in the section.
- The appendix defects (for example the mixed-case keys in `LanguageAnalyzerFactory` and the missing `extractFiles`
  in `usageInfo()`) are only described in the comments, not fixed.

```
 .../nl/obren/sokrates/cli/CommandLineInterface.java     | 15 +++++++++++++++
 cli/src/main/java/nl/obren/sokrates/cli/Commands.java   | 10 ++++++++++
 .../sokrates/sourcecode/contributors/Contributor.java   |  9 +++++++++
 .../sourcecode/contributors/GitContributorsUtil.java    |  9 +++++++++
 .../landscape/analysis/LandscapeAnalysisResults.java    | 11 +++++++++++
 .../landscape/analysis/LandscapeAnalyzer.java           | 11 +++++++++++
 .../sourcecode/lang/LanguageAnalyzerFactory.java        | 11 +++++++++++
 .../sokrates/sourcecode/scoping/ScopingConventions.java | 17 +++++++++++++++++
 .../sokrates/reports/dataexporters/DataExporter.java    | 11 +++++++++++
 .../generators/statichtml/FileAgeReportGenerator.java   | 11 +++++++++++
 .../generators/statichtml/FileChurnReportGenerator.java | 11 +++++++++++
 .../FileTemporalDependenciesReportGenerator.java        | 10 ++++++++++
 .../statichtml/LogicalComponentsReportGenerator.java    | 11 +++++++++++
 .../landscape/statichtml/LandscapeReportGenerator.java  | 10 ++++++++++
 14 files changed, 157 insertions(+)
```

## R1: One catalogue of report pages (`ReportPages`)

**Status:** done (commit `4999b6e`). `./check.sh` (full, with tests): 354 tests passed, report output identical
to the baseline (906 files), `CHECK PASSED`.

**What I did.**
- **New enum `reports/.../reports/core/ReportPages`.** It has 17 constants, one per page: `SOURCE_CODE_OVERVIEW`,
  `COMPONENTS`, `COMPONENT_DEPENDENCIES`, `TEMPORAL_DEPENDENCIES`, `DUPLICATION`, `FILE_SIZE`, `FILE_AGE`,
  `FILE_CHANGE_FREQUENCY`, `COMMITS`, `CONTRIBUTORS`, `UNIT_SIZE`, `CONDITIONAL_COMPLEXITY`, `FEATURES_OF_INTEREST`,
  `METRICS`, `TREND`, `CONTROLS`, `NOTES`.
  - Each constant holds `fileName`, `title`, `menuTitle` and `icon`, plus a Javadoc line with the words people use for
    the page (for example "churn" and "hot spots" on `FILE_CHANGE_FREQUENCY`) and the name of the generator class.
  - The menu titles that differ from the page title are kept as they are: `"Component Dependencies*"` vs
    `"Static Component Dependencies"`, `"Unit Size*"`, `"Conditional Complexity*"`, and `"All Metrics"` vs `"Metrics"`.
  - `newReport()` creates the `RichTextReport`.
  - The enum Javadoc lists its users, and lists `templates/Structure.html` and the in-text generator links as
    "also linked from".
- **`BasicSourceCodeReportGenerator`.** The 17 `new RichTextReport(title, file)` fields now call
  `ReportPages.X.newReport()`. Its private `getIconSvg` copy was never called, so I removed it.
- **`ReportFileExporter.getReportsList`.** Both lists now use the new private helpers `menuEntry(page)` →
  `{file, menuTitle, icon}` and `greyedOutEntry(page)` → `{"", title, icon}`.
  - The conditions and the order of the entries are unchanged.
  - Two odd entries are kept as they are: the `!mainExists` entry `"Components and Dependencies"/"Components"` with
    the `dependencies` icon, and the `!showDependencies` entry, which keeps the file name (so it is not greyed out).
    Both are written out explicitly.
- **Links in `ReportFileExporter`.** The links in `addVisuals`, the overview info blocks (`SourceCodeOverview.html`,
  `FileAge.html`) and the `Commits.html` / `Contributors.html` activity links now use `ReportPages.X.getFileName()`.
- **Other links.** The same goes for the 12 `href`s in `SummaryUtils` and the 8 `get…ReportUrl` methods in
  `LandscapeRepositoriesReport`.
- **`getIconSvg`.** The real implementation is now the new package-private
  `ReportFileExporter.getIconSvg(icon, heightPx, widthPx)`.
  - `ReportFileExporter.getIconSvg(icon, size)` delegates to it with `(size, size)`.
  - `SummaryUtils.getIconSvg(icon, w, h)` delegates to it with `(w, h)`, so that `w` still sets the height and `h`
    still sets the width. This keeps the swap, and a comment says so.

**Differences from the proposal.**
- **Two `getIconSvg` copies are not merged.** `ContributorsReportUtils.getIconSvg(icon, size)` and
  `AnimalIcons.getIconSvg` are not among the three copies the proposal names. To stay inside the section, they were
  left as they are.
- **Other literal file names are kept.** Some in-text links in page generators keep their literals, for example
  `MetricsListReportGenerator` → `Controls.html`, `ControlsReportGenerator` → `Metrics.html` and
  `LogicalComponentsReportGenerator` → `FileTemporalDependencies.html`. The proposal does not list them, and the
  enum Javadoc names them as "also linked from".
- **No new tests.** The proposal does not ask for any. The fixture check covers the generated pages and the index
  menu.

```
 .../sokrates/reports/core/ReportFileExporter.java  | 127 ++++++++++++---------
 .../obren/sokrates/reports/core/ReportPages.java   |  93 +++++++++++++++
 .../obren/sokrates/reports/core/SummaryUtils.java  |  31 +++--
 .../statichtml/BasicSourceCodeReportGenerator.java |  43 +++----
 .../repositories/LandscapeRepositoriesReport.java  |  17 +--
 5 files changed, 206 insertions(+), 105 deletions(-)
```

## R2: One catalogue of data-export and visual file names (`ReportDataFiles`)

**Status:** done (commit `4dd4c7d`). `./check.sh` (full, with tests): 354 tests passed, report output identical
to the baseline (906 files), `CHECK PASSED`.

**What I did.**
- **New class `reports/.../reports/dataexporters/ReportDataFiles`.** It has no instances and only static members,
  each with Javadoc:
  - folders: `DATA_FOLDER` (`"data"`), `TEXT_FOLDER`, `ZIPS_FOLDER`, `EXTRA_ANALYSIS_FOLDER`, `VISUALS_FOLDER`;
  - every `data/text/` file `DataExporter` writes: `METRICS_TXT`, `CONTROLS_TXT`, `CONTRIBUTORS_TXT`,
    `DUPLICATES_TXT`, `UNIT_DUPLICATES_TXT`, `UNITS_TXT`, `MAIN_FILES[_WITH|_WITHOUT]_HISTORY_TXT`,
    `EXCLUDED_FILES_IGNORED_{EXTENSIONS,RULES}_TXT`, the four `TEMPORAL_DEPENDENCIES_*_TXT` and the seven
    `METRICS_TREND_*_TXT`;
  - every `data/` JSON file (`ANALYSIS_RESULTS_JSON`, `CONFIG_JSON`, `FILES_JSON`, `DUPLICATES_JSON`, …), the
    history `ANALYSIS_RESULTS_ZIP`, `ALL_FILES_ZIP` and `GIT_HISTORY_ZIP`, and the four `extra_analysis` configs;
  - visuals: `FILES_3D_HTML`, `UNITS_3D_SIZE_HTML`, `UNITS_3D_COMPLEXITY_HTML`, and the coloring types `COLORING_LOC`,
    `COLORING_AGE`, `COLORING_FRESHNESS`, `COLORING_UPDATE_FREQUENCY` and `COLORING_CONTRIBUTORS_COUNT`;
  - builders for computed visual names: `zoomableCircles(suffix)`, `zoomableSunburst(suffix)`,
    `zoomableCirclesMainColoring(type)`, `zoomableCirclesMainColoringCategories(type)`, and
    `zoomable{Circles,Sunburst}{Commits,Contributors}(daysAgo, nameSuffix)`. The last four take over the
    suffix rules from the CLI unchanged: the all-time commits file has no suffix, the all-time contributors file
    keeps `_<nameSuffix>`;
  - link helpers for pages in `html/`: `dataLink` (`../data/…`), `textDataLink` (`../data/text/…`), `zipLink`
    (`../data/zips/…`) and `visualLink` (`visuals/…`).
- **Write side.** `DataExporter` now uses the constants for every file and folder listed above.
  - `DATA_FOLDER_NAME` keeps its signature and is now `= ReportDataFiles.DATA_FOLDER`.
  - The private `exportDuplicates(list, fileName)` now gets the full file name (`DUPLICATES_TXT`) instead of
    `"duplicates"` + `".txt"`.
  - `git-history.txt` (the input file and the entry name inside the zip) now uses `GitHistoryUtils.GIT_HISTORY_FILE_NAME`.
  - The class Javadoc (from R7) now points to `ReportDataFiles`.
- **Link side.**
  - `ReportFileExporter`: the configuration link in the index footer, the prompt links (`git-history.zip`,
    `files.json`), all links in `addData`, the 11 zoomable-circle / `files_3d` links and the 2 `units_3d` links in
    the visuals tab, the per-scope Circles/Sunburst links, and the `visuals` export folder.
  - Generators: `FileChurn` (5× `mainFilesWithHistory.txt`, 4 visuals), `FileAge`, `FileSize`, `Duplication`
    (`duplicates.txt`, `unit_duplicates.txt`), `UnitsSize`, `ConditionalComplexity`, `Contributors`, `Commits`
    (`contributors.txt` and the zoomable circles per period), `Overview` (excluded files),
    `FileTemporalDependencies` and `Trend` (the seven `metrics_trend_*.txt`).
- **CLI.**
  - `CommandLineInterface`: the `"data"` folder uses `ReportDataFiles.DATA_FOLDER`, and the `"html"` folder uses
    `Commands.ARG_HTML_REPORTS_FOLDER_NAME`. `"html/visuals"` is now `ARG_HTML_REPORTS_FOLDER_NAME + "/" + VISUALS_FOLDER`.
  - The visual file names now come from the builders and constants above, and the coloring types are passed as
    `COLORING_*` constants. The private `suffix` locals were moved into the builders.
  - `GitHistoryExtractor` now uses `GitHistoryUtils.GIT_HISTORY_FILE_NAME`.

**Differences from the proposal.**
- **`LandscapeAnalyzer:285-286` is not changed.** It is in `codeanalyzer`, and that module does not depend on
  `reports`, so it cannot see `ReportDataFiles`. Moving the constants down to `codeanalyzer`, or adding a module
  dependency, would widen the refactoring or change the build. `ReportDataFiles` names it in its Javadoc as a
  remaining literal reader of `excluded_files_ignored_*.txt`.
- **`templates/Structure.html`** is a static HTML resource, so it keeps its literal visual names. They are named in
  the Javadoc.
- **Visual names the CLI does not produce are left as literals.** These are the people-dependency,
  file-changed-together, duplication-graph, racing-chart, bubble-chart and tree-map links in `RFE` and the generators.
  Their producers are other generators that build the names from graph ids. The proposal's evidence (11 literal
  copies in `RFE`) counts only the zoomable-circle and `files_3d` links, which are done. `units_3d_*` was added
  because the CLI produces those files too.
- **Computed per-aspect names are left as they are.** These are `aspect_*.txt` (from `DataExportUtils`), the
  `dependencies_*.txt` prefix, the concern files and the `aspect_*.txt` entries in `all_files.zip`. They already
  have one builder each, or they are written in only one place.
- **Out of the section's scope:** the landscape exports (`LandscapeDataExport`, landscape tabs: `data/contributors.txt`
  etc.). That is a different exporter with its own folder.
- **One file beyond the "Where" list.** `TrendReportGenerator` links the seven `metrics_trend_*.txt` files that
  `DataExporter` writes. The section says "every generator that links a data file", so it is included.
- **No new tests.** The proposal does not ask for any. The fixture check compares all 906 generated files, which
  covers every name that was changed.

```
 .../obren/sokrates/cli/CommandLineInterface.java   |  45 +++--
 .../sokrates/cli/git/GitHistoryExtractor.java      |   3 +-
 .../sokrates/reports/core/ReportFileExporter.java  |  75 ++++----
 .../reports/dataexporters/DataExporter.java        | 114 +++++------
 .../reports/dataexporters/ReportDataFiles.java     | 214 +++++++++++++++++++++
 .../statichtml/CommitsReportGenerator.java         |  11 +-
 .../ConditionalComplexityReportGenerator.java      |   5 +-
 .../statichtml/ContributorsReportGenerator.java    |  13 +-
 .../statichtml/DuplicationReportGenerator.java     |   7 +-
 .../statichtml/FileAgeReportGenerator.java         |  13 +-
 .../statichtml/FileChurnReportGenerator.java       |  19 +-
 .../statichtml/FileSizeReportGenerator.java        |  11 +-
 .../FileTemporalDependenciesReportGenerator.java   |   5 +-
 .../statichtml/OverviewReportGenerator.java        |   5 +-
 .../statichtml/TrendReportGenerator.java           |  15 +-
 .../statichtml/UnitsSizeReportGenerator.java       |   5 +-
 16 files changed, 393 insertions(+), 167 deletions(-)
```

## R6: One renderer for the five-bucket "risk distribution" sections of the file reports

**Status:** done (commit `7da30f3`). `./check.sh` (full, with tests): 354 tests passed, report output identical
to the baseline (906 files), `CHECK PASSED`.

**What I did.**
- New class `reports/.../generators/statichtml/FileRiskDistributionSection`. It is built with the report, the
  threshold labels and the palette, and renders:
  - `addBucketList(totalLine, distribution, description[, valueSuffix])`: the total line, the five bucket lines
    (`count` + wording + ` (` + `value` + ` lines of code)`) and the overall chart. The nested enum `Bucket`
    (VERY_HIGH … NEGLIGIBLE) reads the count, value and threshold label of each bucket. The wording is a
    `BucketText` lambda `(bucket, count) -> text`;
  - `addExploreLinks(links...)` (joins with `" | "`) and `addExploreFragments(fragments...)` (verbatim), plus `link(href, text)`;
  - `addChartPerKeySection` / `addChartPerKeySubSection` (the per-extension chart wrapper);
  - `addChartPerComponent(title, subtitle, distributions, divStyle)` (per-logical-decomposition charts;
    `SCROLLING_DIV_STYLE` equals `startScrollingDiv()`, `COMPACT_DIV_STYLE` is the age page's 300px div);
  - `matchingDecomposition(...)` (the "config decomposition → result with the same name, ignoring case" filter);
  - `addFilesTable(title, subtitle, paragraph, files, ...[, maxHeight])` (the "Title (Top N)" file tables).
- `FileSizeReportGenerator`, `FileChurnReportGenerator`, `FileAgeReportGenerator` now call the section for all
  five-bucket lists (5 copies), explore rows, per-extension charts, per-decomposition charts and the 10 top-N
  tables. Public constructors, methods and constants are unchanged. The generators pass their thresholds,
  labels, palettes, wording and the `ReportDataFiles` (R2) constants.

**Differences from the proposal / left out.**
- **`SummaryUtils` is not changed.** `summarizeFileSize`, `summarizeFileChangeHistory` and
  `summarizeFileUpdateFrequency` do not list the five buckets. They print one or two percentages next to a
  risk-profile bar. The bucket-list helper does not fit them, so no copy was there to remove.
- **Quirks kept as parameters to keep the reports identical:**
  - the corrupt `" lines of codeclsfd_ftr_w_mp_ins)"` suffix of the medium bucket on the File Size page
    (an appendix defect), through the `valueSuffix` lambda;
  - the double space before "files" in the freshness "high" line;
  - the singular/plural wording and the "more than N" very-high line on the churn page;
  - the File Size explore row, where `sunburst</a> | ` was one HTML fragment. Fragments are rendered on separate
    lines, so splitting it changed `FileSize.html`. That page uses `addExploreFragments` with the merged fragment.
- **The unused `logicalDecompositionCounter`** in the churn and age generators was dropped rather than made a
  parameter. It was incremented and never read, so the output does not depend on it.
- No tests were added. The proposal does not ask for any, and `check.sh` compares the three pages byte for byte.

```
 .../statichtml/FileAgeReportGenerator.java         | 148 +++++---------
 .../statichtml/FileChurnReportGenerator.java       | 156 ++++++---------
 .../statichtml/FileRiskDistributionSection.java    | 213 +++++++++++++++++++++
 .../statichtml/FileSizeReportGenerator.java        | 105 +++++-----
 4 files changed, 362 insertions(+), 260 deletions(-)
```

## R4: One place that decides who a contributor is (`ContributorIdentity`)

**Status:** done (commit `0839089`). `./check.sh` (full, with tests): 360 tests passed (354 + 6 new), report
output identical to the baseline (906 files), `CHECK PASSED`.

**What I did.**
- **New class `codeanalyzer/.../sourcecode/contributors/ContributorIdentity`.** It is built from
  `LandscapeConfiguration`, `PeopleConfig` (may be null) and `TeamsConfig`. It keeps references and reads the
  configuration on every call, so later `setConfiguration`/`setPeopleConfig` calls are still seen.
  - Instance methods: `resolve(email)` (ignore-check → transform → ignore-check; `null` when ignored), `transform(email)`
    (transformation only), `isIgnored(email)`, `isBot(email)`, `person(name)` → `Optional<PersonConfig>`,
    `team(email)` and `teamConfig(email)`.
  - Static rules, which the old entry points now delegate to: `transformEmail(email, ops, peopleConfig)` (the body
    and the static cache moved here from `EmailTransformations`), `isIgnored(email, patterns)`, `isBot(email, bots)`,
    `team(email, teams)` / `teamConfig(email, teams)`, and `personImage(personConfig)`.
  - The class Javadoc uses the words users use (alias, merge emails, same person, bot, ignore contributor,
    anonymize, team, people config, avatar). It explains how the single-repository variant in
    `GitHistoryUtils.parseLine` differs and why.
- **Delegates (signatures unchanged):** `EmailTransformations.transformEmail`, `GitHistoryUtils.shouldIgnore`,
  `GitHistoryUtils.isBot` and `TeamsConfig.getTeam`. `GitHistoryUtils.parseLine` keeps its behaviour and gets a
  Javadoc cross-reference.
- **`LandscapeAnalysisResults`:**
  - new `@JsonIgnore public getContributorIdentity()`;
  - `getAllContributors` uses `resolve`;
  - the three committer loops in `getContributorsPerExtension` are now one private `addTransformedCommitters`;
  - the commit-count filter shared by `getContributors` and `getBots` is the private
    `getAllContributorsAboveCommitThreshold()`;
  - `isBot` delegates to the identity;
  - the team loop in `getAllTeams` uses `identity.teamConfig(email)`.
- **Reports:**
  - `LandscapeContributorsReport`: the private `getTeam` loop is replaced by `identity.team`. The new package-private
    static `getAvatarUrl(personConfig, contributorId, linkTemplate)` holds the "configured image, otherwise avatar
    template" lookup, and both `addContributor` and `LandscapeIndividualContributorsReports` use it with
    `identity.person(...)`.
  - `LandscapeReportPeopleTopologyTab.getTeamOf` uses `ContributorIdentity.teamConfig` on its own `teamsConfig`.
  - `ContributorPerExtensionHelper` uses `identity.transform` with exactly the same (odd) filter semantics, plus a
    `// NOTE` comment.
- **New test `ContributorIdentityTest`.** It pins the `resolve` order (ignored before the transformation, ignored
  after it, people mapping after the transformation), shows that `transform` skips the ignore-check, and covers
  bots, teams, `person` and `personImage`.

**Differences from the proposal.**
- **The three `LAR` committer loops do not use `resolve`.** The original loops only transform; they never apply the
  ignore-checks. Going through `resolve` would drop ignored committers and change the per-extension counts, so the
  shared loop uses `transform`, and a comment says why.
- **`getAllTeams` uses `teamConfig(email)` (which returns the `TeamConfig`), not `team(email)` (which returns the name).**
  This keeps the behaviour exact even if a matching team had a null name. The original "already in remainder" check
  (`getMembers().contains(email)`, a list of `ContributorRepositories` checked against a `String`) is kept as it is.
- **`PeopleConfig`, `PersonConfig`: not changed.** The `getPersonByName` cache mix-up (appendix) is preserved: the
  identity calls `getPersonByName` exactly where and as often as before. The `getPersonFromEmailPatterns` cache is
  untouched.
- **`LandscapeAnalysisCommands`: not changed.** It contains no identity rule; it only wires the analysis and the
  report generation.
- **`FileHistoryAnalysisConfig` is not a constructor source.** The single-repository path keeps its own variant (as
  the proposal says), so it has no caller. It shares only the static `isIgnored`/`isBot` rules, through
  `GitHistoryUtils`.
- **`CommitsPerExtension:104` still calls `TeamsConfig.getTeam`.** It is a caller, not a copy, and it now reaches
  `ContributorIdentity` through the delegate.

```
 .../contributors/ContributorIdentity.java          | 168 +++++++++++++++++++++
 .../sourcecode/githistory/GitHistoryUtils.java     |  17 ++-
 .../sokrates/sourcecode/landscape/TeamsConfig.java |  12 +-
 .../analysis/LandscapeAnalysisResults.java         | 113 +++++++-------
 .../landscape/utils/EmailTransformations.java      |  28 +---
 .../contributors/ContributorIdentityTest.java      |  82 ++++++++++
 .../statichtml/LandscapeContributorsReport.java    |  31 ++--
 .../LandscapeIndividualContributorsReports.java    |   9 +-
 .../LandscapeReportPeopleTopologyTab.java          |  11 +-
 .../utils/ContributorPerExtensionHelper.java       |   8 +-
 10 files changed, 347 insertions(+), 132 deletions(-)
```

## R5: Name the activity windows (30/90/180/365 days) once

**Status:** done (commit `9635987`). `./check.sh` (full, with tests): 360 tests passed, report output identical
to the baseline (906 files), `CHECK PASSED`.

**What I did.**
- **New class `codeanalyzer/.../sourcecode/filehistory/ActivityWindows`.** It has no instances and holds:
  - `DAYS_30`, `DAYS_90`, `DAYS_180`, `DAYS_365`;
  - the report labels `PAST_30_DAYS`, `PAST_90_DAYS`, `PAST_180_DAYS` ("past N days"), `LABEL_3_MONTHS`,
    `LABEL_6_MONTHS` and `LABEL_12_MONTHS_WINDOW`, plus the file-name suffix `SUFFIX_30D` (`"_30d"`) and a
    `pastDays(int)` builder;
  - a Javadoc that maps "recent", "recently active", "last month", "last quarter", "active", "last year" and
    "rookie" to a window. It explains the `...30Days/90Days/180Days/365Days` naming of the per-window
    metrics, and says how these fixed windows differ from the configurable `threshold/Thresholds`.
- **Aliases (still public, still compile-time constants):**
  - `LAR.RECENT_THRESHOLD_DAYS`, `Gen.RECENT_THRESHOLD_DAYS` and `Contributor.RECENTLY_ACTIVITY_THRESHOLD_DAYS`
    are now `= ActivityWindows.DAYS_30`;
  - `Contributor.ACTIVITY_THRESHOLD_DAYS` (180) and `ROOKIE_THRESHOLD_DAYS` (365) are also aliased. They are
    the same windows under other names.
- **Inline literals replaced by constants** in every listed place:
  - codeanalyzer: `LAR` (`getRecentContributorsCount6Months/3Months`), `LandscapeAnalyzer`
    (`updatePeopleDependencies`, the 30-day steps in `addHistory`), `ContributorsAnalyzer` (people/file
    dependencies), `FileHistoryAnalyzer` (the `maxDays` checks and the three `FilePairsChangedTogether` windows),
    and `Contributor.addCommit`;
  - reports: `ContributorsReportGenerator` (`renderPeopleDependencies` windows, "No commits in past N days",
    zoomable circles), `CommitsReportGenerator` (zoomable circles, `pastDays`), `ContributorsReportUtils`
    (`isActive(...)` buckets), `FileSizeReportGenerator` (30/90-day correlations),
    `FileTemporalDependenciesReportGenerator` (tab checks), `LandscapeRepositoriesReport` (row colouring,
    "not active" headers) and `LandscapeReportPeopleTopologyTab` (`renderPeopleDependencies` calls, `daysAgo == 30`);
  - CLI: `CommandLineInterface` (zoomable-circle generation).
- **Labels.** The evidence sites in `Tab` (192-196, 656-657) and `Gen` (873, 1544-1545) now use the label
  constants. So do the `RFE` "3 months / 6 months" table header and `Gen`'s "12 months window" link.
  `Gen.CONTRIBUTORS_30_D` / `COMMITS_30_D` are now built from `SUFFIX_30D`; they are still constants with the
  same values.
- **`RFE:520-581`.** The four copied "people dependencies" list items (30/90/180/365) are now one private helper,
  `addPeopleDependencyLinks(report, windowLabel, days, graphNumber)`. It is called once per window, so the
  window shows up in the call instead of inside 28 file-name literals.
- **`"yyyy-MM-dd"`.** The six raw literals (`DateUtils.getWeekMonday`, `Contributor` ×2, `AuthorCommit`,
  `FileHistoryUtils`, `TrendAnalysisConfig`) now use `DateUtils.DATE_FORMAT`.
- **First/latest date update.** There are new `DateUtils.earlierDate(current, candidate)` /
  `laterDate(current, candidate)` helpers. A blank `current` counts as "not set". `Contributor.addCommit`,
  `GitContributorsUtil.importGitContributorsExport`, `LAR.addRepoToTeam` and
  `ContributorRepositories.addRepository` use them.
- **`LAR` accessors.** One comment block before the per-window accessors explains that they are the
  per-window variants of one metric and where they are computed. No signature changed.

**Differences from the proposal / left out.**
- **`ContributorRepositories.addRepository` keeps its different semantics.** Unlike the other three copies, it
  had no blank check, so a blank stored date was never replaced. The helpers treat blank as "not set", so they
  are called there only when the stored date is not blank, and a comment says so.
  `GitContributorsUtil` used `<=`/`>=`. For equal dates this stores an equal string, so the result is the same
  as with `<`/`>`.
- **Section comment instead of per-method Javadoc on the `LAR` accessors.** One Javadoc block can only attach
  to one method. A single block comment before the group documents all ~40 accessors without repeating it
  80 times.
- **Not replaced (deliberately):**
  - visual and data file names that contain a window number inside a literal, such as
    `file_changed_together_dependencies_..._30_days*`, `extension_dependencies_30d*`, `bubble_chart_..._30d`
    and `racing_charts_..._12_months_window`. They are outside the listed ranges, and their producers build
    the names separately;
  - HTML tab ids such as `"30_days"`;
  - the "Past 30 Days / Past 3 Months" tab titles and the other "past 30 days" sentences in the
    topology/landscape paragraphs that the section does not list. Those sites can move to `ActivityWindows`
    labels when they are next edited, or as part of R3.
- **Not touched because they are not windows:** `365.0` used to compute an age in years, list limits, pixel
  sizes and `THOUSAND_YEARS`. Log messages kept their exact text.
- **No new tests.** The proposal does not ask for any. `check.sh` compares all 906 generated files byte for
  byte, which covers every replaced label and window.

```
 .../obren/sokrates/cli/CommandLineInterface.java   | 17 ++--
 .../analysis/files/ContributorsAnalyzer.java       | 17 ++--
 .../analysis/files/FileHistoryAnalyzer.java        | 20 ++---
 .../sourcecode/contributors/Contributor.java       | 30 +++----
 .../contributors/GitContributorsUtil.java          | 10 +--
 .../sourcecode/core/TrendAnalysisConfig.java       |  3 +-
 .../sourcecode/filehistory/ActivityWindows.java    | 67 +++++++++++++++
 .../sokrates/sourcecode/filehistory/DateUtils.java | 23 ++++-
 .../sourcecode/filehistory/FileHistoryUtils.java   |  2 +-
 .../sourcecode/githistory/AuthorCommit.java        |  3 +-
 .../analysis/ContributorRepositories.java          | 11 ++-
 .../analysis/LandscapeAnalysisResults.java         | 24 ++++--
 .../landscape/analysis/LandscapeAnalyzer.java      | 19 +++--
 .../sokrates/reports/core/ReportFileExporter.java  | 99 ++++++++--------------
 .../statichtml/CommitsReportGenerator.java         | 11 +--
 .../statichtml/ContributorsReportGenerator.java    | 23 ++---
 .../statichtml/ContributorsReportUtils.java        |  9 +-
 .../statichtml/FileSizeReportGenerator.java        |  9 +-
 .../FileTemporalDependenciesReportGenerator.java   | 13 +--
 .../statichtml/LandscapeReportContributorsTab.java | 11 +--
 .../statichtml/LandscapeReportGenerator.java       | 16 ++--
 .../LandscapeReportPeopleTopologyTab.java          | 11 +--
 .../repositories/LandscapeRepositoriesReport.java  |  9 +-
 23 files changed, 266 insertions(+), 191 deletions(-)
```

## R3: Remove the copied contributor charts between `LandscapeReportGenerator` and `LandscapeReportContributorsTab`

**Status:** done (commit `b260b7a`). `./check.sh` (full, with tests): 360 tests passed, report output identical
to the baseline (906 files, including the `_sokrates_landscape` output), `CHECK PASSED`.

**What I did.**
- **New package-private class `reports/.../landscape/statichtml/LandscapeContributorsCharts`.** It holds the code that
  was copied between `Gen`, `Tab` and `LandscapeReportPeopleTopologyTab`, and has a "what lives here" Javadoc with
  synonyms. The pieces:
  - **Constants:** `PEOPLE_COLOR`, `CURRENT_YEAR_COLOR` (`#343434`), `PAST_YEAR_COLOR` (`#989898`), `BAR_MAX_HEIGHT`
    (`64.0`), `SHOW_MORE_CUTOFF` (`25`) and `MAX_DEPENDENCIES` (`100`, used as the graph dependency limit and the
    tooltip list limit).
  - **Empty time slots:** one `addMissingTimeSlots` implementation behind static `getContributionDays/Weeks/Months/Years`.
  - **Per-year chart:** `addContributorsPerYear(showContributorsCount, contributorsCount)`. The contributor count is
    the parameter that used to be the only difference between the copies.
  - **Per-extension chart:** `addContributorsPerExtension(title, linkCharts, sortByActivity)`, plus the private
    `addContributorDependencies`, `getMainExtensions`, `addLangInfo` and `addLangInfoBlockExtra`.
  - **Info blocks:** `addInfoBlockWithColor`, `addPeopleInfoBlock`, `addPeopleInfoBlockWithColor`,
    `addWorkloadInfoBlockWithColor`, `addSmallInfoBlock`, `getExtraPeopleInfo` and `addDownloadLinks`.
  - **Time-slot maps:** the per day / week / month / year contributor and rookie maps, built in the constructor by
    `populateTimeSlotMaps`/`updateTimeSlotMap`. They are read through `getContributorsCountPerYear` and
    `getContributorsPerDay/Week/Month`.
- **`Gen`.** It creates one helper (type `CONTRIBUTORS`, list `analysisResults.getContributors()`) where it used to
  call `populateTimeSlotMaps()`.
  - `getContribution*`, `addDownloadLinks` and `addInfoBlockWithColor` are now one-line delegates.
  - `addBigSummary` and `addExtensions` call the helper.
  - The copies of the per-year chart, the per-extension chart, the extension dependencies, the info blocks and the
    time-slot maps are removed.
  - `addInfoBlock`, `addFreshInfoBlock` and `addRepositoriesInfoBlockWithColor` are used only by `Gen`, so they stay
    there and go through the helper's `addInfoBlockWithColor`.
- **`Tab`.**
  - It creates its helper where it used to call `populateTimeSlotMaps()`.
  - The public static `getContributionDays/Weeks/Years/Months` delegate to the helper.
  - The week/day/month chart rows read the helper's maps, and their `64.0` literals use `BAR_MAX_HEIGHT`.
  - The copied methods (about 330 lines) are removed. The rest of the trend charts (`addChartRows`,
    `getLastContributorsPer*`, `getSignificantContributorsPerYear`) are not copied anywhere, so they stay.
- **`LandscapeReportPeopleTopologyTab`.** It uses the helper for its people info block. Its copies of the
  info-block helpers and the time-slot maps are removed. The maps were built there but never read; the helper still
  builds them, so the work done is the same. `setLandscapeReport` also updates the helper's report.
- **`PEOPLE_COLOR`.** The public `PEOPLE_COLOR` in `Gen`, `Tab` and `LandscapeReportPeopleTopologyTab` is now
  `= LandscapeContributorsCharts.PEOPLE_COLOR`. It is still a public compile-time constant with the same value.

**Differences from the proposal / left out.**
- **The per-extension chart has two extra parameters besides `linkCharts`.** The copies differed in more than the
  proposal lists, and both differences reach the HTML:
  - **Title.** `Gen` prints "Contributors per File Extensions (past 30 days)"; `Tab` prints "Contributors Per File
    Extension (past 30 days)" or "Teams Per File Extension …". The title is now a parameter.
  - **Order.** `Tab` re-sorts the shown extensions by 30-day commits and then by contributor count. `Gen` keeps the
    order by contributor count, which differs when counts tie. The `sortByActivity` flag keeps both orders.
  So `Tab`'s version is the general one only after these two parameters.
- **The constructor also takes `TeamsConfig` and the reports folder** (the proposal lists four arguments). The teams
  value function needs the exact `TeamsConfig` instance that `Tab` was given, and the extension dependency graph is
  exported to the reports folder.
- **The extension dependencies graph keeps `Tab`'s `if (isContributorReport())` as a check on the type.** I did not
  add a separate flag. `Gen` always uses `Type.CONTRIBUTORS`, so it always renders, as before. The teams tab still
  computes the graph but does not show it.
- **`getExtraPeopleInfo` now counts recent contributors from its parameter in both pages.** `Gen` used
  `landscapeAnalysisResults.getContributors()` for that one line. `Gen` passes that same cached list as the
  parameter, so the value is the same.
- **The helper has a package-private `setLandscapeReport`**, because `LandscapeReportPeopleTopologyTab` has a
  public `setLandscapeReport` that swaps the target report.
- **No new tests.** The proposal does not ask for any. `check.sh` compares the generated landscape pages byte for byte.

```
 .../statichtml/LandscapeContributorsCharts.java    | 511 +++++++++++++++++++++
 .../statichtml/LandscapeReportContributorsTab.java | 468 ++-----------------
 .../statichtml/LandscapeReportGenerator.java       | 383 +--------------
 .../LandscapeReportPeopleTopologyTab.java          |  85 +---
 4 files changed, 559 insertions(+), 888 deletions(-)
```

## R9: Model → export-DTO mapping next to the DTO, with a field-parity test

**Status:** done (commit `f232506`). `./check.sh` (full, with tests): 364 tests passed (4 new), report output
identical to the baseline (906 files), `CHECK PASSED`.

**What I did.**
- **`UnitInfoExport.from(UnitInfo)`** now holds the whole field copy, including the `SourceFile` →
  `relativeFileName` / `fileLinesCount` / `components` mapping. `UnitListExporter.getAllUnitsData()` calls it.
- **`DuplicateFileBlockExportInfo.from(DuplicatedFileBlock)`** holds the copy that used to sit in
  `DuplicationExporter.getDuplicatesExportInfo()`. The exporter calls it.
- **`DependencyAnchorExportInfo.from(DependencyAnchor)`** holds the copy. The existing public
  `getInstance(DependencyAnchor)` delegates to it, so its callers (`DependencyExportInfo`, `DependenciesExporter`)
  are unchanged.
- **`ContributorRepositoryInfoExport.from(ContributorRepositoryInfo)`** was added. `ContributorExport` now maps
  with `ContributorRepositoryInfoExport::from`.
- **Tests.** There is a new test-only helper, `reports/src/test/.../dataexporters/ExportParity`. It reads the
  readable bean properties of the model and of the DTO with `java.beans.Introspector`. It checks that they match
  after explicit `MODEL_ONLY` / `EXPORT_ONLY` exclusion lists, and it fails if an exclusion is stale. It then
  checks that every shared property has the same value in a populated model and in the DTO built from it. There
  is one test per pair: `UnitInfoExportTest`, `DuplicateFileBlockExportInfoTest`, `DependencyAnchorExportInfoTest`
  and `ContributorRepositoryInfoExportTest`. Each also asserts the mapped fields that the parity check excludes,
  for example the relative path behind `SourceFile` → `FileExportInfo`. The failure message names the DTO and
  its `from(...)`.

**Differences from the proposal.**
- **In `ContributorRepositoryInfoExport` the copy stays in the public constructor, and `from` calls the
  constructor.** The proposal says the constructor should delegate to `from(model)`, but a constructor cannot
  delegate to a static factory without either a second copy or a new no-arg constructor. The class has no no-arg
  constructor today, and adding one (even a private one) could change how Jackson treats the class. The mapping
  still lives in the DTO's own file, which is the point of the refactoring.
- **Callers of `getInstance` are unchanged.** `getInstance` keeps its public signature and delegates to `from`.
- **Exclusion lists in the tests:**
  - `UnitInfo`: `sourceFile`, `body`, `cleanedBody` and `children` on the model side; `relativeFileName`,
    `fileLinesCount` and `components` on the DTO side.
  - `DuplicatedFileBlock`: `sourceFile`, `blockSize` and `percentage` on the model side; `file` on the DTO side.
  - `DependencyAnchor`: `sourceFiles` and `filters` on the model side; `files` on the DTO side.
  - `ContributorRepositoryInfo`: `repositoryAnalysisResults` on the model side; `name` and `description` on the
    DTO side.

```
 .../dependencies/DependencyAnchorExportInfo.java   |  8 +++
 .../duplication/DuplicateFileBlockExportInfo.java  | 18 ++++++
 .../duplication/DuplicationExporter.java           | 12 +---
 .../dataexporters/units/UnitInfoExport.java        | 29 +++++++++
 .../dataexporters/units/UnitListExporter.java      | 20 +-----
 .../reports/landscape/data/ContributorExport.java  |  2 +-
 .../data/ContributorRepositoryInfoExport.java      |  9 +++
 .../reports/dataexporters/ExportParity.java        | 75 ++++++++++++++++++++++
 .../DependencyAnchorExportInfoTest.java            | 38 +++++++++++
 .../DuplicateFileBlockExportInfoTest.java          | 40 ++++++++++++
 .../dataexporters/units/UnitInfoExportTest.java    | 45 +++++++++++++
 .../data/ContributorRepositoryInfoExportTest.java  | 39 +++++++++++
 12 files changed, 304 insertions(+), 31 deletions(-)
```

## R8: `CommandLineInterface`: one dispatch table and named default paths

**Status:** done (commit `610da43`). `./check.sh` (full, with tests): 364 tests passed, report output identical
to the baseline (906 files), `CHECK PASSED`.

**What I did.** No new classes. I changed only `cli/.../CommandLineInterface.java` and `cli/.../Commands.java`.
- **Dispatch table.** The `if/else equalsIgnoreCase` chain in `run` is gone. In its place:
  - a private `CommandHandler` functional interface (`handle(String[] args) throws ParseException, IOException`);
  - a private `Map<String, CommandHandler> commandHandlers`, built in `createCommandHandlers()` from the
    `Commands` name constants. It has one `put` per command (9 commands).
  
  The map is a `TreeMap` with `String.CASE_INSENSITIVE_ORDER`, so the lookup stays case-insensitive. It uses the
  same character comparison as `equalsIgnoreCase`. An unknown command still sets `helpMode` and prints the
  general usage. Only `ParseException` is caught, as before. `generateReports` is now a table entry instead of
  the fall-through after the chain.
- **Prologue.** The parse-then-check-help block repeated in 7 handlers is now the private
  `parseArgsOrShowHelp(args, Supplier<Options>, command, description)`. It returns `null` after printing the
  command usage. The handlers are `extractGitHistory`, `extractGitSubHistory`, `extractFiles`,
  `updateLandscape`, `generateReports(String[])`, `init` and `updateConfig`. The options come from a
  `Supplier` (`commands::getXOptions`), so the `Options` object is built twice, once for parsing and once for
  usage, exactly as before.
- **Default paths.** I added four new public constants in `Commands`:
  - `DEFAULT_CONFIG_FILE_RELATIVE_PATH` (`_sokrates/config.json`);
  - `DEFAULT_REPORTS_FOLDER_RELATIVE_PATH` (`_sokrates/reports`);
  - `DEFAULT_CONFIG_FILE_PATH` (`./_sokrates/config.json`);
  - `DEFAULT_REPORTS_FOLDER_PATH` (`./_sokrates/reports`).
  
  `generateReports(CommandLine)` uses the last two. The help texts of the `srcRoot`, `confFile`, `analysisRoot`
  and `outputFolder` options build their default path from the relative constants. The resulting strings are
  unchanged, including their existing inconsistencies (for example `<currentFolder/_sokrates/reports>` and the
  unclosed quote in the `srcRoot` text).
- I updated the R7 Javadoc in both classes to point to `createCommandHandlers()` as the command → handler table.

**Differences from the proposal / left out.**
- **File-size colouring not moved.** `getFileSizeColor` uses the same five `<=` buckets as
  `PathStringsToTreeStructure.getColor`, but the two return different things:
  - `getFileSizeColor` returns the fixed `Thresholds.RISK_*` `BasicColorInfo` objects used by the 3D view;
  - `getColor` returns a `Palette` colour string from a `sourcecode.threshold.Thresholds`.
  
  Delegating would change the colours in the 3D files and would need a type conversion. It is also a public
  method. As the proposal allows for this case, I left it in place and added a comment explaining why.
- **Defects preserved.** The `updateConfig` options that are read but never registered (an appendix defect) are
  unchanged, and so is the missing `extractFiles` in `usageInfo()`.
- **Help output not compared.** I could not run the CLI help/usage commands against `reference/cli-a.jar` here,
  because running the jars outside `check.sh` was not permitted in this session. The help strings are unchanged
  by construction (same concatenated text), and `check.sh` covers the `init`, `generateReports` and
  `updateLandscape` dispatch.

```
 .../obren/sokrates/cli/CommandLineInterface.java   | 141 +++++++++------------
 .../main/java/nl/obren/sokrates/cli/Commands.java  |  16 ++-
 2 files changed, 73 insertions(+), 84 deletions(-)
```

## R10: One base for the "keyword … end" heuristic unit extractors (Julia, Lua, ObjectPascal)

**Status:** done (commit `d24f5f9`). `./check.sh` (full, with tests): 364 tests passed, report output identical
to the baseline (906 files), `CHECK PASSED`.

**What I did.** One new class, plus the three extractors rewritten onto it.
- **New:** `codeanalyzer/.../sourcecode/lang/EndKeywordHeuristicUnitsExtractor` (abstract). It holds:
  - the shared fields and a protected constructor;
  - the public `extractUnits` loop;
  - `initUnit`, `updateUnit` and `endUnit`;
  - the comment cleaner set-up (code-block parsers set to remove when cleaning);
  - the parameter-count and McCabe skeleton.
- **Hooks:** the four hooks the proposal names, plus two hooks with defaults:
  - abstract: `isFunctionStartLine(line)`, `isUnitEnd(line, prefix)`, `getParameterSeparatorRegex()`,
    `getMcCabeKeywords()` (one `StringUtils.countMatches` per entry, added to 1);
  - with defaults: `getBodyForMcCabeSearch(cleanedBody)` (default: newlines become spaces, a space goes before
    `(` and `{`) and `countParametersOnlyIfSignatureHasParenthesis()` (default `false`).
- **The three extractors** `JuliaHeuristicUnitsExtractor`, `LuaHeuristicUnitsExtractor` and
  `ObjectPascalHeuristicUnitsExtractor` keep their public class and public constructor `(SourceFile, String, XAnalyzer)`.
  They now extend the base and contain only the hooks and a keyword-list constant.
- **Unit end:** Julia and Lua use `startsWith(prefix + "end")`; ObjectPascal uses `equals(prefix + "end;")`.
- **Separators:** `"\\,"` for Julia and Lua, `"\;"` for ObjectPascal.

**Differences from the proposal.**
- **Two extra hooks.** The three copies differed in two places beyond the four hooks the proposal names:
  - ObjectPascal does not put a space before `{` but does lower-case the search body. Putting a space before `{`
    can create new matches, for example `for{` would match `" for "`. So the search-body step is an overridable
    hook (`getBodyForMcCabeSearch`).
  - ObjectPascal counts parameters only when the unit's first line contains `(`. Julia and Lua use the first
    parentheses anywhere in the body. This is the `countParametersOnlyIfSignatureHasParenthesis` hook.
- **Cleaner passed as a `Supplier`.** Each analyzer's `getCommentsAndEmptyLinesCleaner()` is `protected`, and
  `LanguageAnalyzer` does not declare it. The base, in package `lang`, cannot call it, so each subclass passes
  `analyzer::getCommentsAndEmptyLinesCleaner`. As before, every unit gets a fresh cleaner.
- **Quirks preserved, with comments:**
  - ObjectPascal lower-cases the body, then counts `" AND"` and `" OR "`, which never match;
  - Julia's literal `" \\? "` is still in its keyword list.
- **Visibility:** `isFunctionStartLine` went from private to protected (an override). No public or
  package-private signature changed.
- **No new tests.** The proposal asks for none. The existing `JuliaAnalyzerTest`, `LuaAnalyzerTest` and
  `ObjectPascalAnalyzerTest` cover the extractors. `fixture/` has no files in these languages.
- **Check-harness flakiness.** The first two full runs failed only on `TOTAL_ANALYSIS_TIME_IN_MILLIS` in
  `Metrics.html` (for example `N,547` vs `N,324`). When the analysis takes 1000 ms or more, the value is printed
  with a thousands separator, and `normalize.py` replaces only the digits before the comma. This happened in the
  baseline run too, so it is timing, not behaviour. The `--no-tests` run and the next full run then passed.

```
 .../lang/EndKeywordHeuristicUnitsExtractor.java    | 177 +++++++++++++++++++++
 .../lang/julia/JuliaHeuristicUnitsExtractor.java   | 135 ++--------------
 .../lang/lua/LuaHeuristicUnitsExtractor.java       | 132 ++-------------
 .../ObjectPascalHeuristicUnitsExtractor.java       | 139 +++-------------
 4 files changed, 236 insertions(+), 347 deletions(-)
```
