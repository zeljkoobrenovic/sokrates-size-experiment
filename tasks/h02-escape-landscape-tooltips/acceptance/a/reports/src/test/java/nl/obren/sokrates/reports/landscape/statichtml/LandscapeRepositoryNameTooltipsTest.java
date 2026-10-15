package nl.obren.sokrates.reports.landscape.statichtml;

import nl.obren.sokrates.reports.core.ReportFileExporter;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.ContributorsAnalysisResults;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.landscape.SokratesRepositoryLink;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.landscape.analysis.LandscapeAnalysisResults;
import nl.obren.sokrates.sourcecode.landscape.analysis.RepositoryAnalysisResults;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test of task h02: a repository name with HTML metacharacters is shown as text in the tooltips of
 * the landscape overview's per-language blocks ("Lines of Code in Main Code") and of the "Repositories Size
 * Distribution" table, instead of breaking out of the tooltip's title attribute.
 */
public class LandscapeRepositoryNameTooltipsTest {
    private static final String NAME = "acme <b>\"shop\"</b>";

    @TempDir
    File folder;

    @Test
    public void repositoryNamesInTheLanguageAndSizeTooltipsAreEscaped() throws Exception {
        LandscapeAnalysisResults results = new LandscapeAnalysisResults(new TeamsConfig(), new PeopleConfig());
        results.setRepositoryAnalysisResults(new ArrayList<>(Arrays.asList(repository(NAME, 5000))));

        File reportsFolder = new File(folder, "_sokrates_landscape");
        reportsFolder.mkdirs();
        LandscapeReportGenerator generator = new LandscapeReportGenerator(results, new ArrayList<>(), reportsFolder, reportsFolder);
        RichTextReport landscapeReport = generator.report().get(0);
        ReportFileExporter.exportHtml(reportsFolder, "", landscapeReport, "");
        String html = FileUtils.readFileToString(new File(reportsFolder, "index.html"), StandardCharsets.UTF_8);

        List<String> titles = titles(html);
        assertTrue(titles.contains("1 repository: " + NAME + " (5,000 LOC)"),
                "the java block's tooltip lists the repository by its full name: " + matching(titles, "repository:"));
        assertTrue(titles.contains(NAME + " 5000"),
                "the size distribution's tooltip lists the repository by its full name: " + matching(titles, "acme"));
    }

    // the values of all double-quoted title attributes, with HTML entities decoded and whitespace collapsed
    private List<String> titles(String html) {
        List<String> titles = new ArrayList<>();
        Matcher matcher = Pattern.compile("title=\"([^\"]*)\"").matcher(html);
        while (matcher.find()) {
            titles.add(StringEscapeUtils.unescapeHtml4(matcher.group(1)).replaceAll("\\s+", " ").trim());
        }
        return titles;
    }

    private List<String> matching(List<String> titles, String text) {
        List<String> found = new ArrayList<>();
        titles.stream().filter(title -> title.contains(text)).forEach(found::add);
        return found;
    }

    private RepositoryAnalysisResults repository(String name, int mainLoc) {
        CodeAnalysisResults analysisResults = new CodeAnalysisResults();
        analysisResults.setCodeConfiguration(new CodeConfiguration());
        analysisResults.getMetadata().setName(name);
        analysisResults.getMainAspectAnalysisResults().setLinesOfCode(mainLoc);
        analysisResults.getMainAspectAnalysisResults().setLinesOfCodePerExtension(
                new ArrayList<>(Arrays.asList(new NumericMetric("*.java", mainLoc))));
        analysisResults.setContributorsAnalysisResults(new ContributorsAnalysisResults());
        return new RepositoryAnalysisResults(new SokratesRepositoryLink("acme/data/analysisResults.json"), analysisResults, new ArrayList<>());
    }
}
