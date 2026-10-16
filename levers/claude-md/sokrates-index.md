# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Sokrates is a source-code analysis tool (a structured, regex-driven "code spelunking" tool) that produces HTML/JSON reports and has an interactive JavaFX explorer. Java, multi-module Maven build (groupId `nl.obren.sokrates`, version 1.0). Tests use JUnit 5 (Jupiter).

## Build and test

```
mvn clean install                      # builds all modules, runs tests
mvn clean install -DskipTests          # what the Dockerfile does
mvn test -pl codeanalyzer              # tests for one module
mvn test -pl codeanalyzer -Dtest=SourceFileTest             # single test class
mvn test -pl codeanalyzer -Dtest=SourceFileTest#methodName  # single test method
```

Outputs (fat jars from maven-assembly-plugin):
- `cli/target/cli-1.0-jar-with-dependencies.jar` — main class `nl.obren.sokrates.cli.CommandLineInterface`
- `codeexplorer/target/codeexplorer-1.0-jar-with-dependencies.jar` — main class `nl.obren.sokrates.codeexplorer.CodeExplorerLauncher`

Notes:
- The pom targets Java 1.8 source/target, but the Dockerfile builds with JDK 11 and buildspec uses Corretto 11. The explorer needs JavaFX (pulled in as Maven dependencies).
- Graphviz (`dot`) is needed at runtime for dependency/diagram rendering (`GRAPHVIZ_DOT` env var in Docker; `-internalGraphviz` CLI option exists).
- Docker: `docker build -t sokrates .` then `docker run -v "$(pwd):/code" -w /code sokrates init`.

## CLI usage

Commands are defined in `cli/.../Commands.java` (name, description, and argument constants) and dispatched from `CommandLineInterface.java`. Main commands: `init`, `generateReports`, `updateConfig`, `updateLandscape`, `createConventionsFile`, `exportStandardConventions`, `extractGitHistory`, `extractGitSubHistory`, `extractFiles`. Typical flow: `init` creates an analysis config JSON (default under `_sokrates/`), then `generateReports` consumes it. Add a new command by adding constants/options in `Commands` and handling it in `CommandLineInterface`.

## Architecture

Module dependency chain (see root `pom.xml` `<modules>` order): `common` → `codeanalyzer` → `reports` → `cli`; `codeexplorer` depends on `common`, `codeanalyzer`, `reports`, and `cli`.

- **common** — shared utilities: JSON (de)serialization (`JsonMapper`/`JsonGenerator`), Graphviz helpers, rendering utilities and chart builders (`renderingutils/`), `UserProperties`.
- **codeanalyzer** (package `nl.obren.sokrates.sourcecode`) — the analysis engine, with no UI. Key pieces:
  - `core/CodeConfiguration` / `AnalysisConfig` — the JSON analysis configuration (source files scoping, logical decompositions, concerns, goals/controls, etc.) that drives everything.
  - `analysis/CodeAnalyzer` orchestrates the per-aspect analyzers in `analysis/files/` (basics, file size, units, duplication, dependencies, concerns, contributors, file history, controls, logical decomposition) and fills the `analysis/results/*` model classes (`CodeAnalysisResults` is the top-level result).
  - `aspects/` — the "aspect" model: logical decompositions/components, concerns, dependency finders, meta-rules. `scoping/`, `cleaners/`, `lang/`, `units/`, `duplication/`, `dependencies/`, `githistory/`/`filehistory/`, `landscape/`, `search/` are the supporting domains.
- **reports** (package `nl.obren.sokrates.reports`) — turns `CodeAnalysisResults` into HTML reports/visualizations and landscape (multi-project aggregate) reports. Large resource tree (`src/main/resources`: `templates`, `style`, `icons`, `lang`, `prompts`) holds the HTML templates and static assets embedded in the output.
- **cli** — thin command-line front end plus git history extraction (`cli/git/GitHistoryExtractor`, uses JGit).
- **codeexplorer** — JavaFX desktop GUI (code browser, search, dependencies, duplicates, units, config editor) that reuses the analyzer and report modules.

Notable convention: analysis behavior is configuration-driven; changing what gets analyzed usually means changing the config model classes in `core/` / `aspects/` and the corresponding analyzer, then the result class and the report renderer that displays it.

## Misc

- `.ignore` / `.gitignore` exclude `target/`, IDE files, and `/_sokrates/reports`.
- Tests live under `src/test/java` in `codeanalyzer` and `codeexplorer`; some `codeexplorer` tests are in package `nl.obren.sokrates.reports.utils`.

## Code index

Every package of main code, with its classes and, in parentheses, their public methods ("+k" means k more not listed).

- `cli/src/main/java/nl/obren/sokrates/cli/`: CommandLineInterface(main, +6); CommandUsage(getName, +5); Commands(usage, +33); PathStringsToTreeStructure(createDirectoryTree, +16); SokratesFileUtils(extractFiles, +2)
- `cli/src/main/java/nl/obren/sokrates/cli/git/`: GitHistoryExtractor(extractGitHistory)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/`: CleaningResult(getContent, +3); ExtensionGroup(getExtension, +6); ExtensionGroupExtractor(isKnownSourceCodeExtension, +7); IgnoredFilesGroup(getFilter, +3); Link(getLabel, +3); Metadata(getName, +9); SearcheableFilesCache(getInstance, +7); SourceCodeFiles(getLinesOfCode, +13); SourceFile(flattenToAscii, +30); SourceFileFilter(matchesAnyLine, +14); SourceFileWithSearchData(getFoundInstancesCount, +8)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/analysis/`: AnalysisUtils(analyze, +3); Analyzer; AnalyzerOverride(getAnalyzer, +4); CodeAnalyzer(getCodeAnalyzerSettings, +4); CodeAnalyzerSettings(selectAll, +31); ContributorsAnalysisConfig; FileHistoryAnalysisConfig(getImportPath, +15)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/analysis/files/`: BasicsAnalyzer(analyze); ConcernsAnalyzer(analyze); ContributorsAnalyzer(analyze); ControlsAnalyzer(analyze); DuplicationAnalyzer(analyze, +1); FileHistoryAnalyzer(analyze); FileSizeAnalyzer(analyze); LogicalDecompositionAnalyzer(analyze); UnitCategoryNames(getNegligibleRisk, +9); UnitsAnalyzer(analyze)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/analysis/results/`: AspectAnalysisResults(getName, +18); CodeAnalysisResults(getMetadata, +52); ConcernsAnalysisResults(getKey, +7); ContributorsAnalysisResults(getLatestCommitDate, +35); ControlStatus(getControl, +5); ControlsAnalysisResults(getGoalsAnalysisResults, +1); DependenciesAnalysisResults(getControlStatuses, +1); DuplicationAnalysisResults(getOverallDuplication, +15); FileAgeDistributionPerLogicalDecomposition(getName, +3); FileDistributionPerLogicalDecomposition(getName, +3); FilesAnalysisResults(getAllFiles, +13); FilesHistoryAnalysisResults(getAllFiles, +66); GoalsAnalysisResults(getMetricsWithGoal, +3); HistoryPerExtension(getExtension, +7); LogicalDecompositionAnalysisResults(getKey, +13); UnitsAnalysisResults(getTotalNumberOfUnits, +21)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/aspects/`: ComponentGroup(getName, +3); Concern(getTextOperations, +1); ConcernsGroup(getName, +5); DependenciesFinder(isUseBuiltInDependencyFinders, +5); DependencyFinderPattern(getComponent, +9); DerivedConcern; GroupingRule(getName, +3); LogicalDecomposition(getName, +34); MetaDependencyRule(isReverseDirection, +3); MetaRule(getPathPattern, +9); MetaRulesProcessor(getConcernsInstance, +6); NamedSourceCodeAspect(getName, +14); Range(getMin, +8); RenderingOptions(getOrientation, +11); SourceCodeAspectUtils(getMaxLinesOfCode, +7)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/catalogs/`: StandardSecurityAspects
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/cleaners/`: CleanedContent(getCleanedLinesCount, +5); CodeBlockParser(getStringStartIndex, +10); CommentsAndEmptyLinesCleaner(addStringBlockHelper, +5); CommentsCleanerUtils(cleanLineComments, +1); SourceCodeCleanerUtils(normalizeLineEnds, +8)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/contributors/`: ContributionTimeSlot(getTimeSlot, +6); Contributor(addCommit, +28); ContributorsImport(getContributors, +17); GitContributorsUtil(importGitContributorsExport, +3)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/core/`: AnalysisConfig(isSkipDuplication, +45); CodeConfiguration(getDefaultConfiguration, +42); CodeConfigurationUtils(getDefaultSokratesFolder, +5); FoundTag(getTagRule, +3); ReferenceAnalysisResult(getLabel, +3); SimpleCallback(call); TagRule(getTag, +9); TrendAnalysisConfig(getHistoryFolder, +10)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/dependencies/`: ComponentDependency(getFromComponent, +24); DependenciesAnalysis(getDependencies, +5); DependenciesFinderExtractor(getAllDependencies, +2); DependenciesUtils(extractDependencies); Dependency(setFromFiles, +15); DependencyAnchor(getAnchor, +12); DependencyError(getMessage, +3); DependencyEvidence(getPathFrom, +3); DependencyProgressFeedback(getCurrentDependencies, +1); DependencyUtils(addDependency, +5); HeuristicDependenciesExtractor(extractDependencies, +2); HeuristicFilePathDependenciesExtractor(extractDependencies, +2); SourceFileDependency(getSourceFile, +3)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/duplication/`: AspectDuplication(getAspect, +5); DuplicatedFileBlock(getSourceFile, +14); DuplicationAggregator(getDuplicationPerSourceFile, +2); DuplicationDependenciesHelper(extractDependencies, +2); DuplicationEngine(findDuplicates, +4); DuplicationInstance(getDisplayContent, +10); DuplicationUtils(getLinesAsString, +5); ExtensionDuplication(getExtension, +5); SourceFileDuplication(getSourceFile, +7); UnitDuplicatesExtractor(findDuplicatedUnits, +2)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/duplication/impl/`: Block(getFiles, +8); Blocks(extractDuplicatedBlocks); BlocksExtractor(extractMinimalBlocks, +8); DuplicateRange(includes, +4); DuplicateRangePair(includes, +4); DuplicateRangePairs(includes, +2); FileInfoForDuplication(extractBlocks, +11); Files(addAll, +11); LineIndexesExtractor(getTotalLinesCount, +5); LineInfo(resetCounter, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/filehistory/`: CommitInfo(getId, +5); ComponentUpdateHistory(getComponent, +5); DateUtils(isDateWithinRange, +18); FileHistoryComponentsHelper(getNumberOfActiveDays, +1); FileHistoryScopingUtils(getLogicalDecompositionsFileUpdateFrequency, +2); FileHistoryUtils(getDateFromString, +2); FileModificationHistory(getPath, +13); FilePairChangedTogether(getSourceFile1, +11); FilePairsChangedTogether(populate, +1); GitHistoryUtil(importGitLsFilesExport); TemporalDependenciesHelper(extractComponentDependencies, +2)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/findings/`: Findings(getSummary, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/githistory/`: AuthorCommit(getDate, +11); CommitsPerExtension(getExtension, +22); ContributorPerExtensionStats(getContributor, +7); ExtractGitHistoryFileHandler(extractSubHistory); FileUpdate(getDate, +12); GitHistoryPerExtensionUtils(getCommitsPerExtensions, +4); GitHistoryUtils(printContributorsCommand, +5)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/`: ContributionCounter; ContributorConnection(getEmail, +7); ContributorConnectionUtils(getContributorConnections, +13); ContributorTag(getName, +3); CustomTab(getName, +3); DefaultTags(defaultTagGroups); LandscapeConfiguration(getMetadata, +87); LandscapeInfo(getSubLandscapes, +4); MergeExtension(getPrimary, +3); PeopleConfig(getPeople, +3); PersonConfig(getName, +7); RepositoryTag(getTag, +27); SokratesRepositoryLink(getAnalysisResultsPath, +5); SubLandscapeLink(getName, +5); TagGroup(getName, +8); TeamConfig(getName, +5); TeamsConfig(getTeams, +2); WebFrameLink(getTitle, +9)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/analysis/`: ContributorConnections(getEmail, +5); ContributorRepositories(addRepository, +6); ContributorRepositoryInfo(getRepositoryAnalysisResults, +17); DependenciesCreator(add, +2); FileExport(getRepository, +7); LandscapeAnalysisResults(getOverallFileLastModifiedDistribution, +142); LandscapeAnalysisResultsReadData(getMetadata, +37); LandscapeAnalysisUtils(findAllSokratesLandscapeConfigFiles, +2); LandscapeAnalyzer(analyze); RepositoryAnalysisResults(getSokratesRepositoryLink, +5)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/init/`: LandscapeAnalysisInitiator(initConfiguration); LandscapeAnalysisUpdater(updateConfiguration)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/utils/`: EmailTransformations(transformEmail)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/`: DefaultLanguageAnalyzer(cleanForLinesOfCodeCalculations, +4); LanguageAnalyzer(cleanForLinesOfCodeCalculations, +5); LanguageAnalyzerFactory(getInstance, +5); LanguageSupportExporter(main, +8)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/abap/`: AbapAnalyzer(cleanForLinesOfCodeCalculations, +4); AbapHeuristicUnitsExtractor(extractUnits, +3)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/adabasnatural/`: AdabasDependenciesExtractor(extractDependencyAnchors); AdabasNaturalAnalyzer(cleanForLinesOfCodeCalculations, +5)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/cfg/`: CfgAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/clojure/`: ClojureLangAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/cpp/`: CIncludesDependenciesExtractor(extractDependencyAnchors); CStyleAnalyzer(cleanForLinesOfCodeCalculations, +4); CppAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/csharp/`: CSharpAnalyzer(cleanForLinesOfCodeCalculations, +4); CSharpHeuristicDependenciesExtractor(extractDependencyAnchors)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/css/`: CssAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/d/`: DAnalyzer(cleanForLinesOfCodeCalculations, +4); DHeuristicDependenciesExtractor(extractDependencyAnchors); DHeuristicUnitsExtractor(isUnitSignature)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/dbc/`: DbcAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/go/`: GoLangAnalyzer(cleanForLinesOfCodeCalculations, +4); GoLangHeuristicDependenciesExtractor(extractDependencyAnchors); GoLangHeuristicUnitsExtractor(isUnitSignature)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/gradle/`: GradleAnalyzer(extractUnits); GradleHeuristicUnitsExtractor(isUnitSignature)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/groovy/`: GroovyAnalyzer(cleanForLinesOfCodeCalculations, +4); GroovyHeuristicDependenciesExtractor(extractDependencyAnchors); GroovyHeuristicUnitsExtractor(isUnitSignature, +1)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/hack/`: HackAnalyzer(cleanForLinesOfCodeCalculations, +4); HackHeuristicUnitsExtractor(isUnitSignature, +1)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/html/`: HtmlAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/java/`: JavaAnalyzer(cleanForLinesOfCodeCalculations, +4); JavaHeuristicDependenciesExtractor(extractDependencyAnchors); JavaHeuristicUnitsExtractor(isUnitSignature, +2)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/js/`: JavaScriptAnalyzer(cleanForLinesOfCodeCalculations, +4); JavaScriptHeuristicUnitsExtractor(isUnitSignature, +1)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/json/`: JsonAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/jsp/`: JspAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/julia/`: JuliaAnalyzer(cleanForLinesOfCodeCalculations, +5); JuliaHeuristicUnitsExtractor(extractUnits)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/kotlin/`: KotlinAnalyzer(cleanForLinesOfCodeCalculations, +4); KotlinHeuristicDependenciesExtractor(extractDependencyAnchors); KotlinHeuristicUnitsExtractor(isUnitSignature)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/less/`: LessAnalyzer
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/lua/`: LuaAnalyzer(cleanForLinesOfCodeCalculations, +5); LuaHeuristicUnitsExtractor(extractUnits)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/objectivec/`: ObjectiveCAnalyzer
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/objectpascal/`: ObjectPascalAnalyzer(cleanForLinesOfCodeCalculations, +5); ObjectPascalHeuristicUnitsExtractor(extractUnits)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/perl/`: PerlAnalyzer(cleanForLinesOfCodeCalculations, +4); PerlHeuristicDependenciesExtractor(extractDependencyAnchors); PerlHeuristicUnitsExtractor(isUnitSignature, +1)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/php/`: PhpAnalyzer(cleanForLinesOfCodeCalculations, +4); PhpHeuristicDependenciesExtractor(extractDependencyAnchors)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/plsql/`: PlSqlAnalyzer(cleanForLinesOfCodeCalculations, +4); PlSqlHeuristicDependenciesExtractor(extractDependencyAnchors, +2); PlSqlHeuristicUnitsExtractor(extractUnits, +3)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/puppet/`: PuppetAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/python/`: PythonAnalyzer(cleanForLinesOfCodeCalculations, +4); PythonDependenciesExtractor(extractDependencyAnchors); PythonHeuristicUnitsExtractor(replaceTabs, +5)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/r/`: RAnalyzer(cleanForLinesOfCodeCalculations, +4); RHeuristicUnitsExtractor(isUnitSignature)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/ruby/`: RubyAnalyzer(cleanForLinesOfCodeCalculations, +4); RubyHeuristicDependenciesExtractor(extractDependencyAnchors); RubyHeuristicUnitsExtractor(extractUnits)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/rust/`: RustAnalyzer(cleanForLinesOfCodeCalculations, +4); RustHeuristicUnitsExtractor(isUnitSignature)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/sass/`: SassAnalyzer
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/scala/`: ScalaAnalyzer(cleanForLinesOfCodeCalculations, +4); ScalaHeuristicDependenciesExtractor(extractDependencyAnchors); ScalaHeuristicUnitsExtractor(isUnitSignature)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/scss/`: ScssAnalyzer
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/shell/`: ShellAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/sql/`: SqlAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/swift/`: SwiftAnalyzer(cleanForLinesOfCodeCalculations, +4); SwiftHeuristicUnitsExtractor(isUnitSignature)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/thrift/`: ThriftAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/ts/`: TypeScriptAnalyzer
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/vb/`: VisualBasicAnalyzer(cleanForLinesOfCodeCalculations, +6); VisualBasicHeuristicUnitsExtractor(extractUnits)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/xml/`: XmlAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/yaml/`: YamlAnalyzer(cleanForLinesOfCodeCalculations, +4)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/metrics/`: DuplicationMetric(getKey, +10); Metric(getId, +5); MetricRangeControl(getMetric, +5); MetricsList(addMetric, +5); MetricsWithGoal(getGoal, +5); NumericMetric(getName, +5)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/operations/`: ComplexOperation(exec); OperationStatement(getOp, +3); StringOperation(getName, +4); StringOperationFactory(getOperation)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/operations/impl/`: AppendOperation(exec); ExtractRegexOperation(exec); LowerCaseOperation(exec); NoOpsOperation(exec); PrependOperation(exec); RemoveRegexOperation(exec); ReplaceOperation(exec); TrimOperation(exec); UpperCaseOperation(exec)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/scoping/`: ConcernConventions(addConventions); Convention; ConventionUtils(addConventions); ScopeCreator(createScopeFromConventions); ScopingConventions(main, +11)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/scoping/custom/`: CustomConventionsHelper(exportStandardConventions, +3); CustomExtensionConventions(getOnlyInclude, +3); CustomScopingConventions(getExtensions, +39)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/search/`: CleanedFoundText(getFoundText, +3); FoundLine(getLineNumber, +6); FoundText(getText, +3); ReplacePair(getReplace, +4); SearchExpression(getExpression, +3); SearchRequest(getPathSearchExpression, +3); SearchResult(getFoundFiles, +18); SearchResultCleaner(getCleanedTextList, +9); SearchResultDependencies(getDependencies, +7)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/stats/`: RiskDistributionStats(getKey, +49); SourceFileAgeDistribution(getFileAgeRiskDistributionPerExtension, +3); SourceFileChangeDistribution(getDistributionPerExtension, +3); SourceFileSizeDistribution(getFileSizeRiskDistributionPerExtension, +2)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/threshold/`: Thresholds(defaultFileSizeThresholds, +21)
- `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/units/`: CStyleHeuristicUnitsExtractor(setExtractRecursively, +8); CppUnitsExtractor(getEndOfUnitBodyIndex, +1); UnitInfo(getShortName, +27); UnitUtils(getLinesOfCode, +10); UnitsExtractor(getUnits)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/`: CodeExplorerLauncher(main, +1)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/charts/`: ChartUtils(applyCustomColorSequence); FileSizeDistributionChartPanel(getToolBar, +3)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/codebrowser/`: AspectFilesBrowserPane(getCodePreviewEditor, +8); AspectsTablePane(setAspectSelections, +4); CodeBrowserPane(getConsole, +13); CodeExplorerToolbar; FileListPane(getTable, +6); TableViewUtils(addCopyToClipboardContextMenu)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/common/`: NumericBarCellFactory(call, +1); PathCellRenderer(call, +1); ProgressFeedbackPane(getProgressFeedback, +5); SvgIcons; UXUtils(getButton, +5)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/configuration/`: CodeConfigurationView(initEngine, +12); ConfigurationEditorView(initEngine, +9)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/console/`: Console(log); TextAreaConsole(clear, +8); WebViewConsole(clear, +8)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/dependencies/`: DependenciesMatrix(load); DependenciesPane(close, +12); DependenciesView(load)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/duplication/`: DuplicatesPane(openInWindow, +4)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/newanalysis/`: NewAnalysisDialog(setText, +1)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/preview/`: CodePreviewEditor(getCodeArea, +4); CodePreviewView(setEditorValue, +5); SimpleCodePreviewView(clear, +4)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/search/`: PredefinedFiltersMenuPopulator(populate, +1); SearchCleanerPane(connect, +1); SearchDependenciesPane(getDependenciesPane); SearchPane(setColumnConstraints, +12); SearchResultCellRenderer(updateItem, +1); SearchResultsDistributionTablePane(call, +3); SearchStatsPane(show); SearchSummaryPane; SearchVizPane(show)
- `codeexplorer/src/main/java/nl/obren/sokrates/codeexplorer/units/`: UnitsPane(close, +5)
- `common/src/main/java/nl/obren/sokrates/common/analysis/`: Finding(getDisplayText, +16); ValidationMessage(getMessage, +3)
- `common/src/main/java/nl/obren/sokrates/common/io/`: JsonGenerator(generate, +1); JsonMapper(getObject, +1); UserProperties(getUserPropertiesFolder, +11)
- `common/src/main/java/nl/obren/sokrates/common/renderingutils/`: ExplorerTemplate(render); GraphvizSettings(getInstance, +1); GraphvizUtil(getSvgFromDot, +1); RacingChartItem(getName, +5); RichTextRenderingUtils(getPercentage, +13); Threshold(getTitle, +5); Thresholds(getColors, +1); VisualizationItem(getName, +7); VisualizationTemplate(render, +7)
- `common/src/main/java/nl/obren/sokrates/common/renderingutils/charts/`: BarChart(addRow, +13); OrgChart(toString); OrgChartItem(getItem, +5); Palette(getRiskPalette, +7); PieChart(addItem, +13)
- `common/src/main/java/nl/obren/sokrates/common/renderingutils/force3d/`: Force3DLink(getSource, +5); Force3DNode(getId, +3); Force3DObject(getNodes, +3)
- `common/src/main/java/nl/obren/sokrates/common/renderingutils/x3d/`: Unit3D(getName, +5); X3DomExporter(export, +1); X3DomNodeInfo(getName, +11)
- `common/src/main/java/nl/obren/sokrates/common/utils/`: BasicColorInfo(getRed, +5); ExplorerStringUtils(firstIndexOfAny); FormattingUtils(getFormattedPercentage, +7); ProcessingStopwatch(startAsReference, +5); ProcessingTimes(start, +8); ProgressFeedback(clear, +6); ReflectionUtils(getAllFields, +2); RegexUtils(matchesEntirely, +6); SystemUtils(getSafeFileName, +3); Templates; UnzipToTempFilesUtil(unzip)
- `reports/src/main/java/nl/obren/sokrates/reports/charts/`: SimpleOneBarChart(calculateBarOffsetFromTexts, +25)
- `reports/src/main/java/nl/obren/sokrates/reports/core/`: AnalysisEngine; Figure(getDescription, +5); ReportConstants; ReportFileExporter(exportHtml, +6); ReportGenerator; ReportRenderer(renderBreadcrumbsInDiv, +1); ReportRenderingClient; ReportsContainer(getRichTextReports, +1); RichTextFragment(getId, +9); RichTextReport(getDisplayName, +86); Scope; SummaryUtils(getIconSvg, +5)
- `reports/src/main/java/nl/obren/sokrates/reports/dataexporters/`: DataExportUtils(getAspectFileListFileName, +2); DataExporter(dependenciesFileNamePrefix, +10)
- `reports/src/main/java/nl/obren/sokrates/reports/dataexporters/dependencies/`: DependenciesExportInfo(getTitle, +5); DependenciesExporter(getDependenciesExportInfo); DependencyAnchorExportInfo(getInstance, +8); DependencyExportInfo(getInstance, +6); SourceFileDependencyExportInfo(getInstance, +4)
- `reports/src/main/java/nl/obren/sokrates/reports/dataexporters/duplication/`: DuplicateExportInfo(getBlockSize, +3); DuplicateFileBlockExportInfo(getFile, +11); DuplicationExportInfo(getTitle, +7); DuplicationExporter(getDuplicationExportInfo, +1)
- `reports/src/main/java/nl/obren/sokrates/reports/dataexporters/files/`: FileExportInfo(getInstance, +10); FileListExporter(getAllFilesData, +1); FilesExportInfo(getTimestamp, +11)
- `reports/src/main/java/nl/obren/sokrates/reports/dataexporters/trends/`: MetricsTrendExporter(getMetricTrends, +2); MetricsTrendInfo(getId, +5); ReferenceResultsLoader(getRefData); ValueSnapshotPair(getSnapshot, +3)
- `reports/src/main/java/nl/obren/sokrates/reports/dataexporters/units/`: UnitInfoExport(getShortName, +25); UnitListExporter(getAllUnitsData); UnitsExportInfo(getTimestamp, +11)
- `reports/src/main/java/nl/obren/sokrates/reports/generators/explorers/`: FilesExplorerGenerators(exportJson)
- `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/`: BasicSourceCodeReportGenerator(report); CommitTrendsExtractors(getCommitsPerYear, +1); CommitsReportGenerator(addContributorsAnalysisToReport, +2); ConcernsReportGenerator(addConcernsToReport); ConditionalComplexityReportGenerator(addConditionalComplexityToReport); ContributorsReportGenerator(contributorConnections, +2); ContributorsReportUtils(addContributorsSection, +4); ControlsReportGenerator(generateReport, +1); DuplicationReportGenerator(getDuplicatesTable, +1); FileAgeReportGenerator(addFileAgeToReport); FileChurnReportGenerator(addFileHistoryToReport); FileSizeReportGenerator(addFileSizeToReport); FileTemporalDependenciesReportGenerator(addTemporalDependenciesToReport); FindingsReportGenerator(generateReport); ForceGraphExporter(export3DForceGraph); HistoryPerLanguageGenerator(getInstanceCommits, +8); LogicalComponentsReportGenerator(addCodeOrganizationToReport); MetricsListReportGenerator(generateReport); OverviewReportGenerator(addScopeAnalysisToReport, +2); RacingComponentsBarChartsExporter(export); TrendReportGenerator(generateReport); UnitsSizeReportGenerator(addUnitsSizeToReport); VisualizationTools(addDownloadLinks)
- `reports/src/main/java/nl/obren/sokrates/reports/landscape/data/`: ContributorExport(getContributor, +7); ContributorRepositoryInfoExport(getFirstCommitDate, +19); LandscapeDataExport(exportRepositories, +4); RepositoryExport(getSokratesRepositoryLink, +19)
- `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/`: LandscapeAnalysisCommands(update, +1); LandscapeContributorsReport(getContributorUrlFromTemplate, +5); LandscapeIndividualContributorsReports(getContributorIndividualReportFileName, +1); LandscapeReportContributorsTab(singular, +10); LandscapeReportGenerator(getContributionDays, +8); LandscapeReportPeopleTopologyTab(setLandscapeReport)
- `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/repositories/`: LandscapeRepositoriesReport(saveRepositoriesReport, +10); LandscapeRepositoriesTagsLine(addTagsLine); LandscapeRepositoriesTagsMatrixReport(saveRepositoriesReport); LandscapeRepositoriesTagsReport(saveRepositoriesReport); TagMap(updateTagMap, +5)
- `reports/src/main/java/nl/obren/sokrates/reports/landscape/utils/`: ContributorPerExtensionHelper(getContributorsPerExtensionStream, +2); ContributorsExtractor; CorrelationDiagramGenerator(addCorrelations, +2); Counter; ExtractStringListValue; FeaturesOfInterestAggregator(getRepositoryAnalysisResults, +12); Force3DGraphExporter(export2D3DForceGraph); LandscapeGeneratorUtils(getLinesOfCodePerExtension); LandscapeVisualsGenerator(exportVisuals); RacingLanguagesBarChartsExporter(exportRacingChart); RacingRepositoriesBarChartsExporter(exportRacingChart); RepositoryConcernData(getRepositoryName, +5); TagStats(getTag, +3)
- `reports/src/main/java/nl/obren/sokrates/reports/utils/`: AnimalIcons(getAnimals, +6); ChartUtils(getAspectsVolumePieChart, +1); DataImageUtils(getLangDataImage, +7); DateUtils(getCurrentDateTime); DuplicationReportUtils(getSystemDuplicationPieChart, +3); FilesReportUtils(getFilesTable); GraphvizDependencyRenderer(encodeLabel, +26); HtmlTemplateUtils(getResource); PieChartUtils(getRiskDistributionChart); PromptsUtils(addRepositoryPromptSection, +1); ReportUtils(getSvgBar, +2); RiskDistributionStatsReportUtils(getRowData, +1); ScopeRendererItem(setLinesOfCode, +5); ScopesRenderer(getAspectsFileListPaths, +39); ToStringFunction; UtilsReportUtils(getUnitsTable); ZipEntryContent(getName, +3); ZipUtils(stringToZipFile, +2)
