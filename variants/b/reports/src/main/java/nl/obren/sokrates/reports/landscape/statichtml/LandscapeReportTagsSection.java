/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.common.utils.ProcessingStopwatch;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.landscape.statichtml.repositories.LandscapeRepositoriesTagsMatrixReport;
import nl.obren.sokrates.reports.landscape.statichtml.repositories.LandscapeRepositoriesTagsReport;
import nl.obren.sokrates.reports.landscape.utils.LandscapeGeneratorUtils;
import nl.obren.sokrates.reports.landscape.statichtml.repositories.TagMap;
import nl.obren.sokrates.sourcecode.landscape.RepositoryTag;
import nl.obren.sokrates.sourcecode.landscape.TagGroup;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nl.obren.sokrates.reports.landscape.statichtml.LandscapeReportGenerator.OPEN_IN_NEW_TAB_SVG_ICON;

/**
 * The tag maps of the landscape report (custom tags, file extension tags, hidden files/folders tags) and the
 * custom tags section with its tag reports.
 */
class LandscapeReportTagsSection {
    private final RichTextReport landscapeReport;
    private final LandscapeAnalysisResults landscapeAnalysisResults;
    private final File reportsFolder;
    private final List<TagGroup> tagGroups;
    private final TagMap customTagsMap;
    private final RichTextReport landscapeRepositoriesTags;
    private final RichTextReport landscapeRepositoriesTagsMatrix;
    private final RichTextReport landscapeRepositoriesExtensionTags;
    private final RichTextReport landscapeRepositoriesExtensionTagsMatrix;
    private TagMap extensionsTagsMap;
    private List<TagGroup> extensionTagGroups;

    LandscapeReportTagsSection(RichTextReport landscapeReport, LandscapeAnalysisResults landscapeAnalysisResults, File reportsFolder,
                               List<TagGroup> tagGroups, List<RepositoryAnalysisResults> repositories,
                               RichTextReport landscapeRepositoriesTags, RichTextReport landscapeRepositoriesTagsMatrix,
                               RichTextReport landscapeRepositoriesExtensionTags, RichTextReport landscapeRepositoriesExtensionTagsMatrix) {
        this.landscapeReport = landscapeReport;
        this.landscapeAnalysisResults = landscapeAnalysisResults;
        this.reportsFolder = reportsFolder;
        this.tagGroups = tagGroups;
        this.landscapeRepositoriesTags = landscapeRepositoriesTags;
        this.landscapeRepositoriesTagsMatrix = landscapeRepositoriesTagsMatrix;
        this.landscapeRepositoriesExtensionTags = landscapeRepositoriesExtensionTags;
        this.landscapeRepositoriesExtensionTagsMatrix = landscapeRepositoriesExtensionTagsMatrix;
        this.customTagsMap = updateTagsData(landscapeAnalysisResults, tagGroups, repositories);
    }

    TagMap getCustomTagsMap() {
        return customTagsMap;
    }

    private void getHiddenFilesTagGroup(List<RepositoryAnalysisResults> repositories, List<TagGroup> extensionTagGroups) {
        Set<String> hiddenFiles = new HashSet<>();
        Set<String> hiddenFolders = new HashSet<>();
        repositories.forEach(repository -> {
            repository.getFiles().forEach(path -> {
                File file = new File(path.getPath());
                String name = file.getName();
                if (name.startsWith(".")) {
                    hiddenFiles.add(name);
                }
                File parentFile = file.getParentFile();
                while (parentFile != null) {
                    if (parentFile.getName().startsWith(".")) {
                        hiddenFolders.add(parentFile.getName());
                    }
                    parentFile = parentFile.getParentFile();
                }
            });
        });

        TagGroup hiddenFoldersTags = new TagGroup("hidden folders");
        hiddenFoldersTags.setDescription("folders with \".*\" like names");
        hiddenFoldersTags.setColor("lightgrey");

        hiddenFolders.forEach(hiddenFolder -> {
            RepositoryTag tag = new RepositoryTag();
            tag.setGroup(hiddenFoldersTags);
            tag.setTag(hiddenFolder);
            tag.getPathPatterns().add("(|\\/)" + hiddenFolder.replaceAll("\\.", "[.]").replaceAll("\\-", "[-]") + "/.*");
            hiddenFoldersTags.getRepositoryTags().add(tag);
        });

        TagGroup hiddenFileTags = new TagGroup("hidden files");
        hiddenFileTags.setDescription("files with \".*\" like names");
        hiddenFileTags.setColor("lightgrey");

        hiddenFiles.forEach(hiddenFile -> {
            RepositoryTag tag = new RepositoryTag();
            tag.setGroup(hiddenFileTags);
            tag.setTag(hiddenFile);
            tag.getPathPatterns().add("(|\\/)" + hiddenFile.replaceAll("\\.", "[.]").replaceAll("\\-", "[-]"));
            hiddenFileTags.getRepositoryTags().add(tag);
        });

        extensionTagGroups.add(hiddenFoldersTags);
        extensionTagGroups.add(hiddenFileTags);
    }

    private TagMap updateTagsData(LandscapeAnalysisResults landscapeAnalysisResults, List<TagGroup> tagGroups, List<RepositoryAnalysisResults> repositories) {
        final TagMap customTagsMap;
        ProcessingStopwatch.start("reporting/tags/custom tags map");
        customTagsMap = new TagMap(landscapeAnalysisResults, tagGroups);
        customTagsMap.updateTagMap(repositories);
        ProcessingStopwatch.end("reporting/tags/custom tags map");

        ProcessingStopwatch.start("reporting/tags/extensions tags map");
        extensionTagGroups = getExtensionTagGroups();
        getHiddenFilesTagGroup(repositories, extensionTagGroups);
        extensionsTagsMap = new TagMap(landscapeAnalysisResults, extensionTagGroups);
        extensionsTagsMap.updateTagMap(repositories);
        ProcessingStopwatch.end("reporting/tags/extensions tags map");


        return customTagsMap;
    }

    void addTagsSection(List<RepositoryAnalysisResults> repositoryAnalysisResults) {
        landscapeReport.startSubSection("Custom Tags (" + customTagsMap.tagsCount() + ")", "");

        if (repositoryAnalysisResults.size() > 0) {
            landscapeReport.startDiv("margin-top: 14px; max-height: 400px");
            landscapeReport.addNewTabLink("<b>Open in a new tab</b>&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "repositories-tags.html");
            landscapeReport.addHtmlContent("&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;");
            landscapeReport.addNewTabLink("<b>Open expanded view</b> (stats per sub-folder)&nbsp;" + OPEN_IN_NEW_TAB_SVG_ICON, "repositories-tags-matrix.html");
            landscapeReport.endDiv();

            ProcessingStopwatch.start("reporting/repositories/tags");

            ProcessingStopwatch.start("reporting/tags/custom");
            new LandscapeRepositoriesTagsReport(landscapeAnalysisResults, tagGroups, customTagsMap, "custom", "repositories-tags-matrix.html", false)
                    .saveRepositoriesReport(landscapeRepositoriesTags, reportsFolder);
            ProcessingStopwatch.end("reporting/tags/custom");

            ProcessingStopwatch.start("reporting/tags/custom-matrix");
            new LandscapeRepositoriesTagsMatrixReport(landscapeAnalysisResults, tagGroups, customTagsMap, "custom-matrix", false)
                    .saveRepositoriesReport(landscapeRepositoriesTagsMatrix, "Custom Tags / Expanded View");
            ProcessingStopwatch.end("reporting/tags/custom-matrix");

            ProcessingStopwatch.start("reporting/tags/extensions");
            new LandscapeRepositoriesTagsReport(landscapeAnalysisResults, extensionTagGroups, extensionsTagsMap, "extension", "repositories-extensions-matrix.html", true)
                    .saveRepositoriesReport(landscapeRepositoriesExtensionTags, reportsFolder);
            ProcessingStopwatch.end("reporting/tags/extensions");

            ProcessingStopwatch.start("reporting/tags/extensions-matrix");
            new LandscapeRepositoriesTagsMatrixReport(landscapeAnalysisResults, extensionTagGroups, extensionsTagsMap, "extension-matrix", true)
                    .saveRepositoriesReport(landscapeRepositoriesExtensionTagsMatrix, "Extensions Tags / Expanded View");
            ProcessingStopwatch.end("reporting/tags/extensions-matrix");
            ProcessingStopwatch.end("reporting/repositories/tags");
        }

        landscapeReport.addLineBreak();
        landscapeReport.addHtmlContent("<iframe src='repositories-tags.html' frameborder=0 style='height: calc(100vh - 290px); width: 100%; margin-bottom: 0px; padding: 0;'></iframe>");

        landscapeReport.endSection();
    }

    private List<TagGroup> getExtensionTagGroups() {
        List<TagGroup> groups = new ArrayList<>();
        TagGroup mainProgrammingLanguages = new TagGroup("main file extensions", "#ffefd5");
        mainProgrammingLanguages.setDescription("file extensions with most lines of code in a repository");
        LandscapeGeneratorUtils.getLinesOfCodePerExtension(this.landscapeAnalysisResults, this.landscapeAnalysisResults.getMainLinesOfCodePerExtension()).forEach(extension -> {
            String lang = extension.getName().replaceAll(".*[.]", "").trim();
            RepositoryTag langTag = new RepositoryTag();
            langTag.setTag(lang);
            langTag.setMainExtensions(Arrays.asList(lang));
            langTag.setGroup(mainProgrammingLanguages);
            mainProgrammingLanguages.getRepositoryTags().add(langTag);
        });
        TagGroup programmingLanguages = new TagGroup("all file extensions", "#f0f0f0");
        programmingLanguages.setDescription("file extensions with at least one file in a repository");
        LandscapeGeneratorUtils.getLinesOfCodePerExtension(this.landscapeAnalysisResults, this.landscapeAnalysisResults.getMainLinesOfCodePerExtension()).forEach(extension -> {
            String lang = extension.getName().replaceAll(".*[.]", "").trim();
            RepositoryTag langTag = new RepositoryTag();
            langTag.setTag(lang);
            langTag.setAnyExtensions(Arrays.asList(lang));
            langTag.setGroup(programmingLanguages);
            programmingLanguages.getRepositoryTags().add(langTag);
        });

        groups.add(mainProgrammingLanguages);
        groups.add(programmingLanguages);

        return groups;
    }
}
