package org.hildan.livedoc.core.scanners.types.mappers;

import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

/**
 * A mapper that replaces interfaces and abstract classes by their implementations, and leave other types unchanged.
 */
public class ConcreteTypesMapper implements TypeMapper {

    private final Reflections reflections;

    public ConcreteTypesMapper(Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public Set<Class<?>> apply(Class<?> type) {
        if (isAbstract(type)) {
            return getImplementationsOf(type);
        }
        return Collections.singleton(type);
    }

    private static boolean isAbstract(Class<?> type) {
        return Modifier.isAbstract(type.getModifiers());
    }

    private Set<Class<?>> getImplementationsOf(Class<?> interfaceType) {
        Deque<Class<?>> toExplore = new ArrayDeque<>(1);
        Set<Class<?>> concreteSubtypes = new HashSet<>();
        toExplore.add(interfaceType);
        while (!toExplore.isEmpty()) {
            Class<?> t = toExplore.poll();
            if (isAbstract(t)) {
                toExplore.addAll(getSubtypesOf(t));
            } else {
                concreteSubtypes.add(t);
            }
        }
        return concreteSubtypes;
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getSubtypesOf(Class<?> type) {
        return reflections.getSubTypesOf((Class<Object>) type);
    }
}
