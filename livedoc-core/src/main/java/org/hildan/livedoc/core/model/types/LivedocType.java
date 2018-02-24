package org.hildan.livedoc.core.model.types;

import java.util.List;
import java.util.stream.Collectors;

public interface LivedocType {

    /**
     * Returns a string representation of this type with all its components.
     *
     * @return a string representation of this type with all its components
     */
    default String getOneLineText() {
        return getTypeElements().stream().map(TypeElement::getText).collect(Collectors.joining());
    }

    /**
     * Breaks down this type's declaration into multiple display elements. Some of them are textual elements, other
     * are references to other types.
     *
     * @return a list of {@link TypeElement}s that compose this type's declaration
     */
    List<TypeElement> getTypeElements();

    /**
     * Returns all classes/interfaces appearing in this type's declaration. It recursively considers type parameters and
     * array components to find the involved types.
     * <p>
     * For instance, if this type is {@code Map<Custom, List<Integer>[]>}, this method returns {@code [Map, Custom,
     * List, Integer]}.
     *
     * @return a non-null list of all classes involved in this type's declaration
     */
    List<Class<?>> getComposingTypes();
}
