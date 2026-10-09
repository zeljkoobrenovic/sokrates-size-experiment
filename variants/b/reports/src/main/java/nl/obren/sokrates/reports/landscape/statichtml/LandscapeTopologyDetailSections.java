/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.renderingutils.VisualizationItem;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.landscape.ContributorConnectionUtils;
import nl.obren.sokrates.reports.landscape.utils.LandscapeVisualsGenerator;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorConnections;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositories;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * The "show more" detail sections of the topology tab: active contributors history, most connected people,
 * people with most repositories, top connections, repositories with most people, repository dependencies via people.
 */
class LandscapeTopologyDetailSections {
    private final File folder;
    private final File reportsFolder;
    private final LandscapeReportContributorsTab.Type type;
    private final TeamsConfig teamsConfig;
    private RichTextReport landscapeReport;

    LandscapeTopologyDetailSections(RichTextReport landscapeReport, File folder, File reportsFolder, LandscapeReportContributorsTab.Type type, TeamsConfig teamsConfig) {
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

    void addDataSection(String type, double value, int daysAgo, List<Double> history, String info) {
        if (StringUtils.isNotBlank(info)) {
            landscapeReport.addParagraph(type + ": <b>" + ((int) Math.round(value)) + "</b>");
            landscapeReport.addParagraph("<span style='color: #a2a2a2; font-size: 90%;'>" + info + "</span>", "margin-top: -12px;");
        } else {
            landscapeReport.addParagraph(type + ": <b>" + ((int) Math.round(value)) + "</b>");
        }

        if (daysAgo == 30 && history.size() > 0) {
            landscapeReport.startTable("border: none");
            landscapeReport.startTableRow("font-size: 70%;");
            double max = history.stream().max(Double::compare).get();
            history.forEach(historicalValue -> {
                landscapeReport.startTableCell("text-align: center; vertical-align: bottom;border: none");
                landscapeReport.addContentInDiv((int) Math.round(historicalValue) + "", "width: 20px;border: none");
                landscapeReport.addContentInDiv("", "width: 20px; background-color: skyblue; border: none; height:"
                        + (int) (1 + Math.round(40.0 * historicalValue / max)) + "px;");
                landscapeReport.endTableCell();
            });
            landscapeReport.endTableRow();
            landscapeReport.startTableRow("font-size: 70%;");
            landscapeReport.addTableCell("<b>now</b>", "border: none");
            landscapeReport.addTableCell("1m<br>ago", "text-align: center; border: none");
            for (int i = 0; i < history.size() - 2; i++) {
                landscapeReport.addTableCell((i + 2) + "m<br>ago", "text-align: center; border: none");
            }
            landscapeReport.endTableRow();
            landscapeReport.endTable();
            landscapeReport.addLineBreak();
            landscapeReport.addLineBreak();
        }
    }

    void addRepositoryDependenciesViaPeople(List<ComponentDependency> repositoryDependenciesViaPeople) {
        landscapeReport.startShowMoreBlock("show repository dependencies via people...<br>");
        landscapeReport.startTable();
        int maxListSize = Math.min(100, repositoryDependenciesViaPeople.size());
        if (maxListSize < repositoryDependenciesViaPeople.size()) {
            landscapeReport.addLineBreak();
            landscapeReport.addParagraph("Showing top " + maxListSize + " items (out of " + repositoryDependenciesViaPeople.size() + ").");
        } else {
            landscapeReport.addParagraph("Showing all " + maxListSize + (maxListSize == 1 ? " item" : " items") + ".");
        }
        repositoryDependenciesViaPeople.subList(0, maxListSize).forEach(dependency -> {
            landscapeReport.startTableRow();
            landscapeReport.addTableCell(dependency.getFromComponent());
            landscapeReport.addTableCell(dependency.getToComponent());
            landscapeReport.addTableCell(dependency.getCount() + (dependency.getCount() == 1 ? " person" : " people"));
            landscapeReport.endTableRow();
        });
        landscapeReport.endTable();
        landscapeReport.endShowMoreBlock();
    }

    void addRepositoryContributors(List<ContributorRepositories> contributors, int daysAgo) {
        Map<String, Pair<String, Integer>> map = new HashMap<>();
        final List<String> list = new ArrayList<>();

        countRepositoryContributors(contributors, daysAgo, map, list);

        Collections.sort(list, (a, b) -> map.get(b).getRight() - map.get(a).getRight());

        List<String> displayList = list;
        if (list.size() > 100) {
            displayList = list.subList(0, 100);
        }

        landscapeReport.startShowMoreBlock("show repositories with most " + (isContributorReport() ? "people" : "teams") + "...<br>");
        StringBuilder builder = new StringBuilder();
        builder.append("Contributor\t# people\n");
        list.forEach(repository -> builder.append(map.get(repository).getLeft()).append("\t")
                .append(map.get(repository).getRight()).append("\n"));
        String prefix = "repository_with_most_" + (isContributorReport() ? "people" : "teams") + "_" + daysAgo + "_days";
        String fileName = prefix + ".txt";
        saveData(folder, fileName, builder.toString());

        if (displayList.size() < list.size()) {
            landscapeReport.addLineBreak();
            landscapeReport.addParagraph("Showing top 100 items (out of " + list.size() + ").");
        }
        landscapeReport.addLineBreak();
        landscapeReport.addNewTabLink("bubble chart", "visuals/bubble_chart_" + prefix + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("tree map", "visuals/tree_map_" + prefix + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("data", "data/" + fileName);
        landscapeReport.addHtmlContent("</p>");
        List<VisualizationItem> visualizationItems = new ArrayList<>();
        list.forEach(repository -> visualizationItems.add(new VisualizationItem(repository, map.get(repository).getRight())));
        exportVisuals(prefix, visualizationItems);
        addRepositoryContributorsTable(displayList, map);
        landscapeReport.endShowMoreBlock();
    }

    private void countRepositoryContributors(List<ContributorRepositories> contributors, int daysAgo, Map<String, Pair<String, Integer>> map, List<String> list) {
        contributors.forEach(contributorRepositories -> {
            contributorRepositories.getRepositories().stream().filter(repository -> DateUtils.isAnyDateCommittedBetween(repository.getCommitDates(), 0, daysAgo)).forEach(repository -> {
                String key = repository.getRepositoryAnalysisResults().getAnalysisResults().getMetadata().getName();
                if (map.containsKey(key)) {
                    Integer currentValue = map.get(key).getRight();
                    map.put(key, Pair.of(key, currentValue + 1));
                } else {
                    Pair<String, Integer> pair = Pair.of(key, 1);
                    map.put(key, pair);
                    list.add(key);
                }
            });
        });
    }

    private void addRepositoryContributorsTable(List<String> displayList, Map<String, Pair<String, Integer>> map) {
        landscapeReport.startTable();
        displayList.forEach(repository -> {
            landscapeReport.startTableRow();
            landscapeReport.addTableCell(map.get(repository).getLeft());
            Integer count = map.get(repository).getRight();
            landscapeReport.addTableCell(count + (count == 1 ? " person" : " people"));
            landscapeReport.endTableRow();
        });
        landscapeReport.endTable();
    }

    void addTopConnectionsSection(List<ComponentDependency> peopleDependencies, int daysAgo, List<ContributorRepositories> contributors) {
        landscapeReport.startShowMoreBlock("show top connections...<br>");
        landscapeReport.startTable();
        List<ComponentDependency> displayListConnections = peopleDependencies.subList(0, Math.min(100, peopleDependencies.size()));
        if (displayListConnections.size() < peopleDependencies.size()) {
            landscapeReport.addLineBreak();
            landscapeReport.addParagraph("Showing top " + displayListConnections.size() + " items (out of " + peopleDependencies.size() + ").");
        } else {
            landscapeReport.addParagraph("Showing all " + displayListConnections.size() + (displayListConnections.size() == 1 ? " item" : " items") + ".");
        }
        int index[] = {0};
        displayListConnections.forEach(dependency -> {
            index[0] += 1;
            landscapeReport.startTableRow();
            String from = dependency.getFromComponent();
            String to = dependency.getToComponent();
            int dependencyCount = dependency.getCount();
            landscapeReport.addTableCell(index[0] + ".");
            int repositoryCount1 = ContributorConnectionUtils.getRepositoryCount(contributors, from, 0, daysAgo);
            int repositoryCount2 = ContributorConnectionUtils.getRepositoryCount(contributors, to, 0, daysAgo);
            double perc1 = 0;
            double perc2 = 0;
            if (repositoryCount1 > 0) {
                perc1 = 100.0 * dependencyCount / repositoryCount1;
            }
            if (repositoryCount2 > 0) {
                perc2 = 100.0 * dependencyCount / repositoryCount2;
            }
            landscapeReport.addTableCell(from + "<br><span style='color: grey'>" + repositoryCount1 + " repositories (" + FormattingUtils.getFormattedPercentage(perc1) + "%)</span>", "");
            landscapeReport.addTableCell(to + "<br><span style='color: grey'>" + repositoryCount2 + " repositories (" + FormattingUtils.getFormattedPercentage(perc2) + "%)</span>", "");
            landscapeReport.addTableCell(dependencyCount + " shared repositories", "");
            landscapeReport.endTableRow();
        });
        landscapeReport.endTable();
        landscapeReport.endShowMoreBlock();
    }

    void addMostConnectedPeopleSection(List<ContributorConnections> contributorConnections, int daysAgo) {
        landscapeReport.startShowMoreBlock("show most connected people...<br>");
        StringBuilder builder = new StringBuilder();
        builder.append("Contributor\t# repositories\t# connections\n");
        contributorConnections.forEach(c -> builder.append(c.getEmail()).append("\t")
                .append(c.getRepositoriesCount()).append("\t")
                .append(c.getConnectionsCount()).append("\n"));
        String prefix = "most_connected_" + (isContributorReport() ? "people" : "teams") + "_" + daysAgo + "_days";
        String fileName = prefix + ".txt";

        saveData(folder, fileName, builder.toString());

        List<ContributorConnections> displayListPeople = contributorConnections.subList(0, Math.min(100, contributorConnections.size()));
        if (displayListPeople.size() < contributorConnections.size()) {
            landscapeReport.addLineBreak();
            landscapeReport.addParagraph("Showing top " + displayListPeople.size() + " items (out of " + contributorConnections.size() + "). ");
        } else {
            landscapeReport.addParagraph("Showing all " + displayListPeople.size() + (displayListPeople.size() == 1 ? " item" : " items") + ". ");
        }
        landscapeReport.addLineBreak();
        landscapeReport.addNewTabLink("bubble chart", "visuals/bubble_chart_" + prefix + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("tree map", "visuals/tree_map_" + prefix + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("data", "data/" + fileName);
        landscapeReport.addHtmlContent("</p>");
        List<VisualizationItem> visualizationItems = new ArrayList<>();
        contributorConnections.forEach(c -> visualizationItems.add(new VisualizationItem(c.getEmail(), c.getConnectionsCount())));
        exportVisuals(prefix, visualizationItems);
        landscapeReport.addHtmlContent("</p>");
        int index[] = {0};
        landscapeReport.startTable();
        displayListPeople.forEach(name -> {
            index[0] += 1;
            landscapeReport.startTableRow();
            landscapeReport.addTableCell(index[0] + ".", "");
            landscapeReport.addTableCell(name.getEmail(), "");
            landscapeReport.addTableCell(name.getRepositoriesCount() + "&nbsp;repositories");
            landscapeReport.addTableCell(name.getConnectionsCount() + " connections", "");
            landscapeReport.endTableRow();
        });
        landscapeReport.endTable();
        landscapeReport.endShowMoreBlock();

    }

    void addMostRepositoriesPeopleSection(List<ContributorConnections> contributorConnections, int daysAgo) {
        landscapeReport.startShowMoreBlock("show people with most repositories...<br>");
        List<ContributorConnections> sorted = new ArrayList<>(contributorConnections);
        sorted.sort((a, b) -> b.getRepositoriesCount() - a.getRepositoriesCount());
        List<ContributorConnections> displayListPeople = sorted.subList(0, Math.min(100, sorted.size()));
        if (displayListPeople.size() < contributorConnections.size()) {
            landscapeReport.addLineBreak();
            landscapeReport.addParagraph("Showing top " + displayListPeople.size() + " items (out of " + contributorConnections.size() + "). ");
        } else {
            landscapeReport.addParagraph("Showing all " + displayListPeople.size() + (displayListPeople.size() == 1 ? " item" : " items") + ". ");
        }
        String prefix = "most_repositories_" + (isContributorReport() ? "people" : "teams") + "_" + daysAgo + "_days";
        StringBuilder builder = new StringBuilder();
        builder.append("Contributor\t# repositories\t# connections\n");
        contributorConnections.forEach(c -> builder.append(c.getEmail()).append("\t")
                .append(c.getRepositoriesCount()).append("\t")
                .append(c.getConnectionsCount()).append("\n"));
        String fileName = prefix + ".txt";
        saveData(folder, fileName, builder.toString());

        landscapeReport.addLineBreak();
        landscapeReport.addNewTabLink("bubble chart", "visuals/bubble_chart_" + prefix + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("tree map", "visuals/tree_map_" + prefix + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("data", "data/" + fileName);
        landscapeReport.addHtmlContent("</p>");
        List<VisualizationItem> visualizationItems = new ArrayList<>();
        contributorConnections.forEach(c -> visualizationItems.add(new VisualizationItem(c.getEmail(), c.getRepositoriesCount())));
        int index[] = {0};
        landscapeReport.startTable();
        displayListPeople.forEach(name -> {
            index[0] += 1;
            landscapeReport.startTableRow();
            landscapeReport.addTableCell(index[0] + ".", "");
            landscapeReport.addTableCell(name.getEmail(), "");
            landscapeReport.addTableCell(name.getRepositoriesCount() + "&nbsp;repositories");
            landscapeReport.addTableCell(name.getConnectionsCount() + " connections", "");
            landscapeReport.endTableRow();
        });

        exportVisuals(prefix, visualizationItems);
        landscapeReport.endTable();
        landscapeReport.endShowMoreBlock();
    }

    private void exportVisuals(String prefix, List<VisualizationItem> visualizationItems) {
        try {
            new LandscapeVisualsGenerator(reportsFolder, teamsConfig).exportVisuals(prefix, visualizationItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void saveData(File landscapeFolder, String fileName, String content) {
        File reportsFolder = Paths.get(landscapeFolder.getParent(), "").toFile();
        File folder = Paths.get(reportsFolder.getPath(), "_sokrates_landscape/data").toFile();
        folder.mkdirs();

        try {
            File file = new File(folder, fileName);
            FileUtils.writeStringToFile(file, content, UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
