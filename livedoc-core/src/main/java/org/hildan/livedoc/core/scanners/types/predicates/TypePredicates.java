package org.hildan.livedoc.core.scanners.types.predicates;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TypePredicates {

    /**
     * A predicate that matches simple classes like primitive types, primitive wrappers, strings and enums. It is
     * intended to be used as a type inspection filter because we don't want to inspect the internal properties of
     * these types.
     */
    public static final Predicate<Type> IS_BASIC_TYPE = TypePredicates::isBasicType;

    /**
     * A predicate that matches arrays, collection classes and the likes, which we usually don't want to document as
     * independent types, because the exact implementation of a collection doesn't matter once serialized.
     */
    public static final Predicate<Type> IS_CONTAINER = TypePredicates::isContainer;

    private static final Class<?>[] PRIMITIVE_WRAPPERS = {
            Void.class,
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

    /**
     * Returns true on primitive types, primitive wrappers, strings, and enums.
     *
     * @param type
     *         the type to test
     *
     * @return true on primitive types, primitive wrappers, strings, and enums
     */
    public static boolean isBasicType(Type type) {
        if (!(type instanceof Class)) {
            return false;
        }
        Class<?> clazz = (Class<?>) type;
        return isPrimitiveOrWrapper(clazz) || isStringLike(clazz) || clazz.isEnum();
    }

    /**
     * Returns true on primitive types, primitive wrappers, and strings.
     *
     * @param type
     *         the type to test
     *
     * @return true on primitive types, primitive wrappers, and strings
     */
    public static boolean isPrimitiveLike(Type type) {
        return type instanceof Class && isPrimitiveLike((Class<?>) type);
    }

    private static boolean isPrimitiveLike(Class<?> clazz) {
        return isPrimitiveOrWrapper(clazz) || isStringLike(clazz);
    }

    public static boolean isPrimitiveOrWrapper(Type type) {
        return type instanceof Class && isPrimitiveOrWrapper((Class<?>) type);
    }

    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
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

    public static Predicate<Type> isInPackage(List<String> packages) {
        return type -> isInPackage(packages, type);
    }

    public static boolean isInPackage(List<String> packages, Type type) {
        if (!(type instanceof Class)) {
            return true;
        }
        if (TypePredicates.isPrimitiveLike(type)) {
            return false;
        }
        return isInPackage(packages, (Class<?>) type);
    }

    public static boolean isInPackage(List<String> packages, Class<?> clazz) {
        String packageName = clazz.getPackage().getName();
        return packages.stream().anyMatch(packageName::startsWith);
    }
}
