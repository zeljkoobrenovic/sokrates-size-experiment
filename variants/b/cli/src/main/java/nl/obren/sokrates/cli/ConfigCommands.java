/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.cli;

import nl.obren.sokrates.common.io.JsonGenerator;
import nl.obren.sokrates.common.io.JsonMapper;
import nl.obren.sokrates.sourcecode.Link;
import nl.obren.sokrates.sourcecode.Metadata;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.core.CodeConfigurationUtils;
import nl.obren.sokrates.sourcecode.scoping.custom.CustomConventionsHelper;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

class ConfigCommands {
    private static final Log LOG = LogFactory.getLog(ConfigCommands.class);
    private final Commands commands;
    private final CommandLineInterface commandLineInterface;

    ConfigCommands(Commands commands, CommandLineInterface commandLineInterface) {
        this.commands = commands;
        this.commandLineInterface = commandLineInterface;
    }

    void updateConfig(String[] args) throws ParseException, IOException {
        Options options = commands.getUpdateConfigOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(commands.getHelp().getOpt())) {
            CommandLineInterface.helpMode = true;
            commands.usage(Commands.UPDATE_CONFIG, commands.getUpdateConfigOptions(), Commands.UPDATE_CONFIG_DESCRIPTION);
            return;
        }

        commandLineInterface.startTimeoutIfDefined(cmd);

        String strRootPath = cmd.getOptionValue(commands.getSrcRoot().getOpt());
        if (!cmd.hasOption(commands.getSrcRoot().getOpt())) {
            strRootPath = ".";
        }

        File root = new File(strRootPath);
        if (!root.exists()) {
            LOG.error("The src root \"" + root.getPath() + "\" does not exist.");
            return;
        }

        File confFile = getConfigFile(cmd, root);
        LOG.info("Configuration file '" + confFile.getPath() + "'.");

        String jsonContent = FileUtils.readFileToString(confFile, UTF_8);
        CodeConfiguration codeConfiguration = (CodeConfiguration) new JsonMapper().getObject(jsonContent, CodeConfiguration.class);

        applyAnalysisOptions(cmd, codeConfiguration);

        Metadata metadata = codeConfiguration.getMetadata();
        updateMetadataFromCommandLine(cmd, metadata);

        if (cmd.hasOption(commands.getSetCacheFiles().getOpt())) {
            String cacheFileValue = cmd.getOptionValue(commands.getSetCacheFiles().getOpt());
            if (StringUtils.isNotBlank(cacheFileValue)) {
                codeConfiguration.getAnalysis().setSaveSourceFiles(cacheFileValue.equalsIgnoreCase("true"));
            }
        }

        FileUtils.write(confFile, new JsonGenerator().generate(codeConfiguration), UTF_8);
    }

    private void applyAnalysisOptions(CommandLine cmd, CodeConfiguration codeConfiguration) {
        if (cmd.hasOption(commands.getSkipComplexAnalyses().getOpt())) {
            codeConfiguration.getAnalysis().setSkipDependencies(true);
            codeConfiguration.getAnalysis().setSkipDuplication(true);
            codeConfiguration.getAnalysis().setSkipCorrelations(true);
            codeConfiguration.getAnalysis().setSaveSourceFiles(false);
        }

        if (cmd.hasOption(commands.getSkipDuplicationAnalyses().getOpt())) {
            codeConfiguration.getAnalysis().setSkipDuplication(true);
        }

        if (cmd.hasOption(commands.getSkipCorrelationAnalyses().getOpt())) {
            codeConfiguration.getAnalysis().setSkipCorrelations(true);
        }

        if (cmd.hasOption(commands.getEnableDuplicationAnalyses().getOpt())) {
            codeConfiguration.getAnalysis().setSkipDuplication(false);
        }
    }

    void updateMetadataFromCommandLine(CommandLine cmd, Metadata metadata) {
        if (cmd.hasOption(commands.getSetName().getOpt())) {
            String name = cmd.getOptionValue(commands.getSetName().getOpt());
            if (StringUtils.isNotBlank(name)) {
                metadata.setName(name);
            }
        }

        if (cmd.hasOption(commands.getSetDescription().getOpt())) {
            String description = cmd.getOptionValue(commands.getSetDescription().getOpt());
            if (StringUtils.isNotBlank(description)) {
                metadata.setDescription(description);
            }
        }

        if (cmd.hasOption(commands.getSetLogoLink().getOpt())) {
            String logoLink = cmd.getOptionValue(commands.getSetLogoLink().getOpt());
            if (StringUtils.isNotBlank(logoLink)) {
                metadata.setLogoLink(logoLink);
            }
        }

        if (cmd.hasOption(commands.getAddLink().getOpt())) {
            String[] linkData = cmd.getOptionValues(commands.getAddLink().getOpt());
            if (linkData.length >= 1 && StringUtils.isNotBlank(linkData[0])) {
                String href = linkData[0];
                String label = linkData.length > 1 ? linkData[1] : "";
                metadata.getLinks().add(new Link(label, href));
            }
        }
    }

    void exportConventions(String[] args) throws ParseException, IOException {
        File file = new File("standard_analysis_conventions.json");

        CustomConventionsHelper.saveStandardConventionsToFile(file);

        LOG.info("A standard conventions file saved to '" + file.getPath() + "'.");
    }

    void createNewConventionsFile(String[] args) throws ParseException, IOException {
        File file = new File("analysis_conventions.json");

        CustomConventionsHelper.saveEmptyConventionsToFile(file);

        LOG.info("A new conventions file saved to '" + file.getPath() + "'.");
    }

    File getConfigFile(CommandLine cmd, File root) {
        File conf;
        if (cmd.hasOption(commands.getConfFile().getOpt())) {
            conf = new File(cmd.getOptionValue(commands.getConfFile().getOpt()));
        } else {
            conf = CodeConfigurationUtils.getDefaultSokratesConfigFile(root);
        }
        return conf;
    }
}
