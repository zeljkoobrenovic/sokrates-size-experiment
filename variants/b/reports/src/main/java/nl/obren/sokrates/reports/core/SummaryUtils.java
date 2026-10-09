/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.core;

import nl.obren.sokrates.common.renderingutils.RichTextRenderingUtils;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.utils.DataImageUtils;
import nl.obren.sokrates.reports.utils.HtmlTemplateUtils;
import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.FilesAnalysisResults;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.metrics.DuplicationMetric;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import nl.obren.sokrates.sourcecode.stats.RiskDistributionStats;
import nl.obren.sokrates.sourcecode.stats.SourceFileSizeDistribution;
import nl.obren.sokrates.sourcecode.threshold.Thresholds;

import java.util.*;

import static nl.obren.sokrates.reports.core.SummaryVisuals.*;

public class SummaryUtils {
    private String reportRoot = "";
    private SummaryHistorySections historySections = new SummaryHistorySections(this);

    public static String getIconSvg(String icon, int w, int h) {
        String svg = HtmlTemplateUtils.getResource("/icons/" + icon + ".svg");
        svg = svg.replaceAll("height='.*?'", "height='" + w + "px'");
        svg = svg.replaceAll("width='.*?'", "width='" + h + "px'");
        return svg;
    }

    public String getReportRoot() {
        return reportRoot;
    }

    public void setReportRoot(String reportRoot) {
        this.reportRoot = reportRoot;
    }

    public void summarize(CodeAnalysisResults analysisResults, RichTextReport report) {
        CodeConfiguration config = analysisResults.getCodeConfiguration();

        boolean mainExists = analysisResults.getMainAspectAnalysisResults().getFilesCount() > 0;
        boolean showDuplication = mainExists && !analysisResults.skipDuplicationAnalysis();
        boolean showCommitReports = mainExists && analysisResults.getFilesHistoryAnalysisResults().getHistory(Integer.MAX_VALUE).size() > 0 && analysisResults.getContributorsAnalysisResults().getCommitsCount() > 0;
        boolean showControls = mainExists && config.getGoalsAndControls().size() > 0;
        boolean showUnits = mainExists && analysisResults.getUnitsAnalysisResults().getTotalNumberOfUnits() > 0;

        report.startDiv("width: 100%; overflow-x: auto; margin-top: -10px");
        report.startTable("border: none; min-width: 800px; width: 100%");
        summarizeTags(analysisResults, report);
        report.startTableRow();
        report.addMultiColumnTableCell("Standard analyses:", 2, "border: none; padding-top: 0px");
        report.endTableRow();
        summarizeMainVolume(analysisResults, report);
        if (mainExists) {
            if (showDuplication) {
                summarizeDuplication(analysisResults, report);
            }
            summarizeFileSize(report, analysisResults);
            summarizeComponents(analysisResults, report, false);
        }
        if (showCommitReports) {
            summarizeFileChangeHistory(analysisResults, report);
            summarizeFileUpdateFrequency(analysisResults, report);
        }
        if (showControls) {
            summarizeGoals(analysisResults, report);
        }
        if (mainExists) {
            if (showUnits) {
                report.startTableRow();
                report.addMultiColumnTableCell("Experimental analyses (less reliable heuristic analyses):", 3, "border: none; padding-top: 12px");
                report.endTableRow();
                summarizeUnitSize(analysisResults, report);
                summarizeUnitComplexity(analysisResults, report);
                summarizeComponents(analysisResults, report, true);
            }
        }
        summarizeFeaturesOfInterest(analysisResults, report);
        addSummaryFindings(analysisResults, report);
        report.endTable();
        report.endDiv();
    }

    private void summarizeFileSize(RichTextReport report, CodeAnalysisResults analysisResults) {
        FilesAnalysisResults filesAnalysisResults = analysisResults.getFilesAnalysisResults();
        if (filesAnalysisResults != null) {
            SourceFileSizeDistribution distribution = filesAnalysisResults.getOverallFileSizeDistribution();
            if (distribution != null) {
                int mainLOC = analysisResults.getMainAspectAnalysisResults().getLinesOfCode();
                int veryLongFilesLOC = distribution.getVeryHighRiskValue();
                int shortFilesLOC = distribution.getLowRiskValue() + distribution.getNegligibleRiskValue();

                String linkPrefix = "<a target='_blank' href='" + reportRoot + "FileSize.html'  title='file size details' style='vertical-align: top'>";
                report.startTableRow();
                report.addTableCell(linkPrefix + getIconSvg("file_size") + "</a>", "border: none");
                report.addTableCell(linkPrefix + getRiskProfileVisual(distribution) + "</a>", "border: none");
                Thresholds fileSizeThresholds = analysisResults.getCodeConfiguration().getAnalysis().getFileSizeThresholds();
                report.addTableCell("File Size: <b>"
                        + FormattingUtils.getFormattedPercentage(RichTextRenderingUtils.getPercentage(mainLOC, veryLongFilesLOC))
                        + "%</b> long (>" + fileSizeThresholds.getVeryHigh() + " LOC), <b>"
                        + FormattingUtils.getFormattedPercentage(RichTextRenderingUtils.getPercentage(mainLOC, shortFilesLOC))
                        + "%</b> short (<= " + fileSizeThresholds.getMedium() + " LOC)", "border: none; vertical-align: top; padding-top: 11px;");
                report.addTableCell(linkPrefix + getDetailsIcon() + "</a>", "border: none");
                report.endTableRow();
            }
        }
    }

    String getIconSvg(String icon) {
        return getIconSvg(icon, 40, 40);
    }

    public void summarizeListOfLocAspects(StringBuilder summary, int totalLoc, List<NumericMetric> linesOfCodePerAspect) {
        if (linesOfCodePerAspect.size() > 0) {
            summary.append("<span style='color: grey'> = ");
        }
        final boolean[] first = {true};
        linesOfCodePerAspect.forEach(ext -> {
            if (!first[0]) {
                summary.append(" + ");
            } else {
                first[0] = false;
            }
            String language = ext.getName().toUpperCase().replace("*.", "");
            int loc = ext.getValue().intValue();
            double percentage = totalLoc > 0 ? 100.0 * loc / totalLoc : 0;
            String formattedPercentage = FormattingUtils.getFormattedPercentage(percentage) + "%";

            summary.append("<b>" + language + "</b> <span style='color:lightgrey'>(" + formattedPercentage + ")</span>");
        });
        if (linesOfCodePerAspect.size() > 0) {
            summary.append("</span>");
        }
    }

    public void summarizeAndCompare(CodeAnalysisResults analysisResults, CodeAnalysisResults refData, RichTextReport report) {
        compareMainCodeAndDuplication(analysisResults, refData, report);
        compareFileSizeAndUnits(analysisResults, refData, report);
        compareComponentsAndGoals(analysisResults, refData, report);
    }

    private void compareMainCodeAndDuplication(CodeAnalysisResults analysisResults, CodeAnalysisResults refData, RichTextReport report) {
        StringBuilder summary = new StringBuilder("");
        summarizeMainCode(analysisResults, summary, null);

        summary.append(addDiffDiv(analysisResults.getMainAspectAnalysisResults().getLinesOfCode(),
                refData.getMainAspectAnalysisResults().getLinesOfCode()));
        summary.append("<div style='margin-top: 24px;font-size:80%;margin-bottom:46px;opacity: 0.5;'>");
        summarizeMainCode(refData, summary, null);
        summary.append("</div>");

        report.addParagraph(summary.toString());

        report.addHorizontalLine();
        report.addLineBreak();

        report.startDiv("color:black");
        summarizeDuplication(analysisResults, report);
        report.endDiv();

        report.addParagraph(addDiffDiv(analysisResults.getDuplicationAnalysisResults().getOverallDuplication().getDuplicationPercentage().doubleValue(),
                refData.getDuplicationAnalysisResults().getOverallDuplication().getDuplicationPercentage().doubleValue()));
        report.startDiv("margin-top: 24px;font-size:80%;margin-bottom:46px;opacity: 0.5;");
        summarizeDuplication(refData, report);
        report.endDiv();
        report.addHorizontalLine();
        report.addLineBreak();
    }

    private void compareFileSizeAndUnits(CodeAnalysisResults analysisResults, CodeAnalysisResults refData, RichTextReport report) {
        report.startDiv("color:black");
        summarizeFileSize(report, analysisResults);
        report.endDiv();

        report.startDiv("margin-top: 24px;font-size:80%;margin-bottom:46px;opacity: 0.5;;");
        summarizeFileSize(report, refData);
        report.endDiv();
        report.addHorizontalLine();
        report.addLineBreak();

        report.startDiv("color:black");
        summarizeUnitSize(analysisResults, report);
        report.endDiv();

        report.startDiv("margin-top: 24px;font-size:80%;margin-bottom:46px;opacity: 0.5;");
        summarizeUnitSize(refData, report);
        report.endDiv();
        report.addHorizontalLine();
        report.addLineBreak();

        report.startDiv("color:black");
        summarizeUnitComplexity(analysisResults, report);
        report.endDiv();

        report.startDiv("margin-top: 24px;font-size:80%;margin-bottom:46px;opacity: 0.5;");
        summarizeUnitComplexity(refData, report);
        report.endDiv();
        report.addHorizontalLine();
        report.addLineBreak();
    }

    private void compareComponentsAndGoals(CodeAnalysisResults analysisResults, CodeAnalysisResults refData, RichTextReport report) {
        // components
        report.startDiv("color:black");
        summarizeComponents(analysisResults, report, false);
        report.endDiv();

        report.startDiv("margin-top: 24px;font-size:80%;margin-bottom:46px;opacity: 0.5;");
        summarizeComponents(refData, report, false);
        report.endDiv();
        report.addHorizontalLine();
        report.addLineBreak();

        // goals
        report.startDiv("color: black");
        summarizeGoals(analysisResults, report);
        report.endDiv();

        report.startDiv("margin-top: 24px;font-size:80%;margin-bottom:46px;opacity: 0.5;");
        summarizeGoals(refData, report);
        report.endDiv();
        report.addHorizontalLine();
        report.addLineBreak();
    }

    private void summarizeMainVolume(CodeAnalysisResults analysisResults, RichTextReport report) {
        String linkPrefix = "<a target='_blank' href='" + reportRoot + "SourceCodeOverview.html'  title='volume details' style='vertical-align: top'>";
        StringBuilder summary = new StringBuilder("");
        summarizeMainCode(analysisResults, summary, linkPrefix);
        report.addHtmlContent(summary.toString());
        report.addTableCell(linkPrefix + getDetailsIcon() + "</a>", "border: none");
        report.endTableRow();
    }

    String getDetailsIcon() {
        return getIconSvg("details", 22, 22);
    }

    private void summarizeMainCode(CodeAnalysisResults analysisResults, StringBuilder summary, String linkPrefix) {
        if (linkPrefix != null) {
            summary.append("<td style='border: none'>" + linkPrefix + getIconSvg("codebase") + "</a></td>");
            summary.append("<td style='border: none; width: 300px; max-width: 300px'>" + linkPrefix);
        } else {
            summary.append("<td style='border: none'>" + getIconSvg("codebase") + "</td>");
            summary.append("<td style='border: none; width: 300px; max-width: 300px'>" + linkPrefix);
        }
        int totalLoc = analysisResults.getMainAspectAnalysisResults().getLinesOfCode();
        List<NumericMetric> linesOfCodePerExtension = analysisResults.getMainAspectAnalysisResults().getLinesOfCodePerExtension();
        summary.append("<div>" + getVolumeVisual(linesOfCodePerExtension, totalLoc, totalLoc, "") + "</div>");
        if (linkPrefix != null) {
            summary.append("</a>");
        }
        summary.append("</td>");
        summary.append("<td style='max-width: 800px; overflow: hidden; border: none; vertical-align: top; padding-top: 11px;'>");
        summary.append("<a target='_blank' href='visuals/zoomable_circles_main.html'>Main Code</a>");
        summary.append(": " + RichTextRenderingUtils.renderNumberStrong(totalLoc) + " LOC (" + analysisResults.getMainAspectAnalysisResults().getFilesCount() + " files)");
        summarizeListOfLocAspects(summary, totalLoc, linesOfCodePerExtension);
        summary.append("<div style='font-size: 80%; color: grey; margin-top: 5px;'>");
        summary.append("Secondary code: ");
        addSecondaryCodeInfo(summary, analysisResults.getTestAspectAnalysisResults(), "Test", "test");
        addSecondaryCodeInfo(summary, analysisResults.getGeneratedAspectAnalysisResults(), "Generated", "generated");
        addSecondaryCodeInfo(summary, analysisResults.getBuildAndDeployAspectAnalysisResults(), "Build & Deploy", "build");
        addSecondaryCodeInfo(summary, analysisResults.getOtherAspectAnalysisResults(), "Other", "other");
        summary.append("<div>");
        summary.append("</td>");
    }

    private void addSecondaryCodeInfo(StringBuilder summary, AspectAnalysisResults aspectAnalysisResults, String label, String fileSuffix) {
        int loc = aspectAnalysisResults.getLinesOfCode();
        if (loc > 0) {
            summary.append("<a target='_blank' href='visuals/zoomable_circles_" + fileSuffix + ".html'>" + label + "</a>");
        } else {
            summary.append(label);
        }
        summary.append(": " + RichTextRenderingUtils.renderNumberStrong(loc) + " LOC (" + aspectAnalysisResults.getFilesCount() + "); ");
    }

    private void addIconsMainCode(CodeAnalysisResults analysisResults, StringBuilder summary) {
        List<NumericMetric> extensions = analysisResults.getMainAspectAnalysisResults().getLinesOfCodePerExtension();
        summary.append("<div>");
        Set<String> alreadyAddedImage = new HashSet<>();
        extensions.forEach(ext -> {
            String lang = ext.getName().toUpperCase().replace("*.", "").trim();
            String image = DataImageUtils.getLangDataImage(lang);
            if (image == null || !alreadyAddedImage.contains(image)) {
                int loc = ext.getValue().intValue();
                int fontSize = loc >= 1000 ? 18 : 13;
                summary.append("<div style='width: 42px; text-align: center; display: inline-block; border-radius: 5px; background-color: #f0f0f0; padding: 8px; margin-right: 4px;'>"
                        + DataImageUtils.getLangDataImageDiv30(lang)
                        + "<div style='margin-top: 3px; font-size: " + fontSize + "px'>" + FormattingUtils.getSmallTextForNumber(loc) + "</div>"
                        + "<div style='font-size: 10px; white-space: no-wrap; overflow: hidden; color: grey;'>" + lang.toLowerCase() + "</div>"
                        + "</div>");
                if (image != null) {
                    alreadyAddedImage.add(image);
                }
            }
        });
        summary.append("</div>");
    }

    private void addSummaryFindings(CodeAnalysisResults analysisResults, RichTextReport report) {
        List<String> summaryFindings = analysisResults.getCodeConfiguration().getSummary();
        if (summaryFindings != null && summaryFindings.size() > 0) {
            report.addParagraph("Other findings:");
            report.startUnorderedList();
            summaryFindings.forEach(summaryFinding -> {
                report.addListItem(summaryFinding);
            });
            report.endUnorderedList();
        }
    }

    private void summarizeComponents(CodeAnalysisResults analysisResults, RichTextReport report, boolean withStaticDependencies) {
        report.startTableRow();
        String linkPrefix;
        if (withStaticDependencies) {
            linkPrefix = "<a target='_blank' href='" + reportRoot + "ComponentsAndDependencies.html'  title='compoennt dependencies'>";
        } else {
            linkPrefix = "<a target='_blank' href='" + reportRoot + "Components.html'  title='logical decomposition details'>";
        }
        report.addTableCell(
                linkPrefix + getIconSvg(withStaticDependencies ? "dependencies" : "code_organization") + "</a>",
                "border: none");

        report.startTableCell("border: none");
        report.addHtmlContent(linkPrefix);
        analysisResults.getLogicalDecompositionsAnalysisResults().forEach(decomposition -> {
            int mainLoc = analysisResults.getMainAspectAnalysisResults().getLinesOfCode();
            int totalLoc[] = {0};
            List<NumericMetric> linesOfCodePerComponent = decomposition.getLinesOfCodePerComponent();
            linesOfCodePerComponent.forEach(c -> totalLoc[0] += c.getValue().intValue());

            report.addContentInDiv(getVolumeVisual(linesOfCodePerComponent, totalLoc[0], mainLoc, ""));
        });
        report.addHtmlContent("</a>");
        report.endTableCell();

        report.startTableCell("border: none; vertical-align: top; padding-top: 11px;");
        if (withStaticDependencies) {
            report.addHtmlContent("Static Component Dependencies:");
        } else {
            report.addHtmlContent("Logical Component Decomposition:");
        }
        boolean first[] = {true};
        int index[] = {1};
        analysisResults.getLogicalDecompositionsAnalysisResults().forEach(decomposition -> {
            if (!first[0]) {
                report.addHtmlContent(", ");
            } else {
                first[0] = false;
            }
            int componentsCount = decomposition.getComponents().size();
            report.addHtmlContent(decomposition.getKey() +
                    " (<a target='_blank' href='visuals/bubble_chart_components_" + index[0] + ".html'>" +
                    componentsCount + " " + (componentsCount == 1 ? "component" : "components") + "</a>)");
            index[0] += 1;
        });
        report.endTableCell();
        report.addTableCell(linkPrefix + getDetailsIcon() + "</a>",
                "border: none;");

        report.endTableRow();
    }

    private void summarizeFileChangeHistory(CodeAnalysisResults analysisResults, RichTextReport report) {
        historySections.summarizeFileChangeHistory(analysisResults, report);
    }

    private void summarizeFileUpdateFrequency(CodeAnalysisResults analysisResults, RichTextReport report) {
        historySections.summarizeFileUpdateFrequency(analysisResults, report);
    }

    private void summarizeGoals(CodeAnalysisResults analysisResults, RichTextReport report) {
        historySections.summarizeGoals(analysisResults, report);
    }

    private void summarizeFeaturesOfInterest(CodeAnalysisResults analysisResults, RichTextReport report) {
        historySections.summarizeFeaturesOfInterest(analysisResults, report);
    }

    private void summarizeTags(CodeAnalysisResults analysisResults, RichTextReport report) {
        historySections.summarizeTags(analysisResults, report);
    }

    private void summarizeUnitComplexity(CodeAnalysisResults analysisResults, RichTextReport report) {
        int linesOfCodeInUnits = analysisResults.getUnitsAnalysisResults().getLinesOfCodeInUnits();
        RiskDistributionStats distribution = analysisResults.getUnitsAnalysisResults().getConditionalComplexityRiskDistribution();
        int veryComplexUnitsLOC = distribution.getHighRiskValue() + distribution.getVeryHighRiskValue();
        int lowComplexUnitsLOC = distribution.getNegligibleRiskValue();

        Thresholds thresholds = analysisResults.getCodeConfiguration().getAnalysis().getConditionalComplexityThresholds();

        String linkPrefix = "<a target='_blank' href='" + reportRoot + "ConditionalComplexity.html'  title='conditional complexity details' style='vertical-align: top'>";
        report.startTableRow();
        report.addTableCell(linkPrefix + getIconSvg("conditional") + "</a>", "border: none");
        report.addTableCell(linkPrefix + getRiskProfileVisual(distribution) + "</a>", "border: none");
        report.addTableCell("Conditional Complexity: <b>"
                + FormattingUtils.getFormattedPercentage(RichTextRenderingUtils.getPercentage(linesOfCodeInUnits, veryComplexUnitsLOC))
                + "%</b> complex (McCabe index > " + thresholds.getVeryHigh() + "), <b>"
                + FormattingUtils.getFormattedPercentage(RichTextRenderingUtils.getPercentage(linesOfCodeInUnits, lowComplexUnitsLOC))
                + "%</b> simple (McCabe index <= " + thresholds.getLow() + ")", "border: none; vertical-align: top; padding-top: 11px;");

        report.addTableCell(linkPrefix + getDetailsIcon() + "</a>", "border: none");
        report.endTableRow();
    }

    private void summarizeUnitSize(CodeAnalysisResults analysisResults, RichTextReport report) {
        int linesOfCodeInUnits = analysisResults.getUnitsAnalysisResults().getLinesOfCodeInUnits();
        RiskDistributionStats distribution = analysisResults.getUnitsAnalysisResults().getUnitSizeRiskDistribution();
        int veryLongUnitsLOC = distribution.getVeryHighRiskValue();
        int lowUnitsLOC = distribution.getLowRiskValue() + distribution.getNegligibleRiskValue();
        Thresholds thresholds = analysisResults.getCodeConfiguration().getAnalysis().getUnitSizeThresholds();

        String linkPrefix = "<a target='_blank' href='" + reportRoot + "UnitSize.html'  title='unit size details' style='vertical-align: top'>";
        report.startTableRow();
        report.addTableCell(linkPrefix + getIconSvg("unit_size") + "</a>", "border: none");
        report.addTableCell(linkPrefix + getRiskProfileVisual(distribution) + "</a>", "border: none");
        report.addTableCell("Unit Size: <b>"
                + FormattingUtils.getFormattedPercentage(RichTextRenderingUtils.getPercentage(linesOfCodeInUnits, veryLongUnitsLOC))
                + "%</b> long (>" + thresholds.getVeryHigh() + " LOC), <b>"
                + FormattingUtils.getFormattedPercentage(RichTextRenderingUtils.getPercentage(linesOfCodeInUnits, lowUnitsLOC))
                + "%</b> short (<= " + thresholds.getLow() + " LOC)", "border: none; vertical-align: top; padding-top: 11px;");
        report.addTableCell(linkPrefix + getDetailsIcon() + "</a>", "border: none");
        report.endTableRow();
    }

    private void summarizeDuplication(CodeAnalysisResults analysisResults, RichTextReport report) {
        DuplicationMetric overallDuplication = analysisResults.getDuplicationAnalysisResults().getOverallDuplication();
        Number duplicationPercentage = overallDuplication.getDuplicationPercentage();
        double duplication = duplicationPercentage.doubleValue();
        if (!analysisResults.skipDuplicationAnalysis()) {
            String linkPrefix = "<a target='_blank' href='" + reportRoot + "Duplication.html'  title='duplication details' style='vertical-align: top'>";
            report.startTableRow();
            report.addTableCell(linkPrefix + getIconSvg("duplication") + "</a>", "border: none");
            report.addTableCell(linkPrefix + getDuplicationVisual(duplicationPercentage) + "</a>", "border: none");
            report.addTableCell("Duplication: <b>" + FormattingUtils.getFormattedPercentage(duplication) + "%</b>", "margin-bottom: 0;  border: none; vertical-align: top; padding-top: 11px;");
            report.addTableCell(linkPrefix + getDetailsIcon() + "</a>", "border: none");
            report.endTableRow();
        }
    }

}
