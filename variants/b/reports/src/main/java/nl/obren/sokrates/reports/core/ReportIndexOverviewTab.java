/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.core;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.generators.statichtml.ContributorsReportUtils;
import nl.obren.sokrates.reports.utils.DataImageUtils;
import nl.obren.sokrates.sourcecode.Link;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.ContributionTimeSlot;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import nl.obren.sokrates.sourcecode.stats.SourceFileAgeDistribution;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.obren.sokrates.reports.core.ReportFileExporter.getIconSvg;
import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.*;

class ReportIndexOverviewTab {
    static void addOverviewTab(RichTextReport indexReport, CodeAnalysisResults analysisResults) {
        indexReport.startTabContentSection("overview", true);

        indexReport.startDiv("white-space: nowrap; overflow: hidden");

        addSizeBlocks(indexReport, analysisResults);
        ContributorsAnalysisResults contributorsAnalysisResults = analysisResults.getContributorsAnalysisResults();
        if (contributorsAnalysisResults.getCommitsCount() > 0) {
            addAgeBlocks(indexReport, analysisResults);
        }
        indexReport.endDiv();
        StringBuilder icons = new StringBuilder("");
        addIconsMainCode(analysisResults, icons);
        indexReport.startDiv("margin-left: 0px; margin-top: -65px; padding-top: 32px; margin-bottom: 0px; padding-left: 0px; padding-bottom: 10px");
        indexReport.startTable("margin-bottom: -20px");
        indexReport.startTableRow();
        indexReport.addTableCell(icons.toString(), "border: none;");
        indexReport.endTableRow();
        indexReport.endTable();
        indexReport.startDiv("");

        if (contributorsAnalysisResults.getCommitsCount() > 0) {
            addSummaryActivityTable(contributorsAnalysisResults, indexReport);
        }

        indexReport.endDiv();
        indexReport.endDiv();

        indexReport.addHtmlContent("<iframe src='Structure.html' style='border: none; width: 1000px; height: 1090px; overflow: hidden'></iframe>");
        indexReport.endTabContentSection();
    }

    private static void addSizeBlocks(RichTextReport indexReport, CodeAnalysisResults analysisResults) {
        int linesOfCodeMain = analysisResults.getMainAspectAnalysisResults().getLinesOfCode();
        int mainLoc = linesOfCodeMain;
        int mainFilesCount = analysisResults.getMainAspectAnalysisResults().getFilesCount();
        int testLoc = analysisResults.getTestAspectAnalysisResults().getLinesOfCode();
        int secondaryLoc = analysisResults.getBuildAndDeployAspectAnalysisResults().getLinesOfCode()
                + analysisResults.getGeneratedAspectAnalysisResults().getLinesOfCode()
                + analysisResults.getOtherAspectAnalysisResults().getLinesOfCode();
        int testFilesCount = analysisResults.getTestAspectAnalysisResults().getFilesCount();
        int secondaryFilesCount = analysisResults.getBuildAndDeployAspectAnalysisResults().getFilesCount()
                + analysisResults.getGeneratedAspectAnalysisResults().getFilesCount()
                + analysisResults.getOtherAspectAnalysisResults().getFilesCount();

        addInfoBlockWithColor(indexReport, FormattingUtils.getSmallTextForNumberMinK(mainLoc), "lines of main code", FormattingUtils.getSmallTextForNumber(mainFilesCount) + " files", MAIN_LOC_COLOR, "main lines of code", "main", "SourceCodeOverview.html");
        addInfoBlockWithColor(indexReport, FormattingUtils.getSmallTextForNumberMinK(testLoc), "lines of test code", FormattingUtils.getSmallTextForNumber(testFilesCount) + " files", TEST_LOC_COLOR, "test code in scope", "test", "SourceCodeOverview.html");
        addInfoBlockWithColor(indexReport, FormattingUtils.getSmallTextForNumberMinK(secondaryLoc), "lines of other code", FormattingUtils.getSmallTextForNumber(secondaryFilesCount) + " files", TEST_LOC_COLOR, "build & deployment, generated, all other code in scope", "build", "SourceCodeOverview.html");
    }

    private static void addAgeBlocks(RichTextReport indexReport, CodeAnalysisResults analysisResults) {
        int mainLoc = analysisResults.getMainAspectAnalysisResults().getLinesOfCode();
        SourceFileAgeDistribution lastModified = analysisResults.getFilesHistoryAnalysisResults().getOverallFileLastModifiedDistribution();
        SourceFileAgeDistribution firstChange = analysisResults.getFilesHistoryAnalysisResults().getOverallFileFirstModifiedDistribution();
        int notChanged = lastModified.getVeryHighRiskValue();
        double notChangedPerc = lastModified.getVeryHighRiskPercentage();
        int old = firstChange.getVeryHighRiskValue();
        double oldPerc = firstChange.getVeryHighRiskPercentage();
        int ageInDays = analysisResults.getFilesHistoryAnalysisResults().getAgeInDays();
        String age = ageInDays < 365 ? "<1y" : (int) Math.round(ageInDays / 365.0) + "y";
        addInfoBlockWithColor(indexReport, age, "age", FormattingUtils.formatCount(ageInDays) + " days", MAIN_LOC_FRESH_COLOR, "", "file_history", "FileAge.html");
        addInfoBlockWithColor(indexReport, FormattingUtils.getFormattedPercentage(100 - notChangedPerc) + "%", "main code touched", "1 year (" + FormattingUtils.getSmallTextForNumber(mainLoc - notChanged) + " LOC)", MAIN_LOC_FRESH_COLOR, "", "touch", "FileAge.html");
        addInfoBlockWithColor(indexReport, FormattingUtils.getFormattedPercentage(100 - oldPerc) + "%", "new main code", "1 year (" + FormattingUtils.getSmallTextForNumber(mainLoc - old) + " LOC)", MAIN_LOC_FRESH_COLOR, "", "new", "FileAge.html");
    }

    private static void addSummaryActivityTable(ContributorsAnalysisResults contributorsAnalysisResults, RichTextReport indexReport) {
        List<ContributionTimeSlot> contributorsPerYear = contributorsAnalysisResults.getContributorsPerYear();
        Map<String, ContributionTimeSlot> map = new HashMap<>();
        contributorsPerYear.forEach(c -> map.put(c.getTimeSlot(), c));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        String year = currentYear + "";

        while (!map.containsKey(year)) {
            contributorsPerYear.add(new ContributionTimeSlot(year));
            currentYear -= 1;
            year = currentYear + "";
        }

        long contributorsCount = contributorsAnalysisResults.getContributors().stream().filter(c -> !c.isBot() && c.isActive(Contributor.RECENTLY_ACTIVITY_THRESHOLD_DAYS)).count();
        int commitsCount30Days = contributorsAnalysisResults.getCommitsCount30Days();
        indexReport.startTable("margin-bottom: -20px; border-top: 1px dashed grey; border-bottom: 1px dashed grey; padding-top: 10px; margin-top: 10px; margin-bottom: 10px;");
        indexReport.startTableRow();

        indexReport.startTableCell("border: none; vertical-align: top;");

        indexReport.startDiv("margin-top: 8px; width: 80px; height: 81px; background-color: white; border-radius: 5px; vertical-align: middle; text-align: center");
        indexReport.startNewTabLink("Commits.html", commitsCount30Days == 0 ? "opacity: 0.4" : "");
        indexReport.addContentInDiv(FormattingUtils.getSmallTextForNumber(commitsCount30Days),
                "padding-top: 12px; font-size: 36px;");
        indexReport.addContentInDiv((commitsCount30Days == 1 ? "commit" : "commits") + "<br>(30 days)", "color: black; font-size: 80%");
        indexReport.endNewTabLink();
        indexReport.endDiv();

        indexReport.startDiv("margin-top: 32px; width: 80px; height: 81px; background-color: white; border-radius: 5px; vertical-align: middle; text-align: center");
        indexReport.startNewTabLink("Contributors.html", contributorsCount == 0 ? "opacity: 0.4" : "");
        indexReport.addContentInDiv(FormattingUtils.getSmallTextForNumber((int) contributorsCount),
                "padding-top: 12px; font-size: 36px;");
        indexReport.addContentInDiv((contributorsCount == 1 ? "contributor" : "contributors") + "<br>(30 days)", "color: black; font-size: 80%");
        indexReport.endNewTabLink();
        indexReport.endDiv();
        indexReport.endTableCell();
        indexReport.startTableCell("border: none");
        ContributorsReportUtils.addContributorsPerTimeSlot(indexReport, contributorsPerYear, 20, true, true, 8, contributorsCount == 0);
        indexReport.endTableCell();
        indexReport.endTableRow();
        indexReport.endTable();
    }

    private static void addInfoBlockWithColor(RichTextReport report, String mainValue, String subtitle, String extra, String color, String tooltip, String icon, String link) {
        boolean isZero = mainValue.replaceAll("<.*?>", "").replaceAll("\\%", "").equals("0");

        String style = "border-radius: 12px;cursor: pointer;";

        style += "margin: 12px 12px 12px 0px;";
        style += "display: inline-block; width: 130px; height: 102px; z-index: 2;";
        style += "background-color: " + color + "; text-align: center; vertical-align: middle; margin-bottom: 36px;";
        style += "box-shadow: rgba(0, 0, 0, 0.15) 2.4px 2.4px 3.2px;";

        String specialColor = isZero ? " color: grey;" : "color: black;";
        report.startNewTabLink(link, specialColor + "");
        report.startDiv("display: inline-block; text-align: center; margin-top: 12px; cursor: pointer;");
        report.addHtmlContent("<div style='vertical-alignment: bottom; margin: 0px; margin-bottom: -10px; z-index: 3;" + (isZero ? "opacity: 0.4;" : "") + "'>" + getIconSvg(icon, 40) + "</div>");
        report.startDiv(style, tooltip);
        report.addHtmlContent("<div style='font-size: 40px; margin-top: 12px;" + specialColor + "'>" + mainValue + "</div>");
        report.addHtmlContent("<div style='color: #434343; font-size: 12px;" + specialColor + "'>" + subtitle + "</div>");
        report.addHtmlContent("<div style='margin-top: 4px; color: #434343; font-size: 11px;'>" + extra + "</div>");
        report.endDiv();
        report.endDiv();
        report.endNewTabLink();
    }

    private static void addInfoBlockWithColorWithIcon(RichTextReport report, String mainValue, String subtitle, String extra, String color, String tooltip, String icon) {
        String style = "border-radius: 12px;";

        style += "margin: 12px 12px 12px 0px;";
        style += "display: inline-block; width: 130px; height: 93px;";
        style += "background-color: " + color + "; text-align: center; vertical-align: middle; margin-bottom: 36px;";

        report.startDiv(style, tooltip);
        String specialColor = mainValue.equals("<b>0</b>") ? " color: grey;" : "";
        report.addHtmlContent("<div style='font-size: 40px; margin-top: 10px;" + specialColor + "'>" + mainValue + "</div>");
        report.addHtmlContent("<div style='color: #434343; font-size: 12px;" + specialColor + "'>" + subtitle + "</div>");
        report.addHtmlContent("<div style='color: #434343; font-size: 11px;color: grey'>" + extra + "</div>");
        report.endDiv();
    }

    private static void addIconsMainCode(CodeAnalysisResults analysisResults, StringBuilder summary) {
        List<NumericMetric> extensions = analysisResults.getMainAspectAnalysisResults().getLinesOfCodePerExtension();
        summary.append("<div style='margin-bottom: 20px; white-space: nowrap; overflow: hidden;'>");
        boolean first[] = {true};
        extensions.stream().limit(16).forEach(ext -> {
            String lang = ext.getName().toUpperCase().replace("*.", "").trim();
            int loc = ext.getValue().intValue();
            int fontSize = loc >= 1000 ? 20 : 20;
            int width = (first[0] ? loc >= 1000 ? 64 : 65 : loc >= 1000 ? 42 : 43);
            summary.append("<div style='width: " + width + "px; text-align: center; display: inline-block; border-radius: 5px; background-color: white; padding: 8px; margin-right: 4px;'>"
                    + (first[0] ? DataImageUtils.getLangDataImageDiv64(lang) : DataImageUtils.getLangDataImageDiv42(lang))
                    + "<div style='margin-top: 3px; font-size: " + fontSize + "px'>" + FormattingUtils.getSmallTextForNumberMinK(loc) + "</div>"
                    + "<div style='font-size: 10px; white-space: no-wrap; overflow: hidden; color: grey;'>" + lang.toLowerCase() + "</div>"
                    + "</div>");
            first[0] = false;
        });
        summary.append("</div>");
    }

    static void appendLinks(RichTextReport report, CodeAnalysisResults analysisResults) {
        List<Link> links = analysisResults.getCodeConfiguration().getMetadata().getLinks();
        if (links.size() > 0) {
            report.startDiv("font-size: 80%; margin-top: 0px; margin-bottom: 12px; margin-left: 2px;");
            links.forEach(link -> {
                if (links.indexOf(link) > 0) {
                    report.addHtmlContent(" | ");
                }
                report.addNewTabLink(link.getLabel() + "&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON_SMALL, link.getHref());
            });
            report.endDiv();
        }
    }
}
