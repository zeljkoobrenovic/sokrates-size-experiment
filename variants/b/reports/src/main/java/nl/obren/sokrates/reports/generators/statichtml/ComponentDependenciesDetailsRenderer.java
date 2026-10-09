/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.charts.SimpleOneBarChart;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.dataexporters.DataExporter;
import nl.obren.sokrates.sourcecode.analysis.results.LogicalDecompositionAnalysisResults;
import nl.obren.sokrates.sourcecode.aspects.NamedSourceCodeAspect;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.dependencies.DependencyEvidence;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class ComponentDependenciesDetailsRenderer {
    private RichTextReport report;

    ComponentDependenciesDetailsRenderer(RichTextReport report) {
        this.report = report;
    }

    void addMoreDetailsSection(LogicalDecompositionAnalysisResults logicalDecomposition, List<ComponentDependency> componentDependencies) {
        report.startShowMoreBlock("Show more details about dependencies...");
        report.startDiv("width: 100%; overflow-x: auto; max-height: 600px");
        report.startTable();
        report.addTableHeader("From Component<br/>&nbsp;--> To Component", "From Component<br/>(files with dependencies)", "Details");
        Collections.sort(componentDependencies, (o1, o2) -> o2.getCount() - o1.getCount());
        componentDependencies.forEach(componentDependency -> {
            addDependencyRow(logicalDecomposition, componentDependency);
        });
        report.endTable();
        report.endDiv();
        report.endShowMoreBlock();
    }

    private void addDependencyRow(LogicalDecompositionAnalysisResults logicalDecomposition, ComponentDependency componentDependency) {
        report.startTableRow();
        report.addTableCell(componentDependency.getFromComponent() + "<br/>&nbsp&nbsp;-->&nbsp" + componentDependency.getToComponent()
        );
        report.addHtmlContent("<td>");
        int locFromDuplications = componentDependency.getLocFrom();
        NamedSourceCodeAspect fromComponentByName = logicalDecomposition.getLogicalDecomposition().getComponentByName(componentDependency.getFromComponent());
        String percentageHtmlFragment = null;
        int dependencyCount = componentDependency.getCount();
        if (fromComponentByName != null) {
            percentageHtmlFragment = getFromDependencyCoverageSvg(locFromDuplications, fromComponentByName, dependencyCount);
        }

        report.addShowMoreBlock("",
                "<textarea style='width:90%; height: 20em;'>"
                        + componentDependency.getEvidence().stream().map(DependencyEvidence::getPathFrom).collect(Collectors.joining("\n")) +
                        "</textarea>",
                (percentageHtmlFragment != null ? "" + percentageHtmlFragment : dependencyCount + " files (" + locFromDuplications + " LOC)<br/>")
        );
        report.addHtmlContent("</td>");
        report.addTableCell("<a href='../data/text/" + DataExporter.dependenciesFileNamePrefix(componentDependency.getFromComponent(), componentDependency.getToComponent(), logicalDecomposition.getKey()) + ".txt'><b>" + dependencyCount + "</b> source " + (dependencyCount == 1 ? "file" : "files") + "</a>");
        report.endTableRow();
    }

    private String getFromDependencyCoverageSvg(int locFromDuplications, NamedSourceCodeAspect fromComponentByName, int dependencyCount) {
        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(320);
        chart.setMaxBarWidth(100);
        chart.setBarHeight(14);
        chart.setBarStartXOffset(2);
        chart.setSmallerFontSize();

        double percentage = 100.0 * locFromDuplications / fromComponentByName.getLinesOfCode();
        String percentageText = FormattingUtils.getFormattedPercentage(percentage) + "%";

        String textRight = dependencyCount + " "
                + (dependencyCount == 1 ? "file" : "files") + ", "
                + locFromDuplications + " LOC (" + percentageText + ")";

        return chart.getPercentageSvg(percentage, "", textRight);
    }
}
