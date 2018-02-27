package org.hildan.livedoc.core.scanners.types.generics;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

public class MentionedClassesExplorer implements GenericTypeHandler<Set<Class<?>>> {

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
        return GenericDeclarationExplorer.explore(type, new MentionedClassesExplorer());
    }

    @Override
    public Set<Class<?>> handleVoid() {
        return Collections.emptySet();
    }

    @Override
    public Set<Class<?>> handleSimpleClass(@NotNull Class<?> clazz) {
        return Collections.singleton(clazz);
    }

    @Override
    public Set<Class<?>> handleEnumClass(@NotNull Class<?> clazz) {
        return handleSimpleClass(clazz);
    }

    @Override
    public Set<Class<?>> handleArrayClass(@NotNull Class<?> arrayClass, Set<Class<?>> handledComponentClass) {
        return handledComponentClass;
    }

    @Override
    public Set<Class<?>> handleGenericArray(@NotNull GenericArrayType type, Set<Class<?>> handledComponentClass) {
        return handledComponentClass;
    }

    @Override
    public Set<Class<?>> handleParameterizedType(@NotNull ParameterizedType type, Set<Class<?>> handledRawType,
            @NotNull List<Set<Class<?>>> handledTypeParameters) {
        Set<Class<?>> result = new HashSet<>(handledRawType);
        handledTypeParameters.forEach(result::addAll);
        return result;
    }

    @Override
    public Set<Class<?>> handleTypeVariable(@NotNull TypeVariable type, @NotNull List<Set<Class<?>>> handledBounds) {
        return handledBounds.stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @Override
    public Set<Class<?>> handleWildcardType(@NotNull WildcardType type, @NotNull List<Set<Class<?>>> handledUpperBounds,
            @NotNull List<Set<Class<?>>> handledLowerBounds) {
        Stream<Set<Class<?>>> upperBoundsStream = handledUpperBounds.stream();
        Stream<Set<Class<?>>> lowerBoundsStream = handledLowerBounds.stream();
        return Stream.concat(upperBoundsStream, lowerBoundsStream)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toSet());
    }
}
