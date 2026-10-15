package nl.obren.sokrates.cli;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test: "generateReports -dataOnly" stores the analysis data (the data/ folder) but no HTML reports,
 * explorers, visuals or source-code cache; a later run without the flag produces the full report set again.
 */
public class DataOnlyReportsTest {

    @Test
    public void dataOnlyStoresOnlyTheAnalysisData(@TempDir Path tmp) throws Exception {
        File repo = tmp.resolve("repo").toFile();
        FileUtils.write(new File(repo, "src/a.ts"), "export function a(x: number): number { return x + 1; }\n", UTF_8);
        FileUtils.write(new File(repo, "src/b.ts"), "export function b(y: string): string { return y.trim(); }\n", UTF_8);
        new CommandLineInterface().run(new String[]{"init", "-srcRoot", repo.getPath()});
        File config = new File(repo, "_sokrates/config.json");
        assertTrue(config.exists(), "init should create " + config);

        File reports = tmp.resolve("reports").toFile();
        new CommandLineInterface().run(new String[]{"generateReports", "-confFile", config.getPath(),
                "-outputFolder", reports.getPath(), "-dataOnly"});

        assertTrue(new File(reports, "data/analysisResults.json").exists(), "-dataOnly must still store the analysis data");
        assertTrue(new File(reports, "data/config.json").exists(), "-dataOnly must still store the analysis data");
        assertTrue(new File(reports, "data/text/aspect_main.txt").exists(), "-dataOnly must still store the analysis data");
        assertFalse(new File(reports, "html").exists(), "-dataOnly must not write HTML reports or visuals (html/)");
        assertFalse(new File(reports, "explorers").exists(), "-dataOnly must not write the explorers");
        assertFalse(new File(reports, "src").exists(), "-dataOnly must not write the source-code cache (src/)");

        new CommandLineInterface().run(new String[]{"generateReports", "-confFile", config.getPath(),
                "-outputFolder", reports.getPath()});

        assertTrue(new File(reports, "html/index.html").exists(), "without -dataOnly the HTML reports are generated");
        assertTrue(new File(reports, "explorers").exists(), "without -dataOnly the explorers are generated");
        assertTrue(new File(reports, "src").exists(), "without -dataOnly the source-code cache is written");
    }
}
