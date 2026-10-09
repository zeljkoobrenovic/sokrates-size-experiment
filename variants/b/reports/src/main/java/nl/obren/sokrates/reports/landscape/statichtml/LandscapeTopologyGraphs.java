/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.RegexUtils;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.utils.Force3DGraphExporter;
import nl.obren.sokrates.reports.utils.GraphvizDependencyRenderer;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.landscape.TeamConfig;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.OPEN_IN_NEW_TAB_SVG_ICON;
import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportPeopleTopologyTab.REMAINDER;

/**
 * The people/team and repository dependency graphs of the topology tab (graphviz figures, force graph exports,
 * download links) and the team aggregation of people dependencies.
 */
class LandscapeTopologyGraphs {
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final File folder;
    private final File reportsFolder;
    private final LandscapeReportContributorsTab.Type type;
    private final TeamsConfig teamsConfig;
    private RichTextReport landscapeReport;
    private int dependencyVisualCounter = 1;

    LandscapeTopologyGraphs(LandscapeAnalysisResults landscapeAnalysisResults, RichTextReport landscapeReport, File folder, File reportsFolder, LandscapeReportContributorsTab.Type type, TeamsConfig teamsConfig) {
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.landscapeReport = landscapeReport;
        this.folder = folder;
        this.reportsFolder = reportsFolder;
        this.type = type;
        this.teamsConfig = teamsConfig;
    }

    void setLandscapeReport(RichTextReport report) {
        this.landscapeReport = report;
    }

    private boolean isContributorReport() {
        return type == LandscapeReportContributorsTab.Type.CONTRIBUTORS;
    }

    private String getTeamOf(String email) {
        if (email.startsWith("[")) return email;

        for (TeamConfig team : teamsConfig.getTeams()) {
            if (RegexUtils.matchesAnyPattern(email, team.getEmailPatterns())) {
                return team.getName();
            }
        }

        return REMAINDER;
    }

    private List<ComponentDependency> getTeamDependencies(List<ComponentDependency> peopleDependencies) {
        List<ComponentDependency> teamDependencies = new ArrayList<>();
        Map<String, ComponentDependency> map = new HashMap<>();

        for (ComponentDependency dependency : peopleDependencies) {
            String team1 = getTeamOf(dependency.getFromComponent());
            String team2 = getTeamOf(dependency.getToComponent());

            if (!team1.equals(team2)) {
                String key1 = team1 + "::" + team2;
                String key2 = team2 + "::" + team1;

                if (map.containsKey(key1)) {
                    if (dependency.getFromComponent().startsWith("[") || dependency.getToComponent().startsWith("[")) {
                        map.get(key1).increment(1);
                    }
                } else if (map.containsKey(key2)) {
                    if (dependency.getFromComponent().startsWith("[") || dependency.getToComponent().startsWith("[")) {
                        map.get(key2).increment(1);
                    }
                } else {
                    ComponentDependency teamDependency = new ComponentDependency(team1, team2);
                    teamDependency.setCount(1);
                    map.put(key1, teamDependency);
                    teamDependencies.add(teamDependency);
                }
            }
        }

        return teamDependencies;
    }

    void addRepositoriesGraph(int daysAgo, List<ComponentDependency> repositoryDependenciesViaPeople) {
        landscapeReport.startShowMoreBlock("show repository dependencies graph...<br>");
        StringBuilder builder = new StringBuilder();
        builder.append("Repository 1\tRepository 2\t# people\n");
        repositoryDependenciesViaPeople.subList(0, Math.min(10000, repositoryDependenciesViaPeople.size())).forEach(d -> builder
                .append(d.getFromComponent()).append("\t")
                .append(d.getToComponent()).append("\t")
                .append(d.getCount()).append("\n"));
        String fileName = "repository_dependencies_via_" + (isContributorReport() ? "people" : "teams") + "_" + daysAgo + "_days.txt";
        LandscapeTopologyDetailSections.saveData(folder, fileName, builder.toString());

        landscapeReport.addNewTabLink("See data...", "data/" + fileName);

        List<String> repositoryNames = landscapeAnalysisResults.getRepositoryAnalysisResults().stream()
                .filter(p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days() > 0)
                .map(p -> "[" + p.getAnalysisResults().getMetadata().getName() + "]")
                .collect(Collectors.toList());

        String graphId = addDependencyGraphVisuals(repositoryDependenciesViaPeople, new ArrayList<>(repositoryNames), "repository_dependencies_" + daysAgo + "_", "TB");

        landscapeReport.endShowMoreBlock();
        landscapeReport.addNewTabLink(" - 2D force graph&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "visuals/" + graphId + "_force_2d.html");
        landscapeReport.addLineBreak();
        landscapeReport.addNewTabLink(" - 3D force graph&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "visuals/" + graphId + "_force_3d.html");
        landscapeReport.addLineBreak();
    }

    void addPeopleGraph(List<ComponentDependency> peopleDependencies, int daysAgo, String suffix, String extraLabel) {
        StringBuilder builder = new StringBuilder();
        builder.append("Contributor 1\tContributor 2\t# shared repositories\n");
        peopleDependencies.forEach(d -> builder
                .append(d.getFromComponent()).append("\t")
                .append(d.getToComponent()).append("\t")
                .append(d.getCount()).append("\n"));
        String fileName = "repository_shared_repositories_" + suffix + daysAgo + "_days.txt";
        LandscapeTopologyDetailSections.saveData(folder, fileName, builder.toString());

        landscapeReport.startShowMoreBlock("show contributor dependencies graph..." + extraLabel + "<br>");
        landscapeReport.startDiv("border-left: 6px solid lightgrey; padding-left: 4px; margin-left: 4px; overflow-x: auto");
        landscapeReport.addHtmlContent("&nbsp;&nbsp;&nbsp;");
        landscapeReport.addNewTabLink("See data...", "data/" + fileName);

        String orientation = suffix.length() > 0 ? "LR" : "TB";
        String graphId = addDependencyGraphVisuals(peopleDependencies, new ArrayList<>(),
                (isContributorReport() ? "people" : "teams") + "_dependencies_" + suffix + daysAgo + "_", orientation);
        landscapeReport.endDiv();
        landscapeReport.endShowMoreBlock();

        landscapeReport.addNewTabLink(" - 2D force graph" + extraLabel + "&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON,
                "visuals/" + graphId + "_force_2d.html");
        landscapeReport.addLineBreak();
        landscapeReport.addNewTabLink(" - 3D force graph" + extraLabel + "&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON,
                "visuals/" + graphId + "_force_3d.html");
        landscapeReport.addLineBreak();
        landscapeReport.addLineBreak();
    }

    private String addDependencyGraphVisuals(List<ComponentDependency> componentDependencies, List<String> componentNames,
                                             String prefix, String orientation) {
        if (type == LandscapeReportContributorsTab.Type.TEAMS) {
            componentDependencies = getTeamDependencies(componentDependencies);
        }
        GraphvizDependencyRenderer graphvizDependencyRenderer = new GraphvizDependencyRenderer();
        graphvizDependencyRenderer.setDefaultNodeFillColor("deepskyblue2");
        graphvizDependencyRenderer.setMaxNumberOfDependencies(100);
        graphvizDependencyRenderer.setType("graph");
        graphvizDependencyRenderer.setOrientation(orientation);
        graphvizDependencyRenderer.setArrow("--");

        if (100 < componentDependencies.size()) {
            landscapeReport.addLineBreak();
            landscapeReport.addParagraph("Showing top " + 100 + " items (out of " + componentDependencies.size() + ").");
        } else {
            landscapeReport.addParagraph("Showing all " + componentDependencies.size() + (componentDependencies.size() == 1 ? " item" : " items") + ".");
        }
        String graphvizContent = graphvizDependencyRenderer.getGraphvizContent(componentNames, componentDependencies);
        String graphId = prefix + dependencyVisualCounter++;
        landscapeReport.addGraphvizFigure(graphId, "", graphvizContent);
        landscapeReport.addLineBreak();
        landscapeReport.addLineBreak();

        addDownloadLinks(graphId);
        new Force3DGraphExporter().export2D3DForceGraph(componentDependencies, reportsFolder, graphId);

        return graphId;
    }

    private void addDownloadLinks(String graphId) {
        landscapeReport.startDiv("");
        landscapeReport.addHtmlContent("Download: ");
        landscapeReport.addNewTabLink("SVG", "visuals/" + graphId + ".svg");
        landscapeReport.addHtmlContent(" ");
        landscapeReport.addNewTabLink("DOT", "visuals/" + graphId + ".dot.txt");
        landscapeReport.addHtmlContent(" ");
        landscapeReport.addNewTabLink("(open online Graphviz editor)", "https://obren.io/tools/graphviz/");
        landscapeReport.endDiv();
    }
}
