package nl.obren.sokrates.sourcecode.landscape.analysis;

import nl.obren.sokrates.common.utils.ProgressFeedback;
import nl.obren.sokrates.sourcecode.SourceFile;
import nl.obren.sokrates.sourcecode.analysis.AnalysisUtils;
import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.aspects.NamedSourceCodeAspect;
import nl.obren.sokrates.sourcecode.landscape.PeopleConfig;
import nl.obren.sokrates.sourcecode.landscape.TeamsConfig;
import nl.obren.sokrates.sourcecode.metrics.MetricsList;
import nl.obren.sokrates.sourcecode.metrics.NumericMetric;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Acceptance test of task t02: the per-extension entries of a scope's analysis results
 * (linesOfCodePerExtension, fileCountPerExtension) are named by the bare lower-cased extension
 * ("java"), not by the decorated "  *.java"; the metric ids stay LINES_OF_CODE_MAIN_EXT_JAVA etc.
 * A landscape aggregates repositories analyzed before the change ("  *.java") and after ("java")
 * under one bare-extension entry, in the main and in the merged "other" category.
 */
public class LandscapeExtensionMetricNamesTest {

    @Test
    public void scopeAnalysisNamesPerExtensionEntriesByTheBareExtension() {
        NamedSourceCodeAspect main = new NamedSourceCodeAspect("main");
        main.getSourceFiles().add(sourceFile("/repo/src/A.java", 30));
        main.getSourceFiles().add(sourceFile("/repo/src/B.JAVA", 20));
        main.getSourceFiles().add(sourceFile("/repo/src/app.js", 10));

        AspectAnalysisResults results = new AspectAnalysisResults();
        MetricsList metrics = new MetricsList();
        AnalysisUtils.analyze("", main, new ArrayList<>(), new ProgressFeedback(), results, metrics, new StringBuffer(), System.currentTimeMillis());

        assertEquals("[java=50, js=10]", describe(results.getLinesOfCodePerExtension()));
        assertEquals("[java=2, js=1]", describe(results.getFileCountPerExtension()));
        assertNotNull(metrics.getMetricById("LINES_OF_CODE_MAIN_EXT_JAVA"), "metric ids are unchanged");
        assertEquals(50, metrics.getMetricById("LINES_OF_CODE_MAIN_EXT_JAVA").getValue().intValue());
        assertEquals(1, metrics.getMetricById("NUMBER_OF_FILES_MAIN_EXT_JS").getValue().intValue());
    }

    @Test
    public void legacyAndBareExtensionNamesMergeUnderTheBareExtension() {
        RepositoryAnalysisResults legacy = repository("legacy",
                Arrays.asList(new NumericMetric("  *.java", 100), new NumericMetric("  *.xml", 10)),
                Arrays.asList(new NumericMetric("  *.sh", 5)),
                Arrays.asList(new NumericMetric("  *.md", 7)));
        RepositoryAnalysisResults current = repository("current",
                Arrays.asList(new NumericMetric("java", 50), new NumericMetric("kt", 20)),
                Arrays.asList(new NumericMetric("sh", 1)),
                Arrays.asList(new NumericMetric("sh", 2)));

        LandscapeAnalysisResults landscape = new LandscapeAnalysisResults(new TeamsConfig(), new PeopleConfig());
        landscape.setRepositoryAnalysisResults(new ArrayList<>(Arrays.asList(legacy, current)));

        List<NumericMetric> main = landscape.getMainLinesOfCodePerExtension();
        assertEquals("[java=150, kt=20, xml=10]", describe(main));
        assertEquals(2, main.get(0).getDescription().size(), "java came from both repositories");

        List<NumericMetric> other = landscape.getOtherLinesOfCodePerExtension();
        assertEquals("[sh=8, md=7]", describe(other));
    }

    private SourceFile sourceFile(String path, int linesOfCode) {
        SourceFile sourceFile = new SourceFile(new File(path), "");
        sourceFile.setLinesOfCode(linesOfCode);
        return sourceFile;
    }

    private RepositoryAnalysisResults repository(String name, List<NumericMetric> mainLoc, List<NumericMetric> buildLoc, List<NumericMetric> otherLoc) {
        CodeAnalysisResults analysis = new CodeAnalysisResults();
        analysis.getMetadata().setName(name);
        analysis.getMainAspectAnalysisResults().setLinesOfCode(mainLoc.stream().mapToInt(m -> m.getValue().intValue()).sum());
        analysis.getMainAspectAnalysisResults().getLinesOfCodePerExtension().addAll(mainLoc);
        analysis.getBuildAndDeployAspectAnalysisResults().getLinesOfCodePerExtension().addAll(buildLoc);
        analysis.getOtherAspectAnalysisResults().getLinesOfCodePerExtension().addAll(otherLoc);
        return new RepositoryAnalysisResults(null, analysis, null);
    }

    private String describe(List<NumericMetric> metrics) {
        return metrics.stream().map(m -> m.getName() + "=" + m.getValue().intValue()).collect(Collectors.toList()).toString();
    }
}
