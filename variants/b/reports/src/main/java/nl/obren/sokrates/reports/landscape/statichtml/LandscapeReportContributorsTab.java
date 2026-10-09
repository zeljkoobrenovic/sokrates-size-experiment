/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.common.utils.ProcessingStopwatch;
import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.contributors.ContributionTimeSlot;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.*;
import nl.obren.sokrates.sourcecode.landscape.analysis.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.*;

public class LandscapeReportContributorsTab {

    enum Type {
        CONTRIBUTORS("contributor", "contributors", true),
        TEAMS("team", "teams", false);
        private final String singular;
        private final String plural;
        private final boolean showBots;

        Type(String singular, String plural, boolean showBots) {
            this.singular = singular;
            this.plural = plural;
            this.showBots = showBots;
        }

        public String singular() {
            return singular;
        }

        public String plural() {
            return plural;
        }
    }

    private static final Log LOG = LogFactory.getLog(LandscapeReportContributorsTab.class);
    public static final String PEOPLE_COLOR = "#ADD8E6";
    private final List<ContributorRepositories> contributors;
    private RichTextReport landscapeRecentContributorsReport = new RichTextReport("", "${type}-recent.html");
    private RichTextReport landscapeContributorsReport = new RichTextReport("", "${type}.html");
    private RichTextReport landscapeBotsReport = new RichTextReport("", "bots.html");
    private LandscapeAnalysisResults landscapeAnalysisResults;
    private File folder;
    private File reportsFolder;
    private RichTextReport landscapeReport;
    private final Type type;
    private final TeamsConfig teamsConfig;
    private final ContributorActivityCharts activityCharts;
    private final ContributorsPerExtensionSection perExtensionSection;
    private final ContributorListsSection listsSection;

    public LandscapeReportContributorsTab(LandscapeAnalysisResults landscapeAnalysisResults, List<ContributorRepositories> contributors, RichTextReport landscapeReport, File folder, File reportsFolder, Type type, TeamsConfig teamsConfig) {
        this.contributors = contributors;
        this.folder = folder;
        this.reportsFolder = reportsFolder;
        this.landscapeReport = landscapeReport;
        this.type = type;
        this.teamsConfig = teamsConfig;

        landscapeRecentContributorsReport.setFileName(type.plural() + "-recent.html");
        landscapeContributorsReport.setFileName(type.plural() + ".html");

        this.landscapeAnalysisResults = landscapeAnalysisResults;

        landscapeRecentContributorsReport.setEmbedded(true);
        landscapeContributorsReport.setEmbedded(true);
        landscapeBotsReport.setEmbedded(true);

        ContributorTimeSlots timeSlots = new ContributorTimeSlots(contributors, landscapeAnalysisResults);
        this.activityCharts = new ContributorActivityCharts(landscapeReport, landscapeAnalysisResults, contributors, timeSlots, reportsFolder);
        this.perExtensionSection = new ContributorsPerExtensionSection(landscapeReport, landscapeAnalysisResults, type, teamsConfig, reportsFolder);
        this.listsSection = new ContributorListsSection(landscapeReport, landscapeAnalysisResults, contributors, type, type.showBots, reportsFolder,
                landscapeRecentContributorsReport, landscapeContributorsReport, landscapeBotsReport);

        landscapeContributorsReport.setEmbedded(true);
        landscapeBotsReport.setEmbedded(true);
        landscapeRecentContributorsReport.setEmbedded(true);
    }

    void addContributorsTabs(String tabId) {
        int recentContributorsCount = landscapeAnalysisResults.getRecentContributorsCount(contributors);
        landscapeReport.startTabContentSection(tabId, false);
        ProcessingStopwatch.start("reporting/summary");
        LOG.info("Adding big contributors summary...");
        addBigContributorsSummary();
        if (recentContributorsCount > 0) {
            perExtensionSection.addContributorsPerExtension(true);
        }
        addIFrames(landscapeAnalysisResults.getConfiguration().getiFramesContributorsAtStart());
        LOG.info("Adding contributors...");
        listsSection.addContributors();
        if (isContributorReport()) {
            perExtensionSection.addContributorsPerExtension();
        }

        LOG.info("Adding trends...");
        if (isContributorReport()) {
            landscapeReport.addLevel2Header("Contribution Trends");
            addContributionTrends();
        }
        addIFrames(landscapeAnalysisResults.getConfiguration().getiFramesContributors());
        ProcessingStopwatch.end("reporting/summary");
        landscapeReport.endTabContentSection();
    }

    public static List<ContributionTimeSlot> getContributionDays(List<ContributionTimeSlot> contributorsPerDayOriginal, int pastDays, String lastCommitDate) {
        List<ContributionTimeSlot> contributorsPerDay = new ArrayList<>(contributorsPerDayOriginal);
        List<String> slots = contributorsPerDay.stream().map(slot -> slot.getTimeSlot()).collect(Collectors.toCollection(ArrayList::new));
        List<String> pastDates = DateUtils.getPastDays(pastDays, lastCommitDate);
        pastDates.forEach(pastDate -> {
            if (!slots.contains(pastDate)) {
                contributorsPerDay.add(new ContributionTimeSlot(pastDate));
            }
        });
        return contributorsPerDay;
    }

    public static List<ContributionTimeSlot> getContributionWeeks(List<ContributionTimeSlot> contributorsPerWeekOriginal, int pastWeeks, String lastCommitDate) {
        List<ContributionTimeSlot> contributorsPerWeek = new ArrayList<>(contributorsPerWeekOriginal);
        List<String> slots = contributorsPerWeek.stream().map(slot -> slot.getTimeSlot()).collect(Collectors.toCollection(ArrayList::new));
        List<String> pastDates = DateUtils.getPastWeeks(pastWeeks, lastCommitDate);
        pastDates.forEach(pastDate -> {
            if (!slots.contains(pastDate)) {
                contributorsPerWeek.add(new ContributionTimeSlot(pastDate));
            }
        });
        return contributorsPerWeek;
    }

    public static List<ContributionTimeSlot> getContributionYears(List<ContributionTimeSlot> contributorsPerWeekOriginal, int pastYears, String lastCommitDate) {
        List<ContributionTimeSlot> contributorsPerWeek = new ArrayList<>(contributorsPerWeekOriginal);
        List<String> slots = contributorsPerWeek.stream().map(slot -> slot.getTimeSlot()).collect(Collectors.toCollection(ArrayList::new));
        List<String> pastDates = DateUtils.getPastYears(pastYears, lastCommitDate);
        pastDates.forEach(pastDate -> {
            if (!slots.contains(pastDate)) {
                contributorsPerWeek.add(new ContributionTimeSlot(pastDate));
            }
        });
        return contributorsPerWeek;
    }

    public static List<ContributionTimeSlot> getContributionMonths(List<ContributionTimeSlot> contributorsPerMonthOriginal, int pastMonths, String lastCommitDate) {
        List<ContributionTimeSlot> contributorsPerMonth = new ArrayList<>(contributorsPerMonthOriginal);
        List<String> slots = contributorsPerMonth.stream().map(slot -> slot.getTimeSlot()).collect(Collectors.toCollection(ArrayList::new));
        List<String> pastDates = DateUtils.getPastMonths(pastMonths, lastCommitDate);
        pastDates.forEach(pastDate -> {
            if (!slots.contains(pastDate)) {
                contributorsPerMonth.add(new ContributionTimeSlot(pastDate));
            }
        });
        return contributorsPerMonth;
    }

    private void addBigContributorsSummary() {
        long contributorsCount = contributors.size();
        int mainLocActive = landscapeAnalysisResults.getMainLoc1YearActive();
        int mainLocNew = landscapeAnalysisResults.getMainLocNew();
        if (contributorsCount > 0) {
            int recentContributorsCount = landscapeAnalysisResults.getRecentContributorsCount(contributors);
            int locPerRecentContributor = 0;
            int locNewPerRecentContributor = 0;
            if (recentContributorsCount > 0) {
                locPerRecentContributor = (int) Math.round((double) mainLocActive / recentContributorsCount);
                locNewPerRecentContributor = (int) Math.round((double) mainLocNew / recentContributorsCount);
            }
            addPeopleInfoBlock(FormattingUtils.getSmallTextForNumber(recentContributorsCount), "recent " + type.plural(),
                    "(past 30 days)", getExtraPeopleInfo(contributors, contributorsCount) + "\n" + FormattingUtils.formatCount(locPerRecentContributor) + " active lines of code per recent " + type.singular());
            addPeopleInfoBlock(FormattingUtils.getSmallTextForNumber(landscapeAnalysisResults.getRecentContributorsCount3Months(contributors)), "3m " + type.plural(),
                    "(past 90 days)", getExtraPeopleInfo(contributors, contributorsCount));
            addPeopleInfoBlock(FormattingUtils.getSmallTextForNumber(landscapeAnalysisResults.getRecentContributorsCount6Months(contributors)), "6m " + type.plural(),
                    "(past 180 days)", getExtraPeopleInfo(contributors, contributorsCount));
            int rookiesContributorsCount = landscapeAnalysisResults.getRookiesContributorsCount(contributors);
            addPeopleInfoBlock(FormattingUtils.getSmallTextForNumber(rookiesContributorsCount),
                    ("rookie " + type.plural()),
                    "(started in past year)", "active contributors with the first commit in past year");
            addWorkloadInfoBlock(FormattingUtils.getSmallTextForNumber(locPerRecentContributor), type.singular() + " load",
                    "(active LOC/" + type.singular() + ")", "active lines of code per recent " + type.singular() + "\n\n" + FormattingUtils.getPlainTextForNumber(locNewPerRecentContributor) + " new LOC/recent " + type.singular());
            List<ComponentDependency> peopleDependencies = ContributorConnectionUtils.getPeopleDependencies(contributors, 0, 30);
            peopleDependencies.sort((a, b) -> b.getCount() - a.getCount());
        }
    }

    private void addContributionTrends() {
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        int commitsMaxYears = configuration.getCommitsMaxYears();
        int significantContributorMinCommitDaysPerYear = configuration.getSignificantContributorMinCommitDaysPerYear();

        landscapeReport.startSubSection("Per Year", "Past " + commitsMaxYears + " years");
        activityCharts.addContributorsPerYear(true);
        landscapeReport.endSection();
        LOG.info("Adding contributors per extension...");

        landscapeReport.startSubSection("Significant Contributions Per Year (" + significantContributorMinCommitDaysPerYear + "+ commit days per year)", "Past " + commitsMaxYears + " years");
        activityCharts.addContributorsPerYear();
        landscapeReport.endSection();

        landscapeReport.startSubSection("Per Month", "Past two years");
        activityCharts.addContributorsPerMonth();
        landscapeReport.endSection();

        landscapeReport.startSubSection("Per Week", "Past two years");
        activityCharts.addContributorsPerWeek();
        landscapeReport.endSection();

        landscapeReport.startSubSection("Per Day", "Past six months");
        activityCharts.addContributorsPerDay();
        landscapeReport.endSection();

        landscapeReport.addParagraph("latest commit date: <b>" + landscapeAnalysisResults.getLatestCommitDate() + "</b>", "color: grey");
    }

    private void addIFrames(List<WebFrameLink> iframes) {
        if (iframes.size() > 0) {
            iframes.forEach(iframe -> {
                addIFrame(iframe);
            });
        }
    }

    private void addIFrame(WebFrameLink iframe) {
        if (StringUtils.isNotBlank(iframe.getTitle())) {
            String title;
            if (StringUtils.isNotBlank(iframe.getMoreInfoLink())) {
                title = "<a href='" + iframe.getMoreInfoLink() + "' target='_blank' style='text-decoration: none'>" + iframe.getTitle() + "</a>";
                title += "&nbsp;&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON;
            } else {
                title = iframe.getTitle();
            }
            landscapeReport.startSubSection(title, "");
        }
        String style = StringUtils.defaultIfBlank(iframe.getStyle(), "width: 100%; height: 200px; border: 1px solid lightgrey;");
        landscapeReport.addHtmlContent("<iframe src='" + iframe.getSrc()
                + "' frameborder='0' style='" + style + "'"
                + (iframe.getScrolling() ? "" : " scrolling='no' ")
                + "></iframe>");
        if (StringUtils.isNotBlank(iframe.getTitle())) {
            landscapeReport.endSection();
        }
    }

    private String getExtraPeopleInfo(List<ContributorRepositories> contributors, long contributorsCount) {
        String info = "";

        int recentContributorsCount6Months = landscapeAnalysisResults.getRecentContributorsCount6Months(contributors);
        int recentContributorsCount3Months = landscapeAnalysisResults.getRecentContributorsCount3Months(contributors);
        info += FormattingUtils.getPlainTextForNumber(landscapeAnalysisResults.getRecentContributorsCount(contributors)) + " contributors (30 days)\n";
        info += FormattingUtils.getPlainTextForNumber(recentContributorsCount3Months) + " contributors (3 months)\n";
        info += FormattingUtils.getPlainTextForNumber(recentContributorsCount6Months) + " contributors (6 months)\n";

        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        int thresholdCommits = configuration.getContributorThresholdCommits();
        info += FormattingUtils.getPlainTextForNumber((int) contributorsCount) + " contributors (all time)\n";
        info += "\nOnly the contributors with " + (thresholdCommits > 1 ? "(" + thresholdCommits + "+&nbsp;commits)" : "") + " included";

        return info;
    }

    private void addPeopleInfoBlock(String mainValue, String subtitle, String description, String tooltip) {
        addPeopleInfoBlockWithColor(mainValue, subtitle, description, tooltip, PEOPLE_COLOR);
    }

    private void addWorkloadInfoBlock(String mainValue, String subtitle, String description, String tooltip) {
        addWorkloadInfoBlockWithColor(mainValue, subtitle, description, tooltip, "orange");
    }

    private void addPeopleInfoBlockWithColor(String mainValue, String subtitle, String description, String tooltip, String color) {
        if (StringUtils.isNotBlank(description)) {
            subtitle += "<br/><span style='color: #707070; font-size: 80%'>" + description + "</span>";
        }
        addInfoBlockWithColor(mainValue, subtitle, color, tooltip, isContributorReport() ? "contributors" : "teams");
    }

    private void addWorkloadInfoBlockWithColor(String mainValue, String subtitle, String description, String tooltip, String color) {
        if (StringUtils.isNotBlank(description)) {
            subtitle += "<br/><span style='color: grey; font-size: 80%'>" + description + "</span>";
        }
        addInfoBlockWithColor(mainValue, subtitle, color, tooltip, "workload");
    }

    private void addInfoBlockWithColor(String mainValue, String subtitle, String color, String tooltip, String icon) {
        String style = "border-radius: 12px;";

        style += "margin: 12px 12px 12px 0px;";
        style += "display: inline-block; width: 160px; height: 120px;";
        style += "background-color: " + color + "; text-align: center; vertical-align: middle; margin-bottom: 36px;";
        style += "box-shadow: rgb(0 0 0 / 12%) 0px 1px 3px, rgb(0 0 0 / 24%) 0px 1px 2px;";

        landscapeReport.startDiv("display: inline-block; text-align: center", tooltip);
        landscapeReport.addContentInDiv(ReportFileExporter.getIconSvg(icon, 48), "margin-top: 18px; margin-bottom: -12px");
        landscapeReport.startDiv(style, tooltip);
        String specialColor = mainValue.equals("<b>0</b>") ? " color: grey;" : "";
        landscapeReport.addHtmlContent("<div style='font-size: 50px; margin-top: 20px;" + specialColor + "'>" + mainValue + "</div>");
        landscapeReport.addHtmlContent("<div style='color: #434343; font-size: 15px;" + specialColor + "'>" + subtitle + "</div>");
        landscapeReport.endDiv();
        landscapeReport.endDiv();
    }

    private boolean isContributorReport() {
        return type == Type.CONTRIBUTORS;
    }

    public RichTextReport getLandscapeContributorsReport() {
        return landscapeContributorsReport;
    }

    public RichTextReport getLandscapeRecentContributorsReport() {
        return landscapeRecentContributorsReport;
    }

    public RichTextReport getLandscapeBotsReport() {
        return landscapeBotsReport;
    }

    public List<RichTextReport> getIndividualReports() {
        return listsSection.getIndividualReports();
    }

    public List<RichTextReport> getBotReports() {
        return listsSection.getBotReports();
    }
}
