/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.generators.statichtml;

import nl.obren.sokrates.common.utils.RegexUtils;
import nl.obren.sokrates.sourcecode.analysis.results.LogicalDecompositionAnalysisResults;
import nl.obren.sokrates.sourcecode.aspects.ComponentGroup;
import nl.obren.sokrates.sourcecode.dependencies.ComponentDependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class ComponentGroupsUtils {
    static List<ComponentGroup> getComponentGroups(LogicalDecompositionAnalysisResults logicalDecomposition, List<ComponentDependency> dependencies, List<String> components) {
        List<ComponentGroup> componentGroups = new ArrayList<>();
        List<String> allNames = new ArrayList<>();
        Consumer<String> stringConsumer = component -> {
            if (!allNames.contains(component)) {
                allNames.add(component);
            }
        };
        components.forEach(stringConsumer);
        dependencies.forEach(dependency -> {
            stringConsumer.accept(dependency.getFromComponent());
            stringConsumer.accept(dependency.getToComponent());
        });

        List<String> componentsInAnyGroup = new ArrayList<>();
        logicalDecomposition.getLogicalDecomposition().getGroups().forEach(groupingRule -> {
            List<String> componentsInGroupFiltered = allNames.stream().filter(name -> RegexUtils.matchesEntirely(groupingRule.getPattern(), name)).collect(Collectors.toCollection(ArrayList::new));
            List<String> componentsInGroup = new ArrayList<>();
            componentsInGroupFiltered.forEach(c -> {
                if (!componentsInAnyGroup.contains(c)) {
                    componentsInGroup.add(c);
                    componentsInAnyGroup.add(c);
                }
            });
            if (componentsInGroup.size() > 0) {
                componentGroups.add(new ComponentGroup(groupingRule.getName(), componentsInGroup));
            }
        });

        return componentGroups;
    }

    static List<ComponentDependency> getGroupDependencies(List<ComponentDependency> dependencies, List<ComponentGroup> componentGroups) {
        List<ComponentDependency> groupDependencies = new ArrayList<>();
        Map<String, ComponentDependency> groupDependenciesMap = new HashMap<>();

        dependencies.forEach(dependency -> {
            String from = getGroup(dependency.getFromComponent(), componentGroups);
            String to = getGroup(dependency.getToComponent(), componentGroups);

            String key1 = from + "::" + to;
            String key2 = to + "::" + from;

            if (groupDependenciesMap.containsKey(key1)) {
                groupDependenciesMap.get(key1).setCount(groupDependenciesMap.get(key1).getCount() + dependency.getCount());
            } else if (groupDependenciesMap.containsKey(key2)) {
                groupDependenciesMap.get(key2).setCount(groupDependenciesMap.get(key1).getCount() + dependency.getCount());
            } else {
                ComponentDependency newDependency = new ComponentDependency(from, to);
                newDependency.setCount(dependency.getCount());
                groupDependencies.add(newDependency);
                groupDependenciesMap.put(key1, newDependency);
            }
        });

        return groupDependencies;
    }

    static String getGroup(String component, List<ComponentGroup> componentGroups) {
        for (ComponentGroup componentGroup : componentGroups) {
            if (componentGroup.getComponentNames().contains(component)) {
                return componentGroup.getName() + " (" + componentGroup.getComponentNames().size() + ")";
            }
        }

        return component;
    }
}
