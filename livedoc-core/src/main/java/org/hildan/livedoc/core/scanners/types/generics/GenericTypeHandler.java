package org.hildan.livedoc.core.scanners.types.generics;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public interface GenericTypeHandler<T> {

    T handleVoid();

    T handleSimpleClass(@NotNull Class<?> clazz);

    T handleEnumClass(@NotNull Class<?> clazz);

    T handleArrayClass(@NotNull Class<?> arrayClass, T handledComponentClass);

    T handleGenericArray(@NotNull GenericArrayType type, T handledComponentClass);

    T handleParameterizedType(@NotNull ParameterizedType type, T handledRawType,
            @NotNull List<T> handledTypeParameters);

    T handleTypeVariable(@NotNull TypeVariable type, @NotNull List<T> handledBounds);

    T handleWildcardType(@NotNull WildcardType type, @NotNull List<T> handledUpperBounds,
            @NotNull List<T> handledLowerBounds);
}
