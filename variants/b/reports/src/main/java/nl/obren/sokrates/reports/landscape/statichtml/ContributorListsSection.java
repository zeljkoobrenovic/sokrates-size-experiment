/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.ProcessingStopwatch;
import nl.obren.sokrates.reports.core.ReportConstants;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositories;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.*;

class ContributorListsSection {
    private static final Log LOG = LogFactory.getLog(LandscapeReportContributorsTab.class);

    private List<RichTextReport> individualReports = new ArrayList<>();
    private List<RichTextReport> botReports = new ArrayList<>();

    private final RichTextReport landscapeReport;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final List<ContributorRepositories> contributors;
    private final LandscapeReportContributorsTab.Type type;
    private final boolean showBots;
    private final File reportsFolder;
    private final RichTextReport landscapeRecentContributorsReport;
    private final RichTextReport landscapeContributorsReport;
    private final RichTextReport landscapeBotsReport;

    ContributorListsSection(RichTextReport landscapeReport, LandscapeAnalysisResults landscapeAnalysisResults, List<ContributorRepositories> contributors, LandscapeReportContributorsTab.Type type, boolean showBots, File reportsFolder,
                            RichTextReport landscapeRecentContributorsReport, RichTextReport landscapeContributorsReport, RichTextReport landscapeBotsReport) {
        this.landscapeReport = landscapeReport;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.contributors = contributors;
        this.type = type;
        this.showBots = showBots;
        this.reportsFolder = reportsFolder;
        this.landscapeRecentContributorsReport = landscapeRecentContributorsReport;
        this.landscapeContributorsReport = landscapeContributorsReport;
        this.landscapeBotsReport = landscapeBotsReport;
    }

    void addContributors() {
        ProcessingStopwatch.start("reporting/contributors");
        int contributorsCount = landscapeAnalysisResults.getContributorsCount(contributors);

        if (contributorsCount > 0) {
            ProcessingStopwatch.start("reporting/contributors/preparing");

            List<ContributorRepositories> bots = landscapeAnalysisResults.getBots();
            Collections.sort(bots, (a, b) -> b.getContributor().getCommitsCount180Days() - a.getContributor().getCommitsCount180Days());
            Collections.sort(bots, (a, b) -> b.getContributor().getCommitsCount90Days() - a.getContributor().getCommitsCount90Days());
            Collections.sort(bots, (a, b) -> b.getContributor().getCommitsCount30Days() - a.getContributor().getCommitsCount30Days());
            List<ContributorRepositories> recentContributors = landscapeAnalysisResults.getRecentContributors(contributors);
            Collections.sort(recentContributors, (a, b) -> b.getContributor().getCommitsCount30Days() - a.getContributor().getCommitsCount30Days());
            int totalCommits = contributors.stream().mapToInt(c -> c.getContributor().getCommitsCount()).sum();
            int botCommits = bots.stream().mapToInt(c -> c.getContributor().getCommitsCount()).sum();
            int totalRecentCommits = recentContributors.stream().mapToInt(c -> c.getContributor().getCommitsCount30Days()).sum();
            final String[] latestCommit = {""};
            contributors.forEach(c -> {
                if (c.getContributor().getLatestCommitDate().compareTo(latestCommit[0]) > 0) {
                    latestCommit[0] = c.getContributor().getLatestCommitDate();
                }
            });

            int recentContributorsCount = recentContributors.size();

            if (recentContributorsCount > 0) {
                addRecentContributorsSection(recentContributorsCount, latestCommit, recentContributors);
            }

            addAllContributorsSection(contributorsCount, bots, latestCommit);

            ProcessingStopwatch.end("reporting/contributors/table");

            saveContributorsTables(bots, recentContributors, totalCommits, botCommits, totalRecentCommits);
        }
        ProcessingStopwatch.end("reporting/contributors");
    }

    private void addAllContributorsSection(int contributorsCount, List<ContributorRepositories> bots, String[] latestCommit) {
        landscapeReport.startDiv("margin-bottom: 16px; margin-top: -6px; vertical-align: middle;");
        landscapeReport.addContentInDiv(ReportConstants.ANIMATION_SVG_ICON, "display: inline-block; vertical-align: middle; margin: 4px;");
        landscapeReport.addNewTabLink("animated contributors history (all time)", "visuals/racing_charts_commits_contributors.html?tickDuration=600");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("animated contributors history (12 months window)", "visuals/racing_charts_commits_window_contributors.html?tickDuration=600");
        landscapeReport.endDiv();

        landscapeReport.startSubSection("<a href='" + type.plural() + ".html' target='_blank' style='text-decoration: none'>" +
                        "All " + StringUtils.capitalize(type.plural()) + " (" + contributorsCount + ")</a>&nbsp;&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON,
                "latest commit " + latestCommit[0]);

        landscapeReport.startShowMoreBlock("show more details...");
        addContributorLinks();

        landscapeReport.addHtmlContent("<iframe src='" + type.plural() + ".html' frameborder=0 style='height: 450px; width: 100%; margin-bottom: 0px; padding: 0;'></iframe>");

        landscapeReport.endShowMoreBlock();
        landscapeReport.endSection();

        if (showBots) {
            landscapeReport.startSubSection("<a href='bots.html' target='_blank' style='text-decoration: none'>" +
                            "Bots (" + bots.size() + ")</a>&nbsp;&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON,
                    "commits of bots");

            landscapeReport.startShowMoreBlock("show more details...");

            landscapeReport.addHtmlContent("<iframe src='bots.html' frameborder=0 style='height: 450px; width: 100%; margin-bottom: 0px; padding: 0;'></iframe>");

            landscapeReport.endShowMoreBlock();
            landscapeReport.endSection();
        }
    }

    private void saveContributorsTables(List<ContributorRepositories> bots, List<ContributorRepositories> recentContributors, int totalCommits, int botCommits, int totalRecentCommits) {
        ProcessingStopwatch.start("reporting/contributors/saving tables");
        Set<String> contributorsLinkedFromTables = new HashSet<>();
        new LandscapeContributorsReport(landscapeAnalysisResults, landscapeRecentContributorsReport, contributorsLinkedFromTables)
                .saveContributorsTable(recentContributors, totalRecentCommits, true);
        new LandscapeContributorsReport(landscapeAnalysisResults, landscapeContributorsReport, contributorsLinkedFromTables)
                .saveContributorsTable(contributors, totalCommits, false);
        new LandscapeContributorsReport(landscapeAnalysisResults, landscapeBotsReport, contributorsLinkedFromTables)
                .saveContributorsTable(bots, botCommits, false);

        ProcessingStopwatch.end("reporting/contributors/saving tables");

        ProcessingStopwatch.start("reporting/contributors/individual reports");
        List<ContributorRepositories> linkedContributors = contributors.stream()
                .filter(c -> contributorsLinkedFromTables.contains(c.getContributor().getEmail()))
                .collect(Collectors.toList());
        LOG.info("Saving individual reports for " + linkedContributors.size() + " contributor(s) linked from tables (out of " + contributors.size() + ")");
        List<ContributorRepositories> linkedBots = bots.stream()
                .filter(c -> contributorsLinkedFromTables.contains(c.getContributor().getEmail()))
                .collect(Collectors.toList());
        LOG.info("Saving bot reports for " + linkedBots.size() + " contributor(s) linked from tables (out of " + linkedBots.size() + ")");
        individualReports = new LandscapeIndividualContributorsReports(landscapeAnalysisResults, reportsFolder).getIndividualReports(linkedContributors);
        botReports = new LandscapeIndividualContributorsReports(landscapeAnalysisResults, reportsFolder).getIndividualReports(linkedBots);
        ProcessingStopwatch.end("reporting/contributors/individual reports");
    }

    private void addRecentContributorsSection(int recentContributorsCount, String[] latestCommit, List<ContributorRepositories> recentContributors) {
        landscapeReport.startSubSection("<a href='" + type.plural() + "-recent.html' target='_blank' style='text-decoration: none'>" +
                        "Recently Active " + StringUtils.capitalize(type.plural()) + " (" + recentContributorsCount + ")</a>&nbsp;&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON,
                "latest commit " + latestCommit[0]);

        addRecentContributorLinks();

        DescriptiveStatistics stats = new DescriptiveStatistics();
        recentContributors.forEach(c -> stats.addValue(c.getContributor().getCommitsCount30Days()));
        double max = Math.max(stats.getMax(), 1);
        double sum = Math.max(stats.getSum(), 1);

        ProcessingStopwatch.end("reporting/contributors/preparing");

        ProcessingStopwatch.start("reporting/contributors/table");
        StringBuilder barsHtml = getBarsHtml(recentContributorsCount, recentContributors, max, sum);

        StringBuilder distHtml = getDistributionHtml(recentContributorsCount, recentContributors, stats, max);

        if (isContributorReport()) {
            addCommitsDistribution(stats, distHtml);
        }

        landscapeReport.addParagraph("contributors sorted by recent commits:", "font-size: 70%; margin-top: 12px;");
        landscapeReport.startDiv("white-space: nowrap; width: 100%; overflow-x: scroll;");
        landscapeReport.addHtmlContent(barsHtml.toString());
        landscapeReport.endDiv();


        landscapeReport.addHtmlContent("<iframe src='" + type.plural() + "-recent.html' frameborder=0 style='height: 450px; width: 100%; margin-bottom: 0px; padding: 0;'></iframe>");

        landscapeReport.endSection();
    }

    private StringBuilder getBarsHtml(int recentContributorsCount, List<ContributorRepositories> recentContributors, double max, double sum) {
        int cumulativeCount[] = {0};
        double prevCumulativePercentage[] = {0};
        int index[] = {0};

        StringBuilder barsHtml = new StringBuilder();
        recentContributors.stream().limit(landscapeAnalysisResults.getConfiguration().getContributorsListLimit()).forEach(c -> {
            index[0] += 1;
            Contributor contributor = c.getContributor();
            int count = contributor.getCommitsCount30Days();
            int height = (int) (Math.round(64 * count / max)) + 1;
            cumulativeCount[0] += count;
            double cumulativePercentage = Math.round(1000.0 * cumulativeCount[0] / sum) / 10;
            double contributorPercentage = Math.round(10000.0 * index[0] / recentContributorsCount) / 100;
            String tooltip = contributor.getEmail()
                    + "\n - commits (30d): " + count
                    + "\n - cumulative commits (top " + index[0] + "): " + cumulativeCount[0]
                    + "\n - cumulative percentage (top " + contributorPercentage + "% " + "): " + cumulativePercentage + "%";
            String color = (prevCumulativePercentage[0] < 50 && cumulativePercentage >= 50) ? "blue" : "skyblue";
            String style = "cursor: help; margin-right: 1px; vertical-align: bottom; width: 8px; background-color: " + color + "; display: inline-block; height: " + height + "px";

            if (contributor.isRookie()) {
                style += "; border-bottom: 4px solid green;";
            } else {
                style += "; border-bottom: 4px solid " + color + ";";
            }

            barsHtml.append("<div title='" + tooltip + "' style='" + style + "'></div>");
            prevCumulativePercentage[0] = cumulativePercentage;
        });
        return barsHtml;
    }

    private StringBuilder getDistributionHtml(int recentContributorsCount, List<ContributorRepositories> recentContributors, DescriptiveStatistics stats, double max) {
        StringBuilder distHtml = new StringBuilder();

        long most = 1;

        for (int i = 1; i <= max; i++) {
            final int d = i;
            most = Math.max(most, recentContributors.stream().filter(c -> c.getContributor().getCommitsCount30Days() == d).count());
        }

        for (int i = 1; i <= max; i++) {
            final int d = i;
            long count = recentContributors.stream().filter(c -> c.getContributor().getCommitsCount30Days() == d).count();
            long height = count > 0 ? (int) (80.0 * count / most) + 5 : 0;
            double median = stats.getPercentile(50);
            String color = d == median ? "blue" : "#990000";
            String style = "cursor: help; margin-right: 1px; vertical-align: bottom; width: 4px; background-color: " + color + "; display: inline-block; height: " + height + "px";
            String title = count + " contributor(s) (" + (Math.round(10000.0 * count / recentContributorsCount) / 100.0) + "%) with " + d + " commit(s)";
            distHtml.append("<div title='" + title + "' style='" + style + "'></div>");
        }
        return distHtml;
    }

    private void addCommitsDistribution(DescriptiveStatistics stats, StringBuilder distHtml) {
        landscapeReport.startDiv("white-space: nowrap; width: 100%; overflow-x: scroll;");
        landscapeReport.addParagraph("commits distribution:", "font-size: 70%;");
        landscapeReport.addHtmlContent(distHtml.toString());
        landscapeReport.endDiv();
        landscapeReport.startDiv("color: grey; font-size: 70%");
        landscapeReport.addHtmlContent("commits per contributor | ");
        for (int p = 90; p >= 10; p -= 10) {
            double percentile = stats.getPercentile(p);
            landscapeReport.addHtmlContent("p(" + p + ") = " + (int) Math.round(percentile) + "; ");
        }
        landscapeReport.endDiv();
    }

    private void addContributorLinks() {
        landscapeReport.addNewTabLink("bubble chart", "visuals/bubble_chart_" + type.plural() + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("tree map", "visuals/tree_map_" + type.plural() + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("txt", "data/" + type.plural() + ".txt");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("json", "data/" + type.plural() + ".json");
        landscapeReport.addLineBreak();
        landscapeReport.addLineBreak();
    }

    private void addRecentContributorLinks() {
        landscapeReport.addNewTabLink("bubble chart", "visuals/bubble_chart_" + type.plural() + "_30_days.html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("tree map", "visuals/tree_map_" + type.plural() + "_30_days.html");
        landscapeReport.addLineBreak();
        landscapeReport.addLineBreak();
    }

    private boolean isContributorReport() {
        return type == LandscapeReportContributorsTab.Type.CONTRIBUTORS;
    }

    List<RichTextReport> getIndividualReports() {
        return individualReports;
    }

    List<RichTextReport> getBotReports() {
        return botReports;
    }
}
