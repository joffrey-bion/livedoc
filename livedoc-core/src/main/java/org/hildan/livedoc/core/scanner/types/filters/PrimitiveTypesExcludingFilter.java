package org.hildan.livedoc.core.scanner.types.filters;

import java.util.Arrays;

/**
 * A filter that excludes primitive types, primitive wrappers and strings. This is because we don't want to explore the
 * internal properties of these types.
 */
public class PrimitiveTypesExcludingFilter implements TypeFilter {

    private static final Class<?>[] PRIMITIVE_WRAPPERS = {Boolean.class, Byte.class, Short.class, Character.class,
            Integer.class, Long.class, Float.class, Double.class};

    @Override
    public boolean test(Class<?> clazz) {
        return !clazz.isPrimitive() && !isPrimitiveWrapper(clazz) && !String.class.equals(clazz);
    }

    private boolean isPrimitiveWrapper(Class<?> clazz) {
        return Arrays.stream(PRIMITIVE_WRAPPERS).anyMatch(clazz::equals);
    }
}
