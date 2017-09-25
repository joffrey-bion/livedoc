package org.hildan.livedoc.core.builders.merger;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

import org.hildan.livedoc.core.pojo.ApiStage;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.pojo.ApiVisibility;
import org.hildan.livedoc.core.pojo.SpecialDefaultIntValue;
import org.hildan.livedoc.core.pojo.SpecialDefaultStringValue;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.types.predicates.TypePredicates;

public class DocMerger {

    private final PropertyScanner propScanner;

    public DocMerger(PropertyScanner propScanner) {
        this.propScanner = propScanner;
    }

    /**
     * Overrides each property of the given target if the given source has a meaningful (non default) value for the
     * property.
     *
     * @param overridingSource
     *         the object to read the property values from
     * @param baseTarget
     *         the object to set properties on (if there is something meaningful to put in place of the previous value)
     * @param <T>
     *         the type of objects to manipulate
     */
    public <T> void merge(T overridingSource, T baseTarget) {
        assert overridingSource != null;
        assert baseTarget != null;
        List<Property> properties = propScanner.getProperties(overridingSource.getClass());
        for (Property prop : properties) {
            Field field = (Field) prop.getAccessibleObject();
            mergeField(field, overridingSource, baseTarget);
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

    private static boolean shouldReplaceWithoutMerge(Field field, Object sourceValue, Object targetValue) {
        return targetValue == null || (isOfBasicType(sourceValue) && !isDefaultValue(field, sourceValue));
    }

    private static boolean isOfBasicType(Object obj) {
        Class<?> clazz = obj.getClass();
        return TypePredicates.isBasicType(clazz) || clazz.isArray();
    }

    private static boolean isDefaultValue(Field field, Object value) {
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

    private static boolean isDefaultValueOfItsType(Object value) {
        if (value instanceof CharSequence) {
            return ((CharSequence) value).length() == 0;
        }
        if (value instanceof Boolean) {
            return Boolean.FALSE.equals(value);
        }
        if (value instanceof ApiVerb) {
            return value == ApiVerb.UNDEFINED;
        }
        if (value instanceof ApiStage) {
            return value == ApiStage.UNDEFINED;
        }
        if (value instanceof ApiVisibility) {
            return value == ApiVisibility.UNDEFINED;
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
