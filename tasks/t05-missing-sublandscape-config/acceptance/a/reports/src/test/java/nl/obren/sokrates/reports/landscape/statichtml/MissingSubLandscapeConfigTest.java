package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.landscape.SokratesRepositoryLink;
import nl.obren.sokrates.sourcecode.landscape.SubLandscapeLink;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test of task t05: a sub-landscape whose config.json is missing must not crash the parent landscape
 * report; its row in the Sub-landscapes tab is rendered with the default landscape logo.
 */
public class MissingSubLandscapeConfigTest {
    @TempDir
    File folder;

    @Test
    public void subLandscapeWithoutConfigDoesNotCrashTheReport() throws Exception {
        // the parent landscape lives in <folder>/_sokrates_landscape; sub-landscape links are resolved relative to it
        File reportsFolder = new File(folder, "_sokrates_landscape");
        reportsFolder.mkdirs();
        File presentConfig = new File(folder, "present/_sokrates_landscape/config.json");
        presentConfig.getParentFile().mkdirs();
        FileUtils.writeStringToFile(presentConfig, "{\"metadata\": {\"name\": \"Present\", \"logoLink\": \"present-logo.png\"}}", StandardCharsets.UTF_8);
        // "missing" has no folder at all (a stale link, or a child report that did not finish generating)

        LandscapeAnalysisResults results = new LandscapeAnalysisResults(new TeamsConfig(), new PeopleConfig());
        results.setRepositoryAnalysisResults(new ArrayList<>(Arrays.asList(repository("repo", 500, contributor("dev@acme.com", "2025-09-10")))));
        results.getConfiguration().setSubLandscapes(new ArrayList<>(Arrays.asList(
                new SubLandscapeLink("present", "present/_sokrates_landscape/index.html"),
                new SubLandscapeLink("missing", "missing/_sokrates_landscape/index.html"))));

        LandscapeReportGenerator generator = assertDoesNotThrow(
                () -> new LandscapeReportGenerator(results, new ArrayList<>(), reportsFolder, reportsFolder),
                "a sub-landscape without config.json must not abort the parent report");
        RichTextReport landscapeReport = generator.report().get(0);
        ReportFileExporter.exportHtml(reportsFolder, "", landscapeReport, "");
        String html = FileUtils.readFileToString(new File(reportsFolder, "index.html"), StandardCharsets.UTF_8);

        assertTrue(html.contains("present/_sokrates_landscape/index.html"), "the sub-landscape with a config is listed");
        assertTrue(html.contains("present-logo.png"), "the sub-landscape with a config shows its own logo");
        assertTrue(html.contains("missing/_sokrates_landscape/index.html"), "the sub-landscape without a config is still listed");
        assertTrue(html.contains("https://zeljkoobrenovic.github.io/sokrates-media/icons/landscape.png"), "the sub-landscape without a config shows the default landscape logo");
    }

    private RepositoryAnalysisResults repository(String name, int mainLoc, Contributor... contributors) {
        CodeAnalysisResults analysisResults = new CodeAnalysisResults();
        analysisResults.setCodeConfiguration(new CodeConfiguration());
        analysisResults.getMetadata().setName(name);
        analysisResults.getMainAspectAnalysisResults().setLinesOfCode(mainLoc);
        ContributorsAnalysisResults contributorsAnalysisResults = new ContributorsAnalysisResults();
        contributorsAnalysisResults.setContributors(new ArrayList<>(Arrays.asList(contributors)));
        analysisResults.setContributorsAnalysisResults(contributorsAnalysisResults);
        return new RepositoryAnalysisResults(new SokratesRepositoryLink(name + "/data/analysisResults.json"), analysisResults, new ArrayList<>());
    }

    private Contributor contributor(String email, String latestCommitDate) {
        Contributor contributor = new Contributor(email);
        contributor.setUserName(email.substring(0, email.indexOf('@')));
        contributor.setCommitsCount(3);
        contributor.setCommitsCount30Days(1);
        contributor.setCommitsCount90Days(2);
        contributor.setCommitsCount180Days(3);
        contributor.setCommitsCount365Days(3);
        contributor.setFirstCommitDate("2024-01-15");
        contributor.setLatestCommitDate(latestCommitDate);
        return contributor;
    }
}
