package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.reports.core.RichTextFragment;
import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.sourcecode.analysis.results.AspectAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.CodeAnalysisResults;
import nl.obren.sokrates.sourcecode.analysis.results.LogicalDecompositionAnalysisResults;
import nl.obren.sokrates.sourcecode.aspects.GroupingRule;
import nl.obren.sokrates.sourcecode.aspects.LogicalDecomposition;
import nl.obren.sokrates.sourcecode.aspects.NamedSourceCodeAspect;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance test of task t10: the Components report renders when group dependencies run in both directions
 * (they are merged into one link with the summed count) and when a component has no counted lines of code
 * (its dependency coverage bar shows 0% instead of a nonsense percentage).
 */
public class ComponentGroupDependenciesReportTest {

    @Test
    public void groupDependenciesInBothDirectionsAreMergedIntoOneLink() {
        LogicalDecomposition decomposition = decomposition("g1-a", "g1-b", "g2-a");
        decomposition.setGroups(new ArrayList<>(Arrays.asList(group("Group 1", "g1.*"), group("Group 2", "g2.*"))));

        CodeAnalysisResults results = results(decomposition,
                dependency("g1-a", "g2-a", 3, 10),
                dependency("g2-a", "g1-b", 2, 5));

        RichTextReport report = new RichTextReport("Components", "Components.html");
        LogicalComponentsReportGenerator generator = new LogicalComponentsReportGenerator(results, false);
        assertDoesNotThrow(() -> generator.addCodeOrganizationToReport(report),
                "the report renders when a group dependency also appears in the reverse direction");

        String groupGraphs = report.getRichTextFragments().stream()
                .filter(f -> f.getType() == RichTextFragment.Type.GRAPHVIZ)
                .map(RichTextFragment::getFragment)
                .filter(f -> f.contains("\"Group 1 (2)\" -> \"Group 2 (1)\""))
                .collect(Collectors.joining("\n"));
        assertTrue(groupGraphs.contains("\"Group 1 (2)\" -> \"Group 2 (1)\" [label=\" 5 \""),
                "the two directions are merged into one group link with the summed count:\n" + groupGraphs);
    }

    @Test
    public void coverageBarOfAComponentWithoutCountedLinesShowsZeroPercent() {
        LogicalDecomposition decomposition = decomposition("a", "b");
        CodeAnalysisResults results = results(decomposition, dependency("a", "b", 3, 10));

        RichTextReport report = new RichTextReport("Components", "Components.html");
        new LogicalComponentsReportGenerator(results, false).addCodeOrganizationToReport(report);

        String html = report.getRichTextFragments().stream()
                .filter(f -> f.getType() == RichTextFragment.Type.HTML)
                .map(RichTextFragment::getFragment)
                .collect(Collectors.joining("\n"));
        String coverage = html.contains("3 files, 10 LOC (") ? html.substring(html.indexOf("3 files, 10 LOC ("), html.indexOf("3 files, 10 LOC (") + 30) : "<not found>";
        assertTrue(html.contains("3 files, 10 LOC (0%)"), "a component with no counted lines gets a 0% coverage, got: " + coverage);
        assertFalse(html.contains("2147483647") || html.contains("Infinity") || html.contains("NaN"),
                "no overflow, infinity or NaN in the rendered coverage bar, got: " + coverage);
    }

    private LogicalDecomposition decomposition(String... componentNames) {
        LogicalDecomposition decomposition = new LogicalDecomposition("primary");
        List<NamedSourceCodeAspect> components = new ArrayList<>();
        for (String name : componentNames) {
            components.add(new NamedSourceCodeAspect(name));
        }
        decomposition.setComponents(components);
        return decomposition;
    }

    private GroupingRule group(String name, String pattern) {
        GroupingRule rule = new GroupingRule();
        rule.setName(name);
        rule.setPattern(pattern);
        return rule;
    }

    private ComponentDependency dependency(String from, String to, int count, int locFrom) {
        ComponentDependency dependency = new ComponentDependency(from, to);
        dependency.setCount(count);
        dependency.setLocFrom(locFrom);
        return dependency;
    }

    private CodeAnalysisResults results(LogicalDecomposition decomposition, ComponentDependency... dependencies) {
        LogicalDecompositionAnalysisResults decompositionResults = new LogicalDecompositionAnalysisResults(decomposition.getName());
        decompositionResults.setLogicalDecomposition(decomposition);
        List<AspectAnalysisResults> components = new ArrayList<>();
        decomposition.getComponents().forEach(component -> {
            AspectAnalysisResults componentResults = new AspectAnalysisResults(component.getName());
            componentResults.setAspect(component);
            components.add(componentResults);
        });
        decompositionResults.setComponents(components);
        decompositionResults.setComponentDependencies(new ArrayList<>(Arrays.asList(dependencies)));

        CodeAnalysisResults results = new CodeAnalysisResults();
        results.getMainAspectAnalysisResults().setLinesOfCode(100);
        results.getMainAspectAnalysisResults().setFilesCount(3);
        results.setLogicalDecompositionsAnalysisResults(new ArrayList<>(Arrays.asList(decompositionResults)));
        return results;
    }
}
