package org.hildan.livedoc.core.model.types;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the declaration of a type. It provides the list of {@link TypeReferenceElement}s composing this type's
 * declaration. Each of these elements may in turn contain a reference to the documentation of the corresponding class.
 *
 * @see TypeReferenceElement
 */
public interface LivedocType {

    /**
     * Returns a string representation of this type with all its components.
     *
     * @return a string representation of this type with all its components
     */
    default String getOneLineText() {
        return getTypeElements().stream().map(TypeReferenceElement::getText).collect(Collectors.joining());
    }

    /**
     * Breaks down this type's declaration into multiple display elements. Some of them are textual elements, other are
     * references to other types.
     *
     * @return a list of {@link TypeReferenceElement}s that compose this type's declaration
     */
    List<TypeReferenceElement> getTypeElements();

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
