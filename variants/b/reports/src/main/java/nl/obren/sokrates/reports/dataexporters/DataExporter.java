/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.dataexporters;

import nl.obren.sokrates.common.io.JsonGenerator;
import nl.obren.sokrates.common.io.JsonMapper;
import nl.obren.sokrates.common.utils.FormattingUtils;
import nl.obren.sokrates.common.utils.ProgressFeedback;
import nl.obren.sokrates.common.utils.SystemUtils;
import nl.obren.sokrates.reports.dataexporters.dependencies.DependenciesExporter;
import nl.obren.sokrates.reports.dataexporters.duplication.DuplicateExportInfo;
import nl.obren.sokrates.reports.dataexporters.duplication.DuplicateFileBlockExportInfo;
import nl.obren.sokrates.reports.dataexporters.duplication.DuplicationExportInfo;
import nl.obren.sokrates.reports.dataexporters.duplication.DuplicationExporter;
import nl.obren.sokrates.reports.dataexporters.files.FileListExporter;
import nl.obren.sokrates.reports.dataexporters.trends.MetricsTrendExporter;
import nl.obren.sokrates.reports.dataexporters.units.UnitListExporter;
import nl.obren.sokrates.reports.utils.ZipUtils;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.analysis.FileHistoryAnalysisConfig;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.duplication.DuplicationInstance;
import nl.obren.sokrates.sourcecode.filehistory.FileHistoryScopingUtils;
import nl.obren.sokrates.sourcecode.filehistory.GitHistoryUtil;
import nl.obren.sokrates.sourcecode.githistory.FileUpdate;
import nl.obren.sokrates.sourcecode.githistory.GitHistoryUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DataExporter {
    public static final String INTERACTIVE_HTML_FOLDER_NAME = "explorers";
    public static final String SRC_CACHE_FOLDER_NAME = "src";
    public static final String DATA_FOLDER_NAME = "data";
    public static final String HISTORY_FOLDER_NAME = "history";
    public static final String SEPARATOR = "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
    public static final String FOUND_TEXT_PER_FILE_SUFFIX = "_found_text_per_file";
    public static final String FOUND_TEXT_SUFFIX = "_found_text";
    public static final int MAX_EXPORT_LIST_SIZE = 10000;
    private static final Log LOG = LogFactory.getLog(DataExporter.class);
    private ProgressFeedback progressFeedback;
    private File sokratesConfigFile;
    private CodeConfiguration codeConfiguration;
    private File reportsFolder;
    private CodeAnalysisResults analysisResults;
    private File dataFolder;
    private File historyFolder;
    private File textDataFolder;
    private File extraAnalysisDataFolder;

    public DataExporter(ProgressFeedback progressFeedback) {
        this.progressFeedback = progressFeedback;
    }

    public static String dependenciesFileNamePrefix(String fromComponent, String toComponent, String logicalDecompositionName) {
        String fileNamePrefix = "dependencies_" + SystemUtils.getSafeFileName(logicalDecompositionName);
        if (StringUtils.isNotBlank(fromComponent) && StringUtils.isNotBlank(toComponent)) {
            fileNamePrefix += "_" + SystemUtils.getSafeFileName(fromComponent + "_" + toComponent);
        }
        return fileNamePrefix;
    }

    public void saveData(File sokratesConfigFile, CodeConfiguration codeConfiguration, File reportsFolder, CodeAnalysisResults analysisResults) throws IOException {
        this.sokratesConfigFile = sokratesConfigFile;
        this.codeConfiguration = codeConfiguration;
        this.reportsFolder = reportsFolder;
        this.analysisResults = analysisResults;
        this.dataFolder = getDataFolder();
        this.textDataFolder = getTextDataFolder();
        this.extraAnalysisDataFolder = getExtraAnalysisDataFolder();
        this.historyFolder = getDataHistoryFolder();

        LOG.info("Saving file lists");
        new FileListsDataExporter(analysisResults, textDataFolder).exportFileLists();
        LOG.info("Saving metrics data");
        exportMetrics();
        LOG.info("Saving trends data");
        exportTrends();
        LOG.info("Saving controls data");
        exportControls();
        LOG.info("Saving contributors data");
        exportContributors();
        LOG.info("Saving JSON data");
        exportJson();
        LOG.info("Saving duplication data");
        exportDuplicates();
        LOG.info("Saving units data");
        exportUnits();
        // exportInteractiveExplorers();
        LOG.info("Saving source files");
        new SourceCodeCacheExporter(this, codeConfiguration, reportsFolder, analysisResults, dataFolder).exportSourceFile();
        DependenciesDataExporter dependenciesExporter = new DependenciesDataExporter(analysisResults, textDataFolder);
        LOG.info("Saving logical dependencies data");
        dependenciesExporter.exportDependencies(analysisResults);
        LOG.info("Saving temporal dependencies data");
        dependenciesExporter.saveTemporalDependencies(analysisResults);
    }

    private void exportTrends() {
        MetricsTrendExporter exporter = new MetricsTrendExporter(sokratesConfigFile, analysisResults);

        try {
            FileUtils.write(new File(textDataFolder, "metrics_trend.txt"), exporter.getText(), UTF_8);
            FileUtils.write(new File(textDataFolder, "metrics_trend_loc_per_extension.txt"), exporter.getText("LINES_OF_CODE_MAIN_.*"), UTF_8);
            FileUtils.write(new File(textDataFolder, "metrics_trend_loc_duplication.txt"), exporter.getText("(DUPLICATION_NUMBER_OF_CLEANED_LINES|DUPLICATION_NUMBER_OF_DUPLICATED_LINES)"), UTF_8);
            FileUtils.write(new File(textDataFolder, "metrics_trend_unit_size_loc.txt"), exporter.getText("UNIT_SIZE_DISTRIBUTION_.*_LOC"), UTF_8);
            FileUtils.write(new File(textDataFolder, "metrics_trend_conditional_complexity_loc.txt"), exporter.getText("CONDITIONAL_COMPLEXITY_.*_LOC"), UTF_8);
            FileUtils.write(new File(textDataFolder, "metrics_trend_loc_logical_decompositions.txt"), exporter.getText("LINES_OF_CODE_DECOMPOSITION_.*", ".*_EXT_.*"), UTF_8);
            FileUtils.write(new File(textDataFolder, "metrics_trend_loc_file_size.txt"), exporter.getText("FILE_SIZE_.*", ".*_EXT_.*"), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportMetrics() {
        StringBuilder content = new StringBuilder();

        analysisResults.getMetricsList().getMetrics().forEach(metric -> {
            content.append(metric.getId());
            content.append(": ");
            content.append(metric.getValue());
            content.append("\n");
        });
        try {
            FileUtils.write(new File(textDataFolder, "metrics.txt"), content.toString(), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportControls() {
        StringBuilder content = new StringBuilder();

        analysisResults.getControlResults().getGoalsAnalysisResults().forEach(goalsAnalysisResults -> {
            goalsAnalysisResults.getControlStatuses().forEach(status -> {
                content.append("goal: " + goalsAnalysisResults.getMetricsWithGoal().getGoal() + "\n");
                content.append("control metric: " + status.getMetric().getId() + "\n");
                content.append("status: " + status.getStatus() + "\n");
                content.append("desired range: " + status.getControl().getDesiredRange().getTextDescription() + "\n");
                content.append("value: " + status.getMetric().getValue() + "\n");
                content.append("description: " + status.getControl().getDescription() + "\n");
                content.append("\n");
            });
        });
        try {
            FileUtils.write(new File(textDataFolder, "controls.txt"), content.toString(), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void exportContributors() {
        StringBuilder content = new StringBuilder();

        List<Contributor> contributors = analysisResults.getContributorsAnalysisResults().getContributors();
        int total = contributors.stream().mapToInt(c -> c.getCommitsCount()).sum();

        content.append("Contributor\t#commits (all time)\t#commits (30 days)\t#commits (90 days)\t#commits (180 days)\t#commits (365 days)\tfirst commit\tlast commit\n");

        contributors.forEach(contributor -> {
            content.append(contributor.getEmail() + "\t");
            content.append(contributor.getCommitsCount() + "\t");
            content.append(contributor.getCommitsCount30Days() + "\t");
            content.append(contributor.getCommitsCount90Days() + "\t");
            content.append(contributor.getCommitsCount180Days() + "\t");
            content.append(contributor.getCommitsCount365Days() + "\t");
            content.append(contributor.getFirstCommitDate() + "\t");
            content.append(contributor.getLatestCommitDate() + "\t");
            double percentage = 100.0 * contributor.getCommitsCount() / total;
            content.append(FormattingUtils.getFormattedPercentage(percentage) + "%\n");
        });
        try {
            FileUtils.write(new File(textDataFolder, "contributors.txt"), content.toString(), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportDuplicates() {
        exportDuplicates(analysisResults.getDuplicationAnalysisResults().getAllDuplicates(), "duplicates");
        exportDuplicates(analysisResults.getDuplicationAnalysisResults().getUnitDuplicates(), "unit_duplicates");
    }

    private void exportDuplicates(List<DuplicationInstance> instances, final String fileName) {
        DuplicationExportInfo duplicationExportInfo = new DuplicationExporter(instances).getDuplicationExportInfo();
        List<DuplicateExportInfo> duplicates = duplicationExportInfo.getDuplicates();
        StringBuilder content = new StringBuilder();

        int id[] = {1};
        if ((duplicates.size() > MAX_EXPORT_LIST_SIZE)) {
            duplicates = duplicates.subList(0, MAX_EXPORT_LIST_SIZE);
        }
        duplicates.forEach(duplicate -> {
            List<DuplicateFileBlockExportInfo> duplicatedFileBlocks = duplicate.getDuplicatedFileBlocks();
            content.append("duplicated block id: " + id[0] + "\n");
            content.append("size: " + duplicate.getBlockSize() + " cleaned lines of code\n");
            content.append("in " + duplicatedFileBlocks.size() + " files:\n");
            duplicatedFileBlocks.forEach(duplicateFileBlock -> {
                content.append(" - " + duplicateFileBlock.getFile().getRelativePath());
                content.append(" (" + duplicateFileBlock.getStartLine() + ":" + duplicateFileBlock.getEndLine() + ")\n");
            });

            content.append("\n");

            id[0]++;
        });
        try {
            FileUtils.write(new File(textDataFolder, fileName + ".txt"), content.toString(), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportUnits() {
        UnitListExporter units = new UnitListExporter(analysisResults.getUnitsAnalysisResults().getAllUnits());
        int id[] = {1};
        StringBuilder content = new StringBuilder();
        units.getAllUnitsData().forEach(unit -> {
            content.append("id: " + id[0] + "\n");
            content.append("unit: " + unit.getShortName() + "\n");
            content.append("file: " + unit.getRelativeFileName() + "\n");
            content.append("start line: " + unit.getStartLine() + "\n");
            content.append("end line: " + unit.getEndLine() + "\n");
            content.append("size: " + unit.getLinesOfCode() + " LOC\n");
            content.append("McCabe index: " + unit.getMcCabeIndex() + "\n");
            content.append("number of parameters: " + unit.getNumberOfParameters() + "\n");
            content.append("\n");

            id[0]++;
        });
        try {
            FileUtils.write(new File(textDataFolder, "units.txt"), content.toString(), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportJson() throws IOException {
        String analysisResultsJson = new JsonGenerator().generate(analysisResults);
        FileUtils.write(new File(dataFolder, "analysisResults.json"), analysisResultsJson, UTF_8);

        String configJson = FileUtils.readFileToString(sokratesConfigFile, UTF_8);
        FileUtils.write(new File(dataFolder, "config.json"), configJson, UTF_8);

        if (codeConfiguration.getTrendAnalysis().isSaveHistory()) {
            ZipUtils.stringToZipFile(new File(getTodayHistoryFolder(), "analysisResults.zip"),
                    new String[][]{{"config.json", configJson},
                            {"analysisResults.json", analysisResultsJson}});
            ZipUtils.stringToZipFile(new File(getLatestHistoryFolder(), "analysisResults.zip"),
                    new String[][]{{"config.json", configJson},
                            {"analysisResults.json", analysisResultsJson}});
        }

        List<SourceFile> mainSourceFiles = analysisResults.getMainAspectAnalysisResults().getAspect().getSourceFiles();
        FileUtils.write(new File(dataFolder, "mainFiles.json"), new JsonGenerator().generate(mainSourceFiles), UTF_8);

        if (codeConfiguration.getFileHistoryAnalysis().filesHistoryImportPathExists(sokratesConfigFile.getParentFile())) {
            saveExtraAnalysesConfig();
        }

        FileUtils.write(new File(textDataFolder, "mainFiles.txt"), FileListsDataExporter.getFilesAsTxt(mainSourceFiles), UTF_8);
        FileUtils.write(new File(textDataFolder, "mainFilesWithHistory.txt"), FileListsDataExporter.getFilesWithHistoryAsTxt(mainSourceFiles), UTF_8);
        FileUtils.write(new File(textDataFolder, "mainFilesWithoutHistory.txt"), FileListsDataExporter.getFilesWithoutHistoryAsTxt(mainSourceFiles), UTF_8);
        try {
            exportAspectsAndDetailsJson();
            exportZips();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void exportAspectsAndDetailsJson() throws IOException {
        List<SourceFile> testSourceFile = analysisResults.getTestAspectAnalysisResults().getAspect().getSourceFiles();
        List<SourceFile> generatedSourceFiles = analysisResults.getGeneratedAspectAnalysisResults().getAspect().getSourceFiles();
        List<SourceFile> buildAndDeploymentSourceFiles = analysisResults.getBuildAndDeployAspectAnalysisResults().getAspect().getSourceFiles();
        List<SourceFile> otherSourceFiles = analysisResults.getOtherAspectAnalysisResults().getAspect().getSourceFiles();

        FileUtils.write(new File(dataFolder, "testFiles.json"), new JsonGenerator().generate(testSourceFile), UTF_8);
        FileUtils.write(new File(dataFolder, "generatedFiles.json"), new JsonGenerator().generate(generatedSourceFiles), UTF_8);
        FileUtils.write(new File(dataFolder, "buildAndDeploymentFiles.json"), new JsonGenerator().generate(buildAndDeploymentSourceFiles), UTF_8);
        FileUtils.write(new File(dataFolder, "otherFiles.json"), new JsonGenerator().generate(otherSourceFiles), UTF_8);

        FileUtils.write(new File(dataFolder, "units.json"), new JsonGenerator().generate(new UnitListExporter(analysisResults.getUnitsAnalysisResults().getAllUnits()).getAllUnitsData()), UTF_8);
        FileUtils.write(new File(dataFolder, "files.json"), new FileListExporter(analysisResults.getFilesAnalysisResults().getAllFiles()).getJson(), UTF_8);
        List<DuplicationInstance> allDuplicates = analysisResults.getDuplicationAnalysisResults().getAllDuplicates();
        Collections.sort(allDuplicates, (a, b) -> b.getBlockSize() - a.getBlockSize());
        allDuplicates = allDuplicates.stream().limit(10000).collect(Collectors.toList());
        FileUtils.write(new File(dataFolder, "duplicates.json"), new JsonGenerator().generate(new DuplicationExporter(
                allDuplicates).getDuplicationExportInfo()), UTF_8);
        FileUtils.write(new File(dataFolder, "logical_decompositions.json"), new JsonGenerator().generate(
                analysisResults.getLogicalDecompositionsAnalysisResults()), UTF_8);
        FileUtils.write(new File(dataFolder, "dependencies.json"), new JsonGenerator().generate(
                new DependenciesExporter(analysisResults.getAllDependencies()).getDependenciesExportInfo()), UTF_8);
        FileUtils.write(new File(dataFolder, "contributors.json"), new JsonGenerator().generate(analysisResults.getContributorsAnalysisResults().getContributors()), UTF_8);
        FileUtils.write(new File(dataFolder, "concerns.json"), new JsonGenerator().generate(analysisResults.getConcernsAnalysisResults()), UTF_8);
    }

    private void exportZips() throws IOException {
        File zipFolder = new File(dataFolder, "zips");
        zipFolder.mkdirs();

        ZipUtils.stringToZipFile(new File(zipFolder, "all_files.zip"), new String[][]{
                {"aspect_main.txt", FileUtils.readFileToString(new File(textDataFolder, "aspect_main.txt"), UTF_8)},
                {"aspect_test.txt", FileUtils.readFileToString(new File(textDataFolder, "aspect_test.txt"), UTF_8)},
                {"aspect_generated.txt", FileUtils.readFileToString(new File(textDataFolder, "aspect_generated.txt"), UTF_8)},
                {"aspect_build_and_deployment.txt", FileUtils.readFileToString(new File(textDataFolder, "aspect_build_and_deployment.txt"), UTF_8)},
                {"aspect_other.txt", FileUtils.readFileToString(new File(textDataFolder, "aspect_other.txt"), UTF_8)}
        });
        File gitHistoryFile = new File(reportsFolder, "../../git-history.txt");
        if (gitHistoryFile.exists()) {
            String gitHistoryContent = FileUtils.readFileToString(gitHistoryFile, UTF_8);
            ZipUtils.stringToZipFile(new File(zipFolder, "git-history.zip"), "git-history.txt", gitHistoryContent);
        }
    }

    public File getTextDataFolder() {
        File textDataFolder = new File(dataFolder, "text");
        textDataFolder.mkdirs();
        return textDataFolder;
    }

    public File getExtraAnalysisDataFolder() {
        File extraAnalysisDataFolder = new File(dataFolder, "extra_analysis");
        extraAnalysisDataFolder.mkdirs();
        return extraAnalysisDataFolder;
    }

    private void saveExtraAnalysesConfig() {
        try {
            String jsonContent = FileUtils.readFileToString(sokratesConfigFile, UTF_8);

            FileUtils.write(new File(extraAnalysisDataFolder, "config_original.json"), new JsonGenerator().generate(codeConfiguration), UTF_8);

            saveConfigByFileChangeFrequency(jsonContent);
            saveConfigByFileAge(jsonContent);
            saveConfigByFileFreshness(jsonContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveConfigByFileChangeFrequency(String jsonContent) throws IOException {
        CodeConfiguration codeConfiguration = (CodeConfiguration) new JsonMapper().getObject(jsonContent, CodeConfiguration.class);
        codeConfiguration.setLogicalDecompositions(FileHistoryScopingUtils.getLogicalDecompositionsFileUpdateFrequency(analysisResults));

        codeConfiguration.getFileHistoryAnalysis().setImportPath("");

        FileUtils.write(new File(extraAnalysisDataFolder, "config_by_file_change_frequency.json"), new JsonGenerator().generate(codeConfiguration), UTF_8);
    }

    private void saveConfigByFileAge(String jsonContent) throws IOException {
        CodeConfiguration codeConfiguration = (CodeConfiguration) new JsonMapper().getObject(jsonContent, CodeConfiguration.class);
        codeConfiguration.setLogicalDecompositions(FileHistoryScopingUtils.getLogicalDecompositionsByAge(analysisResults));

        codeConfiguration.getFileHistoryAnalysis().setImportPath("");

        FileUtils.write(new File(extraAnalysisDataFolder, "config_by_file_age.json"), new JsonGenerator().generate(codeConfiguration), UTF_8);
    }

    private void saveConfigByFileFreshness(String jsonContent) throws IOException {
        CodeConfiguration codeConfiguration = (CodeConfiguration) new JsonMapper().getObject(jsonContent, CodeConfiguration.class);
        codeConfiguration.setLogicalDecompositions(FileHistoryScopingUtils.getLogicalDecompositionsByFreshness(analysisResults));

        codeConfiguration.getFileHistoryAnalysis().setImportPath("");

        FileUtils.write(new File(extraAnalysisDataFolder, "config_by_file_freshness.json"), new JsonGenerator().generate(codeConfiguration), UTF_8);
    }


    public File getCodeCacheFolder() {
        File codeCacheFolder = new File(reportsFolder, SRC_CACHE_FOLDER_NAME);
        codeCacheFolder.mkdirs();
        return codeCacheFolder;
    }

    public File getInteractiveHtmlFolder() {
        File codeCacheFolder = new File(reportsFolder, INTERACTIVE_HTML_FOLDER_NAME);
        codeCacheFolder.mkdirs();
        return codeCacheFolder;
    }

    public File getDataFolder() {
        File dataFolder = new File(reportsFolder, DATA_FOLDER_NAME);
        dataFolder.mkdirs();
        return dataFolder;
    }

    public File getDataHistoryFolder() {
        File folder = new File(reportsFolder, HISTORY_FOLDER_NAME);
        folder.mkdirs();
        return folder;
    }

    public File getTodayHistoryFolder() {
        return codeConfiguration.getTrendAnalysis().getSnapshotFolder(sokratesConfigFile.getParentFile());
    }

    public File getLatestHistoryFolder() {
        File folder = new File(getDataHistoryFolder(), "LATEST");
        folder.mkdirs();
        return folder;
    }

    private void info(String text) {
        LOG.info(text);
        if (progressFeedback != null) {
            progressFeedback.setText(text);
        }
    }

    public void detailedInfo(String text) {
        LOG.info(text.replaceAll("<.*?>", ""));
        if (progressFeedback != null) {
            progressFeedback.setDetailedText(text);
        }
    }

}
