package nl.obren.sokrates.reports.landscape.data;

import nl.obren.sokrates.reports.dataexporters.ExportParity;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.ContributorRepositoryInfo;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContributorRepositoryInfoExportTest {
    // repositoryAnalysisResults is exported as the repository's name and description (from its metadata)
    private static final Set<String> MODEL_ONLY = Collections.singleton("repositoryAnalysisResults");
    private static final Set<String> EXPORT_ONLY = new HashSet<>(Arrays.asList("name", "description"));

    @Test
    void from() {
        Set<String> shared = ExportParity.assertParity(ContributorRepositoryInfo.class, ContributorRepositoryInfoExport.class, MODEL_ONLY, EXPORT_ONLY);

        CodeAnalysisResults analysisResults = new CodeAnalysisResults();
        analysisResults.getMetadata().setName("repo");
        analysisResults.getMetadata().setDescription("a repository");
        RepositoryAnalysisResults repository = new RepositoryAnalysisResults();
        repository.setAnalysisResults(analysisResults);
        ContributorRepositoryInfo info = new ContributorRepositoryInfo(repository, "2020-01-01", "2021-01-01",
                10, 1, 2, 3, 4, Arrays.asList("2020-01-01", "2021-01-01"));

        ContributorRepositoryInfoExport export = ContributorRepositoryInfoExport.from(info);

        ExportParity.assertValuesCopied(info, export, shared);
        assertEquals("repo", export.getName());
        assertEquals("a repository", export.getDescription());
    }
}
