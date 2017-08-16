package org.hildan.livedoc.core.scanner.types.records;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class FieldTypesScanner implements RecordTypesScanner {

    @Override
    public Set<Type> getFieldTypes(Class<?> clazz) {
        return getAllFields(clazz).stream()
                                  .filter(FieldTypesScanner::isSerializable)
                                  .map(Field::getGenericType)
                                  .collect(toSet());
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

    private static boolean isSerializable(Field field) {
        return !field.isSynthetic() && !Modifier.isTransient(field.getModifiers());
    }
}
