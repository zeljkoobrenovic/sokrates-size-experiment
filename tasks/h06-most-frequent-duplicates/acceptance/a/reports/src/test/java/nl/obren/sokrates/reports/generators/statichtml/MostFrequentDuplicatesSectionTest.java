package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.dataexporters.DataExporter;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.duplication.DuplicatedFileBlock;
import nl.obren.sokrates.sourcecode.duplication.DuplicationInstance;
import nl.obren.sokrates.common.utils.ProgressFeedback;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test: the duplication report renders a "Most Frequent Duplicates" section when the analysis found
 * most-frequent duplicates (and none when the list is empty), and the saved data contains the code fragments of
 * those duplicates when saving code fragments is enabled.
 */
public class MostFrequentDuplicatesSectionTest {

    private DuplicationInstance duplicate(int blockSize, String... paths) {
        DuplicationInstance instance = new DuplicationInstance();
        instance.setBlockSize(blockSize);
        StringBuilder content = new StringBuilder();
        for (int i = 1; i <= 20; i++) {
            content.append("line ").append(i).append("\n");
        }
        for (String path : paths) {
            DuplicatedFileBlock block = new DuplicatedFileBlock();
            SourceFile sourceFile = new SourceFile(new File(path), content.toString());
            sourceFile.setRelativePath(path);
            block.setSourceFile(sourceFile);
            block.setStartLine(1);
            block.setEndLine(blockSize);
            block.setCleanedStartLine(1);
            block.setCleanedEndLine(blockSize);
            block.setSourceFileCleanedLinesOfCode(100);
            instance.getDuplicatedFileBlocks().add(block);
        }
        return instance;
    }

    private CodeAnalysisResults results() {
        CodeAnalysisResults results = new CodeAnalysisResults();
        results.setCodeConfiguration(new CodeConfiguration());
        return results;
    }

    private String html(CodeAnalysisResults results, File reportsFolder) {
        RichTextReport report = new RichTextReport("Duplication", "Duplication.html");
        new DuplicationReportGenerator(results, reportsFolder).addDuplicationToReport(report);
        StringBuilder html = new StringBuilder();
        report.getRichTextFragments().forEach(fragment -> html.append(fragment.getFragment()));
        return html.toString();
    }

    @Test
    public void duplicationReportShowsMostFrequentDuplicates(@TempDir File reportsFolder) {
        CodeAnalysisResults results = results();
        results.getDuplicationAnalysisResults().getMostFrequentDuplicates().add(duplicate(8, "a/x.java", "b/y.java", "c/z.java"));
        results.getDuplicationAnalysisResults().getMostFrequentDuplicates().add(duplicate(6, "a/p.java", "b/q.java", "c/r.java"));

        String html = html(results, reportsFolder);

        assertTrue(html.contains("Most Frequent Duplicates"), "the duplication report should have a Most Frequent Duplicates section");
        assertTrue(html.contains("The list of 2 most frequently found duplicates."), "the section should state how many duplicates it lists");
        assertTrue(html.indexOf("Longest Duplicates") < html.indexOf("Most Frequent Duplicates"), "the section should come after Longest Duplicates");
    }

    @Test
    public void noSectionWithoutMostFrequentDuplicates(@TempDir File reportsFolder) {
        String html = html(results(), reportsFolder);

        assertTrue(html.contains("Longest Duplicates"));
        assertFalse(html.contains("Most Frequent Duplicates"), "no section should be rendered for an empty list");
    }

    @Test
    public void codeFragmentsOfMostFrequentDuplicatesAreSaved(@TempDir File folder) throws Exception {
        CodeAnalysisResults results = results();
        CodeConfiguration codeConfiguration = results.getCodeConfiguration();
        codeConfiguration.getAnalysis().setSaveCodeFragments(true);
        results.setFilesExcludedByExtension(new java.util.ArrayList<>());
        results.setIgnoredFilesGroups(new java.util.HashMap<>());
        results.getMainAspectAnalysisResults().setAspect(codeConfiguration.getMain());
        results.getTestAspectAnalysisResults().setAspect(codeConfiguration.getTest());
        results.getGeneratedAspectAnalysisResults().setAspect(codeConfiguration.getGenerated());
        results.getBuildAndDeployAspectAnalysisResults().setAspect(codeConfiguration.getBuildAndDeployment());
        results.getOtherAspectAnalysisResults().setAspect(codeConfiguration.getOther());
        results.getDuplicationAnalysisResults().getMostFrequentDuplicates().add(duplicate(8, "a/x.java", "b/y.java", "c/z.java"));

        File reportsFolder = new File(folder, "reports");
        reportsFolder.mkdirs();
        File configFile = new File(folder, "config.json");
        FileUtils.write(configFile, "{}", StandardCharsets.UTF_8);
        new DataExporter(new ProgressFeedback()).saveData(configFile, codeConfiguration, reportsFolder, results);

        File[] fragments = FileUtils.listFiles(folder, null, true).stream()
                .filter(file -> file.getName().equals("most_frequent_duplicates_1.java"))
                .toArray(File[]::new);
        assertTrue(fragments.length == 1, "the code fragment of the first most frequent duplicate should be saved as most_frequent_duplicates_1.java");
        String content = FileUtils.readFileToString(fragments[0], StandardCharsets.UTF_8);
        assertTrue(content.contains("a/x.java [1:8]") && content.contains("c/z.java [1:8]"), "the fragment should list every duplicated block");
    }
}
