package org.hildan.livedoc.core.scanners.types;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A utility class allowing to extract all classes involved in a generic type declaration.
 */
public class GenericTypeExplorer {

    private final Set<TypeVariable> resolvedTypeVariables;

    private GenericTypeExplorer() {
        resolvedTypeVariables = new HashSet<>();
    }

    /**
     * Returns all classes/interfaces appearing in the given type declaration. This method recursively explores type
     * parameters and array components to find the involved types. <p> For instance, for the type {@code Map<Custom,
     * List<Integer>[]>}, we get [Map, Custom, List, Integer].
     *
     * @param type
     *         the type to explore
     *
     * @return a non-null set of all classes involved in the given type declaration
     */
    public static Set<Class<?>> getClassesInDeclaration(Type type) {
        return new GenericTypeExplorer().getClasses(type);
    }

    private Set<Class<?>> getClasses(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type declaration may not be null");
        }
        if (type instanceof WildcardType) {
            return Collections.emptySet();
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return getClasses(componentType);
        }
        if (type instanceof TypeVariable) {
            return getClassesFromTypeVariable((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return getClassesFromParameterizedType(((ParameterizedType) type));
        }
        assert type instanceof Class : "Unknown type category " + type.getClass();
        return getClassesFromClassOrArray((Class<?>) type);
    }

    private static Set<Class<?>> getClassesFromClassOrArray(Class<?> clazz) {
        if (clazz.isArray()) {
            return getClassesInDeclaration(clazz.getComponentType());
        }
        return Collections.singleton(clazz);
    }

    private Set<Class<?>> getClassesFromParameterizedType(ParameterizedType type) {
        Class<?> rawType = (Class<?>) type.getRawType();
        Type[] typeParams = type.getActualTypeArguments();

        Set<Class<?>> candidates = new HashSet<>();
        candidates.add(rawType);
        candidates.addAll(getClasses(typeParams));
        return candidates;
    }

    private Set<Class<?>> getClassesFromTypeVariable(TypeVariable type) {
        if (isAlreadyResolved(type) || !hasBounds(type)) {
            return Collections.emptySet();
        }
        resolvedTypeVariables.add(type);
        return getClasses(type.getBounds());
    }

    private boolean isAlreadyResolved(TypeVariable type) {
        return resolvedTypeVariables.contains(type);
    }

    private static boolean hasBounds(TypeVariable type) {
        Type[] bounds = type.getBounds();
        // unbounded variables have one bound of type Object
        return bounds.length != 1 || !bounds[0].equals(Object.class);
    }

    private Set<Class<?>> getClasses(Type... types) {
        return Arrays.stream(types)
                     .flatMap(t -> getClasses(t).stream())
                     .collect(Collectors.toSet());
    }
}
