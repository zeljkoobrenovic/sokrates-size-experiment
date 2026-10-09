/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.charts.SimpleOneBarChart;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.utils.AnimalIcons;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import nl.obren.sokrates.sourcecode.stats.RiskDistributionStats;
import nl.obren.sokrates.sourcecode.stats.SourceFileAgeDistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The repository statistics sections of the landscape report: file age and freshness bars (with the no-history row)
 * and the repositories size distribution ("zoo").
 */
class LandscapeReportStatisticsSection {
    private static final int BAR_WIDTH = 800;
    private static final int BAR_HEIGHT = 42;
    private final RichTextReport landscapeReport;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final SourceFileAgeDistribution overallFileLastModifiedDistribution;
    private final SourceFileAgeDistribution overallFileFirstModifiedDistribution;

    LandscapeReportStatisticsSection(RichTextReport landscapeReport, LandscapeAnalysisResults landscapeAnalysisResults,
                                     SourceFileAgeDistribution overallFileFirstModifiedDistribution,
                                     SourceFileAgeDistribution overallFileLastModifiedDistribution) {
        this.landscapeReport = landscapeReport;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.overallFileFirstModifiedDistribution = overallFileFirstModifiedDistribution;
        this.overallFileLastModifiedDistribution = overallFileLastModifiedDistribution;
    }

    void addFileAgeAndFreshnessSection() {
        landscapeReport.startSubSection("File Age and Freshness", "Lines of code in files first/last updated more than a year ago | 6 to 12 months ago | 3 to 6 months ago | 1 to 3 months ago | month or less ago");

        landscapeReport.startTable();
        landscapeReport.startTableRow();
        landscapeReport.addTableCell("old", "border: none");
        landscapeReport.startTableCell("border: none");
        landscapeReport.startDivWithLabel("file age:\n" + overallFileFirstModifiedDistribution.getDescription(), "");
        landscapeReport.addHtmlContent(getRiskProfileVisual(overallFileFirstModifiedDistribution, Palette.getAgePalette()));
        landscapeReport.endDiv();
        landscapeReport.endTableCell();
        landscapeReport.addTableCell("new", "border: none");
        landscapeReport.endTableRow();

        landscapeReport.startTableRow();
        landscapeReport.addTableCell("stale", "border: none");
        landscapeReport.startTableCell("border: none");
        landscapeReport.startDivWithLabel("file freshness:\n" + overallFileLastModifiedDistribution.getDescription(), "");
        landscapeReport.addHtmlContent(getRiskProfileVisual(overallFileLastModifiedDistribution, Palette.getFreshnessPalette()));
        landscapeReport.endDiv();
        landscapeReport.endTableCell();
        landscapeReport.addTableCell("fresh", "border: none");
        landscapeReport.endTableRow();

        addNoHistoryRow();

        landscapeReport.endTable();

        landscapeReport.endSection();
    }

    void addZooSection() {
        AnimalIcons icons = new AnimalIcons(64);
        List<String> animals = icons.getAnimals();
        List<String> animalsLocInfo = icons.getAnimalsLOCInfo();
        // Collections.reverse(animals);
        // Collections.reverse(animalsLocInfo);

        int totalLoc = landscapeAnalysisResults.getMainLoc();

        Map<String, List<RepositoryAnalysisResults>> animalCounts = new HashMap<>();
        List<RepositoryAnalysisResults> repositories = landscapeAnalysisResults.getRepositoryAnalysisResults();
        repositories.forEach(repositoryAnalysis -> {
            String animal = icons.getAnimalForMainLoc(repositoryAnalysis.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode());
            if (!animalCounts.containsKey(animal)) {
                animalCounts.put(animal, new ArrayList<>());
            }
            animalCounts.get(animal).add(repositoryAnalysis);
        });
        int maxCount[] = {0};
        animals.forEach(animal -> {
            int count = animalCounts.containsKey(animal) ? animalCounts.get(animal).size() : 0;
            maxCount[0] = Math.max(count, maxCount[0]);
        });

        landscapeReport.startSubSection("Repositories Size Distribution", "Size of repositories (main lines of code)");
        landscapeReport.startTable("font-size: 100%; margin-bottom: 6px;");

        landscapeReport.startTableRow();
        animalsLocInfo.forEach(animalInfo -> {
            landscapeReport.startTableCell("text-align: center; border: none; color: black;");
            landscapeReport.addContentInDiv(animalInfo);
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();

        addZooLocRow(animals, animalCounts, totalLoc);

        addZooCountRow(animals, animalCounts, maxCount[0], repositories);

        landscapeReport.endTable();

        landscapeReport.endSection();
    }

    private void addZooLocRow(List<String> animals, Map<String, List<RepositoryAnalysisResults>> animalCounts, int totalLoc) {
        landscapeReport.startTableRow();
        animals.forEach(animal -> {
            int count = animalCounts.containsKey(animal) ? animalCounts.get(animal).size() : 0;
            landscapeReport.startTableCell("vertical-align: bottom; text-align: center; border: none;" + (count > 0 ? "" : "color: grey; opacity: 0.4"));
            int loc = 0;
            if (count > 0) {
                loc += animalCounts.get(animal).stream()
                        .mapToInt(a -> a.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode())
                        .sum();
            }
            double percentage = totalLoc > 0 ? 100.0 * loc / totalLoc : 0;
            int width = totalLoc > 0 ? (200 * loc / totalLoc) : 0;
            if (count > 0 && width == 0) {
                width = 1;
            }
            landscapeReport.addContentInDiv(FormattingUtils.getFormattedPercentage(percentage) + "%", "font-size: 13px; ");
            landscapeReport.addContentInDiv(FormattingUtils.getSmallTextForNumber(loc) + "LOC", "font-size: 11px;");
            landscapeReport.startDiv("border: 1px solid #d0d0d0; width: 64px; margin-bottom: 4px; ");
            landscapeReport.addContentInDiv("", "background-color: blue; width: 100%; height: " + width + "px");
            landscapeReport.endDiv();
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }

    private void addZooCountRow(List<String> animals, Map<String, List<RepositoryAnalysisResults>> animalCounts, int maxCount, List<RepositoryAnalysisResults> repositories) {
        landscapeReport.startTableRow();
        animals.forEach(animal -> {
            int count = animalCounts.containsKey(animal) ? animalCounts.get(animal).size() : 0;
            landscapeReport.startTableCell("vertical-align: top; border: none;" + (count > 0 ? "" : "color: grey; opacity: 0.2"));
            int height = maxCount > 0 ? (int) Math.round(64.0 * count / maxCount) + 1 : 1;
            landscapeReport.addContentInDiv("", "background-color: lightgrey; width: 100%; height: " + height + "px");
            String info = "";
            if (count > 0) {
                info += animalCounts.get(animal).stream()
                        .map(a -> a.getAnalysisResults().getMetadata().getName() + " " + a.getAnalysisResults().getMainAspectAnalysisResults().getLinesOfCode())
                        .collect(Collectors.joining("\n"));
            }

            String perc = "";

            if (repositories.size() > 0) {
                long percValue = Math.round(100.0 * count / repositories.size());
                if (percValue == 0 && count > 0) {
                    perc = "<1%<br>";
                } else {
                    perc = percValue + "%<br>";
                }
            }

            landscapeReport.addContentInDivWithTooltip(perc + count + (count == 0 ? " repo" : " repos"), info, "font-size: 80%; width: 100%; text-align: center");
            landscapeReport.endTableCell();
        });
        landscapeReport.endTableRow();
    }

    private void addNoHistoryRow() {
        int mainLoc = landscapeAnalysisResults.getMainLoc();
        int mainFilesCount = landscapeAnalysisResults.getMainFilesCount();
        int values = getSumOfValues(overallFileLastModifiedDistribution);
        int counts = getSumOfCounts(overallFileLastModifiedDistribution);
        int filesWithoutHistoryCount = mainFilesCount - counts;
        int locWithoutHistory = mainLoc - values;
        if (filesWithoutHistoryCount > 0 && locWithoutHistory > 0) {
            landscapeReport.startTableRow();
            landscapeReport.addTableCell("no<br>history", "border: none");
            landscapeReport.startTableCell("border: none; padding-top: 3px;");
            landscapeReport.startDivWithLabel(FormattingUtils.formatCount(filesWithoutHistoryCount) + " files without commit history, " + FormattingUtils.formatCount(locWithoutHistory) + " lines of code (" + FormattingUtils.getFormattedPercentage(100.0 * locWithoutHistory / mainLoc) + "%)", "");
            landscapeReport.addHtmlContent(addFilesWithoutHistoryBar(locWithoutHistory, mainLoc));
            landscapeReport.endDiv();
            landscapeReport.endTableCell();
            landscapeReport.addTableCell("", "border: none");
            landscapeReport.endTableRow();
        }
    }

    static int getSumOfValues(SourceFileAgeDistribution distribution) {
        return distribution.getNegligibleRiskValue() + distribution.getLowRiskValue() + distribution.getMediumRiskValue() + distribution.getHighRiskValue() + distribution.getVeryHighRiskValue();
    }

    private int getSumOfCounts(SourceFileAgeDistribution distribution) {
        return distribution.getNegligibleRiskCount() + distribution.getLowRiskCount() + distribution.getMediumRiskCount() + distribution.getHighRiskCount() + distribution.getVeryHighRiskCount();
    }

    private String addFilesWithoutHistoryBar(int value, int total) {
        int width = (int) (BAR_WIDTH * (double) value / total);
        return "<svg width='" + (width + 2) + "' height='" + (BAR_HEIGHT) + "'>" +
                "<rect width='" + width + "' height='" + (BAR_HEIGHT - 2) + "' style='fill:rgb(200,200,200);stroke-width:1;stroke:rgb(150,150,150)'/>\n" +
                "</svg>";
    }

    private String getRiskProfileVisual(RiskDistributionStats distributionStats, Palette palette) {
        SimpleOneBarChart chart = new SimpleOneBarChart();
        chart.setWidth(BAR_WIDTH + 20);
        chart.setBarHeight(BAR_HEIGHT);
        chart.setMaxBarWidth(BAR_WIDTH);
        chart.setBarStartXOffset(0);

        List<Integer> values = Arrays.asList(
                distributionStats.getVeryHighRiskValue(),
                distributionStats.getHighRiskValue(),
                distributionStats.getMediumRiskValue(),
                distributionStats.getLowRiskValue(),
                distributionStats.getNegligibleRiskValue());

        return chart.getStackedBarSvg(values, palette, "", "");
    }
}
