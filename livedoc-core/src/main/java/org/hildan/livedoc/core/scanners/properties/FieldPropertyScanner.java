package org.hildan.livedoc.core.scanners.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * A {@link PropertyScanner} reading properties as the fields of a class. Only the relevant fields are considered
 * properties (static, transient and synthetic fields are ignored, for instance).
 */
public class FieldPropertyScanner implements PropertyScanner {

    @Override
    public List<Property> getProperties(Type type) {
        Class<?> clazz = extractClass(type);
        return getAllFields(clazz).stream()
                                  .filter(FieldPropertyScanner::isRelevantWhenSerialized)
                                  .map(FieldPropertyScanner::toProperty)
                                  .collect(toList());
    }

    private static Class<?> extractClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        throw new IllegalArgumentException(
                "Unsupported kind of type " + type.getClass() + " for '" + type.getTypeName() + "'");
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
        Property property = new Property(field.getName(), field.getType(), field.getGenericType(), field);
        property.setField(field);
        return property;
    }
}
