/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.common.renderingutils.RichTextRenderingUtils;
import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.common.utils.ProcessingStopwatch;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.dataexporters.ReportDataFiles;
import nl.obren.sokrates.reports.landscape.utils.CorrelationDiagramGenerator;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.FileAgeDistributionPerLogicalDecomposition;
import nl.obren.sokrates.sourcecode.analysis.results.FilesHistoryAnalysisResults;
import nl.obren.sokrates.sourcecode.aspects.LogicalDecomposition;
import nl.obren.sokrates.sourcecode.filehistory.FileModificationHistory;
import nl.obren.sokrates.sourcecode.stats.RiskDistributionStats;
import nl.obren.sokrates.sourcecode.stats.SourceFileChangeDistribution;
import nl.obren.sokrates.sourcecode.threshold.Thresholds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The "File Change Frequency" report page (output {@code FileChangeFrequency.html}).
 * <p>
 * Synonyms not used in the code: churn = change frequency = update frequency = how often files change,
 * hot spots, volatile files.
 * <p>
 * Changes together with: {@link FileSizeReportGenerator} (same risk-distribution sections),
 * {@link FileAgeReportGenerator}, {@code BasicSourceCodeReportGenerator} (field {@code fileChangeFrequencyReport}),
 * {@code ReportFileExporter} and {@code SummaryUtils} (links to {@code FileChangeFrequency.html}),
 * and {@code ContributorConnectionUtils} (codeanalyzer).
 */
public class FileChurnReportGenerator {
    public static final String THE_NUMBER_OF_FILE_CHANGES = "File Change Frequency";
    public static final String THE_NUMBER_OF_FILE_CHANGES_DESCRIPTION = "The number of recorded file updates";
    public static final String THE_CONTRIBUTORS_COUNT = "Contributors Count Frequency";
    public static final String THE_CONTRIBUTORS_COUNT_DESCRIPTION = "The number of file contributors";
    private final Thresholds thresholds;
    private final Thresholds thresholdsContributors;

    private CodeAnalysisResults codeAnalysisResults;
    private List<String> changeFrequencyLabels;
    private List<String> contributorsCountFrequencyLabels;
    private int graphCounter = 1;

    public FileChurnReportGenerator(CodeAnalysisResults codeAnalysisResults) {
        this.codeAnalysisResults = codeAnalysisResults;
        thresholds = codeAnalysisResults.getCodeConfiguration().getAnalysis().getFileUpdateFrequencyThresholds();
        thresholdsContributors = codeAnalysisResults.getCodeConfiguration().getAnalysis().getFileContributorsCountThresholds();
        changeFrequencyLabels = thresholds.getLabels();
        contributorsCountFrequencyLabels = thresholdsContributors.getLabels();
    }

    public void addFileHistoryToReport(RichTextReport report) {
        report.addParagraph("File change frequency (churn) shows the distribution of file updates " +
                "(days with at least one commit).", "margin-top: 12px; color: grey");

        addOverallSections(report);

        addGraphsPerExtension(report);
        addGraphsPerLogicalComponents(report);
        addMostChangedFilesList(report);
        addFilesWithMostContributors(report);
        addFilesWithLeastContributors(report);
        addCorrelations(report);
    }


    private void addGraphsPerExtension(RichTextReport report) {
        FilesHistoryAnalysisResults ageAnalysisResults = codeAnalysisResults.getFilesHistoryAnalysisResults();

        String extensions = codeAnalysisResults.getCodeConfiguration().getExtensions().stream().collect(Collectors.joining(", "));
        report.startSection("File Change Frequency per File Extension", extensions);

        addChangesGraphPerExtension(report, ageAnalysisResults.getChangeDistributionPerExtension(),
                THE_NUMBER_OF_FILE_CHANGES + " per Extension", THE_NUMBER_OF_FILE_CHANGES_DESCRIPTION);

        report.endSection();
    }

    private void addOverallSections(RichTextReport report) {
        report.startSection("Overview", "");
        report.startSubSection("File Change Frequency Overall", "");
        addGraphOverallChange(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileChangeDistribution());
        report.addLineBreak();
        changeFrequencySection(report).addExploreLinks(
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoring(ReportDataFiles.COLORING_UPDATE_FREQUENCY)), "grouped by folders"),
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoringCategories(ReportDataFiles.COLORING_UPDATE_FREQUENCY)), "grouped by update frequency"),
                FileRiskDistributionSection.link(ReportDataFiles.textDataLink(ReportDataFiles.MAIN_FILES_WITH_HISTORY_TXT), "data"));
        report.endSection();

        report.startSubSection("Contributors Count Frequency Overall", "");
        addGraphOverallContributorCount(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getOverallContributorsCountDistribution());
        report.addLineBreak();
        contributorsCountSection(report).addExploreLinks(
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoring(ReportDataFiles.COLORING_CONTRIBUTORS_COUNT)), "grouped by folders"),
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoringCategories(ReportDataFiles.COLORING_CONTRIBUTORS_COUNT)), "grouped by contributors count"),
                FileRiskDistributionSection.link(ReportDataFiles.textDataLink(ReportDataFiles.MAIN_FILES_WITH_HISTORY_TXT), "data"));
        report.endSection();
        report.endSection();
    }

    private FileRiskDistributionSection changeFrequencySection(RichTextReport report) {
        return new FileRiskDistributionSection(report, changeFrequencyLabels, Palette.getHeatPalette());
    }

    private FileRiskDistributionSection contributorsCountSection(RichTextReport report) {
        return new FileRiskDistributionSection(report, contributorsCountFrequencyLabels, Palette.getHeatPalette());
    }

    private static String totalLine(SourceFileChangeDistribution distribution) {
        return "There are <b>"
                + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalCount())
                + "</b> files with " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalValue())
                + " lines of code" +
                ".";
    }

    private static String files(int count) {
        return count == 1 ? "file" : "files";
    }

    private void addGraphOverallChange(RichTextReport report, SourceFileChangeDistribution distribution) {
        changeFrequencySection(report).addBucketList(totalLine(distribution), distribution, (bucket, count) ->
                bucket == FileRiskDistributionSection.Bucket.VERY_HIGH
                        ? " " + files(count) + " changed more than " + thresholds.getVeryHigh() + " times"
                        : " " + files(count) + " changed " + bucket.label(thresholds) + " times");
    }

    private void addGraphOverallContributorCount(RichTextReport report, SourceFileChangeDistribution distribution) {
        contributorsCountSection(report).addBucketList(totalLine(distribution), distribution, (bucket, count) -> {
            switch (bucket) {
                case VERY_HIGH:
                    return " " + files(count) + " changed by more than " + thresholdsContributors.getVeryHigh() + " contributors";
                case NEGLIGIBLE:
                    return " " + files(count) + " changed by " + bucket.label(thresholdsContributors)
                            + " " + (thresholdsContributors.getLow() == 1 ? "contributor" : "contributors");
                default:
                    return " " + files(count) + " changed by " + bucket.label(thresholdsContributors) + " contributors";
            }
        });
    }

    private void addChangesGraphPerExtension(RichTextReport report, List<RiskDistributionStats> sourceFileAgeDistribution, String title, String subtitle) {
        changeFrequencySection(report).addChartPerKeySubSection(title, subtitle, sourceFileAgeDistribution);
    }

    private void addGraphsPerLogicalComponents(RichTextReport report) {
        String components = codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions().stream().map(c -> c.getName()).collect(Collectors.joining(", "));

        report.startSection("File Change Frequency per Logical Decomposition", components);

        addChangesPerLogicalDecomposition(report);

        report.endSection();
    }

    private LogicalDecomposition getLogicalDecompositionByName(String name) {
        for (LogicalDecomposition logicalDecomposition : this.codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions()) {
            if (logicalDecomposition.getName().equalsIgnoreCase(name)) {
                return logicalDecomposition;
            }
        }

        return null;
    }

    private void addChangesPerLogicalDecomposition(RichTextReport report) {
        codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions().forEach(logicalDecomposition -> {
            String name = logicalDecomposition.getName();
            FileRiskDistributionSection.matchingDecomposition(codeAnalysisResults.getFilesHistoryAnalysisResults().getChangeDistributionPerLogicalDecomposition(),
                    FileAgeDistributionPerLogicalDecomposition::getName, name).forEach(distribution -> {
                addChangeDetailsForLogicalDecomposition(report, distribution);
            });
        });
    }

    private void addChangeDetailsForLogicalDecomposition(RichTextReport report, FileAgeDistributionPerLogicalDecomposition logicalDecomposition) {
        changeFrequencySection(report).addChartPerComponent("" + logicalDecomposition.getName()
                        + " (" + THE_NUMBER_OF_FILE_CHANGES.toLowerCase() + ")", THE_NUMBER_OF_FILE_CHANGES_DESCRIPTION,
                logicalDecomposition.getDistributionPerComponent(), FileRiskDistributionSection.SCROLLING_DIV_STYLE);
    }

    private void addHistoryFilesTable(RichTextReport report, String title, String subtitle, List<SourceFile> files) {
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        String dataLink = "<a href='" + ReportDataFiles.textDataLink(ReportDataFiles.MAIN_FILES_WITH_HISTORY_TXT) + "' target='_blank'>See data for all files...</a>";
        changeFrequencySection(report).addFilesTable(title, subtitle, dataLink, files, cacheSourceFiles, true, false, 500);
    }

    private void addMostChangedFilesList(RichTextReport report) {
        List<SourceFile> youngestFiles = codeAnalysisResults.getFilesHistoryAnalysisResults().getMostChangedFiles();
        addHistoryFilesTable(report, "Most Frequently Changed Files", "", youngestFiles);
    }

    private void addFilesWithMostContributors(RichTextReport report) {
        List<SourceFile> files = codeAnalysisResults.getFilesHistoryAnalysisResults().getFilesWithMostContributors();
        addHistoryFilesTable(report, "Files With Most Contributors", "Based on the number of unique email addresses found in commits.", files);
    }

    private void addFilesWithLeastContributors(RichTextReport report) {
        List<SourceFile> files = codeAnalysisResults.getFilesHistoryAnalysisResults().getFilesWithLeastContributors();
        addHistoryFilesTable(report, "Files With Least Contributors", "Based on the number of unique email addresses found in commits.", files);
    }

    private void addCorrelations(RichTextReport report) {
        if (!codeAnalysisResults.getCodeConfiguration().getAnalysis().isSkipCorrelations() && codeAnalysisResults.getContributorsAnalysisResults().getContributors().size() > 0) {
            report.startSection("Correlations", "");
            CorrelationDiagramGenerator<FileModificationHistory> correlationDiagramGenerator = new CorrelationDiagramGenerator<>(report,
                    codeAnalysisResults.getFilesHistoryAnalysisResults().getHistory(Integer.MAX_VALUE));

            final Map<String,Integer> linesOfCodeMap = new HashMap<>();
            final Map<String,Integer> countributorsCountMap = new HashMap<>();

            codeAnalysisResults.getFilesAnalysisResults().getAllFiles().forEach(sourceFile -> {
                linesOfCodeMap.put(sourceFile.getRelativePath(), sourceFile.getLinesOfCode());
                countributorsCountMap.put(sourceFile.getRelativePath(), sourceFile.getFileModificationHistory().countContributors());
            });
            ProcessingStopwatch.start("reporting/file update frequency/correlations");
            correlationDiagramGenerator.addCorrelations("File Size vs. Number of Changes", "lines of code", "# changes",
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getDates().size(),
                    p -> p.getPath());

            correlationDiagramGenerator.addCorrelations("Number of Contributors vs. Number of Changes", "# contributors", "# changes",
                    p -> countributorsCountMap.getOrDefault(p.getPath(), 0),
                    p -> p.getDates().size(),
                    p -> p.getPath());

            correlationDiagramGenerator.addCorrelations("Number of Contributors vs. File Size", "# contributors", "lines of code",
                    p -> countributorsCountMap.getOrDefault(p.getPath(), 0),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());

            ProcessingStopwatch.end("reporting/file update frequency/correlations");
            report.endSection();
        }
    }
}
