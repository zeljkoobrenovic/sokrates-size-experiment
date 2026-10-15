/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.common.renderingutils.RichTextRenderingUtils;
import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.utils.FilesReportUtils;
import nl.obren.sokrates.reports.utils.PieChartUtils;
import nl.obren.sokrates.reports.utils.RiskDistributionStatsReportUtils;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.stats.RiskDistributionStats;
import nl.obren.sokrates.sourcecode.threshold.Thresholds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Renders the shared building blocks of the file-level "risk distribution" report pages
 * ({@link FileSizeReportGenerator}, {@link FileChurnReportGenerator}, {@link FileAgeReportGenerator}):
 * the five-bucket list (very high / high / medium / low / negligible) with its overall chart,
 * the "explore:" link rows, the per-extension and per-component charts, and the top-N file tables.
 * <p>
 * Each generator passes its own labels, palette and wording; this class decides how a section is laid out.
 */
public class FileRiskDistributionSection {
    public static final String SCROLLING_DIV_STYLE = "max-height: 600px; overflow-y: scroll; overflow-x: hidden;";
    public static final String COMPACT_DIV_STYLE = "max-height: 300px; overflow-x: none; overflow-y: auto";
    public static final String LINES_OF_CODE_SUFFIX = " lines of code)";
    public static final String EXPLORE_SEPARATOR = " | ";

    /**
     * The five buckets of a {@link RiskDistributionStats}, in the order they are listed in the reports.
     */
    public enum Bucket {
        VERY_HIGH, HIGH, MEDIUM, LOW, NEGLIGIBLE;

        public int count(RiskDistributionStats distribution) {
            switch (this) {
                case VERY_HIGH:
                    return distribution.getVeryHighRiskCount();
                case HIGH:
                    return distribution.getHighRiskCount();
                case MEDIUM:
                    return distribution.getMediumRiskCount();
                case LOW:
                    return distribution.getLowRiskCount();
                default:
                    return distribution.getNegligibleRiskCount();
            }
        }

        public int value(RiskDistributionStats distribution) {
            switch (this) {
                case VERY_HIGH:
                    return distribution.getVeryHighRiskValue();
                case HIGH:
                    return distribution.getHighRiskValue();
                case MEDIUM:
                    return distribution.getMediumRiskValue();
                case LOW:
                    return distribution.getLowRiskValue();
                default:
                    return distribution.getNegligibleRiskValue();
            }
        }

        public String label(Thresholds thresholds) {
            switch (this) {
                case VERY_HIGH:
                    return thresholds.getVeryHighRiskLabel();
                case HIGH:
                    return thresholds.getHighRiskLabel();
                case MEDIUM:
                    return thresholds.getMediumRiskLabel();
                case LOW:
                    return thresholds.getLowRiskLabel();
                default:
                    return thresholds.getNegligibleRiskLabel();
            }
        }
    }

    /**
     * The wording of one bucket line, given the bucket and its file count.
     */
    @FunctionalInterface
    public interface BucketText {
        String text(Bucket bucket, int count);
    }

    private final RichTextReport report;
    private final List<String> labels;
    private final Palette palette;

    public FileRiskDistributionSection(RichTextReport report, List<String> labels, Palette palette) {
        this.report = report;
        this.labels = labels;
        this.palette = palette;
    }

    public static String link(String href, String text) {
        return "<a target='_blank' href='" + href + "'>" + text + "</a>";
    }

    public static <T> List<T> matchingDecomposition(List<T> distributions, Function<T, String> nameOf, String name) {
        return distributions.stream().filter(d -> nameOf.apply(d).equalsIgnoreCase(name)).collect(Collectors.toList());
    }

    /**
     * Adds the total line, the five bucket lines ("count" + description + " (" + "value" + " lines of code)")
     * and the overall distribution chart.
     */
    public void addBucketList(String totalLine, RiskDistributionStats distribution, BucketText description) {
        addBucketList(totalLine, distribution, description, (bucket, count) -> LINES_OF_CODE_SUFFIX);
    }

    public void addBucketList(String totalLine, RiskDistributionStats distribution, BucketText description, BucketText valueSuffix) {
        report.startUnorderedList();
        report.addListItem(totalLine);
        report.startUnorderedList();
        for (Bucket bucket : Bucket.values()) {
            int count = bucket.count(distribution);
            report.addListItem(RichTextRenderingUtils.renderNumberStrong(count)
                    + description.text(bucket, count)
                    + " (" + RichTextRenderingUtils.renderNumberStrong(bucket.value(distribution))
                    + valueSuffix.text(bucket, count));
        }
        report.endUnorderedList();
        report.endUnorderedList();
        report.addHtmlContent(PieChartUtils.getRiskDistributionChart(distribution, labels, palette));
    }

    /**
     * Adds "explore: " followed by the given links, separated by " | ".
     */
    public void addExploreLinks(String... links) {
        List<String> fragments = new ArrayList<>();
        for (int i = 0; i < links.length; i++) {
            if (i > 0) {
                fragments.add(EXPLORE_SEPARATOR);
            }
            fragments.add(links[i]);
        }
        addExploreFragments(fragments.toArray(new String[0]));
    }

    /**
     * Adds "explore: " followed by the given HTML fragments as they are (each fragment is rendered on its own line,
     * so callers that must keep a link and its separator together pass them as one fragment).
     */
    public void addExploreFragments(String... fragments) {
        report.addHtmlContent("explore: ");
        for (String fragment : fragments) {
            report.addHtmlContent(fragment);
        }
    }

    public void addChartPerKeySection(String title, String subtitle, List<RiskDistributionStats> distributions) {
        report.startSection(title, subtitle);
        report.addHtmlContent(getChartPerKey(distributions));
        report.endSection();
    }

    public void addChartPerKeySubSection(String title, String subtitle, List<RiskDistributionStats> distributions) {
        report.startSubSection(title, subtitle);
        report.addHtmlContent(getChartPerKey(distributions));
        report.endSection();
    }

    /**
     * Adds a sub-section with the distribution per component of one logical decomposition, in a div with the given style.
     */
    public void addChartPerComponent(String title, String subtitle, List<RiskDistributionStats> distributions, String divStyle) {
        report.startSubSection(title, subtitle);
        report.startDiv(divStyle);
        report.addHtmlContent(getChartPerKey(distributions));
        report.endDiv();
        report.endSection();
    }

    private String getChartPerKey(List<RiskDistributionStats> distributions) {
        return RiskDistributionStatsReportUtils.getRiskDistributionPerKeySvgBarChart(distributions, labels, palette);
    }

    /**
     * Adds a "title (Top N)" section with a table of files, optionally preceded by a paragraph.
     */
    public void addFilesTable(String title, String subtitle, String paragraph, List<SourceFile> files,
                              boolean linkToFiles, boolean showAge, boolean showLineLength) {
        startFilesTable(title, subtitle, paragraph, files);
        report.addHtmlContent(FilesReportUtils.getFilesTable(files, linkToFiles, showAge, showLineLength));
        report.endSection();
    }

    public void addFilesTable(String title, String subtitle, String paragraph, List<SourceFile> files,
                              boolean linkToFiles, boolean showAge, boolean showLineLength, int maxHeight) {
        startFilesTable(title, subtitle, paragraph, files);
        report.addHtmlContent(FilesReportUtils.getFilesTable(files, linkToFiles, showAge, showLineLength, maxHeight));
        report.endSection();
    }

    private void startFilesTable(String title, String subtitle, String paragraph, List<SourceFile> files) {
        report.startSection(title + " (Top " + files.size() + ")", subtitle);
        if (paragraph != null) {
            report.addParagraph(paragraph);
        }
    }
}
