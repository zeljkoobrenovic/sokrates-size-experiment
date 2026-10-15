/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.common.renderingutils.RichTextRenderingUtils;
import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.dataexporters.ReportDataFiles;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.FileAgeDistributionPerLogicalDecomposition;
import nl.obren.sokrates.sourcecode.analysis.results.FilesHistoryAnalysisResults;
import nl.obren.sokrates.sourcecode.filehistory.FileHistoryComponentsHelper;
import nl.obren.sokrates.sourcecode.filehistory.FileHistoryUtils;
import nl.obren.sokrates.sourcecode.filehistory.FileModificationHistory;
import nl.obren.sokrates.sourcecode.stats.RiskDistributionStats;
import nl.obren.sokrates.sourcecode.stats.SourceFileAgeDistribution;
import nl.obren.sokrates.sourcecode.threshold.Thresholds;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The "File Age &amp; Freshness" report page (output {@code FileAge.html}).
 * <p>
 * Synonyms not used in the code: age, freshness, last modified, last changed, stale files, old code, new code.
 * Note: the page is held by the field {@code fileHistoryReport} in {@code BasicSourceCodeReportGenerator},
 * not by a field called "fileAge".
 * <p>
 * Changes together with: {@link FileChurnReportGenerator} and {@link FileSizeReportGenerator}
 * (same risk-distribution sections), {@code ReportFileExporter} (index "age" info blocks link to {@code FileAge.html})
 * and {@code SummaryUtils}.
 */
public class FileAgeReportGenerator {
    public static final String LATEST_CHANGE_DISTRIBUTION = "File Freshness Distribution";
    public static final String FILE_AGE_DISTRIBUTION = "File Age Distribution";
    public static final String FILE_AGE_DESCRIPTION = "Days since first update";
    public static final String LATEST_CHANGE_DESCRIPTION = "Days since last update";
    private final Thresholds thresholds;

    private CodeAnalysisResults codeAnalysisResults;
    private List<String> ageLabels;
    private int daysBetween;
    private int estimatedWorkingDays;

    public FileAgeReportGenerator(CodeAnalysisResults codeAnalysisResults) {
        this.codeAnalysisResults = codeAnalysisResults;
        thresholds = codeAnalysisResults.getCodeConfiguration().getAnalysis().getFileAgeThresholds();
        this.ageLabels = thresholds.getLabels();
    }

    public void addFileAgeToReport(RichTextReport report) {
        describe(report);

        addOverallSections(report);

        addGraphsPerExtension(report);

        addGraphsPerLogicalComponents(report);

        addOldestFilesList(report);
        addMostPreviouslyChangedFilesList(report);
        addYoungestFilesList(report);
        addMostRecentlyChangedFilesList(report);
    }

    private void addSummary(RichTextReport report) {
        FileHistoryComponentsHelper helper = new FileHistoryComponentsHelper();

        List<FileModificationHistory> history = codeAnalysisResults.getFilesHistoryAnalysisResults().getHistory(Integer.MAX_VALUE);
        List<String> uniqueDates = helper.getUniqueDates(history);

        if (uniqueDates.size() > 1) {
            String firstDateString = uniqueDates.get(0);
            String latestDateString = uniqueDates.get(uniqueDates.size() - 1);

            Date firstDate = FileHistoryUtils.getDateFromString(firstDateString);
            Date latestDate = FileHistoryUtils.getDateFromString(latestDateString);

            this.daysBetween = FileHistoryUtils.daysBetween(firstDate, latestDate);

            int weeks = daysBetween / 7;
            estimatedWorkingDays = weeks * 5;

            report.startSection("Summary", "");

            report.startUnorderedList();

            report.addListItem("Number of files: <b>" + FormattingUtils.formatCount(codeAnalysisResults.getMainAspectAnalysisResults().getFilesCount()) + "</b>");
            report.addListItem("Daily file updates (only one update per file and date counted): <b>" + history.size() + "</b>");
            report.addListItem("First update: <b>" + firstDateString + "</b>");
            report.addListItem("Latest update: <b>" + latestDateString + "</b>");
            report.addListItem("Days between first and latest update: <b>" + FormattingUtils.formatCount(daysBetween) + "</b> (" + weeks + " weeks, estimated " + FormattingUtils.formatCount(estimatedWorkingDays) + " working days)");
            report.addListItem("Active days (at least one file change): <b>" + FormattingUtils.formatCount(uniqueDates.size()) + "</b>");
            int ignoredFiles = codeAnalysisResults.getFilesHistoryAnalysisResults().getFilesWithoutCommitHistoryCount();
            if (ignoredFiles > 0) {
                report.addListItem("Files without commit history (ignored): <b>" + FormattingUtils.formatCount(ignoredFiles) + " (" + FormattingUtils.formatCount(codeAnalysisResults.getFilesHistoryAnalysisResults().getFilesWithoutCommitHistoryLinesOfCode()) + " lines of code)</b>");
                report.startUnorderedList();
                report.addListItem("<a href='" + ReportDataFiles.textDataLink(ReportDataFiles.MAIN_FILES_WITHOUT_HISTORY_TXT) + "' target='_blank'>Files without history</a>");
                report.endUnorderedList();
            }
            report.addListItem("Data:");
            report.startUnorderedList();
            report.addListItem("<a href='" + ReportDataFiles.textDataLink(ReportDataFiles.MAIN_FILES_WITH_HISTORY_TXT) + "' target='_blank'>Organized per file</a>");
            report.endUnorderedList();
            report.endUnorderedList();
            report.endSection();
        }
    }

    private void addGraphsPerExtension(RichTextReport report) {
        FilesHistoryAnalysisResults ageAnalysisResults = codeAnalysisResults.getFilesHistoryAnalysisResults();

        String extensions = codeAnalysisResults.getCodeConfiguration().getExtensions().stream().collect(Collectors.joining(", "));
        report.startSection("File Change History per File Extension", extensions);

        addGraphPerExtension(report, ageAnalysisResults.getFirstModifiedDistributionPerExtension(),
                FILE_AGE_DISTRIBUTION + " per Extension", FILE_AGE_DESCRIPTION, Palette.getAgePalette());
        addGraphPerExtension(report, ageAnalysisResults.getLastModifiedDistributionPerExtension(),
                LATEST_CHANGE_DISTRIBUTION + " per Extension", LATEST_CHANGE_DESCRIPTION, Palette.getFreshnessPalette());

        report.endSection();
    }

    private void addOverallSections(RichTextReport report) {
        addSummary(report);

        report.startSection("File Change History Overall", "");
        addAgeGraphOverall(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileFirstModifiedDistribution(),
                FILE_AGE_DISTRIBUTION + " Overall", FILE_AGE_DESCRIPTION, Palette.getAgePalette());
        addFreshnessGraphOverall(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileLastModifiedDistribution(), "" +
                LATEST_CHANGE_DISTRIBUTION + " Overall", LATEST_CHANGE_DESCRIPTION, Palette.getFreshnessPalette());

        report.endSection();
    }

    private void describe(RichTextReport report) {
        report.addParagraph("File age measurements show the distribution of file ages (days since the first commit)" +
                " and the file freshness (days since the latest commit).", "margin-top: 12px; color: grey");
    }

    private FileRiskDistributionSection section(RichTextReport report, Palette palette) {
        return new FileRiskDistributionSection(report, ageLabels, palette);
    }

    private static String totalLine(SourceFileAgeDistribution distribution) {
        return "There are " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalCount())
                + " files with " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalValue())
                + " lines of code in files" +
                ".";
    }

    private void addAgeGraphOverall(RichTextReport report, SourceFileAgeDistribution distribution, String title, String subtitle, Palette palette) {
        report.startSubSection(title, subtitle);
        FileRiskDistributionSection section = section(report, palette);
        section.addBucketList(totalLine(distribution), distribution,
                (bucket, count) -> " files that are " + bucket.label(thresholds) + " days old");
        report.addLineBreak();
        section.addExploreLinks(
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoring(ReportDataFiles.COLORING_AGE)), "grouped by folders"),
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoringCategories(ReportDataFiles.COLORING_AGE)), "grouped by age"));
        report.endSection();
    }

    private void addFreshnessGraphOverall(RichTextReport report, SourceFileAgeDistribution distribution, String title, String subtitle, Palette palette) {
        report.startSubSection(title, subtitle);
        FileRiskDistributionSection section = section(report, palette);
        // the "high" line has always started with two spaces; kept as is
        section.addBucketList(totalLine(distribution), distribution,
                (bucket, count) -> (bucket == FileRiskDistributionSection.Bucket.HIGH ? "  " : " ")
                        + "files have been last changed " + bucket.label(thresholds) + " days ago");
        report.addLineBreak();
        section.addExploreLinks(
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoring(ReportDataFiles.COLORING_FRESHNESS)), "grouped by folders"),
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoringCategories(ReportDataFiles.COLORING_FRESHNESS)), "grouped by freshness"));
        report.endSection();
    }

    private void addGraphPerExtension(RichTextReport report, List<RiskDistributionStats> sourceFileAgeDistribution, String title, String subtitle, Palette palette) {
        section(report, palette).addChartPerKeySubSection(title, subtitle, sourceFileAgeDistribution);
    }

    private void addGraphsPerLogicalComponents(RichTextReport report) {
        String components = codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions().stream().map(c -> c.getName()).collect(Collectors.joining(", "));

        report.startSection("File Change History per Logical Decomposition", components);

        addChangesPerLogicalDecomposition(report);

        report.endSection();
    }

    private void addChangesPerLogicalDecomposition(RichTextReport report) {
        codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions().forEach(logicalDecomposition -> {
            String name = logicalDecomposition.getName();
            FileRiskDistributionSection.matchingDecomposition(codeAnalysisResults.getFilesHistoryAnalysisResults().getFirstModifiedDistributionPerLogicalDecomposition(),
                    FileAgeDistributionPerLogicalDecomposition::getName, name).forEach(distribution -> {
                addFirstModifiedDetailsForLogicalDecomposition(report, name, distribution.getDistributionPerComponent());
            });
            FileRiskDistributionSection.matchingDecomposition(codeAnalysisResults.getFilesHistoryAnalysisResults().getLastModifiedDistributionPerLogicalDecomposition(),
                    FileAgeDistributionPerLogicalDecomposition::getName, name).forEach(distribution -> {
                addLastModifiedPerLogicalComponent(report, name, distribution.getDistributionPerComponent());
            });
        });
    }

    private void addFirstModifiedDetailsForLogicalDecomposition(RichTextReport report, String name, List<RiskDistributionStats> distributionsPerComponent) {
        section(report, Palette.getAgePalette()).addChartPerComponent("" + name + " (" + FILE_AGE_DISTRIBUTION.toLowerCase() + ")", FILE_AGE_DESCRIPTION,
                distributionsPerComponent, FileRiskDistributionSection.COMPACT_DIV_STYLE);
    }

    private void addLastModifiedPerLogicalComponent(RichTextReport report, String name, List<RiskDistributionStats> distributionsPerComponent) {
        section(report, Palette.getFreshnessPalette()).addChartPerComponent("" + name + " (" + LATEST_CHANGE_DISTRIBUTION.toLowerCase() + ")", LATEST_CHANGE_DESCRIPTION,
                distributionsPerComponent, FileRiskDistributionSection.COMPACT_DIV_STYLE);
    }

    private void addHistoryFilesTable(RichTextReport report, String title, List<SourceFile> files) {
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        section(report, Palette.getAgePalette()).addFilesTable(title, "", null, files, cacheSourceFiles, true, false);
    }

    private void addOldestFilesList(RichTextReport report) {
        addHistoryFilesTable(report, "Oldest Files", codeAnalysisResults.getFilesHistoryAnalysisResults().getOldestFiles());
    }

    private void addYoungestFilesList(RichTextReport report) {
        addHistoryFilesTable(report, "Most Recently Created Files", codeAnalysisResults.getFilesHistoryAnalysisResults().getYoungestFiles());
    }

    private void addMostRecentlyChangedFilesList(RichTextReport report) {
        addHistoryFilesTable(report, "Most Recently Changed Files", codeAnalysisResults.getFilesHistoryAnalysisResults().getMostRecentlyChangedFiles());
    }

    private void addMostPreviouslyChangedFilesList(RichTextReport report) {
        addHistoryFilesTable(report, "Files Not Recently Changed", codeAnalysisResults.getFilesHistoryAnalysisResults().getMostPreviouslyChangedFiles());
    }
}
