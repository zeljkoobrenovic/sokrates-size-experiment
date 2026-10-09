/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.reports.core.RichTextReport;
import nl.obren.sokrates.reports.utils.GraphvizDependencyRenderer;
import nl.obren.sokrates.sourcecode.aspects.RenderingOptions;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;

import java.util.*;
import java.util.stream.Collectors;

class IndirectDependenciesRenderer {
    private RichTextReport report;
    private LogicalComponentsReportGenerator generator;

    IndirectDependenciesRenderer(RichTextReport report, LogicalComponentsReportGenerator generator) {
        this.report = report;
        this.generator = generator;
    }

    void renderIndirectDependencies(List<String> componentNames, GraphvizDependencyRenderer graphvizDependencyRenderer, RenderingOptions renderingOptions, boolean renderWithoutDependencies, List<ComponentDependency> dependencies) {
        if (renderingOptions.isRenderIndirectDependencies()) {
            report.startSubSection("Indirect Dependencies", "Dependecies via shared target or source components.");
            renderIndirectDependencies("Shared Targets", componentNames, graphvizDependencyRenderer, renderWithoutDependencies, getIndirectViaTargetsDependencies(dependencies));
            renderIndirectDependencies("Shared Sources", componentNames, graphvizDependencyRenderer, renderWithoutDependencies, getIndirectViaSourcesDependencies(dependencies));

            if (renderingOptions.isRenderInternalIndirectDependencies()) {
                renderInternalsOfIndirectDependencies("Targets", componentNames, graphvizDependencyRenderer, renderWithoutDependencies, getDependenciesWithSharedTargets(dependencies));
                renderInternalsOfIndirectDependencies("Sources", componentNames, graphvizDependencyRenderer, renderWithoutDependencies, getDependenciesWithSharedSources(dependencies));
            }
            report.endSection();
        }
    }

    private void renderIndirectDependencies(String type, List<String> componentNames, GraphvizDependencyRenderer graphvizDependencyRenderer, boolean renderWithoutDependencies, List<ComponentDependency> indirectDependencies) {
        if (indirectDependencies.size() > 0) {
            graphvizDependencyRenderer.setType("graph");
            graphvizDependencyRenderer.setArrow("--");

            report.addLevel3Header("Indirect Dependencies (" + type + ")");
            report.startShowMoreBlock("show details...");
            report.addParagraph("Dependencies via " + type.toLowerCase() + "  components.", "color: grey");
            generator.addDependencyGraphVisuals(indirectDependencies, componentNames.stream()
                    .filter(c -> renderWithoutDependencies || generator.isComponentInDependency(indirectDependencies, c))
                    .collect(Collectors.toCollection(ArrayList::new)), new ArrayList<>(), graphvizDependencyRenderer);

            report.endShowMoreBlock();
            report.addLineBreak();
            report.addLineBreak();
            report.addLineBreak();
        }
    }

    private void renderInternalsOfIndirectDependencies(String type, List<String> componentNames, GraphvizDependencyRenderer graphvizDependencyRenderer, boolean renderWithoutDependencies, List<ComponentDependency> sharedTargetDependencies) {
        if (sharedTargetDependencies.size() > 0) {
            graphvizDependencyRenderer.setType("digraph");
            graphvizDependencyRenderer.setArrow("->");
            report.addLevel3Header("Indirect Dependencies With Shared " + type + " Visible");
            report.startShowMoreBlock("show details...");
            report.addParagraph("Shared target components made visible.", "color: grey");
            generator.addDependencyGraphVisuals(sharedTargetDependencies, componentNames.stream()
                    .filter(c -> renderWithoutDependencies || generator.isComponentInDependency(sharedTargetDependencies, c))
                    .collect(Collectors.toCollection(ArrayList::new)), new ArrayList<>(), graphvizDependencyRenderer);
            report.endShowMoreBlock();
            report.addLineBreak();
            report.addLineBreak();
            report.addLineBreak();
        }
    }

    private List<ComponentDependency> getIndirectViaTargetsDependencies(List<ComponentDependency> dependencies) {
        ArrayList<ComponentDependency> indirect = new ArrayList<>();
        Map<String, ComponentDependency> map = new HashMap<>();

        List<String> alreadyCovered = new ArrayList<>();

        getMergedDependencies(dependencies).forEach(dependency1 -> {
            dependencies.stream()
                    .filter(dependency2 -> !dependency1.getFromComponent().equals(dependency2.getFromComponent()))
                    .filter(dependency2 -> dependency1.getToComponent().equalsIgnoreCase(dependency2.getToComponent()))
                    .forEach(dependency2 -> {
                        String coverKey1 = dependency1.getFromComponent() + " --" + dependency1.getToComponent() + "--> " + dependency2.getFromComponent();
                        String coverKey2 = dependency2.getFromComponent() + " --" + dependency1.getToComponent() + "--> " + dependency1.getFromComponent();
                        if (!(alreadyCovered.contains(coverKey1) || alreadyCovered.contains(coverKey2))) {
                            alreadyCovered.add(coverKey1);
                            addIndirectInternalDependency(indirect, map, dependency1.getFromComponent(), dependency2.getFromComponent());
                        }
                    });
        });

        return indirect;
    }

    private List<ComponentDependency> getMergedDependencies(List<ComponentDependency> dependencies) {
        Map<String, ComponentDependency> mergeMap = new HashMap<>();
        List<ComponentDependency> mergedDependencies = new ArrayList<>();
        dependencies.forEach(dependency -> {
            String key = dependency.getFromComponent() + " -> " + dependency.getToComponent();
            if (mergeMap.containsKey(key)) {
                mergeMap.get(key).increment(dependency.getCount());
            } else {
                ComponentDependency newDependency = new ComponentDependency();
                newDependency.setFromComponent(dependency.getFromComponent());
                newDependency.setToComponent(dependency.getToComponent());
                newDependency.setCount(1);
                mergedDependencies.add(newDependency);
                mergeMap.put(key, newDependency);
            }
        });
        return mergedDependencies;
    }

    private List<ComponentDependency> getIndirectViaSourcesDependencies(List<ComponentDependency> dependencies) {
        ArrayList<ComponentDependency> indirect = new ArrayList<>();
        Map<String, ComponentDependency> map = new HashMap<>();

        List<String> alreadyCovered = new ArrayList<>();

        getMergedDependencies(dependencies).forEach(dependency1 -> {
            dependencies.stream()
                    .filter(dependency2 -> !dependency1.getToComponent().equals(dependency2.getToComponent()))
                    .filter(dependency2 -> dependency1.getFromComponent().equalsIgnoreCase(dependency2.getFromComponent()))
                    .forEach(dependency2 -> {
                        String coverKey1 = dependency1.getToComponent() + " --" + dependency1.getFromComponent() + "--> " + dependency2.getToComponent();
                        String coverKey2 = dependency2.getToComponent() + " --" + dependency1.getFromComponent() + "--> " + dependency1.getToComponent();
                        if (!(alreadyCovered.contains(coverKey1) || alreadyCovered.contains(coverKey2))) {
                            alreadyCovered.add(coverKey1);
                            addIndirectInternalDependency(indirect, map, dependency1.getToComponent(), dependency2.getToComponent());
                        }
                    });
        });

        return indirect;
    }

    private void addIndirectInternalDependency(ArrayList<ComponentDependency> indirect, Map<String, ComponentDependency> map, String component1, String component2) {
        String key1 = component1 + "::" + component2;
        String key2 = component2 + "::" + component1;

        if (map.containsKey(key1)) {
            map.get(key1).increment(1);
        } else if (map.containsKey(key2)) {
            map.get(key2).increment(1);
        } else {
            ComponentDependency indirectDependency = new ComponentDependency(component1, component2);
            map.put(key1, indirectDependency);
            indirect.add(indirectDependency);
        }
    }

    private List<ComponentDependency> getDependenciesWithSharedTargets(List<ComponentDependency> dependencies) {
        Map<String, Set<String>> map = new HashMap<>();
        dependencies.forEach(d -> {
            String to = d.getToComponent();
            if (!map.containsKey(to)) {
                map.put(to, new HashSet<>());
            }
            map.get(to).add(d.getFromComponent());
        });
        return dependencies.stream().filter(d -> map.get(d.getToComponent()).size() > 1).collect(Collectors.toCollection(ArrayList::new));
    }

    private List<ComponentDependency> getDependenciesWithSharedSources(List<ComponentDependency> dependencies) {
        Map<String, Set<String>> map = new HashMap<>();
        dependencies.forEach(d -> {
            String from = d.getFromComponent();
            if (!map.containsKey(from)) {
                map.put(from, new HashSet<>());
            }
            map.get(from).add(d.getToComponent());
        });
        return dependencies.stream().filter(d -> map.get(d.getFromComponent()).size() > 1).collect(Collectors.toCollection(ArrayList::new));
    }
}
