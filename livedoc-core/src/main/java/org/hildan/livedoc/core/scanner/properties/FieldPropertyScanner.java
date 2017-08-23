package org.hildan.livedoc.core.scanner.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FieldPropertyScanner implements PropertyScanner {

    @Override
    public List<Property> getProperties(Class<?> clazz) {
        return getAllFields(clazz).stream()
                                  .filter(FieldPropertyScanner::isRelevantWhenSerialized)
                                  .map(FieldPropertyScanner::toProperty)
                                  .collect(toList());
    }

    private static List<Field> getAllFields(final Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(fields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return fields;
    }

    private static boolean isRelevantWhenSerialized(Field field) {
        int modifiers = field.getModifiers();
        return !field.isSynthetic() && !Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers);
    }

    private static Property toProperty(Field field) {
        return new Property(field.getName(), field.getGenericType());
    }
}
