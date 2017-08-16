package org.hildan.livedoc.core.scanner.types.filters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.hildan.livedoc.core.pojo.Livedoc;

public class DefaultTypeFilter implements TypeFilter {

    /**
     * Types that should not appear in the doc and that won't be explored to discover more types either.
     */
    private static final Class<?>[] IGNORED_CLASSES = {Livedoc.class};

    /**
     * Subtypes of these types will not appear in the doc, neither will they be explored to discover more types, even if
     * they fall under the white-listed packages. This applies in particular to container types like collections and
     * maps, which don't need to have a documented implementation. <p> Note: this is to preserve the original behaviour
     * of JsonDoc, but might very well be reconsidered later on.
     */
    private static final Class<?>[] IGNORED_HIERARCHIES = {Collection.class, Map.class};

    @Override
    public boolean test(Class<?> clazz) {
        return !isIgnored(clazz) && !isInIgnoredHierarchy(clazz);
    }

    private boolean isIgnored(Class<?> clazz) {
        return Arrays.stream(IGNORED_CLASSES).anyMatch(clazz::equals);
    }

    private boolean isInIgnoredHierarchy(Class<?> clazz) {
        return Arrays.stream(IGNORED_HIERARCHIES).anyMatch(c -> c.isAssignableFrom(clazz));
    }
}
