package org.hildan.livedoc.core.util;

import java.lang.reflect.Field;
import java.util.Arrays;

public class BeanUtils {

    public static String maybeOverridden(String overridingValue, String initialValue) {
        return maybeOverridden(overridingValue, initialValue, "");
    }

    public static String[] maybeOverridden(String[] overridingValue, String[] initialValue) {
        return maybeOverridden(overridingValue, initialValue, new String[0]);
    }

    public static <T> T maybeOverridden(T overridingValue, T initialValue, T defaultValue) {
        if (isSetToSomething(overridingValue, defaultValue)) {
            return overridingValue;
        }
        if (isSetToSomething(initialValue, defaultValue)) {
            return initialValue;
        }
        return defaultValue;
    }

    public static <T> T[] maybeOverridden(T[] overridingValue, T[] initialValue, T[] defaultValue) {
        if (isSetToSomething(overridingValue, defaultValue)) {
            return overridingValue;
        }
        if (isSetToSomething(initialValue, defaultValue)) {
            return initialValue;
        }
        return defaultValue;
    }

    private static <T> boolean isSetToSomething(T value, T defaultValue) {
        return value != null && !value.equals(defaultValue);
    }

    private static <T> boolean isSetToSomething(T[] value, T[] defaultValue) {
        return value != null && !Arrays.equals(value, defaultValue);
    }

    public static <T> void copyNonNullFields(T source, T target) {
        assert source != null;
        assert target != null;
        try {
            copyNonNullFieldsUnsafe(source, target);
        } catch (IllegalAccessException e) {
            throw new FieldCopyException("Could not copy the fields of the given object of type " + source.getClass(),
                    e);
        }
    }

    private static <T> void copyNonNullFieldsUnsafe(T source, T target) throws IllegalAccessException {
        Field[] fields = source.getClass().getFields();
        for (Field field : fields) {
            copyNonNullField(field, source, target);
        }
    }

    private static <T> void copyNonNullField(Field field, T source, T target) throws IllegalAccessException {
        Object value = field.get(source);
        if (value != null) {
            setField(field, target, value);
        }
    }

    private static <T> void setField(Field field, T target, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(target, value);
    }

    public static String[] enumConstantsToStringArray(Object[] enumConstants) {
        String[] sarr = new String[enumConstants.length];
        for (int i = 0; i < enumConstants.length; i++) {
            sarr[i] = String.valueOf(enumConstants[i]);
        }
        return sarr;
    }

    public static class FieldCopyException extends RuntimeException {
        FieldCopyException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
