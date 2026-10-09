/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.dataexporters;

import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import nl.obren.sokrates.sourcecode.filehistory.FilePairChangedTogether;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

class DependenciesDataExporter {
    private CodeAnalysisResults analysisResults;
    private File textDataFolder;

    DependenciesDataExporter(CodeAnalysisResults analysisResults, File textDataFolder) {
        this.analysisResults = analysisResults;
        this.textDataFolder = textDataFolder;
    }

    void exportDependencies(CodeAnalysisResults analysisResults) {
        exportDependencies("", "", "");
        analysisResults.getLogicalDecompositionsAnalysisResults().forEach(logicalDecompositionAnalysisResults -> {
            logicalDecompositionAnalysisResults.getComponentDependencies().forEach(componentDependency -> {
                exportDependencies(logicalDecompositionAnalysisResults.getKey(), componentDependency.getFromComponent(), componentDependency.getToComponent());
            });
        });
    }

    void saveTemporalDependencies(CodeAnalysisResults analysisResults) {
        List<FilePairChangedTogether> filePairsChangedTogether = analysisResults.getFilesHistoryAnalysisResults().getFilePairsChangedTogether();
        exportFilesChangedTogether(filePairsChangedTogether,
                "temporal_dependencies.txt");
        exportFilesChangedTogether(analysisResults.getFilesHistoryAnalysisResults().getFilePairsChangedTogetherInDifferentFolders(filePairsChangedTogether),
                "temporal_dependencies_different_folders.txt");
        List<FilePairChangedTogether> filePairsChangedTogether30Days = analysisResults.getFilesHistoryAnalysisResults().getFilePairsChangedTogether30Days();
        exportFilesChangedTogether(filePairsChangedTogether30Days,
                "temporal_dependencies_30_days.txt");
        exportFilesChangedTogether(analysisResults.getFilesHistoryAnalysisResults().getFilePairsChangedTogetherInDifferentFolders(filePairsChangedTogether30Days),
                "temporal_dependencies_different_folders_30_days.txt");
    }

    private void exportFilesChangedTogether(List<FilePairChangedTogether> filePairsChangedTogether, String fileName) {
        StringBuilder content = new StringBuilder();
        content.append("file 1\tfile 2\t# same commits\t# commits file 1\t# commits file 2\n");
        if (filePairsChangedTogether.size() > 0) {
            filePairsChangedTogether.sort((a, b) -> b.getCommits().size() - a.getCommits().size());

            int limit = Math.min(10000, filePairsChangedTogether.size());
            List<FilePairChangedTogether> limitedList = filePairsChangedTogether.subList(0, limit);

            limitedList.forEach(pair -> {
                content.append(pair.getSourceFile1().getRelativePath()).append("\t");
                content.append(pair.getSourceFile2().getRelativePath()).append("\t");
                content.append(pair.getCommits().size()).append("\t");
                content.append(pair.getCommitsCountFile1()).append("\t");
                content.append(pair.getCommitsCountFile2()).append("\n");
            });
        }
        try {
            FileUtils.write(new File(textDataFolder, fileName), content.toString(), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportDependencies(String filterLogicalDecomposition, String filterFrom, String filterTo) {
        analysisResults.getLogicalDecompositionsAnalysisResults().forEach(logicalDecomposition -> {
            String logicalDecompositionName = logicalDecomposition.getKey();
            if (shouldProcessLogicalDecomposition(filterLogicalDecomposition, logicalDecompositionName)) {
                StringBuilder content = new StringBuilder();
                String fileNamePrefix = DataExporter.dependenciesFileNamePrefix(filterFrom, filterTo, logicalDecompositionName);
                logicalDecomposition.getComponentDependencies().forEach(dependency -> {
                    content.append(appendDependency(filterFrom, filterTo, dependency));
                });
                try {
                    FileUtils.write(new File(textDataFolder, fileNamePrefix + ".txt"), content.toString(), UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String appendDependency(String filterFrom, String filterTo, ComponentDependency dependency) {
        StringBuilder content = new StringBuilder();

        String from = dependency.getFromComponent();
        String to = dependency.getToComponent();
        if (shouldAppendDependency(filterFrom, filterTo, from, to)) {
            dependency.getEvidence().forEach(evidence -> {
                content.append("from: " + from);
                content.append("\n");
                content.append("to: " + to);
                content.append("\nevidence:\n");
                content.append(" - file: \"");
                content.append(evidence.getPathFrom());
                content.append("\"\n");
                content.append("   contains \"");
                content.append(evidence.getEvidence());
                content.append("\"\n\n");
            });
        }

        return content.toString();
    }

    private boolean shouldProcessLogicalDecomposition(String filterLogicalDecomposition, String logicalDecompositionName) {
        return StringUtils.isBlank(filterLogicalDecomposition) || logicalDecompositionName.equalsIgnoreCase(filterLogicalDecomposition);
    }

    private boolean shouldAppendDependency(String filterFrom, String filterTo, String fromComponent, String toComponent) {
        return StringUtils.isBlank(filterFrom) || StringUtils.isBlank(filterTo) || (fromComponent.equalsIgnoreCase(filterFrom) && toComponent.equalsIgnoreCase(filterTo));
    }
}
