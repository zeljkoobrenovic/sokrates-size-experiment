/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.common.renderingutils.RichTextRenderingUtils;
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
import nl.obren.sokrates.sourcecode.dependencies.DependencyUtils;
import nl.obren.sokrates.sourcecode.filehistory.FilePairChangedTogether;
import nl.obren.sokrates.sourcecode.filehistory.TemporalDependenciesHelper;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
            new LogicalComponentsIntro(report, forceSkipStaticDependencies).appendIntroduction();
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
        addDependenciesSummaryList(logicalDecomposition, componentDependencies);

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
        addDependencyGraphs(logicalDecomposition, dependenciesAboveThreshold, componentsAboveThreshold, graphvizDependencyRenderer);

        report.addLineBreak();
        report.addLineBreak();
        new ComponentDependenciesDetailsRenderer(report).addMoreDetailsSection(logicalDecomposition, componentDependencies);
        report.addLineBreak();
        report.addLineBreak();

        new IndirectDependenciesRenderer(report, this).renderIndirectDependencies(componentNames, graphvizDependencyRenderer, renderingOptions, renderWithoutDependencies, dependenciesAboveThreshold);
        report.addLineBreak();
        report.addLineBreak();
        report.addLineBreak();
    }

    private void addDependenciesSummaryList(LogicalDecompositionAnalysisResults logicalDecomposition, List<ComponentDependency> componentDependencies) {
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
    }

    private void addDependencyGraphs(LogicalDecompositionAnalysisResults logicalDecomposition, List<ComponentDependency> dependenciesAboveThreshold, ArrayList<String> componentsAboveThreshold, GraphvizDependencyRenderer graphvizDependencyRenderer) {
        List<ComponentGroup> componentGroups = ComponentGroupsUtils.getComponentGroups(logicalDecomposition, dependenciesAboveThreshold, componentsAboveThreshold);
        String graphId = addDependencyGraphVisuals(dependenciesAboveThreshold, componentsAboveThreshold, componentGroups, graphvizDependencyRenderer);
        report.addLineBreak();
        report.addNewTabLink("Open 2D force graph...", "visuals/force_2d_" + graphId + ".html");
        report.addLineBreak();
        report.addNewTabLink("Open 3D force graph...", "visuals/force_3d_" + graphId + ".html");

        if (componentGroups.size() > 0) {
            report.addLevel4Header("Group Dependencies");
            List<ComponentDependency> groupDependencies = ComponentGroupsUtils.getGroupDependencies(dependenciesAboveThreshold, componentGroups);
            addDependencyGraphVisuals(groupDependencies,
                    componentsAboveThreshold.stream().filter(c -> c.equalsIgnoreCase(ComponentGroupsUtils.getGroup(c, componentGroups))).collect(Collectors.toCollection(ArrayList::new)),
                    new ArrayList<>(), graphvizDependencyRenderer);
            report.addLineBreak();
        }
    }

    boolean isComponentInDependency(List<ComponentDependency> dependencies, String component) {
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

    String addDependencyGraphVisuals(List<ComponentDependency> componentDependencies, List<String> componentNames, List<ComponentGroup> componentGroups, GraphvizDependencyRenderer graphvizDependencyRenderer) {
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


}
