package org.hildan.livedoc.core.scanner.readers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.hildan.livedoc.core.annotation.ApiVersion;
import org.hildan.livedoc.core.pojo.ApiVersionDoc;

public class ApiVersionDocReader {

    public static ApiVersionDoc read(Class<?> clazz) {
        ApiVersionDoc apiVersionDoc = null;
        if (clazz.isAnnotationPresent(ApiVersion.class)) {
            apiVersionDoc = buildFromAnnotation(clazz.getAnnotation(ApiVersion.class));
        }
        return apiVersionDoc;
    }

    /**
     * In case this annotation is present at type and method level, then the method annotation will override the type
     * one.
     */
    public static ApiVersionDoc read(Method method) {
        ApiVersionDoc apiVersionDoc = null;
        ApiVersion methodAnnotation = method.getAnnotation(ApiVersion.class);
        ApiVersion typeAnnotation = method.getDeclaringClass().getAnnotation(ApiVersion.class);

        if (typeAnnotation != null) {
            apiVersionDoc = buildFromAnnotation(typeAnnotation);
        }

        if (methodAnnotation != null) {
            apiVersionDoc = buildFromAnnotation(method.getAnnotation(ApiVersion.class));
        }

        return apiVersionDoc;
    }

    public static ApiVersionDoc read(Field field) {
        ApiVersionDoc apiVersionDoc = null;
        if (field.isAnnotationPresent(ApiVersion.class)) {
            apiVersionDoc = buildFromAnnotation(field.getAnnotation(ApiVersion.class));
        }
        return apiVersionDoc;
    }

    private static ApiVersionDoc buildFromAnnotation(ApiVersion annotation) {
        ApiVersionDoc apiVersionDoc = new ApiVersionDoc();
        apiVersionDoc.setSince(annotation.since());
        apiVersionDoc.setUntil(annotation.until());
        return apiVersionDoc;
    }

}
