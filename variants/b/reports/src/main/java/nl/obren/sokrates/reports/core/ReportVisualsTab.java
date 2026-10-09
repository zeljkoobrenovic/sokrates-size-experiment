/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.core;

import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.FilesHistoryAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.LogicalDecompositionAnalysisResults;

import java.io.File;

import static nl.obren.sokrates.reports.core.ReportFileExporter.getIconSvg;

class ReportVisualsTab {
    static void addVisuals(RichTextReport report, CodeAnalysisResults analysisResults, File htmlExportFolder) {
        addScopeExplorers(report, analysisResults);

        report.addLineBreak();
        report.addLineBreak();
        report.addLevel2Header("File Visualizations");

        addFileSizeViews(report);

        boolean showDuplication = !analysisResults.skipDuplicationAnalysis() && analysisResults.getDuplicationAnalysisResults().getAllDuplicates().size() > 0;
        if (showDuplication) {
            addDuplicationViews(report);
        }

        addFileAgeViews(report);
        addFileChangeFrequencyViews(report);
        addContributorsPerFileViews(report);

        report.addLineBreak();
        report.addLevel2Header("Contributor Visualizations");
        addContributorDependencyViews(report);

        report.addLineBreak();
        report.addLevel2Header("Components and Dependencies Visualizations");
        addComponentsTable(report, analysisResults, htmlExportFolder);

        report.addLineBreak();
        report.addLineBreak();
        report.addLevel2Header("File Dependencies Visualizations");
        addFileTemporalDependencyViews(report, analysisResults);

        report.addLineBreak();
        report.addLineBreak();
        report.addLevel2Header("Units Visualizations");
        addUnitsViews(report);

        report.addLineBreak();

    }

    private static void addScopeExplorers(RichTextReport report, CodeAnalysisResults analysisResults) {
        AspectAnalysisResults main = analysisResults.getMainAspectAnalysisResults();
        AspectAnalysisResults test = analysisResults.getTestAspectAnalysisResults();
        AspectAnalysisResults build = analysisResults.getBuildAndDeployAspectAnalysisResults();
        AspectAnalysisResults generated = analysisResults.getGeneratedAspectAnalysisResults();
        AspectAnalysisResults other = analysisResults.getOtherAspectAnalysisResults();

        report.addLevel2Header("Visual Code Explorers");

        report.startTable();
        addScopeVisuals(report, "main", main.getFilesCount());
        addScopeVisuals(report, "test", test.getFilesCount());
        addScopeVisuals(report, "build and deployment", build.getFilesCount());
        addScopeVisuals(report, "generated", generated.getFilesCount());
        addScopeVisuals(report, "other", other.getFilesCount());
        report.endTable();
    }

    private static void addFileSizeViews(RichTextReport report) {
        report.addParagraph("<a target='_blank' href='FileSize.html'>File size</a> views:", "margin-bottom: 0;");
        report.startTable("");
        report.startTableRow();
        report.startTableCell("border: none");
        report.addHtmlContent(getIconSvg("file_size", 50));
        report.endTableCell();
        report.startTableCell("border: none");
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("3D view of file size", "visuals/files_3d.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("files grouped by size category", "visuals/zoomable_circles_main_loc_coloring_categories.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("files grouped by folder", "visuals/zoomable_circles_main_loc_coloring.html");
        report.endListItem();
        report.endUnorderedList();
        report.endTable();
    }

    private static void addDuplicationViews(RichTextReport report) {
        report.addParagraph("<a target='_blank' href='Duplication.html'>Duplication</a> views:", "margin-bottom: 0;");
        report.startTable("");
        report.startTableRow();
        report.startTableCell("border: none");
        report.addHtmlContent(getIconSvg("duplication", 50));
        report.endTableCell();
        report.startTableCell("border: none");
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("graphviz graph of duplication among files", "visuals/duplication_among_files.svg");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("2D force graph of duplication among files", "visuals/duplication_among_files_force_2d.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("3D force graph of duplication among files", "visuals/duplication_among_files_force_3d.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("2D view of duplication among files (with duplicates)", "visuals/duplication_among_files_with_duplicates_force_2d.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("3D view of duplication among files (with duplicates)", "visuals/duplication_among_files_with_duplicates_force_3d.html");
        report.endListItem();
        report.endUnorderedList();
        report.endTable();
    }

    private static void addFileAgeViews(RichTextReport report) {
        report.addParagraph("<a target='_blank' href='FileAge.html'>File age</a> views:", "margin-bottom: 0;");
        report.startTable("");
        report.startTableRow();
        report.startTableCell("border: none");
        report.addHtmlContent(getIconSvg("file_history", 50));
        report.endTableCell();
        report.startTableCell("border: none");
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("files grouped by age category", "visuals/zoomable_circles_main_age_coloring_categories.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("files grouped by folder", "visuals/zoomable_circles_main_age_coloring.html");
        report.endListItem();
        report.endUnorderedList();
        report.endTableCell();
        report.endTableRow();
        report.startTableRow();
        report.startTableCell("border: none");
        report.addHtmlContent(getIconSvg("file_history", 50));
        report.endTableCell();
        report.startTableCell("border: none");
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("files grouped by freshness category", "visuals/zoomable_circles_main_freshness_coloring_categories.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("files grouped by folder", "visuals/zoomable_circles_main_freshness_coloring.html");
        report.endListItem();
        report.endUnorderedList();
        report.endTableCell();
        report.endTableRow();
        report.endTable();
    }

    private static void addFileChangeFrequencyViews(RichTextReport report) {
        report.addParagraph("<a target='_blank' href='FileChangeFrequency.html'>File change frequency</a> views:", "margin-bottom: 0;");
        report.startTable("");
        report.startTableRow();
        report.startTableCell("border: none");
        report.addHtmlContent(getIconSvg("change", 50));
        report.endTableCell();
        report.startTableCell("border: none");
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("files grouped by change frequency category", "visuals/zoomable_circles_main_update_frequency_coloring_categories.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("files grouped by folder", "visuals/zoomable_circles_main_update_frequency_coloring.html");
        report.endListItem();
        report.endUnorderedList();
        report.endTableCell();
        report.endTableRow();
        report.endTable();
    }

    private static void addContributorsPerFileViews(RichTextReport report) {
        report.addParagraph("<a target='_blank' href='FileChangeFrequency.html'>Contributors per file</a> views:", "margin-bottom: 0;");
        report.startTable("");
        report.startTableRow();
        report.startTableCell("border: none");
        report.addHtmlContent(getIconSvg("change", 50));
        report.endTableCell();
        report.startTableCell("border: none");
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("files grouped by number of contributors category", "visuals/zoomable_circles_main_contributors_count_coloring_categories.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("files grouped by folder", "visuals/zoomable_circles_main_contributors_count_coloring.html");
        report.endListItem();
        report.endUnorderedList();
        report.endTableCell();
        report.endTableRow();
        report.endTable();
    }

    private static void addContributorDependencyViews(RichTextReport report) {
        report.addParagraph("<a target='_blank' href='Contributors.html'>Contributor dependency</a> views:", "margin-bottom: 0;");
        report.startTable("");
        report.startTableRow();
        report.startTableCell("border: none");
        report.addHtmlContent(getIconSvg("contributors", 50));
        report.endTableCell();
        report.startTableCell("border: none");
        report.startUnorderedList();
        addPeopleDependenciesItem(report, "past 30 days: ", "30_1", "30_2");
        addPeopleDependenciesItem(report, "past 3 months: ", "90_3", "90_4");
        addPeopleDependenciesItem(report, "past 6 months: ", "180_5", "180_6");
        addPeopleDependenciesItem(report, "past year: ", "365_7", "365_8");
        report.endTableRow();
        report.endTable();
    }

    private static void addPeopleDependenciesItem(RichTextReport report, String label, String suffix, String viaFilesSuffix) {
        report.startListItem();
        report.addHtmlContent(label);
        report.addNewTabLink("graphviz", "visuals/people_dependencies_" + suffix + ".svg");
        report.addHtmlContent(" | ");
        report.addNewTabLink("2D graph", "visuals/people_dependencies_" + suffix + "_force_2d.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("2D graph (with files)", "visuals/people_dependencies_via_files_" + viaFilesSuffix + "_force_2d.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("2D graph (with shared files only)", "visuals/people_dependencies_via_files_" + viaFilesSuffix + "_force_2d_only_shared_file.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("3D graph", "visuals/people_dependencies_" + suffix + "_force_3d.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("3D graph (with files)", "visuals/people_dependencies_via_files_" + viaFilesSuffix + "_force_3d.html");
        report.addHtmlContent(" | ");
        report.addNewTabLink("3D graph (with shared files only)", "visuals/people_dependencies_via_files_" + viaFilesSuffix + "_force_3d_only_shared_file.html");
        report.endListItem();
    }

    private static void addComponentsTable(RichTextReport report, CodeAnalysisResults analysisResults, File htmlExportFolder) {
        report.startTable("text-align: center");
        report.addHtmlContent("<tr>");
        report.addHtmlContent("<td rowspan='3' style='border: none'></td>");
        report.addHtmlContent("<td colspan='2' style='text-align: center; border: none'>" + getIconSvg("code_organization", 50) + "</td>");
        report.addHtmlContent("<td colspan='6' style='text-align: center; border: none'>" + getIconSvg("temporal_dependency", 50) + "</td>");
        report.addHtmlContent("<td colspan='1' style='text-align: center; border: none'>" + getIconSvg("duplication", 50) + "</td>");
        report.addHtmlContent("<td colspan='2' style='text-align: center; border: none'>" + getIconSvg("commits", 50) + "</td>");
        report.addHtmlContent("</tr>");
        report.addHtmlContent("<tr>");
        report.addHtmlContent("<td colspan='2' rowspan='2' style='text-align: center'><a target='_blank' href='Components.html'>Components</a></td>");
        report.addHtmlContent("<td colspan='9' style='text-align: center'><a target='_blank' href='FileTemporalDependencies.html'>Temporal Dependencies</a></td>");
        report.addHtmlContent("<td colspan='1' rowspan='2' style='text-align: center'><a target='_blank' href='Duplication.html'>Duplication</a></td>");
        report.addHtmlContent("<td colspan='2' rowspan='2' style='text-align: center'><a target='_blank' href='Commits.html'>Commits Racing Charts</a></td>");
        report.addHtmlContent("</tr>");
        report.addHtmlContent("<tr>");
        report.addHtmlContent("<td colspan='3' style='text-align: center'>30 days</td>");
        report.addHtmlContent("<td colspan='3' style='text-align: center'>3 months</td>");
        report.addHtmlContent("<td colspan='3' style='text-align: center'>6 months</td>");
        report.addHtmlContent("</tr>");

        int index[] = {0};
        analysisResults.getLogicalDecompositionsAnalysisResults().forEach(logicalDecomposition -> {
            index[0] += 1;
            addComponentsRow(report, analysisResults, htmlExportFolder, logicalDecomposition, index[0]);
        });
        report.endTable();
    }

    private static void addComponentsRow(RichTextReport report, CodeAnalysisResults analysisResults, File htmlExportFolder, LogicalDecompositionAnalysisResults logicalDecomposition, int index) {
        FilesHistoryAnalysisResults history = analysisResults.getFilesHistoryAnalysisResults();
        report.startTableRow();
        report.addTableCell(logicalDecomposition.getKey().toUpperCase() + " (" + logicalDecomposition.getComponents().size() + ")");
        report.startTableCell("text-align: center");
        report.addNewTabLink("Bubble Chart", "visuals/bubble_chart_components_" + index + ".html");
        report.endTableCell();
        report.startTableCell("text-align: center");
        report.addNewTabLink("Tree Map", "visuals/tree_map_components_" + index + ".html");
        report.endTableCell();
        addTemporalDependencyCells(report, index, 30, history.getFilePairsChangedTogether30Days().size() > 0);
        addTemporalDependencyCells(report, index, 90, history.getFilePairsChangedTogether90Days().size() > 0);
        addTemporalDependencyCells(report, index, 180, history.getFilePairsChangedTogether180Days().size() > 0);
        report.startTableCell("text-align: center");
        String duplicationGraphPath = "visuals/duplication_dependencies_" + index + ".svg";
        if (new File(htmlExportFolder, duplicationGraphPath).exists()) {
            report.addNewTabLink("Duplication Graph", duplicationGraphPath);
        } else {
            report.addContentInDiv("Duplication Graph", "color: #c0c0c0");
        }
        report.endTableCell();
        report.startTableCell("text-align: center");
        report.addNewTabLink("All Time", "visuals/racing_charts_component_commits_" + index + ".html?tickDuration=600");
        report.endTableCell();
        report.startTableCell("text-align: center");
        report.addNewTabLink("12 Months", "visuals/racing_charts_component_commits_12_months_window_" + index + ".html?tickDuration=600");
        report.endTableCell();
        report.endTableRow();
    }

    private static void addTemporalDependencyCells(RichTextReport report, int index, int days, boolean hasPairs) {
        String prefix = "visuals/file_changed_together_dependencies_logical_decomposition_" + index + "_" + days + "_days";
        addTemporalDependencyCell(report, "graphviz", hasPairs, prefix + ".svg");
        addTemporalDependencyCell(report, "2D", hasPairs, prefix + "_force_2d.html");
        addTemporalDependencyCell(report, "3D", hasPairs, prefix + "_force_3d.html");
    }

    private static void addTemporalDependencyCell(RichTextReport report, String label, boolean hasPairs, String path) {
        report.startTableCell("text-align: center");
        if (hasPairs) {
            report.addNewTabLink(label, path);
        } else {
            report.addContentInDiv(label, "color: #c0c0c0");
        }
        report.endTableCell();
    }

    private static void addFileTemporalDependencyViews(RichTextReport report, CodeAnalysisResults analysisResults) {
        FilesHistoryAnalysisResults history = analysisResults.getFilesHistoryAnalysisResults();
        report.addParagraph("<a target='_blank' href='FileTemporalDependencies.html'>Temporal dependencies</a> among files:", "margin-bottom: 0;");
        report.startTable("");
        report.startTableRow();
        report.startTableCell("border: none");
        report.addHtmlContent(getIconSvg("temporal_dependency", 50));
        report.endTableCell();
        report.startTableCell("border: none");
        report.startUnorderedList();
        addFileTemporalDependenciesItem(report, "past 30 days: ", history.getFilePairsChangedTogether30Days().size() > 0, 30);
        addFileTemporalDependenciesItem(report, "past 3 months: ", history.getFilePairsChangedTogether90Days().size() > 0, 90);
        addFileTemporalDependenciesItem(report, "past 6 months: ", history.getFilePairsChangedTogether180Days().size() > 0, 180);
        report.endUnorderedList();
        report.endTableCell();
        report.endTableRow();
        report.endTable();
    }

    private static void addFileTemporalDependenciesItem(RichTextReport report, String label, boolean hasPairs, int days) {
        report.startListItem();
        report.addHtmlContent(label);
        if (hasPairs) {
            report.addNewTabLink("graphviz", "visuals/file_changed_together_dependencies_files_" + days + "_days.svg");
            report.addHtmlContent(" | ");
            report.addNewTabLink("2D graph", "visuals/file_changed_together_dependencies_files_" + days + "_days_force_2d.html");
            report.addHtmlContent(" | ");
            report.addNewTabLink("2D graph (with commits)", "visuals/file_changed_together_dependencies_with_commits_components_" + days + "_days_force_2d.html");
            report.addHtmlContent(" | ");
            report.addNewTabLink("3D graph", "visuals/file_changed_together_dependencies_files_" + days + "_days_force_3d.html");
            report.addHtmlContent(" | ");
            report.addNewTabLink("3D graph (with commits)", "visuals/file_changed_together_dependencies_with_commits_components_" + days + "_days_force_3d.html");
        } else {
            report.addHtmlContent("no dependencies");
        }
        report.endListItem();
    }

    private static void addUnitsViews(RichTextReport report) {
        report.addParagraph("Unit <a target='_blank' href='UnitSize.html'>size</a> and <a target='_blank' href='ConditionalComplexity.html'>conditional complexity</a> views:", "margin-bottom: 0;");
        report.startTable("");
        report.startTableRow();
        report.startTableCell("border: none");
        report.addHtmlContent(getIconSvg("unit_size", 50));
        report.endTableCell();
        report.startTableCell("border: none");
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("3D view of unit size", "visuals/units_3d_size.html");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("3D view of unit complexity", "visuals/units_3d_complexity.html");
        report.endListItem();
        report.endUnorderedList();
        report.endTableCell();
        report.endTableRow();
        report.endTable();
    }

    private static void addScopeVisuals(RichTextReport report, String scopeName, int filesCount) {
        String technicalName = scopeName.toLowerCase().replace(" ", "_");
        boolean exists = filesCount > 0;
        report.startTableRow(exists ? "" : "color: #c0c0c0");
        report.startTableCell();
        report.addHtmlContent(getIconSvg(technicalName, 42));
        report.endTableCell();
        report.addTableCell(scopeName.toUpperCase() + " (" + filesCount + ")", "");
        report.startTableCell();
        if (exists) {
            report.addNewTabLink("Circles", "visuals/zoomable_circles_" + technicalName.replace("_and_deployment", "") + ".html");
        } else {
            report.addContentInDiv("Circles", "color: #c0c0c0");
        }
        report.endTableCell();
        report.startTableCell();
        if (exists) {
            report.addNewTabLink("Sunburst", "visuals/zoomable_sunburst_" + technicalName.replace("_and_deployment", "") + ".html");
        } else {
            report.addContentInDiv("Sunburst", "color: #c0c0c0");
        }
        report.endTableCell();
        report.endTableCell();
        report.endTableRow();
    }
}
