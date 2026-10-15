/*
 * Copyright (c) 2021 Željko Obrenović. All rights reserved.
 */

package nl.obren.sokrates.reports.dataexporters;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Compares the bean properties of a model class with those of its export DTO, so that a field added to the model
 * (and not to the DTO's {@code from(model)}) fails a test that names the DTO.
 */
public class ExportParity {
    public static Set<String> properties(Class<?> type) {
        try {
            Set<String> names = new TreeSet<>();
            Arrays.stream(Introspector.getBeanInfo(type, Object.class).getPropertyDescriptors())
                    .filter(property -> property.getReadMethod() != null)
                    .map(PropertyDescriptor::getName)
                    .forEach(names::add);
            return names;
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param modelOnly properties of the model that are not exported under the same name (mapped or left out)
     * @param exportOnly properties of the DTO that are derived from other model properties
     * @return the properties shared by both, which {@code from(model)} must copy one to one
     */
    public static Set<String> assertParity(Class<?> model, Class<?> export, Set<String> modelOnly, Set<String> exportOnly) {
        Set<String> modelProperties = properties(model);
        Set<String> exportProperties = properties(export);

        assertTrue(modelProperties.containsAll(modelOnly), "stale model exclusions for " + export.getSimpleName() + ": " + modelOnly);
        assertTrue(exportProperties.containsAll(exportOnly), "stale export exclusions for " + export.getSimpleName() + ": " + exportOnly);

        Set<String> expected = new TreeSet<>(modelProperties);
        expected.removeAll(modelOnly);
        Set<String> actual = new TreeSet<>(exportProperties);
        actual.removeAll(exportOnly);

        assertEquals(expected, actual, "model " + model.getSimpleName() + " and export " + export.getSimpleName()
                + " differ; copy the new field in " + export.getSimpleName() + ".from(...) or list it as an exclusion");

        return expected;
    }

    /** Asserts that every shared property has the same value in the model and in the DTO made from it. */
    public static void assertValuesCopied(Object model, Object export, Set<String> properties) {
        properties.forEach(property -> assertEquals(read(model, property), read(export, property),
                export.getClass().getSimpleName() + "." + property));
    }

    private static Object read(Object bean, String property) {
        try {
            PropertyDescriptor descriptor = Arrays.stream(Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors())
                    .filter(candidate -> candidate.getName().equals(property))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(property));
            return descriptor.getReadMethod().invoke(bean);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
