package nl.obren.sokrates.reports.dataexporters;

import nl.obren.sokrates.common.io.JsonGenerator;
import nl.obren.sokrates.common.utils.ProgressFeedback;
import nl.obren.sokrates.sourcecode.analysis.CodeAnalyzer;
import nl.obren.sokrates.sourcecode.analysis.CodeAnalyzerSettings;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test of task t08: the scope file lists packaged into data/zips/all_files.zip are named after the
 * configured scope aspects (the names their loose text files are written under), so a renamed aspect no longer
 * breaks the zip exports.
 */
public class AspectFileListsZipTest {
    @TempDir
    File tempDir;

    @Test
    public void aRenamedScopeAspectStillGetsItsFileListIntoAllFilesZip() throws Exception {
        CodeConfiguration configuration = CodeConfiguration.getDefaultConfiguration();
        configuration.getBuildAndDeployment().setName("buildAndDeployment");

        File reportsFolder = analyzeAndExport(configuration);

        File allFilesZip = new File(reportsFolder, "data/zips/all_files.zip");
        assertTrue(allFilesZip.exists(), "all_files.zip is written although a scope aspect was renamed");
        assertEquals(Arrays.asList("aspect_main.txt", "aspect_test.txt", "aspect_generated.txt",
                        "aspect_buildAndDeployment.txt", "aspect_other.txt"), entryNames(allFilesZip),
                "the zip entries carry the names the file lists were written under");
        assertTrue(new File(reportsFolder, "data/zips/git-history.zip").exists(),
                "git-history.zip, written right after all_files.zip, is there too");
    }

    @Test
    public void theDefaultScopeAspectNamesKeepTheHistoricalEntryNames() throws Exception {
        File reportsFolder = analyzeAndExport(CodeConfiguration.getDefaultConfiguration());

        assertEquals(Arrays.asList("aspect_main.txt", "aspect_test.txt", "aspect_generated.txt",
                        "aspect_build_and_deployment.txt", "aspect_other.txt"),
                entryNames(new File(reportsFolder, "data/zips/all_files.zip")));
    }

    private File analyzeAndExport(CodeConfiguration configuration) throws Exception {
        File srcRoot = new File(tempDir, "repo");
        FileUtils.write(new File(srcRoot, "src/Main.java"), "public class Main {\n    void run() {\n    }\n}\n", StandardCharsets.UTF_8);
        FileUtils.write(new File(srcRoot, "test/MainTest.java"), "public class MainTest {\n}\n", StandardCharsets.UTF_8);
        FileUtils.write(new File(srcRoot, "git-history.txt"), "2024-01-10 dev@acme.com 1a2b3c src/Main.java\n", StandardCharsets.UTF_8);

        File configFile = new File(srcRoot, "_sokrates/config.json");
        configuration.setSrcRoot("..");
        FileUtils.write(configFile, new JsonGenerator().generate(configuration), StandardCharsets.UTF_8);

        CodeAnalyzerSettings settings = new CodeAnalyzerSettings();
        settings.setAnalyzeDuplication(false);
        CodeAnalysisResults results = new CodeAnalyzer(settings, configuration, configFile).analyze(new ProgressFeedback());

        File reportsFolder = new File(srcRoot, "_sokrates/reports");
        reportsFolder.mkdirs();
        new DataExporter(new ProgressFeedback()).saveData(configFile, configuration, reportsFolder, results);
        return reportsFolder;
    }

    private List<String> entryNames(File zip) throws Exception {
        List<String> names = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(zip)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                names.add(entries.nextElement().getName());
            }
        }
        return names;
    }
}
