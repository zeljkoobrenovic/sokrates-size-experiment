/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.cli;

import nl.obren.sokrates.common.renderingutils.Thresholds;
import nl.obren.sokrates.common.renderingutils.VisualizationItem;
import nl.obren.sokrates.common.renderingutils.VisualizationTemplate;
import nl.obren.sokrates.common.renderingutils.charts.Palette;
import nl.obren.sokrates.common.renderingutils.force3d.Force3DLink;
import nl.obren.sokrates.common.renderingutils.force3d.Force3DNode;
import nl.obren.sokrates.common.renderingutils.force3d.Force3DObject;
import nl.obren.sokrates.common.renderingutils.x3d.Unit3D;
import nl.obren.sokrates.common.renderingutils.x3d.X3DomExporter;
import nl.obren.sokrates.common.utils.BasicColorInfo;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.core.AnalysisConfig;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.stats.SourceFileSizeDistribution;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

class ReportVisualsGenerator {
    private static final Log LOG = LogFactory.getLog(ReportVisualsGenerator.class);
    private CodeConfiguration codeConfiguration;

    ReportVisualsGenerator(CodeConfiguration codeConfiguration) {
        this.codeConfiguration = codeConfiguration;
    }

    static BasicColorInfo getFileSizeColor(SourceFileSizeDistribution distribution, int linesOfCode) {
        if (linesOfCode <= distribution.getLowRiskThreshold()) {
            return Thresholds.RISK_GREEN;
        } else if (linesOfCode <= distribution.getMediumRiskThreshold()) {
            return Thresholds.RISK_LIGHT_GREEN;
        } else if (linesOfCode <= distribution.getHighRiskThreshold()) {
            return Thresholds.RISK_YELLOW;
        } else if (linesOfCode <= distribution.getVeryHighRiskThreshold()) {
            return Thresholds.RISK_ORANGE;
        } else {
            return Thresholds.RISK_RED;
        }
    }

    void generateVisuals(File reportsFolder, CodeAnalysisResults analysisResults) {
        generateComponentVisuals(reportsFolder, analysisResults);

        generateFileVisuals(reportsFolder, analysisResults);

    }

    private void generateComponentVisuals(File reportsFolder, CodeAnalysisResults analysisResults) {
        AtomicInteger index = new AtomicInteger();
        analysisResults.getLogicalDecompositionsAnalysisResults().forEach(logicalDecomposition -> {
            index.getAndIncrement();
            List<VisualizationItem> items = new ArrayList<>();
            Force3DObject force3DObject = new Force3DObject();
            logicalDecomposition.getComponents().forEach(component -> {
                items.add(new VisualizationItem(component.getName(), component.getLinesOfCode()));
                force3DObject.getNodes().add(new Force3DNode(component.getName(), component.getLinesOfCode()));
            });
            logicalDecomposition.getComponentDependencies().forEach(dependency -> {
                force3DObject.getLinks().add(new Force3DLink(dependency.getFromComponent(), dependency.getToComponent(), dependency.getCount()));
            });
            try {
                String nameSuffix = "components_" + index.toString() + ".html";
                String nameSuffixDependencies = "dependencies_" + index.toString() + ".html";
                File folder = new File(reportsFolder, "html/visuals");
                folder.mkdirs();
                FileUtils.write(new File(folder, "bubble_chart_" + nameSuffix), new VisualizationTemplate().renderBubbleChart(items), UTF_8);
                FileUtils.write(new File(folder, "tree_map_" + nameSuffix), new VisualizationTemplate().renderTreeMap(items), UTF_8);
                FileUtils.write(new File(folder, "force_2d_" + nameSuffixDependencies), new VisualizationTemplate().render2DForceGraph(force3DObject), UTF_8);
                FileUtils.write(new File(folder, "force_3d_" + nameSuffixDependencies), new VisualizationTemplate().render3DForceGraph(force3DObject), UTF_8);

                generate3DUnitsView(folder, analysisResults);
            } catch (IOException e) {
                LOG.warn(e);
            }
        });
    }

    private void generateFileVisuals(File reportsFolder, CodeAnalysisResults analysisResults) {
        try {
            File folder = new File(reportsFolder, "html/visuals");
            folder.mkdirs();

            List<SourceFile> mainSourceFiles = analysisResults.getMainAspectAnalysisResults().getAspect().getSourceFiles();
            generateFileStructureExplorers("main", folder, mainSourceFiles);
            generateFileStructureExplorers("test", folder, analysisResults.getTestAspectAnalysisResults().getAspect().getSourceFiles());
            generateFileStructureExplorers("generated", folder, analysisResults.getGeneratedAspectAnalysisResults().getAspect().getSourceFiles());
            generateFileStructureExplorers("build", folder, analysisResults.getBuildAndDeployAspectAnalysisResults().getAspect().getSourceFiles());
            generateFileStructureExplorers("other", folder, analysisResults.getOtherAspectAnalysisResults().getAspect().getSourceFiles());

            addCommitZoomableCircles("main", folder, mainSourceFiles, 30);
            addCommitZoomableCircles("main", folder, mainSourceFiles, 90);
            addCommitZoomableCircles("main", folder, mainSourceFiles, 180);
            addCommitZoomableCircles("main", folder, mainSourceFiles, 365);
            addCommitZoomableCircles("main", folder, mainSourceFiles, 0);

            addContributorsZoomableCircles("main", folder, mainSourceFiles, 30);
            addContributorsZoomableCircles("main", folder, mainSourceFiles, 90);
            addContributorsZoomableCircles("main", folder, mainSourceFiles, 180);
            addContributorsZoomableCircles("main", folder, mainSourceFiles, 365);
            addContributorsZoomableCircles("main", folder, mainSourceFiles, 0);

            addRiskColoredZoomableCircles(folder, mainSourceFiles, "loc", codeConfiguration.getAnalysis().getFileSizeThresholds(), Palette.getRiskPalette(), (sourceFile) -> sourceFile.getLinesOfCode(), (sourceFile) -> sourceFile.getLinesOfCode());

            addRiskColoredZoomableCircles(folder, mainSourceFiles, "age", codeConfiguration.getAnalysis().getFileAgeThresholds(), Palette.getAgePalette(), (sourceFile) -> sourceFile.getFileModificationHistory() != null ? sourceFile.getFileModificationHistory().daysSinceFirstUpdate() : 0, (sourceFile) -> sourceFile.getLinesOfCode());
            addRiskColoredZoomableCircles(folder, mainSourceFiles, "freshness", codeConfiguration.getAnalysis().getFileAgeThresholds(), Palette.getFreshnessPalette(),
                    (sourceFile) -> sourceFile.getFileModificationHistory() != null ? sourceFile.getFileModificationHistory().daysSinceLatestUpdate() : 0, (sourceFile) -> sourceFile.getLinesOfCode());

            addRiskColoredZoomableCircles(folder, mainSourceFiles, "update_frequency", codeConfiguration.getAnalysis().getFileUpdateFrequencyThresholds(), Palette.getHeatPalette(),
                    (sourceFile) -> sourceFile.getFileModificationHistory() != null ? sourceFile.getFileModificationHistory().getDates().size() : 0, (sourceFile) -> sourceFile.getLinesOfCode());

            addRiskColoredZoomableCircles(folder, mainSourceFiles, "contributors_count", codeConfiguration.getAnalysis().getFileContributorsCountThresholds(), Palette.getHeatPalette(),
                    (sourceFile) -> sourceFile.getFileModificationHistory() != null ? sourceFile.getFileModificationHistory().countContributors() : 0, (sourceFile) -> sourceFile.getLinesOfCode());

            generate3DUnitsView(folder, analysisResults);
        } catch (IOException e) {
            LOG.warn(e);
        }
    }

    private void generateFileStructureExplorers(String nameSuffix, File folder, List<SourceFile> sourceFiles) throws IOException {
        List<VisualizationItem> items = getZoomableCirclesItems(sourceFiles);
        FileUtils.write(new File(folder, "zoomable_circles_" + nameSuffix + ".html"), new VisualizationTemplate().renderZoomableCircles(items), UTF_8);
        FileUtils.write(new File(folder, "zoomable_sunburst_" + nameSuffix + ".html"), new VisualizationTemplate().renderZoomableSunburst(items), UTF_8);
    }

    private void addCommitZoomableCircles(String nameSuffix, File folder, List<SourceFile> sourceFiles, int daysAgo) throws IOException {
        List<VisualizationItem> commitItems = getZoomableCirclesCommitItems(sourceFiles, daysAgo > 0 ? daysAgo : CommandLineInterface.THOUSAND_YEARS);
        String suffix = daysAgo > 0 ? "_" + daysAgo + "_" + nameSuffix : "";
        FileUtils.write(new File(folder, "zoomable_circles_commits" + suffix + ".html"), new VisualizationTemplate().renderZoomableCircles(commitItems), UTF_8);
        FileUtils.write(new File(folder, "zoomable_sunburst_commits" + suffix + ".html"), new VisualizationTemplate().renderZoomableSunburst(commitItems), UTF_8);
    }

    private void addContributorsZoomableCircles(String nameSuffix, File folder, List<SourceFile> sourceFiles, int daysAgo) throws IOException {
        List<VisualizationItem> commitItems = getZoomableCirclesContributorItems(sourceFiles, daysAgo > 0 ? daysAgo : CommandLineInterface.THOUSAND_YEARS);
        String suffix = (daysAgo > 0 ? ("_" + daysAgo) : "") + ("_" + nameSuffix);
        FileUtils.write(new File(folder, "zoomable_circles_contributors" + suffix + ".html"), new VisualizationTemplate().renderZoomableCircles(commitItems), UTF_8);
        FileUtils.write(new File(folder, "zoomable_sunburst_contributors" + suffix + ".html"), new VisualizationTemplate().renderZoomableSunburst(commitItems), UTF_8);
    }

    private void addRiskColoredZoomableCircles(File folder, List<SourceFile> sourceFiles, String type,
                                               nl.obren.sokrates.sourcecode.threshold.Thresholds thresholds, Palette palette, DirectoryNode.SourceFileValueExtractor colorValueExtractor, DirectoryNode.SourceFileValueExtractor sizeValueExtractor) throws IOException {
        List<VisualizationItem> items = getZoomableCirclesRiskProfileItems(sourceFiles, thresholds, palette, colorValueExtractor, sizeValueExtractor);
        FileUtils.write(new File(folder, "zoomable_circles_main_" + type + "_coloring.html"), new VisualizationTemplate().renderZoomableCircles(items), UTF_8);

        List<VisualizationItem> itemsByCategory = getZoomableCirclesRiskProfileItemsCategories(sourceFiles, thresholds, palette, colorValueExtractor);
        FileUtils.write(new File(folder, "zoomable_circles_main_" + type + "_coloring_categories.html"), new VisualizationTemplate().renderZoomableCircles(itemsByCategory), UTF_8);
    }

    private List<VisualizationItem> getZoomableCirclesItems(List<SourceFile> sourceFiles) {
        DirectoryNode directoryTree = PathStringsToTreeStructure.createDirectoryTree(sourceFiles);
        if (directoryTree != null) {
            return directoryTree.toVisualizationItems();
        }

        return new ArrayList<>();
    }

    private List<VisualizationItem> getZoomableCirclesCommitItems(List<SourceFile> sourceFiles, int daysAgo) {
        DirectoryNode directoryTree = PathStringsToTreeStructure.createDirectoryTree(sourceFiles);
        if (directoryTree != null) {
            return directoryTree.toVisualizationCommitItems(daysAgo);
        }

        return new ArrayList<>();
    }

    private List<VisualizationItem> getZoomableCirclesContributorItems(List<SourceFile> sourceFiles, int daysAgo) {
        DirectoryNode directoryTree = PathStringsToTreeStructure.createDirectoryTree(sourceFiles);
        if (directoryTree != null) {
            return directoryTree.toVisualizationContributorItems(daysAgo);
        }

        return new ArrayList<>();
    }

    private List<VisualizationItem> getZoomableCirclesRiskProfileItems(
            List<SourceFile> sourceFiles, nl.obren.sokrates.sourcecode.threshold.Thresholds thresholds, Palette palette, DirectoryNode.SourceFileValueExtractor colorValueExtractor, DirectoryNode.SourceFileValueExtractor sizeValueExtractor) {
        DirectoryNode directoryTree = PathStringsToTreeStructure.createDirectoryTree(sourceFiles);
        if (directoryTree != null) {
            return directoryTree.toVisualizationRiskColoringItems(thresholds, palette, colorValueExtractor, sizeValueExtractor);
        }

        return new ArrayList<>();
    }

    private List<VisualizationItem> getZoomableCirclesRiskProfileItemsCategories(
            List<SourceFile> sourceFiles, nl.obren.sokrates.sourcecode.threshold.Thresholds thresholds, Palette palette, DirectoryNode.SourceFileValueExtractor valueExtractor) {

        VisualizationItem item1 = new VisualizationItem(thresholds.getNegligibleRiskLabel(), 0);
        VisualizationItem item2 = new VisualizationItem(thresholds.getLowRiskLabel(), 0);
        VisualizationItem item3 = new VisualizationItem(thresholds.getMediumRiskLabel(), 0);
        VisualizationItem item4 = new VisualizationItem(thresholds.getHighRiskLabel(), 0);
        VisualizationItem item5 = new VisualizationItem(thresholds.getVeryHighRiskLabel(), 0);

        sourceFiles.forEach(sourceFile -> {
            int value = valueExtractor.getValue(sourceFile);
            String path = sourceFile.getRelativePath();
            int loc = sourceFile.getLinesOfCode();
            if (value <= thresholds.getLow()) {
                item1.getChildren().add(new VisualizationItem(path, loc, PathStringsToTreeStructure.getColor(thresholds, palette, value)));
            } else if (value <= thresholds.getMedium()) {
                item2.getChildren().add(new VisualizationItem(path, loc, PathStringsToTreeStructure.getColor(thresholds, palette, value)));
            } else if (value <= thresholds.getHigh()) {
                item3.getChildren().add(new VisualizationItem(path, loc, PathStringsToTreeStructure.getColor(thresholds, palette, value)));
            } else if (value <= thresholds.getVeryHigh()) {
                item4.getChildren().add(new VisualizationItem(path, loc, PathStringsToTreeStructure.getColor(thresholds, palette, value)));
            } else {
                item5.getChildren().add(new VisualizationItem(path, loc, PathStringsToTreeStructure.getColor(thresholds, palette, value)));
            }
        });

        item1.setName(item1.getName() + " (" + item1.getChildren().size() + ")");
        item2.setName(item2.getName() + " (" + item2.getChildren().size() + ")");
        item3.setName(item3.getName() + " (" + item3.getChildren().size() + ")");
        item4.setName(item4.getName() + " (" + item4.getChildren().size() + ")");
        item5.setName(item5.getName() + " (" + item5.getChildren().size() + ")");

        return new ArrayList<>(Arrays.asList(item1, item2, item3, item4, item5));
    }

    private void generate3DUnitsView(File visualsFolder, CodeAnalysisResults analysisResults) {
        AnalysisConfig analysisConfig = analysisResults.getCodeConfiguration().getAnalysis();

        List<Unit3D> unit3DConditionalComplexity = new ArrayList<>();
        analysisResults.getUnitsAnalysisResults().getAllUnits().forEach(unit -> {
            BasicColorInfo color = Thresholds.getColor(Thresholds.UNIT_MCCABE, unit.getMcCabeIndex());
            unit3DConditionalComplexity.add(new Unit3D(unit.getLongName(), unit.getLinesOfCode(), color));
        });

        List<Unit3D> unit3DSize = new ArrayList<>();
        analysisResults.getUnitsAnalysisResults().getAllUnits().forEach(unit -> {
            BasicColorInfo color = Thresholds.getColor(Thresholds.UNIT_LINES, unit.getLinesOfCode());
            unit3DSize.add(new Unit3D(unit.getLongName(), unit.getLinesOfCode(), color));
        });

        List<Unit3D> files3D = new ArrayList<>();
        analysisResults.getCodeConfiguration().getMain().getSourceFiles().forEach(file -> {
            SourceFileSizeDistribution sourceFileSizeDistribution = new SourceFileSizeDistribution(analysisConfig.getFileSizeThresholds());
            BasicColorInfo color = getFileSizeColor(sourceFileSizeDistribution, file.getLinesOfCode());
            files3D.add(new Unit3D(file.getFile().getPath(), file.getLinesOfCode(), color));
        });

        new X3DomExporter(new File(visualsFolder, "units_3d_complexity.html"), "A 3D View of All Units (Conditional Complexity)", "Each block is one unit. The height of the block represents the file unit size in lines of code. The color of the unit represents its conditional complexity category.").export(unit3DConditionalComplexity, false, 10);

        new X3DomExporter(new File(visualsFolder, "units_3d_size.html"), "A 3D View of All Units (Unit Size)", "Each block is one unit. The height of the block represents the file unit size in lines of code. The color of the unit represents its size category.").export(unit3DSize, false, 10);

        new X3DomExporter(new File(visualsFolder, "files_3d.html"), "A 3D View of All Files", "Each block is one file. The height of the block represents the file relative size in lines of code. The color of the file represents its size category.").export(files3D, false, 50);
    }
}
