package org.hildan.livedoc.core.model.types;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.scanners.types.generics.GenericDeclarationExplorer;
import org.hildan.livedoc.core.scanners.types.generics.GenericTypeHandler;

public class LivedocTypeBuilder implements GenericTypeHandler<LivedocType> {

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
        return GenericDeclarationExplorer.explore(type, new LivedocTypeBuilder());
    }

    @Override
    public LivedocType handleSimpleClass(Class<?> clazz) {
        return new SimpleLivedocType(getCustomClassName(clazz), clazz);
    }

    private static String getCustomClassName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ApiType.class)) {
            ApiType annotation = clazz.getAnnotation(ApiType.class);
            if (annotation.name().isEmpty()) {
                return clazz.getSimpleName();
            } else {
                return annotation.name();
            }
        } else {
            return clazz.getSimpleName();
        }
    }

    @Override
    public LivedocType handleArrayClass(Class<?> arrayClass, LivedocType handledComponentClass) {
        return handleArray(handledComponentClass);
    }

    @Override
    public LivedocType handleGenericArray(GenericArrayType type, LivedocType handledComponentClass) {
        return handleArray(handledComponentClass);
    }

    private static LivedocType handleArray(LivedocType handledComponentClass) {
        return new ArrayLivedocType(handledComponentClass);
    }

    @Override
    public LivedocType handleParameterizedType(ParameterizedType type, LivedocType handledRawType,
            List<LivedocType> handledTypeParameters) {
        return new ParameterizedLivedocType(handledRawType, handledTypeParameters);
    }

    @Override
    public LivedocType handleTypeVariable(TypeVariable type, List<LivedocType> handledBounds) {
        return new LivedocTypeVariable(type.getName(), handledBounds);
    }

    @Override
    public LivedocType handleWildcardType(WildcardType type, List<LivedocType> handledUpperBounds,
            List<LivedocType> handledLowerBounds) {
        if (!handledUpperBounds.isEmpty()) {
            return new WildcardLivedocType(BoundType.UPPER, handledUpperBounds);
        }
        if (!handledLowerBounds.isEmpty()) {
            return new WildcardLivedocType(BoundType.LOWER, handledLowerBounds);
        }
        return new WildcardLivedocType(BoundType.NONE, Collections.emptyList());
    }
}
