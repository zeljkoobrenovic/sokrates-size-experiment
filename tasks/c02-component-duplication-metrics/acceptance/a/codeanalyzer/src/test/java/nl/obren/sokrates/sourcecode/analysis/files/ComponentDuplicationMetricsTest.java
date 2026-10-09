package nl.obren.sokrates.sourcecode.analysis.files;

import nl.obren.sokrates.common.utils.ProgressFeedback;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.analysis.AnalysisUtils;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.aspects.LogicalDecomposition;
import nl.obren.sokrates.sourcecode.aspects.NamedSourceCodeAspect;
import nl.obren.sokrates.sourcecode.core.CodeConfiguration;
import nl.obren.sokrates.sourcecode.duplication.DuplicationInstance;
import nl.obren.sokrates.sourcecode.metrics.Metric;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Acceptance test of task c02: the per-component duplication metrics report the component's cleaned line
 * count and its duplication percentage (duplicated / cleaned lines), and the most frequent duplicates are
 * listed by descending number of occurrences.
 */
public class ComponentDuplicationMetricsTest {
    private static final String DECOMPOSITION = "primary";

    @Test
    public void perComponentMetricsDivideDuplicatedLinesByCleanedLines() {
        String shared = block("shared");
        List<SourceFile> dup = Arrays.asList(
                file("dup/one.unknown", shared + block("one")),
                file("dup/two.unknown", shared + block("two")));
        List<SourceFile> clean = Arrays.asList(
                file("clean/three.unknown", block("three") + "extra 1\nextra 2\nextra 3\nextra 4\n"));

        CodeAnalysisResults results = analyze(components(component("dup", dup), component("clean", clean)));

        assertEquals(12, metric(results, "DUPLICATION_NUMBER_OF_DUPLICATED_LINES", "dup").getValue().intValue());
        assertEquals(24, metric(results, "DUPLICATION_NUMBER_OF_CLEANED_LINES", "dup").getValue().intValue(),
                "the cleaned line count of the component, not its duplicated line count");
        assertEquals(50.0, metric(results, "DUPLICATION_PERCENTAGE", "dup").getValue().doubleValue(), 0.01,
                "12 duplicated of 24 cleaned lines");

        assertEquals(0, metric(results, "DUPLICATION_NUMBER_OF_DUPLICATED_LINES", "clean").getValue().intValue());
        assertEquals(10, metric(results, "DUPLICATION_NUMBER_OF_CLEANED_LINES", "clean").getValue().intValue());
        assertEquals(0.0, metric(results, "DUPLICATION_PERCENTAGE", "clean").getValue().doubleValue(), 0.01,
                "a component without duplicates has 0%, not NaN");
    }

    @Test
    public void mostFrequentDuplicatesAreOrderedByNumberOfOccurrences() {
        String four = block("four");
        String three = block("three");
        List<SourceFile> files = Arrays.asList(
                file("a.unknown", three + block("pad a")),
                file("b.unknown", four + block("pad b")),
                file("c.unknown", three + block("pad c")),
                file("d.unknown", four + block("pad d")),
                file("e.unknown", three + block("pad e")),
                file("f.unknown", four + block("pad f")),
                file("g.unknown", four + block("pad g")));

        CodeAnalysisResults results = analyze(components(component("all", files)));

        List<Integer> occurrences = results.getDuplicationAnalysisResults().getMostFrequentDuplicates().stream()
                .map(d -> d.getDuplicatedFileBlocks().size()).collect(Collectors.toList());
        assertEquals(Arrays.asList(4, 3), occurrences, "most frequent first");
    }

    private CodeAnalysisResults analyze(List<NamedSourceCodeAspect> components) {
        CodeConfiguration configuration = new CodeConfiguration();
        List<SourceFile> allFiles = new ArrayList<>();
        components.forEach(component -> allFiles.addAll(component.getSourceFiles()));
        configuration.getMain().setSourceFiles(allFiles);

        LogicalDecomposition decomposition = new LogicalDecomposition(DECOMPOSITION);
        decomposition.setComponents(new ArrayList<>(components));
        configuration.setLogicalDecompositions(new ArrayList<>(Arrays.asList(decomposition)));

        CodeAnalysisResults results = new CodeAnalysisResults();
        results.setCodeConfiguration(configuration);
        new DuplicationAnalyzer(results).analyze(new ProgressFeedback());
        return results;
    }

    private Metric metric(CodeAnalysisResults results, String name, String component) {
        String id = AnalysisUtils.getMetricId(name) + "_" + DECOMPOSITION + "_" + component;
        Metric metric = results.getMetricsList().getMetricById(id);
        assertNotNull(metric, "metric " + id);
        return metric;
    }

    private List<NamedSourceCodeAspect> components(NamedSourceCodeAspect... components) {
        return Arrays.asList(components);
    }

    private NamedSourceCodeAspect component(String name, List<SourceFile> files) {
        NamedSourceCodeAspect component = new NamedSourceCodeAspect(name);
        component.setFiltering(DECOMPOSITION);
        component.setSourceFiles(new ArrayList<>(files));
        files.forEach(file -> file.getLogicalComponents().add(component));
        return component;
    }

    private SourceFile file(String path, String content) {
        SourceFile sourceFile = new SourceFile(new File(path), content);
        sourceFile.setRelativePath(path);
        return sourceFile;
    }

    /** Six distinct lines marked with the given tag (the minimal duplication block size is 6). */
    private String block(String tag) {
        StringBuilder content = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            content.append(tag).append(" line ").append(i).append("\n");
        }
        return content.toString();
    }
}
