/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.common.renderingutils.RichTextRenderingUtils;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.common.utils.RegexUtils;
import nl.obren.sokrates.reports.charts.SimpleOneBarChart;
import nl.obren.sokrates.reports.core.ReportConstants;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.dataexporters.DataExportUtils;
import nl.obren.sokrates.reports.dataexporters.DataExporter;
import nl.obren.sokrates.reports.utils.GraphvizDependencyRenderer;
import nl.obren.sokrates.reports.utils.ScopesRenderer;
import nl.obren.sokrates.sourcecode.SourceFileFilter;
import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.HistoryPerExtension;
import nl.obren.sokrates.sourcecode.analysis.results.LogicalDecompositionAnalysisResults;
import nl.obren.sokrates.sourcecode.aspects.*;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.dependencies.DependencyEvidence;
import nl.obren.sokrates.sourcecode.dependencies.DependencyUtils;
import nl.obren.sokrates.sourcecode.filehistory.FilePairChangedTogether;
import nl.obren.sokrates.sourcecode.filehistory.TemporalDependenciesHelper;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import nl.obren.sokrates.common.renderingutils.VisualizationTemplate;
import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.common.renderingutils.force3d.Force3DLink;
import nl.obren.sokrates.common.renderingutils.force3d.Force3DNode;
import nl.obren.sokrates.common.renderingutils.force3d.Force3DObject;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.githistory.ContributorPerExtensionStats;
import nl.obren.sokrates.sourcecode.landscape.ContributionCounter;
import nl.obren.sokrates.sourcecode.landscape.ContributorConnection;
import nl.obren.sokrates.sourcecode.landscape.ContributorConnectionUtils;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorConnections;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import java.io.File;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import nl.obren.sokrates.sourcecode.analysis.results.ConcernsAnalysisResults;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import nl.obren.sokrates.reports.utils.DataImageUtils;
import nl.obren.sokrates.sourcecode.IgnoredFilesGroup;
import nl.obren.sokrates.sourcecode.lang.LanguageAnalyzer;
import nl.obren.sokrates.sourcecode.lang.LanguageAnalyzerFactory;
import nl.obren.sokrates.reports.utils.FilesReportUtils;
import nl.obren.sokrates.reports.utils.PieChartUtils;
import nl.obren.sokrates.reports.utils.RiskDistributionStatsReportUtils;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.analysis.results.FilesHistoryAnalysisResults;
import nl.obren.sokrates.sourcecode.filehistory.FileHistoryComponentsHelper;
import nl.obren.sokrates.sourcecode.filehistory.FileHistoryUtils;
import nl.obren.sokrates.sourcecode.filehistory.FileModificationHistory;
import nl.obren.sokrates.sourcecode.stats.RiskDistributionStats;
import nl.obren.sokrates.sourcecode.stats.SourceFileAgeDistribution;
import nl.obren.sokrates.sourcecode.threshold.Thresholds;
import nl.obren.sokrates.common.utils.ProcessingStopwatch;
import nl.obren.sokrates.reports.landscape.utils.CorrelationDiagramGenerator;
import nl.obren.sokrates.sourcecode.analysis.results.FileAgeDistributionPerLogicalDecomposition;
import nl.obren.sokrates.sourcecode.aspects.LogicalDecomposition;
import nl.obren.sokrates.sourcecode.stats.SourceFileChangeDistribution;
import java.util.HashMap;
import java.util.Map;
import nl.obren.sokrates.reports.core.SummaryUtils;
import nl.obren.sokrates.reports.dataexporters.trends.ReferenceResultsLoader;
import nl.obren.sokrates.reports.utils.ReportUtils;
import nl.obren.sokrates.sourcecode.core.ReferenceAnalysisResult;
import nl.obren.sokrates.sourcecode.core.TrendAnalysisConfig;
import nl.obren.sokrates.sourcecode.metrics.Metric;
import java.util.Arrays;
import nl.obren.sokrates.sourcecode.analysis.results.FileDistributionPerLogicalDecomposition;
import nl.obren.sokrates.sourcecode.filehistory.CommitInfo;
import nl.obren.sokrates.sourcecode.stats.SourceFileSizeDistribution;
import java.util.stream.Stream;

public class LogicalComponentsReportGenerator {
    private CodeAnalysisResults codeAnalysisResults;
    private final boolean forceSkipStaticDependencies;
    private boolean elaborate = true;
    private RichTextReport report;
    private int dependencyVisualCounter = 1;
    private int graphCounter = 1;

    public LogicalComponentsReportGenerator(CodeAnalysisResults codeAnalysisResults, boolean forceSkipStaticDependencies) {
        this.codeAnalysisResults = codeAnalysisResults;
        this.forceSkipStaticDependencies = forceSkipStaticDependencies;
    }

    public void addCodeOrganizationToReport(RichTextReport report) {
        this.report = report;
        addSummary();
        // addErrors();
        addFooter();
    }

    private void addFooter() {
        report.addLineBreak();
        report.addHorizontalLine();
        report.addParagraph(RichTextRenderingUtils.italic(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())));
    }

    private void addSummary() {
        if (forceSkipStaticDependencies) {
            report.addParagraph("An overview of source code logical components.", "margin-top: 12px; color: grey");
        } else {
            report.addParagraph("An overview of static code dependencies among source code logical components.", "margin-top: 12px; color: grey");
        }
        report.startSection("Intro", "");
        if (elaborate) {
            appendIntroduction();
        }
        report.endSection();

        report.startSection("Logical Decompositions Overview", "");
        List<LogicalDecompositionAnalysisResults> logicalDecompositionsAnalysisResults = codeAnalysisResults.getLogicalDecompositionsAnalysisResults();
        int size = logicalDecompositionsAnalysisResults.size();
        report.addParagraph("Analyzed system has <b>" + size + "</b> logical decomposition" + (size > 1 ? "s" : "") + ":");
        report.startUnorderedList();
        logicalDecompositionsAnalysisResults.forEach(logicalDecomposition -> {
            int componentsCount = logicalDecomposition.getComponents().size();
            report.addListItem(logicalDecomposition.getLogicalDecomposition().getName() + " (" + componentsCount + " component" + (componentsCount > 1 ? "s" : "") + ")");
        });
        report.endUnorderedList();
        report.endSection();

        report.startTabGroup();
        boolean active[] = {true};
        logicalDecompositionsAnalysisResults.forEach(logicalDecomposition -> {
            LogicalDecomposition decomposition = logicalDecomposition.getLogicalDecomposition();
            report.addTab(decomposition.getName(), decomposition.getName(), active[0]);
            active[0] = false;
        });
        report.endTabGroup();

        int[] sectionIndex = {1};
        active[0] = true;
        logicalDecompositionsAnalysisResults.forEach(logicalDecomposition -> {
            report.startTabContentSection(logicalDecomposition.getLogicalDecomposition().getName(), active[0]);
            analyzeLogicalDecomposition(sectionIndex[0], logicalDecomposition);
            active[0] = false;
            sectionIndex[0]++;
            report.endTabContentSection();
        });
        report.endSection();
    }

    private void analyzeLogicalDecomposition(int sectionIndex, LogicalDecompositionAnalysisResults logicalDecomposition) {
        report.addLevel2Header("Logical Decomposition #" + sectionIndex + ": " + logicalDecomposition.getKey().toUpperCase(), "margin-bottom: 0;");
        report.addParagraph(getDecompositionDescription(logicalDecomposition), "color: grey; margin-bottom: 24px;");
        report.startDiv("margin-top: -8px; margin-bottom: 18px;");
        report.addHtmlContent("<a target='_blank' href='visuals/bubble_chart_components_" + (sectionIndex) + ".html'>Bubble Chart</a> | ");
        report.addHtmlContent("<a target='_blank' href='visuals/tree_map_components_" + (sectionIndex) + ".html'>Tree Map</a>");
        report.endDiv();

        List<NumericMetric> fileCountPerComponent = logicalDecomposition.getFileCountPerComponent();
        List<NumericMetric> linesOfCodePerComponent = logicalDecomposition.getLinesOfCodePerComponent();

        ScopesRenderer renderer = new ScopesRenderer();
        renderer.setLinesOfCodeInMain(codeAnalysisResults.getMainAspectAnalysisResults().getLinesOfCode());

        renderer.setTitle("Component Sizes (Lines of Code)");
        renderer.setDescription("");
        renderer.setFileCountPerComponent(fileCountPerComponent);

        renderer.setLinesOfCode(linesOfCodePerComponent);
        renderer.setMaxFileCount(codeAnalysisResults.getMaxFileCount());
        renderer.setMaxLinesOfCode(codeAnalysisResults.getMaxLinesOfCode());
        List<AspectAnalysisResults> components = logicalDecomposition.getComponents();
        String filePathPrefix = DataExportUtils.getComponentFilePrefix(logicalDecomposition.getKey());
        renderer.setAspectsFileListPaths(components.stream().map(aspect -> aspect.getAspect().getFileSystemFriendlyName(filePathPrefix)).collect(Collectors.toList()));
        renderer.renderReport(report, "The \"" + logicalDecomposition.getLogicalDecomposition().getName() + "\" logical decomposition has <b>" + logicalDecomposition.getLogicalDecomposition().getComponents().size() + "</b> components.");

        List<MetaRule> metaComponents = logicalDecomposition.getLogicalDecomposition().getMetaComponents();
        if (metaComponents.size() > 0) {
            report.startSubSection("Meta-Rules for Componentization", "");
            report.addListItem("The following explicit meta-rules for components are defined:");
            describeMetaRules(metaComponents);
            report.endSection();
        }

        if (!forceSkipStaticDependencies) {
            addStaticDependencies(logicalDecomposition);
        } else {
            if (codeAnalysisResults.getContributorsAnalysisResults().getCommitsCount() > 0) {
                report.startSubSection("Component Commits", "Components ordered by number of commits");
                addCommitsSection(logicalDecomposition);
                addCommitsTrendSection(sectionIndex, logicalDecomposition.getKey());
                addTemporalDependenciesSection(logicalDecomposition);
                report.endSection();
            }
        }

    }

    private void addStaticDependencies(LogicalDecompositionAnalysisResults logicalDecomposition) {
        List<ComponentDependency> componentDependencies = logicalDecomposition.getComponentDependencies();
        report.startSubSection("Static Dependencies", "Dependencies among components are <b>static</b> code dependencies among files in different components.");
        if (componentDependencies != null && componentDependencies.size() > 0) {
            addComponentDependenciesSection(logicalDecomposition, componentDependencies);
        } else {
            report.addParagraph("No component dependencies found.");
        }
        report.endSection();
    }

    private void addTemporalDependenciesSection(LogicalDecompositionAnalysisResults logicalDecomposition) {
        List<FilePairChangedTogether> filePairsChangedTogether180Days = codeAnalysisResults.getFilesHistoryAnalysisResults().getFilePairsChangedTogether180Days();

        addDependenciesSection(filePairsChangedTogether180Days);
    }

    private void addDependenciesSection(List<FilePairChangedTogether> filePairsChangedTogether) {
        report.startDiv("margin: 10px;");
        codeAnalysisResults.getLogicalDecompositionsAnalysisResults().forEach(logicalDecompositionAnalysisResults -> {
            String logicalDecompositionKey = logicalDecompositionAnalysisResults.getKey();

            report.startSubSection("Dependencies between components in same commits (past 180 days)",
                    "The number on the lines shows the number of shared commits.");

            report.addNewTabLink("See detailed temporal dependencies report...", "FileTemporalDependencies.html");
            report.addLineBreak();
            report.addLineBreak();
            TemporalDependenciesHelper helper = new TemporalDependenciesHelper();
            List<ComponentDependency> componentDependencies = helper.extractComponentDependencies(logicalDecompositionKey, filePairsChangedTogether);
            renderComponentDependencies(report, componentDependencies);

            report.endSection();
        });
        report.endDiv();
    }

    private void renderComponentDependencies(RichTextReport report, List<ComponentDependency> dependencies) {
        if (dependencies.size() > 0) {
            GraphvizDependencyRenderer graphvizDependencyRenderer = new GraphvizDependencyRenderer();
            graphvizDependencyRenderer.setDefaultNodeFillColor("deepskyblue2");
            graphvizDependencyRenderer.setType("graph");
            graphvizDependencyRenderer.setArrow("--");
            graphvizDependencyRenderer.setArrowColor("#00688b");
            graphvizDependencyRenderer.setCyclicArrowColor("#a0a0a0");
            graphvizDependencyRenderer.setMaxNumberOfDependencies(50);
            String graphvizContent = graphvizDependencyRenderer.getGraphvizContent(new ArrayList<>(), dependencies);

            String graphId = "logical_decomposition_file_changed_together_dependencies_" + graphCounter++;
            report.addGraphvizFigure(graphId, "File changed together in different components", graphvizContent);
        } else {
            report.addParagraph("No temporal dependencies found.");
        }
    }

    private void addCommitsSection(LogicalDecompositionAnalysisResults logicalDecomposition) {
        List<NumericMetric> fileCountPerComponent = logicalDecomposition.getFileCountPerComponent();
        List<NumericMetric> linesOfCodePerComponent = new CommitTrendsExtractors(codeAnalysisResults).getTotalCommits(logicalDecomposition.getKey());
        ScopesRenderer renderer = new ScopesRenderer();
        renderer.setLinesOfCodeInMain(codeAnalysisResults.getContributorsAnalysisResults().getCommitsCount());

        renderer.setTitle("Total Commits per Component");
        renderer.setDescription("");
        renderer.setMetric("commits");
        renderer.setDescribe(false);
        renderer.setActiveColor("grey");
        renderer.setFileCountPerComponent(fileCountPerComponent);

        renderer.setLinesOfCode(linesOfCodePerComponent);
        renderer.setMaxFileCount(codeAnalysisResults.getMaxFileCount());
        renderer.setMaxLinesOfCode(codeAnalysisResults.getContributorsAnalysisResults().getCommitsCount());
        List<AspectAnalysisResults> components = logicalDecomposition.getComponents();
        String filePathPrefix = DataExportUtils.getComponentFilePrefix(logicalDecomposition.getKey());
        renderer.setAspectsFileListPaths(components.stream().map(aspect -> aspect.getAspect().getFileSystemFriendlyName(filePathPrefix)).collect(Collectors.toList()));
        renderer.renderReport(report, "All commits, some commits may include files from multiple components.");
    }

    private void addCommitsTrendSection(int sectionIndex, String key) {
        report.startSubSection("Yearly File Updates Trend per Components", "The number of file changes in commits");
        report.addContentInDiv(ReportConstants.ANIMATION_SVG_ICON, "display: inline-block; vertical-align: middle; margin: 4px;");
        report.addHtmlContent("animated commit history: ");
        report.addNewTabLink("all time cumulative", "visuals/racing_charts_component_commits_" + sectionIndex + ".html?tickDuration=600");
        report.addHtmlContent(" | ");
        report.addNewTabLink("12 months window", "visuals/racing_charts_component_commits_12_months_window_" + sectionIndex + ".html?tickDuration=600");

        report.startTable();
        report.startTableRow();
        report.startTableCell("border: none");
        List<HistoryPerExtension> historyPerExtensionPerYear = new ArrayList<>();
        Map<String, Map<String, Integer>> commitsPerYear = new CommitTrendsExtractors(codeAnalysisResults).getCommitsPerYear(key);
        commitsPerYear.keySet().forEach(component -> {
            Map<String, Integer> componentYears = commitsPerYear.get(component);
            componentYears.keySet().forEach(year -> {
                int count = componentYears.get(year);
                historyPerExtensionPerYear.add(new HistoryPerExtension(component, year, count));
            });
        });
        List<String> componentNames = new ArrayList<>(commitsPerYear.keySet());
        HistoryPerLanguageGenerator.getInstanceCommits(historyPerExtensionPerYear, componentNames).addHistoryPerComponent(report);
        report.endTableCell();
        report.endTableRow();
        report.endTable();

        report.addLineBreak();
        report.addLineBreak();
        report.endSection();
    }

    private void addComponentDependenciesSection(LogicalDecompositionAnalysisResults logicalDecomposition, List<ComponentDependency> componentDependencies) {
        report.startUnorderedList();
        report.addListItem("Analyzed system has <b>" + componentDependencies.size() + "</b> links (arrows) between components.");
        report.addListItem("The number on the arrow represents the number of files from referring component that depend on files in referred component.");
        report.addListItem("These " + componentDependencies.size() + " links contain <a href='../data/text/" + DataExporter.dependenciesFileNamePrefix("", "", logicalDecomposition.getKey()) + ".txt'><b>" + DependencyUtils.getDependenciesCount(componentDependencies) + "</b> dependencies</a>.");
        int cyclicDependencyPlacesCount = DependencyUtils.getCyclicDependencyPlacesCount(componentDependencies);
        int cyclicDependencyCount = DependencyUtils.getCyclicDependencyCount(componentDependencies);
        if (cyclicDependencyPlacesCount > 0) {
            String numberOfPlacesText = cyclicDependencyPlacesCount == 1
                    ? "is <b>1</b> place"
                    : "are <b>" + cyclicDependencyPlacesCount + "</b> places";
            report.addListItem("There " + numberOfPlacesText + " (" + (cyclicDependencyPlacesCount * 2) + " links) with <b>cyclic</b> dependencies (<b>" + cyclicDependencyCount + "</b> " +
                    "file dependencies).");
        }

        describeDependencyFinder(logicalDecomposition);

        report.endUnorderedList();

        List<String> componentNames = new ArrayList<>();
        logicalDecomposition.getComponents().forEach(c -> componentNames.add(c.getName()));
        GraphvizDependencyRenderer graphvizDependencyRenderer = new GraphvizDependencyRenderer();
        RenderingOptions renderingOptions = logicalDecomposition.getLogicalDecomposition().getRenderingOptions();
        graphvizDependencyRenderer.setMaxNumberOfDependencies(renderingOptions.getMaxNumberOfDependencies());
        graphvizDependencyRenderer.setOrientation(renderingOptions.getOrientation());
        graphvizDependencyRenderer.setReverseDirection(renderingOptions.isReverseDirection());

        boolean renderWithoutDependencies = renderingOptions.isRenderComponentsWithoutDependencies();
        int linkThreshold = logicalDecomposition.getLogicalDecomposition().getDependencyLinkThreshold();
        List<ComponentDependency> dependenciesAboveThreshold = componentDependencies.stream().filter(d -> d.getCount() >= linkThreshold).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> componentsAboveThreshold = componentNames.stream()
                .filter(c -> renderWithoutDependencies || isComponentInDependency(dependenciesAboveThreshold, c))
                .collect(Collectors.toCollection(ArrayList::new));
        List<ComponentGroup> componentGroups = getComponentGroups(logicalDecomposition, dependenciesAboveThreshold, componentsAboveThreshold);
        String graphId = addDependencyGraphVisuals(dependenciesAboveThreshold, componentsAboveThreshold, componentGroups, graphvizDependencyRenderer);
        report.addLineBreak();
        report.addNewTabLink("Open 2D force graph...", "visuals/force_2d_" + graphId + ".html");
        report.addLineBreak();
        report.addNewTabLink("Open 3D force graph...", "visuals/force_3d_" + graphId + ".html");

        if (componentGroups.size() > 0) {
            report.addLevel4Header("Group Dependencies");
            List<ComponentDependency> groupDependencies = getGroupDependencies(dependenciesAboveThreshold, componentGroups);
            addDependencyGraphVisuals(groupDependencies,
                    componentsAboveThreshold.stream().filter(c -> c.equalsIgnoreCase(getGroup(c, componentGroups))).collect(Collectors.toCollection(ArrayList::new)),
                    new ArrayList<>(), graphvizDependencyRenderer);
            report.addLineBreak();
        }

        report.addLineBreak();
        report.addLineBreak();
        addMoreDetailsSection(logicalDecomposition, componentDependencies);
        report.addLineBreak();
        report.addLineBreak();

        renderIndirectDependencies(componentNames, graphvizDependencyRenderer, renderingOptions, renderWithoutDependencies, dependenciesAboveThreshold);
        report.addLineBreak();
        report.addLineBreak();
        report.addLineBreak();
    }

    private List<ComponentGroup> getComponentGroups(LogicalDecompositionAnalysisResults logicalDecomposition, List<ComponentDependency> dependencies, List<String> components) {
        List<ComponentGroup> componentGroups = new ArrayList<>();
        List<String> allNames = new ArrayList<>();
        Consumer<String> stringConsumer = component -> {
            if (!allNames.contains(component)) {
                allNames.add(component);
            }
        };
        components.forEach(stringConsumer);
        dependencies.forEach(dependency -> {
            stringConsumer.accept(dependency.getFromComponent());
            stringConsumer.accept(dependency.getToComponent());
        });

        List<String> componentsInAnyGroup = new ArrayList<>();
        logicalDecomposition.getLogicalDecomposition().getGroups().forEach(groupingRule -> {
            List<String> componentsInGroupFiltered = allNames.stream().filter(name -> RegexUtils.matchesEntirely(groupingRule.getPattern(), name)).collect(Collectors.toCollection(ArrayList::new));
            List<String> componentsInGroup = new ArrayList<>();
            componentsInGroupFiltered.forEach(c -> {
                if (!componentsInAnyGroup.contains(c)) {
                    componentsInGroup.add(c);
                    componentsInAnyGroup.add(c);
                }
            });
            if (componentsInGroup.size() > 0) {
                componentGroups.add(new ComponentGroup(groupingRule.getName(), componentsInGroup));
            }
        });

        return componentGroups;
    }

    private List<ComponentDependency> getGroupDependencies(List<ComponentDependency> dependencies, List<ComponentGroup> componentGroups) {
        List<ComponentDependency> groupDependencies = new ArrayList<>();
        Map<String, ComponentDependency> groupDependenciesMap = new HashMap<>();

        dependencies.forEach(dependency -> {
            String from = getGroup(dependency.getFromComponent(), componentGroups);
            String to = getGroup(dependency.getToComponent(), componentGroups);

            String key1 = from + "::" + to;
            String key2 = to + "::" + from;

            if (groupDependenciesMap.containsKey(key1)) {
                groupDependenciesMap.get(key1).setCount(groupDependenciesMap.get(key1).getCount() + dependency.getCount());
            } else if (groupDependenciesMap.containsKey(key2)) {
                groupDependenciesMap.get(key2).setCount(groupDependenciesMap.get(key1).getCount() + dependency.getCount());
            } else {
                ComponentDependency newDependency = new ComponentDependency(from, to);
                newDependency.setCount(dependency.getCount());
                groupDependencies.add(newDependency);
                groupDependenciesMap.put(key1, newDependency);
            }
        });

        return groupDependencies;
    }

    private String getGroup(String component, List<ComponentGroup> componentGroups) {
        for (ComponentGroup componentGroup : componentGroups) {
            if (componentGroup.getComponentNames().contains(component)) {
                return componentGroup.getName() + " (" + componentGroup.getComponentNames().size() + ")";
            }
        }

        return component;
    }

    private void renderIndirectDependencies(List<String> componentNames, GraphvizDependencyRenderer graphvizDependencyRenderer, RenderingOptions renderingOptions, boolean renderWithoutDependencies, List<ComponentDependency> dependencies) {
        if (renderingOptions.isRenderIndirectDependencies()) {
            report.startSubSection("Indirect Dependencies", "Dependecies via shared target or source components.");
            renderIndirectDependencies("Shared Targets", componentNames, graphvizDependencyRenderer, renderWithoutDependencies, getIndirectViaTargetsDependencies(dependencies));
            renderIndirectDependencies("Shared Sources", componentNames, graphvizDependencyRenderer, renderWithoutDependencies, getIndirectViaSourcesDependencies(dependencies));

            if (renderingOptions.isRenderInternalIndirectDependencies()) {
                renderInternalsOfIndirectDependencies("Targets", componentNames, graphvizDependencyRenderer, renderWithoutDependencies, getDependenciesWithSharedTargets(dependencies));
                renderInternalsOfIndirectDependencies("Sources", componentNames, graphvizDependencyRenderer, renderWithoutDependencies, getDependenciesWithSharedSources(dependencies));
            }
            report.endSection();
        }
    }

    private void renderIndirectDependencies(String type, List<String> componentNames, GraphvizDependencyRenderer graphvizDependencyRenderer, boolean renderWithoutDependencies, List<ComponentDependency> indirectDependencies) {
        if (indirectDependencies.size() > 0) {
            graphvizDependencyRenderer.setType("graph");
            graphvizDependencyRenderer.setArrow("--");

            report.addLevel3Header("Indirect Dependencies (" + type + ")");
            report.startShowMoreBlock("show details...");
            report.addParagraph("Dependencies via " + type.toLowerCase() + "  components.", "color: grey");
            addDependencyGraphVisuals(indirectDependencies, componentNames.stream()
                    .filter(c -> renderWithoutDependencies || isComponentInDependency(indirectDependencies, c))
                    .collect(Collectors.toCollection(ArrayList::new)), new ArrayList<>(), graphvizDependencyRenderer);

            report.endShowMoreBlock();
            report.addLineBreak();
            report.addLineBreak();
            report.addLineBreak();
        }
    }

    private void renderInternalsOfIndirectDependencies(String type, List<String> componentNames, GraphvizDependencyRenderer graphvizDependencyRenderer, boolean renderWithoutDependencies, List<ComponentDependency> sharedTargetDependencies) {
        if (sharedTargetDependencies.size() > 0) {
            graphvizDependencyRenderer.setType("digraph");
            graphvizDependencyRenderer.setArrow("->");
            report.addLevel3Header("Indirect Dependencies With Shared " + type + " Visible");
            report.startShowMoreBlock("show details...");
            report.addParagraph("Shared target components made visible.", "color: grey");
            addDependencyGraphVisuals(sharedTargetDependencies, componentNames.stream()
                    .filter(c -> renderWithoutDependencies || isComponentInDependency(sharedTargetDependencies, c))
                    .collect(Collectors.toCollection(ArrayList::new)), new ArrayList<>(), graphvizDependencyRenderer);
            report.endShowMoreBlock();
            report.addLineBreak();
            report.addLineBreak();
            report.addLineBreak();
        }
    }

    private List<ComponentDependency> getIndirectViaTargetsDependencies(List<ComponentDependency> dependencies) {
        ArrayList<ComponentDependency> indirect = new ArrayList<>();
        Map<String, ComponentDependency> map = new HashMap<>();

        List<String> alreadyCovered = new ArrayList<>();

        getMergedDependencies(dependencies).forEach(dependency1 -> {
            dependencies.stream()
                    .filter(dependency2 -> !dependency1.getFromComponent().equals(dependency2.getFromComponent()))
                    .filter(dependency2 -> dependency1.getToComponent().equalsIgnoreCase(dependency2.getToComponent()))
                    .forEach(dependency2 -> {
                        String coverKey1 = dependency1.getFromComponent() + " --" + dependency1.getToComponent() + "--> " + dependency2.getFromComponent();
                        String coverKey2 = dependency2.getFromComponent() + " --" + dependency1.getToComponent() + "--> " + dependency1.getFromComponent();
                        if (!(alreadyCovered.contains(coverKey1) || alreadyCovered.contains(coverKey2))) {
                            alreadyCovered.add(coverKey1);
                            addIndirectInternalDependency(indirect, map, dependency1.getFromComponent(), dependency2.getFromComponent());
                        }
                    });
        });

        return indirect;
    }

    private List<ComponentDependency> getMergedDependencies(List<ComponentDependency> dependencies) {
        Map<String, ComponentDependency> mergeMap = new HashMap<>();
        List<ComponentDependency> mergedDependencies = new ArrayList<>();
        dependencies.forEach(dependency -> {
            String key = dependency.getFromComponent() + " -> " + dependency.getToComponent();
            if (mergeMap.containsKey(key)) {
                mergeMap.get(key).increment(dependency.getCount());
            } else {
                ComponentDependency newDependency = new ComponentDependency();
                newDependency.setFromComponent(dependency.getFromComponent());
                newDependency.setToComponent(dependency.getToComponent());
                newDependency.setCount(1);
                mergedDependencies.add(newDependency);
                mergeMap.put(key, newDependency);
            }
        });
        return mergedDependencies;
    }

    private List<ComponentDependency> getIndirectViaSourcesDependencies(List<ComponentDependency> dependencies) {
        ArrayList<ComponentDependency> indirect = new ArrayList<>();
        Map<String, ComponentDependency> map = new HashMap<>();

        List<String> alreadyCovered = new ArrayList<>();

        getMergedDependencies(dependencies).forEach(dependency1 -> {
            dependencies.stream()
                    .filter(dependency2 -> !dependency1.getToComponent().equals(dependency2.getToComponent()))
                    .filter(dependency2 -> dependency1.getFromComponent().equalsIgnoreCase(dependency2.getFromComponent()))
                    .forEach(dependency2 -> {
                        String coverKey1 = dependency1.getToComponent() + " --" + dependency1.getFromComponent() + "--> " + dependency2.getToComponent();
                        String coverKey2 = dependency2.getToComponent() + " --" + dependency1.getFromComponent() + "--> " + dependency1.getToComponent();
                        if (!(alreadyCovered.contains(coverKey1) || alreadyCovered.contains(coverKey2))) {
                            alreadyCovered.add(coverKey1);
                            addIndirectInternalDependency(indirect, map, dependency1.getToComponent(), dependency2.getToComponent());
                        }
                    });
        });

        return indirect;
    }

    private void addIndirectInternalDependency(ArrayList<ComponentDependency> indirect, Map<String, ComponentDependency> map, String component1, String component2) {
        String key1 = component1 + "::" + component2;
        String key2 = component2 + "::" + component1;

        if (map.containsKey(key1)) {
            map.get(key1).increment(1);
        } else if (map.containsKey(key2)) {
            map.get(key2).increment(1);
        } else {
            ComponentDependency indirectDependency = new ComponentDependency(component1, component2);
            map.put(key1, indirectDependency);
            indirect.add(indirectDependency);
        }
    }

    private List<ComponentDependency> getDependenciesWithSharedTargets(List<ComponentDependency> dependencies) {
        Map<String, Set<String>> map = new HashMap<>();
        dependencies.forEach(d -> {
            String to = d.getToComponent();
            if (!map.containsKey(to)) {
                map.put(to, new HashSet<>());
            }
            map.get(to).add(d.getFromComponent());
        });
        return dependencies.stream().filter(d -> map.get(d.getToComponent()).size() > 1).collect(Collectors.toCollection(ArrayList::new));
    }

    private List<ComponentDependency> getDependenciesWithSharedSources(List<ComponentDependency> dependencies) {
        Map<String, Set<String>> map = new HashMap<>();
        dependencies.forEach(d -> {
            String from = d.getFromComponent();
            if (!map.containsKey(from)) {
                map.put(from, new HashSet<>());
            }
            map.get(from).add(d.getToComponent());
        });
        return dependencies.stream().filter(d -> map.get(d.getFromComponent()).size() > 1).collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean isComponentInDependency(List<ComponentDependency> dependencies, String component) {
        for (ComponentDependency dependency : dependencies) {
            if (dependency.getFromComponent().equalsIgnoreCase(component)) {
                return true;
            }
            if (dependency.getToComponent().equalsIgnoreCase(component)) {
                return true;
            }
        }

        return false;
    }

    private String addDependencyGraphVisuals(List<ComponentDependency> componentDependencies, List<String> componentNames, List<ComponentGroup> componentGroups, GraphvizDependencyRenderer graphvizDependencyRenderer) {
        String graphvizContent = graphvizDependencyRenderer.getGraphvizContent(componentNames, componentDependencies, componentGroups);
        String graphId = "dependencies_" + dependencyVisualCounter++;
        report.startDiv("max-height: 600px; overflow-y: scroll; overflow-x: scroll;");
        report.addGraphvizFigure(graphId, "", graphvizContent);
        report.endDiv();
        report.addLineBreak();
        report.addLineBreak();
        VisualizationTools.addDownloadLinks(report, graphId);

        return graphId;
    }

    private void addMoreDetailsSection(LogicalDecompositionAnalysisResults logicalDecomposition, List<ComponentDependency> componentDependencies) {
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

    private void describeDependencyFinder(LogicalDecompositionAnalysisResults logicalDecomposition) {
        List<DependencyFinderPattern> rules = logicalDecomposition.getLogicalDecomposition().getDependenciesFinder().getRules();
        if (rules.size() > 0) {
            report.addListItem("The following explicit rules for finding dependencies are defined:");
            report.startUnorderedList();
            rules.forEach(rule -> {
                SourceFileFilter filter = new SourceFileFilter(rule.getPathPattern(), rule.getContentPattern());
                report.addListItem("\"" + rule.getComponent() + "\" <== " + filter.toString());
            });
            report.endUnorderedList();
        }
        List<? extends MetaRule> metaRules = logicalDecomposition.getLogicalDecomposition().getDependenciesFinder().getMetaRules();
        if (metaRules.size() > 0) {
            report.addListItem("The following explicit meta-rules for finding dependencies are defined:");
            describeMetaRules(metaRules);
        }
    }

    private void describeMetaRules(List<? extends MetaRule> metaRules) {
        report.startUnorderedList();
        metaRules.forEach(rule -> {
            SourceFileFilter filter = new SourceFileFilter(rule.getPathPattern(), rule.getContentPattern());
            report.addListItem(filter.toString());
            report.startUnorderedList();
            rule.getNameOperations().forEach(op -> {
                StringBuilder params = new StringBuilder();
                op.getParams().forEach(param -> {
                    if (params.length() > 0) params.append(", ");
                    params.append("\"" + param + "\"");
                });
                report.addListItem(op.getOp() + " (" + params.toString() + ")");
            });
            report.endUnorderedList();
        });
        report.endUnorderedList();
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

    private String getDecompositionDescription(LogicalDecompositionAnalysisResults logicalDecomposition) {
        int numberOfComponents = logicalDecomposition.getComponents().size();
        StringBuilder description = new StringBuilder();
        LogicalDecomposition definition = logicalDecomposition.getLogicalDecomposition();
        if (definition.getComponentsFolderDepth() > 0) {
            if (definition.getMinComponentsCount() <= 1) {
                description.append("The decompositions is based on the folder structure at <b>level " + definition.getComponentsFolderDepth() + "</b> (relative to the source code root).");
            } else {
                description.append("The decompositions is based on the folder structure (relative to the source code root), with automatically defined folder depth to have at least " + definition.getMinComponentsCount() + " components.");
            }
        } else if (logicalDecomposition.getComponents() != null && numberOfComponents > 0) {
            description.append("The \"" + definition.getName() + "\" logical decomposition in based on <b>" + numberOfComponents + "</b> explicitly defined components.");
        }
        return description.toString();
    }

    private void appendIntroduction() {
        report.addParagraph(getShortIntro());
        report.addHtmlContent(getLongIntro());

        if (!forceSkipStaticDependencies) {
            report.startShowMoreBlock("Learn more...");
            report.startUnorderedList();
            report.addListItem("To learn more about good practices on componentization and dependencies, Sokrates recommends the following resources:");
            report.startUnorderedList();
            report.addListItem("<a target='_blank' href='https://www.martinfowler.com/ieeeSoftware/coupling.pdf'>Reduce Coupling</a>, MartinFlower.com (IEEE Software article)");
            report.addListItem("<a target='_blank' href='https://sourcemaking.com/refactoring/smells/couplers'>Couplers Code Smells</a>, sourcemaking.com");
            report.endUnorderedList();
            report.endUnorderedList();
        }

        report.endShowMoreBlock();
    }

    private String getShortIntro() {
        String shortIntro = "";
        if (forceSkipStaticDependencies) {
            shortIntro += "<b>Logical decomposition</b> is a representation of the organization of the <b>main</b> source code, where every and each file is put in exactly one <b>logical component</b>.";
        } else {
            shortIntro = "<b>Static code dependencies</b> refer to the relationships between different modules, libraries, or components in a software system that are established at compile-time rather than at runtime. These dependencies are determined by the code itself, meaning that one module directly references another through imports, includes, or explicit references in the code. Examples include:";
        }
        return shortIntro;
    }

    private String getLongIntro() {
        String longIntro = "";
        if (forceSkipStaticDependencies) {
            longIntro = "<ul>\n";
            longIntro += "<li>A software system can have <b>one</b> or <b>more</b> logical decompositions.</li>\n";
            longIntro += "<li>A logical decomposition can be defined in two ways in Sokrates.</li>\n";
            longIntro += "<li>First approach is based on the <b>folders structure</b>. " +
                    "Components are mapped to folders at defined <b>folder depth</b> relative to the source code root.</li>\n";
            longIntro += "<li>Second approach is based on <b>explicit</b> definition of each component. In such explicit definitions, components are explicitly <b>named</b> and their files are selected based on explicitly defined path and content <b>filters</b>.</li>\n";
            longIntro += "<li>A logical decomposition is considered <b>invalid</b> if a file is selected into <b>two or more components</b>. " +
                    "This constraint is introduced in order to facilitate measuring of <b>dependencies</b> among components.</li>\n";
            longIntro += "<li>Files not assigned to any component are put into a special \"<b>Unclassified</b>\" component.</li>\n";

            longIntro += "</ul>\n";
        } else {
            longIntro = "<ul>\n";
            longIntro += "<li>Library and Module Imports</li>\n";
            longIntro += "<li>Class Dependencies (Inheritance & Composition)</li>\n";
            longIntro += "<li>Function and Method Calls</li>\n";
            longIntro += "<li>Dependency Injection Without Runtime Resolution</li>\n";

            longIntro += "</ul>\n";
        }

        return longIntro;
    }


}

class ContributorsReportGenerator {
    private final CodeAnalysisResults codeAnalysisResults;
    private int dependencyVisualCounter = 1;
    private File reportsFolder;
    private RichTextReport report;
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


        report.addParagraph("An overview of contributor trends.", "margin-top: 12px; color: grey");

        report.startTabGroup();
        report.addTab("matrix", "Contributors Matrix", true);
        report.addTab("30_days", "Past 30 Days", false);
        report.addTab("90_days", "Past 3 Months", false);
        report.addTab("180_days", "Past 6 Months", false);
        report.addTab("365_days", "Past Year", false);
        report.addTab("contributors", "Overview", false);
        report.addTab("data", "Data", false);
        report.endTabGroup();

        ContributorsAnalysisResults analysis = codeAnalysisResults.getContributorsAnalysisResults();
        List<Contributor> contributors = analysis.getContributors();

        List<Contributor> people = contributors.stream().filter(c -> !c.isBot()).collect(Collectors.toList());
        List<Contributor> bots = contributors.stream().filter(c -> c.isBot()).collect(Collectors.toList());

        report.startTabContentSection("contributors", false);
        addZoomableCircleLinks(report);
        ContributorsReportUtils.addContributorsSection(codeAnalysisResults, report);
        report.endTabContentSection();

        report.startTabContentSection("matrix", true);
        addMatrix(new ArrayList<>(people), "Contributors");
        addMatrix(new ArrayList<>(bots), "Bots");
        report.endTabContentSection();

        report.startTabContentSection("30_days", false);
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
        report.endTabContentSection();

        report.startTabContentSection("90_days", false);
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
        report.endTabContentSection();

        report.startTabContentSection("180_days", false);
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
        report.endTabContentSection();

        report.startTabContentSection("365_days", false);
        List<Contributor> commits365Days = contributors.stream().filter(c -> c.getCommitsCount365Days() > 0).collect(Collectors.toList());
        commits365Days.sort((a, b) -> b.getCommitsCount365Days() - a.getCommitsCount365Days());
        List<Contributor> peopleCommits365Days = people.stream().filter(c -> c.getCommitsCount365Days() > 0).collect(Collectors.toList());
        List<Contributor> botCommits365Days = bots.stream().filter(c -> c.getCommitsCount365Days() > 0).collect(Collectors.toList());
        commits365Days.sort((a, b) -> b.getCommitsCount365Days() - a.getCommitsCount365Days());
        addContributorsPanel(report, peopleCommits365Days, c -> c.getCommitsCount365Days(), true, null, "Contributor");
        addContributorsPanel(report, botCommits365Days, c -> c.getCommitsCount365Days(), true, null, "Bot");
        renderPeopleDependencies(analysis.getPeopleDependencies365Days(), analysis.getPeopleFileDependencies365Days(), 365, c -> c.getCommitsCount365Days(), commits365Days);
        report.endTabContentSection();

        report.startTabContentSection("data", false);
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("Contributors' details...", "../data/text/contributors.txt");
        report.endListItem();
        report.endUnorderedList();
        report.endTabContentSection();
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


    private void addMatrix(List<Contributor> contributors, String type) {
        report.addContentInDiv("&nbsp;", "height: 20px");
        report.startSubSection(type + " Matrix (Per Month)", "");
        report.startDiv("width: 100%; overflow-x: scroll; overflow-y: scroll; max-height: 600px");
        report.startTable();

        final List<String> pastMonths = DateUtils.getPastMonths(24, DateUtils.getAnalysisDate());
        report.startTableRow();
        report.addTableCell("", "min-width: 200px; border: none; border: none");
        report.addTableCell("Commits<br>(3m)", "max-width: 100px; text-align: center; border: none");
        report.addTableCell("Commit<br>Days", "max-width: 100px; text-align: center; border: none");
        pastMonths.forEach(pastMonth -> {
            report.startTableCell("font-size: 70%; border: none; color: lightgrey; text-align: center");
            int count[] = {0};
            contributors.forEach(contributor -> {
                boolean active[] = {false};
                contributor.getCommitDates().forEach(date -> {
                    String month = DateUtils.getMonth(date);
                    if (month.equals(pastMonth)) {
                        active[0] = true;
                        return;
                    }
                });
                if (active[0]) {
                    count[0] += 1;
                }
            });
            String tooltip = "Month " + pastMonth + ": " + (count[0] + (count[0] == 1 ? " contributor" : " contributors "));
            report.addContentInDivWithTooltip(count[0] == 0 ? "-" : (count[0] + ""), tooltip, "text-align: center");
            report.endTableCell();
        });
        report.endTableRow();

        Collections.sort(contributors, (a, b) -> {
            if (b.getLatestCommitDate().equals(a.getLatestCommitDate())) {
                if (b.getCommitsCount30Days() == a.getCommitsCount30Days()) {
                    return b.getCommitsCount() - a.getCommitsCount();
                } else {
                    return b.getCommitsCount30Days() - a.getCommitsCount30Days();
                }
            } else {
                return b.getLatestCommitDate().compareTo(a.getLatestCommitDate());
            }
        });

        int listLimit = 500;
        contributors.subList(0, contributors.size() > listLimit ? listLimit : contributors.size()).forEach(contributor -> {
            report.startTableRow();
            String textOpacity = contributor.getCommitsCount90Days() > 0 ? "font-weight: bold;" : "opacity: 0.4";
            report.startTableCell("border: none; " + textOpacity);
            if (StringUtils.isNotBlank(contributor.getEmail()) && StringUtils.isNotBlank(contributor.getUserName())) {
                report.addHtmlContent(contributor.getUserName() + " <div style='color: grey; font-size: 80%; margin-bottom: 6px;'>&lt;" + contributor.getEmail() + "&gt;</div>");
            } else {
                report.addHtmlContent((contributor.getUserName() + contributor.getEmail()).trim());
            }
            report.endTableCell();
            report.addTableCell(contributor.getCommitsCount90Days() > 0 ? contributor.getCommitsCount90Days() + "" : "-", "text-align: center; border: none; " + textOpacity);
            report.addTableCell(contributor.getCommitDates().size() + "", "text-align: center; border: none; " + textOpacity);
            int index[] = {0};
            pastMonths.forEach(pastMonth -> {
                int count[] = {0};
                contributor.getCommitDates().forEach(date -> {
                    String month = DateUtils.getMonth(date);
                    if (month.equals(pastMonth)) {
                        count[0] += 1;
                    }
                });
                index[0] += 1;
                report.startTableCell("text-align: center; padding: 0; border: none; vertical-align: middle;");
                if (count[0] > 0) {
                    int size = 10 + (count[0] / 4) * 4;
                    String tooltip = "Month " + pastMonth + ": " + count[0] + (count[0] == 1 ? " commit day" : " commit days");
                    String opacity = "" + Math.max(0.9 - (index[0] - 1) * 0.2, 0.2);
                    report.addContentInDivWithTooltip("", tooltip,
                            "padding: 0; margin: 0; display: inline-block; background-color: #483D8B; opacity: " + opacity + "; border-radius: 50%; width: " + size + "px; height: " + size + "px;");
                } else {
                    report.addContentInDiv("-", "color: lightgrey; font-size: 80%");
                }
                report.endTableCell();
            });
            report.endTableRow();
        });
        report.endTable();
        if (contributors.size() > 500) {
            report.addParagraph("Top 500 (out of " + contributors.size() + ") items shows.");
        }
        report.endDiv();
        report.endSection();
    }

    private void renderPeopleDependencies(List<ComponentDependency> peopleDependencies,
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
            String cMedian = getRoundedValueOf(ContributorConnectionUtils.getCMedian(contributorConnections(peopleDependencies)));
            report.addParagraph("C-median: " + cMedian, "margin-bottom: 0; margin-top: 10px;");
            report.addParagraph("A half of the contributors has more than " + cMedian + " connections, and a half has less than this number.",
                    "color: grey; font-size: 80%; margin-bottom: 20px");
            report.addParagraph("C-mean: " + getRoundedValueOf(ContributorConnectionUtils.getCMean(contributorConnections(peopleDependencies))), "margin-bottom: 0;");
            report.addParagraph("An average number of connections a contributor has with other contributors.", "color: grey; font-size: 80%; margin-bottom: 20px");
            String cIndex = getRoundedValueOf(ContributorConnectionUtils.getCIndex(contributorConnections(peopleDependencies)));
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

        report.endTable();
        report.endDiv();
    }

    static interface PerExtensionCounter {
        int count(ContributorPerExtensionStats perExtensionStats);
    }
}

class ConcernsReportGenerator {
    private static final Log LOG = LogFactory.getLog(ConcernsReportGenerator.class);
    private CodeAnalysisResults codeAnalysisResults;
    private int groupCounter = 0;
    private int concernCounter = 0;
    private RichTextReport report;

    public ConcernsReportGenerator(CodeAnalysisResults codeAnalysisResults) {
        this.codeAnalysisResults = codeAnalysisResults;
    }

    public void addConcernsToReport(RichTextReport report) {
        this.report = report;
        addIntro(report);

        addSummary(report);

        addConcernsGroup(report);
    }

    private void addSummary(RichTextReport report) {
        report.startSection("Overview", "");
        codeAnalysisResults.getConcernsAnalysisResults().forEach(concernsAnalysisResults -> {
            String group = concernsAnalysisResults.getKey();
            report.addLevel2Header(group.toUpperCase());
            report.startDiv("width: 100%; overflow-x: auto");
            if (concernsAnalysisResults.getConcerns().size() > 1) {
                concernsAnalysisResults.getConcerns().forEach(concern -> {
                    if (!isDerivedConcern(concern.getName())) {
                        int mainLoc = codeAnalysisResults.getMainAspectAnalysisResults().getLinesOfCode();
                        int concernLoc = concern.getLinesOfCode();
                        double relativeConcernSizeInPerc = 100.0 * concernLoc / mainLoc;
                        String fileListPath = concern.getAspect().getFileSystemFriendlyName(DataExportUtils.getConcernFilePrefix(group));
                        String svg = getOverviewCodePercentageSvg(concern, relativeConcernSizeInPerc,
                                concern.getFilesCount(), concernLoc, 400, 20, fileListPath, isDerivedConcern(concern.getName()));
                        report.startDiv("");
                        report.addContentInDiv(concern.getName());
                        report.addHtmlContent(svg);
                        report.endDiv();
                    }
                });
            }
            report.endDiv();
        });

        report.endSection();
    }

    private boolean isDerivedConcern(String name) {
        return name.trim().startsWith("- ")
                || name.equalsIgnoreCase("Unclassified")
                || name.equalsIgnoreCase("Multiple Classifications");
    }

    private void addConcernsGroup(RichTextReport report) {
        codeAnalysisResults.getConcernsAnalysisResults().forEach(concernsAnalysisResults -> {
            if (concernsAnalysisResults.getConcerns().size() > 1) {
                renderConcern(report, concernsAnalysisResults);
            } else {
                report.addParagraph("No concerns defined.");
            }
        });
    }

    private void addIntro(RichTextReport report) {
        report.addParagraph("Aspects in the source code identified through RegEx patterns.", "margin-top: 12px; color: grey");
        report.startSection("Intro", "");
        report.startUnorderedList();
        report.addListItem("Features of interest are any aspects of a software system that can be identified through patterns in code.");
        report.addListItem("Features of interest provide you with a way to focus your attention on relevant parts of the codebase.");
        report.addListItem("Typical examples include, security, TODOs, logging.");
        report.addListItem("A feature of interest may be present in multiple files. Any source code file may be in zero or multiple features of interest.");
        report.endUnorderedList();
        report.endSection();
    }

    private void renderConcern(RichTextReport report, ConcernsAnalysisResults concernsAnalysisResults) {
        String key = concernsAnalysisResults.getKey();
        if (key.equalsIgnoreCase("Unclassified")) {
            return;
        }
        groupCounter++;
        report.startSection("" + groupCounter + " " + key.toUpperCase() + " Concerns", "");
        report.startUnorderedList();
        int count = concernsAnalysisResults.getConcerns().size();
        report.addListItem("The \"" + key + "\" group contains <b>"
                + count + "</b> concern" + (count > 1 ? "s" : "") + ".");

        report.startUnorderedList();
        concernsAnalysisResults.getConcerns().forEach(c -> report.addListItem(c.getName()));
        report.endUnorderedList();

        report.endUnorderedList();
        concernsAnalysisResults.getConcerns().forEach(aspectAnalysisResults -> {
            renderScopes(key, aspectAnalysisResults);
        });
        report.endSection();
    }

    private void renderScopes(String key, AspectAnalysisResults aspectAnalysisResults) {
        String name = aspectAnalysisResults.getName();
        if (name.equalsIgnoreCase("Multiple Classifications")) {
            return;
        }
        if (name.equalsIgnoreCase("Unclassified")) {
            return;
        }
        concernCounter++;
        List<NumericMetric> fileCountPerExtension = aspectAnalysisResults.getFileCountPerExtension();
        List<NumericMetric> linesOfCodePerExtension = aspectAnalysisResults.getLinesOfCodePerExtension();

        ScopesRenderer renderer = new ScopesRenderer();
        renderer.setLinesOfCodeInMain(codeAnalysisResults.getMainAspectAnalysisResults().getLinesOfCode());

        String title = "<span style='color: grey; font-size: 90%'>" + key + "</span><br>";
        title += groupCounter + "." + concernCounter + " " + name.replace(" - ", " Multiple Classifications");
        renderer.setTitle(title);
        renderer.setDescription("");
        if (name.equalsIgnoreCase("Unclassified")) {
            renderer.setDescription("This concern include all files that are not included in any of the previously described concerns in this group.");
        }
        if (name.equalsIgnoreCase("Multiple Classifications")) {
            renderer.setDescription("This concern include all files that are included in two or more concerns in this group.");
        }

        renderer.setFileCountPerComponent(fileCountPerExtension);
        renderer.setAspect(aspectAnalysisResults.getAspect());
        renderer.setLinesOfCode(linesOfCodePerExtension);
        renderer.setMaxFileCount(codeAnalysisResults.getMainAspectAnalysisResults().getFilesCount());
        int mainLoc = codeAnalysisResults.getMainAspectAnalysisResults().getLinesOfCode();
        renderer.setMaxLinesOfCode(mainLoc);
        renderer.setTotalNumberOfRegexMatches(aspectAnalysisResults.getNumberOfRegexLineMatches());

        double relativeSizeInPerc = 100.0 * aspectAnalysisResults.getLinesOfCode() / mainLoc;
        int numberOfFiles = aspectAnalysisResults.getFilesCount();
        int linesOfCode = aspectAnalysisResults.getLinesOfCode();

        report.startSubSection(renderer.getTitle(), "");
        String fileListPath = aspectAnalysisResults.getAspect().getFileSystemFriendlyName(DataExportUtils.getConcernFilePrefix(key));
        report.addContentInDiv(getOverviewCodePercentageSvg(aspectAnalysisResults, relativeSizeInPerc, numberOfFiles, linesOfCode,
                200, 20, fileListPath, isDerivedConcern(name)), fileListPath);

        if (name.contains(" - ") && name.contains(" AND ")) {
            List<Double> percentages = extractPercentages(name);
            if (percentages.size() == 2) {
                String svg = getOverlapSvg(percentages);
                report.addParagraph(svg);
            }
        } else {
            renderer.renderDetails(report, false);
        }

        report.startShowMoreBlock("details...");
        codeAnalysisResults.getLogicalDecompositionsAnalysisResults()
                .forEach(logicalDecompositionAnalysisResults -> {
                    renderConcernPerComponent(aspectAnalysisResults, mainLoc, logicalDecompositionAnalysisResults);
                });
        report.endShowMoreBlock();

        report.endSection();
    }

    private void renderConcernPerComponent(AspectAnalysisResults aspectAnalysisResults, int mainLoc, LogicalDecompositionAnalysisResults logicalDecompositionAnalysisResults) {
        report.addHorizontalLine();
        report.addLevel3Header("per component - " + logicalDecompositionAnalysisResults.getKey() + " logical decomposition");
        report.startDiv("width: 100%; overflow-x: auto");
        logicalDecompositionAnalysisResults.getComponents()
                .stream().sorted(Comparator.comparingInt(AspectAnalysisResults::getLinesOfCode).reversed())
                .forEach(component -> {
                    int componentLoc = component.getLinesOfCode();
                    int[] loc = {0};
                    int[] filesCount = {0};
                    aspectAnalysisResults.getAspect().getSourceFiles()
                            .forEach(sourceFile -> {
                                if (sourceFile.getLogicalComponents().contains(component.getAspect())) {
                                    loc[0] += sourceFile.getLinesOfCode();
                                    filesCount[0] += 1;
                                }
                            });
                    if (filesCount[0] > 0) {
                        double relativeComponentSizeInPerc = 100.0 * loc[0] / componentLoc;
                        String svg = getCodePercentageSvg(relativeComponentSizeInPerc,
                                component.getName(),
                                filesCount[0],
                                loc[0],
                                (int) ((double) 400 * componentLoc / mainLoc), 20);
                        report.startDiv("");
                        report.addHtmlContent(svg);
                        report.endDiv();
                    }
                });
        report.endDiv();
    }

    private String getCodePercentageSvg(double percentage, String aspectName, int numberOfFiles, int linesOfCode, int maxSize, int barHeight) {
        String displayText = "in " + numberOfFiles + (numberOfFiles == 1 ? " file " : " files, ")
                + " " + FormattingUtils.formatCount(linesOfCode) + " LOC ("
                + FormattingUtils.getFormattedPercentage(percentage) + "%)";

        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(900);
        chart.setMaxBarWidth(maxSize);
        chart.setBarHeight(barHeight);

        return chart.getPercentageSvg(percentage, aspectName, displayText);
    }

    private String getOverviewCodePercentageSvg(AspectAnalysisResults concern, double percentage, int numberOfFiles, int linesOfCode, int maxSize, int barHeight, String fileListPath, boolean derivedConcern) {
        String filesFragment = numberOfFiles + (numberOfFiles == 1 ? " file " : " files");
        if (StringUtils.isNotBlank(fileListPath)) {
            filesFragment = "<u><a href='../data/text/aspect_" + fileListPath + ".txt'>" + filesFragment + "</a></u>";
            if (!derivedConcern) {
                if (concern.getFoundFiles().size() > 0) {
                    filesFragment += " | <u><a href='../data/text/aspect_" + fileListPath + "_found_text_per_file.txt'>found text per file</a></u>";
                }
                if (concern.getFoundTextList().size() > 0) {
                    filesFragment += " | <u><a href='../data/text/aspect_" + fileListPath + "_found_text_per_file.txt'>found text per file</a></u>";
                }
            }
        }
        String displayText = FormattingUtils.formatCount(linesOfCode) + " LOC ("
                + FormattingUtils.getFormattedPercentage(percentage) + "%) "
                + filesFragment;

        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(800);
        chart.setMaxBarWidth(maxSize);
        chart.setBarHeight(barHeight);
        chart.setBarStartXOffset(2);

        return chart.getPercentageSvg(percentage, "", displayText);
    }

    private String getOverlapSvg(List<Double> percentages) {
        Double perc1 = percentages.get(0);
        Double perc2 = percentages.get(1);
        double size1 = perc1 <= perc2 ? 100 : 100 * (perc2 / perc1);
        double size2 = perc1 > perc2 ? 100 : 100 * (perc1 / perc2);
        String svg = "<svg width='220' height='40'>";
        svg += "<rect width='"
                + (int) (size1) + "' x='2' y='2' height='20' style='fill-opacity:0.4;fill:#c0deed;stroke:#898989;stroke-width:2;' />";
        svg += "<rect x='" + (2 + (int) (size1 * (100 - perc1) / 100.0)) + "' y='6' width='"
                + (int) (size2) + "' height='20' style='fill-opacity:0.4;fill:#c0deed;fill:#c0deed;stroke:#898989;stroke-width:2;' />";
        svg += "</svg>";
        return svg;
    }

    private List<Double> extractPercentages(String name) {
        List<Double> percentages = new ArrayList<>();
        try {
            Pattern soe = Pattern.compile("\\(.*?\\%\\)");
            Matcher matcher = soe.matcher(name);

            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String strValue = name.substring(start + 1, end - 2);
                if (NumberUtils.isParsable(strValue)) {
                    percentages.add(Double.parseDouble(strValue));
                }
            }
        } catch (PatternSyntaxException e) {
            LOG.debug(e);
        } catch (StackOverflowError e) {
            LOG.error(e);
        }

        return percentages;
    }
}

class OverviewReportGenerator {
    private CodeAnalysisResults codeAnalysisResults;
    private File codeConfigurationFile;

    public OverviewReportGenerator(CodeAnalysisResults codeAnalysisResults, File codeConfigurationFile) {
        this.codeAnalysisResults = codeAnalysisResults;
        this.codeConfigurationFile = codeConfigurationFile;
    }

    public void addScopeAnalysisToReport(RichTextReport report) {
        appendHeader(report);

        String mainName = codeAnalysisResults.getCodeConfiguration().getMain().getName();
        String testName = codeAnalysisResults.getCodeConfiguration().getTest().getName();
        String generatedName = codeAnalysisResults.getCodeConfiguration().getGenerated().getName();
        String buildName = codeAnalysisResults.getCodeConfiguration().getBuildAndDeployment().getName();
        String otherName = codeAnalysisResults.getCodeConfiguration().getOther().getName();

        List<NumericMetric> code = Arrays.asList(new NumericMetric(mainName, codeAnalysisResults.getMainAspectAnalysisResults().getLinesOfCode()),
                new NumericMetric(testName, codeAnalysisResults.getTestAspectAnalysisResults().getLinesOfCode()),
                new NumericMetric(generatedName, codeAnalysisResults.getGeneratedAspectAnalysisResults().getLinesOfCode()),
                new NumericMetric(buildName, codeAnalysisResults.getBuildAndDeployAspectAnalysisResults().getLinesOfCode()),
                new NumericMetric(otherName, codeAnalysisResults.getOtherAspectAnalysisResults().getLinesOfCode()));

        List<NumericMetric> counts = Arrays.asList(new NumericMetric(mainName,
                        codeAnalysisResults.getMainAspectAnalysisResults().getFilesCount()),
                new NumericMetric(testName, codeAnalysisResults.getTestAspectAnalysisResults().getFilesCount()),
                new NumericMetric(generatedName, codeAnalysisResults.getGeneratedAspectAnalysisResults().getFilesCount()),
                new NumericMetric(buildName, codeAnalysisResults.getBuildAndDeployAspectAnalysisResults().getFilesCount()),
                new NumericMetric(otherName, codeAnalysisResults.getOtherAspectAnalysisResults().getFilesCount()));

        List<AspectAnalysisResults> aspects = Arrays.asList(codeAnalysisResults.getMainAspectAnalysisResults(),
                codeAnalysisResults.getTestAspectAnalysisResults(),
                codeAnalysisResults.getGeneratedAspectAnalysisResults(),
                codeAnalysisResults.getBuildAndDeployAspectAnalysisResults(),
                codeAnalysisResults.getOtherAspectAnalysisResults());

        report.startSection("Overview of Analyzed Files", "Basic stats on analyzed files");
        report.startSubSection("Intro", "For analysis purposes we separate files in scope into several categories: <b>main</b>, <b>test</b>, <b>generated</b>, <b>deployment and build</b>, and <b>other</b>.");
        appendIntroduction(report);
        ScopesRenderer renderer = getScopesRenderer("", "", counts, code, "");
        renderer.setInSection(false);
        renderer.setTitle("All Files in Scope");
        renderer.setAspectsFileListPaths(aspects.stream().map(aspect -> aspect.getAspect().getFileSystemFriendlyName("")).collect(Collectors.toList()));
        renderer.renderReport(report, "");

        report.endSection();

        renderScopes(report, codeAnalysisResults.getMainAspectAnalysisResults(), "Main Code", "All <b>manually</b> created or maintained source code that defines logic of the product that is  run in a <b>production</b> environment.", "main");
        renderScopes(report, codeAnalysisResults.getTestAspectAnalysisResults(), "Test Code", "Used only for testing of the product. Normally not deployed in a production environment.", "test");
        renderScopes(report, codeAnalysisResults.getGeneratedAspectAnalysisResults(), "Generated Code", "Automatically generated files, not manually changed after generation.", "generated");
        renderScopes(report, codeAnalysisResults.getBuildAndDeployAspectAnalysisResults(), "Build and Deployment Code", "Source code used to configure or support build and deployment process.", "build");
        renderScopes(report, codeAnalysisResults.getOtherAspectAnalysisResults(), "Other Code", "", "other");

        report.endSection();

        renderAnalyzersInfo(report, codeAnalysisResults);

        addFooter(report);
    }

    private void appendIntroduction(RichTextReport report) {
        String extra = "";
        extra += "<ul>";
        extra += "<li>The <b>main</b> category contains all <b>manually</b> created source code files that are being used in the <b>production</b>.</li>";
        extra += "<li>Files in the <b>main</b> category are used as input for other analyses: logical decomposition, concerns, duplication, file size, unit size, and conditional complexity.</li>";
        extra += "<li><b>Test</b> source code files are used only for testing of the product. These files are normally not deployed to production.</li>";
        extra += "<li><b>Build and deployment</b> source code files are used to configure or support build and deployment process.</li>";
        extra += "<li><b>Generated</b> source code files are automatically generated files that have not been manually changed after generation.</li>";
        extra += "<li>While a source code folder may contain a number of files, we are primarily interested in the source code files that are being written and maintained by developers.</li>";
        extra += "<li>Files containing binaries, documentation, or third-party libraries, for instance, are excluded from analysis. The exception are third-party libraries " +
                "that have been changed by developers.</li>";
        extra += "</ul>";
        report.addParagraph(extra);
    }

    private void addFooter(RichTextReport report) {
        report.addLineBreak();
        report.addHorizontalLine();
        report.addParagraph(RichTextRenderingUtils.italic(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())));
    }


    public void renderAnalyzersInfo(RichTextReport report, CodeAnalysisResults results) {
        report.startSection("Analyzers", "Info about analyzers used for source code examinations.");

        report.startUnorderedList();
        results.getMainAspectAnalysisResults().getFileCountPerExtension().forEach(extension -> {
            String extensionString = extension.getName().replace("*.", "").trim().toLowerCase();
            LanguageAnalyzer analyzer = LanguageAnalyzerFactory.getInstance().getLanguageAnalyzerByExtension(extensionString);

            report.addListItem("*." + extensionString + " files are analyzed with <b> " + analyzer.getClass().getSimpleName() + ":");
            report.startUnorderedList("margin-bottom: 12px");
            analyzer.getFeaturesDescription().forEach(feature -> {
                report.addListItem(feature);
            });
            report.endUnorderedList();
        });
        report.endUnorderedList();
    }

    public void renderScopes(RichTextReport report, AspectAnalysisResults aspectAnalysisResults, String title, String description, String explorers) {
        List<NumericMetric> fileCountPerExtension = aspectAnalysisResults.getFileCountPerExtension();
        StringBuilder langDivs = new StringBuilder("<div style='margin-bottom: 16px'>");
        Set<String> alreadyAddedImage = new HashSet<>();
        fileCountPerExtension.forEach(fileType -> {
            String lang = fileType.getName().replace("*.", "").trim();
            String image = DataImageUtils.getLangDataImage(lang);
            if (image == null || !alreadyAddedImage.contains(image)) {
                langDivs.append(DataImageUtils.getLangDataImageDiv42(lang));
                if (image != null) {
                    alreadyAddedImage.add(image);
                }
            }
        });
        langDivs.append("</div>");
        if (fileCountPerExtension.size() > 0) {
            List<NumericMetric> linesOfCodePerExtension = aspectAnalysisResults.getLinesOfCodePerExtension();
            ScopesRenderer renderer = getScopesRenderer(title, "", fileCountPerExtension, linesOfCodePerExtension, explorers);
            renderer.setTitle(title);
            renderer.setFilesListPath(DataExportUtils.getAspectFileListFileName(aspectAnalysisResults.getAspect(), ""));
            renderer.setAspect(aspectAnalysisResults.getAspect());
            renderer.renderReport(report, description, langDivs.toString());
        }
    }

    private ScopesRenderer getScopesRenderer(String title, String description, List<NumericMetric> fileCounts, List<NumericMetric> linesOfCode, String explorers) {
        ScopesRenderer renderer = new ScopesRenderer();
        renderer.setExplorers(explorers);
        renderer.setSort(false);
        renderer.setLinesOfCodeInMain(codeAnalysisResults.getMainAspectAnalysisResults().getLinesOfCode());
        renderer.setTitle(title.replace("-", "").trim());
        renderer.setDescription(description);
        renderer.setFileCountPerComponent(fileCounts);
        renderer.setLinesOfCode(linesOfCode);

        renderer.setMaxFileCount(fileCounts.stream().map(a -> a.getValue().intValue()).mapToInt(Integer::intValue).sum());
        renderer.setMaxLinesOfCode(linesOfCode.stream().map(a -> a.getValue().intValue()).mapToInt(Integer::intValue).sum());

        return renderer;
    }


    private void appendHeader(RichTextReport report) {
        report.addParagraph("Analysis scope, overview of main, test, generated, deployment, build, and other code.",
                "margin-top: 12px; color: grey");
        report.startSection("Source Code Analysis Scope", "Files includes and excluded from analyses");
        List<String> extensions = codeAnalysisResults.getCodeConfiguration().getExtensions();
        report.startDiv("margin-top: -8px; margin-bottomL: 12px;");
        Set<String> alreadyAddedImage = new HashSet<>();
        extensions.forEach(lang -> {
            String image = DataImageUtils.getLangDataImage(lang);
            if (image == null || !alreadyAddedImage.contains(image)) {
                report.addHtmlContent(DataImageUtils.getLangDataImageDiv42(lang));
                if (image != null) {
                    alreadyAddedImage.add(image);
                }
            }
        });
        report.endDiv();
        report.startUnorderedList();
        int totalNumberOfFilesInScope = codeAnalysisResults.getTotalNumberOfFilesInScope();
        int numberOfExcludedFiles = codeAnalysisResults.getNumberOfExcludedFiles();
        int extensionsCount = extensions.size();
        report.addListItem("<b>" + extensionsCount + "</b> extension" + (extensionsCount > 1 ? "s are" : " is") + " included in analyses: " + getExtensionsString(extensions));
        List<SourceFileFilter> exclusions = codeAnalysisResults.getCodeConfiguration().getIgnore();
        if (exclusions != null && exclusions.size() > 0) {
            report.addListItem(RichTextRenderingUtils.renderNumberStrong(exclusions.size())
                    + (exclusions.size() == 1 ? " criterion is " : " criteria are ") +
                    " used to exclude files from analysis:");
            report.startUnorderedList();
            exclusions.forEach(exclusion -> {
                report.addListItem(describeExclusion(exclusion));
            });
            report.endUnorderedList();
        }
        int totalNumberOfIncludedFiles = totalNumberOfFilesInScope - numberOfExcludedFiles;
        String scopeSvg = getScopeSvg(totalNumberOfFilesInScope, numberOfExcludedFiles, totalNumberOfIncludedFiles);
        report.endUnorderedList();

        report.startUnorderedList();
        report.addListItem("The source code folder contains " + RichTextRenderingUtils.renderNumberStrong(totalNumberOfFilesInScope) + " files:");
        report.startUnorderedList();
        report.addListItem(scopeSvg);
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(totalNumberOfIncludedFiles) + " files are included in analyses. ");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(numberOfExcludedFiles) + " files are excluded from analyses:");
        report.startUnorderedList();
        int filesExcludedByExtensionCount = codeAnalysisResults.getFilesExcludedByExtension().size();
        report.addListItem("<a target='_blank' href='../data/text/excluded_files_ignored_extensions.txt'>" + RichTextRenderingUtils.renderNumberStrong(filesExcludedByExtensionCount) + " based on extension</a>.");
        report.addListItem("<a target='_blank' href='../data/text/excluded_files_ignored_rules.txt'>" +RichTextRenderingUtils.renderNumberStrong(numberOfExcludedFiles - filesExcludedByExtensionCount) + " based on ignore rules</a>.");
        report.endUnorderedList();
        report.endUnorderedList();
        report.endUnorderedList();

        report.startUnorderedList();
        report.addListItem("<a target='_blank' href='../explorers/files-explorer.html'><b>Explore all files...</b></a>.");
        report.endUnorderedList();


        report.endSection();
    }

    private String getScopeSvg(int totalNumberOfFilesInScope, int numberOfExcludedFiles, int totalNumberOfIncludedFiles) {
        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(400);
        chart.setMaxBarWidth(240);
        chart.setBarHeight(10);
        chart.setBarStartXOffset(1);

        double percentageIncluded = totalNumberOfFilesInScope > 0 ? 100.0 * totalNumberOfIncludedFiles / totalNumberOfFilesInScope : 0;
        return chart.getPercentageSvg(percentageIncluded, "", "");
    }

    private String describeExclusion(SourceFileFilter exclusion) {
        String description = exclusion.getException() ? "do not exclude" : "exclude";
        description += " files with ";
        boolean add = false;
        if (StringUtils.isNotBlank(exclusion.getPathPattern())) {
            description += "path like \"<b>" + exclusion.getPathPattern() + "</b>\"";
            add = true;
        }
        if (StringUtils.isNotBlank(exclusion.getContentPattern())) {
            if (add) {
                description += " AND ";
            }
            description += "content like \"<b>" + exclusion.getContentPattern() + "</b>\"";
        }
        if (StringUtils.isNotBlank(exclusion.getNote())) {
            description += " (" + exclusion.getNote() + ")";
        }
        IgnoredFilesGroup ignoredFilesGroup = codeAnalysisResults.getIgnoredFilesGroups().get(exclusion.toString());
        int ignoredGroupFilesCount = 0;
        if (ignoredFilesGroup != null) {
            ignoredGroupFilesCount = ignoredFilesGroup.getSourceFiles().size();
        }
        description += " (<b>" + ignoredGroupFilesCount + "</b> file" + (ignoredGroupFilesCount != 1 ? "s" : "") + ")";
        return description + ".";
    }

    private String getExtensionsString(List<String> extensions) {
        StringBuilder stringBuilder = new StringBuilder();

        extensions.forEach(extension -> {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(extension);
        });

        return stringBuilder.toString();
    }

    private String getExcludedExtensionsOverview(Map<String, Integer> excludedExtensions) {
        List<String> extensions = new ArrayList<>(excludedExtensions.keySet());
        Collections.sort(extensions, (o1, o2) -> -Integer.compare(excludedExtensions.get(o1), excludedExtensions.get(o2)));

        StringBuilder string = new StringBuilder();

        extensions.forEach(extension -> {
            if (string.length() > 0) {
                string.append(", ");
            }
            string.append(excludedExtensions.get(extension) + " <b>" + extension + "</b>");
        });

        return "Excluded files per extension: " + string.toString();
    }

}

class FileAgeReportGenerator {
    public static final String LATEST_CHANGE_DISTRIBUTION = "File Freshness Distribution";
    public static final String FILE_AGE_DISTRIBUTION = "File Age Distribution";
    public static final String FILE_AGE_DESCRIPTION = "Days since first update";
    public static final String LATEST_CHANGE_DESCRIPTION = "Days since last update";
    private final Thresholds thresholds;

    private CodeAnalysisResults codeAnalysisResults;
    private List<String> ageLabels;
    private int daysBetween;
    private int estimatedWorkingDays;

    public FileAgeReportGenerator(CodeAnalysisResults codeAnalysisResults) {
        this.codeAnalysisResults = codeAnalysisResults;
        thresholds = codeAnalysisResults.getCodeConfiguration().getAnalysis().getFileAgeThresholds();
        this.ageLabels = thresholds.getLabels();
    }

    public void addFileAgeToReport(RichTextReport report) {
        describe(report);

        addOverallSections(report);

        addGraphsPerExtension(report);

        addGraphsPerLogicalComponents(report);

        addOldestFilesList(report);
        addMostPreviouslyChangedFilesList(report);
        addYoungestFilesList(report);
        addMostRecentlyChangedFilesList(report);
    }

    private void addSummary(RichTextReport report) {
        FileHistoryComponentsHelper helper = new FileHistoryComponentsHelper();

        List<FileModificationHistory> history = codeAnalysisResults.getFilesHistoryAnalysisResults().getHistory(Integer.MAX_VALUE);
        List<String> uniqueDates = helper.getUniqueDates(history);

        if (uniqueDates.size() > 1) {
            String firstDateString = uniqueDates.get(0);
            String latestDateString = uniqueDates.get(uniqueDates.size() - 1);

            Date firstDate = FileHistoryUtils.getDateFromString(firstDateString);
            Date latestDate = FileHistoryUtils.getDateFromString(latestDateString);

            this.daysBetween = FileHistoryUtils.daysBetween(firstDate, latestDate);

            int weeks = daysBetween / 7;
            estimatedWorkingDays = weeks * 5;

            report.startSection("Summary", "");

            report.startUnorderedList();

            report.addListItem("Number of files: <b>" + FormattingUtils.formatCount(codeAnalysisResults.getMainAspectAnalysisResults().getFilesCount()) + "</b>");
            report.addListItem("Daily file updates (only one update per file and date counted): <b>" + history.size() + "</b>");
            report.addListItem("First update: <b>" + firstDateString + "</b>");
            report.addListItem("Latest update: <b>" + latestDateString + "</b>");
            report.addListItem("Days between first and latest update: <b>" + FormattingUtils.formatCount(daysBetween) + "</b> (" + weeks + " weeks, estimated " + FormattingUtils.formatCount(estimatedWorkingDays) + " working days)");
            report.addListItem("Active days (at least one file change): <b>" + FormattingUtils.formatCount(uniqueDates.size()) + "</b>");
            int ignoredFiles = codeAnalysisResults.getFilesHistoryAnalysisResults().getFilesWithoutCommitHistoryCount();
            if (ignoredFiles > 0) {
                report.addListItem("Files without commit history (ignored): <b>" + FormattingUtils.formatCount(ignoredFiles) + " (" + FormattingUtils.formatCount(codeAnalysisResults.getFilesHistoryAnalysisResults().getFilesWithoutCommitHistoryLinesOfCode()) + " lines of code)</b>");
                report.startUnorderedList();
                report.addListItem("<a href='../data/text/mainFilesWithoutHistory.txt' target='_blank'>Files without history</a>");
                report.endUnorderedList();
            }
            report.addListItem("Data:");
            report.startUnorderedList();
            report.addListItem("<a href='../data/text/mainFilesWithHistory.txt' target='_blank'>Organized per file</a>");
            report.endUnorderedList();
            report.endUnorderedList();
            report.endSection();
        }
    }

    private void addGraphsPerExtension(RichTextReport report) {
        FilesHistoryAnalysisResults ageAnalysisResults = codeAnalysisResults.getFilesHistoryAnalysisResults();

        String extensions = codeAnalysisResults.getCodeConfiguration().getExtensions().stream().collect(Collectors.joining(", "));
        report.startSection("File Change History per File Extension", extensions);

        addGraphPerExtension(report, ageAnalysisResults.getFirstModifiedDistributionPerExtension(),
                FILE_AGE_DISTRIBUTION + " per Extension", FILE_AGE_DESCRIPTION, Palette.getAgePalette());
        addGraphPerExtension(report, ageAnalysisResults.getLastModifiedDistributionPerExtension(),
                LATEST_CHANGE_DISTRIBUTION + " per Extension", LATEST_CHANGE_DESCRIPTION, Palette.getFreshnessPalette());

        report.endSection();
    }

    private void addOverallSections(RichTextReport report) {
        addSummary(report);

        report.startSection("File Change History Overall", "");
        addAgeGraphOverall(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileFirstModifiedDistribution(),
                FILE_AGE_DISTRIBUTION + " Overall", FILE_AGE_DESCRIPTION, Palette.getAgePalette());
        addFreshnessGraphOverall(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileLastModifiedDistribution(), "" +
                LATEST_CHANGE_DISTRIBUTION + " Overall", LATEST_CHANGE_DESCRIPTION, Palette.getFreshnessPalette());

        report.endSection();
    }

    private void describe(RichTextReport report) {
        report.addParagraph("File age measurements show the distribution of file ages (days since the first commit)" +
                " and the file freshness (days since the latest commit).", "margin-top: 12px; color: grey");
    }

    private void addAgeGraphOverall(RichTextReport report, SourceFileAgeDistribution distribution, String title, String subtitle, Palette palette) {
        report.startSubSection(title, subtitle);
        report.startUnorderedList();
        report.addListItem("There are " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalCount())
                + " files with " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalValue())
                + " lines of code in files" +
                ".");
        report.startUnorderedList();
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskCount())
                + " files that are " + thresholds.getVeryHighRiskLabel() + " days old ("
                + RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskCount())
                + " files that are " + thresholds.getHighRiskLabel() + " days old ("
                + RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskCount())
                + " files that are " + thresholds.getMediumRiskLabel() + " days old ("
                + RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskCount())
                + " files that are " + thresholds.getLowRiskLabel() + " days old ("
                + RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskCount())
                + " files that are " + thresholds.getNegligibleRiskLabel() + " days old ("
                + RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskValue())
                + " lines of code)");
        report.endUnorderedList();
        report.endUnorderedList();
        report.addHtmlContent(PieChartUtils.getRiskDistributionChart(distribution, ageLabels, palette));
        report.addLineBreak();
        report.addHtmlContent("explore: ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_age_coloring.html'>grouped by folders</a>");
        report.addHtmlContent(" | ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_age_coloring_categories.html'>grouped by age</a>");
        report.endSection();
    }

    private void addFreshnessGraphOverall(RichTextReport report, SourceFileAgeDistribution distribution, String title, String subtitle, Palette palette) {
        report.startSubSection(title, subtitle);
        report.startUnorderedList();
        report.addListItem("There are " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalCount())
                + " files with " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalValue())
                + " lines of code in files" +
                ".");
        report.startUnorderedList();
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskCount())
                + " files have been last changed " + thresholds.getVeryHighRiskLabel() + " days ago (" + RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskCount())
                + "  files have been last changed " + thresholds.getHighRiskLabel() + " days ago (" + RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskCount())
                + " files have been last changed " + thresholds.getMediumRiskLabel() + " days ago (" + RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskCount())
                + " files have been last changed " + thresholds.getLowRiskLabel() + " days ago (" + RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskCount())
                + " files have been last changed " + thresholds.getNegligibleRiskLabel() + " days ago (" + RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskValue())
                + " lines of code)");
        report.endUnorderedList();
        report.endUnorderedList();
        report.addHtmlContent(PieChartUtils.getRiskDistributionChart(distribution, ageLabels, palette));
        report.addLineBreak();
        report.addHtmlContent("explore: ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_freshness_coloring.html'>grouped by folders</a>");
        report.addHtmlContent(" | ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_freshness_coloring_categories.html'>grouped by freshness</a>");
        report.endSection();
    }

    private void addGraphPerExtension(RichTextReport report, List<RiskDistributionStats> sourceFileAgeDistribution, String title, String subtitle, Palette palette) {
        report.startSubSection(title, subtitle);
        report.addHtmlContent(RiskDistributionStatsReportUtils.getRiskDistributionPerKeySvgBarChart(sourceFileAgeDistribution, ageLabels, palette));
        report.endSection();
    }

    private void addGraphsPerLogicalComponents(RichTextReport report) {
        String components = codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions().stream().map(c -> c.getName()).collect(Collectors.joining(", "));

        report.startSection("File Change History per Logical Decomposition", components);

        addChangesPerLogicalDecomposition(report);

        report.endSection();
    }

    private void addChangesPerLogicalDecomposition(RichTextReport report) {
        int logicalDecompositionCounter[] = {0};
        codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions().forEach(logicalDecomposition -> {
            logicalDecompositionCounter[0]++;

            String name = logicalDecomposition.getName();
            codeAnalysisResults.getFilesHistoryAnalysisResults().getFirstModifiedDistributionPerLogicalDecomposition().stream()
                    .filter(d -> d.getName().equalsIgnoreCase(name)).forEach(distribution -> {
                addFirstModifiedDetailsForLogicalDecomposition(report, name, distribution.getDistributionPerComponent());
            });
            codeAnalysisResults.getFilesHistoryAnalysisResults().getLastModifiedDistributionPerLogicalDecomposition().stream()
                    .filter(d -> d.getName().equalsIgnoreCase(name)).forEach(distribution -> {
                addLastModifiedPerLogicalComponent(report, name, distribution.getDistributionPerComponent());
            });
        });
    }

    private void addFirstModifiedDetailsForLogicalDecomposition(RichTextReport report, String name, List<RiskDistributionStats> distributionsPerComponent) {
        report.startSubSection("" + name + " (" + FILE_AGE_DISTRIBUTION.toLowerCase() + ")", FILE_AGE_DESCRIPTION);
        report.startDiv("max-height: 300px; overflow-x: none; overflow-y: auto");
        report.addHtmlContent(RiskDistributionStatsReportUtils.getRiskDistributionPerKeySvgBarChart(distributionsPerComponent, ageLabels, Palette.getAgePalette()));
        report.endDiv();
        report.endSection();
    }

    private void addLastModifiedPerLogicalComponent(RichTextReport report, String name, List<RiskDistributionStats> distributionsPerComponent) {
        report.startSubSection("" + name + " (" + LATEST_CHANGE_DISTRIBUTION.toLowerCase() + ")", LATEST_CHANGE_DESCRIPTION);
        report.startDiv("max-height: 300px; overflow-x: none; overflow-y: auto");
        report.addHtmlContent(RiskDistributionStatsReportUtils.getRiskDistributionPerKeySvgBarChart(distributionsPerComponent, ageLabels, Palette.getFreshnessPalette()));
        report.endDiv();
        report.endSection();
    }

    private void addOldestFilesList(RichTextReport report) {
        List<SourceFile> longestFiles = codeAnalysisResults.getFilesHistoryAnalysisResults().getOldestFiles();
        report.startSection("Oldest Files (Top " + longestFiles.size() + ")", "");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addHtmlContent(FilesReportUtils.getFilesTable(longestFiles, cacheSourceFiles, true, false).toString());
        report.endSection();
    }

    private void addYoungestFilesList(RichTextReport report) {
        List<SourceFile> youngestFiles = codeAnalysisResults.getFilesHistoryAnalysisResults().getYoungestFiles();
        report.startSection("Most Recently Created Files (Top " + youngestFiles.size() + ")", "");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addHtmlContent(FilesReportUtils.getFilesTable(youngestFiles, cacheSourceFiles, true, false).toString());
        report.endSection();
    }

    private void addMostRecentlyChangedFilesList(RichTextReport report) {
        List<SourceFile> youngestFiles = codeAnalysisResults.getFilesHistoryAnalysisResults().getMostRecentlyChangedFiles();
        report.startSection("Most Recently Changed Files (Top " + youngestFiles.size() + ")", "");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addHtmlContent(FilesReportUtils.getFilesTable(youngestFiles, cacheSourceFiles, true, false).toString());
        report.endSection();
    }

    private void addMostPreviouslyChangedFilesList(RichTextReport report) {
        List<SourceFile> youngestFiles = codeAnalysisResults.getFilesHistoryAnalysisResults().getMostPreviouslyChangedFiles();
        report.startSection("Files Not Recently Changed (Top " + youngestFiles.size() + ")", "");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addHtmlContent(FilesReportUtils.getFilesTable(youngestFiles, cacheSourceFiles, true, false).toString());
        report.endSection();
    }
}

class FileChurnReportGenerator {
    public static final String THE_NUMBER_OF_FILE_CHANGES = "File Change Frequency";
    public static final String THE_NUMBER_OF_FILE_CHANGES_DESCRIPTION = "The number of recorded file updates";
    public static final String THE_CONTRIBUTORS_COUNT = "Contributors Count Frequency";
    public static final String THE_CONTRIBUTORS_COUNT_DESCRIPTION = "The number of file contributors";
    private final Thresholds thresholds;
    private final Thresholds thresholdsContributors;

    private CodeAnalysisResults codeAnalysisResults;
    private List<String> changeFrequencyLabels;
    private List<String> contributorsCountFrequencyLabels;
    private int graphCounter = 1;

    public FileChurnReportGenerator(CodeAnalysisResults codeAnalysisResults) {
        this.codeAnalysisResults = codeAnalysisResults;
        thresholds = codeAnalysisResults.getCodeConfiguration().getAnalysis().getFileUpdateFrequencyThresholds();
        thresholdsContributors = codeAnalysisResults.getCodeConfiguration().getAnalysis().getFileContributorsCountThresholds();
        changeFrequencyLabels = thresholds.getLabels();
        contributorsCountFrequencyLabels = thresholdsContributors.getLabels();
    }

    public void addFileHistoryToReport(RichTextReport report) {
        report.addParagraph("File change frequency (churn) shows the distribution of file updates " +
                "(days with at least one commit).", "margin-top: 12px; color: grey");

        addOverallSections(report);

        addGraphsPerExtension(report);
        addGraphsPerLogicalComponents(report);
        addMostChangedFilesList(report);
        addFilesWithMostContributors(report);
        addFilesWithLeastContributors(report);
        addCorrelations(report);
    }


    private void addGraphsPerExtension(RichTextReport report) {
        FilesHistoryAnalysisResults ageAnalysisResults = codeAnalysisResults.getFilesHistoryAnalysisResults();

        String extensions = codeAnalysisResults.getCodeConfiguration().getExtensions().stream().collect(Collectors.joining(", "));
        report.startSection("File Change Frequency per File Extension", extensions);

        addChangesGraphPerExtension(report, ageAnalysisResults.getChangeDistributionPerExtension(),
                THE_NUMBER_OF_FILE_CHANGES + " per Extension", THE_NUMBER_OF_FILE_CHANGES_DESCRIPTION);

        report.endSection();
    }

    private void addOverallSections(RichTextReport report) {
        report.startSection("Overview", "");
        report.startSubSection("File Change Frequency Overall", "");
        addGraphOverallChange(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getOverallFileChangeDistribution());
        report.addLineBreak();
        report.addHtmlContent("explore: ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_update_frequency_coloring.html'>grouped by folders</a>");
        report.addHtmlContent(" | ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_update_frequency_coloring_categories.html'>grouped by update frequency</a>");
        report.addHtmlContent(" | ");
        report.addHtmlContent("<a target='_blank' href='../data/text/mainFilesWithHistory.txt'>data</a>");
        report.endSection();

        report.startSubSection("Contributors Count Frequency Overall", "");
        addGraphOverallContributorCount(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getOverallContributorsCountDistribution());
        report.addLineBreak();
        report.addHtmlContent("explore: ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_contributors_count_coloring.html'>grouped by folders</a>");
        report.addHtmlContent(" | ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_contributors_count_coloring_categories.html'>grouped by contributors count</a>");
        report.addHtmlContent(" | ");
        report.addHtmlContent("<a target='_blank' href='../data/text/mainFilesWithHistory.txt'>data</a>");
        report.endSection();
        report.endSection();
    }

    private void addGraphOverallChange(RichTextReport report, SourceFileChangeDistribution distribution) {
        report.startUnorderedList();
        report.addListItem("There are <b>"
                + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalCount())
                + "</b> files with " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalValue())
                + " lines of code" +
                ".");
        report.startUnorderedList();
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskCount())
                + " " + (distribution.getVeryHighRiskCount() == 1 ? "file" : "files") + " changed more than "
                + thresholds.getVeryHigh()
                + " times (" + RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskCount())
                + " " + (distribution.getHighRiskCount() == 1 ? "file" : "files") + " changed " + thresholds.getHighRiskLabel() + " times (" + RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskCount())
                + " " + (distribution.getMediumRiskCount() == 1 ? "file" : "files") + " changed " + thresholds.getMediumRiskLabel() + " times (" + RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskCount())
                + " " + (distribution.getLowRiskCount() == 1 ? "file" : "files") + " changed " + thresholds.getLowRiskLabel() + " times (" + RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskCount())
                + " " + (distribution.getNegligibleRiskCount() == 1 ? "file" : "files") + " changed " + thresholds.getNegligibleRiskLabel() + " times (" + RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskValue())
                + " lines of code)");
        report.endUnorderedList();
        report.endUnorderedList();
        report.addHtmlContent(PieChartUtils.getRiskDistributionChart(distribution, changeFrequencyLabels, Palette.getHeatPalette()));
    }

    private void addGraphOverallContributorCount(RichTextReport report, SourceFileChangeDistribution distribution) {
        report.startUnorderedList();
        report.addListItem("There are <b>"
                + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalCount())
                + "</b> files with " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalValue())
                + " lines of code" +
                ".");
        report.startUnorderedList();
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskCount())
                + " " + (distribution.getVeryHighRiskCount() == 1 ? "file" : "files") + " changed by more than "
                + thresholdsContributors.getVeryHigh()
                + " contributors (" + RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskCount())
                + " " + (distribution.getHighRiskCount() == 1 ? "file" : "files") + " changed by " + thresholdsContributors.getHighRiskLabel() + " contributors (" + RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskCount())
                + " " + (distribution.getMediumRiskCount() == 1 ? "file" : "files") + " changed by " + thresholdsContributors.getMediumRiskLabel() + " contributors (" + RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskCount())
                + " " + (distribution.getLowRiskCount() == 1 ? "file" : "files") + " changed by " + thresholdsContributors.getLowRiskLabel() + " contributors (" + RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskCount())
                + " " + (distribution.getNegligibleRiskCount() == 1 ? "file" : "files") + " changed by " + thresholdsContributors.getNegligibleRiskLabel()
                + " " + (thresholdsContributors.getLow() == 1 ? "contributor" : "contributors") + " (" + RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskValue())
                + " lines of code)");
        report.endUnorderedList();
        report.endUnorderedList();
        report.addHtmlContent(PieChartUtils.getRiskDistributionChart(distribution, contributorsCountFrequencyLabels, Palette.getHeatPalette()));
    }

    private void addChangesGraphPerExtension(RichTextReport report, List<RiskDistributionStats> sourceFileAgeDistribution, String title, String subtitle) {
        report.startSubSection(title, subtitle);
        report.addHtmlContent(RiskDistributionStatsReportUtils.getRiskDistributionPerKeySvgBarChart(sourceFileAgeDistribution, changeFrequencyLabels, Palette.getHeatPalette()));
        report.endSection();
    }

    private void addGraphsPerLogicalComponents(RichTextReport report) {
        String components = codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions().stream().map(c -> c.getName()).collect(Collectors.joining(", "));

        report.startSection("File Change Frequency per Logical Decomposition", components);

        addChangesPerLogicalDecomposition(report);

        report.endSection();
    }

    private LogicalDecomposition getLogicalDecompositionByName(String name) {
        for (LogicalDecomposition logicalDecomposition : this.codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions()) {
            if (logicalDecomposition.getName().equalsIgnoreCase(name)) {
                return logicalDecomposition;
            }
        }

        return null;
    }

    private void addChangesPerLogicalDecomposition(RichTextReport report) {
        int logicalDecompositionCounter[] = {0};
        codeAnalysisResults.getCodeConfiguration().getLogicalDecompositions().forEach(logicalDecomposition -> {
            logicalDecompositionCounter[0]++;

            String name = logicalDecomposition.getName();
            codeAnalysisResults.getFilesHistoryAnalysisResults().getChangeDistributionPerLogicalDecomposition().stream()
                    .filter(d -> d.getName().equalsIgnoreCase(name)).forEach(distribution -> {
                        addChangeDetailsForLogicalDecomposition(report, distribution);
                    });
        });
    }

    private void addChangeDetailsForLogicalDecomposition(RichTextReport report, FileAgeDistributionPerLogicalDecomposition logicalDecomposition) {
        report.startSubSection("" + logicalDecomposition.getName()
                + " (" + THE_NUMBER_OF_FILE_CHANGES.toLowerCase() + ")", THE_NUMBER_OF_FILE_CHANGES_DESCRIPTION);
        report.startScrollingDiv();
        report.addHtmlContent(RiskDistributionStatsReportUtils.getRiskDistributionPerKeySvgBarChart(logicalDecomposition.getDistributionPerComponent(), changeFrequencyLabels, Palette.getHeatPalette()));
        report.endDiv();
        report.endSection();

    }

    private void addMostChangedFilesList(RichTextReport report) {
        List<SourceFile> youngestFiles = codeAnalysisResults.getFilesHistoryAnalysisResults().getMostChangedFiles();
        report.startSection("Most Frequently Changed Files (Top " + youngestFiles.size() + ")", "");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addParagraph("<a href='../data/text/mainFilesWithHistory.txt' target='_blank'>See data for all files...</a>");
        report.addHtmlContent(FilesReportUtils.getFilesTable(youngestFiles, cacheSourceFiles, true, false, 500).toString());
        report.endSection();
    }

    private void addFilesWithMostContributors(RichTextReport report) {
        List<SourceFile> files = codeAnalysisResults.getFilesHistoryAnalysisResults().getFilesWithMostContributors();
        report.startSection("Files With Most Contributors (Top " + files.size() + ")", "Based on the number of unique email addresses found in commits.");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addParagraph("<a href='../data/text/mainFilesWithHistory.txt' target='_blank'>See data for all files...</a>");
        report.addHtmlContent(FilesReportUtils.getFilesTable(files, cacheSourceFiles, true, false, 500).toString());
        report.endSection();
    }

    private void addFilesWithLeastContributors(RichTextReport report) {
        List<SourceFile> files = codeAnalysisResults.getFilesHistoryAnalysisResults().getFilesWithLeastContributors();
        report.startSection("Files With Least Contributors (Top " + files.size() + ")", "Based on the number of unique email addresses found in commits.");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addParagraph("<a href='../data/text/mainFilesWithHistory.txt' target='_blank'>See data for all files...</a>");
        report.addHtmlContent(FilesReportUtils.getFilesTable(files, cacheSourceFiles, true, false, 500).toString());
        report.endSection();
    }

    private void addCorrelations(RichTextReport report) {
        if (!codeAnalysisResults.getCodeConfiguration().getAnalysis().isSkipCorrelations() && codeAnalysisResults.getContributorsAnalysisResults().getContributors().size() > 0) {
            report.startSection("Correlations", "");
            CorrelationDiagramGenerator<FileModificationHistory> correlationDiagramGenerator = new CorrelationDiagramGenerator<>(report,
                    codeAnalysisResults.getFilesHistoryAnalysisResults().getHistory(Integer.MAX_VALUE));

            final Map<String,Integer> linesOfCodeMap = new HashMap<>();
            final Map<String,Integer> countributorsCountMap = new HashMap<>();

            codeAnalysisResults.getFilesAnalysisResults().getAllFiles().forEach(sourceFile -> {
                linesOfCodeMap.put(sourceFile.getRelativePath(), sourceFile.getLinesOfCode());
                countributorsCountMap.put(sourceFile.getRelativePath(), sourceFile.getFileModificationHistory().countContributors());
            });
            ProcessingStopwatch.start("reporting/file update frequency/correlations");
            correlationDiagramGenerator.addCorrelations("File Size vs. Number of Changes", "lines of code", "# changes",
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getDates().size(),
                    p -> p.getPath());

            correlationDiagramGenerator.addCorrelations("Number of Contributors vs. Number of Changes", "# contributors", "# changes",
                    p -> countributorsCountMap.getOrDefault(p.getPath(), 0),
                    p -> p.getDates().size(),
                    p -> p.getPath());

            correlationDiagramGenerator.addCorrelations("Number of Contributors vs. File Size", "# contributors", "lines of code",
                    p -> countributorsCountMap.getOrDefault(p.getPath(), 0),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());

            ProcessingStopwatch.end("reporting/file update frequency/correlations");
            report.endSection();
        }
    }
}

class TrendReportGenerator {
    private RichTextReport report;
    private File codeConfigurationFile;

    public TrendReportGenerator(File codeConfigurationFile) {
        this.codeConfigurationFile = codeConfigurationFile;
    }

    public RichTextReport generateReport(CodeAnalysisResults codeAnalysisResults, RichTextReport report) {
        this.report = report;
        report.startSection("Intro", "");
        report.startUnorderedList();
        report.addListItem("Trend report shows difference in metric between the latest measurements and previous reference measurements.");
        report.endUnorderedList();

        report.startShowMoreBlock("Learn more...");
        report.startUnorderedList();
        report.addListItem("For more insights in the value of trend analysis, Sokrates recommends reading the section \"Favor tracking trends over absolute numbers\" in the article <a href='https://martinfowler.com/articles/useOfMetrics.html#FavorTrackingTrendsOverAbsoluteNumbers' target='_blank'>An Appropriate Use of Metrics</a>, (MartinFowler.com).");
        report.endUnorderedList();
        report.endShowMoreBlock();
        report.endSection();

        TrendAnalysisConfig trendAnalysis = codeAnalysisResults.getCodeConfiguration().getTrendAnalysis();
        List<ReferenceAnalysisResult> referenceResults = trendAnalysis.getReferenceAnalyses(codeConfigurationFile.getParentFile());

        if (referenceResults.size() == 0) {
            report.addParagraph("No reference analysis results have been defined.");
            return report;
        }

        summarize(codeAnalysisResults, report, referenceResults);

        referenceResults.forEach(result -> {
            CodeAnalysisResults refData = new ReferenceResultsLoader().getRefData(result.getAnalysisResultsZipFile());
            processReferenceResults(codeAnalysisResults, refData, report, result);
        });

        return report;
    }

    private void summarize(CodeAnalysisResults currentAnalysisResults, RichTextReport report, List<ReferenceAnalysisResult> referenceResults) {
        List<CodeAnalysisResults> analysisResultsList = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        analysisResultsList.add(currentAnalysisResults);
        labels.add("Current");
        int maxMainLoc[] = {currentAnalysisResults.getMainAspectAnalysisResults().getLinesOfCode()};
        int maxTestLoc[] = {currentAnalysisResults.getTestAspectAnalysisResults().getLinesOfCode()};
        int maxTotalLoc[] = {maxMainLoc[0] + maxTestLoc[0]};
        referenceResults.forEach(result -> {
            CodeAnalysisResults refData = new ReferenceResultsLoader().getRefData(result.getAnalysisResultsZipFile());
            if (refData != null && refData.getCodeConfiguration() != null) {
                analysisResultsList.add(refData);
                labels.add(result.getLabel());

                int refLocMain = refData.getMainAspectAnalysisResults().getLinesOfCode();
                int refLocTest = refData.getTestAspectAnalysisResults().getLinesOfCode();

                maxMainLoc[0] = Math.max(refLocMain, maxMainLoc[0]);
                maxTestLoc[0] = Math.max(refLocTest, maxTestLoc[0]);
                maxTotalLoc[0] = Math.max(refLocMain + refLocTest, maxTotalLoc[0]);
            }
        });
        addCodeVolumeSummarySection(report, analysisResultsList, labels, maxTotalLoc[0]);
    }

    private void addCodeVolumeSummarySection(RichTextReport report, List<CodeAnalysisResults> analysisResultsList, List<String> labels, int maxTotalLoc) {
        report.startSection("Summary: Code Volume Change", "");
        report.startDiv("width: 100%; overflow-x: auto");
        report.startTable();
        report.addTableHeader("", "Main LOC", "", "Test LOC", "Duplication");

        int index[] = {0};
        analysisResultsList.forEach(refData -> {
            int mainLoc = refData.getMainAspectAnalysisResults().getLinesOfCode();
            int testLoc = refData.getTestAspectAnalysisResults().getLinesOfCode();
            double duplicationPercentage = refData.getDuplicationAnalysisResults().getOverallDuplication().getDuplicationPercentage().doubleValue();

            report.startTableRow();
            report.addTableCell(labels.get(index[0]));
            report.addTableCell(FormattingUtils.formatCount(mainLoc), "text-align: right");
            report.addTableCell(getVolumeSvgBarChart(maxTotalLoc, mainLoc, testLoc));
            report.addTableCell(FormattingUtils.formatCount(testLoc), "text-align: right");
            report.addTableCell(getDuplicationChart(duplicationPercentage));
            report.endTableRow();
            index[0]++;
        });
        report.endTable();
        report.addLineBreak();
        report.addParagraph("See the details (TXT):");
        report.startUnorderedList();
        report.startListItem();
        report.addNewTabLink("all metrics", "../data/text/metrics_trend.txt");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("lines of code per extension", "../data/text/metrics_trend_loc_per_extension.txt");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("lines of code per logical component", "../data/text/metrics_trend_loc_logical_decompositions.txt");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("duplicated lines", "../data/text/metrics_trend_loc_duplication.txt");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("lines of code per file size category", "../data/text/metrics_trend_loc_file_size.txt");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("lines of code per unit size category", "../data/text/metrics_trend_unit_size_loc.txt");
        report.endListItem();
        report.startListItem();
        report.addNewTabLink("lines of code per conditional complexity catagory", "../data/text/metrics_trend_conditional_complexity_loc.txt");
        report.endListItem();
        report.endUnorderedList();
        report.endDiv();
        report.endSection();
    }

    private String getVolumeSvgBarChart(int maxTotalLoc, int mainLoc, int testLoc) {
        if (maxTotalLoc == 0) return "";

        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(310);
        chart.setMaxBarWidth((int) (300.0 * (mainLoc + testLoc) / maxTotalLoc));
        chart.setBarHeight(20);
        chart.setBarStartXOffset(2);
        return chart.getStackedBarSvg(Arrays.asList(mainLoc, testLoc), Palette.getDefaultPalette(), "", "");
    }

    private String getDuplicationChart(double duplicationPercentage) {
        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(110);
        chart.setMaxBarWidth(60);
        chart.setBarHeight(20);
        chart.setBarStartXOffset(2);
        chart.setActiveColor("crimson");
        return chart.getPercentageSvg(duplicationPercentage, "", FormattingUtils.getFormattedPercentage(duplicationPercentage) + "%");
    }

    private void processReferenceResults(CodeAnalysisResults codeAnalysisResults, CodeAnalysisResults refData, RichTextReport report, ReferenceAnalysisResult result) {
        String analysisResultsPath = result.getAnalysisResultsZipFile().getPath();
        if (refData != null) {
            report.startSection("Current vs. " + result.getLabel(), analysisResultsPath);
            report.startShowMoreBlock("Comparison summary...");
            new SummaryUtils().summarizeAndCompare(codeAnalysisResults, refData, report);
            report.endShowMoreBlock();

            report.addLineBreak();
            report.addLineBreak();

            report.startShowMoreBlock("Detailed comparison of all metrics...");
            report.startDiv("width: 100%; overflow-x: auto");
            report.startTable();
            report.addTableHeader("Metric", "Reference Value", "Current Value", "Difference");
            CodeAnalysisResults finalRefData = refData;
            codeAnalysisResults.getMetricsList().getMetrics().forEach(metric -> {
                addRow(metric, finalRefData);
            });
            report.endTable();
            report.endDiv();
            report.endShowMoreBlock();
            report.endSection();
        } else {
            report.addParagraph("ERROR: could not find the reference result file '" + analysisResultsPath + "'.");
        }
    }

    private void addRow(Metric metric, CodeAnalysisResults refData) {
        Metric refMetric = refData.getMetricsList().getMetricById(metric.getId());
        double currentValue = metric.getValue().doubleValue();
        double refValue = refMetric != null ? refMetric.getValue().doubleValue() : 0;

        report.startTableRow();
        report.startTableCell();
        report.addHtmlContent("<b>" + metric.getId() + "</b><br/>");
        report.endTableCell();


        report.startTableCell("text-align: center; color: lightgrey");
        report.addHtmlContent("" + ReportUtils.formatNumber(refValue));
        report.endTableCell();
        report.startTableCell("text-align: center");
        report.addHtmlContent("<b>" + ReportUtils.formatNumber(currentValue) + "</b>");
        report.endTableCell();

        addDiffCell(currentValue, refValue);

        report.endTableRow();
    }

    private void addDiffCell(double currentValue, double refValue) {
        double diff = currentValue - refValue;
        String diffText = getDiffText(diff, refValue);
        report.startTableCell("text-align: right; color: " +
                (diff == 0 ? "lightgrey" : (diff < 0 ? "#b9936c" : "#6b5b95")));
        if (diff > 0) {
            report.addHtmlContent("+" + diffText + " ⬆");
        } else if (diff < 0) {
            report.addHtmlContent("" + diffText + " ⬇ ");
        } else {
            report.addHtmlContent("" + diffText + "");
        }
        report.endTableCell();
    }

    private String getDiffText(double diff, double refValue) {
        double roundingError = 0.0000000000000000000001;
        String diffText;
        diff = ((int) diff * 100.0) / 100;
        if (Math.abs(diff) < roundingError) {
            diffText = "0 (0%)";
        } else if (Math.abs(refValue) < roundingError) {
            diffText = ReportUtils.formatNumber(diff) + " (NEW)";
        } else {
            double percentage = 100.0 * diff / refValue;
            diffText = ReportUtils.formatNumber(diff) + " (" + (percentage > 0 ? "+" : (percentage < 0 ? "-" : ""))
                    + FormattingUtils.getFormattedPercentage(Math.abs(percentage)) + "%)";
        }
        return diffText;
    }
}

class FileSizeReportGenerator {
    private final Thresholds fileSizeThresholds;
    private CodeAnalysisResults codeAnalysisResults;
    private List<String> labels;

    public FileSizeReportGenerator(CodeAnalysisResults codeAnalysisResults) {
        this.codeAnalysisResults = codeAnalysisResults;
        fileSizeThresholds = codeAnalysisResults.getCodeConfiguration().getAnalysis().getFileSizeThresholds();
        this.labels = fileSizeThresholds.getLabels();
    }

    public void addFileSizeToReport(RichTextReport report) {
        report.addParagraph("The distribution of size of files (measured in lines of code).", "margin-top: 12px; color: grey");
        report.startSection("Intro", "");
        report.startUnorderedList();
        report.addListItem("File size measurements show the distribution of size of files.");
        report.addListItem("Files are classified in four categories based on their size (lines of code): " +
                fileSizeThresholds.getNegligibleRiskLabel() + " (very small files), " +
                fileSizeThresholds.getLowRiskLabel() + " (small files), " +
                fileSizeThresholds.getMediumRiskLabel() + " (medium size files), " +
                fileSizeThresholds.getHighRiskLabel() + " (long files), " +
                fileSizeThresholds.getVeryHighRiskLabel() + "(very long files).");
        report.addListItem("It is a good practice to keep files small. Long files may become \"bloaters\", code that have increased to such gargantuan proportions that they are hard to work with.");
        report.endUnorderedList();
        report.endUnorderedList();

        report.startShowMoreBlock("Learn more...");
        report.startUnorderedList();
        report.addListItem("To learn more about bloaters and how to deal with long code structures, Sokrates recommends the following resources:");
        report.startUnorderedList();
        report.addListItem("<a target='_blank' href='https://sourcemaking.com/refactoring/smells/bloaters'>Refactoring bloaters</a>, sourcemaking.com");
        report.addListItem("<a target='_blank' href='https://sourcemaking.com/antipatterns/the-blob'>The Blob Software Development Anti-Pattern</a>, sourcemaking.com");

        report.endUnorderedList();
        report.endUnorderedList();
        report.endShowMoreBlock();
        report.endSection();

        ProcessingStopwatch.start("reporting/file size/overall");
        addGraphOverall(report, codeAnalysisResults.getFilesAnalysisResults().getOverallFileSizeDistribution());
        ProcessingStopwatch.end("reporting/file size/overall");
        ProcessingStopwatch.start("reporting/file size/per extension");
        addGraphPerExtension(report, codeAnalysisResults.getFilesAnalysisResults().getFileSizeDistributionPerExtension());
        ProcessingStopwatch.end("reporting/file size/per extension");
        ProcessingStopwatch.start("reporting/file size/per logical component");
        addGraphsPerLogicalComponents(report, codeAnalysisResults.getFilesAnalysisResults().getFileSizeDistributionPerLogicalDecomposition());
        ProcessingStopwatch.end("reporting/file size/per logical component");

        ProcessingStopwatch.start("reporting/file size/longest files");
        addLongestFilesList(report);
        ProcessingStopwatch.end("reporting/file size/longest files");
        ProcessingStopwatch.start("reporting/file size/files with most units");
        addFilesWithMostUnitsList(report);
        ProcessingStopwatch.end("reporting/file size/files with most units");
        ProcessingStopwatch.start("reporting/file size/files with most long lines");
        addFilesWithMostLongLines(report);
        ProcessingStopwatch.end("reporting/file size/files with most long lines");

        if (!codeAnalysisResults.getCodeConfiguration().getAnalysis().isSkipCorrelations() && codeAnalysisResults.getContributorsAnalysisResults().getContributors().size() > 0) {
            report.startSection("Correlations", "");
            CorrelationDiagramGenerator<FileModificationHistory> correlationDiagramGenerator = new CorrelationDiagramGenerator<>(report, codeAnalysisResults.getFilesHistoryAnalysisResults().getHistory(Integer.MAX_VALUE));

            final Map<String,Integer> linesOfCodeMap = new HashMap<>();

            codeAnalysisResults.getFilesAnalysisResults().getAllFiles().forEach(sourceFile -> {
                linesOfCodeMap.put(sourceFile.getRelativePath(), sourceFile.getLinesOfCode());
            });

            ProcessingStopwatch.start("reporting/file size/correlations");
            correlationDiagramGenerator.addCorrelations("File Size vs. Commits (all time)", "commits (all time)", "lines of code",
                    p -> p.getCommits().size(),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());

            correlationDiagramGenerator.addCorrelations("File Size vs. Contributors (all time)", "contributors (all time)", "lines of code",
                    p -> p.countContributors(),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());


            report.addHorizontalLine();

            correlationDiagramGenerator.addCorrelations("File Size vs. Commits (30 days)", "commits (30d)", "lines of code",
                    p -> p.getCommits().stream().filter(c -> DateUtils.isDateWithinRange(c.getDate(), 30)).count(),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());

            correlationDiagramGenerator.addCorrelations("File Size vs. Contributors (30 days)", "contributors (30d)", "lines of code",
                    p -> countContributors(p, 30),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());

            report.addHorizontalLine();

            correlationDiagramGenerator.addCorrelations("File Size vs. Commits (90 days)", "commits (90d)", "lines of code",
                    p -> p.getCommits().stream().filter(c -> DateUtils.isDateWithinRange(c.getDate(), 90)).count(),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());


            correlationDiagramGenerator.addCorrelations("File Size vs. Contributors (90 days)", "contributors (90d)", "lines of code",
                    p -> countContributors(p, 90),
                    p -> linesOfCodeMap.getOrDefault(p.getPath(), 0),
                    p -> p.getPath());


            ProcessingStopwatch.end("reporting/file size/correlations");
            report.endSection();
        }
    }

    private long countContributors(FileModificationHistory p, int rangeInDays) {
        Stream<CommitInfo> commitInfoStream = p.getCommits().stream().filter(c -> DateUtils.isDateWithinRange(c.getDate(), rangeInDays));
        Set<String> contributorIds = new HashSet<>();
        commitInfoStream.forEach(commit -> contributorIds.add(commit.getEmail()));
        return contributorIds.size();
    }

    private void addGraphOverall(RichTextReport report, SourceFileSizeDistribution distribution) {
        report.startSection("File Size Overall", "");
        report.startUnorderedList();
        report.addListItem("There are "
                + "<a href='../data/text/mainFiles.txt' target='_blank'>"
                + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalCount())
                + " files</a> with " + RichTextRenderingUtils.renderNumberStrong(distribution.getTotalValue())
                + " lines of code" +
                ".");
        report.startUnorderedList();
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskCount())
                + " very long files (" + RichTextRenderingUtils.renderNumberStrong(distribution.getVeryHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskCount())
                + " long files (" + RichTextRenderingUtils.renderNumberStrong(distribution.getHighRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskCount())
                + " medium size files (" + RichTextRenderingUtils.renderNumberStrong(distribution.getMediumRiskValue())
                + " lines of codeclsfd_ftr_w_mp_ins)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskCount())
                + " small files (" + RichTextRenderingUtils.renderNumberStrong(distribution.getLowRiskValue())
                + " lines of code)");
        report.addListItem(RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskCount())
                + " very small files (" + RichTextRenderingUtils.renderNumberStrong(distribution.getNegligibleRiskValue())
                + " lines of code)");
        report.endUnorderedList();
        report.endUnorderedList();
        report.addHtmlContent(PieChartUtils.getRiskDistributionChart(distribution, labels));
        report.addLineBreak();
        report.addLineBreak();
        report.addHtmlContent("explore: ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_loc_coloring.html'>grouped by folders</a>");
        report.addHtmlContent(" | ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_circles_main_loc_coloring_categories.html'>grouped by size</a>");
        report.addHtmlContent(" | ");
        report.addHtmlContent("<a target='_blank' href='visuals/zoomable_sunburst_main.html'>sunburst</a> | ");
        report.addHtmlContent("<a target='_blank' href='visuals/files_3d.html'>3D view</a>");
        report.endSection();
    }

    private void addGraphPerExtension(RichTextReport report, List<RiskDistributionStats> sourceFileSizeDistribution) {
        report.startSection("File Size per Extension", "");
        report.addHtmlContent(RiskDistributionStatsReportUtils.getRiskDistributionPerKeySvgBarChart(sourceFileSizeDistribution, labels));
        report.endSection();
    }

    private void addGraphsPerLogicalComponents(RichTextReport report, List<FileDistributionPerLogicalDecomposition> fileDistributionPerLogicalDecompositions) {
        report.startSection("File Size per Logical Decomposition", "");
        fileDistributionPerLogicalDecompositions.forEach(logicalDecomposition -> {
            report.startSubSection("" + logicalDecomposition.getName() + "", "");
            report.startScrollingDiv();
            report.addHtmlContent(RiskDistributionStatsReportUtils.getRiskDistributionPerKeySvgBarChart(logicalDecomposition.getFileSizeDistributionPerComponent(), labels));
            report.endDiv();
            report.endSection();
        });
        report.endSection();
    }

    private void addLongestFilesList(RichTextReport report) {
        List<SourceFile> longestFiles = codeAnalysisResults.getFilesAnalysisResults().getLongestFiles();
        report.startSection("Longest Files (Top " + longestFiles.size() + ")", "");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addHtmlContent(FilesReportUtils.getFilesTable(longestFiles, cacheSourceFiles, false, false).toString());
        report.endSection();
    }

    private void addFilesWithMostUnitsList(RichTextReport report) {
        List<SourceFile> filesList = codeAnalysisResults.getFilesAnalysisResults().getFilesWithMostUnits();
        filesList.sort((o1, o2) -> o2.getUnitsCount() - o1.getUnitsCount());
        filesList = filesList.subList(0, Math.min(getMaxTopListSize(), filesList.size()));
        report.startSection("Files With Most Units (Top " + filesList.size() + ")", "");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addHtmlContent(FilesReportUtils.getFilesTable(filesList, cacheSourceFiles, false, false).toString());
        report.endSection();
    }

    private int getMaxTopListSize() {
        return codeAnalysisResults.getCodeConfiguration().getAnalysis().getMaxTopListSize();
    }

    private void addFilesWithMostLongLines(RichTextReport report) {
        int threshold = 120;
        List<SourceFile> filesList = codeAnalysisResults.getFilesAnalysisResults().getAllFiles()
                .stream().filter(sourceFile -> sourceFile.getLongLinesCount(threshold) > 0).collect(Collectors.toList());
        int count = filesList.size();
        int sum = filesList.stream().map(s -> (int) s.getLongLinesCount(120)).collect(Collectors.summingInt(Integer::intValue));
        filesList.sort((o1, o2) -> (int) (o2.getLongLinesCount(threshold) - o1.getLongLinesCount(threshold)));
        filesList = filesList.subList(0, Math.min(getMaxTopListSize(), filesList.size()));
        report.startSection("Files With Long Lines (Top " + filesList.size() + ")", "");
        report.addParagraph("There " + (count == 1 ? "is only one file" : "are <b>" + count + "</b> files") + " with lines longer than 120 characters. In total, there " + (sum == 1 ? "is only one long line" : "are <b>" + sum + "</b> long lines") + ".");
        boolean cacheSourceFiles = codeAnalysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles();
        report.addHtmlContent(FilesReportUtils.getFilesTable(filesList, cacheSourceFiles, false, true).toString());
        report.endSection();
    }
}
