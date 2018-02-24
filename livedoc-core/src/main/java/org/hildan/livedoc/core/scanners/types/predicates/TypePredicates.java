package org.hildan.livedoc.core.scanners.types.predicates;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class TypePredicates {

    /**
     * A predicate that matches simple classes like primitive types, primitive wrappers, strings and enums. It is
     * intended to be used as a type exploration filter because we don't want to explore the internal properties of
     * these types.
     */
    public static final Predicate<Type> IS_BASIC_TYPE = TypePredicates::isBasicType;

    /**
     * A predicate that matches arrays, collection classes and the likes, which we usually don't want to document as
     * independent types, because the exact implementation of a collection doesn't matter once serialized.
     */
    public static final Predicate<Type> IS_CONTAINER = TypePredicates::isContainer;

    private static final Class<?>[] PRIMITIVE_WRAPPERS = {
            Boolean.class,
            Byte.class,
            Short.class,
            Character.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
    };

    private static final Class<?>[] CONTAINERS = {Collection.class, Map.class};

    public static boolean isBasicType(Type type) {
        if (!(type instanceof Class)) {
            return false;
        }
        Class<?> clazz = (Class<?>) type;
        return isPrimitiveOrWrapper(clazz) || isStringLike(clazz) || clazz.isEnum();
    }

    private static boolean isPrimitiveOrWrapper(Type type) {
        if (!(type instanceof Class)) {
            return false;
        }
        Class<?> clazz = (Class<?>) type;
        return clazz.isPrimitive() || isPrimitiveWrapper(clazz);
    }

    private static boolean isPrimitiveWrapper(Type type) {
        return Arrays.stream(PRIMITIVE_WRAPPERS).anyMatch(type::equals);
    }

    private static boolean isStringLike(Class<?> clazz) {
        return CharSequence.class.isAssignableFrom(clazz);
    }

    public static boolean isContainer(Type type) {
        if (type instanceof Class) {
            return isContainer((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return isContainer((Class<?>) parameterizedType.getRawType());
        }
        return false;
    }

    public static boolean isContainer(Class<?> clazz) {
        return clazz.isArray() || Arrays.stream(CONTAINERS).anyMatch(c -> c.isAssignableFrom(clazz));
    }
}
