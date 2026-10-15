package nl.obren.sokrates.cli;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test: what Sokrates itself writes next to the code (_sokrates/, _sokrates_landscape/, the git-*.txt
 * exports, sokrates_*.json) is always ignored. init writes the four ignore rules although no such file exists when it
 * scans the tree, and generateReports also ignores those files for a configuration that lacks the rules, without
 * changing the configuration file.
 */
public class SokratesOutputNeverAnalyzedTest {

    private static final String[] RULES = {
            ".*/_sokrates/.*",
            ".*/_sokrates_landscape/.*",
            ".*/git[-][a-zA-Z0-9_]+[.]txt",
            ".*/sokrates_.*?[.]json"
    };

    @Test
    public void sokratesOutputIsNeverAnalyzed(@TempDir Path tmp) throws Exception {
        File repo = tmp.resolve("repo").toFile();
        FileUtils.write(new File(repo, "src/a.ts"), "export function a(x: number): number { return x + 1; }\n", UTF_8);
        FileUtils.write(new File(repo, "notes.txt"), "Some notes.\nMore notes.\n", UTF_8);

        // The tree has no Sokrates output yet when init scans it.
        new CommandLineInterface().run(new String[]{"init", "-srcRoot", repo.getPath()});
        File config = new File(repo, "_sokrates/config.json");
        assertTrue(config.exists(), "init should create " + config);
        String configText = FileUtils.readFileToString(config, UTF_8);
        assertTrue(configText.contains("\"txt\""), "the fixture relies on txt being an analyzed extension: " + configText);
        for (String rule : RULES) {
            assertTrue(configText.contains("\"" + rule.replace("\\", "\\\\") + "\""),
                    "init should always write the ignore rule " + rule + " although no file matched it at init");
        }

        // Later, the history is extracted and a landscape is created in the tree.
        FileUtils.write(new File(repo, "git-history.txt"), "2025-09-01 ada@example.com 0123456789abcdef src/a.ts\n", UTF_8);
        FileUtils.write(new File(repo, "git-merges.txt"), "2025-09-01 ada@example.com 0123456789abcdef\n", UTF_8);
        FileUtils.write(new File(repo, "_sokrates_landscape/notes.txt"), "Landscape notes.\n", UTF_8);

        File reports = tmp.resolve("reports").toFile();
        generateReports(config, reports);
        assertNoSokratesOutputInScopes(reports);

        // A configuration without these rules (e.g. written by an older version) is protected at analysis time too,
        // and the user's configuration file is left as it is.
        String withoutRules = configText;
        for (String rule : RULES) {
            withoutRules = withoutRules.replace("\"" + rule.replace("\\", "\\\\") + "\"", "\".*/nothing-like-this/.*\"");
        }
        withoutRules = withoutRules.replace("\".*/git[-]history[.]txt\"", "\".*/nothing-like-this/.*\"")
                .replace("\".*/git[-]merges[.]txt\"", "\".*/nothing-like-this/.*\"");
        FileUtils.write(config, withoutRules, UTF_8);
        FileUtils.deleteDirectory(reports);
        generateReports(config, reports);
        assertNoSokratesOutputInScopes(reports);
        assertEquals(withoutRules, FileUtils.readFileToString(config, UTF_8), "the configuration file must not be rewritten");
    }

    private static void generateReports(File config, File reports) {
        try {
            new CommandLineInterface().run(new String[]{"generateReports", "-confFile", config.getPath(), "-outputFolder", reports.getPath()});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertNoSokratesOutputInScopes(File reports) throws Exception {
        StringBuilder all = new StringBuilder();
        for (String name : new String[]{"aspect_main.txt", "aspect_test.txt", "aspect_generated.txt", "aspect_build_and_deployment.txt", "aspect_other.txt"}) {
            File file = new File(reports, "data/text/" + name);
            if (file.exists()) {
                all.append(FileUtils.readFileToString(file, UTF_8));
            }
        }
        String scoped = all.toString();
        assertTrue(scoped.contains("src/a.ts") && scoped.contains("notes.txt"), "the real files stay in scope:\n" + scoped);
        assertFalse(scoped.contains("git-history.txt"), "the git history export must not be analyzed:\n" + scoped);
        assertFalse(scoped.contains("git-merges.txt"), "the git merges export must not be analyzed:\n" + scoped);
        assertFalse(scoped.contains("_sokrates_landscape/"), "the landscape folder must not be analyzed:\n" + scoped);
    }
}
