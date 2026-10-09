/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.dataexporters;

import nl.obren.sokrates.common.io.JsonGenerator;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.reports.utils.HtmlTemplateUtils;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.DuplicationAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.UnitsAnalysisResults;
import nl.obren.sokrates.sourcecode.aspects.NamedSourceCodeAspect;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.duplication.DuplicatedFileBlock;
import nl.obren.sokrates.sourcecode.duplication.DuplicationInstance;
import nl.obren.sokrates.sourcecode.lang.DefaultLanguageAnalyzer;
import nl.obren.sokrates.sourcecode.lang.LanguageAnalyzerFactory;
import nl.obren.sokrates.sourcecode.units.UnitInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static nl.obren.sokrates.reports.dataexporters.DataExporter.SEPARATOR;

class SourceCodeCacheExporter {
    private static final Log LOG = LogFactory.getLog(SourceCodeCacheExporter.class);
    private DataExporter dataExporter;
    private CodeConfiguration codeConfiguration;
    private File reportsFolder;
    private CodeAnalysisResults analysisResults;
    private File dataFolder;
    private File codeCacheFolder;

    SourceCodeCacheExporter(DataExporter dataExporter, CodeConfiguration codeConfiguration, File reportsFolder, CodeAnalysisResults analysisResults, File dataFolder) {
        this.dataExporter = dataExporter;
        this.codeConfiguration = codeConfiguration;
        this.reportsFolder = reportsFolder;
        this.analysisResults = analysisResults;
        this.dataFolder = dataFolder;
    }

    void exportSourceFile() throws IOException {
        this.codeCacheFolder = dataExporter.getCodeCacheFolder();

        detailedInfo("Saving details and source code cache:");

        saveStructureFile();

        if (codeConfiguration.getAnalysis().isSaveSourceFiles()) {
            Set<SourceFile> referencedFiles = getReferencedFiles();

            saveAspectJsonFiles(codeConfiguration.getMain(), "main", referencedFiles);
            saveAspectJsonFiles(codeConfiguration.getTest(), "test", referencedFiles);
            saveAspectJsonFiles(codeConfiguration.getGenerated(), "generated", referencedFiles);
            saveAspectJsonFiles(codeConfiguration.getBuildAndDeployment(), "buildAndDeployment", referencedFiles);
            saveAspectJsonFiles(codeConfiguration.getOther(), "other", referencedFiles);
        }

        if (codeConfiguration.getAnalysis().isSaveCodeFragments()) {
            UnitsAnalysisResults unitsAnalysisResults = analysisResults.getUnitsAnalysisResults();
            saveUnitFragmentFiles(unitsAnalysisResults.getLongestUnits(), "longest_unit");
            saveUnitFragmentFiles(unitsAnalysisResults.getMostComplexUnits(), "most_complex_unit");

            DuplicationAnalysisResults duplicationAnalysisResults = analysisResults.getDuplicationAnalysisResults();
            saveDuplicateFragmentFiles(duplicationAnalysisResults.getLongestDuplicates(), "longest_duplicates");
            saveDuplicateFragmentFiles(duplicationAnalysisResults.getUnitDuplicates(), "unit_duplicates");
        }
    }

    private Set<SourceFile> getReferencedFiles() {
        Set<SourceFile> referencedFiles = new HashSet<>();

        referencedFiles.addAll(analysisResults.getFilesAnalysisResults().getLongestFiles());
        referencedFiles.addAll(analysisResults.getFilesAnalysisResults().getFilesWithMostUnits());
        referencedFiles.addAll(analysisResults.getFilesHistoryAnalysisResults().getFilesWithLeastContributors());
        referencedFiles.addAll(analysisResults.getFilesHistoryAnalysisResults().getFilesWithMostContributors());
        referencedFiles.addAll(analysisResults.getFilesHistoryAnalysisResults().getMostChangedFiles());
        referencedFiles.addAll(analysisResults.getFilesHistoryAnalysisResults().getOldestFiles());
        referencedFiles.addAll(analysisResults.getFilesHistoryAnalysisResults().getMostPreviouslyChangedFiles());
        referencedFiles.addAll(analysisResults.getFilesHistoryAnalysisResults().getMostRecentlyChangedFiles());
        referencedFiles.addAll(analysisResults.getFilesHistoryAnalysisResults().getYoungestFiles());
        analysisResults.getDuplicationAnalysisResults().getLongestDuplicates().forEach(duplicationInstance -> {
            referencedFiles.addAll(duplicationInstance.getDuplicatedFileBlocks().stream().map(d -> d.getSourceFile()).collect(Collectors.toList()));
        });

        return referencedFiles;
    }

    private void saveUnitFragmentFiles(List<UnitInfo> units, String fragmentType) throws IOException {

        File fragmentsFolder = recreateFolder("fragments/" + fragmentType);

        detailedInfo(" - saving source code cache for the " + fragmentType + "fragments");
        int count[] = {0};
        units.forEach(unit -> {
            count[0]++;
            saveUnitAsHtml(fragmentType, fragmentsFolder, count, unit);
        });
    }

    private void saveUnitAsHtml(String fragmentType, File fragmentsFolder, int[] count, UnitInfo unit) {
        try {
            String fileName = fragmentType + "_" + count[0] + "." + unit.getSourceFile().getExtension();
            String fileAndLines = unit.getSourceFile().getRelativePath() + " [" + unit.getStartLine() + ":" + unit.getEndLine() + "]";

            String htmlTemplate = HtmlTemplateUtils.getResource("/templates/CodeFragmentUnit.html");
            String html = htmlTemplate.replace("${title}", unit.getShortName());
            html = html.replace("${unit-name}", unit.getShortName());
            html = html.replace("${file-and-lines}", fileAndLines);
            html = html.replace("${language}", unit.getSourceFile().getExtension());
            html = html.replace("${code}", StringEscapeUtils.escapeHtml4(unit.getBody()));
            html = html.replace("${lines-of-code}", FormattingUtils.formatCount(unit.getLinesOfCode()));
            html = html.replace("${mccabe-index}", FormattingUtils.formatCount(unit.getMcCabeIndex()));

            File htmlFile = new File(fragmentsFolder, fileName + ".html");
            FileUtils.write(htmlFile, html, UTF_8);

        } catch (IOException e) {
            LOG.warn(e);
        }
    }

    private void saveStructureFile() {
        try {

            String html = HtmlTemplateUtils.getResource("/templates/Structure.html");

            File htmlFile = new File(new File(reportsFolder, "html"), "Structure.html");
            FileUtils.write(htmlFile, html, UTF_8);

        } catch (IOException e) {
            LOG.warn(e);
        }
    }

    private void saveFileAsHtml(File htmlFile, SourceFile sourceFile) {
        try {

            String htmlTemplate = HtmlTemplateUtils.getResource("/templates/CodeFragmentFile.html");
            String html = htmlTemplate.replace("${title}", sourceFile.getRelativePath());
            html = html.replace("${file-path}", sourceFile.getRelativePath());
            html = html.replace("${file-name}", sourceFile.getFile().getName());
            String langName = LanguageAnalyzerFactory.getInstance().getLanguageAnalyzer(sourceFile).getClass().getSimpleName().replace("Analyzer", "").toLowerCase();
            String defaultLangName = DefaultLanguageAnalyzer.class.getSimpleName().replace("Analyzer", "");
            html = html.replace("${language}", langName.equalsIgnoreCase(defaultLangName) ? sourceFile.getExtension() : langName);
            html = html.replace("${code}", StringEscapeUtils.escapeHtml4(sourceFile.getContent()));
            html = html.replace("${lines-of-code}", FormattingUtils.formatCount(sourceFile.getLinesOfCode()));

            FileUtils.write(htmlFile, html, UTF_8);

        } catch (IOException e) {
            LOG.warn(e);
        }
    }

    private void saveDuplicateFragmentFiles(List<DuplicationInstance> duplicates, String fragmentType) throws IOException {
        File fragmentsFolder = recreateFolder("fragments/" + fragmentType);

        detailedInfo(" - saving source code cache for the " + fragmentType + "fragments");
        int count[] = {0};
        duplicates.forEach(duplicate -> {
            count[0]++;
            try {
                DuplicatedFileBlock firstFileBlock = duplicate.getDuplicatedFileBlocks().get(0);
                String extension = firstFileBlock.getSourceFile().getExtension();
                String fileName = fragmentType + "_" + count[0] + "." + extension;
                File file = new File(fragmentsFolder, fileName);

                StringBuilder body = new StringBuilder();

                duplicate.getDuplicatedFileBlocks().forEach(block -> {
                    List<String> lines = block.getSourceFile().getLines();
                    int fromIndex = block.getStartLine() - 1;
                    int endLine = block.getEndLine();
                    if (fromIndex >= 0 && endLine > fromIndex && endLine < lines.size()) {
                        body.append(block.getSourceFile().getRelativePath() + " [" + block.getStartLine() + ":" + endLine + "]:\n");
                        body.append(SEPARATOR);
                        body.append(lines.subList(fromIndex, endLine).stream().collect(Collectors.joining("\n")) + "\n" + SEPARATOR + "\n\n\n");
                    }
                });

                FileUtils.write(file, body.toString(), UTF_8);
            } catch (IllegalArgumentException e) {
                duplicate.getDuplicatedFileBlocks().forEach(block -> {
                    LOG.info(block.getSourceFile().getRelativePath() + " [" + block.getStartLine() + ":" + block.getEndLine() + "]:\n");
                });
                LOG.warn(e);
                System.exit(0);
            } catch (IOException e) {
                LOG.warn(e);
            }
        });
    }

    private File recreateFolder(String folderName) throws IOException {
        File fragmentsFolder = new File(codeCacheFolder, folderName);
        if (fragmentsFolder.exists()) {
            FileUtils.deleteDirectory(fragmentsFolder);
        }
        fragmentsFolder.mkdirs();
        return fragmentsFolder;
    }

    private void saveAspectJsonFiles(NamedSourceCodeAspect aspect, String aspectName, Set<SourceFile> referencedFiles) throws IOException {
        File filesListFile = new File(dataFolder, aspectName + "FilesPaths.json");
        detailedInfo(" - storing the file list for the <b>" + aspectName + "</b> aspect in <a href='" + filesListFile.getPath() + "'>" + filesListFile.getPath() + "</a>");
        List<String> files = new ArrayList<>();
        aspect.getSourceFiles().forEach(sourceFile -> {
            files.add(sourceFile.getRelativePath());
        });
        FileUtils.write(filesListFile, new JsonGenerator().generate(files), UTF_8);

        File aspectCodeCacheFolder = recreateFolder(aspectName);

        Map<String, List<String>> contents = new HashMap<>();
        detailedInfo(" - saving source code cache for the <b>" + aspectName + "</b> aspect in <a href='" + aspectCodeCacheFolder.getPath() + "'>" + aspectCodeCacheFolder.getPath() + "</a>");
        aspect.getSourceFiles().stream().filter(f -> referencedFiles.contains(f)).forEach(sourceFile -> {
            contents.put(sourceFile.getRelativePath(), sourceFile.getLines());
            try {
                FileUtils.write(new File(aspectCodeCacheFolder, sourceFile.getRelativePath()), sourceFile.getContent(), UTF_8);
                saveFileAsHtml(new File(aspectCodeCacheFolder, sourceFile.getRelativePath() + ".html"), sourceFile);
            } catch (IOException e) {
                LOG.warn(e);
            }
        });
    }

    private void detailedInfo(String text) {
        dataExporter.detailedInfo(text);
    }
}
