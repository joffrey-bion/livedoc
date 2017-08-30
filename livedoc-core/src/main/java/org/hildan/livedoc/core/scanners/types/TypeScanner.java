package org.hildan.livedoc.core.scanners.types;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;

public interface TypeScanner {

    /**
     * Finds all the types mentioned by the given root types.
     *
     * @param rootTypes
     *         the starting point for the search
     *
     * @return the set of all classes mentioned by (and possibly including) the root types
     */
    Set<Class<?>> findTypes(Collection<? extends Type> rootTypes);
}
