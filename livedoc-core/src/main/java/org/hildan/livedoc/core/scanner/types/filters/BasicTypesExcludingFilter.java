package org.hildan.livedoc.core.scanner.types.filters;

import java.util.Arrays;

/**
 * A filter that excludes primitive types, primitive wrappers, strings and enums. It is intended to be used as a type
 * exploration filter because we don't want to explore the internal properties of these types.
 */
public class BasicTypesExcludingFilter implements TypeFilter {

    private static final Class<?>[] PRIMITIVE_WRAPPERS = {Boolean.class, Byte.class, Short.class, Character.class,
            Integer.class, Long.class, Float.class, Double.class};

    @Override
    public boolean test(Class<?> clazz) {
        return !isPrimitiveOrWrapper(clazz) && !isStringLike(clazz) && !clazz.isEnum();
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() || isPrimitiveWrapper(clazz);
    }

    private boolean isPrimitiveWrapper(Class<?> clazz) {
        return Arrays.stream(PRIMITIVE_WRAPPERS).anyMatch(clazz::equals);
    }

    private boolean isStringLike(Class<?> clazz) {
        return CharSequence.class.isAssignableFrom(clazz);
    }
}
