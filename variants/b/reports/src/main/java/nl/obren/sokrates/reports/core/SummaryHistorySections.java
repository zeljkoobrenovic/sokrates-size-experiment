/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.core;

import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.utils.ReportUtils;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.FilesHistoryAnalysisResults;
import nl.obren.sokrates.sourcecode.core.FoundTag;
import nl.obren.sokrates.sourcecode.core.TagRule;
import nl.obren.sokrates.sourcecode.metrics.MetricsWithGoal;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import nl.obren.sokrates.sourcecode.stats.SourceFileAgeDistribution;
import nl.obren.sokrates.sourcecode.stats.SourceFileChangeDistribution;
import nl.obren.sokrates.sourcecode.threshold.Thresholds;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.core.SummaryVisuals.getControlColor;
import static nl.obren.sokrates.reports.core.SummaryVisuals.getRiskProfileVisual;
import static nl.obren.sokrates.sourcecode.core.CodeConfigurationUtils.FILES_IN_MULTIPLE_CLASSIFICATIONS;
import static nl.obren.sokrates.sourcecode.core.CodeConfigurationUtils.UNCLASSIFIED_FILES;

class SummaryHistorySections {
    private SummaryUtils owner;

    SummaryHistorySections(SummaryUtils owner) {
        this.owner = owner;
    }

    private String reportRoot() {
        return owner.getReportRoot();
    }

    void summarizeFileChangeHistory(CodeAnalysisResults analysisResults, RichTextReport report) {
        String linkPrefix = "<a target='_blank' href='" + reportRoot() + "FileAge.html'  title='file change history details' style='vertical-align: top'>";
        report.startTableRow();
        report.addTableCell(linkPrefix + owner.getIconSvg("file_history") + "</a>", "border: none;  vertical-align: top");

        FilesHistoryAnalysisResults results = analysisResults.getFilesHistoryAnalysisResults();
        report.startTableCell("border: none; padding-top: 4px; vertical-align: top");
        report.addHtmlContent(linkPrefix);
        SourceFileAgeDistribution age = results.getOverallFileFirstModifiedDistribution();
        report.addContentInDiv(getRiskProfileVisual(age, Palette.getAgePalette()));
        SourceFileAgeDistribution changes = results.getOverallFileLastModifiedDistribution();
        report.addContentInDiv(getRiskProfileVisual(changes, Palette.getFreshnessPalette()));
        report.addHtmlContent("</a>");
        report.endTableCell();

        report.startTableCell("border: none; padding-top: 4px;");
        String ageSummary = FormattingUtils.formatPeriod(results.getAgeInDays()) + " old";
        report.addParagraph(ageSummary, "margin-bottom: 0");
        report.startUnorderedList("margin-top: 5px; font-size: 90%");
        Thresholds fileAgeThresholds = analysisResults.getCodeConfiguration().getAnalysis().getFileAgeThresholds();
        report.addListItem(FormattingUtils.getFormattedPercentage(age.getVeryHighRiskPercentage())
                + "% of code older than " + fileAgeThresholds.getVeryHigh() + " days");
        report.addListItem(FormattingUtils.getFormattedPercentage(changes.getVeryHighRiskPercentage())
                + "% of code not updated in the past "
                + fileAgeThresholds.getVeryHigh()
                + " days"
        );
        report.endUnorderedList();
        report.endTableCell();
        report.addTableCell(linkPrefix + owner.getDetailsIcon() + "</a>", "border: none;  vertical-align: top");

        report.endTableRow();
    }

    void summarizeFileUpdateFrequency(CodeAnalysisResults analysisResults, RichTextReport report) {
        String linkPrefix = "<a target='_blank' href='" + reportRoot() + "FileChangeFrequency.html'  title='file change frequency details' style='vertical-align: top'>";

        report.startTableRow();
        report.addTableCell(linkPrefix + owner.getIconSvg("change") + "</a>", "border: none;  vertical-align: top");

        FilesHistoryAnalysisResults results = analysisResults.getFilesHistoryAnalysisResults();
        report.startTableCell("border: none; padding-top: 4px; vertical-align: top");
        report.addHtmlContent(linkPrefix);
        SourceFileChangeDistribution fileChange = results.getOverallFileChangeDistribution();
        SourceFileChangeDistribution contributorsCount = results.getOverallContributorsCountDistribution();
        report.addContentInDiv(getRiskProfileVisual(fileChange, Palette.getHeatPalette()));
        report.addContentInDiv(getRiskProfileVisual(contributorsCount, Palette.getHeatPalette()));
        report.addHtmlContent("</a>");
        report.endTableCell();

        report.startTableCell("border: none; padding-top: 4px;");
        Thresholds thresholds = analysisResults.getCodeConfiguration().getAnalysis().getFileUpdateFrequencyThresholds();
        report.addParagraph(FormattingUtils.getFormattedPercentage(fileChange.getVeryHighRiskPercentage() + fileChange.getHighRiskPercentage())
                + "% of code updated more than " + thresholds.getHigh() + " times", "margin-bottom: 2px");
        report.addParagraph("Also see <a target='_blank' href='FileTemporalDependencies.html'>temporal dependencies</a> for files frequently changed in same commits.", "font-size: 80%; color: grey;");
        report.endTableCell();
        report.addTableCell(linkPrefix + owner.getDetailsIcon() + "</a>", "border: none;  vertical-align: top");

        report.endTableRow();
    }

    void summarizeGoals(CodeAnalysisResults analysisResults, RichTextReport report) {
        String linkPrefix = "<a target='_blank' href='" + reportRoot() + "Controls.html'  title='metrics &amp; goals details' style='vertical-align: top'>";

        report.startTableRow();
        report.addTableCell(linkPrefix + owner.getIconSvg("goal") + "</a>", "border: none");

        report.startTableCell("border: none");
        report.addHtmlContent(linkPrefix);
        analysisResults.getControlResults().getGoalsAnalysisResults().forEach(goalsAnalysisResults -> {
            goalsAnalysisResults.getControlStatuses().forEach(controlStatus -> {
                report.addHtmlContent(ReportUtils.getSvgCircle(getControlColor(controlStatus.getStatus())) + " ");
            });
        });
        report.addHtmlContent("</a>");
        report.endTableCell();

        report.startTableCell("border: none; vertical-align: top; padding-top: 11px;");
        report.addHtmlContent("Goals:");
        boolean first[] = {true};
        analysisResults.getControlResults().getGoalsAnalysisResults().forEach(goalsAnalysisResults -> {
            if (!first[0]) {
                report.addHtmlContent(", ");
            } else {
                first[0] = false;
            }
            MetricsWithGoal metricsWithGoal = goalsAnalysisResults.getMetricsWithGoal();
            report.addHtmlContent(metricsWithGoal.getGoal() + " (" + metricsWithGoal.getControls().size() + ")");
        });
        report.endTableCell();
        report.addTableCell(linkPrefix + owner.getDetailsIcon() + "</a>", "border: none");

        report.endTableRow();
    }

    void summarizeFeaturesOfInterest(CodeAnalysisResults analysisResults, RichTextReport report) {
        List<NumericMetric> fileCount = new ArrayList<>();
        analysisResults.getConcernsAnalysisResults().forEach(concernsGroupResults -> {
            concernsGroupResults.getFileCountPerConcern().stream()
                    .filter(c -> c.getValue().intValue() > 0)
                    .filter(c -> !c.getName().equalsIgnoreCase(UNCLASSIFIED_FILES))
                    .filter(c -> !c.getName().equalsIgnoreCase(FILES_IN_MULTIPLE_CLASSIFICATIONS))
                    .forEach(concern -> {
                        fileCount.add(concern);
                    });
        });

        if (fileCount.size() == 0) {
            return;
        }

        String linkPrefix = "<a target='_blank' href='" + reportRoot() + "FeaturesOfInterest.html'  title='metrics &amp; goals details' style='vertical-align: top'>";
        report.startTableRow();
        report.addTableCell("", "border: none; height: 8px");
        report.endTableRow();
        report.startTableRow();
        report.addTableCell(linkPrefix + owner.getIconSvg("cross_cutting_concerns") + "</a>", "border: none; border-top: 2px solid lightgrey; padding-top: 10px");

        report.startTableCellColSpan("border: none; border-top: 2px solid lightgrey; padding-top: 10px", 2);
        report.addContentInDiv("Features of interest:", "font-size: 80%");
        Collections.sort(fileCount, (a, b) -> b.getValue().intValue() - a.getValue().intValue());
        int limit = 10;
        fileCount.subList(0, fileCount.size() > limit ? limit : fileCount.size()).forEach(concern -> {
            addConcernDiv(report, concern);
        });
        if (fileCount.size() > limit) {
            report.startShowMoreBlockDisappear("", "<div style='vertical-align: middle; font-size: 80%; display: inline-block;'>show all...</div>");
            fileCount.subList(limit, fileCount.size()).forEach(concern -> {
                addConcernDiv(report, concern);
            });
            report.endShowMoreBlock();
        }
        report.endTableCell();
        report.addTableCell(linkPrefix + owner.getDetailsIcon() + "</a>", "border: none; border-top: 2px solid lightgrey; padding-top: 10px");

        report.endTableRow();
    }

    private void addConcernDiv(RichTextReport report, NumericMetric concern) {
        int value = concern.getValue().intValue();
        report.addContentInDiv("<b>" + concern.getName() + "</b> " +
                        "<br><span style='font-size: 85%; color: grey'>" + value + " " + (value == 1 ? "file" : "files") + "",
                "text-align: center; font-size: 80%; border: 1px solid grey; display: inline-block; border-radius: 4px; background-color: #f8f8f8; padding: 3px 9px 3px 9px; margin: 3px 2px 8px 2px");
    }


    void summarizeTags(CodeAnalysisResults analysisResults, RichTextReport report) {
        List<FoundTag> tags = analysisResults.getFoundTags();

        report.startTableRow();
        report.addTableCell(owner.getIconSvg("tags"), "border: none; padding-bottom: 16px; margin-top: -20px");

        report.startTableCellColSpan("border: none", 2);
        tags.forEach(foundTag -> {
            TagRule tagRule = foundTag.getTagRule();
            String tooltip = "added if at least one file matches:\n  - "
                    + tagRule.getPathPatterns().stream().collect(Collectors.joining("\n  - ")) + "\n\n\nmatches:\n  - "
                    + foundTag.getEvidence().replace("\n", "\n  - ");
            String color = StringUtils.isNotBlank(tagRule.getColor()) ? tagRule.getColor() : "white";
            report.addContentInDivWithTooltip(tagRule.getTag(), tooltip, "cursor: help; font-size: 90%; border: 1px lightgrey solid; padding: 4px 10px 5px 10px; display: inline-block; background-color: " + color + "; border-radius: 3px");
        });
        report.addLineBreak();
        report.addLineBreak();
        report.endTableCell();

        report.endTableRow();
    }
}
