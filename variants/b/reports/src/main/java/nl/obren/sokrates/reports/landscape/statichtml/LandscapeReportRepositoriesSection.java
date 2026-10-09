/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.renderingutils.VisualizationItem;
import nl.obren.sokrates.common.renderingutils.VisualizationTemplate;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.common.utils.ProcessingStopwatch;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.statichtml.repositories.LandscapeRepositoriesReport;
import nl.obren.sokrates.reports.landscape.utils.CorrelationDiagramGenerator;
import nl.obren.sokrates.reports.landscape.statichtml.repositories.TagMap;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.TagGroup;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * The repositories tab content of the landscape report (the short/long repository lists and the ignored repositories
 * note), the sub-landscape zoomable circle/sunburst exports and the repository correlation diagrams.
 */
class LandscapeReportRepositoriesSection {
    private static final Log LOG = LogFactory.getLog(LandscapeReportRepositoriesSection.class);
    private final RichTextReport landscapeReport;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final File reportsFolder;
    private final List<TagGroup> tagGroups;
    private final TagMap customTagsMap;
    private final RichTextReport landscapeRepositoriesReportShort;
    private final RichTextReport landscapeRepositoriesReportLong;

    LandscapeReportRepositoriesSection(RichTextReport landscapeReport, LandscapeAnalysisResults landscapeAnalysisResults, File reportsFolder,
                                       List<TagGroup> tagGroups, TagMap customTagsMap,
                                       RichTextReport landscapeRepositoriesReportShort, RichTextReport landscapeRepositoriesReportLong) {
        this.landscapeReport = landscapeReport;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.reportsFolder = reportsFolder;
        this.tagGroups = tagGroups;
        this.customTagsMap = customTagsMap;
        this.landscapeRepositoriesReportShort = landscapeRepositoriesReportShort;
        this.landscapeRepositoriesReportLong = landscapeRepositoriesReportLong;
    }

    private VisualizationItem getParent(Map<String, VisualizationItem> parents, List<String> pathElements) {
        String parentName = "";
        for (int i = 0; i < pathElements.size() - 1; i++) {
            if (parentName.length() > 0) {
                parentName += "/";
            }
            parentName += pathElements.get(i);
        }

        if (parents.containsKey(parentName)) {
            return parents.get(parentName);
        }

        VisualizationItem newParent = new VisualizationItem(parentName, 0);
        parents.put(parentName, newParent);

        if (parentName.length() > 0) {
            getParent(parents, pathElements.subList(0, pathElements.size() - 1)).getChildren().add(newParent);
        }

        return newParent;
    }

    void exportZoomableCircles(String type, List<RepositoryAnalysisResults> repositoryAnalysisResults, LandscapeReportGenerator.ZommableCircleCountExtractors zommableCircleCountExtractors) {
        Map<String, VisualizationItem> parents = new HashMap<>();
        VisualizationItem root = new VisualizationItem("", 0);
        parents.put("", root);

        repositoryAnalysisResults.forEach(analysisResults -> {
            String name = getRepositoryCircleName(analysisResults);
            String[] elements = name.split("/");
            LOG.info(name);
            if (elements.length > 1) {
                name = name.substring(elements[0].length() + 1);
            }
            int count = zommableCircleCountExtractors.getCount(analysisResults);
            if (count > 0) {
                VisualizationItem item = new VisualizationItem(name + " (" + FormattingUtils.getPlainTextForNumber(count) + ")", count);
                getParent(parents, Arrays.asList(elements)).getChildren().add(item);
            }
        });
        try {
            File folder = new File(reportsFolder, "visuals");
            folder.mkdirs();
            FileUtils.write(new File(folder, "sub_landscapes_zoomable_circles_" + type + ".html"), new VisualizationTemplate().renderZoomableCircles(root.getChildren()), UTF_8);
            FileUtils.write(new File(folder, "sub_landscapes_zoomable_sunburst_" + type + ".html"), new VisualizationTemplate().renderZoomableSunburst(root.getChildren()), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRepositoryCircleName(RepositoryAnalysisResults analysisResults) {
        String name = analysisResults.getSokratesRepositoryLink().getAnalysisResultsPath().replace("\\", "/");
        name = name.replace("/data/analysisResults.json", "");
        return name;
    }

    void addCorrelations() {
        List<RepositoryAnalysisResults> repositories = landscapeAnalysisResults.getRepositoryAnalysisResults();
        CorrelationDiagramGenerator<RepositoryAnalysisResults> correlationDiagramGenerator = new CorrelationDiagramGenerator<>(landscapeReport, repositories);

        correlationDiagramGenerator.addCorrelations("Recent Contributors vs. Commits (30 days)", "commits (30d)", "recent contributors (30d)",
                p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days(),
                p -> p.getAnalysisResults().getContributorsAnalysisResults().getContributors().stream().filter(c -> c.isActive(Contributor.RECENTLY_ACTIVITY_THRESHOLD_DAYS)).count(),
                p -> p.getAnalysisResults().getMetadata().getName());

        correlationDiagramGenerator.addCorrelations("Recent Contributors vs. Repository Main LOC", "main LOC", "recent contributors (30d)",
                p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode(),
                p -> p.getAnalysisResults().getContributorsAnalysisResults().getContributors().stream().filter(c -> c.isActive(Contributor.RECENTLY_ACTIVITY_THRESHOLD_DAYS)).count(),
                p -> p.getAnalysisResults().getMetadata().getName());

        correlationDiagramGenerator.addCorrelations("Recent Commits (30 days) vs. Repository Main LOC", "main LOC", "commits (30d)",
                p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode(),
                p -> p.getAnalysisResults().getContributorsAnalysisResults().getCommitsCount30Days(),
                p -> p.getAnalysisResults().getMetadata().getName());

        correlationDiagramGenerator.addCorrelations("Age in Years vs. Repository Main LOC", "main LOC", "age (years)",
                p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode(),
                p -> Math.round(10 * p.getAnalysisResults().getFilesHistoryAnalysisResults().getAgeInDays() / 365.0) / 10,
                p -> p.getAnalysisResults().getMetadata().getName());

        correlationDiagramGenerator.addCorrelations("Number of Files vs. Repository Main LOC", "main LOC", "# main files",
                p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode(),
                p -> p.getAnalysisResults().getMainAspectAnalysisResults().getFilesCount(),
                p -> p.getAnalysisResults().getMetadata().getName());

        correlationDiagramGenerator.addCorrelations("Duplication vs. Repository Main LOC", "main LOC", "% duplication",
                p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode(),
                p -> Math.round(10 * p.getAnalysisResults().getDuplicationAnalysisResults().getOverallDuplication().getDuplicationPercentage().doubleValue()) / 10,
                p -> p.getAnalysisResults().getMetadata().getName());
    }

    void addRepositoriesSection(List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        Collections.sort(repositoryAnalysisResults, (a, b) -> b.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode() - a.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode());
        LandscapeConfiguration configuration = landscapeAnalysisResults.getConfiguration();

        if (repositoryAnalysisResults.size() > 0) {
            int shortLimit = configuration.getRepositoriesShortListLimit();
            ProcessingStopwatch.start("reporting/repositories/short report");
            new LandscapeRepositoriesReport(landscapeAnalysisResults, shortLimit, "See the full list of repositories...", "repositories.html", customTagsMap)
                    .saveRepositoriesReport(landscapeRepositoriesReportShort, reportsFolder, repositoryAnalysisResults, tagGroups);
            ProcessingStopwatch.end("reporting/repositories/short report");

            List<NumericMetric> repositorySizes = new ArrayList<>();
            repositoryAnalysisResults.forEach(repository -> {
                LOG.info("Adding " + repository.getSokratesRepositoryLink().getAnalysisResultsPath());
                CodeAnalysisResults analysisResults = repository.getAnalysisResults();
                repositorySizes.add(new NumericMetric(analysisResults.getMetadata().getName(), analysisResults.getMainAspectAnalysisResults().getLinesOfCode()));
            });

            landscapeReport.addHtmlContent("<iframe src='repositories-short.html' frameborder=0 style='height: calc(100vh - 300px); width: 100%; margin-left: 0; margin-bottom: 0px; padding: 0;'></iframe>");

            if (repositoryAnalysisResults.size() > shortLimit) {
                ProcessingStopwatch.start("reporting/repositories/long report");
                new LandscapeRepositoriesReport(landscapeAnalysisResults, configuration.getRepositoriesListLimit(), customTagsMap)
                        .saveRepositoriesReport(landscapeRepositoriesReportLong, reportsFolder, repositoryAnalysisResults, tagGroups);
                ProcessingStopwatch.end("reporting/repositories/long report");
            }
        }

        List<RepositoryAnalysisResults> ignoredRepositoriess = landscapeAnalysisResults.getIgnoredRepositoryAnalysisResults();
        if (ignoredRepositoriess.size() > 0) {
            String lastUpdatedBefore = configuration.getIgnoreRepositoriesLastUpdatedBefore();
            int thresholdContributors = configuration.getRepositoryThresholdContributors();
            int thresholdLocMain = configuration.getRepositoryThresholdLocMain();
            int ignoredLocMain = ignoredRepositoriess.stream().mapToInt(p -> p.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode()).reduce(0, (a, b) -> a + b);
            landscapeReport.addContentInDiv("<a href='data/ignoredRepositories.txt' target='_blank'>" + ignoredRepositoriess.size() +
                            " repositories (" + FormattingUtils.getSmallTextForNumber(ignoredLocMain) + " lines of main code) are ignored</a> based on any of the following criteria: " +
                            (StringUtils.isNoneBlank(lastUpdatedBefore) ? "not updated after " + lastUpdatedBefore + "; " : "") +
                            ((thresholdContributors > 0) ? "have < " + FormattingUtils.formatCountPlural(thresholdContributors, "contributor", "contributors") + "; " : "") +
                            (thresholdLocMain > 0 ? "have less than " + thresholdLocMain + " lines of main code" : ""),
                    "color: grey; margin: 10px; font-size: 80%");
        }

    }
}
