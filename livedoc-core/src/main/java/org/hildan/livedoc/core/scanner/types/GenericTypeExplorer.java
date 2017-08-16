package org.hildan.livedoc.core.scanner.types;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A utility class allowing to extract all classes involved in a generic type declaration.
 */
class GenericTypeExplorer {

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
    static Set<Class<?>> getClassesInDeclaration(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type declaration may not be null");
        }
        if (type instanceof WildcardType) {
            return Collections.emptySet();
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return getClassesInDeclaration(componentType);
        }
        if (type instanceof TypeVariable) {
            return getTypesFromTypeVariable((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return getTypesFromParameterizedType(((ParameterizedType) type));
        }
        assert type instanceof Class : "Unknown type category " + type.getClass();
        return getTypesFromClassOrArray((Class<?>) type);
    }

    private static Set<Class<?>> getTypesFromClassOrArray(Class<?> clazz) {
        if (clazz.isArray()) {
            return getClassesInDeclaration(clazz.getComponentType());
        }
        return Collections.singleton(clazz);
    }

    private static Set<Class<?>> getTypesFromParameterizedType(ParameterizedType type) {
        Class<?> rawType = (Class<?>) type.getRawType();
        Type[] typeParams = type.getActualTypeArguments();

        Set<Class<?>> candidates = new HashSet<>();
        candidates.add(rawType);
        candidates.addAll(getTypesInDeclarations(typeParams));
        return candidates;
    }

    private static Set<Class<?>> getTypesFromTypeVariable(TypeVariable type) {
        return getTypesInDeclarations(type.getBounds());
    }

    private static Set<Class<?>> getTypesInDeclarations(Type... types) {
        Set<Class<?>> candidates = new HashSet<>();
        for (Type typeParam : types) {
            candidates.addAll(getClassesInDeclaration(typeParam));
        }
        return candidates;
    }
}
