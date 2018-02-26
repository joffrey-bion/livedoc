package org.hildan.livedoc.core.scanners.types.generics;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

public interface GenericTypeHandler<T> {

    T handleSimpleClass(Class<?> clazz);

    T handleArrayClass(Class<?> arrayClass, T handledComponentClass);

    T handleGenericArray(GenericArrayType type, T handledComponentClass);

    T handleParameterizedType(ParameterizedType type, T handledRawType, List<T> handledTypeParameters);

    T handleTypeVariable(TypeVariable type, List<T> handledBounds);

    T handleWildcardType(WildcardType type, List<T> handledUpperBounds, List<T> handledLowerBounds);
}
