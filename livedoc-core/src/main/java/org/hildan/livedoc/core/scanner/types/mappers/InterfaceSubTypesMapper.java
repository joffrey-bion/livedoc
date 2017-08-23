package org.hildan.livedoc.core.scanner.types.mappers;

import java.util.Collection;
import java.util.Collections;

import org.reflections.Reflections;

/**
 * A mapper that yields all subtypes  interface type
 */
public class InterfaceSubTypesMapper implements TypeMapper {

    private final Reflections reflections;

    public InterfaceSubTypesMapper(Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public Collection<Class<?>> apply(Class<?> type) {
        if (type.isInterface()) {
            //noinspection unchecked
            return reflections.getSubTypesOf((Class<Object>) type);
        }
        return Collections.singleton(type);
    }
}
