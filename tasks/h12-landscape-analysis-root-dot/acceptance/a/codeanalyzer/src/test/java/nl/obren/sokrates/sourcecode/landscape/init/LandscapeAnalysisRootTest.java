package nl.obren.sokrates.sourcecode.landscape.init;

import nl.obren.sokrates.sourcecode.Metadata;
import nl.obren.sokrates.sourcecode.landscape.LandscapeConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

class LandscapeAnalysisRootTest {

    @Test
    void relativeRootIsStoredAsDotInTheDefaultLayout() throws Exception {
        // A relative root such as "./acme" must not be stored as such: roots starting with "." are
        // resolved against the landscape folder's parent, so "./acme" would point to ./acme/./acme.
        File root = new File("./target/landscape-root-test-" + System.nanoTime());
        try {
            FileUtils.write(new File(root, "alpha/_sokrates/reports/data/analysisResults.json"), "{}", UTF_8);

            LandscapeConfiguration created = new LandscapeAnalysisUpdater().updateConfiguration(root, null, new Metadata());
            assertEquals(".", created.getAnalysisRoot());
            assertEquals(1, created.getRepositories().size());

            File configFile = new File(root, "_sokrates_landscape/config.json");
            assertTrue(FileUtils.readFileToString(configFile, UTF_8).contains("\"analysisRoot\" : \".\""));
        } finally {
            FileUtils.deleteDirectory(root);
        }
    }

    @Test
    void absoluteRootIsStoredAsDotInTheDefaultLayout(@TempDir Path tmp) {
        File root = tmp.toFile();
        LandscapeConfiguration created = new LandscapeAnalysisUpdater().updateConfiguration(root, null, new Metadata());
        assertEquals(".", created.getAnalysisRoot());

        LandscapeConfiguration initialized = new LandscapeAnalysisInitiator()
                .initConfiguration(root, new File(root, "_sokrates_landscape/config.json"), false);
        assertEquals(".", initialized.getAnalysisRoot());
    }

    @Test
    void configurationKeptElsewhereKeepsTheRootAsGiven(@TempDir Path tmp) {
        File root = new File(tmp.toFile(), "repos");
        root.mkdirs();
        File elsewhere = new File(tmp.toFile(), "configs/acme/config.json");

        LandscapeConfiguration initialized = new LandscapeAnalysisInitiator().initConfiguration(root, elsewhere, false);
        assertEquals(root.getPath(), initialized.getAnalysisRoot());
    }

    @Test
    void brokenStoredRootIsHealedOnUpdate(@TempDir Path tmp) throws Exception {
        File root = tmp.toFile();
        new LandscapeAnalysisUpdater().updateConfiguration(root, null, new Metadata());

        File configFile = new File(root, "_sokrates_landscape/config.json");
        String json = FileUtils.readFileToString(configFile, UTF_8)
                .replaceAll("\"analysisRoot\" : \"[^\"]*\"", "\"analysisRoot\" : \"./acme\"");
        FileUtils.write(configFile, json, UTF_8);
        assertTrue(FileUtils.readFileToString(configFile, UTF_8).contains("./acme"), "precondition");

        LandscapeConfiguration updated = new LandscapeAnalysisUpdater().updateConfiguration(root, null, new Metadata());
        assertEquals(".", updated.getAnalysisRoot());
        assertTrue(FileUtils.readFileToString(configFile, UTF_8).contains("\"analysisRoot\" : \".\""));
    }
}
