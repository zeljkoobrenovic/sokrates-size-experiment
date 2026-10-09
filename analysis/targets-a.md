# Size targets of variant `a` (reference date 2025-09-20)

Thresholds variant B must satisfy: file ≤ 500 LOC, unit ≤ 50 LOC and McCabe ≤ 25.
Sokrates' very-high-risk tier: file > 1000 LOC, unit > 100 LOC or McCabe > 50.

- main files: 489, 48,137 LOC
- files above the threshold: 14 (11,169 LOC, 23% of main code), of which very high risk: 4
- units above the threshold: 60 (5,146 LOC), of which very high risk: 11
- commits counted per file: 2024-09-20 … 2025-09-20

## Files

| tier | file | LOC | lines | units | units over threshold | biggest unit (LOC) | commits 365d |
|---|---|---|---|---|---|---|---|
| very high | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportGenerator.java` | 1,587 | 1,836 | 78 | 3 | 128 | 12 |
| very high | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/analysis/LandscapeAnalysisResults.java` | 1,133 | 1,405 | 153 | 2 | 80 | 11 |
| very high | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportContributorsTab.java` | 1,076 | 1,284 | 59 | 4 | 81 | 4 |
| very high | `reports/src/main/java/nl/obren/sokrates/reports/core/ReportFileExporter.java` | 1,062 | 1,204 | 25 | 4 | 407 | 10 |
| high | `reports/src/main/java/nl/obren/sokrates/reports/dataexporters/DataExporter.java` | 813 | 959 | 50 | 1 | 61 | 2 |
| high | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/repositories/LandscapeRepositoriesReport.java` | 793 | 905 | 40 | 3 | 75 | 4 |
| high | `cli/src/main/java/nl/obren/sokrates/cli/CommandLineInterface.java` | 744 | 904 | 39 | 3 | 59 | 3 |
| high | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/LogicalComponentsReportGenerator.java` | 637 | 736 | 35 | 1 | 53 | 3 |
| high | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportPeopleTopologyTab.java` | 627 | 711 | 27 | 1 | 54 | 2 |
| high | `reports/src/main/java/nl/obren/sokrates/reports/core/SummaryUtils.java` | 594 | 688 | 30 | 1 | 62 | 6 |
| high | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/LanguageAnalyzerFactory.java` | 544 | 637 | 19 | 2 | 149 | 0 |
| high | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/ContributorsReportGenerator.java` | 530 | 598 | 15 | 3 | 86 | 3 |
| high | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeIndividualContributorsReports.java` | 515 | 566 | 9 | 5 | 119 | 9 |
| high | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/scoping/ScopingConventions.java` | 514 | 635 | 19 | 4 | 150 | 6 |

## Units

| tier | unit | file | lines | LOC | McCabe | commits 365d (file) |
|---|---|---|---|---|---|---|
| very high | `private static void addVisuals()` | `reports/src/main/java/nl/obren/sokrates/reports/core/ReportFileExporter.java` | 367–792 | 407 | 16 | 10 |
| very high | `static` | `reports/src/main/java/nl/obren/sokrates/reports/utils/DataImageUtils.java` | 15–259 | 242 | 1 | 2 |
| very high | `public static void exportReportsIndexFile()` | `reports/src/main/java/nl/obren/sokrates/reports/core/ReportFileExporter.java` | 107–310 | 172 | 12 | 10 |
| very high | `private void addIgnoreConventions()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/scoping/ScopingConventions.java` | 403–594 | 150 | 1 | 6 |
| very high | `private LanguageAnalyzerFactory()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/LanguageAnalyzerFactory.java` | 70–268 | 149 | 1 | 0 |
| very high | `private void addSubLandscapeSection()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportGenerator.java` | 604–737 | 128 | 17 | 12 |
| very high | `private RichTextReport getIndividualReport()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeIndividualContributorsReports.java` | 50–184 | 119 | 12 | 9 |
| very high | `private void addOtherConventions()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/scoping/ScopingConventions.java` | 65–215 | 117 | 1 | 6 |
| very high | `private void registerXml()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/LanguageAnalyzerFactory.java` | 470–584 | 114 | 1 | 0 |
| very high | `private void addPerYear()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeIndividualContributorsReports.java` | 454–564 | 105 | 15 | 9 |
| very high | `private void addContributor()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeContributorsReport.java` | 119–225 | 102 | 19 | 6 |
| high | `private void addTagRow()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/repositories/LandscapeRepositoriesTagsReport.java` | 313–415 | 99 | 16 | 1 |
| high | `private void addPerWeek()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeIndividualContributorsReports.java` | 252–353 | 95 | 14 | 9 |
| high | `private static List getReportsList()` | `reports/src/main/java/nl/obren/sokrates/reports/core/ReportFileExporter.java` | 1093–1194 | 91 | 31 | 10 |
| high | `public void addUnitsSizeToReport()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/UnitsSizeReportGenerator.java` | 28–128 | 90 | 1 | 0 |
| high | `private void addMatrix()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/ContributorsReportGenerator.java` | 207–295 | 86 | 16 | 3 |
| high | `private static void addData()` | `reports/src/main/java/nl/obren/sokrates/reports/core/ReportFileExporter.java` | 794–897 | 83 | 1 | 10 |
| high | `public void addContributorsAnalysisToReport()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/ContributorsReportGenerator.java` | 95–188 | 82 | 4 | 3 |
| high | `private void addContributorsPerYear()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportGenerator.java` | 1677–1764 | 81 | 12 | 12 |
| high | `private void addContributorsPerYear()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportContributorsTab.java` | 741–828 | 81 | 12 | 4 |
| high | `private List getAllContributors()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/analysis/LandscapeAnalysisResults.java` | 781–875 | 80 | 9 | 11 |
| high | `public void addCorrelations()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/utils/CorrelationDiagramGenerator.java` | 26–114 | 80 | 8 | 1 |
| high | `public void addFileSizeToReport()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/FileSizeReportGenerator.java` | 39–137 | 80 | 3 | 3 |
| high | `public void addZooSection()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportGenerator.java` | 1385–1477 | 79 | 18 | 12 |
| high | `public void addContributorsPanel()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/ContributorsReportGenerator.java` | 510–593 | 79 | 14 | 3 |
| high | `private void createBasicReport()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/BasicSourceCodeReportGenerator.java` | 157–245 | 79 | 13 | 2 |
| high | `private void addPerMonth()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeIndividualContributorsReports.java` | 355–435 | 79 | 12 | 9 |
| high | `public void addConditionalComplexityToReport()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/ConditionalComplexityReportGenerator.java` | 28–116 | 77 | 1 | 0 |
| high | `public static void addContributorsPerTimeSlot()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/ContributorsReportUtils.java` | 50–131 | 75 | 20 | 0 |
| high | `private void addHistory()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/repositories/LandscapeRepositoriesReport.java` | 653–737 | 75 | 11 | 4 |
| high | `private void addMetricsTable()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/repositories/LandscapeRepositoriesReport.java` | 551–635 | 73 | 11 | 4 |
| high | `private void addRecentContributorsSection()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportContributorsTab.java` | 484–573 | 73 | 9 | 4 |
| high | `private void addContributors()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportContributorsTab.java` | 393–482 | 72 | 5 | 4 |
| high | `private void addTagStats()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/repositories/LandscapeRepositoriesTagsReport.java` | 61–135 | 70 | 10 | 1 |
| high | `public void renderDetails()` | `reports/src/main/java/nl/obren/sokrates/reports/utils/ScopesRenderer.java` | 274–348 | 69 | 16 | 0 |
| high | `public static String getFilesTable()` | `reports/src/main/java/nl/obren/sokrates/reports/utils/FilesReportUtils.java` | 22–99 | 68 | 9 | 0 |
| high | `public List getContributorsPerExtension()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/analysis/LandscapeAnalysisResults.java` | 626–701 | 64 | 7 | 11 |
| high | `private void addTestConventions()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/scoping/ScopingConventions.java` | 336–401 | 63 | 1 | 6 |
| high | `private void addGeneratedConventions()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/scoping/ScopingConventions.java` | 267–334 | 62 | 1 | 6 |
| high | `public void summarizeAndCompare()` | `reports/src/main/java/nl/obren/sokrates/reports/core/SummaryUtils.java` | 156–234 | 62 | 1 | 6 |
| high | `private void exportJson()` | `reports/src/main/java/nl/obren/sokrates/reports/dataexporters/DataExporter.java` | 587–656 | 61 | 5 | 2 |
| high | `private void addMembers()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeIndividualContributorsReports.java` | 186–249 | 61 | 5 | 9 |
| high | `private void generateVisuals()` | `cli/src/main/java/nl/obren/sokrates/cli/CommandLineInterface.java` | 654–723 | 59 | 7 | 3 |
| high | `public void analyze()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/analysis/files/UnitsAnalyzer.java` | 57–128 | 59 | 1 | 0 |
| high | `public LandscapeAnalysisResults analyze()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/analysis/LandscapeAnalyzer.java` | 36–98 | 58 | 11 | 2 |
| high | `private void addFeaturesOfInterest()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/repositories/LandscapeRepositoriesReport.java` | 120–184 | 56 | 7 | 4 |
| high | `public CodeAnalysisResults analyze()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/analysis/CodeAnalyzer.java` | 43–110 | 54 | 8 | 0 |
| high | `private void renderScopes()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/ConcernsReportGenerator.java` | 128–189 | 54 | 8 | 0 |
| high | `private void appendHeader()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/OverviewReportGenerator.java` | 170–227 | 54 | 8 | 0 |
| high | `private void addRepositoryContributors()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportPeopleTopologyTab.java` | 427–485 | 54 | 7 | 2 |
| high | `public static void generateReport()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeAnalysisCommands.java` | 52–112 | 54 | 3 | 3 |
| high | `private void addDuplicationInstances()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/duplication/impl/Blocks.java` | 144–207 | 53 | 11 | 0 |
| high | `public static List getPeopleDependencies()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/ContributorConnectionUtils.java` | 47–107 | 53 | 8 | 5 |
| high | `private void addComponentDependenciesSection()` | `reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/LogicalComponentsReportGenerator.java` | 262–321 | 53 | 5 | 3 |
| high | `private void exportTagGraphs()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/repositories/LandscapeRepositoriesTagsReport.java` | 197–252 | 53 | 5 | 1 |
| high | `private void processRepositoryMonth()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/utils/RacingRepositoriesBarChartsExporter.java` | 50–103 | 52 | 16 | 0 |
| high | `private void init()` | `cli/src/main/java/nl/obren/sokrates/cli/CommandLineInterface.java` | 301–362 | 52 | 13 | 3 |
| high | `public static List getPeopleDependencies()` | `codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/ContributorConnectionUtils.java` | 146–202 | 52 | 8 | 5 |
| high | `private void generateReports()` | `cli/src/main/java/nl/obren/sokrates/cli/CommandLineInterface.java` | 502–565 | 51 | 7 | 3 |
| high | `private void addContributorDependencies()` | `reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportContributorsTab.java` | 322–377 | 51 | 5 | 4 |
