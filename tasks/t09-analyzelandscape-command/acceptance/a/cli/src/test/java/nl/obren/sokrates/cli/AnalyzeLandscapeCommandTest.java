package nl.obren.sokrates.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test of task t09: "analyzeLandscape" is a command of the CLI, doing what "updateLandscape" does.
 */
public class AnalyzeLandscapeCommandTest {
    @TempDir
    File tempDir;

    @Test
    public void analyzeLandscapeCreatesTheLandscapeLikeUpdateLandscapeDoes() throws Exception {
        File rootA = new File(tempDir, "via-analyzeLandscape");
        File rootB = new File(tempDir, "via-updateLandscape");
        rootA.mkdirs();
        rootB.mkdirs();

        new CommandLineInterface().run(new String[]{"analyzeLandscape", "-analysisRoot", rootA.getPath()});
        new CommandLineInterface().run(new String[]{"updateLandscape", "-analysisRoot", rootB.getPath()});

        for (File root : new File[]{rootA, rootB}) {
            assertTrue(new File(root, "_sokrates_landscape/config.json").exists(), "landscape config created under " + root.getName());
            assertTrue(new File(root, "_sokrates_landscape/index.html").exists(), "landscape report created under " + root.getName());
        }
    }

    @Test
    public void analyzeLandscapeHelpDescribesTheCommandAndItsLandscapeOptions() throws Exception {
        String help = captureOutput(new String[]{"analyzeLandscape", "-help"});
        String updateLandscapeHelp = captureOutput(new String[]{"updateLandscape", "-help"});

        assertTrue(help.contains("java -jar sokrates.jar analyzeLandscape"), "the help names the command typed, got:\n" + help);
        assertTrue(help.contains("-analysisRoot"), "the landscape options are listed:\n" + help);
        assertTrue(help.contains("-recursive"), "the landscape options are listed:\n" + help);
        assertFalse(help.contains("java -jar sokrates.jar generateReports"), "not the generic usage of every command:\n" + help);
        assertTrue(updateLandscapeHelp.contains("java -jar sokrates.jar updateLandscape"), "the older name still works:\n" + updateLandscapeHelp);
    }

    private String captureOutput(String[] args) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8.name()));
        try {
            new CommandLineInterface().run(args);
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8.name());
    }
}
