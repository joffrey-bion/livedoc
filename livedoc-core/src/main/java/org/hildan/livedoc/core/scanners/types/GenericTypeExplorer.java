package org.hildan.livedoc.core.scanners.types;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import org.hildan.livedoc.core.model.types.LivedocTypeBuilder;

/**
 * A utility class allowing to extract all classes involved in a generic type declaration.
 */
public class GenericTypeExplorer {

    /**
     * Returns all classes/interfaces appearing in the given type declaration. This method recursively explores type
     * parameters and array components to find the involved types.
     * <p>
     * For instance, for the type {@code Map<Custom, List<Integer>[]>}, we get [Map, Custom, List, Integer].
     *
     * @param type
     *         the type to explore
     *
     * @return a non-null set of all classes involved in the given type declaration
     */
    public static Set<Class<?>> getClassesInDeclaration(Type type) {
        return new HashSet<>(LivedocTypeBuilder.build(type).getComposingTypes());
    }
}
