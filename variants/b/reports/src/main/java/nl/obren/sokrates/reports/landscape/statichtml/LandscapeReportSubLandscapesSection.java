/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.io.JsonMapper;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.utils.Force3DGraphExporter;
import nl.obren.sokrates.reports.utils.GraphvizDependencyRenderer;
import nl.obren.sokrates.sourcecode.Metadata;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.SubLandscapeLink;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResultsReadData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.*;

/**
 * The sub-landscapes tab of the landscape report: the sub-landscape table and the level 1 sub-landscape dependency graphs.
 */
class LandscapeReportSubLandscapesSection {
    private static final Log LOG = LogFactory.getLog(LandscapeReportSubLandscapesSection.class);
    private final RichTextReport landscapeReport;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final File folder;
    private final File reportsFolder;
    private final LandscapeReportChrome chrome;

    LandscapeReportSubLandscapesSection(RichTextReport landscapeReport, LandscapeAnalysisResults landscapeAnalysisResults, File folder, File reportsFolder, LandscapeReportChrome chrome) {
        this.landscapeReport = landscapeReport;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.folder = folder;
        this.reportsFolder = reportsFolder;
        this.chrome = chrome;
    }

    void renderSubLandscapeDependenciesViaContributors() {
        GraphvizDependencyRenderer renderer = new GraphvizDependencyRenderer();
        renderer.setMaxNumberOfDependencies(100);
        renderer.setDefaultNodeFillColor("deepskyblue2");
        renderer.setOrientation("LR");
        renderer.setTypeGraph();
        List<ComponentDependency> dependencies = landscapeAnalysisResults.getSubLandscapeDependenciesViaRepositoriesWithSameContributors();
        String graphvizContent = renderer.getGraphvizContent(landscapeAnalysisResults.getLevel1SubLandscapes().stream().map(s -> "[" + s + "]").collect(Collectors.toCollection(ArrayList::new)), landscapeAnalysisResults.getSubLandscapeIndirectDependenciesViaRepositoriesWithSameContributors());

        landscapeReport.startShowMoreBlock("show sub-landscape/repository dependencies...");
        landscapeReport.addGraphvizFigure("sub_landscape_dependencies_same_contributors", "Extension dependencies", graphvizContent);
        chrome.addDownloadLinks("sub_landscape_dependencies_same_contributors");
        landscapeReport.endShowMoreBlock();
        landscapeReport.addLineBreak();
        landscapeReport.addNewTabLink(" - show dependencies as 2D force graph&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "visuals/sub_landscape_dependencies_same_contributors_force_2d.html");
        landscapeReport.addNewTabLink(" - show dependencies as 3D force graph&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "visuals/sub_landscape_dependencies_same_contributors_force_3d.html");
        new Force3DGraphExporter().export2D3DForceGraph(dependencies, reportsFolder, "sub_landscape_dependencies_same_contributors");

    }

    void renderSubLandscapeDependenciesViaRepoName() {
        GraphvizDependencyRenderer renderer = new GraphvizDependencyRenderer();
        renderer.setMaxNumberOfDependencies(100);
        renderer.setDefaultNodeFillColor("deepskyblue2");
        renderer.setOrientation("LR");
        renderer.setTypeGraph();
        List<ComponentDependency> dependencies = landscapeAnalysisResults.getSubLandscapeDependenciesViaRepositoriesWithSameName();
        String graphvizContent = renderer.getGraphvizContent(landscapeAnalysisResults.getLevel1SubLandscapes().stream().map(s -> "[" + s + "]").collect(Collectors.toCollection(ArrayList::new)), landscapeAnalysisResults.getSubLandscapeIndirectDependenciesViaRepositoriesWithSameName());

        landscapeReport.startShowMoreBlock("show sub-landscape/repository dependencies...");
        landscapeReport.addGraphvizFigure("sub_landscape_dependencies_same_name_repos", "Extension dependencies", graphvizContent);
        chrome.addDownloadLinks("sub_landscape_dependencies_same_name_repos");
        landscapeReport.endShowMoreBlock();
        landscapeReport.addLineBreak();
        landscapeReport.addNewTabLink(" - show dependencies as 2D force graph&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "visuals/sub_landscape_dependencies_same_name_repos_force_2d.html");
        landscapeReport.addNewTabLink(" - show dependencies as 3D force graph&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "visuals/sub_landscape_dependencies_same_name_repos_force_3d.html");
        new Force3DGraphExporter().export2D3DForceGraph(dependencies, reportsFolder, "sub_landscape_dependencies_same_name_repos");

    }

    void addSubLandscapeSection(List<SubLandscapeLink> subLandscapes) {
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();
        final int maxDepth = configuration.getMaxSublandscapeDepth();
        List<SubLandscapeLink> links = subLandscapes.stream().filter(l -> maxDepth == 0 || LandscapeReportChrome.getPathDepth(l.getIndexFilePath()) <= maxDepth).collect(Collectors.toList());
        if (links.size() > 0) {
            Collections.sort(links, Comparator.comparing(a -> getLabel(a).toLowerCase()));
            landscapeReport.startDiv("margin: 12px; margin-bottom: 22px");

            addSubLandscapeVisualLinks();

            landscapeReport.startTable();
            landscapeReport.addTableHeader("", "", "repositories", "main loc", "test loc", "other loc", "commits<br>(all time)", "contributors<br>(30 days)", "commits<br>(30 days)", "commit period");
            String prevRoot[] = {""};
            List<LandscapeAnalysisResultsReadData> loadedSubLandscapes = new ArrayList<>();
            links.stream().sorted((a, b) -> compareSubLandscapeLinks(a, b)).forEach(subLandscape -> {
                addSubLandscapeRow(configuration, subLandscape, prevRoot, loadedSubLandscapes);
            });
            landscapeReport.endTable();

            landscapeReport.endDiv();
        }

    }

    private void addSubLandscapeVisualLinks() {
        landscapeReport.addHtmlContent("zoomable circles: ");
        landscapeReport.addNewTabLink("contributors (30d)", "visuals/sub_landscapes_zoomable_circles_" + CONTRIBUTORS_30_D + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("commits (30d)", "visuals/sub_landscapes_zoomable_circles_" + COMMITS_30_D + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("lines of code (main)", "visuals/sub_landscapes_zoomable_circles_" + MAIN_LOC + ".html");
        landscapeReport.addLineBreak();
        landscapeReport.addHtmlContent("zoomable sunburst: ");
        landscapeReport.addNewTabLink("contributors (30d)", "visuals/sub_landscapes_zoomable_sunburst_" + CONTRIBUTORS_30_D + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("commits (30d)", "visuals/sub_landscapes_zoomable_sunburst_" + COMMITS_30_D + ".html");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("lines of code (main)", "visuals/sub_landscapes_zoomable_sunburst_" + MAIN_LOC + ".html");
        landscapeReport.addLineBreak();
        landscapeReport.addLineBreak();
    }

    private void addSubLandscapeRow(LandscapeConfiguration configuration, SubLandscapeLink subLandscape, String[] prevRoot, List<LandscapeAnalysisResultsReadData> loadedSubLandscapes) {
        LOG.info("Adding " + subLandscape.getIndexFilePath());
        String labelText = StringUtils.removeEnd(getLabel(subLandscape), "/");
        String label = labelText;
        String style = "";
        String root = label.replaceAll("/.*", "");
        boolean isRoot;
        if (!prevRoot[0].equals(root)) {
            isRoot = true;
            label = "<b>" + label + "</b>";
            style = "color: black; font-weight: bold;";
        } else {
            isRoot = false;
            int lastIndex = label.lastIndexOf("/");
            label = "<span style='color: lightgrey'>" + label.substring(0, lastIndex + 1) + "</span>" + label.substring(lastIndex + 1) + "";
            style = "color: grey; font-size: 90%";
        }
        String href = configuration.getRepositoryReportsUrlPrefix() + subLandscape.getIndexFilePath();
        LandscapeAnalysisResultsReadData subLandscapeAnalysisResults = getSubLandscapeAnalysisResults(subLandscape);
        landscapeReport.startTableRow(style);
        LandscapeConfiguration subLandscapeConfig = getSubLandscapeConfig(subLandscape);
        Metadata metadata = subLandscapeConfig.getMetadata();
        landscapeReport.addTableCell(!labelText.contains("/") ? ("<a href='" + href + "' target='_blank'>" +
                (StringUtils.isNotBlank(metadata.getLogoLink())
                        ? "<img src='" + getLogoLink(configuration.getRepositoryReportsUrlPrefix() + subLandscape.getIndexFilePath().replace("/index.html", ""), metadata.getLogoLink()) + "' " +
                        "style='vertical-align: middle; width: 24px' " +
                        "onerror=\"this.onerror=null;this.src='https://zeljkoobrenovic.github.io/sokrates-media/icons/landscape.png'\">"
                        : "<img src='https://zeljkoobrenovic.github.io/sokrates-media/icons/landscape.png' style='vertical-align: middle; width: 24px'>") +
                "</a>") : "", "text-align: center;");

        landscapeReport.startTableCell();
        landscapeReport.addNewTabLink(label, href);
        loadedSubLandscapes.add(subLandscapeAnalysisResults);
        landscapeReport.endTableCell();
        addSubLandscapeCountCells(subLandscapeAnalysisResults);
        addSubLandscapeCommitPeriodCell(subLandscapeAnalysisResults, isRoot);
        landscapeReport.endTableRow();

        prevRoot[0] = root;
    }

    private void addSubLandscapeCountCells(LandscapeAnalysisResultsReadData subLandscapeAnalysisResults) {
        landscapeReport.startTableCell("text-align: right;");
        if (subLandscapeAnalysisResults != null) {
            landscapeReport.addHtmlContent(FormattingUtils.formatCount(subLandscapeAnalysisResults.getRepositoriesCount()) + "");
        }
        landscapeReport.endTableCell();
        landscapeReport.startTableCell("text-align: right;");
        if (subLandscapeAnalysisResults != null) {
            landscapeReport.addHtmlContent(FormattingUtils.formatCount(subLandscapeAnalysisResults.getMainLoc()) + "");
        }
        landscapeReport.endTableCell();
        landscapeReport.startTableCell("text-align: right;");
        if (subLandscapeAnalysisResults != null) {
            landscapeReport.addHtmlContent(FormattingUtils.formatCount(subLandscapeAnalysisResults.getTestLoc()) + "");
        }
        landscapeReport.endTableCell();
        landscapeReport.endTableCell();
        landscapeReport.startTableCell("text-align: right;");
        if (subLandscapeAnalysisResults != null) {
            int other = subLandscapeAnalysisResults.getBuildAndDeploymentLoc()
                    + subLandscapeAnalysisResults.getGeneratedLoc() + subLandscapeAnalysisResults.getOtherLoc();
            landscapeReport.addHtmlContent("<span style='color: lightgrey'>" + FormattingUtils.formatCount(other) + "</span>");
        }
        landscapeReport.endTableCell();
        landscapeReport.startTableCell("text-align: right;");
        if (subLandscapeAnalysisResults != null) {
            landscapeReport.addHtmlContent(FormattingUtils.formatCount(subLandscapeAnalysisResults.getCommitsCount()) + "");
        }
        landscapeReport.endTableCell();
        landscapeReport.startTableCell("text-align: right;");
        if (subLandscapeAnalysisResults != null) {
            landscapeReport.addHtmlContent(FormattingUtils.formatCount(subLandscapeAnalysisResults.getRecentContributorsCount()) + "");
        }
        landscapeReport.endTableCell();
        landscapeReport.startTableCell("text-align: right;");
        if (subLandscapeAnalysisResults != null) {
            landscapeReport.addHtmlContent(FormattingUtils.formatCount(subLandscapeAnalysisResults.getCommitsCount30Days()) + "");
        }
        landscapeReport.endTableCell();
    }

    private void addSubLandscapeCommitPeriodCell(LandscapeAnalysisResultsReadData subLandscapeAnalysisResults, boolean isRoot) {
        landscapeReport.startTableCell("text-align: right; font-size: 70%");
        if (subLandscapeAnalysisResults != null) {
            String firstYear = DateUtils.getYear(subLandscapeAnalysisResults.getFirstCommitDate());
            String lastYear = DateUtils.getYear(subLandscapeAnalysisResults.getLatestCommitDate());
            landscapeReport.addHtmlContent(firstYear);
            landscapeReport.addHtmlContent("-");
            landscapeReport.addHtmlContent(lastYear);
            try {
                int first = Integer.parseInt(firstYear);
                int last = Integer.parseInt(lastYear);
                if (last >= first) {
                    int width = Math.min(20, last - first + 1) * 7;
                    String periodStyle = "margin-left: auto; margin-right: 0; margin-top: 2px; padding: 0; width: " + width + "px;";
                    if (!isRoot) {
                        periodStyle += "background-color: lightgrey; height: 4px;";
                    } else {
                        periodStyle += "height: 7px; background-color: green;";
                    }
                    landscapeReport.addContentInDiv("", periodStyle);
                }
            } catch (NumberFormatException e) {
            }
        }
        landscapeReport.endTableCell();
    }

    private int compareSubLandscapeLinks(SubLandscapeLink a, SubLandscapeLink b) {
        if (a.getLandscapeAnalysisResults() != null && b.getLandscapeAnalysisResults() != null) {
            return b.getLandscapeAnalysisResults().getFirstCommitDate().compareTo(a.getLandscapeAnalysisResults().getFirstCommitDate());
        }

        return 0;
    }

    private String getLogoLink(String repositoryLinkPrefix, String link) {
        return link.startsWith("/") || link.contains("://") || link.startsWith("data:image")
                ? link
                : StringUtils.appendIfMissing(repositoryLinkPrefix, "/") + link;
    }

    private LandscapeAnalysisResultsReadData getSubLandscapeAnalysisResults(SubLandscapeLink subLandscape) {
        try {
            String prefix = landscapeAnalysisResults.getConfiguration().getRepositoryReportsUrlPrefix();
            File resultsFile = new File(new File(folder, prefix + subLandscape.getIndexFilePath()).getParentFile(), "data/landscapeAnalysisResults.json");
            LOG.info(resultsFile.getPath());
            String json = FileUtils.readFileToString(resultsFile, StandardCharsets.UTF_8);
            return (LandscapeAnalysisResultsReadData) new JsonMapper().getObject(json, LandscapeAnalysisResultsReadData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private LandscapeConfiguration getSubLandscapeConfig(SubLandscapeLink subLandscape) {
        try {
            String prefix = landscapeAnalysisResults.getConfiguration().getRepositoryReportsUrlPrefix();
            File resultsFile = new File(new File(folder, prefix + subLandscape.getIndexFilePath()).getParentFile(), "config.json");
            LOG.info(resultsFile.getPath());
            String json = FileUtils.readFileToString(resultsFile, StandardCharsets.UTF_8);
            return (LandscapeConfiguration) new JsonMapper().getObject(json, LandscapeConfiguration.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getLabel(SubLandscapeLink subLandscape) {
        return subLandscape.getIndexFilePath()
                .replaceAll("(/|\\\\)_sokrates_landscape(/|\\\\).*", "");
    }
}
