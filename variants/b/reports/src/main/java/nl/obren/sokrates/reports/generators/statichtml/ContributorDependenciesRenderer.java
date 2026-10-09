/*
 * Copyright (c) 2020 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.common.renderingutils.VisualizationTemplate;
import nl.obren.sokrates.common.renderingutils.force3d.Force3DLink;
import nl.obren.sokrates.common.renderingutils.force3d.Force3DNode;
import nl.obren.sokrates.common.renderingutils.force3d.Force3DObject;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.utils.GraphvizDependencyRenderer;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.landscape.ContributionCounter;
import nl.obren.sokrates.sourcecode.landscape.ContributorConnection;
import nl.obren.sokrates.sourcecode.landscape.ContributorConnectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

class ContributorDependenciesRenderer {
    private int dependencyVisualCounter = 1;
    private File reportsFolder;
    private RichTextReport report;
    private Map<String, Contributor> emailContributorMap;

    ContributorDependenciesRenderer(RichTextReport report, File reportsFolder, Map<String, Contributor> emailContributorMap) {
        this.report = report;
        this.reportsFolder = reportsFolder;
        this.emailContributorMap = emailContributorMap;
    }

    void renderPeopleDependencies(List<ComponentDependency> peopleDependencies,
                                  List<ComponentDependency> peopleFileDependencies,
                                  int daysAgo,
                                  ContributionCounter contributionCounter,
                                  List<Contributor> contributors) {
        if (peopleDependencies.size() > 0) {
            report.addLevel2Header("Contributor Dependencies", "margin-top: 40px; margin-bottom: 0;");
            report.addParagraph("A contributor dependency is detected if two contributors have changed the same files in the past " + daysAgo + " days.",
                    "color: grey; font-size: 80%; margin-bottom: 12px;");
            addDependenciesViaSharedFiles(peopleDependencies, peopleFileDependencies, daysAgo);

            List<ContributorConnection> contributorConnections = ContributorConnectionUtils.getContributorConnections(peopleDependencies, contributors, contributionCounter);
            addContributors(contributorConnections);
            String cMedian = getRoundedValueOf(ContributorConnectionUtils.getCMedian(ContributorsReportGenerator.contributorConnections(peopleDependencies)));
            report.addParagraph("C-median: " + cMedian, "margin-bottom: 0; margin-top: 10px;");
            report.addParagraph("A half of the contributors has more than " + cMedian + " connections, and a half has less than this number.",
                    "color: grey; font-size: 80%; margin-bottom: 20px");
            report.addParagraph("C-mean: " + getRoundedValueOf(ContributorConnectionUtils.getCMean(ContributorsReportGenerator.contributorConnections(peopleDependencies))), "margin-bottom: 0;");
            report.addParagraph("An average number of connections a contributor has with other contributors.", "color: grey; font-size: 80%; margin-bottom: 20px");
            String cIndex = getRoundedValueOf(ContributorConnectionUtils.getCIndex(ContributorsReportGenerator.contributorConnections(peopleDependencies)));
            report.addParagraph("C-index: " + cIndex, "margin-bottom: 0;");
            report.addParagraph("There are " + cIndex + " contributors with " + cIndex + " or more connections.", "color: grey; font-size: 80%; margin-bottom: 40px");

        }
    }

    private void addDependenciesViaSharedFiles(List<ComponentDependency> peopleDependencies, List<ComponentDependency> peopleFileDependencies, int daysAgo) {
        report.addLevel3Header("Contributor Dependencies via Shared Files", "margin-top: 20px");
        report.startShowMoreBlock(" - show contributor dependencies 2D graph");
        report.addParagraph("The number on lines shows the number of same files that both persons changed in past <b>" + daysAgo + "</b> days.", "color: grey");
        GraphvizDependencyRenderer graphvizDependencyRenderer = new GraphvizDependencyRenderer();
        graphvizDependencyRenderer.setMaxNumberOfDependencies(100);
        graphvizDependencyRenderer.setTypeGraph();

        Set<String> emails = new HashSet<>();
        peopleDependencies.forEach(peopleDependency -> {
            emails.add(peopleDependency.getFromComponent());
            emails.add(peopleDependency.getToComponent());
        });

        String prefix = "people_dependencies_" + daysAgo + "_";
        String graphId = addDependencyGraphVisuals(peopleDependencies, new ArrayList<>(), graphvizDependencyRenderer, prefix);
        report.endShowMoreBlock();
        report.addLineBreak();
        report.addNewTabLink("- open 2D force graph", "visuals/" + graphId + "_force_2d.html");
        report.addLineBreak();
        report.addNewTabLink("- open 3D force graph", "visuals/" + graphId + "_force_3d.html");
        report.addLineBreak();
        if (peopleFileDependencies != null) {
            String prefixFile = "people_dependencies_via_files_" + daysAgo + "_";
            String graphIdFile = add3DDependencyGraphVisuals(peopleFileDependencies, prefixFile);
            report.addNewTabLink("- open 2D force graph (including all files)", "visuals/" + graphIdFile + "_force_2d.html");
            report.addLineBreak();
            report.addNewTabLink("- open 2D force graph (including only shared files)", "visuals/" + graphIdFile + "_force_2d_only_shared_file.html");
            report.addLineBreak();
            report.addNewTabLink("- open 3D force graph (including all files)", "visuals/" + graphIdFile + "_force_3d.html");
            report.addLineBreak();
            report.addNewTabLink("- open 3D force graph (including only shared files)", "visuals/" + graphIdFile + "_force_3d_only_shared_file.html");
            report.addLineBreak();
        }
        report.addLineBreak();
        report.addLineBreak();
        addPeopleDependenciesTable(peopleDependencies);
    }

    private void addContributors(List<ContributorConnection> contributorConnections) {
        report.addLevel3Header("Most Connected Contributors", "margin-top: 20px");
        report.startScrollingDiv();
        report.startTable();
        report.addTableHeader("", "Contributor", "# connections", "# commits");
        int index[] = {0};
        contributorConnections.forEach(contributorConnection -> {
            index[0]++;
            report.startTableRow();
            report.addTableCell(index[0] + ".");
            if (StringUtils.isNotBlank(contributorConnection.getEmail()) && StringUtils.isNotBlank(contributorConnection.getUserName())) {
                report.addTableCell(contributorConnection.getUserName() + " <div style='color: grey; font-size: 80%; margin-bottom: 6px;'>&lt;" + contributorConnection.getEmail() + "&gt;</div>");
            } else {
                report.addTableCell((contributorConnection.getUserName() + contributorConnection.getEmail()).trim());
            }
            report.addTableCell(contributorConnection.getCount() + "");
            report.addTableCell(contributorConnection.getCommits() + "");

            report.endTableRow();
        });
        report.endTable();
        report.endDiv();
    }

    private void addPeopleDependenciesTable(List<ComponentDependency> peopleDependencies) {
        report.startScrollingDiv();
        report.startTable();
        report.addTableHeader("", "Contributor 1", "Contributor 2", "# shared files");
        int index[] = {0};
        if (peopleDependencies.size() > 100) {
            peopleDependencies = peopleDependencies.subList(0, 100);
        }
        peopleDependencies.forEach(dependency -> {
            index[0]++;
            int count = dependency.getCount();

            report.startTableRow();
            report.addTableCell(index[0] + ".");

            String from = dependency.getFromComponent() + "";
            String to = dependency.getToComponent() + "";

            if (emailContributorMap.containsKey(from) && StringUtils.isNotBlank(emailContributorMap.get(from).getUserName())) {
                report.addTableCell(emailContributorMap.get(from).getUserName() + " <div style='color: grey; font-size: 80%; margin-bottom: 6px;'>&lt;" + from + "&gt;</div>");
            } else {
                report.addTableCell(from);
            }

            if (emailContributorMap.containsKey(to) && StringUtils.isNotBlank(emailContributorMap.get(to).getUserName())) {
                report.addTableCell(emailContributorMap.get(to).getUserName() + " <div style='color: grey; font-size: 80%; margin-bottom: 6px;'>&lt;" + to + "&gt;</div>");
            } else {
                report.addTableCell(to);
            }


            report.startTableCell();
            report.startShowMoreBlock(count + " shared " + (count == 1 ? "file" : "files"));
            addSharedFiles(dependency);
            report.endShowMoreBlock();
            report.endTableCell();
            report.endTableRow();
        });
        report.endTable();
        report.endDiv();
    }

    private void addSharedFiles(ComponentDependency dependency) {
        List<String> data = dependency.getData();
        boolean tooLong = data.size() > 100;
        if (tooLong) {
            data = data.subList(0, 100);
        }
        data.forEach(path -> report.addHtmlContent("<br>" + path));
        if (tooLong) {
            report.addHtmlContent("<br>...");
        }
    }

    private String getRoundedValueOf(double value) {
        return "" + (((int) (10 * value)) / 10.0);
    }

    private String addDependencyGraphVisuals(List<ComponentDependency> componentDependencies, List<String> componentNames, GraphvizDependencyRenderer graphvizDependencyRenderer, String prefix) {
        String graphvizContent = graphvizDependencyRenderer.getGraphvizContent(
                componentNames,
                componentDependencies);
        String graphId = prefix + dependencyVisualCounter++;
        report.addGraphvizFigure(graphId, "", graphvizContent);
        report.addLineBreak();
        report.addLineBreak();
        VisualizationTools.addDownloadLinks(report, graphId);

        export3DForceGraph(componentDependencies, graphId);

        return graphId;
    }

    private String add3DDependencyGraphVisuals(List<ComponentDependency> componentDependencies, String prefix) {
        String graphId = prefix + dependencyVisualCounter++;
        export3DForceGraph(componentDependencies, graphId);

        return graphId;
    }

    private void export3DForceGraph(List<ComponentDependency> componentDependencies, String graphId) {
        Force3DObject force3DObject = new Force3DObject();
        Force3DObject force3DObjectOnlyLinked = new Force3DObject();
        Map<String, Integer> names = new HashMap<>();
        componentDependencies.forEach(dependency -> {
            String from = dependency.getFromComponent();
            String to = dependency.getToComponent();
            if (names.containsKey(from)) {
                names.put(from, names.get(from) + 1);
            } else {
                names.put(from, 1);
            }
            if (names.containsKey(to)) {
                names.put(to, names.get(to) + 1);
            } else {
                names.put(to, 1);
            }
            force3DObject.getLinks().add(new Force3DLink(from, to, dependency.getCount()));
            force3DObject.getLinks().add(new Force3DLink(to, from, dependency.getCount()));
        });
        names.keySet().forEach(key -> {
            force3DObject.getNodes().add(new Force3DNode(key, names.get(key)));
        });

        force3DObjectOnlyLinked.setNodes(force3DObject.getNodes().stream().filter(n -> n.getSize() > 1).collect(Collectors.toList()));
        Map<String, Force3DNode> force3DNodeMapLinked = new HashMap<>();
        force3DObjectOnlyLinked.getNodes().forEach(node -> force3DNodeMapLinked.put(node.getId(), node));
        force3DObjectOnlyLinked.setLinks(force3DObject.getLinks().stream()
                .filter(l -> force3DNodeMapLinked.containsKey(l.getSource()) && force3DNodeMapLinked.containsKey(l.getTarget()))
                .collect(Collectors.toList()));

        File folder = new File(reportsFolder, "html/visuals");
        folder.mkdirs();

        try {
            FileUtils.write(new File(folder, graphId + "_force_2d.html"), new VisualizationTemplate().render2DForceGraph(force3DObject), UTF_8);
            FileUtils.write(new File(folder, graphId + "_force_2d_only_shared_file.html"), new VisualizationTemplate().render2DForceGraph(force3DObjectOnlyLinked), UTF_8);
            FileUtils.write(new File(folder, graphId + "_force_3d.html"), new VisualizationTemplate().render3DForceGraph(force3DObject), UTF_8);
            FileUtils.write(new File(folder, graphId + "_force_3d_only_shared_file.html"), new VisualizationTemplate().render3DForceGraph(force3DObjectOnlyLinked), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
