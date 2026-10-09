package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.contributors.Contributor;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.landscape.SokratesRepositoryLink;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test of task t06: the landscape's contributor lists hold every contributor; the contributorsListLimit
 * setting (formerly 1000 by default) no longer truncates them. The limit is set to 2 here with 4 contributors so the
 * behaviour is observable with a small landscape.
 */
public class AllContributorsExportedTest {
    private static final List<String> EMAILS = Arrays.asList("ann@acme.com", "bob@acme.com", "cid@acme.com", "dee@acme.com");

    @TempDir
    File folder;

    @Test
    public void everyContributorIsExportedEvenWhenTheListLimitIsLower() throws Exception {
        LandscapeAnalysisResults results = new LandscapeAnalysisResults(new TeamsConfig(), new PeopleConfig());
        results.getConfiguration().setContributorsListLimit(2);
        results.setRepositoryAnalysisResults(new ArrayList<>(Arrays.asList(repository("repo", 500,
                contributor("ann@acme.com", 9), contributor("bob@acme.com", 7),
                contributor("cid@acme.com", 3), contributor("dee@acme.com", 1)))));

        File reportsFolder = new File(folder, "_sokrates_landscape");
        reportsFolder.mkdirs();
        LandscapeReportGenerator generator = new LandscapeReportGenerator(results, new ArrayList<>(), reportsFolder, reportsFolder);
        List<RichTextReport> reports = generator.report();
        reports.forEach(report -> ReportFileExporter.exportHtml(reportsFolder, "", report, ""));

        for (String fileName : Arrays.asList("contributors.html", "contributors-recent.html")) {
            String html = FileUtils.readFileToString(new File(reportsFolder, fileName), StandardCharsets.UTF_8);
            EMAILS.forEach(email -> assertTrue(html.contains(email), fileName + " lists " + email));
            assertFalse(html.contains("The list is limited to"), fileName + " is not truncated");
        }

        String index = FileUtils.readFileToString(new File(reportsFolder, "index.html"), StandardCharsets.UTF_8);
        assertEquals(EMAILS.size(), countOf(index, "cumulative commits (top "),
                "the recent-contributors commit bars of the Contributors tab show one bar per contributor");
    }

    private int countOf(String text, String part) {
        int count = 0;
        for (int i = text.indexOf(part); i >= 0; i = text.indexOf(part, i + part.length())) {
            count++;
        }
        return count;
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

    // every contributor is recently active (commits in the past 30 days)
    private Contributor contributor(String email, int commits) {
        Contributor contributor = new Contributor(email);
        contributor.setUserName(email.substring(0, email.indexOf('@')));
        contributor.setCommitsCount(commits);
        contributor.setCommitsCount30Days(commits);
        contributor.setCommitsCount90Days(commits);
        contributor.setCommitsCount180Days(commits);
        contributor.setCommitsCount365Days(commits);
        contributor.setFirstCommitDate("2024-01-15");
        contributor.setLatestCommitDate("2025-09-10");
        return contributor;
    }
}
