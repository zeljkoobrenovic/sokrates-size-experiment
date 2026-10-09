/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.utils.*;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.*;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorConnections;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositories;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.*;

public class LandscapeReportPeopleTopologyTab {

    public static final String REMAINDER = "Undefined Team";

    private static final Log LOG = LogFactory.getLog(LandscapeReportPeopleTopologyTab.class);
    public static final String PEOPLE_COLOR = "#ADD8E6";
    private final List<ContributorRepositories> contributors;
    private LandscapeAnalysisResults landscapeAnalysisResults;
    private File folder;
    private File reportsFolder;
    private Map<String, List<String>> contributorsPerWeekMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerWeekMap = new HashMap<>();
    private Map<String, List<String>> contributorsPerDayMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerDayMap = new HashMap<>();
    private Map<String, List<String>> contributorsPerMonthMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerMonthMap = new HashMap<>();
    private Map<String, List<String>> contributorsPerYearMap = new HashMap<>();
    private Map<String, List<String>> rookiesPerYearMap = new HashMap<>();
    private RichTextReport landscapeReport;
    private final LandscapeReportContributorsTab.Type type;
    private final TeamsConfig teamsConfig;
    private final LandscapeTopologyGraphs graphs;
    private final LandscapeTopologyDetailSections sections;

    public LandscapeReportPeopleTopologyTab(LandscapeAnalysisResults landscapeAnalysisResults, List<ContributorRepositories> contributors, RichTextReport landscapeReport, File folder, File reportsFolder, LandscapeReportContributorsTab.Type type, TeamsConfig teamsConfig) {
        this.contributors = contributors;
        this.folder = folder;
        this.reportsFolder = reportsFolder;
        this.landscapeReport = landscapeReport;
        this.type = type;
        this.teamsConfig = teamsConfig;

        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.graphs = new LandscapeTopologyGraphs(landscapeAnalysisResults, landscapeReport, folder, reportsFolder, type, teamsConfig);
        this.sections = new LandscapeTopologyDetailSections(landscapeReport, folder, reportsFolder, type, teamsConfig);

        populateTimeSlotMaps();
    }

    void render30DaysTopology() {
        landscapeReport.startSubSection(StringUtils.capitalize(type.singular()) + " Topology (past 30 days)", "");

        boolean recentlyActive = landscapeAnalysisResults.getRecentContributorsCount(contributors) > 0;
        String prefix = isContributorReport() ? "people" : "teams";
        if (recentlyActive) {
            landscapeReport.addParagraph("The diagram shows contributor collaborations defined as working on " +
                    "the same repositories in the past 30 days. The lines display the number of shared repositories " +
                    "between two contributors.\n", "color: grey");
            landscapeReport.addNewTabLink("<div style='font-weight: bold; font-size: 110%; margin-bottom: 8px;'>2D force graph (including repositories)&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON + "</div>", "visuals/" + prefix + "_dependencies_including_repositories_30_2_force_2d.html");
            landscapeReport.addNewTabLink("<div style='font-weight: bold; font-size: 110%; margin-bottom: 8px;'>3D force graph (including repositories)&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON + "</div>", "visuals/" + prefix + "_dependencies_including_repositories_30_2_force_3d.html");
            landscapeReport.startDiv("font-size: 90%");
            landscapeReport.addHtmlContent("direct links: ");
            landscapeReport.addNewTabLink("graphviz", "visuals/" + prefix + "_dependencies_30_1.svg");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("graphviz (including contributors)", "visuals/" + prefix + "_dependencies_including_repositories_30_2.svg");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("2D force graph", "visuals/" + prefix + "_dependencies_30_1_force_2d.html");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("3D force graph", "visuals/" + prefix + "_dependencies_30_1_force_3d.html");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("data", "data/repository_shared_repositories_30_days.txt");
            landscapeReport.addHtmlContent("&nbsp;&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON);
            landscapeReport.endDiv();
            landscapeReport.addLineBreak();
            landscapeReport.addHtmlContent("<iframe src=\"visuals/" + prefix + "_dependencies_30_1.svg\" " +
                    "style=\"border: 1px solid lightgrey; width: 100%; height: 600px\"></iframe>");
        } else {
            landscapeReport.addParagraph("No commits in past 30 days.", "color: grey");
        }
        landscapeReport.endSection();
    }

    void renderDetails() {
        if (!isContributorReport()) {
            landscapeReport.startDiv("display: none");
        }
        landscapeReport.startSubSection(StringUtils.capitalize(type.singular()) + " Dependencies Details", "");

        List<ComponentDependency> peopleDependencies30Days = landscapeAnalysisResults.getPeopleDependencies30Days();
        List<ComponentDependency> peopleRepositoryDependencies30Days = landscapeAnalysisResults.getPeopleRepositoryDependencies30Days();
        List<ContributorConnections> connectionsViaRepositories30Days = landscapeAnalysisResults.getConnectionsViaRepositories30Days();
        this.renderPeopleDependencies(peopleDependencies30Days, peopleRepositoryDependencies30Days, connectionsViaRepositories30Days, 30);

        List<ComponentDependency> peopleDependencies90Days = landscapeAnalysisResults.getPeopleDependencies90Days();
        List<ContributorConnections> connectionsViaRepositories90Days = landscapeAnalysisResults.getConnectionsViaRepositories90Days();
        this.renderPeopleDependencies(peopleDependencies90Days, null, connectionsViaRepositories90Days, 90);

        List<ComponentDependency> peopleDependencies180Days = landscapeAnalysisResults.getPeopleDependencies180Days();
        List<ContributorConnections> connectionsViaRepositories180Days = landscapeAnalysisResults.getConnectionsViaRepositories180Days();
        this.renderPeopleDependencies(peopleDependencies180Days, null, connectionsViaRepositories180Days, 180);

        landscapeReport.endSection();
        if (!isContributorReport()) {
            landscapeReport.endDiv();
        }
    }

    void renderRepoAndKnowlegeTopologies() {
        boolean recentlyActive = landscapeAnalysisResults.getRecentContributorsCount(contributors) > 0;
        String prefix = isContributorReport() ? "people" : "teams";
        landscapeReport.startSubSection("Repository Topology (past 30 days)", "");
        if (recentlyActive) {
            landscapeReport.addParagraph("The diagram shows repository dependencies defined as having the same " +
                    "contributors working on the same repositories in the past 30 days. " +
                    "The lines between repositories display the number of contributors working on both repositories.", "color: grey");
            landscapeReport.addNewTabLink("graphviz", "visuals/repository_dependencies_30_3.svg");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("graphviz (including contributors)", "visuals/" + prefix + "_dependencies_including_repositories_30_2.svg");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("2D force graph", "visuals/repository_dependencies_30_3_force_2d.html");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("2D force graph (including contributors)", "visuals/" + prefix + "_dependencies_including_repositories_30_2_force_2d.html");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("3D force graph", "visuals/repository_dependencies_30_3_force_3d.html");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("3D force graph (including contributors)", "visuals/" + prefix + "_dependencies_including_repositories_30_2_force_3d.html");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("data", "data/repository_shared_repositories_30_days.txt");
            landscapeReport.addHtmlContent("&nbsp;&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON);
            landscapeReport.addLineBreak();
            landscapeReport.addLineBreak();
            landscapeReport.addHtmlContent("<iframe src=\"visuals/repository_dependencies_30_3.svg\" " +
                    "style=\"border: 1px solid lightgrey; width: 100%; height: 600px\"></iframe>");
        } else {
            landscapeReport.addParagraph("No commits in past 30 days.", "color: grey");
        }
        landscapeReport.endSection();
        landscapeReport.startSubSection("Knowledge Topology (past 30 days)", "");
        if (recentlyActive) {
            landscapeReport.addParagraph("The diagram shows dependencies between programming languages (file extensions) defined as having the same contributors committing to files with these extensions in the past 30 days. " +
                    "The lines between repositories display the number of contributors committing to files with both extensions in ht past 30 days.", "color: grey");
            landscapeReport.addNewTabLink("graphviz", "visuals/extension_dependencies_30d.svg");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("2D force graph", "visuals/extension_dependencies_30d_force_2d.html");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("3D force graph", "visuals/extension_dependencies_30d_force_3d.html");
            landscapeReport.addHtmlContent("&nbsp;&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON);
            landscapeReport.addLineBreak();
            landscapeReport.addLineBreak();
            landscapeReport.addHtmlContent("<iframe src=\"visuals/extension_dependencies_30d.svg\" " +
                    "style=\"border: 1px solid lightgrey; width: 100%; height: 600px\"></iframe>");
        } else {
            landscapeReport.addParagraph("No commits in past 30 days.", "color: grey");
        }
        landscapeReport.endSection();
        landscapeReport.addLineBreak();
    }

    void addPeopleInfoBlock() {
        int recentContributorsCount = landscapeAnalysisResults.getRecentContributorsCount(contributors);

        addPeopleInfoBlock(FormattingUtils.getSmallTextForNumber(recentContributorsCount), recentContributorsCount == 1 ? type.singular() : type.plural(), "30 days", "");
    }

    private void addPeopleInfoBlock(String mainValue, String subtitle, String description, String tooltip) {
        addPeopleInfoBlockWithColor(mainValue, subtitle, description, tooltip, PEOPLE_COLOR);
    }

    private void addPeopleInfoBlockWithColor(String mainValue, String subtitle, String description, String tooltip, String color) {
        if (StringUtils.isNotBlank(description)) {
            subtitle += "<br/><span style='color: #707070; font-size: 80%'>" + description + "</span>";
        }
        addInfoBlockWithColor(mainValue, subtitle, color, tooltip, isContributorReport() ? "contributors" : "teams");
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

    private void populateTimeSlotMaps() {
        contributors.forEach(contributorRepositories -> {
            List<String> commitDates = contributorRepositories.getContributor().getCommitDates();
            commitDates.forEach(day -> {
                String week = DateUtils.getWeekMonday(day);
                String month = DateUtils.getMonth(day);
                String year = DateUtils.getYear(day);

                updateTimeSlotMap(contributorRepositories, contributorsPerDayMap, rookiesPerDayMap, day, day);
                updateTimeSlotMap(contributorRepositories, contributorsPerWeekMap, rookiesPerWeekMap, week, week);
                updateTimeSlotMap(contributorRepositories, contributorsPerMonthMap, rookiesPerMonthMap, month, month + "-01");
                updateTimeSlotMap(contributorRepositories, contributorsPerYearMap, rookiesPerYearMap, year, year + "-01-01");
            });
        });

    }

    private void updateTimeSlotMap(ContributorRepositories contributorRepositories,
                                   Map<String, List<String>> map, Map<String, List<String>> rookiesMap, String key, String rookieDate) {
        boolean rookie = contributorRepositories.getContributor().isRookieAtDate(rookieDate);

        String email = contributorRepositories.getContributor().getEmail();
        if (map.containsKey(key)) {
            if (!map.get(key).contains(email)) {
                map.get(key).add(email);
            }
        } else {
            map.put(key, new ArrayList<>(Arrays.asList(email)));
        }
        if (rookie) {
            if (rookiesMap.containsKey(key)) {
                if (!rookiesMap.get(key).contains(email)) {
                    rookiesMap.get(key).add(email);
                }
            } else {
                rookiesMap.put(key, new ArrayList<>(Arrays.asList(email)));
            }
        }
    }

    private void renderPeopleDependencies(List<ComponentDependency> peopleDependencies,
                                          List<ComponentDependency> peopleRepositoryDependencies,
                                          List<ContributorConnections> contributorConnections,
                                          int daysAgo) {
        List<ComponentDependency> repositoryDependenciesViaPeople = ContributorConnectionUtils.getRepositoryDependenciesViaPeople(contributors, 0, daysAgo);

        landscapeReport.addLevel2Header("Contributor Dependencies (past " + daysAgo + " days)", "margin-top: 40px");
        List<Double> activeContributors30DaysHistory = landscapeAnalysisResults.getActiveContributors30DaysHistory();
        if (activeContributors30DaysHistory.size() > 0 && daysAgo == 30) {
            landscapeReport.addLineBreak();
            landscapeReport.addLineBreak();
            sections.addDataSection("Active Contributors", activeContributors30DaysHistory.get(0), daysAgo, activeContributors30DaysHistory,
                    "An active contributor is anyone who has committed code changes in past " + daysAgo + " days.");
        }

        landscapeReport.startTable();
        landscapeReport.startTableRow();
        landscapeReport.startTableCell("border: none; vertical-align: top");
        landscapeReport.addHtmlContent(DEPENDENCIES_ICON);
        landscapeReport.endTableCell();
        landscapeReport.startTableCell("border: none");
        graphs.addPeopleGraph(peopleDependencies, daysAgo, "", "");
        if (peopleRepositoryDependencies != null) {
            graphs.addPeopleGraph(peopleRepositoryDependencies, daysAgo, "including_repositories_", " (including repositories)");
        }
        graphs.addRepositoriesGraph(daysAgo, repositoryDependenciesViaPeople);
        landscapeReport.endTableCell();
        landscapeReport.endTableRow();
        landscapeReport.endTable();

        landscapeReport.startDiv("margin-left: 124px; padding-left: 4px; border-left: 1px dashed lightgrey; margin-top: 10px;");

        peopleDependencies.sort((a, b) -> b.getCount() - a.getCount());

        sections.addMostConnectedPeopleSection(contributorConnections, daysAgo);
        sections.addMostRepositoriesPeopleSection(contributorConnections, daysAgo);
        sections.addTopConnectionsSection(peopleDependencies, daysAgo, contributors);
        sections.addRepositoryContributors(contributors, daysAgo);
        sections.addRepositoryDependenciesViaPeople(repositoryDependenciesViaPeople);

        landscapeReport.endDiv();
    }

    private boolean isContributorReport() {
        return type == LandscapeReportContributorsTab.Type.CONTRIBUTORS;
    }

    public void setLandscapeReport(RichTextReport report) {
        this.landscapeReport = report;
        graphs.setLandscapeReport(report);
        sections.setLandscapeReport(report);
    }
}
