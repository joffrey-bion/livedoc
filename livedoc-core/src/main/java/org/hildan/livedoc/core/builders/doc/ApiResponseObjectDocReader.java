package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;

import org.hildan.livedoc.core.annotations.ApiResponseObject;
import org.hildan.livedoc.core.builders.types.LivedocDefaultType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiResponseObjectDoc;

public class ApiResponseObjectDocReader {

    public static ApiResponseObjectDoc build(Method method) {
        if (method.isAnnotationPresent(ApiResponseObject.class)) {
            ApiResponseObject annotation = method.getAnnotation(ApiResponseObject.class);

            if (annotation.clazz().isAssignableFrom(LivedocDefaultType.class)) {
                return new ApiResponseObjectDoc(LivedocTypeBuilder.build(method.getReturnType(),
                        method.getGenericReturnType()));
            } else {
                return new ApiResponseObjectDoc(
                        LivedocTypeBuilder.build(annotation.clazz(), annotation.clazz()));
            }
        }

        return null;
    }

}
