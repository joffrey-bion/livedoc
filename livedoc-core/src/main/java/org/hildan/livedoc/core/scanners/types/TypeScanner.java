package org.hildan.livedoc.core.scanners.types;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;

/**
 * A component that inspects the types used in the definition of a given type. This is used to know which types
 * should be part of the documentation, given some root types that we know should be documented.
 *
 * @see RecursivePropertyTypeScanner
 */
public interface TypeScanner {

    /**
     * Finds all the types mentioned by the given root types.
     *
     * @param rootTypes
     *         the starting points for the search
     *
     * @return the set of all classes mentioned by (and including) the root types
     */
    Set<Class<?>> findTypeToDocument(Collection<? extends Type> rootTypes);
}
