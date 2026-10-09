/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.githistory.ContributorPerExtensionStats;
import nl.obren.sokrates.sourcecode.landscape.ContributionCounter;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorConnections;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


public class ContributorsReportGenerator {
    private final CodeAnalysisResults codeAnalysisResults;
    private File reportsFolder;
    private RichTextReport report;
    private ContributorDependenciesRenderer dependenciesRenderer;
    private Map<String, Contributor> emailContributorMap = new HashMap<>();
    private Map<String, List<Pair<String, ContributorPerExtensionStats>>> emailStatsMap = new HashMap<>();

    public ContributorsReportGenerator(CodeAnalysisResults codeAnalysisResults) {
        this.codeAnalysisResults = codeAnalysisResults;
        codeAnalysisResults.getContributorsAnalysisResults().getContributors().forEach(contributor -> {
            emailContributorMap.put(contributor.getEmail(), contributor);
        });
        codeAnalysisResults.getContributorsAnalysisResults().getCommitsPerExtensions().forEach(commitsPerExtension -> {
            commitsPerExtension.getContributorPerExtensionStats().forEach(contributorPerExtensionStats -> {
                String email = contributorPerExtensionStats.getContributor();
                if (!emailStatsMap.containsKey(email)) {
                    emailStatsMap.put(email, new ArrayList<>());
                }
                emailStatsMap.get(email).add(Pair.of(commitsPerExtension.getExtension(), contributorPerExtensionStats));
            });
        });
    }

    public static List<ContributorConnections> contributorConnections(List<ComponentDependency> peopleDependencies) {
        Map<String, ContributorConnections> map = new HashMap<>();

        peopleDependencies.forEach(dependency -> {
            String from = dependency.getFromComponent();
            String to = dependency.getToComponent();

            ContributorConnections contributorConnections1 = map.get(from);
            ContributorConnections contributorConnections2 = map.get(to);

            if (contributorConnections1 == null) {
                contributorConnections1 = new ContributorConnections();
                contributorConnections1.setEmail(from);
                contributorConnections1.setConnectionsCount(1);
                map.put(from, contributorConnections1);
            } else {
                contributorConnections1.setConnectionsCount(contributorConnections1.getConnectionsCount() + 1);
            }

            if (contributorConnections2 == null) {
                contributorConnections2 = new ContributorConnections();
                contributorConnections2.setEmail(to);
                contributorConnections2.setConnectionsCount(1);
                map.put(to, contributorConnections2);
            } else {
                contributorConnections2.setConnectionsCount(contributorConnections2.getConnectionsCount() + 1);
            }
        });

        List<ContributorConnections> names = new ArrayList<>(map.values());
        names.sort((a, b) -> b.getConnectionsCount() - a.getConnectionsCount());

        return names;
    }

    public void addContributorsAnalysisToReport(File reportsFolder, RichTextReport report) {
        this.reportsFolder = reportsFolder;
        this.report = report;
        this.dependenciesRenderer = new ContributorDependenciesRenderer(report, reportsFolder, emailContributorMap);


        report.addParagraph("An overview of contributor trends.", "margin-top: 12px; color: grey");

        addTabs();

        ContributorsAnalysisResults analysis = codeAnalysisResults.getContributorsAnalysisResults();
        List<Contributor> contributors = analysis.getContributors();

        List<Contributor> people = contributors.stream().filter(c -> !c.isBot()).collect(Collectors.toList());
        List<Contributor> bots = contributors.stream().filter(c -> c.isBot()).collect(Collectors.toList());

        report.startTabContentSection("contributors", false);
        addZoomableCircleLinks(report);
        ContributorsReportUtils.addContributorsSection(codeAnalysisResults, report);
        report.endTabContentSection();

        report.startTabContentSection("matrix", true);
        ContributorsMatrixRenderer matrixRenderer = new ContributorsMatrixRenderer(report);
        matrixRenderer.addMatrix(new ArrayList<>(people), "Contributors");
        matrixRenderer.addMatrix(new ArrayList<>(bots), "Bots");
        report.endTabContentSection();

        report.startTabContentSection("30_days", false);
        add30DaysSection(analysis, contributors, people, bots);
        report.endTabContentSection();

        report.startTabContentSection("90_days", false);
        add90DaysSection(analysis, contributors, people, bots);
        report.endTabContentSection();

        report.startTabContentSection("180_days", false);
        add180DaysSection(analysis, contributors, people, bots);
        report.endTabContentSection();

        report.startTabContentSection("365_days", false);
        add365DaysSection(analysis, contributors, people, bots);
        report.endTabContentSection();

        report.startTabContentSection("data", false);
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("Contributors' details...", "../data/text/contributors.txt");
        report.endListItem();
        report.endUnorderedList();
        report.endTabContentSection();
    }

    private void addTabs() {
        report.startTabGroup();
        report.addTab("matrix", "Contributors Matrix", true);
        report.addTab("30_days", "Past 30 Days", false);
        report.addTab("90_days", "Past 3 Months", false);
        report.addTab("180_days", "Past 6 Months", false);
        report.addTab("365_days", "Past Year", false);
        report.addTab("contributors", "Overview", false);
        report.addTab("data", "Data", false);
        report.endTabGroup();
    }

    private void add30DaysSection(ContributorsAnalysisResults analysis, List<Contributor> contributors, List<Contributor> people, List<Contributor> bots) {
        List<Contributor> commits30Days = contributors.stream().filter(c -> c.getCommitsCount30Days() > 0).collect(Collectors.toList());
        if (commits30Days.size() > 0) {
            List<Contributor> peopleCommits30Days = people.stream().filter(c -> c.getCommitsCount30Days() > 0).collect(Collectors.toList());
            List<Contributor> botCommits30Days = bots.stream().filter(c -> c.getCommitsCount30Days() > 0).collect(Collectors.toList());
            commits30Days.sort((a, b) -> b.getCommitsCount30Days() - a.getCommitsCount30Days());
            addContributorsPanel(report, peopleCommits30Days, c -> c.getCommitsCount30Days(), true, e -> e.getFileUpdates30Days(), "Contributor");
            addContributorsPanel(report, botCommits30Days, c -> c.getCommitsCount30Days(), true, e -> e.getFileUpdates30Days(), "Bot");
            renderPeopleDependencies(analysis.getPeopleDependencies30Days(), analysis.getPeopleFileDependencies30Days(), 30, c -> c.getCommitsCount30Days(), commits30Days);
        } else {
            report.addParagraph("No commits in past 30 days.", "margin-top: 16px");
        }
    }

    private void add90DaysSection(ContributorsAnalysisResults analysis, List<Contributor> contributors, List<Contributor> people, List<Contributor> bots) {
        List<Contributor> commits90Days = contributors.stream().filter(c -> c.getCommitsCount90Days() > 0).collect(Collectors.toList());
        if (commits90Days.size() > 0) {
            List<Contributor> peopleCommits90Days = people.stream().filter(c -> c.getCommitsCount90Days() > 0).collect(Collectors.toList());
            List<Contributor> botCommits90Days = bots.stream().filter(c -> c.getCommitsCount90Days() > 0).collect(Collectors.toList());
            commits90Days.sort((a, b) -> b.getCommitsCount90Days() - a.getCommitsCount90Days());
            addContributorsPanel(report, peopleCommits90Days, c -> c.getCommitsCount90Days(), true, e -> e.getFileUpdates90Days(), "Contributor");
            addContributorsPanel(report, botCommits90Days, c -> c.getCommitsCount90Days(), true, e -> e.getFileUpdates90Days(), "Bot");
            renderPeopleDependencies(analysis.getPeopleDependencies90Days(), analysis.getPeopleFileDependencies90Days(), 90, c -> c.getCommitsCount90Days(), commits90Days);
        } else {
            report.addParagraph("No commits in past 90 days.", "margin-top: 16px");
        }
    }

    private void add180DaysSection(ContributorsAnalysisResults analysis, List<Contributor> contributors, List<Contributor> people, List<Contributor> bots) {
        List<Contributor> commits180Days = contributors.stream().filter(c -> c.getCommitsCount180Days() > 0).collect(Collectors.toList());
        if (commits180Days.size() > 0) {
            List<Contributor> peopleCommits180Days = people.stream().filter(c -> c.getCommitsCount180Days() > 0).collect(Collectors.toList());
            List<Contributor> botCommits180Days = bots.stream().filter(c -> c.getCommitsCount180Days() > 0).collect(Collectors.toList());
            commits180Days.sort((a, b) -> b.getCommitsCount180Days() - a.getCommitsCount180Days());
            addContributorsPanel(report, peopleCommits180Days, c -> c.getCommitsCount180Days(), true, null, "Contributor");
            addContributorsPanel(report, botCommits180Days, c -> c.getCommitsCount180Days(), true, null, "Bot");
            renderPeopleDependencies(analysis.getPeopleDependencies180Days(), analysis.getPeopleFileDependencies180Days(), 180, c -> c.getCommitsCount180Days(), commits180Days);
        } else {
            report.addParagraph("No commits in past 180 days.", "margin-top: 16px");
        }
    }

    private void add365DaysSection(ContributorsAnalysisResults analysis, List<Contributor> contributors, List<Contributor> people, List<Contributor> bots) {
        List<Contributor> commits365Days = contributors.stream().filter(c -> c.getCommitsCount365Days() > 0).collect(Collectors.toList());
        commits365Days.sort((a, b) -> b.getCommitsCount365Days() - a.getCommitsCount365Days());
        List<Contributor> peopleCommits365Days = people.stream().filter(c -> c.getCommitsCount365Days() > 0).collect(Collectors.toList());
        List<Contributor> botCommits365Days = bots.stream().filter(c -> c.getCommitsCount365Days() > 0).collect(Collectors.toList());
        commits365Days.sort((a, b) -> b.getCommitsCount365Days() - a.getCommitsCount365Days());
        addContributorsPanel(report, peopleCommits365Days, c -> c.getCommitsCount365Days(), true, null, "Contributor");
        addContributorsPanel(report, botCommits365Days, c -> c.getCommitsCount365Days(), true, null, "Bot");
        renderPeopleDependencies(analysis.getPeopleDependencies365Days(), analysis.getPeopleFileDependencies365Days(), 365, c -> c.getCommitsCount365Days(), commits365Days);
    }

    private void renderPeopleDependencies(List<ComponentDependency> peopleDependencies,
                                          List<ComponentDependency> peopleFileDependencies,
                                          int daysAgo,
                                          ContributionCounter contributionCounter,
                                          List<Contributor> contributors) {
        dependenciesRenderer.renderPeopleDependencies(peopleDependencies, peopleFileDependencies, daysAgo, contributionCounter, contributors);
    }

    private void addZoomableCircleLinks(RichTextReport report) {
        report.startDiv("margin-top: 10px");
        report.addHtmlContent("Zoomable circles (number of contributors per file): ");
        report.addNewTabLink("30 days", "visuals/zoomable_circles_contributors_30_main.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("90 days", "visuals/zoomable_circles_contributors_90_main.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("6 months", "visuals/zoomable_circles_contributors_180_main.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("past year", "visuals/zoomable_circles_contributors_365_main.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("all time", "visuals/zoomable_circles_contributors_main.html");
        report.addContentInDiv("Files with only one contributor are shown as grey.", "color: grey; font-size: 80%");
        report.endDiv();
    }

    public void addContributorsPanel(RichTextReport report, List<Contributor> contributors
            , ContributionCounter contributionCounter, boolean showPerExtension, PerExtensionCounter perExtensionCounter, String type) {
        int count = contributors.size();
        if (count == 0) {
            return;
        }
        report.addLineBreak();
        int total[] = {0};
        contributors.forEach(contributor -> total[0] += contributionCounter.count(contributor));
        if (total[0] > 0) {
            report.addParagraph("<b>" + FormattingUtils.formatCount(count) + "</b> " + (count == 1 ? type.toLowerCase() : type.toLowerCase() + "s") + " (" + "<b>" + FormattingUtils.formatCount(total[0]) + "</b> " + (count == 1 ? "commit" : "commits") + "):");
            addContributorsDistributionBar(report, contributors, contributionCounter, type, total);
            report.addLineBreak();
            report.addLineBreak();
        }
        report.startScrollingDiv();
        report.startTable();
        if (showPerExtension && perExtensionCounter != null) {
            report.addTableHeader("#", type + "<br>", "First<br>Commit", "Latest<br>Commit", "Commits<br>Count", "File Updates<br>(per extension)");
        } else {
            report.addTableHeader("#", type + "<br>", "First<br>Commit", "Latest<br>Commit", "Commits<br>Count");
        }
        addContributorsTableRows(report, contributors, contributionCounter, showPerExtension, perExtensionCounter, total);

        report.endTable();
        report.endDiv();
    }

    private void addContributorsDistributionBar(RichTextReport report, List<Contributor> contributors, ContributionCounter contributionCounter, String type, int[] total) {
        StringBuilder map = new StringBuilder("");
        Palette palette = Palette.getDefaultPalette();
        int index[] = {0};
        int cumulative[] = {0};
        contributors.forEach(contributor -> {
            int contributorCommitsCount = contributionCounter.count(contributor);
            cumulative[0] += contributorCommitsCount;
            int w = (int) Math.round(600 * (double) contributorCommitsCount / total[0]);
            int x = 620 - w;
            index[0]++;
            String cumulativeText = "";
            if (index[0] > 1) {
                cumulativeText = "\n\ntop " + index[0]
                        + type.toLowerCase() + "s together ("
                        + FormattingUtils.getFormattedPercentage(100.0 * index[0] / contributors.size())
                        + "% of " + type.toLowerCase() + "s) = "
                        + FormattingUtils.getFormattedPercentage(100.0 * cumulative[0] / total[0])
                        + "% of all commits";
            }
            map.append("<div style='background-color: " + palette.nextColor() + "; display: inline-block; height: 20px; width: " + w + "px' title='" + contributor.getEmail() + "\n" + contributorCommitsCount + " commits (" + (Math.round(100.0 * contributorCommitsCount / total[0])) + "%)" + cumulativeText + "'>&nbsp;</div>");
        });
        report.addHtmlContent(map.toString());
    }

    private void addContributorsTableRows(RichTextReport report, List<Contributor> contributors, ContributionCounter contributionCounter, boolean showPerExtension, PerExtensionCounter perExtensionCounter, int[] total) {
        int index[] = {0};
        contributors.forEach(contributor -> {
            index[0]++;
            String style = "";
            if (contributor.getCommitsCount90Days() == 0) {
                style = "color: lightgrey";
            } else if (contributor.getCommitsCount30Days() == 0) {
                style = "color: grey";
            }
            report.startTableRow(style);
            report.addTableCell(index[0] + ".");
            if (StringUtils.isNotBlank(contributor.getEmail()) && StringUtils.isNotBlank(contributor.getUserName())) {
                report.addTableCell(contributor.getUserName() + " <div style='color: grey; font-size: 80%; margin-bottom: 6px;'>&lt;" + contributor.getEmail() + "&gt;</div>");
            } else {
                report.addTableCell((contributor.getUserName() + contributor.getEmail()).trim());
            }

            report.addTableCell(contributor.getFirstCommitDate());
            report.addTableCell(contributor.getLatestCommitDate());
            int contributorCommitsCount = contributionCounter.count(contributor);
            String formattedCount = FormattingUtils.formatCount(contributorCommitsCount);
            String formattedPercentage = FormattingUtils.getFormattedPercentage(100.0 * contributorCommitsCount / total[0]);
            report.addTableCell(formattedCount + " (" + formattedPercentage + "%)");

            if (showPerExtension && perExtensionCounter != null) {
                String perExtension = emailStatsMap.get(contributor.getEmail()).stream()
                        .filter(e -> perExtensionCounter.count(e.getRight()) > 0)
                        .sorted((a, b) -> perExtensionCounter.count(b.getRight()) - perExtensionCounter.count(a.getRight()))
                        .limit(5)
                        .map(stats -> stats.getLeft() + " (" + perExtensionCounter.count(stats.getRight()) + ")")
                        .collect(Collectors.joining(", "));

                report.addTableCell(perExtension + "");
            }

            report.endTableRow();
        });
    }

    static interface PerExtensionCounter {
        int count(ContributorPerExtensionStats perExtensionStats);
    }
}
