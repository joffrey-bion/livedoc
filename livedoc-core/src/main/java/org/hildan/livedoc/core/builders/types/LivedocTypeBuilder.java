package org.hildan.livedoc.core.builders.types;

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

import org.hildan.livedoc.core.annotations.ApiObject;

public class LivedocTypeBuilder {

    private final Set<TypeVariable> resolvedTypeVariables;

    private LivedocTypeBuilder() {
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
    public static LivedocType build(Type type) {
        return new LivedocTypeBuilder().buildType(type);
    }

    private LivedocType buildType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type declaration may not be null");
        }
        if (type instanceof WildcardType) {
            return WildcardLivedocType.INSTANCE;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return new ArrayLivedocType(buildType(componentType));
        }
        if (type instanceof TypeVariable) {
            return buildTypeVariable((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return buildParameterizedType(((ParameterizedType) type));
        }
        assert type instanceof Class : "Unknown type category " + type.getClass();
        return buildTypeForClassOrArray((Class<?>) type);
    }

    private static LivedocType buildTypeForClassOrArray(Class<?> clazz) {
        if (clazz.isArray()) {
            return new ArrayLivedocType(build(clazz.getComponentType()));
        }
        return buildTypeForClass(clazz);
    }

    private static SimpleLivedocType buildTypeForClass(Class<?> clazz) {
        return new SimpleLivedocType(clazz.getName(), getCustomClassName(clazz), clazz);
    }

    private static String getCustomClassName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ApiObject.class)) {
            ApiObject annotation = clazz.getAnnotation(ApiObject.class);
            if (annotation.name().isEmpty()) {
                return clazz.getSimpleName();
            } else {
                return annotation.name();
            }
        } else {
            return clazz.getSimpleName();
        }
    }

    private LivedocType buildParameterizedType(ParameterizedType type) {
        Class<?> rawType = (Class<?>) type.getRawType();
        Type[] typeParams = type.getActualTypeArguments();

        SimpleLivedocType baseType = buildTypeForClass(rawType);
        List<LivedocType> typeParameters = buildTypes(typeParams);
        return new ParameterizedLivedocType(baseType, typeParameters);
    }

    private LivedocType buildTypeVariable(TypeVariable type) {
        if (resolvedTypeVariables.contains(type)) {
            // we ignore the bounds when already resolved to avoid infinite recursions
            return new LivedocTypeVariable(type.getName(), Collections.emptyList());
        }
        resolvedTypeVariables.add(type);
        return new LivedocTypeVariable(type.getName(), buildTypes(getBounds(type)));
    }

    private static Type[] getBounds(TypeVariable type) {
        Type[] bounds = type.getBounds();
        // unbounded variables have one bound of type Object
        if (bounds.length == 1 && bounds[0].equals(Object.class)) {
            return new Type[0];
        }
        return bounds;
    }

    private List<LivedocType> buildTypes(Type... types) {
        return Arrays.stream(types).map(this::buildType).collect(Collectors.toList());
    }
}
