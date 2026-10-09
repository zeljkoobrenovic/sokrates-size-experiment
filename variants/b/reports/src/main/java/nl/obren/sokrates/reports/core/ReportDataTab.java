/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.core;

import nl.obren.sokrates.reports.utils.PromptsUtils;
import nl.obren.sokrates.sourcecode.Link;
import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;

import java.util.Arrays;

class ReportDataTab {
    static void addPrompts(RichTextReport report, CodeAnalysisResults analysisResults) {
        report.addParagraph("Generative AI tools, like ChatGPT or Gemini, can help you explore and discuss various aspects of source code repositories using simple prompts and file uploads. Sokrates provides you with curated data that you can use to analyze your source code further.", "");

        PromptsUtils.addRepositoryPromptSection("git-history-analyzer", report, analysisResults, "Example Prompt 1: Repository Evolution Analyzer (based on git history)", "", Arrays.asList(new Link[]{new Link("git-history.zip", "../data/zips/git-history.zip")}));

        PromptsUtils.addRepositoryPromptSection("path-name-conventions-analyzer", report, analysisResults, "Example Prompt 2: File name conventions", "", Arrays.asList(new Link("files.json", "../data/files.json")));

        PromptsUtils.addRepositoryPromptSection("technology-analyzer", report, analysisResults, "Example Prompt 3: Technology analyzer (based of file paths)", "", Arrays.asList(new Link("files.json", "../data/files.json")));
    }

    static void addData(RichTextReport report, CodeAnalysisResults analysisResults) {
        addFileLists(report, analysisResults);
        addAnalysisResultsLinks(report);
        addZippedFiles(report);
    }

    private static void addFileLists(RichTextReport report, CodeAnalysisResults analysisResults) {
        AspectAnalysisResults main = analysisResults.getMainAspectAnalysisResults();
        AspectAnalysisResults test = analysisResults.getTestAspectAnalysisResults();
        AspectAnalysisResults build = analysisResults.getBuildAndDeployAspectAnalysisResults();
        AspectAnalysisResults generated = analysisResults.getGeneratedAspectAnalysisResults();
        AspectAnalysisResults other = analysisResults.getOtherAspectAnalysisResults();

        report.addLevel2Header("Lists of Files Per Scope");

        report.startUnorderedList();
        addListsOfFilesInScope(report, "main", main.getFilesCount());
        addListsOfFilesInScope(report, "test", test.getFilesCount());
        addListsOfFilesInScope(report, "build and deployment", build.getFilesCount());
        addListsOfFilesInScope(report, "generated", generated.getFilesCount());
        addListsOfFilesInScope(report, "other", other.getFilesCount());
        addItem(report, "FILES: ", "History Data", "../data/text/mainFilesWithHistory.txt");
        report.startListItem();
        report.addHtmlContent("IGNORED FILES: ");
        report.addNewTabLink("By Extension", "../data/text/excluded_files_ignored_extensions.txt");
        report.addHtmlContent(" | ");
        report.addNewTabLink("By Rule", "../data/text/excluded_files_ignored_rules.txt");
        report.endListItem();
        report.endUnorderedList();
    }

    private static void addAnalysisResultsLinks(RichTextReport report) {
        report.addLineBreak();
        report.addLevel2Header("Analysis Results");
        report.startUnorderedList();

        addItem(report, "CONFIGURATION: ", "JSON", "../data/config.json");

        addItem(report, "ALL ANALYSIS RESULTS: ", "JSON", "../data/analysisResults.json");

        addTxtAndJsonItem(report, "DUPLICATES: ", "../data/text/duplicates.txt", "../data/duplicates.json");

        addTxtAndJsonItem(report, "UNITS: ", "../data/text/units.txt", "../data/units.json");

        addTxtAndJsonItem(report, "CONTRIBUTORS: ", "../data/text/contributors.txt", "../data/contributors.json");

        addItem(report, "LOGICAL DECOMPOSITIONS: ", "JSON", "../data/logical_decompositions.json");

        addItem(report, "CONCERNS: ", "JSON", "../data/concerns.json");

        addItem(report, "CONTROLS: ", "TXT", "../data/text/controls.txt");

        addItem(report, "ALL METRICS: ", "TXT", "../data/text/metrics.txt");


        report.endUnorderedList();
    }

    private static void addZippedFiles(RichTextReport report) {
        //

        report.addLineBreak();
        report.addLevel2Header("Zipped Files");
        report.startUnorderedList();

        addItem(report, "GIT HISTORY: ", "ZIP", "../data/zips/git-history.zip");

        addItem(report, "ALL FILES IN ALL ANALYSIS SCOPES: ", "ZIP", "../data/zips/all_files.zip");


        report.endUnorderedList();
    }

    private static void addItem(RichTextReport report, String label, String linkLabel, String path) {
        report.startListItem();
        report.addHtmlContent(label);
        report.addNewTabLink(linkLabel, path);
        report.endListItem();
    }

    private static void addTxtAndJsonItem(RichTextReport report, String label, String txtPath, String jsonPath) {
        report.startListItem();
        report.addHtmlContent(label);
        report.addNewTabLink("TXT", txtPath);
        report.addHtmlContent(" | ");
        report.addNewTabLink("JSON", jsonPath);
        report.endListItem();
    }

    private static void addListsOfFilesInScope(RichTextReport report, String scopeName, int filesCount) {
        String technicalName = scopeName.toLowerCase().replace(" ", "_");
        boolean exists = filesCount > 0;
        String infoText = filesCount + (filesCount == 1 ? " file" : " files");
        String displayName = scopeName.toUpperCase();
        report.startListItem();
        if (exists) {
            report.addNewTabLink(displayName + " (" + infoText + ")", "../data/text/aspect_" + technicalName + ".txt");
        } else {
            report.addContentInDiv(displayName, "color: #c0c0c0");
        }
        report.endListItem();
    }
}
