package org.hildan.livedoc.springmvc.scanner.utils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

public class GenericTypeExplorer {

    public static Set<Class<?>> getTypesInDeclaration(Type type, Reflections reflections) {
        if (type instanceof Class) {
            return getConcreteTypes((Class<?>) type, reflections);
        }
        if (type instanceof WildcardType) {
            return Collections.singleton(Void.class);
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return getTypesInDeclaration(componentType, reflections);
        }
        if (type instanceof TypeVariable) {
            return getTypesOfTypeVariable((TypeVariable) type, reflections);
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ((ParameterizedType) type);
            return getTypesOfParameterizedType(parameterizedType, reflections);
        }
        throw new IllegalArgumentException("Unknown type category " + type.getClass());
    }

    private static Set<Class<?>> getConcreteTypes(Class<?> clazz, Reflections reflections) {
        if (clazz.isArray()) {
            return getTypesInDeclaration(clazz.getComponentType(), reflections);
        }
        if (shouldGetOnlyConcreteSubTypes(clazz)) {
            return getSubTypes(reflections, clazz);
        }
        return Collections.singleton(clazz);
    }

    private static boolean shouldGetOnlyConcreteSubTypes(Class<?> clazz) {
        boolean isAbstract = clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers());
        boolean isContainer = Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
        return isAbstract && !isContainer;
    }

    private static Set<Class<?>> getSubTypes(Reflections reflections, Class<?> clazz) {
        Set<Class<?>> candidates = new HashSet<>();
        for (Class<?> impl : reflections.getSubTypesOf(clazz)) {
            candidates.addAll(getTypesInDeclaration(impl, reflections));
        }
        return candidates;
    }

    private static Set<Class<?>> getTypesOfParameterizedType(ParameterizedType type, Reflections reflections) {
        Class<?> rawType = (Class<?>) type.getRawType();
        Type[] typeParams = type.getActualTypeArguments();

        Set<Class<?>> candidates = new HashSet<>();
        candidates.add(rawType);
        candidates.addAll(getTypesInDeclarations(reflections, typeParams));
        return candidates;
    }

    private static Set<Class<?>> getTypesOfTypeVariable(TypeVariable type, Reflections reflections) {
        return getTypesInDeclarations(reflections, type.getBounds());
    }

    private static Set<Class<?>> getTypesInDeclarations(Reflections reflections, Type... types) {
        Set<Class<?>> candidates = new HashSet<>();
        for (Type typeParam : types) {
            candidates.addAll(getTypesInDeclaration(typeParam, reflections));
        }
        return candidates;
    }
}
