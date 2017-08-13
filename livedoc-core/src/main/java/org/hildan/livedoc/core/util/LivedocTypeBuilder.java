package org.hildan.livedoc.core.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Map;

import org.hildan.livedoc.core.annotation.ApiObject;

public class LivedocTypeBuilder {

    private static final String WILDCARD = "wildcard";

    private static final String UNDEFINED = "undefined";

    private static final String ARRAY = "array";

    public static LivedocType build(LivedocType livedocType, Class<?> clazz, Type type) {
        if (clazz.isAssignableFrom(LivedocDefaultType.class)) {
            livedocType.addItemToType(UNDEFINED);
            return livedocType;
        }

        if (Map.class.isAssignableFrom(clazz)) {
            livedocType.addItemToType(getCustomClassName(clazz));

            if (type instanceof ParameterizedType) {
                Type mapKeyType = ((ParameterizedType) type).getActualTypeArguments()[0];
                Type mapValueType = ((ParameterizedType) type).getActualTypeArguments()[1];

                livedocType.setMapKey(new LivedocType());
                livedocType.setMapValue(new LivedocType());

                if (mapKeyType instanceof Class) {
                    livedocType.setMapKey(new LivedocType(((Class<?>) mapKeyType).getSimpleName()));
                } else if (mapKeyType instanceof WildcardType) {
                    livedocType.setMapKey(new LivedocType(WILDCARD));
                } else if (mapKeyType instanceof TypeVariable<?>) {
                    livedocType.setMapKey(new LivedocType(((TypeVariable<?>) mapKeyType).getName()));
                } else {
                    livedocType.setMapKey(
                            build(livedocType.getMapKey(), (Class<?>) ((ParameterizedType) mapKeyType).getRawType(),
                                    mapKeyType));
                }

                if (mapValueType instanceof Class) {
                    livedocType.setMapValue(new LivedocType(((Class<?>) mapValueType).getSimpleName()));
                } else if (mapValueType instanceof WildcardType) {
                    livedocType.setMapValue(new LivedocType(WILDCARD));
                } else if (mapValueType instanceof TypeVariable<?>) {
                    livedocType.setMapValue(new LivedocType(((TypeVariable<?>) mapValueType).getName()));
                } else {
                    livedocType.setMapValue(
                            build(livedocType.getMapValue(), (Class<?>) ((ParameterizedType) mapValueType).getRawType(),
                                    mapValueType));
                }
            }
        } else if (Collection.class.isAssignableFrom(clazz)) {
            if (type instanceof ParameterizedType) {
                Type parametrizedType = ((ParameterizedType) type).getActualTypeArguments()[0];
                livedocType.addItemToType(getCustomClassName(clazz));

                if (parametrizedType instanceof Class) {
                    livedocType.addItemToType(getCustomClassName((Class<?>) parametrizedType));
                } else if (parametrizedType instanceof WildcardType) {
                    livedocType.addItemToType(WILDCARD);
                } else if (parametrizedType instanceof TypeVariable<?>) {
                    livedocType.addItemToType(((TypeVariable<?>) parametrizedType).getName());
                } else {
                    return build(livedocType, (Class<?>) ((ParameterizedType) parametrizedType).getRawType(),
                            parametrizedType);
                }
            } else if (type instanceof GenericArrayType) {
                return build(livedocType, clazz, ((GenericArrayType) type).getGenericComponentType());
            } else {
                livedocType.addItemToType(getCustomClassName(clazz));
            }
        } else if (clazz.isArray()) {
            livedocType.addItemToType(ARRAY);
            Class<?> componentType = clazz.getComponentType();
            return build(livedocType, componentType, type);
        } else {
            livedocType.addItemToType(getCustomClassName(clazz));
            if (type instanceof ParameterizedType) {
                Type parametrizedType = ((ParameterizedType) type).getActualTypeArguments()[0];

                if (parametrizedType instanceof Class) {
                    livedocType.addItemToType(getCustomClassName((Class<?>) parametrizedType));
                } else if (parametrizedType instanceof WildcardType) {
                    livedocType.addItemToType(WILDCARD);
                } else if (parametrizedType instanceof TypeVariable<?>) {
                    livedocType.addItemToType(((TypeVariable<?>) parametrizedType).getName());
                } else {
                    return build(livedocType, (Class<?>) ((ParameterizedType) parametrizedType).getRawType(),
                            parametrizedType);
                }
            }
        }

        return livedocType;
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
}
