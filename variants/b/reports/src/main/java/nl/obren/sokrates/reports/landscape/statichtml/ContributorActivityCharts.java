/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.utils.ContributorsExtractor;
import nl.obren.sokrates.reports.landscape.utils.RacingRepositoriesBarChartsExporter;
import nl.obren.sokrates.sourcecode.contributors.ContributionTimeSlot;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositories;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportContributorsTab.*;

class ContributorActivityCharts {
    private final RichTextReport landscapeReport;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final List<ContributorRepositories> contributors;
    private final ContributorTimeSlots timeSlots;
    private final File reportsFolder;

    ContributorActivityCharts(RichTextReport landscapeReport, LandscapeAnalysisResults landscapeAnalysisResults, List<ContributorRepositories> contributors, ContributorTimeSlots timeSlots, File reportsFolder) {
        this.landscapeReport = landscapeReport;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.contributors = contributors;
        this.timeSlots = timeSlots;
        this.reportsFolder = reportsFolder;
    }

    private void addSmallInfoBlock(String value, String subtitle, String color, String link) {
        String style = "border-radius: 8px;";

        style += "margin: 4px 4px 4px 0px;";
        style += "display: inline-block; width: 80px; height: 76px;";
        style += "background-color: " + color + "; text-align: center; vertical-align: middle; margin-bottom: 16px;";

        landscapeReport.startDiv(style);
        if (StringUtils.isNotBlank(link)) {
            landscapeReport.startNewTabLink(link, "text-decoration: none");
        }
        landscapeReport.addHtmlContent("<div style='font-size: 24px; margin-top: 8px;'>" + value + "</div>");
        landscapeReport.addHtmlContent("<div style='color: #434343; font-size: 13px'>" + subtitle + "</div>");
        if (StringUtils.isNotBlank(link)) {
            landscapeReport.endNewTabLink();
        }
        landscapeReport.endDiv();
    }

    void addContributorsPerYear(boolean showContributorsCount) {
        List<ContributionTimeSlot> contributorsPerYear = landscapeAnalysisResults.getContributorsPerYear();
        if (contributorsPerYear.size() > 0) {
            int limit = landscapeAnalysisResults.getConfiguration().getCommitsMaxYears();
            if (contributorsPerYear.size() > limit) {
                contributorsPerYear = contributorsPerYear.subList(0, limit);
            }

            int maxCommits = contributorsPerYear.stream().mapToInt(c -> c.getCommitsCount()).max().orElse(1);

            landscapeReport.startDiv("overflow-y: none;");
            landscapeReport.startTable();

            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            addCommitsPerYearRow(contributorsPerYear, maxCommits, thisYear);

            if (showContributorsCount) {
                addContributorsCountPerYearRow(contributorsPerYear, thisYear);
            }

            addYearLabelsRow(contributorsPerYear, thisYear);

            landscapeReport.endTable();
            landscapeReport.endDiv();

            landscapeReport.addLineBreak();
        }
    }

    private void addCommitsPerYearRow(List<ContributionTimeSlot> contributorsPerYear, int maxCommits, int thisYear) {
        landscapeReport.startTableRow();
        landscapeReport.startTableCell("border: none; height: 100px");
        int commitsCount = landscapeAnalysisResults.getCommitsCount();
        if (commitsCount > 0) {
            landscapeReport.startDiv("max-height: 105px");
            addSmallInfoBlock(FormattingUtils.getSmallTextForNumber(commitsCount), "commits", "white", "");
            landscapeReport.endDiv();
        }
        landscapeReport.endTableCell();
        String style = "border: none; text-align: center; vertical-align: bottom; font-size: 80%; height: 100px";
        contributorsPerYear.forEach(year -> {
            landscapeReport.startTableCell(style);
            int count = year.getCommitsCount();
            String color = year.getTimeSlot().equals(thisYear + "") ? "#343434" : "#989898";
            landscapeReport.addParagraph(count + "", "margin: 2px; color: " + color);
            int height = 1 + (int) (64.0 * count / maxCommits);
            String bgColor = year.getTimeSlot().equals(thisYear + "") ? "#343434" : "lightgrey";
            landscapeReport.addHtmlContent("<div style='width: 100%; background-color: " + bgColor + "; height:" + height + "px'></div>");
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }

    private void addContributorsCountPerYearRow(List<ContributionTimeSlot> contributorsPerYear, int thisYear) {
        String style = "border: none; text-align: center; vertical-align: bottom; font-size: 80%; height: 100px";
        int maxContributors[] = {1};
        contributorsPerYear.forEach(year -> {
            int count = timeSlots.getContributorsCountPerYear(year.getTimeSlot());
            maxContributors[0] = Math.max(maxContributors[0], count);
        });
        landscapeReport.startTableRow();
        landscapeReport.startTableCell("border: none; height: 100px");
        int contributorsCount = contributors.size();
        if (contributorsCount > 0) {
            landscapeReport.startDiv("max-height: 105px");
            addSmallInfoBlock(FormattingUtils.getSmallTextForNumber(contributorsCount), "contributors", "white", "");
            landscapeReport.endDiv();
        }
        landscapeReport.endTableCell();
        contributorsPerYear.forEach(year -> {
            landscapeReport.startTableCell(style);
            int count = timeSlots.getContributorsCountPerYear(year.getTimeSlot());
            String color = year.getTimeSlot().equals(thisYear + "") ? "#343434" : "#989898";
            landscapeReport.addParagraph(count + "", "margin: 2px; color: " + color + ";");
            int height = 1 + (int) (64.0 * count / maxContributors[0]);
            landscapeReport.addHtmlContent("<div style='width: 100%; background-color: skyblue; height:" + height + "px'></div>");
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }

    private void addYearLabelsRow(List<ContributionTimeSlot> contributorsPerYear, int thisYear) {
        landscapeReport.startTableRow();
        landscapeReport.addTableCell("", "border: none; ");
        var ref = new Object() {
            String latestCommitDate = landscapeAnalysisResults.getLatestCommitDate();
        };
        if (ref.latestCommitDate.length() > 5) {
            ref.latestCommitDate = ref.latestCommitDate.substring(5);
        }
        contributorsPerYear.forEach(year -> {
            String color = year.getTimeSlot().equals(thisYear + "") ? "#343434" : "#989898";
            landscapeReport.startTableCell("vertical-align: top; border: none; text-align: center; font-size: 90%; color: " + color);
            landscapeReport.addHtmlContent(year.getTimeSlot());
            if (landscapeAnalysisResults.getLatestCommitDate().startsWith(year.getTimeSlot() + "-")) {
                landscapeReport.addContentInDiv(ref.latestCommitDate, "text-align: center; color: grey; font-size: 9px");
            }
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }

    void addContributorsPerWeek() {
        int limit = 104;
        List<ContributionTimeSlot> contributorsPerWeek = getContributionWeeks(landscapeAnalysisResults.getContributorsPerWeek(),
                limit, landscapeAnalysisResults.getLatestCommitDate());

        contributorsPerWeek.sort(Comparator.comparing(ContributionTimeSlot::getTimeSlot).reversed());

        if (contributorsPerWeek.size() > 0) {
            if (contributorsPerWeek.size() > limit) {
                contributorsPerWeek = contributorsPerWeek.subList(0, limit);
            }

            landscapeReport.startDiv("overflow: hidden");
            landscapeReport.startTable();

            int minMaxWindow = contributorsPerWeek.size() >= 4 ? 4 : contributorsPerWeek.size();

            addChartRows(contributorsPerWeek, "weeks", minMaxWindow,
                    (timeSlot, rookiesOnly) -> timeSlots.getContributorsPerWeek(timeSlot, rookiesOnly),
                    (timeSlot, rookiesOnly) -> timeSlots.getLastContributorsPerWeek(timeSlot, true),
                    (timeSlot, rookiesOnly) -> timeSlots.getLastContributorsPerWeek(timeSlot, false), 14);

            landscapeReport.endTable();
            landscapeReport.endDiv();

            landscapeReport.addLineBreak();
        }
    }

    void addContributorsPerDay() {
        int limit = 180;
        List<ContributionTimeSlot> contributorsPerDay = getContributionDays(landscapeAnalysisResults.getContributorsPerDay(),
                limit, landscapeAnalysisResults.getLatestCommitDate());

        contributorsPerDay.sort(Comparator.comparing(ContributionTimeSlot::getTimeSlot).reversed());

        if (contributorsPerDay.size() > 0) {
            if (contributorsPerDay.size() > limit) {
                contributorsPerDay = contributorsPerDay.subList(0, limit);
            }

            landscapeReport.startDiv("overflow: hidden");
            landscapeReport.startTable();

            int minMaxWindow = contributorsPerDay.size() >= 4 ? 4 : contributorsPerDay.size();

            addChartRows(contributorsPerDay, "days", minMaxWindow,
                    (timeSlot, rookiesOnly) -> timeSlots.getContributorsPerDay(timeSlot, rookiesOnly),
                    (timeSlot, rookiesOnly) -> timeSlots.getLastContributorsPerDay(timeSlot, true),
                    (timeSlot, rookiesOnly) -> timeSlots.getLastContributorsPerDay(timeSlot, false), 14);

            landscapeReport.endTable();
            landscapeReport.endDiv();

            landscapeReport.addLineBreak();
        }
    }

    void addContributorsPerMonth() {
        int limit = 24;
        List<ContributionTimeSlot> monthlyContributions = landscapeAnalysisResults.getContributorsPerMonth();
        new RacingRepositoriesBarChartsExporter(landscapeAnalysisResults, landscapeAnalysisResults.getContributorsPerRepositoryAndMonth(), "repositories").exportRacingChart(reportsFolder);
        new RacingRepositoriesBarChartsExporter(landscapeAnalysisResults, landscapeAnalysisResults.getContributorsCommits(), "contributors").exportRacingChart(reportsFolder);
        List<ContributionTimeSlot> contributorsPerMonth = getContributionMonths(monthlyContributions,
                limit, landscapeAnalysisResults.getLatestCommitDate());

        contributorsPerMonth.sort(Comparator.comparing(ContributionTimeSlot::getTimeSlot).reversed());

        if (contributorsPerMonth.size() > 0) {
            if (contributorsPerMonth.size() > limit) {
                contributorsPerMonth = contributorsPerMonth.subList(0, limit);
            }

            landscapeReport.startDiv("overflow: hidden");
            landscapeReport.startTable();

            int minMaxWindow = contributorsPerMonth.size() >= 3 ? 3 : contributorsPerMonth.size();

            addChartRows(contributorsPerMonth, "months", minMaxWindow, (timeSlot, rookiesOnly) -> timeSlots.getContributorsPerMonth(timeSlot, rookiesOnly),
                    (timeSlot, rookiesOnly) -> timeSlots.getLastContributorsPerMonth(timeSlot, true),
                    (timeSlot, rookiesOnly) -> timeSlots.getLastContributorsPerMonth(timeSlot, false), 40);

            landscapeReport.endTable();
            landscapeReport.endDiv();

            landscapeReport.addLineBreak();
        }
    }

    void addContributorsPerYear() {
        List<ContributionTimeSlot> yearlyContributions = landscapeAnalysisResults.getContributorsPerYear();
        List<ContributionTimeSlot> contributorsPerYear = getContributionYears(yearlyContributions,
                landscapeAnalysisResults.getConfiguration().getCommitsMaxYears(), landscapeAnalysisResults.getLatestCommitDate());

        contributorsPerYear.sort(Comparator.comparing(ContributionTimeSlot::getTimeSlot).reversed());

        if (contributorsPerYear.size() > 0) {
            landscapeReport.startDiv("overflow: hidden");
            landscapeReport.startTable();

            int minMaxWindow = contributorsPerYear.size() >= 3 ? 3 : contributorsPerYear.size();

            addChartRows(contributorsPerYear, "years", minMaxWindow, (timeSlot, rookiesOnly) -> timeSlots.getSignificantContributorsPerYear(contributors, timeSlot, rookiesOnly, landscapeAnalysisResults.getConfiguration().getSignificantContributorMinCommitDaysPerYear()),
                    (timeSlot, rookiesOnly) -> timeSlots.getLastContributorsPerYear(timeSlot, true),
                    (timeSlot, rookiesOnly) -> timeSlots.getLastContributorsPerYear(timeSlot, false), 64);

            landscapeReport.endTable();
            landscapeReport.endDiv();

            landscapeReport.addLineBreak();
        }
    }

    private void addChartRows(List<ContributionTimeSlot> contributorsPerWeek, String unit, int minMaxWindow, ContributorsExtractor contributorsExtractor, ContributorsExtractor firstContributorsExtractor, ContributorsExtractor lastContributorsExtractor, int barWidth) {
        addTickMarksPerWeekRow(contributorsPerWeek, barWidth);
        addCommitsPerWeekRow(contributorsPerWeek, minMaxWindow, barWidth);
        addContributorsPerWeekRow(contributorsPerWeek, contributorsExtractor);
        int maxContributors = contributorsPerWeek.stream().mapToInt(c -> contributorsExtractor.getContributors(c.getTimeSlot(), false).size()).max().orElse(1);
        addContributorsPerTimeUnitRow(contributorsPerWeek, firstContributorsExtractor, maxContributors, true, "bottom");
        addContributorsPerTimeUnitRow(contributorsPerWeek, lastContributorsExtractor, maxContributors, false, "top");
    }

    private void addContributorsPerWeekRow(List<ContributionTimeSlot> contributorsPerWeek, ContributorsExtractor contributorsExtractor) {
        landscapeReport.startTableRow();
        int max = 1;
        for (ContributionTimeSlot contributionTimeSlot : contributorsPerWeek) {
            max = Math.max(contributorsExtractor.getContributors(contributionTimeSlot.getTimeSlot(), false).size(), max);
        }
        int maxContributors = max;
        landscapeReport.addTableCell("<b>Contributors</b>" +
                "<div style='font-size: 80%; margin-left: 8px'><div style='color: green'>rookies</div><div style='color: #588BAE'>veterans</div></div>", "border: none");
        contributorsPerWeek.forEach(week -> {
            landscapeReport.startTableCell("max-width: 20px; padding: 0; margin: 1px; border: none; text-align: center; vertical-align: bottom; font-size: 80%; height: 100px");
            List<String> extractedContributors = contributorsExtractor.getContributors(week.getTimeSlot(), false);
            List<String> rookies = contributorsExtractor.getContributors(week.getTimeSlot(), true);
            int count = extractedContributors.size();
            int rookiesCount = rookies.size();
            int height = 2 + (int) (64.0 * count / maxContributors);
            int heightRookies = 1 + (int) (64.0 * rookiesCount / maxContributors);
            String title = "period " + week.getTimeSlot() + " = " + count + " extractedContributors (" + rookiesCount + " rookies):\n\n" +
                    extractedContributors.subList(0, extractedContributors.size() < 200 ? extractedContributors.size() : 200).stream().collect(Collectors.joining(", "));
            String yearString = week.getTimeSlot().split("[-]")[0];

            String color = "darkgrey";

            if (StringUtils.isNumeric(yearString)) {
                int year = Integer.parseInt(yearString);
                color = year % 2 == 0 ? "#89CFF0" : "#588BAE";
            }

            landscapeReport.addHtmlContent("<div>");
            landscapeReport.addHtmlContent("<div title='" + title + "' style='width: 100%; color: grey; font-size: 80%; margin: 1px'>" + count + "</div>");
            landscapeReport.addHtmlContent("<div title='" + title + "' style='width: 100%; background-color: green; height:" + (heightRookies) + "px; margin: 1px'></div>");
            landscapeReport.addHtmlContent("<div title='" + title + "' style='width: 100%; background-color: " + color + "; height:" + (height - heightRookies) + "px; margin: 1px'></div>");
            landscapeReport.addHtmlContent("</div>");
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }

    private void addContributorsPerTimeUnitRow(List<ContributionTimeSlot> contributorsPerWeek, ContributorsExtractor contributorsExtractor, int maxContributors, boolean first, final String valign) {
        landscapeReport.startTableRow();
        landscapeReport.addTableCell("<b>" + (first ? "First" : "Last") + " Contribution</b>" +
                "<div style='color: grey; font-size: 80%; margin-left: 8px; margin-top: 4px;'>"
                + "</div>", "border: none; vertical-align: " + (first ? "bottom" : "top"));
        boolean firstItem[] = {true};
        contributorsPerWeek.forEach(timeUnit -> {
            landscapeReport.startTableCell("max-width: 20px; padding: 0; margin: 1px; border: none; text-align: center; vertical-align: " + valign + "; font-size: 80%; height: 100px");
            List<String> extractedContributors = contributorsExtractor.getContributors(timeUnit.getTimeSlot(), true);
            int count = extractedContributors.size();
            int height = 4 + (int) (64.0 * count / maxContributors);
            String title = "timeUnit of " + timeUnit.getTimeSlot() + " = " + count + " extractedContributors:\n\n" +
                    extractedContributors.subList(0, extractedContributors.size() < 200 ? extractedContributors.size() : 200).stream().collect(Collectors.joining(", "));
            String yearString = timeUnit.getTimeSlot().split("[-]")[0];

            String color = "lightgrey";

            if (count > 0 && StringUtils.isNumeric(yearString)) {
                int year = Integer.parseInt(yearString);
                if (first) {
                    color = year % 2 == 0 ? "limegreen" : "darkgreen";
                } else {
                    if (firstItem[0]) {
                        color = "rgba(220,220,220,100)";
                    } else {
                        color = year % 2 == 0 ? "crimson" : "rgba(100,0,0,100)";
                    }
                }
            } else {
                height = 1;
            }

            if (first && count > 0) {
                landscapeReport.addHtmlContent("<div title='" + title + "' style='width: 100%; color: grey; font-size: 80%; margin: 1px'>" + count + "</div>");
            }
            landscapeReport.addHtmlContent("<div title='" + title + "' style='width: 100%; background-color: " + color + "; height:" + height + "px; margin: 1px'></div>");
            if (!first && count > 0) {
                landscapeReport.addHtmlContent("<div title='" + title + "' style='width: 100%; color: grey; font-size: 80%; margin: 1px'>" + count + "</div>");
            }
            landscapeReport.endTableCell();
            firstItem[0] = false;
        });
        landscapeReport.endTableRow();
    }

    private void addTickMarksPerWeekRow(List<ContributionTimeSlot> contributorsPerWeek, int barWidth) {
        landscapeReport.startTableRow();
        landscapeReport.addTableCell("", "border: none");

        for (int i = 0; i < contributorsPerWeek.size(); i++) {
            ContributionTimeSlot week = contributorsPerWeek.get(i);

            String yearString = week.getTimeSlot().split("[-]")[0];

            String color = "darkgrey";

            if (StringUtils.isNumeric(yearString)) {
                int year = Integer.parseInt(yearString);
                color = year % 2 == 0 ? "#c9c9c9" : "#656565";
            }
            String[] splitNow = week.getTimeSlot().split("-");
            String textNow = splitNow.length < 2 ? splitNow[0] : splitNow[0] + "<br>" + splitNow[1];

            int colspan = 1;

            while (true) {
                String nextTimeSlot = contributorsPerWeek.size() > i + 1 ? contributorsPerWeek.get(i + 1).getTimeSlot() : "";
                String[] splitNext = nextTimeSlot.split("-");
                String textNext = splitNext.length < 2 ? "" : splitNext[0] + "<br>" + splitNext[1];
                if (contributorsPerWeek.size() <= i + 1 || !textNow.equalsIgnoreCase(textNext)) {
                    break;
                }
                colspan++;
                i++;
            }
            landscapeReport.startTableCellColSpan(colspan, "width: "
                    + barWidth + "px; min-width: "
                    + barWidth + "px; padding: 0; margin: 1px; border: none; text-align: center; vertical-align: bottom; font-size: 80%; height: 16px");
            landscapeReport.addHtmlContent("<div style='width: 100%; margin: 1px; font-size: 80%; color: '" + color + ">"
                    + textNow + "</div>");
            landscapeReport.endTableCell();
        }
        landscapeReport.endTableRow();
    }

    private void addCommitsPerWeekRow(List<ContributionTimeSlot> contributorsPerWeek, int minMaxWindow, int barWidth) {
        landscapeReport.startTableRow();
        int maxCommits = contributorsPerWeek.stream().mapToInt(c -> c.getCommitsCount()).max().orElse(1);
        int maxCommits4Weeks = contributorsPerWeek.subList(0, minMaxWindow).stream().mapToInt(c -> c.getCommitsCount()).max().orElse(0);
        int minCommits4Weeks = contributorsPerWeek.subList(0, minMaxWindow).stream().mapToInt(c -> c.getCommitsCount()).min().orElse(0);
        landscapeReport.addTableCell("<b>Commits</b>" +
                "<div style='color: grey; font-size: 80%; margin-left: 8px; margin-top: 4px;'>"
                + "min (" + minMaxWindow + " weeks): " + minCommits4Weeks
                + "<br>max (" + minMaxWindow + " weeks): " + maxCommits4Weeks + "</div>", "border: none");
        contributorsPerWeek.forEach(week -> {
            landscapeReport.startTableCell("width: " + barWidth + "px; min-width: " + barWidth + "px; padding: 0; margin: 1px; border: none; text-align: center; vertical-align: bottom; font-size: 80%; height: 100px");
            int count = week.getCommitsCount();
            int height = 1 + (int) (64.0 * count / maxCommits);
            String title = "week of " + week.getTimeSlot() + " = " + count + " commits";
            String yearString = week.getTimeSlot().split("[-]")[0];

            String color = "darkgrey";

            if (StringUtils.isNumeric(yearString)) {
                int year = Integer.parseInt(yearString);
                color = year % 2 == 0 ? "#c9c9c9" : "#656565";
            }

            landscapeReport.addHtmlContent("<div title='" + title + "' style='width: 100%; color: grey; font-size: 70%; margin: 0px'>" + count + "</div>");
            landscapeReport.addHtmlContent("<div title='" + title + "' style='width: 100%; background-color: " + color + "; height:" + height + "px; margin: 1px'></div>");
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }
}
