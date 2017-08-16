package org.hildan.livedoc.springmvc.scanner.utils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GenericTypeExplorer {

    public static Set<Class<?>> getTypesInDeclaration(Type type) {
        if (type instanceof Class) {
            return getTypesFromClass((Class<?>) type);
        }
        if (type instanceof WildcardType) {
            return Collections.singleton(Void.class);
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return getTypesInDeclaration(componentType);
        }
        if (type instanceof TypeVariable) {
            return getTypesFromTypeVariable((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return getTypesFromParameterizedType(((ParameterizedType) type));
        }
        throw new IllegalArgumentException("Unknown type category " + type.getClass());
    }

    private static Set<Class<?>> getTypesFromClass(Class<?> clazz) {
        if (clazz.isArray()) {
            return getTypesInDeclaration(clazz.getComponentType());
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
            candidates.addAll(getTypesInDeclaration(typeParam));
        }
        return candidates;
    }
}
