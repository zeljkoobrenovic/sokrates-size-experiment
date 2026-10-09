/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.cli;

import nl.obren.sokrates.cli.git.GitHistoryExtractor;
import nl.obren.sokrates.sourcecode.githistory.ExtractGitHistoryFileHandler;
import nl.obren.sokrates.sourcecode.githistory.GitHistoryUtils;
import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

class GitHistoryCommands {
    private static final Log LOG = LogFactory.getLog(GitHistoryCommands.class);
    private final Commands commands;

    GitHistoryCommands(Commands commands) {
        this.commands = commands;
    }

    void extractGitHistory(String[] args) throws ParseException {
        Options options = commands.getExtractGitHistoryOption();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(commands.getHelp().getOpt())) {
            CommandLineInterface.helpMode = true;
            commands.usage(Commands.EXTRACT_GIT_HISTORY, commands.getExtractGitHistoryOption(), Commands.EXTRACT_GIT_HISTORY_DESCRIPTION);
            return;
        }

        String strRootPath = cmd.getOptionValue(commands.getAnalysisRoot().getOpt());
        if (!cmd.hasOption(commands.getAnalysisRoot().getOpt())) {
            strRootPath = ".";
        }

        File root = new File(strRootPath);
        if (!root.exists()) {
            LOG.error("The analysis root \"" + root.getPath() + "\" does not exist.");
            return;
        }

        new GitHistoryExtractor().extractGitHistory(root);
    }

    void extractGitSubHistory(String[] args) throws ParseException, IOException {
        Options options = commands.getExtractGitSubHistoryOption();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(commands.getHelp().getOpt())) {
            CommandLineInterface.helpMode = true;
            commands.usage(Commands.EXTRACT_GIT_SUB_HISTORY, commands.getExtractGitSubHistoryOption(), Commands.EXTRACT_GIT_SUB_HISTORY_DESCRIPTION);
            return;
        }

        String strRootPath = cmd.getOptionValue(commands.getAnalysisRoot().getOpt());
        if (!cmd.hasOption(commands.getAnalysisRoot().getOpt())) {
            strRootPath = ".";
        }

        File root = new File(strRootPath);
        if (!root.exists()) {
            LOG.error("The analysis root \"" + root.getPath() + "\" does not exist.");
            return;
        }

        String prefixValue = cmd.getOptionValue(commands.getPrefix().getOpt());

        new ExtractGitHistoryFileHandler().extractSubHistory(new File(root, GitHistoryUtils.GIT_HISTORY_FILE_NAME), prefixValue);
    }

    void extractFiles(String[] args) throws ParseException, IOException {
        Options options = commands.getExtractFilesOption();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(commands.getHelp().getOpt())) {
            CommandLineInterface.helpMode = true;
            commands.usage(Commands.EXTRACT_FILES, commands.getExtractFilesOption(), Commands.EXTRACT_FILES_DESCRIPTION);
            return;
        }

        File root = cmd.hasOption(commands.getAnalysisRoot().getOpt()) ? new File(cmd.getOptionValue(commands.getAnalysisRoot().getOpt())) : new File(".");
        String patternValue = cmd.getOptionValue(commands.getPattern().getOpt());
        String dest = cmd.getOptionValue(commands.getDestRoot().getOpt());
        String destParentValue = cmd.getOptionValue(commands.getDestParent().getOpt());

        if (patternValue == null) {
            LOG.info("the pattern value is missing");
            return;
        }
        if (dest == null) {
            LOG.info("the destination folder value is missing");
            return;
        }
        if (destParentValue == null) {
            destParentValue = dest;
        }

        SokratesFileUtils.extractFiles(root, new File(root, dest), new File(root, destParentValue), patternValue);
    }
}
