package com.acme.backoffice;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Finds the one attribute a change added to a record, whatever it was named and wherever it was put, so that an
 * acceptance test can create and read records with it without guessing its name: the attributes the record had
 * before the change are listed, and exactly one other attribute must exist.
 */
final class AddedRecordComponent {
    private AddedRecordComponent() {
    }

    static RecordComponent find(Class<? extends Record> type, List<String> before, String what) {
        List<RecordComponent> added = Arrays.stream(type.getRecordComponents()).filter(c -> !before.contains(c.getName())).toList();
        if (added.size() != 1) {
            return fail(what + ": expected exactly one new attribute on " + type.getSimpleName() + ", found "
                    + added.stream().map(RecordComponent::getName).toList());
        }
        return added.get(0);
    }

    /** A record with the given values of its old attributes (by name) and the given value of the added one. */
    static <R extends Record> R create(Class<R> type, List<String> before, Map<String, Object> values, Object addedValue, String what) {
        RecordComponent added = find(type, before, what);
        RecordComponent[] components = type.getRecordComponents();
        Class<?>[] types = new Class<?>[components.length];
        Object[] args = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            types[i] = components[i].getType();
            args[i] = components[i].getName().equals(added.getName()) ? addedValue : values.get(components[i].getName());
        }
        try {
            Constructor<R> constructor = type.getDeclaredConstructor(types);
            return constructor.newInstance(args);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            return fail(what + ": cannot create " + type.getSimpleName() + " with the new attribute (" + e + ")");
        }
    }

    /** The value of the added attribute of a record. */
    static Object read(Record record, List<String> before, String what) {
        RecordComponent added = find(record.getClass(), before, what);
        try {
            return added.getAccessor().invoke(record);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
