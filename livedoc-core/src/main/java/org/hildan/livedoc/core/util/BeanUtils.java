package org.hildan.livedoc.core.util;

import java.util.Arrays;

public class BeanUtils {

    public static String[] enumConstantsToStringArray(Object[] enumConstants) {
        String[] sarr = new String[enumConstants.length];
        for (int i = 0; i < enumConstants.length; i++) {
            sarr[i] = String.valueOf(enumConstants[i]);
        }
        return sarr;
    }

    public static String maybeOverridden(String overridingValue, String initialValue) {
        return maybeOverridden(overridingValue, initialValue, null);
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
}
