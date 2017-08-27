package org.hildan.livedoc.core.scanner.readers;

import java.lang.reflect.AnnotatedElement;

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
    public static ApiVersionDoc read(AnnotatedElement element, Class<?> declaringClass) {
        ApiVersion elementAnnotation = element.getAnnotation(ApiVersion.class);
        if (elementAnnotation != null) {
            return buildFromAnnotation(element.getAnnotation(ApiVersion.class));
        }

        ApiVersion typeAnnotation = declaringClass.getAnnotation(ApiVersion.class);
        if (typeAnnotation != null) {
            return buildFromAnnotation(typeAnnotation);
        }

        return null;
    }

    private static ApiVersionDoc buildFromAnnotation(ApiVersion annotation) {
        ApiVersionDoc apiVersionDoc = new ApiVersionDoc();
        apiVersionDoc.setSince(annotation.since());
        apiVersionDoc.setUntil(annotation.until());
        return apiVersionDoc;
    }

}
