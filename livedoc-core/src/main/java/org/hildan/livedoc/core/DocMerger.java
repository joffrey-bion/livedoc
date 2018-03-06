package org.hildan.livedoc.core;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hildan.livedoc.core.model.doc.SpecialDefaultIntValue;
import org.hildan.livedoc.core.model.doc.SpecialDefaultStringValue;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.types.predicates.TypePredicates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DocMerger {

    private final PropertyScanner propScanner;

    public DocMerger(PropertyScanner propScanner) {
        this.propScanner = propScanner;
    }

    /**
     * Overrides each property of the given target if the given source has a meaningful (non default) value for the
     * property.
     *
     * @param source
     *         the object to read the property values from
     * @param target
     *         the object to set properties on (if there is something meaningful to put in place of the previous value)
     * @param <T>
     *         the type of objects to manipulate
     */
    public <T> void merge(T source, T target) {
        assert source != null;
        assert target != null;
        List<Property> properties = propScanner.getProperties(source.getClass());
        for (Property prop : properties) {
            Field field = (Field) prop.getAccessibleObject();
            mergeField(field, source, target);
        }
    }

    private <T> void mergeField(Field field, T source, T target) {
        try {
            field.setAccessible(true);
            Object sourceValue = field.get(source);
            Object targetValue = field.get(target);
            if (sourceValue != null) {
                if (shouldReplaceWithoutMerge(field, sourceValue, targetValue)) {
                    field.set(target, sourceValue);
                } else if (!isDefaultValue(field, sourceValue)) {
                    merge(sourceValue, targetValue);
                }
            }
        } catch (IllegalAccessException e) {
            String msg = String.format("Could not copy field '%s' of the given object of type %s", field.getName(),
                    source.getClass().getSimpleName());
            throw new FieldCopyException(msg, e);
        }
    }

    private static boolean shouldReplaceWithoutMerge(Field field, @Nullable Object sourceValue,
            @Nullable Object targetValue) {
        if (targetValue == null) {
            return true;
        }
        if (sourceValue == null) {
            return false;
        }
        if (TypePredicates.isBasicType(sourceValue.getClass())) {
            return !isDefaultValue(field, sourceValue);
        }
        if (TypePredicates.isContainer(sourceValue.getClass())) {
            return !isEmptyContainer(sourceValue);
        }
        return !sameClass(sourceValue, targetValue);
    }

    private static boolean isEmptyContainer(@NotNull Object container) {
        if (container instanceof Collection) {
            return ((Collection) container).isEmpty();
        }
        if (container instanceof Map) {
            return ((Map) container).isEmpty();
        }
        if (container.getClass().isArray()) {
            return ((Object[]) container).length == 0;
        }
        return false;
    }

    private static boolean sameClass(@NotNull Object sourceValue, @NotNull Object targetValue) {
        return sourceValue.getClass().equals(targetValue.getClass());
    }

    private static boolean isDefaultValue(Field field, @NotNull Object value) {
        SpecialDefaultStringValue specialDefaultStr = field.getAnnotation(SpecialDefaultStringValue.class);
        if (specialDefaultStr != null) {
            return specialDefaultStr.value().equals(value);
        }
        SpecialDefaultIntValue specialDefaultInt = field.getAnnotation(SpecialDefaultIntValue.class);
        if (specialDefaultInt != null) {
            return Integer.valueOf(specialDefaultInt.value()).equals(value);
        }
        return isDefaultValueOfItsType(value);
    }

    private static boolean isDefaultValueOfItsType(@NotNull Object value) {
        if (value instanceof CharSequence) {
            return ((CharSequence) value).length() == 0;
        }
        if (value instanceof Boolean) {
            return Boolean.FALSE.equals(value);
        }
        if (value.getClass().isArray()) {
            return Array.getLength(value) == 0;
        }
        return false;
    }

    public static class FieldCopyException extends RuntimeException {
        FieldCopyException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
