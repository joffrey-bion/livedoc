package org.hildan.livedoc.core.scanners.types.generics;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A utility class to recursively explore a generic type declaration using the given {@link GenericTypeHandler} on each
 * type encountered.
 */
public class GenericDeclarationExplorer<T> {

    private final GenericTypeHandler<T> handler;

    private final Set<TypeVariable> resolvedTypeVariables;

    private GenericDeclarationExplorer(GenericTypeHandler<T> handler) {
        this.handler = handler;
        this.resolvedTypeVariables = new HashSet<>();
    }

    public static <T> T explore(Type type, GenericTypeHandler<T> handler) {
        return new GenericDeclarationExplorer<>(handler).explore(type);
    }

    private T explore(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type declaration may not be null");
        }
        if (type instanceof Class) {
            return exploreClassOrArray((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            return exploreParameterizedType(((ParameterizedType) type));
        }
        if (type instanceof GenericArrayType) {
            return exploreGenericArray((GenericArrayType) type);
        }
        if (type instanceof TypeVariable) {
            return exploreTypeVariable((TypeVariable) type);
        }
        if (type instanceof WildcardType) {
            return exploreWildcardType((WildcardType) type);
        }
        throw new IllegalArgumentException("Unknown type category " + type.getClass());
    }

    private T exploreClassOrArray(Class<?> clazz) {
        if (void.class.equals(clazz) || Void.class.equals(clazz)) {
            return handler.handleVoid();
        }
        if (clazz.isArray()) {
            T exploredComponentType = explore(clazz.getComponentType());
            return handler.handleArrayClass(clazz, exploredComponentType);
        }
        if (clazz.isEnum()) {
            return handler.handleEnumClass(clazz);
        }
        return handler.handleSimpleClass(clazz);
    }

    private T exploreGenericArray(GenericArrayType type) {
        Type componentType = type.getGenericComponentType();
        T exploredComponentType = explore(componentType);
        return handler.handleGenericArray(type, exploredComponentType);
    }

    private T exploreParameterizedType(ParameterizedType type) {
        T exploredRawType = explore(type.getRawType());
        List<T> exploredTypeParams = exploreAll(type.getActualTypeArguments());
        return handler.handleParameterizedType(type, exploredRawType, exploredTypeParams);
    }

    private T exploreTypeVariable(TypeVariable type) {
        if (resolvedTypeVariables.contains(type)) {
            // we ignore the bounds when already resolved to avoid infinite recursions
            return handler.handleTypeVariable(type, Collections.emptyList());
        }
        resolvedTypeVariables.add(type);

        Type[] bounds = toSignificantBounds(type.getBounds());
        List<T> exploredBounds = exploreAll(bounds);
        return handler.handleTypeVariable(type, exploredBounds);
    }

    private T exploreWildcardType(WildcardType type) {
        Type[] upperBounds = toSignificantBounds(type.getUpperBounds());
        Type[] lowerBounds = toSignificantBounds(type.getLowerBounds());
        List<T> exploredUpperBounds = exploreAll(upperBounds);
        List<T> exploredLowerBounds = exploreAll(lowerBounds);
        return handler.handleWildcardType(type, exploredUpperBounds, exploredLowerBounds);
    }

    private static Type[] toSignificantBounds(Type[] bounds) {
        // unbounded variables have one bound of type Object
        if (bounds.length == 1 && bounds[0].equals(Object.class)) {
            return new Type[0];
        }
        return bounds;
    }

    private List<T> exploreAll(Type[] types) {
        return Arrays.stream(types).map(this::explore).collect(Collectors.toList());
    }
}
