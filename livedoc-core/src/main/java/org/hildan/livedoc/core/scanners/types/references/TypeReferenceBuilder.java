package org.hildan.livedoc.core.scanners.types.references;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.hildan.generics.GenericTypeHandler;
import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.model.types.ArrayTypeReference;
import org.hildan.livedoc.core.model.types.BoundType;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.model.types.ParameterizedTypeReference;
import org.hildan.livedoc.core.model.types.SimpleClassReference;
import org.hildan.livedoc.core.model.types.TypeVariableReference;
import org.hildan.livedoc.core.model.types.VoidTypeReference;
import org.hildan.livedoc.core.model.types.WildcardTypeReference;
import org.hildan.livedoc.core.readers.LivedocIdGenerator;
import org.jetbrains.annotations.NotNull;

public class TypeReferenceBuilder implements GenericTypeHandler<LivedocType> {

    private final Predicate<? super Class<?>> typeFilter;

    public TypeReferenceBuilder(Predicate<? super Class<?>> typeFilter) {
        this.typeFilter = typeFilter;
    }

    @Override
    public LivedocType handleVoid() {
        return VoidTypeReference.INSTANCE;
    }

    @Override
    public LivedocType handleSimpleClass(@NotNull Class<?> clazz) {
        String livedocId = typeFilter.test(clazz) ? LivedocIdGenerator.getTypeId(clazz) : null;
        return new SimpleClassReference(getCustomClassName(clazz), clazz, livedocId);
    }

    private static String getCustomClassName(Class<?> clazz) {
        ApiType apiType = clazz.getAnnotation(ApiType.class);
        if (apiType != null && !apiType.name().isEmpty()) {
            return apiType.name();
        }
        return clazz.getSimpleName();
    }

    @Override
    public LivedocType handleEnumClass(@NotNull Class<?> clazz) {
        return handleSimpleClass(clazz);
    }

    @Override
    public LivedocType handleArrayClass(@NotNull Class<?> arrayClass, LivedocType handledComponentClass) {
        return handleArray(handledComponentClass);
    }

    @Override
    public LivedocType handleGenericArray(@NotNull GenericArrayType type, LivedocType handledComponentClass) {
        return handleArray(handledComponentClass);
    }

    private static LivedocType handleArray(LivedocType handledComponentClass) {
        return new ArrayTypeReference(handledComponentClass);
    }

    @Override
    public LivedocType handleParameterizedType(@NotNull ParameterizedType type, LivedocType handledRawType,
            @NotNull List<LivedocType> handledTypeParameters) {
        return new ParameterizedTypeReference(handledRawType, handledTypeParameters);
    }

    @Override
    public LivedocType handleTypeVariable(@NotNull TypeVariable type, @NotNull List<LivedocType> handledBounds) {
        return new TypeVariableReference(type.getName(), handledBounds);
    }

    @Override
    public LivedocType handleWildcardType(@NotNull WildcardType type, @NotNull List<LivedocType> handledUpperBounds,
            @NotNull List<LivedocType> handledLowerBounds) {
        if (!handledUpperBounds.isEmpty()) {
            return new WildcardTypeReference(BoundType.UPPER, handledUpperBounds);
        }
        if (!handledLowerBounds.isEmpty()) {
            return new WildcardTypeReference(BoundType.LOWER, handledLowerBounds);
        }
        return new WildcardTypeReference(BoundType.NONE, Collections.emptyList());
    }
}
