/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.cli;

import nl.obren.sokrates.common.io.JsonGenerator;
import nl.obren.sokrates.common.io.JsonMapper;
import nl.obren.sokrates.common.utils.*;
import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.dataexporters.DataExporter;
import nl.obren.sokrates.reports.generators.explorers.FilesExplorerGenerators;
import nl.obren.sokrates.reports.generators.statichtml.BasicSourceCodeReportGenerator;
import nl.obren.sokrates.reports.landscape.statichtml.LandscapeAnalysisCommands;
import nl.obren.sokrates.sourcecode.Link;
import nl.obren.sokrates.sourcecode.Metadata;
import nl.obren.sokrates.sourcecode.analysis.CodeAnalyzer;
import nl.obren.sokrates.sourcecode.analysis.CodeAnalyzerSettings;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.filehistory.DateUtils;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisUtils;
import nl.obren.sokrates.sourcecode.lang.LanguageAnalyzerFactory;
import nl.obren.sokrates.sourcecode.scoping.ScopeCreator;
import nl.obren.sokrates.sourcecode.scoping.custom.CustomConventionsHelper;
import nl.obren.sokrates.sourcecode.scoping.custom.CustomScopingConventions;
import nl.obren.sokrates.sourcecode.stats.SourceFileSizeDistribution;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CommandLineInterface {
    public static final int THOUSAND_YEARS = 365 * 1000;
    private static final Log LOG = LogFactory.getLog(CommandLineInterface.class);
    private ProgressFeedback progressFeedback;
    private final DataExporter dataExporter = new DataExporter(this.progressFeedback);

    private final Commands commands = new Commands();
    private final GitHistoryCommands gitHistoryCommands = new GitHistoryCommands(commands);
    private final ConfigCommands configCommands = new ConfigCommands(commands, this);
    private CodeConfiguration codeConfiguration;

    static boolean helpMode = false;

    public static void main(String[] args) throws IOException {
        ProcessingStopwatch.startAsReference("everything");
        CommandLineInterface commandLineInterface = new CommandLineInterface();
        commandLineInterface.run(args);

        ProcessingStopwatch.end("everything");

        if (!helpMode) {
            ProcessingStopwatch.print();
        }

        System.exit(0);
    }

    public void run(String[] args) throws IOException {
        if (args.length == 0) {
            helpMode = true;
            commands.usage();
            return;
        }

        if (progressFeedback != null) {
            progressFeedback.clear();
        }

        try {
            if (args[0].equalsIgnoreCase(Commands.INIT)) {
                init(args);
                return;
            } else if (args[0].equalsIgnoreCase(Commands.UPDATE_CONFIG)) {
                configCommands.updateConfig(args);
                return;
            } else if (args[0].equalsIgnoreCase(Commands.EXPORT_STANDARD_CONVENTIONS)) {
                configCommands.exportConventions(args);
                return;
            } else if (args[0].equalsIgnoreCase(Commands.UPDATE_LANDSCAPE)) {
                updateLandscape(args);
                return;
            } else if (args[0].equalsIgnoreCase(Commands.INIT_CONVENTIONS)) {
                configCommands.createNewConventionsFile(args);
                return;
            } else if (args[0].equalsIgnoreCase(Commands.EXTRACT_GIT_SUB_HISTORY)) {
                gitHistoryCommands.extractGitSubHistory(args);
                return;
            } else if (args[0].equalsIgnoreCase(Commands.EXTRACT_FILES)) {
                gitHistoryCommands.extractFiles(args);
                return;
            } else if (args[0].equalsIgnoreCase(Commands.EXTRACT_GIT_HISTORY)) {
                gitHistoryCommands.extractGitHistory(args);
                return;
            } else if (!args[0].equalsIgnoreCase(Commands.GENERATE_REPORTS)) {
                helpMode = true;
                commands.usage();
                return;
            }

            generateReports(args);
        } catch (ParseException e) {
            LOG.info("ERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
            helpMode = true;
            commands.usage();
        }
    }

    private void updateDateParam(CommandLine cmd) {
        String dateString = cmd.getOptionValue(commands.getDate().getOpt());
        if (dateString != null) {
            LOG.info("Using '" + dateString + "' as latest source code update date for active contributors reports.");
            DateUtils.setDateParam(dateString);
        }
    }

    private void updateLandscape(String[] args) throws ParseException {
        Options options = commands.getUpdateLandscapeOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(commands.getHelp().getOpt())) {
            helpMode = true;
            commands.usage(Commands.UPDATE_LANDSCAPE, commands.getUpdateLandscapeOptions(), Commands.UPDATE_LANDSCAPE_DESCRIPTION);
            return;
        }

        startTimeoutIfDefined(cmd);

        String strRootPath = cmd.getOptionValue(commands.getAnalysisRoot().getOpt());
        if (!cmd.hasOption(commands.getAnalysisRoot().getOpt())) {
            strRootPath = ".";
        }

        File root = new File(strRootPath);
        if (!root.exists()) {
            LOG.error("The analysis root \"" + root.getPath() + "\" does not exist.");
            return;
        }

        Metadata metadata = new Metadata();

        configCommands.updateMetadataFromCommandLine(cmd, metadata);

        String confFilePath = cmd.getOptionValue(commands.getConfFile().getOpt());
        updateDateParam(cmd);

        if (cmd.hasOption(commands.getRecursive().getOpt())) {
            List<File> landscapeConfigFiles = LandscapeAnalysisUtils.findAllSokratesLandscapeConfigFiles(root);
            landscapeConfigFiles.forEach(landscapeConfigFile -> {
                File landscapeFolder = landscapeConfigFile.getParentFile().getParentFile();
                String absolutePath = landscapeFolder.getAbsolutePath().replace("/./", "/");
                LOG.info(System.getProperty("user.dir"));
                System.setProperty("user.dir", absolutePath);
                LOG.info(System.getProperty("user.dir"));
                LandscapeAnalysisCommands.update(new File(landscapeFolder.getAbsolutePath()), null, metadata);
                DateUtils.reset();
                RegexUtils.reset();
                System.gc();
            });
            LOG.info("Analysed " + landscapeConfigFiles + " landscape(s):");
            landscapeConfigFiles.forEach(landscapeConfigFile -> {
                LOG.info(" -  " + landscapeConfigFile.getPath());
            });
            if (landscapeConfigFiles.size() > 0) {
                saveExecutionStats(new File(landscapeConfigFiles.get(landscapeConfigFiles.size() - 1).getParentFile(), "data"));
            }
        } else {
            File reportsFolder = LandscapeAnalysisCommands.update(root, confFilePath != null ? new File(confFilePath) : null, metadata);
            saveExecutionStats(new File(reportsFolder, "data"));
        }
    }

    private void generateReports(String[] args) throws ParseException, IOException {
        Options options = commands.getReportingOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(commands.getHelp().getOpt())) {
            helpMode = true;
            commands.usage(Commands.GENERATE_REPORTS, commands.getReportingOptions(), Commands.GENERATE_REPORTS_DESCRIPTION);
            return;
        }

        startTimeoutIfDefined(cmd);

        generateReports(cmd);
    }

    private void init(String[] args) throws ParseException, IOException {
        Options options = commands.getInitOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(commands.getHelp().getOpt())) {
            helpMode = true;
            commands.usage(Commands.INIT, commands.getInitOptions(), Commands.INIT_DESCRIPTION);
            return;
        }

        startTimeoutIfDefined(cmd);

        String strRootPath = cmd.getOptionValue(commands.getSrcRoot().getOpt());
        if (!cmd.hasOption(commands.getSrcRoot().getOpt())) {
            strRootPath = ".";
        }

        CustomScopingConventions customScopingConventions = readCustomScopingConventions(cmd);
        String nameValue = "";
        String descriptionValue = "";
        String logoLinkValue = "";
        if (cmd.hasOption(commands.getName().getOpt())) {
            nameValue = cmd.getOptionValue(commands.getName().getOpt());
        }
        if (cmd.hasOption(commands.getDescription().getOpt())) {
            descriptionValue = cmd.getOptionValue(commands.getDescription().getOpt());
        }
        if (cmd.hasOption(commands.getLogoLink().getOpt())) {
            logoLinkValue = cmd.getOptionValue(commands.getLogoLink().getOpt());
        }
        Link link = linkFromCommandLine(cmd);


        File root = new File(strRootPath);
        if (!root.exists()) {
            LOG.error("The src root \"" + root.getPath() + "\" does not exist.");
            return;
        }

        File conf = configCommands.getConfigFile(cmd, root);

        updateDateParam(cmd);

        new ScopeCreator(root, conf, customScopingConventions).createScopeFromConventions(nameValue, descriptionValue, logoLinkValue, link);

        LOG.info("Configuration stored in " + conf.getPath());
    }

    private CustomScopingConventions readCustomScopingConventions(CommandLine cmd) {
        CustomScopingConventions customScopingConventions = null;
        if (cmd.hasOption(commands.getConventionsFile().getOpt())) {
            File scopingConventionsFile = new File(cmd.getOptionValue(commands.getConventionsFile().getOpt()));
            if (scopingConventionsFile.exists()) {
                customScopingConventions = CustomConventionsHelper.readFromFile(scopingConventionsFile);
            }
        }
        return customScopingConventions;
    }

    private Link linkFromCommandLine(CommandLine cmd) {
        Link link = null;
        if (cmd.hasOption(commands.getAddLink().getOpt())) {
            String[] linkData = cmd.getOptionValues(commands.getAddLink().getOpt());
            if (linkData.length >= 1 && StringUtils.isNotBlank(linkData[0])) {
                String href = linkData[0];
                String label = linkData.length > 1 ? linkData[1] : "";
                link = new Link(label, href);
            }
        }
        return link;
    }

    void startTimeoutIfDefined(CommandLine cmd) {
        String timeoutSeconds = cmd.getOptionValue(commands.getTimeout().getOpt());
        if (StringUtils.isNumeric(timeoutSeconds)) {
            int seconds = Integer.parseInt(timeoutSeconds);
            LOG.info("Timeout timer set to " + seconds + " seconds.");
            Executors.newCachedThreadPool().execute(() -> {
                try {
                    Thread.sleep(seconds * 1000L);
                    LOG.info("Timeout after " + seconds + " seconds.");
                    System.exit(-1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void generateReports(CommandLine cmd) throws IOException {
        updateDateParam(cmd);

        File sokratesConfigFile;
        if (!cmd.hasOption(commands.getConfFile().getOpt())) {
            String confFilePath = "./_sokrates/config.json";
            sokratesConfigFile = new File(confFilePath);
        } else {
            sokratesConfigFile = new File(cmd.getOptionValue(commands.getConfFile().getOpt()));
        }

        LOG.info("Configuration file: " + sokratesConfigFile.getPath());
        if (noFileError(sokratesConfigFile)) return;

        ProcessingStopwatch.start("configuring");
        String jsonContent = FileUtils.readFileToString(sokratesConfigFile, UTF_8);
        this.codeConfiguration = (CodeConfiguration) new JsonMapper().getObject(jsonContent, CodeConfiguration.class);
        LanguageAnalyzerFactory.getInstance().setOverrides(codeConfiguration.getAnalysis().getAnalyzerOverrides());

        detailedInfo("Starting analysis based on the configuration file " + sokratesConfigFile.getPath());

        File reportsFolder;

        if (!cmd.hasOption(commands.getOutputFolder().getOpt())) {
            reportsFolder = prepareReportsFolder("./_sokrates/reports");
        } else {
            reportsFolder = prepareReportsFolder(cmd.getOptionValue(commands.getOutputFolder().getOpt()));
        }

        LOG.info("Reports folder: " + reportsFolder.getPath());
        ProcessingStopwatch.end("configuring");
        if (noFileError(reportsFolder)) return;

        ensureProgressFeedback();

        try {
            analyzeAndReport(cmd, sokratesConfigFile, reportsFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ensureProgressFeedback() {
        if (this.progressFeedback == null) {
            this.progressFeedback = new ProgressFeedback() {
                public void setText(String text) {
                    LOG.info(text.replaceAll("<.*?>", ""));
                }

                public void setDetailedText(String text) {
                    LOG.info(text.replaceAll("<.*?>", ""));
                }
            };
        }
    }

    private void analyzeAndReport(CommandLine cmd, File sokratesConfigFile, File reportsFolder) throws IOException {
        CodeAnalyzer codeAnalyzer = new CodeAnalyzer(getCodeAnalyzerSettings(cmd), codeConfiguration, sokratesConfigFile);
        CodeAnalysisResults analysisResults = codeAnalyzer.analyze(progressFeedback);

        ProcessingStopwatch.start("saving data");
        dataExporter.saveData(sokratesConfigFile, codeConfiguration, reportsFolder, analysisResults);
        saveTextualSummary(reportsFolder, analysisResults);
        ProcessingStopwatch.end("saving data");

        ProcessingStopwatch.start("generating visuals");
        new ReportVisualsGenerator(codeConfiguration).generateVisuals(reportsFolder, analysisResults);
        ProcessingStopwatch.end("generating visuals");

        generateAndSaveReports(sokratesConfigFile, reportsFolder, sokratesConfigFile.getParentFile(), codeAnalyzer, analysisResults);
        saveExecutionStats(dataExporter.getDataFolder());
    }

    private void saveExecutionStats(File dataFolder) {
        try {
            List<ProcessingTimes> monitors = ProcessingStopwatch.getMonitors();

            String json = new JsonGenerator().generate(monitors);
            List<String> lines = monitors.stream().map(m -> m.getDurationMs() / 1000.0 + "s => " + m.getProcessing() + " " + ProcessingStopwatch.getPercentage(m.getDurationMs())).collect(Collectors.toList());
            String text = lines.stream().map(l -> StringUtils.repeat("  ", StringUtils.countMatches(l, '/')) + l).collect(Collectors.joining("\n"));

            FileUtils.write(new File(dataFolder, "executionTimes.json"), json, UTF_8);
            FileUtils.write(new File(dataFolder, "executionTimes.txt"), text, UTF_8);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    private boolean noFileError(File inputFile) {
        if (!inputFile.exists()) {
            LOG.info("ERROR: " + inputFile.getPath() + " does not exist.");
            return true;
        }
        return false;
    }

    private boolean noReportingOptions(CommandLine cmd) {
        for (Option arg : cmd.getOptions()) {
            if (arg.getOpt().toLowerCase().startsWith("report")) {
                return false;
            }
        }
        return true;
    }

    private void info(String text) {
        if (progressFeedback != null) {
            progressFeedback.setText(text);
        } else {
            LOG.info(text.replaceAll("<.*?>", ""));
        }
    }

    public void detailedInfo(String text) {
        LOG.info(text);
        if (progressFeedback != null) {
            progressFeedback.setDetailedText(text);
        }
    }

    private void generateAndSaveReports(File inputFile, File reportsFolder, File sokratesConfigFolder, CodeAnalyzer codeAnalyzer, CodeAnalysisResults analysisResults) {
        File htmlReports = getHtmlFolder(reportsFolder);
        File dataReports = dataExporter.getDataFolder();
        File srcCache = dataExporter.getCodeCacheFolder();
        CodeAnalyzerSettings codeAnalyzerSettings = codeAnalyzer.getCodeAnalyzerSettings();
        if (new File(htmlReports, "index.html").exists() || codeAnalyzerSettings.isUpdateIndex()) {
            info("HTML reports: <a href='" + htmlReports.getPath() + "/index.html'>" + htmlReports.getPath() + "</a>");
        } else {
            info("HTML reports: <a href='" + htmlReports.getPath() + "'>" + htmlReports.getPath() + "</a>");
        }
        info("Raw data: <a href='" + dataReports.getPath() + "'>" + dataReports.getPath() + "</a>");
        if (analysisResults.getCodeConfiguration().getAnalysis().isSaveSourceFiles()) {
            info("Source code cache : <a href='" + srcCache.getPath() + "'>" + srcCache.getPath() + "</a>");
        }
        ProcessingStopwatch.start("reporting");
        BasicSourceCodeReportGenerator generator = new BasicSourceCodeReportGenerator(codeAnalyzerSettings, analysisResults, inputFile, reportsFolder);
        List<RichTextReport> reports = generator.report();
        ProcessingStopwatch.end("reporting");

        ProcessingStopwatch.start("saving report");
        reports.forEach(report -> {
            info("Generating the '" + report.getId().toUpperCase() + "' report...");
            String processingName = "saving report/" + report.getId().toLowerCase() + "";
            ProcessingStopwatch.start(processingName);
            ReportFileExporter.exportHtml(reportsFolder, "html", report, analysisResults.getCodeConfiguration().getAnalysis().getCustomHtmlReportHeaderFragment());
            ProcessingStopwatch.end(processingName);
        });
        ProcessingStopwatch.start("saving report/index");
        if (!codeAnalyzerSettings.isDataOnly() && codeAnalyzerSettings.isUpdateIndex()) {
            ReportFileExporter.exportReportsIndexFile(reportsFolder, analysisResults, sokratesConfigFolder);
        }
        ProcessingStopwatch.end("saving report/index");
        ProcessingStopwatch.start("saving report/explorer");
        FilesExplorerGenerators filesExplorerGenerators = new FilesExplorerGenerators(reportsFolder);
        filesExplorerGenerators.exportJson(analysisResults);
        ProcessingStopwatch.end("saving report/explorer");
        ProcessingStopwatch.end("saving report");
    }

    public BasicColorInfo getFileSizeColor(SourceFileSizeDistribution distribution, int linesOfCode) {
        return ReportVisualsGenerator.getFileSizeColor(distribution, linesOfCode);
    }

    private File getHtmlFolder(File reportsFolder) {
        File folder = new File(reportsFolder, Commands.ARG_HTML_REPORTS_FOLDER_NAME);
        folder.mkdirs();
        return folder;
    }

    private void saveTextualSummary(File reportsFolder, CodeAnalysisResults analysisResults) throws IOException {
        File jsonFile = new File(dataExporter.getTextDataFolder(), "textualSummary.txt");
        FileUtils.write(jsonFile, analysisResults.getTextSummary().toString(), UTF_8);
    }

    private File prepareReportsFolder(String path) throws IOException {
        File reportsFolder = new File(path);
        reportsFolder.mkdirs();

        return reportsFolder;
    }

    private CodeAnalyzerSettings getCodeAnalyzerSettings(CommandLine cmd) {
        CodeAnalyzerSettings settings = new CodeAnalyzerSettings();

        if (codeConfiguration.getAnalysis().isSkipDependencies()) {
            settings.setAnalyzeStaticDependencies(false);
        }

        return settings;
    }


    public void setProgressFeedback(ProgressFeedback progressFeedback) {
        this.progressFeedback = progressFeedback;
    }


}
