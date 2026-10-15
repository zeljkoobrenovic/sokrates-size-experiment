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
import nl.obren.sokrates.sourcecode.analysis.results.FileDistributionPerLogicalDecomposition;
import nl.obren.sokrates.sourcecode.filehistory.ActivityWindows;
import nl.obren.sokrates.sourcecode.filehistory.CommitInfo;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.filehistory.FileModificationHistory;
import nl.obren.sokrates.sourcecode.stats.RiskDistributionStats;
import nl.obren.sokrates.sourcecode.stats.SourceFileSizeDistribution;
import nl.obren.sokrates.sourcecode.threshold.Thresholds;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSizeReportGenerator {
    private final Thresholds fileSizeThresholds;
    private CodeAnalysisResults codeAnalysisResults;
    private List<String> labels;

    private static final Map<FileRiskDistributionSection.Bucket, String> SIZE_BUCKET_NAMES = new EnumMap<>(FileRiskDistributionSection.Bucket.class);

    static {
        SIZE_BUCKET_NAMES.put(FileRiskDistributionSection.Bucket.VERY_HIGH, "very long");
        SIZE_BUCKET_NAMES.put(FileRiskDistributionSection.Bucket.HIGH, "long");
        SIZE_BUCKET_NAMES.put(FileRiskDistributionSection.Bucket.MEDIUM, "medium size");
        SIZE_BUCKET_NAMES.put(FileRiskDistributionSection.Bucket.LOW, "small");
        SIZE_BUCKET_NAMES.put(FileRiskDistributionSection.Bucket.NEGLIGIBLE, "very small");
    }

    public FileSizeReportGenerator(CodeAnalysisResults codeAnalysisResults) {
        this.codeAnalysisResults = codeAnalysisResults;
        fileSizeThresholds = codeAnalysisResults.getCodeConfiguration().getAnalysis().getFileSizeThresholds();
        this.labels = fileSizeThresholds.getLabels();
    }

    public void addFileSizeToReport(RichTextReport report) {
        report.addParagraph("The distribution of size of files (measured in lines of code).", "margin-top: 12px; color: grey");
        report.startSection("Intro", "");
        report.startUnorderedList();
        report.addListItem("File size measurements show the distribution of size of files.");
        report.addListItem("Files are classified in four categories based on their size (lines of code): " +
                fileSizeThresholds.getNegligibleRiskLabel() + " (very small files), " +
                fileSizeThresholds.getLowRiskLabel() + " (small files), " +
                fileSizeThresholds.getMediumRiskLabel() + " (medium size files), " +
                fileSizeThresholds.getHighRiskLabel() + " (long files), " +
                fileSizeThresholds.getVeryHighRiskLabel() + "(very long files).");
        report.addListItem("It is a good practice to keep files small. Long files may become \"bloaters\", code that have increased to such gargantuan proportions that they are hard to work with.");
        report.endUnorderedList();
        report.endUnorderedList();

        report.startShowMoreBlock("Learn more...");
        report.startUnorderedList();
        report.addListItem("To learn more about bloaters and how to deal with long code structures, Sokrates recommends the following resources:");
        report.startUnorderedList();
        report.addListItem("<a target='_blank' href='https://sourcemaking.com/refactoring/smells/bloaters'>Refactoring bloaters</a>, sourcemaking.com");
        report.addListItem("<a target='_blank' href='https://sourcemaking.com/antipatterns/the-blob'>The Blob Software Development Anti-Pattern</a>, sourcemaking.com");

        report.endUnorderedList();
        report.endUnorderedList();
        report.endShowMoreBlock();
        report.endSection();

        ProcessingStopwatch.start("reporting/file size/overall");
        addGraphOverall(report, codeAnalysisResults.getFilesAnalysisResults().getOverallFileSizeDistribution());
        ProcessingStopwatch.end("reporting/file size/overall");
        ProcessingStopwatch.start("reporting/file size/per extension");
        addGraphPerExtension(report, codeAnalysisResults.getFilesAnalysisResults().getFileSizeDistributionPerExtension());
        ProcessingStopwatch.end("reporting/file size/per extension");
        ProcessingStopwatch.start("reporting/file size/per logical component");
        addGraphsPerLogicalComponents(report, codeAnalysisResults.getFilesAnalysisResults().getFileSizeDistributionPerLogicalDecomposition());
        ProcessingStopwatch.end("reporting/file size/per logical component");

        ProcessingStopwatch.start("reporting/file size/longest files");
        addLongestFilesList(report);
        ProcessingStopwatch.end("reporting/file size/longest files");
        ProcessingStopwatch.start("reporting/file size/files with most units");
        addFilesWithMostUnitsList(report);
        ProcessingStopwatch.end("reporting/file size/files with most units");
        ProcessingStopwatch.start("reporting/file size/files with most long lines");
        addFilesWithMostLongLines(report);
        ProcessingStopwatch.end("reporting/file size/files with most long lines");

        if (!codeAnalysisResults.getCodeConfiguration().getAnalysis().isSkipCorrelations() && codeAnalysisResults.getContributorsAnalysisResults().getContributors().size() > 0) {
            report.startSection("Correlations", "");
            CorrelationDiagramGenerator<FileModificationHistory> correlationDiagramGenerator = new CorrelationDiagramGenerator<>(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getHistory(Integer.MAX_VALUE));

            final Map<String,Integer> linesOfCodeMap = new HashMap<>();

            codeAnalysisResults.getFilesAnalysisResults().getAllFiles().forEach(sourceFile -> {
                linesOfCodeMap.put(sourceFile.getRelativePath(), sourceFile.getLinesOfCode());
            });

            ProcessingStopwatch.start("reporting/file size/correlations");
            correlationDiagramGenerator.addCorrelations("File Size vs. Commits (all time)", "commits (all time)", "lines of code",
                    p -> p.getCommits().size(),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());

            correlationDiagramGenerator.addCorrelations("File Size vs. Contributors (all time)", "contributors (all time)", "lines of code",
                    p -> p.countContributors(),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());


            report.addHorizontalLine();

            correlationDiagramGenerator.addCorrelations("File Size vs. Commits (30 days)", "commits (30d)", "lines of code",
                    p -> p.getCommits().stream().filter(c -> DateUtils.isDateWithinRange(c.getDate(), ActivityWindows.DAYS_30)).count(),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());

            correlationDiagramGenerator.addCorrelations("File Size vs. Contributors (30 days)", "contributors (30d)", "lines of code",
                    p -> countContributors(p, ActivityWindows.DAYS_30),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());

            report.addHorizontalLine();

            correlationDiagramGenerator.addCorrelations("File Size vs. Commits (90 days)", "commits (90d)", "lines of code",
                    p -> p.getCommits().stream().filter(c -> DateUtils.isDateWithinRange(c.getDate(), ActivityWindows.DAYS_90)).count(),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());


            correlationDiagramGenerator.addCorrelations("File Size vs. Contributors (90 days)", "contributors (90d)", "lines of code",
                    p -> countContributors(p, ActivityWindows.DAYS_90),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());


            ProcessingStopwatch.end("reporting/file size/correlations");
            report.endSection();
        }
    }

    private long countContributors(FileModificationHistory p, int rangeInDays) {
        Stream<CommitInfo> commitInfoStream = p.getCommits().stream().filter(c -> DateUtils.isDateWithinRange(c.getDate(), rangeInDays));
        Set<String> contributorIds = new HashSet<>();
        commitInfoStream.forEach(commit -> contributorIds.add(commit.getEmail()));
        return contributorIds.size();
    }

    private FileRiskDistributionSection section(RichTextReport report) {
        return new FileRiskDistributionSection(report, labels, Palette.getRiskPalette());
    }

    private void addGraphOverall(RichTextReport report, SourceFileSizeDistribution distribution) {
        report.startSection("File Size Overall", "");
        FileRiskDistributionSection section = section(report);
        section.addBucketList("There are "
                        + "<a href='" + ReportDataFiles.textDataLink(ReportDataFiles.MAIN_FILES_TXT) + "' target='_blank'>"
                        + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalCount())
                        + " files</a> with " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalValue())
                        + " lines of code" +
                        ".",
                distribution,
                (bucket, count) -> " " + SIZE_BUCKET_NAMES.get(bucket) + " files",
                // the medium bucket keeps its corrupt suffix (a known defect, preserved to keep the report unchanged)
                (bucket, count) -> bucket == FileRiskDistributionSection.Bucket.MEDIUM
                        ? " lines of codeclsfd_ftr_w_mp_ins)"
                        : FileRiskDistributionSection.LINES_OF_CODE_SUFFIX);
        report.addLineBreak();
        report.addLineBreak();
        // the sunburst link and the separator after it have always been one fragment (one line in the page)
        section.addExploreFragments(
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoring(ReportDataFiles.COLORING_LOC)), "grouped by folders"),
                FileRiskDistributionSection.EXPLORE_SEPARATOR,
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableCirclesMainColoringCategories(ReportDataFiles.COLORING_LOC)), "grouped by size"),
                FileRiskDistributionSection.EXPLORE_SEPARATOR,
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.zoomableSunburst("main")), "sunburst") + FileRiskDistributionSection.EXPLORE_SEPARATOR,
                FileRiskDistributionSection.link(ReportDataFiles.visualLink(ReportDataFiles.FILES_3D_HTML), "3D view"));
        report.endSection();
    }

    private void addGraphPerExtension(RichTextReport report, List<RiskDistributionStats> sourceFileSizeDistribution) {
        section(report).addChartPerKeySection("File Size per Extension", "", sourceFileSizeDistribution);
    }

    private void addGraphsPerLogicalComponents(RichTextReport report, List<FileDistributionPerLogicalDecomposition> fileDistributionPerLogicalDecompositions) {
        report.startSection("File Size per Logical Decomposition", "");
        FileRiskDistributionSection section = section(report);
        fileDistributionPerLogicalDecompositions.forEach(logicalDecomposition -> {
            section.addChartPerComponent("" + logicalDecomposition.getName() + "", "",
                    logicalDecomposition.getFileSizeDistributionPerComponent(), FileRiskDistributionSection.SCROLLING_DIV_STYLE);
        });
        report.endSection();
    }

    private boolean isCacheSourceFiles() {
        return codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
    }

    private void addLongestFilesList(RichTextReport report) {
        List<SourceFile> longestFiles = codeAnalysisResults.getFilesAnalysisResults().getLongestFiles();
        section(report).addFilesTable("Longest Files", "", null, longestFiles, isCacheSourceFiles(), false, false);
    }

    private void addFilesWithMostUnitsList(RichTextReport report) {
        List<SourceFile> filesList = codeAnalysisResults.getFilesAnalysisResults().getFilesWithMostUnits();
        filesList.sort((o1, o2) -> o2.getUnitsCount() - o1.getUnitsCount());
        filesList = filesList.subList(0, Math.min(getMaxTopListSize(), filesList.size()));
        section(report).addFilesTable("Files With Most Units", "", null, filesList, isCacheSourceFiles(), false, false);
    }

    private int getMaxTopListSize() {
        return codeAnalysisResults.getCodeConfiguration().getAnalysis().getMaxTopListSize();
    }

    private void addFilesWithMostLongLines(RichTextReport report) {
        int threshold = 120;
        List<SourceFile> filesList = codeAnalysisResults.getFilesAnalysisResults().getAllFiles()
                .stream().filter(sourceFile -> sourceFile.getLongLinesCount(threshold) > 0).collect(Collectors.toList());
        int count = filesList.size();
        int sum = filesList.stream().map(s -> (int) s.getLongLinesCount(120)).collect(Collectors.summingInt(Integer::intValue));
        filesList.sort((o1, o2) -> (int) (o2.getLongLinesCount(threshold) - o1.getLongLinesCount(threshold)));
        filesList = filesList.subList(0, Math.min(getMaxTopListSize(), filesList.size()));
        String paragraph = "There " + (count == 1 ? "is only one file" : "are <b>" + count + "</b> files") + " with lines longer than 120 characters. In total, there " + (sum == 1 ? "is only one long line" : "are <b>" + sum + "</b> long lines") + ".";
        section(report).addFilesTable("Files With Long Lines", "", paragraph, filesList, isCacheSourceFiles(), false, true);
    }
}
