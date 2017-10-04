package org.hildan.livedoc.core.builders.types;

import java.util.List;

public interface LivedocType {

    String getOneLineText();

    /**
     * Returns all classes/interfaces appearing in this type's declaration. It recursively considers type parameters and
     * array components to find the involved types. <p> For instance, if this type is {@code Map<Custom,
     * List<Integer>[]>}, this method returns {@code [Map, Custom, List, Integer]}.
     *
     * @return a non-null list of all classes involved in this type's declaration
     */
    List<Class<?>> getComposingTypes();
}
