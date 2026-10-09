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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test of task t07: the "past 90d" block of the landscape's repositories summary shows the
 * lines of code of the repositories updated in the past 90 days, not of those updated in the past 180 days.
 */
public class RepositoriesPast90DaysBlockTest {
    @TempDir
    File folder;

    @Test
    public void past90DaysBlockCountsOnlyRepositoriesUpdatedInThePast90Days() throws Exception {
        LandscapeAnalysisResults results = new LandscapeAnalysisResults(new TeamsConfig(), new PeopleConfig());
        results.setRepositoryAnalysisResults(new ArrayList<>(Arrays.asList(
                repository("fresh", 500, contributor("fresh@acme.com", "2025-09-10", 1, 2, 2, 2)),
                repository("older", 2000, contributor("older@acme.com", "2025-05-10", 0, 0, 3, 3)))));

        File reportsFolder = new File(folder, "_sokrates_landscape");
        reportsFolder.mkdirs();
        LandscapeReportGenerator generator = new LandscapeReportGenerator(results, new ArrayList<>(), reportsFolder, reportsFolder);
        RichTextReport landscapeReport = generator.report().get(0);
        ReportFileExporter.exportHtml(reportsFolder, "", landscapeReport, "");
        String html = FileUtils.readFileToString(new File(reportsFolder, "index.html"), StandardCharsets.UTF_8);

        assertEquals("2.5K", locOfBlock(html, "past 180d"), "the past 180d block counts both repositories");
        assertEquals("500", locOfBlock(html, "past 90d"), "the past 90d block counts only the repository updated in the past 90 days");
        assertEquals("500", locOfBlock(html, "past 30d"), "the past 30d block counts only the repository updated in the past 30 days");
    }

    // the lines-of-code figure of the info block with the given label (plain text, e.g. "500" or "2.5K"):
    // the number between the label and the first " LOC" after it
    private String locOfBlock(String html, String label) {
        int start = html.indexOf(label);
        assertTrue(start >= 0, "the report has a '" + label + "' block");
        int end = html.indexOf(" LOC", start);
        assertTrue(end > start, "the '" + label + "' block shows lines of code");
        String between = html.substring(start + label.length(), end);
        return between.substring(between.lastIndexOf("<b>")).replaceAll("<[^>]*>", "");
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

    private Contributor contributor(String email, String latestCommitDate, int commits30Days, int commits90Days, int commits180Days, int commits365Days) {
        Contributor contributor = new Contributor(email);
        contributor.setUserName(email.substring(0, email.indexOf('@')));
        contributor.setCommitsCount(commits365Days);
        contributor.setCommitsCount30Days(commits30Days);
        contributor.setCommitsCount90Days(commits90Days);
        contributor.setCommitsCount180Days(commits180Days);
        contributor.setCommitsCount365Days(commits365Days);
        contributor.setFirstCommitDate("2024-01-15");
        contributor.setLatestCommitDate(latestCommitDate);
        return contributor;
    }
}
