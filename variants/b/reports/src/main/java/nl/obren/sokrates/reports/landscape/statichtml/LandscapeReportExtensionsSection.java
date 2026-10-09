/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.core.ReportConstants;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.generators.statichtml.HistoryPerLanguageGenerator;
import nl.obren.sokrates.reports.landscape.statichtml.repositories.LandscapeRepositoriesTagsLine;
import nl.obren.sokrates.reports.landscape.utils.ExtractStringListValue;
import nl.obren.sokrates.reports.landscape.utils.Force3DGraphExporter;
import nl.obren.sokrates.reports.landscape.utils.LandscapeGeneratorUtils;
import nl.obren.sokrates.reports.landscape.utils.RacingLanguagesBarChartsExporter;
import nl.obren.sokrates.reports.landscape.statichtml.repositories.TagMap;
import nl.obren.sokrates.reports.utils.GraphvizDependencyRenderer;
import nl.obren.sokrates.sourcecode.analysis.results.HistoryPerExtension;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.githistory.CommitsPerExtension;
import nl.obren.sokrates.sourcecode.landscape.TagGroup;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.*;

/**
 * The file extensions sections of the landscape report: lines of code per extension (main, test, other),
 * contributors per extension with their dependencies, the commit history per extension and the tags line.
 */
class LandscapeReportExtensionsSection {
    private final RichTextReport landscapeReport;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final File reportsFolder;
    private final List<TagGroup> tagGroups;
    private final TagMap customTagsMap;
    private final LandscapeReportInfoBlocks infoBlocks;
    private final LandscapeReportChrome chrome;

    LandscapeReportExtensionsSection(RichTextReport landscapeReport, LandscapeAnalysisResults landscapeAnalysisResults, File reportsFolder,
                                     List<TagGroup> tagGroups, TagMap customTagsMap, LandscapeReportInfoBlocks infoBlocks, LandscapeReportChrome chrome) {
        this.landscapeReport = landscapeReport;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.reportsFolder = reportsFolder;
        this.tagGroups = tagGroups;
        this.customTagsMap = customTagsMap;
        this.infoBlocks = infoBlocks;
        this.chrome = chrome;
    }

    void addExtensions() {
        List<NumericMetric> linesOfCodePerExtensionMain = LandscapeGeneratorUtils.getLinesOfCodePerExtension(landscapeAnalysisResults, landscapeAnalysisResults.getMainLinesOfCodePerExtension());
        addMainExtensions("Main", linesOfCodePerExtensionMain, true);
        if (landscapeAnalysisResults.getRecentContributorsCount(landscapeAnalysisResults.getContributors()) > 0) {
            addContributorsPerExtension(true);
        }
        landscapeReport.startShowMoreBlockDisappear("", "&nbsp;&nbsp;>&nbsp;Show test and other code...");
        addMainExtensions("Test", LandscapeGeneratorUtils.getLinesOfCodePerExtension(landscapeAnalysisResults, landscapeAnalysisResults.getTestLinesOfCodePerExtension()), false);
        addMainExtensions("Other", LandscapeGeneratorUtils.getLinesOfCodePerExtension(landscapeAnalysisResults, landscapeAnalysisResults.getOtherLinesOfCodePerExtension()), false);
        landscapeReport.endShowMoreBlock();
        landscapeReport.addLineBreak();
        landscapeReport.addLineBreak();
        landscapeReport.startShowMoreBlockDisappear("", "&nbsp;&nbsp;>&nbsp;Show commit history per extension...");

        landscapeReport.startSubSection("Commit history per file extension", "");
        landscapeReport.startDiv("max-height: 600px; overflow-y: auto;");
        landscapeReport.startDiv("margin-bottom: 16px; vertical-align: middle;");
        landscapeReport.addContentInDiv(ReportConstants.ANIMATION_SVG_ICON, "display: inline-block; vertical-align: middle; margin: 4px;");
        landscapeReport.addHtmlContent("animated commit history: ");
        landscapeReport.addNewTabLink("all time cumulative", "visuals/racing_charts_extensions_commits.html?tickDuration=600");
        landscapeReport.addHtmlContent(" | ");
        landscapeReport.addNewTabLink("12 months window", "visuals/racing_charts_extensions_commits_window.html?tickDuration=600");
        landscapeReport.endDiv();
        List<String> extensions = linesOfCodePerExtensionMain.stream().map(loc -> loc.getName().replaceAll(".*[.]", "").trim()).collect(Collectors.toList());
        List<HistoryPerExtension> yearlyCommitHistoryPerExtension = landscapeAnalysisResults.getYearlyCommitHistoryPerExtension();
        HistoryPerLanguageGenerator.getInstanceCommits(yearlyCommitHistoryPerExtension, extensions).addHistoryPerLanguage(landscapeReport);
        new RacingLanguagesBarChartsExporter(landscapeAnalysisResults, yearlyCommitHistoryPerExtension, extensions).exportRacingChart(reportsFolder);
        landscapeReport.endDiv();
        landscapeReport.endSection();

        landscapeReport.endShowMoreBlock();

        landscapeReport.addLineBreak();
        landscapeReport.addLineBreak();
        landscapeReport.addLineBreak();

        chrome.addIFrames(landscapeAnalysisResults.getConfiguration().getiFramesAtStart());

        landscapeReport.startSubSection("Tags (" + customTagsMap.tagsCount() + ")", "");
        new LandscapeRepositoriesTagsLine(tagGroups, customTagsMap).addTagsLine(landscapeReport);
        landscapeReport.endSection();

        landscapeReport.addLineBreak();
    }

    private void addMainExtensions(String type, List<NumericMetric> linesOfCodePerExtension, boolean linkCharts) {
        int threshold = landscapeAnalysisResults.getConfiguration().getExtensionThresholdLoc();
        landscapeReport.startSubSection("Lines of Code in " + type + " Code (" + linesOfCodePerExtension.size() + ")",
                threshold >= 1 ? threshold + "+ lines of code" : "");
        if (linkCharts) {
            landscapeReport.startDiv("");
            landscapeReport.addNewTabLink("bubble chart", "visuals/bubble_chart_extensions.html");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("tree map", "visuals/tree_map_extensions.html");
            landscapeReport.addLineBreak();
            landscapeReport.addLineBreak();
            landscapeReport.endDiv();
        }
        landscapeReport.startDiv("");
        boolean tooLong = linesOfCodePerExtension.size() > 25;
        List<NumericMetric> linesOfCodePerExtensionDisplay = tooLong ? linesOfCodePerExtension.subList(0, 25) : linesOfCodePerExtension;
        List<NumericMetric> linesOfCodePerExtensionHide = tooLong ? linesOfCodePerExtension.subList(25, linesOfCodePerExtension.size()) : new ArrayList<>();
        linesOfCodePerExtensionDisplay.forEach(extension -> {
            addLangInfo(extension);
        });
        if (linesOfCodePerExtensionHide.size() > 0) {
            landscapeReport.startShowMoreBlockDisappear("", "show all...");
            linesOfCodePerExtensionHide.forEach(extension -> {
                addLangInfo(extension);
            });
            landscapeReport.endShowMoreBlock();
        }
        landscapeReport.endDiv();
        String excludedExtensions = landscapeAnalysisResults.getConfiguration().getIgnoreExtensions().stream().collect(Collectors.joining(", "));
        if (StringUtils.isNotBlank(excludedExtensions)) {
            landscapeReport.addParagraph("The following extensions are configured not to be displayed: [" + excludedExtensions + "]", "margin-top: 12px; font-size: 70%; color: grey;");
        }
        landscapeReport.endSection();
    }

    private void addContributorsPerExtension(boolean linkCharts) {
        landscapeReport.startSubSection("Contributors per File Extensions (past 30 days)", "");
        if (linkCharts) {
            landscapeReport.startDiv("");
            landscapeReport.addNewTabLink("bubble chart", "visuals/bubble_chart_extensions_contributors_30d.html");
            landscapeReport.addHtmlContent(" | ");
            landscapeReport.addNewTabLink("tree map", "visuals/tree_map_extensions_contributors_30d.html");
            landscapeReport.addLineBreak();
            landscapeReport.addLineBreak();
            landscapeReport.endDiv();
        }
        landscapeReport.startDiv("");
        List<String> mainExtensions = getMainExtensions();
        List<CommitsPerExtension> contributorsPerExtension = landscapeAnalysisResults.getContributorsPerExtension()
                .stream().filter(c -> mainExtensions.contains(c.getExtension())).collect(Collectors.toList());
        Collections.sort(contributorsPerExtension, (a, b) -> b.getCommitters30Days().size() - a.getCommitters30Days().size());
        boolean tooLong = contributorsPerExtension.size() > 25;
        List<CommitsPerExtension> contributorsPerExtensionDisplay = tooLong ? contributorsPerExtension.subList(0, 25) : contributorsPerExtension;
        List<CommitsPerExtension> linesOfCodePerExtensionHide = tooLong ? contributorsPerExtension.subList(25, contributorsPerExtension.size()) : new ArrayList<>();
        contributorsPerExtensionDisplay.stream().filter(e -> e.getCommitters30Days().size() > 0).forEach(extension -> {
            addLangInfo(extension, (e) -> e.getCommitters30Days(), extension.getCommitsCount30Days(), DEVELOPER_SVG_ICON);
        });
        if (linesOfCodePerExtensionHide.stream().filter(e -> e.getCommitters30Days().size() > 0).count() > 0) {
            landscapeReport.startShowMoreBlockDisappear("", "show all...");
            linesOfCodePerExtensionHide.stream().filter(e -> e.getCommitters30Days().size() > 0).forEach(extension -> {
                addLangInfo(extension, (e) -> e.getCommitters30Days(), extension.getCommitsCount30Days(), DEVELOPER_SVG_ICON);
            });
            landscapeReport.endShowMoreBlock();
        }
        landscapeReport.endDiv();
        addContributorDependencies(contributorsPerExtension);
        landscapeReport.endSection();
    }

    private void addContributorDependencies(List<CommitsPerExtension> contributorsPerExtension) {
        Map<String, List<String>> contrExtMap = new HashMap<>();
        Set<String> extensionsNames = new HashSet<>();
        contributorsPerExtension.stream().filter(e -> e.getCommitters30Days().size() > 0).forEach(commitsPerExtension -> {
            String extensionDisplayLabel = commitsPerExtension.getExtension() + " (" + commitsPerExtension.getCommitters30Days().size() + ")";
            extensionsNames.add(extensionDisplayLabel);
            commitsPerExtension.getCommitters30Days().forEach(contributor -> {
                if (contrExtMap.containsKey(contributor)) {
                    contrExtMap.get(contributor).add(extensionDisplayLabel);
                } else {
                    contrExtMap.put(contributor, new ArrayList<>(Arrays.asList(extensionDisplayLabel)));
                }
            });
        });
        List<ComponentDependency> dependencies = new ArrayList<>();
        Map<String, ComponentDependency> dependencyMap = new HashMap<>();

        List<String> mainExtensions = getMainExtensions();
        contrExtMap.values().stream().filter(v -> v.size() > 1).forEach(extensions -> {
            extensions.stream().filter(extension1 -> mainExtensions.contains(extension1.replaceAll("\\(.*\\)", "").trim())).forEach(extension1 -> {
                extensions.stream().filter(extension2 -> mainExtensions.contains(extension2.replaceAll("\\(.*\\)", "").trim())).filter(extension2 -> !extension1.equalsIgnoreCase(extension2)).forEach(extension2 -> {
                    String key1 = extension1 + "::" + extension2;
                    String key2 = extension2 + "::" + extension1;

                    if (dependencyMap.containsKey(key1)) {
                        dependencyMap.get(key1).increment(1);
                    } else if (dependencyMap.containsKey(key2)) {
                        dependencyMap.get(key2).increment(1);
                    } else {
                        ComponentDependency dependency = new ComponentDependency(extension1, extension2);
                        dependencyMap.put(key1, dependency);
                        dependencies.add(dependency);
                    }
                });
            });
        });

        dependencies.forEach(dependency -> dependency.setCount(dependency.getCount() / 2));

        GraphvizDependencyRenderer renderer = new GraphvizDependencyRenderer();
        renderer.setMaxNumberOfDependencies(100);
        renderer.setDefaultNodeFillColor("deepskyblue2");
        renderer.setTypeGraph();
        String graphvizContent = renderer.getGraphvizContent(new ArrayList<>(extensionsNames), dependencies);

        landscapeReport.startShowMoreBlock("show extension dependencies...");
        landscapeReport.addGraphvizFigure("extension_dependencies_30d", "Extension dependencies", graphvizContent);
        chrome.addDownloadLinks("extension_dependencies_30d");
        landscapeReport.endShowMoreBlock();
        landscapeReport.addLineBreak();
        landscapeReport.addNewTabLink(" - show extension dependencies as 2D force graph&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "visuals/extension_dependencies_30d_force_2d.html");
        landscapeReport.addNewTabLink(" - show extension dependencies as 3D force graph&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "visuals/extension_dependencies_30d_force_3d.html");
        new Force3DGraphExporter().export2D3DForceGraph(dependencies, reportsFolder, "extension_dependencies_30d");
    }

    private List<String> getMainExtensions() {
        return landscapeAnalysisResults.getMainLinesOfCodePerExtension().stream()
                .map(l -> l.getName().replace("*.", "").trim()).collect(Collectors.toList());
    }

    private void addLangInfo(CommitsPerExtension extension, ExtractStringListValue<CommitsPerExtension> extractor, int commitsCount, String suffix) {
        int size = extractor.getValue(extension).size();
        String smallTextForNumber = FormattingUtils.getSmallTextForNumber(size) + suffix;
        infoBlocks.addLangInfoBlockExtra(smallTextForNumber, extension.getExtension().replace("*.", "").trim(),
                size + " " + (size == 1 ? "contributor" : "contributors (" + commitsCount + " commits)") + ":\n" +
                        extractor.getValue(extension).stream().limit(100)
                                .collect(Collectors.joining(", ")), FormattingUtils.getSmallTextForNumber(commitsCount) + " commits");
    }

    private void addLangInfo(NumericMetric extension) {
        String smallTextForNumber = FormattingUtils.getSmallTextForNumber(extension.getValue().intValue());
        int size = extension.getDescription().size();
        Collections.sort(extension.getDescription(), (a, b) -> b.getValue().intValue() - a.getValue().intValue());
        infoBlocks.addLangInfoBlock(smallTextForNumber, extension.getName().replace("*.", "").trim(),
                size + " " + (size == 1 ? "repository" : "repositories") + ":\n  " +
                        extension.getDescription().stream()
                                .map(a -> a.getName() + " (" + FormattingUtils.formatCount(a.getValue().intValue()) + " LOC)")
                                .collect(Collectors.joining("\n  ")));
    }
}
